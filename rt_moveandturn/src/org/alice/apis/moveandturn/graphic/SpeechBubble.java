/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.moveandturn.graphic;

/**
 * @author Dennis Cosgrove
 */
public abstract class SpeechBubble extends Bubble {

	public SpeechBubble( String text, java.awt.Font font, java.awt.Color foregroundColor, java.awt.Color backgroundColor, java.awt.Color outlineColor, Originator originator ) {
		super( text, font, foregroundColor, backgroundColor, outlineColor, originator );
	}	

	protected abstract java.awt.Stroke getStroke();
	
	private static double sine( double t, double theta0, double theta1 ) {
		double theta = theta0 + t * ( theta1 - theta0 ); 
		return Math.sin( theta );
	}
	
	private java.awt.geom.Area getPortionOfPath( java.awt.geom.GeneralPath path, double portion ) {
		java.awt.geom.Area area = new java.awt.geom.Area( path );
		java.awt.geom.Rectangle2D bounds = area.getBounds2D();
		double maskBottom = bounds.getY() + bounds.getHeight();

		double stylizedPathPortion = sine( portion, -Math.PI/2, 0 ) + 1;
		stylizedPathPortion *= stylizedPathPortion;
		double maskHeight = bounds.getHeight() * stylizedPathPortion;

		java.awt.geom.Rectangle2D mask = new java.awt.geom.Rectangle2D.Double( bounds.getX(), maskBottom-maskHeight, bounds.getWidth(), maskHeight );
		area.intersect(  new java.awt.geom.Area( mask ) );
		return area;
	}
	
	@Override
	protected void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.lang.String text, java.awt.Font font, java.awt.Color textColor,
			java.awt.geom.Rectangle2D textBounds, org.alice.apis.moveandturn.graphic.Bubble.State state, double portion, java.awt.geom.Point2D originOfTail, java.awt.geom.Point2D bodyConnectionLocationOfTail,
			java.awt.geom.Point2D textBoundsOffset, java.awt.Color fillColor, java.awt.Color outlineColor ) {
		java.awt.Stroke stroke = g2.getStroke();
		g2.setStroke( getStroke() );
		g2.setFont( font );
		
		float targetX = (float)bodyConnectionLocationOfTail.getX();
		float targetY = (float)bodyConnectionLocationOfTail.getY();

		float originX = (float)originOfTail.getX();
		float originY = (float)originOfTail.getY();
		
		float controlX = targetX;
		//float controlY = Math.max( originY, targetY+320 );
		float controlY = originY;
		float topOffsetX = 6f;
		float topOffsetY = -2f;

		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( originX, originY );
		path.quadTo( controlX, controlY, targetX + topOffsetX, targetY + topOffsetY );
		path.lineTo( targetX - topOffsetX, targetY + topOffsetY );
		path.quadTo( controlX, controlY, originX, originY );
		path.closePath();

		final double IPAD = 12f;
		java.awt.geom.RoundRectangle2D.Double roundRect = new java.awt.geom.RoundRectangle2D.Double();
		roundRect.x = textBounds.getX() + bodyConnectionLocationOfTail.getX() + textBoundsOffset.getX() - IPAD;
		roundRect.y = textBounds.getY() + bodyConnectionLocationOfTail.getY() + textBoundsOffset.getY() - textBounds.getHeight() + 10 - IPAD;
		roundRect.width =  textBounds.getWidth() + IPAD + IPAD;
		roundRect.height =  textBounds.getHeight() + IPAD + IPAD;
		roundRect.arcwidth = IPAD;
		roundRect.archeight = IPAD;

		java.awt.geom.Area area;
		if( state == State.OPENNING ) {
			area = getPortionOfPath( path, portion );
		} else if( state == State.UPDATING ) {
			area = new java.awt.geom.Area( path );
			area.add( new java.awt.geom.Area( roundRect ) );
		} else if( state == State.CLOSING ) {
			area = getPortionOfPath( path, 1.0-portion );
		} else {
			throw new RuntimeException();
		}

		assert area != null;
		g2.setTransform( new java.awt.geom.AffineTransform() );
		
		g2.setColor( fillColor );
		g2.fill( area );
		g2.setColor( outlineColor );
		g2.draw( area );
		
		if( state == State.UPDATING ) {
			int xPixel = (int)( bodyConnectionLocationOfTail.getX() + textBoundsOffset.getX() );
			int yPixel = (int)( bodyConnectionLocationOfTail.getY() + textBoundsOffset.getY() - 10 );
			g2.setColor( textColor );
			g2.drawString( text, xPixel, yPixel );
		}

		g2.setStroke( stroke );
	}
}
