package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    
    private String percorsoFile;
    static private final String fileUtenti = "utenti.json";
    static private final String fileVisite = "visite.json";

    public void salva(String nomeFile, Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(percorsoFile + nomeFile)) {
            gson.toJson(object, writer);
        } catch (IOException e) {
            // TODO: gestire questa eccezione
        }
    }

    public Object carica(String nomeFile) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(percorsoFile + nomeFile)) {
            return gson.fromJson(reader, Object.class);
        } catch (IOException e) {
            // TODO: gestire anche questa eccezione
            return null;
        }
    }

    
    public FileManager(String percorsoFile) {
        this.percorsoFile = percorsoFile;
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