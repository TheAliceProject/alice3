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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class TreeSelectionState<T> extends ItemState<T> {
	public static interface SelectionObserver<E> {
		public void selectionChanged(E nextValue);
	};
	private class SingleTreeSelectionModel extends javax.swing.tree.DefaultTreeSelectionModel {
	}
	private final SingleTreeSelectionModel treeSelectionModel;
	public TreeSelectionState(Group group, java.util.UUID id, ItemCodec< T > itemCodec ) {
		super(group, id, itemCodec);
		this.treeSelectionModel = new SingleTreeSelectionModel();
		this.treeSelectionModel.addTreeSelectionListener( new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
				T prevValue = getValue();
				T nextValue = getSelection();
				System.err.println( "changing from " + prevValue + " to " + nextValue );
				fireChanged( prevValue, nextValue, false );
			}
		} );
	}

	@Override
	protected void localize() {
	}
	public abstract edu.cmu.cs.dennisc.javax.swing.models.TreeModel<T> getTreeModel();
	public javax.swing.tree.TreeSelectionModel getTreeSelectionModel() {
		return this.treeSelectionModel;
	}
	public T getSelection() {
		javax.swing.tree.TreePath path = this.treeSelectionModel.getSelectionPath();
		if( path != null ) {
			return (T)path.getLastPathComponent();
		} else {
			return null;
		}
	}
	public void setSelection( T e ) {
		this.treeSelectionModel.setSelectionPath( this.getTreeModel().getTreePath( e ) );
	}

	@Override
	public T getValue() {
		return this.getSelection();
	}
	
	public org.lgna.croquet.components.Tree<T> createTree() {
		return new org.lgna.croquet.components.Tree<T>( this );
	}

	public java.util.List< T > getChildren( T node ) {
		edu.cmu.cs.dennisc.javax.swing.models.TreeModel< T > treeModel = this.getTreeModel();
		final int N = treeModel.getChildCount( node );
		java.util.List< T > rv = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( N );
		for( int i=0; i<N; i++ ) {
			rv.add( treeModel.getChild( node, i ) );
		}
		return rv;
	}
	
	public ActionOperation getSelectionOperationFor( T node ) {
		return TreeNodeSelectionOperation.getInstance( this, node );
	}
	
	public java.util.List< T > getChildrenOfSelectedValue() {
		return this.getChildren( this.getValue() );
	}
	
	public org.lgna.croquet.components.PathControl createPathControl( org.lgna.croquet.components.PathControl.Initializer initializer ) {
		assert initializer != null;
		return new org.lgna.croquet.components.PathControl( (TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>>)this, initializer );
	}
}
