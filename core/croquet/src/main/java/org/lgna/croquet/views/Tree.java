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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class Tree<E> extends ViewController<javax.swing.JTree, org.lgna.croquet.SingleSelectTreeState<E>> {
	public Tree( org.lgna.croquet.SingleSelectTreeState<E> model ) {
		super( model );
		this.setSwingTreeModel( model.getTreeModel() );
		this.setSwingTreeSelectionModel( model.getSwingModel().getTreeSelectionModel() );
	}

	private void setSwingTreeModel( javax.swing.tree.TreeModel treeModel ) {
		this.getAwtComponent().setModel( treeModel );
	}

	private void setSwingTreeSelectionModel( javax.swing.tree.TreeSelectionModel treeSelectionModel ) {
		this.getAwtComponent().setSelectionModel( treeSelectionModel );
	}

	@Override
	protected javax.swing.JTree createAwtComponent() {
		return new javax.swing.JTree();
	}

	public javax.swing.tree.TreeCellRenderer getCellRenderer() {
		return this.getAwtComponent().getCellRenderer();
	}

	public void setCellRenderer( javax.swing.tree.TreeCellRenderer cellRenderer ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setCellRenderer( cellRenderer );
	}

	public void expandEachRowOnce() {
		this.checkEventDispatchThread();
		java.util.Set<E> alreadyExpanded = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();
		javax.swing.JTree jTree = this.getAwtComponent();
		for( int i = 0; i < jTree.getRowCount(); i++ ) {
			javax.swing.tree.TreePath treePath = jTree.getPathForRow( i );
			Object item = treePath.getLastPathComponent();
			if( alreadyExpanded.contains( item ) ) {
				jTree.collapsePath( treePath );
			} else {
				alreadyExpanded.add( (E)item );
				jTree.expandRow( i );
			}
		}
	}

	public void expandAllRows() {
		javax.swing.JTree jTree = this.getAwtComponent();
		for( int i = 0; i < jTree.getRowCount(); i++ ) {
			jTree.expandRow( i );
		}
	}

	public void collapseAllRows() {
		javax.swing.JTree jTree = this.getAwtComponent();
		for( int i = 0; i < jTree.getRowCount(); i++ ) {
			jTree.collapseRow( i );
		}
	}

	public void setRootVisible( boolean isRootVisible ) {
		this.getAwtComponent().setRootVisible( isRootVisible );
	}

	public void collapseNode( E node ) {
		javax.swing.tree.TreePath path = this.getModel().getTreeModel().getTreePath( node );
		this.getAwtComponent().collapsePath( path );
	}

	public void expandNode( E node ) {
		javax.swing.tree.TreePath path = this.getModel().getTreeModel().getTreePath( node );
		this.getAwtComponent().expandPath( path );
	}

	public void scrollPathToVisible( javax.swing.tree.TreePath treePath ) {
		final boolean IS_ALIGN_LEFT_DESIRED = true;
		if( IS_ALIGN_LEFT_DESIRED ) {
			if( treePath != null ) {
				this.getAwtComponent().makeVisible( treePath );
				java.awt.Rectangle bounds = this.getAwtComponent().getPathBounds( treePath );
				if( bounds != null ) {
					bounds.width += bounds.x;
					bounds.x = 0;
					this.getAwtComponent().scrollRectToVisible( bounds );
				}
			}
		} else {
			this.getAwtComponent().scrollPathToVisible( treePath );
		}
	}

	public javax.swing.tree.TreePath getSelectionPath() {
		return this.getAwtComponent().getSelectionPath();
	}
}
