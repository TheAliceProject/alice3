package org.lgna.story.resourceutilities;

import com.jogamp.common.nio.Buffers;
import edu.cmu.cs.dennisc.property.DoubleBufferProperty;
import edu.cmu.cs.dennisc.property.FloatBufferProperty;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.OrthogonalMatrix3x3;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Vector3;

public class Orientation {

  static Orientation forUpAxis(String up) {
    if ("X_UP".equals(up)) {
      return new Orientation(QUARTER_TURN_AROUND_Z);
    }
    if ("Z_UP".equals(up)) {
      return new Orientation(QUARTER_TURN_AROUND_X);
    }
    return new Orientation(ABOUT_FACE_AROUND_Y);
  }

  static Orientation forAlice() {
    return new Orientation(ABOUT_FACE_AROUND_Y);
  }

  private Orientation(OrthogonalMatrix3x3 orient3) {
    this.orient3 = orient3;
    inverse4 = orient4();
    inverse4.invert();
  }

  AffineMatrix4x4 orientMatrixToAlice(AffineMatrix4x4 matrix) {
    return orient4().times(matrix).times(inverse4);
  }

  private AffineMatrix4x4 orient4() {
    return new AffineMatrix4x4(orient3, Point3.ORIGIN);
  }

  double[] orientVertices(float[] sourceVertices, DoubleBufferProperty destination) {
    double[] transformedVertices = new double[sourceVertices.length];
    for (int i = 0; i < sourceVertices.length; i += 3) {
      orient3.transformVector(transformedVertices, i, sourceVertices, i);
    }
    destination.setValue(Buffers.newDirectDoubleBuffer(transformedVertices));
    return transformedVertices;
  }

  void orientNormals(float[] normalData, FloatBufferProperty destination) {
    float[] transformedNormals = new float[normalData.length];
    for (int i = 0; i < normalData.length; i += 3) {
      orient3.transformVector(transformedNormals, i, normalData, i);
    }
    destination.setValue(Buffers.newDirectFloatBuffer(transformedNormals));
  }

  private static final OrthogonalMatrix3x3 ABOUT_FACE_AROUND_Y =
      new OrthogonalMatrix3x3(Vector3.NEGATIVE_X_AXIS, Vector3.POSITIVE_Y_AXIS, Vector3.NEGATIVE_Z_AXIS);

  private static final OrthogonalMatrix3x3 QUARTER_TURN_AROUND_Z =
      new OrthogonalMatrix3x3(Vector3.POSITIVE_Y_AXIS, Vector3.NEGATIVE_X_AXIS, Vector3.POSITIVE_Z_AXIS);

  private static final OrthogonalMatrix3x3 QUARTER_TURN_AROUND_X =
      new OrthogonalMatrix3x3(Vector3.POSITIVE_X_AXIS, Vector3.NEGATIVE_Z_AXIS, Vector3.POSITIVE_Y_AXIS);

  private final OrthogonalMatrix3x3 orient3;
  private final AffineMatrix4x4 inverse4;
}
