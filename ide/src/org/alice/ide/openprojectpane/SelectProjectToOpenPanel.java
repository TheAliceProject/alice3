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

package org.alice.ide.openprojectpane;

/**
 * @author Dennis Cosgrove
 */
public class SelectProjectToOpenPanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static abstract class ContentTab extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
		public ContentTab(java.util.UUID individualId, String title) {
			super(individualId, title, null);
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane() {
			edu.cmu.cs.dennisc.croquet.ScrollPane rv = super.createScrollPane();
			rv.setHorizontalScrollbarPolicy( edu.cmu.cs.dennisc.croquet.ScrollPane.HorizontalScrollbarPolicy.NEVER );
			rv.setVerticalScrollbarPolicy( edu.cmu.cs.dennisc.croquet.ScrollPane.VerticalScrollbarPolicy.AS_NEEDED );
			rv.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 12 );
			return rv;
		}
	}

	private ContentTab templatesTab = new ContentTab(java.util.UUID.fromString("e658dbd1-c58b-42ec-9338-49f186aecc71"), "Templates") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			return new org.alice.stageide.openprojectpane.templates.TemplatesTabContentPane();
		}
	};
	private ContentTab myProjectsTab = new ContentTab(java.util.UUID.fromString("c7fb9c47-f215-47dc-941e-872842ce397e"), "My Projects") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			return new MyProjectsPane();
		}
	};
	private ContentTab recentProjectsTab = new ContentTab(java.util.UUID.fromString("b490bb6c-f74f-422b-b9a6-5ef643b02b58"), "Recent") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			return new RecentPane();
		}
	};
	private ContentTab tutorialTab = new ContentTab(java.util.UUID.fromString("f4ff59f1-cf15-4301-a17a-2d80a4ea6fa4"), "Tutorial") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			return new TutorialPane();
		}
	};
	private ContentTab textbookTab = new ContentTab(java.util.UUID.fromString("033afcdf-29b9-4fbf-b9f5-fb5c496a7860"), "Textbook") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			return new TextbookPane();
		}
	};
	private ContentTab fileSystemTab = new ContentTab(java.util.UUID.fromString("b1698424-1f0e-4499-852a-da627fa9e789"), "File System") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			return new FileSystemPane();
		}
	};

	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabSelectionOperation = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation(edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString("12e1d59b-2893-4144-b995-08090680a318"),
			-1, 
			this.templatesTab,
			this.myProjectsTab, 
			this.recentProjectsTab, 
			//this.tutorialTab,
			//this.textbookTab, 
			this.fileSystemTab
	);

	public SelectProjectToOpenPanel() {
		//this.templatesOperation.setState( true );
		this.addComponent(
				//edu.cmu.cs.dennisc.croquet.BoxUtilities.createConstrainedToMinimumPreferredSizeComponent(
						this.tabSelectionOperation.createDefaultFolderTabbedPane(), 
				//		480, 
				//		320),
				Constraint.CENTER);
		//this.tutorialOperation.setEnabled(false);
		//this.textbookOperation.setEnabled(false);
		this.setBackgroundColor(TabContentPanel.DEFAULT_BACKGROUND_COLOR);
	}

	public void refresh() {
		((MyProjectsPane) this.myProjectsTab.getMainComponent()).refresh();
	}
	public java.net.URI getSelectedURI() {
		edu.cmu.cs.dennisc.croquet.PredeterminedTab current = this.tabSelectionOperation.getSelectedItem();
		if( current != null ) {
			return ((TabContentPanel)(current.getMainComponent())).getSelectedURI();
		} else {
			return null;
		}
	}
	public void selectAppropriateTab(boolean isNew) {
		ContentTab tab;
		if (isNew) {
			tab = this.templatesTab;
		} else {
			tab = this.myProjectsTab; // todo: recentPane?
		}
		this.tabSelectionOperation.setSelectedItem( tab );
		//operation.setState( true );
//		this.tabSelectionOperation.setCurrentTabStateOperation(operation);
	}
}
