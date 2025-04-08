package utility;

import application.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class FileManager {
    
    private final String percorsoFile;
    static public final String fileUtenti = getFileName("fileUtenti", "utenti.json");
    static public final String fileVisite = getFileName("fileVisite", "visite.json");
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
        File directory = new File(percorsoFile);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public void salva(String nomeFile, Object object) {
        try (FileWriter writer = new FileWriter(percorsoFile + nomeFile)) {
            gson.toJson(object, writer);
        } catch (IOException ignored) {

        }
    }

    public <T> Set<T> carica (String nomeFile, Class<T> classToLoad) {
        Type arrayType = TypeToken.getParameterized(Set.class, classToLoad).getType();
        try (FileReader reader = new FileReader(percorsoFile + nomeFile)) {
            return gson.fromJson(reader, arrayType);
        } catch (IOException e) {
            return new HashSet<>();
        }
    }

    public void salvaParametriGlobali() {
        JsonObject jo = new JsonObject();
        jo.addProperty("parametro_territoriale", Luogo.getParametroTerritoriale());
        jo.addProperty("numero_massimo_iscritti", TipoVisita.getNumeroMassimoIscrittoPerFruitore());

        JsonArray jsonArraydatePrecluseFuture = new JsonArray();
        TipoVisita.getDatePrecluseFuture().forEach(jsonArraydatePrecluseFuture::add);
        jo.add("date_precluse_future", jsonArraydatePrecluseFuture);

        JsonArray jsonArraydateAttuali = new JsonArray();
        TipoVisita.getDatePrecluseAttuali().forEach(jsonArraydateAttuali::add);
        jo.add("date_precluse_attuali", jsonArraydateAttuali);

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
            JsonArray datePrecluseFuture = parametri.has("date_precluse_future")?
                    parametri.get("date_precluse_future").getAsJsonArray() : new JsonArray();
            JsonArray datePrecluseAttuali = parametri.has("date_precluse_attuali")?
                    parametri.get("date_precluse_attuali").getAsJsonArray() : new JsonArray();

            Luogo.setParametroTerritoriale(parametroTerritoriale);
            TipoVisita.setNumeroMassimoIscrittoPerFruitore(numeroMassimoIscritti);

            Set<Integer> datePrecluseFutureLette = new HashSet<>();
            datePrecluseFuture.forEach((jsonElement -> datePrecluseFutureLette.add(jsonElement.getAsInt())));
            TipoVisita.setDatePrecluseFuture(datePrecluseFutureLette);

            Set<Integer> datePrecluseAttualiLette = new HashSet<>();
            datePrecluseAttuali.forEach((jsonElement -> datePrecluseAttualiLette.add(jsonElement.getAsInt())));
            TipoVisita.setDatePrecluseAttuali(datePrecluseAttualiLette);

        } catch (IOException ignored) {}
    }
}