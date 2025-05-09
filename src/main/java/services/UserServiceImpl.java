package services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import application.Fruitore;
import application.TipoVisita;
import application.Utente;
import application.Volontario;

public class UserServiceImpl implements IUserService {

    private Set<Utente> utenti;
    private Utente utenteAttivo;
    private final IPersistenceService persistenceService;

    public UserServiceImpl(ServiceFactory factory) {
        this.utenti = new HashSet<>();
        this.persistenceService = factory.createPersistenceService();
    }

    @Override
    public Utente login(String nomeUtente, String password) {
        for (Utente user : getUtenti()) {
            if (user.getNomeUtente().equals(nomeUtente) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    @Override
    public Set<Utente> getUtenti() {
        return utenti;
    }

    @Override
    public Utente getUtenteAttivo() {
        return utenteAttivo;
    }

    @Override
    public void setUtenteAttivo(Utente utente) {
        utenteAttivo = utente;
    }

    @Override
    public Set<Volontario> getVolontari() {
        Set<Volontario> volontari = new HashSet<>();
        for (Utente utente : utenti) {
            if (utente instanceof Volontario) {
                volontari.add((Volontario) utente);
            }
        }
        return volontari;
    }

    @Override
    public Set<Fruitore> getFruitori() {
        Set<Fruitore> fruitori = new HashSet<>();
        for (Utente utente : utenti) {
            if (utente instanceof Fruitore) {
                fruitori.add((Fruitore) utente);
            }
        }
        return fruitori;
    }

    @Override
    public void caricaUtenti() {
        utenti = persistenceService.caricaDati(Utente.class) != null
            ? persistenceService.caricaDati(Utente.class)
            : new HashSet<>();
    }

    @Override
    public void salvaUtenti() {
        persistenceService.salvaDati(utenti);
    }

    @Override
    public void aggiungiVolontari(Set<Volontario> volontari) {
        utenti.addAll(volontari);
    }

    @Override
    public void aggiungiFruitore(Fruitore fruitore) {
        utenti.add(fruitore);
    }

    @Override
    public void rimuoviVolontari(Set<Volontario> volontariDaRimuovere, IVisitService visitService) {
        for (Volontario volontario : volontariDaRimuovere) {
            for (TipoVisita tipoVisita : visitService.getTipiVisita()) {
                tipoVisita.rimuoviVolontario(volontario);
            }
        }

        utenti.removeAll(volontariDaRimuovere);
    }

    @Override
    public void cambiaPassword(Utente utente, String newPassword) {
        for (Utente user : utenti) {
            if (user.getNomeUtente().equals(utente.getNomeUtente())) {
                user.setPassword(newPassword);
            }
        }
        salvaUtenti();
    }

    @Override
    public void cleanDisponibilitaVolontari() {
        for (Utente utente : utenti) {
            if (utente instanceof Volontario) {
                ((Volontario) utente).clearDisponibilita();
            }
        }
    }

    @Override
    public void checkCondizioniDiVolontario(Set<TipoVisita> tipiVisita) {
        Iterator<Utente> volontarioIterator = utenti.iterator();
        while (volontarioIterator.hasNext()) {
            Utente utente = volontarioIterator.next();
            if (
                utente instanceof Volontario && !((Volontario) utente).haVisiteAssociate(tipiVisita)
            ) {
                volontarioIterator.remove();

                for (TipoVisita tipoVisita : tipiVisita) {
                    tipoVisita.rimuoviVolontario((Volontario) utente);
                }
            }
        }
    }
}
