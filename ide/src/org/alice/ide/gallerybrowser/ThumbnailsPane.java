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

//class SingleOrDoubleClickListUI extends javax.swing.plaf.basic.BasicListUI {
//	@Override
//	protected javax.swing.event.MouseInputListener createMouseInputListener() {
//		return new javax.swing.event.MouseInputListener() {
//			private long tPrevious = System.currentTimeMillis();
//			public void mouseClicked( java.awt.event.MouseEvent e ) {
//			}
//			public void mouseEntered( java.awt.event.MouseEvent e ) {
//			}
//			public void mouseExited( java.awt.event.MouseEvent e ) {
//			}
//			public void mousePressed( java.awt.event.MouseEvent e ) {
//				long tCurrent = e.getWhen();
//				long tDelta = tCurrent - tPrevious;
//				if( tDelta > 400 ) {
//					int row = SingleOrDoubleClickListUI.this.locationToIndex( list, e.getPoint() );
//	                list.setValueIsAdjusting( true );
//	                list.setSelectionInterval(row, row);
//					tPrevious = tCurrent;
//				}
//			}
//			public void mouseReleased( java.awt.event.MouseEvent e ) {
//                list.setValueIsAdjusting( false );
//			}
//			public void mouseMoved( java.awt.event.MouseEvent e ) {
//			}
//			public void mouseDragged( java.awt.event.MouseEvent e ) {
//			}
//		};
//	}
//}
//
//class SingleOrDoubleClickList<E> extends edu.cmu.cs.dennisc.croquet.List<E> {
//	@Override
//	protected javax.swing.JList createAwtComponent() {
//		return new javax.swing.JList() {
//			@Override
//			public void updateUI() {
//				this.setUI( new SingleOrDoubleClickListUI() );
//			}
//		};
//	}
//}

/**
 * @author Dennis Cosgrove
 */
public abstract class ThumbnailsPane extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
	class ThumbnailSnapshotListCellRenderer extends org.alice.ide.swing.SnapshotListCellRenderer {
		private javax.swing.Icon folderIcon;

		public void setFolderIcon( javax.swing.Icon folderIcon ) {
			this.folderIcon = folderIcon;
		}
		@Override
		protected javax.swing.JLabel updateLabel( javax.swing.JLabel rv, Object value ) {
			java.io.File file = (java.io.File)value;
			if( file != null ) {
				String text = ThumbnailsPane.this.getTextFor( file );
				java.io.File iconFile;
				if( file.isDirectory() ) {
					iconFile = new java.io.File( file, "directoryThumbnail.png" );
				} else {
					iconFile = file;
				}
				rv.setText( text );
				javax.swing.Icon icon;
				if( iconFile.exists() ) {
					String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( iconFile );
					icon = new javax.swing.ImageIcon( path );
				} else {
					icon = null;
				}
				if( file.isDirectory() ) {
					if( this.folderIcon != null ) {
						if( icon != null ) {
							icon = new edu.cmu.cs.dennisc.javax.swing.icons.CompositeIcon( this.folderIcon, icon );
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
	
	private edu.cmu.cs.dennisc.croquet.ItemSelectionOperation<java.io.File> itemSelection = new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation<java.io.File>( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, java.util.UUID.fromString( "1814e4cc-1463-4191-bd85-72b61893d1e5" ) ) {
		@Override
		protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.io.File value) {
			throw new RuntimeException("todo");
		}
		@Override
		protected java.io.File decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
			throw new RuntimeException("todo");
		}
	};
	private ThumbnailSnapshotListCellRenderer thumbnailSnapshotListCellRenderer = new ThumbnailSnapshotListCellRenderer();

	public ThumbnailsPane() {
		final edu.cmu.cs.dennisc.croquet.List<java.io.File> list = itemSelection.createList();
		list.setCellRenderer( this.thumbnailSnapshotListCellRenderer );
		list.setLayoutOrientation( edu.cmu.cs.dennisc.croquet.List.LayoutOrientation.HORIZONTAL_WRAP );
		list.setVisibleRowCount( 1 );
		itemSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver<java.io.File>() {
			public void changed(final java.io.File nextValue) {
				if( nextValue != null ) {
//					itemSelection.setValue( null );
					ThumbnailsPane.this.handleFileActivation( nextValue );
//					ThumbnailsPane.this.handleFileActivation( nextValue );
//					javax.swing.SwingUtilities.invokeLater( new Runnable() {
//						public void run() {
//							list.revalidateAndRepaint();
//						}
//					} );
				}
			}
		} );
		list.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed(java.awt.event.KeyEvent e) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE ) {
					ThumbnailsPane.this.handleBackSpaceKey();
				}
			}
			public void keyReleased(java.awt.event.KeyEvent e) {
			}
			public void keyTyped(java.awt.event.KeyEvent e) {
			}
		} );
		
		this.addComponent( new edu.cmu.cs.dennisc.croquet.ScrollPane( list ) );
	}

	protected abstract String getTextFor( java.io.File file );
	protected abstract void handleFileActivation( java.io.File file );
	protected abstract void handleBackSpaceKey();
	public void setFolderIcon( javax.swing.Icon folderIcon ) {
		this.thumbnailSnapshotListCellRenderer.setFolderIcon( folderIcon );
	}
	
	/*package private*/ static java.io.File[] listPackages( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listDirectories( directory );
	}
	/*package private*/ static java.io.File[] listClasses( java.io.File directory ) {
		return edu.cmu.cs.dennisc.java.io.FileUtilities.listFiles( directory, new java.io.FileFilter() {
			public boolean accept( java.io.File file ) {
				String lcFilename = file.getName().toLowerCase();
				return file.isFile() && lcFilename.equals( "directorythumbnail.png" ) == false;
			}
		} );
	}
	public void setDirectory( java.io.File directory ) {
		java.io.File[] directories = ThumbnailsPane.listPackages( directory );
		java.io.File[] thumbnails = ThumbnailsPane.listClasses( directory );

		java.util.Vector< java.io.File > data = new java.util.Vector< java.io.File >();
		data.ensureCapacity( directories.length + thumbnails.length );
		for( java.io.File file : directories ) {
			data.add( file );
		}
		for( java.io.File file : thumbnails ) {
			data.add( file );
		}
		
		java.util.Collections.sort( data, edu.cmu.cs.dennisc.java.io.FileUtilities.createComparator() );
		
		this.itemSelection.setListData( -1, data );
	}
}
