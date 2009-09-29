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
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
class TreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
	@Override
	public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		java.awt.Component rv = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		javax.swing.JLabel label = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( rv, javax.swing.JLabel.class );
		if( label != null ) {
			javax.swing.tree.DefaultMutableTreeNode treeNode = (javax.swing.tree.DefaultMutableTreeNode)value;
			PreferencesPane preferencesPane = (PreferencesPane)treeNode.getUserObject();
			label.setText( preferencesPane.getTitle() );
		}
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class PreferencesInputPane extends edu.cmu.cs.dennisc.zoot.ZInputPane<Void> {
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private javax.swing.JTree tree = new javax.swing.JTree();
	public PreferencesInputPane( javax.swing.tree.TreeModel treeModel ) {
		this.tree.setModel( treeModel );
		this.tree.setCellRenderer( new TreeCellRenderer() );
		this.tree.addTreeSelectionListener( new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( e );
			}
		} );
		this.splitPane.setLeftComponent( new javax.swing.JScrollPane( this.tree ) );
		this.splitPane.setRightComponent( new javax.swing.JLabel( "please select" ) );
		this.splitPane.setDividerLocation( 200 );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.splitPane, java.awt.BorderLayout.CENTER );
		this.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
	}
	@Override
	protected Void getActualInputValue() {
		return null;
	}
	public static void main(String[] args) {
		java.util.List<edu.cmu.cs.dennisc.preference.Preference<?>> generalPreferences = new java.util.LinkedList<edu.cmu.cs.dennisc.preference.Preference<?>>();
		generalPreferences.add( new edu.cmu.cs.dennisc.preference.BooleanPreference( "isDefaultFieldNameGenerationDesired", false ) );
		PreferencesPane generalPreferencesPane = new PreferencesPane( "General", org.alice.ide.IDE.class, generalPreferences);

		PreferencesPane everydayPreferencesPane = new PreferencesPane( "Everyday", org.alice.ide.IDE.class, generalPreferences );
		PreferencesPane javaPreferencesPane = new PreferencesPane( "Java", org.alice.ide.IDE.class, generalPreferences );
		
		javax.swing.tree.TreeNode root = new javax.swing.tree.DefaultMutableTreeNode( generalPreferencesPane );
		javax.swing.tree.TreeModel treeModel = new javax.swing.tree.DefaultTreeModel( root );
		PreferencesInputPane inputPane = new PreferencesInputPane( treeModel );
		inputPane.showInJDialog(null);
	}
}
