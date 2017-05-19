package it.polimi.ingsw.LM45.serialization;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonTypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String TYPE_KEY = "CONCRETE_CLASS";

	public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = jsonElement.getAsJsonObject();
        String className = jsonObject.get(TYPE_KEY).getAsString();
        try {
            return context.deserialize(jsonElement, Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
	}

	public JsonElement serialize(T element, Type type, JsonSerializationContext context) {
		JsonElement jsonElement = context.serialize(element, element.getClass());
        jsonElement.getAsJsonObject().addProperty(TYPE_KEY, element.getClass().getCanonicalName());
        return jsonElement;
	}

}

