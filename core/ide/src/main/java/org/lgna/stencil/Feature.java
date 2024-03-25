/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.stencil;

import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.polynomial.BezierCubic;
import edu.cmu.cs.dennisc.math.polynomial.BezierQuadratic;
import edu.cmu.cs.dennisc.math.polynomial.Polynomial;
import org.lgna.croquet.Application;
import org.lgna.croquet.resolvers.RuntimeResolver;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.AwtContainerView;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;

import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

/**
 * @author Dennis Cosgrove
 */
public abstract class Feature {
  private static final int ARROW_HEAD_LENGTH = 19;
  private static final double ARROW_HEAD_LENGTH_SQUARED = ARROW_HEAD_LENGTH * ARROW_HEAD_LENGTH;
  private static final int ARROW_HEAD_HALF_HEIGHT = 6;

  public enum ConnectionPreference {
    NORTH_SOUTH, EAST_WEST
  }

  public enum Connection {
    NORTH(SwingConstants.CENTER, SwingConstants.LEADING, false), SOUTH(SwingConstants.CENTER, SwingConstants.TRAILING, false), EAST(SwingConstants.TRAILING, SwingConstants.CENTER, true), WEST(SwingConstants.LEADING, SwingConstants.CENTER, true);
    private int xConstraint;
    private int yConstraint;
    boolean isCurveDesired;

    private Connection(int xConstraint, int yConstraint, boolean isCurveDesired) {
      this.xConstraint = xConstraint;
      this.yConstraint = yConstraint;
      this.isCurveDesired = isCurveDesired;
    }

    public Point getPoint(Rectangle bounds) {
      Point rv = RectangleUtilities.getPoint(bounds, this.xConstraint, this.yConstraint);
      if (this.xConstraint == SwingConstants.CENTER) {
        rv.x = Math.min(rv.x, bounds.x + 128);
      }
      return rv;
    }

    public boolean isCurveDesired() {
      return this.isCurveDesired;
    }
  }

  private static final Stroke ARROW_STROKE = new BasicStroke(3.0f);

  private RuntimeResolver<? extends TrackableShape> trackableShapeResolver;
  private ConnectionPreference connectionPreference;
  private Integer heightConstraint = null;
  private boolean isEntered = false;

  public Feature(RuntimeResolver<? extends TrackableShape> trackableShapeResolver, ConnectionPreference connectionPreference) {
    //assert trackableShape != null;
    this.trackableShapeResolver = trackableShapeResolver;
    this.connectionPreference = connectionPreference;
  }

  private static final String IS_GOOD_TO_GO = null;

  public static boolean isGoodToGo(String status) {
    return status == IS_GOOD_TO_GO;
  }

  public String getStatus() {
    TrackableShape trackableShape = this.trackableShapeResolver.getResolved();
    if (trackableShape != null) {
      return IS_GOOD_TO_GO; //trackableShape.isInView();
    } else {
      //edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.trackableShapeResolver );
      return "cannot resolve " + this.trackableShapeResolver;
    }
  }

  protected abstract boolean isPathRenderingDesired();

  public Rectangle getBoundsForRepaint(AwtComponentView<?> asSeenBy) {
    TrackableShape trackableShape = this.getTrackableShape();
    if (trackableShape != null) {
      Insets boundsInsets = this.getBoundsInsets();
      if (boundsInsets != null) {
        Shape shape = trackableShape.getShape(asSeenBy, boundsInsets);
        if (shape != null) {
          return shape.getBounds();
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public void setHeightConstraint(Integer heightConstraint) {
    this.heightConstraint = heightConstraint;
  }

  private static void repaintAll() {
    Application.getActiveInstance().getDocumentFrame().getFrame().getContentPane().repaint();
    //edu.cmu.cs.dennisc.print.PrintUtilities.println( "repaintAll" );
  }

  private HierarchyBoundsListener hierarchyBoundsListener = new HierarchyBoundsListener() {
    @Override
    public void ancestorMoved(HierarchyEvent e) {
      repaintAll();
    }

    @Override
    public void ancestorResized(HierarchyEvent e) {
      repaintAll();
    }
  };
  private ComponentListener componentListener = new ComponentListener() {
    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
      repaintAll();
    }

    @Override
    public void componentResized(ComponentEvent e) {
      repaintAll();
    }
  };

  protected RuntimeResolver<? extends TrackableShape> getTrackableShapeResolver() {
    return this.trackableShapeResolver;
  }

  private TrackableShape trackableShape;

  public void updateTrackableShapeIfNecessary() {
    TrackableShape nextTrackableShape = this.trackableShapeResolver.getResolved();
    if (nextTrackableShape != this.trackableShape) {
      Logger.info("trackableShape change");
      if (this.trackableShape != null) {
        this.trackableShape.removeHierarchyBoundsListener(this.hierarchyBoundsListener);
        this.trackableShape.removeComponentListener(this.componentListener);
      }
      this.trackableShape = nextTrackableShape;
      if (this.trackableShape != null) {
        this.trackableShape.addComponentListener(this.componentListener);
        this.trackableShape.addHierarchyBoundsListener(this.hierarchyBoundsListener);
      }
    }
  }

  public void bind() {
    this.updateTrackableShapeIfNecessary();
  }

  public void unbind() {
    this.trackableShape = null;
  }

  public TrackableShape getTrackableShape() {
    return this.trackableShape;
  }

  //todo: move this to TrackableShape?
  public boolean isPotentiallyScrollable() {
    return true;
  }

  public boolean isEntered() {
    return this.isEntered;
  }

  public void setEntered(boolean isEntered) {
    this.isEntered = isEntered;
  }

  private static Shape constrainHeightIfNecessary(Shape shape, Integer heightConstraint) {
    if (heightConstraint != null) {
      if (shape instanceof Rectangle2D) {
        Rectangle2D rect = (Rectangle2D) shape;
        rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), Math.min(rect.getHeight(), heightConstraint));
        return rect;
      } else {
        //todo
        return shape;
      }
    } else {
      return shape;
    }
  }

  public ConnectionPreference getConnectionPreference() {
    return this.connectionPreference;
  }

  private static int getXForWestLayout(Rectangle noteBounds, Rectangle featureComponentBounds) {
    int x = featureComponentBounds.x;
    x -= 200;
    x -= noteBounds.width;
    return x;
  }

  private static int getXForEastLayout(Rectangle noteBounds, Rectangle featureComponentBounds) {
    int x = featureComponentBounds.x;
    x += featureComponentBounds.width;
    x += 200;
    return x;
  }

  private static int getYForNorthLayout(Rectangle noteBounds, Rectangle featureComponentBounds) {
    int y = featureComponentBounds.y;
    y -= 200;
    y -= noteBounds.height;
    return y;
  }

  private static int getYForSouthLayout(Rectangle noteBounds, Rectangle featureComponentBounds) {
    int y = featureComponentBounds.y;
    y += featureComponentBounds.height;
    y += 200;
    return y;
  }

  /* package-private */Connection calculateActualConnection(AwtComponentView<?> container, SwingComponentView<?> note) {
    Connection actualConnection = null;
    TrackableShape featureTrackableShape = this.getTrackableShape();
    if (featureTrackableShape != null) {
      Shape shape = featureTrackableShape.getShape(container, null);
      if (shape != null) {
        shape = constrainHeightIfNecessary(shape, this.heightConstraint);
        Rectangle containerBounds = container.getLocalBounds();
        Rectangle noteBounds = note.getBounds(container);
        Rectangle featureComponentBounds = shape.getBounds();
        if ((noteBounds != null) && (featureComponentBounds != null)) {
          final int x = featureComponentBounds.x - noteBounds.x;
          final int y = featureComponentBounds.y - noteBounds.y;

          Feature.ConnectionPreference connectionPreference = this.getConnectionPreference();
          if (connectionPreference == Feature.ConnectionPreference.EAST_WEST) {
            if (x >= 32) {
              actualConnection = Feature.Connection.WEST;
            } else {
              if (x <= (containerBounds.width - noteBounds.width - 32)) {
                actualConnection = Feature.Connection.EAST;
              }
            }
          }
          if (actualConnection != null) {
            //pass
          } else {
            if (y >= 32) {
              actualConnection = Feature.Connection.NORTH;
            } else {
              if (y <= (containerBounds.height - noteBounds.height - 32)) {
                actualConnection = Feature.Connection.SOUTH;
              } else {
                actualConnection = Connection.WEST;
              }
            }
          }
        }
      }
    }
    if (actualConnection == null) {
      actualConnection = Feature.Connection.SOUTH;
    }
    return actualConnection;
  }

  public Point calculateNoteLocation(AwtContainerView<?> container, AwtComponentView<?> note) {
    Rectangle containerBounds = container.getLocalBounds();
    Rectangle noteBounds = note.getBounds(container);

    Point rv = new Point();
    Connection actualConnection = null;
    TrackableShape featureTrackableShape = this.getTrackableShape();
    if (featureTrackableShape != null) {
      Shape shape = featureTrackableShape.getShape(container, null);
      if (shape != null) {
        shape = constrainHeightIfNecessary(shape, this.heightConstraint);
        Rectangle featureComponentBounds = shape.getBounds();
        int xFeatureComponentCenter = featureComponentBounds.x + (featureComponentBounds.width / 2);
        int xCardCenter = (containerBounds.x + containerBounds.width) / 2;
        int yFeatureComponentCenter = featureComponentBounds.y + (featureComponentBounds.height / 2);
        int yCardCenter = (containerBounds.y + containerBounds.height) / 2;

        Feature.ConnectionPreference connectionPreference = this.getConnectionPreference();
        if (connectionPreference == Feature.ConnectionPreference.EAST_WEST) {
          rv.x = getXForWestLayout(noteBounds, featureComponentBounds);
          if (rv.x >= 32) {
            actualConnection = Feature.Connection.WEST;
          } else {
            rv.x = getXForEastLayout(noteBounds, featureComponentBounds);
            if (rv.x <= (containerBounds.width - noteBounds.width - 32)) {
              actualConnection = Feature.Connection.EAST;
            }
          }
        }
        if (actualConnection != null) {
          rv.y = yFeatureComponentCenter;
          if (yFeatureComponentCenter < yCardCenter) {
            rv.y += 128;
          } else {
            rv.y -= noteBounds.height;
            rv.y -= 32;
          }
        } else {
          rv.y = getYForNorthLayout(noteBounds, featureComponentBounds);
          if (rv.y >= 32) {
            actualConnection = Feature.Connection.NORTH;
          } else {
            rv.y = getYForSouthLayout(noteBounds, featureComponentBounds);
            if (rv.y <= (containerBounds.height - noteBounds.height - 32)) {
              actualConnection = Feature.Connection.SOUTH;
            } else {
              class Inset implements Comparable<Inset> {
                private boolean isX;
                private int value;
                private Feature.Connection connection;

                public Inset(boolean isX, int value, Feature.Connection connection) {
                  this.isX = isX;
                  this.value = value;
                  this.connection = connection;
                }

                @Override
                public int compareTo(Inset o) {
                  return o.value - this.value;
                }
              }
              Inset[] insets = {
                  new Inset(true, featureComponentBounds.x, Connection.WEST),
                  new Inset(true, containerBounds.width - (featureComponentBounds.x + featureComponentBounds.width), Connection.EAST),
                  new Inset(false, featureComponentBounds.y, Connection.NORTH),
                  new Inset(false, containerBounds.height - (featureComponentBounds.y + featureComponentBounds.height), Connection.SOUTH),
              };
              Arrays.sort(insets);
              actualConnection = insets[0].connection;
              final int PAD = 64;
              if (insets[0].isX) {
                rv.x = insets[0].value;
                if (actualConnection == Connection.WEST) {
                  rv.x -= noteBounds.width;
                  rv.x -= PAD;
                } else {
                  rv.x += PAD;
                }
                rv.y = yFeatureComponentCenter;
              } else {
                rv.x = xFeatureComponentCenter;
                rv.y = insets[0].value;
                if (actualConnection == Connection.NORTH) {
                  rv.y -= noteBounds.height;
                  rv.y -= PAD;
                } else {
                  rv.y += PAD;
                }
              }
              //note: return
              return rv;
            }
          }
          rv.x = xFeatureComponentCenter;
          if (xFeatureComponentCenter < xCardCenter) {
            rv.x += 200;
          } else {
            rv.x -= noteBounds.width;
            rv.x -= 200;
          }
        }
      }
    }
    return rv;
  }

  protected abstract Insets getBoundsInsets();

  protected abstract Insets getContainsInsets();

  protected abstract Insets getPaintInsets();

  public Shape getShape(AwtComponentView<?> asSeenBy, Insets insets) {
    TrackableShape trackableShape = this.getTrackableShape();
    if (trackableShape != null) {
      if (trackableShape.isInView()) {
        Shape shape = trackableShape.getVisibleShape(asSeenBy, insets);
        shape = constrainHeightIfNecessary(shape, this.heightConstraint);
        return shape;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public Area getAreaToSubstractForContains(AwtComponentView<?> asSeenBy) {
    Shape shape = this.getShape(asSeenBy, this.getContainsInsets());
    if (shape != null) {
      return new Area(shape);
    } else {
      return null;
    }
  }

  public Area getAreaToSubstractForPaint(AwtComponentView<?> asSeenBy) {
    Shape shape = this.getShape(asSeenBy, this.getPaintInsets());
    if (shape != null) {
      return new Area(shape);
    } else {
      return null;
    }
  }

  protected abstract void paint(Graphics2D g2, Shape shape, Connection actualConnection);

  private static void drawPath(Graphics2D g2, float xFrom, float yFrom, float xTo, float yTo, boolean isCurveDesired) {
    Polynomial xPolynomial;
    Polynomial yPolynomial;
    GeneralPath path = new GeneralPath();
    path.moveTo(xFrom, yFrom);
    if (isCurveDesired) {
      float xVector = xTo - xFrom;
      float yVector = yTo - yFrom;
      final float A = 0.15f;
      final float B = 1.0f;

      float xA = xFrom + (xVector * A);
      float yA = yFrom + (yVector * A);

      float xB = xFrom + (xVector * B);
      float yB = yFrom + (yVector * B);

      float xC0 = xB;
      float yC0 = yA;
      float xC1 = xA;
      float yC1 = yB;

      path.curveTo(xC0, yC0, xC1, yC1, xTo, yTo);

      xPolynomial = new BezierCubic(xFrom, xC0, xC1, xTo);
      yPolynomial = new BezierCubic(yFrom, yC0, yC1, yTo);
    } else {
      float xC = xTo;
      float yC = yFrom;
      path.quadTo(xC, yC, xTo, yTo);
      xPolynomial = new BezierQuadratic(xFrom, xC, xTo);
      yPolynomial = new BezierQuadratic(yFrom, yC, yTo);

    }
    g2.draw(path);

    final double tDelta = 0.01;
    double theta = Double.NaN;
    double t = 0.9;
    while (true) {

      double xApproaching = xPolynomial.evaluate(t);
      double yApproaching = yPolynomial.evaluate(t);

      double xDelta = xApproaching - xTo;
      double yDelta = yApproaching - yTo;

      t += tDelta;

      boolean isCloseEnough = ((xDelta * xDelta) + (yDelta * yDelta)) < ARROW_HEAD_LENGTH_SQUARED;
      boolean isBreaking = isCloseEnough || (t >= 1.0);

      if (isBreaking) {
        theta = Math.atan2(yDelta, xDelta);
        break;
      }

    }

    if (Double.isNaN(theta)) {
      //pass
    } else {
      AffineTransform m = g2.getTransform();
      try {
        g2.translate(xTo, yTo);
        g2.rotate(theta);
        GeneralPath arrowHeadPath = new GeneralPath();
        arrowHeadPath.moveTo(0, 0);
        arrowHeadPath.lineTo(ARROW_HEAD_LENGTH, ARROW_HEAD_HALF_HEIGHT);
        arrowHeadPath.lineTo(ARROW_HEAD_LENGTH, -ARROW_HEAD_HALF_HEIGHT);
        arrowHeadPath.closePath();
        g2.fill(arrowHeadPath);
      } finally {
        g2.setTransform(m);
      }
    }
  }

  public final void paint(Graphics2D g2, AwtComponentView<?> asSeenBy, SwingComponentView<?> note) {
    Shape shape = this.getShape(asSeenBy, this.getPaintInsets());
    if (shape != null) {
      Connection actualConnection = this.calculateActualConnection(asSeenBy, note);
      Paint prevPaint = g2.getPaint();
      Stroke prevStroke = g2.getStroke();

      this.paint(g2, shape, actualConnection);

      if (this.isPathRenderingDesired()) {
        g2.setPaint(Color.BLACK);

        AwtComponentView<?> component;
        if (note.getComponentCount() > 0) {
          component = note.getComponent(0);
        } else {
          component = note;
        }
        Rectangle noteBounds = component.getBounds(asSeenBy);
        Rectangle shapeBounds = shape.getBounds();
        if (shapeBounds != null) {
          Point ptComponent = actualConnection.getPoint(shapeBounds);
          int xContraint;
          if (noteBounds.x > ptComponent.x) {
            xContraint = SwingConstants.LEADING;
          } else {
            xContraint = SwingConstants.TRAILING;
          }
          Point ptNote = RectangleUtilities.getPoint(noteBounds, xContraint, SwingConstants.CENTER);
          g2.setStroke(ARROW_STROKE);
          drawPath(g2, ptNote.x, ptNote.y, ptComponent.x, ptComponent.y, actualConnection.isCurveDesired());
        }
      }

      g2.setStroke(prevStroke);
      g2.setPaint(prevPaint);
    }
  }

  public void appendDetailedReport(StringBuilder sb) {
    sb.append("\t\t");
    sb.append(this.getClass().getName());
    sb.append(": ");
    String status = this.getStatus();
    sb.append(status != null ? status : "IS_GOOD_TO_GO");
    sb.append("\n");
  }
}
