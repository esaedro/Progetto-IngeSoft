package application;

public class Configuratore extends Utente {

    public Configuratore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    public void inizializzaParametroTerritoriale(String parametroTerritoriale) {
        Luogo.setParametroTerritoriale(parametroTerritoriale);
    } 

    public void setNumeroMassimoIscritti(int numeroMassimoIscritti) {
        Visita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);
    }

    public void consultaDati() {  //da togliere?

    }

    public void impostaDatePrecluse() {

    }

    public void inserisciLuoghiVisite() {

    }

}