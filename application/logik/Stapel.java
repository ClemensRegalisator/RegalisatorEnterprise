package RegalisatorEnterprise.application.logik;

import RegalisatorEnterprise.application.exceptions.PaketNichtGefundenException;
import RegalisatorEnterprise.application.exceptions.PaketZuGrossException;
import RegalisatorEnterprise.application.exceptions.PaketZuSchwerException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Stapel {

    private ObservableList<Paket> pakete;
    private IntegerProperty position;
    private int maxHoehe;
    private int tragkraft;
    private Lager lager;
    private int breite;

    public Stapel(int position, int maxHoehe, int breite, int tragkraft, Lager lager)
            throws PaketZuSchwerException, PaketZuGrossException {

        this.position = new SimpleIntegerProperty(position);
        this.maxHoehe = maxHoehe;
        this.pakete = FXCollections.observableArrayList();
        this.tragkraft = tragkraft;
        this.lager = lager;
        this.breite = breite;

        /* -- Listener -- */
        pakete.addListener((ListChangeListener<Paket>) c -> {
            if (pakete.size() == 0) {
                setBreite(0);
            } else {
                Paket ganzUnten = pakete.get(pakete.size() - 1);
                setBreite(ganzUnten.getBreite());
            }
        });

    }

    /**
     * Versucht, ein einzelnes Paket hinzuzufügen. Verringert dabei die Tragkraft aller Pakete unterhalb.
     */
    private void addPaket(Paket paket) throws PaketZuSchwerException, PaketZuGrossException {
//        checkGewicht(paket);
//        checkBreite(paket);
//        checkHoehe(paket);

        vermindereTragkraft(paket);
        pakete.add(0, paket);

    }

    /**
     * Versucht, eine Liste von Paketen hinzuzufügen.
     * <p>
     * Für alle Pakete wird getestet, ob sie noch auf den Stapel passen.
     * <p>
     * (Farben werden hier nicht mehr geprüft. Dies passiert in der aufrufenden Methode im Brett)
     *
     * @param paketList Der "Substapel", der hinzugefügt werden soll
     * @throws PaketZuGrossException Wird durch checkBreite / checkHoehe geworfen, wenn:
     *                               a)  Der Stapel durch die zusätzlichen Pakete zu hoch wird
     *                               b)  Das unterste Paket des Substapels breiter ist als das oberste des
     *                               Stapels
     */
    public void addPakete(List<Paket> paketList) throws PaketZuSchwerException, PaketZuGrossException {
        checkGewicht(paketList);  // wirft Exception wenns schiefgeht
        checkBreite(paketList.get(0));
        checkHoehe(paketList);

        try {
            moveAllPakete(paketList);
        } catch (PaketNichtGefundenException e) {
            for (Paket p : paketList)
                addPaket(p);
        }
    }

    /**
     * Vermindere die Tragkraft aller Pakete des Stapels
     *
     * @param paket Das Paket, dessen Gewicht von der Tragkraft des Stapels abgezogen werden soll
     */
    private void vermindereTragkraft(Paket paket) {
        for (Paket p : pakete)
            p.setTragkraft(p.getTragkraft() - paket.getGewicht());
    }

    /**
     * Erhöhe die Tragkraft aller Pakete des Stapels
     *
     * @param paket Das Paket, dessen Gewicht von der Tragkraft des Stapels hinzugefügt werden soll
     */
    public void erhoeheTragkraft(Paket paket) {
        for (Paket p : pakete)
            p.setTragkraft(p.getTragkraft() + paket.getGewicht());
    }

    /**
     * Versucht, einen Stapel Pakete zu verschieben.
     * dabei werden von oben nach unten Pakete verschoben. Durch die Einfügereihenfolge bleibt dann die
     * Reihenfolge erhalten
     */
    private void moveAllPakete(List<Paket> subStapel) throws PaketZuSchwerException, PaketZuGrossException {
        for (int i = subStapel.size() - 1; i >= 0; i--) {
            Paket p = subStapel.get(i);

            removePaket(p.getId());  // Aus ursprünglicher Position entfernen
            addPaket(p);             // und selbst hinzufügen
        }
    }

    /**
     * Sag dem Lager, es soll ein Paket entfernen.
     */
    public void removePaket(int id) {

        lager.removePaket(id);
    }

    /**
     * Test des Gewichts
     *
     * @throws PaketZuSchwerException Wird durch checkGewicht geworfen, wenn eines der Pakete das Gewicht des
     *                                Substapels nicht mehr tragen kann.
     */
    private void checkGewicht(Paket paket) throws PaketZuSchwerException {

        for (Paket p : pakete) {
            if (p.getTragkraft() < paket.getGewicht()) {
                throw new PaketZuSchwerException(p.getName() + " kann das Gewicht nicht tragen!");
            }
        }
    }

    /**
     * Tests des Gewichts für Paketstapel.
     */
    private void checkGewicht(List<Paket> paketList) throws PaketZuSchwerException {
        int gewicht = 0;
        for (Paket p : paketList)
            gewicht += p.getGewicht();

        for (Paket p : getPakete()) {
            if (p.getTragkraft() < gewicht) {
                throw new PaketZuSchwerException(p.getName() + " kann das Gewicht nicht tragen");
            }
        }
    }

    /**
     * Testet, ob ein Paket zu breit ist (Breiter als das oberste Paket)
     */
    private void checkBreite(Paket paket) throws PaketZuGrossException {
        Paket last;
        try {
            last = pakete.get(pakete.size() - 1);
        } catch (ArrayIndexOutOfBoundsException ignored) {
            // Wenn kein Paket vorhanden ist, dann ist die Position immer in ordnung
            return;
        }
        if (!(last.getBreite() >= paket.getBreite())) {
            throw new PaketZuGrossException(paket.getName() + " ist zu groß!");
        }
    }

    /**
     * Testet, ob Der Stapel mit dem übergebenen Paket zu groß wird.
     */
    private void checkHoehe(Paket paket) throws PaketZuGrossException {
        // Gesamthöhe Stapel bestimmen
        int gesamtHoehe = 0;
        for (Paket p : pakete)
            gesamtHoehe += p.getHoehe();
        if (gesamtHoehe + paket.getHoehe() > maxHoehe)
            throw new PaketZuGrossException(paket.getName() + " ist zu hoch für diesen Stapel!");
    }

    /**
     * Höhe-Test für einen Paket-Substapel.
     *
     * @param paketList
     * @throws PaketZuGrossException
     */
    private void checkHoehe(List<Paket> paketList) throws PaketZuGrossException {
        // Gesamthöhe Stapel bestimmen
        int gesamtHoehe = 0;
        for (Paket p : getPakete())
            gesamtHoehe += p.getHoehe();

        // Gesamthoehe neuer Stapel bestimmen
        int subStapelHoehe = 0;
        for (Paket p : paketList)
            subStapelHoehe += p.getHoehe();

        if (gesamtHoehe + subStapelHoehe > getMaxHoehe())
            throw new PaketZuGrossException("Zusatzstapel ist zu hoch für diesen Stapel!");
    }

    /**
     * Liefert eine Liste aus Paketen, bestehend aus dem übergebenen Paket und den daraufliegenden.
     * <p>
     * Es sollte, um eine Exception zu vermeiden, ein im Stapel befindliches Paket übergeben werden.
     */
    public List<Paket> getSubStapel(Paket paket) {
        for (int i = 0; i < getPakete().size(); i++) {
            if (getPakete().get(i).getId() == paket.getId()) {
                return new ArrayList<>(getPakete().subList(0, i + 1));
            }
        }
        throw new PaketNichtGefundenException("Das Paket ist nicht in diesem Stapel!");
    }

    public int getBreite() {
        return this.breite;
    }

    public void setBreite(int breite) {
        this.breite = breite;
    }

    public int getPosition() {
        return position.get();
    }

    public IntegerProperty positionProperty() {
        return position;
    }

    public ObservableList<Paket> getPakete() {
        return pakete;
    }

    public int getMaxHoehe() {
        return maxHoehe;
    }

    @Override
    public String toString() {
        return String.format("Stapel(%s)", pakete);
    }

}
