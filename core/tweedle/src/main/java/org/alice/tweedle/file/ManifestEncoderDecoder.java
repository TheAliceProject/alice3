package org.alice.tweedle.file;

import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class ManifestEncoderDecoder {

	private static final String TYPE_KEY = "type";
	static final String PROGRESSIVE_8601_FORMAT = "yyyy[-MM[-dd['T'HH:mm:ss[Z][z]]]]";


	public static String toJson(Manifest manifest) {
		Gson gson = new GsonBuilder().setPrettyPrinting()
									 .registerTypeAdapter(Temporal.class, new TemporalSerializer())
									 .create();
		return gson.toJson(manifest);
	}

	public static <T extends Manifest> T fromJson( String string, Class<T> manifestClass ) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(ResourceReference.class, new ResourceReferenceDeserializer())
				.registerTypeAdapter(Temporal.class, new TemporalDeserializer())
				.create();
		return gson.fromJson(string, manifestClass );
	}

	private static <O extends ResourceReference> O initializeObject(O o, JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
		for (Field field : o.getClass().getFields()) {
			field.setAccessible(true);
			if (jsonObject.has(field.getName())) {
				try {
					field.set(o,context.deserialize(jsonObject.get(field.getName()) , field.getGenericType()));
				} catch (IllegalAccessException e) {
					throw new JsonParseException("Error setting field '"+field.getName()+"'.", e);
				}
			}
		}
		return o;
	}

	private static class ResourceReferenceDeserializer implements JsonDeserializer<ResourceReference> {
		@Override
		public org.alice.tweedle.file.ResourceReference deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = (JsonObject)json;
			String typeValue = jsonObject.get(TYPE_KEY).getAsString();
			if (typeValue == null) {
				throw new JsonParseException("Cannot find field named 'type'. ResourceReferences must have the 'type' field defined.");
			} else if (typeValue.isEmpty()) {
				throw new JsonParseException("'type' value is null. ResourceReferences must have a non null 'type' value.");
			}
			switch (typeValue) {
				case TypeReference.CONTENT_TYPE : return initializeObject(new TypeReference(), jsonObject, context);
				case AudioReference.CONTENT_TYPE : return initializeObject(new AudioReference(), jsonObject, context);
				case StructureReference.CONTENT_TYPE : return initializeObject(new StructureReference(), jsonObject, context);
				case ModelReference.CONTENT_TYPE : return initializeObject(new ModelReference(), jsonObject, context);
				case ImageReference.CONTENT_TYPE : return initializeObject(new ImageReference(), jsonObject, context);
				case AliceTextureReference.CONTENT_TYPE : return initializeObject(new AliceTextureReference(), jsonObject, context);
				default : throw new JsonParseException("Unknown type value '"+typeValue+"'. Unable to construct a new ResourceReference.");
			}
		}
	}

	private static class TemporalDeserializer implements JsonDeserializer<Temporal> {
		@Override
		public Temporal deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			DateTimeFormatter parser = DateTimeFormatter.ofPattern( PROGRESSIVE_8601_FORMAT );
			return (Temporal) parser.parseBest( jsonElement.getAsString(),
												ZonedDateTime::from,
												OffsetDateTime::from,
												LocalDateTime::from,
												LocalDate::from,
												YearMonth::from,
												Year::from );
		}
	}

	private static class TemporalSerializer implements JsonSerializer<Temporal> {
		@Override
		public JsonElement serialize( Temporal temporal, Type type, JsonSerializationContext jsonSerializationContext ) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern( PROGRESSIVE_8601_FORMAT );
			return new JsonPrimitive(formatter.format(temporal));
		}
	}
}
