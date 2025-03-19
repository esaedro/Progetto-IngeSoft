package application;

public class Volontario extends Utente {

    // Constructor per non dare errore, non fa realmente parte del progetto
    public Volontario(String nomeUtente, String password) {
        super(nomeUtente, password);
    }

    public Volontario(Utente utente) {
        super(utente.getNomeUtente(), utente.getPassword());
        super.setSession(utente.getSession());
    }
}
