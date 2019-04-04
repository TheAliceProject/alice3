package org.alice.tweedle.file;

import org.lgna.common.resources.ImageResource;

import java.util.UUID;

public class ImageReference extends ResourceReference {
  public static final String CONTENT_TYPE = "image";
  public UUID uuid;
  public float height;
  public float width;

  public ImageReference() {
    super();
  }

  public ImageReference(String id, String fileName, String format, int width, int height) {
    super(id, fileName, format);
    uuid = UUID.randomUUID();
    this.width = width;
    this.height = height;
  }

  public ImageReference(ImageResource resource) {
    super(resource);
    uuid = resource.getId();
    height = resource.getHeight();
    width = resource.getWidth();
  }

  @Override
  public String getContentType() {
    return CONTENT_TYPE;
  }
}
