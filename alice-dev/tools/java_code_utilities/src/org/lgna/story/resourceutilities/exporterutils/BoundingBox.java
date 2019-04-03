/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lgna.story.resourceutilities.exporterutils;

import edu.cmu.cs.dennisc.math.Point3;

/**
 *
 * @author alice
 */
public class BoundingBox {

  protected Point3 m_minimum = new Point3();
  protected Point3 m_maximum = new Point3();

  public BoundingBox() {
    setNaN();
  }

  public BoundingBox(Point3 minimum, Point3 maximum) {
    setMinimum(minimum);
    setMaximum(maximum);
  }

  public BoundingBox(double minimumX, double minimumY, double minimumZ, double maximumX, double maximumY, double maximumZ) {
    setMinimum(minimumX, minimumY, minimumZ);
    setMaximum(maximumX, maximumY, maximumZ);
  }

  public BoundingBox(BoundingBox other) {
    set(other);
  }

  public String getCodeString() {
    return "BoundingBox(" + m_minimum.x + ", " + m_minimum.y + ", " + m_minimum.z + ", " + m_maximum.x + ", " + m_maximum.y + ", " + m_maximum.z + ");";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o != null && o instanceof BoundingBox) {
      BoundingBox box = (BoundingBox) o;
      return m_minimum.equals(box.m_minimum) && m_maximum.equals(box.m_maximum);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int rv = 17;
    if (this.m_minimum != null) {
      rv = 37 * rv + this.m_minimum.hashCode();
    }
    if (this.m_maximum != null) {
      rv = 37 * rv + this.m_maximum.hashCode();
    }
    return rv;
  }

  public void set(BoundingBox other) {
    if (other != null) {
      m_minimum.set(other.m_minimum);
      m_maximum.set(other.m_maximum);
    } else {
      setNaN();
    }
  }

  public boolean isNaN() {
    return m_minimum.isNaN() || m_maximum.isNaN();
  }

  public void setNaN() {
    m_minimum.set(Double.NaN, Double.NaN, Double.NaN);
    m_maximum.set(Double.NaN, Double.NaN, Double.NaN);
  }

  public Point3 getMinimum(Point3 rv) {
    rv.set(m_minimum);
    return rv;
  }

  public Point3 getMinimum() {
    return getMinimum(new Point3());
  }

  public void setMinimum(Point3 minimum) {
    if (minimum == null) {
      m_minimum.set(Double.NaN, Double.NaN, Double.NaN);
    } else {
      m_minimum.set(minimum);
    }
  }

  public void setMinimum(double x, double y, double z) {
    m_minimum.set(x, y, z);
  }

  public Point3 getMaximum(Point3 rv) {
    rv.set(m_maximum);
    return rv;
  }

  public Point3 getMaximum() {
    return getMaximum(new Point3());
  }

  public void setMaximum(Point3 maximum) {
    if (maximum == null) {
      m_maximum.set(Double.NaN, Double.NaN, Double.NaN);
    } else {
      m_maximum.set(maximum);
    }
  }

  public void setMaximum(double x, double y, double z) {
    m_maximum.set(x, y, z);
  }

  public double getXMinimum() {
    return m_minimum.x;
  }

  public void setXMinimum(double v) {
    m_minimum.x = v;
  }

  public double getYMinimum() {
    return m_minimum.y;
  }

  public void setYMinimum(double v) {
    m_minimum.y = v;
  }

  public double getZMinimum() {
    return m_minimum.z;
  }

  public void setZMinimum(double v) {
    m_minimum.z = v;
  }

  public double getXMaximum() {
    return m_maximum.x;
  }

  public void setXMaximum(double v) {
    m_maximum.x = v;
  }

  public double getYMaximum() {
    return m_maximum.y;
  }

  public void setYMaximum(double v) {
    m_maximum.y = v;
  }

  public double getZMaximum() {
    return m_maximum.z;
  }

  public void setZMaximum(double v) {
    m_maximum.z = v;
  }

  public Point3 getCenter(Point3 rv) {
    rv.set((m_minimum.x + m_maximum.x) / 2, (m_minimum.y + m_maximum.y) / 2, (m_minimum.z + m_maximum.z) / 2);
    return rv;
  }

  public Point3 getCenter() {
    return getCenter(new Point3());
  }

  public Point3 getCenterOfFrontFace(Point3 rv) {
    rv.set((m_minimum.x + m_maximum.x) / 2, (m_minimum.y + m_maximum.y) / 2, (m_minimum.z));
    return rv;
  }

  public Point3 getCenterOfFrontFace() {
    return getCenterOfFrontFace(new Point3());
  }

  public Point3 getCenterOfBackFace(Point3 rv) {
    rv.set((m_minimum.x + m_maximum.x) / 2, (m_minimum.y + m_maximum.y) / 2, (m_maximum.z));
    return rv;
  }

  public Point3 getCenterOfBackFace() {
    return getCenterOfBackFace(new Point3());
  }

  public Point3 getCenterOfLeftFace(Point3 rv) {
    rv.set((m_minimum.x), (m_minimum.y + m_maximum.y) / 2, (m_minimum.z + m_maximum.z) / 2);
    return rv;
  }

  public Point3 getCenterOfLeftFace() {
    return getCenterOfLeftFace(new Point3());
  }

  public Point3 getCenterOfRightFace(Point3 rv) {
    rv.set((m_maximum.x), (m_minimum.y + m_maximum.y) / 2, (m_minimum.z + m_maximum.z) / 2);
    return rv;
  }

  public Point3 getCenterOfRightFace() {
    return getCenterOfRightFace(new Point3());
  }

  public Point3 getCenterOfTopFace(Point3 rv) {
    rv.set((m_minimum.x + m_maximum.x) / 2, (m_maximum.y), (m_minimum.z + m_maximum.z) / 2);
    return rv;
  }

  public Point3 getCenterOfTopFace() {
    return getCenterOfTopFace(new Point3());
  }

  public Point3 getCenterOfBottomFace(Point3 rv) {
    rv.set((m_minimum.x + m_maximum.x) / 2, (m_minimum.y), (m_minimum.z + m_maximum.z) / 2);
    return rv;
  }

  public Point3 getCenterOfBottomFace() {
    return getCenterOfBottomFace(new Point3());
  }

  public double getWidth() {
    return m_maximum.x - m_minimum.x;
  }

  public double getHeight() {
    return m_maximum.y - m_minimum.y;
  }

  public double getDepth() {
    return m_maximum.z - m_minimum.z;
  }

  public double getVolume() {
    return getWidth() * getHeight() * getDepth();
  }

  public void union(double x, double y, double z) {
    if (m_minimum.isNaN()) {
      m_minimum.set(x, y, z);
    } else {
      m_minimum.x = Math.min(m_minimum.x, x);
      m_minimum.y = Math.min(m_minimum.y, y);
      m_minimum.z = Math.min(m_minimum.z, z);
    }
    if (m_maximum.isNaN()) {
      m_maximum.set(x, y, z);
    } else {
      m_maximum.x = Math.max(m_maximum.x, x);
      m_maximum.y = Math.max(m_maximum.y, y);
      m_maximum.z = Math.max(m_maximum.z, z);
    }
  }

  public void union(Point3 p) {
    union(p.x, p.y, p.z);
  }

  public void union(BoundingBox other) {
    assert other != null;
    if (isNaN()) {
      if (other.isNaN()) {
        //pass
      } else {
        set(other);
      }
    } else {
      if (other.isNaN()) {
        //pass
      } else {
        m_minimum.x = Math.min(m_minimum.x, other.m_minimum.x);
        m_minimum.y = Math.min(m_minimum.y, other.m_minimum.y);
        m_minimum.z = Math.min(m_minimum.z, other.m_minimum.z);
        m_maximum.x = Math.max(m_maximum.x, other.m_maximum.x);
        m_maximum.y = Math.max(m_maximum.y, other.m_maximum.y);
        m_maximum.z = Math.max(m_maximum.z, other.m_maximum.z);
      }
    }
  }

  @Override
  public String toString() {
    return BoundingBox.class.getName() + "[minimum=" + m_minimum + ",maximum=" + m_maximum + "]";
  }
}
