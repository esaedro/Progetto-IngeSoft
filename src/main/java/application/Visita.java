package application;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;

public class Visita implements Serializable {
    
    private static int numeroMassimoIscrittoPerFruitore;
    private static Set<Calendar> datePrecluse;
    private String titolo;
    private String descrizione;
    private String puntoIncontro;

    private Calendar dataInizio;
    private Calendar dataFine;
    private Calendar oraInizio;
    private int durata;
    private Set<GiorniSettimana> giorniSettimana;

    private int maxPartecipante;
    private int minPartecipante;

    private Boolean bigliettoIngresso;
    private Luogo luogo;
    private Set<Volontario> volontariIdonei;
    public Visita(String titolo, String descrizione, String puntoIncontro, Calendar dataInizio, Calendar dataFine,
            Calendar oraInizio, int durata, Set<GiorniSettimana> giorniSettimana, int maxPartecipante, int minPartecipante,
            Boolean bigliettoIngresso, Luogo luogo, Set<Volontario> volontariIdonei) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.puntoIncontro = puntoIncontro;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.oraInizio = oraInizio;
        this.durata = durata;
        this.giorniSettimana = giorniSettimana;
        this.maxPartecipante = maxPartecipante;
        this.minPartecipante = minPartecipante;
        this.bigliettoIngresso = bigliettoIngresso;
        this.luogo = luogo;
        this.volontariIdonei = volontariIdonei;
    }
    public static int getNumeroMassimoIscrittoPerFruitore() {
        return numeroMassimoIscrittoPerFruitore;
    }
    public static void setNumeroMassimoIscrittoPerFruitore(int numeroMassimoIscrittoPerFruitore) {
        Visita.numeroMassimoIscrittoPerFruitore = numeroMassimoIscrittoPerFruitore;
    }
    public String getTitolo() {
        return titolo;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public String getPuntoIncontro() {
        return puntoIncontro;
    }
    public void setPuntoIncontro(String puntoIncontro) {
        this.puntoIncontro = puntoIncontro;
    }
    public Calendar getDataInizio() {
        return dataInizio;
    }
    public void setDataInizio(Calendar dataInizio) {
        this.dataInizio = dataInizio;
    }
    public Calendar getDataFine() {
        return dataFine;
    }
    public void setDataFine(Calendar dataFine) {
        this.dataFine = dataFine;
    }
    public Calendar getOraInizio() {
        return oraInizio;
    }
    public void setOraInizio(Calendar oraInizio) {
        this.oraInizio = oraInizio;
    }
    public int getDurata() {
        return durata;
    }
    public void setDurata(int durata) {
        this.durata = durata;
    }
    public Set<GiorniSettimana> getGiorniSettimana() {
        return giorniSettimana;
    }
    public void setGiorniSettimana(Set<GiorniSettimana> giorniSettimana) {
        this.giorniSettimana = giorniSettimana;
    }
    public int getMaxPartecipante() {
        return maxPartecipante;
    }
    public void setMaxPartecipante(int maxPartecipante) {
        this.maxPartecipante = maxPartecipante;
    }
    public int getMinPartecipante() {
        return minPartecipante;
    }
    public void setMinPartecipante(int minPartecipante) {
        this.minPartecipante = minPartecipante;
    }
    public Boolean getBigliettoIngresso() {
        return bigliettoIngresso;
    }
    public void setBigliettoIngresso(Boolean bigliettoIngresso) {
        this.bigliettoIngresso = bigliettoIngresso;
    }
    public Luogo getLuogo() {
        return luogo;
    }
    public void setLuogo(Luogo luogo) {
        this.luogo = luogo;
    }
    public Set<Volontario> getVolontariIdonei() {
        return volontariIdonei;
    }
    public void setVolontariIdonei(Set<Volontario> volontariIdonei) {
        this.volontariIdonei = volontariIdonei;
    }
    public static Set<Calendar> getDatePrecluse() {
        return datePrecluse;
    }
    public static void aggiungiDatePrecluse(Set<Calendar> datePrecluse) {
        datePrecluse.addAll(datePrecluse);
    }

}