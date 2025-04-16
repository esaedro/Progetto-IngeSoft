package utility;

import application.Fruitore;
import application.Iscrizione;
import application.Visita;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.*;

public class FruitoreDeserializer implements JsonDeserializer<Fruitore> {

    /**
     * @ensures \result != null
     * @ensures \result.getNomeUtente() != null
     * @ensures \result.getPassword() != null
     * @ensures \result.getMappaIscrizioni() != null
     */
    @Override
    public Fruitore deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        JsonArray jsonArrayVisite = jsonObject.getAsJsonArray("visite");
        List<Visita> visite = new ArrayList<>();
        for (JsonElement visitaElem : jsonArrayVisite) {
            Visita visita = context.deserialize(visitaElem, Visita.class);
            visite.add(visita);
        }

        JsonArray jsonArrayIscrizioni = jsonObject.getAsJsonArray("iscrizioni");
        List<Iscrizione> iscrizioni = new ArrayList<>();
        for (JsonElement iscrizioneElem : jsonArrayIscrizioni) {
            Iscrizione iscrizione = context.deserialize(iscrizioneElem, Iscrizione.class);
            iscrizioni.add(iscrizione);
        }

        // Ricostruisci la mappa visita → iscrizione
        Map<Visita, Iscrizione> mappaIscrizioni = new LinkedHashMap<>();
        for (int i = 0; i < visite.size(); i++) {
            mappaIscrizioni.put(visite.get(i), iscrizioni.get(i));
        }

        return new Fruitore(jsonObject.get("nomeUtente").getAsString(),
                jsonObject.get("password").getAsString(), mappaIscrizioni);
    }
}
