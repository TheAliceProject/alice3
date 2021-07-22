package edu.cmu.cs.dennisc.math;


import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.NaN;
import static java.lang.Math.sqrt;

public class ConvexPolygon {
  private final List<Point2> vertices = new ArrayList<>();

  // If not inside the polygon make it a vertex and remove any existing vertices that would prevent convexity
  // Maintain points in clockwise order.
  public void includePoint(Point2 p) {
    if (vertices.contains(p)) {
      return;
    }
    if (vertices.size() < 2) {
      vertices.add(p);
      return;
    }
    addPoint(p);
  }

  private void addPoint(Point2 p) {
    boolean lastEdgeIncluded = false;
    Point2 startOfEdgesToRemove = null;
    Point2 endOfEdgesToRemove = null;
    List<Point2> verticesToRemove = new ArrayList<>();

    int vertexCount = vertices.size();
    for (int i = 0; i < vertexCount; i++) {
      Point2 pointA = vertices.get(i);
      Point2 pointB = vertices.get((i + 1) % vertexCount);
      if (isLeftOfEdge(pointA, pointB, p)) {
        if (!lastEdgeIncluded) {
          startOfEdgesToRemove = pointA;
        }
        lastEdgeIncluded = true;
        if (endOfEdgesToRemove != null) {
          verticesToRemove.add(endOfEdgesToRemove);
        }
        endOfEdgesToRemove = pointB;
      } else {
        lastEdgeIncluded = false;
      }
    }
    if (vertices.get(0).equals(startOfEdgesToRemove) && lastEdgeIncluded) {
      verticesToRemove.add(endOfEdgesToRemove);
    }
    for (Point2 vert : verticesToRemove) {
      vertices.remove(vert);
    }
    if (startOfEdgesToRemove != null) {
      addVertexAfter(p, startOfEdgesToRemove);
    }
  }

  private void addVertexAfter(Point2 addition, Point2 existing) {
    int index = vertices.indexOf(existing) + 1;
    if (index == vertices.size()) {
      vertices.add(addition);
    } else {
      vertices.add(index, addition);
    }
  }

  public double distanceAlong(double x, double y) {
    if (isInside(new Point2(x, y))) {
      return sqrt(x * x + y * y);
    }
    int vertexCount = vertices.size();
    for (int i = 0; i < vertexCount; i++) {
      Point2 pointA = vertices.get(i);
      Point2 pointB = vertices.get((i + 1) % vertexCount);
      Point2 intersection = intersectionWithRay(pointA.x, pointA.y, pointB.x, pointB.y, x, y);
      if (intersection != null) {
        return intersection.calculateMagnitude();
      }
    }
    // No edge found
    return NaN;
  }

  private boolean isInside(Point2 p) {
    int vertexCount = vertices.size();
    for (int i = 0; i < vertexCount; i++) {
      Point2 pointA = vertices.get(i);
      Point2 pointB = vertices.get((i + 1) % vertexCount);
      if (isLeftOfEdge(pointA, pointB, p)) {
        return false;
      }
    }
    return true;
  }

  private boolean isLeftOfEdge(Point2 start, Point2 end, Point2 point) {
    return (point.y - start.y) * (end.x - start.x) > (point.x - start.x) * (end.y - start.y);
  }

  // Guided by segment intersection in https://en.wikipedia.org/wiki/Line–line_intersection
  // Second segment is ray starting at origin, allowing simplifications.
  private static Point2 intersectionWithRay(double x1, double y1,
                                            double x2, double y2,
                                            double x4, double y4) {
    double denominator = (x2 - x1) * y4 - (y2 - y1) * x4;
    double tNumerator = -x1 * y4 + y1 * x4;
    // Check t is between 0 & 1 without doing division. Implies intersection is on the segment
    if (tNumerator < denominator && (tNumerator * denominator) > 0) {
      double uNumerator = (x2 - x1) * y1 - (y2 - y1) * x1;
      // Same 0-1 test for u and the ray. Implies intersection is on the ray
      if (uNumerator < denominator && (uNumerator * denominator) > 0) {
        double xNumerator = (y1 * x2 - x1 * y2) * x4;
        double yNumerator = (y1 * x2 - x1 * y2) * y4;
        return new Point2(xNumerator / denominator, yNumerator / denominator);
      }
    }
    return null;
  }

  // For unit tests
  protected List<Point2> getVertices() {
    return vertices;
  }
}
