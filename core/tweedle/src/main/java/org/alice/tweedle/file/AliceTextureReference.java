package org.alice.tweedle.file;

public class AliceTextureReference extends ResourceReference {
    private static final String ALICE_TEXTURE = "aliceTexture";

    @Override public String getContentType() {
        return ALICE_TEXTURE;
    }
}
