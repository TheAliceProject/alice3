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

class TreeNodeUtilities {
	private TreeNodeUtilities() {
		throw new AssertionError();
	}
	public static <T> java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< T > blankNode, TreeSelectionState< T > model, T node ) {
		for( T childNode : model.getChildren( node ) ) {
			rv.add( model.getBlankChildFor( childNode ) );
		}
		return rv;
	}

}

class TreeNodeFillIn<T> extends CascadeFillIn< T, Void > {
	private static edu.cmu.cs.dennisc.map.MapToMap< TreeSelectionState, Object, TreeNodeFillIn > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized <T> TreeNodeFillIn< T > getInstance( TreeSelectionState< T > model, T node ) {
		TreeNodeFillIn< T > rv = mapToMap.get( model, node );
		if( rv != null ) {
			//pass
		} else {
			rv = new TreeNodeFillIn< T >( model, node );
			mapToMap.put( model, node, rv );
		}
		return rv;
	}

	private final TreeSelectionState< T > model;
	private final T node;

	private TreeNodeFillIn( TreeSelectionState< T > model, T node ) {
		super( java.util.UUID.fromString( "db052fcb-b0e3-482a-aad9-13b9a2efc370" ) );
		this.model = model;
		this.node = node;
	}
	@Override
	public T getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super T, Void > step ) {
		return this.node;
	}
	@Override
	public T createValue( org.lgna.croquet.cascade.ItemNode< ? super T, Void > step ) {
		return this.node;
	}
	@Override
	public String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super T, Void > step ) {
		return this.model.getTextForNode( this.node );
	}
	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super T, Void > step ) {
		return this.model.getIconForNode( this.node );
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super T, Void > step ) {
		throw new AssertionError();
	}
	@Override
	protected String getTutorialItemText() {
		return this.node.toString();
	}
}

class TreeNodeMenu<T> extends CascadeMenuModel< T > {
	private static edu.cmu.cs.dennisc.map.MapToMap< TreeSelectionState, Object, TreeNodeMenu > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized <T> TreeNodeMenu< T > getInstance( TreeSelectionState< T > model, T node ) {
		TreeNodeMenu< T > rv = mapToMap.get( model, node );
		if( rv != null ) {
			//pass
		} else {
			rv = new TreeNodeMenu< T >( model, node );
			mapToMap.put( model, node, rv );
		}
		return rv;
	}

	private final TreeSelectionState< T > model;
	private final T node;

	private TreeNodeMenu( TreeSelectionState< T > model, T node ) {
		super( java.util.UUID.fromString( "3836e893-73c9-4490-9a2a-1cb8a50311e0" ) );
		this.model = model;
		this.node = node;
	}
	@Override
	protected String getDefaultLocalizedText() {
		return null;
	}
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
		return TreeNodeUtilities.updateBlankChildren( rv, blankNode, this.model, this.node );
	}

}

class TreeBlank<T> extends CascadeBlank< T > {
	private final TreeSelectionState< T > model;
	private final T node;

	public TreeBlank( TreeSelectionState< T > model, T node ) {
		super( java.util.UUID.fromString( "7c277038-5aa8-4429-9642-8dc3890aee5b" ) );
		this.model = model;
		this.node = node;
	}
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< T > blankNode ) {
		return TreeNodeUtilities.updateBlankChildren( rv, blankNode, this.model, this.node );
	}
}

class TreeNodeCascade<T> extends Cascade< T > {
	private static edu.cmu.cs.dennisc.map.MapToMap< TreeSelectionState, Object, TreeNodeCascade > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static <T> TreeNodeCascade< T > getInstance( TreeSelectionState< T > model, T node ) {
		synchronized( mapToMap ) {
			TreeNodeCascade< T > rv = mapToMap.get( model, node );
			if( rv != null ) {
				//pass
			} else {
				rv = new TreeNodeCascade< T >( model, node );
				mapToMap.put( model, node, rv );
			}
			return rv;
		}
	}

	private final TreeSelectionState< T > model;
	private final T node;

	public TreeNodeCascade( TreeSelectionState< T > model, T node ) {
		super( model.getGroup(), java.util.UUID.fromString( "dcbf42f7-cd25-4061-a8f6-abcb1b47fe41" ), model.getItemCodec().getValueClass(), new TreeBlank< T >( model, node ) );
		this.model = model;
		this.node = node;
	}
	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CascadeCompletionStep< T > completionStep, T[] values ) {
		assert values.length == 1;
		return new org.lgna.croquet.edits.TreeSelectionStateEdit< T >( completionStep, this.model, this.model.getSelectedNode(), values[ 0 ] );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class TreeSelectionState<T> extends ItemState< T > {
	private static final class InternalTreeNodeSelectionOperation<T> extends ActionOperation {
		private static edu.cmu.cs.dennisc.map.MapToMap< TreeSelectionState, Object, InternalTreeNodeSelectionOperation > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
		/*package-private*/ static <T> InternalTreeNodeSelectionOperation<T> getInstance( TreeSelectionState<T> treeSelectionState, T treeNode ) {
			InternalTreeNodeSelectionOperation<T> rv = mapToMap.get(treeSelectionState, treeNode);
			if( rv != null ) {
				//pass
			} else {
				rv = new InternalTreeNodeSelectionOperation<T>(treeSelectionState, treeNode);
				mapToMap.put( treeSelectionState, treeNode, rv );
			}
			return rv;
		}

		private final TreeSelectionState<T> treeSelectionState;
		private final T treeNode;
		
		private InternalTreeNodeSelectionOperation( TreeSelectionState<T> treeSelectionState, T treeNode ) {
			super( Application.INHERIT_GROUP, java.util.UUID.fromString( "ca407baf-13b1-4530-bf35-67764efbf5f0" ) );
			this.treeSelectionState = treeSelectionState;
			this.treeNode = treeNode;
		}

		@Override
		protected void localize() {
			super.localize();
			this.setName( this.treeSelectionState.getTextForNode( this.treeNode ) );
			this.setSmallIcon( this.treeSelectionState.getIconForNode( this.treeNode ) );
		}
		@Override
		protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
			//todo: create edit
			this.treeSelectionState.setSelectedNode( this.treeNode );
			step.finish();
		}
	}

	private class SingleTreeSelectionModel extends javax.swing.tree.DefaultTreeSelectionModel {
	}
	public class SwingModel {
		private final SingleTreeSelectionModel treeSelectionModel = new SingleTreeSelectionModel();
		private SwingModel() {
		}
		public javax.swing.tree.TreeSelectionModel getTreeSelectionModel() {
			return this.treeSelectionModel;
		}
	}
	private final SwingModel swingModel = new SwingModel();
	private final SingleTreeSelectionModel treeSelectionModel;
	private final javax.swing.event.TreeSelectionListener treeSelectionListener = new javax.swing.event.TreeSelectionListener() {
		public void valueChanged( javax.swing.event.TreeSelectionEvent e ) {
			T nextValue = getSelectedNode();
			boolean isAdjusting = false;
			TreeSelectionState.this.changeValueFromSwing( nextValue, isAdjusting, new org.lgna.croquet.triggers.TreeSelectionEventTrigger( e ) );
			T prevValue = getValue();
			System.err.println( "changing from " + prevValue + " to " + nextValue );
			fireChanged( prevValue, nextValue, false );
		}
	};

	public TreeSelectionState( Group group, java.util.UUID id, ItemCodec< T > itemCodec ) {
		super( group, id, null, itemCodec );
		this.treeSelectionModel = new SingleTreeSelectionModel();
		this.treeSelectionModel.addTreeSelectionListener( this.treeSelectionListener );
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}
	
	@Override
	protected void localize() {
	}
	protected abstract String getTextForNode( T node );
	protected abstract javax.swing.Icon getIconForNode( T node );
	public abstract edu.cmu.cs.dennisc.javax.swing.models.TreeModel< T > getTreeModel();
	public T getSelectedNode() {
		javax.swing.tree.TreePath path = this.treeSelectionModel.getSelectionPath();
		if( path != null ) {
			return (T)path.getLastPathComponent();
		} else {
			return null;
		}
	}
	public void setSelectedNode( T e ) {
		this.treeSelectionModel.setSelectionPath( this.getTreeModel().getTreePath( e ) );
	}
	@Override
	public T getValue() {
		return this.getSelectedNode();
	}

	@Override
	protected void updateSwingModel(T nextValue) {
		this.setSelectedNode( nextValue );
	}

	@Override
	protected void commitStateEdit( T prevValue, T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		//todo
	}

	public java.util.List< T > getChildren( T node ) {
		edu.cmu.cs.dennisc.javax.swing.models.TreeModel< T > treeModel = this.getTreeModel();
		final int N = treeModel.getChildCount( node );
		java.util.List< T > rv = edu.cmu.cs.dennisc.java.util.Collections.newArrayListWithMinimumCapacity( N );
		for( int i = 0; i < N; i++ ) {
			rv.add( treeModel.getChild( node, i ) );
		}
		return rv;
	}
	public boolean isLeaf( T node ) {
		edu.cmu.cs.dennisc.javax.swing.models.TreeModel< T > treeModel = this.getTreeModel();
		return treeModel.isLeaf( node );
	}

	protected boolean isComboDesired( T childNode ) {
		return this.isLeaf( childNode ) == false;
	}
	protected CascadeBlankChild< T > getBlankChildFor( T childNode ) {
		TreeNodeFillIn< T > fillIn = TreeNodeFillIn.getInstance( this, childNode );
		CascadeBlankChild< T > blankChild;
		if( this.isComboDesired( childNode ) ) {
			blankChild = new CascadeFillInMenuCombo< T >( fillIn, TreeNodeMenu.getInstance( this, childNode ) );
		} else {
			blankChild = fillIn;
		}
		return blankChild;
	}
	public Cascade< T > getCascadeFor( T node ) {
		return TreeNodeCascade.getInstance( this, node );
	}
	public ActionOperation getSelectionOperationFor( T node ) {
		return InternalTreeNodeSelectionOperation.getInstance( this, node );
	}
	public java.util.List< T > getChildrenOfSelectedValue() {
		return this.getChildren( this.getValue() );
	}
	public org.lgna.croquet.components.Tree< T > createTree() {
		return new org.lgna.croquet.components.Tree< T >( this );
	}
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return rv;
	}
}
