package services;

import java.util.*;
import application.*;

public class VisitServiceImpl implements IVisitService {

    private Set<TipoVisita> tipiVisita;
    private final IPersistenceService persistenceService;

    public VisitServiceImpl(ServiceFactory factory) {
        tipiVisita = new HashSet<>();
        persistenceService = factory.createPersistenceService();
    }

    @Override
    public Set<TipoVisita> getTipiVisita() {
        return tipiVisita;
    }

    @Override
    public void caricaVisite() {
        tipiVisita = persistenceService.caricaDati(TipoVisita.class);
    }

    /**
     * @ requires tipoVisita != null;
     */
    @Override
    public void aggiungiTipoVisita(TipoVisita tipoVisita) {
        tipiVisita.add(tipoVisita);
    }

    /**
     * @ requires tipoVisitaDaAggiungere != null;
     */
    @Override
    public void aggiungiTipiVisita(Set<TipoVisita> tipiVisitaDaAggiungere) {
        tipiVisita.addAll(tipiVisitaDaAggiungere);
    }

    /**
     * @ requires tipiVisita != null;
     */
    @Override
    public void setTipiVisita(Set<TipoVisita> tipiVisita) {
        this.tipiVisita = tipiVisita;
    }

    /**
     * @ requires visiteDaRimuovere != null;
     */
    @Override
    public void rimuoviTipiVisita(
        Set<TipoVisita> visiteDaRimuovere,
        IPlaceService placeService,
        IUserService userService
    ) {
        for (TipoVisita tipoVisita : visiteDaRimuovere) {
            for (Luogo luogo : placeService.getLuoghi()) {
                luogo.rimuoviVisita(tipoVisita.getTitolo());
            }
            for (Utente fruitore : userService.getFruitori()) {
                    tipoVisita
                        .getVisiteAssociate()
                        .forEach(visita -> fruitore.rimuoviIscrizione(visita));
            }
        }

        tipiVisita.removeAll(visiteDaRimuovere);
    }

    @Override
    public Set<Visita> getAllVisite() {
        Set<Visita> visite = new HashSet<>();
        if (tipiVisita != null) {
            for (TipoVisita tipoVisita : tipiVisita) {
                if (tipoVisita.getVisiteAssociate() != null) {
                    visite.addAll(tipoVisita.getVisiteAssociate());
                }
            }
        }

        for (Map.Entry<String, Set<Visita>> entry : 
                persistenceService.caricaDatiArchivioStorico(String.class, Visita.class).entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                visite.addAll(entry.getValue());
            }
        }
        return visite;
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

        visitePerStato.remove(StatoVisita.NON_ISTANZIATA);
        visitePerStato.remove(StatoVisita.EFFETTUATA); //verranno visualizzate dall'archivio

        return visitePerStato;
    }


    @Override
    public void salvaStoricoVisite() {
        // Set<Visita> visiteDaSalvare = new HashSet<>();
        HashMap<String, Set<Visita>> visiteDaSalvare = new HashMap<>();
        for (TipoVisita tipoVisita : tipiVisita) {
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
            HashMap<String, Set<Visita>> storicoVisite = persistenceService.caricaDatiArchivioStorico(
                String.class,
                Visita.class
            );
            if (storicoVisite != null) {
                // visiteDaSalvare.addAll(storicoVisite);
                visiteDaSalvare.putAll(storicoVisite);
            }
            persistenceService.salvaDatiArchivioStorico(visiteDaSalvare);
            visiteDaSalvare.clear();
        }
    }

    /**
     * @ requires inizioMese != null && fineMese != null;
     */
    @Override
    public Set<TipoVisita> getTipiVisitaProssimoMese(Calendar inizioMese, Calendar fineMese) {
        // Ordino le visite in base alla data di fine per evitare che le visite che terminano a breve non vengano istanziate
        Set<TipoVisita> risultato = new TreeSet<>(Comparator.comparing(TipoVisita::getDataFine));
        risultato.addAll(tipiVisita);

        // Rimuovo tutti i tipi di vista che finiscono entro questo mese
        risultato.removeIf(tipoVisita -> tipoVisita.getDataFine().before(inizioMese.getTime()));

        // Rimuovo tutti tipi di visita che iniziano a più di due mesi da oggi
        risultato.removeIf(tipoVisita -> tipoVisita.getDataInizio().after(fineMese.getTime()));

        return risultato;
    }

    @Override
    public void salvaDatePrecluseFutureInAttuali() {
        TipoVisita.aggiungiDatePrecluseAttuali(TipoVisita.getDatePrecluseFuture());
        TipoVisita.clearDatePrecluseFuture();
        persistenceService.salvaParametriGlobali();
    }

    @Override
    public void controllaCondizioniTipiVisita(Set<Luogo> luoghi) {
        Iterator<TipoVisita> tipoVisitaIterator = tipiVisita.iterator();
        while (tipoVisitaIterator.hasNext()) {
            TipoVisita tipoVisita = tipoVisitaIterator.next();
            if (!tipoVisita.haLuoghiAssociati(luoghi)) {
                tipoVisitaIterator.remove();
                // checkCondizioniDiVolontario(tipiVisita);
            }
            if (!tipoVisita.haVolontariAssociati()) {
                for (Luogo luogo : luoghi) {
                    luogo.rimuoviVisita(tipoVisita.getTitolo());
                }
                tipoVisitaIterator.remove();
                // checkCondizioniDiLuogo();
            }
        }
    }

    /**
     * @ requires datePossibiliPerVisita != null && mappaVolontariPerVisita != null && massimo != null;
     */
    @Override
    public List<Calendar> estraiDateCasuali(
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
                    .toList()
            ); // Rimuovo tutte le date della stessa settimana
        }

        return dateEstratte;
    }

    /**
     * @ requires dateEstratte != null && mappaVolontariPerData != null && tipiVisite != null && massimo != null;
     */
    @Override
    public List<Volontario> estraiVolontariCasuali(
        List<Calendar> dateEstratte,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        Set<TipoVisita> tipiVisita,
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
                for (TipoVisita tipoV : volontarioEstratto.getTipiVisiteAssociate(tipiVisita)) {
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
    @Override
    public void creaVisitePerDatiEstratti(
        List<Calendar> dateEstratte,
        List<Volontario> volontariEstratti,
        TipoVisita tipoVisita
    ) {
        // Per ogni data estratta creo una visita e la aggiungo al tipo di visita
        for (int i = 0; i < Math.min(dateEstratte.size(), volontariEstratti.size()); i++) {
            Calendar dataVisita = dateEstratte.get(i);
            Visita nuovaVisita = new Visita(
                tipoVisita.getTitolo(),
                dataVisita,
                StatoVisita.PROPOSTA,
                0
            );
            nuovaVisita.setVolontarioAssociato(volontariEstratti.get(i));
            tipoVisita.addVisita(nuovaVisita);
        }
    }

    /**
     * @ requires volontari != null && datePossibiliPerVisita != null;
     */
    @Override
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

    @Override
    public void salvaVisite() {
        persistenceService.salvaDati(tipiVisita);
    }
}
