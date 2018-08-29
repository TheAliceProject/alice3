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
package org.lgna.croquet;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.javax.swing.models.TreeModel;
import edu.cmu.cs.dennisc.map.MapToMap;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.croquet.triggers.TreeSelectionEventTrigger;
import org.lgna.croquet.views.Tree;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

class TreeNodeUtilities {
	private TreeNodeUtilities() {
		throw new AssertionError();
	}

	public static <T> void updateBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<T> blankNode, SingleSelectTreeState<T> model, T node ) {
		for( T childNode : model.getChildren( node ) ) {
			CascadeBlankChild<T> child = model.getBlankChildFor( childNode );
			if( child != null ) {
				blankChildren.add( child );
			}
		}
	}

}

class TreeNodeFillIn<T> extends ImmutableCascadeFillIn<T, Void> {
	private static MapToMap<SingleSelectTreeState, Object, TreeNodeFillIn> mapToMap = MapToMap.newInstance();

	public static synchronized <T> TreeNodeFillIn<T> getInstance( SingleSelectTreeState<T> model, T node ) {
		return mapToMap.getInitializingIfAbsent( model, node, new MapToMap.Initializer<SingleSelectTreeState, Object, TreeNodeFillIn>() {
			@Override
			public TreeNodeFillIn<T> initialize( SingleSelectTreeState model, Object node ) {
				return new TreeNodeFillIn<T>( model, (T)node );
			}
		} );
	}

	private final SingleSelectTreeState<T> model;
	private final T node;

	private TreeNodeFillIn( SingleSelectTreeState<T> model, T node ) {
		super( UUID.fromString( "db052fcb-b0e3-482a-aad9-13b9a2efc370" ) );
		this.model = model;
		this.node = node;
	}

	@Override
	public T getTransientValue( ItemNode<? super T, Void> node ) {
		return this.node;
	}

	@Override
	public T createValue( ItemNode<? super T, Void> node ) {
		return this.node;
	}

	@Override
	public String getMenuItemText( ItemNode<? super T, Void> node ) {
		return this.model.getTextForNode( this.node );
	}

	@Override
	public Icon getMenuItemIcon( ItemNode<? super T, Void> node ) {
		return this.model.getIconForNode( this.node );
	}

	@Override
	protected JComponent createMenuItemIconProxy( ItemNode<? super T, Void> node ) {
		throw new AssertionError();
	}
}

class TreeNodeMenu<T> extends CascadeMenuModel<T> {
	private static MapToMap<SingleSelectTreeState, Object, TreeNodeMenu> mapToMap = MapToMap.newInstance();

	public static synchronized <T> TreeNodeMenu<T> getInstance( SingleSelectTreeState<T> model, T node ) {
		return mapToMap.getInitializingIfAbsent( model, node, new MapToMap.Initializer<SingleSelectTreeState, Object, TreeNodeMenu>() {
			@Override
			public TreeNodeMenu<T> initialize( SingleSelectTreeState model, Object node ) {
				return new TreeNodeMenu<T>( model, (T)node );
			}
		} );
	}

	private final SingleSelectTreeState<T> model;
	private final T node;

	private TreeNodeMenu( SingleSelectTreeState<T> model, T node ) {
		super( UUID.fromString( "3836e893-73c9-4490-9a2a-1cb8a50311e0" ) );
		this.model = model;
		this.node = node;
	}

	@Override
	protected String findDefaultLocalizedText() {
		return null;
	}

	@Override
	protected void updateBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<T> blankNode ) {
		TreeNodeUtilities.updateBlankChildren( blankChildren, blankNode, this.model, this.node );
	}

}

class TreeBlank<T> extends CascadeBlank<T> {
	private final SingleSelectTreeState<T> model;
	private final T node;

	public TreeBlank( SingleSelectTreeState<T> model, T node ) {
		super( UUID.fromString( "7c277038-5aa8-4429-9642-8dc3890aee5b" ) );
		this.model = model;
		this.node = node;
	}

	@Override
	protected void updateChildren( List<CascadeBlankChild> children, BlankNode<T> blankNode ) {
		TreeNodeUtilities.updateBlankChildren( children, blankNode, this.model, this.node );
	}
}

class TreeNodeCascade<T> extends ImmutableCascade<T> {
	private static MapToMap<SingleSelectTreeState, Object, TreeNodeCascade> mapToMap = MapToMap.newInstance();

	public static <T> TreeNodeCascade<T> getInstance( SingleSelectTreeState<T> model, T node ) {
		return mapToMap.getInitializingIfAbsent( model, node, new MapToMap.Initializer<SingleSelectTreeState, Object, TreeNodeCascade>() {
			@Override
			public TreeNodeCascade<T> initialize( SingleSelectTreeState model, Object node ) {
				return new TreeNodeCascade<T>( model, (T)node );
			}
		} );
	}

	private final SingleSelectTreeState<T> model;
	private final T node;

	public TreeNodeCascade( SingleSelectTreeState<T> model, T node ) {
		super( model.getGroup(), UUID.fromString( "dcbf42f7-cd25-4061-a8f6-abcb1b47fe41" ), model.getItemCodec().getValueClass(), new TreeBlank<T>( model, node ) );
		this.model = model;
		this.node = node;
	}

	@Override
	protected Edit createEdit( UserActivity userActivity, T[] values ) {
		assert values.length == 1;
		this.model.changeValueFromIndirectModel( values[ 0 ], State.IsAdjusting.FALSE, NullTrigger.createUserInstance() );
		return null;
		//return new org.lgna.croquet.edits.TreeSelectionStateEdit< T >( completionStep, this.model, this.model.getSelectedNode(), values[ 0 ] );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class SingleSelectTreeState<T> extends ItemState<T> {
	private class SingleTreeSelectionModel extends DefaultTreeSelectionModel {
		public SingleTreeSelectionModel() {
			this.setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		}
	}

	public class SwingModel {
		private final SingleTreeSelectionModel treeSelectionModel = new SingleTreeSelectionModel();

		private SwingModel() {
		}

		public TreeSelectionModel getTreeSelectionModel() {
			return this.treeSelectionModel;
		}
	}

	private final SwingModel swingModel = new SwingModel();
	private final TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
		@Override
		public void valueChanged( TreeSelectionEvent e ) {
			T nextValue = getSelectedNode();
			SingleSelectTreeState.this.changeValueFromSwing( nextValue, IsAdjusting.FALSE, TreeSelectionEventTrigger.createUserInstance( e ) );
			//			T prevValue = getValue();
			//			fireChanged( prevValue, nextValue, IsAdjusting.FALSE );
		}
	};

	public SingleSelectTreeState( Group group, UUID id, T initialValue, ItemCodec<T> itemCodec ) {
		super( group, id, initialValue, itemCodec );
		this.swingModel.treeSelectionModel.addTreeSelectionListener( this.treeSelectionListener );
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Collections.emptyList();
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	@Override
	protected void localize() {
	}

	protected abstract String getTextForNode( T node );

	protected abstract Icon getIconForNode( T node );

	public abstract TreeModel<T> getTreeModel();

	public abstract void refresh( T node );

	public final void refreshAll() {
		this.refresh( this.getTreeModel().getRoot() );
	}

	public T getSelectedNode() {
		TreePath path = this.swingModel.treeSelectionModel.getSelectionPath();
		if( path != null ) {
			return (T)path.getLastPathComponent();
		} else {
			return null;
		}
	}

	private void setSelectedNode( T e ) {
		TreePath currTreePath = this.swingModel.treeSelectionModel.getSelectionPath();
		TreePath nextTreePath = this.getTreeModel().getTreePath( e );
		if( Objects.equals( currTreePath, nextTreePath ) ) {
			//pass
		} else {
			this.swingModel.treeSelectionModel.setSelectionPath( nextTreePath );
		}
	}

	@Override
	protected T getSwingValue() {
		return this.getSelectedNode();
	}

	@Override
	protected void setSwingValue( T nextValue ) {
		this.setSelectedNode( nextValue );
	}

	public List<T> getChildren( T node ) {
		TreeModel<T> treeModel = this.getTreeModel();
		final int N = treeModel.getChildCount( node );
		List<T> rv = Lists.newArrayListWithInitialCapacity( N );
		for( int i = 0; i < N; i++ ) {
			rv.add( treeModel.getChild( node, i ) );
		}
		return rv;
	}

	public boolean isLeaf( T node ) {
		TreeModel<T> treeModel = this.getTreeModel();
		return treeModel.isLeaf( node );
	}

	public T getParent( T node ) {
		TreeModel<T> treeModel = this.getTreeModel();
		TreePath treePath = treeModel.getTreePath( node );
		return (T)treePath.getParentPath().getLastPathComponent();
	}

	protected boolean isComboDesired( T childNode ) {
		return this.isLeaf( childNode ) == false;
	}

	protected CascadeBlankChild<T> getBlankChildFor( T childNode ) {
		CascadeBlankChild<T> blankChild;
		TreeNodeFillIn<T> fillIn = TreeNodeFillIn.getInstance( this, childNode );
		if( this.isComboDesired( childNode ) ) {
			blankChild = new CascadeItemMenuCombo<T>( fillIn, TreeNodeMenu.getInstance( this, childNode ) );
		} else {
			blankChild = fillIn;
		}
		return blankChild;
	}

	public Cascade<T> getCascadeFor( T node ) {
		return TreeNodeCascade.getInstance( this, node );
	}

	public Tree<T> createTree() {
		return new Tree<T>( this );
	}
}
