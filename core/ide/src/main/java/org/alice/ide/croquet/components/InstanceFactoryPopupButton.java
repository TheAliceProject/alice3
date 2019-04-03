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

package org.alice.ide.croquet.components;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.awt.font.FontUtilities;
import edu.cmu.cs.dennisc.pattern.HowMuch;
import org.alice.ide.IDE;
import org.alice.ide.Theme;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.State;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.views.CustomItemStatePopupButton;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.NamedUserType;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import java.awt.Dimension;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryPopupButton extends CustomItemStatePopupButton<InstanceFactory> {
  // note: for singleton ThisInstanceFactory
  private final ValueListener<NamedUserType> typeListener = new ValueListener<NamedUserType>() {
    @Override
    public void valueChanged(ValueEvent<NamedUserType> e) {
      InstanceFactoryPopupButton.this.repaint();
    }
  };

  public InstanceFactoryPopupButton(InstanceFactoryState instanceFactoryState) {
    super(instanceFactoryState);
    this.getAwtComponent().setLayout(new BoxLayout(this.getAwtComponent(), BoxLayout.LINE_AXIS));
  }

  @Override
  protected AbstractButton createSwingButton() {
    return new JFauxComboBoxPopupButton() {
      @Override
      public void invalidate() {
        super.invalidate();
        refreshIfNecessary();
      }
    };
  }

  private InstanceFactory nextValue;

  @Override
  protected void handleChanged(State<InstanceFactory> state, InstanceFactory prevValue, InstanceFactory nextValue) {
    this.nextValue = nextValue;
    this.refreshLater();
  }

  private boolean isInTheMidstOfRefreshing = false;
  private boolean isRefreshNecessary = true;

  protected void internalRefresh() {
    this.internalForgetAndRemoveAllComponents();
    SwingComponentView<?> expressionPane = PreviewAstI18nFactory.getInstance().createExpressionPane(nextValue != null ? nextValue.createTransientExpression() : null);

    for (JLabel label : ComponentUtilities.findAllMatches(expressionPane.getAwtComponent(), HowMuch.COMPONENT_AND_DESCENDANTS, JLabel.class)) {
      FontUtilities.setFontToScaledFont(label, 2.0f);
    }

    if (nextValue != null) {
      IconFactory iconFactory = nextValue.getIconFactory();
      if ((iconFactory != null) && (iconFactory != EmptyIconFactory.getInstance())) {
        final boolean IS_TRIMMED_ICON_DESIRED = true;
        Dimension size = IS_TRIMMED_ICON_DESIRED ? iconFactory.getTrimmedSizeForHeight(Theme.DEFAULT_SMALL_ICON_SIZE.height) : Theme.DEFAULT_SMALL_ICON_SIZE;
        Icon icon = iconFactory.getIcon(size);
        if (icon != null) {
          this.internalAddComponent(new Label(icon));
        }
      }
    }
    this.internalAddComponent(expressionPane);

    this.revalidateAndRepaint();
  }

  private void refreshIfNecessary() {
    if (this.isRefreshNecessary) {
      if (this.isInTheMidstOfRefreshing) {
        //pass
      } else {
        this.isInTheMidstOfRefreshing = true;
        try {
          //this.forgetAndRemoveAllComponents();
          synchronized (this.getTreeLock()) {
            this.internalRefresh();
          }
          this.isRefreshNecessary = false;
        } finally {
          this.isInTheMidstOfRefreshing = false;
        }
      }
    }
  }

  public final void refreshLater() {
    this.isRefreshNecessary = true;
    this.revalidateAndRepaint();
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.refreshLater();
    IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addValueListener(this.typeListener);
  }

  @Override
  protected void handleUndisplayable() {
    IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().removeValueListener(this.typeListener);
    super.handleUndisplayable();
  }
}
