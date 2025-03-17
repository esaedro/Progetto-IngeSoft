package application;
import utility.FileManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

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
    }

    public void salvaUtenti() {
        filemanager.salva(FileManager.fileUtenti, utenti);
    }

    private void salvaStoricoVisite() {
        Iterator<TipoVisita> iteratorVisita = visite.iterator();
        ArrayList<TipoVisita> visiteDaSalvare = new ArrayList<>();
        while(iteratorVisita.hasNext()) {
            TipoVisita visita = iteratorVisita.next();
            if (((Visita) visita).getStato() == StatoVisita.EFFETTUATA) {
                visiteDaSalvare.add(visita);
                iteratorVisita.remove();
            }
        }

        if (!visiteDaSalvare.isEmpty()) {
            ArrayList<TipoVisita> storicoVisite = filemanager.carica(FileManager.fileStorico, TipoVisita.class);
            if (storicoVisite != null) {
                visiteDaSalvare.addAll(storicoVisite);
            }
            filemanager.salva(FileManager.fileStorico, visiteDaSalvare);
            visiteDaSalvare.clear();
        }
    }

    public void carica() {
        visite = filemanager.carica(FileManager.fileVisite, TipoVisita.class);
        luoghi = filemanager.carica(FileManager.fileLuoghi, Luogo.class);
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

    public ArrayList<TipoVisita> getVisite() {
        return visite;
    }

    public void setVisite(ArrayList<TipoVisita> visite) {
        this.visite = visite;
    }

    public void addVisita(TipoVisita visite) {
        this.visite.add(visite);
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

}