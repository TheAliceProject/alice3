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
package edu.cmu.cs.dennisc.alice.ide.gallerypane;

/**
 * @author Dennis Cosgrove
 */
public abstract class PathControl extends edu.cmu.cs.dennisc.moot.ZLineAxisPane {
	class DirectoryControl extends edu.cmu.cs.dennisc.moot.ZPane {
		private static final int ARROW_SIZE = 10;
		class SelectDirectoryActionOperation extends edu.cmu.cs.dennisc.moot.AbstractActionOperation {
			public SelectDirectoryActionOperation() {
				this.putValue( javax.swing.Action.NAME, PathControl.this.getTextFor( DirectoryControl.this.file ) );
			}
			public void perform() {
				PathControl.this.handleSelectDirectory( DirectoryControl.this.file );
			}
		}
		class SelectChildDirectoryActionOperation extends edu.cmu.cs.dennisc.moot.AbstractActionOperation /*implements edu.cmu.cs.dennisc.moot.ResponseOperation*/ {
			//public void respond( java.util.EventObject e ) {
			//}
			public void perform() {
				DirectoryControl.this.handleSelectChildDirectory();
			}
		}
		private edu.cmu.cs.dennisc.moot.ZButton selectButton;
		private edu.cmu.cs.dennisc.moot.ZButton selectChildButton;
		private java.io.File file;
		public DirectoryControl( java.io.File file ) {
			this.file = file;
			this.selectButton = new edu.cmu.cs.dennisc.moot.ZButton( new SelectDirectoryActionOperation() );
			this.selectChildButton = new edu.cmu.cs.dennisc.moot.ZButton( new SelectChildDirectoryActionOperation() ) {
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
			this.add( this.selectButton, java.awt.BorderLayout.CENTER );
			this.add( this.selectChildButton, java.awt.BorderLayout.EAST );
		}
		private void handleSelectChildDirectory() {
			PathControl.this.handleSelectChildDirectory( this.selectChildButton, this.file );
		}
	
	}
	private java.io.File rootDirectory;
	private java.io.File currentDirectory;
	private javax.swing.Icon folderIconSmall;
	private javax.swing.Icon fileIconSmall;
	public PathControl( java.io.File rootDirectory ) {
		this.rootDirectory = rootDirectory;
		setCurrentDirectory( rootDirectory );
	}

	public void setFolderIconSmall( javax.swing.Icon folderIconSmall ) {
		this.folderIconSmall = folderIconSmall;
		this.fileIconSmall = new javax.swing.Icon() {
			public int getIconWidth() {
				return PathControl.this.folderIconSmall.getIconWidth();
			}
			public int getIconHeight() {
				return PathControl.this.folderIconSmall.getIconHeight();
			}
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
			}
		};
	}
	public void setCurrentDirectoryUpOneLevelIfAppropriate() {
		if( this.currentDirectory.equals( this.rootDirectory ) ) {
			//pass
		} else {
			setCurrentDirectory( this.currentDirectory.getParentFile() );
		}
	}
	protected abstract String getTextFor( java.io.File file );
	public void setCurrentDirectory( java.io.File nextDirectory ) {
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
		this.currentDirectory = nextDirectory;
		this.revalidate();
		this.repaint();
	}
	private void handleSelectDirectory( java.io.File directory ) {
		setCurrentDirectory( directory );
	}
	protected abstract void handleSelectFile( java.io.File file );
	private void handleSelectChildDirectory( java.awt.Component invoker, java.io.File directory ) {
		java.io.File[] packages = ThumbnailsPane.listPackages( directory );
		java.io.File[] classes = ThumbnailsPane.listClasses( directory );

		class SelectFileActionOperation extends edu.cmu.cs.dennisc.moot.AbstractActionOperation {
			private java.io.File file;
			public SelectFileActionOperation( java.io.File file ) {
				this.file = file;
				this.putValue( javax.swing.Action.NAME, PathControl.this.getTextFor( this.file ) );
				javax.swing.Icon icon;
				if( this.file.isDirectory() ) {
					icon = PathControl.this.folderIconSmall;
				} else {
					icon = PathControl.this.fileIconSmall;
				}
				this.putValue( javax.swing.Action.SMALL_ICON, icon );
			}
			public void perform() {
				if( this.file.isDirectory() ) {
					PathControl.this.handleSelectDirectory( this.file );
				} else {
					PathControl.this.handleSelectFile( this.file );
				}
			}
		}

		javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
		for( java.io.File file : packages ) {
			SelectFileActionOperation operation = new SelectFileActionOperation( file );
			popupMenu.add( operation.getActionForConfiguringSwingComponents() );
		}
		for( java.io.File file : classes ) {
			SelectFileActionOperation operation = new SelectFileActionOperation( file );
			popupMenu.add( operation.getActionForConfiguringSwingComponents() );
		}
		popupMenu.show( invoker, 0, invoker.getHeight() );
	}
}
