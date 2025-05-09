package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import application.Fruitore;
import application.Iscrizione;
import application.StatoVisita;
import application.TipoVisita;
import application.Visita;

public class BookingServiceImpl implements IBookingService {

	@Override
	public boolean puoIscriversi(Fruitore fruitore, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
	    if (visita.getStato() == StatoVisita.PROPOSTA && !fruitore.getIscrizioni().containsKey(visita)) {
            if (tipoVisita != null) {
                return (visita.getNumeroIscritti() + numeroIscritti <= tipoVisita.getMaxPartecipante());
            }
        }
        return false;
	}

	@Override
	public boolean puoDisiscriversi(Fruitore fruitore, Visita visita) {
	    if (fruitore.getIscrizioni().containsKey(visita)) {
            return (visita.getStato() == StatoVisita.COMPLETA ||
                    visita.getStato() == StatoVisita.PROPOSTA);
        }
        return false;
	}

	@Override
	public void iscrizione(Fruitore fruitore, Set<Fruitore> tuttiIFruitori, Visita visita, int numeroIscritti, TipoVisita tipoVisita) {
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

	@Override
	public void disiscrizione(Fruitore fruitore, Visita visita) {
	    if (puoDisiscriversi(fruitore, visita)) {
            visita.setNumeroIscritti(visita.getNumeroIscritti() - fruitore.getIscrizioni().get(visita).getNumeroDiIscritti());
            fruitore.rimuoviIscrizione(visita);
            visita.setStato(StatoVisita.PROPOSTA);
        }
	}}
