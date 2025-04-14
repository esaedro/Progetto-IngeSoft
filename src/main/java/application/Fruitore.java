package application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fruitore extends Utente {

    private Map<Visita, Iscrizione> iscrizioni = new HashMap<>(); // <Visita, codice di prenotazione>
    
    public Fruitore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }
    
    public Fruitore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
    }

    public void aggiungiIscrizione(Visita visita, Iscrizione iscrizione) {
        iscrizioni.put(visita, iscrizione);
    }

    public void rimuoviIscrizione(Visita visita) {
        iscrizioni.remove(visita);
    }
}
