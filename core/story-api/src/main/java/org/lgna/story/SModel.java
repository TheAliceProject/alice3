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

import org.lgna.common.LgnaIllegalArgumentException;
import org.lgna.project.annotations.GetterTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.annotation.PortionDetails;
import org.lgna.story.implementation.ModelImp;

/**
 * @author Dennis Cosgrove
 */
public abstract class SModel extends SMovableTurnable implements MutableRider, Resizable, VisualWithPaint {
  @Override
  /* package-private */ abstract ModelImp getImplementation();

  @Override
  public void setVehicle(SThing vehicle) {
    this.getImplementation().setVehicle(vehicle != null ? vehicle.getImplementation() : null);
  }

  //VisualWithPaint interface
  @Override
  @MethodTemplate()
  @GetterTemplate(isPersistent = true)
  public Paint getPaint() {
    return this.getImplementation().paint.getValue();
  }

  @Override
  @MethodTemplate()
  public void setPaint(Paint paint, SetPaint.Detail... details) {
    this.getImplementation().paint.animateValue(paint, Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  //Visual interface
  @Override
  @MethodTemplate()
  @GetterTemplate(isPersistent = true)
  @ValueTemplate(detailsEnumCls = PortionDetails.class)
  public Double getOpacity() {
    return (double) this.getImplementation().opacity.getValue();
  }

  @Override
  @MethodTemplate()
  public void setOpacity(Number opacity, SetOpacity.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsBetween0and1(opacity, 0);
    this.getImplementation().opacity.animateValue(opacity.floatValue(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  //Resizable interface
  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public Scale getScale() {
    return Scale.createInstance(this.getImplementation().getScale());
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void setScale(Scale scale, SetScale.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsNotNull(scale, 0);
    this.getImplementation().animateSetScale(Scale.getInternal(scale), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public Size getSize() {
    return Size.createInstance(this.getImplementation().getSize());
  }

  @Override
  @MethodTemplate(visibility = Visibility.TUCKED_AWAY)
  public void setSize(Size size, SetSize.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsNotNull(size, 0);
    this.getImplementation().animateSetSize(Size.getInternal(size), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public Double getWidth() {
    return this.getImplementation().getSize().x;
  }

  @Override
  @MethodTemplate()
  public void setWidth(Number width, SetWidth.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(width, 0);
    SetDimensionPolicy policy = SetDimensionPolicy.getValue(details);
    //todo: allow for 0.0
    this.getImplementation().animateSetWidth(width.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public Double getHeight() {
    return this.getImplementation().getSize().y;
  }

  @Override
  @MethodTemplate()
  public void setHeight(Number height, SetHeight.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(height, 0);
    SetDimensionPolicy policy = SetDimensionPolicy.getValue(details);
    //todo: allow for 0.0
    this.getImplementation().animateSetHeight(height.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public Double getDepth() {
    return this.getImplementation().getSize().z;
  }

  @Override
  @MethodTemplate()
  public void setDepth(Number depth, SetDepth.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(depth, 0);
    SetDimensionPolicy policy = SetDimensionPolicy.getValue(details);
    //todo: allow for 0.0
    this.getImplementation().animateSetDepth(depth.doubleValue(), policy.isVolumePreserved(), policy.isAspectRatioPreserved(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public void resize(Number factor, Resize.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(factor, 0);
    //todo: explain how to make things smaller
    this.getImplementation().animateResize(factor.doubleValue(), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public void resizeWidth(Number factor, ResizeWidth.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(factor, 0);
    //todo: explain how to make things smaller
    this.getImplementation().animateResizeWidth(factor.doubleValue(), IsVolumePreserved.getValue(details), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public void resizeHeight(Number factor, ResizeHeight.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(factor, 0);
    //todo: explain how to make things smaller
    this.getImplementation().animateResizeHeight(factor.doubleValue(), IsVolumePreserved.getValue(details), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @Override
  @MethodTemplate()
  public void resizeDepth(Number factor, ResizeDepth.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsPositive(factor, 0);
    //todo: explain how to make things smaller
    this.getImplementation().animateResizeDepth(factor.doubleValue(), IsVolumePreserved.getValue(details), Duration.getValue(details), AnimationStyle.getValue(details).getInternal());
  }

  @MethodTemplate()
  public void say(String text, Say.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsNotNull(text, 0);
    double textScale = TextScale.getValue(details);
    int textSize = (int) (16 * textScale);
    int textStyle = TextStyle.getValue(details).getInternal();
    this.getImplementation().say(text, Duration.getValue(details), TextFont.getValue(details, null, textStyle, textSize).getAsAWTFont(), TextColor.getValue(details, Color.BLACK).getInternal(), BubbleFillColor.getValue(details, Color.WHITE).getInternal(), BubbleOutlineColor.getValue(details, Color.WHITE).getInternal(), BubblePosition.getValue(details).getInternal());
  }

  @MethodTemplate()
  public void think(String text, Think.Detail... details) {
    LgnaIllegalArgumentException.checkArgumentIsNotNull(text, 0);
    double textScale = TextScale.getValue(details);
    int textSize = (int) (16 * textScale);
    int textStyle = TextStyle.getValue(details).getInternal();
    this.getImplementation().think(text, Duration.getValue(details), TextFont.getValue(details, null, textStyle, textSize).getAsAWTFont(), TextColor.getValue(details, Color.BLACK).getInternal(), BubbleFillColor.getValue(details, Color.WHITE).getInternal(), BubbleOutlineColor.getValue(details, Color.WHITE).getInternal(), BubblePosition.getValue(details).getInternal());
  }

  //TODO: Get this to work
  //  @MethodTemplate()
  //  public void sayOutLoud( String text, org.alice.flite.VoiceType voice, SayOutLoud.Detail... details ) {
  //    SpeechBubble bubble = null;
  //    if (ShowSpeechBubble.getValue(details, true))
  //    {
  //      bubble = new SpeechBubble();
  //      bubble.text.setValue(text);
  //      initializeBubble(bubble, details);
  //    }
  //    this.getImplementation().sayText(text, voice, bubble);
  //  }
}
