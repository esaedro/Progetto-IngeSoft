package application;

import java.util.Calendar;

import utility.Controller;

public class Visita {
    
    private Calendar dataVisita;
    private StatoVisita stato;
    private int numeroIscritti;

    //@ public invariant volontarioAssociato =! null;
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
        TipoVisita tipoVisita = Controller.getIstance().getTipoVisitaAssociato(this);
        if (tipoVisita == null) {
            return "Questa visita non è associata ad alcun tipo di visita";
        }
        StringBuilder sb = new StringBuilder();

        if (this.stato != StatoVisita.CANCELLATA) {
            sb.append("Titolo: " + tipoVisita.getTitolo() + "\n");
            sb.append("Descrizione: " + tipoVisita.getDescrizione() + "\n");
            sb.append("Punto di incontro: " + tipoVisita.getPuntoIncontro() + "\n");
            sb.append("Data di svolgimento: " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata") + "\n");
            sb.append("Ora inizio: " + (tipoVisita.getOraInizio() != null 
                ? tipoVisita.getOraInizio().get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", tipoVisita.getOraInizio().get(Calendar.MINUTE)) 
                : "non specificata") + "\n");
            sb.append("Biglietto di ingresso" + (tipoVisita.getBigliettoIngresso() ? " " : " non ") + "necessario\n");
        }
        else {
            sb.append("Visita cancellata\n");
            sb.append("Titolo: " + tipoVisita.getTitolo() + "\n");
            sb.append("Data di mancato svolgimento: " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata") + "\n");
        }

        return sb.toString();

       /*  return ("Visita{" +
                "dataVisita=" + (dataVisita != null? dataVisita.getTime() : "nessuna") +
                ", stato=" + stato +
                ", numeroIscritti=" + numeroIscritti +
                '}'); */
    }
    
}