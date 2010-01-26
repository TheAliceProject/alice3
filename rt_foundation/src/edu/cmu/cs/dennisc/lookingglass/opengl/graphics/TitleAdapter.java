package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public abstract class TitleAdapter<E extends edu.cmu.cs.dennisc.scenegraph.graphics.Title> extends ShapeEnclosedTextAdapter< E > {
	protected static final float VERTICAL_MARGIN_TIMES_2 = 20.0f;
	private java.awt.geom.Rectangle2D.Float backgroundBounds = new java.awt.geom.Rectangle2D.Float();
	@Override
	protected float getWrapWidth( java.awt.Rectangle actualViewport ) {
		return (float)( actualViewport.getWidth() * 0.9 ); 
	}
	protected abstract java.awt.geom.Rectangle2D.Float getFillBounds( java.awt.geom.Rectangle2D.Float rv, java.awt.Rectangle actualViewport, java.awt.geom.Dimension2D multilineTextSize );
	@Override
	protected void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			edu.cmu.cs.dennisc.awt.MultilineText multilineText, 
			java.awt.Font font, 
			java.awt.Color textColor,
			float wrapWidth,
			java.awt.Color fillColor, 
			java.awt.Color outlineColor ) {	
		synchronized( this.backgroundBounds ) {
			g2.setFont( font );
			java.awt.geom.Dimension2D size = multilineText.getDimension( g2, wrapWidth );
			this.getFillBounds( this.backgroundBounds, actualViewport, size );
			if( fillColor != null ) {
				g2.setColor( fillColor );
				g2.fill( this.backgroundBounds );
			}
			if( outlineColor != null ) {
				g2.setColor( outlineColor );
				g2.draw( this.backgroundBounds );
			}
			if( textColor != null ) {
				g2.setColor( textColor );
				g2.setFont( font );
				multilineText.paint( g2, wrapWidth, edu.cmu.cs.dennisc.awt.TextAlignment.CENTER, this.backgroundBounds );
//				float x = this.backgroundBounds.x - (float)textBounds.getX() + this.backgroundBounds.width * 0.5f - (float)textBounds.getWidth() * 0.5f;
//				float y = this.backgroundBounds.y - (float)textBounds.getY() + this.backgroundBounds.height * 0.5f - (float)textBounds.getHeight() * 0.5f;
//				g2.setFont( font );
//				g2.drawString( text, x, y );
			}
		}
	}
}
