package org.lgna.story.resourceutilities;

import com.dddviewr.collada.visualscene.Matrix;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

import java.util.Arrays;

public class ColladaTransformUtilities {

    /**
     * 	Alice models are in a different geometric space than Maya/Collada models
     *	Maya/Collada models are modeled with:
     *	  forward = +z
     *	  right   = -x
     *	  up      = +y
     *
     *	Alice models are modeled with:
     *	  forward = -z
     *	  right   = +x
     *	  up      = +y
     */

    public static AffineMatrix4x4 createFlippedAliceTransform( AffineMatrix4x4 transform ) {
        AffineMatrix4x4 flippedTransform = new AffineMatrix4x4(transform);
        flippedTransform.orientation.right.y *= -1;

        flippedTransform.orientation.up.x *= -1;
        flippedTransform.orientation.up.z *= -1;

        flippedTransform.orientation.backward.y *= -1;

        flippedTransform.translation.x *= -1;
        flippedTransform.translation.z *= -1;

        return flippedTransform;
    }

    public static double[] createFlippedColladaTransform( double transform[] ) {
        double[] flippedTransform = Arrays.copyOf(transform, transform.length);
        double temp;

        //Flip values to convert to Collada space
        // Converting to Collada space:
        //		orientation.right.y *= -1;
        //		orientation.up.x *= -1;
        //		orientation.up.z *= -1;
        //		orientation.backward.y *= -1;
        //
        //		translation.x *= -1;
        //		translation.z *= -1;
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
        for (int i=0; i<point3Values.length; i+=3) {
            flippedValues[i] = point3Values[i] * -1;
            flippedValues[i+1] = point3Values[i+1];
            flippedValues[i+2] = point3Values[i+2] * -1;
        }
        return flippedValues;
    }

    public static float[] createFlippedPoint3FloatArray(float[] point3Values) {
        float[] flippedValues = new float[point3Values.length];
        for (int i=0; i<point3Values.length; i+=3) {
            flippedValues[i] = point3Values[i] * -1;
            flippedValues[i+1] = point3Values[i+1];
            flippedValues[i+2] = point3Values[i+2] * -1;
        }
        return flippedValues;
    }


    public static double[] createColladaTransformFromAliceTransform( AffineMatrix4x4 transform ) {
        double[] colladaMatrixValues = new double[16];

        //Pull the values from the Alice transform
        colladaMatrixValues[0] = transform.orientation.right.x;
        colladaMatrixValues[1] = transform.orientation.up.x;
        colladaMatrixValues[2] = transform.orientation.backward.x;
        colladaMatrixValues[3] = transform.translation.x;

        colladaMatrixValues[4] = transform.orientation.right.y;
        colladaMatrixValues[5] = transform.orientation.up.y;
        colladaMatrixValues[6] = transform.orientation.backward.y;
        colladaMatrixValues[7] = transform.translation.y;

        colladaMatrixValues[8] = transform.orientation.right.z;
        colladaMatrixValues[9] = transform.orientation.up.z;
        colladaMatrixValues[10] = transform.orientation.backward.z;
        colladaMatrixValues[11] = transform.translation.z;

        colladaMatrixValues[12] = 0;
        colladaMatrixValues[13] = 0;
        colladaMatrixValues[14] = 0;
        colladaMatrixValues[15] = 1;

        return colladaMatrixValues;
    }

    public static AffineMatrix4x4 createAliceTransformFromArray12( double[] matrixArray ) {
        assert matrixArray.length == 12;
        AffineMatrix4x4 rv = AffineMatrix4x4.createNaN();
        rv.orientation.right.x = matrixArray[ 0 ];
        rv.orientation.up.x = matrixArray[ 1 ];
        rv.orientation.backward.x = matrixArray[ 2 ];
        rv.translation.x = matrixArray[ 3 ];
        rv.orientation.right.y = matrixArray[ 4 ];
        rv.orientation.up.y = matrixArray[ 5 ];
        rv.orientation.backward.y = matrixArray[ 6 ];
        rv.translation.y = matrixArray[ 7 ];
        rv.orientation.right.z = matrixArray[ 8 ];
        rv.orientation.up.z = matrixArray[ 9 ];
        rv.orientation.backward.z = matrixArray[ 10 ];
        rv.translation.z = matrixArray[ 11 ];
        return rv;
    }

    public static AffineMatrix4x4 createAliceTransformFromArray16( double[] matrixArray ) {
        assert matrixArray.length == 16;
        AffineMatrix4x4 rv = AffineMatrix4x4.createNaN();
        rv.orientation.right.x = matrixArray[0];
        rv.orientation.up.x = matrixArray[1];
        rv.orientation.backward.x = matrixArray[2];
        rv.translation.x = matrixArray[3];
        rv.orientation.right.y = matrixArray[4];
        rv.orientation.up.y = matrixArray[5];
        rv.orientation.backward.y = matrixArray[6];
        rv.translation.y = matrixArray[7];
        rv.orientation.right.z = matrixArray[8];
        rv.orientation.up.z = matrixArray[9];
        rv.orientation.backward.z = matrixArray[10];
        rv.translation.z = matrixArray[11];
        return rv;
    }

}
