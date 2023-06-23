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

package org.alice.ide.instancefactory.croquet;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import org.alice.ide.IDE;
import org.alice.ide.Theme;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessMethodInvocationFactory;
import org.alice.ide.instancefactory.ThisInstanceFactory;
import org.alice.ide.instancefactory.ThisMethodInvocationFactory;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.ImmutableCascadeFillIn;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.NamedUserType;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class InstanceFactoryFillIn extends ImmutableCascadeFillIn<InstanceFactory, Void> {
  private static final Map<InstanceFactory, InstanceFactoryFillIn> map = Maps.newHashMap();

  public static InstanceFactoryFillIn getInstance(InstanceFactory value) {
    synchronized (map) {
      InstanceFactoryFillIn rv = map.get(value);
      if (rv == null) {
        rv = new InstanceFactoryFillIn(value);
        map.put(value, rv);
      }
      return rv;
    }
  }

  private final InstanceFactory value;

  private InstanceFactoryFillIn(InstanceFactory value) {
    super(UUID.fromString("2fce347e-f10e-4eec-8ac4-291225a5da4f"));
    this.value = value;

    if (this.value == ThisInstanceFactory.getInstance()) {
      ValueListener<NamedUserType> typeListener = e -> InstanceFactoryFillIn.this.markDirty();
      IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addValueListener(typeListener);
    }

    InstanceProperty<?>[] mutablePropertiesOfInterest = this.value.getMutablePropertiesOfInterest();
    PropertyListener propertyAdapter;
    if ((mutablePropertiesOfInterest != null) && (mutablePropertiesOfInterest.length > 0)) {
      propertyAdapter = e -> InstanceFactoryFillIn.this.markDirty();
      for (InstanceProperty<?> property : mutablePropertiesOfInterest) {
        property.addPropertyListener(propertyAdapter);
      }
    }
  }

  @Override
  protected final JComponent createMenuItemIconProxy(ItemNode<? super InstanceFactory, Void> step) {
    Expression expression = this.value.createTransientExpression();
    JComponent expressionPane = PreviewAstI18nFactory.getInstance().createExpressionPane(expression).getAwtComponent();

    Dimension desiredIconSize;
    if ((this.value instanceof ThisMethodInvocationFactory) || (this.value instanceof ThisFieldAccessMethodInvocationFactory)) {
      desiredIconSize = Theme.DEFAULT_SMALLER_ICON_SIZE;
    } else {
      desiredIconSize = Theme.DEFAULT_SMALL_ICON_SIZE;
    }

    // getIcon will scale our icon to whatever dimensions we give it, but want to preserve our aspect ratio
    Dimension iconSize = this.value.getIconFactory().getDefaultSizeForHeight(desiredIconSize.height);
    Icon icon = this.value.getIconFactory().getIcon(iconSize);
    JLabel label = new JLabel(icon);
    int sidePadding = (desiredIconSize.width - icon.getIconWidth()) / 2;
    label.setBorder(new EmptyBorder(0, sidePadding, 0, sidePadding));

    expressionPane.setAlignmentY(0.5f);
    label.setAlignmentY(0.5f);

    JPanel rv = new JPanel();
    //    rv.setLayout( new java.awt.BorderLayout() );
    //    rv.add( label, java.awt.BorderLayout.LINE_START );
    //    rv.add( expressionPane, java.awt.BorderLayout.CENTER );
    rv.setLayout(new BoxLayout(rv, BoxLayout.LINE_AXIS));
    rv.add(label);
    rv.add(expressionPane);
    rv.setOpaque(false);
    return rv;

  }

  @Override
  public final InstanceFactory getTransientValue(ItemNode<? super InstanceFactory, Void> node) {
    return this.value;
  }

  @Override
  public InstanceFactory createValue(ItemNode<? super InstanceFactory, Void> node) {
    return this.value;
  }

  @Override
  protected void appendRepr(StringBuilder sb) {
    super.appendRepr(sb);
    sb.append("value=");
    sb.append(this.value);
  }
}
