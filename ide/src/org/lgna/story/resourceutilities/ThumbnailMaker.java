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

package org.lgna.story.resourceutilities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Hexahedron;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

public class ThumbnailMaker {
	private static final int THUMBNAIL_WIDTH = 160;
	private static final int THUMBNAIL_HEIGHT = THUMBNAIL_WIDTH * 3 / 4;
	private static final int ANTI_ALIAS_FACTOR = 4;
	private static final double SEARCH_FACTOR = .25;
	
	private int width;
	private int height;
	private final edu.cmu.cs.dennisc.scenegraph.util.World world = new edu.cmu.cs.dennisc.scenegraph.util.World();
	private final edu.cmu.cs.dennisc.scenegraph.Transformable sgModelTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass offscreenLookingGlass;
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass testImageOffscreenLookingGlass;
	
	private static ThumbnailMaker instance;
	
	public static ThumbnailMaker getInstance() {
		if (instance == null) {
			instance = new ThumbnailMaker();
		}
		return instance;
	}
	
	private ThumbnailMaker()
	{
		world.addComponent(this.sgModelTransformable);
	}
	
	private void setUpCamera(edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass lookingGlass)
	{
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = world.getSGCamera();
		boolean isClearingAndAddingRequired;
		if( lookingGlass.getCameraCount() == 1 ) {
			if( lookingGlass.getCameraAt( 0 ) == sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			lookingGlass.clearCameras();
			lookingGlass.addCamera( sgCamera );
		}
	}
	
	private void initializeIfNecessary(int width, int height)
	{
		boolean forceNew = this.width != width || this.height != height;
		this.width = width;
		this.height = height;
		if( offscreenLookingGlass == null || forceNew) {
			offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createOffscreenLookingGlass( null );
			offscreenLookingGlass.setSize( this.width*ANTI_ALIAS_FACTOR, this.height*ANTI_ALIAS_FACTOR );
		}
		if ( testImageOffscreenLookingGlass == null || forceNew)
		{
			testImageOffscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createOffscreenLookingGlass( null );
			testImageOffscreenLookingGlass.setSize( (int)(this.width*SEARCH_FACTOR), (int)(this.height*SEARCH_FACTOR) );
		}
		setUpCamera(offscreenLookingGlass);
		setUpCamera(testImageOffscreenLookingGlass);
		
	}
	
	private AffineMatrix4x4 getThumbnailCameraOrientation(AxisAlignedBox bbox)
	{
		Vector3 cameraDir = new Vector3(1.0, -2.0, -3.0);
		cameraDir.normalize();
		
		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray(bbox.getCenter(), cameraDir);
		double horizontalAngle = world.getSGCamera().horizontalViewingAngle.getValue().getAsRadians();
		double verticalAngle = world.getSGCamera().verticalViewingAngle.getValue().getAsRadians();
		double halfCameraFOV = (horizontalAngle < verticalAngle) ? horizontalAngle : verticalAngle;
		halfCameraFOV /= 2.0;
		
		Hexahedron hex = bbox.getHexahedron();
		double minVal = Double.MAX_VALUE;
		for (Point3 p : hex.getPoints())
		{
			double t = cameraRay.getProjectedPointT(p);
			Point3 rayPoint = cameraRay.getPointAlong(t);
			double distanceToRay = Point3.calculateDistanceBetween(p, rayPoint);
			double distanceProjectedPointNeedsToBeFromCamera = (distanceToRay / Math.tan(halfCameraFOV));
			double val = t - distanceProjectedPointNeedsToBeFromCamera;
			if (val < minVal)
			{
				minVal = val;
			}
		}
		
		Point3 cameraLocation = cameraRay.getPointAlong(minVal);
		OrthogonalMatrix3x3 pointAtOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(cameraDir, Vector3.accessPositiveYAxis());
		AffineMatrix4x4 rv = new AffineMatrix4x4(pointAtOrientation, cameraLocation);
		return rv;
	}
	
	private void setSize(int width, int height)
	{
		Dimension d = offscreenLookingGlass.getSize();
		if (d.width != width || d.height != height)
		{
			offscreenLookingGlass.setSize(width, height);
		}
	}
	
	private boolean isTransparent(int pixel)
	{
		int alpha = pixel >> 24;
		return alpha == 0;
	}
	
	private int getLeftBorder(java.awt.image.BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		for (int x=0; x<width; x++)
		{
			for (int y=0; y<height; y++)
			{
				if (!isTransparent(image.getRGB(x, y)))
				{
					return x;
				}
			}
		}
		return width;
	}
	
	private int getRightBorder(java.awt.image.BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		for (int x=width-1; x>=0; x--)
		{
			for (int y=0; y<height; y++)
			{
				if (!isTransparent(image.getRGB(x, y)))
				{
					return width - x;
				}
			}
		}
		return width;
	}
	
	private int getTopBorder(java.awt.image.BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y=0; y<height; y++)
		{
			for (int x=0; x<width; x++)
			{
				if (!isTransparent(image.getRGB(x, y)))
				{
					return y;
				}
			}
		}
		return height;
	}
	
	private int getBottomBorder(java.awt.image.BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		for (int y=height-1; y>=0; y--)
		{
			for (int x=0; x<width; x++)
			{
				if (!isTransparent(image.getRGB(x, y)))
				{
					return height - y;
				}
			}
		}
		return height;
	}
	
	private boolean isFullyFramed(java.awt.image.BufferedImage image)
	{
		int width = image.getWidth();
		int right = width-1;
		int height = image.getHeight();
		int bottom = height - 1;
		for (int x=0; x<width; x++)
		{
			int topPixel = image.getRGB(x, 0);
			if (!isTransparent(topPixel))
			{
				return false;
			}
			int bottomPixel = image.getRGB(x, bottom);
			if (!isTransparent(bottomPixel))
			{
				return false;
			}
		}
		for (int y=0; y<height; y++)
		{
			int leftPixel = image.getRGB(0, y);
			if (!isTransparent(leftPixel))
			{
				return false;
			}
			int rightPixel = image.getRGB(right, y);
			if (!isTransparent(rightPixel))
			{
				return false;
			}
		}
		return true;
	}
	
	private Point3 getRecenterPositionBasedOnImage(java.awt.image.BufferedImage testImage, Point3 currentPosition, AxisAlignedBox bbox)
	{
		int topBorder = getTopBorder(testImage);
		int bottomBorder = getBottomBorder(testImage);
		int rightBorder = getRightBorder(testImage);
		int leftBorder = getLeftBorder(testImage);
		
		double shiftUpPercent = (bottomBorder - topBorder) / ((double)testImage.getHeight());
		double shiftRightPercent = (leftBorder - rightBorder) / ((double)testImage.getHeight());
		
		double shiftUpAmount = shiftUpPercent * bbox.getHeight();
		double shiftRightAmount = shiftRightPercent * bbox.getWidth()* .5;
		
		Point3 testPosition = new Point3(currentPosition);
		
		testPosition.y += shiftUpAmount;
		testPosition.x += shiftRightAmount;
		
		return testPosition;
	}
	
	public java.awt.image.BufferedImage createThumbnail(edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox, int width, int height) throws Exception {
		initializeIfNecessary(width, height);
		
		v.setParent(this.sgModelTransformable);
		world.getSGCameraVehicle().setLocalTransformation(getThumbnailCameraOrientation(bbox));
		
		AffineMatrix4x4 cameraTransform = world.getSGCameraVehicle().getAbsoluteTransformation();
		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray(cameraTransform.translation, Vector3.createMultiplication(cameraTransform.orientation.backward, -1));
		java.awt.image.BufferedImage testImage = testImageOffscreenLookingGlass.createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer();
		java.nio.FloatBuffer depthBuffer = testImageOffscreenLookingGlass.createFloatBufferForUseAsDepthBuffer();
		
		testImageOffscreenLookingGlass.clearAndRenderOffscreen();
		testImage = testImageOffscreenLookingGlass.getColorBufferWithTransparencyBasedOnDepthBuffer(testImage, depthBuffer);
		
		Point3 testPosition = getRecenterPositionBasedOnImage(testImage, cameraTransform.translation, bbox);
		world.getSGCameraVehicle().setTranslationOnly(testPosition, world);
		boolean framed = true;
		Point3 lastGoodPosition = new Point3(testPosition);
		double distanceToCenter = Point3.calculateDistanceBetween(cameraRay.accessOrigin(), bbox.getCenter());
		double distanceStep = distanceToCenter / 20;
		double currentT = 0;
		while (framed && distanceStep < distanceToCenter)
		{
			cameraRay.getPointAlong(testPosition, currentT);
			world.getSGCameraVehicle().setTranslationOnly(testPosition, world);
			testImageOffscreenLookingGlass.clearAndRenderOffscreen();
			testImage = testImageOffscreenLookingGlass.getColorBufferWithTransparencyBasedOnDepthBuffer(testImage, depthBuffer);
			framed = isFullyFramed(testImage);
			if (framed)
			{
				lastGoodPosition.set(testPosition);
			}
			currentT += distanceStep;
		}
		
		world.getSGCameraVehicle().setTranslationOnly(lastGoodPosition, world);
		offscreenLookingGlass.clearAndRenderOffscreen();
		java.awt.image.BufferedImage rv = offscreenLookingGlass.getColorBufferWithTransparencyBasedOnDepthBuffer();
		
		v.setParent(null);
		
		Image returnImage = rv.getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);
		if (returnImage instanceof java.awt.image.BufferedImage)
		{
			return (java.awt.image.BufferedImage)returnImage;
		}
		else if (returnImage != null)
		{
			return ImageUtilities.constructBufferedImage(returnImage, BufferedImage.TYPE_INT_ARGB);
		}
		else
		{
			return null;
		}
	}
	
	public java.awt.image.BufferedImage createThumbnail(edu.cmu.cs.dennisc.scenegraph.Visual v) throws Exception {
		return createThumbnail(v, v.getAxisAlignedMinimumBoundingBox(), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}
	
	public java.awt.image.BufferedImage createThumbnail(edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox) throws Exception {
		return createThumbnail(v, bbox, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}
}
