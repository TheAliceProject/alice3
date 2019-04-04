package org.alice.tweedle.file;

public class AliceTextureReference extends ResourceReference {
  public static final String CONTENT_TYPE = "aliceTexture";

  public AliceTextureReference() {
    super();
  }

  @Override
  public String getContentType() {
    return CONTENT_TYPE;
  }
}
