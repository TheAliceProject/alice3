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
package test.iconrender;

/**
 * @author Dennis Cosgrove
 */
public class IconRenderTest {
	public static void main( String[] args ) {
		org.lgna.story.SSphere sphere = new org.lgna.story.SSphere();
		test.story.TestScene scene = new test.story.TestScene( sphere );
		org.lgna.story.SProgram program = new org.lgna.story.SProgram();
		program.initializeInFrame( args );
		program.setActiveScene( scene );

		java.awt.Dimension size = new java.awt.Dimension( 512, 512 );

		edu.cmu.cs.dennisc.renderer.RenderFactory renderFactory = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance();

		final edu.cmu.cs.dennisc.renderer.OffscreenRenderTarget offscreenRenderTarget = renderFactory.createOffscreenRenderTarget( size.width, size.height, null );

		org.lgna.story.implementation.CameraImp<?> impCamera = org.lgna.story.EmployeesOnly.getImplementation( scene.getCamera() );
		offscreenRenderTarget.addSgCamera( impCamera.getSgCamera() );

		final javax.swing.JFrame frame = new javax.swing.JFrame();
		final javax.swing.JLabel label = new javax.swing.JLabel();

		final boolean IS_ASYNC = true;
		javax.swing.Icon icon;
		if( IS_ASYNC ) {
			icon = org.alice.stageide.icons.TorusIconFactory.getInstance().getIcon( size );
			label.setIcon( icon );

			final boolean IS_TESTING_ALPHA = true;
			if( IS_TESTING_ALPHA ) {

				label.setOpaque( true );
				label.setBackground( java.awt.Color.RED );
				scene.getGround().setOpacity( 0.0 );

				edu.cmu.cs.dennisc.renderer.ColorAndDepthBuffers rColorAndDepthBuffers = renderFactory.createColorAndDepthBuffers();
				offscreenRenderTarget.getAsynchronousImageCapturer().captureColorBufferWithTransparencyBasedOnDepthBuffer( rColorAndDepthBuffers, new edu.cmu.cs.dennisc.renderer.Observer<edu.cmu.cs.dennisc.renderer.ColorAndDepthBuffers>() {
					@Override
					public void done( edu.cmu.cs.dennisc.renderer.ColorAndDepthBuffers result ) {
						//edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 2000 );
						label.setIcon( new javax.swing.ImageIcon( result.getImage() ) );
						frame.pack();
					}
				} );
			} else {
				edu.cmu.cs.dennisc.renderer.ColorBuffer rColorBuffer = renderFactory.createColorBuffer();
				offscreenRenderTarget.getAsynchronousImageCapturer().captureColorBuffer( rColorBuffer, new edu.cmu.cs.dennisc.renderer.Observer<edu.cmu.cs.dennisc.renderer.ColorBuffer>() {
					@Override
					public void done( edu.cmu.cs.dennisc.renderer.ColorBuffer result ) {
						//edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 2000 );
						label.setIcon( new javax.swing.ImageIcon( result.getImage() ) );
						frame.pack();
					}
				} );
			}
			offscreenRenderTarget.clearAndRenderOffscreen();

		} else {
			java.awt.image.BufferedImage image = offscreenRenderTarget.getSynchronousImageCapturer().getColorBuffer();
			icon = new javax.swing.ImageIcon( image );
			label.setIcon( icon );
		}

		frame.getContentPane().add( label );
		frame.setLocation( 1000, 0 );
		frame.pack();
		frame.setVisible( true );

	}
}
