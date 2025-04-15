package application;

import java.util.HashMap;
import java.util.Map;

public class Fruitore extends Utente {

    private HashMap<String, Iscrizione> iscrizioni = new HashMap<>();


    public Fruitore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }
    /**
     * @ requires nomeUtente != null && password != null;
     */
    public Fruitore(String nomeUtente, String password, HashMap<String, Iscrizione> iscrizioni) {
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
        iscrizioni.put(visita.getId(), iscrizione);
    }

    /**
     * @ requires visita != null;
     */
    public void rimuoviIscrizione(Visita visita) {
        iscrizioni.remove(visita.getId());
    }

    public HashMap<String, Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    /**
     * @ requires iscrizioni != null;
     */
    public void setIscrizioni(HashMap<String, Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
}
