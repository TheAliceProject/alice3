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
package edu.cmu.cs.dennisc.java.lang;

import edu.cmu.cs.dennisc.app.ApplicationRoot;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.xml.XMLUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dennis Cosgrove
 */
public class SystemUtilities {
  private SystemUtilities() {
    throw new AssertionError();
  }

  public static boolean isPropertyTrue(String propertyName) {
    return "true".equals(System.getProperty(propertyName));
  }

  public static boolean isPropertyFalse(String propertyName) {
    return "false".equals(System.getProperty(propertyName));
  }

  public static boolean getBooleanProperty(String propertyName, boolean defaultValue) {
    String textValue = System.getProperty(propertyName);
    if (textValue != null) {
      return Boolean.parseBoolean(textValue);
    } else {
      return defaultValue;
    }
  }

  private static ByteArrayOutputStream getPropertiesAsXMLByteArrayOutputStream() {
    List<SystemProperty> properties = getSortedPropertyList();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Document xmlDocument = XMLUtilities.createDocument();
    Element xmlRootElement = xmlDocument.createElement("systemProperties");
    xmlDocument.appendChild(xmlRootElement);
    for (SystemProperty property : properties) {
      Element xmlProperty = xmlDocument.createElement("property");
      xmlProperty.setAttribute("key", property.getKey());
      xmlProperty.appendChild(xmlDocument.createTextNode(property.getValue()));
      xmlRootElement.appendChild(xmlProperty);
    }
    XMLUtilities.write(xmlDocument, baos);
    try {
      baos.flush();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    return baos;
  }

  public static byte[] getPropertiesAsXMLByteArray() {
    return getPropertiesAsXMLByteArrayOutputStream().toByteArray();
  }

  public static String getPropertiesAsXMLString() {
    return getPropertiesAsXMLByteArrayOutputStream().toString();
  }

  public static List<SystemProperty> getPropertyList() {
    List<SystemProperty> rv = Lists.newLinkedList();
    Properties systemProperties = System.getProperties();
    for (Map.Entry<Object, Object> entry : systemProperties.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();
      if ((key instanceof String) && (value instanceof String)) {
        rv.add(new SystemProperty((String) key, (String) value));
      } else {
        Logger.severe(key, value);
      }
    }
    return rv;
  }

  public static List<SystemProperty> getSortedPropertyList() {
    List<SystemProperty> rv = getPropertyList();
    Collections.sort(rv);
    return rv;
  }

  private enum Platform {
    WINDOWS, OSX, LINUX
  }

  private static Platform platform;

  private static final String architecture = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

  static {
    String lowercaseOSName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
    if (lowercaseOSName.contains("windows")) {
      platform = Platform.WINDOWS;
    } else if (lowercaseOSName.startsWith("mac os x")) {
      platform = Platform.OSX;
    } else if (lowercaseOSName.startsWith("linux")) {
      platform = Platform.LINUX;
    } else {
      //todo
      platform = null;
    }
  }

  public static boolean isLinux() {
    return SystemUtilities.platform == Platform.LINUX;
  }

  public static boolean isArmArchitecture() {
    return architecture.contains("arm");
  }

  public static boolean isMac() {
    return SystemUtilities.platform == Platform.OSX;
  }

  public static boolean isWindows() {
    return SystemUtilities.platform == Platform.WINDOWS;
  }

  public static Integer getBitCount() {
    String bitCountText = System.getProperty("sun.arch.data.model");
    if (bitCountText != null) {
      try {
        return Integer.parseInt(bitCountText);
      } catch (NumberFormatException nfe) {
        return null;
      }
    } else {
      return null;
    }
  }

  public static boolean is64Bit() {
    Integer bitCount = getBitCount();
    return (bitCount != null) && (bitCount == 64);
  }

  public static boolean is32Bit() {
    Integer bitCount = getBitCount();
    return (bitCount != null) && (bitCount == 32);
  }

  public static double getJavaVersionAsDouble() {
    try {
      String javaVersionText = System.getProperty("java.version");
      int i = javaVersionText.indexOf('.');
      if (i > 0) {
        return Double.parseDouble(javaVersionText.substring(0, i + 2));
      } else {
        throw new RuntimeException();
      }
    } catch (Throwable t) {
      return Double.NaN;
    }
  }

  public static void loadLibrary(String libDirectoryName, String libraryName, LoadLibraryReportStyle loadLibraryReportStyle) {
    File directory = new File(ApplicationRoot.getArchitectureSpecificDirectory(), libDirectoryName);
    if (libDirectoryName.equalsIgnoreCase("jogl")) {
      directory = new File(directory, ApplicationRoot.getArchitectureSpecificJoglSubDirectory());
    }
    String filename = System.mapLibraryName(libraryName);
    File file = new File(directory, filename);
    if (file.exists()) {
      System.load(file.getAbsolutePath());
    } else {
      //      edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "could not find:", file );
      //      edu.cmu.cs.dennisc.java.lang.SystemUtilities.loadPlatformSpecific( libName );
      System.loadLibrary(libraryName);
    }
  }

  public static boolean areIconsDisplayedInMenus() {
    return true;
  }

  public static <E> E[] returnArray(Class<E> componentType, E... rv) {
    return rv;
  }

  public static <E> E[] createArray(Class<E> componentType, E[]... arrays) {
    int n = 0;
    for (E[] array : arrays) {
      if (array != null) {
        n += array.length;
      }
    }
    E[] rv = ReflectionUtilities.newTypedArrayInstance(componentType, n);
    int offset = 0;
    for (E[] array : arrays) {
      if (array != null) {
        System.arraycopy(array, 0, rv, offset, array.length);
        offset += array.length;
      }
    }
    return rv;
  }

  public static final String PATH_SEPARATOR = System.getProperty("path.separator");

  private static String[] parsePath(String propertyName) {
    String value = System.getProperty(propertyName);
    assert value != null;
    return value.split(PATH_SEPARATOR);
  }

  public static String[] getClassPath() {
    return parsePath("java.class.path");
  }

  public static String[] getLibraryPath() {
    return parsePath("java.library.path");
  }

  public static File getEnvironmentVariableDirectory(String name) {
    String env = System.getenv(name);
    assert env != null : name;

    File dir = new File(env);
    assert dir.exists() : dir;
    assert dir.isDirectory() : dir;
    return dir;
  }
}
