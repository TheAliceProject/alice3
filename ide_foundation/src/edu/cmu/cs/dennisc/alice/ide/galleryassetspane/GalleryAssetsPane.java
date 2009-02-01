package edu.cmu.cs.dennisc.alice.ide.galleryassetspane;

abstract class AbstractPathControl extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
	public abstract void setCurrentDirectory( java.io.File directory );
}

class PathControl extends AbstractPathControl {
	class DirectoryControl extends edu.cmu.cs.dennisc.zoot.ZPane {
		private static final int ARROW_SIZE = 10;
		class SelectDirectoryActionOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
			public SelectDirectoryActionOperation() {
				this.putValue( javax.swing.Action.NAME, DirectoryControl.this.file.getName() );
			}
			public void perform() {
				PathControl.this.handleSelectDirectory( DirectoryControl.this.file );
			}
		}
		class SelectChildDirectoryActionOperation extends edu.cmu.cs.dennisc.zoot.AbstractActionOperation {
			public SelectChildDirectoryActionOperation() {
				//this.putValue( javax.swing.Action.SMALL_ICON, ARROW_ICON );
			}
			public void perform() {
				PathControl.this.handleSelectChildDirectory( DirectoryControl.this.file );
			}
		}
		private java.io.File file;
		public DirectoryControl( java.io.File file ) {
			this.file = file;
			edu.cmu.cs.dennisc.zoot.ZButton selectButton = new edu.cmu.cs.dennisc.zoot.ZButton( new SelectDirectoryActionOperation() );
			edu.cmu.cs.dennisc.zoot.ZButton selectChildButton = new edu.cmu.cs.dennisc.zoot.ZButton( new SelectChildDirectoryActionOperation() ) {
				@Override
				protected void paintComponent(java.awt.Graphics g) {
					super.paintComponent( g );
					edu.cmu.cs.dennisc.awt.GraphicsUtilties.Heading heading;
					if( this.getModel().isPressed() ) {
						heading = edu.cmu.cs.dennisc.awt.GraphicsUtilties.Heading.SOUTH; 
					} else {
						heading = edu.cmu.cs.dennisc.awt.GraphicsUtilties.Heading.EAST; 
					}
					java.awt.Rectangle bounds = javax.swing.SwingUtilities.getLocalBounds( this );
					edu.cmu.cs.dennisc.awt.GraphicsUtilties.fillTriangle( g, heading, edu.cmu.cs.dennisc.awt.RectangleUtilties.createCenteredRectangle( bounds, DirectoryControl.ARROW_SIZE, DirectoryControl.ARROW_SIZE ) );
				}
				@Override
				public java.awt.Dimension getPreferredSize() {
					final int SIZE = ARROW_SIZE + 8;
					return new java.awt.Dimension( SIZE, SIZE );
				}
			};
			//selectChildButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
			this.setLayout( new java.awt.BorderLayout() );
			this.add( selectButton, java.awt.BorderLayout.CENTER );
			this.add( selectChildButton, java.awt.BorderLayout.EAST );
		}
	}
	private java.io.File rootDirectory;
	public PathControl( java.io.File rootDirectory ) {
		this.rootDirectory = rootDirectory;
		setCurrentDirectory( rootDirectory );
	}
	@Override
	public void setCurrentDirectory( java.io.File nextDirectory ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.rootDirectory );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( nextDirectory );
		java.util.Stack< java.io.File > ancestors = new java.util.Stack< java.io.File >();
		java.io.File d = nextDirectory;
		while( d.equals( this.rootDirectory ) == false ) {
			d = d.getParentFile();
			ancestors.add( d );
		}
		this.removeAll();
		while( ancestors.size() > 0 ) {
			java.io.File ancestor = ancestors.pop();
			this.add( new DirectoryControl( ancestor ) );
		}
		this.add( new DirectoryControl( nextDirectory ) );
		this.revalidate();
		this.repaint();
	}
	private void handleSelectDirectory( java.io.File directory ) {
		setCurrentDirectory( directory );
	}
	private void handleSelectChildDirectory( java.io.File directory ) {
	}
}

class ThumbnailSnapshotListCellRenderer extends edu.cmu.cs.dennisc.alice.ide.swing.SnapshotListCellRenderer {
	private javax.swing.Icon folderIcon;

	public void setFolderIcon( javax.swing.Icon folderIcon ) {
		this.folderIcon = folderIcon;
	}
	@Override
	protected javax.swing.JLabel updateLabel( javax.swing.JLabel rv, Object value ) {
		java.io.File file = (java.io.File)value;
		if( file != null ) {
			String name = file.getName();
			java.io.File iconFile;
			if( file.isDirectory() ) {
				name = "package: " + name;
				iconFile = new java.io.File( file, "directoryThumbnail.png" );
			} else {
				name = name.substring( 0, name.length() - 4 );
				iconFile = file;
			}
			rv.setText( name );
			javax.swing.Icon icon;
			if( iconFile.exists() ) {
				String path = edu.cmu.cs.dennisc.io.FileUtilities.getCanonicalPathIfPossible( iconFile );
				icon = new javax.swing.ImageIcon( path );
			} else {
				icon = null;
			}
			if( file.isDirectory() ) {
				if( this.folderIcon != null ) {
					if( icon != null ) {
						icon = new edu.cmu.cs.dennisc.swing.CompositeIcon( this.folderIcon, icon );
					} else {
						icon = this.folderIcon;
					}
				}
			}
			rv.setIcon( icon );
		}
		return rv;
	}
}

abstract class ThumbnailsPane extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
	private javax.swing.JList list = new javax.swing.JList();
	private ThumbnailSnapshotListCellRenderer thumbnailSnapshotListCellRenderer = new ThumbnailSnapshotListCellRenderer();

	public ThumbnailsPane() {
		this.list.setCellRenderer( this.thumbnailSnapshotListCellRenderer );
		this.list.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP );
		this.list.setVisibleRowCount( 1 );
		this.list.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					java.io.File file = (java.io.File)ThumbnailsPane.this.list.getSelectedValue();
					if( file != null ) {
						ThumbnailsPane.this.handleFileActivation( file );
					}
				}
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}
		} );
		this.add( new javax.swing.JScrollPane( this.list ) );
	}

	public void setFolderIcon( javax.swing.Icon folderIcon ) {
		this.thumbnailSnapshotListCellRenderer.setFolderIcon( folderIcon );
	}
	protected abstract void handleFileActivation( java.io.File file );
	public void setDirectory( java.io.File directory ) {
		java.io.File[] directories = edu.cmu.cs.dennisc.io.FileUtilities.listDirectories( directory );
		//java.io.File[] thumbnails = edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, "png" );
		java.io.File[] thumbnails = edu.cmu.cs.dennisc.io.FileUtilities.listFiles( directory, new java.io.FileFilter() {
			public boolean accept( java.io.File file ) {
				return file.isFile() && file.getName().equals( "directoryThumbnail.png" ) == false;
			}
		} );

		java.util.Vector< java.io.File > data = new java.util.Vector< java.io.File >();
		data.ensureCapacity( directories.length + thumbnails.length );
		for( java.io.File file : directories ) {
			data.add( file );
		}
		for( java.io.File file : thumbnails ) {
			data.add( file );
		}
		this.list.setListData( data );
	}
}

public abstract class GalleryAssetsPane extends edu.cmu.cs.dennisc.zoot.ZPane {
	private ThumbnailsPane thumbnailsPane = new ThumbnailsPane() {
		@Override
		protected void handleFileActivation( java.io.File file ) {
			GalleryAssetsPane.this.handleFileActivationFromThumbnails( file );
		}
	};
	private AbstractPathControl pathControl;

	public GalleryAssetsPane() {
		this.thumbnailsPane.setDirectory( this.getRootDirectory() );
		this.thumbnailsPane.setFolderIcon( this.createFolderIcon() );

		this.pathControl = this.createPathControl();
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.pathControl, java.awt.BorderLayout.NORTH );
		this.add( this.thumbnailsPane, java.awt.BorderLayout.CENTER );
	}
	protected abstract javax.swing.Icon createFolderIcon();
	protected abstract AbstractPathControl createPathControl();
	protected abstract java.io.File getRootDirectory();
	protected void handleFileActivationFromThumbnails( java.io.File file ) {
		if( file.isDirectory() ) {
			this.pathControl.setCurrentDirectory( file );
		}
	}
	protected void handleDirectoryActivationFromPathControl( java.io.File directory ) {
		this.thumbnailsPane.setDirectory( directory );
	}
	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new GalleryAssetsPane() {
			@Override
			protected javax.swing.Icon createFolderIcon() {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: remove absolute path" );
				return new javax.swing.ImageIcon( "C:/Program Files/Alice/3.beta.0000/gallery/folder.png" );
			}
			@Override
			protected java.io.File getRootDirectory() {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: remove absolute path" );
				return new java.io.File( "C:/Program Files/Alice/3.beta.0000/gallery/thumbnails" );
			}
			@Override
			protected AbstractPathControl createPathControl() {
//				return new AbstractPathControl() {
//					@Override
//					public void setCurrentDirectory( java.io.File directory ) {
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleFileActivation:", directory );
//					}
//				};
				return new PathControl( this.getRootDirectory() ) {
					@Override
					public void setCurrentDirectory(java.io.File nextDirectory) {
						super.setCurrentDirectory( nextDirectory );
						handleDirectoryActivationFromPathControl( nextDirectory );
					}
				};
			}
		} );
		frame.setSize( 1024, 320 );
		frame.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		frame.setVisible( true );
	}
}
