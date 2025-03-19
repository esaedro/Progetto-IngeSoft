package org.example;

import java.util.ArrayList;

import application.Configuratore;
import application.Session;
import application.Utente;
import utility.AppView;

public class Main {
    public static void main(String[] args) {
        // Session session = new Session();
        // Configuratore configuratore = new Configuratore("C01", "config01");
        // ArrayList<Utente> utenti = new ArrayList<>();
        // utenti.add(configuratore);
        // session.setUtenti(utenti);
        // session.salvaUtenti();
        AppView view = new AppView();
        view.start();
    }
}