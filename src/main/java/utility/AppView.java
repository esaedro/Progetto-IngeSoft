package utility;

import application.*;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;

public class AppView {

    CliMenu<String, Runnable> myMenu = new CliMenu<>();

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

    public Set<TipoVisita> menuInserimentoTipiVisita(Utente utenteAttivo, Set<Volontario> volontari) {
        TipoVisita tipoVisita;
        Set<TipoVisita> visite = new HashSet<>();

        System.out.println("\nInserire almeno un tipo di visita");
        do {
            tipoVisita = menuInserimentoTipoVisita(utenteAttivo, volontari);
            if (tipoVisita != null) {
                visite.add(tipoVisita);
            }
            else break;
        } while (InputDati.conferma("Inserire un altro tipo di visita?"));

        return visite;
    }

    private TipoVisita menuInserimentoTipoVisita(Utente utenteAttivo, Set<Volontario> volontari) {
        if (utenteAttivo instanceof Configuratore) {
            String titolo, descrizione, puntoIncontro;
            Calendar dataInizio, dataFine, oraInizio;
            int durata, minPartecipante, maxPartecipante;
            boolean bigliettoIngresso;
            Set<DayOfWeek> giorniSettimana = new HashSet<>();
            Set<Volontario> volontariIdonei = new HashSet<>();
            Controller controller = Controller.getIstance();

            titolo = InputDati.leggiStringaNonVuota("Inserire il titolo della visita: ", "Il titolo della visita non puo' essere vuoto");
            descrizione = InputDati.leggiStringaNonVuota("Inserire la descrizione della visita: ", "La descrizione della visita non puo' essere vuota");
            puntoIncontro = InputDati.leggiStringaNonVuota("Inserire il punto di incontro della visita: ", "Il punto di incontro della visita non puo' essere vuoto");

            dataInizio = InputDati.leggiData("Inserisci data inizio ", "/");
            dataInizio.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            dataFine = InputDati.leggiData("Inserisci data fine ", "/");
            dataFine.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
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
                System.out.print((giorno.getValue()) + ". " + BelleStringhe.traduciGiorno(giorno) + "\t");
                if (InputDati.conferma("")) {
                    giorniSettimana.add(giorno);
                }
            }

            System.out.println("Inserire i volontari idonei alla visita: ");
            if (volontari.isEmpty()) {
                System.out.println("Non ci sono volontari nel database, è necessario crearne uno");
                volontariIdonei = menuInserimentoVolontari(volontari);
            }
            else {
                volontariIdonei = InputDati.selezionaPiuDaLista("Selezionare tra i volontari", volontari, Volontario::getNomeUtente, 0, volontari.size());
                if (volontariIdonei.isEmpty()) {
                    System.out.println("Nessun volontario selezionato, è necessario crearne uno");
                    volontariIdonei = menuInserimentoVolontari(volontari);
                    controller.aggiungiVolontariInSession(volontariIdonei);
                } else {
                    if (InputDati.conferma("Si vuole creare un nuovo volontario da aggiungere alla visita oltre a quelli già selezionati?")) {
                        volontariIdonei.addAll(menuInserimentoVolontari(volontari));
                        controller.aggiungiVolontariInSession(volontariIdonei);
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

    public Set<Volontario> menuInserimentoVolontari(Set<Volontario> volontariPresenti) {
        String nomeUtente, password;
        boolean esisteVolontario = false;
        Volontario nuovoVolontario;
        Set<Volontario> nuoviVolontari = new HashSet<>();

        System.out.println("\nInserire almeno un nuovo volontario");
        do {
            do {
                nomeUtente = InputDati.leggiStringaNonVuota("Inserire il nome utente del volontario: ", "Il nome utente non puo' essere vuoto");
                for (Volontario vol : volontariPresenti) {
                    if (vol.getNomeUtente().trim().equals(nomeUtente.trim())) {
                        System.out.println("Esiste già un volontario con questo nome utente, sceglierne un altro");
                        esisteVolontario = true;
                        break;
                    }
                }
            } while (esisteVolontario);
            
            password = "config" + nomeUtente;

            nuovoVolontario = new Volontario(nomeUtente, password, new HashSet<>());
            nuoviVolontari.add(nuovoVolontario);
        } while (InputDati.conferma("Inserire un altro volontario?"));

        return nuoviVolontari;
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

    public void setMenuConfiguratore() {
        myMenu.removeAllVoci();
        myMenu.setTitolo("Menu configuratore");
        Controller controller = Controller.getIstance();
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();

        voci.put("Salva sessione", controller::salva);
        voci.put("Carica sessione", controller::carica);
        voci.put("Mostra lista luoghi", controller::mostraLuoghi);
        voci.put("Mostra lista volontari", controller::mostraVolontari);
        voci.put("Mostra lista visite", controller::mostraTipiVisite);
        voci.put("Inserisci massimo iscritti", controller::dichiaraMassimoNumeroFruitori);
        voci.put("Inserisci date precluse", controller::inserisciDatePrecluse);
        voci.put("Mostra visite separate per stato", controller::mostraVisite);
        myMenu.addVoci(voci);
    }

    public void setMenuConfiguratoreGestioneRaccoltaDisponibilitaStart() {
        myMenu.removeAllVoci();
        setMenuConfiguratore();

        Controller controller = Controller.getIstance();
        Map.Entry<String, Runnable> voce = Map.entry("Chiudere raccolta disponibilità e produci piano delle visite",
                controller::chiudiDisponibilitaERealizzaPianoVisite);
        myMenu.addVoce(voce);

    }

    public void setMenuConfiguratoreEditor() {
        myMenu.removeAllVoci();
        setMenuConfiguratore();

        Controller controller = Controller.getIstance();

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
    //TODO: Voci del menu, interazione utente da View ed elaborazione aggiunte/rimozioni da Controller


    public void setMenuVolontario() {
        myMenu.removeAllVoci();
        myMenu.setTitolo("Menu volontario");
        Controller controller = Controller.getIstance();
        LinkedHashMap<String, Runnable> voci = new LinkedHashMap<>();
        voci.put("Salva sessione", controller::salva);
        voci.put("Carica sessione", controller::carica);
        voci.put("Mostra lista visite a cui sei associato ", controller::mostraVisiteAssociate);
        voci.put("Inserisci disponibilita'", controller::inserisciDisponibilita);

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

    public Set<Integer> menuInserimentoDate() {
        int dataInserita;
        Set<Integer> datePrecluse = new HashSet<>();
        Month meseLavoro = CalendarManager.meseDiLavoro(3);

        System.out.println("\nInserire le date precluse per i giorni dal 1 al " + meseLavoro.maxLength() + " " + BelleStringhe.traduciMese(meseLavoro) + ": ");
        do {
            do {
                dataInserita = InputDati.leggiInteroMinMax("Inserire una data preclusa (0 per uscire): ", 0, meseLavoro.maxLength(), "Data non valida");
                if (dataInserita > 0) {
                    datePrecluse.add(dataInserita);
                }
            } while (dataInserita!=0);
        } while (!InputDati.conferma("Confermare inserimento date?"));

        return datePrecluse;
    }

    public void mostraLuoghi(Set<Luogo> luoghi) {
        if (luoghi == null || luoghi.isEmpty()) {
            System.out.println("Non ci sono luoghi disponibili");
        } else {
            for (Luogo luogo : luoghi) {
                System.out.println(luogo.toString());
            }
        }
    }

    public void mostraVolontari(Set<Volontario> volontari) {
        if (volontari == null || volontari.isEmpty()) {
            System.out.println("Non ci sono volontari disponibili");
        } else {
            for (Volontario user : volontari) {
                System.out.println(user.getNomeUtente());
            }
        }
    }

    public void mostraTipiVisite(Set<TipoVisita> tipiVisita, Set<TipoVisita> storicoVisite) {
        if ((tipiVisita == null || tipiVisita.isEmpty()) && (storicoVisite == null || storicoVisite.isEmpty())) {
            System.out.println("Non ci sono visite disponibili");
            return;
        }
        if (tipiVisita != null && !tipiVisita.isEmpty()) {
            for(TipoVisita visita : tipiVisita) {
                System.out.println("\n" + visita.toString());
            }
        }
        if (storicoVisite != null && !storicoVisite.isEmpty()) {
            for(TipoVisita visita: storicoVisite) {
                System.out.println("\n" + visita.toString());
            }
        }
    }

    public void mostraVisite(Map<StatoVisita, List<Visita>> visitePerStato) {
        if (visitePerStato.isEmpty()) {
            System.out.println("Non ci sono visite");
        } else {
            for (Map.Entry<StatoVisita, List<Visita>> entry : visitePerStato.entrySet()) {
                System.out.println("\nStato: " + entry.getKey());
                if (entry.getValue().isEmpty()) {
                    System.out.println("Nessuna visita associata a questo stato");
                } else {
                    for (Visita visita : entry.getValue()) {
                        System.out.println(visita.toString());
                    }
                }
            }
        }
    }

    public void mostraVisiteAssociateAlVolontario(Set<TipoVisita> visiteAssociate) {
        if (visiteAssociate.isEmpty()) {
            System.out.println("Non ci sono visite associate al volontario");
        } else {
            for (TipoVisita visita : visiteAssociate) {
                System.out.println("\n" + visita.toString());
            }
            System.out.println();
        }
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

}