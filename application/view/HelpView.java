package RegalisatorEnterprise.application.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Beinhaltet einen ImageViewer, der die Anleitung darstellt.
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class HelpView extends BorderPane {
    private PictureGalery pg;
    private Label ueberschrift;
    private Button backButton;
    private Label text;
    private VBox topContainer;
    private VBox bottomContainer;

    public HelpView(MenuView menuView) {
        pg = new PictureGalery("/RegalisatorEnterprise/application/persistenz/pictures/manual/"); //Eigener Viewer
        ueberschrift = new Label("Hilfe");
        text = new Label("Anleitung und Hilfestellung");
        topContainer = new VBox();
        bottomContainer = new VBox();
        backButton = new Button("Zurück");


        topContainer.getChildren().addAll(ueberschrift, text);
        bottomContainer.getChildren().addAll(backButton);


        this.setTop(topContainer);
        this.setCenter(pg);
        this.setBottom(bottomContainer);

        //styling
        this.setStyle("-fx-background-color: #2e2e34");
        pg.minHeightProperty().bind(menuView.heightProperty().subtract(400));
        pg.maxHeightProperty().bind(menuView.heightProperty().subtract(400));
        ueberschrift.setId("h1");
        text.setId("h2");
        topContainer.setAlignment(Pos.BOTTOM_CENTER);
        bottomContainer.setAlignment(Pos.CENTER);


        backButton.setOnAction(e -> {
            menuView.hideHelp();
        });
    }
}
