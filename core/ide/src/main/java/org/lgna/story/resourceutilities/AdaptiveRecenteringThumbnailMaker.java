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

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

public class AdaptiveRecenteringThumbnailMaker extends AbstractThumbnailMaker {
	private static final double DEFAULT_SEARCH_FACTOR = .25;
	private static AdaptiveRecenteringThumbnailMaker instance;

	public static AdaptiveRecenteringThumbnailMaker getInstance( int width, int height ) {
		if( ( instance == null ) || ( instance.getWidth() != width ) || ( instance.getHeight() != height ) ) {
			instance = new AdaptiveRecenteringThumbnailMaker( width, height );
		}
		else {
			instance.clear();
		}
		return instance;
	}

	public static AdaptiveRecenteringThumbnailMaker getInstance() {
		if( instance == null ) {
			instance = new AdaptiveRecenteringThumbnailMaker();
		}
		else {
			instance.clear();
		}
		return instance;
	}

	private final double searchFactor;
	private final edu.cmu.cs.dennisc.render.OffscreenRenderTarget testImageOffscreenRenderTarget;

	private AdaptiveRecenteringThumbnailMaker( int width, int height ) {
		super( width, height, AbstractThumbnailMaker.DEFAULT_ANTI_ALIAS_FACTOR );
		this.searchFactor = DEFAULT_SEARCH_FACTOR;
		this.testImageOffscreenRenderTarget = edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().createOffscreenRenderTarget( (int)( this.getWidth() * this.searchFactor ), (int)( this.getHeight() * this.searchFactor ), null, new edu.cmu.cs.dennisc.render.RenderCapabilities.Builder().build() );
		setUpCamera( this.testImageOffscreenRenderTarget );
	}

	private AdaptiveRecenteringThumbnailMaker() {
		this( AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT );
	}

	@Override
	protected void clear() {
		super.clear();
		this.testImageOffscreenRenderTarget.forgetAllCachedItems();
	}

	private Point3 getRecenterPositionBasedOnImage( java.awt.image.BufferedImage testImage, Point3 currentPosition, AxisAlignedBox bbox ) {
		int topBorder = getTopBorder( testImage );
		int bottomBorder = getBottomBorder( testImage );
		int rightBorder = getRightBorder( testImage );
		int leftBorder = getLeftBorder( testImage );

		double shiftUpPercent = ( bottomBorder - topBorder ) / ( (double)testImage.getHeight() );
		double shiftRightPercent = ( leftBorder - rightBorder ) / ( (double)testImage.getHeight() );

		double shiftUpAmount = shiftUpPercent * bbox.getHeight() * .75;
		double shiftRightAmount = shiftRightPercent * bbox.getWidth() * .75;

		Point3 testPosition = new Point3( currentPosition );

		testPosition.y += shiftUpAmount;
		testPosition.x += shiftRightAmount;

		return testPosition;
	}

	@Override
	protected AffineMatrix4x4 getThumbnailTransform( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox ) {
		v.setParent( this.getModelTransformable() );
		getSGCameraVehicle().setLocalTransformation( getThumbnailCameraOrientation( bbox ) );

		AffineMatrix4x4 cameraTransform = getSGCameraVehicle().getAbsoluteTransformation();

		edu.cmu.cs.dennisc.render.OffscreenRenderTarget testImageRT = testImageOffscreenRenderTarget;
		java.awt.image.BufferedImage testImage = testImageRT.getSynchronousImageCapturer().createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer();
		java.nio.FloatBuffer depthBuffer = testImageRT.getSynchronousImageCapturer().createFloatBufferForUseAsDepthBuffer();

		testImageRT.clearAndRenderOffscreen();
		testImage = testImageRT.getSynchronousImageCapturer().getColorBufferWithTransparencyBasedOnDepthBuffer( testImage, depthBuffer );

		writeDebugImageIfAppropriate( "initial.png", testImage );

		Point3 testPosition = getRecenterPositionBasedOnImage( testImage, cameraTransform.translation, bbox );
		getSGCameraVehicle().setTranslationOnly( testPosition, this.getScene().getSgReferenceFrame() );
		Point3 lastGoodPosition = new Point3( testPosition );

		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray( testPosition, Vector3.createMultiplication( cameraTransform.orientation.backward, -1 ) );
		double distanceToCenter = Point3.calculateDistanceBetween( cameraRay.accessOrigin(), bbox.getCenter() );
		double bboxDiagonal = Point3.calculateDistanceBetween( bbox.getMinimum(), bbox.getMaximum() );
		double distanceToEdge = distanceToCenter - bboxDiagonal;
		double distanceStep = distanceToCenter / 20;
		double currentT = 0;
		boolean framed = isFullyFramed( testImage );
		//zoom out until framed
		final int COUNT_LIMIT = 30;
		int limitCount = 0;
		while( !framed && ( limitCount < COUNT_LIMIT ) ) {
			cameraRay.getPointAlong( testPosition, currentT );
			getSGCameraVehicle().setTranslationOnly( testPosition, this.getScene().getSgReferenceFrame() );
			testImageRT.clearAndRenderOffscreen();
			testImage = testImageRT.getSynchronousImageCapturer().getColorBufferWithTransparencyBasedOnDepthBuffer( testImage, depthBuffer );

			writeDebugImageIfAppropriate( "test" + limitCount + ".png", testImage );

			framed = isFullyFramed( testImage );
			if( framed )
			{
				lastGoodPosition.set( testPosition );
			}
			limitCount++;
			currentT -= distanceStep;
		}
		if( limitCount > COUNT_LIMIT ) {
			System.err.println( "hit thumbnail limit count" );
		}
		int firstLimit = limitCount;
		limitCount = 0;
		//zoom in until just framed
		while( ( limitCount < COUNT_LIMIT ) && framed && ( ( distanceToEdge - currentT ) > getSGCamera().nearClippingPlaneDistance.getValue() ) )
		{
			cameraRay.getPointAlong( testPosition, currentT );
			getSGCameraVehicle().setTranslationOnly( testPosition, this.getScene().getSgReferenceFrame() );
			testImageRT.clearAndRenderOffscreen();
			testImage = testImageRT.getSynchronousImageCapturer().getColorBufferWithTransparencyBasedOnDepthBuffer( testImage, depthBuffer );

			writeDebugImageIfAppropriate( "test" + ( firstLimit + limitCount ) + ".png", testImage );

			framed = isFullyFramed( testImage );
			if( framed )
			{
				lastGoodPosition.set( testPosition );
			}
			limitCount++;
			currentT += distanceStep;
		}
		if( limitCount > COUNT_LIMIT ) {
			System.err.println( "hit thumbnail limit count" );
		}
		getSGCameraVehicle().setTranslationOnly( lastGoodPosition, this.getScene().getSgReferenceFrame() );
		AffineMatrix4x4 finalCameraTransform = getSGCameraVehicle().getLocalTransformation();
		return finalCameraTransform;
	}

}
