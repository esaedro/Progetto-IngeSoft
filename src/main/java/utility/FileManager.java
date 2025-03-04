package utility;

public class FileManager {
    
    private String percorsoFile;
    private final String fileUtenti;
    private final String fileVisite;
    
    public void salva() {
    }

    public void carica() {
    }

    
    public FileManager(String percorsoFile, String fileUtenti, String fileVisite) {
        this.percorsoFile = percorsoFile;
        this.fileUtenti = fileUtenti;
        this.fileVisite = fileVisite;
    }

    public String getPercorsoFile() {
        return percorsoFile;
    }

    public void setPercorsoFile(String percorsoFile) {
        this.percorsoFile = percorsoFile;
    }

    public String getFileUtenti() {
        return fileUtenti;
    }

    public String getFileVisite() {
        return fileVisite;
    }

}