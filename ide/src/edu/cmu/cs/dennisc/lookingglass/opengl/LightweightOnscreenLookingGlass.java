/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
class LightweightOnscreenLookingGlass extends OnscreenLookingGlass implements edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass{
	class RenderPane extends /*edu.cmu.cs.dennisc.*/javax.media.opengl.GLJPanel {
		
		private Throwable prevThrowable = null;
		
		public RenderPane() {
			super( LookingGlassFactory.createDesiredGLCapabilities( LookingGlassFactory.getDesiredOnscreenSampleCount() ), LookingGlassFactory.getGLCapabilitiesChooser(), null );
//			edu.cmu.cs.dennisc.awt.FontUtilities.setFontToScaledFont( this, 1.5f );
		}
		@Override
		public void display() {
			if( LightweightOnscreenLookingGlass.this.isRenderingEnabled() ) {
				super.display();
			}
		}
		
		private void paintRenderingDisabledMessage( java.awt.Graphics g ) {
			java.awt.Dimension size = this.getSize();
			g.setColor( java.awt.Color.GRAY );
			g.fillRect( 0, 0, size.width, size.height );
			String text = "rendering disabled for performance considerations";
			g.setColor( java.awt.Color.BLACK );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, text, size );
			g.setColor( java.awt.Color.YELLOW );
			g.translate( -1, -1 );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, text, size );
			g.translate( 1, 1 );
			g.dispose();
		}
		@Override
		public void paint( java.awt.Graphics g ) {
			if( LightweightOnscreenLookingGlass.this.isRenderingEnabled() ) {
				super.paint( g );
			} else {
				this.paintRenderingDisabledMessage( g );
			}
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			if( LightweightOnscreenLookingGlass.this.isRenderingEnabled() ) {
				if( LightweightOnscreenLookingGlass.this.getCameraCount() > 0 ) {
					try {
						super.paintComponent( g );
						this.prevThrowable = null;
					} catch( Throwable throwable ) {
						g.setColor( java.awt.Color.RED );
						g.fillRect( 0, 0, getWidth(), getHeight() );
						g.setColor( java.awt.Color.BLACK );
						edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, "error in attempting to render scene", this.getSize() );
						//edu.cmu.cs.dennisc.awt.GraphicsUtilities.drawCenteredText( g, t.getClass().getSimpleName() + " in attempting to render scene", this.getSize() );
						if( this.prevThrowable != null ) {
							//pass
						} else {
							this.prevThrowable = throwable;
							throwable.printStackTrace();
						}
					}
				} else {
					g.setColor( java.awt.Color.DARK_GRAY );
					g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
				}
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "paintComponent" );
////					java.awt.image.BufferedImage offscreenImage = this.getOffscreenImage();
////					if( offscreenImage != null ) {
////						g.drawImage( offscreenImage, 0, 0, this );
////					}
			}
		}
	}
	private RenderPane glPanel = new RenderPane();
//	private javax.media.opengl.GLJPanel glPanel = new javax.media.opengl.GLJPanel();
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
