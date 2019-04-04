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
package edu.cmu.cs.dennisc.video.vlcj;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import edu.cmu.cs.dennisc.app.ApplicationRoot;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.timing.Timer;
import edu.cmu.cs.dennisc.video.VideoPlayer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;

/**
 * @author Dennis Cosgrove
 */
public class VlcjUtilities {
  private static boolean isInitializationAttempted = false;
  private static boolean isInitialized = false;

  private static void initializeIfNecessary() {
    if (isInitializationAttempted) {
      //pass
    } else {
      isInitializationAttempted = true;
      Timer timer = new Timer("initialize vlcj");
      timer.start();
      String vlcLibraryName = RuntimeUtil.getLibVlcLibraryName();
      boolean isWorthAttemptingToLoad;
      if (SystemUtilities.isLinux()) {
        isWorthAttemptingToLoad = true;
      } else {
        File archDirectory = ApplicationRoot.getArchitectureSpecificDirectory();
        File vlcDirectory = new File(archDirectory, "libvlc");
        File toBeSearchedDirectory;
        if (SystemUtilities.isMac()) {
          toBeSearchedDirectory = new File(vlcDirectory, "lib");
        } else {
          toBeSearchedDirectory = vlcDirectory;
        }
        if (toBeSearchedDirectory.exists()) {
          NativeLibrary.addSearchPath(vlcLibraryName, toBeSearchedDirectory.getAbsolutePath());
          isWorthAttemptingToLoad = true;
        } else {
          isWorthAttemptingToLoad = false;
        }
      }

      if (isWorthAttemptingToLoad) {
        try {
          Native.loadLibrary(vlcLibraryName, LibVlc.class);
          isInitialized = true;
        } catch (UnsatisfiedLinkError ule) {
          //uk.co.caprica.vlcj.discovery.NativeDiscovery nativeDiscovery = new uk.co.caprica.vlcj.discovery.NativeDiscovery();
          //isWorthAttemptingToLoad = nativeDiscovery.discover();
          ule.printStackTrace();
        }
      } else {
        System.err.println("failed to discover vlc");
      }
      timer.stopAndPrintResults();
    }
  }

  public static VideoPlayer createVideoPlayer() {
    initializeIfNecessary();
    if (isInitialized) {
      return new VlcjVideoPlayer();
    } else {
      return null;
    }
  }
}
