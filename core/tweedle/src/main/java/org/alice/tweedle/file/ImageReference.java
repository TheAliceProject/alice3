package org.alice.tweedle.file;

import java.util.UUID;

public class ImageReference extends ResourceReference {
	private static final String IMAGE = "image";
	private UUID uuid;
	private float height;
	private float width;

	@Override public String getContentType() {
		return IMAGE;
	}
}
