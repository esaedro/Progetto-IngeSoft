package utility;

import application.Configuratore;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfiguratoreSerializer implements JsonSerializer<Configuratore> {

    @Override
    public JsonElement serialize(Configuratore src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("nomeUtente", src.getNomeUtente());
        jsonObject.addProperty("password", src.getPassword());

        jsonObject.addProperty("type", "configuratore");

        return jsonObject;
    }
}
