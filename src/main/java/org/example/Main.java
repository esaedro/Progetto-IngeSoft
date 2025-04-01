package org.example;

import application.Configuratore;
import application.Session;
import application.Utente;
import utility.Controller;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Session session = new Session();
        // Configuratore configuratore = new Configuratore("C01", "config01");
        // ArrayList<Utente> utenti = new ArrayList<>();
        // utenti.add(configuratore);
        // session.setUtenti(utenti);
        // session.salvaUtenti();
        Session session = new Session();
        Set<Utente> uten = new HashSet<>();
        uten.add(new Configuratore("C01", "nuova"));
        session.setUtenti(uten);
        session.salvaUtenti();
        Controller controller = Controller.getIstance();
        controller.start();
    }
}