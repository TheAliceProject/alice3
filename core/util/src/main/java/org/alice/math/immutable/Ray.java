package org.alice.math.immutable;

public record Ray(Point3 origin, Vector3 direction) {
  public static final Ray NaN = new Ray(Point3.NaN, Vector3.NaN);

  public static Ray fromAtoB(Point3 start, Point3 end) {
    Vector3 direction = end.minus(start).normalized();
    return new Ray(start, direction);
  }

  public boolean isNaN() {
    return origin.isNaN() || direction.isNaN();
  }

  public Point3 getPointAlong(double t) {
    return origin.plus(direction.times(t));
  }

  public double getProjectedPointT(Point3 p) {
    return p.minus(origin).dotProduct(direction);
  }

  public Point3 getProjectedPoint(Point3 p) {
    return getPointAlong(getProjectedPointT(p));
  }

  public Ray normalized() {
    return direction.isNormalized() ? this : new Ray(origin, direction.normalized());
  }
}
