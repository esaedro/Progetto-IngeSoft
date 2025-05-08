package application;

import java.util.*;

public interface IUserService {
    Utente login(String username, String password);
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
    void changePassword(Utente user, String newPassword);
    void cleanDisponibilitaVolontari();
    void checkCondizioniDiVolontario(Set<TipoVisita> tipiVisita);
}
