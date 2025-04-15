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

    public String getIdentificativo() {
        String id = "";
        id += Controller.getInstance().getTipoVisitaAssociato(this).getTitolo();
        id += " " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata");
        return id;
    }

    public String getDataStato() {
        String dataStato = "";

        dataStato += " " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata");
        dataStato += " " + stato;

        return dataStato;
    }

    public TipoVisita getTipoVisita() {
        TipoVisita tipoVisita = Controller.getInstance().getTipoVisitaAssociato(this);
        return tipoVisita;
    }

    public String getTipoVisitaTitolo() {
        return Controller.getInstance().getTipoVisitaTitolo(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        TipoVisita tipoVisita = getTipoVisita();

        if (this.stato != StatoVisita.EFFETTUATA &&  this.stato != StatoVisita.CANCELLATA) {
            if (tipoVisita == null) {
                return "Questa visita non è associata ad alcun tipo di visita";
            }
            sb.append("Titolo: " + tipoVisita.getTitolo() + "\t\t\t\t");
            sb.append("Descrizione: " + tipoVisita.getDescrizione() + "\t\t");
            sb.append("Punto di incontro: " + tipoVisita.getPuntoIncontro() + "\n");
            sb.append("Data di svolgimento: " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata") + "\t\t");
            sb.append("Ora inizio: " + (tipoVisita.getOraInizio() != null 
                ? tipoVisita.getOraInizio().get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", tipoVisita.getOraInizio().get(Calendar.MINUTE)) 
                : "non specificata") + "\t\t");
            sb.append("Biglietto di ingresso" + (tipoVisita.getBigliettoIngresso() ? " " : " non ") + "necessario\n");
        }

        else if (this.stato == StatoVisita.EFFETTUATA) {
            //sb.append("Titolo: " + (getTipoVisitaTitolo() != null ? getTipoVisitaTitolo() : "non trovato") + "\t\t");
            sb.append("Data svolgimento: " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata"));
        }

        else if (this.stato == StatoVisita.CANCELLATA) {
            if (tipoVisita == null) {
                return "Questa visita non è associata ad alcun tipo di visita";
            }
            sb.append("Visita cancellata\n");
            sb.append("Titolo: " + tipoVisita.getTitolo() + "\n");
            sb.append("Data di mancato svolgimento: " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata"));
        }    

        return sb.toString();
    }    
}