package utility;

import application.*;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;

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
                    .setNumeroMassimoIscritti(
                            appview.menuInserimentoMassimoIscritti()
                    );
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

    public void mostraVisite() {
        appview.mostraVisite(session.getVisite(), session.getStoricoVisite());
    }

    public void mostraVisiteAssociate() {
        appview.mostraVisiteAssociateAlVolontario(((Volontario) session.getUtenteAttivo()).getVisiteAssociate(session));
    }

    public void inserisciDisponibilita() {
        ((Volontario) session.getUtenteAttivo()).addDisponibilita(appview.menuInserimentoDisponibilita());
    }

}














