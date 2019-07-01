package RegalisatorEnterprise.application.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Obere Leiste des Fensters mit Menüelementen
 */
public class TopMenu extends BorderPane {
    private Button closeButton; //Im Moment nur der Close Button
    private Application main;

    private Button hilfe;
    private MenuView menuView;
    private Label ueberschrift;
    private HBox container;
    private HBox menuButtons;

    public TopMenu(Application main, MenuView menuView) {
        this.menuView = menuView;
        this.main = main;
        this.setId("top");


        // Erstelle Elemente
        closeButton = new Button();
        ueberschrift = new Label("Paket Erstellen");
        hilfe = new Button();
        menuButtons = new HBox();
        container = new HBox();
        container.getChildren().addAll(ueberschrift);

        // Funktionen zuweisen
        closeButton.setOnAction(e -> System.exit(0));
        hilfe.setOnAction(e -> menuView.showHelp());

        // styling
        closeButton.setId("close_button");
        hilfe.setId("help_button");

        ueberschrift.setId("h1");
        container.setMaxSize(300, 50);
        container.setMinSize(300, 50);
        container.setId("sidemenu_ueberschriftContainer");
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setSpacing(10);
        menuButtons.setPadding(new Insets(0, 10, 0, 0));


        // Zusammenbauen
        menuButtons.getChildren().addAll(hilfe, closeButton);
        this.setRight(menuButtons);
        this.setLeft(container);


    }

    public void setUeberschrift(String text) {
        this.ueberschrift.setText(text);
    }
}
