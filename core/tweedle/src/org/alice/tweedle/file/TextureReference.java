package org.alice.tweedle.file;

public class TextureReference extends ResourceReference {
	private static final String TEXTURE = "texture";

	@Override public String getContentType() {
		return TEXTURE;
	}
}
