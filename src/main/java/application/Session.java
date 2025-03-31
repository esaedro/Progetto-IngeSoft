package application;
import utility.FileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Session {

    private ArrayList<Utente> utenti;
    private ArrayList<Luogo> luoghi;
    private ArrayList<TipoVisita> visite;
    private FileManager filemanager;

    public Session() {
        this.filemanager = new FileManager("database/");
    }

    public Session(ArrayList<Utente> utenti, ArrayList<Luogo> luoghi, ArrayList<TipoVisita> visite, FileManager filemanager) {
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
            ArrayList<Visita> storicoVisite = filemanager.carica(FileManager.fileStorico, Visita.class);
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

    public void caricaParametriGlobali() {
        filemanager.caricaParametriGlobali();
    }

    public void carica() {
        visite = filemanager.carica(FileManager.fileVisite, TipoVisita.class) != null
                ? filemanager.carica(FileManager.fileVisite, TipoVisita.class) : new ArrayList<>();
        luoghi = filemanager.carica(FileManager.fileLuoghi, Luogo.class) != null
                ? filemanager.carica(FileManager.fileLuoghi, Luogo.class) : new ArrayList<>();
        caricaParametriGlobali();
    }

    public ArrayList<TipoVisita> getStoricoVisite() {
        return filemanager.carica(FileManager.fileStorico, TipoVisita.class);
    }

    public void cambiaPassword(Utente utente, String newPassword) {
        for (Utente user : utenti) {
            if (user.getNomeUtente().equals(utente.getNomeUtente())) {
                user.setPassword(newPassword);
            }
        }
        salvaUtenti();
    }

    public Utente login(String nomeUtente, String password) {
        utenti = filemanager.carica(FileManager.fileUtenti, Utente.class) != null
                ? filemanager.carica(FileManager.fileUtenti, Utente.class) : new ArrayList<>();

        for (Utente user: utenti) {
            if (user.getNomeUtente().equals(nomeUtente) && user.getPassword().equals(password)) {
                user.setSession(this);
                return user;
            }
            else {
                System.out.println("\nUtente non trovato. Nome utente o password errati.");
            }
        }
        return null;
    }

    public ArrayList<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(ArrayList<Utente> utenti) {
        this.utenti = utenti;
    }

    public ArrayList<Luogo> getLuoghi() {
        return luoghi;
    }

    public void setLuoghi(ArrayList<Luogo> luoghi) {
        this.luoghi = luoghi;
    }

    public void addLuoghi (ArrayList<Luogo> luoghiDaAggiungere) {
        this.luoghi.addAll(luoghiDaAggiungere);
    } 

    public ArrayList<TipoVisita> getVisite() {
        return visite;
    }

    public void setVisite(ArrayList<TipoVisita> visite) {
        this.visite = visite;
    }

    public void addVisita(TipoVisita visite) {
        this.visite.add(visite);
    }

    public void addAllVisite(Set<Visita> visite) {
        visite.addAll(visite);
    }

    public FileManager getFilemanager() {
        return filemanager;
    }

    public void setFilemanager(FileManager filemanager) {
        this.filemanager = filemanager;
    }

    public ArrayList<Volontario> getVolontari() {
        ArrayList<Volontario> volontari = new ArrayList<>();
        for (Utente utente : utenti) {
            if (utente instanceof Volontario) {
                volontari.add((Volontario) utente);
            }
        }
        return volontari;
    }

    public void addAllTipoVisite(Set<TipoVisita> tipoVisiteToAdd) {
        visite.addAll(tipoVisiteToAdd);
    }

    public void salvataggioDatePrecluseFutureInAttuali() {
        TipoVisita.aggiungiDatePrecluseAttuali(TipoVisita.getDatePrecluseFuture());
        TipoVisita.clearDatePrecluseFuture();
        salvaParametriGlobali();
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
}