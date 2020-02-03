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

package org.lgna.story;

/**
 * @author Dennis Cosgrove
 */
public class Duration implements
                      //Turnable
                          Turn.Detail, Roll.Detail, OrientTo.Detail, TurnToFace.Detail, OrientToUpright.Detail, PointAt.Detail, SetOrientationRelativeToVehicle.Detail,
                          //MoveableTurnable
                          Move.Detail, MoveToward.Detail, MoveAwayFrom.Detail, MoveTo.Detail, MoveAndOrientTo.Detail, SetPositionRelativeToVehicle.Detail, Place.Detail,
                          //Visual
                          SetPaint.Detail, SetOpacity.Detail,
                          //Resizable
                          SetScale.Detail, SetSize.Detail, SetWidth.Detail, SetHeight.Detail, SetDepth.Detail, Resize.Detail, ResizeWidth.Detail, ResizeHeight.Detail, ResizeDepth.Detail,
                          //JointedModel
                          StraightenOutJoints.Detail, Say.Detail, Think.Detail,
                          //Biped (for now?)
                          StrikePose.Detail,
                          //Flyer
                          UnfoldWings.Detail,
                          //Room
                          SetFloorPaint.Detail, SetWallPaint.Detail, SetCeilingPaint.Detail,
                          //Billboard
                          SetBackPaint.Detail,
                          //Camera,
                          MoveAndOrientToAGoodVantagePointOf.Detail,
                          //Scene
                          SetAtmosphereColor.Detail, SetAmbientLightColor.Detail, SetFogDensity.Detail,
                          //Sphere,Disc
                          SetRadius.Detail,
                          //Torus
                          SetInnerRadius.Detail, SetOuterRadius.Detail,
                          //Cone
                          SetBaseRadius.Detail, SetLength.Detail {
  private static final double DEFAULT_VALUE = 1.0;
  private final double value;

  public Duration(Number value) {
    this.value = value.doubleValue();
  }

  private static double getValue(Object[] details, double defaultValue) {
    for (Object detail : details) {
      if (detail instanceof Duration) {
        Duration duration = (Duration) detail;
        return duration.value;
      }
    }
    return defaultValue;
  }

  /* package-private */
  static double getValue(Object[] details) {
    return getValue(details, DEFAULT_VALUE);
  }

  //Testing a different way to make keyword parameters on the java side. For use in exported NetBeans project.
  public static Duration make(Number value) {
    return new Duration(value);
  }
}
