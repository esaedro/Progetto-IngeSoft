package utility;

import application.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Controller {

    private static Controller controller = new Controller();
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
        dichiaraMassimoNumeroFruitori();

        session.salvaParametriGlobali();
    }

    private void creaLuoghi() {
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
        if (session.getUtenteAttivo() instanceof Configuratore && TipoVisita.getNumeroMassimoIscrittoPerFruitore() == 0) {
            ((Configuratore) session.getUtenteAttivo())
                    .setNumeroMassimoIscritti(appview.menuInserimentoMassimoIscritti());
        }
    }
    private void esecuzione() {
        appview.creaMenu(session.getUtenteAttivo());
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
        if (session.getVisite() == null) {}
        else {
            for (TipoVisita tipoVisita : session.getVisite()) {
                visite.addAll(tipoVisita.getVisiteAssociate());
            }
        }
        
        if (session.getStoricoVisite() == null) {}
        else {
            for (TipoVisita visita : session.getStoricoVisite()) {
                visite.addAll(visita.getVisiteAssociate());
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
        appview.mostraVisiteAssociateAlVolontario(((Volontario) session.getUtenteAttivo()).getVisiteAssociate(session));
    }

    public void inserisciDisponibilita() {
        ((Volontario) session.getUtenteAttivo()).addDisponibilita(appview.menuInserimentoDisponibilita());
    }

}














