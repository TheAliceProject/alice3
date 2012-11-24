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

/**
 * @author Dave Culyba
 */
public abstract class AbstractThumbnailMaker {
	public static final int DEFAULT_THUMBNAIL_WIDTH = 160;
	public static final int DEFAULT_THUMBNAIL_HEIGHT = ( DEFAULT_THUMBNAIL_WIDTH * 3 ) / 4;
	protected static final int DEFAULT_ANTI_ALIAS_FACTOR = 4;

	private final int width;
	private final int height;
	private final int antAliasFactor;

	private final org.lgna.story.implementation.SceneImp scene;
	private final edu.cmu.cs.dennisc.scenegraph.Transformable sgModelTransformable;
	private final Transformable sgCameraVehicle;
	private final SymmetricPerspectiveCamera sgCamera;
	private final edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass offscreenLookingGlass;

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
		this.offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createOffscreenLookingGlass( this.width * this.antAliasFactor, this.height * this.antAliasFactor, null );
		setUpCamera( this.offscreenLookingGlass );
	}

	protected void clear()
	{
		this.offscreenLookingGlass.forgetAllCachedItems();
		this.offscreenLookingGlass.clearUnusedTextures();
	}

	protected synchronized java.awt.image.BufferedImage takePicture( AffineMatrix4x4 cameraTransform ) throws Exception {
		getSGCameraVehicle().setLocalTransformation( cameraTransform );
		offscreenLookingGlass.clearAndRenderOffscreen();
		java.awt.image.BufferedImage rv = offscreenLookingGlass.getColorBufferWithTransparencyBasedOnDepthBuffer();
		Image returnImage;
		if( this.antAliasFactor != 0 ) {
			returnImage = rv.getScaledInstance( this.width, this.height, Image.SCALE_SMOOTH );
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

	protected int getWidth() {
		return this.width;
	}

	protected int getHeight() {
		return this.height;
	}

	protected AffineMatrix4x4 getThumbnailCameraOrientation( AxisAlignedBox bbox )
	{
		Vector3 cameraDir = new Vector3( -1.0, -2.0, 3.0 );
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

	protected abstract AffineMatrix4x4 getThumbnailTransform( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox );

	public synchronized java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v, AxisAlignedBox bbox ) throws Exception {
		v.setParent( this.sgModelTransformable );
		AffineMatrix4x4 finalCameraTransform = getThumbnailTransform( v, bbox );
		java.awt.image.BufferedImage returnImage = takePicture( finalCameraTransform );
		v.setParent( null );
		return returnImage;
	}

	public java.awt.image.BufferedImage createThumbnail( edu.cmu.cs.dennisc.scenegraph.Visual v ) throws Exception {
		return createThumbnail( v, v.getAxisAlignedMinimumBoundingBox() );
	}

	protected void setUpCamera( edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass lookingGlass )
	{
		boolean isClearingAndAddingRequired;
		if( lookingGlass.getCameraCount() == 1 ) {
			if( lookingGlass.getCameraAt( 0 ) == this.sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			lookingGlass.clearCameras();
			lookingGlass.addCamera( this.sgCamera );
		}
	}

}
