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

package org.alice.stageide.oneshot;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.ast.sort.OneShotSorter;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;
import org.lgna.story.SCamera;
import org.lgna.story.SFlyer;
import org.lgna.story.SGround;
import org.lgna.story.SJointedModel;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.STurnable;

import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class MethodInvocationBlank extends CascadeBlank<MethodInvocationEditFactory> {
  private static Map<InstanceFactory, MethodInvocationBlank> map = Maps.newHashMap();

  public static MethodInvocationBlank getInstance(InstanceFactory value) {
    synchronized (map) {
      MethodInvocationBlank rv = map.get(value);
      if (rv == null) {
        rv = new MethodInvocationBlank(value);
        map.put(value, rv);
      }
      return rv;
    }
  }

  private final InstanceFactory instanceFactory;

  private MethodInvocationBlank(InstanceFactory instanceFactory) {
    this.instanceFactory = instanceFactory;
  }

  @Override
  protected void updateChildren(List<CascadeBlankChild> children, BlankNode<MethodInvocationEditFactory> blankNode) {
    JavaType turnableType = JavaType.getInstance(STurnable.class);
    JavaType movableTurnableType = JavaType.getInstance(SMovableTurnable.class);
    JavaType jointedModelType = JavaType.getInstance(SJointedModel.class);
    JavaType flyerType = JavaType.getInstance(SFlyer.class);
    JavaType cameraType = JavaType.getInstance(SCamera.class);
    JavaType groundType = JavaType.getInstance(SGround.class);
    JavaType modelType = JavaType.getInstance(SModel.class);

    AbstractType<?, ?, ?> instanceFactoryValueType = this.instanceFactory.getValueType();
    List<JavaMethod> methods = Lists.newLinkedList();
    Map<MethodInvocation, List<SimpleArgument>> poseMethods = Maps.newHashMap();
    if (turnableType.isAssignableFrom(instanceFactoryValueType)) {
      methods.add(OneShotSorter.TURN_METHOD);
      methods.add(OneShotSorter.ROLL_METHOD);
      methods.add(OneShotSorter.TURN_TO_FACE_METHOD);
      methods.add(OneShotSorter.POINT_AT_METHOD);
      methods.add(OneShotSorter.ORIENT_TO_METHOD);
      methods.add(OneShotSorter.ORIENT_TO_UPRIGHT_METHOD);
    }
    if (movableTurnableType.isAssignableFrom(instanceFactoryValueType)) {
      methods.add(OneShotSorter.MOVE_METHOD);
      methods.add(OneShotSorter.MOVE_TOWARD_METHOD);
      methods.add(OneShotSorter.MOVE_AWAY_FROM_METHOD);
      methods.add(OneShotSorter.MOVE_TO_METHOD);
      methods.add(OneShotSorter.MOVE_AND_ORIENT_TO_METHOD);
      methods.add(OneShotSorter.PLACE_METHOD);
    }

    if (jointedModelType.isAssignableFrom(instanceFactoryValueType)) {
      methods.add(OneShotSorter.STRAIGHTEN_OUT_JOINTS_METHOD);

      if (flyerType.isAssignableFrom(instanceFactoryValueType)) {
        methods.add(OneShotSorter.SPREAD_WINGS_METHOD);
        methods.add(OneShotSorter.FOLD_WINGS_METHOD);
      }

      //Search the UserMethods on the type looking for generated pose animations
      //Grab the java method invocation (strikePose) inside the pose animation and add it to the methodInvocations list
      List<AbstractMethod> declaredMethods = AstUtilities.getAllMethods(instanceFactoryValueType);
      for (AbstractMethod method : declaredMethods) {
        if (method instanceof UserMethod) {
          UserMethod userMethod = (UserMethod) method;
          //Pose animations are GENERATED and have no return value
          if ((userMethod.managementLevel.getValue() == ManagementLevel.GENERATED) && (userMethod.getReturnType() == JavaType.VOID_TYPE)) {
            //UserMethod pose animations contain a single JavaMethod in their body called "strikePose"
            //Grab the first statement in the body and check to see if it's actually a pose call
            Statement poseStatement = userMethod.body.getValue().statements.get(0);
            if (poseStatement instanceof ExpressionStatement) {
              ExpressionStatement expressionStatement = (ExpressionStatement) poseStatement;
              Expression expression = expressionStatement.expression.getValue();
              if (expression instanceof MethodInvocation) {
                MethodInvocation poseInvocation = (MethodInvocation) expression;
                if ("strikePose".equals(poseInvocation.method.getValue().getName())) {
                  if (poseInvocation.method.getValue() instanceof JavaMethod) {
                    List<SimpleArgument> arguments = poseInvocation.requiredArguments.getValue();
                    poseMethods.put(poseInvocation, arguments);
                  }
                }
              }
            }
          }
        }
      }

    }
    if (cameraType.isAssignableFrom(instanceFactoryValueType)) {
      methods.add(OneShotSorter.MOVE_AND_ORIENT_TO_A_GOOD_VANTAGE_POINT_METHOD);
    }

    if (groundType.isAssignableFrom(instanceFactoryValueType)) {
      methods.add(OneShotSorter.GROUND_SET_PAINT_METHOD);
      methods.add(OneShotSorter.GROUND_SET_OPACITY_METHOD);
    }
    NebulousIde.nonfree.addRoomMethods(instanceFactoryValueType, methods);
    if (modelType.isAssignableFrom(instanceFactoryValueType)) {
      methods.add(OneShotSorter.MODEL_SET_PAINT_METHOD);
      methods.add(OneShotSorter.MODEL_SET_OPACITY_METHOD);
    }

    List<JavaMethod> sortedMethods = OneShotSorter.SINGLETON.createSortedList(methods);
    for (JavaMethod method : sortedMethods) {
      if (method != null) {
        CascadeBlankChild<?> roomFillin = NebulousIde.nonfree.getRoomFillIns(method, this.instanceFactory);
        //todo
        if (method == OneShotSorter.STRAIGHTEN_OUT_JOINTS_METHOD) {
          children.add(AllJointLocalTransformationsMethodInvocationFillIn.getInstance(this.instanceFactory, method));
        } else if ("setPaint".equals(method.getName())) {
          children.add(SetPaintMethodInvocationFillIn.getInstance(this.instanceFactory, method));
        } else if (roomFillin != null) {
          children.add(roomFillin);
        } else if ("setOpacity".equals(method.getName())) {
          children.add(SetOpacityMethodInvocationFillIn.getInstance(this.instanceFactory, method));
        } else if ("foldWings".equals(method.getName()) || "spreadWings".equals(method.getName())) {
          children.add(JavaDefinedStrikePoseMethodInvocationFillIn.getInstance(this.instanceFactory, method));
        } else {
          children.add(LocalTransformationMethodInvocationFillIn.getInstance(this.instanceFactory, method));
        }
      } else {
        children.add(CascadeLineSeparator.getInstance());
      }
    }
    for (Map.Entry<MethodInvocation, List<SimpleArgument>> poseMethodEntry : poseMethods.entrySet()) {
      children.add(StrikePoseMethodInvocationFillIn.getInstance(this.instanceFactory, (JavaMethod) poseMethodEntry.getKey().method.getValue(), poseMethodEntry.getValue()));
    }
  }
}
