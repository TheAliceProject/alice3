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
class TreeCellRenderer extends edu.cmu.cs.dennisc.croquet.DefaultMutableTreeNodeTreeCellRenderer<PreferencesPane> {
	@Override
	protected javax.swing.JLabel getListCellRendererComponentForUserObject(javax.swing.JLabel rv, javax.swing.JTree tree, org.alice.ide.preferencesinputpane.PreferencesPane value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		rv.setText( value.getTitle() );
		return rv;
	}
}


/**
 * @author Dennis Cosgrove
 */
public class PreferencesInputPane extends edu.cmu.cs.dennisc.zoot.ZInputPane<Void> {
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private javax.swing.JTree tree = new javax.swing.JTree();
	class TreeSelectionAdapter extends edu.cmu.cs.dennisc.croquet.event.DefaultMutableTreeNodeTreeSelectionAdapter<org.alice.ide.preferencesinputpane.PreferencesPane> {
		@Override
		protected void valueChangedUserObject(javax.swing.event.TreeSelectionEvent e, org.alice.ide.preferencesinputpane.PreferencesPane oldLeadValue, org.alice.ide.preferencesinputpane.PreferencesPane newLeadValue) {
//			if( oldLeadValue != null ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "oldLeadValue:", oldLeadValue.getTitle() );
//			}
			if( newLeadValue != null ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "newLeadValue:", newLeadValue.getTitle() );
				int dividerLocation = PreferencesInputPane.this.splitPane.getDividerLocation();
				PreferencesInputPane.this.splitPane.setRightComponent( newLeadValue );
				PreferencesInputPane.this.splitPane.setDividerLocation( dividerLocation );
			}
		}
	}

	public PreferencesInputPane( javax.swing.tree.TreeModel treeModel ) {
		this.tree.setModel( treeModel );
		this.tree.setRootVisible( false );
		this.tree.setCellRenderer( new TreeCellRenderer() );
		this.tree.addTreeSelectionListener( new TreeSelectionAdapter() );
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
		org.alice.ide.preferences.PreferencesNode rootPreferencesNode = new org.alice.ide.preferences.PreferencesNode() {};
		PreferencesPane rootPreferencesPane = new PreferencesPane( "Root", rootPreferencesNode );
		
		PreferencesPane generalPreferencesPane = new PreferencesPane( "General", org.alice.ide.preferences.GeneralPreferencesNode.getSingleton() );
		PreferencesPane languagePreferencesPane = new PreferencesPane( "Perspective", org.alice.ide.preferences.perspective.exposure.PreferencesNode.getSingleton() );
		PreferencesPane everydayPreferencesPane = new PreferencesPane( "Exposure", org.alice.ide.preferences.perspective.exposure.PreferencesNode.getSingleton() );
		PreferencesPane javaPreferencesPane = new PreferencesPane( "Transition", org.alice.ide.preferences.perspective.preparation.PreferencesNode.getSingleton() );
		
		javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode( rootPreferencesPane );
		root.add( new javax.swing.tree.DefaultMutableTreeNode( generalPreferencesPane ) );
		javax.swing.tree.DefaultMutableTreeNode language = new javax.swing.tree.DefaultMutableTreeNode( languagePreferencesPane );
		root.add( language );
		language.add( new javax.swing.tree.DefaultMutableTreeNode( everydayPreferencesPane ) );
		language.add( new javax.swing.tree.DefaultMutableTreeNode( javaPreferencesPane ) );
		javax.swing.tree.TreeModel treeModel = new javax.swing.tree.DefaultTreeModel( root );
		PreferencesInputPane inputPane = new PreferencesInputPane( treeModel );
		inputPane.showInJDialog(null);
		System.exit( 0 );
	}
}
