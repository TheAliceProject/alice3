/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.nebulous;

import com.jogamp.opengl.GL;
import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy;
import edu.cmu.cs.dennisc.texture.Texture;
import org.lgna.story.resourceutilities.NebulousStorytellingResources;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author alice
 *
 */
public class NebulousTexture extends Texture {

  static {
    AdapterFactory.register(NebulousTexture.class, NebulousTextureAdapter.class);
    if (SystemUtilities.getBooleanProperty("org.alice.ide.disableDefaultNebulousLoading", false)) {
      //Don't load nebulous resources if the default loading is disabled
      //Disabling should only happen under controlled circumstances like running the model batch process
    } else {
      NebulousStorytellingResources.INSTANCE.loadSimsBundles();
    }
  }

  private final String m_textureKey;
  private boolean m_isMipMappingDesired = true;
  private boolean m_isPotentiallyAlphaBlended = false;
  private BufferedImage cachedImage;

  public native void initializeIfNecessary(Object textureKey);

  public native void setup(GL gl);

  public native void addReference();

  public native void removeReference();

  private native int getImageWidth();

  private native int getImageHeight();

  private native int getBytesPerPixel();

  private native void getImageData(byte[] imageData);

  public NebulousTexture(String textureKey) {
    m_textureKey = textureKey;
    this.initializeIfNecessary(m_textureKey);
  }

  public NebulousTexture(BinaryDecoder binaryDecoder) {
    super(binaryDecoder);
    m_textureKey = binaryDecoder.decodeString();
    this.initializeIfNecessary(m_textureKey);
  }

  public void doSetup(GL gl) {
    assert m_textureKey != null;
    this.initializeIfNecessary(m_textureKey);
    this.setup(gl);
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    assert m_textureKey != null;
    binaryEncoder.encode(m_textureKey);
  }

  public String getTextureKey() {
    return m_textureKey;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public boolean isMipMappingDesired() {
    return m_isMipMappingDesired;
  }

  public void setMipMappingDesired(boolean isMipMappingDesired) {
    if (m_isMipMappingDesired != isMipMappingDesired) {
      m_isMipMappingDesired = isMipMappingDesired;
      fireTextureChanged();
    }
  }

  @Override
  public boolean isPotentiallyAlphaBlended() {
    return m_isPotentiallyAlphaBlended;
  }

  public void setPotentiallyAlphaBlended(boolean isPotentiallyAlphaBlended) {
    if (m_isPotentiallyAlphaBlended != isPotentiallyAlphaBlended) {
      m_isPotentiallyAlphaBlended = isPotentiallyAlphaBlended;
      fireTextureChanged();
    }
  }

  @Override
  public int getWidth() {
    if (cachedImage != null) {
      return cachedImage.getWidth();
    }
    return getImageWidth();
  }

  @Override
  public int getHeight() {
    if (cachedImage != null) {
      return cachedImage.getHeight();
    }
    return getImageHeight();
  }

  public BufferedImage getBufferedImage() {
    if (cachedImage == null) {
      cacheImage();
    }
    return cachedImage;
  }

  public static BufferedImage createBufferedImageFromNebulousData(byte[] imageData, int width, int height, int bytesPerPixel) {
    if (bytesPerPixel < 3) {
      throw new RuntimeException(String.format("Unexpected bytes per pixel %d", bytesPerPixel));
    }
    final int imageType = bytesPerPixel == 3 ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage bufferedImage = new BufferedImage(width, height, imageType);

    int byteIndex = 0;
    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        //Nebulous images are packed RGB or RGBA
        int pixelValue = (imageData[byteIndex] & 0x000000FF) << 16; //Red
        pixelValue += (imageData[byteIndex + 1] & 0x000000FF) << 8; //Green
        pixelValue += (imageData[byteIndex + 2] & 0x000000FF); //Blue
        byteIndex += 3;
        if (bytesPerPixel == 4) {
          pixelValue += (imageData[byteIndex] & 0x000000FF) << 24; //Alpha
          byteIndex++;
        }
        bufferedImage.setRGB(w, h, pixelValue);
      }
    }

    return bufferedImage;
  }

  private void cacheImage() {
      final int bytesPerPixel = getBytesPerPixel();
      final int imageHeight = getHeight();
      final int imageWidth = getWidth();
      byte[] imageByteData = new byte[bytesPerPixel * imageWidth * imageHeight];
      getImageData(imageByteData);

      cachedImage = createBufferedImageFromNebulousData(imageByteData, imageWidth, imageHeight, bytesPerPixel);
  }

  @Override
  public MipMapGenerationPolicy getMipMapGenerationPolicy() {
    return MipMapGenerationPolicy.PAINT_EACH_INDIVIDUAL_LEVEL;
  }

  @Override
  public void paint(Graphics2D g, int width, int height) {
    throw new RuntimeException("NOT SUPPORTED");
  }
}
