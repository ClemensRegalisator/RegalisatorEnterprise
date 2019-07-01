package RegalisatorEnterprise.application.view;

import RegalisatorEnterprise.application.Main;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Ein Bildviewer, der einen Pfad mit Bildern bekommt und einem ermöglicht
 * durch alle Bilder des Pfades durchzunavigieren
 *
 * @author Clemens Klein, Julian Sauer, Leo Back, Joshua Barth
 */
public class PictureGalery extends BorderPane {
    private String path;
    private ArrayList<ImageView> imageViews;
    private ArrayList<Rectangle> rechtecke;
    private int size;
    private StackPane center;
    private BorderPane onCenter;

    private Button left;
    private Button right;
    private VBox leftBox;
    private VBox rightBox;
    private HBox seitenAnzeige;


    private boolean isnext;
    private boolean isrecent;

    private double biggestHeight;
    private ImageView biggestWidth;

    private SimpleIntegerProperty aktPic;

    public PictureGalery(String path) {
        aktPic = new SimpleIntegerProperty();
        imageViews = new ArrayList<ImageView>();
        rechtecke = new ArrayList<Rectangle>();

        this.path = path;
        readFiles(path);
        size = imageViews.size();


        isnext = false;
        isrecent = false;
        buildUI();


        aktPic.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rechtecke.get(newValue.intValue()).setFill(Color.WHITE);
                rechtecke.get(oldValue.intValue()).setFill(Color.BLACK);
            }
        });


    }

    /**
     * Erstellt je nach Anzahl der Bilder die
     * erforderlichen Panes und setzt sie zusammen
     */
    public void buildUI() {
        left = new Button();
        right = new Button();
        onCenter = new BorderPane();
        center = new StackPane();
        leftBox = new VBox();
        rightBox = new VBox();
        seitenAnzeige = new HBox();
        this.setCenter(center);


        leftBox.getChildren().addAll(left);
        rightBox.getChildren().addAll(right);
        onCenter.setLeft(leftBox);
        onCenter.setRight(rightBox);
        onCenter.setBottom(seitenAnzeige);


        //Stylen
        center.setAlignment(Pos.CENTER);
        leftBox.setAlignment(Pos.CENTER);
        rightBox.setAlignment(Pos.CENTER);
        seitenAnzeige.setAlignment(Pos.CENTER);
        seitenAnzeige.setSpacing(4);
        seitenAnzeige.setPadding(new Insets(0, 0, 20, 0));
        right.prefHeightProperty().bind(center.heightProperty());
        right.prefWidthProperty().bind(center.widthProperty().divide(10));
        left.prefHeightProperty().bind(center.heightProperty());
        left.prefWidthProperty().bind(center.widthProperty().divide(10));

        left.setId("imageViewerLeft");
        right.setId("imageViewerRight");
        center.maxWidthProperty().bind(imageViews.get(aktPic.getValue()).fitWidthProperty());

        onCenter.maxWidthProperty().bind(center.widthProperty());


        center.getChildren().addAll(imageViews.get(0));
        center.getChildren().addAll(onCenter);


        //Erstelle rechtecke als Seitenanzahl
        for (int i = 0; i < size; i++) {
            Rectangle r = new Rectangle(30, 5);
            r.setFill(Color.BLACK);
            seitenAnzeige.getChildren().addAll(r);
            rechtecke.add(r);
        }

        rechtecke.get(0).setFill(Color.WHITE);
        aktPic.setValue(0);


        //Buttonfunktionen
        right.setOnAction(e -> {
            if (!isnext && !isrecent)
                next();
        });

        left.setOnAction(e -> {
            if (!isnext && !isrecent)
                recent();
        });


    }

    /**
     * Springt zum nächstem Bild (Animiert)
     */
    public void next() {
        isnext = true;

        int last = (aktPic.getValue().intValue());
        aktPic.setValue(((aktPic.getValue().intValue()) + 1) % size);

        center.getChildren().add(1, imageViews.get(aktPic.getValue().intValue()));


        imageViews.get(aktPic.getValue().intValue()).setOpacity(0);

        TranslateTransition st = new TranslateTransition(Duration.millis(400), imageViews.get(aktPic.getValue().intValue()));
        st.setFromX(this.getWidth());
        st.setFromY(0);
        st.setToX(0);
        st.setToY(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(300), imageViews.get(last));
        scale.setFromX(getScaleX());
        scale.setFromY(getScaleY());
        scale.setToX(0.6);
        scale.setToY(0.6);


        KeyFrame k1 = new KeyFrame(Duration.millis(1), a -> {
            scale.play();

        });


        KeyFrame k2 = new KeyFrame(Duration.millis(200), a -> {
            imageViews.get(aktPic.getValue().intValue()).setOpacity(1);

        });

        KeyFrame k3 = new KeyFrame(Duration.millis(1), a -> {
            st.play();
        });


        Timeline t = new Timeline();
        t.setAutoReverse(false);
        t.setCycleCount(1);
        t.getKeyFrames().addAll(k1, k2, k3);
        t.play();


        st.setOnFinished(e -> {
            center.getChildren().remove(imageViews.get(last));
            imageViews.get(last).setScaleX(1);
            imageViews.get(last).setScaleY(1);
            isnext = false;

        });


    }

    /**
     * Springt zum vorherigem Bild (Animiert)
     */
    public void recent() {
        isrecent = true;
        int last = aktPic.getValue().intValue();
        int neu = (aktPic.getValue().intValue() - 1) % size;
        if (neu < 0) {
            aktPic.setValue(size - 1);
        } else {
            aktPic.setValue((aktPic.getValue().intValue() - 1) % size);

        }


        center.getChildren().add(0, imageViews.get(aktPic.getValue().intValue()));

        TranslateTransition st1 = new TranslateTransition(Duration.millis(1), imageViews.get(aktPic.getValue().intValue()));
        st1.setFromX(this.getWidth());
        st1.setFromY(0);
        st1.setToX(0);
        st1.setToY(0);


        TranslateTransition st = new TranslateTransition(Duration.millis(300), imageViews.get(last));
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(this.getWidth());
        st.setToY(0);

        ScaleTransition scale = new ScaleTransition(Duration.millis(400), imageViews.get(aktPic.getValue().intValue()));
        scale.setFromX(0.6);
        scale.setFromY(0.6);
        scale.setToX(1);
        scale.setToY(1);


        imageViews.get(aktPic.getValue().intValue()).setLayoutX(getLayoutX());
        imageViews.get(aktPic.getValue().intValue()).setLayoutY(getLayoutY());
        scale.play();
        imageViews.get(aktPic.getValue().intValue()).setOpacity(1);
        st1.play();
        scale.play();
        st.play();

        st.setOnFinished(e -> {
            isrecent = false;
            imageViews.get(last).setLayoutX(0);
            imageViews.get(last).setLayoutY(0);
            center.getChildren().remove(imageViews.get(last));


        });


    }

    /**
     * Liest rekursiv alle Bilder aus angegebenem Pfad ein und
     * fuegt sie in eine Arraylist
     *
     * @param path Dateipfad der Bilder
     */
    public void readFiles(String path) {
        int i = 1;
        while (true) {
            try {
                ImageView iv = new ImageView(new Image(Main.class.getResourceAsStream(path + i + ".png")));

                iv.fitHeightProperty().bind(this.heightProperty());
                iv.fitWidthProperty().bind(this.widthProperty());
                iv.setPreserveRatio(true);
                imageViews.add(iv);

            } catch (NullPointerException e) {
                break;

            }
            i++;
        }

    }


}
