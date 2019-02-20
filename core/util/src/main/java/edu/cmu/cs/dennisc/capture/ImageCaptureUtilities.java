/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package edu.cmu.cs.dennisc.capture;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.awt.ToolkitUtilities;
import edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities;
import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.geom.AffineTransform;

/**
 * @author Dennis Cosgrove
 */
public class ImageCaptureUtilities {
	private ImageCaptureUtilities() {
	}

	private static Image captureHeavyweight( Component awtComponent, Rectangle bounds ) {
		try {
			GraphicsConfiguration graphicsConfiguration = awtComponent.getGraphicsConfiguration();
			Robot robot = new Robot( graphicsConfiguration.getDevice() );
			Point locationOnScreen = awtComponent.getLocationOnScreen();
			Rectangle graphicsConfigurationBounds = graphicsConfiguration.getBounds();
			locationOnScreen.x -= graphicsConfigurationBounds.x;
			locationOnScreen.y -= graphicsConfigurationBounds.y;
			Rectangle rect;
			if( bounds != null ) {
				rect = new Rectangle( locationOnScreen.x + bounds.x, locationOnScreen.y + bounds.y, bounds.width, bounds.height );
			} else {
				rect = new Rectangle( locationOnScreen, awtComponent.getSize() );
			}
			return robot.createScreenCapture( rect );
		} catch( AWTException awte ) {
			throw new RuntimeException( awte );
		}
	}

	private static class ImageScalePair {
		private final Image image;
		private final double scale;

		public ImageScalePair( Image image, double scale ) {
			this.image = image;
			this.scale = scale;
		}

		public Image getImage() {
			return this.image;
		}

		public double getScale() {
			return this.scale;
		}
	}

	private static ImageScalePair createImage( Component awtComponent, Rectangle bounds, Integer dpiImage ) {
		int dpiScreen = ToolkitUtilities.getScreenResolution( awtComponent );
		int dpiImageActual;
		if( dpiImage != null ) {
			dpiImageActual = dpiImage;
		} else {
			dpiImageActual = dpiScreen;
		}
		Dimension sizeScreen;
		if( bounds != null ) {
			sizeScreen = bounds.getSize();
		} else {
			sizeScreen = awtComponent.getSize();
		}
		int imageWidth;
		int imageHeight;
		double scale;
		if( dpiImageActual == dpiScreen ) {
			scale = Double.NaN;
			imageWidth = sizeScreen.width;
			imageHeight = sizeScreen.height;
		} else {
			double imageToScreenRatio = dpiImageActual / (double)dpiScreen;
			imageWidth = (int)Math.floor( sizeScreen.width * imageToScreenRatio );
			imageHeight = (int)Math.floor( sizeScreen.height * imageToScreenRatio );
			scale = imageWidth / (double)sizeScreen.width;
		}

		Image image = awtComponent.createImage( imageWidth, imageHeight );
		return new ImageScalePair( image, scale );
	}

	private static final boolean IS_PRINT_GOOD_TO_GO_GL = false;

	public static Image captureRectangle( Component awtComponent, Rectangle bounds, Integer dpiImage ) {
		if( awtComponent.isLightweight() ) { //todo: check descendants?
			ImageScalePair imageScalePair = createImage( awtComponent, bounds, dpiImage );

			Image rv = imageScalePair.getImage();
			double scale = imageScalePair.getScale();

			Graphics g = rv.getGraphics();
			Graphics2D g2 = (Graphics2D)g;

			g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			AffineTransform prevTrans = g2.getTransform();
			if( Double.isNaN( scale ) ) {
				//pass
			} else {
				g2.scale( scale, scale );
			}
			if( bounds != null ) {
				g2.translate( -bounds.x, -bounds.y );
			}

			if( IS_PRINT_GOOD_TO_GO_GL ) {
				awtComponent.print( g2 );
			} else {
				awtComponent.paint( g2 );
			}

			g2.setTransform( prevTrans );
			g.dispose();

			return rv;
		} else {
			throw new IllegalArgumentException( awtComponent + " is heavyweight." );
		}
	}

	public static Image captureComplete( Component awtComponent, Integer dpiImage ) {
		ImageScalePair imageScalePair = createImage( awtComponent, null, dpiImage );

		Image rv = imageScalePair.getImage();
		double scale = imageScalePair.getScale();

		Graphics g = rv.getGraphics();
		Graphics2D g2 = (Graphics2D)g;

		g2.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		AffineTransform prevTrans = g2.getTransform();
		if( Double.isNaN( scale ) ) {
			//pass
		} else {
			g2.scale( scale, scale );
		}

		Component lightweightComponent;
		if( awtComponent.isLightweight() ) {
			lightweightComponent = awtComponent;
		} else {
			Image heavyweight = captureHeavyweight( awtComponent, null );
			g.drawImage( heavyweight, 0, 0, awtComponent );

			if( awtComponent instanceof JFrame ) {
				JFrame jFrame = (JFrame)awtComponent;
				lightweightComponent = jFrame.getRootPane();
			} else if( awtComponent instanceof JDialog ) {
				JDialog jDialog = (JDialog)awtComponent;
				lightweightComponent = jDialog.getRootPane();
			} else if( awtComponent instanceof JWindow ) {
				JWindow jWindow = (JWindow)awtComponent;
				lightweightComponent = jWindow.getRootPane();
			} else {
				lightweightComponent = null;
			}
		}

		if( lightweightComponent != null ) {
			if( lightweightComponent != awtComponent ) {
				Point p = ComponentUtilities.convertPoint( lightweightComponent, 0, 0, awtComponent );
				g.translate( p.x, p.y );
			}
			if( IS_PRINT_GOOD_TO_GO_GL ) {
				lightweightComponent.print( g );
			} else {
				lightweightComponent.paint( g );
			}
		}

		//todo: check for heavyweight popup menus
		//for( java.awt.Window window : java.awt.Window.getWindows() ) {
		//	edu.cmu.cs.dennisc.java.util.logging.Logger.outln( window );
		//}

		g2.setTransform( prevTrans );
		g.dispose();

		return rv;
	}

	public static void main( String[] args ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();
				frame.setTitle( "test image capture" );
				frame.getContentPane().setBackground( new Color( 191, 191, 255 ) );
				frame.getContentPane().add( new JLabel( "four score and seven years ago" ) );
				frame.setSize( 320, 240 );
				frame.setLocation( 0, 0 );
				frame.setVisible( true );
				new Thread() {
					@Override
					public void run() {
						ThreadUtilities.sleep( 1000 );
						Image image = captureComplete( frame, 300 );
						Logger.outln( image );
						ClipboardUtilities.setClipboardContents( image );
					}
				}.start();
			}
		} );
	}
}
