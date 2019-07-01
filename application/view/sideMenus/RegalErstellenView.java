package RegalisatorEnterprise.application.view.sideMenus;

import RegalisatorEnterprise.application.logik.Brett;
import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.logik.Regal;
import RegalisatorEnterprise.application.view.MenuView;
import RegalisatorEnterprise.application.view.datenViews.BrettView;
import RegalisatorEnterprise.application.view.datenViews.RegalView;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Bietet die Möglichkeit Regale zu konfigurieren
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class RegalErstellenView extends SideMenu {

    //BrettView einbinden
    protected BrettView aktBrettView;
    //neuesRegalAnsicht (oben)
    private VBox neuesRegalAnsicht;
    private Label neuesRegal;
    private Label hoehe;
    private Label breite;
    private Label name;
    private Slider hoeheSlider;
    private Slider breiteSlider;
    private TextField nameTextField;
    private VBox regalSliderContainer;
    private VBox brettPreview;
    private Label brettPreviewUeberschrift;

    //neuesRegalbrettAnsicht (unten)
    private VBox neuesRegalbrettAnsicht;
    private Label neuesRegalbrett;
    private Label belastbarkeit;
    private Label hoeheBrett;
    private Slider hoeheBrettSlider;
    private Slider tragkraftSlider;

    public RegalErstellenView(MenuView menuView, Lager lager) {
        super(menuView, lager);
        //Im Konstruktor der Überklasse wird direkt die Methode buildSettings aufgerufen


    }

    @Override
    protected void buildPreview() {
        aktDatenView = new RegalView(new Regal(130, 200, lager));
        preview.getChildren().add(aktDatenView);
    }

    @Override
    public void buildSettings() {

        buildNeuesRegalAnsicht();
        buildNeuesRegalbrettAnsicht();
        buildNeuesRegalbrettAnsicht();


        settings.getChildren().addAll(neuesRegalAnsicht, brettPreviewUeberschrift, brettPreview, neuesRegalbrettAnsicht);
        settings.setAlignment(Pos.CENTER);
    }

    /**
     * Baut die Regalerstellen Elemente auf
     */
    public void buildNeuesRegalAnsicht() {

        neuesRegalAnsicht = new VBox();

        neuesRegal = new Label("Neues Regal");
        neuesRegal.setId("h2");
        hoehe = new Label("Höhe: ");
        breite = new Label("Breite: ");

        hoeheSlider = new Slider(10, 700, 300);
        breiteSlider = new Slider(10, 500, 300);

        /* -- Listener -- */
        hoeheSlider.valueProperty().bindBidirectional(((RegalView) aktDatenView).getRegal().hoeheProperty());
        breiteSlider.valueProperty().bindBidirectional(((RegalView) aktDatenView).getRegal().breiteProperty());

        neuesRegalAnsicht.getChildren().addAll(neuesRegal, hoehe, hoeheSlider, breite, breiteSlider);

        //styling
        neuesRegalAnsicht.setAlignment(Pos.CENTER);
    }

    /**
     * Baut die "Neues Brett" Elemente auf
     */
    public void buildNeuesRegalbrettAnsicht() {
        //Brett Vorschau
        brettPreview = new VBox();
        brettPreview.setAlignment(Pos.BOTTOM_CENTER);
        brettPreviewUeberschrift = new Label("Neues Brett");
        brettPreviewUeberschrift.setId("h2");
        aktBrettView = new BrettView(new Brett(100, 200));
        brettPreview.setMinHeight(200);
        brettPreview.setMaxWidth(150);

        //styling
        brettPreview.setMinSize(300, 140);
        brettPreview.setId("preview");
        aktBrettView.setId("aktBrettView");


        brettPreview.getChildren().addAll(aktBrettView);


        //Eingaben
        neuesRegalbrettAnsicht = new VBox();
        belastbarkeit = new Label("Belastbarkeit: ");
        hoeheBrett = new Label("Höhe: ");
        hoeheBrettSlider = new Slider(0, 300, 50);
        tragkraftSlider = new Slider(0, 100, 50);

        /* -- Listener -- */
        hoeheBrettSlider.valueProperty().bindBidirectional(((BrettView) aktBrettView).getBrett().hoeheProperty());
        tragkraftSlider.valueProperty().bindBidirectional(((BrettView) aktBrettView).getBrett().tragkraftProperty());


        /* -- Zusammenfügen -- */
        neuesRegalbrettAnsicht.getChildren().addAll(hoeheBrett, hoeheBrettSlider, belastbarkeit, tragkraftSlider);

        //styling
        neuesRegalbrettAnsicht.setAlignment(Pos.CENTER);
    }


}
