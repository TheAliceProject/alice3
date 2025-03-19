package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public record Vector3f(float x, float y, float z) implements Tuple3f {
    public static final Vector3f ZERO = new Vector3f(0, 0, 0);
    public static final Vector3f NaN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

    //<editor-fold desc="Static Constructors">
    public static Vector3f createNormalized(float x, float y, float z) {
        double magnitudeSquared = magnitudeSquared(x, y, z);
        if (magnitudeSquared == 0.0) {
            // Cannot normalize a zero vector
            return Vector3f.NaN;
        }
        if (magnitudeSquared == 1.0) {
            // Already normal
            return new Vector3f(x, y, z);
        }
        float magnitude = (float) Math.sqrt(magnitudeSquared);
        return new Vector3f(x / magnitude, y / magnitude, z / magnitude);
    }
    //</editor-fold>

    //<editor-fold desc="Condition Checks">
    public boolean isNormalized() {
        return isWithinEpsilonOfNormalized(EpsilonUtilities.REASONABLE_EPSILON_FLOAT);
    }

    boolean isWithinEpsilonOfNormalized(float epsilon) {
        final double magSquare = magnitudeSquared();
        final double min = 1.0 - epsilon;
        final double max = 1.0 + epsilon;
        return ((min * min) < magSquare) && (magSquare < (max * max));
    }
    //</editor-fold>

    //<editor-fold desc="Matrix Operations">
    // Operations create new records.
    public Vector3f plus(Vector3f b) {
        return new Vector3f(x + b.x, y + b.y, z + b.z);
    }

    public Vector3f minus(Vector3f b) {
        return new Vector3f(x - b.x, y - b.y, z - b.z);
    }

    public Vector3f times(Vector3f b) {
        return new Vector3f(x * b.x, y * b.y, z * b.z);
    }

    public Vector3f times(float factor) {
        return new Vector3f(factor * x, factor * y, factor * z);
    }

    public Vector3f dividedBy(float divisor) {
        return new Vector3f(x / divisor, y / divisor, z / divisor);
    }

    public Vector3f negate() {
        return new Vector3f(-x, -y, -z);
    }

    public double dotProduct(Tuple3f b) {
        return (x * b.x()) + (y * b.y()) + (z * b.z());
    }

    public Vector3f crossProduct(Vector3f b) {
        return new Vector3f((y * b.z) - (z * b.y), (b.x * z) - (b.z * x), (x * b.y) - (y * b.x));
    }

    public Vector3f interpolate(Vector3f b, float portion) {
        return new Vector3f(x + ((b.x - x) * portion),
                y + ((b.y - y) * portion),
                z + ((b.z - z) * portion));
    }
    //</editor-fold>

    //<editor-fold desc="Magnitude">
    public double magnitudeSquared() {
        return magnitudeSquared(x, y, z);
    }

    public double magnitude() {
        double magnitudeSquared = magnitudeSquared();
        return magnitudeSquared == 1.0 ? 1.0 : Math.sqrt(magnitudeSquared);
    }

    private static double magnitudeSquared(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    public Vector3f normalized() {
        double magnitudeSquared = magnitudeSquared();
        return magnitudeSquared == 1.0 ? this : dividedBy((float) Math.sqrt(magnitudeSquared));
    }
    //</editor-fold>

    public void encode(BinaryEncoder binaryEncoder) {
        binaryEncoder.encodeRecord(this);
    }
}

