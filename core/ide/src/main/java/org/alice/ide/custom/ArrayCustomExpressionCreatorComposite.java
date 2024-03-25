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
package org.alice.ide.custom;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import org.alice.ide.IDE;
import org.alice.ide.croquet.codecs.NodeCodec;
import org.alice.ide.custom.components.ArrayCustomExpressionCreatorView;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Cascade;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.data.MutableListData;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArrayInstanceCreation;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ArrayCustomExpressionCreatorComposite extends CustomExpressionCreatorComposite<ArrayCustomExpressionCreatorView> {
  private static Map<AbstractType<?, ?, ?>, ArrayCustomExpressionCreatorComposite> map = Maps.newHashMap();

  public static ArrayCustomExpressionCreatorComposite getInstance(AbstractType<?, ?, ?> arrayType) {
    synchronized (map) {
      ArrayCustomExpressionCreatorComposite rv = map.get(arrayType);
      if (rv == null) {
        rv = new ArrayCustomExpressionCreatorComposite(arrayType);
        map.put(arrayType, rv);
      }
      return rv;
    }
  }

  private final PlainStringValue arrayTypeLabel = this.createStringValue("arrayTypeLabel");
  private final AbstractType<?, ?, ?> arrayType;

  private final MutableListData<Expression> data = new MutableListData<Expression>(NodeCodec.getInstance(Expression.class));

  private final Cascade<Expression> addItemCascade = this.createCascadeWithInternalBlank("addItemCascade", Expression.class, new CascadeCustomizer<Expression>() {
    @Override
    public void appendBlankChildren(List<CascadeBlankChild> rv, BlankNode<Expression> blankNode) {
      IDE ide = IDE.getActiveInstance();
      ide.getExpressionCascadeManager().appendItems(rv, blankNode, arrayType.getComponentType(), null);
    }

    @Override
    public Edit createEdit(Expression[] values) {
      assert values.length == 1;
      data.internalAddItem(values[0]);
      getView().updatePreview();
      return null;
    }
  });

  private ArrayCustomExpressionCreatorComposite(AbstractType<?, ?, ?> arrayType) {
    super(UUID.fromString("187d56c4-cc05-4157-a5fc-55943ca5b099"));
    assert arrayType.isArray() : arrayType;
    this.arrayType = arrayType;
  }

  public AbstractType<?, ?, ?> getArrayType() {
    return this.arrayType;
  }

  public PlainStringValue getArrayTypeLabel() {
    return this.arrayTypeLabel;
  }

  public MutableListData<Expression> getData() {
    return this.data;
  }

  public Cascade<Expression> getAddItemCascade() {
    return this.addItemCascade;
  }

  @Override
  protected ArrayCustomExpressionCreatorView createView() {
    return new ArrayCustomExpressionCreatorView(this);
  }

  @Override
  protected Expression createValue() {
    return AstUtilities.createArrayInstanceCreation(this.arrayType, this.data.toArray());
  }

  @Override
  protected Status getStatusPreRejectorCheck() {
    return IS_GOOD_TO_GO_STATUS;
  }

  @Override
  protected void initializeToPreviousExpression(Expression expression) {
    List<Expression> items = Lists.newLinkedList();
    if (expression instanceof ArrayInstanceCreation) {
      ArrayInstanceCreation arrayInstanceCreation = (ArrayInstanceCreation) expression;
      if (this.arrayType.isAssignableFrom(arrayInstanceCreation.getType())) {
        for (Expression itemExpression : arrayInstanceCreation.expressions) {
          items.add(IDE.getActiveInstance().createCopy(itemExpression));
        }
      }
    }
    this.data.internalSetAllItems(items);
  }

  @Override
  protected Dimension calculateWindowSize(AbstractWindow<?> window) {
    return new Dimension(400, 500);
  }

  public static void main(String[] args) throws Exception {
    UIManagerUtilities.setLookAndFeel("Nimbus");
    //new org.alice.stageide.StageIDE();
    try {
      ArrayCustomExpressionCreatorComposite.getInstance(JavaType.getInstance(String[].class)).getValueCreator().fire(NullTrigger.createUserActivity());
    } catch (CancelException ce) {
      //pass
    }
    System.exit(0);
  }
}
