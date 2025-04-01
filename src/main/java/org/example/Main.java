package org.example;

import application.Configuratore;
import application.Session;
import application.Utente;
import application.Volontario;
import java.util.ArrayList;
import utility.AppView;

public class Main {

    public static void main(String[] args) {
        Session session = new Session();
        Configuratore configuratore = new Configuratore("C01", "config01");
        Volontario v1 = new Volontario("V01", "configV01");
        Volontario v2 = new Volontario("V02", "configV02");
        Volontario v3 = new Volontario("V03", "configV03");
        Volontario v4 = new Volontario("V04", "configV04");
        Volontario v5 = new Volontario("V05", "configV05");
        ArrayList<Utente> utenti = new ArrayList<>();
        utenti.add(configuratore);
        utenti.add(v1);
        utenti.add(v2);
        utenti.add(v3);
        utenti.add(v4);
        utenti.add(v5);
        session.setUtenti(utenti);
        session.salvaUtenti();
        AppView view = new AppView();
        view.start();
    }
}
