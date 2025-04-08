package utility;

import static utility.BelleStringhe.ANSI_RED;
import static utility.BelleStringhe.ANSI_RESET;
import static utility.BelleStringhe.ANSI_YELLOW;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
Questa classe rappresenta un menu testuale generico a piu' voci
Si suppone che la voce per uscire sia sempre associata alla scelta 0
e sia presentata in fondo al menu
*/
public class CliMenu<T, U> {

    public static boolean colori = BelleStringhe.colori; // Abilita la colorazione degli input e dei messaggi di errore
    public static final String VOCE_USCITA = (colori)
        ? ANSI_YELLOW + "q  " + ANSI_RESET + "per uscire"
        : "q  " + "per uscire";
    public static final String RICHIESTA_INSERIMENTO =
        "Digita il numero dell'opzione desiderata > ";
    public static final String ERRORE_INSERIMENTO = (colori)
        ? ANSI_RED + "Scelta non valida" + ANSI_RESET
        : "Scelta non valida";

    private String titolo;
    protected LinkedHashMap<T, U> voci;

    public CliMenu() {
        voci = new LinkedHashMap<>();
    }

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
            String voce = (colori)
                ? ANSI_YELLOW + i + ") " + ANSI_RESET + entry.getKey()
                : i + ") " + entry.getKey();
            System.out.println(voce);
        }
        System.out.println("\n" + VOCE_USCITA + "\n");
    }

    protected U scegli() {
        stampaMenu();
        do {
            String scelta = InputDati.leggiStringaNonVuota(RICHIESTA_INSERIMENTO);

            try {
                int voce_scelta = Integer.parseInt(String.valueOf(scelta));
                if (voce_scelta <= 0 || voce_scelta > voci.size()) System.out.println(
                    ERRORE_INSERIMENTO
                );
                else {
                    if (colori) System.out.println(ANSI_RESET);
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
                    if (colori) System.out.println(ANSI_RESET);
                    return null;
                } else System.out.println(ERRORE_INSERIMENTO);
            }
        } while (true);
    }

    protected void addVoce(Entry<T, U> voce) {
        voci.putIfAbsent(voce.getKey(), voce.getValue());
    }

    protected void addVoci(LinkedHashMap<T, U> vociDaAggiungere) {
        vociDaAggiungere.forEach((voce, runnable) -> voci.putIfAbsent(voce, runnable));
    }

    protected void removeVoce(Entry<T, U> voce) {
        voci.remove(voce.getKey(), voce.getValue());
    }

    protected void removeVoci(LinkedHashMap<T, U> vociDaRimuovere) {
        vociDaRimuovere.forEach((voce, runnable) -> voci.remove(voce, runnable));
    }

    protected void removeAllVoci() {
        voci.clear();
    }

    protected void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}
