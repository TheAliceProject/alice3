package org.lgna.story.resourceutilities;

import com.jogamp.common.nio.Buffers;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.DoubleBufferProperty;
import edu.cmu.cs.dennisc.property.FloatBufferProperty;

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
		return orient4().multiply(matrix).multiply(inverse4);
	}

	private AffineMatrix4x4 orient4() {
		return new AffineMatrix4x4(orient3, Point3.ORIGIN);
	}

	double[] orientVertices(float[] sourceVertices, DoubleBufferProperty destination) {
		double[] transformedVertices = new double[sourceVertices.length];
		for (int i=0; i<sourceVertices.length; i += 3) {
			orient3.transformVector(transformedVertices, i, sourceVertices, i);
		}
		destination.setValue(Buffers.newDirectDoubleBuffer(transformedVertices) );
		return transformedVertices;
	}

	void orientNormals(float[] normalData, FloatBufferProperty destination) {
		float[] transformedNormals = new float[normalData.length];
		for (int i=0; i<normalData.length; i += 3) {
			orient3.transformVector(transformedNormals, i, normalData, i);
		}
		destination.setValue( Buffers.newDirectFloatBuffer(transformedNormals) );
	}

	private static final OrthogonalMatrix3x3 ABOUT_FACE_AROUND_Y = new OrthogonalMatrix3x3(
		new Vector3(-1, 0, 0),
		new Vector3(0, 1, 0),
		new Vector3(0, 0, -1));

	private static final OrthogonalMatrix3x3 QUARTER_TURN_AROUND_Z = new OrthogonalMatrix3x3(
		new Vector3(0,1,0),
		new Vector3(-1,0,0),
		new Vector3(0,0,1));

	private static final OrthogonalMatrix3x3 QUARTER_TURN_AROUND_X = new OrthogonalMatrix3x3(
		new Vector3(1,0,0),
		new Vector3(0,0,-1),
		new Vector3(0,1,0));

	private final OrthogonalMatrix3x3 orient3;
	private final AffineMatrix4x4 inverse4;
}
