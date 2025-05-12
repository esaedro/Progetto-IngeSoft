package services;

import java.util.*;

import application.*;

public interface IUserService {
    Utente login(String nomeUtente, String password);
    void setUtenti(Set<Utente> utenti);
    Utente getUtenteAttivo();
    void setUtenteAttivo(Utente utente);
    Set<Utente> getUtenti();
    Set<Volontario> getVolontari();
    Set<Fruitore> getFruitori();
    void caricaUtenti();
    void salvaUtenti();
    void aggiungiVolontari(Set<Volontario> volontari);
    void aggiungiFruitore(Fruitore fruitore);
    void rimuoviVolontari(Set<Volontario> volontari, Set<TipoVisita> tipiVisita);
    void cambiaPassword(String password);
    void cleanDisponibilitaVolontari();
    void checkCondizioniDiVolontario(Set<TipoVisita> tipiVisita);
}
