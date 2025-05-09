package services;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.Luogo;
import application.TipoVisita;
import application.Visita;
import application.Volontario;

public interface IVisitService {
    Set<TipoVisita> getTipiVisita();
    void salvaVisite();
    void caricaVisite();
    void aggiungiTipoVisita(TipoVisita tipoVisita);
    void aggiungiTipiVisita(Set<TipoVisita> tipiVisita);
    void setTipiVisita(Set<TipoVisita> tipiVisita);
    void rimuoviTipiVisita(
        Set<TipoVisita> tipiVisita,
        IPlaceService placeService,
        IUserService userService
    );
    HashMap<String, Set<Visita>> getStoricoVisite();
    void salvaStoricoVisite();
    Set<TipoVisita> getTipiVisitaProssimoMese(Calendar inizioMese, Calendar fineMese);
    void salvaDatePrecluseFutureInAttuali();
    void controllaCondizioniTipiVisita(Set<Luogo> luoghi);
    List<Calendar> estraiDateCasuali(
        Set<Calendar> datePossibiliPerVisita,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        int massimo
    );
    List<Volontario> estraiVolontariCasuali(
        List<Calendar> dateEstratte,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        Set<TipoVisita> tipiVisita,
        int massimo
    );
    void creaVisitePerDatiEstratti(
        List<Calendar> dateEstratte,
        List<Volontario> volontariEstratti,
        TipoVisita tipoVisita
    );
    Map<Calendar, Set<Volontario>> creaMappaVolontariPerOgniDataPossibile(
        Set<Volontario> volontari,
        Set<Calendar> datePossibiliPerVisita
    );
}
