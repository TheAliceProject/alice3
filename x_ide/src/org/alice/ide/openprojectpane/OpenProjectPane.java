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

abstract class TabPane extends swing.Pane {
	//	protected boolean isTabEnabled() {
	//		return this.isEnabled();
	//	}
	private zoot.ZInputPane< java.io.File > inputPane;
	public TabPane() {
		this.setBackground( new java.awt.Color( 191, 191, 255 ) );
		this.setOpaque( true );
		final int INSET = 8;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
	}
	public void setInputPane( zoot.ZInputPane< java.io.File > inputPane ) {
		this.inputPane = inputPane;
	}
	public abstract java.io.File getSelectedFile();
	public javax.swing.Icon getTabTitleIcon() {
		return null;
	}
	public abstract String getTabTitleText();
	protected void updateOKButton() {
		if( this.inputPane != null ) {
			this.inputPane.updateOKButton();
		}
	}
	protected void fireOKButtonIfPossible() {
		if( this.inputPane != null ) {
			this.inputPane.fireOKButtonIfPossible();
		}
	}
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

class ProjectSnapshotListCellRenderer extends org.alice.ide.swing.SnapshotListCellRenderer {
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
	@Override
	protected javax.swing.JLabel updateLabel( javax.swing.JLabel rv, Object value ) {
		java.io.File file = (java.io.File)value;
		javax.swing.Icon icon;
		
		
		
		//todo: remove
		String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file );
		if( path != null ) {
			String snapshotPath = path.substring( 0, path.length()-3 ) + "png";
			if( edu.cmu.cs.dennisc.io.FileUtilities.existsAndHasLengthGreaterThanZero( snapshotPath ) ) {
				icon = new javax.swing.ImageIcon( snapshotPath );
			} else {
				icon = null;
			}
		} else {
			icon = null;
		}
		
		
		
		if( icon != null ) {
			//pass
		} else {
			try {
				java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile( file );
				java.util.zip.ZipEntry zipEntry = zipFile.getEntry( "thumbnail.png" );
				if( zipEntry != null ) {
					java.io.InputStream is = zipFile.getInputStream( zipEntry );
					java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read(edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is);
					icon = new javax.swing.ImageIcon( image );
				} else {
					icon = NOT_AVAILABLE_ICON;
				}
				zipFile.close();
			} catch( Throwable t ) {
				icon = NOT_AVAILABLE_ICON;
			}
		}
		rv.setText( file.getName() );
		rv.setIcon( icon );
		return rv;
	}
}

//class ProjectSnapshotListCellRenderer implements javax.swing.ListCellRenderer {
//	private static final javax.swing.Icon NOT_AVAILABLE_ICON = new javax.swing.Icon() {
//		public int getIconWidth() {
//			return 160;
//		}
//		public int getIconHeight() {
//			return 120;
//		}
//		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
//			int width = this.getIconWidth();
//			int height = this.getIconHeight();
//			g.setColor( java.awt.Color.DARK_GRAY );
//			g.fillRect( x, y, width, height );
//			g.setColor( java.awt.Color.LIGHT_GRAY );
//			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "snapshot not available", x, y, width, height );
//		}
//	};
//
//	private final int PANEL_INSET = 4;
//	private final int LABEL_INSET = 8;
//	private javax.swing.JPanel panel = new javax.swing.JPanel();
//	private javax.swing.JLabel label = new javax.swing.JLabel();
//	public ProjectSnapshotListCellRenderer() {
//		this.panel.setOpaque( false );
//		this.label.setOpaque( true );
//		this.panel.setBorder( javax.swing.BorderFactory.createEmptyBorder( PANEL_INSET, PANEL_INSET, PANEL_INSET, PANEL_INSET ) );
//		this.label.setBorder( javax.swing.BorderFactory.createEmptyBorder( LABEL_INSET, LABEL_INSET, LABEL_INSET, LABEL_INSET ) );
//		this.label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
//		this.label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );
//		
//		this.panel.setLayout( new java.awt.GridLayout( 1, 1 ) );
//		this.panel.add( this.label );
//	}
//	
//	public java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
//		java.io.File file = (java.io.File)value;
//		String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file );
//		if( path != null ) {
//			this.label.setText( file.getName() );
//			String snapshotPath = path.substring( 0, path.length()-3 ) + "png";
//			javax.swing.Icon icon;
//			if( edu.cmu.cs.dennisc.io.FileUtilities.existsAndHasLengthGreaterThanZero( snapshotPath ) ) {
//				icon = new javax.swing.ImageIcon( snapshotPath );
//			} else {
//				icon = NOT_AVAILABLE_ICON;
//			}
//			this.label.setIcon( icon );
//		}
//		java.awt.Color background;
//		java.awt.Color foreground;
//		if( isSelected ) {
//			background = new java.awt.Color( 63, 63, 127 );
//			foreground = java.awt.Color.YELLOW;
//		} else {
//			background = java.awt.Color.WHITE;
//			foreground = java.awt.Color.BLACK;
//		}
//		this.label.setBackground( background );
//		this.label.setForeground( foreground );
//		return this.panel;
//	}
//}

//class ProjectSnapshotListCellRenderer extends javax.swing.DefaultListCellRenderer {
//	private static final javax.swing.Icon NOT_AVAILABLE_ICON = new javax.swing.Icon() {
//		public int getIconWidth() {
//			return 160;
//		}
//		public int getIconHeight() {
//			return 120;
//		}
//		public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
//			int width = this.getIconWidth();
//			int height = this.getIconHeight();
//			g.setColor( java.awt.Color.DARK_GRAY );
//			g.fillRect( x, y, width, height );
//			g.setColor( java.awt.Color.LIGHT_GRAY );
//			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "snapshot not available", x, y, width, height );
//		}
//	};
//	private final int INSET = 8;
//	@Override
//	public java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
//		java.awt.Component rv = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
//		if( rv instanceof javax.swing.JLabel ) {
//			javax.swing.JLabel label = (javax.swing.JLabel)rv;
//			label.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
//			java.io.File file = (java.io.File)value;
//			String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( file );
//			if( path != null ) {
//				label.setText( file.getName() );
//				String snapshotPath = path.substring( 0, path.length()-3 ) + "png";
//				javax.swing.Icon icon;
//				if( edu.cmu.cs.dennisc.io.FileUtilities.existsAndHasLengthGreaterThanZero( snapshotPath ) ) {
//					icon = new javax.swing.ImageIcon( snapshotPath );
//				} else {
//					icon = NOT_AVAILABLE_ICON;
//				}
//				label.setIcon( icon );
//			}
//			if( isSelected ) {
//				label.setBackground( new java.awt.Color( 127, 127, 255 ) );
//			}
//			label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
//			label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );
//			
//		}
//		return rv;
//	}
//}

abstract class DirectoryListPane extends TabPane {
	private javax.swing.JList list = new javax.swing.JList() {
		@Override
		public void paint(java.awt.Graphics g) {
			super.paint( g );
			if( this.getModel().getSize() > 0 ) {
				//pass
			} else {
				java.awt.Font font = this.getFont();
				font = font.deriveFont( java.awt.Font.ITALIC );
				g.setFont( font );
				edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "there are no projects in this directory", this.getSize() );
			}
		}
	};
	public DirectoryListPane() {
		this.refresh();
		this.list.setOpaque( false );
		this.list.setCellRenderer( new ProjectSnapshotListCellRenderer() );
		this.list.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.list.setVisibleRowCount( -1 );
		this.list.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					DirectoryListPane.this.fireOKButtonIfPossible();
				}
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
		this.list.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_F5 ) {
					DirectoryListPane.this.refresh();
				}
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		} );
		this.list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					DirectoryListPane.this.updateOKButton();
				}
			}
		} );
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( this.list );
	}

	public void refresh() {
		java.io.File directory = this.getDirectory();
		java.io.File[] files;
		if( directory != null ) {
			files = edu.cmu.cs.dennisc.alice.io.FileUtilities.listProjectFiles( this.getDirectory() );
		} else {
			files = new java.io.File[ 0 ];
		}
		this.list.setListData( files );
	}

	protected abstract java.io.File getDirectory();
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
	public String getTabTitleText() {
		return "File System";
	}
}

/**
 * @author Dennis Cosgrove
 */
public class OpenProjectPane extends zoot.ZInputPane< java.io.File > {
	//private javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
	private zoot.ZTabbedPane tabbedPane = new zoot.ZTabbedPane();
	private MyProjectsPane myProjectsPane = new MyProjectsPane();
	private FileSystemPane fileSystemPane = new FileSystemPane();
	private TemplatesPane templatesPane = new TemplatesPane();
	
	private java.util.Map< TabPane, javax.swing.JScrollPane > mapTabPaneToScrollPane = new java.util.HashMap< TabPane, javax.swing.JScrollPane >();
	public OpenProjectPane() {
		TabPane[] tabPanes = new TabPane[] { 
			myProjectsPane, 
			templatesPane, 
			fileSystemPane, 
			//new ExamplesPane(), 
			//new TextbookPane() 
		};
		for( TabPane tabPane : tabPanes ) { 
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
	private TabPane getSelectedTabPane() {
		java.awt.Component selectedComponent = this.tabbedPane.getSelectedComponent();
		if( selectedComponent instanceof javax.swing.JScrollPane ) {
			javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)selectedComponent;
			scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
			java.awt.Component view = scrollPane.getViewport().getView();
			if( view instanceof TabPane ) {
				return (TabPane)view;
			} else {
				return null;
			}
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
	public java.awt.Dimension getPreferredSize() {
		int longerSideLength = 650;
		return new java.awt.Dimension( longerSideLength, edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( longerSideLength ) );
	}
	public void selectAppropriateTab( boolean isNew ) {
		TabPane tabPane;
		if( isNew ) {
			tabPane = this.templatesPane;
		} else {
			tabPane = this.myProjectsPane; //todo: recentPane?
		}
		this.tabbedPane.setSelectedComponent( this.mapTabPaneToScrollPane.get( tabPane ) );
	}
	public void refresh() {
		this.myProjectsPane.refresh();
	}

	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.getActualInputValue() != null;
	}
	public static void main( String[] args ) {
		OpenProjectPane openProjectPane = new OpenProjectPane();
		openProjectPane.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		java.io.File file = openProjectPane.showInJDialog( null );
		if( file != null ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( file );
		}
		System.exit( 0 );
	}
}
