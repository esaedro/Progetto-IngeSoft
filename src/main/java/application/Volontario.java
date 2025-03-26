package application;

import java.util.HashSet;
import java.util.Set;

public class Volontario extends Utente {

    Set<Integer> disponibilita = new HashSet<>();

    // Constructor per non dare errore, non fa realmente parte del progetto
    public Volontario(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    public Volontario(String nomeUtente, String password, Set<Integer> disponibilita) {
        super(nomeUtente, password);
        this.disponibilita = disponibilita;
    }

    public Volontario(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
        super.setSession(utente.getSession());
    }

    public Set<Integer> getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Set<Integer> disponibilita) {
        this.disponibilita = disponibilita;
    }
}
