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
                visite = appview.menuInserimentoTipiVisita(
                    session.getUtenteAttivo(),
                    session.getVolontari()
                );
                for (TipoVisita tipoVisita : visite) {
                    luogoDaAggiungere.addVisita(tipoVisita.getTitolo());
                }
                luoghi.add(luogoDaAggiungere);
                session.addAllTipoVisite(visite);
            } while (appview.confermaLuoghi());

            session.addLuoghi(luoghi);
        }
    }

    private void istanziaParametroTerritoriale() {
        if (
            session.getUtenteAttivo() instanceof Configuratore &&
            Luogo.getParametroTerritoriale() == null
        ) {
            ((Configuratore) session.getUtenteAttivo()).inizializzaParametroTerritoriale(
                    appview.menuInserimentoParametroTerritoriale()
                );
        }
    }

    public void dichiaraMassimoNumeroFruitori() {
        if (session.getUtenteAttivo() instanceof Configuratore) {
            ((Configuratore) session.getUtenteAttivo()).setNumeroMassimoIscritti(
                    appview.menuInserimentoMassimoIscritti()
                );
        }
    }

    private void esecuzione() {
        if (session.getUtenteAttivo() instanceof Configuratore) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 2) {
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
        appview.mostraVisiteAssociateAlVolontario(
            ((Volontario) session.getUtenteAttivo()).getVisiteAssociate(session.getVisite())
        );
    }

    public void inserisciDisponibilita() {
        Volontario volontario = (Volontario) session.getUtenteAttivo();
        volontario.addDisponibilita(
            appview.menuInserimentoDisponibilita(volontario.getDisponibilita())
        );
    }

    //Esaedro
    public void inizializzaPianoViste() {
        // Ordino le visite in base alla data di fine per evitare che le visite che terminano a breve non vengano istanziate
        Set<TipoVisita> tipiVisite = new TreeSet<>(Comparator.comparing(TipoVisita::getDataFine));
        tipiVisite.addAll(session.getVisite());

        // Rimuovo tutti i tipi di vista che finiscono entro questo mese
        Calendar calInizioMese = Calendar.getInstance();
        calInizioMese.add(Calendar.MONTH, 1);
        calInizioMese.set(Calendar.DAY_OF_MONTH, 1);
        tipiVisite.removeIf(tipoVisita -> tipoVisita.getDataFine().before(calInizioMese.getTime()));

        // Rimuovo tutti tipi di visita che iniziano a più di due mesi da oggi
        Calendar calFineMese = Calendar.getInstance();
        calFineMese.add(Calendar.MONTH, 1);
        calFineMese.set(Calendar.DAY_OF_MONTH, calFineMese.getMaximum(Calendar.DAY_OF_MONTH));
        tipiVisite.removeIf(tipoVisita -> tipoVisita.getDataInizio().after(calFineMese.getTime()));

        // È meglio creare un insieme di date possibili, togliendo già quelle precluse per evitare di dover fare dei controlli successivamente
        Set<Integer> datePrecluse = TipoVisita.getDatePrecluseAttuali();
        Set<Calendar> datePossibili = new HashSet<>();
        Set<Calendar> orePossibili = new HashSet<>();

        // Le visite potranno essere svolte tra le 8 e le 18 (scelta arbitraria perché non specificato nei requisiti)
        for (int ora = 8; ora <= 18; ora++) {
            Calendar calOra = Calendar.getInstance();
            calOra.set(Calendar.HOUR_OF_DAY, ora);
            calOra.set(Calendar.MINUTE, 0);
            calOra.set(Calendar.SECOND, 0);
            orePossibili.add((Calendar) calOra.clone());
        }

        // Riempio l'insieme delle date possibili
        Calendar temp = Calendar.getInstance();
        temp.add(Calendar.MONTH, 1);
        temp.set(Calendar.HOUR_OF_DAY, 0);
        temp.set(Calendar.MINUTE, 0);
        temp.set(Calendar.SECOND, 0);
        for (int i = 0; i < calFineMese.getMaximum(Calendar.DAY_OF_MONTH); i++) {
            if (!datePrecluse.contains(i)) {
                temp.set(Calendar.DAY_OF_MONTH, i);
                datePossibili.add(temp);
            }
        }

        // Recupero tutti i volontari dal sistema
        Set<Volontario> volontari = session.getVolontari();

        // L'algoritmo creerà al più due visite per ogni TipoVisita, massimo una a settimana per ogni TipoVisita (scelta arbitraria perché non specificato nei requisiti)
        int maxVisitePerTipo = 2;
        for (TipoVisita tipoVisita : tipiVisite) {
            Calendar calFineVisita = tipoVisita.getDataFine().after(calFineMese.getTime())
                ? calFineMese
                : tipoVisita.getDataFine();

            // Calcolo le date possibili per questa visita
            Set<Calendar> datePossibiliPerVisita = new HashSet<>();
            if (calFineVisita.equals(calFineMese)) {
                datePossibiliPerVisita = datePossibili;
            } else {
                for (Calendar data : datePossibili) {
                    if (data.before(calFineVisita)) {
                        datePossibiliPerVisita.add(data);
                    }
                    if (data.equals(calFineVisita)) {
                        datePossibiliPerVisita.add(data);
                        break;
                    }
                }
            }

            // Creo una mappa che lega le date possibili per la vista con i volontari disponibili per le rispettive date
            Map<Calendar, Set<Volontario>> mappaVolontariPerData = new HashMap<>();
            for (Calendar data : datePossibiliPerVisita) {
                Set<Volontario> volontariDisponibili = new HashSet<>();
                for (Volontario volontario : volontari) {
                    if (volontario.getDisponibilita().contains(data.get(Calendar.DAY_OF_MONTH))) {
                        volontariDisponibili.add(volontario);
                    }
                }
                mappaVolontariPerData.put(data, volontariDisponibili);
            }

            // Creo un contentore per le date e le ore estratte
            List<Calendar> dateEstratte = new ArrayList<>();
            List<Calendar> oreEstratte = new ArrayList<>();
            List<Volontario> volontariEstratti = new ArrayList<>();

            // Estraggo a caso due date e due ore possibili per questa visita (non nella stessa settimana)
            for (
                int i = 0;
                i < datePossibiliPerVisita.size() && dateEstratte.size() < maxVisitePerTipo;
                i++
            ) {
                Calendar data = datePossibiliPerVisita
                    .stream()
                    .skip(new Random().nextInt(datePossibiliPerVisita.size()))
                    .findFirst()
                    .orElse(null); // Prendo una data casuale
                if (mappaVolontariPerData.get(data).isEmpty()) { // Controllo se c'è almeno un volontario disponibile per quella data
                    continue;
                }
                dateEstratte.add(data); // Aggiungo la data estratta alla lista
                Calendar ora = orePossibili
                    .stream()
                    .skip(new Random().nextInt(orePossibili.size()))
                    .findFirst()
                    .orElse(null);
                oreEstratte.add(ora);
                datePossibiliPerVisita.removeAll(
                    datePossibiliPerVisita
                        .stream()
                        .filter(date -> date.getWeekYear() == data.getWeekYear())
                        .collect(Collectors.toList())
                ); // Rimuovo tutte le date della stessa settimana
            }
            // Estraggo a caso due volontari per ogni data estratta (controllando che non siano già impegnati in altre visite per le stesse date)
            for (
                int i = 0;
                i < dateEstratte.size() && volontariEstratti.size() < maxVisitePerTipo;
                i++
            ) {
                Calendar data = dateEstratte.get(i);
                Volontario volontarioEstratto = mappaVolontariPerData
                    .get(data)
                    .stream()
                    .skip(new Random().nextInt(mappaVolontariPerData.get(data).size()))
                    .findFirst()
                    .orElse(null);

                // Controllo se il volontario è già impegnato in altre visite per la stessa data
                boolean impegnato = false;
                if (volontarioEstratto != null) {
                    for (TipoVisita tipoV : volontarioEstratto.getVisiteAssociate(tipiVisite)) {
                        for (Visita visita : tipoV.getVisiteAssociate()) {
                            if (
                                visita.getDataVisita().get(Calendar.DAY_OF_MONTH) ==
                                data.get(Calendar.DAY_OF_MONTH)
                            ) {
                                impegnato = true;
                                break;
                            }
                        }
                    }
                }

                if (!impegnato) {
                    volontariEstratti.add(volontarioEstratto);
                }
            }

            // Controllo se ci sono date disponibili per le visite
            if (dateEstratte.isEmpty()) {
                throw new IllegalStateException("Nessuna data disponibile per le visite");
            }

            // Controllo se ci sono volontari disponibili per le visite
            if (volontariEstratti.isEmpty()) {
                throw new IllegalStateException("Nessun volontario disponibile per le visite");
            }

            // Per ogni data estratta creo una visita e la aggiungo al tipo di visita
            for (int i = 0; i < Math.min(dateEstratte.size(), volontariEstratti.size()); i++) {
                Calendar dataVisita = dateEstratte.get(i);
                Calendar oraVisita = oreEstratte.get(i);
                dataVisita.set(Calendar.HOUR_OF_DAY, oraVisita.get(Calendar.HOUR_OF_DAY));
                dataVisita.set(Calendar.MINUTE, oraVisita.get(Calendar.MINUTE));
                dataVisita.set(Calendar.SECOND, oraVisita.get(Calendar.SECOND));
                Visita nuovaVisita = new Visita(dataVisita, StatoVisita.PROPOSTA, 0);
                nuovaVisita.setVolontarioAssociato(volontariEstratti.get(i));
                tipoVisita.addVisita(nuovaVisita);
            }
        }

        appview.setMenuConfiguratoreEditor();
    }

    public void aggiungiVolontario() {
        Set<Volontario> nuoviVolontari = new HashSet<>();

        nuoviVolontari = appview.menuInserimentoVolontari(/*parametri?*/);

        session.addVolontari(nuoviVolontari); //aggiungi a session (addVolontari è nuovo metodo)
    }

    public void aggiungiTipoVisita() {
        Set<TipoVisita> nuoveVisite = new HashSet<>();

        nuoveVisite = appview.menuInserimentoTipiVisita(
            session.getUtenteAttivo(),
            session.getVolontari()
        );

        session.addAllTipoVisite(nuoveVisite);
    }

    public void rimuoviLuogo() {
        session.removeLuoghi(appview.menuRimozioneLuoghi(session.getLuoghi()));
        gestisciEffettiCollaterali();
    }

    public void rimuoviTipoVisita() {
        // TODO: rimuovi reference nei luoghi
        session.removeTipoVisita(appview.menuRimozioneTipoVisita(session.getVisite()));
        gestisciEffettiCollaterali();
    }

    public void rimuoviVolontario() {
        // TODO: rimuovi reference nel TipoVisita
        session.removeVolontario(appview.menuRimozioneVolontario(session.getVolontari()));
        gestisciEffettiCollaterali();
    }

    public void gestisciEffettiCollaterali() {
        session.checkCondizioniDiClassi();
    }

    public void riapriDisponibilita() {
        session.salvataggioDatePrecluseFutureInAttuali();
        appview.setMenuConfiguratore();
    }
}
