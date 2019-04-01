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
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class BubbleManager {
	private static int Y_GAP = 8; //Default distance to place bubble from top or bottom of screen (is scaled based on font scale)
	private static int X_GAP = 16; //Default distance to place bubble from left or right of screen (is scaled based on font scale)

	private static class SingletonHolder {
		private static BubbleManager instance = new BubbleManager();
	}

	public static BubbleManager getInstance() {
		return SingletonHolder.instance;
	}

	private Hashtable<Bubble, OnscreenBubble> bubbleMap = new Hashtable<Bubble, OnscreenBubble>();
	private List<OnscreenBubble> activeBubbles = new LinkedList<OnscreenBubble>();

	public synchronized OnscreenBubble addBubble( Bubble bubbleOwner, Point2D.Float originOfTail, Dimension2D textSize, float padding, float textScale, Rectangle viewport ) {
		Bubble.PositionPreference positionPreference = bubbleOwner.getPositionPreference();
		if( positionPreference == Bubble.PositionPreference.AUTOMATIC ) {
			if( originOfTail.x < ( viewport.width * .33 ) ) {
				positionPreference = Bubble.PositionPreference.TOP_LEFT;
			} else if( originOfTail.x < ( viewport.width * .66 ) ) {
				positionPreference = Bubble.PositionPreference.TOP_CENTER;
			} else {
				positionPreference = Bubble.PositionPreference.TOP_RIGHT;
			}
		}
		double IPAD = padding;
		Rectangle2D.Double bubbleBounds = getRectForBubble( ( textSize.getWidth() + ( IPAD * 2 ) ), ( textSize.getHeight() + ( IPAD * 2 ) ), positionPreference, viewport );
		RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double();
		roundRect.width = bubbleBounds.width;
		roundRect.height = bubbleBounds.height;
		roundRect.x = bubbleBounds.x;
		roundRect.y = bubbleBounds.y;
		roundRect.arcwidth = IPAD;
		roundRect.archeight = IPAD;

		Rectangle2D.Double textBounds = new Rectangle2D.Double(
				roundRect.x + IPAD,
				roundRect.y + IPAD,
				textSize.getWidth(),
				textSize.getHeight() );

		double percentOriginAcrossScreen = originOfTail.getX() / viewport.width;
		double tailEndX = roundRect.getMinX() + IPAD + ( percentOriginAcrossScreen * textSize.getWidth() );
		Point2D.Float endOfTail = new Point2D.Float( (float)tailEndX, (float)roundRect.getMaxY() );

		OnscreenBubble bubble = new OnscreenBubble( originOfTail, endOfTail, roundRect, textBounds, positionPreference );
		placeBubble( bubble, viewport, textScale );
		this.bubbleMap.put( bubbleOwner, bubble );
		this.activeBubbles.add( bubble );
		return bubble;
	}

	private Rectangle2D.Double getRectForBubble( double width, double height, Bubble.PositionPreference positionPreference, Rectangle viewport ) {
		Rectangle2D.Double bubbleRect = new Rectangle2D.Double();
		bubbleRect.width = width;
		bubbleRect.height = height;
		bubbleRect.x = 0;
		bubbleRect.y = 0;

		return bubbleRect;
	}

	private void placeBubble( OnscreenBubble bubble, Rectangle viewport, float textScale ) {
		double scaledYGap = Y_GAP * textScale;
		double scaledXGap = X_GAP * textScale;
		double x, y;
		switch( bubble.getPositionPreference() ) {
		case TOP_LEFT: {
			x = scaledXGap;
		}
			break;
		case TOP_CENTER: {
			x = ( viewport.width - bubble.getBubbleRect().width ) * .5f;
		}
			break;
		case TOP_RIGHT: {
			x = viewport.width - bubble.getBubbleRect().width - scaledXGap;
		}
			break;
		default:
			x = scaledXGap;
		}
		double rightEdge = x + bubble.getBubbleRect().width;
		double maxY = 0;
		for( OnscreenBubble otherBubble : this.activeBubbles ) {
			double minX = otherBubble.getBubbleRect().getMinX();
			double maxX = otherBubble.getBubbleRect().getMaxX();
			if( ( ( x >= minX ) && ( x <= maxX ) ) || ( ( rightEdge >= minX ) && ( rightEdge <= maxX ) ) ) {
				if( otherBubble.getBubbleRect().getMaxY() > maxY ) {
					maxY = otherBubble.getBubbleRect().getMaxY();
				}
			}
		}
		y = maxY + scaledYGap;
		bubble.setPosition( x, y );
	}

	public void packBubbles( Rectangle viewport ) {

	}

	public OnscreenBubble getBubble( Bubble bubbleOwner ) {
		return this.bubbleMap.get( bubbleOwner );
	}

	public synchronized void removeBubble( Bubble bubbleOwner ) {
		OnscreenBubble bubble = this.bubbleMap.get( bubbleOwner );
		if( bubble != null ) {
			this.activeBubbles.remove( bubble );
			this.bubbleMap.remove( bubbleOwner );
		}
	}

}
