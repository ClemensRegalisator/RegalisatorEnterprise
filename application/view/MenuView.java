package RegalisatorEnterprise.application.view;

import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.view.datenViews.LagerView;
import RegalisatorEnterprise.application.view.sideMenus.*;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

/**
 * Oberste Viewklasse, die alle view-Elemente koordiniert und verwaltet
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class MenuView extends BorderPane {
    private TopMenu topMenu;
    private LagerView lagerView;
    private Application main;

    private SideMenu paketErstellenView;
    private SideMenu regalErstellenView;
    private SideMenu suchenView;
    private SideMenu vorlagenView;

    private SideMenu aktSideView;
    private HelpView helpView;


    public MenuView(Application main) {
        this.main = main;
        this.helpView = new HelpView(this);
        initViews();

        //Füge zusammen
        this.setTop(topMenu);
        this.setCenter(lagerView);
        this.aktSideView = paketErstellenView;


        showSidebar();
    }

    public static Alert createAlert(String alertText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Obacht min Jung!");
        alert.setHeaderText(null);
        alert.setContentText(alertText);
        alert.showAndWait();


        return alert;
    }

    public void initViews() {

        Lager lager = new Lager();


        this.topMenu = new TopMenu(this.main, this);
        this.lagerView = new LagerView(lager);


        this.paketErstellenView = new PaketErstellenView(this, getLager());
        this.regalErstellenView = new RegalErstellenView(this, getLager());
        this.suchenView = new SuchenView(this, getLager());
        this.vorlagenView = new VorlagenView(this, lager);
    }

    public void showSidebar() {
        this.setLeft(aktSideView);

    }

    public void showPaketErstellenReiter() {
        aktSideView = paketErstellenView;
        this.setLeft(aktSideView);
    }

    public void showVorlagenReiter() {
        aktSideView = vorlagenView;
        this.setLeft(aktSideView);

    }

    public void showSuchenReiter() {
        aktSideView = suchenView;
        this.setLeft(aktSideView);

    }

    public void showHelp() {
        this.getChildren().clear();
        this.setCenter(helpView);
    }

    public void hideHelp() {
        this.setCenter(lagerView);
        this.setLeft(aktSideView);
        this.setTop(topMenu);

    }

    public TopMenu getTopMenu() {
        return this.topMenu;
    }

    public LagerView getLagerView() {
        return this.lagerView;
    }

    public SideMenu getAktSideView() {
        return this.aktSideView;
    }

    public void showRegalView() {
        aktSideView = regalErstellenView;
        this.setLeft(regalErstellenView);
    }

    public Lager getLager() {
        return this.lagerView.getLager();
    }

    public void showAktPaketModus() {
        aktSideView = paketErstellenView;

        this.setLeft(aktSideView);
    }

}






