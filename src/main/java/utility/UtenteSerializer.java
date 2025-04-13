package utility;

import application.Configuratore;
import application.Fruitore;
import application.Utente;
import application.Volontario;
import com.google.gson.*;

import java.lang.reflect.Type;

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
            // TODO gestisci scrittura volontario
        }

        return jsonObject;
    }
}