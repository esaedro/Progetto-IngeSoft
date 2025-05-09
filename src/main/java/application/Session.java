package application;

import java.util.*;
import services.*;

import services.IBookingService;
import services.IPlaceService;
import services.IVisitService;
import services.ServiceFactory;

public class Session {

    public final IUserService userService;
    public final IVisitService visitService;
    public final IPlaceService placeService;
    public final IPersistenceService persistenceService;
    public final IBookingService bookingService;

    public Session() {
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

    /**
     * @ requires utente != null && newPassword != null && !newPassword.isEmpty();
     * @ ensures utenti.stream().anyMatch(u -> u.getNomeUtente().equals(utente.getNomeUtente())) ==>
     *           utenti.stream().filter(u -> u.getNomeUtente().equals(utente.getNomeUtente()))
     *                  .allMatch(u -> u.getPassword().equals(newPassword));
     * @ ensures \old(salvaUtenti());
     */
    public void cambiaPassword(Utente utente, String newPassword) {
        userService.cambiaPassword(utente, newPassword);
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
        return userService.login(nomeUtente, password);
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

    /**
     * @ requires inizioMese != null && fineMese != null;
     */
    public Set<TipoVisita> getTipiVisiteProssimoMese(Calendar inizioMese, Calendar fineMese) {
        return visitService.getTipiVisitaProssimoMese(inizioMese, fineMese);
    }

    /**
     * @ requires volontari != null && satePossibiliPerVisita != null;
     */
    public Map<Calendar, Set<Volontario>> creaMappaVolontariPerOgniDataPossibile(
        Set<Volontario> volontari,
        Set<Calendar> datePossibiliPerVisita
    ) {
        return visitService.creaMappaVolontariPerOgniDataPossibile(volontari, datePossibiliPerVisita);
    }

    /**
     * @ requires datePossibiliPerVisita != null && mappaVolontariPerVisita != null && massimo != null;
     */
    public List<Calendar> estraiDateCausali(
        Set<Calendar> datePossibiliPerVisita,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        int massimo
    ) {
        return visitService.estraiDateCasuali(datePossibiliPerVisita, mappaVolontariPerData, massimo);
    }

    /**
     * @ requires dateEstratte != null && mappaVolontariPerData != null && tipiVisite != null && massimo != null;
     */
    public List<Volontario> estraiVolontariCasuali(
        List<Calendar> dateEstratte,
        Map<Calendar, Set<Volontario>> mappaVolontariPerData,
        Set<TipoVisita> tipiVisita,
        int massimo
    ) {
        return visitService.estraiVolontariCasuali(dateEstratte, mappaVolontariPerData, tipiVisita, massimo);
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
        visitService.creaVisitePerDatiEstratti(dateEstratte, volontariEstratti, tipoVisita);
    }

    /**
     * @ requires fruitore != null && visita != null && numeroIscritti != null && numeroIscritti > 0;
     */
    public boolean puoIscriversi(Fruitore fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
        return bookingService.puoIscriversi(fruitore, visita, numeroIscritti, tipoVisita);
    }

    /**
     * @ requires fruitore != null && visita != null;
     */
    public boolean puoDisiscriversi(Fruitore fruitore, Visita visita) {
        return bookingService.puoDisiscriversi(fruitore, visita);
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
        bookingService.iscrizione(fruitore, getFruitori(), visita, numeroIscritti, tipoVisita);
    }
    
    /**
     * @ requires fruitore != null && visita != null;
     * @ ensures fruitore.getIscrizioni().containsKey(visita) == false <==> \old(puoDisiscriversi(fruitore, visita));
     * @ ensures \old(puoDisiscriversi(fruitore, visita)) ==> 
     *           visita.getNumeroIscritti() == \old(visita.getNumeroIscritti()) - \old(fruitore.getIscrizioni().get(visita).getNumeroDiIscritti());
     * @ ensures \old(puoDisiscriversi(fruitore, visita)) ==> visita.getStato() == StatoVisita.PROPOSTA;
     */
    public void disiscrizione(Fruitore fruitore, Visita visita) {
        bookingService.disiscrizione(fruitore, visita);
    }
}