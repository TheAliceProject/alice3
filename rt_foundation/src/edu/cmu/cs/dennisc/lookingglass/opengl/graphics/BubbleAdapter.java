package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public abstract class BubbleAdapter<E extends edu.cmu.cs.dennisc.scenegraph.graphics.Bubble> extends ShapeEnclosedTextAdapter< E > {
	private java.awt.geom.Point2D.Float originOfTail = new java.awt.geom.Point2D.Float();
	private java.awt.geom.Point2D.Float bodyConnectionLocationOfTail = new java.awt.geom.Point2D.Float();
	private java.awt.geom.Point2D.Float textBoundsOffset = new java.awt.geom.Point2D.Float();
	@Override
	protected float getWrapWidth( java.awt.Rectangle actualViewport ) {
		return (float)( actualViewport.getWidth() * 0.5 ); 
	}
	protected abstract void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			edu.cmu.cs.dennisc.awt.MultilineText multilineText, 
			java.awt.Font font, 
			java.awt.Color textColor, 
			float wrapWidth,
			java.awt.Color fillColor, 
			java.awt.Color outlineColor,
			java.awt.geom.Point2D.Float originOfTail,
			java.awt.geom.Point2D.Float bodyConnectionLocationOfTail,
			java.awt.geom.Point2D.Float textBoundsOffset,
			double portion );
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
		edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator originator = this.m_element.originator.getValue();
		if( originator != null ) {
			g2.setFont( font );
			java.awt.geom.Dimension2D size = multilineText.getDimension( g2, wrapWidth );
			originator.calculate( originOfTail, bodyConnectionLocationOfTail, textBoundsOffset, this.m_element, lookingGlass, actualViewport, camera, size );
			this.render( g2, lookingGlass, actualViewport, camera, multilineText, font, textColor, wrapWidth, fillColor, outlineColor, originOfTail, bodyConnectionLocationOfTail, textBoundsOffset, m_element.portion.getValue() );
		}
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property ) {
		if( property == m_element.originator ) {
			//pass
		} else if( property == m_element.portion ) {
			//pass
		} else {
			super.propertyChanged( property );
		}
	}
}
