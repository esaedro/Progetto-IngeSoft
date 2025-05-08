package application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Fruitore extends Utente {

    private Map<Visita, Iscrizione> iscrizioni = new HashMap<>();

    /**
     * @ requires nomeUtente != null && password != null;
     */
    public Fruitore(String nomeUtente, String password, Map<Visita, Iscrizione> iscrizioni) {
        super(nomeUtente, password);
        this.iscrizioni = iscrizioni;
    }

    
    @Override  
    public Map<Visita, Iscrizione> getIscrizioni() {
        return iscrizioni;
    }

    /**
     * @ requires visita != null && iscrizione != null;
     */
    @Override
    public void aggiungiIscrizione(Visita visita, Iscrizione iscrizione) {  //TODO: togliere instanceof in session
        iscrizioni.put(visita, iscrizione);
    }

    /**
     * @ requires visita != null;
     */
    @Override
    public void rimuoviIscrizione(Visita visita) {  //TODO: togliere instanceof in session
        iscrizioni.remove(visita);
    }

    @Override
    public Map<StatoVisita, List<Visita>> filtraVisitePerStato(Map<StatoVisita, List<Visita>> visitePerStato) {
        Map<StatoVisita, List<Visita>> visiteDaStampare = visitePerStato;
        visiteDaStampare.remove(StatoVisita.COMPLETA);
        return visiteDaStampare;
    }

    @Override
    public Map<String, Set<Visita>> getStoricoVisiteDaVisualizzare(Map<String, Set<Visita>> storicoVisite) {
        return new HashMap<>(); //Per il frutore non viene stampato lo storico
    }

    @Override
    public TipoMenu getTipoMenu() {
        return TipoMenu.FRUITORE;
    }
}
