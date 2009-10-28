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
class TreeCellRenderer extends edu.cmu.cs.dennisc.croquet.swing.DefaultMutableTreeNodeTreeCellRenderer<CollectionOfPreferencesPane> {
	@Override
	protected javax.swing.JLabel getListCellRendererComponentForUserObject(javax.swing.JLabel rv, javax.swing.JTree tree, org.alice.ide.preferencesinputpane.CollectionOfPreferencesPane value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
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
	class TreeSelectionAdapter extends edu.cmu.cs.dennisc.croquet.swing.event.DefaultMutableTreeNodeTreeSelectionAdapter<org.alice.ide.preferencesinputpane.CollectionOfPreferencesPane> {
		@Override
		protected void valueChangedUserObject(javax.swing.event.TreeSelectionEvent e, CollectionOfPreferencesPane oldLeadValue, org.alice.ide.preferencesinputpane.CollectionOfPreferencesPane newLeadValue) {
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
		this.tree.setSelectionRow( 1 );
	}
	@Override
	protected Void getActualInputValue() {
		return null;
	}
	public static void main(String[] args) {
		edu.cmu.cs.dennisc.preference.CollectionOfPreferences rootPreferencesNode = new edu.cmu.cs.dennisc.preference.CollectionOfPreferences() {};
		rootPreferencesNode.initialize();
		
		CollectionOfPreferencesPane rootPreferencesPane = new CollectionOfPreferencesPane( "Root", rootPreferencesNode );
		
		CollectionOfPreferencesPane generalPreferencesPane = new CollectionOfPreferencesPane( "General", org.alice.ide.preferences.GeneralPreferences.getSingleton() ) {
			@Override
			protected org.alice.ide.preferencesinputpane.PreferenceProxy createDefaultProxyFor( edu.cmu.cs.dennisc.preference.Preference preference ) {
				if( preference == org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectCount ) {
					return new IntPreferenceSpinnerProxy( preference, 0, 16, 1 );
				} else {
					return super.createDefaultProxyFor( preference );
				}
			}
		};
		CollectionOfPreferencesPane perspectivePreferencesPane = new CollectionOfPreferencesPane("Programming", org.alice.ide.preferences.ProgrammingPreferences.getSingleton()) {
			@Override
			protected org.alice.ide.preferencesinputpane.PreferenceProxy createDefaultProxyFor( edu.cmu.cs.dennisc.preference.Preference preference ) {
				if( preference == org.alice.ide.preferences.ProgrammingPreferences.getSingleton().listOfCustomProgrammingPreferencesPreference ) {
					return null;
				} else if( preference == org.alice.ide.preferences.ProgrammingPreferences.getSingleton().activePerspective ) {
						return new ConfigurationPreferencePaneProxy( preference );
				} else {
					return super.createDefaultProxyFor( preference );
				}
			}
		};
		CollectionOfPreferencesPane sceneEditorPreferencesPane = new CollectionOfPreferencesPane( "Scene Editor", org.alice.ide.preferences.SceneEditorPreferences.getSingleton() );

		javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode( rootPreferencesPane );
		root.add( new javax.swing.tree.DefaultMutableTreeNode( generalPreferencesPane ) );
		javax.swing.tree.DefaultMutableTreeNode perspective = new javax.swing.tree.DefaultMutableTreeNode( perspectivePreferencesPane );
		root.add( perspective );
		root.add( new javax.swing.tree.DefaultMutableTreeNode( sceneEditorPreferencesPane ) );
		javax.swing.tree.TreeModel treeModel = new javax.swing.tree.DefaultTreeModel( root );
		PreferencesInputPane inputPane = new PreferencesInputPane( treeModel );
		inputPane.showInJDialog(null);
		System.exit( 0 );
	}
}
