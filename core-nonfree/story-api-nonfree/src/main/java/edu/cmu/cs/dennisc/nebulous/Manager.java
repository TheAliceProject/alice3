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

package edu.cmu.cs.dennisc.nebulous;

import com.jogamp.opengl.GL;
import edu.cmu.cs.dennisc.eula.EULAUtilities;
import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

import javax.swing.JOptionPane;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * @author Dennis Cosgrove
 */
public class Manager {
  public static final double NEBULOUS_VERSION = 1.7;

  private static boolean s_isInitialized = false;
  private static boolean s_isLicensePromptDesired = true;
  private static final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";

  private static List<File> s_pendingBundles;

  private static native void setVersion(double version);

  private static native void addBundlePath(String bundlePath);

  private static native void removeBundlePath(String bundlePath);

  private static native void setRawResourceDirectory(String rourcePath);

  private static native void unloadActiveModelData();

  private static native void unloadUnusedTextures(GL gl);

  public static native void setDebugDraw(boolean debugDraw);

  private static long lastErrorOrNotification = 0L;

  private static void doInitializationIfNecessary() {
    try {
      initializeIfNecessary();
    } catch (LicenseRejectedException lre) {
      JOptionPane.showMessageDialog(null, "license rejected");
      //throw new RuntimeException( lre );
    } catch (Throwable t) {
      // To keep the user from having to dismiss the same dialog repeatedly only show the dialog if it has
      // been more than a second since the last dialog or the last error.
      if (System.currentTimeMillis() - lastErrorOrNotification > 1000) {
        JOptionPane.showMessageDialog(null, "failed to initialize art assets");
        t.printStackTrace();
      }
      lastErrorOrNotification = System.currentTimeMillis();
    }
  }

  private static List<File> getPendingBundles() {
    if (s_pendingBundles == null) {
      s_pendingBundles = new LinkedList<File>();
    }
    return s_pendingBundles;
  }

  public static void unloadNebulousModelData() {
    if (isInitialized()) {
      unloadActiveModelData();
    }
  }

  public static void unloadUnusedNebulousTextureData(GL gl) {
    if (isInitialized()) {
      try {
        unloadUnusedTextures(gl);
      } catch (RuntimeException e) {
        System.err.println("Unable to unload unused textures in nebulous code. Continuing execution.");
        e.printStackTrace();
      }
    }
  }

  public static void initializeIfNecessary() throws LicenseRejectedException {
    if (isInitialized()) {
      //pass
    } else {
      if (s_isLicensePromptDesired) {
        EULAUtilities.promptUserToAcceptEULAIfNecessary(License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement: The Sims (TM) 2 Art Assets", License.TEXT, "The Sims (TM) 2 Art Assets");
        Preferences userPreferences = Preferences.userNodeForPackage(License.class);
        boolean isLicenseAccepted = userPreferences.getBoolean(IS_LICENSE_ACCEPTED_PREFERENCE_KEY, false);
        if (isLicenseAccepted) {
          //pass
        } else {
          s_isLicensePromptDesired = false;
        }
        if (isLicenseAccepted) {
          userPreferences.putBoolean(IS_LICENSE_ACCEPTED_PREFERENCE_KEY, true);
          if (SystemUtilities.isPropertyTrue("org.alice.ide.internalDebugMode")) {
            SystemUtilities.loadLibrary("", "jni_nebulous", LoadLibraryReportStyle.EXCEPTION);
          } else {
            SystemUtilities.loadLibrary("nebulous", "jni_nebulous", LoadLibraryReportStyle.EXCEPTION);
          }
          for (File directory : Manager.getPendingBundles()) {
            Manager.addBundlePath(directory.getAbsolutePath());
          }
          Manager.setVersion(NEBULOUS_VERSION);

          s_isInitialized = true;
        } else {
          throw new LicenseRejectedException();
        }
      }
      RenderContext.addUnusedTexturesListener(new RenderContext.UnusedTexturesListener() {
        @Override
        public void unusedTexturesCleared(GL gl) {
          unloadUnusedNebulousTextureData(gl);
        }
      });
    }
  }

  public static boolean isInitialized() {
    return s_isInitialized;
  }

  public static void resetLicensePromptDesiredToTrue() {
    s_isLicensePromptDesired = true;
  }

  public static void setRawResourcePath(File file) {
    doInitializationIfNecessary();
    Manager.setRawResourceDirectory(file.getAbsolutePath());
  }

  public static void addBundle(File file) {
    doInitializationIfNecessary();
    if (isInitialized()) {
      Manager.addBundlePath(file.getAbsolutePath());
    } else {
      Manager.getPendingBundles().add(file);
    }
  }

  public static void removeBundle(File file) {
    doInitializationIfNecessary();
    if (isInitialized()) {
      Manager.removeBundlePath(file.getAbsolutePath());
    } else {
      Manager.getPendingBundles().remove(file);
    }
  }
}
