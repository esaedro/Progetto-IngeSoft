package application;

import java.util.*;
import utility.FileManager;

public class Session {

    private final IUserService userService;
    private final IVisitService visitService;
    private final IPlaceService placeService;
    private final IPersistenceService persistenceService;
    /**
     * @ invariant utenti != null && !utenti.isEmpty();
     */
    private Set<Utente> utenti;
    /**
     * @ invariant luoghi != null;
     */
    private Set<Luogo> luoghi;
    /**
     * @ invariant visite != null;
     */
    private Set<TipoVisita> visite;
    /**
     * @ invariant filemanager != null;
     */
    private final FileManager filemanager;

    private Utente utenteAttivo;

    public Session() {
        ServiceFactory factory = new ServiceFactory();
        userService = factory.createUserService();
        visitService = factory.createVisitService();
        placeService = factory.createPlaceService();
        persistenceService = factory.createPersistenceService();
        this.filemanager = FileManager.getInstance();
    }

    public void salva() {
        visitService.salvaStoricoVisite();
        visitService.salvaVisite();
        placeService.salvaLuoghi();
        salvaParametriGlobali();
    }

    public void salvaUtenti() {
        userService.salvaUtenti();
    }

    public void salvaParametriGlobali() {
        persistenceService.salvaParametriGlobali();
    }

    /**
     * @ requires utente != null && newPassword != null && !newPassword.isEmpty();
     * @ ensures utenti.stream().anyMatch(u -> u.getNomeUtente().equals(utente.getNomeUtente())) ==>
     *           utenti.stream().filter(u -> u.getNomeUtente().equals(utente.getNomeUtente()))
     *                  .allMatch(u -> u.getPassword().equals(newPassword));
     * @ ensures \old(salvaUtenti());
     */
    public void cambiaPassword(Utente utente, String newPassword) {
        for (Utente user : utenti) {
            if (user.getNomeUtente().equals(utente.getNomeUtente())) {
                user.setPassword(newPassword);
            }
        }
        salvaUtenti();
    }

    /**
     * @ ensures visite != null && luoghi != null && parametriGlobali != null && utenti != null;
     */
    public void carica() {
        visitService.caricaVisite();
        placeService.caricaLuoghi();
        caricaParametriGlobali();
    }

    public void caricaParametriGlobali() {
        persistenceService.caricaParametriGlobali();
    }

    public void caricaUtenti() {
        userService.caricaUtenti();
    }

    /**
     * @ requires nomeUtente != null && !nomeUtente.isEmpty() && password != null && !password.isEmpty();
     */
    public Utente login(String nomeUtente, String password) {
        caricaUtenti();

        for (Utente user : getUtenti()) {
            if (user.getNomeUtente().equals(nomeUtente) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public Set<Utente> getUtenti() {
        return userService.getUtenti();
    }

    /**
     * @ requires utenti != null;
     */
    public void setUtenti(Set<Utente> utenti) {
        userService.setUtenti(utenti);
    }

    public Utente getUtenteAttivo() {
        return userService.getUtenteAttivo();
    }

    /**
     * @ requires utenteAttivo != null;
     */
    public void setUtenteAttivo(Utente utenteAttivo) {
        userService.setUtenteAttivo(utenteAttivo);
    }

    public Set<Luogo> getLuoghi() {
        return placeService.getLuoghi();
    }

    /**
     * @ requires luoghi != null;
     */
    public void setLuoghi(Set<Luogo> luoghi) {
        placeService.setLuoghi(luoghi);
    }

    /**
     * @ requires luoghiDaAggiungere != null;
     */
    public void addLuoghi(Set<Luogo> luoghiDaAggiungere) {
        placeService.aggiungiLuoghi(luoghiDaAggiungere);
    }

    /**
     * @ requires luogo != null;
     */
    public void addLuogo(Luogo luogo) {
        placeService.aggiungiLuogo(luogo);
    }

    // TODO: realize a proxy
    /**
     * @ requires luoghiDaRimuovere != null;
     */
    public void removeLuoghi(Set<Luogo> luoghiDaRimuovere) {
        placeService.rimuoviLuoghi(luoghiDaRimuovere);
    }

    public Set<TipoVisita> getVisite() {
        return visitService.getTipiVisita();
    }

    /**
     * @ requires visite != null;
     */
    public void setVisite(Set<TipoVisita> visite) {
        visitService.setTipiVisita(visite);
    }

    /**
     * @ requires visite != null;
     */
    public void addVisita(TipoVisita visita) {
        visitService.aggiungiTipoVisita(visita);
    }

    /**
     * @ requires tipoVisiteDaAggiungere != null;
     */
    public void addTipoVisite(Set<TipoVisita> tipiVisitaDaAggiungere) {
        visitService.aggiungiTipiVisita(tipiVisitaDaAggiungere);
    }

    public HashMap<String, Set<Visita>> getStoricoVisite() {
        return persistenceService.caricaDatiArchioStorico(String.class, Visita.class);
    }

    // TODO: realize a proxy
    /**
     * @ requires visiteDaRimuovere != null;
     */
    public void removeTipoVisita(Set<TipoVisita> visiteDaRimuovere) {
        visitService.rimuoviTipiVisita(visiteDaRimuovere, placeService, userService);
    }

    public Set<Volontario> getVolontari() {
        return userService.getVolontari();
    }

    public Set<Fruitore> getFruitori() {
        return userService.getFruitori();
    }

    /**
     * @ requires nuoviVolontari != null;
     */
    public void addVolontari(Set<Volontario> nuoviVolontari) {
        userService.aggiungiVolontari(nuoviVolontari);
    }

    /**
     * @ requires visiteDaRimuovere != null;
     */
    public void addFruitore(Fruitore fruitore) {
        userService.aggiungiFruitore(fruitore);
    }

    // TODO: realize a proxy
    /**
     * @ requires volontariDaRimuovere != null;
     * @ ensures utenti != null && utenti.stream().allMatch(utente -> utente instanceof Volontario ==>
     *           utente instanceof Volontario && ((Volontario) utente).getDisponibilita().isEmpty()));
     */
    public void removeVolontario(Set<Volontario> volontariDaRimuovere) {
        userService.rimuoviVolontari(volontariDaRimuovere, visitService);
    }

    public void cleanDisponibilitaDeiVolontari() {
        userService.cleanDisponibilitaVolontari();
    }

    public void salvataggioDatePrecluseFutureInAttuali() {
        visitService.salvaDatePrecluseFutureInAttuali();
    }

    public void checkCondizioniDiClassi() {
        checkCondizioniDiLuogo();
        checkCondizioniDiTipoVisita();
        userService.checkCondizioniDiVolontario(visitService.getTipiVisita());
    }

    private void checkCondizioniDiLuogo() {
        placeService.controllaCondizioniLuogo();
    }

    private void checkCondizioniDiTipoVisita() {
        visitService.controllaCondizioniTipiVisita(placeService.getLuoghi());
        userService.checkCondizioniDiVolontario(visitService.getTipiVisita());
        placeService.controllaCondizioniLuogo();
    }

    public FileManager getFilemanager() {
        return filemanager;
    }

    /**
     * @ requires inizioMese != null && fineMese != null;
     */
    public Set<TipoVisita> getTipiVisiteProssimoMese(Calendar inizioMese, Calendar fineMese) {
        // Ordino le visite in base alla data di fine per evitare che le visite che terminano a breve non vengano istanziate
        Set<TipoVisita> tipiVisite = new TreeSet<>(Comparator.comparing(TipoVisita::getDataFine));
        tipiVisite.addAll(visite);

        // Rimuovo tutti i tipi di vista che finiscono entro questo mese
        tipiVisite.removeIf(tipoVisita -> tipoVisita.getDataFine().before(inizioMese.getTime()));

        // Rimuovo tutti tipi di visita che iniziano a più di due mesi da oggi
        tipiVisite.removeIf(tipoVisita -> tipoVisita.getDataInizio().after(fineMese.getTime()));

        return tipiVisite;
    }

    /**
     * @ requires volontari != null && satePossibiliPerVisita != null;
     */
    public Map<Calendar, Set<Volontario>> creaMappaVolontariPerOgniDataPossibile(
        Set<Volontario> volontari,
        Set<Calendar> datePossibiliPerVisita
    ) {
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

        return mappaVolontariPerData;
    }

    /**
     * @ requires datePossibiliPerVisita != null && mappaVolontariPerVisita != null && massimo != null;
     */
    public List<Calendar> estraiDateCausali(
        Set<Calendar> datePossibiliPerVisita,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        int massimo
    ) {
        // Creo un contentore per le date e le ore estratte
        List<Calendar> dateEstratte = new ArrayList<>();

        // Estraggo a caso due date e due ore possibili per questa visita (non nella stessa settimana)
        for (int i = 0; i < datePossibiliPerVisita.size() && dateEstratte.size() < massimo; i++) {
            Calendar data = datePossibiliPerVisita
                .stream()
                .skip(new Random().nextInt(datePossibiliPerVisita.size()))
                .findFirst()
                .orElse(null); // Prendo una data casuale
            if (mappaVolontariPerData.get(data).isEmpty()) { // Controllo se c'è almeno un volontario disponibile per quella data
                continue;
            }
            dateEstratte.add(data); // Aggiungo la data estratta alla lista
            datePossibiliPerVisita.removeAll(
                datePossibiliPerVisita
                    .stream()
                    .filter(date -> date.getWeekYear() == data.getWeekYear())
                    .toList()); // Rimuovo tutte le date della stessa settimana
        }

        return dateEstratte;
    }

    /**
     * @ requires dateEstratte != null && mappaVolontariPerData != null && tipiVisite != null && massimo != null;
     */
    public List<Volontario> estraiVolontariCasuali(
        List<Calendar> dateEstratte,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        Set<TipoVisita> tipiVisite,
        int massimo
    ) {
        List<Volontario> volontariEstratti = new ArrayList<>();
        // Estraggo a caso due volontari per ogni data estratta (controllando che non siano già impegnati in altre visite per le stesse date)
        for (int i = 0; i < dateEstratte.size() && volontariEstratti.size() < massimo; i++) {
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

        return volontariEstratti;
    }

    /**
     * @ requires dateEstratte != null && volontariEstratti != null && tipiVisita != null;
     * @ ensures tipoVisita.getVisiteAssociate().size() >= \old(tipoVisita.getVisiteAssociate().size())
     */
    public void creaVisitePerDatiEstratti(
        List<Calendar> dateEstratte,
        List<Volontario> volontariEstratti,
        TipoVisita tipoVisita
    ) {
        // Per ogni data estratta creo una visita e la aggiungo al tipo di visita
        for (int i = 0; i < Math.min(dateEstratte.size(), volontariEstratti.size()); i++) {
            Calendar dataVisita = dateEstratte.get(i);
            Visita nuovaVisita = new Visita(tipoVisita.getTitolo(), dataVisita, StatoVisita.PROPOSTA, 0);
            nuovaVisita.setVolontarioAssociato(volontariEstratti.get(i));
            tipoVisita.addVisita(nuovaVisita);
        }
    }

    /**
     * @ requires fruitore != null && visita != null && numeroIscritti != null && numeroIscritti > 0;
     */
    public boolean puoIscriversi(Fruitore fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
        if (visita.getStato() == StatoVisita.PROPOSTA && !fruitore.getIscrizioni().containsKey(visita)) {
            if (tipoVisita != null) {
                return (visita.getNumeroIscritti() + numeroIscritti <= tipoVisita.getMaxPartecipante());
            }
        }
        return false;
    }

    /**
     * @ requires fruitore != null && visita != null;
     */
    public boolean puoDisiscriversi(Fruitore fruitore, Visita visita) {
        if (fruitore.getIscrizioni().containsKey(visita)) {
            return (visita.getStato() == StatoVisita.COMPLETA ||
                    visita.getStato() == StatoVisita.PROPOSTA);
        }
        return false;
    }

    /**
     * @ requires fruitore != null && visita != null && numeroIscritti != null && numeroIscritti > 0;
     * @ ensures fruitore.getIscrizioni().containsKey(visita) <==> \old(puoIscriversi(fruitore, visita, numeroIscritti));
     * @ ensures \old(puoIscriversi(fruitore, visita, numeroIscritti)) ==> 
     *           visita.getNumeroIscritti() == \old(visita.getNumeroIscritti()) + numeroIscritti;
     * @ ensures \old(puoIscriversi(fruitore, visita, numeroIscritti)) ==> 
     *           fruitore.getIscrizioni().get(visita) != null && 
     *           fruitore.getIscrizioni().get(visita).getNumeroDiIscritti() == numeroIscritti;
     * @ ensures \old(puoIscriversi(fruitore, visita, numeroIscritti)) && 
     *           visita.getNumeroIscritti() == (\exists TipoVisita t; visite.contains(t) && t.getVisiteAssociate().contains(visita); t.getMaxPartecipante()) ==> 
     *           visita.getStato() == StatoVisita.COMPLETA;
     */
    public void iscrizione(Fruitore fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
        if (puoIscriversi(fruitore, visita, numeroIscritti, tipoVisita)) {
            Iscrizione nuovaIscrizione;
            String codiceIscrizione;
            Set<Fruitore> fruitori = getFruitori();
            List<Iscrizione> iscrizioniVisita = new ArrayList<>(); // Lista di tutte le iscrizioni per la visita fornita in input
            List<String> codiciIscrizioniVisita = new ArrayList<>(); // Lista di tutti i codici di iscrizione per la visita fornita in input

            for (Fruitore f : fruitori) {
                if (f.getIscrizioni().containsKey(visita)) {
                    iscrizioniVisita.add(f.getIscrizioni().get(visita));
                }
            }
            for (Iscrizione i : iscrizioniVisita) {
                codiciIscrizioniVisita.add(i.getCodiceUnivoco());
            }

            // Controllo che il codice creato non sia già presente per le iscrizioni alla stessa visita
            do {
                codiceIscrizione = String.format("%06d", new Random().nextInt(1000000));
            } while (codiciIscrizioniVisita.stream().anyMatch(codiceIscrizione::equals));

            nuovaIscrizione = new Iscrizione(codiceIscrizione, numeroIscritti);
            fruitore.aggiungiIscrizione(visita, nuovaIscrizione);
            visita.setNumeroIscritti(visita.getNumeroIscritti() + numeroIscritti);

            if (tipoVisita != null && visita.getNumeroIscritti() == tipoVisita.getMaxPartecipante()) {
                visita.setStato(StatoVisita.COMPLETA);
            }
        }
    }
    
    /**
     * @ requires fruitore != null && visita != null;
     * @ ensures fruitore.getIscrizioni().containsKey(visita) == false <==> \old(puoDisiscriversi(fruitore, visita));
     * @ ensures \old(puoDisiscriversi(fruitore, visita)) ==> 
     *           visita.getNumeroIscritti() == \old(visita.getNumeroIscritti()) - \old(fruitore.getIscrizioni().get(visita).getNumeroDiIscritti());
     * @ ensures \old(puoDisiscriversi(fruitore, visita)) ==> visita.getStato() == StatoVisita.PROPOSTA;
     */
    public void disiscrizione(Fruitore fruitore, Visita visita) {
        if (puoDisiscriversi(fruitore, visita)) {
            visita.setNumeroIscritti(visita.getNumeroIscritti() - fruitore.getIscrizioni().get(visita).getNumeroDiIscritti());
            fruitore.rimuoviIscrizione(visita);
            visita.setStato(StatoVisita.PROPOSTA);
        }
    }
}