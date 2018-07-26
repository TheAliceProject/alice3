package org.alice.tweedle.file;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class ManifestEncoderDecoder {

	private static final String JAVA_CLASS_KEY = "javaClass";

	public static String toJson(Manifest manifest) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(ResourceReference.class, new SubClassSerializer<ResourceReference>())
				.create();
		return gson.toJson(manifest);
	}

	public static <T extends Manifest> T fromJson( String string, Class<T> manifestClass ) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(ResourceReference.class, new SubClassDeserializer<ResourceReference>())
				.create();
		return gson.fromJson(string, manifestClass );
	}

	private static class SubClassDeserializer<T> implements JsonDeserializer<T> {
		@Override
		public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = (JsonObject)json;
			String javaClassName = jsonObject.get(JAVA_CLASS_KEY).getAsString();
			if (javaClassName == null || javaClassName.isEmpty())
				throw new JsonParseException("Cannot find javaClass field. ResourceReference is abstract and must be properly serialized with the javaClass field.");
			try {
				Class<?> javaClass = Class.forName(javaClassName);
				Object o  = javaClass.getConstructor().newInstance();
				for (Field field : o.getClass().getFields()) {
					field.setAccessible(true);
					if (jsonObject.has(field.getName())) {
						field.set(o,context.deserialize(jsonObject.get(field.getName()) , field.getGenericType()));
					}
				}
				return (T) o;
			} catch (ClassNotFoundException e) {
				throw new JsonParseException( "Cannot find class with name "+javaClassName+".", e);
			} catch (IllegalAccessException e){
				throw new JsonParseException(e);
			} catch (NoSuchMethodException | InstantiationException | InvocationTargetException e) {
				throw new JsonParseException("Cannot deserialize object of class "+javaClassName+". Unable to create a new instance invoking empty constructor.", e);
			}
		}
	}

	private static class SubClassSerializer<T> implements JsonSerializer<T>{

		@Override
		public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) throws JsonParseException {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(JAVA_CLASS_KEY, src.getClass().getCanonicalName());

			for (Field field : src.getClass().getFields()) {
				if (Modifier.isPublic(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
					field.setAccessible(true);
					try {
						if (field.get(src) != null)
							jsonObject.add(field.getName(), context.serialize(field.get(src)));
					} catch (IllegalAccessException e) {
						throw new JsonParseException("Error adding " + field + " to JSON", e);
					}
				}
			}

			return jsonObject;
		}
	}

}
