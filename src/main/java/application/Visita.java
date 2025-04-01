package application;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Set;

public class Visita extends TipoVisita {
    
    private Calendar dataVisita;
    private StatoVisita stato;
    private int numeroIscritti;
    
    public Visita(String titolo, String descrizione, String puntoIncontro, Calendar dataInizio, Calendar dataFine,
            Calendar oraInizio, int durata, Set<DayOfWeek> giorniSettimana, int minPartecipante, int maxPartecipante,
            Boolean bigliettoIngresso, Set<Volontario> volontariIdonei, Calendar dataVisita,
            StatoVisita stato, int numeroIscritti) {
        super(titolo, descrizione, puntoIncontro, dataInizio, dataFine, oraInizio, durata, giorniSettimana,
                minPartecipante, maxPartecipante, bigliettoIngresso, volontariIdonei);
        this.dataVisita = dataVisita;
        this.stato = stato;
        this.numeroIscritti = numeroIscritti;
    }
    
    public Calendar getDataVisita() {
        return dataVisita;
    }
    public void setDataVisita(Calendar dataVisita) {
        this.dataVisita = dataVisita;
    }
    public StatoVisita getStato() {
        return stato;
    }
    public void setStato(StatoVisita stato) {
        this.stato = stato;
    }
    public int getNumeroIscritti() {
        return numeroIscritti;
    }
    public void setNumeroIscritti(int numeroIscritti) {
        this.numeroIscritti = numeroIscritti;
    }

    public String toString() {
        return ("Visita{ " +
                "dataVisita=" + (dataVisita != null? dataVisita.getTime() : "nessuna") +
                ", stato=" + stato +
                ", numeroIscritti=" + numeroIscritti +
                ", " + super.toString() +'}');
    }
    
}