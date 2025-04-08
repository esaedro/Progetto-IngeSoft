package application;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class TipoVisita implements Serializable {
    
    private static int numeroMassimoIscrittoPerFruitore = 0;
    private static Set<Integer> datePrecluseFuture = new HashSet<>(), datePrecluseAttuali = new HashSet<>();
    private String titolo;
    private String descrizione;
    private String puntoIncontro;

    private Calendar dataInizio;
    private Calendar dataFine;
    private Calendar oraInizio;
    private int durata;
    private Set<DayOfWeek> giorniSettimana;

    private int minPartecipante;
    private int maxPartecipante;

    private Boolean bigliettoIngresso;
    private Set<Volontario> volontariIdonei;

    private ArrayList<Visita> visiteAssociate = new ArrayList<>();

    public TipoVisita(String titolo, String descrizione, String puntoIncontro, Calendar dataInizio, Calendar dataFine,
            Calendar oraInizio, int durata, Set<DayOfWeek> giorniSettimana, int minPartecipante, int maxPartecipante,
            Boolean bigliettoIngresso, Set<Volontario> volontariIdonei) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.puntoIncontro = puntoIncontro;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.oraInizio = oraInizio;
        this.durata = durata;
        this.giorniSettimana = giorniSettimana;
        this.minPartecipante = minPartecipante;
        this.maxPartecipante = maxPartecipante;
        this.bigliettoIngresso = bigliettoIngresso;
        this.volontariIdonei = volontariIdonei;
    }

    public static int getNumeroMassimoIscrittoPerFruitore() {
        return numeroMassimoIscrittoPerFruitore;
    }

    public static void setNumeroMassimoIscrittoPerFruitore(int numeroMassimoIscrittoPerFruitore) {
        TipoVisita.numeroMassimoIscrittoPerFruitore = numeroMassimoIscrittoPerFruitore;
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

    public Set<DayOfWeek> getGiorniSettimana() {
        return giorniSettimana;
    }

    public void setGiorniSettimana(Set<DayOfWeek> giorniSettimana) {
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

    public Set<Volontario> getVolontariIdonei() {
        return volontariIdonei;
    }

    public void setVolontariIdonei(Set<Volontario> volontariIdonei) {
        this.volontariIdonei = volontariIdonei;
    }

    public boolean haVolontariAssociati() {
        return !volontariIdonei.isEmpty();
    }

    public static Set<Integer> getDatePrecluseFuture() {
        return datePrecluseFuture;
    }

    public static void aggiungiDatePrecluseFuture(Set<Integer> datePrecluseDaAggiungere) {
        datePrecluseFuture.addAll(datePrecluseDaAggiungere);
    }

    public static void setDatePrecluseFuture(Set<Integer> datePrecluseDaAggiungere) {
        TipoVisita.datePrecluseFuture = datePrecluseDaAggiungere;
    }

    public static void clearDatePrecluseFuture() {
        datePrecluseFuture.clear();
    }

    public static Set<Integer> getDatePrecluseAttuali() {
        return datePrecluseAttuali;
    }

    public static void aggiungiDatePrecluseAttuali(Set<Integer> datePrecluseDaAggiungere) {
        datePrecluseAttuali.addAll(datePrecluseDaAggiungere);
    }

    public static void setDatePrecluseAttuali(Set<Integer> datePrecluseDaAggiungere) {
        TipoVisita.datePrecluseAttuali = datePrecluseDaAggiungere;
    }

    public ArrayList<Visita> getVisiteAssociate() {
        return visiteAssociate;
    }

    public void setVisiteAssociate(ArrayList<Visita> visiteAssociate) {
        this.visiteAssociate = visiteAssociate;
    }

    public void addVisita(Visita visita) {
        visiteAssociate.add(visita);
    }

    public Set<Luogo> getLuoghiAssociati(Set<Luogo> luoghi) {
        Set<Luogo> luoghiTemp = new HashSet<>(luoghi);
        luoghiTemp.removeIf(luogo -> !luogo.getVisiteIds().contains(titolo));
        return luoghiTemp;
    }

    public boolean haLuoghiAssociati(Set<Luogo> luoghi) {
        return !getLuoghiAssociati(luoghi).isEmpty();
    }

    public void rimuoviVolontario(Volontario volontario) {
        if (volontariIdonei != null && volontariIdonei.contains(volontario)) {
            volontariIdonei.remove(volontario);
        }

        for (Visita visita : visiteAssociate) {
            if (visita.getVolontarioAssociato() != null && visita.getVolontarioAssociato().equals(volontario)) {
                visita.setVolontarioAssociato(null);
                visita.setStato(StatoVisita.CANCELLATA);
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder volontari = new StringBuilder();
        if (volontariIdonei != null) {
            for (Volontario volontario : volontariIdonei) {
                volontari.append(volontario.getNomeUtente()).append(", ");
            }
            if (!volontari.isEmpty()) {
                volontari.setLength(volontari.length() - 2); // Remove trailing comma and space
            }
        }
        return "TipoVisita{ " +
                "titolo='" + titolo + '\'' +
                ", descrizione='" + descrizione + '\'' +
                ", puntoIncontro='" + puntoIncontro + '\'' +
                ", dataInizio=" + (dataInizio != null? dataInizio.getTime() : "nessuna") +
                ", dataFine=" + (dataFine != null? dataFine.getTime() : "nessuna") +
                ", oraInizio=" + (oraInizio != null? oraInizio.getTime() : "nessuna") +
                ", durata=" + durata +
                ", giorniSettimana=" + giorniSettimana +
                ", maxPartecipante=" + maxPartecipante +
                ", minPartecipante=" + minPartecipante +
                ", bigliettoIngresso=" + bigliettoIngresso +
                ", volontariIdonei=[" + volontari + "]" +
                ", visiteAssociate=" + (visiteAssociate != null ? visiteAssociate.toString() : "nessuna") +
                '}';
    }
}