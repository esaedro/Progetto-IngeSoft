package application;
import utility.FileManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Session {

    private Set<Utente> utenti;
    private Set<Luogo> luoghi;
    private Set<TipoVisita> visite;
    private FileManager filemanager;

    private Utente utenteAttivo;

    public Session() {
        this.filemanager = new FileManager("database/");
    }

    public Session(Set<Utente> utenti, Set<Luogo> luoghi, Set<TipoVisita> visite, FileManager filemanager) {
        this.utenti = utenti;
        this.luoghi = luoghi;
        this.visite = visite;
        this.filemanager = filemanager;
    }

    public void salva() {
        salvaStoricoVisite();
        filemanager.salva(FileManager.fileVisite, visite);
        filemanager.salva(FileManager.fileLuoghi, luoghi);
        salvaParametriGlobali();
    }

    public void salvaUtenti() {
        filemanager.salva(FileManager.fileUtenti, utenti);
    }

    private void salvaStoricoVisite() {
        ArrayList<Visita> visiteDaSalvare = new ArrayList<>();

        for(TipoVisita tipoVisita: visite) {
            Iterator<Visita> visiteIterator = tipoVisita.getVisiteAssociate().iterator();
            while (visiteIterator.hasNext()) {
                Visita visita = visiteIterator.next();
                if (visita.getStato() == StatoVisita.EFFETTUATA) {
                    visiteDaSalvare.add(visita);
                    visiteIterator.remove();
                }
            }
        }

        if (!visiteDaSalvare.isEmpty()) {
            Set<Visita> storicoVisite = filemanager.carica(FileManager.fileStorico, Visita.class);
            if (storicoVisite != null) {
                visiteDaSalvare.addAll(storicoVisite);
            }
            filemanager.salva(FileManager.fileStorico, visiteDaSalvare);
            visiteDaSalvare.clear();
        }
    }

    public void salvaParametriGlobali() {
        filemanager.salvaParametriGlobali();
    }

    public void cambiaPassword(Utente utente, String newPassword) {
        for (Utente user : utenti) {
            if (user.getNomeUtente().equals(utente.getNomeUtente())) {
                user.setPassword(newPassword);
            }
        }
        salvaUtenti();
    }

    public void carica() {
        visite = filemanager.carica(FileManager.fileVisite, TipoVisita.class) != null
                ? filemanager.carica(FileManager.fileVisite, TipoVisita.class) : new HashSet<>();
        luoghi = filemanager.carica(FileManager.fileLuoghi, Luogo.class) != null
                ? filemanager.carica(FileManager.fileLuoghi, Luogo.class) : new HashSet<>();
        caricaParametriGlobali();
    }

    public void caricaParametriGlobali() {
        filemanager.caricaParametriGlobali();
    }

    public Utente login(String nomeUtente, String password) {
        utenti = filemanager.carica(FileManager.fileUtenti, Utente.class) != null
                ? filemanager.carica(FileManager.fileUtenti, Utente.class) : new HashSet<>();

        for (Utente user: utenti) {
            if (user.getNomeUtente().equals(nomeUtente) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    public Utente getUtenteAttivo() {
        return utenteAttivo;
    }

    public void setUtenteAttivo(Utente utenteAttivo) {
        this.utenteAttivo = utenteAttivo;
    }

    public Set<Luogo> getLuoghi() {
        return luoghi;
    }

    public void setLuoghi(Set<Luogo> luoghi) {
        this.luoghi = luoghi;
    }

    public void addLuoghi (Set<Luogo> luoghiDaAggiungere) {
        this.luoghi.addAll(luoghiDaAggiungere);
    }

    public void addLuogo(Luogo luogo) {
        this.luoghi.add(luogo);
    }

    // TODO: realize a proxy
    public void removeLuoghi(Set<Luogo> luoghidaRimuovere) {
        this.luoghi.removeAll(luoghidaRimuovere);
    }

    public Set<TipoVisita> getVisite() {
        return visite;
    }

    public void setVisite(Set<TipoVisita> visite) {
        this.visite = visite;
    }

    public void addVisita(TipoVisita visite) {
        this.visite.add(visite);
    }

    public void addTipoVisite(Set<TipoVisita> tipoVisiteToAdd) {
        visite.addAll(tipoVisiteToAdd);
    }

    public Set<TipoVisita> getStoricoVisite() {
        return filemanager.carica(FileManager.fileStorico, TipoVisita.class);
    }

    public ArrayList<TipoVisita> getVisiteAssociateALuogo(Luogo luogo) {
        ArrayList<TipoVisita> visiteResult = new ArrayList<>();
        for (TipoVisita visita: visite) {
            if (luogo.getVisiteIds().contains(visita.getTitolo())) {
                visiteResult.add(visita);
            }
        }
        return visiteResult;
    }

    // TODO: realize a proxy
    public void removeTipoVisita(Set<TipoVisita> visiteDaRimuovere) {
        
        for (TipoVisita tipoVisita : visiteDaRimuovere) {
            for (Luogo luogo : luoghi) {
                luogo.rimuoviVisita(tipoVisita.getTitolo());
            }
        }

        this.visite.removeAll(visiteDaRimuovere);
    }

    public Set<Volontario> getVolontari() {
        Set<Volontario> volontari = new HashSet<>();
        for (Utente utente : utenti) {
            if (utente instanceof Volontario) {
                volontari.add((Volontario) utente);
            }
        }
        return volontari;
    }

    public void addVolontari(Set<Volontario> nuoviVolontari) {
        utenti.addAll(nuoviVolontari);
    }

    // TODO: realize a proxy
    public void removeVolontario(Set<Volontario> volontariDaRimuovere) {
        
        for (Volontario volontario : volontariDaRimuovere) {
            for (TipoVisita tipoVisita : visite) {
                tipoVisita.rimuoviVolontario(volontario);
            }
        }

        utenti.removeAll(volontariDaRimuovere);
    }

    public void salvataggioDatePrecluseFutureInAttuali() {
        TipoVisita.aggiungiDatePrecluseAttuali(TipoVisita.getDatePrecluseFuture());
        TipoVisita.clearDatePrecluseFuture();
        salvaParametriGlobali();
    }

    public void checkCondizioniDiClassi() {
        checkCondizioniDiLuogo();
        checkCondizioniDiTipoVisita();
        chechCondizioniDiVolontario();
    }

    private void checkCondizioniDiLuogo() {
        luoghi.removeIf(luogo -> !luogo.haVisiteAssociate());
    }

    private void chechCondizioniDiVolontario() {
        Iterator<Utente> volontarioIterator = utenti.iterator();
        while (volontarioIterator.hasNext()) {
            Utente utente = volontarioIterator.next();
            if (utente instanceof Volontario && !((Volontario) utente).haVisiteAssociate(visite)) {
                volontarioIterator.remove();

                for (TipoVisita tipoVisita : visite) {
                    tipoVisita.rimuoviVolontario((Volontario) utente);
                }
            }
        }
    }

    private void checkCondizioniDiTipoVisita() {
        Iterator<TipoVisita> tipoVisitaIterator = visite.iterator();
        while (tipoVisitaIterator.hasNext()) {
            TipoVisita tipoVisita = tipoVisitaIterator.next();
            if (!tipoVisita.haLuoghiAssociati(luoghi)) {
                tipoVisitaIterator.remove();
                chechCondizioniDiVolontario();
            }
            if (!tipoVisita.haVolontariAssociati()) {
                for (Luogo luogo : luoghi) {
                    luogo.rimuoviVisita(tipoVisita.getTitolo());
                }
                tipoVisitaIterator.remove();
                checkCondizioniDiLuogo();
            }
        }
    }

    public FileManager getFilemanager() {
        return filemanager;
    }
}