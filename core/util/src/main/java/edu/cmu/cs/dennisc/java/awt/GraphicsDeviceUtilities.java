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

package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsDeviceUtilities {
	private GraphicsDeviceUtilities() {
		throw new AssertionError();
	}

	public static java.awt.Rectangle getGraphicsDeviceConfigurationBoundsFor( java.awt.GraphicsDevice[] graphicsDevices, java.awt.Point p ) {
		for( java.awt.GraphicsDevice graphicsDevice : graphicsDevices ) {
			java.awt.GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
			java.awt.Rectangle bounds = graphicsConfiguration.getBounds();
			if( bounds.contains( p ) ) {
				return bounds;
			}

			// slllllllllllllowwwwwwwwwwwww
			// for( java.awt.GraphicsConfiguration graphicsConfiguration : graphicsDevice.getConfigurations() ) {

			// returns null
			//java.awt.Window window = graphicsDevice.getFullScreenWindow();
			//if( window != null ) {
			//	java.awt.Rectangle bounds = window.getBounds();
			//}
		}
		return null;
	}

	public static java.awt.Rectangle getGraphicsDeviceConfigurationBoundsFor( java.awt.Point p ) {
		java.awt.GraphicsEnvironment graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
		return getGraphicsDeviceConfigurationBoundsFor( graphicsEnvironment.getScreenDevices(), p );
	}

	public static java.awt.Rectangle getScreenDeviceDefaultConfigurationBounds( int screenDeviceIndex ) {
		java.awt.GraphicsEnvironment graphicsEnvironment = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
		java.awt.GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		if( screenDeviceIndex < graphicsDevices.length ) {
			java.awt.GraphicsDevice graphicsDevice = graphicsDevices[ screenDeviceIndex ];
			java.awt.GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
			return graphicsConfiguration.getBounds();
		} else {
			return null;
		}
	}
	//	public static void main( String[] args ) {
	//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
	//			public void run() {
	////				new javax.swing.JFrame().setVisible( true );
	//				java.awt.Rectangle bounds = getGraphicsDeviceConfigurationBoundsFor( new java.awt.Point( -1000, 100 ) );
	//				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( bounds );
	//			}
	//		} );
	//	}
}
