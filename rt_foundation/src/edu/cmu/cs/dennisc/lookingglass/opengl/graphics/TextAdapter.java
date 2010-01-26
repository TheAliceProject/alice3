package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public abstract class TextAdapter< E extends edu.cmu.cs.dennisc.scenegraph.graphics.Text > extends edu.cmu.cs.dennisc.lookingglass.opengl.GraphicAdapter< E > {
	private edu.cmu.cs.dennisc.awt.MultilineText multilineText;
	private java.awt.Font rememberedFont = null;
	private java.awt.Color textColor = null;
	protected abstract float getWrapWidth( java.awt.Rectangle actualViewport );
	protected abstract void render( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, edu.cmu.cs.dennisc.awt.MultilineText multilineText, java.awt.Font font, java.awt.Color textColor, float wrapWidth );
	private void forgetFontIfNecessary( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 ) {
		if( this.rememberedFont != null ) {
			g2.forget( this.rememberedFont );
			this.rememberedFont = null;
		}
	}
	@Override
	protected void render( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		String text = this.m_element.text.getValue();
		java.awt.Font font = this.m_element.font.getValue();
		if( font == this.rememberedFont ) {
			//pass
		} else {
			this.forgetFontIfNecessary( g2 );
			g2.remember( font );
			this.rememberedFont = font;
		}
		if( this.multilineText != null ) {
			//pass
		} else {
			this.multilineText = new edu.cmu.cs.dennisc.awt.MultilineText( text );
		}
		this.render( g2, lookingGlass, actualViewport, camera, this.multilineText, this.rememberedFont, this.textColor, this.getWrapWidth( actualViewport ) );
	}
	@Override
	protected void forget( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 ) {
		this.forgetFontIfNecessary( g2 );
	}
	
	private void markMultilineTextInvalid() {
		this.multilineText = null;
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.text ) {
			this.markMultilineTextInvalid();
		} else if( property == m_element.font ) {
			this.markMultilineTextInvalid();
		} else if( property == m_element.textColor ) {
			edu.cmu.cs.dennisc.color.Color4f color = this.m_element.textColor.getValue();
			if( color != null ) {
				this.textColor = color.getAsAWTColor();
			} else {
				this.textColor = null;
			}
		} else {
			super.propertyChanged( property );
		}
	}
}
