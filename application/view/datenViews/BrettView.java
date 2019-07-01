package RegalisatorEnterprise.application.view.datenViews;

import RegalisatorEnterprise.application.exceptions.PaketIstUnvertraeglichException;
import RegalisatorEnterprise.application.exceptions.PaketZuGrossException;
import RegalisatorEnterprise.application.exceptions.PaketZuSchwerException;
import RegalisatorEnterprise.application.logik.Brett;
import RegalisatorEnterprise.application.logik.Stapel;
import RegalisatorEnterprise.application.view.MenuView;
import javafx.collections.ListChangeListener;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * Darstellung der Brett-Klasse in der GUI.
 * <p>
 * Das Pane Objekt von dem geerbt wird ermöglicht bequeme Platzierung der enthaltenen Stapel mit X - Koordidnaten
 */
public class BrettView extends Pane {

    private Brett brett;

    public BrettView(Brett brett) {
        this.brett = brett;

        // View mit Werten aus Logik-Objekt füllen

        setMinHeight(brett.getHoehe());
        setMaxHeight(brett.getHoehe());


        getStyleClass().add("brett");

        /* -- Listener -- */
        brett.getStapel().addListener((ListChangeListener<Stapel>) change -> BrettView.this.reloadStapel());

        setOnDragOver(e -> {
            String dbString = e.getDragboard().getString();
            if (!(dbString.startsWith("R") || dbString.startsWith("B")))
                e.acceptTransferModes(TransferMode.ANY);
            e.consume();
        });

        setOnDragDropped(e -> {
            try {
                int id = Integer.parseInt(e.getDragboard().getString());

                brett.addPaket(id, (int) e.getX());

            } catch (PaketZuGrossException | PaketZuSchwerException | PaketIstUnvertraeglichException e1) {
                MenuView.createAlert(e1.getLocalizedMessage());

            } catch (NumberFormatException ignored) {

            }
            e.consume();
        });


        setOnDragDetected(e -> {
            if (!brett.isPlatziert()) {
                Dragboard db = startDragAndDrop(TransferMode.ANY);
                ClipboardContent c = new ClipboardContent();
                c.putString("B " + brett.getTragkraft());

                db.setContent(c);

            }
        });

        // Stapel initial laden
        reloadStapel();

    }

    /**
     * Lädt die StapelViews in der Klasse neu
     */
    private void reloadStapel() {
        getChildren().clear();

        for (Stapel s : brett.getStapel()) {
            StapelView sv = new StapelView(s);
            getChildren().add(sv);
        }
    }

    public Brett getBrett() {
        return brett;
    }
}
