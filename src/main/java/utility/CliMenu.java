package utility;

import java.util.Map.Entry;
import java.util.LinkedHashMap;
import static utility.BelleStringhe.ANSI_RESET;
import static utility.BelleStringhe.ANSI_YELLOW;
import static utility.BelleStringhe.ANSI_RED;

/**
Questa classe rappresenta un menu testuale generico a piu' voci
Si suppone che la voce per uscire sia sempre associata alla scelta 0 
e sia presentata in fondo al menu
*/
public class CliMenu <T, U> {
    public static final String VOCE_USCITA = "q  " + "per uscire";
    // public static final String VOCE_USCITA = ANSI_YELLOW + "q  " + ANSI_RESET + "per uscire";
    public static final String RICHIESTA_INSERIMENTO = "Digita il numero dell'opzione desiderata > ";
    public static final String ERRORE_INSERIMENTO = "Scelta non valida";
    // public static final String ERRORE_INSERIMENTO = ANSI_RED + "Scelta non valida" + ANSI_RESET;

    private final String titolo;
    protected LinkedHashMap<T, U> voci;

    /**
     * Costruttore per il menu
     * @param titolo
     * @param voci
     */
    public CliMenu(String titolo, LinkedHashMap<T, U> voci) {
        this.titolo = titolo;
        this.voci = voci;
    }

    protected void stampaMenu() {
        System.out.println(BelleStringhe.incornicia(titolo));
        int i = 0;
        for (Entry<T, U> entry : voci.entrySet()) {
            i++;
            System.out.println(i + ") " + entry.getKey());
            // System.out.println(ANSI_YELLOW + i + ") " + ANSI_RESET + entry.getKey());
        }
        System.out.println("\n" + VOCE_USCITA + "\n");

    }

    protected U scegli() {
        stampaMenu();
        do {
        String scelta = InputDati.leggiStringaNonVuota(RICHIESTA_INSERIMENTO);
        
        try {
            int voce_scelta = Integer.parseInt(String.valueOf(scelta));
            if (voce_scelta <= 0 || voce_scelta > voci.size())
                System.out.println(ERRORE_INSERIMENTO);
                // System.out.println(ANSI_RESET + ERRORE_INSERIMENTO);
            else {
                // System.out.println(ANSI_RESET);
                int index = 0;
                for (U value : voci.values()) {
                    if (index == voce_scelta - 1) {
                        return value;
                    }
                    index++;
                }
            }
        } catch (NumberFormatException e) {
            if (scelta.equals("q")) {
                // System.out.println(ANSI_RESET);
                return null;
            }
            else
                System.out.println(ERRORE_INSERIMENTO);
                // System.out.println(ANSI_RESET + ERRORE_INSERIMENTO);
        }
        } while (true);
    }
}
