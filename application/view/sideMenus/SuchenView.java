package RegalisatorEnterprise.application.view.sideMenus;

import RegalisatorEnterprise.application.exceptions.PaketNichtGefundenException;
import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.logik.Paket;
import RegalisatorEnterprise.application.view.MenuView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Created by Josh on 21.06.17.
 * Seitenmenü, das dem User das Suchen von Paketen im Lager ermöglicht
 *
 * @author Leo Back, Joshua Barth, Julian Sauer, Clemens Klein
 */
public class SuchenView extends SideMenu {
    private HBox suche;
    private Label suchTextLabel;
    private TextField suchText;
    private Button suchen;
    private Label ergebnisLabel;
    private ListView ergebnisAnzeige;
    private List<Paket> ergebnisse;
    private ObservableList<HBoxCell> ergebnisBoxes;
    private int id;
    private String name;

    public SuchenView(MenuView menuView, Lager lager) {
        super(menuView, lager);
    }

    @Override
    protected void buildPreview() {
        // TODO implemtn
    }

    /**
     * Erstellt die View des Suchmenüs
     */
    @Override
    void buildSettings() {
        suche = new HBox(10.0);
        ergebnisAnzeige = new ListView();
        suchTextLabel = new Label("Paket Name:");
        suchText = new TextField();
        ergebnisBoxes = FXCollections.observableArrayList();
        suchText.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                name = newValue;
            }
        });
        suchen = new Button("suchen");
        suchen.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                refreshView();

            }
        });


        suche.setAlignment(Pos.CENTER);
        suche.getChildren().addAll(suchTextLabel, suchText, suchen);
        ergebnisLabel = new Label("Ergebnisse: ");
        ergebnisAnzeige.setPrefWidth(100);
        ergebnisAnzeige.setPrefHeight(300);
        settings.getChildren().addAll(suche, ergebnisLabel, ergebnisAnzeige);
    }

    /**
     * Erneuert die View sobald sich die Liste der Suchergebnisse ändert
     */

    private void refreshView() {
        ergebnisBoxes.clear();
        try {
            ergebnisse = lager.getPaketByName(name);
        } catch (PaketNichtGefundenException e) {
            ergebnisse.clear();
            //   menuView.createAlert("Keine Pakete gefunden!");
        }
        for (Paket p : ergebnisse) {

            ergebnisBoxes.add(new HBoxCell(p));
        }
        ergebnisAnzeige.setItems(ergebnisBoxes);
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

            delete.setOnAction(e -> menuView.getLager().removePaket(paket.getId()));

            delete.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    menuView.getLager().removePaket(paket.getId());
                    try {
                        ergebnisse = lager.getPaketByName(paket.getName());
                    } catch (PaketNichtGefundenException e) {
                        ergebnisBoxes.clear();
                        //  menuView.createAlert("Keine Pakete übrig!");
                    }
                    refreshView();
                }
            });


            this.getChildren().addAll(kreis, label, delete);
        }


    }


}




