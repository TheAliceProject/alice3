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

package org.alice.app.openprojectpane;

/**
 * @author Dennis Cosgrove
 */
public class SelectProjectToOpenPanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static abstract class ContentTabStateOperation extends edu.cmu.cs.dennisc.croquet.TabStateOperation {
		public ContentTabStateOperation(java.util.UUID individualId, String title) {
			super(edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, individualId, false, title);
		}
//		@Override
//		protected edu.cmu.cs.dennisc.croquet.ScrollPane createSingletonScrollPane() {
//			edu.cmu.cs.dennisc.croquet.ScrollPane rv = super.createSingletonScrollPane();
//			rv.setHorizontalScrollbarPolicy( edu.cmu.cs.dennisc.croquet.ScrollPane.HorizontalScrollbarPolicy.NEVER );
//			rv.setVerticalScrollbarPolicy( edu.cmu.cs.dennisc.croquet.ScrollPane.VerticalScrollbarPolicy.AS_NEEDED );
//			return rv;
//		}
		@Override
		protected final boolean isCloseAffordanceDesired() {
			return false;
		}
	}

	private ContentTabStateOperation tutorialOperation = new ContentTabStateOperation(java.util.UUID.fromString("f4ff59f1-cf15-4301-a17a-2d80a4ea6fa4"), "Tutorial") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new TutorialPane();
		}
	};
	private ContentTabStateOperation myProjectsOperation = new ContentTabStateOperation(java.util.UUID.fromString("c7fb9c47-f215-47dc-941e-872842ce397e"), "My Projects") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new MyProjectsPane();
		}
	};
	private ContentTabStateOperation recentProjectsOperation = new ContentTabStateOperation(java.util.UUID.fromString("b490bb6c-f74f-422b-b9a6-5ef643b02b58"), "Recent") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new RecentPane();
		}
	};
	private ContentTabStateOperation templatesOperation = new ContentTabStateOperation(java.util.UUID.fromString("e658dbd1-c58b-42ec-9338-49f186aecc71"), "Templates") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new org.alice.stageide.openprojectpane.templates.TemplatesTabContentPane();
		}
	};
	private ContentTabStateOperation textbookOperation = new ContentTabStateOperation(java.util.UUID.fromString("033afcdf-29b9-4fbf-b9f5-fb5c496a7860"), "Textbook") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new TextbookPane();
		}
	};
	private ContentTabStateOperation fileSystemOperation = new ContentTabStateOperation(java.util.UUID.fromString("b1698424-1f0e-4499-852a-da627fa9e789"), "File System") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new FileSystemPane();
		}
	};

	private edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation tabSelectionOperation = new edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation(edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString("12e1d59b-2893-4144-b995-08090680a318"),
			this.templatesOperation, 
			this.myProjectsOperation, 
			this.recentProjectsOperation, 
			//this.tutorialOperation,
			//this.textbookOperation, 
			this.fileSystemOperation
	);

	public SelectProjectToOpenPanel() {
		this.addComponent(this.tabSelectionOperation.getSingletonTabbedPane(), Constraint.CENTER);
		this.tutorialOperation.setEnabled(false);
		this.textbookOperation.setEnabled(false);
	}

	public void refresh() {
		((MyProjectsPane) this.myProjectsOperation.getSingletonView()).refresh();
	}
	public java.net.URI getSelectedURI() {
		edu.cmu.cs.dennisc.croquet.TabStateOperation current = this.tabSelectionOperation.getCurrentTabStateOperation();
		if( current != null ) {
			return ((TabContentPanel)(current.getSingletonView())).getSelectedURI();
		} else {
			return null;
		}
	}
	public void selectAppropriateTab(boolean isNew) {
		ContentTabStateOperation operation;
		if (isNew) {
			operation = this.templatesOperation;
		} else {
			operation = this.myProjectsOperation; // todo: recentPane?
		}
		operation.setState( true );
//		this.tabSelectionOperation.setCurrentTabStateOperation(operation);
	}
}
