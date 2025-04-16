package application;

import java.util.Calendar;

public class Visita {
    
    private Calendar dataVisita;
    private StatoVisita stato;
    private int numeroIscritti = 0;
    private String titolo;

    /**
     * @ invariant volontarioAssociato != null;
     */
    private Volontario volontarioAssociato;

    /**
     * @ requires volontarioAssociato != null;
     */
    public Visita(String titolo, Calendar dataVisita, StatoVisita stato, int numeroIscritti) {
        this.titolo = titolo;
        this.dataVisita = dataVisita;
        this.stato = stato;
        this.numeroIscritti = numeroIscritti;

    }

    public String getTitolo() {
        return titolo;
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
        id += titolo;
        id += " " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata");
        return id;
    }

    public String getDataStato() {
        String dataStato = "";
        dataStato += " " + (dataVisita != null ? dataVisita.get(Calendar.DAY_OF_MONTH) + "/" + (dataVisita.get(Calendar.MONTH) + 1) + "/" + dataVisita.get(Calendar.YEAR) : "non specificata");
        dataStato += " " + stato;
        return dataStato;
    }    
}