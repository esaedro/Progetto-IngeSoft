package utility;

import application.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

class UtenteSerializer implements JsonSerializer<Utente> {

    /**
     * @ requires utente instanceof Configuratore || utente instanceof Volontario || utente instanceof Fruitore;
     * @ ensures jsonObject != null;
     * @ ensures jsonObject contiene le informazioni di base dell'utente (nomeUtente, password);
     * @ ensures jsonObject contiene il campo "type" che identifica il tipo di utente;
     * @ ensures se utente instanceof Volontario, jsonObject contiene la sua disponibilità;
     * @ ensures se utente instanceof Fruitore, jsonObject contiene le sue visite e iscrizioni;
     */
    @Override
    public JsonElement serialize(Utente utente, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("nomeUtente", utente.getNomeUtente());
        jsonObject.addProperty("password", utente.getPassword());

        if (utente instanceof Configuratore) {
            jsonObject.addProperty("type", "configuratore");
        } else if (utente instanceof Volontario) {
            jsonObject.addProperty("type", "volontario");
            JsonArray jsonArraydateDisponiblita = new JsonArray();
            ((Volontario) utente).getDisponibilita().forEach(jsonArraydateDisponiblita::add);
            jsonObject.add("disponibilita", jsonArraydateDisponiblita);
        } else if (utente instanceof Fruitore) {
            jsonObject.addProperty("type", "fruitore");

            Gson gson = new Gson();

            ArrayList<Visita> visite = new ArrayList<>();
            ArrayList<Iscrizione> iscrizioni = new ArrayList<>();

            for (Map.Entry<Visita, Iscrizione> entry: ((Fruitore) utente).getIscrizioni().entrySet()) {
                visite.add(entry.getKey());
                iscrizioni.add(entry.getValue());
            }

            JsonArray jsonArrayVisite = new JsonArray();

            visite.forEach((visita)->{
                JsonElement element = gson.toJsonTree(visita);
                jsonArrayVisite.add(element);
            });

            JsonArray jsonArrayIscrizioni = new JsonArray();
            iscrizioni.forEach((iscrizione)->{
                JsonElement element = gson.toJsonTree(iscrizione);
                jsonArrayIscrizioni.add(element);
            });

            jsonObject.add("visite", jsonArrayVisite);
            jsonObject.add("iscrizioni", jsonArrayIscrizioni);
        }

        return jsonObject;
    }
}