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

import java.awt.Image;
import java.awt.image.BufferedImage;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Hexahedron;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class ThumbnailMaker {
	private static final int THUMBNAIL_WIDTH = 160;
	private static final int THUMBNAIL_HEIGHT = THUMBNAIL_WIDTH * 3 / 4;
	private static final int ANTI_ALIAS_FACTOR = 4;
	private static final double SEARCH_FACTOR = .25;
	
	private int width;
	private int height;
//	private final edu.cmu.cs.dennisc.scenegraph.util.World world = new edu.cmu.cs.dennisc.scenegraph.util.World();
	private final org.lgna.story.implementation.SceneImp world = new org.lgna.story.implementation.SceneImp(null);
	private final edu.cmu.cs.dennisc.scenegraph.Transformable sgModelTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
	private Transformable m_sgCameraVehicle = new Transformable();
	private SymmetricPerspectiveCamera m_sgCamera = new SymmetricPerspectiveCamera();
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass offscreenLookingGlass;
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass testImageOffscreenLookingGlass;
	
	private static ThumbnailMaker instance;
	
	public static ThumbnailMaker getInstance() {
		if (instance == null) {
			instance = new ThumbnailMaker();
		}
		return instance;
	}
	
	public static ThumbnailMaker getNewInstance() {
		if (instance != null) {
			instance.release();
			instance = null;
			try { 
				System.gc();
				Thread.sleep(100); 
			} catch (Exception e){}
		}
		instance = new ThumbnailMaker();
		return instance;
	}
	
	private ThumbnailMaker()
	{
		world.getSgComposite().addComponent(this.sgModelTransformable);
		m_sgCameraVehicle.setParent( world.getSgComposite() );
		m_sgCameraVehicle.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( 0, 0, 32 ) );
		m_sgCamera.farClippingPlaneDistance.setValue( 1000.0 );
		m_sgCamera.nearClippingPlaneDistance.setValue( .1 );
		m_sgCamera.setParent( m_sgCameraVehicle );
	}
	
	private void release()
	{
		if (this.offscreenLookingGlass != null) {
			this.offscreenLookingGlass.forgetAllCachedItems();
			this.offscreenLookingGlass.release();
			this.offscreenLookingGlass = null;
		}
		
		if (this.testImageOffscreenLookingGlass != null) {
			this.testImageOffscreenLookingGlass.forgetAllCachedItems();
			this.testImageOffscreenLookingGlass.release();
			this.testImageOffscreenLookingGlass = null;
		}
		if (this.world != null) {
			this.world.getSgComposite().release();
		}
	}
	
	private void setUpCamera(edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass lookingGlass)
	{
		boolean isClearingAndAddingRequired;
		if( lookingGlass.getCameraCount() == 1 ) {
			if( lookingGlass.getCameraAt( 0 ) == this.m_sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			lookingGlass.clearCameras();
			lookingGlass.addCamera( m_sgCamera );
		}
	}
	
	private void setSize(int width, int height)
	{
		boolean forceNew = this.width != width || this.height != height;
		this.width = width;
		this.height = height;
		if( offscreenLookingGlass == null || forceNew) {
			offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createOffscreenLookingGlass( null );
			offscreenLookingGlass.setSize( this.width, this.height );
		}
		setUpCamera(offscreenLookingGlass);
	}
	
	private void initializeIfNecessary(int width, int height)
	{
		boolean forceNew = this.width != width || this.height != height;
		this.width = width;
		this.height = height;
		if( offscreenLookingGlass == null || forceNew) {
			if (this.offscreenLookingGlass != null) {
				this.offscreenLookingGlass.release();
				this.offscreenLookingGlass = null;
			}
			offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createOffscreenLookingGlass( null );
			offscreenLookingGlass.setSize( this.width*ANTI_ALIAS_FACTOR, this.height*ANTI_ALIAS_FACTOR );
		}
		if ( testImageOffscreenLookingGlass == null || forceNew)
		{
			if (this.testImageOffscreenLookingGlass != null) {
				this.testImageOffscreenLookingGlass.release();
				this.testImageOffscreenLookingGlass = null;
			}
			testImageOffscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createOffscreenLookingGlass( null );
			testImageOffscreenLookingGlass.setSize( (int)(this.width*SEARCH_FACTOR), (int)(this.height*SEARCH_FACTOR) );
		}
		if (forceNew) {
			try { 
				System.gc();
				Thread.sleep(100); 
			} catch (Exception e){}
		}
		setUpCamera(offscreenLookingGlass);
		setUpCamera(testImageOffscreenLookingGlass);
		
	}
	
	private edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera getSGCamera() {
		return this.m_sgCamera;
	}
	
	private edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraVehicle() {
		return m_sgCameraVehicle;
	}
	
	private AffineMatrix4x4 getThumbnailCameraOrientation(AxisAlignedBox bbox)
	{
		Vector3 cameraDir = new Vector3(-1.0, -2.0, 3.0);
		cameraDir.normalize();
		
		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray(bbox.getCenter(), cameraDir);
		double horizontalAngle = getSGCamera().horizontalViewingAngle.getValue().getAsRadians();
		double verticalAngle = getSGCamera().verticalViewingAngle.getValue().getAsRadians();
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
		
		double shiftUpAmount = shiftUpPercent * bbox.getHeight() * .75;
		double shiftRightAmount = shiftRightPercent * bbox.getWidth()* .75;
		
		Point3 testPosition = new Point3(currentPosition);
		
		testPosition.y += shiftUpAmount;
		testPosition.x += shiftRightAmount;
		
		return testPosition;
	}
	
	public java.awt.image.BufferedImage createThumbnail(edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox, int inputWidth, int inputHeight) throws Exception {
		initializeIfNecessary(inputWidth, inputHeight);
//		this.setSize((int)(inputWidth*SEARCH_FACTOR), (int)(inputHeight*SEARCH_FACTOR));
		
		edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass testImageLG = testImageOffscreenLookingGlass;
		
		v.setParent(this.sgModelTransformable);
		getSGCameraVehicle().setLocalTransformation(getThumbnailCameraOrientation(bbox));
		
		AffineMatrix4x4 cameraTransform = getSGCameraVehicle().getAbsoluteTransformation();
		java.awt.image.BufferedImage testImage = testImageLG.createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer();
		java.nio.FloatBuffer depthBuffer = testImageLG.createFloatBufferForUseAsDepthBuffer();
		
		testImageLG.clearAndRenderOffscreen();
		testImage = testImageLG.getColorBufferWithTransparencyBasedOnDepthBuffer(testImage, depthBuffer);
		
		ImageUtilities.write("C:/batchOutput/thumbnailTest/initial.png", testImage);
		
		Point3 testPosition = getRecenterPositionBasedOnImage(testImage, cameraTransform.translation, bbox);
		getSGCameraVehicle().setTranslationOnly(testPosition, world.getSgReferenceFrame());
		Point3 lastGoodPosition = new Point3(testPosition);
		edu.cmu.cs.dennisc.math.Ray cameraRay = new edu.cmu.cs.dennisc.math.Ray(testPosition, Vector3.createMultiplication(cameraTransform.orientation.backward, -1));
		double distanceToCenter = Point3.calculateDistanceBetween(cameraRay.accessOrigin(), bbox.getCenter());
		double bboxDiagonal = Point3.calculateDistanceBetween(bbox.getMinimum(), bbox.getMaximum());
		double distanceToEdge = distanceToCenter - bboxDiagonal;
		double distanceStep = distanceToCenter / 20;
		double currentT = 0;
		int count = 0;
		boolean framed = isFullyFramed(testImage);
		//zoom out until framed
		while (!framed) {
			cameraRay.getPointAlong(testPosition, currentT);
			getSGCameraVehicle().setTranslationOnly(testPosition, world.getSgReferenceFrame());
			testImageLG.clearAndRenderOffscreen();
			testImage = testImageLG.getColorBufferWithTransparencyBasedOnDepthBuffer(testImage, depthBuffer);
			
			ImageUtilities.write("C:/batchOutput/thumbnailTest/test"+count+".png", testImage);
			
			framed = isFullyFramed(testImage);
			if (framed)
			{
				lastGoodPosition.set(testPosition);
			}
			count++;
			currentT -= distanceStep;
		}
		//zoom in until just framed
		while (framed && ((distanceToEdge - currentT) > getSGCamera().nearClippingPlaneDistance.getValue()))
		{
			cameraRay.getPointAlong(testPosition, currentT);
			getSGCameraVehicle().setTranslationOnly(testPosition, world.getSgReferenceFrame());
			testImageLG.clearAndRenderOffscreen();
			testImage = testImageLG.getColorBufferWithTransparencyBasedOnDepthBuffer(testImage, depthBuffer);
			
			ImageUtilities.write("C:/batchOutput/thumbnailTest/test"+count+".png", testImage);
			
			framed = isFullyFramed(testImage);
			if (framed)
			{
				lastGoodPosition.set(testPosition);
			}
			count++;
			currentT += distanceStep;
		}
		System.out.println(v.getName()+": framed: "+framed+", distance to edge: "+distanceToEdge+", t: "+currentT+", near clip: "+getSGCamera().nearClippingPlaneDistance.getValue());
		getSGCameraVehicle().setTranslationOnly(lastGoodPosition, world.getSgReferenceFrame());
		
		this.setSize( inputWidth*ANTI_ALIAS_FACTOR, inputHeight*ANTI_ALIAS_FACTOR );
		
		offscreenLookingGlass.clearAndRenderOffscreen();
		java.awt.image.BufferedImage rv = offscreenLookingGlass.getColorBufferWithTransparencyBasedOnDepthBuffer();
		
		ImageUtilities.write("C:/batchOutput/thumbnailTest/final.png", rv);
		
		v.setParent(null);
		
		Image returnImage = rv.getScaledInstance(inputWidth, inputHeight, Image.SCALE_SMOOTH);
		if (returnImage instanceof java.awt.image.BufferedImage)
		{
			return (java.awt.image.BufferedImage)returnImage;
		}
		else if (returnImage != null)
		{
			return ImageUtilities.createBufferedImage(returnImage, BufferedImage.TYPE_INT_ARGB);
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
