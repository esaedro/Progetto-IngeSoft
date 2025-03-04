package application;
import utility.FileManager;

import java.util.ArrayList;

public class Session {

    protected ArrayList<Utente> utenti;
    protected ArrayList<Luogo> luoghi;
    protected ArrayList<Visita> visite;

    private Utente utenteAcceduto; //utenteCorrente?
    private FileManager filemanager;

    public Session(ArrayList<Utente> utenti, ArrayList<Luogo> luoghi, ArrayList<Visita> visite, Utente utenteAcceduto) {
        this.utenti = utenti;
        this.luoghi = luoghi;
        this.visite = visite;
        this.utenteAcceduto = utenteAcceduto;
        this.filemanager = new FileManager("database/");
    }

    public void salva() {
        filemanager.salva(FileManager.fileVisite, visite);
        filemanager.salva(FileManager.fileLuoghi, luoghi);
    }

    public void carica() {
        luoghi = filemanager.carica(FileManager.fileLuoghi, Luogo.class);
        visite = filemanager.carica(FileManager.fileVisite, Visita.class);
        utenti = filemanager.carica(FileManager.fileUtenti, Utente.class);
    }

    public void cambiaPassword(Utente utente) {

    }

    public Boolean logic(Utente utente) {
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

    public Utente getUtenteAcceduto() {
        return utenteAcceduto;
    }

    public void setUtenteAcceduto(Utente utenteAcceduto) {
        this.utenteAcceduto = utenteAcceduto;
    }

    public FileManager getFilemanager() {
        return filemanager;
    }

    public void setFilemanager(FileManager filemanager) {
        this.filemanager = filemanager;
    }


}