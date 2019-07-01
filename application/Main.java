package RegalisatorEnterprise.application;

import RegalisatorEnterprise.application.persistenz.FileHandler;
import RegalisatorEnterprise.application.view.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Regalisator Ultimate bietet die Möglichkeit ein Lager mit Regalen aufzubauen und Pakete zu erstellen und zu verwalten
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class Main extends Application {
    private Stage primaryStage;
    private MenuView menuView;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.menuView = new MenuView(this);

        Scene scene = new Scene(menuView);


        primaryStage.setTitle("Regalisator Enterprise");

        // primaryStage.setMinWidth(1100);
        // primaryStage.setMinHeight(800);
         primaryStage.setFullScreen(true);


        primaryStage.setScene(scene);

        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); //Deaktiviert Fullscreen Meldung


        scene.getStylesheets().add(("RegalisatorEnterprise/application/view/css/menu.css"));
        scene.getStylesheets().add(("RegalisatorEnterprise/application/view/css/lager.css"));

        primaryStage.show();

    }

    public void stop() throws Exception {

    }


}
