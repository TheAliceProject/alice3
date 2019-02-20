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
package edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics;

import edu.cmu.cs.dennisc.java.awt.MultilineText;
import edu.cmu.cs.dennisc.java.awt.TextAlignment;
import edu.cmu.cs.dennisc.render.Graphics2D;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.graphics.OnscreenBubble;
import edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class GlrSpeechBubble extends GlrBubble<SpeechBubble> {
	//	protected abstract java.awt.Stroke getStroke();

	private static double sine( double t, double theta0, double theta1 ) {
		double theta = theta0 + ( t * ( theta1 - theta0 ) );
		return Math.sin( theta );
	}

	private Area getPortionOfPath( GeneralPath path, double portion ) {
		Area area = new Area( path );
		Rectangle2D bounds = area.getBounds2D();
		double maskBottom = bounds.getY() + bounds.getHeight();

		double stylizedPathPortion = sine( portion, -Math.PI / 2, 0 ) + 1;
		stylizedPathPortion *= stylizedPathPortion;
		double maskHeight = bounds.getHeight() * stylizedPathPortion;

		Rectangle2D mask = new Rectangle2D.Double( bounds.getX(), maskBottom - maskHeight, bounds.getWidth(), maskHeight );
		area.intersect( new Area( mask ) );
		return area;
	}

	@Override
	protected void render(
			Graphics2D g2,
			RenderTarget renderTarget,
			Rectangle actualViewport,
			AbstractCamera camera,
			MultilineText multilineText,
			Font font,
			Color textColor,
			float wrapWidth,
			Color fillColor,
			Color outlineColor,
			OnscreenBubble bubble,
			double portion ) {
		assert bubble != null;

		g2.setFont( font );

		float targetX = bubble.getEndOfTail().x;
		float targetY = bubble.getEndOfTail().y;

		float originX = (float)bubble.getOriginOfTail().getX();
		float originY = (float)bubble.getOriginOfTail().getY();

		float controlX = targetX;
		float controlY = originY;
		float tailWidth = 6f;
		float topOffsetY = -1f;

		GeneralPath path = new GeneralPath();
		path.moveTo( originX, originY );
		path.quadTo( controlX, controlY, targetX + tailWidth, targetY + topOffsetY );
		path.lineTo( targetX - tailWidth, targetY + topOffsetY );
		path.quadTo( controlX, controlY, originX, originY );
		path.closePath();

		Area area;
		if( portion < 1.0 ) {
			area = getPortionOfPath( path, portion );
		} else {
			area = new Area( path );
			area.add( new Area( bubble.getBubbleRect() ) );
		}

		assert area != null;
		g2.setTransform( new AffineTransform() );

		g2.setColor( fillColor );
		g2.fill( area );
		g2.setColor( outlineColor );
		g2.draw( area );

		if( portion < 1.0 ) {
			//pass
		} else {
			g2.setColor( textColor );
			g2.setFont( font );

			//g2.translate( xT, yT );
			//			g2.setPaint( java.awt.Color.RED );
			//			g2.draw( textBounds );
			//			g2.setPaint( java.awt.Color.BLACK );
			//			g2.setColor(Color.RED);
			//			g2.drawRect((int)textBounds.getMinX(), (int)textBounds.getMinX(), (int)textBounds.getWidth(), (int)textBounds.getHeight());
			multilineText.paint( g2, wrapWidth, TextAlignment.LEADING, bubble.getTextBounds() );

			//g2.translate( -xT, -yT );
			//			int xPixel = (int)( bodyConnectionLocationOfTail.getX() + textBoundsOffset.getX() );
			//			int yPixel = (int)( bodyConnectionLocationOfTail.getY() + textBoundsOffset.getY() - 10 );
			//			g2.setColor( textColor );
			//			g2.drawString( text, xPixel, yPixel );
		}

		//		g2.setStroke( stroke );
	}
}
