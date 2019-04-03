import org.alice.apis.moveandturn.gallery.environments.Ground;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;

import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.IndexedTriangleArray;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.Vertex;

public class ExportGround {
  private static boolean isSharingVerticesNecessary(edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices) {
    final int N = vertices.length;
    for (int i = 0; i < N; i++) {
      edu.cmu.cs.dennisc.scenegraph.Vertex vI = vertices[i];
      for (int j = i + 1; j < N; j++) {
        edu.cmu.cs.dennisc.scenegraph.Vertex vJ = vertices[j];
        if (vI.equals(vJ)) {
          return true;
        }
      }
    }
    return false;
  }

  private static IndexedTriangleArray shareVertices(IndexedTriangleArray rv) {
    edu.cmu.cs.dennisc.scenegraph.Vertex[] vertices = rv.vertices.getValue();
    if (isSharingVerticesNecessary(vertices)) {
      java.util.Map<Integer, Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
      java.util.List<edu.cmu.cs.dennisc.scenegraph.Vertex> sharedVertices = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
      final int N = vertices.length;
      for (int i = 0; i < N; i++) {
        if (map.keySet().contains(i)) {
          //pass
        } else {
          edu.cmu.cs.dennisc.scenegraph.Vertex vI = vertices[i];
          //assert vI.equals( vI );
          int sharedIndex = sharedVertices.size();
          sharedVertices.add(vI);
          map.put(i, sharedIndex);
          for (int j = i + 1; j < N; j++) {
            edu.cmu.cs.dennisc.scenegraph.Vertex vJ = vertices[j];
            if (vI.equals(vJ)) {
              map.put(j, sharedIndex);
            }
          }
        }
      }

      //edu.cmu.cs.dennisc.print.PrintUtilities.println( "sharing", rv.getName(), vertices.length, "--->", sharedVertices.size() );
      rv.vertices.setValue(edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray(sharedVertices, edu.cmu.cs.dennisc.scenegraph.Vertex.class));
      int[] array = rv.polygonData.getValueAsArray();
      for (int i = 0; i < array.length; i++) {
        array[i] = map.get(array[i]);
      }

    }
    return rv;
  }

  private static boolean isRemovingExcessTrianglesNecessary(IndexedTriangleArray ita) {
    int[] polygonData = ita.polygonData.getValueAsArray();
    final int N = polygonData.length;
    for (int i = 0; i < N; i += 3) {
      int a1 = polygonData[i + 0];
      int b1 = polygonData[i + 1];
      int c1 = polygonData[i + 2];
      if (a1 == b1) {
        return true;
      }
      if (a1 == c1) {
        return true;
      }
      if (b1 == c1) {
        return true;
      }
      for (int j = i + 3; j < N; j += 3) {
        int a2 = polygonData[j + 0];
        int b2 = polygonData[j + 1];
        int c2 = polygonData[j + 2];
        if (a1 != a2 || b1 != b2 || c1 != c2) {
          //pass
        } else {
          return true;
        }
      }
    }
    return false;
  }

  private static IndexedTriangleArray removeExcessTriangles(IndexedTriangleArray rv) {
    if (isRemovingExcessTrianglesNecessary(rv)) {
      class Triangle {
        private int a;
        private int b;
        private int c;
        private boolean isToBeIncluded;

        public Triangle(int a, int b, int c) {
          this.a = a;
          this.b = b;
          this.c = c;
          this.isToBeIncluded = this.isLineOrPoint() == false;
          if (this.isToBeIncluded) {
            assert this.a != this.b;
            assert this.a != this.c;
            assert this.b != this.c;
          }
        }

        private boolean isLineOrPoint() {
          if (this.a == this.b) {
            return true;
          }
          if (this.a == this.c) {
            return true;
          }
          if (this.b == this.c) {
            return true;
          }
          return false;
        }

        @Override
        public boolean equals(Object o) {
          if (this == o) {
            return true;
          } else {
            if (o instanceof Triangle) {
              Triangle other = (Triangle) o;
              return this.a == other.a && this.b == other.b && this.c == other.c;
            } else {
              return false;
            }
          }
        }

        @Override
        public String toString() {
          return "Triangle[" + this.a + "," + this.b + "," + this.c + "]";
        }
      }
      Vertex[] vertices = rv.vertices.getValue();

      int[] polygonData = rv.polygonData.getValueAsArray();

      java.util.ArrayList<Triangle> triangles = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
      final int N_POLYGON_DATA = polygonData.length;
      for (int i = 0; i < N_POLYGON_DATA; i += 3) {
        int a = polygonData[i + 0];
        int b = polygonData[i + 1];
        int c = polygonData[i + 2];
        assert a < vertices.length;
        assert b < vertices.length;
        assert c < vertices.length;
        triangles.add(new Triangle(a, b, c));
      }

      final int N_TRIANGLES = triangles.size();
      for (int i = 0; i < N_TRIANGLES; i++) {
        Triangle triangleI = triangles.get(i);
        if (triangleI.isToBeIncluded) {
          for (int j = i + 1; j < N_TRIANGLES; j++) {
            Triangle triangleJ = triangles.get(j);
            if (triangleI.equals(triangleJ)) {
              triangleJ.isToBeIncluded = false;
            }
          }
        }
      }

      //      java.util.ListIterator< Triangle > triangleIterator = triangles.listIterator();
      //      while( triangleIterator.hasNext() ) {
      //        Triangle triangle = triangleIterator.next();
      //        if( triangle.isToBeIncluded ) {
      //          //pass
      //        } else {
      //          edu.cmu.cs.dennisc.print.PrintUtilities.println( "removing triangle", triangle );
      //          triangleIterator.remove();
      //        }
      //      }
      //
      //      int[] trimmedPolygonData = new int[ triangles.size()*3 ];
      //      int i = 0;
      //      for( Triangle triangle : triangles ) {
      //        polygonData[ i++ ] = triangle.a;
      //        polygonData[ i++ ] = triangle.b;
      //        polygonData[ i++ ] = triangle.c;
      //      }
      //      rv.polygonData.setValue( trimmedPolygonData );

      java.util.LinkedList<Integer> trimmedPolygonData = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
      for (Triangle triangle : triangles) {
        if (triangle.isToBeIncluded) {
          trimmedPolygonData.add(triangle.a);
          trimmedPolygonData.add(triangle.b);
          trimmedPolygonData.add(triangle.c);
          assert triangle.a < vertices.length;
          assert triangle.b < vertices.length;
          assert triangle.c < vertices.length;
        } else {
          System.err.println("removing triangle");
        }
      }
      rv.polygonData.setValue(edu.cmu.cs.dennisc.java.util.CollectionUtilities.createIntArray(trimmedPolygonData));
    }
    return rv;
  }

  private static IndexedTriangleArray removeUnreferencedVertices(IndexedTriangleArray rv) {
    Vertex[] vertices = rv.vertices.getValue();
    final int N = vertices.length;
    boolean[] isReferencedArray = new boolean[N];
    int[] polygonData = rv.polygonData.getValueAsArray();

    for (int i : polygonData) {
      isReferencedArray[i] = true;
    }

    boolean isRequiringTrimming = false;
    for (boolean isReferenced : isReferencedArray) {
      if (isReferenced) {
        //pass
      } else {
        isRequiringTrimming = true;
        break;
      }
    }

    if (isRequiringTrimming) {
      java.util.List<Vertex> trimmedVertices = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
      java.util.Map<Integer, Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
      for (int i = 0; i < N; i++) {
        if (isReferencedArray[i]) {
          map.put(i, trimmedVertices.size());
          trimmedVertices.add(vertices[i]);
        }
      }

      //not necessary at the moment, but might as well create new array
      int[] reassignedPolygonData = new int[polygonData.length];
      for (int i = 0; i < polygonData.length; i++) {
        reassignedPolygonData[i] = map.get(polygonData[i]);
      }

      rv.vertices.setValue(edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray(trimmedVertices, Vertex.class));
      rv.polygonData.setValue(reassignedPolygonData);
    }
    return rv;
  }

  private static Mesh meshify(IndexedTriangleArray ita) {
    Vertex[] vertices = ita.vertices.getValue();
    final int N = vertices.length;
    double[] xyzs = new double[N * 3];
    float[] ijks = new float[N * 3];
    float[] uvs = new float[N * 2];

    for (int i = 0; i < N; i++) {
      Vertex v = vertices[i];
      xyzs[i * 3 + 0] = v.position.x;
      xyzs[i * 3 + 1] = v.position.y;
      xyzs[i * 3 + 2] = v.position.z;
      ijks[i * 3 + 0] = v.normal.x;
      ijks[i * 3 + 1] = v.normal.y;
      ijks[i * 3 + 2] = v.normal.z;
      uvs[i * 2 + 0] = v.textureCoordinate0.u;
      uvs[i * 2 + 1] = v.textureCoordinate0.v;
    }

    int[] polygonData = ita.polygonData.getValueAsArray();
    final int M = polygonData.length;
    short[] xyzTriangleIndices = new short[M];
    for (int i = 0; i < M; i++) {
      assert polygonData[i] <= Short.MAX_VALUE;
      xyzTriangleIndices[i] = (short) polygonData[i];
    }

    for (int i = 0; i < M; i += 3) {
      short a1 = xyzTriangleIndices[i + 0];
      short b1 = xyzTriangleIndices[i + 1];
      short c1 = xyzTriangleIndices[i + 2];
      assert a1 != b1 : a1 + " " + b1;
      assert a1 != c1 : a1 + " " + c1;
      assert a1 < N;
      assert b1 < N;
      assert c1 < N;
      for (int j = i + 3; j < M; j += 3) {
        short a2 = xyzTriangleIndices[j + 0];
        short b2 = xyzTriangleIndices[j + 1];
        short c2 = xyzTriangleIndices[j + 2];
        assert a1 != a2 || b1 != b2 || c1 != c2 : "(" + a1 + " " + b1 + " " + c1 + ") (" + a2 + " " + b2 + " " + c2 + ")";
      }
    }

    Mesh rv = new Mesh();
    rv.xyzs.setValue(xyzs);
    rv.ijks.setValue(ijks);
    rv.uvs.setValue(uvs);
    rv.xyzTriangleIndices.setValue(xyzTriangleIndices);
    return rv;
  }

  public static void main(String[] args) {
    GrassyGround ground = new GrassyGround();
    IndexedTriangleArray sgITA = (IndexedTriangleArray) ground.getSGVisual().getGeometry();
    sgITA = shareVertices(sgITA);
    sgITA = removeExcessTriangles(sgITA);
    java.text.NumberFormat format = new java.text.DecimalFormat("0.0000");
    int i = 0;
    for (Vertex v : sgITA.vertices.getValue()) {
      StringBuilder sb = new StringBuilder();
      sb.append("createXYZIJKUV( ");
      //      sb.append( "VERTICES[" );
      //      sb.append( i++ );
      //      sb.append( "] = createXYZIJKUV( " );
      sb.append(format.format(v.position.x));
      sb.append(",y,");
      sb.append(format.format(v.position.z));
      sb.append(",i,j,k,");
      sb.append(format.format(v.textureCoordinate0.u));
      sb.append("f,");
      sb.append(format.format(v.textureCoordinate0.v));
      sb.append("f),");
      PrintUtilities.println(sb.toString());
    }
    StringBuilder sb = new StringBuilder();
    for (int index : sgITA.polygonData.getValueAsArray()) {
      sb.append(index);
      sb.append(", ");
    }
    PrintUtilities.println(sb.toString());
  }
}
