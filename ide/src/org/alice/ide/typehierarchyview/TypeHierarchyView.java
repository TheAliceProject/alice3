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

package org.alice.ide.typehierarchyview;

//class TreeNodeAdpater<T> implements edu.cmu.cs.dennisc.javax.swing.models.TreeNode< edu.cmu.cs.dennisc.tree.Node< T > > {
//	private final edu.cmu.cs.dennisc.tree.Node< T > value;
//	public TreeNodeAdpater( edu.cmu.cs.dennisc.tree.Node< T > value ) {
//		this.value = value;
//	}
//	public java.util.Enumeration<T> children() {
//		return null;
//	}
//	public boolean getAllowsChildren() {
//		//return this.value.getChildren().size() > 0;
//		return true; //?
//	}
//	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode< edu.cmu.cs.dennisc.tree.Node< T > > getChildAt( int childIndex ) {
//		return null;
//	}
//	public int getChildCount() {
//		return this.value.getChildren().size();
//	}
//	public int getIndex( javax.swing.tree.TreeNode node ) {
//		return this.value.getChildren().indexOf( (TreeNodeAdpater<T>).value );
//	}
//	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode< edu.cmu.cs.dennisc.tree.Node< T > > getParent() {
//		return null;
//	}
//	public edu.cmu.cs.dennisc.tree.Node< T > getValue() {
//		return this.value;
//	}
//	public boolean isLeaf() {
//		return this.value.getChildren().size() > 0;
//	}
//	public java.util.Iterator< edu.cmu.cs.dennisc.javax.swing.models.TreeNode< edu.cmu.cs.dennisc.tree.Node< T > >> iterator() {
//		return null;
//	}
//}
//
//
//class NamedUserTypeTreeNodeAdapter extends TreeNodeAdpater< org.lgna.project.ast.NamedUserType > {
//	public NamedUserTypeTreeNodeAdapter( edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > value ) {
//		super( value );
//	}
//}
//
//class RootNamedUserTypeNode implements edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > {
//	public RootNamedUserTypeNode() {
//	}
//	public boolean contains( org.lgna.project.ast.NamedUserType value ) {
//		return false;
//	}
//	public edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > get( org.lgna.project.ast.NamedUserType value ) {
//		return null;
//	}
//	public java.lang.Iterable< ? extends edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType >> getChildren() {
//		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
//		org.lgna.project.Project project = ide.getProject();
//		edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > tree = org.lgna.project.project.ProjectUtilities.getNamedUserTypesAsTree( project );
//		return tree.getChildren();
//	}
//	public org.lgna.project.ast.NamedUserType getValue() {
//		return null;
//	}
//}

//class NamedUserTypeTreeModel implements edu.cmu.cs.dennisc.javax.swing.models.TreeModel< org.lgna.project.ast.NamedUserType > {
//}
class NamedUserTypeTreeModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractTreeModel< edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > > {
	public edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > getChild( java.lang.Object parent, int index ) {
		edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > node = (edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType >)parent;
		return node.getChildren().get( index );
	}
	public int getChildCount( java.lang.Object parent ) {
		edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > node = (edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType >)parent;
		return node.getChildren().size();
	}
	public int getIndexOfChild( java.lang.Object parent, java.lang.Object child ) {
		edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > node = (edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType >)parent;
		return node.getChildren().indexOf( child );
	}
	public edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > getRoot() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project = ide.getProject();
		if( project != null ) {
			edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > tree = org.lgna.project.project.ProjectUtilities.getNamedUserTypesAsTree( project );
			return tree;
		} else {
			return null;
		}
	}
	public javax.swing.tree.TreePath getTreePath( edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > e ) {
		return null;
	}
	public boolean isLeaf( java.lang.Object node ) {
		return this.getChildCount( node ) == 0;
	}
	public void refresh() {
		System.err.println( "refresh" );
		//this.fireTreeNodeChanged( source, path, childIndices, children );
	}
}
/**
 * @author Dennis Cosgrove
 */
public class TypeHierarchyView extends org.lgna.croquet.components.BorderPanel {
	private final NamedUserTypeTreeModel treeModel = new NamedUserTypeTreeModel();
	public TypeHierarchyView() {
		javax.swing.JTree jTree = new javax.swing.JTree( this.treeModel );
		//jTree.setRootVisible( false );
		org.lgna.croquet.components.Component< ? > viewportView = new org.lgna.croquet.components.SwingAdapter( jTree );
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( viewportView );
		this.addComponent( scrollPane, Constraint.CENTER );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.treeModel.refresh();
	}
}
