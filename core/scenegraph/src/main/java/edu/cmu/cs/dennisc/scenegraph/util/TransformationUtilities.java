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

//todo: unify w/ lookingglass.TransformationUtilities?

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Scene;

/**
 * @author Dennis Cosgrove
 */
public abstract class TransformationUtilities {
  private static boolean isAbsolute(ReferenceFrame sgReferenceFrame) {
    //todo: call isSceneOf()?
    if ((sgReferenceFrame instanceof Scene) || (sgReferenceFrame == AsSeenBy.SCENE)) {
      return true;
    } else {
      return false;
    }
  }

  //Vector4d
  public static Vector4 transformToAbsolute(Vector4 xyzInFrom, Component sgFrom) {
    return transformToAbsolute_internal(new Vector4(xyzInFrom), sgFrom);
  }

  private static Vector4 transformToAbsolute_internal(Vector4 rv, Component sgFrom) {
    if (!isAbsolute(sgFrom)) {
      AffineMatrix4x4 m = sgFrom.getAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public static Vector4 transformFromAbsolute(Vector4 xyzInAbsolute, Component sgTo) {
    return transformFromAbsolute_internal(new Vector4(xyzInAbsolute), sgTo);
  }

  private static Vector4  transformFromAbsolute_internal(Vector4 rv, Component sgTo) {
    if (!isAbsolute(sgTo)) {
      AffineMatrix4x4 m = sgTo.getInverseAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public static Vector4 transformTo(Vector4 xyzInFrom, Component sgFrom, Component sgTo) {
    Vector4 rv = new Vector4(xyzInFrom);
    transformToAbsolute_internal(rv, sgFrom);
    transformFromAbsolute_internal(rv, sgTo);
    return rv;
  }

  //Vector3d
  public static Vector3 transformToAbsolute(Vector3 xyzInFrom, Component sgFrom) {
    return transformToAbsolute_internal(new Vector3(xyzInFrom), sgFrom);
  }

  private static Vector3 transformToAbsolute_internal(Vector3 rv, Component sgFrom) {
    if (!isAbsolute(sgFrom)) {
      AffineMatrix4x4 m = sgFrom.getAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public static Vector3 transformFromAbsolute(Vector3 xyzInAbsolute, Component sgTo) {
    return transformFromAbsolute_internal(new Vector3(xyzInAbsolute), sgTo);
  }

  private static Vector3  transformFromAbsolute_internal(Vector3 rv, Component sgTo) {
    if (!isAbsolute(sgTo)) {
      AffineMatrix4x4 m = sgTo.getInverseAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public static Vector3 transformTo(Vector3 xyzInFrom, Component sgFrom, Component sgTo) {
    Vector3 rv = new Vector3(xyzInFrom);
    transformToAbsolute_internal(rv, sgFrom);
    transformFromAbsolute_internal(rv, sgTo);
    return rv;
  }

  //Point3d
  public static Point3 transformToAbsolute(Point3 xyzInFrom, Component sgFrom) {
    return transformToAbsolute_internal(new Point3(xyzInFrom), sgFrom);
  }

  private static Point3 transformToAbsolute_internal(Point3 rv, Component sgFrom) {
    if (!isAbsolute(sgFrom)) {
      AffineMatrix4x4 m = sgFrom.getAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public static Point3 transformFromAbsolute(Point3 xyzInAbsolute, Component sgTo) {
    return transformFromAbsolute_internal(new Point3(xyzInAbsolute), sgTo);
  }

  private static Point3 transformFromAbsolute_internal(Point3 rv, Component sgTo) {
    if (!isAbsolute(sgTo)) {
      AffineMatrix4x4 m = sgTo.getInverseAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public static Point3 transformTo(Point3 xyzInFrom, Component sgFrom, Component sgTo) {
    Point3 rv = new Point3(xyzInFrom);
    transformToAbsolute_internal(rv, sgFrom);
    transformFromAbsolute_internal(rv, sgTo);
    return rv;
  }
}
