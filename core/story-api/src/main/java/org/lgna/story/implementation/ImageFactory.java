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
package org.lgna.story.implementation;

import org.lgna.common.event.ResourceContentEvent;
import org.lgna.common.event.ResourceContentListener;
import org.lgna.common.resources.ImageResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public final class ImageFactory {
  private static Map<ImageResource, BufferedImage> resourceToBufferedImageMap = new HashMap<>();

  private static ResourceContentListener resourceContentListener = new ResourceContentListener() {
    @Override
    public void contentChanging(ResourceContentEvent e) {
    }

    @Override
    public void contentChanged(ResourceContentEvent e) {
      ImageFactory.forget((ImageResource) e.getTypedSource());
    }
  };

  private ImageFactory() {
  }

  private static void forget(ImageResource imageResource) {
    ImageFactory.resourceToBufferedImageMap.remove(imageResource);
    imageResource.removeContentListener(ImageFactory.resourceContentListener);
  }

  public static BufferedImage getBufferedImage(ImageResource imageResource) {
    assert imageResource != null;
    BufferedImage cachedImage = ImageFactory.resourceToBufferedImageMap.get(imageResource);
    if (cachedImage != null) {
      return cachedImage;
    }
    try {
      BufferedImage baseImage = ImageIO.read(new ByteArrayInputStream(imageResource.getData()));
      if (baseImage != null) {
        BufferedImage image = stretchSourceImage(baseImage);
        imageResource.setWidth(image.getWidth());
        imageResource.setHeight(image.getHeight());
        imageResource.addContentListener(ImageFactory.resourceContentListener);
        ImageFactory.resourceToBufferedImageMap.put(imageResource, image);
        return image;
      }
    } catch (IOException ioe) {
      //todo: return warning texture
    }
    return null;
  }

  private static BufferedImage stretchSourceImage(BufferedImage baseImage) {
    final int srcHeight = baseImage.getHeight();
    final int srcWidth = baseImage.getWidth();
    if (isPowerOfTwo(srcHeight) && isPowerOfTwo(srcWidth)) {
      return baseImage;
    }

    final int destHeight = nextPowerOfTwo(srcHeight);
    final int destWidth = nextPowerOfTwo(srcWidth);
    int[] pixel = new int[baseImage.getColorModel().getPixelSize() / 8];
    BufferedImage destImage = new BufferedImage(destWidth, destHeight, baseImage.getType());
    final WritableRaster srcRaster = baseImage.getRaster();
    final WritableRaster destRaster = destImage.getRaster();

    for (int destRow = 0; destRow < destHeight; destRow++) {
      int srcRow = destRow * srcHeight / destHeight;
      for (int destCol = 0; destCol < destWidth; destCol++) {
        int srcCol = destCol * srcWidth / destWidth;
        srcRaster.getPixel(srcCol, srcRow, pixel);
        destRaster.setPixel(destCol, destRow, pixel);
      }
    }
    return destImage;
  }

  private static int nextPowerOfTwo(int n) {
    if (isPowerOfTwo(n)) {
      return n;
    }
    double power =  (Math.ceil((Math.log(n) / Math.log(2))));
    return (int) Math.round(Math.pow(2.0, power));
  }

  // In binary, 2^n has a single bit set (e.g. 10000)
  // Subtracting 1 flips all the bits iff it starts as 2^n
  // 1000… - 1 == 0111…
  // The & then produces 0 only for 2^n
  private static boolean isPowerOfTwo(int n) {
    return n != 0 && ((n & (n - 1)) == 0);
  }

  public static ImageResource createImageResource(BufferedImage image, String fileName) throws IOException {
    String contentType = ImageResource.getContentType(fileName);
    if (contentType != null) {
      ImageResource rv = new ImageResource(image, fileName, contentType);
      ImageFactory.resourceToBufferedImageMap.put(rv, image);
      return rv;
    } else {
      throw new RuntimeException("content type not found for " + fileName);
    }
  }

  public static ImageResource createImageResource(File file) throws IOException {
    String contentType = ImageResource.getContentType(file);
    if (contentType != null) {
      ImageResource rv = new ImageResource(file, contentType);

      //update width and height details
      if (null == getBufferedImage(rv)) {
        throw new IOException("content not found for " + file);
      }

      return rv;
    } else {
      throw new RuntimeException("content type not found for " + file);
    }
  }

}
