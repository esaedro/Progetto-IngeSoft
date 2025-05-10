package application;

import java.util.*;
import services.*;
import services.IBookingService;
import services.IPlaceService;
import services.IVisitService;
import services.ServiceFactory;

public class SessionController {

    public final IUserService userService;
    public final IVisitService visitService;
    public final IPlaceService placeService;
    public final IPersistenceService persistenceService;
    public final IBookingService bookingService;

    public SessionController() {
        ServiceFactory factory = new ServiceFactory();
        userService = factory.createUserService();
        visitService = factory.createVisitService();
        placeService = factory.createPlaceService();
        persistenceService = factory.createPersistenceService();
        bookingService = factory.createBookingService();
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

    public void cambiaPassword(Utente utente, String newPassword) {
        userService.cambiaPassword(utente, newPassword);
    }

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

    public Utente login(String nomeUtente, String password) {
        caricaUtenti();
        return userService.login(nomeUtente, password);
    }

    public Set<Utente> getUtenti() {
        return userService.getUtenti();
    }

    public void setUtenti(Set<Utente> utenti) {
        userService.setUtenti(utenti);
    }

    public Utente getUtenteAttivo() {
        return userService.getUtenteAttivo();
    }

    public void setUtenteAttivo(Utente utenteAttivo) {
        userService.setUtenteAttivo(utenteAttivo);
    }

    public Set<Luogo> getLuoghi() {
        return placeService.getLuoghi();
    }

    public void setLuoghi(Set<Luogo> luoghi) {
        placeService.setLuoghi(luoghi);
    }

    public void addLuoghi(Set<Luogo> luoghiDaAggiungere) {
        placeService.aggiungiLuoghi(luoghiDaAggiungere);
    }

    public void addLuogo(Luogo luogo) {
        placeService.aggiungiLuogo(luogo);
    }

    public void removeLuoghi(Set<Luogo> luoghiDaRimuovere) {
        placeService.rimuoviLuoghi(luoghiDaRimuovere);
    }

    public Set<TipoVisita> getVisite() {
        return visitService.getTipiVisita();
    }

    public void setVisite(Set<TipoVisita> visite) {
        visitService.setTipiVisita(visite);
    }

    public void addVisita(TipoVisita visita) {
        visitService.aggiungiTipoVisita(visita);
    }

    public void addTipoVisite(Set<TipoVisita> tipiVisitaDaAggiungere) {
        visitService.aggiungiTipiVisita(tipiVisitaDaAggiungere);
    }

    public HashMap<String, Set<Visita>> getStoricoVisite() {
        return persistenceService.caricaDatiArchioStorico(String.class, Visita.class);
    }

    public void removeTipoVisita(Set<TipoVisita> visiteDaRimuovere) {
        visitService.rimuoviTipiVisita(visiteDaRimuovere, placeService, userService);
    }

    public Set<Volontario> getVolontari() {
        return userService.getVolontari();
    }

    public Set<Fruitore> getFruitori() {
        return userService.getFruitori();
    }

    public void addVolontari(Set<Volontario> nuoviVolontari) {
        userService.aggiungiVolontari(nuoviVolontari);
    }

    public void addFruitore(Fruitore fruitore) {
        userService.aggiungiFruitore(fruitore);
    }

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

    public Set<TipoVisita> getTipiVisiteProssimoMese(Calendar inizioMese, Calendar fineMese) {
        return visitService.getTipiVisitaProssimoMese(inizioMese, fineMese);
    }

    public Map<Calendar, Set<Volontario>> creaMappaVolontariPerOgniDataPossibile(
        Set<Volontario> volontari,
        Set<Calendar> datePossibiliPerVisita
    ) {
        return visitService.creaMappaVolontariPerOgniDataPossibile(
            volontari,
            datePossibiliPerVisita
        );
    }

    public List<Calendar> estraiDateCausali(
        Set<Calendar> datePossibiliPerVisita,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        int massimo
    ) {
        return visitService.estraiDateCasuali(
            datePossibiliPerVisita,
            mappaVolontariPerData,
            massimo
        );
    }

    public List<Volontario> estraiVolontariCasuali(
        List<Calendar> dateEstratte,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        Set<TipoVisita> tipiVisita,
        int massimo
    ) {
        return visitService.estraiVolontariCasuali(
            dateEstratte,
            mappaVolontariPerData,
            tipiVisita,
            massimo
        );
    }

    public void creaVisitePerDatiEstratti(
        List<Calendar> dateEstratte,
        List<Volontario> volontariEstratti,
        TipoVisita tipoVisita
    ) {
        visitService.creaVisitePerDatiEstratti(dateEstratte, volontariEstratti, tipoVisita);
    }

    public boolean puoIscriversi(
        Fruitore fruitore,
        Visita visita,
        int numeroIscritti,
        TipoVisita tipoVisita
    ) {
        return bookingService.puoIscriversi(fruitore, visita, numeroIscritti, tipoVisita);
    }

    public boolean puoDisiscriversi(Fruitore fruitore, Visita visita) {
        return bookingService.puoDisiscriversi(fruitore, visita);
    }

    public void iscrizione(
        Fruitore fruitore,
        Visita visita,
        int numeroIscritti,
        TipoVisita tipoVisita
    ) {
        bookingService.iscrizione(fruitore, getFruitori(), visita, numeroIscritti, tipoVisita);
    }

    public void disiscrizione(Fruitore fruitore, Visita visita) {
        bookingService.disiscrizione(fruitore, visita);
    }
}
