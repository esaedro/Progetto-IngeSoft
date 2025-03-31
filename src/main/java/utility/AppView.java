package utility;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import application.*;

public class AppView {
    
    private Utente utente;

    public AppView() {
    }

    public void start() { //Prima impostazione password
        System.out.println(BelleStringhe.incornicia("Benvenuto nel sistema di gestione delle visite guidate"));
        System.out.println("Per uscire scrivere '0' nella password");
        CliMenu<String, Runnable> myMenu = null;

        carica();

        System.out.println(BelleStringhe.incornicia("Benvenuto " + utente.getNomeUtente()));
        if (utente.getPassword().startsWith("config")) {
            menuCambioPassword();
            System.out.println("Password cambiata con successo");
            
            System.out.println("Si inseriscano luoghi e visite da includere nell'applicazione");
            menuInserimentoLuoghi(); 
        }

        if (Luogo.getParametroTerritoriale()==null) {
            System.out.println("\nSi inizializzi il parametro territoriale a cui fa riferimento l'applicazione");
            menuInserimentoParametroTerritoriale();
        }
        
        if (TipoVisita.getNumeroMassimoIscrittoPerFruitore()==0) {
            System.out.println("\nSi inizializzi il numero massimo di iscritti per visita");
            menuInserimentoMassimoIscritti();
        }

        if (utente instanceof Configuratore) {
            myMenu = creaMenuConfiguratore();
        }
        else if (utente instanceof Volontario) {
            myMenu = creaMenuVolontario();
        }

        stampaMenu(myMenu);
        salva();
    }

    public void salva() {
        utente.getSession().salva();
        utente.getSession().salvaParametriGlobali();
        utente.getSession().salvaUtenti();
        System.out.println("\nSessione salvata");
    }

    public void carica() {
        Utente utenteProvvisorio = null;
        System.out.println();
    
        while (utenteProvvisorio == null) {
            utenteProvvisorio = menuInserimentoCredenziali(new Utente(new Session()));
            if (utenteProvvisorio == null) System.out.println("\nUtente non trovato. Nome utente o password errati.");
        }

        utente = utenteProvvisorio;
        
        utente.getSession().carica();
        System.out.println("\nSessione caricata");
    }

    public CliMenu<String, Runnable> creaMenuConfiguratore() {
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();
        voci.put("Salva sessione", () -> salva());
        voci.put("Carica sessione", () -> carica());
        voci.put("Mostra lista luoghi", () -> mostraLista(false, false, true));
        voci.put("Mostra lista volontari", () -> mostraLista(false, true, false));
        voci.put("Mostra lista visite", () -> mostraLista(true, false, false));
        //voci.put("Inserisci nuovi luoghi e visite", () -> menuInserimentoLuoghi());
        voci.put("Inserisci massimo iscritti", () -> menuInserimentoMassimoIscritti());
        voci.put("Inserisci date precluse", () -> menuInserimentoDate());

        return new CliMenu<String, Runnable>("Menu Configuratore", voci);
    }

    public CliMenu<String, Runnable> creaMenuVolontario() {
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();
        voci.put("Salva sessione", () -> salva());
        voci.put("Carica sessione", () -> carica());
        voci.put("Mostra lista visite a cui sei associato ", () -> mostraVisiteAssociateAlVolontario());
        voci.put("Inserisci disponibilita'", () -> menuInserimentoDisponibilita());

        return new CliMenu<String, Runnable>("Menu Volontario", voci);
    }

    public void stampaMenu(CliMenu<String,Runnable> myMenu) {
        Runnable scelta;
        do {
            scelta = myMenu.scegli();
            if (scelta != null) {
                scelta.run();
            }
        } while (scelta != null);
    }

    public void mostraLista(Boolean visite, Boolean volontari, Boolean luoghi) { 
        //ipotizzo che sia vero un solo booleano quando viene chiamata
        if (luoghi) {
            if (utente.getSession().getLuoghi() == null || utente.getSession().getLuoghi().isEmpty()) {
                System.out.println("Non ci sono luoghi disponibili");
            } else {
                for (Luogo luogo : utente.getSession().getLuoghi()) {
                    System.out.println(luogo.toString());
                }
            }
        } else if (volontari) {
            if (utente.getSession().getVolontari() == null || utente.getSession().getVolontari().isEmpty()) {
                System.out.println("Non ci sono volontari disponibili");
            } else {
                for (Volontario user : utente.getSession().getVolontari()) {
                    System.out.println(user);
                }
            }

        } else if (visite) { //TODO: suddivisione in categorie
            if (utente.getSession().getVisite() == null || utente.getSession().getVisite().isEmpty()) {
                if (utente.getSession().getStoricoVisite() == null || utente.getSession().getStoricoVisite().isEmpty()) {
                    System.out.println("Non ci sono visite disponibili");
                }
            } else {
                for(TipoVisita visita : utente.getSession().getVisite()) {
                    System.out.println("\n" + visita.toString());
                }
            }
            if (utente.getSession().getStoricoVisite() != null && !utente.getSession().getStoricoVisite().isEmpty()) {
                for(TipoVisita visita: utente.getSession().getStoricoVisite()) {
                    System.out.println("\n" + visita.toString());
                }
            }
        }
        else {
            System.out.println("Nessuna lista selezionata");
        }
    }

    public void mostraVisiteAssociateAlVolontario() {
        ArrayList<TipoVisita> visiteAssociate = ((Volontario) utente).getVisiteAssociate();
        if (visiteAssociate.isEmpty()) {
            System.out.println("Non ci sono visite associate al volontario " + utente.getNomeUtente());
        } else {
            for (TipoVisita visita : visiteAssociate) {
                System.out.println(visita.toString());
            }
        }
    }

    public Utente menuInserimentoCredenziali(Utente utente) {
            String nomeUtente, password;

            do {
                System.out.println("\nInserire le credenziali del configuratore: ");
                nomeUtente = InputDati.leggiStringaNonVuota("Inserire il nome utente: ", "Il nome utente non puo' essere vuoto");
                password = InputDati.leggiStringaNonVuota("Inserire la password: ", "La password non puo' essere vuota");
            } while (!(password.equals(Character.toString('0'))) && !(InputDati.conferma("Confermare le credenziali?")));
            
            if (password.equals(Character.toString('0'))) return null;

        return utente.login(nomeUtente, password);
    }

    public void menuCambioPassword() {
        String newPassword;
        do {
            newPassword = InputDati.leggiStringaNonVuota("\nInserire la nuova password: ", "La password non puo' essere vuota");
            if (newPassword.equals(utente.getPassword())) {
                System.out.println("\nLa nuova password non puo' essere uguale a quella attuale");
            }
            if (newPassword.contains("config")) {
                System.out.println("\nLa password non puo' contenere la parola 'config'");
            }
        } while (newPassword.equals(utente.getPassword()) || newPassword.contains("config") || !InputDati.conferma("Confermare la nuova password?"));

        utente.getSession().cambiaPassword(utente, newPassword);
    }

    public void menuInserimentoParametroTerritoriale() { //chiamo poi il metodo di configuratore passandogli il parametro da tastiera
        
        if (Luogo.getParametroTerritoriale() == null) {

            if (utente instanceof Configuratore) {    
                String parametro;
                do {
                    parametro = InputDati.leggiStringaNonVuota("Inserire il parametro territoriale: ", "Il parametro territoriale non puo' essere vuoto");
                } while (!InputDati.conferma("Confermare inserimento?"));

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
                maxIscritti = InputDati.leggiInteroPositivo("Inserire il nuovo numero massimo di iscritti: ", "Numero non valido");
            } while(!InputDati.conferma("\nConferma, nuovo numero massimo di iscritti = " + maxIscritti));
            
            ((Configuratore) utente).setNumeroMassimoIscritti(maxIscritti);
            ((Configuratore) utente).getSession().salvaParametriGlobali();
        }
        else System.out.println("\nPermessi non sufficienti");
    }

    public void menuInserimentoDate() {
        if (utente instanceof Configuratore) {
            int dataInserita;
            Set<Integer> datePrecluse = new HashSet<>();
            Month meseLavoro = CalendarManager.meseDiLavoro(3);

            System.out.println("\nInserire le date precluse per i giorni dal 1 al " + meseLavoro.maxLength() + " " + traduciMese(meseLavoro) + ": ");
            do {
                do {
                    dataInserita = InputDati.leggiInteroMinMax("Inserire una data preclusa (0 per uscire): ", 0, meseLavoro.maxLength(), "Data non valida");
                    if (dataInserita > 0) {
                        datePrecluse.add(dataInserita);
                    }
                } while (dataInserita!=0);
            } while (!InputDati.conferma("Confermare inserimento date?"));
            
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
                    nomeLuogo = InputDati.leggiStringaNonVuota("\nInserire il nome del luogo da inserire: ", "Il nome del luogo non puo' essere vuoto");
                    indirizzoLuogo = InputDati.leggiStringaNonVuota("Inserire l'indirizzo del luogo da inserire: ", "L'indirizzo del luogo non puo' essere vuoto");
                } while(!InputDati.conferma("Conferma inserimento luogo?"));

                Luogo luogo = new Luogo(nomeLuogo, indirizzoLuogo);

                System.out.println("\nInserire almeno un tipo di visita");
                do {
                    tipoVisita = menuInserimentoTipoVisita();
                    if (tipoVisita != null) {
                        visite.add(tipoVisita);
                        luogo.addVisite(tipoVisita.getTitolo());
                    }
                    else break;
                } while (InputDati.conferma("Inserire un altro tipo di visita?"));

               
                luoghi.add(luogo);
                ((Configuratore) utente).inserisciVisite(visite); //Inserisce la/le visite nella session dell'utente
            } while(InputDati.conferma("Inserire un altro luogo?"));

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
            ArrayList<Volontario> volontari = utente.getSession().getVolontari();

            titolo = InputDati.leggiStringaNonVuota("Inserire il titolo della visita: ", "Il titolo della visita non puo' essere vuoto");
            descrizione = InputDati.leggiStringaNonVuota("Inserire la descrizione della visita: ", "La descrizione della visita non puo' essere vuota");
            puntoIncontro = InputDati.leggiStringaNonVuota("Inserire il punto di incontro della visita: ", "Il punto di incontro della visita non puo' essere vuoto");
            
            dataInizio = InputDati.leggiData("Inserisci data inizio ", "/");
            dataFine = InputDati.leggiData("Inserisci data fine ", "/");
            oraInizio = InputDati.leggiOra("Inserisci ora inizio ", ":");
            durata = InputDati.leggiInteroMinMax("Inserisci durata in minuti: ", 1, 600, "Durata non valida");

            do {
                minPartecipante = InputDati.leggiInteroPositivo("Inserisci numero minimo partecipanti: ", "Numero non valido");
                maxPartecipante = InputDati.leggiInteroPositivo("Inserisci numero massimo partecipanti: ", "numero non valido");
                if (minPartecipante > maxPartecipante) System.out.println("Il numero minimo di partecipanti non puo' essere maggiore del numero massimo");
            } while (minPartecipante > maxPartecipante);
            bigliettoIngresso = InputDati.conferma("E' richiesto un biglietto d'ingresso?");

            System.out.println("Inserire i giorni della settimana in cui si svolge la visita: ");
            for (DayOfWeek giorno : DayOfWeek.values()) {
                System.out.print((giorno.getValue()) + ". " + traduciGiorno(giorno) + "\t");
                if (InputDati.conferma("")) {
                    giorniSettimana.add(giorno);
                }
            }

            System.out.println("Inserire i volontari idonei alla visita: ");  
            if (volontari.isEmpty()) {
                System.out.println("Non ci sono volontari disponibili");
            }
            else {
                volontariIdonei = InputDati.selezionaPiuDaLista("Selezionare tra i volontari", volontari, Volontario::getNomeUtente, 1, volontari.size());
            }
            
            return new TipoVisita(titolo, descrizione, puntoIncontro, dataInizio, dataFine, oraInizio, durata,
                    giorniSettimana, minPartecipante, maxPartecipante, bigliettoIngresso, volontariIdonei);
        }
        else {
            System.out.println("Permessi non sufficienti");
            return null;
        }
    }

    public void menuInserimentoDisponibilita() {
        Month meseDiLavoro = CalendarManager.meseDiLavoro(2);
        Set<Integer> disponibilita = new HashSet<>();
        
        disponibilita = InputDati.selezionaDateDaMese(CalendarManager.annoCorrente(), meseDiLavoro, TipoVisita.getDatePrecluseFuture());
        if (disponibilita.isEmpty()) {
            System.out.println("Nessuna disponibilita' selezionata");
            return;
        }   
        if (disponibilita != null) {
            ((Volontario)utente).addDisponibilita(disponibilita);
            return;
        }
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