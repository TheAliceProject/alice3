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

import edu.cmu.cs.dennisc.scenegraph.Vertex;
import org.alice.math.immutable.AxisAlignedBox;
import org.alice.math.immutable.Point3;

import java.nio.DoubleBuffer;

public class BoundUtilities {
  private BoundUtilities() {
    throw new AssertionError();
  }

  public static AxisAlignedBox getBoundingBox(Vertex[] va) {
    DynamicLimits limits = new DynamicLimits();
    for (Vertex v : va) {
      limits.check(v.position.x(), v.position.y(), v.position.z());
    }
    return limits.getBoundingBox();
  }

  public static AxisAlignedBox getBoundingBox(Iterable<Point3> pi) {
    DynamicLimits limits = new DynamicLimits();
    for (Point3 p : pi) {
      limits.check(p.x(), p.y(), p.z());
    }
    return limits.getBoundingBox();
  }

  public static AxisAlignedBox getBoundingBox(double[] xyzs) {
    DynamicLimits limits = new DynamicLimits();
    for (int i = 0; i < xyzs.length; i += 3) {
      limits.check(xyzs[i], xyzs[i + 1], xyzs[i + 2]);
    }
    return limits.getBoundingBox();
  }

  public static AxisAlignedBox getBoundingBox(DoubleBuffer xyzs) {
    DynamicLimits limits = new DynamicLimits();
    for (int i = 0; i < xyzs.limit(); i += 3) {
      limits.check(xyzs.get(i), xyzs.get(i + 1), xyzs.get(i + 2));
    }
    return limits.getBoundingBox();
  }

  private static class DynamicLimits {
    double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
    double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

    void check(double x, double y, double z) {
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
      minZ = Math.min(minZ, z);
      maxZ = Math.max(maxZ, z);
    }

    public AxisAlignedBox getBoundingBox() {
      if (minX == +Double.MAX_VALUE) {
        return AxisAlignedBox.NaN;
      }
      return new AxisAlignedBox(new Point3(minX, minY, minZ), new Point3(maxX, maxY, maxZ));
    }
  }
}
