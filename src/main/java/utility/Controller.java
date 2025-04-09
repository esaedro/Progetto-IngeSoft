package utility;

import application.*;
import java.util.*;
import java.util.stream.Collectors;

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

    public void creaLuoghi() {
            Set<Luogo> luoghi = new HashSet<>();
            Set<TipoVisita> visite;

            do {
                Luogo luogoDaAggiungere = appview.menuInserimentoLuogo();
                visite = appview.menuInserimentoTipiVisita(
                    session.getUtenteAttivo(),
                    session.getVolontari()
                );
                for (TipoVisita tipoVisita : visite) {
                    luogoDaAggiungere.addVisita(tipoVisita.getTitolo());
                }
                luoghi.add(luogoDaAggiungere);
                session.addTipoVisite(visite);
            } while (appview.confermaLuoghi());

            session.addLuoghi(luoghi);
    }

    private void istanziaParametroTerritoriale() {
        if (Luogo.getParametroTerritoriale() == null) {
            ((Configuratore) session.getUtenteAttivo()).inizializzaParametroTerritoriale(
                    appview.menuInserimentoParametroTerritoriale()
                );
        }
    }

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
        }

        appview.stampaMenu();
    }

    public void inserisciDatePrecluse() {
        if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        }
        ((Configuratore) session.getUtenteAttivo()).impostaDatePrecluse(
                appview.menuInserimentoDate()
            );
    }

    public void mostraLuoghi() {
        if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        }
        appview.mostraLuoghi(session.getLuoghi());
    }

    public void mostraVolontari() {
        if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        }
        appview.mostraVolontari(session.getVolontari());
    }

    public void mostraTipiVisite() {
        if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        }
        appview.mostraTipiVisite(session.getVisite(), session.getStoricoVisite());
    }

    public void mostraVisite() {
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

        appview.mostraVisite(separaVisitePerStato(visite));
    }

    public Map<StatoVisita, List<Visita>> separaVisitePerStato(Set<Visita> visite) {
        Map<StatoVisita, List<Visita>> visitePerStato = new TreeMap<>();

        for (Visita visita : visite) {
            visitePerStato.computeIfAbsent(visita.getStato(), k -> new ArrayList<>()).add(visita);
        }
        for (StatoVisita stato : StatoVisita.values()) {
            if (!visitePerStato.containsKey(stato)) {
                visitePerStato.put(stato, new ArrayList<>());
            }
        }
        return visitePerStato;
    }

    public void mostraVisiteAssociate() {
        if (!(session.getUtenteAttivo() instanceof Volontario)) {
            throw new IllegalStateException("Solo il volontario ha i permessi necessari per eseguire questa operazione");
        }
        appview.mostraVisiteAssociateAlVolontario(
            ((Volontario) session.getUtenteAttivo()).getVisiteAssociate(session.getVisite())
        );
    }

    public void inserisciDisponibilita() {
        if (!(session.getUtenteAttivo() instanceof Volontario)) {
            throw new IllegalStateException("Solo il volontario ha i permessi necessari per eseguire questa operazione");
        }
        Volontario volontario = (Volontario) session.getUtenteAttivo();
        volontario.addDisponibilita(
            appview.menuInserimentoDisponibilita(volontario.getDisponibilita())
        );
    }


    public void inizializzaPianoViste() {
        if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        }

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

    public void aggiungiVolontario() {
        TipoVisita tipoVisitaSelezionato = appview.selezioneTipoVisita(session.getVisite());

        if (!session.getVisite().isEmpty()) {
            Set<Volontario> nuoviVolontari = appview.menuInserimentoVolontari();

            tipoVisitaSelezionato.aggiungiVolontariIdonei(nuoviVolontari);
            session.addVolontari(nuoviVolontari); 
        }       
    }

    public void aggiungiTipoVisita() {
        Luogo luogoSelezionato = appview.selezioneLuogo(session.getLuoghi());  

        if (!session.getLuoghi().isEmpty()) {
            Set<TipoVisita> nuoveVisite = appview.menuInserimentoTipiVisita(
                session.getUtenteAttivo(), session.getVolontari());

            luogoSelezionato.aggiungiVisite(nuoveVisite);
            session.addTipoVisite(nuoveVisite);
        } 
    }

    public void rimuoviLuogo() {
/*         if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        } */
        session.removeLuoghi(appview.menuRimozioneLuoghi(session.getLuoghi()));
        gestisciEffettiCollaterali();
    }

    public void rimuoviTipoVisita() {
/*         if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        } */

        //reference ai luoghi gestite nel metodo di session
        session.removeTipoVisita(appview.menuRimozioneTipoVisita(session.getVisite()));
        gestisciEffettiCollaterali();
        
    }

    public void rimuoviVolontario() {
/*         if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        } */

        //reference a tipoVisita e Visita gestite nel metodo di session
        session.removeVolontario(appview.menuRimozioneVolontario(session.getVolontari()));
        gestisciEffettiCollaterali();
    }

    public void gestisciEffettiCollaterali() {
        session.checkCondizioniDiClassi();
    }

    public void riapriDisponibilita() {
        if (!(session.getUtenteAttivo() instanceof Configuratore)) {
            throw new IllegalStateException("Solo il configuratore ha i permessi necessari per eseguire questa operazione");
        }
        session.cleanDisponibilitaDeiVolontari();
        session.salvataggioDatePrecluseFutureInAttuali();
        appview.setMenuConfiguratore();
    }
}
