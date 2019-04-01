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

package edu.cmu.cs.dennisc.scenegraph.graphics;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class OnscreenBubble {

	private Point2D.Float originOfTail;
	private Point2D.Float endOfTail;
	private RoundRectangle2D.Double bubbleRect;
	private Rectangle2D.Double textBounds;
	private Bubble.PositionPreference positionPreference;

	public OnscreenBubble( Point2D.Float originOfTail, Point2D.Float endOfTail, RoundRectangle2D.Double bubbleRect, Rectangle2D.Double textBounds, Bubble.PositionPreference positionPreference ) {
		this.originOfTail = originOfTail;
		this.endOfTail = endOfTail;
		this.bubbleRect = bubbleRect;
		this.textBounds = textBounds;
		this.positionPreference = positionPreference;
	}

	public void setPosition( double x, double y ) {
		Point2D.Double tailEndOffset = new Point2D.Double( endOfTail.x - bubbleRect.x, endOfTail.y - bubbleRect.y );
		Point2D.Double textOffset = new Point2D.Double( textBounds.x - bubbleRect.x, textBounds.y - bubbleRect.y );

		this.bubbleRect.x = x;
		this.bubbleRect.y = y;
		endOfTail.setLocation( bubbleRect.x + tailEndOffset.x, bubbleRect.y + tailEndOffset.y );
		textBounds.x = bubbleRect.x + textOffset.x;
		textBounds.y = bubbleRect.y + textOffset.y;
	}

	public void setPosition( Point2D.Double position ) {
		setPosition( position.x, position.y );
	}

	public Bubble.PositionPreference getPositionPreference() {
		return this.positionPreference;
	}

	public Point2D.Float getOriginOfTail() {
		return this.originOfTail;
	}

	public Point2D.Float getEndOfTail() {
		return this.endOfTail;
	}

	public RoundRectangle2D.Double getBubbleRect() {
		return this.bubbleRect;
	}

	public Rectangle2D.Double getTextBounds() {
		return this.textBounds;
	}

	public void updateOriginOfTail( Point2D.Float newOrigin, Rectangle viewport ) {
		this.originOfTail.x = newOrigin.x;
		this.originOfTail.y = newOrigin.y;
		double percentOriginAcrossScreen = originOfTail.getX() / viewport.width;
		this.endOfTail.x = (float)( this.bubbleRect.getMinX() + this.getHorizontalPadding() + ( percentOriginAcrossScreen * this.textBounds.getWidth() ) );
	}

	public double getHorizontalPadding() {
		return ( this.bubbleRect.getWidth() - this.textBounds.getWidth() ) * .5;
	}

	public double getVerticalPadding() {
		return ( this.bubbleRect.getHeight() - this.textBounds.getHeight() ) * .5;
	}
}
