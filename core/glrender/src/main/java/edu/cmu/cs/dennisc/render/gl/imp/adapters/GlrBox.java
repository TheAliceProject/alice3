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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static com.jogamp.opengl.GL2ES3.GL_QUADS;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Box;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 * Texture is defined by glTexCoord2f, which uses two values to crop the original picture
 */

public class GlrBox extends GlrShape<Box> {
  private void glBox(Context c, boolean isLightingEnabled, boolean isSubElementRequired, boolean isTexturingEnabled) {
    int id = 0;
    double[][] leftFace = {{this.xMin, this.yMin, this.zMax}, {this.xMin, this.yMax, this.zMax}, {this.xMin, this.yMax, this.zMin}, {this.xMin, this.yMin, this.zMin}};
    double[][] rightFace = {{this.xMax, this.yMin, this.zMin}, {this.xMax, this.yMax, this.zMin}, {this.xMax, this.yMax, this.zMax}, {this.xMax, this.yMin, this.zMax}};
    double[][] bottomFace = {{this.xMin, this.yMin, this.zMin}, {this.xMax, this.yMin, this.zMin}, {this.xMax, this.yMin, this.zMax}, {this.xMin, this.yMin, this.zMax}};
    double[][] topFace = {{this.xMin, this.yMax, this.zMax}, {this.xMax, this.yMax, this.zMax}, {this.xMax, this.yMax, this.zMin}, {this.xMin, this.yMax, this.zMin}};
    double[][] frontFace = {{this.xMin, this.yMax, this.zMin}, {this.xMax, this.yMax, this.zMin}, {this.xMax, this.yMin, this.zMin}, {this.xMin, this.yMin, this.zMin}};
    double[][] backFace = {{this.xMin, this.yMin, this.zMax}, {this.xMax, this.yMin, this.zMax}, {this.xMax, this.yMax, this.zMax}, {this.xMin, this.yMax, this.zMax}};
    vertexMap.put("left", leftFace);
    vertexMap.put("right", rightFace);
    vertexMap.put("bottom", bottomFace);
    vertexMap.put("top", topFace);
    vertexMap.put("front", frontFace);
    vertexMap.put("back", backFace);

    c.gl.glBegin(GL_QUADS);

    // xMin face
    // c.gl.glColor3d( 1,1,1 );
    if (isSubElementRequired) {
      c.gl.glLoadName(id++);
    }
    drawBox(c, isLightingEnabled, isTexturingEnabled, "left");
    // xMax face
    // c.gl.glColor3d( 1,0,0 );
    if (isSubElementRequired) {
      c.gl.glLoadName(id++);
    }
    drawBox(c, isLightingEnabled, isTexturingEnabled, "right");
    // yMin face
    // c.gl.glColor3d( 1,1,1 );
    if (isSubElementRequired) {
      c.gl.glLoadName(id++);
    }
    drawBox(c, isLightingEnabled, isTexturingEnabled, "bottom");
    // yMax face
    // c.gl.glColor3d( 0,1,0 );
    if (isSubElementRequired) {
      c.gl.glLoadName(id++);
    }
    drawBox(c, isLightingEnabled, isTexturingEnabled, "top");
    // zMin face
    // c.gl.glColor3d( 1,1,1 );
    if (isSubElementRequired) {
      c.gl.glLoadName(id++);
    }
    drawBox(c, isLightingEnabled, isTexturingEnabled, "front");
    // zMax face
    // c.gl.glColor3d( 0,0,1 );
    if (isSubElementRequired) {
      c.gl.glLoadName(id++);
    }
    drawBox(c, isLightingEnabled, isTexturingEnabled, "back");

    c.gl.glEnd();
  }

  private void drawBox(Context c, boolean isLightingEnabled, boolean isTexturingEnabled, String side) {
    int[] sideNormal3d = normal3dMap.get(side);
    float[][] sideTex = texCoordMap.get(side);
    double[][] sideVertex = vertexMap.get(side);

    if (isLightingEnabled) {
      c.gl.glNormal3d(sideNormal3d[0], sideNormal3d[1], sideNormal3d[2]);
    }
    for (int i = 0; i < numberOfCorner; i++) {
      if (isTexturingEnabled) {
        c.gl.glTexCoord2f(sideTex[i][0], sideTex[i][1]);
      }
      c.gl.glVertex3d(sideVertex[i][0], sideVertex[i][1], sideVertex[i][2]);
    }
  }

  @Override
  protected void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType) {
    glBox(rc, true, false, rc.isTextureEnabled());
  }

  @Override
  protected void pickGeometry(PickContext pc, boolean isSubElementRequired) {
    pc.gl.glPushName(-1);
    glBox(pc, false, isSubElementRequired, false);
    pc.gl.glPopName();
  }

  @Override
  public Point3 getIntersectionInSource(Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement) {
    Point3 origin = new Point3(0, 0, 0);
    Vector3 direction = new Vector3(0, 0, 0);
    switch (subElement) {
      case 0:
        origin.x = this.xMin;
        direction.x = -1;
        break;
      case 1:
        origin.x = this.xMax;
        direction.x = 1;
        break;
      case 2:
        origin.y = this.yMin;
        direction.y = -1;
        break;
      case 3:
        origin.y = this.yMax;
        direction.y = 1;
        break;
      case 4:
        origin.z = this.zMin;
        direction.z = -1;
        break;
      case 5:
        origin.z = this.zMax;
        direction.z = 1;
        break;
      default:
        rv.setNaN();
        return rv;
    }
    GlrGeometry.getIntersectionInSourceFromPlaneInLocal(rv, ray, m, origin.x, origin.y, origin.z, direction.x, direction.y, direction.z);
    return rv;
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.xMinimum) {
      this.xMin = owner.xMinimum.getValue();
      markGeometryAsChanged();
    } else if (property == owner.xMaximum) {
      this.xMax = owner.xMaximum.getValue();
      markGeometryAsChanged();
    } else if (property == owner.yMinimum) {
      this.yMin = owner.yMinimum.getValue();
      markGeometryAsChanged();
    } else if (property == owner.yMaximum) {
      this.yMax = owner.yMaximum.getValue();
      markGeometryAsChanged();
    } else if (property == owner.zMinimum) {
      this.zMin = owner.zMinimum.getValue();
      markGeometryAsChanged();
    } else if (property == owner.zMaximum) {
      this.zMax = owner.zMaximum.getValue();
      markGeometryAsChanged();
    } else {
      super.propertyChanged(property);
    }
  }

  private double xMin;
  private double xMax;
  private double yMin;
  private double yMax;
  private double zMin;
  private double zMax;
  private static final int numberOfCorner = 4;
  private static final int[] leftNormal3d = {-1, 0, 0};
  private static final int[] rightNormal3d = {1, 0, 0};
  private static final int[] bottomNormal3d = {0, -1, 0};
  private static final int[] topNormal3d = {0, 1, 0};
  private static final int[] frontNormal3d = {0, 0, -1};
  private static final int[] backNormal3d = {0, 0, 1};
  private static final float[][] leftTexCoord = {{.75f, .66f}, {.75f, .33f}, {.5f, .33f}, {.5f, .66f}};
  private static final float[][] rightTexCoord = {{.25f, .66f}, {.25f, .33f}, {.0f, .33f}, {.0f, .66f}};
  private static final float[][] bottomTexCoord = {{.5f, .66f}, {.25f, .66f}, {.25f, 1.0f}, {.5f, 1.0f}};
  private static final float[][] topTexCoord = {{.5f, .0f}, {.25f, .0f}, {.25f, .33f}, {.5f, .33f}};
  private static final float[][] frontTexCoord = {{.5f, .33f}, {.25f, .33f}, {.25f, .66f}, {.5f, .66f}};
  private static final float[][] backTexCoord = {{.75f, .66f}, {1.0f, .66f}, {1.0f, .33f}, {.75f, .33f}};
  private static final Map<String, int[]> normal3dMap = new HashMap<>();
  private static final Map<String, float[][]> texCoordMap = new HashMap<>();
  private Map<String, double[][]> vertexMap = new HashMap<>();

  static {
    normal3dMap.put("left", leftNormal3d);
    normal3dMap.put("right", rightNormal3d);
    normal3dMap.put("bottom", bottomNormal3d);
    normal3dMap.put("top", topNormal3d);
    normal3dMap.put("front", frontNormal3d);
    normal3dMap.put("back", backNormal3d);
    texCoordMap.put("left", leftTexCoord);
    texCoordMap.put("right", rightTexCoord);
    texCoordMap.put("bottom", bottomTexCoord);
    texCoordMap.put("top", topTexCoord);
    texCoordMap.put("front", frontTexCoord);
    texCoordMap.put("back", backTexCoord);
  }
}
