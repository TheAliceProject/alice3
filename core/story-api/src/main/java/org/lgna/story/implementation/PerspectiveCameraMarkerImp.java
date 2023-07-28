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
package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.scenegraph.Box;
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
import org.lgna.story.PerspectiveCameraMarker;
import org.lgna.story.SCamera;
import org.lgna.story.resources.DynamicResource;

import java.util.List;

/**
 * @author dculyba
 */
public class PerspectiveCameraMarkerImp extends CameraMarkerImp {
  private static final double VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA = 3;
  private static final float START_ALPHA = .5f;
  private static final float END_ALPHA = 0f;
  private static final double LENGTH = .75;
  private static final double RADIUS = LENGTH / 4;
  private static final double HEIGHT = .4;
  private static final double WIDTH = .15;
  private static final double LENS_HOOD_LENGTH = .2;
  private static final float LASER_LINE_RED = 1;
  private static final float LASER_LINE_GREEN = 0;
  private static final float LASER_LINE_BLUE = 0;

  public PerspectiveCameraMarkerImp(PerspectiveCameraMarker abstraction) {
    super(abstraction);
  }

  @Override
  protected Color4f getDefaultMarkerColor() {
    return Color4f.GRAY;
  }

  @Override
  protected void createVisuals() {
    initialize();
    addLaserSightLine();
    updateViewGeometry();
    setDetailedViewShowing(false);
    updateDetailIsShowing();
  }

  private void initialize() {
    sgAppearances = new SimpleAppearance[] {new SimpleAppearance()};
    sgDetailedComponents = Lists.newLinkedList();
    farClippingPlane = 100;
    sgVrVisuals = createVRVisual(getSgPaintAppearances()[0], getSgComposite());
    showVisuals(sgVrVisuals, false);
    sgCameraVisuals = createCameraVisuals(getSgPaintAppearances()[0], getSgComposite());
    showVisuals(sgCameraVisuals, false);
  }

  public static Visual[] createVRVisual(SimpleAppearance paint, Transformable parent) {
    var rigResource = DynamicResource.getInternalResource("VRrigNewMesh", "VRrigNewMesh");
    Visual[] visuals = rigResource.getImplementationAndVisualFactory().createVisualData().getSgVisuals();
    visuals[0].setParent(parent);
    visuals[0].frontFacingAppearance.setValue(paint);
    return visuals;
  }

  public static Visual[] createCameraVisuals(SimpleAppearance paint, Transformable parent) {
    return new Visual[]{createLensVisual(paint, parent),
        createCylinderVisual("Camera Cylinder 1", RADIUS, paint, parent),
        createCylinderVisual("Camera Cylinder 2", RADIUS * 3, paint, parent),
        createBoxVisual(paint, parent)};
  }

  private static Visual createBoxVisual(SimpleAppearance paint, Transformable parent) {
    Visual sgBoxVisual = new Visual();
    sgBoxVisual.setName("Camera Box Visual");
    sgBoxVisual.frontFacingAppearance.setValue(paint);
    Box sgBox = new Box();
    sgBox.setMinimum(new Point3(-WIDTH / 2, -HEIGHT / 2, 0));
    sgBox.setMaximum(new Point3(WIDTH / 2, HEIGHT / 2, LENGTH));
    sgBoxVisual.geometries.setValue(new Geometry[] {sgBox});
    sgBoxVisual.setParent(parent);
    return sgBoxVisual;
  }

  private static Visual createCylinderVisual(String name, double offset, SimpleAppearance paint, Transformable parent) {
    Visual visual = new Visual();
    visual.setName(name + " Visual");
    visual.frontFacingAppearance.setValue(paint);
    Transformable transformable = new Transformable();
    transformable.setName(name);
    transformable.applyTranslation(new Vector3(-WIDTH / 2, (HEIGHT / 2) + RADIUS, offset));
    visual.geometries.setValue(new Geometry[]{createFilmCylinder()});
    visual.setParent(transformable);
    transformable.setParent(parent);
    return visual;
  }

  private static Cylinder createFilmCylinder() {
    Cylinder sgCylinder1 = new Cylinder();
    sgCylinder1.topRadius.setValue(RADIUS);
    sgCylinder1.bottomRadius.setValue(RADIUS);
    sgCylinder1.length.setValue(WIDTH);
    sgCylinder1.bottomToTopAxis.setValue(BottomToTopAxis.POSITIVE_X);
    sgCylinder1.hasTopCap.setValue(true);
    sgCylinder1.hasBottomCap.setValue(true);
    return sgCylinder1;
  }

  private static Visual createLensVisual(SimpleAppearance paint, Transformable parent) {
    Visual sgLensVisual = new Visual();
    sgLensVisual.setName("Camera Lens Hood Visual");
    sgLensVisual.frontFacingAppearance.setValue(paint);
    QuadArray sgLensGeometry = new QuadArray();
    Vertex[] sgLensVertices = new Vertex[32];
    Point3 innerTopLeft = new Point3(-WIDTH / 2, HEIGHT / 4, 0);
    Point3 innerTopRight = new Point3(WIDTH / 2, HEIGHT / 4, 0);
    Point3 innerBottomLeft = new Point3(-WIDTH / 2, -HEIGHT / 4, 0);
    Point3 innerBottomRight = new Point3(WIDTH / 2, -HEIGHT / 4, 0);
    Point3 outerTopLeft = new Point3(-HEIGHT / 2, HEIGHT / 2, -LENS_HOOD_LENGTH);
    Point3 outerTopRight = new Point3(HEIGHT / 2, HEIGHT / 2, -LENS_HOOD_LENGTH);
    Point3 outerBottomLeft = new Point3(-HEIGHT / 2, -HEIGHT / 2, -LENS_HOOD_LENGTH);
    Point3 outerBottomRight = new Point3(HEIGHT / 2, -HEIGHT / 2, -LENS_HOOD_LENGTH);

    Vector3 topNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerTopRight, innerTopLeft), Vector3.createSubtraction(outerTopLeft, innerTopLeft));
    topNormal.normalize();
    Vector3 rightNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerBottomRight, innerTopRight), Vector3.createSubtraction(outerTopRight, innerTopRight));
    rightNormal.normalize();
    Vector3 bottomNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerBottomLeft, innerBottomRight), Vector3.createSubtraction(outerBottomRight, innerBottomRight));
    bottomNormal.normalize();
    Vector3 leftNormal = Vector3.createCrossProduct(Vector3.createSubtraction(innerTopLeft, innerBottomLeft), Vector3.createSubtraction(outerBottomLeft, innerBottomLeft));
    leftNormal.normalize();

    Vector3f topNormalf = new Vector3f((float) topNormal.x, (float) topNormal.y, (float) topNormal.z);
    Vector3f rightNormalf = new Vector3f((float) rightNormal.x, (float) rightNormal.y, (float) rightNormal.z);
    Vector3f bottomNormalf = new Vector3f((float) bottomNormal.x, (float) bottomNormal.y, (float) bottomNormal.z);
    Vector3f leftNormalf = new Vector3f((float) leftNormal.x, (float) leftNormal.y, (float) leftNormal.z);
    Vector3f negTopNormalf = Vector3f.createMultiplication(topNormalf, -1);
    Vector3f negRightNormalf = Vector3f.createMultiplication(rightNormalf, -1);
    Vector3f negBottomNormalf = Vector3f.createMultiplication(bottomNormalf, -1);
    Vector3f negLeftNormalf = Vector3f.createMultiplication(leftNormalf, -1);

    TextureCoordinate2f uvs = TextureCoordinate2f.createNaN();

    sgLensVertices[0] = Vertex.createXYZIJKUV(outerTopLeft, topNormalf, uvs);
    sgLensVertices[1] = Vertex.createXYZIJKUV(innerTopLeft, topNormalf, uvs);
    sgLensVertices[2] = Vertex.createXYZIJKUV(innerTopRight, topNormalf, uvs);
    sgLensVertices[3] = Vertex.createXYZIJKUV(outerTopRight, topNormalf, uvs);

    sgLensVertices[4] = Vertex.createXYZIJKUV(outerTopRight, rightNormalf, uvs);
    sgLensVertices[5] = Vertex.createXYZIJKUV(innerTopRight, rightNormalf, uvs);
    sgLensVertices[6] = Vertex.createXYZIJKUV(innerBottomRight, rightNormalf, uvs);
    sgLensVertices[7] = Vertex.createXYZIJKUV(outerBottomRight, rightNormalf, uvs);

    sgLensVertices[8] = Vertex.createXYZIJKUV(outerBottomRight, bottomNormalf, uvs);
    sgLensVertices[9] = Vertex.createXYZIJKUV(innerBottomRight, bottomNormalf, uvs);
    sgLensVertices[10] = Vertex.createXYZIJKUV(innerBottomLeft, bottomNormalf, uvs);
    sgLensVertices[11] = Vertex.createXYZIJKUV(outerBottomLeft, bottomNormalf, uvs);

    sgLensVertices[12] = Vertex.createXYZIJKUV(outerBottomLeft, leftNormalf, uvs);
    sgLensVertices[13] = Vertex.createXYZIJKUV(innerBottomLeft, leftNormalf, uvs);
    sgLensVertices[14] = Vertex.createXYZIJKUV(innerTopLeft, leftNormalf, uvs);
    sgLensVertices[15] = Vertex.createXYZIJKUV(outerTopLeft, leftNormalf, uvs);

    //The opposite faces
    sgLensVertices[16] = Vertex.createXYZIJKUV(outerTopRight, negTopNormalf, uvs);
    sgLensVertices[17] = Vertex.createXYZIJKUV(innerTopRight, negTopNormalf, uvs);
    sgLensVertices[18] = Vertex.createXYZIJKUV(innerTopLeft, negTopNormalf, uvs);
    sgLensVertices[19] = Vertex.createXYZIJKUV(outerTopLeft, negTopNormalf, uvs);

    sgLensVertices[20] = Vertex.createXYZIJKUV(outerBottomRight, negRightNormalf, uvs);
    sgLensVertices[21] = Vertex.createXYZIJKUV(innerBottomRight, negRightNormalf, uvs);
    sgLensVertices[22] = Vertex.createXYZIJKUV(innerTopRight, negRightNormalf, uvs);
    sgLensVertices[23] = Vertex.createXYZIJKUV(outerTopRight, negRightNormalf, uvs);

    sgLensVertices[24] = Vertex.createXYZIJKUV(outerBottomLeft, negBottomNormalf, uvs);
    sgLensVertices[25] = Vertex.createXYZIJKUV(innerBottomLeft, negBottomNormalf, uvs);
    sgLensVertices[26] = Vertex.createXYZIJKUV(innerBottomRight, negBottomNormalf, uvs);
    sgLensVertices[27] = Vertex.createXYZIJKUV(outerBottomRight, negBottomNormalf, uvs);

    sgLensVertices[28] = Vertex.createXYZIJKUV(outerTopLeft, negLeftNormalf, uvs);
    sgLensVertices[29] = Vertex.createXYZIJKUV(innerTopLeft, negLeftNormalf, uvs);
    sgLensVertices[30] = Vertex.createXYZIJKUV(innerBottomLeft, negLeftNormalf, uvs);
    sgLensVertices[31] = Vertex.createXYZIJKUV(outerBottomLeft, negLeftNormalf, uvs);

    sgLensGeometry.vertices.setValue(sgLensVertices);
    sgLensVisual.geometries.setValue(new Geometry[] {sgLensGeometry});
    sgLensVisual.setParent(parent);
    return sgLensVisual;
  }

  private void addLaserSightLine() {
    SimpleAppearance sgLaserLinesFrontFacingAppearance = new SimpleAppearance();
    sgLaserLinesFrontFacingAppearance.diffuseColor.setValue(Color4f.RED);
    sgLaserLinesFrontFacingAppearance.shadingStyle.setValue(ShadingStyle.NONE);
    sgLaserLineVertices = new Vertex[2];
    sgLaserLineVertices[0] = Vertex.createXYZRGBA(0, 0, 0, LASER_LINE_RED, LASER_LINE_GREEN, LASER_LINE_BLUE, START_ALPHA);
    sgLaserLineVertices[1] = Vertex.createXYZRGBA(0, 0, -VIEW_LINES_DEFAULT_DISTANCE_FROM_CAMERA, LASER_LINE_RED, LASER_LINE_GREEN, LASER_LINE_BLUE, END_ALPHA);
    Visual sgLaserLineVisual = new Visual();
    sgLaserLineVisual.setName("Camera Laser Line Visual");
    sgLaserLineVisual.frontFacingAppearance.setValue(sgLaserLinesFrontFacingAppearance);
    sgLaserLine = new LineArray();
    sgLaserLine.vertices.setValue(sgLaserLineVertices);
    sgLaserLineVisual.geometries.setValue(new Geometry[] {sgLaserLine});
    sgLaserLineVisual.setParent(getSgComposite());
    sgDetailedComponents.add(sgLaserLineVisual);
  }

  public void setDetailedViewShowing(boolean isShowing) {
    if (this.showDetail != isShowing) {
      this.showDetail = isShowing;
      this.updateDetailIsShowing();
    }
  }

  private void updateDetailIsShowing() {
    for (Visual v : this.sgDetailedComponents) {
      v.isShowing.setValue(this.showDetail && this.isShowing());
    }
  }

  private void updateViewGeometry() {
    if ((this.sgLaserLineVertices != null) && (this.sgLaserLine != null)) {
      this.sgLaserLineVertices[1].position.z = -this.farClippingPlane;
      this.sgLaserLine.vertices.setValue(this.sgLaserLineVertices);
    }
  }

  @Override
  protected final SimpleAppearance[] getSgPaintAppearances() {
    return this.sgAppearances;
  }

  @Override
  protected final SimpleAppearance[] getSgOpacityAppearances() {
    return this.getSgPaintAppearances();
  }

  public void setVrActive(Boolean isActive) {
    this.isVrActive = isActive;
    showVisuals(sgVrVisuals, isVrActive && getDisplayEnabled());
    showVisuals(sgCameraVisuals, !isVrActive && getDisplayEnabled());
    if (isVrActive) {
      sgLaserLineVertices[0].position.y = SCamera.DEFAULT_POSITION.getUp();
    }
    sgLaserLine.vertices.setValue(sgLaserLineVertices);
  }

  private static void showVisuals(Visual[] visuals, boolean show) {
    if (visuals != null) {
      for (Visual v : visuals) {
        v.isShowing.setValue(show);
      }
    }
  }

  @Override
  public Visual[] getSgVisuals() {
    return isVrActive ? sgVrVisuals : sgCameraVisuals;
  }

  // Add the ENTITY_IMP entry to the Element's bonus data.
  // Manipulators look for it and will then treat this marker as the camera.
  public void actAsCamera(TransformableImp camera) {
    camera.putInstance(getSgComposite());
  }

  private double farClippingPlane;

  private Vertex[] sgLaserLineVertices;
  private LineArray sgLaserLine;

  private boolean isVrActive;
  private Visual[] sgVrVisuals;
  private Visual[] sgCameraVisuals;
  private SimpleAppearance[] sgAppearances;
  private List<Visual> sgDetailedComponents;

  protected boolean showDetail = false;
}
