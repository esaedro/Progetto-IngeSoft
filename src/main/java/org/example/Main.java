package org.example;

import utility.BelleStringhe;
import utility.Controller;
import utility.InputDati;

public class Main {
    public static void main(String[] args) {
        BelleStringhe.colori = true;
        InputDati.newLineDifferente = true;
        
        Controller controller = Controller.getInstance();
//        controller.creaVisita();
        controller.start();

        // TODO: condizioni Session, Controller, FileManager, UtenteSerializer, UtenteDeserializer
    }
}