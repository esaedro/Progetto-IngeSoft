package utility;

import application.Session;

public class View {
    
    private Session session;

    
    public View(Session session) {
        this.session = session;
    }


    public void salva() {
    
    }

    public void carica() {

    }

    public void mostraLista(Boolean visite, Boolean volontari, Boolean luoghi) {

    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}