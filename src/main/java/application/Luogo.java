package application;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Luogo implements Serializable {
    
    private static String parametroTerritoriale;
    private String nome;
    private String indirizzo;

    //@ public invariant visiteIds.size() > 0;
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

    public void aggiungiVisite(Set<TipoVisita> visiteDaAggiungere) {
        for (TipoVisita visita : visiteDaAggiungere) {
            String titolo = visita.getTitolo();
            if (!visiteIds.contains(titolo)) {
                visiteIds.add(titolo);
            }
        }
    }

    @Override
    public String toString() {
        return  nome +
                "\t\tIndirizzo: " + indirizzo + 
                "\nTipi di visita svolti qui: " + visiteIds
                ;
    }

}