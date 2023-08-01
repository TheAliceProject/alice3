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

/**
 * @author Dennis Cosgrove
 */
public class WaitingAnimation {
  private final Animation m_animation;
  private final AnimationObserver m_animationObserver;
  private final Thread m_thread;
  private Exception m_exception;

  public WaitingAnimation(Animation animation, AnimationObserver animationObserver, Thread thread) {
    m_animation = animation;
    m_animationObserver = animationObserver;
    m_thread = thread;
  }

  public Exception getException() {
    return m_exception;
  }

  public void setException(Exception exception) {
    m_exception = exception;
  }

  public Animated getAnimated() {
    return m_animation.getAnimated();
  }

  public boolean update(double tCurrent) {
    double tRemaining =  m_animation.update(tCurrent, m_animationObserver);
    if (tRemaining <= 0.0) {
      notifyNext();
      return true;
    }
    return false;
  }

  public void stop() {
    m_animation.stop();
    notifyNext();
  }

  public void complete() {
    m_animation.complete(m_animationObserver);
    notifyNext();
  }

  public void notifyNext() {
    if (m_thread != null) {
      synchronized (m_thread) {
        m_thread.notify();
      }
    }
  }

  @Override
  public String toString() {
    return this.getClass().getName() + "[animation=" + m_animation + ";observer=" + m_animationObserver + ";thread=" + m_thread + "]";
  }
}
