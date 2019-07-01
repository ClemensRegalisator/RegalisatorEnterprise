package RegalisatorEnterprise.application.logik;


import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

/**
 * Verwaltet eine Liste aus  Farben-Konstanten als Grundlage für die Paket-Färbung.
 */
public class Farben {
    public static final Color ROT = Color.web("#FB5151");
    public static final Color TURKIS = Color.web("#FF753C");
    public static final Color ORANGE = Color.web("#FD86BC");
    public static final Color PINK = Color.web("#EDC12A");
    public static final Color GELB = Color.web("#90D269");
    public static final Color GRUEN = Color.web("#5B82B8");
    public static final Color DUNKELBLAU = Color.web("#8E77A7");
    public static final Color LILA = Color.web("#83D8FE");
    public static final Color GRAU = Color.web("#CDC7C9");
    public static final Color BRAUN = Color.web("#7A5C5E");
    public static final Color WEISS = Color.web("#DADADA");
    public static final Color SCHWARZ = Color.web("#26252A");

    public static List<Color> getList() {
        return Arrays.asList(ROT, TURKIS, ORANGE, PINK, GELB, GRUEN, DUNKELBLAU, LILA, GRAU, BRAUN, WEISS, SCHWARZ);
    }

    public static String hex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));

    }
}

