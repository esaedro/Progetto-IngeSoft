package org.example;

import utility.BelleStringhe;
import utility.Controller;
import utility.InputDati;

public class Main {
    public static void main(String[] args) {
        BelleStringhe.colori = true;
        InputDati.newLineDifferente = false;
        
        Controller controller = Controller.getIstance();
        controller.start();
    }
}