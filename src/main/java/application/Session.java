package application;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import utility.FileManager;

public class Session {

    private Set<Utente> utenti;
    private Set<Luogo> luoghi;
    private Set<TipoVisita> visite;
    private FileManager filemanager;

    private Utente utenteAttivo;

    public Session() {
        this.filemanager = new FileManager("database/");
    }

    public Session(
        Set<Utente> utenti,
        Set<Luogo> luoghi,
        Set<TipoVisita> visite,
        FileManager filemanager
    ) {
        this.utenti = utenti;
        this.luoghi = luoghi;
        this.visite = visite;
        this.filemanager = filemanager;
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
        ArrayList<Visita> visiteDaSalvare = new ArrayList<>();

        for (TipoVisita tipoVisita : visite) {
            Iterator<Visita> visiteIterator = tipoVisita.getVisiteAssociate().iterator();
            while (visiteIterator.hasNext()) {
                Visita visita = visiteIterator.next();
                if (visita.getStato() == StatoVisita.EFFETTUATA) {
                    visiteDaSalvare.add(visita);
                    visiteIterator.remove();
                }
            }
        }

        if (!visiteDaSalvare.isEmpty()) {
            Set<Visita> storicoVisite = filemanager.carica(FileManager.fileStorico, Visita.class);
            if (storicoVisite != null) {
                visiteDaSalvare.addAll(storicoVisite);
            }
            filemanager.salva(FileManager.fileStorico, visiteDaSalvare);
            visiteDaSalvare.clear();
        }
    }

    public void salvaParametriGlobali() {
        filemanager.salvaParametriGlobali();
    }

    public void cambiaPassword(Utente utente, String newPassword) {
        for (Utente user : utenti) {
            if (user.getNomeUtente().equals(utente.getNomeUtente())) {
                user.setPassword(newPassword);
            }
        }
        salvaUtenti();
    }

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

    public Utente login(String nomeUtente, String password) {
        utenti = filemanager.carica(FileManager.fileUtenti, Utente.class) != null
            ? filemanager.carica(FileManager.fileUtenti, Utente.class)
            : new HashSet<>();

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

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    public Utente getUtenteAttivo() {
        return utenteAttivo;
    }

    public void setUtenteAttivo(Utente utenteAttivo) {
        this.utenteAttivo = utenteAttivo;
    }

    public Set<Luogo> getLuoghi() {
        return luoghi;
    }

    public void setLuoghi(Set<Luogo> luoghi) {
        this.luoghi = luoghi;
    }

    public void addLuoghi(Set<Luogo> luoghiDaAggiungere) {
        this.luoghi.addAll(luoghiDaAggiungere);
    }

    public void addLuogo(Luogo luogo) {
        this.luoghi.add(luogo);
    }

    // TODO: realize a proxy
    public void removeLuoghi(Set<Luogo> luoghidaRimuovere) {
        this.luoghi.removeAll(luoghidaRimuovere);
    }

    public Set<TipoVisita> getVisite() {
        return visite;
    }

    public void setVisite(Set<TipoVisita> visite) {
        this.visite = visite;
    }

    public void addVisita(TipoVisita visite) {
        this.visite.add(visite);
    }

    public void addTipoVisite(Set<TipoVisita> tipoVisiteToAdd) {
        visite.addAll(tipoVisiteToAdd);
    }

    public Set<TipoVisita> getStoricoVisite() {
        return filemanager.carica(FileManager.fileStorico, TipoVisita.class);
    }

    public ArrayList<TipoVisita> getVisiteAssociateALuogo(Luogo luogo) {
        ArrayList<TipoVisita> visiteResult = new ArrayList<>();
        for (TipoVisita visita : visite) {
            if (luogo.getVisiteIds().contains(visita.getTitolo())) {
                visiteResult.add(visita);
            }
        }
        return visiteResult;
    }

    // TODO: realize a proxy
    public void removeTipoVisita(Set<TipoVisita> visiteDaRimuovere) {
        for (TipoVisita tipoVisita : visiteDaRimuovere) {
            for (Luogo luogo : luoghi) {
                luogo.rimuoviVisita(tipoVisita.getTitolo());
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

    public void addVolontari(Set<Volontario> nuoviVolontari) {
        utenti.addAll(nuoviVolontari);
    }

    // TODO: realize a proxy
    public void removeVolontario(Set<Volontario> volontariDaRimuovere) {
        for (Volontario volontario : volontariDaRimuovere) {
            for (TipoVisita tipoVisita : visite) {
                tipoVisita.rimuoviVolontario(volontario);
            }
        }

        utenti.removeAll(volontariDaRimuovere);
    }

    public void salvataggioDatePrecluseFutureInAttuali() {
        TipoVisita.aggiungiDatePrecluseAttuali(TipoVisita.getDatePrecluseFuture());
        TipoVisita.clearDatePrecluseFuture();
        salvaParametriGlobali();
    }

    public void checkCondizioniDiClassi() {
        checkCondizioniDiLuogo();
        checkCondizioniDiTipoVisita();
        chechCondizioniDiVolontario();
    }

    private void checkCondizioniDiLuogo() {
        luoghi.removeIf(luogo -> !luogo.haVisiteAssociate());
    }

    private void chechCondizioniDiVolontario() {
        utenti.removeIf(utente -> (utente instanceof Volontario && !((Volontario) utente).haVisiteAssociate(visite)));
}

    private void checkCondizioniDiTipoVisita() {
        Iterator<TipoVisita> tipoVisitaIterator = visite.iterator();
        while (tipoVisitaIterator.hasNext()) {
            TipoVisita tipoVisita = tipoVisitaIterator.next();
            if (!tipoVisita.haLuoghiAssociati(luoghi)) {
                tipoVisitaIterator.remove();
                chechCondizioniDiVolontario();
            }
            if (!tipoVisita.haVolontariAssociati()) {
                tipoVisitaIterator.remove();
                checkCondizioniDiLuogo();
            }
        }
    }

    public FileManager getFilemanager() {
        return filemanager;
    }

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

    public AbstractMap.SimpleImmutableEntry<List<Calendar>, List<Calendar>> estraiDateOreCausali(
        Set<Calendar> datePossibiliPerVisita,
        Set<Calendar> orePossibili,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        int massimo
    ) {
        // Creo un contentore per le date e le ore estratte
        List<Calendar> dateEstratte = new ArrayList<>();
        List<Calendar> oreEstratte = new ArrayList<>();

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

        return new AbstractMap.SimpleImmutableEntry<>(dateEstratte, oreEstratte);
    }

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

    public void creaVisitePerDatiEstratti(
        List<Calendar> dateEstratte,
        List<Calendar> oreEstratte,
        List<Volontario> volontariEstratti,
        TipoVisita tipoVisita
    ) {
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
}
