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
package edu.cmu.cs.dennisc.javax.swing.components;

public class JImageView extends javax.swing.JComponent {
	private java.awt.image.BufferedImage bufferedImage;
	private int desiredSize;
	public JImageView( java.awt.image.BufferedImage bufferedImage, int desiredSize ) {
		this.bufferedImage = bufferedImage;
		this.desiredSize = desiredSize;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		if( this.bufferedImage != null ) {
			double aspectRatio = this.bufferedImage.getWidth() / (double)this.bufferedImage.getHeight();
			int width;
			int height;
			if( this.bufferedImage.getWidth() > (double)this.bufferedImage.getHeight() ) {
				width = this.desiredSize;
				height = (int)(width / aspectRatio);
			} else {
				height = this.desiredSize;
				width = (int)(height * aspectRatio);
			}
			return new java.awt.Dimension( width, height );
		} else {
			return super.getPreferredSize();
		}
	}
//	@Override
//	public java.awt.Dimension getMaximumSize() {
//		return this.getPreferredSize();
//	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent( g );
		if( this.bufferedImage != null ) {
			java.awt.Dimension preferredSize = this.getPreferredSize();

//			int w;
//			int h;
//			double componentAspectRatio = this.getWidth() / (double)this.getHeight();
//			double imageAspectRatio = this.bufferedImage.getWidth() / (double)this.bufferedImage.getHeight();
//			if( componentAspectRatio > imageAspectRatio ) {
//				if( this.getWidth() > this.getHeight() ) {
//					w = this.getWidth();
//					h = (int)(w / imageAspectRatio);
//				} else {
//					h = this.getHeight();
//					w = (int)(h * imageAspectRatio);
//				}
//			} else {
//				if( this.getWidth() > this.getHeight() ) {
//					h = this.getHeight();
//					w = (int)(h * imageAspectRatio);
//				} else {
//					w = this.getWidth();
//					h = (int)(w / imageAspectRatio);
//				}
//			}
			int w = (int)preferredSize.getWidth();
			int h = (int)preferredSize.getHeight();
//			int x = (this.getWidth() - w)/2;
//			int y = (this.getHeight() - h)/2;
			
			int x = 0;
			int y = 0;
			
//			g.setColor( java.awt.Color.BLACK );
//			g.drawRect( x, y, w-1, h-1 );
//			g.drawRect( 0, 0, this.getWidth()-1, this.getHeight()-1 );
			g.drawImage( this.bufferedImage, x, y, x+w, y+h, 0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), this );
		}
	}
}
