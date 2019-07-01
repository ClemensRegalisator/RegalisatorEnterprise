package RegalisatorEnterprise.application.view.datenViews;

import RegalisatorEnterprise.application.exceptions.RegalZuGrossException;
import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.logik.Regal;
import RegalisatorEnterprise.application.view.MenuView;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

/**
 * Darstellung des Lagers in der GUI. enthaltene Regale werden einfach Horizontal, unten, nebeneinander angeordnet.
 */
public class LagerView extends HBox {
    private Lager lager;

    public LagerView(Lager l) {
        this.setId("lager");
        lager = l;

        setPadding(new Insets(0, 0, 20, 30));

        /* Listener */
        l.getRegale().addListener((ListChangeListener<Regal>) c -> reloadRegale());
        setOnDragOver(e -> {
            if (e.getDragboard().getString().startsWith("R")) {
                e.acceptTransferModes(TransferMode.ANY);
                e.consume();
            }
        });
        setOnDragDropped(event -> {
            try {
                lager.addRegal(parseRegalString(event.getDragboard().getString()));
            } catch (RegalZuGrossException e) {
                MenuView.createAlert(e.getMessage());
            }
        });

        widthProperty().addListener((observable, oldValue, newValue) -> {
            lager.setBreite(newValue.intValue());
        });

        /* Styling */
        setAlignment(Pos.BOTTOM_LEFT);

        // lager initial laden
        reloadRegale();
    }

    /**
     * Parst den im DragBoard enthaltenen String, der die Daten für ein gedraggtes Regal (Breite, Höhe) enthält und
     * erzeugt daraus ein neues Regal - Objekt.
     */
    private Regal parseRegalString(String regalString) {
        String[] regalStringSpl = regalString.split(" ");

        int breite = Integer.parseInt(regalStringSpl[1]);
        int hoehe = Integer.parseInt(regalStringSpl[2]);

        return new Regal(breite, hoehe, getLager());
    }

    /**
     * Lädt enthaltenen RegalViews neu.
     */
    public void reloadRegale() {
        getChildren().clear();

        for (Regal r : lager.getRegale()) {

            getChildren().add(new RegalView(r));

        }
    }

    public Lager getLager() {
        return this.lager;
    }

}
