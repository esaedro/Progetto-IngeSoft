package application;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Luogo implements Serializable {

    /**
     * @ invariant parametroTerritoriale != null && !parametroTerritoriale.isEmpty();
     */
    private static String parametroTerritoriale;

    /**
     * @ invariant nome != null && !nome.isEmpty();
     */
    private String nome;

    /**
     * @ invariant indirizzo != null && !indirizzo.isEmpty();
     */
    private String indirizzo;

    /**
     * @ invariant visiteIds.size() > 0;
     */
    private Set<String> visiteIds = new HashSet<>();

    /**
     * @ requires nome != null;
     * @ requires indirizzo != null;
     */
    public Luogo(String nome, String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
    }

    public static String getParametroTerritoriale() {
        return parametroTerritoriale;
    }

    /**
     * @ requires parametroTerritoriale != null && !parametroTerritoriale.isEmpty();
     */
    public static void setParametroTerritoriale(String parametroTerritoriale) {
        Luogo.parametroTerritoriale = parametroTerritoriale;
    }

    public String getNome() {
        return nome;
    }

    /**
     * @ requires visiteDaAggiungere != null;
     */
    public void addVisita(String visiteDaAggiungere) {
        visiteIds.add(visiteDaAggiungere);
    }

    public Set<String> getVisiteIds() {
        return visiteIds;
    }

    public boolean haVisiteAssociate() {
        return !visiteIds.isEmpty();
    }

    public void rimuoviVisita(String titoloVisita) {
        if (visiteIds != null) {
            visiteIds.remove(titoloVisita);
        }
    }

    public void aggiungiVisite(Set<TipoVisita> visiteDaAggiungere) {
        for (TipoVisita visita : visiteDaAggiungere) {
            String titolo = visita.getTitolo();
            visiteIds.add(titolo);
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