package org.alice.math.immutable;

import java.io.Serializable;

public record Vector3(double x, double y, double z) implements Serializable, Tuple3 {
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 NaN = new Vector3(Double.NaN, Double.NaN, Double.NaN);
    public static final Vector3 POSITIVE_X_AXIS = new Vector3(1, 0, 0);
    public static final Vector3 POSITIVE_Y_AXIS = new Vector3(0, 1, 0);
    public static final Vector3 POSITIVE_Z_AXIS = new Vector3(0, 0, 1);

    // Operations. These create new records.
    public Vector3 plus(Vector3 b) {
        return new Vector3(x + b.x, y + b.y, z + b.z);
    }

    public Vector3 minus(Vector3 b) {
        return new Vector3(x - b.x, y - b.y, z - b.z);
    }

    public Vector3 times(Vector3 b) {
        return new Vector3(x * b.x, y * b.y, z * b.z);
    }

    public Vector3 times(double factor) {
        return new Vector3(factor * x, factor * y, factor * z);
    }

    public Vector3 dividedBy(double divisor) {
        return new Vector3(x / divisor, y / divisor, z / divisor);
    }

    public Vector3 negate() {
        return new Vector3(-x, -y, -z);
    }

    public double dotProduct(Tuple3 b) {
        return (x * b.x()) + (y * b.y()) + (z * b.z());
    }

    public Vector3 crossProduct(Vector3 b) {
        return new Vector3((y * b.z) - (z * b.y), (b.x * z) - (b.z * x), (x * b.y) - (y * b.x));
    }

    public Vector3 interpolate(Vector3 b, double portion) {
        return new Vector3(x + ((b.x - x) * portion),
                y + ((b.y - y) * portion),
                z + ((b.z - z) * portion));
    }

    //Magnitude
    public double magnitudeSquared() {
        return magnitudeSquared(x, y, z);
    }

    public double magnitude() {
        return magnitude(x, y, z);
    }

    //Normalize
    public Vector3 normalized() {
        double magnitudeSquared = magnitudeSquared();
        return magnitudeSquared == 1.0 ? this : dividedBy(Math.sqrt(magnitudeSquared));
    }

    public boolean isNormalized() {
        return magnitudeSquared() == 1.0;
    }

    // Temporary use during transition to immutable Records
    @Deprecated(forRemoval = true)
    public edu.cmu.cs.dennisc.math.Vector3 mutable() {
        return new edu.cmu.cs.dennisc.math.Vector3(x, y, z);
    }

    // Temporary use during transition to immutable Records
    @Deprecated(forRemoval = true)
    public edu.cmu.cs.dennisc.math.Point3 mutablePoint() {
        return new edu.cmu.cs.dennisc.math.Point3(x, y, z);
    }

    //Magnitude
    public static double magnitudeSquared(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    public static double magnitude(double x, double y, double z) {
        double magnitudeSquared = magnitudeSquared(x, y, z);
        return magnitudeSquared == 1.0 ? 1.0 : Math.sqrt(magnitudeSquared);
    }
}

