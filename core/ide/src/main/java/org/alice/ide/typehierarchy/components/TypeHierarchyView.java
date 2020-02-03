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

package org.alice.ide.typehierarchy.components;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.models.AbstractTreeModel;
import edu.cmu.cs.dennisc.javax.swing.renderers.TreeCellRenderer;
import edu.cmu.cs.dennisc.tree.Node;
import org.alice.ide.IDE;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.ast.AstEventManager;
import org.alice.ide.common.TypeIcon;
import org.alice.ide.declarationseditor.TypeComposite;
import org.alice.ide.typehierarchy.TypeHierarchyComposite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.SwingAdapter;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.NamedUserType;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class NamedUserTypeTreeModel extends AbstractTreeModel<Node<NamedUserType>> {
  @Override
  public Node<NamedUserType> getChild(Object parent, int index) {
    Node<NamedUserType> node = (Node<NamedUserType>) parent;
    return node.getChildren().get(index);
  }

  @Override
  public int getChildCount(Object parent) {
    Node<NamedUserType> node = (Node<NamedUserType>) parent;
    return node.getChildren().size();
  }

  @Override
  public int getIndexOfChild(Object parent, Object child) {
    Node<NamedUserType> node = (Node<NamedUserType>) parent;
    return node.getChildren().indexOf(child);
  }

  @Override
  public Node<NamedUserType> getRoot() {
    IDE ide = IDE.getActiveInstance();
    if (ide != null) {
      return ide.getApiConfigurationManager().getNamedUserTypesAsTreeFilteredForSelection();
    } else {
      return null;
    }
  }

  @Override
  public TreePath getTreePath(Node<NamedUserType> e) {
    //todo
    return null;
  }

  @Override
  public boolean isLeaf(Object node) {
    return this.getChildCount(node) == 0;
  }

  public void refresh() {
    if (this.getRoot() != null) {
      this.fireTreeStructureChanged(this, new Object[] {this.getRoot()}, null, null);
    }
  }
}

class NamedUserTypeTreeCellRenderer extends TreeCellRenderer<Node<NamedUserType>> {

  @Override
  protected JLabel updateListCellRendererComponent(JLabel rv, JTree tree, Node<NamedUserType> node, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    rv.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
    rv.setOpaque(false);
    rv.setIcon(TypeIcon.getInstance(node != null ? node.getValue() : null));
    rv.setText("");
    return rv;
  }
}

/**
 * @author Dennis Cosgrove
 */
public class TypeHierarchyView extends BorderPanel {
  private final ValueListener<NamedUserType> typeListener = new ValueListener<NamedUserType>() {
    @Override
    public void valueChanged(ValueEvent<NamedUserType> e) {
      TypeHierarchyView.this.handleTypeStateChanged(e.getNextValue());
    }
  };
  private final AstEventManager.TypeHierarchyListener typeHierarchyListener = new AstEventManager.TypeHierarchyListener() {
    @Override
    public void typeHierarchyHasPotentiallyChanged() {
      TypeHierarchyView.this.refreshLater();
    }
  };

  private final NamedUserTypeTreeModel treeModel = new NamedUserTypeTreeModel();
  private final KeyListener keyListener = new KeyListener() {
    @Override
    public void keyPressed(KeyEvent e) {
      TypeHierarchyView.this.refreshLater();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
  };

  private boolean isIgnoringChangesToTree = false;
  private final TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
    @Override
    public void valueChanged(TreeSelectionEvent e) {
      TreePath treePath = jTree.getSelectionPath();
      if (treePath != null) {
        if (isIgnoringChangesToTree) {
          //pass
        } else {
          Object last = treePath.getLastPathComponent();
          if (last instanceof Node) {
            Node<NamedUserType> node = (Node<NamedUserType>) last;
            IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().setValueTransactionlessly(TypeComposite.getInstance(node.getValue()));
          }
        }
        jTree.repaint();
      }
    }
  };
  private final JTree jTree;

  public TypeHierarchyView(TypeHierarchyComposite composite) {
    super(composite, 0, 4);
    Color color = ThemeUtilities.getActiveTheme().getMutedTypeColor();
    this.jTree = new JTree(this.treeModel);
    this.jTree.setRootVisible(false);
    this.jTree.setCellRenderer(new NamedUserTypeTreeCellRenderer());
    this.jTree.setBackground(color);

    SwingComponentView<?> viewportView = new SwingAdapter(this.jTree);
    ScrollPane scrollPane = new ScrollPane(viewportView);
    String hierarchyText = ResourceBundleUtilities.getStringForKey("typeHierarchy", getClass());
    Label label = new Label(hierarchyText, 1.2f, TextPosture.OBLIQUE, TextWeight.LIGHT);
    this.setBackgroundColor(color);
    this.addPageStartComponent(label);
    this.addCenterComponent(scrollPane);
  }

  private void handleTypeStateChanged(NamedUserType nextValue) {
    this.refreshLater();
  }

  @Override
  public void handleCompositePreActivation() {
    super.handleCompositePreActivation();
    this.refreshIfNecessary();
    AstEventManager.addAndInvokeTypeHierarchyListener(this.typeHierarchyListener);
    IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addAndInvokeValueListener(this.typeListener);
    this.jTree.addKeyListener(this.keyListener);
    this.jTree.addTreeSelectionListener(this.treeSelectionListener);
  }

  @Override
  public void handleCompositePostDeactivation() {
    this.jTree.removeTreeSelectionListener(this.treeSelectionListener);
    this.jTree.removeKeyListener(this.keyListener);
    IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().removeValueListener(this.typeListener);
    AstEventManager.removeTypeHierarchyListener(this.typeHierarchyListener);
    super.handleCompositePostDeactivation();
  }

  @Override
  protected void internalRefresh() {
    super.internalRefresh();
    this.isIgnoringChangesToTree = true;
    try {
      this.treeModel.refresh();
      for (int i = 0; i < this.jTree.getRowCount(); i++) {
        this.jTree.expandRow(i);
        TreePath treePath = this.jTree.getPathForRow(i);
        Node<NamedUserType> lastNode = (Node<NamedUserType>) treePath.getLastPathComponent();
        if (lastNode.getValue() == IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue()) {
          this.jTree.setSelectionRow(i);
        }
      }
      this.jTree.repaint();
    } finally {
      this.isIgnoringChangesToTree = false;
    }
  }
}
