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
     * @ requires utente != null;
     */
    public Configuratore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
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