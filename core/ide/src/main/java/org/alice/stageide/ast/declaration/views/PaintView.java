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

package org.alice.stageide.ast.declaration.views;

import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.common.resources.ImageResource;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.ViewController;
import org.lgna.project.ast.Expression;
import org.lgna.project.virtualmachine.VirtualMachine;
import org.lgna.story.Color;
import org.lgna.story.ImageSource;
import org.lgna.story.Paint;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author Dennis Cosgrove
 */
public class PaintView extends ViewController<JComponent, CustomItemState<Expression>> {
  private ValueListener<Expression> valueListener = e -> PaintView.this.repaint();

  PaintView(CustomItemState<Expression> model) {
    super(model);
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.getModel().addNewSchoolValueListener(this.valueListener);
  }

  @Override
  protected void handleUndisplayable() {
    this.getModel().removeNewSchoolValueListener(this.valueListener);
    super.handleUndisplayable();
  }

  @Override
  protected JComponent createAwtComponent() {
    JComponent rv = new JComponent() {
      @Override
      public Dimension getMinimumSize() {
        return this.getPreferredSize();
      }

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Expression expression = PaintView.this.getModel().getValue();
        if (expression != null) {
          VirtualMachine vm = StorytellingSceneEditor.getInstance().getVirtualMachine();

          Object[] values = vm.ENTRY_POINT_evaluate(null, new Expression[] {expression});
          if ((values.length == 1) && (values[0] instanceof Paint)) {
            Paint paint = (Paint) values[0];
            if (paint instanceof Color) {
              g.setColor(((Color) paint).toAwtColor());
              g.fillRect(0, 0, this.getWidth(), this.getHeight());
            } else {
              if (paint instanceof ImageSource) {
                paintImage(g, (ImageSource) paint);
              }
            }
          }
        }
      }

      private void paintImage(Graphics g, ImageSource imageSource) {
        Texture texture = imageSource.getTextureIfPresent();
        BufferedImageTexture bufferedImageTexture = (BufferedImageTexture) texture;
        if (texture == null) {
          return;
        }
        ImageResource resource = imageSource.getImageResource();
        Image image = scaleImageToFit(bufferedImageTexture.getBufferedImage(), resource);

        int x = (getWidth() - image.getWidth(null)) / 2;
        int y = (getHeight() - image.getHeight(null)) / 2;
        g.drawImage(image, x, y, this);
      }

      private Image scaleImageToFit(Image image, ImageResource resource) {
        int componentWidth = getWidth();
        int componentHeight = getHeight();
        int imageWidth = resource.getWidth();
        int imageHeight = resource.getHeight();
        double widthRatio = imageWidth / (double) componentWidth;
        double heightRatio = imageHeight / (double) componentHeight;
        int widthFactor = -1;
        int heightFactor = -1;
        if ((imageWidth > imageHeight) && (widthRatio > 1.0)) {
          widthFactor = componentWidth;
          heightFactor = componentHeight * imageHeight / imageWidth;
        } else if ((imageHeight > imageWidth) && (heightRatio > 1.0)) {
          widthFactor = componentWidth * imageWidth / imageHeight;
          heightFactor = componentHeight;
        }
        if ((widthFactor > 0) || (heightFactor > 0)) {
          return image.getScaledInstance(widthFactor, heightFactor, Image.SCALE_SMOOTH);
        }
        return image;
      }

    };
    rv.setPreferredSize(new Dimension(128, 128));
    return rv;
  }
}
