/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public class CaptureLookingGlass extends AbstractLookingGlass implements edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass {
	public static interface Observer {
		public void handleImage( java.awt.image.BufferedImage image, boolean isUpSideDown );
	}

	private class JRecordPanel extends javax.swing.JPanel {
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );

		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			return CaptureLookingGlass.this.size;
		}

		@Override
		public void paint( java.awt.Graphics g ) {
			//super.paint( g );
			if( image != null ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.geom.AffineTransform m = g2.getTransform();
				g2.translate( 0, this.getHeight() );
				g2.scale( 1.0, -1.0 );
				g.drawImage( image, 0, 0, this );
				g2.setTransform( m );
			} else {
				g.setColor( java.awt.Color.BLACK );
				g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
			}
		}
	}

	private final java.awt.Dimension size;

	private final JRecordPanel jPanel = new JRecordPanel();

	private final javax.media.opengl.GLPbuffer glPixelBuffer;

	private java.awt.image.BufferedImage image;

	public CaptureLookingGlass( java.awt.Dimension size, AbstractLookingGlass lookingGlassToShareContextWith ) {
		super( LookingGlassFactory.getInstance() );
		this.size = size;
		javax.media.opengl.GLContext share;
		if( lookingGlassToShareContextWith != null ) {
			share = lookingGlassToShareContextWith.getGLAutoDrawable().getContext();
		} else {
			share = null;
		}
		this.glPixelBuffer = GlDrawableUtilities.createGlPixelBuffer( GlDrawableUtilities.createGlCapabilities(), GlDrawableUtilities.getPerhapsMultisampledGlCapabilitiesChooser(), size.width, size.height, share );
	}

	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		rv.setSize( this.size );
		return rv;
	}

	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		return this.glPixelBuffer;
	}

	public javax.swing.JPanel getJPanel() {
		return this.jPanel;
	}

	public java.awt.image.BufferedImage getImage() {
		return this.image;
	}

	public void acquireImage( Observer observer ) {
		if( this.image != null ) {
			//pass
		} else {
			this.image = this.createBufferedImageForUseAsColorBuffer();
		}
		boolean[] atIsUpSideDown = { false };
		this.getColorBufferNotBotheringToFlipVertically( this.image, atIsUpSideDown );
		observer.handleImage( image, atIsUpSideDown[ 0 ] );
		this.jPanel.repaint();
	}

	@Override
	protected void repaintIfAppropriate() {
	}

	public java.awt.Component getAWTComponent() {
		return this.jPanel;
	}

	public void repaint() {
	}
}
