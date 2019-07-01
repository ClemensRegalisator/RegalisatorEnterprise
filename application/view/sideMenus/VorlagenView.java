package RegalisatorEnterprise.application.view.sideMenus;

import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.logik.Paket;
import RegalisatorEnterprise.application.view.MenuView;
import RegalisatorEnterprise.application.view.datenViews.PaketView;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Die Vorlagenview speichert bei jeder Auswahl aus dem Menü, die aktuelle auswahl in den Zwischenspeicher des Lagers
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class VorlagenView extends SideMenu {

    private ListView vorlagenAnzeige;
    private ObservableList<Paket> vorlagen;
    private ObservableList<HBoxCell> vorlagenEintraege;


    private Button edit;
    private Circle kreis;
    private ArrayList<Paket> vorlagenListe;

    //info
    private VBox infoContainer;
    private HBox infoContainer2;
    private HBox nameBox;
    private HBox weightBox;
    private HBox heightBox;
    private HBox widthBox;
    private Label nameInfo;
    private Label weightInfo;
    private Label heightInfo;
    private Label widthInfo;


    public VorlagenView(MenuView menuView, Lager lager) {
        super(menuView, lager);

    }

    /**
     * Erstellt die Detailansicht für die aktuell ausgewählte Vorlage
     */
    @Override
    protected void buildPreview() {
        aktDatenView = new Label("Vorschau");
        buildInfo();

        preview.getChildren().add(aktDatenView);
        settings.getChildren().add(infoContainer);

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
     * erstellt die View für das Vorlagenmenü indem der User Vorlagen auswählen kann um diese zu draggen, oder zu löschen
     */
    @Override
    void buildSettings() {

        vorlagenAnzeige = new ListView();
        vorlagen = menuView.getLager().getVorlagen(); //Beim erstem Einruf einlesen
        vorlagenEintraege = FXCollections.observableArrayList();
        //vorlagen.addAll(test, test1);


        vorlagenAnzeige.setPrefWidth(100);
        vorlagenAnzeige.setPrefHeight(300);
        zeigeAktVorlagen();

        vorlagenAnzeige.setItems(vorlagenEintraege);
        settings.getChildren().addAll(vorlagenAnzeige);

        //Bei änderung direkt anzeigen
        menuView.getLager().getVorlagen().addListener((ListChangeListener) c -> zeigeAktVorlagen());

        //AktAuswahl in Preview anzeigen
        vorlagenAnzeige.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBoxCell>() {
            @Override
            public void changed(ObservableValue<? extends HBoxCell> observable, HBoxCell oldValue, HBoxCell newValue) {
                //Falls eine Zelle ausgewählt ist
                if (newValue != null) {
                    aktDatenView = new PaketView(newValue.getPaket());
                    preview.getChildren().clear();
                    //((PaketView)aktDatenView).erzeugePaketMuster();
                    preview.getChildren().add(aktDatenView);

                    //Lade bindings neu
                    nameInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().nameProperty());
                    widthInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().breiteProperty().divide(Lager.GRID_BREITE).asString());
                    heightInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().hoeheProperty().divide(Lager.GRID_HOEHE).asString());
                    weightInfo.textProperty().bind(((PaketView) aktDatenView).getPaket().gewichtProperty().asString());
                    writeAllVorlagenToZwischenspeicher(((PaketView) aktDatenView).getPaket());


                } else {
                    aktDatenView = new Label("Vorschau");

                    preview.getChildren().clear();
                    preview.getChildren().add(aktDatenView);
                    nameInfo.textProperty().unbind();
                    widthInfo.textProperty().unbind();
                    heightInfo.textProperty().unbind();
                    weightInfo.textProperty().unbind();

                    nameInfo.setText("");
                    widthInfo.setText("");
                    heightInfo.setText("");
                    weightInfo.setText("");
                }

            }
        });


    }

    private void buildInfo() {
        infoContainer = new VBox();
        infoContainer2 = new HBox();
        heightBox = new HBox();
        widthBox = new HBox();
        weightBox = new HBox();
        nameBox = new HBox();

        weightInfo = new Label("");
        heightInfo = new Label("");
        widthInfo = new Label("");
        nameInfo = new Label("");


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
        infoContainer.getChildren().addAll(nameInfo, infoContainer2);


    }

    /**
     * Zeigt die Liste der aktuell gespeicherten Templates
     */
    private void zeigeAktVorlagen() {
        //Liste leeren
        vorlagenEintraege.removeAll(vorlagenEintraege);

        //Liste neu aufbauen
        for (int i = 0; i < vorlagen.size(); i++) {

            Paket aktPaket = vorlagen.get(i);
            HBoxCell aktEintrag = new HBoxCell(aktPaket);
            vorlagenEintraege.add(aktEintrag);

        }


    }

    /**
     * Schreibt die aktuelle Vorlage in den Zwischenspeicher
     *
     * @param p Paket der ausgewählten Vorlage
     */
    private void writeAllVorlagenToZwischenspeicher(Paket p) {
        lager.addToZwischenspeicher(p);

    }

    /**
     * zeigt den Papierkorb zum löschen eines Paketes
     */
    protected void showPapierkorb() {
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


    protected void showPreview() {
        preview.getChildren().clear();
        preview.getChildren().addAll(aktDatenView);

    }

    /**
     * Hilfsklasse die das Anzeigen mehrerer Elemente pro Spalte ermöglicht
     */

    class HBoxCell extends HBox {
        Circle kreis = new Circle();
        Label label = new Label();
        Button delete = new Button();
        private Paket paket;


        HBoxCell(Paket paket) {
            super();
            this.paket = paket;
            label.setText(paket.getName());
            label.setId("textSchwarz");
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);
            kreis.setFill(paket.getFarbe());
            kreis.setRadius(8.0);
            delete.setText("X");

            delete.setOnAction(e -> menuView.getLager().deleteVorlage(paket));


            this.getChildren().addAll(kreis, label, delete);
        }

        public Paket getPaket() {
            return this.paket;
        }


    }


}
