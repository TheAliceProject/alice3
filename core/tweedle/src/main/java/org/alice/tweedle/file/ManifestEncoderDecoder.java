package org.alice.tweedle.file;

import com.google.gson.Gson;

public class ManifestEncoderDecoder {

	public static String toJson(Manifest manifest) {
		Gson gson = new Gson();
		return gson.toJson(manifest);
	}

	public static <T extends Manifest> T fromJson( String string, Class<T> manifestClass ) {
		Gson gson = new Gson();
		return gson.fromJson(string, manifestClass );
	}
}
