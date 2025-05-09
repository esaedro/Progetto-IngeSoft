package services;

import java.util.Set;

import application.Fruitore;
import application.TipoVisita;
import application.Visita;

public interface IBookingService {
    boolean puoIscriversi(Fruitore fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita);
    boolean puoDisiscriversi(Fruitore fruitore, Visita visita);
    void iscrizione(Fruitore fruitore, Set<Fruitore> tuttiIFruitori, Visita visita, int numeroIscritti, TipoVisita tipoVisita);
    void disiscrizione(Fruitore fruitore, Visita visita);
}
