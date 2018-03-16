package org.alice.tweedle.file;

import java.util.UUID;

public class AudioReference extends ResourceReference {
	private static final String AUDIO = "audio";
	private UUID uuid;
	private float duration;

	@Override public String getContentType() {
		return AUDIO;
	}
}
