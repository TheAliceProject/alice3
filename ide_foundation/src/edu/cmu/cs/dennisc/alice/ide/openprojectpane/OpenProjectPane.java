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

package edu.cmu.cs.dennisc.alice.ide.openprojectpane;

abstract class TabPane extends edu.cmu.cs.dennisc.zoot.ZPane {
	//	protected boolean isTabEnabled() {
	//		return this.isEnabled();
	//	}
	public TabPane() {
		setOpaque( false );
	}
	public abstract java.io.File getSelectedFile();
	public javax.swing.Icon getTabTitleIcon() {
		return null;
	}
	public abstract String getTabTitleText();
}

abstract class ComingSoonTabPane extends TabPane {
	public ComingSoonTabPane() {
		setEnabled( false );
		setToolTipText( "coming soon" );
	}
	@Override
	public java.io.File getSelectedFile() {
		return null;
	}
}

class TutorialPane extends ComingSoonTabPane {
	@Override
	public String getTabTitleText() {
		return "Tutorial";
	}
}

class RecentPane extends ComingSoonTabPane {
	@Override
	public String getTabTitleText() {
		return "Recent";
	}
}

class ProjectSnapshotListCellRenderer extends javax.swing.DefaultListCellRenderer {
	private static final javax.swing.Icon NOT_AVAILABLE_ICON = new javax.swing.Icon() {
		public int getIconWidth() {
			return 160;
		}
		public int getIconHeight() {
			return 120;
		}
		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
			int width = this.getIconWidth();
			int height = this.getIconHeight();
			g.setColor( java.awt.Color.DARK_GRAY );
			g.fillRect( x, y, width, height );
			g.setColor( java.awt.Color.LIGHT_GRAY );
			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "snapshot not available", x, y, width, height );
		}
	};
	private final int INSET = 8;
	@Override
	public java.awt.Component getListCellRendererComponent( javax.swing.JList list, java.lang.Object value, int index, boolean isSelected, boolean cellHasFocus ) {
		java.awt.Component rv = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
		if( rv instanceof javax.swing.JLabel ) {
			javax.swing.JLabel label = (javax.swing.JLabel)rv;
			label.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
			java.io.File file = (java.io.File)value;
			String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file );
			if( path != null ) {
				label.setText( file.getName() );
				String snapshotPath = path.substring( 0, path.length()-3 ) + "png";
				javax.swing.Icon icon;
				if( edu.cmu.cs.dennisc.io.FileUtilities.existsAndHasLengthGreaterThanZero( snapshotPath ) ) {
					icon = new javax.swing.ImageIcon( snapshotPath );
				} else {
					icon = NOT_AVAILABLE_ICON;
				}
				label.setIcon( icon );
			}
			label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
			label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );
			
		}
		return rv;
	}
	
}

abstract class DirectoryListPane extends TabPane {
	private class DirectoryListModel implements javax.swing.ListModel {
		public void addListDataListener( javax.swing.event.ListDataListener l ) {
		}
		public void removeListDataListener( javax.swing.event.ListDataListener l ) {
		}
		public Object getElementAt( int index ) {
			return DirectoryListPane.this.getFiles()[ index ];
		}
		public int getSize() {
			return DirectoryListPane.this.getFiles().length;
		}
	}

	private javax.swing.JList list = new javax.swing.JList() {
		@Override
		public void paint(java.awt.Graphics g) {
			if( this.getModel().getSize() > 0 ) {
				super.paint( g );
			} else {
				java.awt.Font font = this.getFont();
				font = font.deriveFont( java.awt.Font.ITALIC );
				g.setFont( font );
				edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "there are no projects in this directory", this.getSize() );
			}
		}
	};
	private java.io.File[] files = null;

	public DirectoryListPane() {
		this.list.setModel( new DirectoryListModel() );
		this.list.setCellRenderer( new ProjectSnapshotListCellRenderer() );
		this.list.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.list.setVisibleRowCount( -1 );
		this.list.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited(java.awt.event.MouseEvent e) {
			}
			public void mousePressed(java.awt.event.MouseEvent e) {
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
			}
		} );
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( this.list );
	}

	protected abstract java.io.File getDirectory();
	private java.io.File[] getFiles() {
		if( this.files != null ) {
			//pass
		} else {
			java.io.File directory = this.getDirectory();
			if( directory != null ) {
				this.files = edu.cmu.cs.dennisc.alice.io.FileUtilities.listProjectFiles( directory );
			} else {
				this.files = new java.io.File[ 0 ];
			}
		}
		return this.files;
	}
	@Override
	public java.io.File getSelectedFile() {
		Object selectedValue = this.list.getSelectedValue();
		if( selectedValue instanceof java.io.File ) {
			return (java.io.File)selectedValue;
		} else {
			return null;
		}
	}
}

class MyProjectsPane extends DirectoryListPane {
	@Override
	public java.io.File getDirectory() {
		return edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory();
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
		edu.cmu.cs.dennisc.alice.ide.IDE ide = edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			return new java.io.File( ide.getApplicationRootDirectory(), this.getSubPath() );
		} else {
			return null;
		}
	}
}

class TemplatesPane extends ApplicationRootDirectoryListPane {
	@Override
	public String getSubPath() {
		return "projects/templates";
	}
	@Override
	public String getTabTitleText() {
		return "Templates";
	}
}

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

class FileSystemPane extends TabPane {
	private javax.swing.JTextField textField = new javax.swing.JTextField();
	public FileSystemPane() {
		this.textField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			}
		} );
		javax.swing.JButton browseButtton = new javax.swing.JButton( "browse..." );
		browseButtton.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
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
	@Override
	public java.io.File getSelectedFile() {
		String path = this.textField.getText();
		java.io.File file = new java.io.File( path );
		if( file.exists() ) {
			return file;
		} else {
			return null;
		}
	}
	@Override
	public java.lang.String getTabTitleText() {
		return "File System";
	}
}

/**
 * @author Dennis Cosgrove
 */
public class OpenProjectPane extends edu.cmu.cs.dennisc.swing.InputPane< java.io.File > {
	private javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();

	public OpenProjectPane() {
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( this.tabbedPane );
		TabPane[] tabPanes = new TabPane[] { 
			new MyProjectsPane(), 
			new FileSystemPane(), 
			new TemplatesPane(), 
			//new ExamplesPane(), 
			//new TextbookPane() 
		};
		for( TabPane tabPane : tabPanes ) { 
			this.tabbedPane.addTab( tabPane.getTabTitleText(), tabPane.getTabTitleIcon(), tabPane );
		}
	}
	private TabPane getSelectedTabPane() {
		java.awt.Component selectedComponent = this.tabbedPane.getSelectedComponent();
		if( selectedComponent instanceof TabPane ) {
			return (TabPane)selectedComponent;
		} else {
			return null;
		}
	}
	@Override
	protected java.io.File getActualInputValue() {
		TabPane tabPane = this.getSelectedTabPane();
		if( tabPane != null ) {
			return tabPane.getSelectedFile();
		} else {
			return null;
		}
	}

	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.getActualInputValue() != null;
	}
	public static void main( String[] args ) {
		OpenProjectPane openProjectPane = new OpenProjectPane();
		openProjectPane.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		openProjectPane.showInJDialog( null );
	}
}
