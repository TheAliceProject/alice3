/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package test.video.views;

class PlayIcon implements javax.swing.Icon {
	public static void paint( java.awt.Component c, java.awt.Graphics g, javax.swing.ButtonModel buttonModel, java.awt.Stroke stroke, int x, int y, int width, int height ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g.translate( x, y );
		Object prevAntialiasing = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		try {
			if( buttonModel.isEnabled() ) {
				if( buttonModel.isSelected() ) {
					//pass
				} else {
					if( buttonModel.isRollover() ) {
						float xCenter = width * 0.5f;
						float yCenter = height * 0.5f;
						g2.setPaint( new java.awt.Color( 255, 255, 191, 9 ) );
						for( float radius = 50.0f; radius < 80.0f; radius += 4.0f ) {
							java.awt.geom.Ellipse2D.Float shape = new java.awt.geom.Ellipse2D.Float( xCenter - radius, yCenter - radius, radius + radius, radius + radius );
							g2.fill( shape );
						}
					}

					java.awt.Stroke prevStroke = g2.getStroke();
					g2.setStroke( stroke );
					java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( 0, 0, width, height, width * 0.6f, height * 0.6f );
					g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 70 ) );
					g2.fill( rr );
					g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 220 ) );
					g2.draw( rr );

					int w = (int)( width * 0.4 );
					int h = (int)( height * 0.45 );
					int xFudge = width / 20;
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.fillTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.EAST, ( ( width - w ) / 2 ) + xFudge, ( height - h ) / 2, w, h );
					g2.setStroke( prevStroke );
				}
			}
		} finally {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, prevAntialiasing );
			g.translate( -x, -y );
		}
	}

	private final java.awt.Stroke stroke;
	private final int width;
	private final int height;

	public PlayIcon( int width, int height ) {
		this.width = width;
		this.height = height;
		int size = Math.max( this.width, this.height );
		this.stroke = new java.awt.BasicStroke( size / 25.0f );
	}

	public int getIconWidth() {
		return this.width;
	}

	public int getIconHeight() {
		return this.height;
	}

	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		if( c instanceof javax.swing.AbstractButton ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			javax.swing.ButtonModel buttonModel = button.getModel();
			paint( c, g, buttonModel, this.stroke, x, y, this.width, this.height );
		}
	}
}

class PlayButtonUI extends javax.swing.plaf.basic.BasicToggleButtonUI {
	@Override
	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
		//super.paint( g, c );
	}
}

class JPlayView extends javax.swing.JToggleButton {
	private static final int SIZE = 64;

	private final javax.swing.Icon icon = new PlayIcon( SIZE, SIZE );

	public JPlayView() {
		this.setModel( new javax.swing.JToggleButton.ToggleButtonModel() );
		this.setRolloverEnabled( true );
		this.setPreferredSize( new java.awt.Dimension( 640, 360 ) );
		this.setLayout( new java.awt.BorderLayout() );
	}

	//	@Override
	//	protected void paintChildren( java.awt.Graphics g ) {
	//		if( this.isSelected() ) {
	//			super.paintChildren( g );
	//		} else {
	//			//pass
	//		}
	//	}
	//
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		if( this.isSelected() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.paintIconCentered( this.icon, this, g );
		}
	}

	@Override
	public void updateUI() {
		this.setUI( new PlayButtonUI() );
	}
}

abstract class PlayView extends org.lgna.croquet.components.View<javax.swing.JToggleButton, test.video.VideoComposite> {
	private final java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			PlayView.this.handleItemStateChanged( e );
		}
	};

	public PlayView( test.video.VideoComposite composite ) {
		super( composite );
		this.getAwtComponent().addItemListener( this.itemListener );
	}

	protected abstract void handleItemStateChanged( java.awt.event.ItemEvent e );

	@Override
	protected javax.swing.JToggleButton createAwtComponent() {
		return new JPlayView();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class VideoView extends PlayView {
	private java.io.File file;

	public VideoView( test.video.VideoComposite composite ) {
		super( composite );
	}

	public void setFile( java.io.File file ) {
		if( file != null ) {
			if( file.exists() ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( file );
			}
		}
		this.file = file;
	}

	private void initializeAndPlay() {
		edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer = edu.cmu.cs.dennisc.video.VideoUtilties.createVideoPlayer();
		this.getAwtComponent().add( videoPlayer.getAwtComponent(), java.awt.BorderLayout.CENTER );
		this.revalidateAndRepaint();
		videoPlayer.playMedia( this.file );
	}

	private void play() {
		java.awt.Component component = this.getAwtComponent().getComponent( 0 );
		if( component instanceof edu.cmu.cs.dennisc.video.VideoPlayer ) {
			edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer = (edu.cmu.cs.dennisc.video.VideoPlayer)component;
			videoPlayer.playMedia( this.file );
		}
	}

	private void setPlaying( boolean isPlaying ) {
		if( this.file != null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( isPlaying );

			javax.swing.JToggleButton jView = this.getAwtComponent();
			int N = jView.getComponentCount();
			switch( N ) {
			case 0:
				this.initializeAndPlay();
				break;
			case 1:
				this.play();
				break;
			default:
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
			}
		}
	}

	@Override
	protected void handleItemStateChanged( java.awt.event.ItemEvent e ) {
		boolean isPlaying = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
		this.setPlaying( isPlaying );
	}
}
