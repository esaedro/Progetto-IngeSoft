package utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import application.Configuratore;
import application.Luogo;
import application.Session;
import application.TipoVisita;
import application.Utente;
import application.Volontario;

public class AppView {
    
    private Utente utente;
//    private TextIO textIO = TextIoFactory.getTextIO();

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
        System.out.println("asd");
        carica();
        
        System.out.println("\nBenvenuto " + utente.getNomeUtente());

        //se la password inizia con sequenza predefinita allora è da cambiare perchè questo è il primo accesso 
        menuCambioPassword();

        System.out.println("\nSi inizializzi il parametro territoriale a cui fa riferimento l'applicazione");
        menuInserimentoParametroTerritoriale();
        System.out.println("\nSi inizializzi il numero massimo di iscritti per visita");
        menuInserimentoMassimoIscritti();

        mostraMenu(utente);
        salva();
    }

    public void salva() {
        utente.getSession().salva();
        System.out.println("\nSessione salvata");
    }

    public void carica() {
        System.out.println();
        utente.getSession().carica();
        System.out.println("\nSessione caricata");
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
                    System.out.println(user);
            }

        } else if (visite) { //TODO: suddivisione in categorie
            for(TipoVisita visita : utente.getSession().getVisite()) {
                System.out.println(visita.toString());
            }
            for(TipoVisita visita: utente.getSession().getStoricoVisite()) {
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
            System.out.println("\nMenu:");
            System.out.println("1. Mostra lista luoghi");
            System.out.println("2. Mostra lista volontari");
            System.out.println("3. Mostra lista visite");
            System.out.println("4. Salva sessione");
            System.out.println("5. Carica sessione");
            System.out.println("6. Inserisci credenziali");
            System.out.println("7. Inserisci nuovi luoghi e visite");
            System.out.println("8. Inserisci massimo iscritti");
            System.out.println("9. Inserisci date precluse");
            System.out.println("0. Esci");

            scelta = leggiIntero("Scegli un opzione: ", 0, 9);
            switch (scelta) {
                case 1 -> mostraLista(false, false, true);
                case 2 -> mostraLista(false, true, false);
                case 3 -> mostraLista(true, false, false);
                case 4 -> salva();
                case 5 -> carica();
                case 6 -> menuInserimentoCredenziali(utente);
                case 7 -> menuInserimentoLuoghi();
                case 8 -> menuInserimentoMassimoIscritti();
                case 9 -> menuInserimentoDate();
                case 0 -> System.out.println("\nUscita dal programma.");
                default -> System.out.println("\nOpzione non valida.");
            }
        } while (scelta != 0);
    }

    public Utente menuInserimentoCredenziali(Utente utente) {
            String nomeUtente, password;

            do {
                System.out.println("\nInserire le credenziali del configuratore: ");
                nomeUtente = leggiStringa("Inserire il nome utente: ");
                password = leggiStringa("Inserire la password: ");
            } while (!(password.equals(Character.toString('0'))) && !(conferma("Credenziali inserite")));
            
            if (password.equals(Character.toString('0'))) return null;

        return utente.login(nomeUtente, password);
    }

    public void menuCambioPassword() {
        String newPassword;
        do {
            newPassword = leggiStringa("\nInserire la nuova password: ");
            if (newPassword.equals(utente.getPassword())) {
                System.out.println("\nLa nuova password non puo' essere uguale a quella attuale");
            }
        } while (newPassword.equals(utente.getPassword()) || !conferma("Password accettata"));

        utente.getSession().cambiaPassword(utente, newPassword);
    }

    public void menuInserimentoParametroTerritoriale() { //chiamo poi il metodo di configuratore passandogli il parametro da tastiera
        
        if (Luogo.getParametroTerritoriale() == null) {

            if (utente instanceof Configuratore) {    
                String parametro;
                do {
                    parametro = leggiStringa("\nInserire il parametro territoriale: ");
                } while (!conferma("Parametro inserito correttamente"));

                ((Configuratore) utente).inizializzaParametroTerritoriale(parametro);
            }
            else System.out.println("\nPermessi non sufficienti");
        }
        else System.out.println("\nParametro territoriale gia' impostato: " + Luogo.getParametroTerritoriale());
    }

    public void menuInserimentoMassimoIscritti() {
        if (utente instanceof Configuratore) {

            int maxIscritti = TipoVisita.getNumeroMassimoIscrittoPerFruitore();
            do {
                maxIscritti = leggiIntero("\nInserire il nuovo numero massimo di iscritti: ", 1, 1000);
            } while(!conferma("\nNuovo numero massimo di iscritti: " + maxIscritti));
            
            ((Configuratore) utente).setNumeroMassimoIscritti(maxIscritti);
        }
        else System.out.println("\nPermessi non sufficienti");
    }

     public void menuInserimentoDate() {
        if (utente instanceof Configuratore) {
            Month meseLavoro = CalendarManager.meseDiLavoro(3);
            Month meseLavoro2 = meseLavoro.plus(1);
            System.out.println("\nInserire le date precluse per i giorni dal 16 " + traduciMese(meseLavoro) + " al 15 " + traduciMese(meseLavoro2) + ": ");

            int dataInserita;
            Set<Calendar> datePrecluse = new HashSet<>();

            do {
                do {
                    dataInserita = leggiIntero("Inserire una data preclusa (0 per uscire): ", 0, meseLavoro.maxLength());
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

    /*introdurre un insieme di luoghi da destinare a visite guidate, associando a ciascun
    luogo uno o più tipi di visita e almeno un volontario per ciascun tipo*/
    private void menuInserimentoLuoghi() {
        if (utente instanceof Configuratore) {
            Set<Luogo> luoghi = new HashSet<>();
            Set<TipoVisita> visite = new HashSet<>();
            String nomeLuogo, indirizzoLuogo;
            TipoVisita tipoVisita;

            do {
                do {
                    nomeLuogo = leggiStringa("\nInserire il nome del luogo da inserire: ");
                    indirizzoLuogo = leggiStringa("Inserire l'indirizzo del luogo da inserire: ");
                } while(!conferma("Luogo inserito"));

                Luogo luogo = new Luogo(nomeLuogo, indirizzoLuogo);

                System.out.println("Inserire almeno un tipo di visita: ");
                do {
                    tipoVisita = menuInserimentoTipoVisita();
                    if (tipoVisita != null) {
                        visite.add(tipoVisita);
                    }
                    else break;
                } while (yn("Inserire un altro tipo di visita?"));

                luogo.addVisite(visite);
                luoghi.add(luogo);
                ((Configuratore) utente).inserisciVisite(visite); //Inserisce la/le visite nella session dell'utente
            } while(yn("Inserire un altro luogo?"));

            ((Configuratore) utente).inserisciLuoghi(luoghi); //Inserisce i luoghi nella session dell'utente

        }
        else System.out.println("Permessi non sufficienti");
    }

    private TipoVisita menuInserimentoTipoVisita() {
        if (utente instanceof Configuratore) {
            String titolo, descrizione, puntoIncontro;
            Calendar dataInizio, dataFine, oraInizio;
            int durata, minPartecipante, maxPartecipante;
            Boolean bigliettoIngresso;
            Set<DayOfWeek> giorniSettimana = new HashSet<>();
            Set<Volontario> volontariIdonei = new HashSet<>();

            titolo = leggiStringa("Inserire il titolo della visita: ");
            descrizione = leggiStringa("Inserire la descrizione della visita: ");
            puntoIncontro = leggiStringa("Inserire il punto di incontro della visita: ");
            dataInizio = leggiData("Inserisci data inizio ");
            dataFine = leggiData("Inserisci data fine ");
            
            oraInizio = leggiOra("Inserisci ora inizio ");
/*             int minuti, ora;
            ora = leggiIntero("Inserire ora inizio ", 0, 23);
            minuti = leggiIntero("Inserire minuti inizio ", 0, 59);
            oraInizio = Calendar.getInstance();
            oraInizio.set(Calendar.HOUR_OF_DAY, ora);
            oraInizio.set(Calendar.MINUTE, minuti); */
            
            durata = leggiIntero("Inserisci durata in minuti: ", 1, 600);
            do {
                minPartecipante = leggiIntero("Inserisci numero minimo partecipanti: ", 1, 1000);
                maxPartecipante = leggiIntero("Inserisci numero massimo partecipanti: ", 1, 1000);
                if (minPartecipante > maxPartecipante) System.out.println("Il numero minimo di partecipanti non puo' essere maggiore del numero massimo");
            } while (minPartecipante > maxPartecipante);
            bigliettoIngresso = yn("E' richiesto un biglietto d'ingresso? ");

            System.out.println("Inserire i giorni della settimana in cui si svolge la visita: ");
            for (DayOfWeek giorno : DayOfWeek.values()) {
                System.out.print((giorno.getValue()) + ". " + traduciGiorno(giorno) + "\t");
                if (yn("")) {
                    giorniSettimana.add(giorno);
                }
            }

            System.out.println("Inserire i volontari idonei alla visita: ");  
            if (utente.getSession().getVolontari().isEmpty()) {
                System.out.println("Non ci sono volontari disponibili");
            }
            else {
                while(volontariIdonei.isEmpty() || yn("Inserire un altro volontario?")) {
                    Volontario volontario = menuSelezioneVolontario();
                    if (volontario != null) {
                        volontariIdonei.add(volontario);
                    } else if (volontariIdonei.isEmpty()) {
                        System.out.println("Inserire almeno un volontario / Non ci sono volontari");
                    }
                }
            }
            return new TipoVisita(titolo, descrizione, puntoIncontro, dataInizio, dataFine, oraInizio, durata,
                    giorniSettimana, minPartecipante, maxPartecipante, bigliettoIngresso, volontariIdonei);
        }
        else {
            System.out.println("Permessi non sufficienti");
            return null;
        }
    }

    private Volontario menuSelezioneVolontario(){
        if (utente instanceof Configuratore) {

            System.out.println("Selezionare un volontario: ");
            int i = 1;
            for (Volontario volontario : utente.getSession().getVolontari()) {
                    System.out.println(i + ". " + volontario.getNomeUtente());
                    i++;
            }
            int scelta = leggiIntero("Scegli un volontario: ", 0, i);
            return scelta == 0 ? null : (Volontario)utente.getSession().getVolontari().toArray()[scelta-1];
        }
        else {
            System.out.println("Permessi non sufficienti");
            return null;
        }
    }

    private Calendar leggiData(String messaggio) {
        Scanner lettore = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
        formatter.setLenient(false); // Impedisce di accettare date non valide
        
        Calendar calendar = Calendar.getInstance();
        boolean inputValido = false;

        while (!inputValido) {
            System.out.print(messaggio + "(dd/MM): ");
            String input = lettore.nextLine();
        
            try {
                Date data = formatter.parse(input); // Parsing della data
                calendar.setTime(data);
                //calendar.set(Calendar.YEAR, Calendar.YEAR); // Aggiungiamo l'anno corrente
                                // Controllo per date impossibili (es. 30 febbraio, 31 aprile, ecc.)
                int giornoInserito = calendar.get(Calendar.DAY_OF_MONTH);
                if (giornoInserito != Integer.parseInt(input.split("/")[0])) {
                    throw new ParseException("Data non valida", 0);
                }
                inputValido = true;
            } catch (ParseException e) {
                System.out.println("Formato non valido");
            }
        }
        return calendar;
    }

     private Calendar leggiOra(String messaggio) {
        Scanner lettore = new Scanner(System.in);
        SimpleDateFormat formatter = new SimpleDateFormat("HH/mm");
        formatter.setLenient(false); // Impedisce di accettare date non valide
        
        Calendar calendar = Calendar.getInstance();
        boolean inputValido = false;

        while (!inputValido) {
            System.out.print(messaggio + "(HH:mm): ");
            String input = lettore.nextLine();
        
            try {
                Date data = formatter.parse(input); // ERRORE QUI PARSE EXCEPTION
                calendar.setTime(data);
 
                // Controllo per orari impossibili
                int oraInserita = calendar.get(Calendar.HOUR_OF_DAY);
                int minutiInseriti = calendar.get(Calendar.MINUTE);
                String[] parti = input.split(":");
                int oraDigitata = Integer.parseInt(parti[0]);
                int minutiDigitati = Integer.parseInt(parti[1]);

                if (oraInserita != oraDigitata || minutiInseriti != minutiDigitati) {
                    throw new ParseException("Orario non valido", 0);
                }

                inputValido = true;

            } catch (ParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Formato non valido o orario inesistente");
            }
        }
        return calendar;
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

        return valoreLetto;
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
           if (!lettura.isEmpty()) {
              finito = true;
           } else {
              System.out.println("Attenzione: non hai inserito alcun carattere");
           }
        } while(!finito);

        return lettura;
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

    public Boolean yn(String messaggio) {
        String input = leggiStringa(messaggio + "(y/n): ");
        return input.equalsIgnoreCase("y");
    }

    public static String traduciGiorno(DayOfWeek giorno) {
        return switch (giorno) {
            case MONDAY -> "Lunedì";
            case TUESDAY -> "Martedì";
            case WEDNESDAY -> "Mercoledì";
            case THURSDAY -> "Giovedì";
            case FRIDAY -> "Venerdì";
            case SATURDAY -> "Sabato";
            case SUNDAY -> "Domenica";
            default -> "Giorno non valido";
        };
    }

    public static String traduciMese(Month mese) {
        return switch (mese) {
            case JANUARY -> "Gennaio";
            case FEBRUARY -> "Febbraio";
            case MARCH -> "Marzo";
            case APRIL -> "Aprile";
            case MAY -> "Maggio";
            case JUNE -> "Giugno";
            case JULY -> "Luglio";
            case AUGUST -> "Agosto";
            case SEPTEMBER -> "Settembre";
            case OCTOBER -> "Ottobre";
            case NOVEMBER -> "Novembre";
            case DECEMBER -> "Dicembre";
            default -> "Mese non valido";
        };
    }

}