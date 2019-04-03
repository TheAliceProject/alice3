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
package org.alice.ide.custom.components;

import edu.cmu.cs.dennisc.java.awt.font.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.javax.swing.components.JCloseButton;
import net.miginfocom.swing.MigLayout;
import org.alice.ide.IDE;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.cascade.ExpressionCascadeContext;
import org.alice.ide.common.TypeIcon;
import org.alice.ide.custom.ArrayCustomExpressionCreatorComposite;
import org.alice.ide.x.AstI18nFactory;
import org.alice.ide.x.DialogAstI18nFactory;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeWithInternalBlank;
import org.lgna.croquet.Group;
import org.lgna.croquet.PopupPrepModel;
import org.lgna.croquet.data.ListData;
import org.lgna.croquet.data.MutableListData;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.DropDown;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.MutableList;
import org.lgna.croquet.views.PopupButton;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ArrayCustomExpressionCreatorView extends CustomExpressionCreatorView {
  private abstract static class ItemAtIndexCascade<T> extends CascadeWithInternalBlank<T> {
    private final ListData<T> data;
    private final int index;

    public ItemAtIndexCascade(Group group, UUID migrationId, ListData<T> data, int index) {
      super(group, migrationId, data.getItemCodec().getValueClass());
      this.data = data;
      this.index = index;
    }

    public ListData<T> getData() {
      return this.data;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    protected Edit createEdit(UserActivity userActivity, T[] values) {
      T[] items = this.data.toArray();
      items[this.index] = values[0];
      this.data.internalSetAllItems(items);
      return null;
    }
  }

  private static class ExpressionAtIndexContext implements ExpressionCascadeContext {
    private final Expression expression;

    public ExpressionAtIndexContext(Expression expression) {
      this.expression = expression;
    }

    @Override
    public Expression getPreviousExpression() {
      return this.expression;
    }

    @Override
    public BlockStatementIndexPair getBlockStatementIndexPair() {
      return null;
    }
  }

  private static class ExpressionAtIndexCascade extends ItemAtIndexCascade<Expression> {
    private final AbstractType<?, ?, ?> componentType;
    private ExpressionAtIndexContext pushedContext;

    public ExpressionAtIndexCascade(ListData<Expression> data, int index, AbstractType<?, ?, ?> componentType) {
      super(Application.INHERIT_GROUP, UUID.fromString("bbdd16fe-0ea0-41ae-8e09-fde5ee075e06"), data, index);
      this.componentType = componentType;
    }

    @Override
    protected void prologue() {
      this.pushedContext = new ExpressionAtIndexContext(this.getData().getItemAt(this.getIndex()));
      IDE.getActiveInstance().getExpressionCascadeManager().pushContext(this.pushedContext);
      super.prologue();
    }

    @Override
    protected void epilogue() {
      super.epilogue();
      IDE.getActiveInstance().getExpressionCascadeManager().popAndCheckContext(this.pushedContext);
      this.pushedContext = null;
    }

    @Override
    protected List<CascadeBlankChild> updateBlankChildren(List<CascadeBlankChild> rv, BlankNode<Expression> blankNode) {
      IDE ide = IDE.getActiveInstance();
      ide.getExpressionCascadeManager().appendItems(rv, blankNode, this.componentType, null);
      return rv;
    }
  }

  private static class DeleteItemAtIndexOperation<T> extends ActionOperation {
    private final ListData<T> data;
    private final int index;

    public DeleteItemAtIndexOperation(ListData<T> data, int index) {
      super(Application.INHERIT_GROUP, UUID.fromString("138a9eae-eeb8-40ab-972a-64569a9e16e8"));
      this.data = data;
      this.index = index;
    }

    public ListData<T> getData() {
      return this.data;
    }

    public int getIndex() {
      return this.index;
    }

    @Override
    protected void perform(UserActivity activity) {
      this.data.internalRemoveItem(this.data.getItemAt(this.index));
      activity.finish();
    }
  }

  private static class ExpressionDropDown extends DropDown<PopupPrepModel> {
    private class MainComponent extends BorderPanel {
      private final AstI18nFactory factory;

      public MainComponent(AstI18nFactory factory) {
        this.factory = factory;
      }

      @Override
      protected void internalRefresh() {
        super.internalRefresh();
        this.forgetAndRemoveAllComponents();
        Expression expression = cascade.getData().getItemAt(cascade.getIndex());
        this.addLineStartComponent(factory.createExpressionPane(expression));
        this.addCenterComponent(new Label());
        this.revalidateAndRepaint();
      }
    }

    private final ExpressionAtIndexCascade cascade;
    private final MainComponent mainComponent;

    public ExpressionDropDown(ExpressionAtIndexCascade cascade, AstI18nFactory factory) {
      super(cascade.getRoot().getPopupPrepModel());
      this.cascade = cascade;
      this.mainComponent = new MainComponent(factory);
      this.setMainComponent(this.mainComponent);
      this.setMaximumSizeClampedToPreferredSize(true);
    }

    public ExpressionAtIndexCascade getCascade() {
      return this.cascade;
    }

    public void refreshInternalLater() {
      this.mainComponent.refreshLater();
    }
  }

  private static class ExpressionMutableList extends MutableList<Expression> {
    private final AbstractType<?, ?, ?> componentType;

    private class JExpressionItemAtIndexButton extends JItemAtIndexButton {
      private final JLabel prefixLabel = new JLabel() {
        @Override
        public Color getForeground() {
          return JExpressionItemAtIndexButton.this.getModel().isSelected() ? Color.WHITE : Color.BLACK;
        }
      };
      private final ExpressionDropDown expressionDropDown;

      public JExpressionItemAtIndexButton(int index, Action deleteAction) {
        this.prefixLabel.setText("[" + index + "]");
        FontUtilities.setFontToDerivedFont(this.prefixLabel, TextWeight.BOLD);

        ExpressionAtIndexCascade cascade = new ExpressionAtIndexCascade(ExpressionMutableList.this.getData(), index, ExpressionMutableList.this.componentType);
        this.expressionDropDown = new ExpressionDropDown(cascade, DialogAstI18nFactory.getInstance());

        this.setLayout(new MigLayout("insets 0", "[]8[]push[]"));
        this.add(this.prefixLabel);
        this.add(this.expressionDropDown.getAwtComponent());
        if (deleteAction != null) {
          JButton closeButton = new JCloseButton(true);
          closeButton.setAction(deleteAction);
          this.add(closeButton, "aligny top");
        }
      }

      @Override
      public void update() {
        this.expressionDropDown.refreshInternalLater();
      }
    }

    public ExpressionMutableList(MutableListData<Expression> data, AbstractType<?, ?, ?> componentType) {
      super((MutableListData<Expression>) data);
      this.componentType = componentType;
    }

    @Override
    protected JExpressionItemAtIndexButton createJItemAtIndexButton(int index) {
      DeleteItemAtIndexOperation<Expression> deleteOperation = new DeleteItemAtIndexOperation<Expression>(this.getData(), index);
      return new JExpressionItemAtIndexButton(index, deleteOperation.getImp().getSwingModel().getAction());
    }

    @Override
    protected void handleDisplayable() {
      super.handleDisplayable();
      this.registerKeyboardActions();
    }

    @Override
    protected void handleUndisplayable() {
      this.unregisterKeyboardActions();
      super.handleUndisplayable();
    }
  }

  private MouseListener mouseListener = new MouseListener() {
    @Override
    public void mousePressed(MouseEvent e) {
      expressionList.clearSelection();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
  };

  private final ExpressionMutableList expressionList;

  public ArrayCustomExpressionCreatorView(ArrayCustomExpressionCreatorComposite composite) {
    super(composite);
    this.expressionList = new ExpressionMutableList(composite.getData(), composite.getArrayType().getComponentType());
    this.expressionList.setBackgroundColor(this.getBackgroundColor());
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.addMouseListener(this.mouseListener);
  }

  @Override
  protected void handleUndisplayable() {
    this.removeMouseListener(this.mouseListener);
    super.handleUndisplayable();
  }

  @Override
  protected SwingComponentView<?> createMainComponent() {
    MigPanel rv = new MigPanel(null, "insets 0, fillx", "[align right, grow 0]8[fill]", "[]8[]0[]");
    ArrayCustomExpressionCreatorComposite composite = (ArrayCustomExpressionCreatorComposite) this.getComposite();

    ScrollPane scrollPane = new ScrollPane(this.expressionList);
    PopupButton popupButton = composite.getAddItemCascade().getRoot().getPopupPrepModel().createPopupButton();
    scrollPane.getAwtComponent().setMinimumSize(new Dimension(0, 0));
    popupButton.setMaximumSizeClampedToPreferredSize(true);

    rv.addComponent(composite.getArrayTypeLabel().createLabel());
    rv.addComponent(new Label(TypeIcon.getInstance(composite.getArrayType())), "wrap");

    AbstractLabel label = composite.getValueLabel().createLabel();
    label.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
    rv.addComponent(label, "aligny top, spany 2");
    rv.addComponent(scrollPane, "wrap");

    rv.addComponent(popupButton, "cell 1 2, grow 0, wrap");
    return rv;
  }
}
