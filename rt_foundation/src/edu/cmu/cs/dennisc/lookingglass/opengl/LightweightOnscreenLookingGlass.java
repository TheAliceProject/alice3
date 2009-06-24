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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
class LightweightOnscreenLookingGlass extends OnscreenLookingGlass implements edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass{
//	class RenderPane extends javax.media.opengl.GLJPanel {
	class RenderPane extends edu.cmu.cs.dennisc.media.opengl.GLJPanel {
		private Throwable prevThrowable = null;
		@Override
		public void display() {
			if( LightweightOnscreenLookingGlass.this.isRenderingEnabled() ) {
				super.display();
			}
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			try {
				if( LightweightOnscreenLookingGlass.this.isRenderingEnabled() ) {
					super.paintComponent( g );
				} else {
					java.awt.image.BufferedImage offscreenImage = this.getOffscreenImage();
					if( offscreenImage != null ) {
						g.drawImage( offscreenImage, 0, 0, this );
						String text = "rendering disabled for performance considerations";
						java.awt.Dimension size = this.getSize();
						g.setColor( java.awt.Color.BLACK );
						edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, size );
						g.setColor( java.awt.Color.YELLOW );
						g.translate( -1, -1 );
						edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, size );
						g.translate( 1, 1 );
					}
				}
				this.prevThrowable = null;
			} catch( Throwable t ) {
				g.setColor( java.awt.Color.RED );
				g.fillRect( 0, 0, getWidth(), getHeight() );
				g.setColor( java.awt.Color.BLACK );
				edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, "error in attempting to render scene", this.getSize() );
				//edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, t.getClass().getSimpleName() + " in attempting to render scene", this.getSize() );
				if( this.prevThrowable != null ) {
					//pass
				} else {
					this.prevThrowable = t;
					t.printStackTrace();
				}
			}
		}
	}
	private RenderPane glPanel = new RenderPane();
	//private javax.media.opengl.GLJPanel glPanel = new javax.media.opengl.GLJPanel();
	/*package-private*/ LightweightOnscreenLookingGlass( LookingGlassFactory lookingGlassFactory ) {
		super( lookingGlassFactory );
		this.glPanel.setFocusable( true );
	}
	
	public javax.swing.JPanel getJPanel() {
		return this.glPanel;
	}
	public java.awt.Component getAWTComponent() {
		return getJPanel();
	}
	public java.awt.Dimension getSize( java.awt.Dimension rv ) {
		return this.glPanel.getSize( rv );
	}
	public void repaint() {
		this.glPanel.repaint();
	}
	
	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		return this.glPanel;
	}
}
