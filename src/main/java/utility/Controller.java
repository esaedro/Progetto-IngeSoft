package utility;

import application.*;
import java.util.*;

public class Controller {

    private static final Controller controller = new Controller();
    Session session = new Session();
    AppView appview = new AppView();

    private Controller() {}

    public static Controller getIstance() {
        return controller;
    }

    public void start() { //Prima impostazione password
        Utente utenteProvvisorio = null;

        while (utenteProvvisorio == null) {
            AbstractMap.SimpleEntry<String, String> valoriUtente =
                appview.menuInserimentoCredenziali();
            if (valoriUtente == null) return; //uscita dal programma se utente scrive '0' nella password

            utenteProvvisorio = session.login(valoriUtente.getKey(), valoriUtente.getValue());
            if (utenteProvvisorio == null) appview.erroreLogin();
        }
        session.setUtenteAttivo(utenteProvvisorio);
        carica();

        inizializzazione();
        esecuzione();

        salva();
    }

    public void salva() {
        session.salva();
        session.salvaParametriGlobali();
        session.salvaUtenti();
        System.out.println("\nSessione salvata");
    }

    public void carica() {
        session.carica();
        session.caricaParametriGlobali();
        System.out.println("\nSessione caricata");
    }

    private void inizializzazione() {
        appview.benvenutoMsg(session.getUtenteAttivo());
        
        if (session.getUtenteAttivo().getPassword().startsWith("config")) {
            session.cambiaPassword(
                session.getUtenteAttivo(),
                appview.menuCambioPassword(session.getUtenteAttivo())
            );
        }

        if (session.getUtenteAttivo() instanceof Configuratore) {
            if (session.getLuoghi().isEmpty()) creaLuoghi();
            istanziaParametroTerritoriale();
            if (TipoVisita.getNumeroMassimoIscrittoPerFruitore() == 0) dichiaraMassimoNumeroFruitori();
            session.salvaParametriGlobali();
        }
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void creaLuoghi() {
            Set<Luogo> luoghi = new HashSet<>();
            Set<TipoVisita> visite;

            do {
                Luogo luogoDaAggiungere = appview.menuInserimentoLuogo(session.getLuoghi());
                visite = appview.menuInserimentoTipiVisita(
                    session.getUtenteAttivo(), session.getUtenti(), session.getVolontari());
                
                if (visite == null) return;
                
                for (TipoVisita tipoVisita : visite) {
                    luogoDaAggiungere.addVisita(tipoVisita.getTitolo());
                }
                luoghi.add(luogoDaAggiungere);
                session.addTipoVisite(visite);
            } while (appview.confermaLuoghi());

            session.addLuoghi(luoghi);
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    private void istanziaParametroTerritoriale() {
        if (Luogo.getParametroTerritoriale() == null) {
            ((Configuratore) session.getUtenteAttivo()).inizializzaParametroTerritoriale(
                    appview.menuInserimentoParametroTerritoriale()
                );
        }
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void dichiaraMassimoNumeroFruitori() {
        ((Configuratore) session.getUtenteAttivo()).setNumeroMassimoIscritti(
                appview.menuInserimentoMassimoIscritti()
            );
    }

    private void esecuzione() {
        if (session.getUtenteAttivo() instanceof Configuratore) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 9) {
                appview.setMenuConfiguratore();
            } else {
                appview.setMenuConfiguratoreGestioneRaccoltaDisponibilitaStart();
            }
        } else if (session.getUtenteAttivo() instanceof Volontario) {
            appview.setMenuVolontario();
        } else if (session.getUtenteAttivo() instanceof Fruitore) {
            appview.setMenuFruitore();
        }

        appview.stampaMenu();
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void inserisciDatePrecluse() {
        ((Configuratore) session.getUtenteAttivo()).impostaDatePrecluse(
                appview.menuInserimentoDate()
            );
    }

    public void mostraLuoghi() {
        appview.mostraLuoghi(session.getLuoghi());
    }

    public void mostraVolontari() {
        appview.mostraVolontari(session.getVolontari());
    }

    public void mostraTipiVisite() {
        appview.mostraTipiVisite(session.getVisite(), session.getStoricoVisite());
    }

    public void mostraVisitePerStato() {
        Set<Visita> visite = new HashSet<>();
        if (session.getVisite() != null) {
            for (TipoVisita tipoVisita : session.getVisite()) {
                if (tipoVisita.getVisiteAssociate() != null) {
                    visite.addAll(tipoVisita.getVisiteAssociate());
                }
            }
        }

        if (session.getStoricoVisite() != null) {
            for (TipoVisita tipoVisita : session.getStoricoVisite()) {
                if (tipoVisita.getVisiteAssociate() != null) {
                    visite.addAll(tipoVisita.getVisiteAssociate());
                }
            }
        }

        appview.mostraVisiteStato(separaVisitePerStato(visite, session.getUtenteAttivo()));
    }

    public Map<StatoVisita, List<Visita>> separaVisitePerStato(Set<Visita> visite, Utente utenteAttivo) {
        Map<StatoVisita, List<Visita>> visitePerStato = new TreeMap<>();

        for (Visita visita : visite) {
            visitePerStato.computeIfAbsent(visita.getStato(), k -> new ArrayList<>()).add(visita);
        }
        for (StatoVisita stato : StatoVisita.values()) {
            if (!visitePerStato.containsKey(stato)) {
                visitePerStato.put(stato, new ArrayList<>());
            }
        }

        visitePerStato.remove(StatoVisita.NON_ISTANZIATA);

        if (utenteAttivo instanceof Fruitore) {             //Il fruitore visualizza solo proposte, confermate e cancellate
            visitePerStato.remove(StatoVisita.COMPLETA);   
            visitePerStato.remove(StatoVisita.EFFETTUATA);
        }
        
        return visitePerStato;
    }

    //TODO: ver4 aggiungere le Visita con stato Confermata alla visualizzazione del volontario
    /**
     * @ requires session.getUtenteAttivo() instanceof Volontario
     */
    public void mostraVisiteAssociate() {
        appview.mostraVisiteAssociateAlVolontario(
            ((Volontario) session.getUtenteAttivo()).getVisiteAssociate(session.getVisite())
        );
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Volontario
     */
    public void inserisciDisponibilita() {
        Volontario volontario = (Volontario) session.getUtenteAttivo();
        volontario.addDisponibilita(
            appview.menuInserimentoDisponibilita(volontario.getDisponibilita())
        );
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void chiudiDisponibilitaERealizzaPianoVisite() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gestisciEffettiCollaterali();
            riapriDisponibilita();
            salva();
        }));


        inizializzaPianoViste();
        appview.setMenuConfiguratoreEditor();
    }

    public void inizializzaPianoViste() {
        AbstractMap.SimpleImmutableEntry<Calendar, Calendar> inizioFineMese =
            CalendarManager.getInizioFineMeseVisite();
        Calendar inizioMese = inizioFineMese.getKey();
        Calendar fineMese = inizioFineMese.getValue();
        Set<TipoVisita> tipiVisite = session.getTipiVisiteProssimoMese(inizioMese, fineMese);
        Set<Calendar> datePossibili = TipoVisita.getDatePossibiliAttuali(fineMese);
        // Recupero tutti i volontari dal sistema
        Set<Volontario> volontari = session.getVolontari();

        // L'algoritmo creerà al più due visite per ogni TipoVisita, massimo una a settimana per ogni TipoVisita (scelta arbitraria perché non specificato nei requisiti)
        int maxVisitePerTipo = 2;
        for (TipoVisita tipoVisita : tipiVisite) {
            Set<Calendar> datePossibiliPerVisita = tipoVisita.getDatePossibiliPerVisita(
                inizioMese,
                fineMese,
                datePossibili
            );

            Map<Calendar, Set<Volontario>> mappaVolontariPerData =
                session.creaMappaVolontariPerOgniDataPossibile(volontari, datePossibiliPerVisita);

            List<Calendar> dateEstratte =
                session.estraiDateCausali(
                    datePossibiliPerVisita,
                    mappaVolontariPerData,
                    maxVisitePerTipo
                );
            List<Volontario> volontariEstratti = session.estraiVolontariCasuali(
                dateEstratte,
                mappaVolontariPerData,
                tipiVisite,
                maxVisitePerTipo
            );

            // Controllo se ci sono date disponibili per le visite
            if (dateEstratte.isEmpty() || volontariEstratti.isEmpty()) continue;

            session.creaVisitePerDatiEstratti(dateEstratte, volontariEstratti, tipoVisita);
        }
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void aggiungiVolontario() {
        TipoVisita tipoVisitaSelezionato = appview.selezioneTipoVisita(session.getVisite());

        if (!session.getVisite().isEmpty()) {
            Set<Volontario> nuoviVolontari = appview.menuInserimentoVolontari(session.getUtenti());

            tipoVisitaSelezionato.aggiungiVolontariIdonei(nuoviVolontari);
            aggiungiVolontariInSession(nuoviVolontari);
            //session.addVolontari(nuoviVolontari); 
        }       
    }

    public void aggiungiVolontariInSession(Set<Volontario> volontari) {
        session.addVolontari(volontari);
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void aggiungiTipoVisita() {
        Luogo luogoSelezionato = appview.selezioneLuogo(session.getLuoghi());  

        if (!session.getLuoghi().isEmpty()) {
            Set<TipoVisita> nuoveVisite = appview.menuInserimentoTipiVisita(
                session.getUtenteAttivo(), session.getUtenti(), session.getVolontari());
            if (nuoveVisite != null) {
                luogoSelezionato.aggiungiVisite(nuoveVisite);
                session.addTipoVisite(nuoveVisite);
            }
        } 
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void rimuoviLuogo() {
        session.removeLuoghi(appview.menuRimozioneLuoghi(session.getLuoghi()));
        gestisciEffettiCollaterali();
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void rimuoviTipoVisita() {
        //reference ai luoghi gestite nel metodo di session
        session.removeTipoVisita(appview.menuRimozioneTipoVisita(session.getVisite()));
        gestisciEffettiCollaterali();
        
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void rimuoviVolontario() {
        //reference a tipoVisita e Visita gestite nel metodo di session
        session.removeVolontario(appview.menuRimozioneVolontario(session.getVolontari()));
        gestisciEffettiCollaterali();
    }

    public void gestisciEffettiCollaterali() {
        session.checkCondizioniDiClassi();
    }

    public void riapriDisponibilita() {
        session.cleanDisponibilitaDeiVolontari();
        session.salvataggioDatePrecluseFutureInAttuali();
        appview.setMenuConfiguratore();
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void iscrizioneFruitore() {

    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void annullaIscrizione() {

    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void visualizzaVisiteConIscrizione() {

    }

}
