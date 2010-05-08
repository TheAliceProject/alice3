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

abstract class ComingSoonTabPane extends TabContentPane {
	public ComingSoonTabPane( java.util.UUID individualId ) {
		super( individualId );
		setEnabled( false );
		setToolTipText( "coming soon" );
	}
	@Override
	public java.net.URI getSelectedURI() {
		return null;
	}
}

class TutorialPane extends ComingSoonTabPane {
	public TutorialPane() {
		super( java.util.UUID.fromString( "f4ff59f1-cf15-4301-a17a-2d80a4ea6fa4" ) );
	}
	@Override
	public String getTabTitleText() {
		return "Tutorial";
	}
}

class MyProjectsPane extends DirectoryListPane {
	public MyProjectsPane() {
		super( java.util.UUID.fromString( "c7fb9c47-f215-47dc-941e-872842ce397e" ) );
	}
	@Override
	public java.io.File getDirectory() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: MyProjectsPane" );
		if( org.alice.ide.IDE.getSingleton() != null ) {
			return org.alice.ide.IDE.getSingleton().getMyProjectsDirectory();
		} else {
			return org.alice.app.ProjectApplication.getSingleton().getMyProjectsDirectory();
		}
	}
	@Override
	public String getTabTitleText() {
		return "My Projects";
	}
}

abstract class ApplicationRootDirectoryListPane extends DirectoryListPane {
	public ApplicationRootDirectoryListPane( java.util.UUID individualId ) {
		super( individualId );
	}
	protected abstract String getSubPath();
	@Override
	public java.io.File getDirectory() {
		org.alice.app.ProjectApplication application = org.alice.app.ProjectApplication.getSingleton();
		if( application != null ) {
			return new java.io.File( application.getApplicationRootDirectory(), this.getSubPath() );
		} else {
			return null;
		}
	}
}

//class TemplatesPane extends ApplicationRootDirectoryListPane {
//	@Override
//	public String getSubPath() {
//		return "projects/templates";
//	}
//	@Override
//	public String getTabTitleText() {
//		return "Templates";
//	}
//}

class ExamplesPane extends ApplicationRootDirectoryListPane {
	public ExamplesPane() {
		super( java.util.UUID.fromString( "a8d38b0a-049e-4818-9b81-7c3b7b65a739" ) );
	}
	@Override
	public String getSubPath() {
		return "projects/examples";
	}
	@Override
	public String getTabTitleText() {
		return "Examples";
	}
}

class TextbookPane extends ApplicationRootDirectoryListPane {
	public TextbookPane() {
		super( java.util.UUID.fromString( "033afcdf-29b9-4fbf-b9f5-fb5c496a7860" ) );
	}
	@Override
	public String getSubPath() {
		return "projects/textbook";
	}
	@Override
	public String getTabTitleText() {
		return "Textbook";
	}
}

class FileSystemPane extends TabContentPane {
	private javax.swing.JTextField textField = new javax.swing.JTextField();
	public FileSystemPane() {
		super( java.util.UUID.fromString( "b1698424-1f0e-4499-852a-da627fa9e789" ) );
		this.textField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			private void handleUpdate( javax.swing.event.DocumentEvent e ) {
				FileSystemPane.this.updateOKButton();
			}
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				this.handleUpdate( e );
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				this.handleUpdate( e );
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				this.handleUpdate( e );
			}
		} );
		javax.swing.JButton browseButtton = new javax.swing.JButton( "browse..." );
		browseButtton.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				FileSystemPane.this.handleBrowse( e );
			}
		} );
		
		edu.cmu.cs.dennisc.croquet.BorderPanel pane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		pane.setOpaque( false );
		pane.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "file:" ), edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.WEST );
		pane.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.textField ), edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.CENTER );
		pane.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( browseButtton ), edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.EAST );

		this.addComponent( pane, edu.cmu.cs.dennisc.croquet.BorderPanel.CardinalDirection.NORTH );
	}
	private void handleBrowse( java.awt.event.ActionEvent e ) {
		java.io.File file = edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showOpenFileDialog( this.getJComponent(), org.alice.app.ProjectApplication.getSingleton().getMyProjectsDirectory(), null, edu.cmu.cs.dennisc.alice.project.ProjectUtilities.PROJECT_EXTENSION, true );
		if( file != null ) {
			this.textField.setText( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
		}
	}
	@Override
	public java.net.URI getSelectedURI() {
		String path = this.textField.getText();
		java.io.File file = new java.io.File( path );
		if( file.exists() ) {
			return file.toURI();
		} else {
			return null;
		}
	}
	@Override
	public String getTabTitleText() {
		return "File System";
	}
}

/**
 * @author Dennis Cosgrove
 */
public class OpenProjectPane extends org.alice.ide.InputPanel< java.net.URI > {
	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabSelectionOperation = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "12e1d59b-2893-4144-b995-08090680a318" ));
	private edu.cmu.cs.dennisc.croquet.TabbedPane tabbedPane;
	private MyProjectsPane myProjectsPane = new MyProjectsPane();
	private TabContentPane templatesPane;
	private FileSystemPane fileSystemPane = new FileSystemPane();
	private RecentPane recentPane = new RecentPane();
	
	private java.util.Map< TabContentPane, edu.cmu.cs.dennisc.croquet.ScrollPane > mapTabPaneToScrollPane = edu.cmu.cs.dennisc.java.util.Collections.newWeakHashMap();
	public OpenProjectPane( TabContentPane templatesPane ) {
		this.templatesPane = templatesPane;
		TabContentPane[] tabPanes = new TabContentPane[] { 
			this.myProjectsPane, 
			this.recentPane, 
			this.templatesPane, 
			this.fileSystemPane, 
			//new ExamplesPane(), 
			//new TextbookPane() 
		};
		for( TabContentPane tabPane : tabPanes ) { 
			if( tabPane != null ) {
				tabPane.setInputPanel( this );
				
				//todo: add scrollpane
				
				edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( tabPane );
				scrollPane.setOpaque( false );
				scrollPane.setBackgroundColor( tabPane.getBackgroundColor() );
				scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
				//scrollPane.setHorizontalScrollBarPolicy( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
				//scrollPane.setVerticalScrollBarPolicy( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
				
				this.mapTabPaneToScrollPane.put( tabPane, scrollPane );
				
				this.tabSelectionOperation.addTabIsSelectedOperation( tabPane.getTabIsSelectedOperation() );
				//this.tabbedPane.addTab( tabPane.getTabTitleText(), tabPane.getTabTitleIcon(), scrollPane );
			}
		}
		this.tabbedPane = this.tabSelectionOperation.createTabbedPane();
//		this.tabbedPane.addVetoableChangeListener( new java.beans.VetoableChangeListener() {
//			public void vetoableChange( java.beans.PropertyChangeEvent evt ) throws java.beans.PropertyVetoException {
		this.tabbedPane.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				OpenProjectPane.this.updateOKButton();
			}
		} );
		this.addComponent( this.tabbedPane, java.awt.BorderLayout.CENTER );
	}
	
	
	private TabContentPane getSelectedTabPane() {
		edu.cmu.cs.dennisc.croquet.Component< ? > selectedComponent = this.tabbedPane.getSelectedComponent();
		if( selectedComponent instanceof edu.cmu.cs.dennisc.croquet.ScrollPane ) {
			edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = (edu.cmu.cs.dennisc.croquet.ScrollPane)selectedComponent;
			
			
			//todo?
			scrollPane.getJComponent().getVerticalScrollBar().setUnitIncrement( 12 );
			
			
			edu.cmu.cs.dennisc.croquet.Component< ? > view = scrollPane.getViewportView();
			if( view instanceof TabContentPane ) {
				return (TabContentPane)view;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	@Override
	protected java.net.URI getActualInputValue() {
		TabContentPane tabPane = this.getSelectedTabPane();
		if( tabPane != null ) {
			return tabPane.getSelectedURI();
		} else {
			return null;
		}
	}
	
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		int longerSideLength = 650;
//		return new java.awt.Dimension( longerSideLength, edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( longerSideLength ) );
//	}
	public void selectAppropriateTab( boolean isNew ) {
		TabContentPane tabPane;
		if( isNew ) {
			tabPane = this.templatesPane;
		} else {
			tabPane = this.myProjectsPane; //todo: recentPane?
		}
		if( tabPane != null ) {
			this.tabbedPane.setSelectedComponent( this.mapTabPaneToScrollPane.get( tabPane ) );
		}
	}
	public void refresh() {
		this.myProjectsPane.refresh();
	}

	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.getActualInputValue() != null;
	}
//	public static void main( String[] args ) {
////		java.util.List<String> paths = new java.util.LinkedList< String >();
////		paths.add( "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/b.a3p" );
////		paths.add( "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/a.a3p" );
////		paths.add( "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/c.a3p" );
////		org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.setAndCommitValue( paths );
////		java.util.List<String> postPaths = org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.getValue();
////		edu.cmu.cs.dennisc.print.PrintUtilities.println( postPaths );
//		
//		org.alice.ide.FauxIDE ide = new org.alice.ide.FauxIDE();
//		
//		OpenProjectPane openProjectPane = new OpenProjectPane( null );
//		openProjectPane.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
//		java.net.URI uri = openProjectPane.showInJDialog( null );
//		if( uri != null ) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( uri );
//		}
//		System.exit( 0 );
//	}
}
