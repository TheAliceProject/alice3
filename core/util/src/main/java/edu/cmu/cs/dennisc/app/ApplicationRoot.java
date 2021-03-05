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
package edu.cmu.cs.dennisc.app;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;

import javax.swing.JOptionPane;
import java.io.File;

/**
 * @author Dennis Cosgrove
 */
public class ApplicationRoot {
  private static final String DEFAULT_APPLICATION_ROOT_SYSTEM_PROPERTY = "org.alice.ide.rootDirectory";
  private static final String DEFAULT_APPLICATION_NAME = "Alice";

  private static File rootDirectory;

  public static void initializeIfNecessary() {
    if (rootDirectory != null) {
      //pass
    } else {
      String rootDirectoryPath = System.getProperty(DEFAULT_APPLICATION_ROOT_SYSTEM_PROPERTY);
      //todo: fallback to System.getProperty( "user.dir" ) ???
      if (rootDirectoryPath != null) {
        rootDirectory = new File(rootDirectoryPath);
        if (rootDirectory.exists()) {
          //pass
        } else {
          StringBuilder sb = new StringBuilder();
          sb.append("system property: ");
          sb.append(DEFAULT_APPLICATION_ROOT_SYSTEM_PROPERTY);
          sb.append(" is incorrectly set.\n");
          sb.append(rootDirectory);
          sb.append(" does not exist.\n");
          sb.append(DEFAULT_APPLICATION_NAME);
          sb.append(" will not work until this is addressed.");
          JOptionPane.showMessageDialog(null, sb.toString(), "Application Root Error", JOptionPane.ERROR_MESSAGE);
          System.exit(-1);
        }
      } else {
        StringBuilder sb = new StringBuilder();
        sb.append("system property: ");
        sb.append(DEFAULT_APPLICATION_ROOT_SYSTEM_PROPERTY);
        sb.append(" is not set.\n");
        sb.append(DEFAULT_APPLICATION_NAME);
        sb.append(" will not work until this is addressed.");
        JOptionPane.showMessageDialog(null, sb.toString(), "Application Root Error", JOptionPane.ERROR_MESSAGE);
        rootDirectory = null;
        System.exit(-1);
      }
    }
  }

  private ApplicationRoot() {
    throw new AssertionError();
  }

  public static File getRootDirectory() {
    initializeIfNecessary();
    return rootDirectory;
  }

  public static File getPlatformDirectory() {
    return new File(getRootDirectory(), "platform");
  }

  public static String getArchitectureSpecificJoglSubDirectory() {
    StringBuilder sb = new StringBuilder("natives/");
    if (SystemUtilities.isMac()) {
      sb.append("macosx-universal/");
    } else {
      Integer bitCount = SystemUtilities.getBitCount();
      if (bitCount != null) {
        if (SystemUtilities.isWindows()) {
          sb.append("windows-");
        } else if (SystemUtilities.isLinux()) {
          sb.append("linux-");
        } else {
          throw new RuntimeException(System.getProperty("os.name"));
        }
        appendArchitecture(sb, bitCount);
      } else {
        throw new RuntimeException(System.getProperty("sun.arch.data.model"));
      }
    }
    return sb.toString();
  }

  private static void appendArchitecture(StringBuilder sb, Integer bitCount) {
    if (SystemUtilities.isArmArchitecture()) {
      sb.append("armv6hf/");
      return;
    }
    switch (bitCount) {
    case 32:
      sb.append("i586/");
      break;
    case 64:
      sb.append("amd64/");
      break;
    default:
      throw new RuntimeException(System.getProperty("sun.arch.data.model"));
    }
  }

  public static File getArchitectureSpecificDirectory() {
    StringBuilder sb = new StringBuilder();
    if (SystemUtilities.isMac()) {
      sb.append("macosx");
    } else {
      Integer bitCount = SystemUtilities.getBitCount();
      if (bitCount != null) {
        if (SystemUtilities.isWindows()) {
          sb.append("win");
          sb.append(bitCount);
        } else if (SystemUtilities.isLinux()) {
          sb.append("linux-");
          appendArchitecture(sb, bitCount);
        } else {
          throw new RuntimeException(System.getProperty("os.name"));
        }
      } else {
        throw new RuntimeException(System.getProperty("sun.arch.data.model"));
      }
    }
    return new File(getPlatformDirectory(), sb.toString());
  }
  //  public static java.io.File getCommand( String subPath ) {
  //    StringBuilder sb = new StringBuilder();
  //    sb.append( subPath );
  //    if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
  //      sb.append( ".exe" );
  //    }
  //    return new java.io.File( this.getArchitectureSpecificDirectory(), sb.toString() );
  //  }
}
