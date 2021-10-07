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
package edu.cmu.cs.dennisc.javax.swing.option;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.javax.swing.WindowStack;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import java.awt.EventQueue;
import java.io.File;

public class Dialogs {
  private Dialogs() {
  }

  public static boolean confirm(String title, String message) {
    // TODO ensure this is run on DispatchThread
    return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(WindowStack.peek(), message, title, JOptionPane.YES_NO_OPTION);
  }

  public static boolean confirmWithWarning(String title, String message) {
    // TODO ensure this is run on DispatchThread
    return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(WindowStack.peek(), message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
  }

  public static YesNoCancelResult confirmOrCancel(String title, String message) {
    // TODO ensure this is run on DispatchThread
    return YesNoCancelResult.getInstance(JOptionPane.showConfirmDialog(WindowStack.peek(), message, title, JOptionPane.YES_NO_CANCEL_OPTION));
  }

  public static void showUnableToOpenFileDialog(File file, String message) {
    //TODO I18n
    Dialogs.showError("Cannot read file", String.format("Unable to open file %s.\n\n%s", FileUtilities.getCanonicalPathIfPossible(file), message));
  }

  public static void showError(String title, String message) {
    showMessageDialog(title, message, JOptionPane.ERROR_MESSAGE, null);
  }

  public static void showWarning(String title, String message) {
    showMessageDialog(title, message, JOptionPane.WARNING_MESSAGE, null);
  }

  public static void showWarning(String message) {
    showWarning("", message);
  }

  public static void showInfo(String title, String message, Icon icon) {
    showMessageDialog(title, message, JOptionPane.INFORMATION_MESSAGE, icon);
  }

  public static void showInfo(String title, String message) {
    showMessageDialog(title, message, JOptionPane.INFORMATION_MESSAGE, null);
  }

  public static void showInfo(String message) {
    showInfo("", message);
  }

  private static void showMessageDialog(String title, String message, int messageType, Icon icon) {
    show(() -> JOptionPane.showMessageDialog(WindowStack.peek(), message, title, messageType, icon));
  }

  private static void show(Runnable showDialog) {
    if (EventQueue.isDispatchThread()) {
      showDialog.run();
    } else {
      EventQueue.invokeLater(showDialog);
    }
  }
}
