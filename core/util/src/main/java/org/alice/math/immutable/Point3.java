package org.alice.math.immutable;

import java.io.Serializable;

public record Point3(double x, double y, double z) implements Serializable, Tuple3 {
    public static Point3 ORIGIN = new Point3(0.0, 0.0, 0.0);
    public static Point3 NaN = new Point3(Double.NaN, Double.NaN, Double.NaN);

    //Operations
    public Point3 plus(Tuple3 b) {
        return new Point3(x + b.x(), y + b.y(),z + b.z());
    }
    public Vector3 minus(Tuple3 b) {
        return new Vector3(x - b.x(), y - b.y(),z - b.z());
    }

    public Point3 times(double factor) {
        return new Point3(x * factor, y * factor, z * factor);
    }

    public Point3 interpolate(Point3 b, double portion) {
        return new Point3(x + ((b.x - x) * portion),
                          y + ((b.y - y) * portion),
                          z + ((b.z - z) * portion));
    }

    // Temporary use during transition to immutable Records
    @Deprecated(forRemoval = true)
    public edu.cmu.cs.dennisc.math.Point3 mutable() {
        return new edu.cmu.cs.dennisc.math.Point3(x, y, z);
    }
}
