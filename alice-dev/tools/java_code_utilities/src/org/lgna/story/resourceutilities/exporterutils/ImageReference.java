package org.lgna.story.resourceutilities.exporterutils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ImageReference extends AssetReference {

  private boolean usesAlphaTest = false;
  private boolean usesAlphaBlend = false;
  private boolean isSecondary = false;

  public ImageReference(String name, String assetReference) {
    super(name, assetReference);
  }

  public ImageReference(Element e) {
    super(e);
    if (e.hasAttribute(TextureReference.USES_ALPHA_TEST_KEY)) {
      this.usesAlphaTest = e.getAttribute(TextureReference.USES_ALPHA_TEST_KEY).equalsIgnoreCase("true");
    }
    if (e.hasAttribute(TextureReference.USES_ALPHA_BLEND_KEY)) {
      this.usesAlphaBlend = e.getAttribute(TextureReference.USES_ALPHA_BLEND_KEY).equalsIgnoreCase("true");
    }
    if (e.hasAttribute(TextureReference.IS_SECONDARY_KEY)) {
      this.usesAlphaBlend = e.getAttribute(TextureReference.IS_SECONDARY_KEY).equalsIgnoreCase("true");
    }

  }

  public boolean usesAlphaTest() {
    return usesAlphaTest;
  }

  public void setUsesAlphaTest(boolean usesAlphaTest) {
    this.usesAlphaTest = usesAlphaTest;
  }

  public boolean usesAlphaBlend() {
    return usesAlphaBlend;
  }

  public void setUsesAlphaBlend(boolean usesAlphaBlend) {
    this.usesAlphaBlend = usesAlphaBlend;
  }

  public boolean isSecondary() {
    return isSecondary;
  }

  public void setIsSecondary(boolean isSecondary) {
    this.isSecondary = isSecondary;
  }

  @Override
  protected void setAttributes(Element element) {
    super.setAttributes(element);
    if (usesAlphaTest) {
      element.setAttribute(TextureReference.USES_ALPHA_TEST_KEY, "true");
    }
    if (usesAlphaBlend) {
      element.setAttribute(TextureReference.USES_ALPHA_BLEND_KEY, "true");
    }
    if (isSecondary) {
      element.setAttribute(TextureReference.IS_SECONDARY_KEY, "true");
    }
  }

}
