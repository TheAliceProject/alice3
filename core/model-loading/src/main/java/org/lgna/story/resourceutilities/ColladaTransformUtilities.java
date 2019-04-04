package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

import java.util.Arrays;

/**
 *   Alice models are in a different geometric space than Maya models
 *  Maya models are modeled with:
 *    forward = +z
 *    right   = -x
 *    up      = +y
 *
 *  Alice models are modeled with:
 *    forward = -z
 *    right   = +x
 *    up      = +y
 */
public class ColladaTransformUtilities {

  public static AffineMatrix4x4 createFlippedAffineTransform(AffineMatrix4x4 transform) {
    AffineMatrix4x4 flippedTransform = new AffineMatrix4x4(transform);
    flippedTransform.orientation.right.y *= -1;

    flippedTransform.orientation.up.x *= -1;
    flippedTransform.orientation.up.z *= -1;

    flippedTransform.orientation.backward.y *= -1;

    flippedTransform.translation.x *= -1;
    flippedTransform.translation.z *= -1;

    return flippedTransform;
  }

  public static double[] createFlippedRowMajorTransform(double transform[]) {
    double[] flippedTransform = Arrays.copyOf(transform, transform.length);

    flippedTransform[1] *= -1; //up.x
    flippedTransform[3] *= -1; //translation.x

    flippedTransform[4] *= -1; //right.y
    flippedTransform[6] *= -1; //backward.y

    flippedTransform[9] *= -1; //up.z
    flippedTransform[11] *= -1; //translation.z

    return flippedTransform;
  }

  public static double[] createFlippedPoint3DoubleArray(double[] point3Values) {
    double[] flippedValues = new double[point3Values.length];
    for (int i = 0; i < point3Values.length; i += 3) {
      flippedValues[i] = point3Values[i] * -1;
      flippedValues[i + 1] = point3Values[i + 1];
      flippedValues[i + 2] = point3Values[i + 2] * -1;
    }
    return flippedValues;
  }

  public static float[] createFlippedPoint3FloatArray(float[] point3Values) {
    float[] flippedValues = new float[point3Values.length];
    for (int i = 0; i < point3Values.length; i += 3) {
      flippedValues[i] = point3Values[i] * -1;
      flippedValues[i + 1] = point3Values[i + 1];
      flippedValues[i + 2] = point3Values[i + 2] * -1;
    }
    return flippedValues;
  }

}
