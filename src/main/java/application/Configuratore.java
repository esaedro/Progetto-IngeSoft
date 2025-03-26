package application;

import java.util.Set;

public class Configuratore extends Utente {

    public Configuratore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    public Configuratore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
        super.setSession(utente.getSession());
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

    public void inserisciLuoghi(Set<Luogo> luogo) {
        super.getSession().getLuoghi().addAll(luogo);
    }

    public void inserisciVisite(Set<Visita> visite) {
        super.getSession().addAllTipoVisite(visite);
    }

}