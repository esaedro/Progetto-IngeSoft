package application;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IVisitService {
    Set<TipoVisita> getTipiVisita();
    void salvaVisite();
    void caricaVisite();
    void aggiungiTipoVisita(TipoVisita visitType);
    void aggiungiTipiVisita(Set<TipoVisita> visitTypes);
    void setTipiVisita(Set<TipoVisita> visitTypes);
    void rimuoviTipiVisita(
        Set<TipoVisita> visitTypes,
        IPlaceService placeService,
        IUserService userService
    );
    HashMap<String, Set<Visita>> getStoricoVisite();
    void salvaStoricoVisite();
    Set<TipoVisita> getTipiVisitaProssimoMese(Calendar startMonth, Calendar endMonth);
    void salvaDatePrecluseFutureInAttuali();
    void controllaCondizioniTipiVisita(Set<Luogo> luoghi);
    List<Calendar> estraiDateCasuali(
        Set<Calendar> possibleDates,
        Map<Calendar, Set<Volontario>> volunteerMap,
        int maximum
    );
    List<Volontario> estraiVolontariCasuali(
        List<Calendar> extractedDates,
        Map<Calendar, Set<Volontario>> volunteerMap,
        Set<TipoVisita> visitTypes,
        int maximum
    );
    void creaVisitePerDatiEstratti(
        List<Calendar> extractedDates,
        List<Volontario> extractedVolunteers,
        TipoVisita visitType
    );
    Map<Calendar, Set<Volontario>> creaMappaVolontariPerOgniDataPossibile(
        Set<Volontario> volunteers,
        Set<Calendar> possibleDates
    );
}
