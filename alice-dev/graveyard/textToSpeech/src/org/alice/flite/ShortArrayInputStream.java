/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.flite;

import java.io.IOException;
import java.io.InputStream;

public class ShortArrayInputStream extends InputStream {
  private short[] data;
  private int readIndex = 0;
  private boolean closed = false;

  public ShortArrayInputStream(short[] data) {
    super();
    this.data = data;
    this.readIndex = 0;
    this.closed = false;
  }

  private int numBytes() {
    if (this.data != null) {
      return data.length * 2;
    }
    return 0;
  }

  private byte getByte(int index) {
    int shortIndex = (index / 2);
    if ((this.data != null) && (shortIndex < this.data.length)) {
      short shortVal = this.data[shortIndex];
      int shiftAmount = (1 - (index % 2)) * 8;
      byte byteVal = (byte) (shortVal >> shiftAmount);
      return byteVal;
    } else {
      return -1;
    }
  }

  @Override
  public boolean markSupported() {
    return false;
  }

  @Override
  public int available() throws IOException {
    if (this.data != null) {
      return this.numBytes() - this.readIndex;
    }
    return 0;
  }

  @Override
  public long skip(long n) throws IOException {
    if (n > 0) {
      int numLeft = this.available();
      if (n > numLeft) {
        this.readIndex = this.numBytes();
        return numLeft;
      } else {
        this.readIndex += n;
        return (int) n;
      }
    }
    return 0;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (b == null) {
      throw new NullPointerException();
    }
    if (b.length == 0) {
      return 0;
    }
    if (this.closed) {
      throw new IOException("Try to read on a closed stream.");
    }
    int bytesRead = 0;
    for (int i = off; i < (off + len); i++) {
      if ((i < b.length) && (this.available() > 0)) {
        b[i] = this.getByte(this.readIndex);
        this.readIndex++;
        bytesRead++;
      } else {
        break;
      }
    }
    return bytesRead;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return this.read(b, 0, b.length);
  }

  @Override
  public int read() throws IOException {
    if (this.closed) {
      throw new IOException("Try to read on a closed stream.");
    }
    byte toReturn = -1;
    if (readIndex < numBytes()) {
      toReturn = getByte(this.readIndex);
      this.readIndex++;
    }
    return toReturn;
  }

  @Override
  public void close() throws IOException {
    this.data = null;
    this.closed = true;
  }

}
