package utility;

import application.Luogo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileManager {
    
    private String percorsoFile;
    static public final String fileUtenti = "utenti.json";
    static public final String fileVisite = "visite.json";
    static public final String fileLuoghi = "luoghi.json";

    public void salva(String nomeFile, Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(percorsoFile + nomeFile)) {
            gson.toJson(object, writer);
        } catch (IOException e) {
            // TODO: gestire questa eccezione
        }
    }

    public <T> ArrayList<T> carica (String nomeFile, Class<T> _class) {
        Gson gson = new Gson();
        Type arrayType = TypeToken.getParameterized(ArrayList.class, _class).getType();
        try (FileReader reader = new FileReader(percorsoFile + nomeFile)) {
            return gson.fromJson(reader, arrayType);
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