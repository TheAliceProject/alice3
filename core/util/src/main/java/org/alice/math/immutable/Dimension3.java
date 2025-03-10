package org.alice.math.immutable;

import java.io.Serializable;

public record Dimension3(double x, double y, double z) implements Serializable, Tuple3 {
    public static final Dimension3 UNIT_SIZE = new Dimension3(1, 1, 1);
    public static Dimension3 TOO_SMALL = new Dimension3(0.0, 0.0, 0.0);
    public static Dimension3 NaN = new Dimension3(Double.NaN, Double.NaN, Double.NaN);

    public static Dimension3 uniformScale(double factor) {
        return new Dimension3(factor, factor, factor);
    }

    //Operations
    public Dimension3 times(double factor) {
        return new Dimension3(x * factor, y * factor, z * factor);
    }

    public Dimension3 times(Dimension3 b) {
        return new Dimension3(x * b.x(), y * b.y(), z * b.z());
    }

    public Dimension3 dividedBy(Dimension3 b) {
        return new Dimension3(x / b.x(), y / b.y(), z / b.z());
    }

    public Dimension3 interpolate(Dimension3 b, double portion) {
        return new Dimension3(x + ((b.x - x) * portion),
                          y + ((b.y - y) * portion),
                          z + ((b.z - z) * portion));
    }

    public OrthogonalMatrix3x3 asScaleMatrix() {
        return new OrthogonalMatrix3x3(
            new Vector3(x, 0, 0),
            new Vector3(0, y, 0),
            new Vector3(0, 0, z));
    }

    // Temporary use during transition to immutable Records

    public Vector3 asVector() {
        return new Vector3(x, y, z);
    }

    public Dimension3 withSafeNumbers() {
        if (this.isSafe()) {
            return this;
        }
        return new Dimension3(
            Double.isFinite(x) ? this.x : 1.0,
            Double.isFinite(y) ? this.y : 1.0,
            Double.isFinite(z) ? this.z : 1.0);
    }

    private boolean isSafe() {
        return Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z);
    }

    public boolean hasNegativeComponents() {
        return x < 0.0 || y < 0.0 || z < 0.0;
    }
}
