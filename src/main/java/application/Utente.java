package application;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class Utente implements Serializable {
    
    private String nomeUtente;
    private String password;

    /**
     * @ requires nomeUtente != null && password != null;
     */
    public Utente(String nomeUtente, String password) {
        this.nomeUtente = nomeUtente;
        this.password = password;
    }

    public Utente() {

    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @ requires password != null;
     */
    public void setPassword(String password) {
        this.password = password;
    }


    //Metodi per Configuratore 
    public void inizializzaParametroTerritoriale(String parametroTerritoriale) {}
    public void setNumeroMassimoIscritti(int numeroMassimoIscritti) {}
    public void impostaDatePrecluse(Set<Integer> datePrecluse) {}

    //Metodi per Volontario

    public void addDisponibilita(Set<Integer> disponibilitaDaAggiungere) {}
    public void clearDisponibilita() {}

    public Set<Integer> getDisponibilita() {
        throw new UnsupportedOperationException("Metodo getDisponibilita() non implementato per la classe Utente");
    }

    public Set<TipoVisita> getVisiteAssociate(Set<TipoVisita> visite) {
        throw new UnsupportedOperationException("Metodo getVisiteAssociate() non implementato per la classe Utente");
    }

    public boolean haVisiteAssociate(Set<TipoVisita> visite) {
        throw new UnsupportedOperationException("Metodo haVisiteAssociate() non implementato per la classe Utente");

    }

    //Metodi per Fruitore
    public void aggiungiIscrizione(Visita visita, Iscrizione iscrizione) {}
    public void rimuoviIscrizione(Visita visita) {}

    public Map<Visita, Iscrizione> getIscrizioni() {
        throw new UnsupportedOperationException("Metodo getIscrizioni() non implementato per la classe Utente");
    }

}