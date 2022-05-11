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
package edu.cmu.cs.dennisc.media.jmf;

import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import org.lgna.common.resources.AudioResource;

import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.GainControl;
import javax.media.RealizeCompleteEvent;
import javax.media.StopEvent;
import javax.media.Time;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

abstract class BarrierControllerListener implements ControllerListener {
  private final CyclicBarrier barrier = new CyclicBarrier(2);

  public void await() {
    try {
      barrier.await();
    } catch (InterruptedException | BrokenBarrierException ie) {
      throw new RuntimeException(ie);
    }
  }
}

class RealizeControllerListener extends BarrierControllerListener {
  @Override
  public void controllerUpdate(ControllerEvent e) {
    if (e instanceof RealizeCompleteEvent) {
      this.await();
    }
  }
}

class StopControllerListener extends BarrierControllerListener {
  @Override
  public void controllerUpdate(ControllerEvent e) {
    //todo?
    if (e instanceof StopEvent) {
      this.await();
    }
  }
}

/**
 * @author Dennis Cosgrove
 */
public class Player extends edu.cmu.cs.dennisc.media.Player {
  private final javax.media.Player player;
  private final double volumeLevel;
  private final double startTime;
  private final double stopTime;
  private final AudioResource audioResource;

  Player(javax.media.Player player, double volumeLevel, double startTime, double stopTime, AudioResource resourceReference) {
    this.player = player;
    this.volumeLevel = volumeLevel;
    this.startTime = startTime;
    this.stopTime = stopTime;
    this.audioResource = resourceReference;
  }

  @Override
  public void realize() {
    if (this.player.getState() < Controller.Realized) {
      PrintUtilities.println("realize: ", this.player.getState());
      RealizeControllerListener controllerListener = new RealizeControllerListener();
      this.player.addControllerListener(controllerListener);
      this.player.realize();
      controllerListener.await();
      this.player.removeControllerListener(controllerListener);
    }
  }

  @Override
  public void start() {
    this.realize();
    if (!Double.isNaN(this.startTime)) {
      this.player.setMediaTime(new Time(this.startTime));
    }
    if (!Double.isNaN(this.stopTime)) {
      this.player.setStopTime(new Time(this.stopTime));
    }
    if (!EpsilonUtilities.isWithinReasonableEpsilon(this.volumeLevel, 1.0)) {
      GainControl gainControl = this.player.getGainControl();
      float defaultVolumeLevel = gainControl.getLevel();

      float v = (float) (this.volumeLevel * defaultVolumeLevel);
      v = Math.max(v, 0.0f);
      v = Math.min(v, 1.0f);
      gainControl.setLevel(v);
    }
    this.player.start();
  }

  @Override
  public void stop() {
    this.player.stop();
    this.player.close();
  }

  @Override
  public double getDuration() {
    return this.player.getDuration().getSeconds();
  }

  private static final double CONSIDERED_TO_BE_STARTED_THRESHOLD = 0.1;

  @Override
  public double getTimeRemaining() {
    Time duration = this.player.getDuration();
    Time stop = this.player.getStopTime();
    Time curr = this.player.getMediaTime();

    double endSeconds = Math.min(duration.getSeconds(), stop.getSeconds());
    double currSeconds = curr.getSeconds();
    double rv = endSeconds - currSeconds;

    int state = this.player.getState();
    if (state < Controller.Started && currSeconds > (this.startTime + CONSIDERED_TO_BE_STARTED_THRESHOLD)) {
      rv = 0.0;
    }
    return rv;
  }

  @Override
  public void playUntilStop() {
    StopControllerListener controllerListener = new StopControllerListener();
    this.player.addControllerListener(controllerListener);
    this.start();
    controllerListener.await();
    this.player.removeControllerListener(controllerListener);
  }

  public double getVolumeLevel() {
    return this.volumeLevel;
  }

  public double getStartTime() {
    return this.startTime;
  }

  public double getStopTime() {
    return this.stopTime;
  }

  public AudioResource getAudioResource() {
    return this.audioResource;
  }

}
