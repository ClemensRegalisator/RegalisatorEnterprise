package RegalisatorEnterprise.application.view.datenViews;

import RegalisatorEnterprise.application.logik.Paket;
import RegalisatorEnterprise.application.logik.Stapel;
import javafx.collections.ListChangeListener;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Stellt die Logik - Repräsentation der Stapel in der GUI dar.
 */
public class StapelView extends VBox {
    private Stapel stapel;

    public StapelView(Stapel stapel) {
        this.stapel = stapel;

        /* -- Styling -- */
        getStyleClass().add("stapel");

        /* -- Listener -- */
        stapel.getPakete().addListener((ListChangeListener<Paket>) change -> reloadPakete());

        setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.ANY);
            e.consume();
        });

        setOnDragDetected(e -> {
            getChildren().remove(e.getSource());
        });

        stapel.positionProperty().addListener((observable, oldValue, newValue) -> {
            translateXProperty().setValue(newValue);
        });

        //1x initial positionieren
        setLayoutX(stapel.getPosition());


        reloadPakete();
    }


    private void reloadPakete() {
        getChildren().clear();

        int stapelHoehe = 0;
        for (Paket p : stapel.getPakete()) {

            getChildren().add(new PaketView(p));
            stapelHoehe += p.getHoehe();
        }
        /*
         * - Erbärmlich aber muss sein: -
         * Damit die Boxen unten platziert werden, wird oben auf den stapel ein
         * unsichtbares Viereck platziert, das den Rest der Höhe einnimmt, da alignment in den Children eines Panes
         * nicht funktioniert (BrettView ist ein Pane).
         * Bedankt euch bei JavaFX >:/
         */

        getChildren().add(0, new Rectangle(0, stapel.getMaxHoehe() - stapelHoehe - 6, Color.TRANSPARENT));

    }
}
