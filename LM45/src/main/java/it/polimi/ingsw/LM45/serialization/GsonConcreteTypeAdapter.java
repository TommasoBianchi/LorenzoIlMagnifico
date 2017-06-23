package it.polimi.ingsw.LM45.serialization;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonConcreteTypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String TYPE_KEY = "SUBCLASS";

	@Override
	public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
		JsonElement typeKey = jsonElement.getAsJsonObject().get(TYPE_KEY);
		if(typeKey == null)
			return GSON.fromJson(jsonElement, type); 
		else {
			String subclassName = typeKey.getAsString();
			try {
				return jsonDeserializationContext.deserialize(jsonElement, Class.forName(subclassName));
			}
			catch (ClassNotFoundException e) {
	            throw new JsonParseException(e);
			}
		}		
	}

	@Override
	public JsonElement serialize(T element, Type type, JsonSerializationContext jsonSerializationContext) {
		if(element.getClass().getTypeName().equals(type.getTypeName()))
			return GSON.toJsonTree(element, type);
		else {
			JsonElement jsonElement = jsonSerializationContext.serialize(element, element.getClass());
			jsonElement.getAsJsonObject().addProperty(TYPE_KEY, element.getClass().getCanonicalName());
			return jsonElement;
		}
	}	

}
