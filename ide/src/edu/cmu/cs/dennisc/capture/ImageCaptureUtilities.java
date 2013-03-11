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
package edu.cmu.cs.dennisc.capture;

/**
 * @author Dennis Cosgrove
 */
public class ImageCaptureUtilities {
	private ImageCaptureUtilities() {
	}

	public static java.awt.Image captureImage( java.awt.Component awtComponent, Integer dpiImage ) {
		java.awt.Toolkit toolkit = awtComponent.getToolkit();

		if( awtComponent.isLightweight() ) { //todo: check descendants?
			int dpiScreen = toolkit.getScreenResolution();
			int dpiImageActual;
			if( dpiImage != null ) {
				dpiImageActual = dpiImage;
			} else {
				dpiImageActual = dpiScreen;
			}
			java.awt.Dimension componentSizeScreen = awtComponent.getSize();
			int imageWidth;
			int imageHeight;
			double scale;
			if( dpiImageActual == dpiScreen ) {
				scale = Double.NaN;
				imageWidth = componentSizeScreen.width;
				imageHeight = componentSizeScreen.height;
			} else {
				scale = dpiImageActual / (double)dpiScreen;
				imageWidth = (int)Math.floor( componentSizeScreen.width * scale );
				imageHeight = (int)Math.floor( componentSizeScreen.height * scale );
			}

			java.awt.Image rv = awtComponent.createImage( imageWidth, imageHeight );

			java.awt.Graphics g = rv.getGraphics();
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			java.awt.geom.AffineTransform prevTrans = g2.getTransform();
			if( Double.isNaN( scale ) ) {
				//pass
			} else {
				g2.scale( scale, scale );
			}

			awtComponent.print( g );

			g2.setTransform( prevTrans );
			g.dispose();

			return rv;
		} else {
			if( dpiImage != null ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( awtComponent, "is heavyweight." );
			}
			try {
				java.awt.GraphicsConfiguration graphicsConfiguration = awtComponent.getGraphicsConfiguration();
				java.awt.Robot robot = new java.awt.Robot( graphicsConfiguration.getDevice() );
				java.awt.Point locationOnScreen = awtComponent.getLocationOnScreen();
				java.awt.Rectangle rect = new java.awt.Rectangle( locationOnScreen, awtComponent.getSize() );
				return robot.createScreenCapture( rect );
			} catch( java.awt.AWTException awte ) {
				throw new RuntimeException( awte );
			}
		}
	}

	public static void main( String[] args ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				final javax.swing.JFrame frame = new javax.swing.JFrame();
				frame.setTitle( "test image capture" );
				frame.getContentPane().setBackground( java.awt.Color.RED );
				frame.setSize( 320, 240 );
				frame.setVisible( true );
				new Thread() {
					@Override
					public void run() {
						edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 1000 );
						java.awt.Image image = captureImage( frame, 300 );
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( image );
						edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
					}
				}.start();
			}
		} );
	}
}
