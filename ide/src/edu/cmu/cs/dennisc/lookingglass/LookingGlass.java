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

package edu.cmu.cs.dennisc.lookingglass;

/**
 * @author Dennis Cosgrove
 */
public interface LookingGlass extends edu.cmu.cs.dennisc.pattern.Releasable {
	public LookingGlassFactory getLookingGlassFactory();
	
	public java.awt.Dimension getSize();
	public java.awt.Dimension getSize( java.awt.Dimension rv );
	public int getWidth();
	public int getHeight();

	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer();
	public java.awt.image.BufferedImage getColorBuffer( java.awt.image.BufferedImage rv );
	public java.awt.image.BufferedImage getColorBuffer();
	
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer();
	public java.nio.FloatBuffer createFloatBufferForUseAsDepthBuffer();
	public java.nio.FloatBuffer getDepthBuffer( java.nio.FloatBuffer rv );
	public java.nio.FloatBuffer getDepthBuffer();

	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer );
	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer();
		
	public java.awt.Graphics2D createGraphics( edu.cmu.cs.dennisc.texture.Texture texture );
	public void commitGraphics( edu.cmu.cs.dennisc.texture.Texture texture, java.awt.Graphics2D g, int x, int y, int width, int height );
	public void commitGraphics( edu.cmu.cs.dennisc.texture.Texture texture, java.awt.Graphics2D g );

	//todo
	public java.awt.Image getImage( edu.cmu.cs.dennisc.texture.Texture texture );
	

	public String getDescription();
	public void setDescription( String description );

	public void addCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public void removeCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public void clearCameras();
	public int getCameraCount();
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAt( int index );
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] getCameras( edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] rv );
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] getCameras();
	public Iterable<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> accessCameras();

	public void addLookingGlassListener( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener );
	public void removeLookingGlassListener( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener );
	public int getLookingGlassListenerCount();
	public edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener getLookingGlassListenerAt( int index );
	public edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] getLookingGlassListeners( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] rv );
	public edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] getLookingGlassListeners();
	public Iterable< edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener > accessLookingGlassListeners();
	
	public Picker getPicker();
	
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera );
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera );
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera perspectiveCamera );
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera perspectiveCamera );
	
	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera symmetricPerspectiveCamera );
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera symmetricPerspectiveCamera );
	
	
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel );
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel );
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel );

	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public java.awt.Rectangle getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	public java.awt.Rectangle getSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public void setSpecifiedViewport( java.awt.Rectangle viewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );

	public boolean isLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	public void setIsLetterboxedAsOpposedToDistorted( boolean isLetterboxedAsOpposedToDistorted, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera );
	
	public boolean isRenderingEnabled();
	public void setRenderingEnabled( boolean isRenderingEnabled );
}
