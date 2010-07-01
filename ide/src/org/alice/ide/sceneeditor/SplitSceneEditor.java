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
	private edu.cmu.cs.dennisc.alice.ast.AbstractField root;
	public TreeModel( edu.cmu.cs.dennisc.alice.ast.AbstractField root ) {
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
		if( value instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)value;
			this.setText( field.getName() );
		}
		return rv;
	}
}

class LookingGlass extends edu.cmu.cs.dennisc.croquet.CornerSpringPanel {
	private edu.cmu.cs.dennisc.croquet.CheckBoxMenuItem isSceneEditorExpandedCheckBox; 
	public LookingGlass() {
		this.setBackgroundColor( java.awt.Color.RED );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		this.isSceneEditorExpandedCheckBox = ide.getIsSceneEditorExpandedState().createCheckBoxMenuItem();
		this.setSouthEastComponent( this.isSceneEditorExpandedCheckBox );
	}
}

class FieldTreeSelectionState extends edu.cmu.cs.dennisc.croquet.TreeSelectionState<edu.cmu.cs.dennisc.alice.ast.AbstractField> {
	public FieldTreeSelectionState() {
		super( org.alice.ide.IDE.IDE_GROUP, java.util.UUID.fromString( "89223cff-76d3-4cb7-baf3-3a5e990bcaff" ), null, null );
		throw new RuntimeException( "todo" );
	}
	@Override
	protected edu.cmu.cs.dennisc.javax.swing.models.TreeNode<edu.cmu.cs.dennisc.alice.ast.AbstractField> decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		throw new RuntimeException( "todo" );
	}
	@Override
	protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<edu.cmu.cs.dennisc.alice.ast.AbstractField> value) {
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
public class SplitSceneEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane root = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane();
	private FieldTreeSelectionState treeSelectionState = new FieldTreeSelectionState();
	private LookingGlass lookingGlass = new LookingGlass();
	private org.alice.app.ProjectApplication.ProjectObserver projectObserver = new org.alice.app.ProjectApplication.ProjectObserver() { 
		public void projectOpening( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
		}
		public void projectOpened( edu.cmu.cs.dennisc.alice.Project previousProject, edu.cmu.cs.dennisc.alice.Project nextProject ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> programType = nextProject.getProgramType();
			edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = programType.getDeclaredFields().get( 0 );
			treeSelectionState.setSwingTreeModel( new TreeModel( sceneField ) );
		}
	};

	public SplitSceneEditor() {
		edu.cmu.cs.dennisc.croquet.Tree< edu.cmu.cs.dennisc.alice.ast.AbstractField > tree = this.treeSelectionState.createTree();
		tree.setCellRenderer( new TreeCellRenderer() );
		this.root.setLeftComponent( new edu.cmu.cs.dennisc.croquet.ScrollPane( tree ) );
		this.root.setRightComponent( this.lookingGlass );
		this.addComponent( this.root, Constraint.CENTER );
	}
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		org.alice.app.ProjectApplication.getSingleton().addProjectObserver( this.projectObserver );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		org.alice.app.ProjectApplication.getSingleton().removeProjectObserver( this.projectObserver );
		super.handleRemovedFrom( parent );
	}
}
