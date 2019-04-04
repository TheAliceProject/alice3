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

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Sphere;

/**
 * @author Dennis Cosgrove
 */
public class Limb extends ModelTransformable {
  private ModelTransformable m_sgB = new ModelTransformable();
  private ModelTransformable m_sgC = new ModelTransformable();

  public Limb(double abLength, double bcLength, double ctLength) {
    double sphereRadius = Math.min(abLength, Math.min(bcLength, ctLength)) * 0.25;
    double cylinderRadius = sphereRadius * 0.5;

    m_sgB.setLocalTransformation(AffineMatrix4x4.createTranslation(0, 0, abLength));
    m_sgC.setLocalTransformation(AffineMatrix4x4.createTranslation(0, 0, bcLength));

    Sphere sgSphere = new Sphere();
    sgSphere.radius.setValue(sphereRadius);

    this.getSGVisual().geometries.setValue(new Geometry[] {sgSphere});
    m_sgB.getSGVisual().geometries.setValue(new Geometry[] {sgSphere});
    m_sgC.getSGVisual().geometries.setValue(new Geometry[] {sgSphere});

    this.getSGVisual().frontFacingAppearance.getValue().setDiffuseColor(Color4f.RED);
    m_sgB.getSGVisual().frontFacingAppearance.getValue().setDiffuseColor(Color4f.GREEN);
    m_sgC.getSGVisual().frontFacingAppearance.getValue().setDiffuseColor(Color4f.BLUE);

    ModelVisual sgVisualAB = new ModelVisual();
    ModelVisual sgVisualBC = new ModelVisual();
    ModelVisual sgVisualCT = new ModelVisual();

    Cylinder sgCylinderAB = new Cylinder();
    Cylinder sgCylinderBC = new Cylinder();
    Cylinder sgCylinderCT = new Cylinder();

    sgCylinderAB.topRadius.setValue(cylinderRadius);
    sgCylinderAB.bottomRadius.setValue(cylinderRadius);
    sgCylinderAB.originAlignment.setValue(Cylinder.OriginAlignment.BOTTOM);
    sgCylinderAB.bottomToTopAxis.setValue(Cylinder.BottomToTopAxis.POSITIVE_Z);
    sgCylinderAB.length.setValue(abLength);

    sgCylinderBC.topRadius.setValue(cylinderRadius);
    sgCylinderBC.bottomRadius.setValue(cylinderRadius);
    sgCylinderBC.originAlignment.setValue(Cylinder.OriginAlignment.BOTTOM);
    sgCylinderBC.bottomToTopAxis.setValue(Cylinder.BottomToTopAxis.POSITIVE_Z);
    sgCylinderBC.length.setValue(bcLength);

    sgCylinderCT.topRadius.setValue(0.0);
    sgCylinderCT.bottomRadius.setValue(cylinderRadius);
    sgCylinderCT.originAlignment.setValue(Cylinder.OriginAlignment.BOTTOM);
    sgCylinderCT.bottomToTopAxis.setValue(Cylinder.BottomToTopAxis.POSITIVE_Z);
    sgCylinderCT.length.setValue(ctLength);

    sgVisualAB.frontFacingAppearance.getValue().setDiffuseColor(Color4f.YELLOW);
    sgVisualBC.frontFacingAppearance.getValue().setDiffuseColor(Color4f.CYAN);
    sgVisualCT.frontFacingAppearance.getValue().setDiffuseColor(Color4f.WHITE);

    sgVisualAB.geometries.setValue(new Geometry[] {sgCylinderAB});
    sgVisualAB.setParent(this);

    sgVisualBC.geometries.setValue(new Geometry[] {sgCylinderBC});
    sgVisualBC.setParent(m_sgB);

    sgVisualCT.geometries.setValue(new Geometry[] {sgCylinderCT});
    sgVisualCT.setParent(m_sgC);

    m_sgC.setParent(m_sgB);
    m_sgB.setParent(this);
  }
}
