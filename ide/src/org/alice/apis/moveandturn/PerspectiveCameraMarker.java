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

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.LineArray;
import edu.cmu.cs.dennisc.scenegraph.QuadArray;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.texture.TextureCoordinate2f;

/**
 * @author David Culyba
 */
public class PerspectiveCameraMarker extends CameraMarker 
{
	
	private Vertex[] sgViewLineVertices;
	
	@Override
	protected Color4f getMarkerColor()
	{
		return Color4f.GRAY;
	}
	
	@Override
	protected void createVisuals()
	{
		super.createVisuals();
		
		final double length = .75;
		final double height = .4;
		final double width = .15;
		final double lensHoodLength = .2;
		
		Visual sgBoxVisual = new Visual();
		sgBoxVisual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		edu.cmu.cs.dennisc.scenegraph.Box sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();
		sgBox.setMinimum(new Point3( -width/2, -height/2, 0));
		sgBox.setMaximum(new Point3( width/2, height/2, length));
		sgBoxVisual.geometries.setValue( new Geometry[] { sgBox } );
		
		final double radius = length / 4;
		
		Visual sgCylinder1Visual= new Visual();
		sgCylinder1Visual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		Cylinder sgCylinder1 = new Cylinder();
		sgCylinder1.topRadius.setValue( radius );
		sgCylinder1.bottomRadius.setValue( radius );
		sgCylinder1.length.setValue( width );
		sgCylinder1.bottomToTopAxis.setValue( BottomToTopAxis.POSITIVE_X );
		sgCylinder1.hasTopCap.setValue( true );
		sgCylinder1.hasBottomCap.setValue( true );
		Vector3 cylinder1Translation = new Vector3(-width/2, height/2 + radius , radius);
		Transformable sgTransformableCylinder1 = new Transformable();
		sgTransformableCylinder1.applyTranslation( cylinder1Translation );
		sgCylinder1Visual.geometries.setValue( new Geometry[] { sgCylinder1 } );
		sgCylinder1Visual.setParent(sgTransformableCylinder1);
		
		Visual sgCylinder2Visual= new Visual();
		sgCylinder2Visual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		Cylinder sgCylinder2 = new Cylinder();
		sgCylinder2.topRadius.setValue( radius );
		sgCylinder2.bottomRadius.setValue( radius );
		sgCylinder2.length.setValue( width );
		sgCylinder2.bottomToTopAxis.setValue( BottomToTopAxis.POSITIVE_X );
		sgCylinder2.hasTopCap.setValue( true );
		sgCylinder2.hasBottomCap.setValue( true );
		Vector3 cylinder2Translation = new Vector3(-width/2, height/2 + radius , radius*3);
		Transformable sgTransformableCylinder2 = new Transformable();
		sgTransformableCylinder2.applyTranslation( cylinder2Translation );
		sgCylinder2Visual.geometries.setValue( new Geometry[] { sgCylinder2 } );
		sgCylinder2Visual.setParent(sgTransformableCylinder2);
		
		Visual sgLensVisual= new Visual();
		sgLensVisual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		QuadArray sgLensGeometry = new QuadArray();
		Vertex[] sgLensVertices = new Vertex[32];
		Point3 innerTopLeft = new Point3(-width/2, height/4, 0);
		Point3 innerTopRight = new Point3(width/2, height/4, 0);
		Point3 innerBottomLeft = new Point3(-width/2, -height/4, 0);
		Point3 innerBottomRight = new Point3(width/2, -height/4, 0);
		Point3 outerTopLeft = new Point3(-height/2, height/2, -lensHoodLength);
		Point3 outerTopRight = new Point3(height/2, height/2, -lensHoodLength);
		Point3 outerBottomLeft = new Point3(-height/2, -height/2, -lensHoodLength);
		Point3 outerBottomRight = new Point3(height/2, -height/2, -lensHoodLength);

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
		
		double viewDisplacement = 3;
		double viewLength = 3;
		float startAlpha = .5f;
		float endAlpha = 0f;
		this.sgViewLineVertices = new Vertex[8];
		this.sgViewLineVertices[0] = Vertex.createXYZRGBA(0,0,0,0,0,0,startAlpha);
		this.sgViewLineVertices[1] = Vertex.createXYZRGBA(-viewDisplacement,viewDisplacement,-viewLength, 0,0,0,endAlpha);
		this.sgViewLineVertices[2] = Vertex.createXYZRGBA(0,0,0,0,0,0,startAlpha);
		this.sgViewLineVertices[3] = Vertex.createXYZRGBA(-viewDisplacement,-viewDisplacement,-viewLength, 0,0,0,endAlpha);
		this.sgViewLineVertices[4] = Vertex.createXYZRGBA(0,0,0,0,0,0,startAlpha);
		this.sgViewLineVertices[5] = Vertex.createXYZRGBA(viewDisplacement,viewDisplacement,-viewLength, 0,0,0,endAlpha);
		this.sgViewLineVertices[6] = Vertex.createXYZRGBA(0,0,0,0,0,0,startAlpha);
		this.sgViewLineVertices[7] = Vertex.createXYZRGBA(viewDisplacement,-viewDisplacement,-viewLength, 0,0,0,endAlpha);
		
		
		Visual sgViewLinesVisual = new Visual();
		sgViewLinesVisual.frontFacingAppearance.setValue( this.sgFrontFacingAppearance );
		LineArray sgViewLines = new LineArray();
		sgViewLines.vertices.setValue(this.sgViewLineVertices);
		sgViewLinesVisual.geometries.setValue(new Geometry[] { sgViewLines } );
		
		sgViewLinesVisual.setParent(this.sgVisualTransformable);
	    sgLensVisual.setParent( this.sgVisualTransformable );
		sgTransformableCylinder1.setParent( this.sgVisualTransformable );
		sgTransformableCylinder2.setParent( this.sgVisualTransformable );
		sgBoxVisual.setParent( this.sgVisualTransformable );
	}
	
	public void setViewAngle( Angle horizontalViewAngle, Angle verticalViewAngle )
	{
//		this.
	}
}
