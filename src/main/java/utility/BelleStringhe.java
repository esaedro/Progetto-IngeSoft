package utility;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.Year;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Set;

public class BelleStringhe {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String PULISCI_TERMINALE = "\033\143";

    private static final String SPAZIO = " ";
    private static final String ACAPO = "\n";

    public static char[] generaCornice(int l) {
        l += 4;
        char[] res = new char[l];

        for (int i = 0; i < l; i++) res[i] = '-';

        return res;
    }

    public static char[] generaCornice(int l, char c) {
        l += 4;
        char[] res = new char[l];

        for (int i = 0; i < l; i++) res[i] = c;

        return res;
    }

    public static String incornicia(String s) {
        StringBuffer res = new StringBuffer();

        res.append(generaCornice(s.length()));
        res.append("\n|" + SPAZIO + s + SPAZIO + "|\n");
        res.append(generaCornice(s.length()));

        return res.toString();
    }

    public static String incolonna(String s, int larghezza) {
        StringBuffer res = new StringBuffer(larghezza);
        int numCharDaStampare = Math.min(larghezza, s.length());
        res.append(s.substring(0, numCharDaStampare));

        for (int i = s.length() + 1; i <= larghezza; i++) {
            res.append(SPAZIO);
        }

        return res.toString();
    }

    public static String centrata(String s, int larghezza) {
        StringBuffer res = new StringBuffer(larghezza);
        if (larghezza <= s.length()) res.append(s.substring(larghezza));
        else {
            int spaziPrima = (larghezza - s.length()) / 2;
            int spaziDopo = larghezza - spaziPrima - s.length();
            for (int i = 1; i <= spaziPrima; i++) res.append(SPAZIO);

            res.append(s);

            for (int i = 1; i <= spaziDopo; i++) res.append(SPAZIO);
        }
        return res.toString();
    }

    public static String ripetiChar(char elemento, int larghezza) {
        StringBuffer result = new StringBuffer(larghezza);
        for (int i = 0; i < larghezza; i++) {
            result.append(elemento);
        }
        return result.toString();
    }

    public static String rigaIsolata(String daIsolare) {
        StringBuffer result = new StringBuffer();
        result.append(ACAPO);
        result.append(daIsolare);
        result.append(ACAPO);
        return result.toString();
    }

    /**
     * Metodo che permette di creare una tabella a partire da una matrice di stringhe
     * @param matrice
     * @return
     */
    public static String tabella(String[][] matrice) {
        StringBuffer result = new StringBuffer();
        int[] larghezze = new int[matrice[0].length];
        for (int i = 0; i < matrice[0].length; i++) {
            larghezze[i] = 0;
            for (int j = 0; j < matrice.length; j++) {
                if (matrice[j][i].length() > larghezze[i] - 3) {
                    larghezze[i] = matrice[j][i].length() + 3;
                }
            }
        }
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[0].length; j++) {
                result.append(incolonna(matrice[i][j], larghezze[j]));
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static void stampaCalendario(
        Month mese,
        Year anno,
        Collection<Integer> giorniOccupati,
        Collection<Integer> giorniSelezionati
    ) {
        StringBuffer result = new StringBuffer();
        result.append(incornicia(traduciMese(mese) + " " + anno.toString()));
        result.append("\nLUN   MAR   MER   GIO   VEN   SAB   DOM\n");
        int giornoSettimana = 1;

        for (int i = 1; i <= mese.length(anno.isLeap()); i++) {
            if (giornoSettimana == 1) {
                //result.append(incolonna("", 6));
            }
            if (giorniOccupati.contains(i)) {
                result.append(incolonna("x" + String.valueOf(i) + "x", 6));
            } else if (giorniSelezionati.contains(i)) {
                result.append(incolonna("@" + String.valueOf(i) + "@", 6));
            } else {
                result.append(incolonna(String.valueOf(i), 6));
            }
            if (giornoSettimana == 7) {
                result.append("\n");
                giornoSettimana = 1;
            } else {
                giornoSettimana++;
            }
        }
        result.append("\n");

        System.out.println(result.toString());
    }

        public static String traduciGiorno(DayOfWeek giorno) {
        return switch (giorno) {
            case MONDAY -> "Lunedì";
            case TUESDAY -> "Martedì";
            case WEDNESDAY -> "Mercoledì";
            case THURSDAY -> "Giovedì";
            case FRIDAY -> "Venerdì";
            case SATURDAY -> "Sabato";
            case SUNDAY -> "Domenica";
            default -> "Giorno non valido";
        };
    }

    public static String traduciMese(Month mese) {
        return switch (mese) {
            case JANUARY -> "Gennaio";
            case FEBRUARY -> "Febbraio";
            case MARCH -> "Marzo";
            case APRIL -> "Aprile";
            case MAY -> "Maggio";
            case JUNE -> "Giugno";
            case JULY -> "Luglio";
            case AUGUST -> "Agosto";
            case SEPTEMBER -> "Settembre";
            case OCTOBER -> "Ottobre";
            case NOVEMBER -> "Novembre";
            case DECEMBER -> "Dicembre";
            default -> "Mese non valido";
        };
    }
}