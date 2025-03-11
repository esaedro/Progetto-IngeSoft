package application;

import java.util.Calendar;
import java.util.Set;

public class Configuratore extends Utente {

    public Configuratore(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    public Configuratore(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
        super.session = utente.session;
    }

    public void inizializzaParametroTerritoriale(String parametroTerritoriale) {
        Luogo.setParametroTerritoriale(parametroTerritoriale);
    } 

    public void setNumeroMassimoIscritti(int numeroMassimoIscritti) {
        Visita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);
    }

    public void impostaDatePrecluse(Set<Calendar> datePrecluse) {
        Visita.aggiungiDatePrecluse(datePrecluse); 
    }

    public void inserisciLuoghiVisite(Set<Luogo> luogo) {
        super.session.luoghi.addAll(luogo);
    }

}