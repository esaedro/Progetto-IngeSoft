package services;

import java.util.*;
import application.*;

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
   
    public Set<Visita> getAllVisite();
    public Map<StatoVisita, List<Visita>> separaVisitePerStato(Set<Visita> visite);
    
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
