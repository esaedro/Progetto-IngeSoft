package utility;

import java.time.Month;
import java.util.Calendar;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import application.*;

public class AppView {
    
    private Utente utente;

    public AppView() {
    }

    public void start() { //Prima impostazione password
        System.out.println("\n----------------------------------------------------\nBenvenuto nel sistema di gestione delle visite guidate\n----------------------------------------------------\n");
        System.out.println("Per uscire scrivere '0' nella password");
        Utente utenteProvvisorio = null;

        while (utenteProvvisorio == null) {
            utenteProvvisorio = menuInserimentoCredenziali(new Utente(new Session()));
        }

        utente = utenteProvvisorio;
        carica();
        
        System.out.println("Benvenuto " + utente.getNomeUtente());

        mostraMenu(utente);
        salva();
    }

    public void salva() {
        utente.getSession().salva();
    }

    public void carica() {
        utente.getSession().carica();
    }

    public void mostraLista(Boolean visite, Boolean volontari, Boolean luoghi) { 
        //ipotizzo che sia vero un solo booleano quando viene chiamata
        if (luoghi) {
            for(Luogo luogo : utente.getSession().getLuoghi()) {
                System.out.println(luogo.toString());
            }
        } else if (volontari) {
            for(Utente user : utente.getSession().getUtenti()) {
                if (user instanceof Volontario)
                    System.out.println(user.toString());
            }

        } else if (visite) { //TODO: suddivisione in categorie
            for(Visita visita : utente.getSession().getVisite()) {
                System.out.println(visita.toString());
            }
        }
        else {
            System.out.println("Nessuna lista selezionata");
        }
    }

    public void mostraMenu(Utente utente) { //elenco a scelta, visualizza le liste, oppure parametri
        int scelta = 0;

        do {
            System.out.println("Menu:");
            System.out.println("1. Mostra lista luoghi");
            System.out.println("2. Mostra lista volontari");
            System.out.println("3. Mostra lista visite");
            System.out.println("4. Salva sessione");
            System.out.println("5. Carica sessione");
            System.out.println("6. Inserisci credenziali");
            System.out.println("7. Inserisci parametro territoriale");
            System.out.println("8. Inserisci massimo iscritti");
            System.out.println("9. Inserisci date precluse");
            System.out.println("0. Esci");

            scelta = leggiIntero("Scegli un opzione: ", 0, 9);
            switch (scelta) {
                case 1:
                mostraLista(false, false, true);
                break;
                case 2:
                mostraLista(false, true, false);
                break;
                case 3:
                mostraLista(true, false, false);
                break;
                case 4:
                salva();
                break;
                case 5:
                carica();
                break;
                case 6:
                menuInserimentoCredenziali(utente);
                break;
                case 7:
                menuInserimentoParametroTerritoriale();
                break;
                case 8:
                menuInserimentoMassimoIscritti();
                break;
                case 9:
                menuInserimentoDate();
                break;
                case 0:
                System.out.println("Uscita dal programma.");
                break;
                default:
                System.out.println("Opzione non valida.");
                break;
            }
        } while (scelta != 0);
    }

    public Utente menuInserimentoCredenziali(Utente utente) {
        if (utente instanceof Configuratore || true) {
            String nomeUtente, password;

            do {
                System.out.println("Inserire le credenziali del configuratore: ");
                nomeUtente = leggiStringa("Inserire il nome utente: ");
                password = leggiStringa("Inserire la password: ");
            } while (!(password.equals(Character.toString('0'))) && !(conferma("Credenziali inserite")));
            
            if (password.equals(Character.toString('0'))) return null;

            return utente.login(nomeUtente, password);
        }
        else {
            System.out.println("Permessi non sufficienti"); //Fase 1 solo configuratori accedono
            return null;
        }
    }

    public void menuInserimentoParametroTerritoriale() { //chiamo poi il metodo di configuratore passandogli il parametro da tastiera
        
        if (Luogo.getParametroTerritoriale() != null) {

            if (utente instanceof Configuratore) {    
                String parametro;
                do {
                    parametro = leggiStringa("Inserire il parametro territoriale: ");
                } while (!conferma("Parametro inserito correttamente"));

                ((Configuratore) utente).inizializzaParametroTerritoriale(parametro);
            }
            else System.out.println("Permessi non sufficienti");
        }
        else System.out.println("Parametro territoriale gia' impostato: " + Luogo.getParametroTerritoriale());
    }

    public void menuInserimentoMassimoIscritti() {
        if (utente instanceof Configuratore) {

            int maxIscritti = Visita.getNumeroMassimoIscrittoPerFruitore();
            do {
                maxIscritti = leggiIntero("Inserire il nuovo numero massimo di iscritti: ", 1, 1000);
            } while(!conferma("Nuovo numero massimo di iscritti: " + maxIscritti));
            
            ((Configuratore) utente).setNumeroMassimoIscritti(maxIscritti);
        }
        else System.out.println("Permessi non sufficienti");
    }

     public void menuInserimentoDate() {
        if (utente instanceof Configuratore) {
            Month meseLavoro = CalendarManager.meseDiLavoro(3);
            Month meseLavoro2 = meseLavoro.plus(1);
            System.out.println("Inserire le date precluse per i giorni dal 16" + meseLavoro.toString() + "al 15" + meseLavoro2.toString() + ": ");

            int dataInserita;
            Set<Calendar> datePrecluse = new HashSet<>();

            do {
                do {
                    dataInserita = leggiIntero("Inserire una data preclusa (0 per uscire)", 1, meseLavoro.maxLength());
                    if (dataInserita > 15) {
                        Calendar dataDaInserire = Calendar.getInstance();
                        dataDaInserire.set(Calendar.DAY_OF_MONTH, dataInserita);
                        dataDaInserire.set(Calendar.MONTH, meseLavoro.getValue()-1); // Month is 0-based in Calendar
                        datePrecluse.add(dataDaInserire);
                    }
                    else {
                        Calendar dataDaInserire = Calendar.getInstance();
                        dataDaInserire.set(Calendar.DAY_OF_MONTH, dataInserita);
                        dataDaInserire.set(Calendar.MONTH, meseLavoro2.getValue()-1); 
                        datePrecluse.add(dataDaInserire);
                    }
                } while (dataInserita!=0);
            } while (!conferma("Date inserite correttamente"));
            
            ((Configuratore) utente).impostaDatePrecluse(datePrecluse);
        }
        else System.out.println("Permessi non sufficienti");
    }

    private int leggiIntero(String msgInserimento, int min, int max) {
        Scanner lettore = new Scanner(System.in);
        boolean finito = false;
        int valoreLetto = 0;

        do { 
            System.out.print(msgInserimento);
            try {
                valoreLetto = lettore.nextInt();
                if (valoreLetto >= min && valoreLetto <= max) {
                    finito = true;
                } else {
                    System.out.println("Numero non accettabile");
                }
            } catch (InputMismatchException imex) {
                System.out.println("Attenzione: il dato inserito non e' un numero");
                lettore.next();
            }
        } while(!finito);

        // lettore.close();
        return valoreLetto;
    }


    public Boolean conferma(String messaggio) {
        String input;
        input = leggiStringa("Confermare l'inserimento dei dati (y/n): ");
        if (input.equalsIgnoreCase("y")) {
            System.out.println("Confermato: " + messaggio);
            return true;
        } else {
            System.out.println("Annullato");
            return false;
        }
    }

    public static String leggiStringa(String messaggio) {
        Scanner lettore = new Scanner(System.in);
        // lettore.useDelimiter(System.getProperty("line.separator"));
        boolean finito = false;
        String lettura = null;

        do {
           System.out.print(messaggio);
           lettura = lettore.next();
           lettura = lettura.trim();
           if (lettura.length() > 0) {
              finito = true;
           } else {
              System.out.println("Attenzione: non hai inserito alcun carattere");
           }
        } while(!finito);

        // lettore.close();
        return lettura;
     }

}