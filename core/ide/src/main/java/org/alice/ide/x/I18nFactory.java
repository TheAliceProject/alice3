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

package org.alice.ide.x;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.alice.ide.i18n.Chunk;
import org.alice.ide.i18n.GetsChunk;
import org.alice.ide.i18n.Line;
import org.alice.ide.i18n.MethodInvocationChunk;
import org.alice.ide.i18n.Page;
import org.alice.ide.i18n.PropertyChunk;
import org.alice.ide.i18n.TextChunk;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.MethodInvocation;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

/**
 * @author Dennis Cosgrove
 */
public abstract class I18nFactory {
  protected abstract SwingComponentView<?> createGetsComponent(boolean isTowardLeadingEdge);

  protected abstract SwingComponentView<?> createPropertyComponent(InstanceProperty<?> property, int underscoreCount);

  private SwingComponentView<?> createComponent(GetsChunk getsChunk, InstancePropertyOwner owner) {
    return this.createGetsComponent(getsChunk.isTowardLeading());
  }

  private SwingComponentView<?> createComponent(TextChunk textChunk, InstancePropertyOwner owner) {
    return new Label(textChunk.getText());
  }

  private SwingComponentView<?> createComponent(PropertyChunk propertyChunk, InstancePropertyOwner owner) {
    int underscoreCount = propertyChunk.getUnderscoreCount();
    String propertyName = propertyChunk.getPropertyName();
    InstanceProperty<?> property = owner.getPropertyNamed(propertyName);
    if (property != null) {
      return createPropertyComponent(property, underscoreCount);
    } else {
      Logger.severe(propertyName, owner);
      Label rv = new Label("TODO: " + propertyName);
      rv.setBackgroundColor(Color.RED);
      return rv;
    }
  }

  protected abstract SwingComponentView<?> createComponent(MethodInvocationChunk methodInvocationChunk, InstancePropertyOwner owner);

  private SwingComponentView<?> createComponent(Chunk chunk, InstancePropertyOwner owner) {
    if (chunk instanceof TextChunk) {
      return createComponent((TextChunk) chunk, owner);
    } else if (chunk instanceof PropertyChunk) {
      return createComponent((PropertyChunk) chunk, owner);
    } else if (chunk instanceof MethodInvocationChunk) {
      return createComponent((MethodInvocationChunk) chunk, owner);
    } else if (chunk instanceof GetsChunk) {
      return createComponent((GetsChunk) chunk, owner);
    } else {
      return new Label("unhandled: " + chunk.toString());
    }
  }

  private int getPixelsPerIndent() {
    return 4;
  }

  private SwingComponentView<?> createComponent(Line line, InstancePropertyOwner owner) {
    int indentCount = line.getIndentCount();
    Chunk[] chunks = line.getChunks();
    assert chunks.length > 0 : owner;
    if ((indentCount > 0) || (chunks.length > 1)) {
      LineAxisPanel rv = new LineAxisPanel();
      if (indentCount > 0) {
        rv.addComponent(BoxUtilities.createHorizontalSliver(indentCount * this.getPixelsPerIndent()));
      }
      for (Chunk chunk : chunks) {
        SwingComponentView<?> component = createComponent(chunk, owner);
        assert component != null : chunk.toString();
        //        rv.setAlignmentY( 0.5f );
        rv.addComponent(component);
      }
      return rv;
    } else {
      //edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping line" );
      SwingComponentView<?> rv = createComponent(chunks[0], owner);
      assert rv != null : chunks[0].toString();
      return rv;
    }
  }

  public SwingComponentView<?> createComponent(Page page, InstancePropertyOwner owner) {
    Line[] lines = page.getLines();
    final int N = lines.length;
    assert N > 0;
    if (N > 1) {
      final boolean isLoop = lines[N - 1].isLoop();
      PageAxisPanel pagePane = new PageAxisPanel() {
        @Override
        protected JPanel createJPanel() {
          return new DefaultJPanel() {
            @Override
            protected void paintComponent(Graphics g) {
              Color prev = g.getColor();
              if (isLoop) {
                int n = this.getComponentCount();
                Component cFirst = this.getComponent(0);
                Component cLast = this.getComponent(n - 1);
                g.setColor(ColorUtilities.createGray(160));
                int xB = I18nFactory.this.getPixelsPerIndent();
                int xA = xB / 2;
                int yTop = cFirst.getY() + cFirst.getHeight();
                int yBottom = cLast.getY() + (cLast.getHeight() / 2);
                g.drawLine(xA, yTop, xA, yBottom);
                g.drawLine(xA, yBottom, xB, yBottom);

                int xC = cLast.getX() + cLast.getWidth();
                int xD = xC + I18nFactory.this.getPixelsPerIndent();

                g.drawLine(xC, yBottom, xD, yBottom);
                g.drawLine(xD, yBottom, xD, cLast.getY());

                final int HALF_TRIANGLE_WIDTH = 3;
                GraphicsUtilities.fillTriangle(g, GraphicsUtilities.Heading.NORTH, xA - HALF_TRIANGLE_WIDTH, yTop, HALF_TRIANGLE_WIDTH + 1 + HALF_TRIANGLE_WIDTH, 10);
              }
              g.setColor(prev);
              super.paintComponent(g);
            }
          };
        }
      };
      for (Line line : lines) {
        if (line.getChunks().length > 0) {
          pagePane.addComponent(createComponent(line, owner));
        } else {
          Logger.severe(line);
        }
      }
      pagePane.revalidateAndRepaint();
      return pagePane;
    } else {
      //edu.cmu.cs.dennisc.print.PrintUtilities.println( "skipping page" );
      return createComponent(lines[0], owner);
    }
  }

  public SwingComponentView<?> createComponent(InstancePropertyOwner owner) {
    Formatter formatter = FormatterState.getInstance().getValue();
    SwingComponentView<?> rv;
    if (owner != null) {
      String value;
      if (owner instanceof MethodInvocation) {
        MethodInvocation methodInvocation = (MethodInvocation) owner;
        AbstractMethod method = methodInvocation.method.getValue();
        String text = formatter.getNameForDeclaration(method);
        if (text.contains("</expression/>")) {
          value = text;
        } else {
          value = null;
        }
      } else {
        value = null;
      }
      if (value != null) {
        //pass
      } else {
        Class<?> cls = owner.getClass();
        value = formatter.getTemplateText(cls);
      }
      Page page = new Page(value);
      rv = createComponent(page, owner);
    } else {
      rv = new Label(formatter.getTextForNull());
    }
    return rv;
  }
}
