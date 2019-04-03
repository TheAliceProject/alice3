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
package org.lgna.issue.swing;

import edu.cmu.cs.dennisc.java.lang.ThrowableUtilities;
import edu.cmu.cs.dennisc.javax.swing.JOptionPaneUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JFauxHyperlink;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;

/**
 * @author Dennis Cosgrove
 */
public class JExceptionSubPane extends JPanel {
  private final Thread thread;
  private final Throwable originalThrowable;
  private final Throwable originalThrowableOrTarget;

  public JExceptionSubPane(Thread thread, Throwable originalThrowable, Throwable originalThrowableOrTarget) {
    assert thread != null;
    assert originalThrowable != null;
    this.thread = thread;
    this.originalThrowable = originalThrowable;
    this.originalThrowableOrTarget = originalThrowableOrTarget;
    //this.removeAll();
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    JFauxHyperlink vcShowStackTrace = new JFauxHyperlink(new AbstractAction("show complete stack trace...") {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPaneUtilities.showMessageDialogInScrollableUneditableTextArea(JExceptionSubPane.this, ThrowableUtilities.getStackTraceAsString(getOriginalThrowable()), "Stack Trace", JOptionPane.INFORMATION_MESSAGE);
      }
    });

    StringBuffer sb = new StringBuffer();
    sb.append(originalThrowable.getClass().getSimpleName());
    String message = originalThrowable.getLocalizedMessage();
    if ((message != null) && (message.length() > 0)) {
      sb.append("[");
      sb.append(message);
      sb.append("]");
    }
    sb.append(" in ");
    sb.append(thread.getClass().getSimpleName());
    sb.append("[");
    sb.append(thread.getName());
    sb.append("]");

    this.add(new JLabel(sb.toString()));
    StackTraceElement[] elements = originalThrowable.getStackTrace();
    if (elements.length > 0) {
      StackTraceElement e0 = elements[0];
      this.add(new JLabel("class: " + e0.getClassName()));
      this.add(new JLabel("method: " + e0.getMethodName()));
      this.add(new JLabel("in file " + e0.getFileName() + " at line number " + e0.getLineNumber()));
    }
    this.add(vcShowStackTrace);
  }

  public Thread getThread() {
    return this.thread;
  }

  public Throwable getOriginalThrowable() {
    return this.originalThrowable;
  }

  public Throwable getOriginalThrowableOrTarget() {
    return this.originalThrowableOrTarget;
  }
}
