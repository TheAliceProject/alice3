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
package edu.wustl.lookingglass.media;

/**
 * @author Dennis Cosgrove
 */
public class ImagesToWebmEncoderTest {
	public static void main( String[] args ) throws Exception {
		final java.awt.Dimension dimension = new java.awt.Dimension( 1080, 720 );
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( dimension.width, dimension.height, java.awt.image.BufferedImage.TYPE_3BYTE_BGR );
		boolean isUpsideDown = true;
		ImagesToWebmEncoder encoder = new ImagesToWebmEncoder( 30.0, dimension );
		//NewSchoolImagesToWebmEncoder encoder = new NewSchoolImagesToWebmEncoder( 30, dimension, isUpsideDown );
		encoder.start();
		java.awt.Color color1 = new java.awt.Color( 0, 0, 0 );
		for( int i = 0; i < 2560; i += 1 ) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			java.awt.Color color2 = new java.awt.Color( i % 256, 0, 0 );
			g2.setPaint( new java.awt.GradientPaint( 0, 0, color1, dimension.width, dimension.height, color2 ) );
			g2.fillRect( 0, 0, dimension.width, dimension.height );
			encoder.addBufferedImage( image, isUpsideDown );
			System.out.println( i );
			//Thread.sleep( 10 );
		}
		encoder.stop();
		//System.out.println( new String( encoder.getStdout().toByteArray() ) );
		//System.err.println( new String( encoder.getStderr().toByteArray() ) );
		final java.io.File file = encoder.getEncodedVideoFile();
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( file, file.exists() );

		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				javax.swing.JFrame frame = new javax.swing.JFrame();
				final edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer = edu.cmu.cs.dennisc.video.vlcj.VlcjUtilities.createVideoPlayer();
				videoPlayer.prepareMedia( file.toURI() );
				frame.getContentPane().add( videoPlayer.getVideoSurface() );
				videoPlayer.getVideoSurface().setPreferredSize( dimension );
				frame.pack();
				frame.setVisible( true );

				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						videoPlayer.playResume();
					}
				} );
			}
		} );
	}
}
