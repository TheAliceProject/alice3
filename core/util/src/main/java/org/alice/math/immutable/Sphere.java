package org.alice.math.immutable;

public record Sphere(Point3 center, double radius) {

  public double intersect(Ray ray) {
    Vector3 vOriginToCenter = ray.origin().minus(center);
    double a = ray.direction().dotProduct(ray.direction());
    double b = 2 * ray.direction().dotProduct(vOriginToCenter);
    double c = vOriginToCenter.dotProduct(vOriginToCenter) - (this.radius * this.radius);

    double discriminant = (b * b) - (4 * a * c);

    if (discriminant < 0) {
      // No real solutions
      return Double.NaN;
    }
    double sqrtDiscriminant = Math.sqrt(discriminant);

    //account for b close to sqrt discriminant
    double q = (b < 0) ? (-b - sqrtDiscriminant) / 2.0 : (-b + sqrtDiscriminant) / 2.0;
    double tSolution0 = q / a;
    double tSolution1 = c / q;

    double tMin;
    double tMax;
    if (tSolution0 < tSolution1) {
      tMin = tSolution0;
      tMax = tSolution1;
    } else {
      tMin = tSolution1;
      tMax = tSolution0;
    }

    if (tMax < 0) {
      return Double.NaN;
    } else {
      if (tMin < 0) {
        return tMax;
      } else {
        return tMin;
      }
    }
  }
}
