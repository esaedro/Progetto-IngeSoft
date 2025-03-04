package application;

import java.io.Serializable;

public class Luogo implements Serializable {
    
    private static String parametroTerritoriale;
    private String nome;

    public Luogo(String nome) {
        this.nome = nome;
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

}