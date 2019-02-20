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
package org.lgna.croquet.icon;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

/**
 * @author Dennis Cosgrove
 */
public class TrimmedIcon extends AbstractIcon {
	private final ImageIcon imageIcon;

	public TrimmedIcon( ImageIcon imageIcon, Dimension size ) {
		super( size );
		this.imageIcon = imageIcon;
	}

	public ImageIcon getImageIcon() {
		return this.imageIcon;
	}

	@Override
	protected void paintIcon( Component c, Graphics2D g2 ) {
		if( this.imageIcon != null ) {
			Image image = this.imageIcon.getImage();
			int imageWidth = image.getWidth( c );
			int imageHeight = image.getHeight( c );

			int width = this.getIconWidth();
			int height = this.getIconHeight();

			AffineTransform t = g2.getTransform();

			//todo
			int dx;
			int dy;
			if( ( height != imageHeight ) || ( width != imageWidth ) ) {
				double factorX = width / (double)imageWidth;
				double factorY = height / (double)imageHeight;
				double factor;

				//todo: handle icons larger than image
				if( factorX > factorY ) {
					factor = factorY;
					dx = (int)( ( ( width / factor ) - imageWidth ) / 2 );
					dy = 0;
				} else {
					factor = factorX;
					dx = 0;
					dy = (int)( ( ( height / factor ) - imageHeight ) / 2 );
				}
				g2.scale( factor, factor );
			} else {
				dx = ( width - imageWidth ) / 2;
				dy = ( height - imageHeight ) / 2;
				final boolean DEBUG = false;
				if( DEBUG ) {
					g2.setPaint( Color.RED );
					g2.fillRect( 0, 0, this.getIconWidth(), this.getIconHeight() );
					g2.setPaint( Color.GREEN );
					g2.fillRect( dx, dy, this.imageIcon.getIconWidth(), this.imageIcon.getIconHeight() );
				}
			}

			g2.translate( dx, dy );
			g2.drawImage( image, 0, 0, imageWidth, imageHeight, c );
			g2.setTransform( t );
		}
	}
}
