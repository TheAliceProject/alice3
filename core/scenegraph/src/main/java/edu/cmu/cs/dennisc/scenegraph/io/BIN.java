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
package edu.cmu.cs.dennisc.scenegraph.io;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.texture.TextureCoordinate2f;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Dennis Cosgrove
 */
public class BIN {
  public static Vertex[] loadVertices(InputStream is) throws IOException {
    Vertex[] vertices = null;
    BufferedInputStream bis = new BufferedInputStream(is);
    DataInputStream dis = new DataInputStream(bis);
    int version = dis.readInt();
    if (version == 1) {
      int vertexCount = dis.readInt();
      vertices = new Vertex[vertexCount];
      for (int index = 0; index < vertices.length; index++) {
        double x = dis.readDouble();
        double y = dis.readDouble();
        double z = dis.readDouble();
        float i = (float) dis.readDouble();
        float j = (float) dis.readDouble();
        float k = (float) dis.readDouble();
        float u = (float) dis.readDouble();
        float v = (float) dis.readDouble();
        vertices[index] = Vertex.createXYZIJKUV(x, y, z, i, j, k, u, v);
      }
    } else if (version == 2) {
      int vertexCount = dis.readInt();
      vertices = new Vertex[vertexCount];
      for (int index = 0; index < vertices.length; index++) {
        int format = dis.readInt();
        final Point3 position = Point3.createNaN();
        if ((format & Vertex.FORMAT_POSITION) != 0) {
          position.x = dis.readDouble();
          position.y = dis.readDouble();
          position.z = dis.readDouble();
        }
        final Vector3f normal = Vector3f.createNaN();
        if ((format & Vertex.FORMAT_NORMAL) != 0) {
          normal.x = (float) dis.readDouble();
          normal.y = (float) dis.readDouble();
          normal.z = (float) dis.readDouble();
        }
        final Color4f diffuseColor;
        if ((format & Vertex.FORMAT_DIFFUSE_COLOR) != 0) {
          float red = (float) dis.readDouble();
          float green = (float) dis.readDouble();
          float blue = (float) dis.readDouble();
          float alpha = (float) dis.readDouble();
          diffuseColor = new Color4f(red, green, blue, alpha);
        } else {
          diffuseColor = null;
        }
        if ((format & Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0) {
        }
        final TextureCoordinate2f textureCoordinate0;
        if ((format & Vertex.FORMAT_TEXTURE_COORDINATE_0) != 0) {
          float u = (float) dis.readDouble();
          float v = (float) dis.readDouble();
          textureCoordinate0 = new TextureCoordinate2f(u, v);
        } else {
          textureCoordinate0 = null;
        }
        vertices[index] = new Vertex(position, normal, diffuseColor, null, textureCoordinate0);
      }
    } else if (version == 3) {
      int vertexCount = dis.readInt();
      vertices = new Vertex[vertexCount];
      for (int index = 0; index < vertices.length; index++) {
        int format = dis.readInt();
        final Point3 position = Point3.createNaN();
        if ((format & Vertex.FORMAT_POSITION) != 0) {
          position.x = dis.readDouble();
          position.y = dis.readDouble();
          position.z = dis.readDouble();
        }
        final Vector3f normal = Vector3f.createNaN();
        if ((format & Vertex.FORMAT_NORMAL) != 0) {
          normal.x = (float) dis.readDouble();
          normal.y = (float) dis.readDouble();
          normal.z = (float) dis.readDouble();
        }
        final Color4f diffuseColor;
        if ((format & Vertex.FORMAT_DIFFUSE_COLOR) != 0) {
          float red = dis.readFloat();
          float green = dis.readFloat();
          float blue = dis.readFloat();
          float alpha = dis.readFloat();
          diffuseColor = new Color4f(red, green, blue, alpha);
        } else {
          diffuseColor = null;
        }
        final Color4f specularHighlightColor;
        if ((format & Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0) {
          float red = dis.readFloat();
          float green = dis.readFloat();
          float blue = dis.readFloat();
          float alpha = dis.readFloat();
          specularHighlightColor = new Color4f(red, green, blue, alpha);
        } else {
          specularHighlightColor = null;
        }
        final TextureCoordinate2f textureCoordinate0;
        if ((format & Vertex.FORMAT_TEXTURE_COORDINATE_0) != 0) {
          float u = dis.readFloat();
          float v = dis.readFloat();
          textureCoordinate0 = new TextureCoordinate2f(u, v);
        } else {
          textureCoordinate0 = null;
        }
        vertices[index] = new Vertex(position, normal, diffuseColor, specularHighlightColor, textureCoordinate0);
      }
    } else {
      throw new RuntimeException("invalid file version: " + version);
    }
    return vertices;
  }

  public static void storeVertices(Vertex[] vertices, OutputStream os) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(os);
    DataOutputStream dos = new DataOutputStream(bos);
    dos.writeInt(3);
    dos.writeInt(vertices.length);
    for (Vertex vertice : vertices) {
      int format = vertice.getFormat();
      dos.writeInt(format);
      if ((format & Vertex.FORMAT_POSITION) != 0) {
        dos.writeDouble(vertice.position.x);
        dos.writeDouble(vertice.position.y);
        dos.writeDouble(vertice.position.z);
      }
      if ((format & Vertex.FORMAT_NORMAL) != 0) {
        dos.writeDouble(vertice.normal.x);
        dos.writeDouble(vertice.normal.y);
        dos.writeDouble(vertice.normal.z);
      }
      if ((format & Vertex.FORMAT_DIFFUSE_COLOR) != 0) {
        dos.writeFloat(vertice.diffuseColor.red);
        dos.writeFloat(vertice.diffuseColor.green);
        dos.writeFloat(vertice.diffuseColor.blue);
        dos.writeFloat(vertice.diffuseColor.alpha);
      }
      if ((format & Vertex.FORMAT_SPECULAR_HIGHLIGHT_COLOR) != 0) {
        dos.writeFloat(vertice.specularHighlightColor.red);
        dos.writeFloat(vertice.specularHighlightColor.green);
        dos.writeFloat(vertice.specularHighlightColor.blue);
        dos.writeFloat(vertice.specularHighlightColor.alpha);
      }
      if ((format & Vertex.FORMAT_TEXTURE_COORDINATE_0) != 0) {
        dos.writeFloat(vertice.textureCoordinate0.u);
        dos.writeFloat(vertice.textureCoordinate0.v);
      }
    }
    dos.flush();
  }

  public static int[] loadTriangleData(InputStream is) throws IOException {
    int[] indices = null;
    BufferedInputStream bis = new BufferedInputStream(is);
    DataInputStream dis = new DataInputStream(bis);
    int version = dis.readInt();
    if (version == 1) {
      int faceCount = dis.readInt();
      int verticesPerFace = dis.readInt();
      indices = new int[faceCount * verticesPerFace];
      for (int i = 0; i < indices.length; i++) {
        indices[i] = dis.readInt();
      }
    } else if (version == 2) {
      int indicesCount = dis.readInt();
      indices = new int[indicesCount];
      for (int i = 0; i < indices.length; i++) {
        indices[i] = dis.readInt();
      }
    } else {
      throw new RuntimeException("invalid file version: " + version);
    }
    return indices;
  }

  public static void storeTriangleData(int[] indices, OutputStream os) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(os);
    DataOutputStream dos = new DataOutputStream(bos);
    dos.writeInt(2);
    dos.writeInt(indices.length);
    for (int indice : indices) {
      dos.writeInt(indice);
    }
    dos.flush();
  }
}
