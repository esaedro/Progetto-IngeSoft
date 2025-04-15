package utility;

import application.*;
import java.util.*;

public class Controller {

    private static final Controller controller = new Controller();
    private final Session session = new Session();
    private final AppView appview = new AppView();

    private Controller() {}

    public static Controller getInstance() {
        return controller;
    }

    public void start() { //Prima impostazione password

        appview.setMenuStart(this);
        
        if (appview.stampaMenuOnce() == null) return;       //si esegue login/registrazione a seconda di cosa inserisce l'utente

        carica();
        inizializzazione();
        
        esecuzione();

        salva();
    }

    /**
     * @ ensures session.getUtenteAttivo() != null
     */
    public void loginCredenziali() {
        Utente utenteProvvisorio = null;

        while (utenteProvvisorio == null) {
            AbstractMap.SimpleEntry<String, String> valoriUtente =
                appview.menuInserimentoCredenziali();
            if (valoriUtente == null) return; //uscita dal programma se utente scrive '0' nella password

            utenteProvvisorio = session.login(valoriUtente.getKey(), valoriUtente.getValue());
            if (utenteProvvisorio == null) appview.erroreLogin();
        }
        session.setUtenteAttivo(utenteProvvisorio);
    }


    public void registrazioneFruitore() {
        session.caricaUtenti();
        
        String nomeUtente = appview.inserimentoNomeUtente("Inserire il nome utente: ", session.getUtenti());
        String password = appview.inserimentoPassword("Inserire la password: ");

        Fruitore fruitore = new Fruitore(nomeUtente, password, new HashMap<>());
        session.addFruitore(fruitore);
        session.salvaUtenti();

        session.setUtenteAttivo(fruitore);
    }

    public void salva() {
        session.salva();
        session.salvaParametriGlobali();
        session.salvaUtenti();
        System.out.println("\nSessione salvata");
    }

    public void carica() {
        //carica utenti viene fatto in login/registrazione
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
                visite = appview.menuInserimentoTipiVisita(session.getUtenteAttivo(), session.getUtenti(),
                        session.getVolontari(), this);
                
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
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 15) {
                appview.setMenuConfiguratore(this);
            } else {
                appview.setMenuConfiguratoreGestioneRaccoltaDisponibilitaStart(this);
            }
        } else if (session.getUtenteAttivo() instanceof Volontario) {
            appview.setMenuVolontario(this);
        } else if (session.getUtenteAttivo() instanceof Fruitore) {
            appview.setMenuFruitore(this);
        }

        appview.stampaMenu();
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void inserisciDatePrecluse() {
        ((Configuratore) session.getUtenteAttivo()).impostaDatePrecluse(
                appview.menuInserimentoDatePrecluse(TipoVisita.getDatePrecluseFuture()));
        session.salvaParametriGlobali();
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
        Set<Visita> visite = getAllVisite();
        Map<StatoVisita, List<Visita>> visitePerStato = separaVisitePerStato(visite, session.getUtenteAttivo());

        if (session.getUtenteAttivo() instanceof Volontario) {
            appview.mostraVisiteStato(visitePerStato, session.getStoricoVisite());
        } else if (session.getUtenteAttivo() instanceof Fruitore) {
            visitePerStato.remove(StatoVisita.COMPLETA);
            appview.mostraVisiteStato(visitePerStato, new HashMap<>());
        }


        //appview.mostraVisiteStato(separaVisitePerStato(visite, session.getUtenteAttivo()));
    }

    private Set<Visita> getAllVisite() {
        Set<Visita> visite = new HashSet<>();
        if (session.getVisite() != null) {
            for (TipoVisita tipoVisita : session.getVisite()) {
                if (tipoVisita.getVisiteAssociate() != null) {
                    visite.addAll(tipoVisita.getVisiteAssociate());
                }
            }
        }

        for (Map.Entry<String, Set<Visita>> entry: session.getStoricoVisite().entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()){
                visite.addAll(entry.getValue());
            }
        }
        return visite;
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
        visitePerStato.remove(StatoVisita.EFFETTUATA);      //verranno visualizzate dall'archivio
        
        return visitePerStato;
    }

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
        appview.setMenuConfiguratoreEditor(this);
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
        session.salvaUtenti();
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void aggiungiTipoVisita() {
        Luogo luogoSelezionato = appview.selezioneLuogo(session.getLuoghi());  

        if (!session.getLuoghi().isEmpty()) {
            Set<TipoVisita> nuoveVisite = appview.menuInserimentoTipiVisita(
                session.getUtenteAttivo(), session.getUtenti(), session.getVolontari(), this);
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

    /**
     * @ requires session.getUtenteAttivo() instanceof Configuratore
     */
    public void riapriDisponibilita() {
        session.cleanDisponibilitaDeiVolontari();
        session.salvataggioDatePrecluseFutureInAttuali();
        appview.setMenuConfiguratore(this);
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Fruitore
     */
    public void iscrizioneFruitore() {
        if (session.getUtenteAttivo() instanceof Fruitore) {
            AbstractMap.SimpleEntry<Visita, Integer> visitaConIscritti;

            Set<Visita> visiteProposte = new HashSet<>();

            for (Visita visita : getAllVisite()) {
                if (visita.getStato() == StatoVisita.PROPOSTA) {
                    visiteProposte.add(visita);
                }
            }

            //interazione con l'utente per la scelta della visita (tutte visite, quale iscriversi)
            //menuIscrizione restituisce sia la visita selezionata che il numero di iscritti.
            visitaConIscritti = appview.menuIscrizione(visiteProposte, this);
            
            if (visitaConIscritti != null) {
                session.iscrizione((Fruitore)session.getUtenteAttivo(), visitaConIscritti.getKey(), visitaConIscritti.getValue());
            }
        }
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Fruitore
     */
    public void annullaIscrizione() {
        if (session.getUtenteAttivo() instanceof Fruitore) {
            //interazione con l'utente per la scelta della visita (mostrate tutte visite a cui si è iscritti a video, quale annullare)
            Fruitore fruitore = (Fruitore) session.getUtenteAttivo();
            Visita visitaDaCuiDisiscriversi;

            visitaDaCuiDisiscriversi = appview.menuDisiscrizione(fruitore.getIscrizioni().keySet());

            if (visitaDaCuiDisiscriversi != null)
                session.disiscrizione(fruitore, visitaDaCuiDisiscriversi);
        }

    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Fruitore
     */
    public Map<Visita, Set<Iscrizione>> getIscrizioniPerQuesteVisite(Set<Visita> visite) {
        Map<Visita, Set<Iscrizione>> visiteConIscrizioni = new HashMap<>();
        Set<Iscrizione> iscrizioni = new HashSet<>();

        for (Visita visita : visite) {
            iscrizioni.clear();

            for (Fruitore fruitore : session.getFruitori()) {
                if (fruitore.getIscrizioni() != null && fruitore.getIscrizioni().containsKey(visita)) {
                    iscrizioni.add(fruitore.getIscrizioni().get(visita));        
                }
            }
            visiteConIscrizioni.put(visita, iscrizioni);
        }

        return visiteConIscrizioni;
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Fruitore
     */
    public void mostraVisiteConfermateConIscrizioni() {
        if (session.getUtenteAttivo() instanceof Volontario) {
            Set<Visita> visiteConfermate = new HashSet<>();
            for (Visita visita : getAllVisite()) {

                //TODO: controllare se il metodo se funziona
                if (visita.getVolontarioAssociato() != null) {
                    if (visita.getStato() == StatoVisita.CONFERMATA && visita.getVolontarioAssociato().equals(session.getUtenteAttivo())) {
                        visiteConfermate.add(visita);
                    }
                }
            }
            appview.mostraVisiteConfermateConIscrizioni(getIscrizioniPerQuesteVisite(visiteConfermate));
        }
    }

    /**
     * @ requires session.getUtenteAttivo() instanceof Fruitore
     */
    public void mostraIscrizioniFruitore() {

        //visite nello stato proposta/confermata/cancellata a cui ha effettuato un'iscrizione
        if (session.getUtenteAttivo() instanceof Fruitore fruitore) {

            Map<Visita, Iscrizione> iscrizioni = fruitore.getIscrizioni();
            Map<StatoVisita, Map<Visita, Iscrizione>> visiteConIscrizioniPerStato = new TreeMap<>();

            if (iscrizioni != null && !iscrizioni.isEmpty()) {                
                for (Map.Entry<Visita, Iscrizione> entry : iscrizioni.entrySet()) {
                    Visita visita = entry.getKey();
                    Iscrizione iscrizione = entry.getValue();
                    StatoVisita stato = visita.getStato();

                    //se non va fixare qui
                    if (stato == StatoVisita.PROPOSTA || stato == StatoVisita.CONFERMATA || stato == StatoVisita.CANCELLATA) {
                        visiteConIscrizioniPerStato
                            .computeIfAbsent(stato, k -> new HashMap<>())
                            .put(visita, iscrizione);
                    }
                }
            }

            appview.mostraVisiteStatoConIscrizioni(visiteConIscrizioniPerStato);
        }
    }

    public TipoVisita getTipoVisitaAssociato(Visita visita) {
        if (session.getVisite() != null && !session.getVisite().isEmpty()) {
            for (TipoVisita tipoVisita : session.getVisite()) {
                if (tipoVisita.getVisiteAssociate().contains(visita))
                    return tipoVisita;
                // for (Visita visitaAssociata : tipoVisita.getVisiteAssociate()) {
                //     if (visitaAssociata.equals(visita)) return tipoVisita;
                // }
            }
        }
        return null;
    }

    //recupera il titolo del tipoVisita di una visita effettuata che sta nell'archivii
    public String getTipoVisitaTitolo(Visita visita) {
        if (session.getStoricoVisite() != null && !session.getStoricoVisite().isEmpty()) {
            for (Map.Entry<String, Set<Visita>> entry : session.getStoricoVisite().entrySet()) {
                if (entry.getValue().contains(visita)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

}