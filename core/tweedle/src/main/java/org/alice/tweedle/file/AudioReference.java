package org.alice.tweedle.file;

import org.lgna.common.resources.AudioResource;

import java.util.UUID;

public class AudioReference extends ResourceReference {
	private static final String AUDIO = "audio";
	public UUID uuid;
	public double duration;

	public AudioReference() {
		super();
	}

	public AudioReference( AudioResource resource ) {
		super(resource);
		uuid = resource.getId();
		duration = resource.getDuration();
	}

	@Override public String getContentType() {
		return AUDIO;
	}
}
