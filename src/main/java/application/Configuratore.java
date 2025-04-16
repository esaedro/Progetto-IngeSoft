package application;

import java.util.Set;

public class Configuratore extends Utente {

    /**
     * @ requires nomeUtente != null && password != null;
     */
    public Configuratore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    /**
     * @ requires parametroTerritoriale != null && !parametroTerritoriale.isEmpty();
     */
    public void inizializzaParametroTerritoriale(String parametroTerritoriale) {
        Luogo.setParametroTerritoriale(parametroTerritoriale);
    }

    /**
     * @ requires parametroTerritoriale > 0;
     */
    public void setNumeroMassimoIscritti(int numeroMassimoIscritti) {
        TipoVisita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);
    }

    /**
     * @ requires datePrecluse != null;
     */
    public void impostaDatePrecluse(Set<Integer> datePrecluse) {
        TipoVisita.aggiungiDatePrecluseFuture(datePrecluse);
    }
}