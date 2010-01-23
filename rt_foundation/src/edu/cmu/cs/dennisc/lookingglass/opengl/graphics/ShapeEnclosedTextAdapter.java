package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public abstract class ShapeEnclosedTextAdapter<E extends edu.cmu.cs.dennisc.scenegraph.graphics.ShapeEnclosedText> extends TextAdapter< E > {
	private java.awt.Color fillColor = null;
	private java.awt.Color outlineColor = null;
	protected abstract void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			edu.cmu.cs.dennisc.awt.MultilineText multilineText, 
			java.awt.Font font, 
			java.awt.Color textColor,
			java.awt.Color fillColor, 
			java.awt.Color outlineColor );
	@Override
	protected void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			edu.cmu.cs.dennisc.awt.MultilineText multilineText, 
			java.awt.Font font,
			java.awt.Color textColor ) {
		this.render( g2, lookingGlass, actualViewport, camera, multilineText, font, textColor, this.fillColor, this.outlineColor );
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property ) {
		if( property == m_element.fillColor ) {
			edu.cmu.cs.dennisc.color.Color4f color = this.m_element.fillColor.getValue();
			if( color != null ) {
				this.fillColor = color.getAsAWTColor();
			} else {
				this.fillColor = null;
			}
		} else if( property == m_element.outlineColor ) {
			edu.cmu.cs.dennisc.color.Color4f color = this.m_element.outlineColor.getValue();
			if( color != null ) {
				this.outlineColor = color.getAsAWTColor();
			} else {
				this.outlineColor = null;
			}
		} else {
			super.propertyChanged( property );
		}
	}
}
