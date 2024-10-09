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

import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_SHORT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL2GL3.GL_DOUBLE;
import static com.jogamp.opengl.GL2GL3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_NORMAL_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_TEXTURE_COORD_ARRAY;
import static com.jogamp.opengl.fixedfunc.GLPointerFunc.GL_VERTEX_ARRAY;

import com.jogamp.opengl.GL2;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.OldMesh;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * @author Dennis Cosgrove
 */
public class GlrOldMesh extends GlrGeometry<OldMesh> {
  @Override
  public boolean isAlphaBlended() {
    return false;
  }

  private void glDraw(GL2 gl, int mode, short[] xyzIndices, short[] ijkIndices, short[] uvIndices) {
    final int N = xyzIndices != null ? xyzIndices.length : 0;
    if (N > 0) {
      gl.glBegin(mode);
      try {
        for (int i = 0; i < N; i++) {
          int xyzIndex = xyzIndices[i];
          int ijkIndex = ijkIndices != null ? ijkIndices[i] : xyzIndex;
          int uvIndex = uvIndices != null ? uvIndices[i] : xyzIndex;
          gl.glTexCoord2f(this.uvs[(uvIndex * 2) + 0], this.uvs[(uvIndex * 2) + 1]);
          gl.glNormal3f(this.ijks[(ijkIndex * 3) + 0], this.ijks[(ijkIndex * 3) + 1], this.ijks[(ijkIndex * 3) + 2]);
          gl.glVertex3d(this.xyzs[(xyzIndex * 3) + 0], this.xyzs[(xyzIndex * 3) + 1], this.xyzs[(xyzIndex * 3) + 2]);
        }
      } finally {
        gl.glEnd();
      }
    }
  }

  private void glDrawElements(GL2 gl, int mode, boolean b) {
    if (b) {
      short[] xyzIndices;
      if (mode == GL_TRIANGLES) {
        xyzIndices = this.xyzTriangleIndices;
      } else if (mode == GL_QUADS) {
        xyzIndices = this.xyzQuadrangleIndices;
      } else {
        throw new AssertionError();
      }
      final int N = xyzIndices != null ? xyzIndices.length : 0;
      if (N > 0) {
        gl.glBegin(mode);
        try {
          for (int i = 0; i < xyzIndices.length; i++) {
            gl.glArrayElement(i);
          }
        } finally {
          gl.glEnd();
        }
      }
    } else {
      if (mode == GL_TRIANGLES) {
        if (this.triangleIndexBuffer == null) {
          this.triangleIndexBuffer = BufferUtilities.createDirectShortBuffer(this.xyzTriangleIndices);
        }
        this.triangleIndexBuffer.rewind();

        gl.glDrawElements(mode, this.xyzTriangleIndices.length / 3, GL_SHORT, this.triangleIndexBuffer);
      } else if (mode == GL_QUADS) {
        if (this.quadrangleIndexBuffer == null) {
          this.quadrangleIndexBuffer = BufferUtilities.createDirectShortBuffer(this.xyzQuadrangleIndices);
          this.quadrangleIndexBuffer.rewind();
        }
        gl.glDrawElements(mode, this.xyzQuadrangleIndices.length / 4, GL_SHORT, this.quadrangleIndexBuffer);
      } else {
        throw new AssertionError();
      }
    }
  }

  private void glGeometry(GL2 gl, boolean isArrayRenderingDesired) {
    if ((isArrayRenderingDesired == false) || (this.ijkTriangleIndices != null) || (this.uvTriangleIndices != null) || (this.ijkQuadrangleIndices != null) || (this.uvQuadrangleIndices != null)) {
      glDraw(gl, GL_TRIANGLES, this.xyzTriangleIndices, this.ijkTriangleIndices, this.uvTriangleIndices);
      glDraw(gl, GL_QUADS, this.xyzQuadrangleIndices, this.ijkQuadrangleIndices, this.uvQuadrangleIndices);
    } else {
      if (this.xyzBuffer == null) {
        this.xyzBuffer = BufferUtilities.createDirectDoubleBuffer(xyzs);
      }
      this.xyzBuffer.rewind();
      if (this.ijkBuffer == null) {
        this.ijkBuffer = BufferUtilities.createDirectFloatBuffer(ijks);
      }
      this.ijkBuffer.rewind();
      if (this.uvBuffer == null) {
        this.uvBuffer = BufferUtilities.createDirectFloatBuffer(uvs);
      }
      this.uvBuffer.rewind();
      gl.glEnableClientState(GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL_NORMAL_ARRAY);
      gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
      try {
        gl.glVertexPointer(3, GL_DOUBLE, 0, this.xyzBuffer);
        gl.glNormalPointer(GL_FLOAT, 0, this.ijkBuffer);
        gl.glTexCoordPointer(2, GL_FLOAT, 0, this.uvBuffer);

        boolean b = true;
        glDrawElements(gl, GL_TRIANGLES, b);
        glDrawElements(gl, GL_QUADS, b);
      } finally {
        gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL_VERTEX_ARRAY);
      }
    }
  }

  @Override
  protected void renderGeometry(RenderContext rc, GlrVisual.RenderType renderType) {
    //    if( xyzs != null && xyzs.length > Short.MAX_VALUE / 3 ) {
    //      edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning" );
    //    }
    glGeometry(rc.gl, false);
  }

  @Override
  protected void pickGeometry(PickContext pc, boolean isSubElementRequired) {
    pc.gl.glPushName(-1);
    if (isSubElementRequired) {
      throw new RuntimeException("todo");
    } else {
      glGeometry(pc.gl, false);
    }
    pc.gl.glPopName();
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.xyzs) {
      this.xyzs = owner.xyzs.getValue();
      this.xyzBuffer = null;
      markGeometryAsChanged();
    } else if (property == owner.ijks) {
      this.ijks = owner.ijks.getValue();
      this.ijkBuffer = null;
      markGeometryAsChanged();
    } else if (property == owner.uvs) {
      this.uvs = owner.uvs.getValue();
      this.uvBuffer = null;
      markGeometryAsChanged();
    } else if (property == owner.xyzTriangleIndices) {
      this.xyzTriangleIndices = owner.xyzTriangleIndices.getValue();
      this.triangleIndexBuffer = null;
      markGeometryAsChanged();
    } else if (property == owner.ijkTriangleIndices) {
      this.ijkTriangleIndices = owner.ijkTriangleIndices.getValue();
      markGeometryAsChanged();
    } else if (property == owner.uvTriangleIndices) {
      this.uvTriangleIndices = owner.uvTriangleIndices.getValue();
      markGeometryAsChanged();
    } else if (property == owner.xyzQuadrangleIndices) {
      this.xyzQuadrangleIndices = owner.xyzQuadrangleIndices.getValue();
      this.quadrangleIndexBuffer = null;
      markGeometryAsChanged();
    } else if (property == owner.ijkQuadrangleIndices) {
      this.ijkQuadrangleIndices = owner.ijkQuadrangleIndices.getValue();
      markGeometryAsChanged();
    } else if (property == owner.uvQuadrangleIndices) {
      this.uvQuadrangleIndices = owner.uvQuadrangleIndices.getValue();
      markGeometryAsChanged();
    } else {
      super.propertyChanged(property);
    }
  }

  @Override
  public Point3 getIntersectionInSource(Ray ray, Matrix4x4 m, int subElement) {
    return Point3.NaN;
  }

  private double[] xyzs;
  private float[] ijks;
  private float[] uvs;
  private short[] xyzTriangleIndices;
  private short[] ijkTriangleIndices;
  private short[] uvTriangleIndices;
  private short[] xyzQuadrangleIndices;
  private short[] ijkQuadrangleIndices;
  private short[] uvQuadrangleIndices;

  private DoubleBuffer xyzBuffer;
  private FloatBuffer ijkBuffer;
  private FloatBuffer uvBuffer;
  private ShortBuffer triangleIndexBuffer;
  private ShortBuffer quadrangleIndexBuffer;
}
