package RegalisatorEnterprise.application.view.sideMenus;


import RegalisatorEnterprise.application.Main;
import RegalisatorEnterprise.application.logik.Farben;
import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.logik.Paket;
import RegalisatorEnterprise.application.view.MenuView;
import RegalisatorEnterprise.application.view.datenViews.PaketView;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Diese MenuView bietet die Möglichkeit ein Paket zu konfigurieren
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class PaketErstellenView extends SideMenu {

    private BorderPane widgetContainer;

    //Preview und Info
    private VBox infoContainer;
    private HBox infoContainer2;
    private HBox nameBox;
    private HBox weightBox;
    private HBox heightBox;
    private HBox widthBox;
    private HBox idBox;
    private Label nameInfo;
    private Label weightInfo;
    private Label heightInfo;
    private Label widthInfo;
    private Label idInfo;
    private TextField nameTextField;
    private VBox previewContainer;

    //Widget
    //Obere Buttonleiste
    private HBox buttonBarReiter;
    private Button unvertraeglichkeiten;
    private Button oberflaechen;
    //Zentrales Inhaltselement
    private VBox oberflMitte;
    //Oberflächenmodus
    private BorderPane oberflContainer;
    //Linke Buttonleiste im OberflModus
    private VBox oberflReiter;
    private Button farbenButton;
    private Button muster;
    private Button pic;
    //Oberflmodus FARBE
    private FlowPane farben;
    private HashMap<Color, Button> alleFarben;
    //Oberflmodus Muster
    private VBox musterContainer;
    private FlowPane alleMuster;
    private ArrayList<Circle> cicles;

    //Oberflmodus Pic
    private VBox picContainer;
    private FlowPane allePics;
    private ArrayList<Circle> cicles2;

    //unverträglichkeitsmodus
    private VBox unvertrContainer;
    private FlowPane unvertraeglichkeitenColors;
    private HashMap<Color, Button> alleUnvertr;


    //Slider
    private VBox allSliders;
    private HBox weightSliderBox;
    private HBox heightSliderBox;
    private HBox widthSliderBox;

    private Label weight;
    private Label height;
    private Label width;

    private Slider weightSlider;
    private Slider heightSlider;
    private Slider widthSlider;


    //Unten Buttons
    private VBox buttons;
    private Button saveAsTemplate;


    public PaketErstellenView(MenuView menuView, Lager lager) {
        super(menuView, lager);
        //Im Konstruktor der Überklasse wird direkt die Methode buildSettings aufgerufen
    }

    @Override
    protected void buildPreview() {
        //Vorschaupaket
        Paket previewPaket = new Paket(3 * Lager.GRID_BREITE, 3 * Lager.GRID_HOEHE, 50, Farben.BRAUN, "Paket");
        previewPaket.setId(0);


        previewContainer = new VBox();
        aktDatenView = new PaketView(previewPaket);
        lager.setPreviewPaket(previewPaket);
        buildInfo(); //Baut den infoContainer auf

        previewContainer.getChildren().addAll(aktDatenView);
        preview.getChildren().addAll(previewContainer, infoContainer);

        //styling
        previewContainer.setMinHeight(240);
        previewContainer.setAlignment(Pos.CENTER);


        //Papierkorb anzeigen
        Lager.paketIsDragged.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showPapierkorb();
                } else {
                    showPreview();
                }
            }
        });


    }

    /**
     * Baut den Infobereich auf
     */
    private void buildInfo() {
        infoContainer = new VBox();
        infoContainer2 = new HBox();
        heightBox = new HBox();
        widthBox = new HBox();
        weightBox = new HBox();
        nameBox = new HBox();

        weightInfo = new Label("12");
        heightInfo = new Label("4");
        widthInfo = new Label("200");
        nameInfo = new Label("Paketname der sehr lang ist");
        nameTextField = new TextField();


        nameBox.getChildren().add(nameInfo);
        widthBox.getChildren().add(widthInfo);
        heightBox.getChildren().add(heightInfo);
        weightBox.getChildren().add(weightInfo);
        infoContainer2.getChildren().addAll(widthBox, heightBox, weightBox);


        weightBox.setId("weightInfo");
        weightBox.getStyleClass().add("sidemenu_infoIcon");
        weightBox.setPadding(new Insets(5, 10, 0, 30));
        weightInfo.setId("orangeText");

        heightBox.setId("heightInfo");
        heightBox.getStyleClass().add("sidemenu_infoIcon");
        heightBox.setPadding(new Insets(5, 10, 0, 30));
        heightInfo.setId("orangeText");


        widthBox.setId("widthInfo");
        widthBox.getStyleClass().add("sidemenu_infoIcon");
        widthBox.setPadding(new Insets(5, 10, 0, 30));
        widthInfo.setId("orangeText");


        nameBox.setId("nameInfo");
        nameBox.getStyleClass().add("sidemenu_infoIcon");
        nameBox.setPadding(new Insets(5, 10, 0, 30));
        nameInfo.setId("orangeTextBOLD");


        infoContainer.setId("preview_infoContainer");
        infoContainer.setAlignment(Pos.CENTER);
        infoContainer2.setAlignment(Pos.CENTER);
        infoContainer.getChildren().addAll(nameTextField, infoContainer2);

        nameTextField.textProperty().bindBidirectional(((PaketView) aktDatenView).getPaket().nameProperty());
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        nameTextField.setId("preview_nameTextfield");

        //Bindings
        nameInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().nameProperty());
        widthInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().breiteProperty().divide(Lager.GRID_BREITE).asString());
        heightInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().hoeheProperty().divide(Lager.GRID_HOEHE).asString());
        weightInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().gewichtProperty().asString());
    }

    @Override
    public void buildSettings() {

        buildWidgetContainer();
        buildAllSliders();
        buildButtons();


        //styling
        buttonBar.setAlignment(Pos.CENTER);
        widgetContainer.setPadding(new Insets(24, 10, 15, 10));
        allSliders.setAlignment(Pos.CENTER);
        allSliders.setSpacing(10);
        allSliders.setPadding(new Insets(15, 0, 0, 10));


        //zusammensetzen
        settings.getChildren().addAll(allSliders, widgetContainer, buttons);


    }

    /**
     * Baut die unteren Slider auf
     */
    public void buildAllSliders() {
        allSliders = new VBox();
        weightSliderBox = new HBox();
        heightSliderBox = new HBox();
        widthSliderBox = new HBox();

        weight = new Label("Gewicht: ");
        height = new Label("Höhe: ");
        width = new Label("Breite: ");

        weightSlider = new Slider(Lager.PAKET_MINGEWICHT, Lager.PAKET_MAXGEWICHT, ((PaketView) aktDatenView).getPaket().getGewicht());
        heightSlider = new Slider(Lager.PAKET_MINHOEHE, Lager.PAKET_MAXHOEHE, (((PaketView) aktDatenView).getPaket().getHoehe()) / Lager.getGridHoehe());
        widthSlider = new Slider(Lager.PAKET_MINBREITE, Lager.PAKET_MAXBREITE, (((PaketView) aktDatenView).getPaket().getBreite()) / Lager.getGridBreite());


        widthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int umgerechnet = newValue.intValue() * Lager.GRID_BREITE;
            ((PaketView) aktDatenView).getPaket().breiteProperty().setValue(umgerechnet);

        });

        heightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int umgerechnet = newValue.intValue() * Lager.GRID_HOEHE;
            ((PaketView) aktDatenView).getPaket().hoeheProperty().setValue(umgerechnet);


        });

        weightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            ((PaketView) aktDatenView).getPaket().gewichtProperty().setValue(newValue.intValue());

        });

        //Erstellt immer ein neues Paket, wenn das letzte erfolgreich per Drag & Drop platziert wurde
        // BrettView.dropErfolgreichEingefuegt.addListener((observable, oldValue, newValue) -> createNewPackage());

        //styling
        widthSlider.setMinWidth(230);
        heightSlider.setMinWidth(230);
        weightSlider.setMinWidth(230);
        widthSliderBox.setAlignment(Pos.CENTER_RIGHT);
        heightSliderBox.setAlignment(Pos.CENTER_RIGHT);
        weightSliderBox.setAlignment(Pos.CENTER_RIGHT);


        widthSliderBox.getChildren().addAll(width, widthSlider);
        heightSliderBox.getChildren().addAll(height, heightSlider);
        weightSliderBox.getChildren().addAll(weight, weightSlider);


        allSliders.getChildren().addAll(widthSliderBox, heightSliderBox, weightSliderBox);

    }

    /**
     * Baut den Template sichern Button auf
     */
    private void buildButtons() {
        buttons = new VBox();
        saveAsTemplate = new Button("Als Template sichern");


        saveAsTemplate.setOnAction((ActionEvent e) -> {
            menuView.getLager().addVorlage(((PaketView) aktDatenView).getPaket());
        });

        buttons.getChildren().addAll(saveAsTemplate);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(0, 0, 15, 0));

    }


    /**
     * Baut das Farb/Muster/Bild und Unverträglichkeitswidget auf
     */
    private void buildWidgetContainer() {
        //Erstellen
        oberflMitte = new VBox();
        widgetContainer = new BorderPane();
        oberflContainer = new BorderPane();

        buildObereButtonleiste();
        buildLinkeButtonleiste();
        buildColors();
        buildUnvertr();
        buildMuster();
        buildTextures();


        //Zusammensetzten
        widgetContainer.setTop(buttonBarReiter);
        widgetContainer.setLeft(oberflReiter);
        widgetContainer.setCenter(oberflMitte);

        //Styling
        oberflMitte.setAlignment(Pos.CENTER);

    }

    /**
     * Baut die Linke Buttonleiste des Widgets auf
     */
    private void buildLinkeButtonleiste() {
        //Elemente Erstellen
        oberflReiter = new VBox();
        farbenButton = new Button("Farben");
        muster = new Button("Muster");
        pic = new Button("Texturen");

        //Zusammenfügen
        oberflReiter.getChildren().addAll(farbenButton, muster, pic);

        //Styling
        farbenButton.setMinSize(74, 54);
        muster.setMinSize(74, 54);
        pic.setMinSize(74, 54);

        //Funktionen
        farbenButton.setOnAction(e -> showColors());
        muster.setOnAction(e -> showMuster());
        pic.setOnAction(e -> showPic());

    }

    /**
     * Baut die obere Leiste des Widgets auf
     */

    private void buildObereButtonleiste() {
        //Erstellen
        buttonBarReiter = new HBox();
        oberflaechen = new Button("Oberflächen");
        unvertraeglichkeiten = new Button("Unverträglichk.");

        //Zusammenfügen
        buttonBarReiter.getChildren().addAll(oberflaechen, unvertraeglichkeiten);

        //Funktionen
        oberflaechen.setOnAction(e -> showColors());
        unvertraeglichkeiten.setOnAction(e -> showUnver());

        //Styling
        oberflaechen.setMinSize(140, 30);
        unvertraeglichkeiten.setMinSize(140, 30);
        oberflaechen.setId("farben_button");
        unvertraeglichkeiten.setId("unv_button");
    }

    /**
     * Baut die Farbauswahl aus
     */
    private void buildColors() {
        alleFarben = new HashMap<>();
        farben = new FlowPane();
        List<Color> allColors = new ArrayList<>(Farben.getList());


        for (int i = 0; i < allColors.size(); i++) {
            Button button = new Button();
            Color aktFarbe = allColors.get(i);

            button.setStyle("-fx-background-color: " + Farben.hex(aktFarbe));
            button.setId("colorChoser_buttons");
            button.setOnAction(e -> {
                ((PaketView) aktDatenView).getPaket().setFarbe(aktFarbe);
                umrandeAktColorButton();

            });

            alleFarben.put(allColors.get(i), button);

            farben.getChildren().add(button);

        }
        oberflMitte.getChildren().add(farben);

        //styling
        farben.setVgap(10);
        farben.setHgap(10);
        farben.setAlignment(Pos.CENTER);
        alleFarben.get(Farben.BRAUN).setId("colorChoser_ausgewaehlt");
        farben.setId("colorchoser");


    }

    /**
     * Baut die Unverträglichkeitsauswahl auf
     */
    private void buildUnvertr() {
        unvertrContainer = new VBox();
        unvertraeglichkeitenColors = new FlowPane();
        alleUnvertr = (HashMap) alleFarben.clone();


        List<Color> allColors = new ArrayList<>(Farben.getList());

        for (int i = 0; i < alleUnvertr.size(); i++) {
            Button button = new Button();
            Color aktFarbe = allColors.get(i);
            button.setStyle("-fx-background-color: " + Farben.hex(aktFarbe));
            button.setId("colorChoser_buttons");

            button.setOnAction(e -> {

                if (((PaketView) aktDatenView).getPaket().isUnvertraeglichkeit(aktFarbe)) {
                    ((PaketView) aktDatenView).getPaket().deleteUnvertraeglichkeit(aktFarbe);

                } else {
                    ((PaketView) aktDatenView).getPaket().addUnvertraeglichkeit(aktFarbe);
                }

            });
            alleUnvertr.put(aktFarbe, button);
            unvertraeglichkeitenColors.getChildren().addAll(button);

        }
        unvertrContainer.getChildren().add(unvertraeglichkeitenColors);

        ((PaketView) aktDatenView).getPaket().getUnverträglichkeiten().addListener((ListChangeListener) c -> umrandeAlleUnvertr());

        unvertrContainer.setId("colorchoser");
        unvertrContainer.setAlignment(Pos.CENTER);
        unvertraeglichkeitenColors.setHgap(10);
        unvertraeglichkeitenColors.setVgap(10);
        unvertraeglichkeitenColors.setAlignment(Pos.CENTER);


    }

    /**
     * Baut die Musterauswahl auf
     */
    private void buildMuster() {
        //Erstellen
        musterContainer = new VBox();
        alleMuster = new FlowPane();
        cicles = new ArrayList<>();


        //Read Musterfiles
        int i = 1;
        while (true) {
            try {
                Image muster = new Image(Main.class.getResourceAsStream("/RegalisatorEnterprise/application/persistenz/pictures/muster/" + i + ".png"));

                Circle circle = new Circle();

                circle.setRadius(19);
                circle.setId("circle_unchosen");

                circle.setFill(new ImagePattern(muster));


                int a = i;
                //Funktion
                circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (((PaketView) aktDatenView).getPaket().getMuster().contains(a)) {
                            circle.setId("circle_unchosen");
                            ((PaketView) aktDatenView).getPaket().getMuster().remove((Object) a);

                        } else {
                            ((PaketView) aktDatenView).getPaket().addMuster(a);
                            circle.setId("circle_chosen");
                            for (Circle c : cicles2) {
                                c.setId("circle_unchosen");
                            }
                            ((PaketView) aktDatenView).getPaket().setTextur(0);
                        }

                    }
                });

                cicles.add(circle);
                alleMuster.getChildren().add(circle);
            } catch (NullPointerException e) {
                break;
            }
            i++;
        }
        musterContainer.getChildren().add(alleMuster);
        alleMuster.setHgap(10);
        alleMuster.setVgap(10);
        alleMuster.setAlignment(Pos.TOP_LEFT);
        musterContainer.setAlignment(Pos.TOP_LEFT);
        alleMuster.setPadding(new Insets(10, 10, 10, 10));

        musterContainer.setId("colorchoser");


    }

    /**
     * Baut die Texturenauswahl auf
     */
    private void buildTextures() {
        picContainer = new VBox();
        allePics = new FlowPane();
        cicles2 = new ArrayList<>();


        //Read Textures
        int i = 1;
        while (true) {
            try {
                Image texture;
                try {
                    texture = new Image(Main.class.getResourceAsStream("/RegalisatorEnterprise/application/persistenz/pictures/texturen/" + i + ".png"));
                } catch (NullPointerException e) {
                    texture = new Image(Main.class.getResourceAsStream("/RegalisatorEnterprise/application/persistenz/pictures/texturen/" + i + ".gif"));
                }
                Circle circle2 = new Circle();

                circle2.setRadius(19);
                circle2.setId("circle_unchosen");

                circle2.setFill(new ImagePattern(texture));


                int a = i;
                //Funktion
                circle2.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (((PaketView) aktDatenView).getPaket().getTextur().getValue() == a) {
                            circle2.setId("circle_unchosen");
                            ((PaketView) aktDatenView).getPaket().setTextur(0);

                        } else {
                            for (Circle c : cicles2) {
                                c.setId("circle_unchosen");
                            }
                            ((PaketView) aktDatenView).getPaket().setTextur(a);
                            circle2.setId("circle_chosen");
                            //Wenn Farbe gewählt ist deaktiviere alle Muster
                            for (Circle c : cicles) {
                                c.setId("circle_unchosen");
                            }
                            ((PaketView) aktDatenView).getPaket().getMuster().clear();

                        }


                    }
                });

                cicles2.add(circle2);
                allePics.getChildren().add(circle2);

            } catch (NullPointerException e) {

                break;
            }
            i++;
        }


        picContainer.getChildren().add(allePics);
        allePics.setHgap(10);
        allePics.setVgap(10);
        allePics.setAlignment(Pos.TOP_LEFT);
        picContainer.setAlignment(Pos.TOP_LEFT);
        allePics.setPadding(new Insets(10, 10, 10, 10));
        picContainer.setId("colorchoser");


    }

    /**
     * Interne Funktion zum Umranden des ausgewählten Buttons
     */
    private void umrandeAktColorButton() {
        for (Button b : alleFarben.values()) {
            b.setId("colorChoser_buttons");
        }
        Color farbe = ((PaketView) aktDatenView).getPaket().getFarbe();
        alleFarben.get(farbe).setId("colorChoser_ausgewaehlt");
    }

    /**
     * Überprüft alle Unverträglichkeiten und rahmt die richtigen Buttons entsprechend ein
     */
    private void umrandeAlleUnvertr() {
        ObservableList<Color> ol = ((PaketView) aktDatenView).getPaket().getUnverträglichkeiten();
        for (Button b : alleUnvertr.values()) {
            b.setId("colorChoser_buttons");
        }

        for (Color c : ol) {
            alleUnvertr.get(c).setId("unvertrChoser_ausgewaehlt");
        }


    }

    /**
     * wechselt das Menü
     */
    private void showColors() {
        oberflMitte.getChildren().clear();
        oberflMitte.getChildren().add(farben);

        widgetContainer.setLeft(oberflReiter);
        widgetContainer.setCenter(oberflMitte);


    }

    /**
     * wechselt das Menü
     */
    private void showUnver() {
        oberflMitte.getChildren().clear();
        widgetContainer.setLeft(null);
        widgetContainer.setCenter(null);

        widgetContainer.setCenter(unvertrContainer);
    }


    /**
     * wechselt das Menü
     */
    private void showMuster() {
        oberflMitte.getChildren().clear();
        widgetContainer.setCenter(null);

        widgetContainer.setCenter(musterContainer);
    }

    /**
     * wechselt das Menü
     */
    private void showPic() {
        oberflMitte.getChildren().clear();
        widgetContainer.setCenter(null);

        widgetContainer.setCenter(picContainer);


    }

    /**
     * Zeigt den Papierkorb im Oberem Bereich
     */
    private void showPapierkorb() {
        papierkorb = new VBox();
        HBox oben = new HBox();
        HBox unten = new HBox();

        papierkorb.getChildren().addAll(oben, unten);
        oben.setId("papierkorb_oben");
        unten.setId("papierkorb_unten");

        preview.getChildren().clear();


        preview.getChildren().addAll(papierkorb);

        papierkorb.setOnDragOver(e -> e.acceptTransferModes(TransferMode.ANY));

        //Listener
        papierkorb.setOnDragDropped(event -> {

            lager.removePaket(Integer.parseInt(event.getDragboard().getString()));


        });


        preview.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //Animation
                RotateTransition t = new RotateTransition(Duration.millis(100), oben);
                t.setFromAngle(0);
                t.setToAngle(-20);

                ScaleTransition st = new ScaleTransition(Duration.millis(200), papierkorb);
                st.setFromX(1);
                st.setFromY(1);
                st.setToX(1.2);
                st.setToY(1.2);

                TranslateTransition tt = new TranslateTransition(Duration.millis(100), oben);
                tt.setFromY(0);
                tt.setToY(-10);


                KeyFrame k1 = new KeyFrame(Duration.millis(1), a -> {
                    t.play();
                    st.play();
                    tt.play();


                });


                //st2.setOnFinished(e-> st3.play());

                Timeline timeline = new Timeline();
                timeline.setAutoReverse(false);
                timeline.setCycleCount(1);
                timeline.getKeyFrames().addAll(k1);
                if (!event.getDragboard().getString().equals("0"))
                    timeline.play();

            }
        });


        preview.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //Animation
                RotateTransition t = new RotateTransition(Duration.millis(100), oben);
                t.setFromAngle(-20);
                t.setToAngle(0);
                ScaleTransition st = new ScaleTransition(Duration.millis(200), papierkorb);
                st.setFromX(1.2);
                st.setFromY(1.2);
                st.setToX(1);
                st.setToY(1);
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), oben);
                tt.setFromY(-10);
                tt.setToY(0);


                KeyFrame k1 = new KeyFrame(Duration.millis(1), a -> {
                    t.play();
                    st.play();
                    tt.play();
                });


                //st2.setOnFinished(e-> st3.play());

                Timeline timeline = new Timeline();
                timeline.setAutoReverse(false);
                timeline.setCycleCount(1);
                timeline.getKeyFrames().addAll(k1);

                if (!event.getDragboard().getString().equals("0")) {
                    timeline.play();

                }

            }
        });


        //styling
        papierkorb.setMinSize(110, 110);
        papierkorb.setMaxSize(110, 110);
        oben.setMinSize(110, 30);
        unten.setMinSize(110, 110);
        papierkorb.setAlignment(Pos.CENTER);


    }


    /**
     * Löscht den Previewbereich und stellt ihn wieder her
     */
    private void showPreview() {
        preview.getChildren().clear();
        preview.getChildren().addAll(previewContainer, infoContainer);
    }


}
