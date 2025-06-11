package org.alice.math.immutable;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public record Vector3(double x, double y, double z) implements Tuple3 {
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 NaN = new Vector3(Double.NaN, Double.NaN, Double.NaN);
    public static final Vector3 POSITIVE_X_AXIS = new Vector3(1, 0, 0);
    public static final Vector3 POSITIVE_Y_AXIS = new Vector3(0, 1, 0);
    public static final Vector3 POSITIVE_Z_AXIS = new Vector3(0, 0, 1);
    public static final Vector3 NEGATIVE_X_AXIS = new Vector3(-1, 0, 0);
    public static final Vector3 NEGATIVE_Y_AXIS = new Vector3(0, -1, 0);
    public static final Vector3 NEGATIVE_Z_AXIS = new Vector3(0, 0, -1);

    // Operations. These create new records.
    public Vector3 plus(Vector3 b) {
        return new Vector3(x + b.x, y + b.y, z + b.z);
    }

    public Vector3 minus(Vector3 b) {
        return new Vector3(x - b.x, y - b.y, z - b.z);
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

    public Angle angleWith(Vector3 b) {
        double dot = this.dotProduct(b);
        if (dot < -1.0d) {
            return Angle.PI;
        }
        if (dot > 1.0d) {
            return Angle.ZERO;
        }
        return new AngleInRadians(Math.acos(dot));
    }

    //Magnitude
    public double magnitudeSquared() {
        return magnitudeSquared(x, y, z);
    }

    public double magnitude() {
        return magnitude(x, y, z);
    }

    //Normalize
    public static Vector3 createNormalized(double x, double y, double z) {
        double magnitudeSquared = magnitudeSquared(x, y, z);
        if (magnitudeSquared == 0.0) {
            // Cannot normalize a zero vector
            return Vector3.NaN;
        }
        if (magnitudeSquared == 1.0) {
            // Already normal
            return new Vector3(x, y, z);
        }
        double magnitude = Math.sqrt(magnitudeSquared);
        return new Vector3(x / magnitude, y / magnitude, z / magnitude);
    }

    public Vector3 normalized() {
        double magnitudeSquared = magnitudeSquared();
        return magnitudeSquared == 1.0 ? this : dividedBy(Math.sqrt(magnitudeSquared));
    }

    public boolean isNormalized() {
        return isWithinEpsilonOfNormalized(EpsilonUtilities.REASONABLE_EPSILON);
    }

    boolean isWithinEpsilonOfNormalized(double epsilon) {
        final double magSquare = magnitudeSquared();
        final double min = 1.0 - epsilon;
        final double max = 1.0 + epsilon;
        return ((min * min) < magSquare) && (magSquare < (max * max));
    }

    //Magnitude
    public static double magnitudeSquared(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    public static double magnitude(double x, double y, double z) {
        double magnitudeSquared = magnitudeSquared(x, y, z);
        return magnitudeSquared == 1.0 ? 1.0 : Math.sqrt(magnitudeSquared);
    }

    public boolean isOrthogonalTo(Vector3 other) {
        // This epsilon provides wiggle enough for our models where some error can accumulate in processing.
        return EpsilonUtilities.isWithinEpsilon(0.0, dotProduct(other), 0.005);
    }

    public OrthogonalMatrix3x3 asScaleMatrix() {
        return new OrthogonalMatrix3x3(
            new Vector3(x, 0, 0),
            new Vector3(0, y, 0),
            new Vector3(0, 0, z));
    }

    //<editor-fold desc="Conversions">
    public Point3 asPoint() {
        return new Point3(x, y, z);
    }

    public Vector3 withX(double newX) {
        return new Vector3(newX, y, z);
    }

    public Vector3 withY(double newY) {
        return new Vector3(x, newY, z);
    }

    public Vector3 withZ(double newZ) {
        return new Vector3(x, y, newZ);
    }

    // Projects this vector onto the target
    public Vector3 projectedOnto(Vector3 target) {
        Vector3 unitTarget = target.normalized();
        return unitTarget.times(this.dotProduct(unitTarget));
    }
    //</editor-fold>
}

