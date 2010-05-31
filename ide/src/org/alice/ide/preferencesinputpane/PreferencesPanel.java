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
package org.alice.ide.preferencesinputpane;

/**
 * @author Dennis Cosgrove
 */
class TreeCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.DefaultMutableTreeNodeTreeCellRenderer<CollectionOfPreferencesPane> {
	@Override
	protected javax.swing.JLabel getListCellRendererComponentForUserObject(javax.swing.JLabel rv, javax.swing.JTree tree, org.alice.ide.preferencesinputpane.CollectionOfPreferencesPane value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		rv.setText( value.getTitle() );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class PreferencesPanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane splitPane = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane();
	private edu.cmu.cs.dennisc.croquet.Tree<?> tree = new edu.cmu.cs.dennisc.croquet.Tree();
	class TreeSelectionAdapter extends edu.cmu.cs.dennisc.javax.swing.event.DefaultMutableTreeNodeTreeSelectionAdapter<org.alice.ide.preferencesinputpane.CollectionOfPreferencesPane> {
		@Override
		protected void valueChangedUserObject(javax.swing.event.TreeSelectionEvent e, CollectionOfPreferencesPane oldLeadValue, org.alice.ide.preferencesinputpane.CollectionOfPreferencesPane newLeadValue) {
//			if( oldLeadValue != null ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "oldLeadValue:", oldLeadValue.getTitle() );
//			}
			if( newLeadValue != null ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "newLeadValue:", newLeadValue.getTitle() );
				int dividerLocation = PreferencesPanel.this.splitPane.getDividerLocation();
				PreferencesPanel.this.splitPane.setRightComponent( newLeadValue );
				PreferencesPanel.this.splitPane.setDividerLocation( dividerLocation );
			}
		}
	}

	public PreferencesPanel() {
		edu.cmu.cs.dennisc.preference.CollectionOfPreferences rootPreferencesNode = new edu.cmu.cs.dennisc.preference.CollectionOfPreferences() {};
		rootPreferencesNode.initialize();
		
		CollectionOfPreferencesPane rootPreferencesPane = new CollectionOfPreferencesPane( "Root", rootPreferencesNode );
		
		
		class ClearRecentProjectsOperation extends org.alice.ide.operations.ActionOperation {
			public ClearRecentProjectsOperation() {
				super( org.alice.ide.IDE.PREFERENCES_GROUP, java.util.UUID.fromString( "f8b4c080-88cf-41e5-ac7d-afc2bd340083" ) );
				this.setName( "Clear Recent Projects" );
			}
			@Override
			protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
				org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.clear();
				context.finish();
			}
		}
		
		CollectionOfPreferencesPane generalPreferencesPane = new CollectionOfPreferencesPane( "General", org.alice.ide.preferences.GeneralPreferences.getSingleton() ) {
			@Override
			protected org.alice.ide.preferencesinputpane.PreferenceProxy createDefaultProxyFor( edu.cmu.cs.dennisc.preference.Preference preference ) {
				if( preference == org.alice.ide.preferences.GeneralPreferences.getSingleton().desiredRecentProjectCount ) {
					return new IntPreferenceSpinnerProxy( preference, 0, 16, 1 );
				} else if( preference == org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths ) {
					return null;
				} else {
					return super.createDefaultProxyFor( preference );
				}
			}
			@Override
			protected void updateCenterComponent( edu.cmu.cs.dennisc.croquet.PageAxisPanel centerComponent, edu.cmu.cs.dennisc.preference.CollectionOfPreferences collectionOfPreferences ) {
				super.updateCenterComponent( centerComponent, collectionOfPreferences );
				centerComponent.addComponent( new ClearRecentProjectsOperation().createButton() );
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

		this.tree.setModel( treeModel );
		this.tree.setRootVisible( false );
		this.tree.setCellRenderer( new TreeCellRenderer() );
		this.tree.addTreeSelectionListener( new TreeSelectionAdapter() );
		this.splitPane.setLeftComponent( new edu.cmu.cs.dennisc.croquet.ScrollPane( this.tree ) );
		this.splitPane.setRightComponent( new edu.cmu.cs.dennisc.croquet.Label( "please select" ) );
		this.splitPane.setDividerLocation( 200 );
		this.addComponent( this.splitPane, Constraint.CENTER );
		//this.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		this.tree.setSelectionRow( 1 );
	}
}
