package application;
import utility.FileManager;

import java.util.ArrayList;

public class Session {

    protected ArrayList<Utente> utenti;
    protected ArrayList<Luogo> luoghi;
    protected ArrayList<Visita> visite;
    private FileManager filemanager;

    public Session() {
        this.filemanager = new FileManager("database/");
    }

    public Session(ArrayList<Utente> utenti, ArrayList<Luogo> luoghi, ArrayList<Visita> visite, FileManager filemanager) {
        this.utenti = utenti;
        this.luoghi = luoghi;
        this.visite = visite;
        this.filemanager = filemanager;
    }

    public void salva() {
        filemanager.salva(FileManager.fileVisite, visite);
        filemanager.salva(FileManager.fileLuoghi, luoghi);
    }
    public void salvaUtenti() {
        filemanager.salva(FileManager.fileUtenti, utenti);
    }

    public void carica() {
        visite = filemanager.carica(FileManager.fileUtenti, Visita.class);
        luoghi = filemanager.carica(FileManager.fileUtenti, Luogo.class);
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
                if (user.getNomeUtente().equals(nomeUtente)
                        && user.getPassword().equals(password)) {
                    user.session = this;
                    if (nomeUtente.startsWith("C")) {
                        utenti.remove(user);
                        user = new Configuratore(user);
                        utenti.add(user);
                    } else if (nomeUtente.startsWith("V")) {
                        utenti.remove(user);
                        user = new Volontario(user);
                        utenti.add(user);
                    }
                    return user;
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

    public ArrayList<Visita> getVisite() {
        return visite;
    }

    public void setVisite(ArrayList<Visita> visite) {
        this.visite = visite;
    }

    public FileManager getFilemanager() {
        return filemanager;
    }

    public void setFilemanager(FileManager filemanager) {
        this.filemanager = filemanager;
    }

}