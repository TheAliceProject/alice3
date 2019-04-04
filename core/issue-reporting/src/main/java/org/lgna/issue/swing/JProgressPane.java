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

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.javax.swing.DocumentUtilities;
import org.lgna.issue.IssueSubmissionProgressWorker;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

/**
 * @author Dennis Cosgrove
 */
public class JProgressPane extends JPanel {
  private class RunInBackgroundAction extends AbstractAction {
    public RunInBackgroundAction() {
      super("run in background");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      isBackgrounded = true;
      SwingUtilities.getRoot(JProgressPane.this).setVisible(false);
      worker.hideOwnerDialog();
    }
  }

  public JProgressPane(IssueSubmissionProgressWorker worker) {
    this.worker = worker;
    JScrollPane scrollPane = new JScrollPane(this.textArea);

    this.runInBackgroundButton = new JButton(new RunInBackgroundAction());
    JPanel bottomPane = new JPanel();
    bottomPane.setLayout(new FlowLayout(FlowLayout.TRAILING));
    bottomPane.add(this.runInBackgroundButton);

    this.setLayout(new BorderLayout());
    this.add(scrollPane, BorderLayout.CENTER);
    this.add(bottomPane, BorderLayout.PAGE_END);

    this.setPreferredSize(DimensionUtilities.createWiderGoldenRatioSizeFromWidth(600));
  }

  @Override
  public void addNotify() {
    super.addNotify();
    this.getRootPane().setDefaultButton(this.runInBackgroundButton);
  }

  public void addMessage(String message) {
    Document document = this.textArea.getDocument();
    DocumentUtilities.appendString(document, message);
    DocumentUtilities.appendString(document, "\n");
  }

  public boolean isBackgrounded() {
    return this.isBackgrounded;
  }

  private final IssueSubmissionProgressWorker worker;
  private final JTextArea textArea = new JTextArea();
  private final JButton runInBackgroundButton;
  private boolean isBackgrounded;
}
