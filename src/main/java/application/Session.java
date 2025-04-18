package application;

import utility.FileManager;

import java.util.*;

public class Session {

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
        this.filemanager = new FileManager("database/");
    }

    public void salva() {
        salvaStoricoVisite();
        filemanager.salva(FileManager.fileVisite, visite);
        filemanager.salva(FileManager.fileLuoghi, luoghi);
        salvaParametriGlobali();
    }

    public void salvaUtenti() {
        filemanager.salva(FileManager.fileUtenti, utenti);
    }

    private void salvaStoricoVisite() {
        // Set<Visita> visiteDaSalvare = new HashSet<>();
        HashMap<String, Set<Visita>> visiteDaSalvare = new HashMap<>();
        for (TipoVisita tipoVisita : visite) {
            Iterator<Visita> visiteIterator = tipoVisita.getVisiteAssociate().iterator();
            while (visiteIterator.hasNext()) {
                Visita visita = visiteIterator.next();
                if (visita.getStato() == StatoVisita.EFFETTUATA) {
                    Set<Visita> visiteTemp = new HashSet<>();
                    if (visiteDaSalvare.get(tipoVisita.getTitolo()) != null) {
                        visiteTemp = visiteDaSalvare.get(tipoVisita.getTitolo());
                    }
                    visiteTemp.add(visita);
                    visiteDaSalvare.put(tipoVisita.getTitolo(), visiteTemp);
                    visiteIterator.remove();
                }
            }
        }

        if (!visiteDaSalvare.isEmpty()) {
            HashMap<String, Set<Visita>> storicoVisite = filemanager.caricaStorico(FileManager.fileStorico, String.class, Visita.class);
            if (storicoVisite != null) {
                // visiteDaSalvare.addAll(storicoVisite);
                visiteDaSalvare.putAll(storicoVisite);
            }
            filemanager.salva(FileManager.fileStorico, visiteDaSalvare);
            visiteDaSalvare.clear();
        }
    }

    public void salvaParametriGlobali() {
        filemanager.salvaParametriGlobali();
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
        visite = filemanager.carica(FileManager.fileVisite, TipoVisita.class) != null
            ? filemanager.carica(FileManager.fileVisite, TipoVisita.class)
            : new HashSet<>();
        luoghi = filemanager.carica(FileManager.fileLuoghi, Luogo.class) != null
            ? filemanager.carica(FileManager.fileLuoghi, Luogo.class)
            : new HashSet<>();
        caricaParametriGlobali();
    }

    public void caricaParametriGlobali() {
        filemanager.caricaParametriGlobali();
    }

    /**
     * @ ensures utenti != null;
     */
    public void caricaUtenti() {
        utenti = filemanager.carica(FileManager.fileUtenti, Utente.class) != null
        ? filemanager.carica(FileManager.fileUtenti, Utente.class)
        : new HashSet<>();
    }

    /**
     * @ requires nomeUtente != null && !nomeUtente.isEmpty() && password != null && !password.isEmpty();
     */
    public Utente login(String nomeUtente, String password) {
        caricaUtenti();

        for (Utente user : utenti) {
            if (user.getNomeUtente().equals(nomeUtente) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    /**
     * @ requires utenti != null;
     */
    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    public Utente getUtenteAttivo() {
        return utenteAttivo;
    }

    /**
     * @ requires utenteAttivo != null;
     */
    public void setUtenteAttivo(Utente utenteAttivo) {
        this.utenteAttivo = utenteAttivo;
    }

    public Set<Luogo> getLuoghi() {
        return luoghi;
    }

    /**
     * @ requires luoghi != null;
     */
    public void setLuoghi(Set<Luogo> luoghi) {
        this.luoghi = luoghi;
    }

    /**
     * @ requires luoghiDaAggiungere != null;
     */
    public void addLuoghi(Set<Luogo> luoghiDaAggiungere) {
        this.luoghi.addAll(luoghiDaAggiungere);
    }

    /**
     * @ requires luogo != null;
     */
    public void addLuogo(Luogo luogo) {
        this.luoghi.add(luogo);
    }

    // TODO: realize a proxy
    /**
     * @ requires luoghiDaRimuovere != null;
     */
    public void removeLuoghi(Set<Luogo> luoghiDaRimuovere) {
        this.luoghi.removeAll(luoghiDaRimuovere);
    }

    public Set<TipoVisita> getVisite() {
        return visite;
    }

    /**
     * @ requires visite != null;
     */
    public void setVisite(Set<TipoVisita> visite) {
        this.visite = visite;
    }

    /**
     * @ requires visite != null;
     */
    public void addVisita(TipoVisita visite) {
        this.visite.add(visite);
    }

    /**
     * @ requires tipoVisiteDaAggiungere != null;
     */
    public void addTipoVisite(Set<TipoVisita> tipoVisiteDaAggiungere) {
        visite.addAll(tipoVisiteDaAggiungere);
    }

    public HashMap<String, Set<Visita>> getStoricoVisite() {
        return filemanager.caricaStorico(FileManager.fileStorico, String.class, Visita.class);
    }

    // TODO: realize a proxy
    /**
     * @ requires visiteDaRimuovere != null;
     */
    public void removeTipoVisita(Set<TipoVisita> visiteDaRimuovere) {
        for (TipoVisita tipoVisita : visiteDaRimuovere) {
            for (Luogo luogo : luoghi) {
                luogo.rimuoviVisita(tipoVisita.getTitolo());
            }
            for (Utente fruitore: utenti) {
                if (fruitore instanceof Fruitore) {
                    tipoVisita.getVisiteAssociate().forEach((visita -> ((Fruitore) fruitore).rimuoviIscrizione(visita)));
                }
            }
        }

        this.visite.removeAll(visiteDaRimuovere);
    }

    public Set<Volontario> getVolontari() {
        Set<Volontario> volontari = new HashSet<>();
        for (Utente utente : utenti) {
            if (utente instanceof Volontario) {
                volontari.add((Volontario) utente);
            }
        }
        return volontari;
    }

    public Set<Fruitore> getFruitori() {
        Set<Fruitore> fruitori = new HashSet<>();
        for (Utente utente : utenti) {
            if (utente instanceof Fruitore) {
                fruitori.add((Fruitore) utente);
            }
        }
        return fruitori;
    }

    /**
     * @ requires nuoviVolontari != null;
     */
    public void addVolontari(Set<Volontario> nuoviVolontari) {
        utenti.addAll(nuoviVolontari);
    }

    /**
     * @ requires visiteDaRimuovere != null;
     */
    public void addFruitore(Fruitore fruitore) {
        utenti.add(fruitore);
    }

    // TODO: realize a proxy
    /**
     * @ requires volontariDaRimuovere != null;
     * @ ensures utenti != null && utenti.stream().allMatch(utente -> utente instanceof Volontario ==> 
     *           utente instanceof Volontario && ((Volontario) utente).getDisponibilita().isEmpty()));
     */
    public void removeVolontario(Set<Volontario> volontariDaRimuovere) {
        for (Volontario volontario : volontariDaRimuovere) {
            for (TipoVisita tipoVisita : visite) {
                tipoVisita.rimuoviVolontario(volontario);
            }
        }

        utenti.removeAll(volontariDaRimuovere);
    }

    public void cleanDisponibilitaDeiVolontari() {
        for (Utente utente : utenti) {
            if (utente instanceof Volontario) {
                ((Volontario) utente).clearDisponibilita();
            }
        }
    }

    public void salvataggioDatePrecluseFutureInAttuali() {
        TipoVisita.aggiungiDatePrecluseAttuali(TipoVisita.getDatePrecluseFuture());
        TipoVisita.clearDatePrecluseFuture();
        salvaParametriGlobali();
    }

    public void checkCondizioniDiClassi() {
        checkCondizioniDiLuogo();
        checkCondizioniDiTipoVisita();
        checkCondizioniDiVolontario();
    }

    private void checkCondizioniDiLuogo() {
        luoghi.removeIf(luogo -> !luogo.haVisiteAssociate());
    }

    private void checkCondizioniDiVolontario() {
        Iterator<Utente> volontarioIterator = utenti.iterator();
        while (volontarioIterator.hasNext()) {
            Utente utente = volontarioIterator.next();
            if (utente instanceof Volontario && !((Volontario) utente).haVisiteAssociate(visite)) {
                volontarioIterator.remove();

                for (TipoVisita tipoVisita : visite) {
                    tipoVisita.rimuoviVolontario((Volontario) utente);
                }
            }
        }
    }

    private void checkCondizioniDiTipoVisita() {
        Iterator<TipoVisita> tipoVisitaIterator = visite.iterator();
        while (tipoVisitaIterator.hasNext()) {
            TipoVisita tipoVisita = tipoVisitaIterator.next();
            if (!tipoVisita.haLuoghiAssociati(luoghi)) {
                tipoVisitaIterator.remove();
                checkCondizioniDiVolontario();
            }
            if (!tipoVisita.haVolontariAssociati()) {
                for (Luogo luogo : luoghi) {
                    luogo.rimuoviVisita(tipoVisita.getTitolo());
                }
                tipoVisitaIterator.remove();
                checkCondizioniDiLuogo();
            }
        }
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