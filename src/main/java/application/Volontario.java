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
    }

    public Set<Integer> getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Set<Integer> disponibilitaNuova) {
        this.disponibilita = disponibilitaNuova;
    }

    public void addDisponibilita(Set<Integer> disponibilitaDaAggiungere) {
        this.disponibilita.addAll(disponibilitaDaAggiungere);
    }

    public void removeDisponibilita(Set<Integer> disponibilitaDaRimuovere) {
        this.disponibilita.removeAll(disponibilitaDaRimuovere);
    }

    public Set<TipoVisita> getVisiteAssociate(Set<TipoVisita> visite) {
        Set<TipoVisita> visiteTemp = new HashSet<>(visite);
        visiteTemp.removeIf(luogo -> !luogo.getVolontariIdonei().contains(this));
        return visiteTemp;
    }

    public boolean haVisiteAssociate(Set<TipoVisita> visite) {
        return !getVisiteAssociate(visite).isEmpty();
    }

    public void clearDisponibilita() {
        disponibilita.clear();
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
