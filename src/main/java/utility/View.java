package utility;

import application.Session;

public class View {
    
    private Session session;

    
    public View(Session session) {
        this.session = session;
    }


    public void salva() {
        session.salva();
    }

    public void carica() {
        session.carica();
    }

    public void mostraLista(Boolean visite, Boolean volontari, Boolean luoghi) { //cambio nome

    }

    public void login() {

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}