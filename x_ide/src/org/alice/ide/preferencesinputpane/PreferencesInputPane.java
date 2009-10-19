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
class TreeCellRenderer extends edu.cmu.cs.dennisc.croquet.DefaultMutableTreeNodeTreeCellRenderer<OuterPreferencesPane> {
	@Override
	protected javax.swing.JLabel getListCellRendererComponentForUserObject(javax.swing.JLabel rv, javax.swing.JTree tree, org.alice.ide.preferencesinputpane.OuterPreferencesPane value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		rv.setText( value.getTitle() );
		return rv;
	}
}


/**
 * @author Dennis Cosgrove
 */
class PerspectiveOuterPreferencesPane extends OuterPreferencesPane {
	class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation<InnerPreferencesPane> {
		public ItemSelectionOperation( InnerPreferencesPane... panes ) {
			super( new javax.swing.DefaultComboBoxModel( panes ) );
		}
		
		@Override
		protected void handleSelectionChange(InnerPreferencesPane value) {
			String key;
			if( value != null ) {
				key = value.toString();
			} else {
				key = null;
			}
			if( PerspectiveOuterPreferencesPane.this.cardPane != null ) {
				PerspectiveOuterPreferencesPane.this.cardPane.show( key );
				PerspectiveOuterPreferencesPane.this.revalidate();
				edu.cmu.cs.dennisc.print.PrintUtilities.println( key );
			}
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
	}

	class List extends edu.cmu.cs.dennisc.zoot.ZComboBox {
//		class PerspectiveListCellRenderer extends edu.cmu.cs.dennisc.croquet.ListCellRenderer<org.alice.ide.preferences.perspective.PreferencesNode> {
//			@Override
//			protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, org.alice.ide.preferences.perspective.PreferencesNode value, int index, boolean isSelected, boolean cellHasFocus) {
//				rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
//				rv.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
////				String packageName = value.getClass().getPackage().getName();
////				rv.setText( packageName.substring( packageName.lastIndexOf('.')+1 ) );
//				return rv;
//			}
//		}
		public List( InnerPreferencesPane... panes ) {
			super( new ItemSelectionOperation( panes ) );
//			this.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
//			this.setVisibleRowCount( 1 );
//			this.setOpaque( false );
			//this.setCellRenderer( new PerspectiveListCellRenderer() );
		}
//		@Override
//		public java.awt.Dimension getMaximumSize() {
//			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getMaximumSize(), Short.MAX_VALUE );
//		}
	}
	
	class RemoveVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public RemoveVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "Remove" );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	class NewVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public NewVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "New..." );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	class ImportVariantOperation extends org.alice.ide.operations.AbstractActionOperation {
		public ImportVariantOperation() {
			this.putValue( javax.swing.Action.NAME, "Import..." );
		}
		@Override
		public boolean isSignificant() {
			//todo?
			return false;
		}
		public void perform(edu.cmu.cs.dennisc.zoot.ActionContext actionContext) {
		}
	}
	
	private edu.cmu.cs.dennisc.croquet.CardPane cardPane;
	public PerspectiveOuterPreferencesPane() {
		super("Programming", org.alice.ide.preferences.ProgrammingPreferences.getSingleton());
	}
	
	@Override
	protected java.awt.Component createCenterComponent(edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
//		org.alice.ide.preferences.programming.Configuration[] preferencesNodes = org.alice.ide.preferences.ProgrammingPreferences.getSingleton().getBuiltInPreferenceNodes();
		org.alice.ide.preferences.programming.Configuration[] preferencesNodes = {};
		org.alice.ide.preferences.ProgrammingPreferences.getSingleton().getBuiltInPreferenceNodes();
		final int N = preferencesNodes.length;
		InnerPreferencesPane[] panes = new InnerPreferencesPane[ N ];
		for( int i=0; i<N; i++ ){ 
			//panes[ i ] = new InnerPreferencesPane( preferencesNodes[ i ] );
		}
		
		edu.cmu.cs.dennisc.zoot.ZButton removeButton = new edu.cmu.cs.dennisc.zoot.ZButton( new RemoveVariantOperation() );
		removeButton.setEnabled( false );
		removeButton.setToolTipText( "todo" );
		edu.cmu.cs.dennisc.zoot.ZButton newButton = new edu.cmu.cs.dennisc.zoot.ZButton( new NewVariantOperation() );
		newButton.setEnabled( false );
		newButton.setToolTipText( "todo" );
		edu.cmu.cs.dennisc.zoot.ZButton importButton = new edu.cmu.cs.dennisc.zoot.ZButton( new ImportVariantOperation() );
		importButton.setEnabled( false );
		importButton.setToolTipText( "todo" );
		List list = new List( panes );
		edu.cmu.cs.dennisc.croquet.BorderPane northTopPane = new edu.cmu.cs.dennisc.croquet.BorderPane();
		northTopPane.add( list, java.awt.BorderLayout.CENTER );
		//northTopPane.add( new edu.cmu.cs.dennisc.croquet.LineAxisPane( /*editButton,*/ removeButton ), java.awt.BorderLayout.EAST );
		northTopPane.add( removeButton, java.awt.BorderLayout.EAST );
		edu.cmu.cs.dennisc.croquet.LineAxisPane northBottomPane = new edu.cmu.cs.dennisc.croquet.LineAxisPane( newButton, importButton, javax.swing.Box.createHorizontalGlue() );

		this.cardPane = new edu.cmu.cs.dennisc.croquet.CardPane();
		for( InnerPreferencesPane pane : panes ) {
			this.cardPane.add( pane, pane.toString() );
		}
		edu.cmu.cs.dennisc.croquet.PageAxisPane rv = new edu.cmu.cs.dennisc.croquet.PageAxisPane(
				edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "active variant:" ),
				javax.swing.Box.createVerticalStrut( 4 ),  
				northTopPane, 
				javax.swing.Box.createVerticalStrut( 4 ),  
				northBottomPane,
				javax.swing.Box.createVerticalStrut( 32 ),
				edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "values:" ),
				this.cardPane
		);
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 4 ) );
		return rv;
	}
	@Override
	protected boolean isCenterComponentScrollPaneDesired() {
		return false;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class PreferencesInputPane extends edu.cmu.cs.dennisc.zoot.ZInputPane<Void> {
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private javax.swing.JTree tree = new javax.swing.JTree();
	class TreeSelectionAdapter extends edu.cmu.cs.dennisc.croquet.event.DefaultMutableTreeNodeTreeSelectionAdapter<org.alice.ide.preferencesinputpane.OuterPreferencesPane> {
		@Override
		protected void valueChangedUserObject(javax.swing.event.TreeSelectionEvent e, OuterPreferencesPane oldLeadValue, org.alice.ide.preferencesinputpane.OuterPreferencesPane newLeadValue) {
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
		edu.cmu.cs.dennisc.preference.CollectionOfPreferences rootPreferencesNode = new edu.cmu.cs.dennisc.preference.CollectionOfPreferences() {};
		rootPreferencesNode.initialize();
		
		OuterPreferencesPane rootPreferencesPane = new OuterPreferencesPane( "Root", rootPreferencesNode );
		
		OuterPreferencesPane generalPreferencesPane = new OuterPreferencesPane( "General", org.alice.ide.preferences.GeneralPreferences.getSingleton() );
		OuterPreferencesPane perspectivePreferencesPane = new PerspectiveOuterPreferencesPane();
		OuterPreferencesPane sceneEditorPreferencesPane = new OuterPreferencesPane( "Scene Editor", org.alice.ide.preferences.SceneEditorPreferences.getSingleton() );

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
