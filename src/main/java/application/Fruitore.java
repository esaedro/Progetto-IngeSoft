package application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fruitore extends Utente {

    
    //al fruitore verrà rilasciato un codice di prenotazione unico, che egli dovrà portare
    //con sé alla visita oppure potrà usare per disdire l’iscrizione alla stessa.

    //private Map<Visita, String> iscrizioni = new HashMap<>(); // <Visita, codice di prenotazione>

    //oppure in Visita associamo i fruitori iscritti?
   
    
    public Fruitore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }
    
    public Fruitore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
    }

/*  public void aggiungiIscrizione() {}
    public void rimuoviIscrizione() {} */
}
