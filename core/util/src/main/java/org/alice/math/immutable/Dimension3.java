package org.alice.math.immutable;

public record Dimension3(double x, double y, double z) implements Tuple3 {
    //<editor-fold desc="Constants">
    public static final Dimension3 UNIT_SIZE = new Dimension3(1, 1, 1);
    public static Dimension3 NaN = new Dimension3(Double.NaN, Double.NaN, Double.NaN);
    //</editor-fold>

    //<editor-fold desc="Static Constructor">
    public static Dimension3 uniformScale(double factor) {
        return new Dimension3(factor, factor, factor);
    }
    //</editor-fold>

    //<editor-fold desc="Operations">
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

    public Dimension3 withSafeNumbers() {
        if (this.isSafe()) {
            return this;
        }
        return new Dimension3(
            Double.isFinite(x) ? this.x : 1.0,
            Double.isFinite(y) ? this.y : 1.0,
            Double.isFinite(z) ? this.z : 1.0);
    }
    //</editor-fold>

    //<editor-fold desc="Conversion">
    public OrthogonalMatrix3x3 asScaleMatrix() {
        return new OrthogonalMatrix3x3(
            new Vector3(x, 0, 0),
            new Vector3(0, y, 0),
            new Vector3(0, 0, z));
    }
    //</editor-fold>

    //<editor-fold desc="Flags">
    private boolean isSafe() {
        return Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z);
    }

    public boolean hasNegativeComponents() {
        return x < 0.0 || y < 0.0 || z < 0.0;
    }
    //</editor-fold>

    //<editor-fold desc="Scale Application">
    public Point3 applyScale(Point3 p) {
        return new Point3(x * p.x(), y * p.y(), z * p.z());
    }

    public Point3 removeScale(Point3 p) {
        return new Point3(p.x() / x, p.y() / y, p.z() / z);
    }
    //</editor-fold>
}
