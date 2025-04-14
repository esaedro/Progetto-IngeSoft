package org.example;

import utility.BelleStringhe;
import utility.Controller;
import utility.InputDati;

public class Main {
    public static void main(String[] args) {
        BelleStringhe.colori = true;
        InputDati.newLineDifferente = false;
        
        Controller controller = Controller.getInstance();
        controller.start();
    
        //TODO: cambiare archivio storico da TipoVisita ad oggetti Visita con stato cancellata
        //TODO: controllare che venga salvato in visite.json il campo VolontarioAssociato di  (contenute a loro volta nel campo visiteAssociate)
    }
}