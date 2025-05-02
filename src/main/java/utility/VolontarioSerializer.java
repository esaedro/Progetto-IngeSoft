package utility;

import application.Volontario;
import com.google.gson.*;

import java.lang.reflect.Type;

public class VolontarioSerializer implements JsonSerializer<Volontario> {
    @Override
    public JsonElement serialize(Volontario src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("nomeUtente", src.getNomeUtente());
        jsonObject.addProperty("password", src.getPassword());

        jsonObject.addProperty("type", "volontario");
        JsonArray jsonArraydateDisponiblita = new JsonArray();
        src.getDisponibilita().forEach(jsonArraydateDisponiblita::add);
        jsonObject.add("disponibilita", jsonArraydateDisponiblita);

        return jsonObject;
    }
}
