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

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3f;
import edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Vector;

/**
 * @author Dennis Cosgrove
 */
public class OBJ {
  private static double getNextNumber(StreamTokenizer streamTokenizer) {
    try {
      streamTokenizer.nextToken();
      if (streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
        double f = streamTokenizer.nval;
        streamTokenizer.nextToken();
        if (streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
          if (streamTokenizer.sval.startsWith("E")) {
            int exponent = Integer.parseInt(streamTokenizer.sval.substring(1));
            return f * Math.pow(10, exponent);
          }
        }
        streamTokenizer.pushBack();
        return f;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return Double.NaN;
  }

  public static IndexedTriangleArray decode(InputStream is) throws IOException {
    BufferedReader r = new BufferedReader(new InputStreamReader(is));
    StreamTokenizer st = new StreamTokenizer(r);
    st.commentChar('#');
    st.slashSlashComments(false);
    st.slashStarComments(false);
    st.whitespaceChars('/', '/');
    st.parseNumbers();
    Vector<double[]> xyzs = new Vector<double[]>();
    Vector<double[]> ijks = new Vector<double[]>();
    Vector<double[]> uvs = new Vector<double[]>();
    Vector<Vector<Integer>> fs = new Vector<Vector<Integer>>();
    while (st.nextToken() == StreamTokenizer.TT_WORD) {
      if (st.sval.startsWith("vt")) {
        double uv[] = new double[3];
        uv[0] = getNextNumber(st);
        uv[1] = getNextNumber(st);
        uvs.addElement(uv);
      } else if (st.sval.startsWith("vn")) {
        double ijk[] = new double[3];
        ijk[0] = getNextNumber(st);
        ijk[1] = getNextNumber(st);
        ijk[2] = getNextNumber(st);
        ijks.addElement(ijk);
      } else if (st.sval.startsWith("v")) {
        double xyz[] = new double[3];
        xyz[0] = getNextNumber(st);
        xyz[1] = getNextNumber(st);
        xyz[2] = getNextNumber(st);
        xyzs.addElement(xyz);
      } else if (st.sval.startsWith("f")) {
        Vector<Integer> f = new Vector<Integer>();
        while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
          int index = (int) st.nval;
          if (index < 0) {
            //todo: account for different lengthed ijks and uvs
            index += xyzs.size();
          } else {
            index -= 1;
          }
          f.addElement(index);
        }
        st.pushBack();
        fs.addElement(f);
      } else {
        break;
      }
    }
    int nVertexCount = xyzs.size();
    Vertex[] vertices = new Vertex[nVertexCount];
    double ijkDefault[] = new double[3];
    ijkDefault[0] = 0;
    ijkDefault[1] = 1;
    ijkDefault[2] = 0;
    double uvDefault[] = new double[2];
    uvDefault[0] = 0;
    uvDefault[1] = 0;
    for (int v = 0; v < nVertexCount; v++) {
      double xyz[] = xyzs.elementAt(v);
      double ijk[];
      double uv[];
      try {
        ijk = ijks.elementAt(v);
      } catch (ArrayIndexOutOfBoundsException e) {
        ijk = ijkDefault;
      }
      try {
        uv = uvs.elementAt(v);
      } catch (ArrayIndexOutOfBoundsException e) {
        uv = uvDefault;
      }
      vertices[v] = Vertex.createXYZIJKUV(xyz[0], xyz[1], xyz[2], (float) ijk[0], (float) ijk[1], (float) ijk[2], (float) uv[0], (float) uv[1]);
    }
    //todo
    int[] indices = new int[fs.size() * 3];
    int i = 0;
    for (int f = 0; f < fs.size(); f++) {
      Vector<Integer> face = fs.elementAt(f);
      switch (face.size()) {
      case 3:
        indices[i++] = face.elementAt(0).intValue();
        indices[i++] = face.elementAt(1).intValue();
        indices[i++] = face.elementAt(2).intValue();
        break;
      case 6:
        indices[i++] = face.elementAt(0).intValue();
        indices[i++] = face.elementAt(2).intValue();
        indices[i++] = face.elementAt(4).intValue();
        break;
      case 9:
        indices[i++] = face.elementAt(0).intValue();
        indices[i++] = face.elementAt(3).intValue();
        indices[i++] = face.elementAt(6).intValue();
        break;
      default:
        throw new RuntimeException("unhandled face index size");
      }
    }
    IndexedTriangleArray ita = new IndexedTriangleArray();
    ita.vertices.setValue(vertices);
    ita.polygonData.setValue(indices);

    for (int index : indices) {
      assert index >= 0;
      assert index < vertices.length;
    }

    return ita;
  }

  public static void encode(OutputStream os, IndexedTriangleArray ita, AffineMatrix4x4 m, String groupName) {
    Vertex[] vertices = ita.vertices.getValue();
    int[] indices = ita.polygonData.getValueAsArray();
    if ((vertices != null) && (indices != null)) {
      BufferedOutputStream bos = new BufferedOutputStream(os);
      PrintWriter pw = new PrintWriter(bos);
      if (groupName != null) {
        pw.println("g " + groupName);
      }
      Point3 p = new Point3();
      Vector3f n = new Vector3f();
      for (Vertex vertice : vertices) {
        p.set(vertice.position);
        n.set(vertice.normal);
        double u = vertice.textureCoordinate0.u;
        double v = vertice.textureCoordinate0.v;
        if (m != null) {
          m.transform(p);
          m.transform(n);
        }
        pw.print("v ");
        pw.print(p.x);
        pw.print(" ");
        pw.print(p.y);
        pw.print(" ");
        pw.print(p.z);
        pw.println();
        pw.print("vt ");
        pw.print(u);
        pw.print(" ");
        pw.print(v);
        pw.println();
        pw.print("vn ");
        pw.print(n.x);
        pw.print(" ");
        pw.print(n.y);
        pw.print(" ");
        pw.print(n.z);
        pw.println();
      }
      for (int i = 0; i < indices.length; i += 3) {
        pw.print("f ");
        for (int j = 0; j < 3; j++) {
          int a = indices[i + j] - vertices.length;
          pw.print(a + "/" + a + "/" + a + " ");
        }
        pw.println();
      }
      pw.flush();
    }
  }
}
