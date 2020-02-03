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
package edu.cmu.cs.dennisc.java.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Dennis Cosgrove
 */
public class SerializationUtilities {
  public static void serialize(Serializable serializable, ObjectOutputStream oos) {
    try {
      oos.writeObject(serializable);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void serialize(Serializable serializable, File outFile) {
    try {
      FileOutputStream fos = new FileOutputStream(outFile);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(serializable);
      fos.flush();
      fos.close();
    } catch (IOException ioe) {
      throw new RuntimeException(outFile.getAbsolutePath(), ioe);
    }
  }

  public static Serializable unserialize(ObjectInputStream ois) {
    try {
      return (Serializable) ois.readObject();
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static Serializable unserialize(File inFile) {
    try {
      FileInputStream fis = new FileInputStream(inFile);
      ObjectInputStream ois = new ObjectInputStream(fis);
      Serializable serializable = (Serializable) ois.readObject();
      fis.close();
      return serializable;
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(inFile.getAbsolutePath(), cnfe);
    } catch (IOException ioe) {
      throw new RuntimeException(inFile.getAbsolutePath(), ioe);
    }
  }

  public static void serializeBufferedImage(BufferedImage bufferedImage, ObjectOutputStream oos) {
    try {
      if (bufferedImage != null) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        //        int imageType = m_bufferedImage.getType();
        //        if( imageType == java.awt.image.BufferedImage.TYPE_CUSTOM ) {
        //        }

        int[] pixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        oos.writeObject(pixels);
        oos.writeInt(width);
        oos.writeInt(height);
      } else {
        oos.writeObject(null);
      }
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static void serializeBufferedImage(BufferedImage bufferedImage, File outFile) {
    try {
      FileOutputStream fos = new FileOutputStream(outFile);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      serializeBufferedImage(bufferedImage, oos);
      fos.flush();
      fos.close();
    } catch (IOException ioe) {
      throw new RuntimeException(outFile.getAbsolutePath(), ioe);
    }
  }

  public static BufferedImage unserializeBufferedImage(ObjectInputStream ois) {
    try {
      BufferedImage bufferedImage;
      Object o = ois.readObject();
      if (o instanceof int[]) {
        int[] pixels = (int[]) o;
        int width = ois.readInt();
        int height = ois.readInt();
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
      } else {
        assert o == null;
        bufferedImage = null;
      }
      return bufferedImage;
    } catch (ClassNotFoundException cnfe) {
      throw new RuntimeException(cnfe);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public static BufferedImage unserializeBufferedImage(File inFile) {
    try {
      FileInputStream fis = new FileInputStream(inFile);
      ObjectInputStream ois = new ObjectInputStream(fis);
      BufferedImage bufferedImage = unserializeBufferedImage(ois);
      fis.close();
      return bufferedImage;
    } catch (IOException ioe) {
      throw new RuntimeException(inFile.getAbsolutePath(), ioe);
    }
  }

}
