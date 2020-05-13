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
package org.lgna.story.implementation.eventhandling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.common.ComponentExecutor;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.Visual;
import org.lgna.story.event.AbstractEvent;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public abstract class AbstractEventHandler<L, E extends AbstractEvent> {

  protected boolean shouldFire = true;
  protected Integer count = 0;
  protected final Map<L, MultipleEventPolicy> policyMap = Maps.newConcurrentHashMap();
  protected final Map<L, Map<Object, Boolean>> isFiringMap = Maps.newConcurrentHashMap();
  private final CopyOnWriteArrayList<E> queue = new CopyOnWriteArrayList<>();
  protected final Object NULL_OBJECT = new Object();
  protected SceneImp scene;

  protected void fireEvent(final L listener, final E event, final Object multiEventLock) {
    if (shouldFire) {
      final Object eventLock = multiEventLock == null ? NULL_OBJECT : multiEventLock;
      final Map<Object, Boolean> activeThings = activeThingsFor(listener, eventLock);
      if (!activeThings.get(eventLock)) {
        activeThings.put(eventLock, true);
        newEventCall(listener, event, eventLock).start();
      } else if (policyMap.get(listener).equals(MultipleEventPolicy.COMBINE)) {
        newEventCall(listener, event, eventLock).start();
      } else if (policyMap.get(listener).equals(MultipleEventPolicy.ENQUEUE)) {
        enqueue(event);
      }
    }
  }

  private Map<Object, Boolean> activeThingsFor(L listener, Object eventLock) {
    isFiringMap.computeIfAbsent(listener, k -> new ConcurrentHashMap<>());
    final Map<Object, Boolean> activeThings = isFiringMap.get(listener);
    activeThings.putIfAbsent(eventLock, false);
    return activeThings;
  }

  private ComponentExecutor newEventCall(L listener, E event, Object eventLock) {
    return new ComponentExecutor(() -> {
      fire(listener, event);
      if (policyMap.get(listener).equals(MultipleEventPolicy.ENQUEUE)) {
        fireDequeue(listener);
      }
      isFiringMap.get(listener).put(eventLock, false);
    }, "eventThread");
  }

  protected void enqueue(E event) {
    queue.add(event);
  }

  protected void fireDequeue(L listener) {
    if (queue.size() == 0) {
      return;
    }
    CopyOnWriteArrayList<E> internalQueue = new CopyOnWriteArrayList<>(queue);
    queue.clear();
    while (internalQueue.size() > 0) {
      fire(listener, internalQueue.remove(0));
    }
    fireDequeue(listener);
  }

  protected abstract void fire(L listener, E event);

  public final void silenceListeners() {
    shouldFire = false;
  }

  public final void restoreListeners() {
    shouldFire = true;
  }

  protected void registerIsFiringMap(L eventListener) {
    isFiringMap.put(eventListener, new ConcurrentHashMap<>());
    isFiringMap.get(eventListener).put(eventListener, false);
  }

  protected void registerIsFiringMap(L eventListener, Visual[] targets) {
    isFiringMap.put(eventListener, new ConcurrentHashMap<>());
    if ((targets != null) && (targets.length > 0)) {
      for (Visual target : targets) {
        isFiringMap.get(eventListener).put(target, false);
      }
    }
  }

  protected void registerPolicyMap(L listener, MultipleEventPolicy policy) {
    policyMap.put(listener, policy);
  }

  protected void fireEvent(L listener, E event) {
    fireEvent(listener, event, listener); //used if policy is not constrained by anything else, such as selected model for mouse click events
  }

  public void setScene(SceneImp scene) {
    this.scene = scene;
  }

}
