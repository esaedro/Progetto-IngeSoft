package application;

import java.io.Serializable;

public class Utente implements Serializable {
    
    private String nomeUtente;
    private String password;
    private transient Session session;

    public Utente(String nomeUtente, String password) {
        this.nomeUtente = nomeUtente;
        this.password = password;
    }

    public Utente(Session session) {
        this.session = session;
    }

    public Utente login(String nomeUtente, String password) {
        return session.login(nomeUtente, password);
    }

    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    } 

}