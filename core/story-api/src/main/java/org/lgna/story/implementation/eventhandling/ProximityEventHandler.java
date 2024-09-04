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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SThing;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityEvent;
import org.lgna.story.event.ProximityExitListener;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class ProximityEventHandler extends AbstractBinaryEventHandler<Object, ProximityEvent, SThing> {
  private final Map<SThing, VerticalPrismCollisionHull> hulls;
  private final Map<Object, Map<SThing, Map<SThing, Boolean>>> wereClose = Maps.newConcurrentHashMap();
  private final Map<Object, Double> listenerDistances = Maps.newConcurrentHashMap();
  private final Map<Object, List<SThing>> listenerToGroupA = Maps.newConcurrentHashMap();

  public ProximityEventHandler(Map<SThing, VerticalPrismCollisionHull> hulls) {
    this.hulls = hulls;
  }

  public void addProximityEventListener(Object listener, List<SThing> groupA, List<SThing> groupB, Double distance, MultipleEventPolicy policy) {
    wereClose.put(listener, new HashMap<>());
    listenerToGroupA.put(listener, groupA);
    listenerDistances.put(listener, distance);
    startTrackingListener(listener, groupA, groupB, policy);
  }

  @Override
  protected void startTrackingInteraction(SThing a, SThing b, Object listener) {
    super.startTrackingInteraction(a, b, listener);
    Map<SThing, Map<SThing, Boolean>> closeEnoughForListener = wereClose.get(listener);
    closeEnoughForListener.computeIfAbsent(a, k -> new HashMap<>());
    closeEnoughForListener.computeIfAbsent(b, k -> new HashMap<>());
  }

  protected void markAsChanged(SThing changedThing) {
    hulls.remove(changedThing);
  }

  @Override
  protected void checkForEvents(SThing changedThing) {
    hulls.remove(changedThing);
    final Map<SThing, Set<Object>> thingsToCheckAgainst = interactionListeners.get(changedThing);
    for (SThing thing : thingsToCheckAgainst.keySet()) {
      for (Object listener : thingsToCheckAgainst.get(thing)) {
        boolean areTheseCloseEnough = areTheseCloseEnough(changedThing, thing, listenerDistances.get(listener));
        final boolean isProximityStart = wasFalse(wereClose.get(listener), changedThing, thing) && areTheseCloseEnough;
        final boolean isProximityEnd = wasTrue(wereClose.get(listener), changedThing, thing) && !areTheseCloseEnough;
        wereClose.get(listener).get(changedThing).put(thing, areTheseCloseEnough);
        wereClose.get(listener).get(thing).put(changedThing, areTheseCloseEnough);
        if (listener instanceof ProximityEnterListener && isProximityStart) {
          if (listenerToGroupA.get(listener).contains(thing)) {
            fireEvent(listener, new EnterProximityEvent(thing, changedThing));
          } else {
            fireEvent(listener, new EnterProximityEvent(changedThing, thing));
          }
        } else if (listener instanceof ProximityExitListener && isProximityEnd) {
          if (listenerToGroupA.get(listener).contains(thing)) {
            fireEvent(listener, new ExitProximityEvent(thing, changedThing));
          } else {
            fireEvent(listener, new ExitProximityEvent(changedThing, thing));
          }
        }
      }
    }
  }

  private boolean areTheseCloseEnough(SThing changedThing, SThing thing, Double proximity) {
    VerticalPrismCollisionHull changedHull = hulls.computeIfAbsent(changedThing, this::newCollisionHull);
    VerticalPrismCollisionHull hull = hulls.computeIfAbsent(thing, this::newCollisionHull);
    return changedHull.isWithinDistance(hull, proximity);
  }

  private VerticalPrismCollisionHull newCollisionHull(SThing thing) {
    return thing.getImplementation().getCollisionHull();
  }

  @Override
  protected void fire(Object listener, ProximityEvent e) {
    if (listener instanceof ProximityEnterListener) {
      ProximityEnterListener enter = (ProximityEnterListener) listener;
      enter.proximityEntered((EnterProximityEvent) e);
    } else if (listener instanceof ProximityExitListener) {
      ProximityExitListener exit = (ProximityExitListener) listener;
      exit.proximityExited((ExitProximityEvent) e);
    }
  }
}
