package org.alice.math.immutable;

public record Ray(Point3 origin, Vector3 direction) {
  public static final Ray NaN = new Ray(Point3.NaN, Vector3.NaN);

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

  // TODO move to matrix?
  public Ray transform(AffineMatrix4x4 m) {
    return new Ray(m.transform(origin), m.transform(direction));
  }

  public Ray normalized() {
    return direction.isNormalized() ? this : new Ray(origin, direction.normalized());
  }

  // Temporary use during transition to immutable Records
  @Deprecated(forRemoval = true)
  public edu.cmu.cs.dennisc.math.Ray mutable() {
    return new edu.cmu.cs.dennisc.math.Ray(origin.mutable(), direction.mutable());
  }
}
