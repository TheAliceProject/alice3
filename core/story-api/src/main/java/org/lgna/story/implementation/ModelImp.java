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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.Animated;
import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.math.animation.Dimension3Animation;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Leaf;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;
import edu.cmu.cs.dennisc.scenegraph.graphics.Bubble;
import edu.cmu.cs.dennisc.scenegraph.graphics.SpeechBubble;
import edu.cmu.cs.dennisc.scenegraph.graphics.ThoughtBubble;
import edu.cmu.cs.dennisc.scenegraph.scale.Resizer;
import edu.cmu.cs.dennisc.scenegraph.scale.Scalable;
import edu.cmu.cs.dennisc.scenegraph.util.BoundingBoxDecorator;
import edu.cmu.cs.dennisc.texture.Texture;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.Paint;
import org.lgna.story.implementation.overlay.BubbleAnimation;
import org.lgna.story.implementation.overlay.BubbleImp;
import org.lgna.story.implementation.overlay.SpeechBubbleImp;
import org.lgna.story.implementation.overlay.ThoughtBubbleImp;

import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

/**
 * @author Dennis Cosgrove
 */
public abstract class ModelImp extends TransformableImp implements Scalable {
  @Override
  public Resizer[] getResizers() {
    return new Resizer[] {Resizer.UNIFORM};
  }

  @Override
  public double getValueForResizer(Resizer resizer) {
    assert resizer == Resizer.UNIFORM : resizer;
    return this.getScale().x;
  }

  @Override
  public void setValueForResizer(Resizer resizer, double value) {
    assert resizer == Resizer.UNIFORM : resizer;
    this.setScale(new Dimension3(value, value, value));
  }

  public final PaintProperty paint = new PaintProperty(ModelImp.this) {
    @Override
    protected void internalSetValue(Paint value) {
      Color4f color4f = EmployeesOnly.getColor4f(value, Color4f.WHITE);
      Texture texture = EmployeesOnly.getTexture(value, null);
      for (SimpleAppearance sgAppearance : ModelImp.this.getSgPaintAppearances()) {
        if (!Objects.equals(color4f, sgAppearance.diffuseColor.getValue())) {
          sgAppearance.diffuseColor.setValue(color4f);
        }
        if (sgAppearance instanceof TexturedAppearance) {
          TexturedAppearance sgTexturedAppearance = (TexturedAppearance) sgAppearance;
          if (!Objects.equals(texture, sgTexturedAppearance.diffuseColorTexture.getValue())) {
            sgTexturedAppearance.setDiffuseColorTextureAndInferAlphaBlend(texture);
            textureChanged();
          }
        }
      }
    }
  };

  // For subclasses that need to update the sceneGraph
  protected void textureChanged() {
  }

  public final FloatProperty opacity = new FloatProperty(ModelImp.this) {
    @Override
    public Float getValue() {
      return ModelImp.this.getSgOpacityAppearances()[0].opacity.getValue();
    }

    @Override
    protected void handleSetValue(Float value) {
      for (SimpleAppearance sgAppearance : ModelImp.this.getSgOpacityAppearances()) {
        sgAppearance.opacity.setValue(value);
      }
    }
  };

  public ModelImp() {
    this.getSgComposite().putBonusDataFor(Scalable.KEY, this);
  }

  protected abstract SimpleAppearance[] getSgPaintAppearances();

  protected abstract SimpleAppearance[] getSgOpacityAppearances();

  public abstract Visual[] getSgVisuals();

  @Override
  public void setName(String name) {
    super.setName(name);
    int i = 0;
    for (Visual sgVisual : this.getSgVisuals()) {
      sgVisual.setName(name + ".sgVisual" + i);
      i += 1;
    }
  }

  @Override
  protected void updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans) {
    for (Visual sgVisual : this.getSgVisuals()) {
      rv.add(sgVisual, trans);
    }
  }

  protected abstract InstanceProperty[] getScaleProperties();

  @Override
  public final void addScaleListener(PropertyListener listener) {
    for (InstanceProperty property : this.getScaleProperties()) {
      property.addPropertyListener(listener);
    }
  }

  @Override
  public final void removeScaleListener(PropertyListener listener) {
    for (InstanceProperty property : this.getScaleProperties()) {
      property.removePropertyListener(listener);
    }
  }

  public abstract Dimension3 getScale();

  public abstract void setScale(Dimension3 scale);

  public void animateSetScale(Dimension3 scale, double duration, Style style) {
    animateSetSize(getSizeForScale(scale), duration, style);
  }

  public abstract void setSize(Dimension3 size);

  public void animateSetSize(Dimension3 size, double duration, Style style) {
    double actualDuration = this.adjustDurationIfNecessary(duration);
    if (EpsilonUtilities.isWithinReasonableEpsilon(actualDuration, 0.0)) {
      this.setSize(size);
    } else {
      class SizeAnimation extends Dimension3Animation {
        private SizeAnimation(double duration, Style style, Dimension3 size0, Dimension3 size1) {
          super(duration, style, size0, size1);
        }

        @Override
        protected void updateValue(Dimension3 v) {
          ModelImp.this.setSize(v);
        }

        @Override
        public Animated getAnimated() {
          return ModelImp.this;
        }
      }
      this.perform(new SizeAnimation(duration, style, ModelImp.this.getSize(), size));
    }
  }

  private Dimension3 getSizeForScale(Dimension3 scale) {
    Dimension3 prevSize = this.getSize();
    Dimension3 prevScale = this.getScale();

    return new Dimension3(scale.x * (prevSize.x / prevScale.x), scale.y * (prevSize.y / prevScale.y), scale.z * (prevSize.z / prevScale.z));
  }

  protected Dimension3 getScaleForSize(Dimension3 size) {
    Dimension3 prevSize = this.getSize();
    Dimension3 prevScale = this.getScale();

    Dimension3 scale = new Dimension3(size.x / (prevSize.x / prevScale.x), size.y / (prevSize.y / prevScale.y), size.z / (prevSize.z / prevScale.z));
    if (Double.isNaN(scale.x)) {
      scale.x = 1;
    }
    if (Double.isNaN(scale.y)) {
      scale.y = 1;
    }
    if (Double.isNaN(scale.z)) {
      scale.z = 1;
    }
    return scale;
  }

  // The projected size in the displayed world. The inner representation's size multiplied by any scale factor
  public Dimension3 getSize() {
    return getAxisAlignedMinimumBoundingBox().getSize();
  }

  public double getWidth() {
    return this.getSize().x;
  }

  public double getHeight() {
    return this.getSize().y;
  }

  public double getDepth() {
    return this.getSize().z;
  }

  public void animateSetWidth(double width, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, Style style) {
    assert !(isVolumePreserved && isAspectRatioPreserved);
    double prevWidth = this.getWidth();
    assert !Double.isNaN(prevWidth);
    assert prevWidth >= 0;
    if (prevWidth > 0.0) {
      double factor = width / prevWidth;
      if (isAspectRatioPreserved) {
        this.animateResize(factor, duration, style);
      } else {
        this.animateResizeWidth(factor, isVolumePreserved, duration, style);
      }
    } else {
      if (width != 0.0) {
        throw new RuntimeException("unable to set the width of model that has zero(0) width");
      }
    }
  }

  public void animateSetHeight(double height, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, Style style) {
    assert !(isVolumePreserved && isAspectRatioPreserved);
    double prevHeight = this.getHeight();
    assert !Double.isNaN(prevHeight);
    assert prevHeight >= 0;
    if (prevHeight > 0.0) {
      double factor = height / prevHeight;
      if (isAspectRatioPreserved) {
        this.animateResize(factor, duration, style);
      } else {
        this.animateResizeHeight(factor, isVolumePreserved, duration, style);
      }
    } else {
      if (height != 0.0) {
        throw new RuntimeException("unable to set the height of model that has zero(0) height");
      }
    }
  }

  public void animateSetDepth(double depth, boolean isVolumePreserved, boolean isAspectRatioPreserved, double duration, Style style) {
    assert !(isVolumePreserved && isAspectRatioPreserved);
    double prevDepth = this.getDepth();
    assert !Double.isNaN(prevDepth);
    assert prevDepth >= 0;
    if (prevDepth > 0.0) {
      double factor = depth / prevDepth;
      if (isAspectRatioPreserved) {
        this.animateResize(factor, duration, style);
      } else {
        this.animateResizeDepth(factor, isVolumePreserved, duration, style);
      }
    } else {
      if (depth != 0.0) {
        throw new RuntimeException("unable to set the depth of model that has zero(0) depth");
      }
    }
  }

  protected enum Dimension {
    LEFT_TO_RIGHT(true, false, false), TOP_TO_BOTTOM(false, true, false), FRONT_TO_BACK(false, false, true);

    private final boolean isXScaled;
    private final boolean isYScaled;
    private final boolean isZScaled;

    Dimension(boolean isXScaled, boolean isYScaled, boolean isZScaled) {
      this.isXScaled = isXScaled;
      this.isYScaled = isYScaled;
      this.isZScaled = isZScaled;
      assert this.isXScaled ^ this.isYScaled ^ this.isZScaled;
    }

    public Dimension3 getResizeAxis(Dimension3 rv, double amount, boolean isVolumePreserved) {
      //todo: center around 0 as opposed to 1?
      assert amount > 0;

      double x;
      double y;
      double z;

      if (isVolumePreserved) {
        double squash = 1.0 / Math.sqrt(amount);
        if (this.isXScaled) {
          x = amount;
          y = squash;
          z = squash;
        } else if (this.isYScaled) {
          x = squash;
          y = amount;
          z = squash;
        } else if (this.isZScaled) {
          x = squash;
          y = squash;
          z = amount;
        } else {
          throw new RuntimeException();
        }
      } else {
        x = 1;
        y = 1;
        z = 1;
        if (this.isXScaled) {
          x = amount;
        }
        if (this.isYScaled) {
          y = amount;
        }
        if (this.isZScaled) {
          z = amount;
        }
      }

      rv.set(x, y, z);
      return rv;
    }

    public Dimension3 getResizeAxis(double amount, boolean isVolumePreserved) {
      return getResizeAxis(Dimension3.createNaN(), amount, isVolumePreserved);
    }
  }

  private void animateScale(Dimension3 scale, double duration, Style style) {
    Dimension3 prevScale = this.getScale();
    this.animateSetScale(new Dimension3(prevScale.x * scale.x, prevScale.y * scale.y, prevScale.z * scale.z), duration, style);
  }

  public void animateResize(double factor, double duration, Style style) {
    this.animateScale(new Dimension3(factor, factor, factor), duration, style);
  }

  public void animateResizeWidth(double factor, boolean isVolumePreserved, double duration, Style style) {
    this.animateScale(Dimension.LEFT_TO_RIGHT.getResizeAxis(factor, isVolumePreserved), duration, style);
  }

  public void animateResizeHeight(double factor, boolean isVolumePreserved, double duration, Style style) {
    this.animateScale(Dimension.TOP_TO_BOTTOM.getResizeAxis(factor, isVolumePreserved), duration, style);
  }

  public void animateResizeDepth(double factor, boolean isVolumePreserved, double duration, Style style) {
    this.animateScale(Dimension.FRONT_TO_BACK.getResizeAxis(factor, isVolumePreserved), duration, style);
  }

  public void say(String text, double duration, Font font, Color4f textColor, Color4f fillColor, Color4f outlineColor, Bubble.PositionPreference positionPreference) {
    BubbleImp bubbleImp = new SpeechBubbleImp(this, getSpeechBubbleOriginator(), text, font, textColor, fillColor, outlineColor, positionPreference);
    displayBubble(bubbleImp, duration);
  }

  public void think(String text, double duration, Font font, Color4f textColor, Color4f fillColor, Color4f outlineColor, Bubble.PositionPreference positionPreference) {
    BubbleImp bubbleImp = new ThoughtBubbleImp(this, getSpeechBubbleOriginator(), text, font, textColor, fillColor, outlineColor, positionPreference);
    displayBubble(bubbleImp, duration);
  }

  private void displayBubble(BubbleImp bubbleImp, double duration) {
    if (getScene() != null) {
      duration = adjustDurationIfNecessary(duration);
      double inOutDuration;
      if (duration >= 1) {
        duration = duration - 0.4;
        inOutDuration = 0.2;
      } else {
        duration = duration * 0.6;
        inOutDuration = duration * 0.2;
      }
      perform(new BubbleAnimation(inOutDuration, duration, inOutDuration, bubbleImp));
    } else {
      //todo
      JOptionPane.showMessageDialog(null, "unable to display bubble");
    }
  }

  private Bubble.Originator getSpeechBubbleOriginator() {
    return m_originator;
  }

  private Bubble.Originator m_originator = createOriginator();

  protected Vector4 getThoughtBubbleOffset() {
    Vector4 offsetAsSeenBySubject = new Vector4();
    AxisAlignedBox bb = ModelImp.this.getAxisAlignedMinimumBoundingBox();
    offsetAsSeenBySubject.x = (bb.getXMinimum() + bb.getXMaximum()) * 0.5;
    offsetAsSeenBySubject.y = bb.getYMaximum();
    offsetAsSeenBySubject.z = (bb.getZMinimum() + bb.getZMaximum()) * 0.5;
    offsetAsSeenBySubject.w = 1.0;
    return offsetAsSeenBySubject;
  }

  protected Vector4 getSpeechBubbleOffset() {
    Vector4 offsetAsSeenBySubject = new Vector4();
    AxisAlignedBox bb = ModelImp.this.getAxisAlignedMinimumBoundingBox();
    offsetAsSeenBySubject.x = (bb.getXMinimum() + bb.getXMaximum()) * 0.5;
    offsetAsSeenBySubject.y = (bb.getYMinimum() + bb.getYMaximum()) * 0.75;
    offsetAsSeenBySubject.z = bb.getZMinimum();
    offsetAsSeenBySubject.w = 1.0;
    return offsetAsSeenBySubject;
  }

  private Bubble.Originator createOriginator() {
    return new Bubble.Originator() {
      @Override
      public void calculate(Point2D.Float out_originOfTail, Point2D.Float out_bodyConnectionLocationOfTail, Point2D.Float out_textBoundsOffset, Bubble bubble, RenderTarget renderTarget, Rectangle actualViewport, AbstractCamera sgCamera, Dimension2D textSize) {
        Vector4 offsetAsSeenBySubject;
        if (bubble instanceof SpeechBubble) {
          offsetAsSeenBySubject = getSpeechBubbleOffset();
        } else if (bubble instanceof ThoughtBubble) {
          offsetAsSeenBySubject = getThoughtBubbleOffset();
        } else {
          offsetAsSeenBySubject = getThoughtBubbleOffset();
        }
        Vector4 offsetAsSeenByCamera = ModelImp.this.getSgComposite().transformTo_New(offsetAsSeenBySubject, sgCamera);
        //      edu.cmu.cs.dennisc.math.Vector4d offsetAsSeenByViewport = m_camera.transformToViewport( m_lookingGlass, offsetAsSeenByCamera );
        Point p = sgCamera.transformToAWT_New(offsetAsSeenByCamera, renderTarget, sgCamera);
        //      float x = (float)( offsetAsSeenByViewport.x / offsetAsSeenByViewport.w );
        //      float y = (float)( offsetAsSeenByViewport.y / offsetAsSeenByViewport.w );

        out_originOfTail.setLocation(p);
        out_bodyConnectionLocationOfTail.setLocation(actualViewport.getWidth() * 0.05, textSize.getHeight() + (actualViewport.getHeight() * 0.05));
        out_textBoundsOffset.setLocation(0f, 0f);
      }
    };
  }

  private BoundingBoxDecorator boundingBoxDecorator;

  protected Leaf getVisualization() {
    if (this.boundingBoxDecorator == null) {
      this.boundingBoxDecorator = new BoundingBoxDecorator();
      this.boundingBoxDecorator.setBox(this.getAxisAlignedMinimumBoundingBox());

      this.addScaleListener(e -> boundingBoxDecorator.setBox(getAxisAlignedMinimumBoundingBox()));
    }
    return this.boundingBoxDecorator;
  }

  public void showVisualization() {
    this.getVisualization().setParent(this.getSgComposite());
  }

  public void hideVisualization() {
    if (this.boundingBoxDecorator != null) {
      this.boundingBoxDecorator.setParent(null);
    }
  }
}
