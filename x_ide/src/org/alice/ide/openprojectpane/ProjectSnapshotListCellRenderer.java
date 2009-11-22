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

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractNotAvailableIcon implements javax.swing.Icon {
	public int getIconWidth() {
		return 160;
	}
	public int getIconHeight() {
		return 120;
	}
	protected abstract String getText();
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		int width = this.getIconWidth();
		int height = this.getIconHeight();
		g.setColor( java.awt.Color.DARK_GRAY );
		g.fillRect( x, y, width, height );
		g.setColor( java.awt.Color.LIGHT_GRAY );
		edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, this.getText(), x, y, width, height );
	}
}

/**
 * @author Dennis Cosgrove
 */
class ProjectSnapshotListCellRenderer extends org.alice.ide.swing.SnapshotListCellRenderer {
	private static final javax.swing.Icon SNAPSHOT_NOT_AVAILABLE_ICON = new AbstractNotAvailableIcon() {
		@Override
		protected String getText() {
			return "snapshot not available";
		}
	};
	private static final javax.swing.Icon FILE_DOES_NOT_EXIST_ICON = new AbstractNotAvailableIcon() {
		@Override
		protected String getText() {
			return "file does not exist";
		}
	};
	@Override
	protected javax.swing.JLabel updateLabel( javax.swing.JLabel rv, Object value ) {
		java.net.URI uri = (java.net.URI)value;
		java.io.File file = new java.io.File( uri );

		String text;
		javax.swing.Icon icon;

		if( file != null ) {
			text = file.getName();
			if( file.exists() ) {
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
							icon = SNAPSHOT_NOT_AVAILABLE_ICON;
						}
						zipFile.close();
					} catch( Throwable t ) {
						icon = SNAPSHOT_NOT_AVAILABLE_ICON;
					}
				}
			} else {
				icon = FILE_DOES_NOT_EXIST_ICON;
			}
		} else {
			text = null;
			icon = FILE_DOES_NOT_EXIST_ICON;
		}
		
		rv.setText( text );
		rv.setIcon( icon );
		return rv;
	}
}
