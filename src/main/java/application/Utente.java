package application;

import java.io.Serializable;

public class Utente implements Serializable {
    
    protected String nomeUtente;
    protected String password;
    protected Session session;

    private Utente login(String nomeUtente, String password) {
        return session.login(nomeUtente, password);
    }

    public Utente(String nomeUtente, String password) {
        this.nomeUtente = nomeUtente;
        this.password = password;
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