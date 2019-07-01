package RegalisatorEnterprise.application.logik;

import RegalisatorEnterprise.application.exceptions.PaketNichtGefundenException;
import RegalisatorEnterprise.application.exceptions.RegalZuGrossException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Die Lager - Repräsentation. Verwaltet eine Liste von Regalen.
 */
public class Lager {
    public static final int GRID_HOEHE = 20;
    public static final int GRID_BREITE = 20;

    public static final int PAKET_MINGEWICHT = 5;
    public static final int PAKET_MAXGEWICHT = 80;
    public static final int PAKET_MAXHOEHE = 11;
    public static final int PAKET_MAXBREITE = 13;
    public static final int PAKET_MINHOEHE = 1;
    public static final int PAKET_MINBREITE = 1;
    public static BooleanProperty paketIsDragged = new SimpleBooleanProperty();
    private static IntegerProperty currentID;
    private ObservableList<Regal> regale;
    private ObservableList<Paket> vorlagen;
    private List<Paket> zwischenspeicher;
    private IntegerProperty breite;


    private Paket previewPaket;

    public Lager() {
        this.regale = FXCollections.observableArrayList();
        this.vorlagen = FXCollections.observableArrayList();

        zwischenspeicher = new ArrayList<>();
        currentID = new SimpleIntegerProperty();
        currentID.setValue(0);

        breite = new SimpleIntegerProperty(Integer.MAX_VALUE);
    }

    public static int getGridHoehe() {
        return GRID_HOEHE;
    }

    public static int getGridBreite() {
        return GRID_BREITE;
    }

    public static int getID() {
        erhoeheId();
        return currentID.getValue();
    }

    public static void erhoeheId() {
        currentID.setValue((currentID.getValue()) + 1);
    }

    public boolean addRegal(Regal regal) throws RegalZuGrossException {

        if (getBelegterPlatz() + regal.getBreite() > getBreite()) {
            throw new RegalZuGrossException("Regal ist zu groß!");
        }
        regal.setPlatziert(true);
        getRegale().add(regal);

        return true;
    }

    public ObservableList<Regal> getRegale() {
        return regale;
    }


    /**
     * Returned das Paket, dass die entsprechende ID enthält.
     * <p>
     * Die ID 0 ist IMMER eindeutig dem previewPaket zuzuordnen. Daher wird dieses bei Anforderung kopiert,
     * um neu eingefügte Pakete unabhängig vom previewPaket zu halten.
     */
    public Paket getPaketById(int id) {
        if (id == 0)
            id = copyPreviewPaket().getId();

        for (Paket p : zwischenspeicher)
            if (p.getId() == id) {
                return p;
            }
        for (Regal regal : getRegale())
            for (Brett brett : regal.getBretter())
                for (Stapel stapel : brett.getStapel())
                    for (Paket paket : stapel.getPakete())
                        if (paket.getId() == id)
                            return paket;

        throw new PaketNichtGefundenException("Paket nicht gefunden!");
    }


    /**
     *
     */
    public List<Paket> getPaketByName(String name) {
        ObservableList<Paket> pakete = FXCollections.observableArrayList();

        for (Regal regal : getRegale())
            for (Brett brett : regal.getBretter())
                for (Stapel stapel : brett.getStapel())
                    for (Paket paket : stapel.getPakete())
                        if (paket.getName().equals(name))
                            pakete.add(paket);
        if (!pakete.isEmpty()) {
            return pakete;
        }
        throw new PaketNichtGefundenException("Paket nicht gefunden!");
    }

    /**
     * Liefert ein Paket mit der entsprechenden ID und alle daraufliegenden Pakete.
     */
    public List<Paket> getPaketListById(int id) {
        if (id == 0)
            id = copyPreviewPaket().getId();

        for (Regal regal : getRegale())
            for (Brett brett : regal.getBretter())
                for (Stapel stapel : brett.getStapel())
                    for (Paket paket : stapel.getPakete())
                        if (paket.getId() == id)
                            return stapel.getSubStapel(paket);

        for (Paket p : zwischenspeicher)
            if (p.getId() == id) {
                return Arrays.asList(p);
            }

        throw new PaketNichtGefundenException("Paket nicht gefunden!");
    }

    /**
     * Löscht Paket aber schreibt es in den Zwischenspeicher. So kann es noch gefunden werden, falls das Paket nur
     * verschoben werden sollte.
     */
    public void removePaket(int id) {
        for (Regal regal : getRegale())
            for (Brett brett : regal.getBretter())
                for (Stapel stapel : brett.getStapel())
                    for (int i = 0; i < stapel.getPakete().size(); i++) {
                        Paket p = stapel.getPakete().get(i);
                        if (p.getId() == id) {
                            zwischenspeicher.add(p);
                            stapel.erhoeheTragkraft(p);
                            stapel.getPakete().remove(i);
                            return;
                        }
                    }

    }

    public ObservableList<Paket> getVorlagen() {
        return this.vorlagen;
    }

    public void addVorlage(Paket paket) {
        //Hier wird ein neues Paket erstellt
        Paket p = paket.clone();

        this.vorlagen.add(p);
    }

    public void deleteVorlage(Paket paket) {
        this.vorlagen.remove(paket);
    }

    public void addToZwischenspeicher(Paket paket) {
        if (!zwischenspeicher.contains(paket)) {
            zwischenspeicher.add(paket);
        } else {
            zwischenspeicher.remove(paket);
            zwischenspeicher.add(paket);
        }
    }

    public Paket getPreviewPaket() {
        return previewPaket;
    }

    public void setPreviewPaket(Paket previewPaket) {
        previewPaket.setId(0);
        this.previewPaket = previewPaket;
    }

    public Paket copyPreviewPaket() {
        Paket p = previewPaket.clone();
        addToZwischenspeicher(p);
        return p;
    }

    @Override
    public String toString() {
        return String.format("Lager(%s)", regale);
    }

    public int getBelegterPlatz() {
        int belegterPlatz = 0;

        for (Regal r : getRegale())
            belegterPlatz += r.getBreite();

        return belegterPlatz;
    }

    public int getBreite() {
        return breite.get();
    }

    public void setBreite(int breite) {
        this.breite.set(breite);
    }

}

