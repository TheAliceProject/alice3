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

class NamedUserTypeTreeModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractTreeModel<edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>> {
	@Override
	public edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> getChild( Object parent, int index ) {
		edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> node = (edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>)parent;
		return node.getChildren().get( index );
	}

	@Override
	public int getChildCount( Object parent ) {
		edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> node = (edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>)parent;
		return node.getChildren().size();
	}

	@Override
	public int getIndexOfChild( Object parent, Object child ) {
		edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> node = (edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>)parent;
		return node.getChildren().indexOf( child );
	}

	@Override
	public edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> getRoot() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			return ide.getApiConfigurationManager().getNamedUserTypesAsTreeFilteredForSelection();
		} else {
			return null;
		}
	}

	@Override
	public javax.swing.tree.TreePath getTreePath( edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> e ) {
		//todo
		return null;
	}

	@Override
	public boolean isLeaf( Object node ) {
		return this.getChildCount( node ) == 0;
	}

	public void refresh() {
		if( this.getRoot() != null ) {
			this.fireTreeStructureChanged( this, new Object[] { this.getRoot() }, null, null );
		}
	}
}

class NamedUserTypeTreeCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.TreeCellRenderer<edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>> {

	@Override
	protected javax.swing.JLabel updateListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTree tree, edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> node, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 0, 2, 0 ) );
		rv.setOpaque( false );
		rv.setIcon( org.alice.ide.common.TypeIcon.getInstance( node != null ? node.getValue() : null ) );
		rv.setText( "" );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class TypeHierarchyView extends org.lgna.croquet.views.BorderPanel {
	private final org.lgna.croquet.event.ValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.NamedUserType>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.NamedUserType> e ) {
			TypeHierarchyView.this.handleTypeStateChanged( e.getNextValue() );
		}
	};
	private final org.alice.ide.ast.AstEventManager.TypeHierarchyListener typeHierarchyListener = new org.alice.ide.ast.AstEventManager.TypeHierarchyListener() {
		@Override
		public void typeHierarchyHasPotentiallyChanged() {
			TypeHierarchyView.this.refreshLater();
		}
	};

	private final NamedUserTypeTreeModel treeModel = new NamedUserTypeTreeModel();
	private final java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
		@Override
		public void keyPressed( java.awt.event.KeyEvent e ) {
			TypeHierarchyView.this.refreshLater();
		}

		@Override
		public void keyReleased( java.awt.event.KeyEvent e ) {
		}

		@Override
		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};

	private boolean isIgnoringChangesToTree = false;
	private final javax.swing.event.TreeSelectionListener treeSelectionListener = new javax.swing.event.TreeSelectionListener() {
		@Override
		public void valueChanged( javax.swing.event.TreeSelectionEvent e ) {
			javax.swing.tree.TreePath treePath = jTree.getSelectionPath();
			if( treePath != null ) {
				if( isIgnoringChangesToTree ) {
					//pass
				} else {
					Object last = treePath.getLastPathComponent();
					if( last instanceof edu.cmu.cs.dennisc.tree.Node ) {
						edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> node = (edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>)last;
						org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().setValueTransactionlessly( org.alice.ide.declarationseditor.TypeComposite.getInstance( node.getValue() ) );
					}
				}
				jTree.repaint();
			}
		}
	};
	private final javax.swing.JTree jTree;

	public TypeHierarchyView( org.alice.ide.typehierarchy.TypeHierarchyComposite composite ) {
		super( composite, 0, 4 );
		java.awt.Color color = org.alice.ide.ThemeUtilities.getActiveTheme().getMutedTypeColor();
		this.jTree = new javax.swing.JTree( this.treeModel );
		this.jTree.setRootVisible( false );
		this.jTree.setCellRenderer( new NamedUserTypeTreeCellRenderer() );
		this.jTree.setBackground( color );

		org.lgna.croquet.views.SwingComponentView<?> viewportView = new org.lgna.croquet.views.SwingAdapter( this.jTree );
		org.lgna.croquet.views.ScrollPane scrollPane = new org.lgna.croquet.views.ScrollPane( viewportView );
		org.lgna.croquet.views.Label label = new org.lgna.croquet.views.Label( "type hierarchy", 1.2f, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
		this.setBackgroundColor( color );
		this.addPageStartComponent( label );
		this.addCenterComponent( scrollPane );
	}

	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		this.refreshLater();
	}

	@Override
	public void handleCompositePreActivation() {
		super.handleCompositePreActivation();
		this.refreshIfNecessary();
		org.alice.ide.ast.AstEventManager.addAndInvokeTypeHierarchyListener( this.typeHierarchyListener );
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().addAndInvokeValueListener( this.typeListener );
		this.jTree.addKeyListener( this.keyListener );
		this.jTree.addTreeSelectionListener( this.treeSelectionListener );
	}

	@Override
	public void handleCompositePostDeactivation() {
		this.jTree.removeTreeSelectionListener( this.treeSelectionListener );
		this.jTree.removeKeyListener( this.keyListener );
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().removeValueListener( this.typeListener );
		org.alice.ide.ast.AstEventManager.removeTypeHierarchyListener( this.typeHierarchyListener );
		super.handleCompositePostDeactivation();
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		this.isIgnoringChangesToTree = true;
		try {
			this.treeModel.refresh();
			for( int i = 0; i < this.jTree.getRowCount(); i++ ) {
				this.jTree.expandRow( i );
				javax.swing.tree.TreePath treePath = this.jTree.getPathForRow( i );
				edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> lastNode = (edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType>)treePath.getLastPathComponent();
				if( lastNode.getValue() == org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue() ) {
					this.jTree.setSelectionRow( i );
				}
			}
			this.jTree.repaint();
		} finally {
			this.isIgnoringChangesToTree = false;
		}
	}
}
