package application;
import utility.FileManager;

public class Session {

    protected Utente[] utenti;
    protected Luogo[] luoghi;
    protected Visita[] visite;

    private Utente utenteAcceduto; //utenteCorrente?
    private FileManager filemanager;

    private void salva() {

    }

    private void carica() {

    }

    private void cambiaPassword(Utente utente) {

    }


    private void logic(Utente utente) {

    }

    public Utente[] getUtenti() {
        return utenti;
    }

    public void setUtenti(Utente[] utenti) {
        this.utenti = utenti;
    }

    public Luogo[] getLuoghi() {
        return luoghi;
    }

    public void setLuoghi(Luogo[] luoghi) {
        this.luoghi = luoghi;
    }

    public Visita[] getVisite() {
        return visite;
    }

    public void setVisite(Visita[] visite) {
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

    public Session(Utente[] utenti, Luogo[] luoghi, Visita[] visite, Utente utenteAcceduto, FileManager filemanager) {
        this.utenti = utenti;
        this.luoghi = luoghi;
        this.visite = visite;
        this.utenteAcceduto = utenteAcceduto;
        this.filemanager = filemanager;
    }


}