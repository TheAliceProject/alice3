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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.Application;
import org.lgna.croquet.resolvers.RuntimeResolver;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.TrackableShape;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

/**
 * @author Dennis Cosgrove
 */
public class Feature {

  private static final int PAD = 4;
  private static final Insets PAINT_INSETS = new Insets(PAD, PAD, PAD, PAD);
  protected final Painter painter;

  private final RuntimeResolver<? extends TrackableShape> trackableShapeResolver;

  public Feature(RuntimeResolver<? extends TrackableShape> trackableShapeResolver, Painter painter) {
    //assert trackableShape != null;
    this.trackableShapeResolver = trackableShapeResolver;
    this.painter = painter;
  }

  private static void repaintAll() {
    Application.getActiveInstance().getDocumentFrame().getFrame().getContentPane().repaint();
    //edu.cmu.cs.dennisc.print.PrintUtilities.println( "repaintAll" );
  }

  private final HierarchyBoundsListener hierarchyBoundsListener = new HierarchyBoundsListener() {
    @Override
    public void ancestorMoved(HierarchyEvent e) {
      repaintAll();
    }

    @Override
    public void ancestorResized(HierarchyEvent e) {
      repaintAll();
    }
  };
  private final ComponentListener componentListener = new ComponentListener() {
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

  public void updateShape() {
    this.trackableShape = null;
    this.updateTrackableShapeIfNecessary();
  }

  protected Insets getContainsInsets() {
    return null;
  }

  protected Insets getPaintInsets() {
    return Feature.PAINT_INSETS;
  }

  private Shape getShape(AwtComponentView<?> asSeenBy, Insets insets) {
    if (trackableShape != null && trackableShape.isInView()) {
      return trackableShape.getVisibleShape(asSeenBy, insets);
    } else {
      return null;
    }
  }

  public Shape getShapeToSubtractForContains(AwtComponentView<?> asSeenBy) {
    return this.getShape(asSeenBy, this.getContainsInsets());
  }

  public Shape getShapeToSubtractForPaint(AwtComponentView<?> asSeenBy) {
    return this.getShape(asSeenBy, this.getPaintInsets());
  }

  public final void paint(Graphics2D g2, AwtComponentView<?> asSeenBy) {
    Shape shape = this.getShape(asSeenBy, this.getPaintInsets());
    if (shape != null) {
      Paint prevPaint = g2.getPaint();
      Stroke prevStroke = g2.getStroke();

      painter.paint(g2, shape);

      g2.setStroke(prevStroke);
      g2.setPaint(prevPaint);
    }
  }
}
