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
package org.alice.stageide.icons;

import org.alice.stageide.sceneeditor.ThumbnailGenerator;
import org.lgna.croquet.icon.AbstractIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class SceneIcon extends AbstractIcon {
	public SceneIcon( Dimension size ) {
		super( size );
	}

	private ThumbnailGenerator thumbnailGenerator;
	private boolean isDirty = true;
	private BufferedImage image = null;

	/* package-private */void markDirty() {
		this.image = null;
		this.isDirty = true;
	}

	@Override
	protected void paintIcon( Component c, Graphics2D g2 ) {
		if( this.isDirty ) {
			try {
				if( this.thumbnailGenerator != null ) {
					//pass
				} else {
					this.thumbnailGenerator = new ThumbnailGenerator( this.getIconWidth(), this.getIconHeight() );
				}
				this.image = this.thumbnailGenerator.createThumbnail();
			} catch( Throwable t ) {
				this.image = null;
				t.printStackTrace();
			}
			//			if( this.image != null ) {
			//				this.image = edu.cmu.cs.dennisc.image.ImageUtilities.createAlphaMaskedImage( this.image, new edu.cmu.cs.dennisc.java.awt.Painter() {
			//					public void paint( java.awt.Graphics2D g2, int width, int height ) {
			//						final int N = 15;
			//						g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			//						g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 1.0f/N ) );
			//						g2.setColor( java.awt.Color.BLACK );
			//						for( int i=0; i<N; i++ ) {
			//							g2.fillRoundRect( N-i, N-i, width-(N-i)*2+1, height-(N-i)*2+1, N-i, N-i );
			//						}
			//					}
			//				} );
			//			}
			this.isDirty = false;
		}
		if( this.image != null ) {
			g2.drawImage( this.image, 0, 0, null );
		} else {
			int w = this.getIconWidth();
			int h = this.getIconHeight() / 2;
			g2.setColor( Color.BLUE );
			g2.fillRect( 0, 0, w, h );
			g2.setColor( Color.GREEN );
			g2.fillRect( 0, 0 + h, w, h );
		}
	}
}
