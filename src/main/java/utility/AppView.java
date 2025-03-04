package utility;

import java.util.Scanner;
import application.Configuratore;
import application.Utente;

public class AppView {
    
    private Utente utente;

    public AppView(Utente utente) {
        this.utente = utente;
    }

    public void start() {

    }

    public void salva() {
        utente.getSession().salva();
    }

    public void carica() {
        utente.getSession().carica();
    }

    /* public void login(Utente utente) {
        //utente.login();
    } */

    public void mostraLista(Boolean visite, Boolean volontari, Boolean luoghi) { 
        //ipotizzo che sia vero un solo booleano quando viene chiamata
        if (luoghi) {

        } else if (volontari) {

        } else if (visite) {

        }
        else {
            System.out.println("Nessuna lista selezionata");
        }
    }

    public void mostraMenu(Utente utente) { //elenco a scelta, visualizza le liste, oppure parametri

    }

    public void mostraInserimentoCredenziali(Utente utente) {
        if (utente instanceof Configuratore) {
            System.out.println("Inserire le credenziali del configuratore: ");


        }
        else {
            System.out.println("Permessi non sufficienti"); //Fase 1 solo configuratori accedono
        }
    }

    public void menuInserimentoParametroTerritoriale() { //chiamo poi il metodo di configuratore passandogli il parametro da tastiera
        if (utente instanceof Configuratore) {    
            System.out.println("Inserire il parametro territoriale: ");
            Scanner scanner = new Scanner(System.in);
            String parametro = scanner.nextLine();
            conferma("Parametro globale inserito correttamente");
            ((Configuratore) utente).inizializzaParametroTerritoriale(parametro);
        }
        else {
            System.out.println("Permessi non sufficienti");
        }
        }

    public void menuInserimentoDate() {
        
    }


    public void conferma(String messaggio) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Confermare l'inserimento dei dati (y/n): ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            System.out.println("Confermato: " + messaggio);
        } else {
            System.out.println("Annullato");
        }
    }
}