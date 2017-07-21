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
package org.lgna.story.resourceutilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Hexahedron;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author Dave Culyba
 */
public abstract class AbstractThumbnailMaker {
	public static final int DEFAULT_THUMBNAIL_WIDTH = 160;
	public static final int DEFAULT_THUMBNAIL_HEIGHT = ( DEFAULT_THUMBNAIL_WIDTH * 9 ) / 16;
	protected static final int DEFAULT_ANTI_ALIAS_FACTOR = 4;

	private final int width;
	private final int height;
	private final int antAliasFactor;

	private final org.lgna.story.implementation.SceneImp scene;
	private final edu.cmu.cs.dennisc.scenegraph.Transformable sgModelTransformable;
	private final Transformable sgCameraVehicle;
	private final SymmetricPerspectiveCamera sgCamera;
	private final edu.cmu.cs.dennisc.render.OffscreenRenderTarget offscreenRenderTarget;

	protected AbstractThumbnailMaker( int width, int height )
	{
		this( width, height, DEFAULT_ANTI_ALIAS_FACTOR );
	}

	protected AbstractThumbnailMaker( int width, int height, int antiAliasFactor )
	{
		this.width = width;
		this.height = height;
		this.antAliasFactor = antiAliasFactor;

		this.scene = new org.lgna.story.implementation.SceneImp( null );
		this.sgModelTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
		this.sgCameraVehicle = new Transformable();
		this.sgCamera = new SymmetricPerspectiveCamera();

		this.scene.getSgComposite().addComponent( this.sgModelTransformable );
		this.sgCameraVehicle.setParent( this.scene.getSgComposite() );
		this.sgCameraVehicle.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( 0, 0, 32 ) );
		this.sgCamera.farClippingPlaneDistance.setValue( 1000.0 );
		this.sgCamera.nearClippingPlaneDistance.setValue( .1 );
		this.sgCamera.setParent( this.sgCameraVehicle );
		this.offscreenRenderTarget = edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().createOffscreenRenderTarget( this.width * this.antAliasFactor, this.height * this.antAliasFactor, null, new edu.cmu.cs.dennisc.render.RenderCapabilities.Builder().build() );
		setUpCamera( this.offscreenRenderTarget );
	}

	protected void removeComponent( edu.cmu.cs.dennisc.scenegraph.Component sgComponent )
	{
		if( this.offscreenRenderTarget.getSgCameraCount() > 0 ) {
			for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera : this.offscreenRenderTarget.getSgCameras() ) {
				GlrAbstractCamera<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapterI = AdapterFactory.getAdapterFor( camera );
				edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene sceneAdapter = cameraAdapterI.getGlrScene();
				edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?> componentAdapter = AdapterFactory.getAdapterFor( sgComponent );
				if( componentAdapter != null ) {
					sceneAdapter.EPIC_HACK_FOR_THUMBNAIL_MAKER_removeDescendant( componentAdapter );
				}
			}
		}
	}

	protected void clear()
	{

		this.offscreenRenderTarget.forgetAllCachedItems();
		this.offscreenRenderTarget.clearUnusedTextures();
	}

	protected static boolean isTransparent( int pixel ) {
		int alpha = pixel >> 24;
		return alpha == 0;
	}

	protected static int getLeftBorder( java.awt.image.BufferedImage image )
	{
		int width = image.getWidth();
		int height = image.getHeight();
		for( int x = 0; x < width; x++ )
		{
			for( int y = 0; y < height; y++ )
			{
				if( !isTransparent( image.getRGB( x, y ) ) )
				{
					return x;
				}
			}
		}
		if( width >= image.getWidth() )
		{
			width = 0;
		}
		return width;
	}

	protected static int getRightBorder( java.awt.image.BufferedImage image ) {
		int width = image.getWidth();
		int height = image.getHeight();
		for( int x = width - 1; x >= 0; x-- )
		{
			for( int y = 0; y < height; y++ )
			{
				if( !isTransparent( image.getRGB( x, y ) ) )
				{
					return width - x;
				}
			}
		}
		if( width >= image.getWidth() )
		{
			width = 0;
		}
		return width;
	}

	protected static int getTopBorder( java.awt.image.BufferedImage image ) {
		int width = image.getWidth();
		int height = image.getHeight();
		for( int y = 0; y < height; y++ )
		{
			for( int x = 0; x < width; x++ )
			{
				if( !isTransparent( image.getRGB( x, y ) ) )
				{
					return y;
				}
			}
		}
		if( height >= image.getHeight() )
		{
			height = 0;
		}
		return height;
	}

	protected static int getBottomBorder( java.awt.image.BufferedImage image ) {
		int width = image.getWidth();
		int height = image.getHeight();
		for( int y = height - 1; y >= 0; y-- )
		{
			for( int x = 0; x < width; x++ )
			{
				if( !isTransparent( image.getRGB( x, y ) ) )
				{
					return height - y;
				}
			}
		}
		if( height >= image.getHeight() )
		{
			height = 0;
		}
		return height;
	}

	protected static boolean isFullyFramed( java.awt.image.BufferedImage image ) {
		int width = image.getWidth();
		int right = width - 1;
		int height = image.getHeight();
		int bottom = height - 1;
		for( int x = 0; x < width; x++ )
		{
			int topPixel = image.getRGB( x, 0 );
			if( !isTransparent( topPixel ) )
			{
				return false;
			}
			int bottomPixel = image.getRGB( x, bottom );
			if( !isTransparent( bottomPixel ) )
			{
				return false;
			}
		}
		for( int y = 0; y < height; y++ )
		{
			int leftPixel = image.getRGB( 0, y );
			if( !isTransparent( leftPixel ) )
			{
				return false;
			}
			int rightPixel = image.getRGB( right, y );
			if( !isTransparent( rightPixel ) )
			{
				return false;
			}
		}
		return true;
	}

	private static final boolean DEBUG_SAVE_TEST_IMAGES = false;
	private static final String THUMBNAIL_SCRATCH_SPACE = "C:/batchOutput/thumbnailScratchSpace/";

	protected static void writeDebugImageIfAppropriate( String filename, java.awt.image.BufferedImage image ) {
		if( DEBUG_SAVE_TEST_IMAGES ) {
			String path = THUMBNAIL_SCRATCH_SPACE + filename;
			try {
				ImageUtilities.write( path, image );
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( path, ioe );
			}
		}
	}

	protected synchronized java.awt.image.BufferedImage takePicture( AffineMatrix4x4 cameraTransform, boolean trimWhitespace, java.awt.Color colorKey ) {
		getSGCameraVehicle().setLocalTransformation( cameraTransform );
		//offscreenRenderTarget.clearAndRenderOffscreen();
		java.awt.image.BufferedImage rv = offscreenRenderTarget.getSynchronousImageCapturer().getColorBufferWithTransparencyBasedOnDepthBuffer();

		writeDebugImageIfAppropriate( "rawFinal.png", rv );

		if( trimWhitespace ) {
			int topBorder = getTopBorder( rv );
			int bottomBorder = getBottomBorder( rv );
			int rightBorder = getRightBorder( rv );
			int leftBorder = getLeftBorder( rv );

			int newHeight = rv.getHeight() - topBorder - bottomBorder;
			int newWidth = rv.getWidth() - leftBorder - rightBorder;

			if( ( leftBorder < 0 ) || ( topBorder < 0 ) || ( newWidth < 0 ) || ( newHeight < 0 ) )
			{
				//pass
			}
			else
			{
				rv = rv.getSubimage( leftBorder, topBorder, newWidth, newHeight );
			}

		}
		//If a color key is specified, go through the image and make all matching pixels transparent
		//Make sure to do this before the image is scaled to prevent the scaling from blending the pixels and making the color keying harder
		if( colorKey != null ) {
			float[] chromaHSB = new float[ 3 ];
			float[] pixelHSB = new float[ 3 ];
			int[] pixelBuffer = new int[ 4 ];
			java.awt.Color.RGBtoHSB( colorKey.getRed(), colorKey.getGreen(), colorKey.getBlue(), chromaHSB );
			Raster imageData = rv.getRaster();
			WritableRaster writableData = null;
			if( imageData instanceof WritableRaster ) {
				writableData = (WritableRaster)imageData;
				for( int x = 0; x < writableData.getWidth(); x++ )
				{
					for( int y = 0; y < writableData.getHeight(); y++ )
					{
						try {
							int[] imagePixel = writableData.getPixel( x, y, pixelBuffer );
							java.awt.Color.RGBtoHSB( imagePixel[ 0 ], imagePixel[ 1 ], imagePixel[ 2 ], pixelHSB );
							if( pixelHSB[ 0 ] == chromaHSB[ 0 ] ) {
								imagePixel[ 3 ] = 0;
								writableData.setPixel( x, y, imagePixel );
							}
						} catch( ArrayIndexOutOfBoundsException e ) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		Image returnImage;
		if( this.antAliasFactor != 0 ) {
			int finalWidth = this.width;
			int finalHeight = this.height;
			int currentWidth = rv.getWidth();
			int currentHeight = rv.getHeight();
			if( trimWhitespace ) {
				double actualAspectRatio = ( (double)rv.getWidth() ) / rv.getHeight();
				double targetAspectRatio = ( (double)this.width ) / this.height;
				if( actualAspectRatio > targetAspectRatio ) {
					finalWidth = this.width;
					finalHeight = (int)( this.width / actualAspectRatio );
				}
				else {
					finalHeight = this.height;
					finalWidth = (int)( this.height * actualAspectRatio );
				}
			}
			returnImage = rv.getScaledInstance( finalWidth, finalHeight, Image.SCALE_SMOOTH );
		}
		else {
			returnImage = rv;
		}
		if( returnImage instanceof java.awt.image.BufferedImage )
		{
			return (java.awt.image.BufferedImage)returnImage;
		}
		else if( returnImage != null )
		{
			return ImageUtilities.createBufferedImage( returnImage, BufferedImage.TYPE_INT_ARGB );
		}
		else
		{
			return null;
		}
	}

	protected edu.cmu.cs.dennisc.scenegraph.Transformable getModelTransformable() {
		return this.sgModelTransformable;
	}

	protected edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera getSGCamera() {
		return this.sgCamera;
	}

	protected edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraVehicle() {
		return sgCameraVehicle;
	}

	protected org.lgna.story.implementation.SceneImp getScene() {
		return this.scene;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	protected AffineMatrix4x4 getThumbnailCameraOrientation( Point3 centerPoint, Vector3 cameraDir, double zoom )
	{
		cameraDir.normalize();
		Vector3 negCameraDir = new Vector3( cameraDir );
		negCameraDir.multiply( -1 );
		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray( centerPoint, negCameraDir );
		Point3 cameraLocation = cameraRay.getPointAlong( zoom );
		OrthogonalMatrix3x3 pointAtOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide( cameraDir, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 rv = new AffineMatrix4x4( pointAtOrientation, cameraLocation );
		return rv;
	}

	protected AffineMatrix4x4 getThumbnailCameraOrientation( AxisAlignedBox bbox, Vector3 cameraDir )
	{
		cameraDir.normalize();
		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray( bbox.getCenter(), cameraDir );
		double horizontalAngle = getSGCamera().horizontalViewingAngle.getValue().getAsRadians();
		double verticalAngle = getSGCamera().verticalViewingAngle.getValue().getAsRadians();
		double halfCameraFOV = ( horizontalAngle < verticalAngle ) ? horizontalAngle : verticalAngle;
		halfCameraFOV /= 2.0;

		Hexahedron hex = bbox.getHexahedron();
		double minVal = Double.MAX_VALUE;
		for( Point3 p : hex.getPoints() )
		{
			double t = cameraRay.getProjectedPointT( p );
			Point3 rayPoint = cameraRay.getPointAlong( t );
			double distanceToRay = Point3.calculateDistanceBetween( p, rayPoint );
			double distanceProjectedPointNeedsToBeFromCamera = ( distanceToRay / Math.tan( halfCameraFOV ) );
			double val = t - distanceProjectedPointNeedsToBeFromCamera;
			if( val < minVal )
			{
				minVal = val;
			}
		}

		Point3 cameraLocation = cameraRay.getPointAlong( minVal );
		OrthogonalMatrix3x3 pointAtOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide( cameraDir, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 rv = new AffineMatrix4x4( pointAtOrientation, cameraLocation );
		assert !rv.isNaN() : "Failed to make a useful camera orientation from " + bbox;
		return rv;
	}

	protected AffineMatrix4x4 getThumbnailCameraOrientation( AxisAlignedBox bbox )
	{
		Vector3 cameraDir = new Vector3( -1.0, -2.0, 3.0 );
		return getThumbnailCameraOrientation( bbox, cameraDir );
	}

	protected abstract AffineMatrix4x4 getThumbnailTransform( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox );

	public java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox, boolean trimWhitespace ) {
		return createThumbnail( v, bbox, trimWhitespace, null );
	}

	public synchronized java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox, boolean trimWhitespace, java.awt.Color colorKey ) {
		v.setParent( this.sgModelTransformable );
		AffineMatrix4x4 finalCameraTransform = getThumbnailTransform( v, bbox );
		java.awt.image.BufferedImage returnImage = takePicture( finalCameraTransform, trimWhitespace, colorKey );
		v.setParent( null );
		return returnImage;
	}

	public java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox ) {
		return createThumbnail( v, bbox, true );
	}

	public java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v ) {
		return createThumbnail( v, v.getAxisAlignedMinimumBoundingBox(), true );
	}

	public java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v, boolean trimWhitespace ) {
		return createThumbnail( v, v.getAxisAlignedMinimumBoundingBox(), trimWhitespace );
	}

	protected void setUpCamera( edu.cmu.cs.dennisc.render.OffscreenRenderTarget renderTarget )
	{
		boolean isClearingAndAddingRequired;
		if( renderTarget.getSgCameraCount() == 1 ) {
			if( renderTarget.getSgCameraAt( 0 ) == this.sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			renderTarget.clearSgCameras();
			renderTarget.addSgCamera( this.sgCamera );
		}
	}

}
