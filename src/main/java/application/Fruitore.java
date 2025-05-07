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

    
    @Override  
    public Map<Visita, Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    /**
     * @ requires visita != null && iscrizione != null;
     */
    @Override
    public void aggiungiIscrizione(Visita visita, Iscrizione iscrizione) {  //TODO: togliere instanceof in session
        iscrizioni.put(visita, iscrizione);
    }

    /**
     * @ requires visita != null;
     */
    @Override
    public void rimuoviIscrizione(Visita visita) {  //TODO: togliere instanceof in session
        iscrizioni.remove(visita);
    }
}