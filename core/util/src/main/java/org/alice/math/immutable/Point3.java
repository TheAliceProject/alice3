package org.alice.math.immutable;

public record Point3(double x, double y, double z) implements Tuple3 {
    public static Point3 ORIGIN = new Point3(0.0, 0.0, 0.0);
    public static Point3 NaN = new Point3(Double.NaN, Double.NaN, Double.NaN);

    //Operations
    public Point3 plus(Vector3 b) {
        return new Point3(x + b.x(), y + b.y(), z + b.z());
    }

    // The difference between two points is a vector
    public Vector3 minus(Point3 b) {
        return new Vector3(x - b.x(), y - b.y(), z - b.z());
    }

    // Applying a vector to a point produces a new point
    public Point3 minus(Vector3 b) {
        return new Point3(x - b.x(), y - b.y(), z - b.z());
    }

    public Point3 times(double factor) {
        return new Point3(x * factor, y * factor, z * factor);
    }

    public Point3 interpolate(Point3 b, double portion) {
        return new Point3(x + ((b.x - x) * portion),
                          y + ((b.y - y) * portion),
                          z + ((b.z - z) * portion));
    }

    // Point-Vector conversions should be avoided and may indicate a problem.
    public Vector3 asVector() {
        return new Vector3(x, y, z);
    }

    public Point3 withX(double newX) {
        return new Point3(newX, y, z);
    }

    public Point3 withY(double newY) {
        return new Point3(x, newY, z);
    }

    public Point3 withZ(double newZ) {
        return new Point3(x, y, newZ);
    }
}
