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
package org.alice.ide.ast.type.merge.croquet.views.icons;

import edu.cmu.cs.dennisc.java.awt.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.ast.type.merge.croquet.ActionStatus;
import org.alice.ide.ast.type.merge.croquet.MemberHub;
import org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities;
import org.lgna.croquet.icon.AbstractIcon;
import org.lgna.croquet.icon.IconSize;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Dennis Cosgrove
 */
public class ActionStatusIcon extends AbstractIcon {
  private static final int PAD = 1;
  private static final Dimension SIZE = IconSize.SMALL.getSize();
  private static final Paint ADD_REPLACE_FILL_PAINT = new Color(0, 127, 0);
  private static final Paint ADD_REPLACE_DRAW_PAINT = Color.DARK_GRAY;
  private static final Shape ADD_SHAPE;

  private static final Paint ERROR_PAINT = MemberViewUtilities.ACTION_MUST_BE_TAKEN_COLOR;
  private static final Font ERROR_FONT = FontUtilities.deriveFont(new Font("Serif", 0, SIZE.height - 2), TextWeight.EXTRABOLD);

  private static final Shape CHECK_SHAPE;
  private static final Stroke CHECK_INNER_STROKE;
  private static final Stroke CHECK_OUTER_STROKE;

  private static final GeneralPath ERROR_SHAPE;

  static {
    int w = SIZE.width - PAD - PAD;
    int h = SIZE.height - PAD - PAD;

    int unit = h;

    double xA = 0.2;
    double xC = 0.8;
    double xB = xA + ((xC - xA) * 0.3);

    double yA = 0.45;
    double yB = xC;
    double yC = xA;

    GeneralPath path = new GeneralPath();
    path.moveTo(xA * unit, yA * unit);
    path.lineTo(xB * unit, yB * unit);
    path.lineTo(xC * unit, yC * unit);
    CHECK_SHAPE = path;

    double v0 = 0.0;
    double v1 = 0.3 * unit;
    double v2 = 0.7 * unit;
    double v3 = unit;

    ERROR_SHAPE = new GeneralPath();
    ERROR_SHAPE.moveTo(v1, v0);
    ERROR_SHAPE.lineTo(v2, v0);
    ERROR_SHAPE.lineTo(v3, v1);
    ERROR_SHAPE.lineTo(v3, v2);
    ERROR_SHAPE.lineTo(v2, v3);
    ERROR_SHAPE.lineTo(v1, v3);
    ERROR_SHAPE.lineTo(v0, v2);
    ERROR_SHAPE.lineTo(v0, v1);
    ERROR_SHAPE.closePath();
    CHECK_INNER_STROKE = new BasicStroke(unit * 0.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    CHECK_OUTER_STROKE = new BasicStroke(unit * 0.25f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    int x = 2;
    int y = 2;
    Rectangle horizontal = new Rectangle(x, (h / 2) - 2, w - x - x, 4);
    Rectangle vertical = new Rectangle((w / 2) - 2, y, 4, h - y - y);
    ADD_SHAPE = AreaUtilities.createUnion(horizontal, vertical);

  }

  private MemberHub<?> memberHub;

  public ActionStatusIcon(MemberHub<?> memberHub) {
    super(SIZE);
    this.memberHub = memberHub;
  }

  private void paintAdd(Component c, Graphics2D g2, ButtonModel buttonModel) {
    g2.setPaint(ADD_REPLACE_FILL_PAINT);
    g2.fill(ADD_SHAPE);
    boolean isRollover = buttonModel != null ? buttonModel.isRollover() : false;
    g2.setPaint(isRollover ? Color.WHITE : ADD_REPLACE_DRAW_PAINT);
    g2.draw(ADD_SHAPE);
  }

  private void paintCheck(Component c, Graphics2D g2, ButtonModel buttonModel, Paint fillPaint) {
    boolean isRollover = buttonModel != null ? buttonModel.isRollover() : false;
    Stroke prevStroke = g2.getStroke();
    g2.setStroke(CHECK_OUTER_STROKE);
    g2.setPaint(isRollover ? Color.WHITE : Color.BLACK);
    g2.draw(CHECK_SHAPE);
    g2.setStroke(CHECK_INNER_STROKE);
    g2.setPaint(fillPaint);
    g2.draw(CHECK_SHAPE);
    g2.setStroke(prevStroke);
  }

  private void paintReplace(Component c, Graphics2D g2, ButtonModel buttonModel) {
    paintCheck(c, g2, buttonModel, ADD_REPLACE_FILL_PAINT);
  }

  private void paintKeep(Component c, Graphics2D g2, ButtonModel buttonModel) {
    paintCheck(c, g2, buttonModel, Color.LIGHT_GRAY);
  }

  private void paintOmit(Component c, Graphics2D g2, ButtonModel buttonModel, int width, int height) {
    boolean isPaintDesired = buttonModel != null ? buttonModel.isRollover() : true;
    if (isPaintDesired) {
      float size = Math.min(width, height) * 0.9f;

      float w = size;
      float h = size * 0.25f;
      float xC = -w * 0.5f;
      float yC = -h * 0.5f;
      RoundRectangle2D.Float rr = new RoundRectangle2D.Float(xC, yC, w, h, h, h);

      Area area0 = new Area(rr);
      Area area1 = new Area(rr);

      AffineTransform m0 = new AffineTransform();
      m0.rotate(Math.PI * 0.25);
      area0.transform(m0);

      AffineTransform m1 = new AffineTransform();
      m1.rotate(Math.PI * 0.75);
      area1.transform(m1);

      area0.add(area1);

      AffineTransform m = new AffineTransform();
      m.translate((width / 2), (height / 2));
      area0.transform(m);

      g2.setPaint(new Color(127, 63, 63));
      g2.fill(area0);
    }
  }

  private void paintError(Component c, Graphics2D g2, ButtonModel buttonModel, int width, int height) {
    boolean isRollover = buttonModel != null ? buttonModel.isRollover() : false;
    g2.setPaint(ERROR_PAINT);
    g2.fill(ERROR_SHAPE);
    g2.setPaint(isRollover ? Color.WHITE : Color.GRAY);
    g2.draw(ERROR_SHAPE);

    Font prevFont = g2.getFont();
    g2.setPaint(Color.WHITE);
    g2.setFont(ERROR_FONT);
    GraphicsUtilities.drawCenteredText(g2, "!", 0, 0, width, height);
    g2.setFont(prevFont);
  }

  @Override
  protected void paintIcon(Component c, Graphics2D g2) {
    ActionStatus actionStatus = this.memberHub.getActionStatus();
    ButtonModel buttonModel;
    if (c instanceof AbstractButton) {
      AbstractButton button = (AbstractButton) c;
      buttonModel = button.getModel();
    } else {
      buttonModel = null;
    }

    int xOffset = PAD;
    int yOffset = PAD;
    int width = this.getIconWidth() - PAD - PAD;
    int height = this.getIconHeight() - PAD - PAD;
    g2.translate(xOffset, yOffset);

    Object prevAntialiasing = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    //      g2.setPaint( java.awt.Color.RED );
    //      g2.fillRect( 0, 0, width, height );
    if ((actionStatus == ActionStatus.ADD_UNIQUE) || (actionStatus == ActionStatus.ADD_AND_RENAME)) {
      paintAdd(c, g2, buttonModel);
    } else if (actionStatus == ActionStatus.REPLACE_OVER_ORIGINAL) {
      paintReplace(c, g2, buttonModel);
    } else if ((actionStatus == ActionStatus.KEEP_IDENTICAL) || (actionStatus == ActionStatus.KEEP_UNIQUE) || (actionStatus == ActionStatus.KEEP_OVER_REPLACEMENT) || (actionStatus == ActionStatus.KEEP_OVER_DIFFERENT_SIGNATURE) || (actionStatus == ActionStatus.KEEP_AND_RENAME)) {
      paintKeep(c, g2, buttonModel);
    } else if ((actionStatus == ActionStatus.RENAME_REQUIRED) || (actionStatus == ActionStatus.SELECTION_REQUIRED)) {
      paintError(c, g2, buttonModel, width, height);
    } else if ((actionStatus == ActionStatus.OMIT) || (actionStatus == ActionStatus.OMIT_IN_FAVOR_OF_ORIGINAL) || (actionStatus == ActionStatus.DELETE_IN_FAVOR_OF_REPLACEMENT)) {
      paintOmit(c, g2, buttonModel, width, height);
    } else {
      Logger.severe(actionStatus);
    }
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, prevAntialiasing);
    g2.translate(-xOffset, -yOffset);
  }
}
