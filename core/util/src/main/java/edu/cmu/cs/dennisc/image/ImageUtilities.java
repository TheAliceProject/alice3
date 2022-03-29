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

package edu.cmu.cs.dennisc.image;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.print.PrintUtilities;

import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageCodec;

/**
 * @author Dennis Cosgrove
 */
public class ImageUtilities {
  public static final String PNG_CODEC_NAME = "png";
  public static final String JPEG_CODEC_NAME = "jpeg";
  public static final String BMP_CODEC_NAME = "bmp";
  public static final String GIF_CODEC_NAME = "gif";
  public static final String TIFF_CODEC_NAME = "tiff";
  public static final String TGA_CODEC_NAME = "tga";
  public static final String WEBP_CODEC_NAME = "webp";

  private static final Map<String, String> s_extensionToCodecNameMap;
  private static final Set<String> extensions;

  static {
    s_extensionToCodecNameMap = Maps.newHashMap();
    s_extensionToCodecNameMap.put("png", PNG_CODEC_NAME);
    s_extensionToCodecNameMap.put("jpeg", JPEG_CODEC_NAME);
    s_extensionToCodecNameMap.put("jpg", JPEG_CODEC_NAME);
    s_extensionToCodecNameMap.put("bmp", BMP_CODEC_NAME);
    s_extensionToCodecNameMap.put("gif", GIF_CODEC_NAME);
    s_extensionToCodecNameMap.put("tif", TIFF_CODEC_NAME);
    s_extensionToCodecNameMap.put("tiff", TIFF_CODEC_NAME);
    s_extensionToCodecNameMap.put("tga", TGA_CODEC_NAME);
    s_extensionToCodecNameMap.put("webp", WEBP_CODEC_NAME);
    extensions = Collections.unmodifiableSet(s_extensionToCodecNameMap.keySet());
  }

  public static String getContentType(String extension) {
    String codec = getCodecNameForExtension(extension);
    return codec != null ? "image/" + codec : null;
  }

  public static boolean isAcceptable(String path) {
    return getCodecNameForExtension(FileUtilities.getExtension(path)) != null;
  }

  public static boolean isAcceptable(File file) {
    return isAcceptable(file.getName());
  }

  public static String getCodecNameForExtension(String extension) {
    return extension != null ? s_extensionToCodecNameMap.get(extension.toLowerCase(Locale.ENGLISH)) : null;
  }

  public static Set<String> getFileExtensions() {
    return extensions;
  }

  public static BufferedImage read(String path) throws IOException {
    return read(new File(path));
  }

  public static BufferedImage read(File file) throws IOException {
    String extension = FileUtilities.getExtension(file);
    String codecName = getCodecNameForExtension(extension);
    if (codecName != null) {
      try (FileInputStream fis = new FileInputStream(file)) {
        return read(codecName, fis);
      } catch (RuntimeException re) {
        PrintUtilities.println(FileUtilities.getCanonicalPathIfPossible(file));
        PrintUtilities.accessPrintStream().flush();
        throw re;
      }
    } else {
      throw new RuntimeException("Could not find codec for extension: " + extension);
    }
  }

  public static BufferedImage read(URL url) throws IOException {
    //todo: net.URLUtilities?
    String extension = FileUtilities.getExtension(url.getPath());
    String codecName = getCodecNameForExtension(extension);
    if (codecName != null) {
      BufferedImage rv;
      try (InputStream is = url.openStream()) {
        rv = read(codecName, is);
      } catch (NoClassDefFoundError ncdfe) {
        Logger.errln(url);
        rv = null;
      }
      return rv;
    } else {
      throw new RuntimeException("Could not find codec for extension: " + extension);
    }
  }

  public static BufferedImage read(String codecName, InputStream inputStream) throws IOException {
    BufferedInputStream bufferedInputStream;
    if (inputStream instanceof BufferedInputStream) {
      bufferedInputStream = (BufferedInputStream) inputStream;
    } else {
      bufferedInputStream = new BufferedInputStream(inputStream);
    }
    return switch (codecName) {
      case TGA_CODEC_NAME -> TgaUtilities.readTGA(bufferedInputStream);
      case TIFF_CODEC_NAME -> readTIFF(bufferedInputStream);
      default -> ImageIO.read(bufferedInputStream);
    };
  }

  private static BufferedImage readTIFF(BufferedInputStream inputStream) throws IOException {
    byte[] data = readBufferIntoArray(inputStream);
    SeekableStream seekableStream = new ByteArraySeekableStream(data);
    String[] names = ImageCodec.getDecoderNames(seekableStream);
    ImageDecoder dec = ImageCodec.createImageDecoder(names[0], seekableStream, null);
    RenderedImage im = dec.decodeAsRenderedImage();
    return PlanarImage.wrapRenderedImage(im).getAsBufferedImage();
  }

  private static byte[] readBufferIntoArray(BufferedInputStream stream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int nRead;
    byte[] data = new byte[1024];
    while ((nRead = stream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    buffer.flush();
    return buffer.toByteArray();
  }

  public static BufferedImage stretchToPowersOfTwo(BufferedImage baseImage) {
    final int srcHeight = baseImage.getHeight();
    final int srcWidth = baseImage.getWidth();
    if (isPowerOfTwo(srcHeight) && isPowerOfTwo(srcWidth)) {
      return baseImage;
    }

    final int destHeight = nextPowerOfTwo(srcHeight);
    final int destWidth = nextPowerOfTwo(srcWidth);
    ColorModel sourceCM = baseImage.getColorModel();
    int pixelByteSize = sourceCM.getPixelSize() / 8;
    pixelByteSize = pixelByteSize == 0 ? 1 : pixelByteSize;
    int[] pixel = new int[pixelByteSize];
    final WritableRaster srcRaster = baseImage.getRaster();
    WritableRaster destRaster = sourceCM.createCompatibleWritableRaster(destWidth, destHeight);

    for (int destRow = 0; destRow < destHeight; destRow++) {
      int srcRow = destRow * srcHeight / destHeight;
      for (int destCol = 0; destCol < destWidth; destCol++) {
        int srcCol = destCol * srcWidth / destWidth;
        srcRaster.getPixel(srcCol, srcRow, pixel);
        destRaster.setPixel(destCol, destRow, pixel);
      }
    }
    return new BufferedImage(sourceCM, destRaster, sourceCM.isAlphaPremultiplied(), null);
  }

  private static int nextPowerOfTwo(int n) {
    if (isPowerOfTwo(n)) {
      return n;
    }
    double power =  Math.ceil(Math.log(n) / Math.log(2));
    return (int) Math.round(Math.pow(2.0, power));
  }

  // In binary, 2^n has a single bit set (e.g. 10000)
  // Subtracting 1 flips all the bits iff it starts as 2^n
  // 1000… - 1 == 0111…
  // The & then produces 0 only for 2^n
  private static boolean isPowerOfTwo(int n) {
    return n != 0 && ((n & (n - 1)) == 0);
  }

  public static void write(String path, Image image) throws IOException {
    write(new File(path), image);
  }

  public static void write(File file, Image image) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    String extension = FileUtilities.getExtension(file);
    String codecName = getCodecNameForExtension(extension);
    if (codecName != null) {
      try (FileOutputStream fos = new FileOutputStream(file)) {
        write(codecName, fos, image);
      }
    } else {
      throw new RuntimeException("Could not find codec for extension: " + extension);
    }
  }

  public static void write(String codecName, OutputStream outputStream, Image image) throws IOException {
    int width = ImageUtilities.getWidth(image);
    int height = ImageUtilities.getHeight(image);

    RenderedImage renderedImage;

    if (codecName.equals(JPEG_CODEC_NAME)) {
      BufferedImage bufferedImageBGR = null;
      if (image instanceof BufferedImage) {
        BufferedImage bufferedImage = (BufferedImage) image;
        if (bufferedImage.getType() == BufferedImage.TYPE_3BYTE_BGR) {
          bufferedImageBGR = bufferedImage;
        }
      }

      if (bufferedImageBGR == null) {
        bufferedImageBGR = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.getGraphics();
        g.drawImage(image, 0, 0, accessImageObserver());
        // todo: investigate - does dispose ensure the image is finished
        // drawing?
        g.dispose();
      }

      image = bufferedImageBGR;
    }
    if (image instanceof RenderedImage) {
      renderedImage = (RenderedImage) image;
    } else {
      int[] pixels = ImageUtilities.getPixels(image, width, height);
      BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
      renderedImage = bufferedImage;
    }
    //try {
    ImageIO.write(renderedImage, codecName, outputStream);
    outputStream.flush();
    //} catch( IndexOutOfBoundsException ioobe ) {
    //todo: throw new IoException???
    //}
  }

  public static byte[] writeToByteArray(String codecName, Image image) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    write(codecName, baos, image);
    return baos.toByteArray();
  }

  private static final MediaTracker s_mediaTracker = new MediaTracker(new Panel());

  private static final ImageObserver s_imageObserver = (image, infoflags, x, y, width, height) -> true;

  public static ImageObserver accessImageObserver() {
    return s_imageObserver;
  }

  private static void waitForImage(Image image) {
    s_mediaTracker.addImage(image, 0);
    try {
      s_mediaTracker.waitForID(0);
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    } finally {
      s_mediaTracker.removeImage(image);
    }
  }

  public static int getWidth(Image image) {
    waitForImage(image);
    return image.getWidth(s_imageObserver);
  }

  public static int getHeight(Image image) {
    waitForImage(image);
    return image.getHeight(s_imageObserver);
  }

  public static int[] getPixels(Image image, int width, int height) {
    int[] pixels = new int[width * height];
    PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
    try {
      pg.grabPixels();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
    if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
      throw new RuntimeException("image fetch aborted or errored");
    }
    return pixels;
  }

  public static BufferedImage createBufferedImage(Image image, int imageType) {
    int width = getWidth(image);
    int height = getHeight(image);
    BufferedImage bi = new BufferedImage(width, height, imageType);
    Graphics g = bi.getGraphics();
    g.drawImage(image, 0, 0, accessImageObserver());
    g.dispose();
    return bi;
  }
}
