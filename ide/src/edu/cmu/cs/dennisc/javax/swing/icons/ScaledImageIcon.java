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
package edu.cmu.cs.dennisc.javax.swing.icons;

/**
 * @author Dennis Cosgrove
 */
public class ScaledImageIcon implements javax.swing.Icon {
	private java.awt.Image image;
	private int width;
	private int height;

	public ScaledImageIcon( java.awt.Image image, int width, int height ) {
		if( image != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "image is null", this );
		}
		this.image = image;
		this.width = width;
		this.height = height;
	}

	public int getIconWidth() {
		return this.width;
	}

	public int getIconHeight() {
		return this.height;
	}

	public java.awt.Image getImage() {
		return this.image;
	}

	private void paintErrorCondition( java.awt.Graphics2D g2, int x, int y ) {
		java.awt.Paint prevPaint = g2.getPaint();
		g2.setColor( java.awt.Color.RED );
		g2.fillRect( x, y, this.width, this.height );
		g2.setPaint( prevPaint );
	}

	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( this.image != null ) {
			int imageWidth = this.image.getWidth( c );
			int imageHeight = this.image.getHeight( c );
			if( ( imageWidth > 0 ) && ( imageHeight > 0 ) ) {
				g2.translate( x, y );
				g2.drawImage( this.image, 0, 0, this.width, this.height, 0, 0, imageWidth, imageHeight, c );
				g2.translate( -x, -y );
			} else {
				this.paintErrorCondition( g2, x, y );
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "image size is 0,0", this );
			}
		} else {
			this.paintErrorCondition( g2, x, y );
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "image is null", this );
		}
	}
}
