package org.example;

import utility.Controller;
import utility.InputDati;

public class Main {
    public static void main(String[] args) {
        InputDati.newLineDifferente = true;
        
        Controller controller = Controller.getInstance();
        controller.start();
    }
}