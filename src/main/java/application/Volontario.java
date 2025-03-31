package application;

import java.util.*;

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

    public void addDisponibilita(Set<Integer> disponibilita) {
        this.disponibilita.addAll(disponibilita);
    }

    public void removeDisponibilita(Set<Integer> disponibilita) {
        this.disponibilita.removeAll(disponibilita);
    }

    public ArrayList<TipoVisita> getVisiteAssociate() {
        ArrayList<TipoVisita> visite = super.getSession().getVisite();
        visite.removeIf(visita -> !visita.getVolontariIdonei().contains(this));
        return visite;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Volontario that = (Volontario) obj;
        return this.getNomeUtente().equals(that.getNomeUtente());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNomeUtente());
    }
}
