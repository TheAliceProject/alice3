package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

import java.io.Serializable;

public record AxisAlignedBox(Point3 minimum, Point3 maximum) implements BinaryEncodableAndDecodable, Serializable {
  public static AxisAlignedBox NaN = new AxisAlignedBox(Point3.NaN, Point3.NaN);
  // TODO Make min very high and max very low so all points get added?
  public static AxisAlignedBox Empty = new AxisAlignedBox(Point3.ORIGIN, Point3.ORIGIN);

  public static AxisAlignedBox createAxisAlignedBox(double minimumX, double minimumY, double minimumZ, double maximumX, double maximumY, double maximumZ) {
    return new AxisAlignedBox(new Point3(minimumX, minimumY, minimumZ), new Point3(maximumX, maximumY, maximumZ));
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encodeRecord(this);
  }

  public boolean isNaN() {
    return minimum.isNaN() || maximum.isNaN();
  }

  public double getXMinimum() {
    return minimum.x();
  }

  public double getYMinimum() {
    return minimum.y();
  }

  public double getZMinimum() {
    return minimum.z();
  }

  public double getXMaximum() {
    return maximum.x();
  }

  public double getYMaximum() {
    return maximum.y();
  }

  public double getZMaximum() {
    return maximum.z();
  }

  public Point3 getCenter() {
    return minimum.interpolate(maximum, 0.5);
  }

  public Point3 getCenterOfFrontFace() {
    return new Point3((minimum.x() + maximum.x()) / 2, (minimum.y() + maximum.y()) / 2, (minimum.z()));
  }

  public Point3 getCenterOfBackFace() {
    return new Point3((minimum.x() + maximum.x()) / 2, (minimum.y() + maximum.y()) / 2, maximum.z());
  }

  public Point3 getCenterOfLeftFace() {
    return new Point3((minimum.x()), (minimum.y() + maximum.y()) / 2, (minimum.z() + maximum.z()) / 2);
  }

  public Point3 getCenterOfRightFace() {
    return new Point3((maximum.x()), (minimum.y() + maximum.y()) / 2, (minimum.z() + maximum.z()) / 2);
  }

  public Point3 getCenterOfTopFace() {
    return new Point3((minimum.x() + maximum.x()) / 2, (maximum.y()), (minimum.z() + maximum.z()) / 2);
  }

  public Point3 getCenterOfBottomFace() {
    return new Point3((minimum.x() + maximum.x()) / 2, (minimum.y()), (minimum.z() + maximum.z()) / 2);
  }

  public double getWidth() {
    return maximum.x() - minimum.x();
  }

  public double getHeight() {
    return maximum.y() - minimum.y();
  }

  public double getDepth() {
    return maximum.z() - minimum.z();
  }

  public Dimension3 getSize() {
    return new Dimension3(getWidth(), getHeight(), getDepth());
  }

  public double getVolume() {
    return getWidth() * getHeight() * getDepth();
  }

  public double getDiagonal() {
    if (isNaN()) {
      return Double.NaN;
    }
    return minimum.distanceFrom(maximum);
  }

  public AxisAlignedBox union(Point3 p) {
    if (p.isNaN()) {
      // Ignore invalid points
      return this;
    }
    if (isNaN()) {
      return new AxisAlignedBox(p, p);
    }
    if (contains(p)) {
      return this;
    }
    Point3 min = new Point3(Math.min(minimum.x(), p.x()), Math.min(minimum.y(), p.y()), Math.min(minimum.z(), p.z()));
    Point3 max = new Point3(Math.max(maximum.x(), p.x()), Math.max(maximum.y(), p.y()), Math.max(maximum.z(), p.z()));
    return new AxisAlignedBox(min, max);
  }

  public boolean contains(Point3 p) {
    return !isNaN()
        && minimum.x() <= p.x() && minimum.y() <= p.y() && minimum.z() <= p.z()
        && maximum.x() >= p.x() && maximum.y() >= p.y() && maximum.z() >= p.z();
  }

  public AxisAlignedBox union(AxisAlignedBox other) {
    if (other == null || other.isNaN()) {
      return this;
    }
    if (isNaN()) {
      return other;
    }
    Point3 min = new Point3(Math.min(minimum.x(), other.minimum.x()), Math.min(minimum.y(), other.minimum.y()), Math.min(minimum.z(), other.minimum.z()));
    Point3 max = new Point3(Math.max(maximum.x(), other.maximum.x()), Math.max(maximum.y(), other.maximum.y()), Math.max(maximum.z(), other.maximum.z()));
    return new AxisAlignedBox(min, max);
  }

  public Point3[] getPoints() {
    return new Point3[]{
        new Point3(minimum.x(), minimum.y(), minimum.z()),
        new Point3(maximum.x(), minimum.y(), minimum.z()),
        new Point3(minimum.x(), maximum.y(), minimum.z()),
        new Point3(maximum.x(), maximum.y(), minimum.z()),
        new Point3(minimum.x(), minimum.y(), maximum.z()),
        new Point3(maximum.x(), minimum.y(), maximum.z()),
        new Point3(minimum.x(), maximum.y(), maximum.z()),
        new Point3(maximum.x(), maximum.y(), maximum.z())};
  }

  public Vector4[] getVectors() {
    return new Vector4[]{
        new Vector4(minimum.x(), minimum.y(), minimum.z(), 1),
        new Vector4(maximum.x(), minimum.y(), minimum.z(), 1),
        new Vector4(minimum.x(), maximum.y(), minimum.z(), 1),
        new Vector4(maximum.x(), maximum.y(), minimum.z(), 1),
        new Vector4(minimum.x(), minimum.y(), maximum.z(), 1),
        new Vector4(maximum.x(), minimum.y(), maximum.z(), 1),
        new Vector4(minimum.x(), maximum.y(), maximum.z(), 1),
        new Vector4(maximum.x(), maximum.y(), maximum.z(), 1)};
  }

  public AxisAlignedBox translate(Vector3 v) {
    return new AxisAlignedBox(minimum.plus(v), maximum.plus(v));
  }

  public AxisAlignedBox scale(double scale) {
    return new AxisAlignedBox(minimum.times(scale), maximum.times(scale));
  }

  // Used to scale, but could apply any transform
  public AxisAlignedBox scale(Matrix3x3 m) {
    return new AxisAlignedBox(m.transform(minimum), m.transform(maximum));
  }
}
