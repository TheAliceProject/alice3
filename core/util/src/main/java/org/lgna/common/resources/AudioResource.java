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
package org.lgna.common.resources;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.lgna.common.Resource;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AudioResource extends Resource {
  private static Map<String, String> extensionToContentTypeMap, contentTypeToExtensionMap;
  private static final Set<String> extensions;

  static {
    AudioResource.extensionToContentTypeMap = new HashMap<>();
    AudioResource.extensionToContentTypeMap.put("au", "audio.basic");
    AudioResource.extensionToContentTypeMap.put("wav", "audio.x_wav");
    AudioResource.extensionToContentTypeMap.put("mp3", "audio.mpeg");
    extensions = Collections.unmodifiableSet(extensionToContentTypeMap.keySet());

    AudioResource.contentTypeToExtensionMap = new HashMap<>();
    for (Map.Entry<String, String> entry : AudioResource.extensionToContentTypeMap.entrySet()) {
      AudioResource.contentTypeToExtensionMap.put(entry.getValue(), entry.getKey());
    }
  }

  public static String getContentType(String path) {
    String extension = FileUtilities.getExtension(path);
    return extension != null ? AudioResource.extensionToContentTypeMap.get(extension.toLowerCase(Locale.ENGLISH)) : null;
  }

  public static String getContentType(File file) {
    return getContentType(file.getName());
  }

  public static Set<String> getFileExtensions() {
    return extensions;
  }

  public static FilenameFilter createFilenameFilter(final boolean areDirectoriesAccepted) {
    return (dir, name) -> {
      File file = new File(dir, name);
      return file.isDirectory() && areDirectoriesAccepted
          || !file.isDirectory() && getContentType(name) != null;
    };
  }

  private static final Map<UUID, AudioResource> uuidToResourceMap = new HashMap<>();

  private static AudioResource get(UUID uuid) {
    AudioResource rv = uuidToResourceMap.get(uuid);
    if (rv == null) {
      rv = new AudioResource(uuid);
      uuidToResourceMap.put(uuid, rv);
    }
    return rv;
  }

  public static AudioResource valueOf(String s) {
    return get(UUID.fromString(s));
  }

  private double duration = Double.NaN;
  private File tempFile = null;

  protected AudioResource(UUID uuid) {
    super(uuid);
  }

  public AudioResource(Class<?> cls, String resourceName, String contentType) {
    super(cls, resourceName, contentType);
    uuidToResourceMap.put(this.getId(), this);
  }

  public AudioResource(Class<?> cls, String resourceName) {
    this(cls, resourceName, getContentType(resourceName));
  }

  public AudioResource(File file, String contentType) throws IOException {
    super(file, contentType);
    uuidToResourceMap.put(this.getId(), this);
  }

  public AudioResource(File file) throws IOException {
    this(file, getContentType(file));
  }

  public double getDuration() {
    return this.duration;
  }

  public void setDuration(double duration) {
    this.duration = duration;
  }

  private static final String XML_DURATION_ATTRIBUTE = "duration";

  @Override
  public void encodeAttributes(Element xmlElement) {
    super.encodeAttributes(xmlElement);
    xmlElement.setAttribute(XML_DURATION_ATTRIBUTE, Double.toString(this.duration));
  }

  @Override
  public void decodeAttributes(Element xmlElement, byte[] data) {
    super.decodeAttributes(xmlElement, data);
    this.duration = Double.parseDouble(xmlElement.getAttribute(XML_DURATION_ATTRIBUTE));
  }

  public File getTempFile() {
    if (tempFile == null) {
      FileOutputStream fos = null;

      try {
        String extension = "." + AudioResource.contentTypeToExtensionMap.get(getContentType());

        tempFile = File.createTempFile("VACA", extension, null);
        tempFile.deleteOnExit();

        fos = new FileOutputStream(tempFile);
        fos.write(getData());
      } catch (IOException ioe) {
        System.out.println("Error creating temp file for AudioResource conversion");
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (IOException e) {
            System.out.println("Error closing temp file for AudioResource conversion");
          }
        }
      }
    }

    return tempFile;
  }
}
