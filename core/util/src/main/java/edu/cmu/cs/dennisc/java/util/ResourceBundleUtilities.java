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

import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Dennis Cosgrove
 */
public abstract class ResourceBundleUtilities {
  private ResourceBundleUtilities() {
    throw new AssertionError();
  }

  private static final class Utf8ResourceBundleControl extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
      String bundleName = this.toBundleName(baseName, locale);
      String resourceName = this.toResourceName(bundleName, "properties");
      InputStream stream = null;
      if (reload) {
        URL url = loader.getResource(resourceName);
        if (url != null) {
          URLConnection connection = url.openConnection();
          if (connection != null) {
            connection.setUseCaches(false);
            stream = connection.getInputStream();
          }
        }
      } else {
        stream = loader.getResourceAsStream(resourceName);
      }
      ResourceBundle bundle = null;
      if (stream != null) {
        try {
          bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
        } finally {
          stream.close();
        }
      }
      return bundle;
    }
  }

  public static ResourceBundle getUtf8Bundle(String baseName, Locale locale) {
    return ResourceBundle.getBundle(baseName, locale, new Utf8ResourceBundleControl());
  }

  private static ResourceBundle getUtf8Bundle(String bundleName) {
    return getUtf8Bundle(bundleName, JComponent.getDefaultLocale());
  }

  public static String getStringForKey(String key, String bundleName) {
    try {
      return getUtf8Bundle(bundleName).getString(key);
    } catch (MissingResourceException mre) {
      Logger.throwable(mre, bundleName, key);
      return key;
    }
  }

  public static String getStringForKey(String key, Class<?> cls) {
    return getStringForKey(key, cls.getPackage().getName() + ".croquet");
  }

  public static String getStringFromSimpleNames(Class<?> cls, String baseName) {
    ResourceBundle resourceBundle = getUtf8Bundle(baseName);
    String key;
    Class<?> c = cls;
    do {
      if (c != null) {
        key = c.getSimpleName();
        c = c.getSuperclass();
      } else {
        throw new RuntimeException("cannot find resource for " + cls);
        //edu.cmu.cs.dennisc.print.PrintUtilities.println( "cannot find resource for", cls );
        //return null;
      }
      try {
        String unused = resourceBundle.getString(key);
        break;
      } catch (RuntimeException re) {
        //pass;
      }
    } while (true);
    return resourceBundle.getString(key);
  }
}
