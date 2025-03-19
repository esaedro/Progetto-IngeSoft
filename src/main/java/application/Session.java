package application;
import utility.FileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;

public class Session {

    private ArrayList<Utente> utenti;
    private ArrayList<Luogo> luoghi;
    private ArrayList<Visita> visite;
    private ArrayList<TipoVisita> tipoVisite;
    private FileManager filemanager;

    public Session() {
        this.filemanager = new FileManager("database/");
    }

    public Session(ArrayList<Utente> utenti, ArrayList<Luogo> luoghi, ArrayList<Visita> visite, ArrayList<TipoVisita> tipoVisite, FileManager filemanager) {
        this.utenti = utenti;
        this.luoghi = luoghi;
        this.visite = visite;
        this.tipoVisite = tipoVisite;
        this.filemanager = filemanager;
    }

    public void salva() {
        salvaStoricoVisite();
        filemanager.salva(FileManager.fileTipoVisite, tipoVisite);
        filemanager.salva(FileManager.fileVisite, visite);
        filemanager.salva(FileManager.fileLuoghi, luoghi);
        salvaParametriGlobali();
    }

    public void salvaUtenti() {
        filemanager.salva(FileManager.fileUtenti, utenti);
    }

    private void salvaStoricoVisite() {
        Iterator<Visita> iteratorVisita = visite.iterator();
        ArrayList<Visita> visiteDaSalvare = new ArrayList<>();
        while(iteratorVisita.hasNext()) {
            Visita visita = iteratorVisita.next();
            if (visita.getStato() == StatoVisita.EFFETTUATA) {
                visiteDaSalvare.add(visita);
                iteratorVisita.remove();
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
        visite = filemanager.carica(FileManager.fileVisite, Visita.class);
        luoghi = filemanager.carica(FileManager.fileLuoghi, Luogo.class);
        tipoVisite = filemanager.carica(FileManager.fileTipoVisite, TipoVisita.class);
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
        utenti = filemanager.carica(FileManager.fileUtenti, Utente.class);

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

    public ArrayList<Visita> getVisite() {
        return visite;
    }

    public void setVisite(ArrayList<Visita> visite) {
        this.visite = visite;
    }

    public void addVisita(Visita visite) {
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
        tipoVisite.addAll(tipoVisiteToAdd);
    }
}