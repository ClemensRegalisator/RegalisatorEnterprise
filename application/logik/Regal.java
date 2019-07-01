package RegalisatorEnterprise.application.logik;

import RegalisatorEnterprise.application.exceptions.BrettZuHochException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Regal {
    private IntegerProperty breite;
    private IntegerProperty hoehe;
    private ObservableList<Brett> bretter;
    private Lager lager;
    private boolean platziert;

    public Regal(int breite, int hoehe, Lager lager) {
        this.breite = new SimpleIntegerProperty(breite);
        this.hoehe = new SimpleIntegerProperty(hoehe);
        this.lager = lager;

        bretter = FXCollections.observableArrayList();
    }

    /**
     * Testet die Größe des Bretts und fügt es anschließend hinzu.
     */
    public boolean addBrett(Brett b) throws BrettZuHochException {
        if (checkPosition(b)) {  // überprüfen ob noch platz in List mit checkPosition

            b.setBreite(getBreite());  // Eigene Breite als Breite des Bretts setzen
            b.setLager(lager);
            b.setPlatziert(true);

            bretter.add(b);
            return true;
        }

        return false;
    }

    /**
     * Testet, ob übergebenes Brett zu hoch ist.
     */
    private boolean checkPosition(Brett b) throws BrettZuHochException {
        int pos = 0;
        for (Brett brett : bretter) {  //iteriert durch bereits vorhandene Bretter im Regal und  addiert
            pos += brett.getHoehe();          //deren Höhe auf um so die Position des neuen Brettes zu errechnen
        }

        if (b.getHoehe() + pos > getHoehe()) {
            throw new BrettZuHochException();
        }

        return true;  //returnt true wenn einfügen möglich und false wenn pos + höhe die höhe
    }                                        //des Regals überschreitet

    public int getBreite() {
        return breite.get();
    }

    public int getHoehe() {
        return hoehe.get();
    }


    public ObservableList<Brett> getBretter() {
        return bretter;
    }

    public boolean isPlatziert() {
        return platziert;
    }

    public void setPlatziert(boolean platziert) {
        this.platziert = platziert;
    }

    public IntegerProperty breiteProperty() {
        return breite;
    }

    public IntegerProperty hoeheProperty() {
        return hoehe;
    }

    @Override
    public String toString() {
        return String.format("Regal(%s)", bretter);
    }
}
