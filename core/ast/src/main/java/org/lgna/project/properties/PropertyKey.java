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
package org.lgna.project.properties;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.codec.InputStreamBinaryDecoder;
import edu.cmu.cs.dennisc.codec.OutputStreamBinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.project.Project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class PropertyKey<T> {
  private static Map<UUID, PropertyKey> map = Maps.newHashMap();

  public static <T> PropertyKey<T> lookupInstance(UUID id) {
    return map.get(id);
  }

  public static void decodeIdAndValueAndPut(Project project, BinaryDecoder decoder, String version) {
    UUID id = decoder.decodeId();
    byte[] buffer = decoder.decodeByteArray();
    PropertyKey<Object> propertyKey = PropertyKey.lookupInstance(id);
    if (propertyKey != null) {
      ByteArrayInputStream bisProperty = new ByteArrayInputStream(buffer);
      BinaryDecoder bdProperty = new InputStreamBinaryDecoder(bisProperty);
      Object value = propertyKey.decodeValue(bdProperty);
      project.putValueFor(propertyKey, value);
    } else {
      Logger.todo(id, buffer);
    }
  }

  private final UUID id;
  private final String repr;

  public PropertyKey(UUID id, String repr) {
    this.id = id;
    this.repr = repr;
    map.put(id, this);
  }

  public UUID getId() {
    return this.id;
  }

  protected abstract T decodeValue(BinaryDecoder binaryDecoder);

  protected abstract void encodeValue(BinaryEncoder binaryEncoder, T value);

  public void encodeIdAndValue(Project project, BinaryEncoder encoder) {
    T value = project.getValueFor(this);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    BinaryEncoder internalEncoder = new OutputStreamBinaryEncoder(bos);
    this.encodeValue(internalEncoder, value);
    internalEncoder.flush();
    byte[] buffer = bos.toByteArray();
    encoder.encode(this.getId());
    encoder.encode(buffer);
    encoder.flush();
  }

  @Override
  public String toString() {
    return this.repr;
  }
}
