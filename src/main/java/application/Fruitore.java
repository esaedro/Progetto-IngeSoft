package application;

import java.util.HashMap;
import java.util.Map;

public class Fruitore extends Utente {

    private Map<Visita, Iscrizione> iscrizioni = new HashMap<>();

    public Fruitore(String nomeUtente, String password) {
        super(nomeUtente, password);
        iscrizioni = new HashMap<>();
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

    public Map<Visita, Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    public void setIscrizioni(Map<Visita, Iscrizione> iscrizioni) {
        this.iscrizioni = iscrizioni;
    }
}
