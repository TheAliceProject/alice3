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

package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.Model;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import java.awt.Insets;
import java.util.Enumeration;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractButton<J extends javax.swing.AbstractButton, M extends Model> extends ViewController<J, M> {
  private static final ButtonModel MODEL_FOR_NULL = new DefaultButtonModel();

  private final String uiDefaultsName;
  private boolean isIconClobbered;
  private Icon clobberIcon;

  public AbstractButton(M model, String uiDefaultsName) {
    super(model);
    this.uiDefaultsName = uiDefaultsName;
  }

  public boolean isIconClobbered() {
    return this.isIconClobbered;
  }

  public void setIconClobbered(boolean isIconClobbered) {
    this.isIconClobbered = isIconClobbered;
  }

  public Icon getClobberIcon() {
    return this.clobberIcon;
  }

  public void setClobberIcon(Icon clobberIcon) {
    this.clobberIcon = clobberIcon;
    this.isIconClobbered = true;
  }

  public int getIconTextGap() {
    return this.getAwtComponent().getIconTextGap();
  }

  public void setIconTextGap(int iconTextGap) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setIconTextGap(iconTextGap);
  }

  private static final Insets ZERO_MARGIN = new Insets(0, 0, 0, 0);

  public void tightenUpMargin(Insets margin) {
    this.checkEventDispatchThread();
    javax.swing.AbstractButton jButton = this.getAwtComponent();
    if ("javax.swing.plaf.synth.SynthButtonUI".equals(jButton.getUI().getClass().getName())) {
      if (this.uiDefaultsName != null) {
        if (margin == null) {
          int right;
          String text = jButton.getText();
          final int PAD = 4;
          if ((text != null) && (text.length() > 0)) {
            right = PAD + 4;
          } else {
            right = PAD;
          }
          margin = new Insets(PAD, PAD, PAD, right);
        }
        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put(this.uiDefaultsName + ".contentMargins", margin);
        this.getAwtComponent().putClientProperty("Nimbus.Overrides", uiDefaults);
      } else {
        Enumeration<Object> enm = UIManager.getDefaults().keys();
        while (enm.hasMoreElements()) {
          Object key = enm.nextElement();
          if (key != null) {
            if (key.toString().endsWith(".contentMargins")) {
              Logger.errln(key, UIManager.get(key));
            }
          }
        }
        Logger.severe("uiDefaultsName is null:", this);
      }
    } else {
      this.setMargin(margin != null ? margin : ZERO_MARGIN);
    }
  }

  public final void tightenUpMargin() {
    this.tightenUpMargin(null);
  }

  /* package-private */void setSwingButtonModel(ButtonModel model) {
    if (model == null) {
      model = MODEL_FOR_NULL;
    }
    if (model != this.getAwtComponent().getModel()) {
      this.checkEventDispatchThread();
      this.getAwtComponent().setModel(model);
    }
  }

  /* package-private */void setAction(Action action) {
    if (action != this.getAwtComponent().getAction()) {
      this.checkEventDispatchThread();
      this.getAwtComponent().setAction(action);
    }
  }

  public void doClick() {
    this.checkEventDispatchThread();
    this.getAwtComponent().doClick();
  }

  public void setHorizontalTextPosition(HorizontalTextPosition horizontalTextPosition) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setHorizontalTextPosition(horizontalTextPosition.getInternal());
  }

  public void setVerticalTextPosition(VerticalTextPosition verticalTextPosition) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setVerticalTextPosition(verticalTextPosition.getInternal());
  }

  public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setHorizontalAlignment(horizontalAlignment.getInternal());
  }

  public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setVerticalAlignment(verticalAlignment.getInternal());
  }

  public Insets getMargin() {
    return this.getAwtComponent().getMargin();
  }

  public void setMargin(Insets margin) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setMargin(margin);
  }
}
