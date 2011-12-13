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
package org.alice.apis.moveandturn;

import java.util.LinkedList;
import java.util.List;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.LineArray;
import edu.cmu.cs.dennisc.scenegraph.QuadArray;
import edu.cmu.cs.dennisc.scenegraph.ShadingStyle;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.texture.TextureCoordinate2f;

/**
 * @author David Culyba
 */
public class PerspectiveCameraMarker extends CameraMarker 
{
	private static final double VIEW_LINES_DEFAULT_DISPLACEMENT = 3;
	private static final double VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA = 3;
	private static final float START_ALPHA = .5f;
	private static final float END_ALPHA = 0f;
	private static final double LENGTH = .75;
	private static final double HEIGHT = .4;
	private static final double WIDTH = .15;
	private static final double LENS_HOOD_LENGTH = .2;
	private static final float LINE_RED = 1;
	private static final float LINE_GREEN = 1;
	private static final float LINE_BLUE = 0;
	private static final float LASER_LINE_RED = 1;
	private static final float LASER_LINE_GREEN = 0;
	private static final float LASER_LINE_BLUE = 0;
	
	private double farClippingPlane;
	private edu.cmu.cs.dennisc.math.Angle horizontalViewAngle;
	private edu.cmu.cs.dennisc.math.Angle verticalViewAngle;
	
//	private Vertex[] sgViewLineVertices;
//	private LineArray sgViewLines;
//	private SingleAppearance sgLinesFrontFacingAppearance;
	
	private Vertex[] sgLaserLineVertices;
	private LineArray sgLaserLine;
	private SimpleAppearance sgLaserLinesFrontFacingAppearance;
	
	private List<Visual> sgDetailedComponents;
	
	protected boolean showDetail = false;
	
	public PerspectiveCameraMarker()
	{
		super();
		this.showDetail = false;
	}
	
	@Override
	protected Color4f getDefaultMarkerColor()
	{
		return Color4f.GRAY;
	}
	
	@Override
	protected void createVisuals()
	{
		this.sgDetailedComponents = new LinkedList<Visual>();
		this.farClippingPlane = 100;
		this.horizontalViewAngle = new edu.cmu.cs.dennisc.math.AngleInDegrees(90);
		this.verticalViewAngle = new edu.cmu.cs.dennisc.math.AngleInDegrees(90);

		Visual sgBoxVisual = new Visual();
		sgBoxVisual.setName("Camera Box Visual");
		sgBoxVisual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		edu.cmu.cs.dennisc.scenegraph.Box sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();
		sgBox.setMinimum(new Point3( -WIDTH/2, -HEIGHT/2, 0));
		sgBox.setMaximum(new Point3( WIDTH/2, HEIGHT/2, LENGTH));
		sgBoxVisual.geometries.setValue( new Geometry[] { sgBox } );
		
		final double radius = LENGTH / 4;
		
		Visual sgCylinder1Visual= new Visual();
		sgCylinder1Visual.setName("Camera Cylinder 1 Visual");
		sgCylinder1Visual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		Cylinder sgCylinder1 = new Cylinder();
		sgCylinder1.topRadius.setValue( radius );
		sgCylinder1.bottomRadius.setValue( radius );
		sgCylinder1.length.setValue( WIDTH );
		sgCylinder1.bottomToTopAxis.setValue( BottomToTopAxis.POSITIVE_X );
		sgCylinder1.hasTopCap.setValue( true );
		sgCylinder1.hasBottomCap.setValue( true );
		Vector3 cylinder1Translation = new Vector3(-WIDTH/2, HEIGHT/2 + radius , radius);
		Transformable sgTransformableCylinder1 = new Transformable();
		sgTransformableCylinder1.setName("Camera Cylinder 1");
		sgTransformableCylinder1.applyTranslation( cylinder1Translation );
		sgCylinder1Visual.geometries.setValue( new Geometry[] { sgCylinder1 } );
		sgCylinder1Visual.setParent(sgTransformableCylinder1);
		
		Visual sgCylinder2Visual= new Visual();
		sgCylinder2Visual.setName("Camera Cylinder 2 Visual");
		sgCylinder2Visual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		Cylinder sgCylinder2 = new Cylinder();
		sgCylinder2.topRadius.setValue( radius );
		sgCylinder2.bottomRadius.setValue( radius );
		sgCylinder2.length.setValue( WIDTH );
		sgCylinder2.bottomToTopAxis.setValue( BottomToTopAxis.POSITIVE_X );
		sgCylinder2.hasTopCap.setValue( true );
		sgCylinder2.hasBottomCap.setValue( true );
		Vector3 cylinder2Translation = new Vector3(-WIDTH/2, HEIGHT/2 + radius , radius*3);
		Transformable sgTransformableCylinder2 = new Transformable();
		sgTransformableCylinder2.setName("Camera Cylinder 2");
		sgTransformableCylinder2.applyTranslation( cylinder2Translation );
		sgCylinder2Visual.geometries.setValue( new Geometry[] { sgCylinder2 } );
		sgCylinder2Visual.setParent(sgTransformableCylinder2);
		
		Visual sgLensVisual= new Visual();
		sgLensVisual.setName("Camera Lens Hood Visual");
		sgLensVisual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		QuadArray sgLensGeometry = new QuadArray();
		Vertex[] sgLensVertices = new Vertex[32];
		Point3 innerTopLeft = new Point3(-WIDTH/2, HEIGHT/4, 0);
		Point3 innerTopRight = new Point3(WIDTH/2, HEIGHT/4, 0);
		Point3 innerBottomLeft = new Point3(-WIDTH/2, -HEIGHT/4, 0);
		Point3 innerBottomRight = new Point3(WIDTH/2, -HEIGHT/4, 0);
		Point3 outerTopLeft = new Point3(-HEIGHT/2, HEIGHT/2, -LENS_HOOD_LENGTH);
		Point3 outerTopRight = new Point3(HEIGHT/2, HEIGHT/2, -LENS_HOOD_LENGTH);
		Point3 outerBottomLeft = new Point3(-HEIGHT/2, -HEIGHT/2, -LENS_HOOD_LENGTH);
		Point3 outerBottomRight = new Point3(HEIGHT/2, -HEIGHT/2, -LENS_HOOD_LENGTH);

		Vector3 topNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerTopRight, innerTopLeft), Vector3.createSubtraction(outerTopLeft, innerTopLeft));
		topNormal.normalize();
		Vector3 rightNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerBottomRight, innerTopRight), Vector3.createSubtraction(outerTopRight, innerTopRight));
		rightNormal.normalize();
		Vector3 bottomNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerBottomLeft, innerBottomRight), Vector3.createSubtraction(outerBottomRight, innerBottomRight));
		bottomNormal.normalize();
		Vector3 leftNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerTopLeft, innerBottomLeft), Vector3.createSubtraction(outerBottomLeft, innerBottomLeft));
		leftNormal.normalize();
		
		Vector3f topNormalf = new Vector3f((float)topNormal.x, (float)topNormal.y, (float)topNormal.z);
		Vector3f rightNormalf = new Vector3f((float)rightNormal.x, (float)rightNormal.y, (float)rightNormal.z);
		Vector3f bottomNormalf = new Vector3f((float)bottomNormal.x, (float)bottomNormal.y, (float)bottomNormal.z);
		Vector3f leftNormalf = new Vector3f((float)leftNormal.x, (float)leftNormal.y, (float)leftNormal.z);
		Vector3f negTopNormalf = Vector3f.createMultiplication(topNormalf, -1);
		Vector3f negRightNormalf = Vector3f.createMultiplication(rightNormalf, -1);
		Vector3f negBottomNormalf = Vector3f.createMultiplication(bottomNormalf, -1);
		Vector3f negLeftNormalf = Vector3f.createMultiplication(leftNormalf, -1);
		
		Color4f diffuse = Color4f.createNaN();
		Color4f specular = Color4f.createNaN();
		TextureCoordinate2f uvs = edu.cmu.cs.dennisc.texture.TextureCoordinate2f.createNaN();
		
		sgLensVertices[0] = new Vertex(outerTopLeft, topNormalf, diffuse, specular, uvs);
		sgLensVertices[1] = new Vertex(innerTopLeft, topNormalf, diffuse, specular, uvs);
		sgLensVertices[2] = new Vertex(innerTopRight, topNormalf, diffuse, specular, uvs);
		sgLensVertices[3] = new Vertex(outerTopRight, topNormalf, diffuse, specular, uvs);
		
		sgLensVertices[4] = new Vertex(outerTopRight, rightNormalf, diffuse, specular, uvs);
		sgLensVertices[5] = new Vertex(innerTopRight, rightNormalf, diffuse, specular, uvs);
		sgLensVertices[6] = new Vertex(innerBottomRight, rightNormalf, diffuse, specular, uvs);
		sgLensVertices[7] = new Vertex(outerBottomRight, rightNormalf, diffuse, specular, uvs);
		
		sgLensVertices[8] = new Vertex(outerBottomRight, bottomNormalf, diffuse, specular, uvs);
		sgLensVertices[9] = new Vertex(innerBottomRight, bottomNormalf, diffuse, specular, uvs);
		sgLensVertices[10] = new Vertex(innerBottomLeft, bottomNormalf, diffuse, specular, uvs);
		sgLensVertices[11] = new Vertex(outerBottomLeft, bottomNormalf, diffuse, specular, uvs);
		
		sgLensVertices[12] = new Vertex(outerBottomLeft, leftNormalf, diffuse, specular, uvs);
		sgLensVertices[13] = new Vertex(innerBottomLeft, leftNormalf, diffuse, specular, uvs);
		sgLensVertices[14] = new Vertex(innerTopLeft, leftNormalf, diffuse, specular, uvs);
		sgLensVertices[15] = new Vertex(outerTopLeft, leftNormalf, diffuse, specular, uvs);
		
		//The opposite faces
		sgLensVertices[16] = new Vertex(outerTopRight, negTopNormalf, diffuse, specular, uvs);
		sgLensVertices[17] = new Vertex(innerTopRight, negTopNormalf, diffuse, specular, uvs);
		sgLensVertices[18] = new Vertex(innerTopLeft, negTopNormalf, diffuse, specular, uvs);
		sgLensVertices[19] = new Vertex(outerTopLeft, negTopNormalf, diffuse, specular, uvs);
		
		sgLensVertices[20] = new Vertex(outerBottomRight, negRightNormalf, diffuse, specular, uvs);
		sgLensVertices[21] = new Vertex(innerBottomRight, negRightNormalf, diffuse, specular, uvs);
		sgLensVertices[22] = new Vertex(innerTopRight, negRightNormalf, diffuse, specular, uvs);
		sgLensVertices[23] = new Vertex(outerTopRight, negRightNormalf, diffuse, specular, uvs);
		
		sgLensVertices[24] = new Vertex(outerBottomLeft, negBottomNormalf, diffuse, specular, uvs);
		sgLensVertices[25] = new Vertex(innerBottomLeft, negBottomNormalf, diffuse, specular, uvs);
		sgLensVertices[26] = new Vertex(innerBottomRight, negBottomNormalf, diffuse, specular, uvs);
		sgLensVertices[27] = new Vertex(outerBottomRight, negBottomNormalf, diffuse, specular, uvs);
		
		sgLensVertices[28] = new Vertex(outerTopLeft, negLeftNormalf, diffuse, specular, uvs);
		sgLensVertices[29] = new Vertex(innerTopLeft, negLeftNormalf, diffuse, specular, uvs);
		sgLensVertices[30] = new Vertex(innerBottomLeft, negLeftNormalf, diffuse, specular, uvs);
		sgLensVertices[31] = new Vertex(outerBottomLeft, negLeftNormalf, diffuse, specular, uvs);
		
		sgLensGeometry.vertices.setValue( sgLensVertices );
		sgLensVisual.geometries.setValue( new Geometry[] { sgLensGeometry } );
		
//		this.sgLinesFrontFacingAppearance = new SingleAppearance();
//		this.sgLinesFrontFacingAppearance.diffuseColor.setValue( Color4f.YELLOW );
//		this.sgLinesFrontFacingAppearance.shadingStyle.setValue(ShadingStyle.NONE);
//		this.sgViewLineVertices = new Vertex[8];
//		this.sgViewLineVertices[0] = Vertex.createXYZRGBA(0,0,0,LINE_RED,LINE_GREEN,LINE_BLUE,START_ALPHA);
//		this.sgViewLineVertices[1] = Vertex.createXYZRGBA(-VIEW_LINES_DEFAULT_DISPLACEMENT,VIEW_LINES_DEFAULT_DISPLACEMENT,-VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA, LINE_RED,LINE_GREEN,LINE_BLUE,END_ALPHA);
//		this.sgViewLineVertices[2] = Vertex.createXYZRGBA(0,0,0,LINE_RED,LINE_GREEN,LINE_BLUE,START_ALPHA);
//		this.sgViewLineVertices[3] = Vertex.createXYZRGBA(-VIEW_LINES_DEFAULT_DISPLACEMENT,-VIEW_LINES_DEFAULT_DISPLACEMENT,-VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA, LINE_RED,LINE_GREEN,LINE_BLUE,END_ALPHA);
//		this.sgViewLineVertices[4] = Vertex.createXYZRGBA(0,0,0,LINE_RED,LINE_GREEN,LINE_BLUE,START_ALPHA);
//		this.sgViewLineVertices[5] = Vertex.createXYZRGBA(VIEW_LINES_DEFAULT_DISPLACEMENT,VIEW_LINES_DEFAULT_DISPLACEMENT,-VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA, LINE_RED,LINE_GREEN,LINE_BLUE,END_ALPHA);
//		this.sgViewLineVertices[6] = Vertex.createXYZRGBA(0,0,0,LINE_RED,LINE_GREEN,LINE_BLUE,START_ALPHA);
//		this.sgViewLineVertices[7] = Vertex.createXYZRGBA(VIEW_LINES_DEFAULT_DISPLACEMENT,-VIEW_LINES_DEFAULT_DISPLACEMENT,-VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA, LINE_RED,LINE_GREEN,LINE_BLUE,END_ALPHA);
//		Visual sgViewLinesVisual = new Visual();
//		sgViewLinesVisual.setName("Camera View Lines Visual");
//		sgViewLinesVisual.frontFacingAppearance.setValue( this.sgLinesFrontFacingAppearance );
//		this.sgViewLines = new LineArray();
//		sgViewLines.vertices.setValue(this.sgViewLineVertices);
//		sgViewLinesVisual.geometries.setValue(new Geometry[] { this.sgViewLines } );
//		sgViewLinesVisual.setParent( this.getSGTransformable() );
//		sgDetailedComponents.add(sgViewLinesVisual);
		
		this.sgLaserLinesFrontFacingAppearance = new SimpleAppearance();
		this.sgLaserLinesFrontFacingAppearance.diffuseColor.setValue( Color4f.RED );
		this.sgLaserLinesFrontFacingAppearance.shadingStyle.setValue(ShadingStyle.NONE);
		this.sgLaserLineVertices = new Vertex[2];
		this.sgLaserLineVertices[0] = Vertex.createXYZRGBA(0,0,0,LASER_LINE_RED,LASER_LINE_GREEN,LASER_LINE_BLUE,START_ALPHA);
		this.sgLaserLineVertices[1] = Vertex.createXYZRGBA(0,0,-VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA, LASER_LINE_RED,LASER_LINE_GREEN,LASER_LINE_BLUE,END_ALPHA);
		Visual sgLaserLineVisual = new Visual();
		sgLaserLineVisual.setName("Camera Laser Line Visual");
		sgLaserLineVisual.frontFacingAppearance.setValue( this.sgLaserLinesFrontFacingAppearance );
		this.sgLaserLine = new LineArray();
		sgLaserLine.vertices.setValue(this.sgLaserLineVertices);
		sgLaserLineVisual.geometries.setValue(new Geometry[] { this.sgLaserLine } );
		sgLaserLineVisual.setParent( this.getSGTransformable() );
		sgDetailedComponents.add(sgLaserLineVisual);
		
		setViewingAngle(new AngleInDegrees(90), new AngleInDegrees(45));
		
		sgVisuals.add(sgBoxVisual);
		sgVisuals.add(sgCylinder1Visual);
		sgVisuals.add(sgCylinder2Visual);
		sgVisuals.add(sgLensVisual);
		
		
		
	    sgLensVisual.setParent( this.getSGTransformable() );
		sgTransformableCylinder1.setParent( this.getSGTransformable() );
		sgTransformableCylinder2.setParent( this.getSGTransformable() );
		sgBoxVisual.setParent( this.getSGTransformable() );
		
		setDetailedViewShowing(false);
		updateDetailIsShowing();
		
	}
	
	public void setDetailedViewShowing(boolean isShowing)
	{
		if (this.showDetail != isShowing)
		{
			this.showDetail = isShowing;
			this.updateDetailIsShowing();
		}
		
	}
	
	private void updateDetailIsShowing()
	{
		//System.out.println("Doing detail showing.");
		for (Visual v : this.sgDetailedComponents)
		{
			v.isShowing.setValue(this.showDetail && this.isShowing);
		}
	}
	
	
	@Override
	public void setShowing( Boolean isShowing ) {
		boolean applyValue = isShowing != this.isShowing;
		super.setShowing(isShowing);
		if (applyValue)
		{
			updateDetailIsShowing();
		}
	}
	
	
	public void setFarClippingPlane(double farClippingPlane)
	{
		this.farClippingPlane = farClippingPlane;
		updateViewGeometry();
	}
	
	public void setViewingAngle( edu.cmu.cs.dennisc.math.Angle horizontalViewAngle, edu.cmu.cs.dennisc.math.Angle verticalViewAngle )
	{
		this.horizontalViewAngle.set(horizontalViewAngle);
		this.verticalViewAngle.set(verticalViewAngle);
		updateViewGeometry();
	}
	
	private void updateViewGeometry()
	{
		double halfHorizontalAngle = this.horizontalViewAngle.getAsRadians() * .5;
		double halfVerticalAngle = this.verticalViewAngle.getAsRadians() * .5;
		
		
		
		if (this.sgLaserLineVertices != null && this.sgLaserLine != null)
		{
			this.sgLaserLineVertices[1].position.z = -this.farClippingPlane;
			this.sgLaserLine.vertices.setValue(this.sgLaserLineVertices);
		}
//		if (this.sgViewLineVertices != null && this.sgViewLines != null)
//		{
//			double xVal = Math.tan(halfHorizontalAngle) * this.farClippingPlane;
//			double yVal = Math.tan(halfVerticalAngle) * this.farClippingPlane;
//			this.sgViewLineVertices[1].position.x = -xVal;
//			this.sgViewLineVertices[1].position.y = yVal;
//			this.sgViewLineVertices[1].position.z = -this.farClippingPlane;
//			this.sgViewLineVertices[3].position.x = -xVal;
//			this.sgViewLineVertices[3].position.y = -yVal;
//			this.sgViewLineVertices[3].position.z = -this.farClippingPlane;
//			this.sgViewLineVertices[5].position.x = xVal;
//			this.sgViewLineVertices[5].position.y = yVal;
//			this.sgViewLineVertices[5].position.z = -this.farClippingPlane;
//			this.sgViewLineVertices[7].position.x = xVal;
//			this.sgViewLineVertices[7].position.y = -yVal;
//			this.sgViewLineVertices[7].position.z = -this.farClippingPlane;
//			this.sgViewLines.vertices.setValue(this.sgViewLineVertices);
//		}
	}
	
	
}
