package application;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Luogo implements Serializable {
    
    private static String parametroTerritoriale;
    private String nome;
    private String indirizzo;
    private Set<String> visiteIds = new HashSet<>();

    public Luogo(String nome, String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
    }

    public static String getParametroTerritoriale() {
        return parametroTerritoriale;
    }

    public static void setParametroTerritoriale(String parametroTerritoriale) {
        Luogo.parametroTerritoriale = parametroTerritoriale;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void addVisita(String visiteDaAggiungere) {
        visiteIds.add(visiteDaAggiungere);
    }

    public Set<String> getVisiteIds() {
        return visiteIds;
    }

    public void setVisiteIds(Set<String> visiteIds) {
        this.visiteIds = visiteIds;
    }

    public boolean haVisiteAssociate() {
        return !visiteIds.isEmpty();
    }

    public void rimuoviVisita(String titoloVisita) {
        if (visiteIds != null && visiteIds.contains(titoloVisita)) {
            visiteIds.remove(titoloVisita);
        }
    }

    @Override
    public String toString() {
        return "Luogo{" +
                "nome='" + nome + '\'' + 
                ", indirizzo='" + indirizzo + '\'' + 
                ", visiteIds=" + visiteIds +
                '}';
    }

}