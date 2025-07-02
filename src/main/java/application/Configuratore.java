package application;

import java.util.Calendar;
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
    @Override
    public void inizializzaParametroTerritoriale(String parametroTerritoriale) {
        Luogo.setParametroTerritoriale(parametroTerritoriale);
    }

    /**
     * @ requires parametroTerritoriale > 0;
     */
    @Override
    public void setNumeroMassimoIscritti(int numeroMassimoIscritti) {
        TipoVisita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);
    }

    /**
     * @ requires datePrecluse != null;
     */
    @Override
    public void impostaDatePrecluse(Set<Integer> datePrecluse) {
        TipoVisita.aggiungiDatePrecluseFuture(datePrecluse);
    }

    @Override
    public TipoMenu getTipoMenu() {
        return (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 16) 
            ? TipoMenu.CONFIGURATORE
            : TipoMenu.CONFIGURATORE_RACCOLTA;
    }
}