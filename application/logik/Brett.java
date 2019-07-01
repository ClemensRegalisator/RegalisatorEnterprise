package RegalisatorEnterprise.application.logik;

import RegalisatorEnterprise.application.exceptions.PaketIstUnvertraeglichException;
import RegalisatorEnterprise.application.exceptions.PaketZuGrossException;
import RegalisatorEnterprise.application.exceptions.PaketZuSchwerException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by Clemens on 21.06.2017
 * Brett Datenobjekt
 * solide Holzklasse ohne Splitter
 */
public class Brett {
    private IntegerProperty hoehe;
    private int breite;
    private IntegerProperty tragkraft;
    private ObservableList<Stapel> stapel;
    private Lager lager;
    private boolean platziert;

    public Brett(int hoehe, int tragkraft) {
        this.hoehe = new SimpleIntegerProperty(hoehe);
        this.tragkraft = new SimpleIntegerProperty(tragkraft);
        stapel = FXCollections.observableArrayList();
        platziert = false;
    }

    /**
     * Versucht, Paket mit gegebener id und Position dem Brett hinzuzufügen.
     * <p>
     * Leitet dabei enstehende Exceptions zum Aufrufer weiter.
     * <p>
     * Die Methde holt sich aus dem Lager eine Liste mit Paketen. Diese Liste enthält das verlangte Paket
     * sowie alle aufliegenden Pakete.
     */
    public void addPaket(int id, int pos) throws PaketZuGrossException, PaketIstUnvertraeglichException, PaketZuSchwerException {
        addPakete(lager.getPaketListById(id), pos);
    }

    /**
     * Versucht einen Substapel Pakete dem Brett hinzuzufügen.
     * <p>
     * Die checkPosition-Methode entscheidet dabei, ob die Methode einen neuen Stapel anlegt oder einen
     * bestehenden ergänzt
     * <p>
     * Sie führt erst alle Tests durch und fügt anschließend Pakete ein.
     */
    private void addPakete(List<Paket> paketList, int pos) throws PaketZuSchwerException, PaketZuGrossException, PaketIstUnvertraeglichException {
        Paket paket = paketList.get(0);
        Stapel s = new Stapel(pos, getHoehe(), paket.getBreite(), getTragkraft(), lager);

        checkGewicht(paketList, aufBrett(paket));
        checkColors(paketList);

        s = checkPosition(s);

        s.addPakete(paketList);


    }

    /**
     * Prüft, ob sich ein Paket bereits auf einem Brett befindet.
     */
    private boolean aufBrett(Paket paket) {
        for (Stapel s : getStapel())
            for (Paket p : s.getPakete())
                if (p.getId() == paket.getId())
                    return true;
        return false;
    }


    /**
     * Liefert, abhängig von der Position des übergebenen Stapels, einen schon existierenden Stapel zurück,
     * oder einen neu angeleten, falls sich an der Position noch keiner befindet.
     */
    private Stapel checkPosition(Stapel st) throws PaketZuGrossException {
        // Anfang und Ende des belegten Bereichs bestimmen
        int a = st.getPosition();
        int b = a + st.getBreite();


        if (b > getBreite() || a < 0) {  // Wenn der Stapel außerhalb des Bretts liegt
            throw new PaketZuGrossException("Paket ist zu Breit für das Brett!");
        }

        for (Stapel s : stapel) {
            // Wenn zu testender Stepel in bereits bestehendem liegt
            if (s.getPosition() <= a && s.getPosition() + s.getBreite() >= a ||
                    s.getPosition() <= b && s.getPosition() + s.getBreite() >= b) {
                return s;
            }
        }

        getStapel().add(st);

        return st;
    }

    /**
     * Testet die Pakete auf unverträglichkeiten im Bezug auf die Farben.
     *
     * @throws PaketIstUnvertraeglichException geworfen, wenn Farben nicht passen
     */
    private void checkColors(Paket paket) throws PaketIstUnvertraeglichException {
        for (Stapel s : stapel) {
            for (Paket p2 : s.getPakete()) {
                Color farbeP = paket.getFarbe();
                Color farbeP2 = p2.getFarbe();

                //noinspection PointlessBooleanExpression
                if (!paket.checkFarbe(farbeP2) || !p2.checkFarbe(farbeP))
                    throw new PaketIstUnvertraeglichException(paket.getName() + " verträgt sich nicht mit " + paket.getName());
            }
        }
    }

    /**
     * Führt checkColors für eine Liste von Paketen durch.
     */
    private void checkColors(List<Paket> paketList) throws PaketIstUnvertraeglichException {
        for (Paket p : paketList)
            checkColors(p);
    }

    /**
     * Führt Tests für das Gewicht durch. Prüft, ob das Brett noch das Paket tragen kann.
     *
     * @param brettIntern Wenn die Verschiebung von Paketen innerhalb einer Brett-Instanz
     *                    geschieht, muss das Gewicht nicht geprüft werden: Es liegt ja alles jetzt
     *                    schon auf dem Brett.
     * @throws PaketZuSchwerException geworfen, wenn substapel zu schwer für das Brett ist
     */
    private void checkGewicht(List<Paket> paketList, boolean brettIntern) throws PaketZuSchwerException {

        if (brettIntern)  // Gewicht stimmt auf jeden Fall wenn innerhalb eines Bretts verschoben wird
            return;

        int gewicht = 0;
        int paketStapelGewicht = 0;

        for (Paket p : paketList) {
            paketStapelGewicht += p.getGewicht();
        }

        for (Stapel st : stapel) {
            for (Paket p : st.getPakete())
                gewicht += p.getGewicht();
        }

        if (gewicht + paketStapelGewicht >= getTragkraft())
            throw new PaketZuSchwerException("Das Brett kann das Paket nicht tragen!");

    }

    public int getHoehe() {
        return hoehe.get();
    }

    public IntegerProperty hoeheProperty() {
        return hoehe;
    }

    public int getTragkraft() {
        return tragkraft.get();
    }

    public IntegerProperty tragkraftProperty() {
        return tragkraft;
    }

    public int getBreite() {
        return breite;
    }

    public void setBreite(int breite) {
        this.breite = breite;
    }

    public ObservableList<Stapel> getStapel() {
        return stapel;
    }

    @Override
    public String toString() {
        return String.format("Brett(%s)", stapel);
    }

    public void setLager(Lager lager) {
        this.lager = lager;
    }

    public boolean isPlatziert() {
        return platziert;
    }

    public void setPlatziert(boolean platziert) {
        this.platziert = platziert;
    }

}
