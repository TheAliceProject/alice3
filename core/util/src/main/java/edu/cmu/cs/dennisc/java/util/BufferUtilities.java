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

package edu.cmu.cs.dennisc.java.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class BufferUtilities {
  private BufferUtilities() {
    throw new AssertionError();
  }

  public static byte[] convertByteBufferToArray(ByteBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      byte[] array = new byte[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static char[] convertCharBufferToArray(CharBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      char[] array = new char[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static short[] convertShortBufferToArray(ShortBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      short[] array = new short[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static int[] convertIntBufferToArray(IntBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      int[] array = new int[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static long[] convertLongBufferToArray(LongBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      long[] array = new long[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static float[] convertFloatBufferToArray(FloatBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      float[] array = new float[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static double[] convertDoubleBufferToArray(DoubleBuffer buf) {
    if (buf.hasArray()) {
      return buf.array();
    } else {
      buf.rewind();
      double[] array = new double[buf.remaining()];
      int index = 0;
      while (buf.hasRemaining()) {
        array[index++] = buf.get();
      }
      return array;
    }
  }

  public static DoubleBuffer createDirectDoubleBuffer(double[] data) {
    if (data == null) {
      return null;
    }
    DoubleBuffer buf = ByteBuffer.allocateDirect((Double.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static DoubleBuffer copyDoubleBuffer(DoubleBuffer data) {
    if (data == null) {
      return null;
    }
    DoubleBuffer buf = createDirectDoubleBuffer(convertDoubleBufferToArray(data));
    return buf;
  }

  public static FloatBuffer createDirectFloatBuffer(float[] data) {
    if (data == null) {
      return null;
    }
    FloatBuffer buf = ByteBuffer.allocateDirect((Float.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static FloatBuffer copyFloatBuffer(FloatBuffer data) {
    if (data == null) {
      return null;
    }
    FloatBuffer buf = createDirectFloatBuffer(convertFloatBufferToArray(data));
    return buf;
  }

  public static IntBuffer createDirectIntBuffer(int[] data) {
    if (data == null) {
      return null;
    }
    IntBuffer buf = ByteBuffer.allocateDirect((Integer.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asIntBuffer();
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static IntBuffer copyIntBuffer(IntBuffer data) {
    if (data == null) {
      return null;
    }
    IntBuffer buf = createDirectIntBuffer(convertIntBufferToArray(data));
    return buf;
  }

  public static LongBuffer createDirectLongBuffer(long[] data) {
    if (data == null) {
      return null;
    }
    LongBuffer buf = ByteBuffer.allocateDirect((Long.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asLongBuffer();
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static LongBuffer copyLongBuffer(LongBuffer data) {
    if (data == null) {
      return null;
    }
    LongBuffer buf = createDirectLongBuffer(convertLongBufferToArray(data));
    return buf;
  }

  public static ByteBuffer createDirectByteBuffer(byte[] data) {
    if (data == null) {
      return null;
    }
    ByteBuffer buf = ByteBuffer.allocateDirect((Byte.SIZE / 8) * data.length).order(ByteOrder.nativeOrder());
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static ByteBuffer copyByteBuffer(ByteBuffer data) {
    if (data == null) {
      return null;
    }
    ByteBuffer buf = createDirectByteBuffer(convertByteBufferToArray(data));
    return buf;
  }

  public static CharBuffer createDirectCharBuffer(char[] data) {
    if (data == null) {
      return null;
    }
    CharBuffer buf = ByteBuffer.allocateDirect((Character.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asCharBuffer();
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static CharBuffer copyCharBuffer(CharBuffer data) {
    if (data == null) {
      return null;
    }
    CharBuffer buf = createDirectCharBuffer(convertCharBufferToArray(data));
    return buf;
  }

  public static ShortBuffer createDirectShortBuffer(short[] data) {
    if (data == null) {
      return null;
    }
    ShortBuffer buf = ByteBuffer.allocateDirect((Short.SIZE / 8) * data.length).order(ByteOrder.nativeOrder()).asShortBuffer();
    buf.clear();
    buf.put(data);
    buf.flip();
    return buf;
  }

  public static ShortBuffer copyShortBuffer(ShortBuffer data) {
    if (data == null) {
      return null;
    }
    ShortBuffer buf = createDirectShortBuffer(convertShortBufferToArray(data));
    return buf;
  }
}
