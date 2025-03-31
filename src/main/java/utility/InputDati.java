package utility;

import static utility.BelleStringhe.ANSI_GREEN;
import static utility.BelleStringhe.ANSI_RED;
import static utility.BelleStringhe.ANSI_RESET;
import static utility.BelleStringhe.PULISCI_TERMINALE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

public class InputDati {

    private static Scanner lettore = creaScanner();

    private static final String ERRORE_FORMATO = "Il dato inserito non e' nel formato corretto";
    private static final String ERRORE_MINIMO = "È richiesto un valore maggiore o uguale a ";
    private static final String ERRORE_STRINGA_VUOTA = "Non hai inserito alcun carattere";
    private static final String ERRORE_MASSIMO = "È richiesto un valore minore o uguale a ";
    private static final String MESSAGGIO_AMMISSIBILI = ANSI_RED + "I caratteri ammissibili sono: ";
    // private final static String ERRORE_FORMATO = ANSI_RED + "Il dato inserito non e' nel formato corretto" + ANSI_RESET;
    // private final static String ERRORE_MINIMO = ANSI_RED + "È richiesto un valore maggiore o uguale a " + ANSI_RESET;
    // private final static String ERRORE_STRINGA_VUOTA = ANSI_RED + "Non hai inserito alcun carattere" + ANSI_RESET;
    // private final static String ERRORE_MASSIMO = ANSI_RED + "È richiesto un valore minore o uguale a " + ANSI_RESET;
    // private final static String MESSAGGIO_AMMISSIBILI = ANSI_RED + "I caratteri ammissibili sono: ";
    private static final char[] CARATTERI_AMMISSIBILI = { 'S', 's', 'N', 'n' };

    /*
     * I metodi che contengono il parametro formale "String messaggioErrore" sono metodi
     * in cui è possibile personalizzare il messaggio di errore nel caso in cui l'input non sia del tipo richiesto
     */

    private static Scanner creaScanner() {
        Scanner creato = new Scanner(System.in);
        creato.useDelimiter(System.getProperty("line.separator"));
        return creato;
    }

    /**
     * Legge una stringa da tastiera
     * @param messaggioInserimento
     * @return la stringa letta
     */
    public static String leggiStringa(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        System.out.print(messaggioInserimento);
        String lettura = lettore.next();
        // System.out.print(ANSI_RESET);
        return lettura;
    }

    /**
     * Legge una stringa non vuota da tastiera
     * @param messaggioInserimento
     * @return la stringa letta
     */
    public static String leggiStringaNonVuota(String messaggioInserimento) {
        boolean finito = false;
        String lettura = null;
        do {
            lettura = leggiStringa(messaggioInserimento);
            lettura = lettura.trim();
            if (lettura.length() > 0) finito = true;
            else System.out.println(ERRORE_STRINGA_VUOTA);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return lettura;
    }

    /**
     * Legge una stringa non vuota da tastiera
     * @param messaggioInserimento
     * @param messaggioErrore
     * @return la stringa letta
     */
    public static String leggiStringaNonVuota(String messaggioInserimento, String messaggioErrore) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        String lettura = null;
        do {
            lettura = leggiStringa(messaggioInserimento);
            lettura = lettura.trim();
            if (lettura.length() > 0) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return lettura;
    }

    public static char leggiChar(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            System.out.print(messaggioInserimento);
            String lettura = lettore.next();
            if (lettura.length() > 0) {
                valoreLetto = lettura.charAt(0);
                finito = true;
            } else {
                System.out.println(ERRORE_STRINGA_VUOTA);
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static char leggiChar(String messaggioInserimento, String messaggioErrore) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            System.out.print(messaggioInserimento);
            String lettura = lettore.next();
            if (lettura.length() > 0) {
                valoreLetto = lettura.charAt(0);
                finito = true;
            } else {
                System.out.println(messaggioErrore);
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static char leggiUpperChar(String messaggioInserimento, char[] ammissibili) {
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            valoreLetto = leggiChar(messaggioInserimento);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (String.valueOf(ammissibili).indexOf(valoreLetto) != -1) finito = true;
            else System.out.println(MESSAGGIO_AMMISSIBILI + ammissibili.toString());
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static char leggiUpperChar(
        String messaggioInserimento,
        char[] ammissibili,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        char valoreLetto = '\0';
        do {
            valoreLetto = leggiChar(messaggioInserimento);
            valoreLetto = Character.toUpperCase(valoreLetto);
            if (String.valueOf(ammissibili).indexOf(valoreLetto) != -1) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static int leggiIntero(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        boolean finito = false;
        int valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextInt();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
                lettore.next();
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static int leggiIntero(String messaggioInserimento, String messaggioErrore) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        int valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextInt();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(messaggioErrore);
                lettore.next();
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static int leggiInteroPositivo(String messaggioInserimento) {
        return leggiInteroMin(messaggioInserimento, 1);
    }

    public static int leggiInteroPositivo(String messaggioInserimento, String messaggioErrore) {
        return leggiInteroMin(messaggioInserimento, 1, messaggioErrore);
    }

    public static int leggiInteroNonNegativo(String messaggioInserimento) {
        return leggiInteroMin(messaggioInserimento, 0);
    }

    public static int leggiInteroNonNegativo(String messaggioInserimento, String messaggioErrore) {
        return leggiInteroMin(messaggioInserimento, 0, messaggioErrore);
    }

    public static int leggiInteroMin(String messaggioInserimento, int minimo) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(ERRORE_MINIMO + minimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static int leggiInteroMin(
        String messaggioInserimento,
        int minimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static int leggiInteroMinMax(String messaggioInserimento, int minimo, int massimo) {
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggioInserimento);
            if (valoreLetto >= minimo && valoreLetto <= massimo) finito = true;
            else if (valoreLetto < minimo) System.out.println(ERRORE_MINIMO + minimo);
            else System.out.println(ERRORE_MASSIMO + massimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static int leggiInteroMinMax(
        String messaggioInserimento,
        int minimo,
        int massimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        int valoreLetto = 0;
        do {
            valoreLetto = leggiIntero(messaggioInserimento);
            if (valoreLetto >= minimo && valoreLetto <= massimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static short leggiShort(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        boolean finito = false;
        short valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextShort();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
                lettore.next();
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static short leggiShort(String messaggioInserimento, String messaggioErrore) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        short valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextShort();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(messaggioErrore);
                lettore.next();
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static short leggiShortPositivo(String messaggioInserimento) {
        return leggiShortMin(messaggioInserimento, 1);
    }

    public static short leggiShortPositivo(String messaggioInserimento, String messaggioErrore) {
        return leggiShortMin(messaggioInserimento, 1, messaggioErrore);
    }

    public static short leggiShortNonNegativo(String messaggioInserimento) {
        return leggiShortMin(messaggioInserimento, 0);
    }

    public static short leggiShortNonNegativo(String messaggioInserimento, String messaggioErrore) {
        return leggiShortMin(messaggioInserimento, 0, messaggioErrore);
    }

    public static short leggiShortMin(String messaggioInserimento, int minimo) {
        boolean finito = false;
        short valoreLetto = 0;
        do {
            valoreLetto = leggiShort(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(ERRORE_MINIMO + minimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static short leggiShortMin(
        String messaggioInserimento,
        int minimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        short valoreLetto = 0;
        do {
            valoreLetto = leggiShort(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static short leggiShortMinMax(String messaggioInserimento, int minimo, int massimo) {
        boolean finito = false;
        short valoreLetto = 0;
        do {
            valoreLetto = leggiShort(messaggioInserimento);
            if (valoreLetto >= minimo && valoreLetto <= massimo) finito = true;
            else if (valoreLetto < minimo) System.out.println(ERRORE_MINIMO + minimo);
            else System.out.println(ERRORE_MASSIMO + massimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static short leggiShortMinMax(
        String messaggioInserimento,
        int minimo,
        int massimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        short valoreLetto = 0;
        do {
            valoreLetto = leggiShort(messaggioInserimento);
            if (valoreLetto >= minimo && valoreLetto <= massimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static long leggiLong(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        boolean finito = false;
        long valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextLong();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
                lettore.next();
            }
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static long leggiLong(String messaggioInserimento, String messaggioErrore) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        long valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextLong();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(messaggioErrore);
                lettore.next();
            }
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static long leggiLongPositivo(String messaggioInserimento) {
        return leggiLongMin(messaggioInserimento, 1);
    }

    public static long leggiLongPositivo(String messaggioInserimento, String messaggioErrore) {
        return leggiLongMin(messaggioInserimento, 1, messaggioErrore);
    }

    public static long leggiLongNonNegativo(String messaggioInserimento) {
        return leggiLongMin(messaggioInserimento, 0);
    }

    public static long leggiLongNonNegativo(String messaggioInserimento, String messaggioErrore) {
        return leggiLongMin(messaggioInserimento, 0, messaggioErrore);
    }

    public static long leggiLongMin(String messaggioInserimento, int minimo) {
        boolean finito = false;
        long valoreLetto = 0;
        do {
            valoreLetto = leggiLong(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(ERRORE_MINIMO + minimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static long leggiLongMin(
        String messaggioInserimento,
        int minimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        long valoreLetto = 0;
        do {
            valoreLetto = leggiLong(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static long leggiLongMinMax(String messaggioInserimento, int minimo, int massimo) {
        boolean finito = false;
        long valoreLetto = 0;
        do {
            valoreLetto = leggiLong(messaggioInserimento);
            if (valoreLetto >= minimo && valoreLetto <= massimo) finito = true;
            else if (valoreLetto < minimo) System.out.println(ERRORE_MINIMO + minimo);
            else System.out.println(ERRORE_MASSIMO + massimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static long leggiLongMinMax(
        String messaggioInserimento,
        int minimo,
        int massimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        long valoreLetto = 0;
        do {
            valoreLetto = leggiLong(messaggioInserimento);
            if (valoreLetto >= minimo && valoreLetto <= massimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static double leggiDouble(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        boolean finito = false;
        double valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextDouble();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(ERRORE_FORMATO);
                lettore.next();
            }
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static double leggiDouble(String messaggioInserimento, String messaggioErrore) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN;
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        double valoreLetto = 0;
        do {
            System.out.print(messaggioInserimento);
            try {
                valoreLetto = lettore.nextDouble();
                finito = true;
            } catch (InputMismatchException e) {
                System.out.println(messaggioErrore);
                lettore.next();
            }
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static double leggiDoubleConMinimo(String messaggioInserimento, double minimo) {
        boolean finito = false;
        double valoreLetto = 0;
        do {
            valoreLetto = leggiDouble(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(ERRORE_MINIMO + minimo);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    public static double leggiDoubleConMinimo(
        String messaggioInserimento,
        double minimo,
        String messaggioErrore
    ) {
        // messaggioErrore = ANSI_RED + messaggioErrore + ANSI_RESET;
        boolean finito = false;
        double valoreLetto = 0;
        do {
            valoreLetto = leggiDouble(messaggioInserimento);
            if (valoreLetto >= minimo) finito = true;
            else System.out.println(messaggioErrore);
        } while (!finito);

        // System.out.print(ANSI_RESET);
        return valoreLetto;
    }

    /**
     * Richiede conferma all'utente tramite l'inserimento di un carattere
     * @param messaggioInserimento
     * @return true se l'utente ha inserito 's' o 'S', false altrimenti
     */
    public static boolean conferma(String messaggioInserimento) {
        // messaggioInserimento = ANSI_RESET + messaggioInserimento + ANSI_GREEN + " [" + CARATTERI_AMMISSIBILI[0] + "/" + CARATTERI_AMMISSIBILI[2] + "]: ";
        char valoreLetto = leggiUpperChar(
            messaggioInserimento,
            CARATTERI_AMMISSIBILI,
            "Risposta non valida"
        );

        System.out.print(ANSI_RESET);
        return (valoreLetto == CARATTERI_AMMISSIBILI[0] || valoreLetto == CARATTERI_AMMISSIBILI[1])
            ? true
            : false;
    }

    /**
     * Richiede all'utente di selezionare un elemento da una lista. Stampa la lista
     * con i numeri da 1 a n, e accanto ad ogni elemento, un carattere '*' se
     * l'elemento è selezionato, un carattere ' ' se non lo è.
     *
     * L'utente deve inserire un numero compreso tra 1 e n per selezionare l'elemento
     * corrispondente, oppure 'q' per terminare. Se l'utente inserisce un numero
     * non valido, viene stampato un errore e richiesto nuovamente l'inserimento.
     *
     * Se l'utente seleziona un elemento, esso viene deselezionato se era già
     * selezionato, oppure selezionato se non lo era.
     *
     * @param messaggioInserimento messaggio da stampare prima di richiedere
     *                              l'inserimento all'utente
     * @param lista lista di elementi da selezionare
     * @param toString funzione che trasforma ogni elemento in una stringa
     * @return l'elemento selezionato, se l'utente ha selezionato un elemento, null
     *         altrimenti
     */
    public static <T> T selezionaUnoDaLista(
        String messaggioInserimento,
        Iterable<T> lista,
        Function<T, String> toString
    ) {
        // System.out.print(ANSI_RESET);
        String selezionato = "*";
        // selezionato = ANSI_GREEN + "*" + ANSI_RESET;
        char nonSelezionato = ' ';

        ArrayList<T> listaFittizia = new ArrayList<>();
        for (T t : lista) {
            listaFittizia.add(t);
        }

        HashMap<T, Boolean> selezionati = new HashMap<>();
        for (T t : lista) {
            selezionati.put(t, false);
        }

        boolean finito = false;
        String carattereInserito;
        String riga;
        T elementoSelezionato;
        String erroreInterno = "";

        while (!finito) {
            // System.out.print(PULISCI_TERMINALE); // Probabilmente non funzionerà su Windows, in caso è possibile sostituirla con molti println
            if (!erroreInterno.isEmpty()) {
                // erroreInterno = ANSI_RED + erroreInterno + ANSI_RESET;
                System.out.println(erroreInterno);
            }
            System.out.println(messaggioInserimento);

            for (int i = 0; i < listaFittizia.size(); i++) {
                T elemento = listaFittizia.get(i);
                riga = String.format(
                    "%d) [%s] %s",
                    i + 1,
                    selezionati.get(elemento) ? selezionato : nonSelezionato,
                    toString.apply(elemento)
                );
                System.out.println(riga);
            }

            carattereInserito = leggiStringa("('q' per terminare) > ");
            try {
                if (
                    Integer.parseInt(carattereInserito) > 0 &&
                    Integer.parseInt(carattereInserito) <= listaFittizia.size()
                ) {
                    elementoSelezionato = listaFittizia.get(
                        Integer.parseInt(carattereInserito) - 1
                    );
                    selezionati.replace(elementoSelezionato, !selezionati.get(elementoSelezionato));
                    if (selezionati.containsValue(true)) {
                        selezionati.forEach((k, v) -> {
                            selezionati.replace(k, false);
                        });
                        selezionati.replace(
                            elementoSelezionato,
                            !selezionati.get(elementoSelezionato)
                        );
                        erroreInterno = "";
                    }
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            } catch (NumberFormatException e) {
                if (carattereInserito.equals("q")) {
                    finito = true;
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            }
        }

        // System.out.print(ANSI_RESET);
        return selezionati
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue())
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /**
     * Richiede all'utente di selezionare un insieme di elementi da una lista. Stampa la lista
     * con i numeri da 1 a n, e accanto ad ogni elemento, un carattere '*' se
     * l'elemento è selezionato, un carattere ' ' se non lo è.
     *
     * L'utente deve inserire un numero compreso tra 1 e n per selezionare l'elemento
     * corrispondente, oppure 'q' per terminare. Se l'utente inserisce un numero
     * non valido, viene stampato un errore e richiesto nuovamente l'inserimento.
     *
     * Se l'utente seleziona un elemento, esso viene deselezionato se era già
     * selezionato, oppure selezionato se non lo era.
     *
     * @param messaggioInserimento messaggio da stampare prima di richiedere
     *                              l'inserimento all'utente
     * @param lista lista di elementi da selezionare
     * @param toString funzione che trasforma ogni elemento in una stringa
     * @param minimo minimo numero di elementi che l'utente deve selezionare
     * @param massimo massimo numero di elementi che l'utente può selezionare
     * @return l'insieme degli elementi selezionati
     */
    public static <T> Set<T> selezionaPiuDaLista(
        String messaggioInserimento,
        Iterable<T> lista,
        Function<T, String> toString,
        int minimo,
        int massimo
    ) {
        // System.out.print(ANSI_RESET);
        String selezionato = "*";
        // selezionato = ANSI_GREEN + "*" + ANSI_RESET;
        char nonSelezionato = ' ';

        ArrayList<T> listaFittizia = new ArrayList<>();
        for (T t : lista) {
            listaFittizia.add(t);
        }

        HashMap<T, Boolean> selezionati = new HashMap<>();
        for (T t : lista) {
            selezionati.put(t, false);
        }

        boolean finito = false;
        String carattereInserito;
        String riga;
        T elementoSelezionato;
        String erroreInterno = "";

        while (!finito) {
            // System.out.print(PULISCI_TERMINALE); // Probabilmente non funzionerà su Windows, in caso è possibile sostituirla con molti println
            if (!erroreInterno.isEmpty()) {
                // erroreInterno = ANSI_RED + erroreInterno + ANSI_RESET;
                System.out.println(erroreInterno);
            }
            System.out.println(
                messaggioInserimento + " (minimo: " + minimo + ", massimo: " + massimo + ")"
            );

            for (int i = 0; i < listaFittizia.size(); i++) {
                T elemento = listaFittizia.get(i);
                riga = String.format(
                    "%d) [%s] %s",
                    i + 1,
                    selezionati.get(elemento) ? selezionato : nonSelezionato,
                    toString.apply(elemento)
                );
                System.out.println(riga);
            }

            carattereInserito = leggiStringa("('q' per terminare) > ");
            try {
                if (
                    Integer.parseInt(carattereInserito) > 0 &&
                    Integer.parseInt(carattereInserito) <= listaFittizia.size()
                ) {
                    elementoSelezionato = listaFittizia.get(
                        Integer.parseInt(carattereInserito) - 1
                    );
                    if (!selezionati.get(elementoSelezionato)) {
                        if (selezionati.values().stream().filter(v -> v).count() >= massimo) {
                            erroreInterno = "Hai già selezionato il massimo numero di elementi";
                        } else {
                            selezionati.replace(
                                elementoSelezionato,
                                !selezionati.get(elementoSelezionato)
                            );
                            erroreInterno = "";
                        }
                    } else {
                        selezionati.replace(
                            elementoSelezionato,
                            !selezionati.get(elementoSelezionato)
                        );
                        erroreInterno = "";
                    }
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            } catch (NumberFormatException e) {
                if (carattereInserito.equals("q")) {
                    if (selezionati.values().stream().filter(v -> v).count() < minimo) {
                        erroreInterno = "Devi selezionare almeno " + minimo + " elementi";
                    } else {
                        finito = true;
                    }
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            }
        }

        // System.out.print(ANSI_RESET);
        return selezionati
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue())
            .map(Map.Entry::getKey)
            .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Richiede all'utente di selezionare un elemento da un elenco di enumerazioni.
     * Stampa l'elenco con i numeri da 1 a n, e accanto ad ogni elemento, un
     * carattere '*' se l'elemento è selezionato, un carattere ' ' se non lo è.
     *
     * L'utente deve inserire un numero compreso tra 1 e n per selezionare l'elemento
     * corrispondente, oppure 'q' per terminare. Se l'utente inserisce un numero
     * non valido, viene stampato un errore e richiesto nuovamente l'inserimento.
     *
     * Se l'utente seleziona un elemento, esso viene deselezionato se era già
     * selezionato, oppure selezionato se non lo era. Alla fine, restituisce
     * l'elemento selezionato o null se nessun elemento è stato selezionato.
     *
     * @param messaggioInserimento il messaggio da stampare prima di richiedere
     *                             l'inserimento all'utente
     * @param lista l'elenco degli elementi enumerati da cui selezionare
     * @return l'elemento selezionato, se l'utente ha selezionato un elemento, null
     *         altrimenti
     */

    public static <E extends Enum<E>> E selezionaUnEnum(String messaggioInserimento, E[] lista) {
        // System.out.print(ANSI_RESET);
        String selezionato = "*";
        // selezionato = ANSI_GREEN + "*" + ANSI_RESET;
        char nonSelezionato = ' ';

        EnumMap<E, Boolean> selezionati = new EnumMap<>(lista[0].getDeclaringClass());
        for (E e : lista) {
            selezionati.put(e, false);
        }

        boolean finito = false;
        String carattereInserito;
        String riga;
        E elementoSelezionato;
        String erroreInterno = "";

        while (!finito) {
            // System.out.print(PULISCI_TERMINALE); // Probabilmente non funzionerà su Windows, in caso è possibile sostituirla con molti println
            if (!erroreInterno.isEmpty()) {
                // erroreInterno = ANSI_RED + erroreInterno + ANSI_RESET;
                System.out.println(erroreInterno);
            }
            System.out.println(messaggioInserimento);

            for (int i = 0; i < lista.length; i++) {
                E elemento = lista[i];
                riga = String.format(
                    "%d) [%s] %s",
                    i + 1,
                    selezionati.get(elemento) ? selezionato : nonSelezionato,
                    elemento.toString()
                );
                System.out.println(riga);
            }

            carattereInserito = leggiStringa("('q' per terminare) > ");
            try {
                if (
                    Integer.parseInt(carattereInserito) > 0 &&
                    Integer.parseInt(carattereInserito) <= lista.length
                ) {
                    elementoSelezionato = lista[Integer.parseInt(carattereInserito) - 1];
                    selezionati.replace(elementoSelezionato, !selezionati.get(elementoSelezionato));
                    if (selezionati.containsValue(true)) {
                        selezionati.forEach((k, v) -> {
                            selezionati.replace(k, false);
                        });
                        selezionati.replace(
                            elementoSelezionato,
                            !selezionati.get(elementoSelezionato)
                        );
                        erroreInterno = "";
                    }
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            } catch (NumberFormatException e) {
                if (carattereInserito.equals("q")) {
                    finito = true;
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            }
        }

        // System.out.print(ANSI_RESET);
        return selezionati
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue())
            .findFirst()
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /**
     * Richiede all'utente di selezionare un numero di elementi tra minimo e massimo
     * dalla lista di enumerati. Stampa la lista con i numeri da 1 a n, e accanto ad
     * ogni elemento, un carattere '*' se l'elemento è selezionato, un carattere ' '
     * se non lo è.
     *
     * L'utente deve inserire un numero compreso tra 1 e n per selezionare l'elemento
     * corrispondente, oppure 'q' per terminare. Se l'utente inserisce un numero
     * non valido, viene stampato un errore e richiesto nuovamente l'inserimento.
     *
     * Se l'utente seleziona un elemento, esso viene deselezionato se era già
     * selezionato, oppure selezionato se non lo era.
     *
     * @param messaggioInserimento messaggio da stampare prima di richiedere
     *                              l'inserimento all'utente
     * @param lista lista di elementi enumerati da cui selezionare
     * @param minimo numero minimo di elementi da selezionare
     * @param massimo numero massimo di elementi da selezionare
     * @return il set di elementi selezionati
     */
    public static <E extends Enum<E>> Set<E> selezionaPiuEnum(
        String messaggioInserimento,
        E[] lista,
        int minimo,
        int massimo
    ) {
        // System.out.print(ANSI_RESET);
        String selezionato = "*";
        // selezionato = ANSI_GREEN + "*" + ANSI_RESET;
        char nonSelezionato = ' ';

        EnumMap<E, Boolean> selezionati = new EnumMap<>(lista[0].getDeclaringClass());
        for (E e : lista) {
            selezionati.put(e, false);
        }

        boolean finito = false;
        String carattereInserito;
        String riga;
        E elementoSelezionato;
        String erroreInterno = "";

        while (!finito) {
            // System.out.print(PULISCI_TERMINALE); // Probabilmente non funzionerà su Windows, in caso è possibile sostituirla con molti println
            if (!erroreInterno.isEmpty()) {
                // erroreInterno = ANSI_RED + erroreInterno + ANSI_RESET;
                System.out.println(erroreInterno);
            }
            System.out.println(
                messaggioInserimento + " (minimo: " + minimo + ", massimo: " + massimo + ")"
            );

            for (int i = 0; i < lista.length; i++) {
                E elemento = lista[i];
                riga = String.format(
                    "%d) [%s] %s",
                    i + 1,
                    selezionati.get(elemento) ? selezionato : nonSelezionato,
                    elemento.toString()
                );
                System.out.println(riga);
            }

            carattereInserito = leggiStringa("('q' per terminare) > ");
            try {
                if (
                    Integer.parseInt(carattereInserito) > 0 &&
                    Integer.parseInt(carattereInserito) <= lista.length
                ) {
                    elementoSelezionato = lista[Integer.parseInt(carattereInserito) - 1];
                    if (!selezionati.get(elementoSelezionato)) {
                        if (selezionati.values().stream().filter(v -> v).count() >= massimo) {
                            erroreInterno = "Hai già selezionato il massimo numero di elementi";
                        } else {
                            selezionati.replace(
                                elementoSelezionato,
                                !selezionati.get(elementoSelezionato)
                            );
                            erroreInterno = "";
                        }
                    } else {
                        selezionati.replace(
                            elementoSelezionato,
                            !selezionati.get(elementoSelezionato)
                        );
                        erroreInterno = "";
                    }
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            } catch (NumberFormatException e) {
                if (carattereInserito.equals("q")) {
                    if (selezionati.values().stream().filter(v -> v).count() < minimo) {
                        erroreInterno = "Devi selezionare almeno " + minimo + " elementi";
                    } else {
                        finito = true;
                    }
                } else {
                    erroreInterno = "Inserimento non valido";
                }
            }
        }

        // System.out.print(ANSI_RESET);
        return selezionati
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue())
            .map(Map.Entry::getKey)
            .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Legge una data da tastiera nel formato "dd/MM/yyyy".
     * Continua a richiedere l'inserimento finché non viene inserita una data valida.
     *
     * @param messaggioInserimento il messaggio da visualizzare all'utente prima dell'inserimento
     * @return la data letta come oggetto Calendar
     */
    public static Calendar leggiData(String messaggioInserimento, String separatore) {
        boolean finito = false;
        Calendar data = Calendar.getInstance();
        SimpleDateFormat formattatore = new SimpleDateFormat("dd" + separatore + "MM");
        formattatore.setLenient(false); // Impedisce di accettare date non valide
        do {
            messaggioInserimento = messaggioInserimento + "\n> ";
            // messaggioInserimento += ANSI_GREEN;
            System.out.print(messaggioInserimento);
            String dataInserita = lettore.next();
            // System.out.print(ANSI_RESET);
            try {
                data.setTime(formattatore.parse(dataInserita));
                int giornoInserito = data.get(Calendar.DAY_OF_MONTH);
                if (giornoInserito != Integer.parseInt(dataInserita.split(separatore)[0])) {
                    String messaggioErrore = "Data non valida";
                    // messaggioErrore = ANSI_RED + "Data non valida" + ANSI_RESET;
                    throw new ParseException(messaggioErrore, 0);
                } else {
                    finito = true;
                }
            } catch (ParseException e) {
                System.out.println(ERRORE_FORMATO);
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return data;
    }

    /**
     * Legge una data da tastiera nel formato "dd/MM/yyyy", e verifica che sia
     * compresa tra minimo e massimo. Continua a richiedere l'inserimento finché
     * non viene inserita una data valida.
     *
     * @param messaggioInserimento il messaggio da visualizzare all'utente prima
     *                              dell'inserimento
     * @param minimo la data minima che l'utente deve inserire
     * @param massimo la data massima che l'utente può inserire
     * @return la data letta come oggetto Calendar
     */
    public static Calendar leggiDataMinMax(
        String messaggioInserimento,
        String separatore,
        Calendar minimo,
        Calendar massimo
    ) {
        // System.out.print(ANSI_RESET);
        int anno = minimo.get(Calendar.YEAR);
        boolean finito = false;
        Calendar data = Calendar.getInstance();
        SimpleDateFormat formattatore = new SimpleDateFormat("dd" + separatore + "MM");
        formattatore.setLenient(false); // Impedisce di accettare date non valide

        String messaggioInserimento2 =
            messaggioInserimento +
            " (minimo: " +
            formattatore.format(minimo.getTime()) +
            ", massimo: " +
            formattatore.format(massimo.getTime()) +
            ")";
        do {
            data = leggiData(messaggioInserimento2, separatore);
            data.set(Calendar.YEAR, anno);
            Calendar minimoFittizio = (Calendar) minimo.clone();
            minimoFittizio.add(Calendar.DAY_OF_MONTH, -1);
            if (data.after(minimoFittizio) && data.before(massimo)) finito = true;
            else if (data.before(minimo)) System.out.println(
                ERRORE_MINIMO + formattatore.format(minimo.getTime())
            );
            // System.out.println(ERRORE_MINIMO + ANSI_RED + formattatore.format(minimo.getTime()) + ANSI_RESET);
            else System.out.println(ERRORE_MASSIMO + formattatore.format(massimo.getTime()));
            // System.out.println(ERRORE_MASSIMO + ANSI_RED + formattatore.format(massimo.getTime()) + ANSI_RESET);
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return data;
    }

    /**
     * Legge un'ora da tastiera nel formato "HH" + separatore + "mm".
     * Continua a richiedere l'inserimento finché non viene inserita un'ora valida.
     *
     * @param messaggioInserimento il messaggio da visualizzare all'utente prima dell'inserimento
     * @param separatore il carattere separatore tra le due cifre dell'ora
     * @return l'ora letta come oggetto Calendar
     */
    public static Calendar leggiOra(String messaggioInserimento, String separatore) {
        boolean finito = false;
        Calendar ora = Calendar.getInstance();
        SimpleDateFormat formattatore = new SimpleDateFormat("HH" + separatore + "mm");
        formattatore.setLenient(false); // Impedisce di accettare ore non valide
        do {
            messaggioInserimento = messaggioInserimento + "\n> ";
            // messaggioInserimento += ANSI_GREEN;
            System.out.print(messaggioInserimento);
            String oraInserita = lettore.next();
            // System.out.print(ANSI_RESET);
            try {
                ora.setTime(formattatore.parse(oraInserita));
                if (
                    ora.get(Calendar.HOUR_OF_DAY) !=
                    Integer.parseInt(oraInserita.split(separatore)[0])
                ) {
                    String messaggioErrore = "Ora non valida";
                    // messaggioErrore = ANSI_RED + "Data non valida" + ANSI_RESET;
                    throw new ParseException(messaggioErrore, 0);
                } else if (
                    ora.get(Calendar.MINUTE) != Integer.parseInt(oraInserita.split(separatore)[1])
                ) {
                    String messaggioErrore = "Ora non valida";
                    // messaggioErrore = ANSI_RED + "Data non valida" + ANSI_RESET;
                    throw new ParseException(messaggioErrore, 0);
                } else {
                    finito = true;
                }
            } catch (ParseException e) {
                System.out.println(ERRORE_FORMATO);
            }
        } while (!finito);
        // System.out.print(ANSI_RESET);
        return ora;
    }

    /**
     * Legge un'ora da tastiera nel formato "HH" + separatore + "mm", e verifica che sia
     * compresa tra minimo e massimo. Continua a richiedere l'inserimento finché
     * non viene inserita un'ora valida.
     *
     * @param messaggioInserimento il messaggio da visualizzare all'utente prima
     *                              dell'inserimento
     * @param separatore il carattere separatore tra le due cifre dell'ora
     * @param minimo l'ora minima che l'utente deve inserire
     * @param massimo l'ora massima che l'utente può inserire
     * @return l'ora letta come oggetto Calendar
     */
    public static Calendar leggiOraMinMax(
        String messaggioInserimento,
        String separatore,
        Calendar minimo,
        Calendar massimo
    ) {
        int giorno = minimo.get(Calendar.DAY_OF_MONTH);
        int mese = minimo.get(Calendar.MONTH);
        int anno = minimo.get(Calendar.YEAR);
        boolean finito = false;
        Calendar ora = Calendar.getInstance();
        SimpleDateFormat formattatore = new SimpleDateFormat("HH" + separatore + "mm");
        formattatore.setLenient(false); // Impedisce di accettare ore non valide
        String messaggioInserimento2 =
            messaggioInserimento +
            " (minimo: " +
            formattatore.format(minimo.getTime()) +
            ", massimo: " +
            formattatore.format(massimo.getTime()) +
            ")";
        do {
            ora = leggiOra(messaggioInserimento2, separatore);
            ora.set(Calendar.DAY_OF_MONTH, giorno);
            ora.set(Calendar.MONTH, mese);
            ora.set(Calendar.YEAR, anno);
            Calendar minimoFittizio = (Calendar) minimo.clone();
            minimoFittizio.add(Calendar.MINUTE, -1);
            if (ora.after(minimoFittizio) && ora.before(massimo)) finito = true;
            else if (ora.before(minimo)) System.out.println(
                ERRORE_MINIMO + formattatore.format(minimo.getTime())
            );
            // System.out.println(ERRORE_MINIMO + ANSI_RED + formattatore.format(minimo.getTime()) + ANSI_RESET);
            else System.out.println(ERRORE_MASSIMO + formattatore.format(massimo.getTime()));
            // System.out.println(ERRORE_MASSIMO + ANSI_RED + formattatore.format(massimo.getTime()) + ANSI_RESET);
        } while (!finito);
        System.out.print(ANSI_RESET);
        return ora;
    }
    // public static Set<Calendar> selezionaDateDaMese(
    //     Year anno,
    //     Month mese,
    //     int giornoMinimo,
    //     int giornoMassimo
    // ) {
    //     Set<Calendar> dateSelezionate = new HashSet<>();
    //     Calendar data = Calendar.getInstance();
    //     data.set(Calendar.YEAR, anno.getValue());
    //     data.set(Calendar.MONTH, mese.getValue());
    //     data.set(Calendar.DAY_OF_MONTH, giornoMinimo);

    //     while (data.get(Calendar.DAY_OF_MONTH) <= giornoMassimo) {
    //         dateSelezionate.add((Calendar) data.clone());
    //         data.add(Calendar.DAY_OF_MONTH, 1);
    //     }

    //     return dateSelezionate;
    // }
}
