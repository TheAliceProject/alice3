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
package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
public abstract class Widget extends Component {
	private class OverlayWidgetTexture extends edu.cmu.cs.dennisc.texture.CustomTexture {
		private int m_width;
		private int m_height;
		public OverlayWidgetTexture( int width, int height ) {
			m_width = width;
			m_height = height;
		}
		@Override
		public edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy getMipMapGenerationPolicy() {
			return edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy.PAINT_ONLY_HIGHEST_LEVEL_THEN_SCALE_REMAINING;
		}
		@Override
		public boolean isMipMappingDesired() {
			return false;
		}
		@Override
		public boolean isPotentiallyAlphaBlended() {
			//todo?
			return true;
		}
		public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			throw new RuntimeException( "todo" );
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			throw new RuntimeException( "todo" );
		}
		@Override
		public void layoutIfNecessary( java.awt.Graphics2D g2 ) {
			//Widget.this.computePreferredSize( g2, -1, -1 );
		}
		@Override
		public int getWidth() {
			return m_width;
		}
		@Override
		public int getHeight() {
			return m_height;
		}
		@Override
		public boolean isAnimated() {
			return false;
		}
		@Override
		public void paint( java.awt.Graphics2D g2, int width, int height ) {
			Widget.this.clearTexture( g2, m_width, m_height );
			Widget.this.paintTexture( g2 );
		}
	};

	private OverlayWidgetTexture m_texture = null;

	private Renderer m_renderer = null;

	public Renderer getRenderer() {
		return m_renderer;
	}
	public void setRenderer( Renderer renderer ) {
		m_renderer = renderer;
	}

	private boolean m_isTextureDirty = true;

	public boolean isTextureDirty() {
		return m_isTextureDirty;
	}
	public void setTextureDirty( boolean isTextureDirty ) {
		if( m_isTextureDirty != isTextureDirty ) {
			m_isTextureDirty = isTextureDirty;
			repaint();
		}
	}

	@Override
	public void computePreferredSize( java.awt.Graphics g, int width, int height ) {
		m_renderer.layoutIfNecessary( g, isHighlighted(), isPressed(), isSelected() );
		int desiredTextureWidth = (int)(m_renderer.getWidth() + 0.999f);		
		int desiredTextureHeight = (int)(m_renderer.getHeight() + 0.999f);		
		if( m_texture != null ) {
			if( m_texture.getWidth() < desiredTextureHeight || m_texture.getHeight() < desiredTextureHeight ) {
				m_texture.release();
				m_texture = null;
			}
		}
		
		if( m_texture != null ) {
			//pass
		} else {
			m_texture = new OverlayWidgetTexture( desiredTextureWidth, desiredTextureHeight );
		}
		setTextureDirty( true );
	}

	@Override
	public float getWidth() {
		return m_renderer.getWidth();
	}
	@Override
	public float getHeight() {
		return m_renderer.getHeight();
	}

	protected void clearTexture( java.awt.Graphics2D g, int width, int height ) {
		java.awt.Composite composite = g.getComposite();
		g.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.CLEAR, 0.0f ) );
		try {
			g.fill( new java.awt.geom.Rectangle2D.Double( 0, 0, width, height ) );
		} finally {
			g.setComposite( composite );
		}
	}
	protected void paintTexture( java.awt.Graphics2D g2 ) {
		assert m_renderer != null;
		m_renderer.paint( g2, isHighlighted(), isPressed(), isSelected() );
	}

	@Override
	public void paint( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e, java.awt.Graphics2D g2 ) {
		assert isVisible();
		assert Float.isNaN( getX() ) == false;
		assert Float.isNaN( getY() ) == false;
		if( m_isTextureDirty ) {
			if( m_texture != null ) {
				m_texture.fireTextureChanged();
			}
			m_isTextureDirty = false;
		}
		edu.cmu.cs.dennisc.lookingglass.Graphics2D g = e.getGraphics2D();
		if( m_texture != null ) {
			if( g.isRemembered( m_texture ) ) {
				//pass
			} else {
				g.remember( m_texture );
			}
			g.paint( m_texture, getX(), getY(), getOpacity() );
		}
	}
}
