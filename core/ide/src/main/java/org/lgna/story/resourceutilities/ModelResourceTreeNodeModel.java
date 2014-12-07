package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.javax.swing.models.TreeModel;

public class ModelResourceTreeNodeModel extends javax.swing.tree.DefaultTreeModel implements TreeModel<ModelResourceTreeNode> {
	public ModelResourceTreeNodeModel( ModelResourceTreeNode root ) {
		super( root );
	}

	@Override
	public ModelResourceTreeNode getChild( Object parent, int index ) {
		return (ModelResourceTreeNode)super.getChild( parent, index );
	}

	@Override
	public ModelResourceTreeNode getRoot() {
		return (ModelResourceTreeNode)super.getRoot();
	}

	@Override
	public javax.swing.tree.TreePath getTreePath( ModelResourceTreeNode e ) {
		Object[] nodes = this.getPathToRoot( e );
		javax.swing.tree.TreePath path = new javax.swing.tree.TreePath( nodes );
		return path;
	}
}
