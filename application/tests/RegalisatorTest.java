package RegalisatorEnterprise.application.tests;


import RegalisatorEnterprise.application.exceptions.*;
import RegalisatorEnterprise.application.logik.*;
import org.junit.Test;

/**
 * Created by Clemens on 05.07.2017
 */
public class RegalisatorTest {
    private Regal testRegal;
    private Brett testBrett;

    public Lager createTestLager() {
        Lager lager = new Lager();
        testRegal = new Regal(10, 20, lager);
        try {
            lager.addRegal(testRegal);
        } catch (RegalZuGrossException e) {
            e.printStackTrace();
        }

        testBrett = new Brett(20, 100);

        try {

            testRegal.addBrett(testBrett);

        } catch (BrettZuHochException e) {
            e.printStackTrace();
        }

        return lager;
    }

    /*
     * @Clemens: so kann man testen ob eine Exception an der richtigen Stelle auftritt
     */
    @Test(expected = PaketZuGrossException.class)
    public void paketZuHoch() throws PaketZuGrossException, PaketZuSchwerException, PaketIstUnvertraeglichException {
        Lager l = createTestLager();

        Paket testpaket =  new Paket(5, 30, 1, Farben.BRAUN, "Testpaket");

        l.setPreviewPaket(testpaket);

        testBrett.addPaket(testpaket.getId(), 0);
    }

    @Test
    public void paketNichtZuHoch() throws PaketZuGrossException, PaketZuSchwerException, PaketIstUnvertraeglichException {
        Lager l = createTestLager();

        Paket testpaket = new Paket(1, 20, 1, Farben.BRAUN, "Testpaket");

        l.setPreviewPaket(testpaket);
        testBrett.addPaket(testpaket.getId(), 0);
    }

    @Test(expected = PaketZuGrossException.class)
    public void paketZuBreit() throws PaketZuGrossException, PaketZuSchwerException, PaketIstUnvertraeglichException {
        Lager l = createTestLager();

        Paket testpaket = new Paket(20, 20, 1, Farben.BRAUN, "Testpaket");
        l.setPreviewPaket(testpaket);

        testBrett.addPaket(testpaket.getId(), 0);

    }

    @Test
    public void positionierung() throws PaketZuGrossException, PaketZuSchwerException, PaketIstUnvertraeglichException {
        Lager l = createTestLager();

        Paket testpaket = new Paket(5, 20, 1, Farben.BRAUN, "Testpaket");
        l.setPreviewPaket(testpaket);

        testBrett.addPaket(testpaket.getId(), 0);

    }

    @Test(expected = PaketIstUnvertraeglichException.class)
    public void farbenFehlschlag() throws PaketZuGrossException, PaketZuSchwerException, PaketIstUnvertraeglichException {
        Lager l = createTestLager();

        Paket testpaket = new Paket(5, 5, 1, Farben.BRAUN, "Testpaket");

        l.setPreviewPaket(testpaket);

        testBrett.addPaket(testpaket.getId(), 0);

        // 2. Paket erstellen
        testpaket = new Paket(5, 5, 1, Farben.SCHWARZ, "Testpaket Zwei");
        testpaket.addUnvertraeglichkeit(Farben.BRAUN);
        l.setPreviewPaket(testpaket);

        testBrett.addPaket(testpaket.getId(), 0);
    }

    @Test(expected = PaketZuGrossException.class)
    /**
     * Ein nicht zu lösender Fehler in der GUI zwingt uns leider dazu, den Logik - Test umzubauen;
     * Ein Paket darf der Kante nicht näher als 60px kommen, sonst ragt es heraus.
     *
     */
    public void brettRagtHeraus() throws PaketZuGrossException, PaketZuSchwerException, PaketIstUnvertraeglichException {
        Lager l = createTestLager();

        Paket testpaket = new Paket(5, 5, 1, Farben.BRAUN, "Testpaket");

        l.setPreviewPaket(testpaket);

        testBrett.addPaket(testpaket.getId(), 6);

    }
}
