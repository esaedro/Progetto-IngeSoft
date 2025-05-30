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

    /**
     * @ public invariant maxPartecipante >= minPartecipante
     */
    private int minPartecipante;
    private int maxPartecipante;

    private Boolean bigliettoIngresso;

    /**
     * @ public invariant volontariIdonei.size() > 0;
     */
    private Set<Volontario> volontariIdonei;
    private ArrayList<Visita> visiteAssociate = new ArrayList<>();

    public TipoVisita(
        String titolo,
        String descrizione,
        String puntoIncontro,
        Calendar dataInizio,
        Calendar dataFine,
        Calendar oraInizio,
        int durata,
        Set<DayOfWeek> giorniSettimana,
        int minPartecipante,
        int maxPartecipante,
        Boolean bigliettoIngresso,
        Set<Volontario> volontariIdonei
    ) {
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

    /**
     * @ requires numeroMassimoIscrittoPerFruitore >= 1
     */
    public static void setNumeroMassimoIscrittoPerFruitore(int numeroMassimoIscrittoPerFruitore) {
        TipoVisita.numeroMassimoIscrittoPerFruitore = numeroMassimoIscrittoPerFruitore;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getPuntoIncontro() {
        return puntoIncontro;
    }

    public Calendar getDataInizio() {
        return dataInizio;
    }

    public Calendar getDataFine() {
        return dataFine;
    }

    public Calendar getOraInizio() {
        return oraInizio;
    }

    public int getDurata() {
        return durata;
    }

    public int getMaxPartecipante() {
        return maxPartecipante;
    }

    public int getMinPartecipante() {
        return minPartecipante;
    }

    public Set<DayOfWeek> getGiorniSettimana() {
        return giorniSettimana;
    }
    
    public Boolean getBigliettoIngresso() {
        return bigliettoIngresso;
    }

    public Set<Volontario> getVolontariIdonei() {
        return volontariIdonei;
    }

    public boolean haVolontariAssociati() {
        return !volontariIdonei.isEmpty();
    }

    public static Set<Integer> getDatePrecluseFuture() {
        return datePrecluseFuture;
    }

    /**
     * @ requires datePrecluseDaAggiungere != null
     * @ ensures datePrecluseFuture.contains(datePrecluseDaAggiungere);
     */
    public static void aggiungiDatePrecluseFuture(Set<Integer> datePrecluseDaAggiungere) {
        datePrecluseFuture.addAll(datePrecluseDaAggiungere);
    }

    /**
     * @ requires datePrecluseDaAggiungere != null
     * @ ensures datePrecluseFuture != null && datePrecluseFuture.size() > 0
     */
    public static void setDatePrecluseFuture(Set<Integer> datePrecluseDaAggiungere) {
        TipoVisita.datePrecluseFuture = datePrecluseDaAggiungere;
    }

    public static void clearDatePrecluseFuture() {
        datePrecluseFuture.clear();
    }

    public static Set<Integer> getDatePrecluseAttuali() {
        return datePrecluseAttuali;
    }

    /**
     * @ requires datePrecluseDaAggiungere != null
     * @ ensures datePrecluseAttuali.contains(datePrecluseDaAggiungere);
     */
    public static void aggiungiDatePrecluseAttuali(Set<Integer> datePrecluseDaAggiungere) {
        datePrecluseAttuali.addAll(datePrecluseDaAggiungere);
    }

    /**
     * @ requires datePrecluseDaAggiungere != null
     * @ ensures datePrecluseAttuali != null && datePrecluseAttuali.size() > 0
     */
    public static void setDatePrecluseAttuali(Set<Integer> datePrecluseDaAggiungere) {
        TipoVisita.datePrecluseAttuali = datePrecluseDaAggiungere;
    }

    public ArrayList<Visita> getVisiteAssociate() {
        return visiteAssociate;
    }

    public void addVisita(Visita visita) {
        visiteAssociate.add(visita);
    }

    /**
     * @ requires luoghi != null && luoghi.size() > 0
     */
    public Set<Luogo> getLuoghiAssociati(Set<Luogo> luoghi) {
        Set<Luogo> luoghiTemp = new HashSet<>(luoghi);
        luoghiTemp.removeIf(luogo -> !luogo.getVisiteIds().contains(titolo));
        return luoghiTemp;
    }

    public boolean haLuoghiAssociati(Set<Luogo> luoghi) {
        return !getLuoghiAssociati(luoghi).isEmpty();
    }

    /**
     * @ requires volontario != null
     * @ ensures volontariIdonei == null || !volontariIdonei.contains(volontario);
     * @ ensures (\forall Visita v; visiteAssociate.contains(v) && v.getVolontarioAssociato() != null && v.getVolontarioAssociato().equals(volontario); 
     *            v.getVolontarioAssociato() == null && v.getStato() == StatoVisita.CANCELLATA);
     */
    public void rimuoviVolontario(Volontario volontario) {
        if (volontariIdonei != null) {
            volontariIdonei.remove(volontario);
        }

        for (Visita visita : visiteAssociate) {
            if (visita.getVolontarioAssociato() != null && visita.getVolontarioAssociato().equals(volontario)) {
                visita.setVolontarioAssociato(null);
                visita.setStato(StatoVisita.CANCELLATA);
            }
        }
    }

    /**
     * @ requires volontariDaAggiungere != null && volontariDaAggiungere > 0
     * @ ensures volontariIdonei != null;
     * @ ensures (\forall Volontario v; volontariDaAggiungere.contains(v); volontariIdonei.contains(v));
     */
    public void aggiungiVolontariIdonei(Set<Volontario> volontariDaAggiungere) {
        if (volontariIdonei == null) {
            volontariIdonei = new HashSet<>();
        }
        volontariIdonei.addAll(volontariDaAggiungere);
    }

    public static Set<Calendar> getDatePossibiliAttuali(Calendar fineMese) {
        // È meglio creare un insieme di date possibili, togliendo già quelle precluse per evitare di dover fare dei controlli successivamente
        Set<Integer> datePrecluse = TipoVisita.getDatePrecluseAttuali();
        Set<Calendar> datePossibili = new HashSet<>();

        // Riempio l'insieme delle date possibili

        for (int i = 1; i <= fineMese.getMaximum(Calendar.DAY_OF_MONTH); i++) {
            if (!datePrecluse.contains(i)) {
                Calendar temp = Calendar.getInstance();
                temp.add(Calendar.MONTH, 1);
                temp.set(Calendar.HOUR_OF_DAY, 0);
                temp.set(Calendar.MINUTE, 0);
                temp.set(Calendar.SECOND, 0);
                temp.set(Calendar.DAY_OF_MONTH, i);
                datePossibili.add(temp);
            }
        }

        return datePossibili;
    }

    public Set<Calendar> getDatePossibiliPerVisita(Calendar inizioMese, Calendar fineMese, Set<Calendar> datePossibili) { 
        if (inizioMese.after(dataFine) || fineMese.before(dataInizio)) {
            return new HashSet<>();
        }

        Calendar fineVisita = dataFine.after(fineMese.getTime()) ? fineMese : dataFine;

        // Calcolo le date possibili per questa visita
        Set<Calendar> datePossibiliPerVisita = new HashSet<>();
        for (Calendar data : datePossibili) {
            if (data.before(fineVisita) && data.after(dataInizio) && giorniSettimana.contains(DayOfWeek.of(data.get(Calendar.DAY_OF_WEEK)))) {
                datePossibiliPerVisita.add(data);
            } else if (data.equals(fineVisita) && data.after(dataInizio) && giorniSettimana.contains(DayOfWeek.of(data.get(Calendar.DAY_OF_WEEK)))) {
                datePossibiliPerVisita.add(data);
                break;
            }
        }
        
        // Imposto l'ora di inizio per ogni data possibile
        datePossibiliPerVisita.forEach(data -> {
            data.set(Calendar.HOUR_OF_DAY, oraInizio.get(Calendar.HOUR_OF_DAY));
            data.set(Calendar.MINUTE, oraInizio.get(Calendar.MINUTE));
            data.set(Calendar.SECOND, oraInizio.get(Calendar.SECOND));
        });
        
        return datePossibiliPerVisita;
    }



}