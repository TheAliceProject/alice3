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
package org.alice.interact;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author David Culyba
 */
public class PlaneUtilities {
  public static Point3 getPointInPlane(Plane plane, Ray ray) {
    double t = plane.intersect(ray);
    if (Double.isNaN(t) || (t < 0)) {
      return null;
    } else {
      return ray.getPointAlong(t);
    }
  }

  public static Ray getRayFromPixel(RenderTarget renderTarget, AbstractCamera camera, int xPixel, int yPixel) {
    Ray ray = renderTarget.getRayAtPixel(xPixel, yPixel, camera).mutable();
    AffineMatrix4x4 m = camera.getAbsoluteTransformation();
    ray.transform(m);
    return ray;
  }

  public static double distanceToPlane(Plane plane, Point3 point) {
    double[] equation = plane.getEquation();
    double topVal = (equation[0] * point.x) + (equation[1] * point.y) + (equation[2] * point.z) + equation[3];
    double bottomVal = Math.sqrt((equation[0] * equation[0]) + (equation[1] * equation[1]) + (equation[2] * equation[2]));
    return topVal / bottomVal;
  }

  public static Vector3 getPlaneNormal(Plane plane) {
    double[] equation = plane.getEquation();
    return new Vector3(equation[0], equation[1], equation[2]);
  }

  public static Point3 projectPointIntoPlane(Plane plane, Point3 point) {
    double distanceToPlane = distanceToPlane(plane, point);
    Vector3 offsetVector = Vector3.createMultiplication(getPlaneNormal(plane), -distanceToPlane);
    return Point3.createAddition(point, offsetVector);
  }

}
