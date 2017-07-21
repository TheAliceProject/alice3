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
package edu.cmu.cs.dennisc.javax.swing;

/**
 * @author Dennis Cosgrove
 * 
 */
public class IconUtilities {
	private IconUtilities() {
		throw new AssertionError();
	}

	private static final boolean IS_PRINT_USED = true;
	private static final java.awt.Container privateContainer = new java.awt.Container();

	public static javax.swing.Icon createIcon( java.awt.Component component ) {
		javax.swing.Icon rv;
		java.awt.Dimension size = component.getPreferredSize();
		if( ( size.width > 0 ) && ( size.height > 0 ) ) {
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
			if( IS_PRINT_USED ) {
				component.print( g );
			} else {
				javax.swing.SwingUtilities.paintComponent( g, component, privateContainer, 0, 0, size.width, size.height );
			}
			g.dispose();
			rv = new javax.swing.ImageIcon( image );
		} else {
			rv = new javax.swing.Icon() {
				@Override
				public int getIconWidth() {
					return 24;
				}

				@Override
				public int getIconHeight() {
					return 12;
				}

				@Override
				public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				}
			};
		}
		return rv;
	}

	public static javax.swing.ImageIcon createImageIcon( java.net.URL url ) {
		if( url != null ) {
			return new javax.swing.ImageIcon( url );
		} else {
			return null;
		}
	}

	public static javax.swing.ImageIcon createImageIcon( java.awt.Image image ) {
		if( image != null ) {
			return new javax.swing.ImageIcon( image );
		} else {
			return null;
		}
	}

	public static javax.swing.ImageIcon[] createImageIcons( java.net.URL... urls ) {
		javax.swing.ImageIcon[] rv = new javax.swing.ImageIcon[ urls.length ];
		int i = 0;
		for( java.net.URL url : urls ) {
			rv[ i ] = createImageIcon( url );
			i++;
		}
		return rv;
	}

	public static javax.swing.ImageIcon[] createImageIcons( java.awt.Image... images ) {
		javax.swing.ImageIcon[] rv = new javax.swing.ImageIcon[ images.length ];
		int i = 0;
		for( java.awt.Image image : images ) {
			rv[ i ] = createImageIcon( image );
			i++;
		}
		return rv;
	}

	public static boolean areSizesEqual( javax.swing.Icon prevIcon, javax.swing.Icon nextIcon ) {
		int prevWidth = prevIcon != null ? prevIcon.getIconWidth() : 0;
		int prevHeight = prevIcon != null ? prevIcon.getIconHeight() : 0;
		int nextWidth = nextIcon != null ? nextIcon.getIconWidth() : 0;
		int nextHeight = nextIcon != null ? nextIcon.getIconHeight() : 0;
		return ( prevWidth == nextWidth ) && ( prevHeight == nextHeight );
	}

	public static javax.swing.Icon getInformationIcon() {
		return javax.swing.UIManager.getIcon( "OptionPane.informationIcon" );
	}

	public static javax.swing.Icon getQuestionIcon() {
		return javax.swing.UIManager.getIcon( "OptionPane.questionIcon" );
	}

	public static javax.swing.Icon getWarningIcon() {
		return javax.swing.UIManager.getIcon( "OptionPane.warningIcon" );
	}

	public static javax.swing.Icon getErrorIcon() {
		return javax.swing.UIManager.getIcon( "OptionPane.errorIcon" );
	}

	public static java.awt.Dimension newDimension( javax.swing.Icon icon ) {
		if( icon != null ) {
			return new java.awt.Dimension( icon.getIconWidth(), icon.getIconHeight() );
		} else {
			return null;
		}
	}
}
