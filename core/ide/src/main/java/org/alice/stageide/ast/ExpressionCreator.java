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

package org.alice.stageide.ast;

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import org.lgna.common.resources.ImageResource;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaConstructor;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.ResourceExpression;
import org.lgna.story.Color;
import org.lgna.story.Font;
import org.lgna.story.ImagePaint;
import org.lgna.story.ImageSource;
import org.lgna.story.Orientation;
import org.lgna.story.Paint;
import org.lgna.story.Pose;
import org.lgna.story.PoseBuilder;
import org.lgna.story.Position;
import org.lgna.story.Scale;
import org.lgna.story.Size;
import org.lgna.story.fontattributes.Attribute;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.PoseUtilities;

import edu.cmu.cs.dennisc.math.UnitQuaternion;
import org.lgna.story.resources.JointId;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionCreator extends org.alice.ide.ast.ExpressionCreator {
  private Expression createPositionExpression(Position position) {
    if (position != null) {
      Class<?> cls = Position.class;
      JavaConstructor constructor = JavaConstructor.getInstance(cls, Number.class, Number.class, Number.class);
      return AstUtilities.createInstanceCreation(constructor, this.createDoubleExpression(position.getRight(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(position.getUp(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(position.getBackward(), MILLI_DECIMAL_PLACES));
    } else {
      return new NullLiteral();
    }
  }

  private Expression createOrientationExpression(Orientation orientation) {
    if (orientation != null) {
      UnitQuaternion q = orientation.createUnitQuaternion();
      Class<?> cls = Orientation.class;
      JavaConstructor constructor = JavaConstructor.getInstance(cls, Number.class, Number.class, Number.class, Number.class);
      return AstUtilities.createInstanceCreation(constructor, this.createDoubleExpression(q.x, MICRO_DECIMAL_PLACES), this.createDoubleExpression(q.y, MICRO_DECIMAL_PLACES), this.createDoubleExpression(q.z, MICRO_DECIMAL_PLACES), this.createDoubleExpression(q.w, MICRO_DECIMAL_PLACES));
    } else {
      return new NullLiteral();
    }
  }

  private Expression createScaleExpression(Scale scale) {
    if (scale != null) {
      Class<?> cls = Scale.class;
      JavaConstructor constructor = JavaConstructor.getInstance(cls, Number.class, Number.class, Number.class);
      return AstUtilities.createInstanceCreation(constructor, this.createDoubleExpression(scale.getLeftToRight(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(scale.getBottomToTop(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(scale.getFrontToBack(), MILLI_DECIMAL_PLACES));
    } else {
      return new NullLiteral();
    }
  }

  private Expression createSizeExpression(Size size) {
    if (size != null) {
      Class<?> cls = Size.class;
      JavaConstructor constructor = JavaConstructor.getInstance(cls, Number.class, Number.class, Number.class);
      return AstUtilities.createInstanceCreation(constructor, this.createDoubleExpression(size.getLeftToRight(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(size.getBottomToTop(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(size.getFrontToBack(), MILLI_DECIMAL_PLACES));
    } else {
      return new NullLiteral();
    }
  }

  private Expression createFontExpression(Font font) throws CannotCreateExpressionException {
    Class<?> cls = Font.class;
    JavaConstructor constructor = JavaConstructor.getInstance(cls, Attribute[].class);
    return AstUtilities.createInstanceCreation(constructor, this.createExpression(font.getFamily()), this.createExpression(font.getWeight()), this.createExpression(font.getPosture()));
  }

  private Expression createColorExpression(Color color) {
    Expression rv = null;
    Class<?> cls = Color.class;
    for (Field fld : ReflectionUtilities.getPublicStaticFinalFields(cls, cls)) {
      if (ReflectionUtilities.get(fld, null).equals(color)) {
        rv = this.createPublicStaticFieldAccess(fld);
        break;
      }
    }
    if (rv == null) {
      JavaConstructor constructor = JavaConstructor.getInstance(cls, Number.class, Number.class, Number.class);
      rv = AstUtilities.createInstanceCreation(constructor, this.createDoubleExpression(color.getRed(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(color.getGreen(), MILLI_DECIMAL_PLACES), this.createDoubleExpression(color.getBlue(), MILLI_DECIMAL_PLACES));
    }
    return rv;
  }

  private Expression createImageSourceExpression(ImageSource imageSource) {
    if (imageSource != null) {
      JavaConstructor constructor = JavaConstructor.getInstance(ImageSource.class, ImageResource.class);
      Expression arg0Expression;
      ImageResource imageResource = imageSource.getImageResource();
      if (imageResource != null) {
        arg0Expression = new ResourceExpression(ImageResource.class, imageResource);
      } else {
        arg0Expression = new NullLiteral();
      }
      return AstUtilities.createInstanceCreation(constructor, arg0Expression);
    } else {
      return new NullLiteral();
    }
  }

  private Expression createImagePaintExpression(ImagePaint imagePaint) throws CannotCreateExpressionException {
    if (imagePaint != null) {
      if (imagePaint instanceof Enum<?>) {
        return this.createEnumExpression((Enum<?>) imagePaint);
      } else {
        throw new CannotCreateExpressionException(imagePaint);
      }
    } else {
      return new NullLiteral();
    }
  }

  private Expression createPaintExpression(Paint paint) throws CannotCreateExpressionException {
    if (paint != null) {
      if (paint instanceof Color) {
        Color color = (Color) paint;
        return createColorExpression(color);
      } else if (paint instanceof ImagePaint) {
        ImagePaint imagePaint = (ImagePaint) paint;
        return this.createImagePaintExpression(imagePaint);
      } else if (paint instanceof ImageSource) {
        ImageSource imageSource = (ImageSource) paint;
        return this.createImageSourceExpression(imageSource);
      } else {
        throw new CannotCreateExpressionException(paint);
      }
    } else {
      return new NullLiteral();
    }
  }

  private Expression createJointIdExpression(JointId jointId) throws CannotCreateExpressionException {
    Field fld = jointId.getPublicStaticFinalFld();
    if (fld != null) {
      return this.createPublicStaticFieldAccess(fld);
    } else {
      throw new CannotCreateExpressionException(jointId);
    }
  }

  //private static final org.lgna.project.ast.JavaMethod ADD_CUSTOM = org.lgna.project.ast.JavaMethod.getInstance( PoseBuilder.class, "arbitraryJoint", JointId.class, Orientation.class );
  private static final JavaMethod BUILD = JavaMethod.getInstance(PoseBuilder.class, "build");

  private Expression createPoseExpression(Pose<?> pose) throws CannotCreateExpressionException {
    if ((pose != null) && (pose.getJointIdTransformationPairs().length > 0)) {
      Class<? extends PoseBuilder> builderCls = PoseUtilities.getBuilderClassForPoseClass(pose.getClass());
      InstanceCreation builderExpression0 = AstUtilities.createInstanceCreation(builderCls);
      Expression prevExpression = null;
      for (JointIdTransformationPair jtPair : (pose.getJointIdTransformationPairs())) {

        //NOTE: this does not take into account that poses may affect translation as well.
        //TODO: check jtPair.affectsTranslation() to see if creating a different pose entry is necessary
        UnitQuaternion q = jtPair.getTransformation().orientation.createUnitQuaternion();
        Orientation orientation = new Orientation(q.x, q.y, q.z, q.w);

        Expression callerExpression = prevExpression == null ? builderExpression0 : prevExpression;
        Method jSpecificMethod = PoseUtilities.getSpecificPoseBuilderMethod(builderCls, jtPair.getJointId());
        if (jSpecificMethod != null) {
          prevExpression = AstUtilities.createMethodInvocation(callerExpression, JavaMethod.getInstance(jSpecificMethod), this.createOrientationExpression(orientation));
        } else {
          Method jCatchAllMethod = PoseUtilities.getCatchAllPoseBuilderMethod(builderCls);
          if (jCatchAllMethod != null) {
            prevExpression = AstUtilities.createMethodInvocation(callerExpression, JavaMethod.getInstance(jCatchAllMethod), this.createJointIdExpression(jtPair.getJointId()), this.createOrientationExpression(orientation));
          } else {
            //should not happen
            //throw new CannotCreateExpressionException( pose );
            throw new Error("cannot find catch all method for pose builder " + pose + " " + builderCls);
          }
        }
      }
      assert prevExpression != null;
      return AstUtilities.createMethodInvocation(prevExpression, BUILD);
    } else {
      return new NullLiteral();
    }
  }

  @Override
  protected Expression createCustomExpression(Object value) throws CannotCreateExpressionException {

    if (value instanceof Position) {
      return this.createPositionExpression((Position) value);
    } else if (value instanceof Orientation) {
      return this.createOrientationExpression((Orientation) value);
    } else if (value instanceof Scale) {
      return this.createScaleExpression((Scale) value);
    } else if (value instanceof Size) {
      return this.createSizeExpression((Size) value);
    } else if (value instanceof Paint) {
      return this.createPaintExpression((Paint) value);
    } else if (value instanceof Font) {
      return this.createFontExpression((Font) value);
    } else if (value instanceof Pose<?>) {
      return this.createPoseExpression((Pose<?>) value);
    } else if (value instanceof JointId) {
      return this.createJointIdExpression((JointId) value);
    } else {
      throw new CannotCreateExpressionException(value);
    }
  }
}
