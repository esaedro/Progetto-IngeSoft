package application;

import java.util.Set;

public class Configuratore extends Utente {

    public Configuratore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    public Configuratore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
    }

    public void inizializzaParametroTerritoriale(String parametroTerritoriale) {
        Luogo.setParametroTerritoriale(parametroTerritoriale);
    } 

    public void setNumeroMassimoIscritti(int numeroMassimoIscritti) {
        TipoVisita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);
    }

    public void impostaDatePrecluse(Set<Integer> datePrecluse) {
        TipoVisita.aggiungiDatePrecluseFuture(datePrecluse);
    }

}