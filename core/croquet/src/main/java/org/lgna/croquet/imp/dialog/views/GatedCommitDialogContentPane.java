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
package org.lgna.croquet.imp.dialog.views;

import org.lgna.croquet.GatedCommitDialogCoreComposite;
import org.lgna.croquet.imp.dialog.GatedCommitDialogContentComposite;
import org.lgna.croquet.views.GridBagPanel;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.Separator;
import org.lgna.croquet.views.StatusLabel;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.GridBagConstraints;

/**
 * @author Dennis Cosgrove
 */
public abstract class GatedCommitDialogContentPane extends DialogContentPane {
  public static final Color ERROR_COLOR = Color.RED.darker().darker();

  private final StatusLabel statusLabel = new StatusLabel();
  private final LineAxisPanel controlLine = new LineAxisPanel();

  public GatedCommitDialogContentPane(GatedCommitDialogContentComposite<?> composite) {
    super(composite);
    controlLine.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    GridBagPanel pageEndPanel = new GridBagPanel();
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;
    gbc.weighty = 0.0;
    GatedCommitDialogCoreComposite<?, ?> coreComposite = (GatedCommitDialogCoreComposite<?, ?>) composite.getCoreComposite();
    if (coreComposite.isStatusLineDesired()) {
      pageEndPanel.addComponent(this.getStatusLabel(), gbc);
    }
    pageEndPanel.addComponent(Separator.createInstanceSeparatingTopFromBottom(), gbc);
    pageEndPanel.addComponent(controlLine, gbc);
    controlLine.setBackgroundColor(null);

    this.addPageEndComponent(pageEndPanel);

    this.statusLabel.setForegroundColor(ERROR_COLOR);
  }

  protected LineAxisPanel getControlLine() {
    return this.controlLine;
  }

  public StatusLabel getStatusLabel() {
    return this.statusLabel;
  }
}
