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
package edu.cmu.cs.dennisc.animation;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Queues;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractAnimator implements Animator {
  private final Queue<WaitingAnimation> waitingAnimations = Queues.newConcurrentLinkedQueue();
  private final List<FrameObserver> frameObservers = Lists.newCopyOnWriteArrayList();

  private double speedFactor = 1.0;
  private double tCurrent;

  protected abstract void updateCurrentTime(boolean isPaused);

  protected final void setCurrentTime(double tCurrent) {
    this.tCurrent = tCurrent;
  }

  @Override
  public final double getCurrentTime() {
    return this.tCurrent;
  }

  @Override
  public double getSpeedFactor() {
    return this.speedFactor;
  }

  @Override
  public void setSpeedFactor(double speedFactor) {
    this.speedFactor = speedFactor;
  }

  @Override
  public boolean isUpdateRequired() {
    return this.waitingAnimations.isEmpty() == false;
  }

  @Override
  public void update() {
    boolean isPaused = this.speedFactor <= 0.0;
    updateCurrentTime(isPaused);
    if (isPaused) {
      //pass
    } else {
      double tCurrent = getCurrentTime();
      if (this.waitingAnimations.size() > 0) {
        //edu.cmu.cs.dennisc.print.PrintUtilities.println( this.waitingAnimations.size() );
        Iterator<WaitingAnimation> iterator = this.waitingAnimations.iterator();
        while (iterator.hasNext()) {
          WaitingAnimation waitingAnimation = iterator.next();
          double tRemaining = waitingAnimation.getAnimation().update(tCurrent, waitingAnimation.getAnimationObserver());
          if (tRemaining > 0.0) {
            //pass
          } else {
            Thread thread = waitingAnimation.getThread();
            if (thread != null) {
              synchronized (thread) {
                thread.notify();
              }
            }
            iterator.remove();
          }
        }
      }
      if (this.frameObservers.size() > 0) {
        for (FrameObserver frameObserver : this.frameObservers) {
          frameObserver.update(tCurrent);
        }
      }
    }
  }

  protected WaitingAnimation createWaitingAnimation(Animation animation, AnimationObserver animationObserver, Thread currentThread) {
    return new WaitingAnimation(animation, animationObserver, currentThread);
  }

  @Override
  public void invokeLater(Animation animation, AnimationObserver animationObserver) {
    WaitingAnimation waitingAnimation = createWaitingAnimation(animation, animationObserver, null);
    this.waitingAnimations.add(waitingAnimation);
  }

  protected boolean isAcceptableThread() {
    return EventQueue.isDispatchThread() == false;
  }

  @Override
  public void invokeAndWait(Animation animation, AnimationObserver animationObserver) throws InterruptedException, InvocationTargetException {
    if (this.isAcceptableThread()) {
      Thread currentThread = Thread.currentThread();
      WaitingAnimation waitingAnimation = createWaitingAnimation(animation, animationObserver, currentThread);
      synchronized (currentThread) {
        this.waitingAnimations.add(waitingAnimation);
        currentThread.wait();
      }
      if (waitingAnimation.getException() != null) {
        throw new InvocationTargetException(waitingAnimation.getException());
      }
    } else {
      Logger.warning("Animation called from AWT event dispatch thread.  Launching as separate thread.");
      new AnimationThread(this, animation, animationObserver).start();
    }
  }

  @Override
  public void invokeAndWait_ThrowRuntimeExceptionsIfNecessary(Animation animation, AnimationObserver animationObserver) {
    try {
      invokeAndWait(animation, animationObserver);
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    } catch (InvocationTargetException ie) {
      throw new RuntimeException(ie);
    }
  }

  @Override
  public Iterable<FrameObserver> getFrameObservers() {
    return this.frameObservers;
  }

  @Override
  public void addFrameObserver(FrameObserver frameObserver) {
    this.frameObservers.add(frameObserver);
  }

  @Override
  public void removeFrameObserver(FrameObserver frameObserver) {
    this.frameObservers.remove(frameObserver);
  }

  public void cancelAnimation() {
    Iterator<WaitingAnimation> iterator = this.waitingAnimations.iterator();
    while (iterator.hasNext()) {
      WaitingAnimation waitingAnimation = iterator.next();
      Thread thread = waitingAnimation.getThread();
      if (thread != null) {
        synchronized (thread) {
          thread.notify();
        }
      }
      iterator.remove();
    }
  }

  @Override
  public void completeAnimations(AnimationObserver animationObserver) {
    Iterator<WaitingAnimation> iterator = this.waitingAnimations.iterator();
    while (iterator.hasNext()) {
      WaitingAnimation waitingAnimation = iterator.next();
      waitingAnimation.getAnimation().complete(waitingAnimation.getAnimationObserver());
      Thread thread = waitingAnimation.getThread();
      if (thread != null) {
        synchronized (thread) {
          thread.notify();
        }
      }
      iterator.remove();
    }
  }

  @Override
  public void completeFrameObservers() {
    if (this.frameObservers.size() > 0) {
      for (FrameObserver frameObserver : this.frameObservers) {
        frameObserver.complete();
      }
    }
  }

  @Override
  public void completeAll(AnimationObserver animationObserver) {
    this.completeAnimations(animationObserver);
    this.completeFrameObservers();
  }
}
