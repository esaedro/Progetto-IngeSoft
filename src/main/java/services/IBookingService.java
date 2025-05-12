package services;

import java.util.Set;

import application.*;

public interface IBookingService {
    boolean puoIscriversi(Utente fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita);
    boolean puoDisiscriversi(Utente fruitore, Visita visita);
    void iscrizione(Utente fruitore, Set<Fruitore> tuttiIFruitori, Visita visita, int numeroIscritti, TipoVisita tipoVisita);
    void disiscrizione(Utente fruitore, Visita visita);
}
