/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package alice.ide.sceneeditor;

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

//class TreeCellRenderer extends javax.swing.JLabel implements javax.swing.tree.TreeCellRenderer {
//	public java.awt.Component getTreeCellRendererComponent( javax.swing.JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus ) {
//		if( value instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
//			edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)value;
//			this.setText( field.getName() );
//		}
//		return this;
//	}
//}
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

class Tree extends zoot.ZTree {
	private TreeModel model = new TreeModel( null ); 
	public Tree() {
		this.setModel( this.model );
		this.setCellRenderer( new TreeCellRenderer() );
	}
	
}

/**
 * @author Dennis Cosgrove
 */
public class SceneEditor extends alice.ide.Editor<edu.cmu.cs.dennisc.alice.ast.AbstractType> {
	private javax.swing.JSplitPane root = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private Tree tree = new Tree();
	private javax.swing.JPanel lookingGlass = new javax.swing.JPanel() {
		@Override
		public java.awt.Dimension getPreferredSize() {
			return new java.awt.Dimension( 320, 240 );
		}
	};
	public SceneEditor() {
		this.lookingGlass.setBackground( java.awt.Color.RED );
		this.root.setLeftComponent( new javax.swing.JScrollPane( this.tree ) );
		this.root.setRightComponent( this.lookingGlass );
		this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.add( this.root );
	}
	@Override
	public void projectOpened( alice.ide.event.ProjectOpenEvent e ) {
		super.projectOpened( e );
		edu.cmu.cs.dennisc.alice.ast.AbstractType programType = this.getIDE().getProject().getProgramType();
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = programType.getDeclaredFields().get( 0 );
		tree.setModel( new TreeModel( sceneField ) );
	}
}
