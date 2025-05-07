package utility;

import application.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;

public class AppView {

    private final CliMenu<String, Runnable> myMenu = new CliMenu<>();

    public void benvenutoMsg(Utente utenteAttivo) {
        System.out.println(BelleStringhe.incornicia(
            "Benvenuto nel sistema di gestione delle visite guidate, utente: " + utenteAttivo.getNomeUtente()));
    }

    public AbstractMap.SimpleEntry<String, String> menuInserimentoCredenziali() {

        String nomeUtente, password;

        do {
            System.out.println("\nInserire le credenziali (inserire '0' nel campo password per uscire dal programma)");
            nomeUtente = InputDati.leggiStringaNonVuota("Inserire il nome utente: ", "Il nome utente non puo' essere vuoto");
            password = InputDati.leggiStringaNonVuota("Inserire la password: ", "La password non puo' essere vuota");
        } while (!(password.equals(Character.toString('0'))) && !(InputDati.conferma("Confermare le credenziali?")));

        if (password.equals(Character.toString('0'))) return null;

        return new AbstractMap.SimpleEntry<>(nomeUtente, password);
    }

    public void erroreLogin() {
        System.out.println("\nUtente non trovato. Nome utente o password errati.");
    }

    public String menuCambioPassword(Utente utenteAttivo) {
        String newPassword;
        do {
            newPassword = InputDati.leggiStringaNonVuota("\nInserire la nuova password: ", "La password non puo' essere vuota");
            if (newPassword.equals(utenteAttivo.getPassword())) {
                System.out.println("\nLa nuova password non puo' essere uguale a quella attuale");
            }
            if (newPassword.contains("config")) {
                System.out.println("\nLa password non puo' contenere la parola 'config'");
            }
        } while (newPassword.equals(utenteAttivo.getPassword()) || newPassword.contains("config") || !InputDati.conferma("Confermare la nuova password?"));

        System.out.println("Password cambiata con successo");
        return newPassword;
    }

    public Luogo menuInserimentoLuogo(Set<Luogo> luoghiPresenti) {
        System.out.println("Si inseriscano luoghi e visite da includere nell'applicazione");
        String nomeLuogo, indirizzoLuogo;
        boolean luogoEsistente = false;

        do {
            do {
                nomeLuogo = InputDati.leggiStringaNonVuota("\nInserire il nome del luogo da inserire: ", "Il nome del luogo non puo' essere vuoto");
                for (Luogo luogo : luoghiPresenti) {
                    if (luogo.getNome().trim().equals(nomeLuogo.trim())) {
                        System.out.println("Un luogo con questo nome è già presente, sceglierne un altro");
                        luogoEsistente = true;
                        break;
                    }
                }
            } while (luogoEsistente);
            
            indirizzoLuogo = InputDati.leggiStringaNonVuota("Inserire l'indirizzo del luogo da inserire: ", "L'indirizzo del luogo non puo' essere vuoto");
        } while(!InputDati.conferma("Conferma inserimento luogo?"));

        return new Luogo(nomeLuogo, indirizzoLuogo);

    }

    public boolean confermaLuoghi() {
        return InputDati.conferma("Inserire un altro luogo?");
    }

    public Set<Luogo> menuRimozioneLuoghi(Set<Luogo> luoghiPresenti) {
        Set<Luogo> luoghiDaRimuovere = new HashSet<>();
        if (!luoghiPresenti.isEmpty()) {
            luoghiDaRimuovere = InputDati.selezionaPiuDaLista("Selezionare uno o più luoghi da eliminare", 
                                    luoghiPresenti, Luogo::getNome, 1, luoghiPresenti.size());
        } else {
            System.out.println("Impossibile eliminare, non ci sono luoghi nel database");
        }
        return luoghiDaRimuovere;
    }

    public Luogo selezioneLuogo(Set<Luogo> luoghiPresenti) {
        Luogo luogoSelezionato = null;
        if (!luoghiPresenti.isEmpty()) {
            luogoSelezionato = InputDati.selezionaUnoDaLista(
            "Selezionare un luogo a cui associare la nuova visita", luoghiPresenti, Luogo::getNome); 
        } else {
            System.out.println("Azione impossibile, non ci sono luoghi nel database");
        }
        return luogoSelezionato;
    }

    public Set<TipoVisita> menuInserimentoTipiVisita(Utente utenteAttivo, Set<Utente> utenti, Set<Volontario> volontari, Controller controller) {
        TipoVisita tipoVisita;
        Set<TipoVisita> visite = new HashSet<>();

        if (utenteAttivo instanceof Configuratore) {
            System.out.println("\nInserire almeno un tipo di visita");
            do {
                tipoVisita = menuInserimentoTipoVisita(utenti, volontari, controller);
                visite.add(tipoVisita);
            } while (InputDati.conferma("Inserire un altro tipo di visita?"));
        } else {
                System.out.println("Permessi non sufficienti");
                return null;
            }

        return visite;
    }

    private TipoVisita menuInserimentoTipoVisita(Set<Utente> utenti, Set<Volontario> volontari, Controller controller) {
            String titolo, descrizione, puntoIncontro;
            Calendar dataInizio, dataFine, oraInizio;
            int durata, minPartecipante, maxPartecipante;
            boolean bigliettoIngresso;
            Set<DayOfWeek> giorniSettimana = new HashSet<>();
            Set<Volontario> volontariIdonei;

            titolo = InputDati.leggiStringaNonVuota("Inserire il titolo della visita: ", "Il titolo della visita non puo' essere vuoto");
            descrizione = InputDati.leggiStringaNonVuota("Inserire la descrizione della visita: ", "La descrizione della visita non puo' essere vuota");
            puntoIncontro = InputDati.leggiStringaNonVuota("Inserire il punto di incontro della visita: ", "Il punto di incontro della visita non puo' essere vuoto");

            dataInizio = InputDati.leggiData("Inserisci data inizio (dd/mm)", "/");
            dataInizio.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            dataFine = InputDati.leggiData("Inserisci data fine (dd/mm)", "/");
            dataFine.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            oraInizio = InputDati.leggiOra("Inserisci ora inizio (hh:mm)", ":");

            durata = InputDati.leggiInteroMinMax("Inserisci durata in minuti: ", 1, 600, "Durata non valida");

            do {
                minPartecipante = InputDati.leggiInteroPositivo("Inserisci numero minimo partecipanti: ", "Numero non valido");
                maxPartecipante = InputDati.leggiInteroPositivo("Inserisci numero massimo partecipanti: ", "numero non valido");
                if (minPartecipante > maxPartecipante) System.out.println("Il numero minimo di partecipanti non puo' essere maggiore del numero massimo");
            } while (minPartecipante > maxPartecipante);
            bigliettoIngresso = InputDati.conferma("E' richiesto un biglietto d'ingresso?");

            System.out.println("Inserire i giorni della settimana in cui si svolge la visita: ");
            for (DayOfWeek giorno : DayOfWeek.values()) {
                System.out.print(BelleStringhe.traduciGiorno(giorno) + "\t\t");
                if (InputDati.conferma("")) {
                    giorniSettimana.add(giorno);
                }
            }

            System.out.println("Inserire i volontari idonei alla visita: ");
            if (volontari.isEmpty()) {
                System.out.println("Non ci sono volontari nel database, è necessario crearne uno");
                volontariIdonei = menuInserimentoVolontari(utenti);
            }
            else {
                volontariIdonei = InputDati.selezionaPiuDaLista("Selezionare tra i volontari", volontari, Volontario::getNomeUtente, 0, volontari.size());
                if (volontariIdonei.isEmpty()) {
                    System.out.println("Nessun volontario selezionato, è necessario crearne uno");
                    volontariIdonei = menuInserimentoVolontari(utenti);
                    controller.aggiungiVolontariInSession(volontariIdonei);
                } else {
                    if (InputDati.conferma("Si vuole creare un nuovo volontario da aggiungere alla visita oltre a quelli già selezionati?")) {
                        volontariIdonei.addAll(menuInserimentoVolontari(utenti));
                        controller.aggiungiVolontariInSession(volontariIdonei);
                    }
                }
            }

            return new TipoVisita(titolo, descrizione, puntoIncontro, dataInizio, dataFine, oraInizio, durata,
                    giorniSettimana, minPartecipante, maxPartecipante, bigliettoIngresso, volontariIdonei);
        }

    public Set<TipoVisita> menuRimozioneTipoVisita(Set<TipoVisita> tipiVisitaPresenti) {
        Set<TipoVisita> tipiVisitaDaRimuovere = new HashSet<>();
        if (!tipiVisitaPresenti.isEmpty()) {
            tipiVisitaDaRimuovere = InputDati.selezionaPiuDaLista("Selezionare uno o più tipi di visita da eliminare", 
                                        tipiVisitaPresenti, TipoVisita::getTitolo, 1, tipiVisitaPresenti.size());            
        } else {
            System.out.println("Impossibile eliminare, non ci sono tipi di visita nel database");
        }
        return tipiVisitaDaRimuovere;
    }

    public TipoVisita selezioneTipoVisita(Set<TipoVisita> tipiVisitaPresenti) {
        TipoVisita tipoVisitaSelezionato = null;
        if (!tipiVisitaPresenti.isEmpty()) {
            tipoVisitaSelezionato = InputDati.selezionaUnoDaLista(
            "Selezionare un tipo di visita a cui associare il nuovo volontario", tipiVisitaPresenti, TipoVisita::getTitolo); 
        } else {
            System.out.println("Azione impossibile, non ci sono tipi di visita nel database");
        }
        return tipoVisitaSelezionato;
    }

    public Set<Volontario> menuInserimentoVolontari(Set<Utente> utentiPresenti) {
        String nomeUtente, password;
        Volontario nuovoVolontario;
        Set<Volontario> nuoviVolontari = new HashSet<>();

        System.out.println("\nInserire almeno un nuovo volontario");
        do {
            nomeUtente = inserimentoNomeUtente("Inserire il nome utente del volontario: ", utentiPresenti);
            
            password = "config" + nomeUtente;

            nuovoVolontario = new Volontario(nomeUtente, password, new HashSet<>());
            nuoviVolontari.add(nuovoVolontario);
        } while (InputDati.conferma("Inserire un altro volontario?"));

        return nuoviVolontari;
        }

    public String inserimentoNomeUtente(String msg, Set<Utente> utentiPresenti) {
        String nomeUtente;
        boolean giaEsistente;
        do {
            giaEsistente = false;
            nomeUtente = InputDati.leggiStringaNonVuota(msg, "Il nome utente non puo' essere vuoto");
            for (Utente utente : utentiPresenti) {
                if (utente.getNomeUtente().trim().equals(nomeUtente.trim())) {
                    System.out.println("Esiste già un utente con questo nome, sceglierne un altro");
                    giaEsistente = true;
                    break;
                }
            }
        } while (giaEsistente || !InputDati.conferma("Confermare il nome utente?"));
        return nomeUtente;
    }

    public String inserimentoPassword(String msg) {
        String password;
        boolean check;

        do {
            check = false;
            password = InputDati.leggiStringaNonVuota(msg, "La password non puo' essere vuota");
            if (password.contains("config")) {
                check = true;
                System.out.println("La password contiene parole riservate");
            }
            if (password.trim().length() < 8) {
                check = true;
                System.out.println("La password deve essere lunga almeno 8 caratteri");
            }
        } while(check || !InputDati.conferma("Confermare la password?"));

        return password;
    }

    public Set<Volontario> menuRimozioneVolontario(Set<Volontario> volontariPresenti) {
        Set<Volontario> volontariDaRimuovere = new HashSet<>();
        if (!volontariPresenti.isEmpty()) {
            volontariDaRimuovere = InputDati.selezionaPiuDaLista("Selezionare uno o più volontari da eliminare", 
                                        volontariPresenti, Volontario::getNomeUtente, 1, volontariPresenti.size());
        } else {
            System.out.println("Impossibile eliminare, non ci sono volontari nel database");
        }
        return volontariDaRimuovere;
    }


    public String menuInserimentoParametroTerritoriale() { 
        String parametro;
        do {
            parametro = InputDati.leggiStringaNonVuota("Inserire il parametro territoriale: ", "Il parametro territoriale non puo' essere vuoto");
        } while (!InputDati.conferma("Confermare inserimento?"));
        return parametro;

    }

    public int menuInserimentoMassimoIscritti() {
        System.out.println("\nSi inizializzi il numero massimo di iscritti per visita");

        int maxIscritti;
        do {
            maxIscritti = InputDati.leggiInteroPositivo("Inserire il nuovo numero massimo di iscritti: ", "Numero non valido");
        } while(!InputDati.conferma("\nConferma, nuovo numero massimo di iscritti = " + maxIscritti));

        return maxIscritti;
    }

    public void setMenuStart (Controller controller) {
        myMenu.removeAllVoci();
        myMenu.setTitolo("Avvio Applicazione");
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();

        voci.put("Login", controller::loginCredenziali);
        voci.put("Registrati", controller::registrazioneFruitore);
        voci.put("Disabilita colori e caratteri speciali (se la visualizzazione di questo menu presenta dei problemi)", controller::disabilitaColoriCaratteriSpeciali);

        myMenu.addVoci(voci);
    }

    public void setMenuConfiguratore(Controller controller) {
        myMenu.removeAllVoci();
        myMenu.setTitolo("Menu configuratore");
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();

        voci.put("Salva sessione", controller::salva);
        voci.put("Carica sessione", controller::carica);
        voci.put("Mostra lista luoghi", controller::mostraLuoghi);
        voci.put("Mostra lista volontari", controller::mostraVolontari);
        voci.put("Mostra lista visite", controller::mostraTipiVisite);
        voci.put("Inserisci massimo iscritti", controller::dichiaraMassimoNumeroFruitori);
        voci.put("Inserisci date precluse", controller::inserisciDatePrecluse);
        voci.put("Mostra visite separate per stato", controller::mostraVisitePerStato);
        myMenu.addVoci(voci);
    }

    public void setMenuConfiguratoreGestioneRaccoltaDisponibilitaStart(Controller controller) {
        myMenu.removeAllVoci();
        setMenuConfiguratore(controller);

        Map.Entry<String, Runnable> voce = Map.entry("Chiudere raccolta disponibilità e produci piano delle visite",
                controller::chiudiDisponibilitaERealizzaPianoVisite);
        myMenu.addVoce(voce);

    }

    public void setMenuConfiguratoreEditor(Controller controller) {
        myMenu.removeAllVoci();
        setMenuConfiguratore(controller);

        Map.Entry<String, Runnable> voce = Map.entry("Chiudere raccolta disponibilità e produci piano delle visite",
                controller::inizializzaPianoViste);
        myMenu.removeVoce(voce);

        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();
        voci.put("Aggiungi un nuovo luogo", controller::creaLuoghi);
        voci.put("Aggiungi un nuovo tipo visita ad un luogo esistente", controller::aggiungiTipoVisita);
        voci.put("Aggiungi un nuovo volontario a un tipo visita esistente", controller::aggiungiVolontario);
        voci.put("Elimina un luogo", controller::rimuoviLuogo);
        voci.put("Elimina un tipo visita", controller::rimuoviTipoVisita);
        voci.put("Elimina un volontario", controller::rimuoviVolontario);
        voci.put("Apri raccolta disponibilità", controller::riapriDisponibilita);
        myMenu.addVoci(voci);
    }

    public void setMenuVolontario(Controller controller) {
        myMenu.removeAllVoci();
        myMenu.setTitolo("Menu volontario");
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();
        voci.put("Mostra i tipi di visite a cui sei associato", controller::mostraTipiVisiteAssociate);        
        voci.put("Mostra le tue visite confermate con le relative iscrizioni", controller::mostraVisiteConfermateConIscrizioni);
        voci.put("Inserisci disponibilita'", controller::inserisciDisponibilita);

        myMenu.addVoci(voci);
    }

    public void setMenuFruitore(Controller controller) {
        myMenu.removeAllVoci();
        myMenu.setTitolo("Menu Fruitore");
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();
        voci.put("Visualizza visite proposte/confermate/cancellate", controller::mostraVisitePerStato);
        voci.put("Visualizza le visite a cui hai effettuato un'iscrizione", controller::mostraIscrizioniFruitore);
        voci.put("Iscrivi persone a una visita ", controller::iscrizioneFruitore);
        voci.put("Annulla un'iscrizione", controller::annullaIscrizione);

        myMenu.addVoci(voci);
    }

    public void stampaMenu() {
        Runnable scelta;
        do {
            scelta = myMenu.scegli();
            if (scelta != null) {
                scelta.run();
            }
        } while (scelta != null);
    }

    public Runnable stampaMenuOnce() {
        Runnable scelta = myMenu.scegli();
        if (scelta != null) {
            scelta.run();
            return scelta;
        }
        else return null;
    }

    public Set<Integer> menuInserimentoDatePrecluse(Set<Integer> datePrecluseFuture) {
        Month meseDiLavoro = CalendarManager.meseDiLavoro(3);
        Set<Integer> nuoveDatePrecluse = InputDati.selezionaDateDaMese(
            CalendarManager.annoCorrente(), meseDiLavoro, Collections.emptySet(), datePrecluseFuture);

        if (nuoveDatePrecluse.isEmpty()) {
            System.out.println("Nessuna data preclusa inserita");
        }
        return nuoveDatePrecluse;
    }

    public void mostraLuoghi(Set<Luogo> luoghi) {
        if (luoghi == null || luoghi.isEmpty()) {
            System.out.println("Non ci sono luoghi disponibili");
        } else {
            System.out.println(BelleStringhe.incornicia("Luoghi Presenti nel territorio di " + Luogo.getParametroTerritoriale()));
            for (Luogo luogo : luoghi) {
                System.out.println(formattaLuogo(luogo) + "\n");
            }
        }
    }

    public void mostraVolontari(Set<Volontario> volontari) {
        if (volontari == null || volontari.isEmpty()) {
            System.out.println("Non ci sono volontari disponibili");
        } else {
            System.out.println("Volontari Presenti: ");
            for (Volontario user : volontari) {
                System.out.println(user.getNomeUtente());
            }
        }
    }

    public void mostraTipiVisite(Set<TipoVisita> tipiVisita, HashMap<String, Set<Visita>> storicoVisite) {
        if ((tipiVisita == null || tipiVisita.isEmpty()) && (storicoVisite == null || storicoVisite.isEmpty())) {
            System.out.println("Non ci sono visite disponibili");
            return;
        }

        if (tipiVisita != null && !tipiVisita.isEmpty()) {
            for(TipoVisita tipoVisita : tipiVisita) {
                System.out.println("\n" + formattaTipoVisita(tipoVisita));
            }
        }
        if (storicoVisite != null && !storicoVisite.isEmpty()) {
            System.out.println("\nVisite effettuate mantenute nell'archivio: ");
            for (Map.Entry<String, Set<Visita>> entry: storicoVisite.entrySet()) {
                for(Visita visita: entry.getValue()) {
                    System.out.println(entry.getKey() + "\t\t" + formattaVisitaArchivio(visita));
                }
            }
        }
    }

    public void mostraVisiteStato(Map<StatoVisita, List<Visita>> visitePerStato, Map<String, Set<Visita>> storicoVisite, Controller controller) {
        if (!visitePerStato.isEmpty()) {
            for (Map.Entry<StatoVisita, List<Visita>> entry : visitePerStato.entrySet()) {
                System.out.println("\nStato: " + entry.getKey());
                if (!entry.getValue().isEmpty()) {
                    for (Visita visita : entry.getValue()) {
                        System.out.println(formattaVisita(visita, controller));
                    }
                } else System.out.println("Nessuna visita associata a questo stato");
            }
        } else System.out.println("Non ci sono visite al di fuori dell'archivio storico");
    

        if (!storicoVisite.isEmpty()) {
            System.out.println("\nStato: " + StatoVisita.EFFETTUATA);
            for (Map.Entry<String, Set<Visita>> entry : storicoVisite.entrySet()) {
                for (Visita visita : entry.getValue()) {
                    System.out.println(entry.getKey() + "\t\t" + formattaVisitaArchivio(visita));
                }
            }
        }
    }

    /**
     * Stampa le visite negli stati proposta/confermata/cancellata a cui è iscritto il fruitore (utente attivo)
     */
    public void mostraVisiteStatoConIscrizioni(Map<StatoVisita, Map<Visita, Iscrizione>> visiteConIscrizioniPerStato, Controller controller) {
        if (!visiteConIscrizioniPerStato.isEmpty()) {
            for (Map.Entry<StatoVisita, Map<Visita, Iscrizione>> entry : visiteConIscrizioniPerStato.entrySet()) {
                System.out.println("\nStato: " + entry.getKey());
                if (!entry.getValue().isEmpty()) {

                    for (Map.Entry<Visita, Iscrizione> visiteIscrizioni : entry.getValue().entrySet()) {
                        System.out.println(formattaVisita(visiteIscrizioni.getKey(), controller));
                        System.out.println(formattaIscrizione(visiteIscrizioni.getValue()));
                    }
                } else System.out.println("Nessuna iscrizione a visite in questo stato");
            
            }
        } else {
            System.out.println("Non sei iscritto a nessuna visita\n");
        }
    }

    /**
     * Stampa le visite confermate a cui il volontario (utente attivo) deve presenziare, con le relative iscrizioni
     */
    public void mostraVisiteConfermateConIscrizioni(Map<Visita, Set<Iscrizione>> visiteConfermateConIscrizioni, Controller controller) {
        if (!visiteConfermateConIscrizioni.isEmpty()) {
            for (Map.Entry<Visita, Set<Iscrizione>> entry : visiteConfermateConIscrizioni.entrySet()) {
                System.out.println("\n" + formattaVisita(entry.getKey(), controller));
                if (!entry.getValue().isEmpty()) {
                    for (Iscrizione iscrizione : entry.getValue()) {
                        System.out.println(formattaIscrizione(iscrizione));
                    }
                } else System.out.println("Nessuna iscrizione a questa visita");
            }
        } else {
            System.out.println("Non ci sono visite confermate");
        }
    }
   
    public void mostraTipiVisiteAssociateAlVolontario(Set<TipoVisita> visiteAssociate) {
        if (!visiteAssociate.isEmpty()) {
            for (TipoVisita tipoVisita : visiteAssociate) {
                System.out.println("\n" + formattaTipoVisita(tipoVisita));
            }
            System.out.println();
        } else System.out.println("Non ci sono visite associate al volontario");
    }

    public Set<Integer> menuInserimentoDisponibilita(Set<Integer> disponibilita) {
        Month meseDiLavoro = CalendarManager.meseDiLavoro(2);
        Set<Integer> nuoveDisponibilita = InputDati.selezionaDateDaMese
            (CalendarManager.annoCorrente(), meseDiLavoro, TipoVisita.getDatePrecluseFuture(), disponibilita);

        if (nuoveDisponibilita.isEmpty()) {
            System.out.println("Nessuna disponibilita' selezionata");
        }
        return nuoveDisponibilita;
    }

    public AbstractMap.SimpleEntry<Visita, Integer> menuIscrizione(Set<Visita> visiteProposte, Controller controller, int maxIscrittiperFruitore) {
        AbstractMap.SimpleEntry<Visita, Integer> iscrizione = null;
        Visita visita;
        int numeroIscritti;

        if (!visiteProposte.isEmpty()) {
            do {
                visita = InputDati.selezionaUnoDaLista("Selezionare una visita a cui iscriversi", visiteProposte, Visita::getIdentificativo);
                
                if (visita == null) return null;      //uscita dal menu
                
                TipoVisita tipoVisita = controller.getTipoVisitaAssociato(visita.getTitolo());
                int maxIscrivibili = Math.min(tipoVisita.getMaxPartecipante() - visita.getNumeroIscritti(), maxIscrittiperFruitore);
                numeroIscritti = InputDati.leggiInteroMinMax(String.format("Quante persone si vogliono iscrivere (massimo %d) ? ", maxIscrivibili), 0, maxIscrivibili, "Numero non valido");        
                if (numeroIscritti == 0) return null;    
            } while (!InputDati.conferma("Confermare iscrizione?"));

            iscrizione = new AbstractMap.SimpleEntry<>(visita, numeroIscritti);
        }
        else System.out.println("Non ci sono visite proposte a cui iscriversi");

        return iscrizione;
    }

    public Visita menuDisiscrizione(Set<Visita> visiteIscritte) {
        Visita visitaSelezionata = null;

        if (!visiteIscritte.isEmpty()) {
            do {
                visitaSelezionata = InputDati.selezionaUnoDaLista("Selezionare la visita di cui annullare l'iscrizione", visiteIscritte, Visita::getIdentificativo);
                if (visitaSelezionata == null) return null;     //uscita dal menu
    
            } while (!InputDati.conferma("Confermare disiscrizione?"));
        }
        else System.out.println("Non si è iscritti a nessuna visita");

        return visitaSelezionata;
    }

    public String formattaLuogo(Luogo luogo) {
        StringBuilder sb = new StringBuilder();

        if (luogo == null) return "Luogo_null";
        else {  
            sb.append(luogo.getNome());
            sb.append("\t\tIndirizzo: ").append(luogo.getIndirizzo()).append("\n");
            sb.append("Tipi di visita svolti qui: ").append(luogo.getVisiteIds());
        }
        
        return sb.toString();  
    }


    public String formattaTipoVisita(TipoVisita tipoVisita) {
        if (tipoVisita == null) return "TipoVisita_null";
        
        StringBuilder sb = new StringBuilder();
        
        StringBuilder volontari = new StringBuilder();
        if (tipoVisita.getVolontariIdonei() != null) {
            for (Volontario volontario : tipoVisita.getVolontariIdonei()) {
                volontari.append(volontario.getNomeUtente()).append(", ");
            }
            if (!volontari.isEmpty()) {
                volontari.setLength(volontari.length() - 2); // Remove trailing comma and space
            }
        }

        StringBuilder visiteID = new StringBuilder();
        if (tipoVisita.getVisiteAssociate() != null) {        
            for (Visita visita : tipoVisita.getVisiteAssociate()) {
                visiteID.append(visita.getDataStato()).append("; ");
            }
            if (!visiteID.isEmpty()) {
                visiteID.setLength(visiteID.length() - 2); // Remove trailing comma and space
            }
        }

        sb.append("Titolo:\t\t\t").append(tipoVisita.getTitolo()).append("\n");
        sb.append("Descrizione:\t\t").append(tipoVisita.getDescrizione()).append("\n");
        sb.append("Punto di incontro:\t").append(tipoVisita.getPuntoIncontro()).append("\n");
        sb.append("Data inizio:\t\t").append(formattaData(tipoVisita.getDataInizio())).append("\n");
        sb.append("Data fine:\t\t").append(formattaData(tipoVisita.getDataFine())).append("\n");
        sb.append("Ora inizio:\t\t").append(formattaOra(tipoVisita.getOraInizio())).append("\n");
        sb.append("Durata:\t\t\t").append(tipoVisita.getDurata()).append(" minuti\n");

        sb.append("Giorni della settimana:\t[");
        List<DayOfWeek> giorniOrdinati = tipoVisita.getGiorniSettimana().stream().sorted(Comparator.comparingInt(DayOfWeek::getValue)).toList();
        for (DayOfWeek giorno : giorniOrdinati) {
            sb.append(BelleStringhe.traduciGiorno(giorno)).append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append("]\n");

        sb.append("Minimo partecipanti:\t").append(tipoVisita.getMinPartecipante()).append("\n");
        sb.append("Massimo partecipanti:\t").append(tipoVisita.getMaxPartecipante()).append("\n");
        sb.append("Biglietto d'ingresso:\t").append(tipoVisita.getBigliettoIngresso() ? "" : "non ").append("necessario\n");
        sb.append("Volontari idonei:\t[").append(volontari).append("]\n");
        sb.append("Visite associate:\t[").append(visiteID).append("]\n");

        return sb.toString();
    }

    public String formattaData(Calendar data) {
        if (data == null) return "Data_null";
        return data.get(Calendar.DAY_OF_MONTH) + "/" + (data.get(Calendar.MONTH) + 1) + "/" + data.get(Calendar.YEAR);
    }

    public String formattaOra(Calendar ora) {
        if (ora == null) return "Ora_null";
        return ora.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", ora.get(Calendar.MINUTE));
    }

    public String formattaVisita(Visita visita, Controller controller) {

        // TODO remove getInstance
        TipoVisita tipoVisita = controller.getTipoVisitaAssociato(visita.getTitolo());
    
        StringBuilder sb = new StringBuilder();
        
        if (visita.getStato() == StatoVisita.EFFETTUATA) return formattaVisitaArchivio(visita);

        if (visita.getStato() == StatoVisita.CANCELLATA) {
            sb.append("Titolo: ").append(visita.getTitolo()).append("\n");
            sb.append("Data di mancato svolgimento: ").append(formattaData(visita.getDataVisita())).append("\n");
            return sb.toString();
        }

        sb.append("Titolo: ").append(visita.getTitolo()).append("\n");

        if (tipoVisita == null) {
            sb.append("tipoVisita null");
            return sb.toString();
        }

        sb.append("Descrizione: ").append(tipoVisita.getDescrizione()).append("\t\t");
        sb.append("Punto di incontro: ").append(tipoVisita.getPuntoIncontro()).append("\n");
        sb.append("Data di svolgimento: ").append(formattaData(visita.getDataVisita())).append("\n");
        sb.append("Ora inizio: ").append(formattaOra(tipoVisita.getOraInizio())).append("\n");
        sb.append("Biglietto di ingresso").append(tipoVisita.getBigliettoIngresso() ? " " : " non ").append("necessario\n");
    
        return sb.toString();
    }

    public String formattaVisitaArchivio(Visita visita) {
        return "Data svolgimento " + formattaData(visita.getDataVisita());
    }

    private String formattaIscrizione(Iscrizione iscrizione) {
        if (iscrizione == null) return "Iscrizione_null";

        String s = "Iscrizione #";
        s += iscrizione.getCodiceUnivoco() + " : ";
        s += iscrizione.getNumeroDiIscritti() + " iscritti\n";
        return s;
    }
}