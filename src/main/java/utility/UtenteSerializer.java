package utility;

import application.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class UtenteSerializer implements JsonSerializer<Utente> {
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
            JsonElement element = gson.toJsonTree(((Fruitore)utente).getIscrizioni());
            jsonObject.add("iscrizioni", element);
        }

        return jsonObject;
    }
}