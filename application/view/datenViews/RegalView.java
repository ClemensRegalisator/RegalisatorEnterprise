package RegalisatorEnterprise.application.view.datenViews;

import RegalisatorEnterprise.application.exceptions.BrettZuHochException;
import RegalisatorEnterprise.application.logik.Brett;
import RegalisatorEnterprise.application.logik.Regal;
import RegalisatorEnterprise.application.view.MenuView;
import javafx.collections.ListChangeListener;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

/**
 * Zeigt Regale in der GUI an. Per Drag and Drop eingefügte Bretter werden von oben nach unten angeordnet dargestellt.
 */
public class RegalView extends VBox {

    private static final int BORDER_WIDTH = 15;

    private Regal regal;

    public RegalView(Regal regal) {

        this.regal = regal;

        /* -- Styling -- */
        // View mit Logik-Werten initialisieren
        setMinHeight(regal.getHoehe());
        setMaxHeight(regal.getHoehe());

        setMinWidth(regal.getBreite() + 2 * BORDER_WIDTH);
        setMaxWidth(regal.getBreite() + 2 * BORDER_WIDTH);

        getStyleClass().add("regal");

        /* -- Listener -- */
        regal.getBretter().addListener((ListChangeListener<Brett>) change -> reloadBretter());

        setOnDragOver(e -> {
            if (e.getDragboard().getString().startsWith("B"))
                e.acceptTransferModes(TransferMode.ANY);
            else
                e.acceptTransferModes(TransferMode.NONE);
            e.consume();

        });

        setOnDragDropped(e -> {
            try {
                String brettString = e.getDragboard().getString();
                Brett b = parseBrettString(brettString, e.getY());

                getRegal().addBrett(b);

            } catch (BrettZuHochException e1) {
                MenuView.createAlert("Zu Hoch");
            }
            e.consume();
        });

        setOnDragDetected(e -> {
            if (!regal.isPlatziert()) {
                Dragboard db = startDragAndDrop(TransferMode.ANY);

                ClipboardContent c = new ClipboardContent();

                c.putString("R " + regal.getBreite() + " " + regal.getHoehe());

                db.setContent(c);
            }
        });

        // Bretter initial laden
        reloadBretter();

    }

    /**
     * Parst den im DragBoard enthaltenen String, der die Daten für ein gedraggtes Brett (Tragkraft) enthält und
     * erzeugt daraus ein neues Regal - Objekt.
     */
    private Brett parseBrettString(String brettString, double y) {
        String[] brStrSplitted = brettString.split(" ");  // String aufteilen

        int tragkraft = Integer.parseInt(brStrSplitted[1]);

        int hoehe = calcHoehe(y);

        return new Brett(hoehe, tragkraft);
    }

    private int calcHoehe(double y) {
        int bretterHoehe = 0;

        for (Brett b : getRegal().getBretter())
            bretterHoehe += b.getHoehe();

        return (int) y - bretterHoehe;
    }


    private void reloadBretter() {
        getChildren().clear();

        for (Brett b : regal.getBretter()) {
            BrettView bv = new BrettView(b);
            getChildren().add(bv);

        }

    }

    public Regal getRegal() {
        return regal;
    }
}
