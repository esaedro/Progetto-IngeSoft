package services;

import java.util.*;

import application.Fruitore;
import application.TipoVisita;
import application.Utente;
import application.Volontario;

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
    void rimuoviVolontari(Set<Volontario> volontari, IVisitService visitService);
    void cambiaPassword(Utente utente, String password);
    void cleanDisponibilitaVolontari();
    void checkCondizioniDiVolontario(Set<TipoVisita> tipiVisita);
}
