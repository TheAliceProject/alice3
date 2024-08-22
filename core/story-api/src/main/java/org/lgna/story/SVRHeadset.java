/*******************************************************************************
 * Copyright (c) 2023 Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.math.AngleInRevolutions;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.implementation.VrHeadsetImp;

public class SVRHeadset extends SThing {
  private final VrHeadsetImp implementation;

  public SVRHeadset(String name, SVRUser parent) {
    implementation = new VrHeadsetImp(name, this, parent.getImplementation());
  }

  @Override
  public VrHeadsetImp getImplementation() {
    return implementation;
  }

  @MethodTemplate()
  public void setFarClippingPlaneDistance(Number distance) {
    getImplementation().getSgCamera().farClippingPlaneDistance.setValue(distance.doubleValue());
  }

  @MethodTemplate()
  public Double getFarClippingPlaneDistance() {
    return getImplementation().getSgCamera().farClippingPlaneDistance.getValue();
  }

  @MethodTemplate()
  public void setNearClippingPlaneDistance(Number distance) {
    getImplementation().getSgCamera().nearClippingPlaneDistance.setValue(distance.doubleValue());
  }

  @MethodTemplate()
  public Double getNearClippingPlaneDistance() {
    return getImplementation().getSgCamera().nearClippingPlaneDistance.getValue();
  }

  @MethodTemplate()
  public void setHorizontalViewingAngle(Number angle) {
    getImplementation().getSgCamera().horizontalViewingAngle.setValue(new AngleInRevolutions(angle.doubleValue()));
  }

  @MethodTemplate()
  public Double getHorizontalViewingAngle() {
    return getImplementation().getSgCamera().horizontalViewingAngle.getValue().getAsRevolutions();
  }

  @MethodTemplate()
  public Double getVerticalViewingAngle() {
    return getImplementation().getSgCamera().verticalViewingAngle.getValue().getAsRevolutions();
  }

  @MethodTemplate()
  public void setVerticalViewingAngle(Number angle) {
    getImplementation().getSgCamera().verticalViewingAngle.setValue(new AngleInRevolutions(angle.doubleValue()));
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public Orientation getOrientationRelativeToVehicle() {
    return new Orientation(this.getImplementation().getLocalOrientation());
  }

  // Based on STurnable, but not accessible and there is always a vehicle, the VRUser
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public void setOrientationRelativeToVehicle(Orientation orientation, SetOrientationRelativeToVehicle.Detail... details) {
    implementation.animateOrientationOnly(implementation.getVehicle(),
        orientation.getInternal(),
        Duration.getValue(details),
        AnimationStyle.getValue(details).getInternal());
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public Position getPositionRelativeToVehicle() {
    return new Position(implementation.getLocalPosition());
  }

  // Based on SMovableTurnable, but not accessible and there is always a vehicle, the VRUser
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public void setPositionRelativeToVehicle(Position position, SetPositionRelativeToVehicle.Detail... details) {
    implementation.animatePositionOnly(implementation.getVehicle(),
        position.getInternal(),
        PathStyle.getValue(details).isSmooth(),
        Duration.getValue(details),
        AnimationStyle.getValue(details).getInternal());
  }
}
