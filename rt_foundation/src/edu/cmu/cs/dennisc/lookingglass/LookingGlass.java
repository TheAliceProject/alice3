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
	
	public static final boolean SUB_ELEMENT_IS_REQUIRED = true;
	public static final boolean SUB_ELEMENT_IS_NOT_REQUIRED = false;
	public PickResult pickFrontMost( int xPixel, int yPixel, boolean isSubElementRequired, PickObserver pickObserver );
	public PickResult pickFrontMost( int xPixel, int yPixel, boolean isSubElementRequired );

	public java.util.List<PickResult> pickAll( int xPixel, int yPixel, boolean isSubElementRequired, PickObserver pickObserver );
	public java.util.List<PickResult> pickAll( int xPixel, int yPixel, boolean isSubElementRequired );

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
