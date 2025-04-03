package utility;

import application.*;

import java.util.*;

public class Controller {

    private static final Controller controller = new Controller();
    Session session = new Session();
    AppView appview = new AppView();

    private Controller() {
    }

    public static Controller getIstance() {
        return controller;
    }

    public void start() { //Prima impostazione password

        Utente utenteProvvisorio = null;

        while (utenteProvvisorio == null) {
            AbstractMap.SimpleEntry<String, String> valoriUtente = appview.menuInserimentoCredenziali();
            if (valoriUtente == null) return;   //uscita dal programma se utente scrive '0' nella password
            
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
            session.cambiaPassword(session.getUtenteAttivo(), appview.menuCambioPassword(session.getUtenteAttivo()));
        }

        creaLuoghi();
        istanziaParametroTerritoriale();
        if (TipoVisita.getNumeroMassimoIscrittoPerFruitore() == 0) dichiaraMassimoNumeroFruitori();

        session.salvaParametriGlobali();
    }

    public void creaLuoghi() {
        if (session.getUtenteAttivo() instanceof Configuratore && session.getLuoghi().isEmpty()) {
            Set<Luogo> luoghi = new HashSet<>();
            Set<TipoVisita> visite;

            do {
                Luogo luogoDaAggiungere = appview.menuInserimentoLuogo();
                visite = appview.menuInserimentoTipiVisita(session.getUtenteAttivo(), session.getVolontari());
                for (TipoVisita tipoVisita: visite) {
                    luogoDaAggiungere.addVisita(tipoVisita.getTitolo());
                }
                luoghi.add(luogoDaAggiungere);
                session.addAllTipoVisite(visite);
            } while(appview.confermaLuoghi());

            session.addLuoghi(luoghi);
        }
    }

    private void istanziaParametroTerritoriale() {
        if (session.getUtenteAttivo() instanceof Configuratore && Luogo.getParametroTerritoriale() == null) {
            ((Configuratore) session.getUtenteAttivo())
                    .inizializzaParametroTerritoriale(appview.menuInserimentoParametroTerritoriale());
        }
    }

    public void dichiaraMassimoNumeroFruitori() {
        if (session.getUtenteAttivo() instanceof Configuratore) {
            ((Configuratore) session.getUtenteAttivo())
                    .setNumeroMassimoIscritti(appview.menuInserimentoMassimoIscritti());
        }
    }
    private void esecuzione() {
        if (session.getUtenteAttivo() instanceof Configuratore) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 2) {
                appview.setMenuConfiguratore();
            } else {
                appview.setMenuConfiguratoreGestioneRaccoltaDisponibilitaStart();
            }
        }
        else if (session.getUtenteAttivo() instanceof Volontario) {
            appview.setMenuVolontario();
        }

        appview.stampaMenu();
    }

    public void inserisciDatePrecluse() {
        ((Configuratore) session.getUtenteAttivo()).impostaDatePrecluse(appview.menuInserimentoDate());
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
        appview.mostraVisiteAssociateAlVolontario(((Volontario) session.getUtenteAttivo())
                .getVisiteAssociate(session.getVisite()));
    }

    public void inserisciDisponibilita() {
        Volontario volontario = (Volontario) session.getUtenteAttivo();
        volontario.addDisponibilita(appview.menuInserimentoDisponibilita(volontario.getDisponibilita())); 
    }

    //Esaedro
    public void inizializzaPianoViste() {


        appview.setMenuConfiguratoreEditor();
    }

    public void aggiungiVolontario() {
        //check utenteAttivo instanceof Configuratore?
        Set<Volontario> nuoviVolontari = new HashSet<>();

        nuoviVolontari = appview.menuInserimentoVolontari(/*parametri?*/);

        session.addVolontari(nuoviVolontari);    //aggiungi a session (addVolontari è nuovo metodo)
    }

    public void aggiungiTipoVisita() {
        //check utenteAttivo instanceof Configuratore?
        Set<TipoVisita> nuoveVisite = new HashSet<>();

        nuoveVisite = appview.menuInserimentoTipiVisita(session.getUtenteAttivo(), session.getVolontari());

        session.addAllTipoVisite(nuoveVisite);
    }

    public void rimuoviLuogo() {
        /* Set<Luogo> luoghiDaEliminare = new HashSet<>();
        //check utenteAttivo instanceof Configuratore?
        luoghiDaEliminare = appview.menuRimozioneLuoghi(session.getLuoghi());
        session.rimuoviLuoghi(luoghiDaEliminare); */

        session.rimuoviLuoghi(appview.menuRimozioneLuoghi(session.getLuoghi()));
        gestisciEffettiCollaterali();
    }

    public void rimuoviTipoVisita() {
        session.rimuoviTipoVisita(appview.menuRimozioneTipoVisita(session.getVisite()));
        gestisciEffettiCollaterali();
    }

    public void rimuoviVolontario() {
        if (!session.getVolontari().isEmpty())
        session.removeVolontario(appview.menuRimozioneVolontario(session.getVolontari()));
        gestisciEffettiCollaterali();
    }

    public void gestisciEffettiCollaterali() {
        //TODO: esegue i check di session?
    }

    public void riapriDisponibilita() {
        //TODO
        appview.setMenuConfiguratore();
    }

}
