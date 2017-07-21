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
package edu.cmu.cs.dennisc.scenegraph.util;

/**
 * @author Dennis Cosgrove
 */
public class Limb extends edu.cmu.cs.dennisc.scenegraph.util.ModelTransformable {
	private edu.cmu.cs.dennisc.scenegraph.util.ModelTransformable m_sgB = new edu.cmu.cs.dennisc.scenegraph.util.ModelTransformable();
	private edu.cmu.cs.dennisc.scenegraph.util.ModelTransformable m_sgC = new edu.cmu.cs.dennisc.scenegraph.util.ModelTransformable();

	public Limb( double abLength, double bcLength, double ctLength ) {
		double sphereRadius = Math.min( abLength, Math.min( bcLength, ctLength ) ) * 0.25;
		double cylinderRadius = sphereRadius * 0.5;

		m_sgB.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( 0, 0, abLength ) );
		m_sgC.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( 0, 0, bcLength ) );

		edu.cmu.cs.dennisc.scenegraph.Sphere sgSphere = new edu.cmu.cs.dennisc.scenegraph.Sphere();
		sgSphere.radius.setValue( sphereRadius );

		this.getSGVisual().geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgSphere } );
		m_sgB.getSGVisual().geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgSphere } );
		m_sgC.getSGVisual().geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgSphere } );

		this.getSGVisual().frontFacingAppearance.getValue().setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.RED );
		m_sgB.getSGVisual().frontFacingAppearance.getValue().setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.GREEN );
		m_sgC.getSGVisual().frontFacingAppearance.getValue().setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.BLUE );

		edu.cmu.cs.dennisc.scenegraph.util.ModelVisual sgVisualAB = new edu.cmu.cs.dennisc.scenegraph.util.ModelVisual();
		edu.cmu.cs.dennisc.scenegraph.util.ModelVisual sgVisualBC = new edu.cmu.cs.dennisc.scenegraph.util.ModelVisual();
		edu.cmu.cs.dennisc.scenegraph.util.ModelVisual sgVisualCT = new edu.cmu.cs.dennisc.scenegraph.util.ModelVisual();

		edu.cmu.cs.dennisc.scenegraph.Cylinder sgCylinderAB = new edu.cmu.cs.dennisc.scenegraph.Cylinder();
		edu.cmu.cs.dennisc.scenegraph.Cylinder sgCylinderBC = new edu.cmu.cs.dennisc.scenegraph.Cylinder();
		edu.cmu.cs.dennisc.scenegraph.Cylinder sgCylinderCT = new edu.cmu.cs.dennisc.scenegraph.Cylinder();

		sgCylinderAB.topRadius.setValue( cylinderRadius );
		sgCylinderAB.bottomRadius.setValue( cylinderRadius );
		sgCylinderAB.originAlignment.setValue( edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.BOTTOM );
		sgCylinderAB.bottomToTopAxis.setValue( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z );
		sgCylinderAB.length.setValue( abLength );

		sgCylinderBC.topRadius.setValue( cylinderRadius );
		sgCylinderBC.bottomRadius.setValue( cylinderRadius );
		sgCylinderBC.originAlignment.setValue( edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.BOTTOM );
		sgCylinderBC.bottomToTopAxis.setValue( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z );
		sgCylinderBC.length.setValue( bcLength );

		sgCylinderCT.topRadius.setValue( 0.0 );
		sgCylinderCT.bottomRadius.setValue( cylinderRadius );
		sgCylinderCT.originAlignment.setValue( edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.BOTTOM );
		sgCylinderCT.bottomToTopAxis.setValue( edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z );
		sgCylinderCT.length.setValue( ctLength );

		sgVisualAB.frontFacingAppearance.getValue().setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.YELLOW );
		sgVisualBC.frontFacingAppearance.getValue().setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.CYAN );
		sgVisualCT.frontFacingAppearance.getValue().setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.WHITE );

		sgVisualAB.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgCylinderAB } );
		sgVisualAB.setParent( this );

		sgVisualBC.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgCylinderBC } );
		sgVisualBC.setParent( m_sgB );

		sgVisualCT.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgCylinderCT } );
		sgVisualCT.setParent( m_sgC );

		m_sgC.setParent( m_sgB );
		m_sgB.setParent( this );
	}
}
