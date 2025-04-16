package application;

import java.io.Serializable;

public class Utente implements Serializable {
    
    private String nomeUtente;
    private String password;

    /**
     * @ requires nomeUtente != null && password != null;
     */
    public Utente(String nomeUtente, String password) {
        this.nomeUtente = nomeUtente;
        this.password = password;
    }

    public Utente() {

    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public String getPassword() {
        return password;
    }

    /**
     * @ requires password != null;
     */
    public void setPassword(String password) {
        this.password = password;
    }

}