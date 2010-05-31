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
package org.alice.ide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public abstract class PathControl extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
	class DirectoryControl extends edu.cmu.cs.dennisc.croquet.BorderPanel {
		private static final int ARROW_SIZE = 10;
		private static final int ARROW_BORDER_HALF_SIZE = 3;

		class SelectDirectoryActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
			public SelectDirectoryActionOperation() {
				super( java.util.UUID.fromString( "ca407baf-13b1-4530-bf35-67764efbf5f0" ) );
				this.setName( PathControl.this.getTextFor( DirectoryControl.this.file ) );
			}

			@Override
			protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
				PathControl.this.handleSelectDirectory( DirectoryControl.this.file );
			}
		}

		class SelectChildDirectoryActionOperation extends edu.cmu.cs.dennisc.croquet.AbstractPopupMenuOperation {
			public SelectChildDirectoryActionOperation() {
				super( java.util.UUID.fromString( "cc6a0de7-91b1-4a2b-86ff-21ca9de14bed" ) );
				javax.swing.Icon icon = new javax.swing.Icon() {
					public int getIconHeight() {
						return ARROW_SIZE + ARROW_BORDER_HALF_SIZE + ARROW_BORDER_HALF_SIZE;
					}

					public int getIconWidth() {
						return ARROW_SIZE + ARROW_BORDER_HALF_SIZE + ARROW_BORDER_HALF_SIZE;
					}

					public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
						if( c instanceof javax.swing.AbstractButton ) {
							javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
							edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading heading;
							if( button.getModel().isPressed() ) {
								heading = edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading.SOUTH;
							} else {
								heading = edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.Heading.EAST;
							}
							g.setColor( java.awt.Color.BLACK );
							edu.cmu.cs.dennisc.java.awt.GraphicsUtilties.fillTriangle( g, heading, x + ARROW_BORDER_HALF_SIZE, y + ARROW_BORDER_HALF_SIZE, DirectoryControl.ARROW_SIZE, DirectoryControl.ARROW_SIZE );
						}
					}
				};
				this.setSmallIcon( icon );
			}
			
			@Override
			public edu.cmu.cs.dennisc.croquet.Model[] getOperations() {
				return DirectoryControl.this.getOperations();
			}
		}

		private java.io.File file;

		//todo: remove. rely only on operations.
		private edu.cmu.cs.dennisc.croquet.Button selectChildButton;

		public DirectoryControl( java.io.File file ) {
			this.setMaximumSizeClampedToPreferredSize( true );
			this.file = file;
			this.selectChildButton = new SelectChildDirectoryActionOperation().createButton();
			selectChildButton.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
			this.addComponent( new SelectDirectoryActionOperation().createButton(), Constraint.CENTER );
			this.addComponent( selectChildButton, Constraint.EAST );
		}

		private edu.cmu.cs.dennisc.croquet.Model[] getOperations() {
			return PathControl.this.getOperations( this.file );
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

	public java.io.File getRootDirectory() {
		return this.rootDirectory;
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
			// pass
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
		this.removeAllComponents();
		while( ancestors.size() > 0 ) {
			java.io.File ancestor = ancestors.pop();
			this.addComponent( new DirectoryControl( ancestor ) );
			this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 2 ) );
		}
		this.addComponent( new DirectoryControl( nextDirectory ) );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue() );
		this.currentDirectory = nextDirectory;
		this.revalidateAndRepaint();
	}

	private void handleSelectDirectory( java.io.File directory ) {
		setCurrentDirectory( directory );
	}

	protected abstract void handleSelectFile( java.io.File file );

	private edu.cmu.cs.dennisc.croquet.Model[] getOperations( java.io.File directory ) {
		java.io.File[] packages = ThumbnailsPane.listPackages( directory );
		java.util.Arrays.sort( packages, edu.cmu.cs.dennisc.java.io.FileUtilities.createComparator() );
		java.io.File[] classes = ThumbnailsPane.listClasses( directory );
		java.util.Arrays.sort( classes, edu.cmu.cs.dennisc.java.io.FileUtilities.createComparator() );

		class SelectFileActionOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
			private java.io.File file;

			public SelectFileActionOperation( java.io.File file ) {
				super( org.alice.ide.IDE.IDE_GROUP, java.util.UUID.fromString( "b8a442d5-4a36-4d27-be67-061ea9a5d2b7" ) );
				this.file = file;
				this.setName( PathControl.this.getTextFor( this.file ) );
				javax.swing.Icon icon;
				if( this.file.isDirectory() ) {
					icon = PathControl.this.folderIconSmall;
				} else {
					icon = PathControl.this.fileIconSmall;
				}
				this.setSmallIcon( icon );
			}

			@Override
			protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
				if( this.file.isDirectory() ) {
					PathControl.this.handleSelectDirectory( this.file );
				} else {
					PathControl.this.handleSelectFile( this.file );
				}
			}
		}

		edu.cmu.cs.dennisc.croquet.Model[] rv = new edu.cmu.cs.dennisc.croquet.Model[ packages.length + classes.length ];
		int i=0;
		for( java.io.File file : packages ) {
			rv[ i++ ] = new SelectFileActionOperation( file );
		}
		for( java.io.File file : classes ) {
			rv[ i++ ] = new SelectFileActionOperation( file );
		}
		return rv;
	}
}
