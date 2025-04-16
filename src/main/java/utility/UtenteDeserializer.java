package utility;

import application.Configuratore;
import application.Fruitore;
import application.Utente;
import application.Volontario;
import com.google.gson.*;

import java.lang.reflect.Type;

class UtenteDeserializer implements JsonDeserializer<Utente> {

    /**
     * @ requires jsonObject.get("type").getAsString().equals("configuratore") ||
     * jsonObject.get("type").getAsString().equals("volontario") ||
     * jsonObject.get("type").getAsString().equals("fruitore");
     * @ ensures \result != null && (
     *         (\result instanceof Configuratore && jsonObject.get("type").getAsString().equals("configuratore")) ||
     *         (\result instanceof Volontario && jsonObject.get("type").getAsString().equals("volontario")) ||
     *         (\result instanceof Fruitore && jsonObject.get("type").getAsString().equals("fruitore"))
     *         );
     */
    @Override
    public Utente deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String userType = jsonObject.get("type").getAsString();

        if ("configuratore".equals(userType)) {
            return context.deserialize(jsonObject, Configuratore.class);
        } else if ("volontario".equals(userType)) {
            return context.deserialize(jsonObject, Volontario.class);
        } else if ("fruitore".equals(userType)) {
            return context.deserialize(jsonObject, Fruitore.class);
        }

        throw new JsonParseException("Unknown user type: " + userType);
    }
}