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
package edu.cmu.cs.dennisc.ui.imageeditor;

/**
 * @author Dennis Cosgrove
 */
public class ImagePane extends javax.swing.JPanel {
	private java.awt.Image m_image = null;
	public final int UNSPECIFIED = -1;
	private int m_desiredWidth = UNSPECIFIED;
	private int m_desiredHeight = UNSPECIFIED;

	protected void paintImage( java.awt.Graphics g, int x, int y, int width, int height ) {
		if( m_image != null ) {
			g.drawImage( m_image, x, y, width, height, this );
		} else {
			g.setColor( getForeground() );
			g.drawRect( x, y, width, height );
		}
	}

	private int getDesiredImageWidth() {
		if( m_desiredWidth == UNSPECIFIED ) {
			if( m_image != null ) {
				return edu.cmu.cs.dennisc.image.ImageUtilities.getWidth( m_image );
			} else {
				return 512;
			}
		} else {
			return m_desiredWidth;
		}
	}

	private int getDesiredImageHeight() {
		if( m_desiredHeight == UNSPECIFIED ) {
			if( m_image != null ) {
				return edu.cmu.cs.dennisc.image.ImageUtilities.getHeight( m_image );
			} else {
				return 512;
			}
		} else {
			return m_desiredHeight;
		}
	}

	public void setDesiredImageSize( int width, int height ) {
		m_desiredWidth = width;
		m_desiredHeight = height;
	}

	@Override
	public final void paintComponent( java.awt.Graphics g ) {
		g.setColor( getBackground() );
		java.awt.Rectangle r = g.getClipBounds();
		g.fillRect( r.x, r.y, r.width, r.height );

		int componentWidth = getWidth();
		int componentHeight = getHeight();

		int imageWidth = getDesiredImageWidth();
		int imageHeight = getDesiredImageHeight();
		int imageX = ( componentWidth - imageWidth ) / 2;
		int imageY = ( componentHeight - imageHeight ) / 2;

		paintImage( g, imageX, imageY, imageWidth, imageHeight );
	}

	public java.awt.Image accessImage() {
		return m_image;
	}

	public void setImage( java.awt.Image image ) {
		m_image = image;
		repaint();
	}
}
