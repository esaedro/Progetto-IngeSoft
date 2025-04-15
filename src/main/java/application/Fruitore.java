package application;

import java.util.HashMap;
import java.util.Map;

public class Fruitore extends Utente {

    private Map<Visita, Iscrizione> iscrizioni = new HashMap<>();

    /**
     * @ requires nomeUtente != null && password != null;
     */
    public Fruitore(String nomeUtente, String password, Map<Visita, Iscrizione> iscrizioni) {
        super(nomeUtente, password);
        this.iscrizioni = iscrizioni;
    }

    /**
     * @ requires utente != null;
     */
    public Fruitore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
    }

    /**
     * @ requires visita != null && iscrizione != null;
     */
    public void aggiungiIscrizione(Visita visita, Iscrizione iscrizione) {
        iscrizioni.put(visita, iscrizione);
    }

    /**
     * @ requires visita != null;
     */
    public void rimuoviIscrizione(Visita visita) {
        iscrizioni.remove(visita);
    }

    public Map<Visita, Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    /**
     * @ requires iscrizioni != null;
     */
    public void setIscrizioni(Map<Visita, Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
}
