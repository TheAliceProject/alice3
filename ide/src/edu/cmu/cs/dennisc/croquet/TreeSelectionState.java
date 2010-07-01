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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class TreeSelectionState<E> extends Model< TreeSelectionState<E> > {
	public static interface ValueObserver<E> {
		public void changed(E nextValue);
	};

	private java.util.List<ValueObserver<E>> valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver(ValueObserver<E> valueObserver) {
		this.valueObservers.add(valueObserver);
	}
	public void addAndInvokeValueObserver(ValueObserver<E> valueObserver) {
		this.addValueObserver(valueObserver);
		valueObserver.changed(this.getSelectedItem());
	}
	public void removeValueObserver(ValueObserver<E> valueObserver) {
		this.valueObservers.remove(valueObserver);
	}

	private void fireValueChanged(E nextValue) {
		for (ValueObserver<E> valueObserver : this.valueObservers) {
			valueObserver.changed(nextValue);
		}
	}

	private class SingleTreeSelectionModel extends javax.swing.tree.DefaultTreeSelectionModel {
	}
	private class TreeModel extends javax.swing.tree.DefaultTreeModel {
		public TreeModel( javax.swing.tree.TreeNode treeNode ) {
			super( treeNode );
		}
	}

	private SingleTreeSelectionModel treeSelectionModel;
	private TreeModel treeModel;
	public TreeSelectionState(Group group, java.util.UUID individualUUID, javax.swing.tree.TreeNode root, javax.swing.tree.TreeNode selectedNode ) {
		super(group, individualUUID);
		this.treeSelectionModel = new SingleTreeSelectionModel();
		this.treeModel = new TreeModel( root );
		this.setSelectedTreeNode( selectedNode );
	}
	protected abstract E decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder);
	protected abstract void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, E value);

	public E getSelectedItem() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getSelectedItem" );
		return null;
	}
	public void setSelectedItem(E selectedItem) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: setSelectedItem" );
	}
	
	/*package-private*/ void setSelectedTreeNode( javax.swing.tree.TreeNode treeNode ) {
		javax.swing.tree.TreeNode[] nodes = this.treeModel.getPathToRoot( treeNode );
		javax.swing.tree.TreePath path = new javax.swing.tree.TreePath( nodes );
		this.treeSelectionModel.setSelectionPath( path );
	}

	public Tree<E> createTree() {
		Tree<E> rv = new Tree<E>( this ) {
			// private ItemListener itemListener = new ItemListener( this );
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo(parent);
				TreeSelectionState.this.addComponent(this);
				// this.addItemListener( this.itemListener );
			};

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				// this.removeItemListener( this.itemListener );
				TreeSelectionState.this.removeComponent(this);
				super.handleRemovedFrom(parent);
			}
		};
		Application.getSingleton().register(this);
		rv.setSwingTreeModel(this.treeModel);
		rv.setSwingTreeSelectionModel( this.treeSelectionModel );
		return rv;
	}

	public PathControl<E> createPathControl( PathControl.Initializer initializer ) {
		PathControl<E> rv = new PathControl<E>( this, initializer );
		Application.getSingleton().register(this);
		rv.setSwingTreeModel( this.treeModel );
		rv.setSwingTreeSelectionModel( this.treeSelectionModel );
		return rv;
	}
	
//	public TrackableShape getTrackableShapeFor( E item ) {
//		ItemSelectable< ?, E > itemSelectable = this.getFirstComponent( ItemSelectable.class );
//		if( itemSelectable != null ) {
//			return itemSelectable.getTrackableShapeFor( item );
//		} else {
//			return null;
//		}
//	}
//	public JComponent< ? > getMainComponentFor( E item ) {
//		AbstractTabbedPane< E, ? > abstractTabbedPane = this.getFirstComponent( AbstractTabbedPane.class );
//		if( abstractTabbedPane != null ) {
//			return abstractTabbedPane.getMainComponentFor( item );
//		} else {
//			return null;
//		}
//	}
//	public ScrollPane getScrollPaneFor( E item ) {
//		AbstractTabbedPane< E, ? > abstractTabbedPane = this.getFirstComponent( AbstractTabbedPane.class );
//		if( abstractTabbedPane != null ) {
//			return abstractTabbedPane.getScrollPaneFor( item );
//		} else {
//			return null;
//		}
//	}
//	public JComponent< ? > getRootComponentFor( E item ) {
//		AbstractTabbedPane< E, ? > abstractTabbedPane = this.getFirstComponent( AbstractTabbedPane.class );
//		if( abstractTabbedPane != null ) {
//			return abstractTabbedPane.getRootComponentFor( item );
//		} else {
//			return null;
//		}
//	}

//	private javax.swing.Action action = new javax.swing.AbstractAction() {
//		public void actionPerformed(java.awt.event.ActionEvent e) {
//		}
//	};
//	
//	public String getName() {
//		return String.class.cast(this.action.getValue(javax.swing.Action.NAME));
//	}
//
//	public void setName(String name) {
//		this.action.putValue(javax.swing.Action.NAME, name);
//	}
//
//	public javax.swing.Icon getSmallIcon() {
//		return javax.swing.Icon.class.cast(this.action.getValue(javax.swing.Action.SMALL_ICON));
//	}
//
//	public void setSmallIcon(javax.swing.Icon icon) {
//		this.action.putValue(javax.swing.Action.SMALL_ICON, icon);
//	}
//
//	public int getMnemonicKey() {
//		return Integer.class.cast(this.action.getValue(javax.swing.Action.MNEMONIC_KEY));
//	}
//
//	public void setMnemonicKey(int mnemonicKey) {
//		this.action.putValue(javax.swing.Action.MNEMONIC_KEY, mnemonicKey);
//	}
//
//	public Menu<ListSelectionState<E>> createMenu() {
//		throw new RuntimeException( "todo: TreeSelectionOperation createMenu()");
//	}
}
