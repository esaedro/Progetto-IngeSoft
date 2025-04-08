package application;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Set;

public class Visita {
    
    private Calendar dataVisita;
    private StatoVisita stato;
    private int numeroIscritti;
    private Volontario volontarioAssociato;
    
    public Visita(Calendar dataVisita, StatoVisita stato, int numeroIscritti) {
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

    public void setVolontarioAssociato(Volontario volontarioAssociato) {
        this.volontarioAssociato = volontarioAssociato;
    }
    public Volontario getVolontarioAssociato() {
        return volontarioAssociato;
    }

    public String toString() {
        return ("Visita{" +
                "dataVisita=" + (dataVisita != null? dataVisita.getTime() : "nessuna") +
                ", stato=" + stato +
                ", numeroIscritti=" + numeroIscritti +
                '}');
    }
    
}