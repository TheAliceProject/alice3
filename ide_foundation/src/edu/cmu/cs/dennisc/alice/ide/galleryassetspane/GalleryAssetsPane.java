package edu.cmu.cs.dennisc.alice.ide.galleryassetspane;

abstract class AbstractPathControl extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
	protected abstract void handleFileActivation( java.io.File file );
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
				name = name.substring( 0, name.length()-4 );
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
			public void mouseExited(java.awt.event.MouseEvent e) {
			}
			public void mousePressed(java.awt.event.MouseEvent e) {
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
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
			GalleryAssetsPane.this.handleFileActivation( file );
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
	protected void handleFileActivation( java.io.File file ) {
		this.pathControl.handleFileActivation( file );
	}
	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new GalleryAssetsPane() {
			@Override
			protected javax.swing.Icon createFolderIcon() {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: remove absolute path" );
				return new javax.swing.ImageIcon( "C:/Program Files (x86)/Alice/3.beta.0000/gallery/folder.png" );
			}
			@Override
			protected java.io.File getRootDirectory() {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: remove absolute path" );
				return new java.io.File( "C:/Program Files (x86)/Alice/3.beta.0000/gallery/thumbnails/org.alice.apis.moveandturn.gallery/animals" );
			}
			@Override
			protected AbstractPathControl createPathControl() {
				return new AbstractPathControl() {
					@Override
					protected void handleFileActivation( java.io.File file ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleFileActivation:", file );
					}
				};
			}
		} );
		frame.setSize( 1024, 320 );
		frame.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		frame.setVisible( true );
	}
}
