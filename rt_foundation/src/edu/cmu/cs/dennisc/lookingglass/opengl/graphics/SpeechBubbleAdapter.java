package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public class SpeechBubbleAdapter extends BubbleAdapter< edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble > {
//	protected abstract java.awt.Stroke getStroke();
	
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
	protected void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			edu.cmu.cs.dennisc.awt.MultilineText multilineText, 
			java.awt.Font font, 
			java.awt.Color textColor, 
			java.awt.Color fillColor, 
			java.awt.Color outlineColor,
			java.awt.geom.Point2D.Float originOfTail,
			java.awt.geom.Point2D.Float bodyConnectionLocationOfTail,
			java.awt.geom.Point2D.Float textBoundsOffset,
			double portion,
			double widthGuide ) {
		assert originOfTail != null;
		assert bodyConnectionLocationOfTail != null;
		
		g2.setFont( font );
		java.awt.geom.Dimension2D textSize = multilineText.getDimension( g2, widthGuide );
		java.awt.geom.Rectangle2D textBounds = new java.awt.geom.Rectangle2D.Double( 
				textBoundsOffset.x, 
				textBoundsOffset.y, 
				textSize.getWidth(), 
				textSize.getHeight() );

		
//		java.awt.Stroke stroke = g2.getStroke();
//		g2.setStroke( getStroke() );
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
		if( portion < 1.0 ) {
			area = getPortionOfPath( path, portion );
		} else {
			area = new java.awt.geom.Area( path );
			area.add( new java.awt.geom.Area( roundRect ) );
		}

		assert area != null;
		g2.setTransform( new java.awt.geom.AffineTransform() );
		
		g2.setColor( fillColor );
		g2.fill( area );
		g2.setColor( outlineColor );
		g2.draw( area );
		
		if( portion < 1.0 ) {
			//pass
		} else {
			g2.setFont( font );
			
			double xT = bodyConnectionLocationOfTail.getX();
			double yT = bodyConnectionLocationOfTail.getY() - textBounds.getHeight();
			g2.translate( xT, yT );
			multilineText.paint( g2, widthGuide, edu.cmu.cs.dennisc.awt.MultilineText.Alignment.LEADING, textBounds );
			g2.translate( -xT, -yT );
//			int xPixel = (int)( bodyConnectionLocationOfTail.getX() + textBoundsOffset.getX() );
//			int yPixel = (int)( bodyConnectionLocationOfTail.getY() + textBoundsOffset.getY() - 10 );
//			g2.setColor( textColor );
//			g2.drawString( text, xPixel, yPixel );
		}

//		g2.setStroke( stroke );
	}
}
