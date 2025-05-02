package utility;

import application.Fruitore;
import application.Iscrizione;
import application.Visita;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class FruitoreSerializer implements JsonSerializer<Fruitore> {
    @Override
    public JsonElement serialize(Fruitore src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("nomeUtente", src.getNomeUtente());
        jsonObject.addProperty("password", src.getPassword());

        jsonObject.addProperty("type", "fruitore");

        Gson gson = new Gson();

        ArrayList<Visita> visite = new ArrayList<>();
        ArrayList<Iscrizione> iscrizioni = new ArrayList<>();

        for (Map.Entry<Visita, Iscrizione> entry: src.getIscrizioni().entrySet()) {
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


        return jsonObject;
    }
}
