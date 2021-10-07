package edu.cmu.cs.dennisc.math;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ConvexPolygonTest {
  ConvexPolygon poly;

  Point2 p1 = new Point2(1, 1);
  Point2 p2 = new Point2(1, -2);
  Point2 p3 = new Point2(-1, 1);
  Point2 p4 = new Point2(0, 1.4);
  Point2 p5 = new Point2(-1, -1.4);

  Point2 p6 = new Point2(0.5839488784380005, 0.480680149401);
  Point2 p7 = new Point2(0.11895594339800031, -0.835438350387);
  Point2 p8 = new Point2(-0.5839488784379996, -0.480680149401);
  Point2 p9 = new Point2(0.0945380387160002, 0.11388972930899999);
  Point2 p10 = new Point2(-0.11895594339799986, 0.835438350387);
  Point2 p11 = new Point2(-0.0945380387160002, -0.11388972930899999);
  
  @Before
  public void setUp() {
    poly = new ConvexPolygon();
  }

  @Test
  public void includeSinglePoint() {
    poly.includePoint(new Point2(1,1));
    assertEquals(1, poly.getVertices().size());
  }

  @Test
  public void duplicatePointsShouldNotBeIncluded() {
    poly.includePoint(new Point2(1,1));
    poly.includePoint(new Point2(1,2));
    poly.includePoint(new Point2(1,1));
    assertEquals(2, poly.getVertices().size());
  }

  @Test
  public void clockwisePointsShouldBeClockwise() {
    poly.includePoint(p1);
    poly.includePoint(p2);
    poly.includePoint(p3);
    List<Point2> vertices = poly.getVertices();
    assertEquals(3, vertices.size());
    assertEquals(vertices.get(0), p1);
    assertEquals(vertices.get(1), p2);
    assertEquals(vertices.get(2), p3);
  }

  @Test
  public void counterclockwisePointsShouldBeClockwise() {
    poly.includePoint(p1);
    poly.includePoint(p3);
    poly.includePoint(p2);
    List<Point2> vertices = poly.getVertices();
    assertEquals(3, vertices.size());
    assertEquals(vertices.get(0), p1);
    assertEquals(vertices.get(1), p2);
    assertEquals(vertices.get(2), p3);
  }

  @Test
  public void interiorPointsShouldNotBeAdded() {
    poly.includePoint(p1);
    poly.includePoint(p2);
    poly.includePoint(p3);
    assertEquals(3, poly.getVertices().size());
    poly.includePoint(Point2.createZero());
    assertEquals(3, poly.getVertices().size());
  }

  @Test
  public void laterInsertionOrderShouldNotMatter() {
    poly.includePoint(p1);
    poly.includePoint(p2);
    poly.includePoint(p3);
    poly.includePoint(p4);
    poly.includePoint(p5);
    ConvexPolygon poly2 = new ConvexPolygon();
    poly2.includePoint(p1);
    poly2.includePoint(p2);
    poly2.includePoint(p5);
    poly2.includePoint(p3);
    poly2.includePoint(p4);
    assertTrue(haveMatchingVertices(poly, poly2));
  }

  @Test
  public void insertionOrderShouldNotMatter() {
    poly.includePoint(p6);
    poly.includePoint(p8);
    poly.includePoint(p7);
    poly.includePoint(p9);
    poly.includePoint(p10);
    poly.includePoint(p11);
    ConvexPolygon poly2 = new ConvexPolygon();
    poly2.includePoint(p8);
    poly2.includePoint(p11);
    poly2.includePoint(p7);
    poly2.includePoint(p6);
    poly2.includePoint(p9);
    poly2.includePoint(p10);
    assertTrue(haveMatchingVertices(poly, poly2));
  }

  @Test
  public void insertionCycleOrderShouldNotMatterForCycle() {
    poly.includePoint(p1);
    poly.includePoint(p2);
    poly.includePoint(p3);
    poly.includePoint(p4);
    poly.includePoint(p5);
    ConvexPolygon poly2 = new ConvexPolygon();
    poly2.includePoint(p5);
    poly2.includePoint(p1);
    poly2.includePoint(p2);
    poly2.includePoint(p3);
    poly2.includePoint(p4);
    assertTrue(haveMatchingVertices(poly, poly2));
  }

  @Test
  public void insertionMixedOrderShouldNotMatterForCycle() {
    poly.includePoint(p1);
    poly.includePoint(p2);
    poly.includePoint(p3);
    poly.includePoint(p4);
    poly.includePoint(p5);
    ConvexPolygon poly2 = new ConvexPolygon();
    poly2.includePoint(p3);
    poly2.includePoint(p5);
    poly2.includePoint(p2);
    poly2.includePoint(p1);
    poly2.includePoint(p4);
    assertTrue(haveMatchingVertices(poly, poly2));
  }

  private boolean haveMatchingVertices(ConvexPolygon poly, ConvexPolygon poly2) {
    List<Point2> verts1 = poly.getVertices();
    List<Point2> verts2 = poly2.getVertices();
    if (verts1.isEmpty()) {
       return  verts2.isEmpty();
    }
    if (verts1.size() != verts2.size()) {
      return false;
    }
    int offset = verts2.indexOf(verts1.get(0));
    for (int i = 0; i < verts1.size(); i++) {
      if (!verts2.get((i + offset) % verts2.size()).equals(verts1.get(i))) {
        return false;
      }
    }
    return true;
  }
}