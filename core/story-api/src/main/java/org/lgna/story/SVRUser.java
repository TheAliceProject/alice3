/*******************************************************************************
 * Copyright (c) 2023, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story;

import org.lgna.common.LgnaIllegalArgumentException;
import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.story.implementation.VrUserImp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dennis Cosgrove
 */
public class SVRUser extends SMovableTurnable implements MutableRider {
  private final VrUserImp implementation = new VrUserImp("VRUser", this);
  private final SVRHeadset headset = new SVRHeadset("VRHeadset", this);
  private final SVRHand leftHand = new SVRHand("LeftHand", this);
  private final SVRHand rightHand = new SVRHand("RightHand", this);

  @Override
  public void setVehicle(SThing vehicle) {
    this.getImplementation().setVehicle(vehicle != null ? vehicle.getImplementation() : null);
  }

  @MethodTemplate(visibility = Visibility.PRIME_TIME)
  public SVRHand getLeftHand() {
    return leftHand;
  }

  @MethodTemplate(visibility = Visibility.PRIME_TIME)
  public SVRHand getRightHand() {
    return rightHand;
  }

  @MethodTemplate(visibility = Visibility.PRIME_TIME)
  public SVRHeadset getHeadset() {
    return headset;
  }

  public static List<AbstractMethod> getDeviceMethods(AbstractType<?, ?, ?> type) {
    return type.getDeclaredMethods().stream()
               .filter(method -> (method.getName().endsWith("Hand") || method.getName().endsWith("Headset")))
               .collect(Collectors.toList());
  }

  @Override
  VrUserImp getImplementation() {
    return this.implementation;
  }


  @GetterTemplate(isPersistent = true)
  @MethodTemplate()
  public Double getScale() {
    return getImplementation().scale.getValue();
  }

  @MethodTemplate()
  public void setScale(Number scale, SetScale.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(scale, 0);
    getImplementation().animateSetScale(scale.doubleValue(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @MethodTemplate()
  public void moveAndOrientToAGoodVantagePointOf(SThing entity, MoveAndOrientToAGoodVantagePointOf.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsNotNull(entity, 0);
    this.implementation.animateSetTransformationToAGoodVantagePointOf(entity.getImplementation(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

}
