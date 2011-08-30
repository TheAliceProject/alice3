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

package org.alice.ide.croquet.models.openproject;

/*package-private*/ abstract class ContentTab extends org.lgna.croquet.PredeterminedTab {
	public ContentTab( java.util.UUID id, String title ) {
		super( id );
		this.setTitleText( title );
	}
	@Override
	public org.lgna.croquet.components.ScrollPane createScrollPane() {
		org.lgna.croquet.components.ScrollPane rv = super.createScrollPane();
		rv.setHorizontalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.HorizontalScrollbarPolicy.NEVER );
		rv.setVerticalScrollbarPolicy( org.lgna.croquet.components.ScrollPane.VerticalScrollbarPolicy.AS_NEEDED );
		rv.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ProjectTabSelectionState extends org.lgna.croquet.PredeterminedTabSelectionState< ContentTab > {
	public static final java.util.UUID TEMPLATES_TAB_ID = java.util.UUID.fromString( "e658dbd1-c58b-42ec-9338-49f186aecc71" );
	public static final java.util.UUID MY_PROJECTS_TAB_ID = java.util.UUID.fromString( "c7fb9c47-f215-47dc-941e-872842ce397e" );
	public static final java.util.UUID RECENT_TAB_ID = java.util.UUID.fromString( "b490bb6c-f74f-422b-b9a6-5ef643b02b58" );
//	public static final java.util.UUID TUTORIAL_TAB_ID = java.util.UUID.fromString( "f4ff59f1-cf15-4301-a17a-2d80a4ea6fa4" );
//	public static final java.util.UUID TEXTBOOK_TAB_ID = java.util.UUID.fromString( "033afcdf-29b9-4fbf-b9f5-fb5c496a7860" );
	public static final java.util.UUID FILE_SYSTEM_TAB_ID = java.util.UUID.fromString( "b1698424-1f0e-4499-852a-da627fa9e789" );

	private static class ContentPredeterminedTabCodec extends org.lgna.croquet.PredeterminedTabCodec< ContentTab > {
		private ContentPredeterminedTabCodec() {
			super( ContentTab.class );
		}
		@Override
		protected ContentTab lookUp( java.util.UUID id ) {
			return map.get( id );
		}
	}

	private static java.util.Map< java.util.UUID, ContentTab > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	static {
		map.put( TEMPLATES_TAB_ID, new ContentTab( TEMPLATES_TAB_ID, "Templates" ) {
			@Override
			protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
				return new org.alice.stageide.openprojectpane.components.TemplatesTabContentPane();
			}
		} );
		map.put( MY_PROJECTS_TAB_ID, new ContentTab( MY_PROJECTS_TAB_ID, "My Projects" ) {
			@Override
			protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
				return new org.alice.ide.openprojectpane.DirectoryListContentPanel( org.alice.ide.openprojectpane.models.MyProjectsUriSelectionState.getInstance() );
			}
		} );
		map.put( RECENT_TAB_ID, new ContentTab( RECENT_TAB_ID, "Recent" ) {
			@Override
			protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
				return new org.alice.ide.openprojectpane.RecentPane();
			}
		} );
//		map.put( TUTORIAL_TAB_ID, new ContentTab( TUTORIAL_TAB_ID, "Tutorial" ) {
//			@Override
//			protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
//				return new org.alice.ide.openprojectpane.TutorialPane();
//			}
//		} );
//		map.put( TEXTBOOK_TAB_ID, new ContentTab( TEXTBOOK_TAB_ID, "Textbook" ) {
//			@Override
//			protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
//				return new org.alice.ide.openprojectpane.TextbookPane();
//			}
//		} );
		map.put( FILE_SYSTEM_TAB_ID, new ContentTab( FILE_SYSTEM_TAB_ID, "File System" ) {
			@Override
			protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
				return new org.alice.ide.openprojectpane.FileSystemPane();
			}
		} );
	}
	
	private static class SingletonHolder {
		private static ProjectTabSelectionState instance = new ProjectTabSelectionState();
	}
	public static ProjectTabSelectionState getInstance() {
		return SingletonHolder.instance;
	}
	private ProjectTabSelectionState() {
		super( org.lgna.croquet.Application.UI_STATE_GROUP,
			java.util.UUID.fromString( "12e1d59b-2893-4144-b995-08090680a318" ), new ContentPredeterminedTabCodec(), -1, 
			map.get( TEMPLATES_TAB_ID ),
			map.get( MY_PROJECTS_TAB_ID ),
			map.get( RECENT_TAB_ID ),
			//map.get( TUTORIAL_TAB_ID ),
			//map.get( TEXTBOOK_TAB_ID ),
			map.get( FILE_SYSTEM_TAB_ID )
		);
	}
	public void refresh() {
		//todo
		org.alice.ide.croquet.models.openproject.RecentProjectsState.getInstance().refresh();
	}
	
	public java.net.URI getSelectedURI() {
		org.lgna.croquet.PredeterminedTab current = org.alice.ide.croquet.models.openproject.ProjectTabSelectionState.getInstance().getSelectedItem();
		if( current != null ) {
			return ((org.alice.ide.openprojectpane.TabContentPanel)(current.getMainComponent())).getSelectedURI();
		} else {
			return null;
		}
	}
	public void selectAppropriateTab( boolean isNew ) {
		ContentTab tab;
		if( isNew ) {
			tab = map.get( org.alice.ide.croquet.models.openproject.ProjectTabSelectionState.TEMPLATES_TAB_ID );
		} else {
			tab = map.get( org.alice.ide.croquet.models.openproject.ProjectTabSelectionState.MY_PROJECTS_TAB_ID ); //todo: recentPane?
		}
		this.setSelectedItem( tab );
	}
}
