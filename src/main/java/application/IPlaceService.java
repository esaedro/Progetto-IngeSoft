package application;

import java.util.Set;

public interface IPlaceService {
    Set<Luogo> getLuoghi();
    void salvaLuoghi();
    void caricaLuoghi();
    void aggiungiLuogo(Luogo luogodaAggiungere);
    void aggiungiLuoghi(Set<Luogo> luoghiDaAggiungere);
    void setLuoghi(Set<Luogo> luoghi);
    void rimuoviLuoghi(Set<Luogo> luoghiDaRimuovere);
    void controllaCondizioniLuogo();
}
