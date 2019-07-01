package RegalisatorEnterprise.application.view.datenViews;

import RegalisatorEnterprise.application.Main;
import RegalisatorEnterprise.application.logik.Lager;
import RegalisatorEnterprise.application.logik.Paket;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Darstelleung eines Pakets. Übernimmt im wesentlichen die Darstellung der im Paket-Objekt hinterlegten Daten.
 */
public class PaketView extends StackPane {

    private Paket paket;
    private Rectangle rectangle;

    public PaketView(Paket paket) {
        rectangle = new Rectangle();
        this.paket = paket;


        this.setId("paket");


        /* -- Listener -- */

        // Aussehen aus Datenobjekt ziehen und einstellen
        rectangle.fillProperty().bind(paket.farbeProperty());


        //Wenn die Textur geändert wurde, lade die Muster des Pakets neu
        this.paket.getTextur().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                erzeugePaketMuster();
            }
        });

        //Wenn die muster verändert werden, lade Muster des Pakets neu
        this.paket.getMuster().addListener((ListChangeListener<Integer>) c -> {
            erzeugePaketMuster();
        });


        //Wenn eine Unverträglichkeit hinzugefügt wird, aktualisiere Muster
        this.paket.getUnverträglichkeiten().addListener((ListChangeListener<Color>) c -> {
            erzeugePaketMuster();
        });

        //Wenn eine Unverträglichkeit hinzugefügt wird, aktualisiere Muster
        this.paket.getTextur().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > 0) {
                erzeugePaketMuster();
            }
        });


        rectangle.widthProperty().bind(paket.breiteProperty());
        rectangle.heightProperty().bind(paket.hoeheProperty());

        setOnDragDetected(e -> {

            Dragboard db = startDragAndDrop(TransferMode.ANY);

            ClipboardContent c = new ClipboardContent();

            c.putString("" + getPaket().getId());

            db.setContent(c);

            Lager.paketIsDragged.set(true);


        });

        setOnDragDone(event -> {
            Lager.paketIsDragged.set(false);
        });

        this.getChildren().addAll(rectangle);
        erzeugePaketMuster();

    }


    /**
     * Überprüft welche Musternummer das aktuelle Paket bestitzt und weist das entsprechende Bild zu,
     * falls eine Unverträglichkeit besteht, wird ein Warnsymbol aufgetragen
     */
    public void erzeugePaketMuster() {
        this.getChildren().clear();

        //Falls eine Textur da ist zeige nur das Muster an
        if (this.paket.getTextur().getValue() != 0) {
            ImageView aktMuster = new ImageView();
            Image texture;

            try {
                texture = new Image(Main.class.getResourceAsStream(
                        "/RegalisatorEnterprise/application/persistenz/pictures/texturen/"
                                + paket.getTextur().getValue() + ".png"));

            } catch (NullPointerException e) {
                texture = new Image(Main.class.getResourceAsStream(
                        "/RegalisatorEnterprise/application/persistenz/pictures/texturen/"
                                + paket.getTextur().getValue() + ".gif"));
            }

            aktMuster.setImage(texture);
            aktMuster.fitHeightProperty().bind(rectangle.heightProperty());
            aktMuster.fitWidthProperty().bind(rectangle.widthProperty());
            //aktMuster.setPreserveRatio(true);
            this.getChildren().add(aktMuster);
        } else //Ansonsten zeige Erst Farbe, dann alle gewählten Muster und dann Unverträglichkeitsmuster an
        {
            //Füge Rechteck (Farbe) hinzu
            this.getChildren().add(rectangle);

            //setze Muster
            for (int muster : this.paket.getMuster()) {
                ImageView aktMuster = new ImageView();
                //Danger Muster abfangen
                if (muster > 0) {
                    aktMuster.setImage(new Image(Main.class.getResourceAsStream("persistenz/pictures/muster/" + muster + ".png")));
                    aktMuster.fitHeightProperty().bind(rectangle.heightProperty());
                    aktMuster.fitWidthProperty().bind(rectangle.widthProperty());
                    //aktMuster.setPreserveRatio(true);
                    this.getChildren().add(aktMuster);
                }
            }

            if (paket.getUnverträglichkeiten().size() > 0) {
                ImageView danger = new ImageView();
                Image dangerImage = new Image(Main.class.getResourceAsStream("persistenz/pictures/muster/x.png"));
                danger.setImage(dangerImage);
                danger.fitHeightProperty().bind(rectangle.heightProperty());
                danger.fitWidthProperty().bind(rectangle.widthProperty());
                this.getChildren().add(danger);
            }

        }

    }

    public Paket getPaket() {
        return paket;
    }
}
