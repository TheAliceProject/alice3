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
import edu.cmu.cs.dennisc.java.awt.Painter;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.print.PrintUtilities;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.media.jai.PlanarImage;
import javax.swing.filechooser.FileFilter;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

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

  private static final String[] s_codecNames = {PNG_CODEC_NAME, JPEG_CODEC_NAME, BMP_CODEC_NAME, GIF_CODEC_NAME, TIFF_CODEC_NAME, TGA_CODEC_NAME};
  private static final Map<String, String[]> s_codecNameToExtensionsMap;
  private static final Map<String, String> s_extensionToCodecNameMap;

  static {
    String[] pngExtensions = {"png"};
    String[] jpegExtensions = {"jpeg", "jpg"};
    String[] bmpExtensions = {"bmp"};
    String[] gifExtensions = {"gif"};
    String[] tiffExtensions = {"tiff", "tif"};
    String[] tgaExtensions = {"tga"};
    s_codecNameToExtensionsMap = Maps.newHashMap();
    s_codecNameToExtensionsMap.put(PNG_CODEC_NAME, pngExtensions);
    s_codecNameToExtensionsMap.put(JPEG_CODEC_NAME, jpegExtensions);
    s_codecNameToExtensionsMap.put(BMP_CODEC_NAME, bmpExtensions);
    s_codecNameToExtensionsMap.put(GIF_CODEC_NAME, gifExtensions);
    s_codecNameToExtensionsMap.put(TIFF_CODEC_NAME, tiffExtensions);
    s_codecNameToExtensionsMap.put(TGA_CODEC_NAME, tgaExtensions);

    s_extensionToCodecNameMap = Maps.newHashMap();
    for (String key : s_codecNameToExtensionsMap.keySet()) {
      String[] value = s_codecNameToExtensionsMap.get(key);
      for (String element : value) {
        s_extensionToCodecNameMap.put(element, key);
      }
    }
  }

  public static String[] accessCodecNames() {
    return s_codecNames;
  }

  private static FileFilter s_fileFilter = new FileFilter() {
    @Override
    public boolean accept(File file) {
      return file.isDirectory() || isAcceptable(file);
    }

    @Override
    public String getDescription() {
      StringBuffer sb = new StringBuffer();
      sb.append("Image (");
      for (String key : s_codecNameToExtensionsMap.keySet()) {
        String[] value = s_codecNameToExtensionsMap.get(key);
        for (String element : value) {
          sb.append("*.");
          sb.append(element);
          sb.append(";");
        }
      }
      sb.append(")");
      return sb.toString();
    }
  };

  public static FileFilter accessFileFilter() {
    return s_fileFilter;
  }

  public static boolean isAcceptable(String path) {
    String codecName = getCodecNameForExtension(FileUtilities.getExtension(path));
    return codecName != null;
  }

  public static boolean isAcceptable(File file) {
    return isAcceptable(file.getName());
  }

  public static String[] getExtensionsForCodecName(String codecName) {
    return s_codecNameToExtensionsMap.get(codecName);
  }

  public static String getCodecNameForExtension(String extension) {
    if (extension != null) {
      return s_extensionToCodecNameMap.get(extension.toLowerCase(Locale.ENGLISH));
    } else {
      return null;
    }
  }

  public static BufferedImage read(String path) throws IOException {
    return read(new File(path));
  }

  public static BufferedImage read(String path, ImageReadParam imageReadParam) throws IOException {
    return read(new File(path), imageReadParam);
  }

  public static BufferedImage read(File file) throws IOException {
    return read(file, null);
  }

  public static BufferedImage read(File file, ImageReadParam imageReadParam) throws IOException {
    String extension = FileUtilities.getExtension(file);
    String codecName = getCodecNameForExtension(extension);
    if (codecName != null) {
      try (FileInputStream fis = new FileInputStream(file)) {
        return read(codecName, fis, imageReadParam);
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
    return read(url, null);
  }

  public static BufferedImage read(URL url, ImageReadParam imageReadParam) throws IOException {
    //todo: net.URLUtilities?
    String extension = FileUtilities.getExtension(url.getPath());
    String codecName = getCodecNameForExtension(extension);
    if (codecName != null) {
      BufferedImage rv;
      InputStream is = url.openStream();
      try {
        rv = read(codecName, is, imageReadParam);
      } catch (NoClassDefFoundError ncdfe) {
        Logger.errln(url);
        rv = null;
      } finally {
        is.close();
      }
      return rv;
    } else {
      throw new RuntimeException("Could not find codec for extension: " + extension);
    }
  }

  public static BufferedImage read(String codecName, InputStream inputStream) throws IOException {
    return read(codecName, inputStream, null);
  }

  public static BufferedImage read(String codecName, InputStream inputStream, ImageReadParam imageReadParam) throws IOException {
    BufferedInputStream bufferedInputStream;
    if (inputStream instanceof BufferedInputStream) {
      bufferedInputStream = (BufferedInputStream) inputStream;
    } else {
      bufferedInputStream = new BufferedInputStream(inputStream);
    }
    switch (codecName) {
    case TGA_CODEC_NAME:
      return TgaUtilities.readTGA(bufferedInputStream);
    case TIFF_CODEC_NAME:
      return readTIFF(bufferedInputStream);
    default:
      return ImageIO.read(bufferedInputStream);
    }
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

  public static void write(String path, Image image) throws IOException {
    write(path, image, null);
  }

  public static void write(String path, Image image, ImageReadParam imageWriteParam) throws IOException {
    write(new File(path), image, imageWriteParam);
  }

  public static void write(File file, Image image) throws IOException {
    write(file, image, null);
  }

  public static void write(File file, Image image, ImageReadParam imageWriteParam) throws IOException {
    FileUtilities.createParentDirectoriesIfNecessary(file);
    String extension = FileUtilities.getExtension(file);
    String codecName = getCodecNameForExtension(extension);
    if (codecName != null) {
      FileOutputStream fos = new FileOutputStream(file);
      try {
        write(codecName, fos, image, imageWriteParam);
      } finally {
        fos.close();
      }
    } else {
      throw new RuntimeException("Could not find codec for extension: " + extension);
    }
  }

  public static void write(String codecName, OutputStream outputStream, Image image) throws IOException {
    write(codecName, outputStream, image, null);
  }

  public static void write(String codecName, OutputStream outputStream, Image image, ImageReadParam imageWriteParam) throws IOException {
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

      if (bufferedImageBGR != null) {
        // pass
      } else {
        Image originalImage = image;
        bufferedImageBGR = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = image.getGraphics();
        g.drawImage(originalImage, 0, 0, accessImageObserver());
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

    // if( imageEncodeParam == null ) {
    // if( codecName.equals( PNG_CODEC_NAME ) ) {
    // imageEncodeParam =
    // edu.cmu.cs.dennisc.image.codec.PNGEncodeParam.getDefaultEncodeParam(
    // renderedImage );
    // }
    // }
    // java.io.BufferedOutputStream bufferedOutputStream;
    // if( outputStream instanceof java.io.BufferedOutputStream ) {
    // bufferedOutputStream = (java.io.BufferedOutputStream)outputStream;
    // } else {
    // bufferedOutputStream = new java.io.BufferedOutputStream( outputStream
    // );
    // }
    //
    // edu.cmu.cs.dennisc.image.codec.ImageEncoder imageEncoder =
    // edu.cmu.cs.dennisc.image.codec.ImageCodec.createImageEncoder(
    // codecName, bufferedOutputStream, imageEncodeParam );
    // try {
    // imageEncoder.encode( renderedImage );
    // bufferedOutputStream.flush();
    // } catch( java.io.IOException ioe ) {
    // throw new RuntimeException( ioe );
    // }
  }

  public static byte[] writeToByteArray(String codecName, Image image, ImageReadParam imageWriteParam) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    write(codecName, baos, image, imageWriteParam);
    return baos.toByteArray();
  }

  public static byte[] writeToByteArray(String codecName, Image image) throws IOException {
    return writeToByteArray(codecName, image, null);
  }

  private static MediaTracker s_mediaTracker = new MediaTracker(new Panel());

  private static ImageObserver s_imageObserver = new ImageObserver() {
    @Override
    public boolean imageUpdate(Image image, int infoflags, int x, int y, int width, int height) {
      return true;
    }
  };

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
    // int type;
    // if( isAlphaBlended ) {
    // type = java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
    // } else {
    // type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
    // }
    BufferedImage bi = new BufferedImage(width, height, imageType);
    Graphics g = bi.getGraphics();
    g.drawImage(image, 0, 0, accessImageObserver());
    g.dispose();
    return bi;
  }

  public static BufferedImage createAlphaMaskedImage(Image image, Painter<Void> painter, float alpha) {
    int width = getWidth(image);
    int height = getHeight(image);
    BufferedImage rv = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g2 = rv.createGraphics();
    g2.drawImage(image, 0, 0, null);

    BufferedImage alphaImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D ag2 = alphaImage.createGraphics();
    painter.paint(ag2, null, width, height);
    ag2.dispose();

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN, alpha));
    g2.drawImage(alphaImage, 0, 0, null);
    g2.dispose();
    return rv;
  }

  public static BufferedImage createAlphaMaskedImage(Image image, Painter<Void> painter) {
    return createAlphaMaskedImage(image, painter, 1.0f);
  }
}
