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

package org.alice.ide.openprojectpane;

abstract class ComingSoonTabPane extends TabContentPane {
	public ComingSoonTabPane() {
		setEnabled( false );
		setToolTipText( "coming soon" );
	}
	@Override
	public java.net.URI getSelectedURI() {
		return null;
	}
}

class TutorialPane extends ComingSoonTabPane {
	@Override
	public String getTabTitleText() {
		return "Tutorial";
	}
}

class MyProjectsPane extends DirectoryListPane {
	@Override
	public java.io.File getDirectory() {
		return org.alice.ide.IDE.getSingleton().getMyProjectsDirectory();
	}
	@Override
	public String getTabTitleText() {
		return "My Projects";
	}
}

abstract class ApplicationRootDirectoryListPane extends DirectoryListPane {
	protected abstract String getSubPath();
	@Override
	public java.io.File getDirectory() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			return new java.io.File( ide.getApplicationRootDirectory(), this.getSubPath() );
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
		
		javax.swing.JLabel prefixLabel = new javax.swing.JLabel( "file:" );
		javax.swing.JPanel pane = new javax.swing.JPanel();
		pane.setOpaque( false );
		pane.setLayout( new java.awt.BorderLayout() );
		pane.add( prefixLabel, java.awt.BorderLayout.WEST );
		pane.add( this.textField, java.awt.BorderLayout.CENTER );
		pane.add( browseButtton, java.awt.BorderLayout.EAST );

		this.setLayout( new java.awt.BorderLayout() );
		this.add( pane, java.awt.BorderLayout.NORTH );
	}
	private void handleBrowse( java.awt.event.ActionEvent e ) {
		java.io.File file = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showOpenFileDialog( this, org.alice.ide.IDE.getSingleton().getMyProjectsDirectory(), null, edu.cmu.cs.dennisc.alice.io.FileUtilities.PROJECT_EXTENSION, true );
		if( file != null ) {
			this.textField.setText( edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file ) );
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
public class OpenProjectPane extends edu.cmu.cs.dennisc.zoot.ZInputPane< java.net.URI > {
	private edu.cmu.cs.dennisc.zoot.ZTabbedPane tabbedPane = new edu.cmu.cs.dennisc.zoot.ZTabbedPane();
	private MyProjectsPane myProjectsPane = new MyProjectsPane();
	private TabContentPane templatesPane;
	private FileSystemPane fileSystemPane = new FileSystemPane();
	private RecentPane recentPane = new RecentPane();
	
	private java.util.Map< TabContentPane, javax.swing.JScrollPane > mapTabPaneToScrollPane = new java.util.HashMap< TabContentPane, javax.swing.JScrollPane >();
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
				tabPane.setInputPane( this );
				javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( tabPane );
				scrollPane.setOpaque( false );
				scrollPane.setBackground( tabPane.getBackground() );
				scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
				//scrollPane.setHorizontalScrollBarPolicy( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
				//scrollPane.setVerticalScrollBarPolicy( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
				
				this.mapTabPaneToScrollPane.put( tabPane, scrollPane );
				this.tabbedPane.addTab( tabPane.getTabTitleText(), tabPane.getTabTitleIcon(), scrollPane );
			}
		}
//		this.tabbedPane.addVetoableChangeListener( new java.beans.VetoableChangeListener() {
//			public void vetoableChange( java.beans.PropertyChangeEvent evt ) throws java.beans.PropertyVetoException {
		this.tabbedPane.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				OpenProjectPane.this.updateOKButton();
			}
		} );
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( this.tabbedPane );
	}
	
	private TabContentPane getSelectedTabPane() {
		java.awt.Component selectedComponent = this.tabbedPane.getSelectedComponent();
		if( selectedComponent instanceof javax.swing.JScrollPane ) {
			javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)selectedComponent;
			scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
			java.awt.Component view = scrollPane.getViewport().getView();
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
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		int longerSideLength = 650;
		return new java.awt.Dimension( longerSideLength, edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( longerSideLength ) );
	}
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
	public static void main( String[] args ) {
//		java.util.List<String> paths = new java.util.LinkedList< String >();
//		paths.add( "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/b.a3p" );
//		paths.add( "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/a.a3p" );
//		paths.add( "C:/Documents and Settings/estrian/My Documents/Alice3/MyProjects/c.a3p" );
//		org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.setAndCommitValue( paths );
//		java.util.List<String> postPaths = org.alice.ide.preferences.GeneralPreferences.getSingleton().recentProjectPaths.getValue();
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( postPaths );
		
		org.alice.ide.FauxIDE ide = new org.alice.ide.FauxIDE();
		
		OpenProjectPane openProjectPane = new OpenProjectPane( null );
		openProjectPane.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		java.net.URI uri = openProjectPane.showInJDialog( null );
		if( uri != null ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( uri );
		}
		System.exit( 0 );
	}
}
