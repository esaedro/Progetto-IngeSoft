package application;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Luogo implements Serializable {
    
    private static String parametroTerritoriale;
    private String nome;
    private String indirizzo;
    private Set<TipoVisita> visite = new HashSet<>();
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

    public Set<TipoVisita> getVisite() {
        return visite;
    }
    
    public void addVisite(Set<TipoVisita> visiteDaAggiungere) {
        visite.addAll(visiteDaAggiungere);
    }

    public Set<String> getVisiteIds() {
        return visiteIds;
    }

    public void setVisiteIds(Set<String> visiteIds) {
        this.visiteIds = visiteIds;
    }

    @Override
    public String toString() {
        return "Luogo{" +
                "nome='" + nome + '\'' + ", indirizzo='" + indirizzo + '\'' + '}';
    }

}