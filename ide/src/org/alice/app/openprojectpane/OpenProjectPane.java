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

abstract class ComingSoonTabContentPanel extends TabContentPanel {
	public ComingSoonTabContentPanel() {
		this.setEnabled(false);
		this.setToolTipText("coming soon");
	}

	@Override
	public java.net.URI getSelectedURI() {
		return null;
	}
}

class TutorialPane extends ComingSoonTabContentPanel {
}

class MyProjectsPane extends DirectoryListContentPanel {
	@Override
	public java.io.File getDirectory() {
		return org.alice.app.ProjectApplication.getSingleton().getMyProjectsDirectory();
	}
}

abstract class ApplicationRootDirectoryListPane extends DirectoryListContentPanel {
	protected abstract String getSubPath();

	@Override
	public java.io.File getDirectory() {
		org.alice.app.ProjectApplication application = org.alice.app.ProjectApplication.getSingleton();
		if (application != null) {
			return new java.io.File(application.getApplicationRootDirectory(), this.getSubPath());
		} else {
			return null;
		}
	}
}

// class TemplatesPane extends ApplicationRootDirectoryListPane {
// @Override
// public String getSubPath() {
// return "projects/templates";
// }
// @Override
// public String getTabTitleText() {
// return "Templates";
// }
// }

class ExamplesPane extends ApplicationRootDirectoryListPane {
	@Override
	public String getSubPath() {
		return "projects/examples";
	}
}

class TextbookPane extends ApplicationRootDirectoryListPane {
	@Override
	public String getSubPath() {
		return "projects/textbook";
	}
}

class FileSystemPane extends TabContentPanel {
	private javax.swing.JTextField textField = new javax.swing.JTextField();

	public FileSystemPane() {
		this.textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			private void handleUpdate(javax.swing.event.DocumentEvent e) {
				FileSystemPane.this.updateOKButton();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				this.handleUpdate(e);
			}

			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				this.handleUpdate(e);
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				this.handleUpdate(e);
			}
		});

		class BrowseOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
			public BrowseOperation() {
				super(edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString("67936a64-be55-44d5-9441-4cc3cce5cc75"));
				this.setName("browse...");
			}

			@Override
			protected void perform(edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton<?> button) {
				java.io.File file = edu.cmu.cs.dennisc.croquet.Application.getSingleton().showOpenFileDialog(org.alice.app.ProjectApplication.getSingleton().getMyProjectsDirectory(), null, edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION, true);
				if (file != null) {
					FileSystemPane.this.textField.setText(edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible(file));
				}
				context.finish();
			}
		}

		BrowseOperation browseOperation = new BrowseOperation();
		edu.cmu.cs.dennisc.croquet.BorderPanel pane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		pane.setOpaque(false);
		pane.addComponent(new edu.cmu.cs.dennisc.croquet.Label("file:"), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.WEST);
		pane.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(this.textField), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
		pane.addComponent(browseOperation.createButton(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.EAST);

		this.addComponent(pane, Constraint.NORTH);
	}

	@Override
	public java.net.URI getSelectedURI() {
		String path = this.textField.getText();
		java.io.File file = new java.io.File(path);
		if (file.exists()) {
			return file.toURI();
		} else {
			return null;
		}
	}
}

class SelectProjectToOpenPanel extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static abstract class ContentTabIsSelectedStateOperation extends edu.cmu.cs.dennisc.croquet.TabIsSelectedStateOperation {
		public ContentTabIsSelectedStateOperation(java.util.UUID individualId, String title) {
			super(edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, individualId, false, title);
			TabContentPanel contentPanel = (TabContentPanel) this.getSingletonView();
		}

		@Override
		protected final boolean isCloseAffordanceDesired() {
			return false;
		}
	}

	private ContentTabIsSelectedStateOperation tutorialOperation = new ContentTabIsSelectedStateOperation(java.util.UUID.fromString("f4ff59f1-cf15-4301-a17a-2d80a4ea6fa4"), "Tutorial") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new TutorialPane();
		}
	};
	private ContentTabIsSelectedStateOperation myProjectsOperation = new ContentTabIsSelectedStateOperation(java.util.UUID.fromString("c7fb9c47-f215-47dc-941e-872842ce397e"), "My Projects") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new MyProjectsPane();
		}
	};
	private ContentTabIsSelectedStateOperation recentProjectsOperation = new ContentTabIsSelectedStateOperation(java.util.UUID.fromString("b490bb6c-f74f-422b-b9a6-5ef643b02b58"), "Recent") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new RecentPane();
		}
	};
	private ContentTabIsSelectedStateOperation templatesOperation = new ContentTabIsSelectedStateOperation(java.util.UUID.fromString("e658dbd1-c58b-42ec-9338-49f186aecc71"), "Templates") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new org.alice.stageide.openprojectpane.templates.TemplatesTabContentPane();
		}
	};
	private ContentTabIsSelectedStateOperation textbookOperation = new ContentTabIsSelectedStateOperation(java.util.UUID.fromString("033afcdf-29b9-4fbf-b9f5-fb5c496a7860"), "Textbook") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new TextbookPane();
		}
	};
	private ContentTabIsSelectedStateOperation fileSystemOperation = new ContentTabIsSelectedStateOperation(java.util.UUID.fromString("b1698424-1f0e-4499-852a-da627fa9e789"), "File System") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.Component<?> createSingletonView() {
			return new FileSystemPane();
		}
	};

	private edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation tabSelectionOperation = new edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation(edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString("12e1d59b-2893-4144-b995-08090680a318"),
			this.templatesOperation, this.myProjectsOperation, this.recentProjectsOperation, this.tutorialOperation, this.textbookOperation, this.fileSystemOperation);

	public SelectProjectToOpenPanel() {
		this.addComponent(this.tabSelectionOperation.getSingletonTabbedPane(), Constraint.CENTER);
	}

	/* package-private */void refresh() {
		((MyProjectsPane) this.myProjectsOperation.getSingletonView()).refresh();
	}

	/* package-private */TabContentPanel getSelectedTabContentPanel() {
		return (TabContentPanel) this.tabSelectionOperation.getCurrentSelectedTabOperation().getSingletonView();
	}

	/* package-private */void selectAppropriateTab(boolean isNew) {
		ContentTabIsSelectedStateOperation operation;
		if (isNew) {
			operation = this.templatesOperation;
		} else {
			operation = this.myProjectsOperation; // todo: recentPane?
		}
		this.tabSelectionOperation.setCurrentSelectedTabOperation(operation);
	}
}

/**
 * @author Dennis Cosgrove
 */
public class OpenProjectPane extends org.alice.ide.InputPanel<java.net.URI> {
	private SelectProjectToOpenPanel selectProjectToOpenPanel = new SelectProjectToOpenPanel();

	public OpenProjectPane() {
		this.addComponent(this.selectProjectToOpenPanel, java.awt.BorderLayout.CENTER);
	}

	public void refresh() {
		this.selectProjectToOpenPanel.refresh();
	}

	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.getActualInputValue() != null;
	}

	public void selectAppropriateTab(boolean isNew) {
		this.selectProjectToOpenPanel.selectAppropriateTab( isNew );
	}

	@Override
	protected java.net.URI getActualInputValue() {
		TabContentPanel tabContentPanel = this.selectProjectToOpenPanel.getSelectedTabContentPanel();
		if (tabContentPanel != null) {
			return tabContentPanel.getSelectedURI();
		} else {
			return null;
		}
	}
	// private edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation
	// tabSelectionOperation = new
	// edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation(
	// edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP,
	// java.util.UUID.fromString( "12e1d59b-2893-4144-b995-08090680a318" ));
	// private MyProjectsPane myProjectsPane = new MyProjectsPane();
	// private TabContentPanel templatesPane;
	// private FileSystemPane fileSystemPane = new FileSystemPane();
	// private RecentPane recentPane = new RecentPane();
	// public OpenProjectPane( TabContentPanel templatesPane ) {
	// this.templatesPane = templatesPane;
	// TabContentPanel[] tabPanes = new TabContentPanel[] {
	// this.myProjectsPane,
	// this.recentPane,
	// this.templatesPane,
	// this.fileSystemPane,
	// //new ExamplesPane(),
	// //new TextbookPane()
	// };
	// for( TabContentPanel tabPane : tabPanes ) {
	// if( tabPane != null ) {
	// tabPane.setInputPanel( this );
	// this.tabSelectionOperation.addTabIsSelectedStateOperation( tabPane );
	// //this.tabbedPane.addTab( tabPane.getTabTitleText(),
	// tabPane.getTabTitleIcon(), scrollPane );
	// }
	// }
	// // this.tabbedPane.addVetoableChangeListener( new
	// java.beans.VetoableChangeListener() {
	// // public void vetoableChange( java.beans.PropertyChangeEvent evt )
	// throws java.beans.PropertyVetoException {
	// this.tabSelectionOperation.addSelectionObserver( new
	// edu.cmu.cs.dennisc.croquet.TabbedPaneSelectionOperation.SelectionObserver()
	// {
	// public void
	// selecting(edu.cmu.cs.dennisc.croquet.TabIsSelectedStateOperation prev,
	// edu.cmu.cs.dennisc.croquet.TabIsSelectedStateOperation next) {
	// }
	// public void
	// selected(edu.cmu.cs.dennisc.croquet.TabIsSelectedStateOperation prev,
	// edu.cmu.cs.dennisc.croquet.TabIsSelectedStateOperation next) {
	// OpenProjectPane.this.updateOKButton();
	// }
	// } );
	// this.addComponent( this.tabSelectionOperation.getSingletonTabbedPane(),
	// java.awt.BorderLayout.CENTER );
	// }
	//	
	//	
	// // private TabContentPane getSelectedTabPane() {
	// // edu.cmu.cs.dennisc.croquet.Component< ? > selectedComponent =
	// this.tabbedPane.getSelectedComponent();
	// // if( selectedComponent instanceof edu.cmu.cs.dennisc.croquet.ScrollPane
	// ) {
	// // edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane =
	// (edu.cmu.cs.dennisc.croquet.ScrollPane)selectedComponent;
	// // edu.cmu.cs.dennisc.croquet.Component< ? > view =
	// scrollPane.getViewportView();
	// // if( view instanceof TabContentPane ) {
	// // return (TabContentPane)view;
	// // } else {
	// // return null;
	// // }
	// // } else {
	// // return null;
	// // }
	// // }
	// @Override
	// protected java.net.URI getActualInputValue() {
	// TabContentPanel tabPane =
	// (TabContentPanel)this.tabSelectionOperation.getCurrentSelectedTabOperation();
	// if( tabPane != null ) {
	// return tabPane.getSelectedURI();
	// } else {
	// return null;
	// }
	// }
	//	
	// // @Override
	// // public java.awt.Dimension getPreferredSize() {
	// // int longerSideLength = 650;
	// // return new java.awt.Dimension( longerSideLength,
	// edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength(
	// longerSideLength ) );
	// // }
	// public void selectAppropriateTab( boolean isNew ) {
	// TabContentPanel tabPane;
	// if( isNew ) {
	// tabPane = this.templatesPane;
	// } else {
	// tabPane = this.myProjectsPane; //todo: recentPane?
	// }
	// this.tabSelectionOperation.setCurrentSelectedTabOperation( tabPane );
	// }
	// public void refresh() {
	// this.myProjectsPane.refresh();
	// }
	//
	// @Override
	// public boolean isOKButtonValid() {
	// return super.isOKButtonValid() && this.getActualInputValue() != null;
	// }
	// // public static void main( String[] args ) {
	// //// java.util.List<String> paths = new java.util.LinkedList< String >();
	// //// paths.add(
	// "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/b.a3p"
	// );
	// //// paths.add(
	// "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/a.a3p"
	// );
	// //// paths.add(
	// "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/c.a3p"
	// );
	// ////
	// org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.setAndCommitValue(
	// paths );
	// //// java.util.List<String> postPaths =
	// org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.getValue();
	// //// edu.cmu.cs.dennisc.print.PrintUtilities.println( postPaths );
	// //
	// // org.alice.ide.FauxIDE ide = new org.alice.ide.FauxIDE();
	// //
	// // OpenProjectPane openProjectPane = new OpenProjectPane( null );
	// // openProjectPane.setPreferredSize( new java.awt.Dimension( 640, 480 )
	// );
	// // java.net.URI uri = openProjectPane.showInJDialog( null );
	// // if( uri != null ) {
	// // edu.cmu.cs.dennisc.print.PrintUtilities.println( uri );
	// // }
	// // System.exit( 0 );
	// // }
}
