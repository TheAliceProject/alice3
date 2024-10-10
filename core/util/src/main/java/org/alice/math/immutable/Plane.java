package org.alice.math.immutable;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;

public record Plane(double a, double b, double c, double d) {
    public static final Plane NaN = new Plane(Double.NaN, Double.NaN, Double.NaN, Double.NaN);
    public static final Plane XZ_PLANE = new Plane(0, 1, 0, 0);

    //Kept private to avoid confusion on order
    private static Plane createInstance(double xPosition, double yPosition, double zPosition, double xNormal, double yNormal, double zNormal) {
      final double EPSILON = 0.01;
      double magnitudeSquared = Vector3.magnitudeSquared(xNormal, yNormal, zNormal);
      if (!EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace(magnitudeSquared, EPSILON)) {
        double magnitude = Math.sqrt(magnitudeSquared);
        Logger.severe(magnitude, xNormal, yNormal, zNormal);
        xNormal /= magnitude;
        yNormal /= magnitude;
        zNormal /= magnitude;
      }
      return new Plane(xNormal, yNormal, zNormal, -((xNormal * xPosition) + (yNormal * yPosition) + (zNormal * zPosition)));
    }

    public static Plane createInstance(Point3 position, Vector3 normal) {
      return createInstance(position.x(), position.y(), position.z(), normal.x(), normal.y(), normal.z());
    }

    public static Plane createInstance(AffineMatrix4x4 m) {
      return createInstance(m.translation().x(), m.translation().y(), m.translation().z(), -m.orientation().backward().x(), -m.orientation().backward().y(), -m.orientation().backward().z());
    }

    public boolean isNaN() {
      return Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(d);
    }

    public double[] getEquation(double[] rv) {
      rv[0] = a;
      rv[1] = b;
      rv[2] = c;
      rv[3] = d;
      return rv;
    }

    public double[] getEquation() {
      return getEquation(new double[4]);
    }

    public double intersect(Ray ray) {
      Vector3 direction = ray.direction();
      double denominator = (a * direction.x()) + (b * direction.y()) + (c * direction.z());
      return denominator == 0 ? Double.NaN : - evaluate(ray.origin()) / denominator;
    }

    public double evaluate(Point3 p) {
      return (a * p.x()) + (b * p.y()) + (c * p.z()) + d;
    }
}
