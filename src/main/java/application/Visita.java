package application;

import java.util.Calendar;

import utility.Controller;

public class Visita {
    
    private Calendar dataVisita;
    private StatoVisita stato;
    private int numeroIscritti;
    private String titolo;

    /**
     * @ invariant volontarioAssociato != null;
     */
    private Volontario volontarioAssociato;

    /**
     * @ requires volontarioAssociato != null;
     */
    public Visita(Calendar dataVisita, StatoVisita stato, int numeroIscritti, String titolo) {
        this.dataVisita = dataVisita;
        this.stato = stato;
        this.numeroIscritti = numeroIscritti;
        this.titolo = titolo;
    }

    public Visita(String visitaID) {
        String[] params = visitaID.split("-");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(params[2]), Integer.parseInt(params[1]), Integer.parseInt(params[0]));
        this.dataVisita = calendar;

        this.stato = StatoVisita.valueOf(params[3]);
        this.numeroIscritti = Integer.parseInt(params[4]);
        this.titolo = params[5];
    }

    public String getId() {
        return dataVisita.get(Calendar.DAY_OF_MONTH) + "-" + dataVisita.get(Calendar.MONTH)
                + "-" + dataVisita.get(Calendar.YEAR) + "-" + stato + "-" + numeroIscritti +
                "-" + titolo;
    }
    
    public Calendar getDataVisita() {
        return dataVisita;
    }

    public StatoVisita getStato() {
        return stato;
    }

    /**
     * @ requires stato != null;
     */
    public void setStato(StatoVisita stato) {
        this.stato = stato;
    }

    public int getNumeroIscritti() {
        return numeroIscritti;
    }

    /**
     * @ requires stato >= 0;
     */
    public void setNumeroIscritti(int numeroIscritti) {
        this.numeroIscritti = numeroIscritti;
    }

    /**
     * @ requires volontarioAssociato != null;
     */
    public void setVolontarioAssociato(Volontario volontarioAssociato) {
        this.volontarioAssociato = volontarioAssociato;
    }
    
    public Volontario getVolontarioAssociato() {
        return volontarioAssociato;
    }

    /**
     * @ ensures id == getTipoVisitaAssociato(this).getTitolo();
     */
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
        return Controller.getInstance().getTipoVisitaAssociato(this);
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
            sb.append("Titolo: ").append(tipoVisita.getTitolo()).append("\t\t\t\t");
            sb.append("Descrizione: ").append(tipoVisita.getDescrizione()).append("\t\t");
            sb.append("Punto di incontro: ").append(tipoVisita.getPuntoIncontro()).append("\n");
            sb.append("Data di svolgimento: ").append(dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata").append("\t\t");
            sb.append("Ora inizio: ").append(tipoVisita.getOraInizio() != null
                    ? tipoVisita.getOraInizio().get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", tipoVisita.getOraInizio().get(Calendar.MINUTE))
                    : "non specificata").append("\t\t");
            sb.append("Biglietto di ingresso").append(tipoVisita.getBigliettoIngresso() ? " " : " non ").append("necessario\n");
        }

        else if (this.stato == StatoVisita.EFFETTUATA) {
            //sb.append("Titolo: " + (getTipoVisitaTitolo() != null ? getTipoVisitaTitolo() : "non trovato") + "\t\t");
            sb.append("Data svolgimento: ").append(dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata");
        }

        else {
            if (tipoVisita == null) {
                return "Questa visita non è associata ad alcun tipo di visita";
            }
            sb.append("Visita cancellata\n");
            sb.append("Titolo: ").append(tipoVisita.getTitolo()).append("\n");
            sb.append("Data di mancato svolgimento: ").append(dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata");
        }

        return sb.toString();
    }



}