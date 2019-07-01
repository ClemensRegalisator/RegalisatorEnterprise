package RegalisatorEnterprise.application.view.sideMenus;

import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.view.MenuView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Koordiniert das Aussehen der Seitenleiste, bietet das obere Vorschaufenster und den Seitenswitcher
 * Alles dazwischen wird von den einzelnen menuViews erledigt. Jede Seitenleiste besteht aus dem PreviewBereich und dem Settings
 * Bereich. Jedes Seitmenü hat unten den Buttonswitcher. Jede Klasse hat Preview und Settings zu implementieren.
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public abstract class SideMenu extends BorderPane {
    protected MenuView menuView;
    protected VBox preview;
    protected VBox settings;
    protected HBox buttonBar;


    //Buttonbar
    protected Button paketErstellenReiter;
    protected Button vorlagenReiter;
    protected Button sucheReiter;
    protected Button regalReiter;
    protected Node aktDatenView;
    protected Lager lager;
    protected VBox papierkorb;
    //CloseButtonSeitenleiste
    private Button close;
    private boolean open;


    public SideMenu(MenuView menuView, Lager lager) {

        this.menuView = menuView;
        this.lager = lager;
        this.setId("sideMenu");
        settings = new VBox();


        buildSwitchButtons();
        open = true;

        preview = new VBox();

        buildPreview(); //Wenn nichts ausgewählt ist, was beim Programmstart der Fall ist
        buildSettings();

        //styling
        this.setMinWidth(300);
        this.setMaxWidth(300);
        preview.setAlignment(Pos.CENTER);
        preview.setId("preview");
        preview.setMinHeight(250);


        //Zusammenbauen
        this.setTop(preview);
        this.setCenter(settings);
        this.setBottom(buttonBar);


    }

    /**
     * In dieser Methode wird der Previewbereich aufgebaut in dem Objekt "Preview"
     */
    protected abstract void buildPreview();

    /**
     * Hier wird der Inhalt der jeweiligen SideMenuViews erstellt in dem Objekt "settings"
     */
    abstract void buildSettings();


    /**
     * Untere Buttonleiste wird aufgebaut
     */
    public void buildSwitchButtons() {
        buttonBar = new HBox();

        paketErstellenReiter = new Button();
        vorlagenReiter = new Button();
        sucheReiter = new Button();
        regalReiter = new Button();

        //styling
        paketErstellenReiter.setId("paket_erstellen_button");
        paketErstellenReiter.getStyleClass().add("sidemenu_buttonbar_button");
        vorlagenReiter.setId("vorlagen_button");
        vorlagenReiter.getStyleClass().add("sidemenu_buttonbar_button");
        sucheReiter.setId("suchen_button");
        sucheReiter.getStyleClass().add("sidemenu_buttonbar_button");
        regalReiter.setId("regal_button");
        regalReiter.getStyleClass().add("sidemenu_buttonbar_button");


        buttonBar.setId("buttonbar_sidebar");
        buttonBar.setAlignment(Pos.CENTER);

        paketErstellenReiter.setMinSize(75, 50);
        vorlagenReiter.setMinSize(75, 50);
        sucheReiter.setMinSize(75, 50);
        regalReiter.setMinSize(75, 50);


        paketErstellenReiter.setOnAction(e -> {
            menuView.showPaketErstellenReiter();
            menuView.getTopMenu().setUeberschrift("Paket Erstellen");

        });

        vorlagenReiter.setOnAction(e -> {
            menuView.showVorlagenReiter();
            menuView.getTopMenu().setUeberschrift("Vorlagen");


        });

        sucheReiter.setOnAction(e -> {
            menuView.showSuchenReiter();
            menuView.getTopMenu().setUeberschrift("Suche");


        });

        regalReiter.setOnAction(e -> {
            menuView.showRegalView();
            menuView.getTopMenu().setUeberschrift("Regalmodus");
        });


        buttonBar.getChildren().addAll(regalReiter, paketErstellenReiter, vorlagenReiter, sucheReiter);


    }


}
