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
package org.lgna.croquet.imp.frame;

import org.lgna.croquet.BooleanState;
import org.lgna.croquet.Element;
import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.Group;
import org.lgna.croquet.views.Frame;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractIsFrameShowingState extends BooleanState {
  public AbstractIsFrameShowingState(Group group, UUID migrationId) {
    super(group, migrationId, false);
  }

  @Override
  protected void localize() {
    super.localize();
    this.title = this.findLocalizedText("title");
  }

  @Override
  protected abstract Class<? extends Element> getClassUsedForLocalization();

  public abstract FrameComposite<?> getFrameComposite();

  private String getFrameTitle() {
    this.initializeIfNecessary();
    String rv = this.title;
    if (rv == null) {
      rv = this.getTrueText();
      if (rv != null) {
        rv = rv.replaceAll("<[a-z]*>", "");
        rv = rv.replaceAll("</[a-z]*>", "");
        if (rv.endsWith("...")) {
          rv = rv.substring(0, rv.length() - 3);
        }
      }
    }
    return rv;
  }

  @Override
  protected void fireChanged(Boolean prevValue, Boolean nextValue, boolean isAdjusting) {
    super.fireChanged(prevValue, nextValue, isAdjusting);
    if (nextValue) {
      Frame frameView = this.getOwnerFrameView_createIfNecessary();
      frameView.setTitle(this.getFrameTitle());
      this.getFrameComposite().handlePreActivation();
      frameView.setVisible(true);
    } else {
      if (this.ownerFrameView != null) {
        if (this.ownerFrameView.isVisible()) {
          this.getFrameComposite().handlePostDeactivation();
          this.ownerFrameView.setVisible(false);
        }
      } else {
        //pass
      }
    }
  }

  private Frame getOwnerFrameView_createIfNecessary() {
    if (this.ownerFrameView == null) {
      FrameComposite<?> frameComposite = this.getFrameComposite();
      this.ownerFrameView = new Frame();
      this.ownerFrameView.getContentPane().addCenterComponent(frameComposite.getRootComponent());
      frameComposite.updateWindowSize(this.ownerFrameView);
      this.ownerFrameView.addWindowListener(this.windowListener);
    }
    return this.ownerFrameView;
  }

  private void handleWindowClosing(WindowEvent e) {
    this.setValueTransactionlessly(false);
  }

  private final WindowListener windowListener = new WindowListener() {
    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
      handleWindowClosing(e);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }
  };

  private String title;
  private Frame ownerFrameView;

}
