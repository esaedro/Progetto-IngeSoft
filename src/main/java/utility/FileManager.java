package utility;

import application.Configuratore;
import application.Utente;
import application.Volontario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileManager {
    
    private final String percorsoFile;
    static public final String fileUtenti = "utenti.json";
    static public final String fileVisite = "visite.json";
    static public final String fileLuoghi = "luoghi.json";

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Utente.class, new UtenteSerializer())
            .registerTypeAdapter(Configuratore.class, new UtenteSerializer())
            .registerTypeAdapter(Volontario.class, new UtenteSerializer())
            .registerTypeAdapter(Utente.class, new UtenteDeserializer())
            .setPrettyPrinting()
            .create();

    public FileManager(String percorsoFile) {
        this.percorsoFile = percorsoFile;
    }

    public void salva(String nomeFile, Object object) {
        try (FileWriter writer = new FileWriter(percorsoFile + nomeFile)) {
            gson.toJson(object, writer);
        } catch (IOException ignored) {

        }
    }

    public <T> ArrayList<T> carica (String nomeFile, Class<T> classToLoad) {
        Type arrayType = TypeToken.getParameterized(ArrayList.class, classToLoad).getType();
        try (FileReader reader = new FileReader(percorsoFile + nomeFile)) {
            return gson.fromJson(reader, arrayType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}