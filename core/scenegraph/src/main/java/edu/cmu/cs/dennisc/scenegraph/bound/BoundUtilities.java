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
package edu.cmu.cs.dennisc.scenegraph.bound;

import org.alice.math.immutable.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

import java.nio.DoubleBuffer;
import java.util.Vector;

/**
 * @author Dennis Cosgrove
 */
public class BoundUtilities {
  private BoundUtilities() {
    throw new AssertionError();
  }

  //TODO: remove duplicate code, if possible

  public static AxisAlignedBox getBoundingBox(Vertex[] va) {
    Point3 min = new Point3(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE);
    Point3 max = new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
    for (Vertex v : va) {
      min.x = Math.min(min.x, v.position.x);
      min.y = Math.min(min.y, v.position.y);
      min.z = Math.min(min.z, v.position.z);
      max.x = Math.max(max.x, v.position.x);
      max.y = Math.max(max.y, v.position.y);
      max.z = Math.max(max.z, v.position.z);
    }
    if (min.x == +Double.MAX_VALUE) {
      return AxisAlignedBox.NaN;
    }
    return new AxisAlignedBox(min.immutable(), max.immutable());
  }

  public static AxisAlignedBox getBoundingBox(Iterable<Point3> pi) {
    Point3 min = new Point3(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE);
    Point3 max = new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
    for (Point3 p : pi) {
      min.x = Math.min(min.x, p.x);
      min.y = Math.min(min.y, p.y);
      min.z = Math.min(min.z, p.z);
      max.x = Math.max(max.x, p.x);
      max.y = Math.max(max.y, p.y);
      max.z = Math.max(max.z, p.z);
    }
    if (min.x == +Double.MAX_VALUE) {
      return AxisAlignedBox.NaN;
    }
    return new AxisAlignedBox(min.immutable(), max.immutable());
  }

  public static AxisAlignedBox getBoundingBox(Point3[] pa) {
    Point3 min = new Point3(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE);
    Point3 max = new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
    for (Point3 p : pa) {
      min.x = Math.min(min.x, p.x);
      min.y = Math.min(min.y, p.y);
      min.z = Math.min(min.z, p.z);
      max.x = Math.max(max.x, p.x);
      max.y = Math.max(max.y, p.y);
      max.z = Math.max(max.z, p.z);
    }
    if (min.x == +Double.MAX_VALUE) {
      return AxisAlignedBox.NaN;
    }
    return new AxisAlignedBox(min.immutable(), max.immutable());
  }

  public static AxisAlignedBox getBoundingBox(double[] xyzs) {
    Point3 min = new Point3(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE);
    Point3 max = new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
    final int N = xyzs.length;
    for (int i = 0; i < N; i += 3) {
      double x = xyzs[i + 0];
      double y = xyzs[i + 1];
      double z = xyzs[i + 2];
      min.x = Math.min(min.x, x);
      min.y = Math.min(min.y, y);
      min.z = Math.min(min.z, z);
      max.x = Math.max(max.x, x);
      max.y = Math.max(max.y, y);
      max.z = Math.max(max.z, z);
    }
    if (min.x == +Double.MAX_VALUE) {
      return AxisAlignedBox.NaN;
    }
    return new AxisAlignedBox(min.immutable(), max.immutable());
  }

  public static AxisAlignedBox getBoundingBox(DoubleBuffer xyzs) {
    Point3 min = new Point3(+Double.MAX_VALUE, +Double.MAX_VALUE, +Double.MAX_VALUE);
    Point3 max = new Point3(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
    final int N = xyzs.limit();
    for (int i = 0; i < N; i += 3) {
      double x = xyzs.get(i + 0);
      double y = xyzs.get(i + 1);
      double z = xyzs.get(i + 2);
      min.x = Math.min(min.x, x);
      min.y = Math.min(min.y, y);
      min.z = Math.min(min.z, z);
      max.x = Math.max(max.x, x);
      max.y = Math.max(max.y, y);
      max.z = Math.max(max.z, z);
    }
    if (min.x == +Double.MAX_VALUE) {
      return AxisAlignedBox.NaN;
    }
    return new AxisAlignedBox(min.immutable(), max.immutable());
  }
  }
}
