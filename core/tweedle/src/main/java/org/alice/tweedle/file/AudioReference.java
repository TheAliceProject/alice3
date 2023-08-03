package org.alice.tweedle.file;

import org.lgna.common.resources.AudioResource;

import java.util.UUID;

public class AudioReference extends ResourceReference {
  public static final String CONTENT_TYPE = "audio";
  public UUID uuid;
  public double duration;

  public AudioReference() {
    super();
  }

  public AudioReference(AudioResource resource) {
    super(resource);
    uuid = resource.getId();
    duration = resource.getDuration();
    if (Double.isNaN(duration)) {
      duration = 0;
    }
  }

  @Override
  public String getContentType() {
    return CONTENT_TYPE;
  }
}
