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
	
	private final edu.cmu.cs.dennisc.scenegraph.util.World world = new edu.cmu.cs.dennisc.scenegraph.util.World();
	private final edu.cmu.cs.dennisc.scenegraph.Transformable sgModelTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass offscreenLookingGlass;
	
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
		
		Point3 cameraLocation = cameraRay.getPointAlong(minVal*.8);
		OrthogonalMatrix3x3 pointAtOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(cameraDir, Vector3.accessPositiveYAxis());
		AffineMatrix4x4 rv = new AffineMatrix4x4(pointAtOrientation, cameraLocation);
		return rv;
	}
	
	public java.awt.image.BufferedImage createThumbnail(edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox, int width, int height) throws Exception {
		v.setParent(this.sgModelTransformable);
		
		world.getSGCameraVehicle().setLocalTransformation(getThumbnailCameraOrientation(bbox));
		
		int scaledWidth = ANTI_ALIAS_FACTOR * width;
		int scaledHeight = ANTI_ALIAS_FACTOR * height;
		
		if( offscreenLookingGlass != null ) {
			Dimension d = offscreenLookingGlass.getSize();
			if (d.width != scaledWidth || d.height != scaledHeight)
			{
				offscreenLookingGlass.setSize(scaledWidth, scaledHeight);
			}
		} else {
			offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createOffscreenLookingGlass( null );
			offscreenLookingGlass.setSize( scaledWidth, scaledHeight );
		}
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = world.getSGCamera();
		boolean isClearingAndAddingRequired;
		if( offscreenLookingGlass.getCameraCount() == 1 ) {
			if( offscreenLookingGlass.getCameraAt( 0 ) == sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			offscreenLookingGlass.clearCameras();
			offscreenLookingGlass.addCamera( sgCamera );
		}
		offscreenLookingGlass.clearAndRenderOffscreen();
		java.awt.image.BufferedImage rv = offscreenLookingGlass.getColorBufferWithTransparencyBasedOnDepthBuffer();
		v.setParent(null);
		
		Image returnImage = rv.getScaledInstance(width, height, Image.SCALE_SMOOTH);
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
