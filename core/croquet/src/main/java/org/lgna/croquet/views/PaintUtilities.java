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
package org.lgna.croquet.views;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class PaintUtilities {
	private static Paint disabledTexturePaint = null;

	public static Paint getDisabledTexturePaint() {
		if( PaintUtilities.disabledTexturePaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			Graphics2D g2 = (Graphics2D)image.getGraphics();
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setColor( new Color( 128, 128, 128, 31 ) );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( new Color( 31, 31, 31, 127 ) );
			g2.drawLine( 0, height, width, 0 );
			g2.drawLine( 0, 0, 0, 0 );
			g2.dispose();
			PaintUtilities.disabledTexturePaint = new TexturePaint( image, new Rectangle( 0, 0, width, height ) );
		}
		return PaintUtilities.disabledTexturePaint;
	}

	private static TexturePaint copyTexturePaint = null;

	public static TexturePaint getCopyTexturePaint() {
		if( PaintUtilities.copyTexturePaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
			Graphics2D g2 = (Graphics2D)image.getGraphics();
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2.setColor( new Color( 0, 0, 255, 96 ) );
			g2.drawLine( 2, 4, 6, 4 );
			g2.drawLine( 4, 2, 4, 6 );
			g2.dispose();
			PaintUtilities.copyTexturePaint = new TexturePaint( image, new Rectangle( 0, 0, width, height ) );
		}
		return PaintUtilities.copyTexturePaint;
	}
}
