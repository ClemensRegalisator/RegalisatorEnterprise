package RegalisatorEnterprise.application.logik;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Paket Datenbjekt.
 */
public class Paket implements Serializable {

    private IntegerProperty breite;
    private IntegerProperty hoehe;
    private IntegerProperty gewicht;
    private ObjectProperty<Color> farbe;
    private StringProperty name;
    private IntegerProperty id;
    private ObservableList<Integer> muster;
    private int tragkraft;
    private ObservableList<Color> unvertraeglichkeiten;
    private IntegerProperty textur;

    /**
     * Paket Klasse. Im Wesentlichen für die Datenhaltung zuständig.
     *
     * @param breite
     * @param hoehe
     * @param gewicht
     * @param farbe
     * @param name
     */
    public Paket(int breite, int hoehe, int gewicht, Color farbe, String name) {
        this.breite = new SimpleIntegerProperty(breite);
        this.hoehe = new SimpleIntegerProperty(hoehe);
        this.gewicht = new SimpleIntegerProperty(gewicht);
        this.farbe = new SimpleObjectProperty<>(farbe);
        this.name = new SimpleStringProperty(name);
        this.tragkraft = gewicht;
        this.id = new SimpleIntegerProperty(Lager.getID());
        this.muster = FXCollections.observableArrayList();
        this.unvertraeglichkeiten = FXCollections.observableArrayList();
        textur = new SimpleIntegerProperty();
        textur.set(0);
    }

    /**
     * Prüft die übergebene Farbe auf Unverträglichkeiten
     */
    public boolean checkFarbe(Color farbe) {

        for (Color f : unvertraeglichkeiten)
            if (f.equals(farbe))  // Wenn das Paket die Farben nicht verträgt
                return false;

        return true;
    }


    public int getBreite() {
        return breite.get();
    }

    public int getHoehe() {
        return hoehe.get();
    }

    public int getGewicht() {
        return gewicht.get();
    }

    public Color getFarbe() {
        return farbe.get();
    }

    public void setFarbe(Color farbe) {
        this.farbe.set(farbe);
    }

    public IntegerProperty breiteProperty() {
        return breite;
    }

    public IntegerProperty hoeheProperty() {
        return hoehe;
    }

    public IntegerProperty gewichtProperty() {
        return gewicht;
    }

    public ObjectProperty<Color> farbeProperty() {
        return farbe;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void addUnvertraeglichkeit(Color farbe) {
        unvertraeglichkeiten.add(farbe);
    }

    public void deleteUnvertraeglichkeit(Color farbe) {
        unvertraeglichkeiten.remove(farbe);
    }

    public ObservableList<Color> getUnverträglichkeiten() {
        return this.unvertraeglichkeiten;
    }

    public boolean isUnvertraeglichkeit(Color farbe) {
        for (Color c : unvertraeglichkeiten) {
            if (farbe == c) {
                return true;
            }
        }
        return false;

    }

    public void setMuster(int muster) {
        this.muster.clear();
        this.muster.add(muster);
    }

    public void addMuster(int muster) {
        if (!this.muster.contains(muster)) {
            this.muster.add(muster);
        }
    }


    public ObservableList<Integer> getMuster() {
        return this.muster;
    }

    private void setMuster(ObservableList l) {
        muster = l;
    }

    public IntegerProperty getTextur() {
        return this.textur;
    }

    public void setTextur(int textur) {
        this.textur.set(textur);
    }

    public int getTragkraft() {
        return tragkraft;
    }

    public void setTragkraft(int tragkraft) {
        this.tragkraft = tragkraft;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Paket clone() {
        Paket kopie = new Paket(this.getBreite(), this.getHoehe(), this.getGewicht(), this.getFarbe(), this.getName());
        for (int i : muster) {
            kopie.addMuster(i);
        }
        for (Color c : this.unvertraeglichkeiten) {
            kopie.addUnvertraeglichkeit(c);
        }
        kopie.setTextur(this.textur.getValue());
        return kopie;
    }

    @Override
    public String toString() {
        return String.format("Paket(%s, id=%s, breite:%s)", getName(), getId(), getBreite());
    }

}
