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
package org.alice.stageide.cascade;

import org.alice.stageide.ast.JointedTypeInfo;
import org.alice.stageide.cascade.fillerinners.*;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeMenuModel;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;
import org.lgna.story.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionCascadeManager extends org.alice.ide.cascade.ExpressionCascadeManager {
  public ExpressionCascadeManager() {
    this.addExpressionFillerInner(new ImagePaintFillerInner());
    this.addExpressionFillerInner(new ImageSourceFillerInner());
    this.addExpressionFillerInner(new AudioSourceFillerInner());
    this.addExpressionFillerInner(new ColorFillerInner());
    this.addExpressionFillerInner(new KeyFillerInner());
    this.addExpressionFillerInner(new ArrowKeyListenerFillerInner());
    this.addExpressionFillerInner(new NumberKeyListenerFillerInner());
    this.addExpressionFillerInner(new MouseClickedOnScreenFillerInner());
    this.addExpressionFillerInner(new MouseClickOnObjectFillerInner());
    this.addExpressionFillerInner(new TransformationListenerFillerInner());
    this.addExpressionFillerInner(new ComesIntoViewEventListenerFillerInner());
    this.addExpressionFillerInner(new LeavesViewEventListenerFillerInner());
    this.addExpressionFillerInner(new StartCollisionListenerFillerInner());
    this.addExpressionFillerInner(new EndCollisionListenerFillerInner());
    this.addExpressionFillerInner(new EnterProximityEventListenerFillerInner());
    this.addExpressionFillerInner(new ExitProximityEventListenerFillerInner());
    this.addExpressionFillerInner(new StartOcclusionEventListenerFillerInner());
    this.addExpressionFillerInner(new EndOcclusionEventListenerFillerInner());
    this.addExpressionFillerInner(new SceneActivationEventFillerInner());
    this.addExpressionFillerInner(new TimerEventListenerFillerInner());
    this.addExpressionFillerInner(new KeyListenerFillerInner());

    this.addSimsExpressionFillerInners();
    //this.addExpressionFillerInner( org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner.getInstance( org.lgna.story.Color.class ) );

    this.addRelationalTypeToBooleanFillerInner(SThing.class);
    this.addRelationalTypeToBooleanFillerInner(MoveDirection.class);
    this.addRelationalTypeToBooleanFillerInner(TurnDirection.class);
    this.addRelationalTypeToBooleanFillerInner(RollDirection.class);
    this.addRelationalTypeToBooleanFillerInner(Key.class);
    this.addRelationalTypeToBooleanFillerInner(Color.class);
    this.addRelationalTypeToBooleanFillerInner(Paint.class);
  }

  @Override
  protected AbstractType<?, ?, ?> getEnumTypeForInterfaceType(AbstractType<?, ?, ?> interfaceType) {
    if (interfaceType == JavaType.getInstance(Style.class)) {
      return JavaType.getInstance(AnimationStyle.class);
    } else {
      return super.getEnumTypeForInterfaceType(interfaceType);
    }
  }

  @Override
  protected boolean areEnumConstantsDesired(AbstractType enumType) {
    if (enumType == JavaType.getInstance(Key.class)) {
      return false;
    } else {
      return super.areEnumConstantsDesired(enumType);
    }
  }

  @Override
  protected CascadeMenuModel<Expression> createPartMenuModel(Expression expression, AbstractType<?, ?, ?> desiredType, AbstractType<?, ?, ?> expressionType, boolean isOwnedByCascadeItemMenuCombo) {
    if (expressionType.isAssignableTo(SJointedModel.class)
        && desiredType.isAssignableFrom(SJoint.class)
        && JointedTypeInfo.isJointed(expressionType)) {
      List<JointedTypeInfo> jointedTypeInfos = JointedTypeInfo.getInstances(expressionType);
      return new JointExpressionMenuModel(expression, jointedTypeInfos, 0, isOwnedByCascadeItemMenuCombo);
    }
    if (expressionType.isAssignableTo(SVRUser.class)
        && (desiredType.isAssignableFrom(SVRHand.class) || (desiredType.isAssignableFrom(SVRHeadset.class)))) {
      return new CascadeMenuModel<Expression>(UUID.fromString("2b2c901a-22f3-4080-8a28-3d3b81f326c8")) {
        @Override
        protected void updateBlankChildren(List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode) {
          for (AbstractMethod method : SVRUser.getDeviceMethods(expressionType)) {
            blankChildren.add(JointExpressionFillIn.getInstance(expression, method));
          }
        }
      };
    }
    return null;
  }

  @Override
  protected boolean isApplicableForPartFillIn(AbstractType<?, ?, ?> desiredType, AbstractType<?, ?, ?> expressionType) {
    return desiredType.isAssignableFrom(SJoint.class) && expressionType.isAssignableTo(SJointedModel.class)
        || desiredType.isAssignableFrom(SVRHand.class) && expressionType.isAssignableTo(SVRUser.class)
        || desiredType.isAssignableFrom(SVRHeadset.class) && expressionType.isAssignableTo(SVRUser.class);
  }

  @Override
  protected void appendOtherTypes(List<AbstractType<?, ?, ?>> otherTypes) {
    super.appendOtherTypes(otherTypes);
    otherTypes.add(JavaType.getInstance(SThing.class));

  }

  protected void addSimsExpressionFillerInners() {
  }
}
