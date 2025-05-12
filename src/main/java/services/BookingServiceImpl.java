package services;

import java.util.*;
import application.*;

public class BookingServiceImpl implements IBookingService {

    /**
     * @ requires fruitore != null && visita != null && numeroIscritti != null && numeroIscritti > 0;
     */
	@Override
	public boolean puoIscriversi(Utente fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
	    if (visita.getStato() == StatoVisita.PROPOSTA && !fruitore.getIscrizioni().containsKey(visita)) {
            if (tipoVisita != null) {
                return (visita.getNumeroIscritti() + numeroIscritti <= tipoVisita.getMaxPartecipante());
            }
        }
        return false;
	}

	/**
     * @ requires fruitore != null && visita != null;
     */
	@Override
	public boolean puoDisiscriversi(Utente fruitore, Visita visita) {
	    if (fruitore.getIscrizioni().containsKey(visita)) {
            return (visita.getStato() == StatoVisita.COMPLETA ||
                    visita.getStato() == StatoVisita.PROPOSTA);
        }
        return false;
	}

	/**
     * @ requires fruitore != null && visita != null && numeroIscritti != null && numeroIscritti > 0;
     * @ ensures fruitore.getIscrizioni().containsKey(visita) <==> \old(puoIscriversi(fruitore, visita, numeroIscritti));
     * @ ensures \old(puoIscriversi(fruitore, visita, numeroIscritti)) ==> 
     *           visita.getNumeroIscritti() == \old(visita.getNumeroIscritti()) + numeroIscritti;
     * @ ensures \old(puoIscriversi(fruitore, visita, numeroIscritti)) ==> 
     *           fruitore.getIscrizioni().get(visita) != null && 
     *           fruitore.getIscrizioni().get(visita).getNumeroDiIscritti() == numeroIscritti;
     * @ ensures \old(puoIscriversi(fruitore, visita, numeroIscritti)) && 
     *           visita.getNumeroIscritti() == (\exists TipoVisita t; visite.contains(t) && t.getVisiteAssociate().contains(visita); t.getMaxPartecipante()) ==> 
     *           visita.getStato() == StatoVisita.COMPLETA;
     */
	@Override
	public void iscrizione(Utente fruitore, Set<Fruitore> tuttiIFruitori, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
	    if (puoIscriversi(fruitore, visita, numeroIscritti, tipoVisita)) {
            Iscrizione nuovaIscrizione;
            String codiceIscrizione;
            Set<Fruitore> fruitori = tuttiIFruitori;
            List<Iscrizione> iscrizioniVisita = new ArrayList<>(); // Lista di tutte le iscrizioni per la visita fornita in input
            List<String> codiciIscrizioniVisita = new ArrayList<>(); // Lista di tutti i codici di iscrizione per la visita fornita in input

            for (Fruitore f : fruitori) {
                if (f.getIscrizioni().containsKey(visita)) {
                    iscrizioniVisita.add(f.getIscrizioni().get(visita));
                }
            }
            for (Iscrizione i : iscrizioniVisita) {
                codiciIscrizioniVisita.add(i.getCodiceUnivoco());
            }

            // Controllo che il codice creato non sia già presente per le iscrizioni alla stessa visita
            do {
                codiceIscrizione = String.format("%06d", new Random().nextInt(1000000));
            } while (codiciIscrizioniVisita.stream().anyMatch(codiceIscrizione::equals));

            nuovaIscrizione = new Iscrizione(codiceIscrizione, numeroIscritti);
            fruitore.aggiungiIscrizione(visita, nuovaIscrizione);
            visita.setNumeroIscritti(visita.getNumeroIscritti() + numeroIscritti);

            if (tipoVisita != null && visita.getNumeroIscritti() == tipoVisita.getMaxPartecipante()) {
                visita.setStato(StatoVisita.COMPLETA);
            }
        }
	}

	/**
     * @ requires fruitore != null && visita != null;
     * @ ensures fruitore.getIscrizioni().containsKey(visita) == false <==> \old(puoDisiscriversi(fruitore, visita));
     * @ ensures \old(puoDisiscriversi(fruitore, visita)) ==> 
     *           visita.getNumeroIscritti() == \old(visita.getNumeroIscritti()) - \old(fruitore.getIscrizioni().get(visita).getNumeroDiIscritti());
     * @ ensures \old(puoDisiscriversi(fruitore, visita)) ==> visita.getStato() == StatoVisita.PROPOSTA;
     */
	@Override
	public void disiscrizione(Utente fruitore, Visita visita) {
	    if (puoDisiscriversi(fruitore, visita)) {
            visita.setNumeroIscritti(visita.getNumeroIscritti() - fruitore.getIscrizioni().get(visita).getNumeroDiIscritti());
            fruitore.rimuoviIscrizione(visita);
            visita.setStato(StatoVisita.PROPOSTA);
        }
	}}
