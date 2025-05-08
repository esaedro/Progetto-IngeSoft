package application;

import java.security.Provider.Service;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Override
    public void aggiungiTipoVisita(TipoVisita tipoVisita) {
        tipiVisita.add(tipoVisita);
    }

    @Override
    public void aggiungiTipiVisita(Set<TipoVisita> tipiVisitaDaAggiungere) {
        tipiVisita.addAll(tipiVisitaDaAggiungere);
    }

    @Override
    public void setTipiVisita(Set<TipoVisita> tipiVisita) {
        this.tipiVisita = tipiVisita;
    }

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
            for (Utente fruitore : userService.getUtenti()) {
                if (fruitore instanceof Fruitore) {
                    tipoVisita
                        .getVisiteAssociate()
                        .forEach((visita -> ((Fruitore) fruitore).rimuoviIscrizione(visita)));
                }
            }
        }

        tipiVisita.removeAll(visiteDaRimuovere);
    }

    @Override
    public HashMap<String, Set<Visita>> getStoricoVisite() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStoricoVisite'");
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
            HashMap<String, Set<Visita>> storicoVisite = persistenceService.caricaDatiArchioStorico(
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

    @Override
    public Set<TipoVisita> getTipiVisitaProssimoMese(Calendar startMonth, Calendar endMonth) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTipiVisitaProssimoMese'");
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

    @Override
    public List<Calendar> estraiDateCasuali(
        Set<Calendar> possibleDates,
        Map<Calendar, Set<Volontario>> volunteerMap,
        int maximum
    ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estraiDateCasuali'");
    }

    @Override
    public List<Volontario> estraiVolontariCasuali(
        List<Calendar> extractedDates,
        Map<Calendar, Set<Volontario>> volunteerMap,
        Set<TipoVisita> visitTypes,
        int maximum
    ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'estraiVolontariCasuali'");
    }

    @Override
    public void creaVisitePerDatiEstratti(
        List<Calendar> extractedDates,
        List<Volontario> extractedVolunteers,
        TipoVisita visitType
    ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'creaVisitePerDatiEstratti'");
    }

    @Override
    public Map<Calendar, Set<Volontario>> creaMappaVolontariPerOgniDataPossibile(
        Set<Volontario> volunteers,
        Set<Calendar> possibleDates
    ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'creaMappaVolontariPerOgniDataPossibile'"
        );
    }

    @Override
    public void salvaVisite() {
        persistenceService.salvaDati(tipiVisita);
    }
}
