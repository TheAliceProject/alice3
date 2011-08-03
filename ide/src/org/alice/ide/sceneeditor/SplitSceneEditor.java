/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.sceneeditor;

class TreeModel implements javax.swing.tree.TreeModel {
	private org.lgna.project.ast.AbstractField root;
	public TreeModel( org.lgna.project.ast.AbstractField root ) {
		this.root = root;
	}
	public Object getChild( Object parent, int index ) {
		return root.getValueType().getDeclaredFields().get( index );
	}
	public int getChildCount( Object parent ) {
		if( parent == root ) {
			return root.getValueType().getDeclaredFields().size();
		} else {
			return 0;
		}
	}
	public int getIndexOfChild( Object parent, Object child ) {
		return 0;
	}
	public Object getRoot() {
		return this.root;
	}
	public boolean isLeaf( Object node ) {
		return node != root;
	}
	public void addTreeModelListener( javax.swing.event.TreeModelListener l ) {
	}
	public void removeTreeModelListener( javax.swing.event.TreeModelListener l ) {
	}
	public void valueForPathChanged( javax.swing.tree.TreePath path, Object newValue ) {
	}
}

class TreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
	@Override
	public java.awt.Component getTreeCellRendererComponent( javax.swing.JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
		java.awt.Component rv = super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, row, hasFocus );
		if( value instanceof org.lgna.project.ast.AbstractField ) {
			org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)value;
			this.setText( field.getName() );
		}
		return rv;
	}
}

class LookingGlass extends org.lgna.croquet.components.CornerSpringPanel {
	public LookingGlass() {
		this.setBackgroundColor( java.awt.Color.RED );
		this.setSouthEastComponent( org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().createCheckBox() );
	}
}

class FieldTreeSelectionState extends org.lgna.croquet.TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<org.lgna.project.ast.AbstractField>> {
	public FieldTreeSelectionState() {
		super( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "89223cff-76d3-4cb7-baf3-3a5e990bcaff" ), new org.lgna.croquet.ItemCodec< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<org.lgna.project.ast.AbstractField> >() {
			public Class getValueClass() {
				return edu.cmu.cs.dennisc.javax.swing.models.TreeNode.class;
			}
			public edu.cmu.cs.dennisc.javax.swing.models.TreeNode< org.lgna.project.ast.AbstractField > decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
				throw new RuntimeException( "todo" );
			}
			public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.javax.swing.models.TreeNode< org.lgna.project.ast.AbstractField > value ) {
				throw new RuntimeException( "todo" );
			}
			public StringBuilder appendRepresentation( StringBuilder rv, edu.cmu.cs.dennisc.javax.swing.models.TreeNode< org.lgna.project.ast.AbstractField > value, java.util.Locale locale ) {
				throw new RuntimeException( "todo" );
			}
		}, null, null );
		throw new RuntimeException( "todo" );
	}
	@Deprecated
	public void setSwingTreeModel( javax.swing.tree.TreeModel swingTreeModel ) {
		throw new RuntimeException( "todo" );
	}
}
//todo
/**
 * @author Dennis Cosgrove
 */
public class SplitSceneEditor extends org.lgna.croquet.components.BorderPanel {
	private org.lgna.croquet.components.HorizontalSplitPane root = new org.lgna.croquet.components.HorizontalSplitPane();
	private FieldTreeSelectionState treeSelectionState = new FieldTreeSelectionState();
	private LookingGlass lookingGlass = new LookingGlass();
	private org.alice.ide.ProjectApplication.ProjectObserver projectObserver = new org.alice.ide.ProjectApplication.ProjectObserver() { 
		public void projectOpening( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
		}
		public void projectOpened( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
			org.lgna.project.ast.AbstractType<?,?,?> programType = nextProject.getProgramType();
			org.lgna.project.ast.AbstractField sceneField = programType.getDeclaredFields().get( 0 );
			treeSelectionState.setSwingTreeModel( new TreeModel( sceneField ) );
		}
	};

	public SplitSceneEditor() {
		org.lgna.croquet.components.Tree< edu.cmu.cs.dennisc.javax.swing.models.TreeNode< org.lgna.project.ast.AbstractField > > tree = this.treeSelectionState.createTree();
		tree.setCellRenderer( new TreeCellRenderer() );
		this.root.setLeftComponent( new org.lgna.croquet.components.ScrollPane( tree ) );
		this.root.setRightComponent( this.lookingGlass );
		this.addComponent( this.root, Constraint.CENTER );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.ProjectApplication.getActiveInstance().addProjectObserver( this.projectObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.ProjectApplication.getActiveInstance().removeProjectObserver( this.projectObserver );
		super.handleUndisplayable();
	}
}
