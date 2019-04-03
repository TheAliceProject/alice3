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

import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.media.MediaFactory;
import org.lgna.common.resources.AudioResource;
import org.lgna.project.annotations.ConstructorTemplate;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.annotation.VolumeLevelDetails;

/**
 * @author Dennis Cosgrove
 */
public final class AudioSource {
  public static boolean isWithinReasonableEpsilonOfDefaultVolume(double volume) {
    return EpsilonUtilities.isWithinReasonableEpsilon(MediaFactory.DEFAULT_VOLUME, volume);
  }

  public static boolean isWithinReasonableEpsilonOfDefaultStartTime(double startTime) {
    return EpsilonUtilities.isWithinReasonableEpsilon(MediaFactory.DEFAULT_START_TIME, startTime);
  }

  public static boolean isDefaultStopTime_aka_NaN(double stopTime) {
    return Double.isNaN(stopTime);
  }

  private final AudioResource audioResource;
  private final Double volume;
  private final Double startTime;
  private final Double stopTime;

  @ConstructorTemplate()
  public AudioSource(AudioResource audioResource, @ValueTemplate(detailsEnumCls = VolumeLevelDetails.class) Number volume, Number startTime, Number stopTime) {
    this.audioResource = audioResource;
    this.volume = volume.doubleValue();
    if (startTime != null) {
      this.startTime = startTime.doubleValue();
    } else {
      this.startTime = Double.NaN;
    }
    if (stopTime != null) {
      this.stopTime = stopTime.doubleValue();
    } else {
      this.stopTime = Double.NaN;
    }
  }

  @ConstructorTemplate()
  public AudioSource(AudioResource audioResource, @ValueTemplate(detailsEnumCls = VolumeLevelDetails.class) Number volume, Number startTime) {
    this(audioResource, volume, startTime, MediaFactory.DEFAULT_STOP_TIME);
  }

  @ConstructorTemplate()
  public AudioSource(AudioResource audioResource, @ValueTemplate(detailsEnumCls = VolumeLevelDetails.class) Number volume) {
    this(audioResource, volume, MediaFactory.DEFAULT_START_TIME);
  }

  @ConstructorTemplate(isFollowedByLongerConstructor = true)
  public AudioSource(AudioResource audioResource) {
    this(audioResource, MediaFactory.DEFAULT_VOLUME);
  }

  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public AudioResource getAudioResource() {
    return this.audioResource;
  }

  public Double getVolume() {
    return this.volume;
  }

  public Double getStartTime() {
    return this.startTime;
  }

  public Double getStopTime() {
    return this.stopTime;
  }
}
