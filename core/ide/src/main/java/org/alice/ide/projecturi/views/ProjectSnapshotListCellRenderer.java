/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.projecturi.views;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import org.alice.ide.swing.SnapshotListCellRenderer;
import org.alice.ide.uricontent.StarterProjectUtilities;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractNotAvailableIcon implements Icon {
	@Override
	public int getIconWidth() {
		return 160;
	}

	@Override
	public int getIconHeight() {
		return 120;
	}

	protected abstract String getText();

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y ) {
		int width = this.getIconWidth();
		int height = this.getIconHeight();
		g.setColor( Color.DARK_GRAY );
		g.fillRect( x, y, width, height );
		g.setColor( Color.LIGHT_GRAY );
		GraphicsUtilities.drawCenteredText( g, this.getText(), x, y, width, height );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ProjectSnapshotListCellRenderer extends SnapshotListCellRenderer {
	private static final Icon SNAPSHOT_NOT_AVAILABLE_ICON = new AbstractNotAvailableIcon() {
		@Override
		protected String getText() {
			return "snapshot not available";
		}
	};
	private static final Icon FILE_DOES_NOT_EXIST_ICON = new AbstractNotAvailableIcon() {
		@Override
		protected String getText() {
			return "snapshot does not exist";
		}
	};

	private static final String PROJECT_LOCALIZATION_BUNDLE_NAME = "org.lgna.story.resources.ProjectNames";

	private static String findLocalizedFileNameText( String key, String bundleName, Locale locale ) {
		if( ( bundleName != null ) && ( key != null ) ) {
			try {
				ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle( bundleName, locale );
				String rv = resourceBundle.getString( key );
				return rv;
			} catch( MissingResourceException mre ) {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	protected JLabel updateLabel( JLabel rv, Object value ) {
		URI uri = (URI)value;
		String text;
		Icon icon;
		if( uri != null ) {
			if( uri.isAbsolute() ) {
				boolean isExtensionDesired;
				if( "file".equals( uri.getScheme() ) ) {
					isExtensionDesired = true;
				} else {
					uri = StarterProjectUtilities.toFileUriFromStarterUri( uri );
					isExtensionDesired = false;
				}
				File file = new File( uri );
				text = file.getName();
				if( isExtensionDesired ) {
					//pass
				} else {
					final String SUFFIX = ".a3p";
					if( text.endsWith( SUFFIX ) ) {
						text = text.substring( 0, text.length() - SUFFIX.length() );
					}

					//Localize the text to display
					String localizedName = findLocalizedFileNameText( text, PROJECT_LOCALIZATION_BUNDLE_NAME, JComponent.getDefaultLocale() );
					if( localizedName != null ) {
						text = localizedName;
					}
				}
				if( file.exists() ) {
					//					//todo: remove
					//					String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file );
					//					if( path != null ) {
					//						String snapshotPath = path.substring( 0, path.length() - 3 ) + "png";
					//						if( edu.cmu.cs.dennisc.java.io.FileUtilities.existsAndHasLengthGreaterThanZero( snapshotPath ) ) {
					//							icon = new javax.swing.ImageIcon( snapshotPath );
					//						} else {
					//							icon = null;
					//						}
					//					} else {
					//						icon = null;
					//					}
					//
					//					if( icon != null ) {
					//						//pass
					//					} else {
					try {
						ZipFile zipFile = new ZipFile( file );
						ZipEntry zipEntry = zipFile.getEntry( "thumbnail.png" );
						if( zipEntry != null ) {
							InputStream is = zipFile.getInputStream( zipEntry );
							Image image = ImageUtilities.read( ImageUtilities.PNG_CODEC_NAME, is );
							icon = new SnapshotIcon( image );
						} else {
							icon = SNAPSHOT_NOT_AVAILABLE_ICON;
						}
						zipFile.close();
					} catch( Throwable t ) {
						icon = SNAPSHOT_NOT_AVAILABLE_ICON;
					}
					//					}
				} else {
					icon = FILE_DOES_NOT_EXIST_ICON;
				}
			} else {
				text = uri.toString();
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
