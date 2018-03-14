package org.alice.tweedle.file;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class ManifestEncoderDecoder {

	public static String toJson(Manifest manifest) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(manifest);
	}

	public static <T extends Manifest> T fromJson( String string, Class<T> manifestClass ) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(string, manifestClass );
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
