package utility;

import application.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileManager {
    
    private final String percorsoFile;
    static public final String fileUtenti = getFileName("fileUtenti", "utenti.json");
    static public final String fileVisite = getFileName("fileVisite", "visite.json");
    static public final String fileTipoVisite = getFileName("fileTipoVisite", "tipoVisite.json");
    static public final String fileLuoghi = getFileName("fileLuoghi", "luoghi.json");
    static public final String fileStorico = getFileName("fileStorico", "storico.json");
    static public final String fileParametriGlobali = getFileName("fileParametriGlobali", "parametriGlobali.json");

    private static String getFileName(String property, String defaultFileName) {
        return System.getProperty(property, defaultFileName);
    }

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

    public void salvaParametriGlobali() {
        JsonObject jo = new JsonObject();
        jo.addProperty("parametro_territoriale", Luogo.getParametroTerritoriale());
        jo.addProperty("numero_massimo_iscritti", TipoVisita.getNumeroMassimoIscrittoPerFruitore());

        JsonArray jsonArray = new JsonArray();
        TipoVisita.getDatePrecluse().forEach(jsonArray::add);

        jo.add("date_precluse", jsonArray);

        try (FileWriter writer = new FileWriter(percorsoFile + fileParametriGlobali)) {
            gson.toJson(jo, writer);
        } catch (IOException ignored) {}

    }

    public void caricaParametriGlobali() {
        try (FileReader writer = new FileReader(percorsoFile + fileParametriGlobali)) {
            JsonObject parametri = gson.fromJson(writer, JsonObject.class);

            String parametroTerritoriale = parametri.has("parametro_territoriale") ?
                    parametri.get("parametro_territoriale").getAsString() : null;
            int numeroMassimoIscritti = parametri.has("numero_massimo_iscritti") ?
                    parametri.get("numero_massimo_iscritti").getAsInt() : 0;
            JsonArray datePrecluse = parametri.has("date_precluse")?
                    parametri.get("date_precluse").getAsJsonArray() : new JsonArray();

            Luogo.setParametroTerritoriale(parametroTerritoriale);
            TipoVisita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);

            Set<Integer> datePrecluseLettete = new HashSet<>();

            datePrecluse.forEach((jsonElement -> {
                datePrecluseLettete.add(jsonElement.getAsInt());
            }));

            TipoVisita.setDatePrecluse(datePrecluseLettete);

        } catch (IOException ignored) {}
    }
}