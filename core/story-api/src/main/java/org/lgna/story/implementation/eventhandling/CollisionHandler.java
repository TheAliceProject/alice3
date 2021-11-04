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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SThing;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.StartCollisionEvent;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class CollisionHandler extends AbstractBinaryEventHandler<Object, CollisionEvent, SThing> {
  private final Map<SThing, VerticalPrismCollisionHull> hulls;
  private final Map<SThing, Map<SThing, Boolean>> wereTouching = Maps.newConcurrentHashMap();
  private final Map<Object, List<SThing>> listenerToGroupA = Maps.newConcurrentHashMap();

  public CollisionHandler(Map<SThing, VerticalPrismCollisionHull> hulls) {
    this.hulls = hulls;
  }

  public void addCollisionListener(Object collisionListener, List<SThing> groupA, List<SThing> groupB, MultipleEventPolicy policy) {
    startTrackingListener(collisionListener, groupA, groupB, policy);
    listenerToGroupA.put(collisionListener, groupA);
  }

  @Override
  protected void startTrackingThing(SThing thing) {
    super.startTrackingThing(thing);
    if (wereTouching.get(thing) == null) {
      wereTouching.put(thing, new ConcurrentHashMap<>());
    }
  }

  @Override
  protected void checkForEvents(SThing changedThing) {
    VerticalPrismCollisionHull hull = getUpdatedHull(changedThing);
    final Map<SThing, Set<Object>> thingsToCollideWith = interactionListeners.get(changedThing);
    for (SThing thing : thingsToCollideWith.keySet()) {
      Set<Object> listeners = thingsToCollideWith.get(thing);
      boolean doTheseCollide = hull.collidesWith(collisionHull(thing));
      final boolean isCollisionStart = wasFalse(wereTouching, changedThing, thing) && doTheseCollide;
      final boolean isCollisionEnd = wasTrue(wereTouching, changedThing, thing) && !doTheseCollide;
      wereTouching.get(thing).put(changedThing, doTheseCollide);
      wereTouching.get(changedThing).put(thing, doTheseCollide);
      for (Object listener : listeners) {
        if (isCollisionStart && listener instanceof CollisionStartListener) {
          if (listenerToGroupA.get(listener).contains(thing)) {
            fireEvent(listener, new StartCollisionEvent(thing, changedThing));
          } else {
            fireEvent(listener, new StartCollisionEvent(changedThing, thing));
          }
        }
        if (isCollisionEnd && listener instanceof CollisionEndListener) {
          if (listenerToGroupA.get(listener).contains(thing)) {
            fireEvent(listener, new EndCollisionEvent(thing, changedThing));
          } else {
            fireEvent(listener, new EndCollisionEvent(changedThing, thing));
          }
        }
      }
    }
  }

  private VerticalPrismCollisionHull getUpdatedHull(SThing changedThing) {
    VerticalPrismCollisionHull oldHull = hulls.remove(changedThing);
    VerticalPrismCollisionHull newHull = collisionHull(changedThing);
    // Compute a combination only if there is an old hull that does not overlap the new
    return oldHull == null || newHull.collidesWith(oldHull)
        ? newHull
        : PolygonPrismHull.combinationHull(oldHull, newHull);
  }

  public boolean doTheseCollide(SThing changedThing, SThing thing) {
    return collisionHull(changedThing).collidesWith(collisionHull(thing));
  }

  private VerticalPrismCollisionHull collisionHull(SThing changedThing) {
    return hulls.computeIfAbsent(changedThing, this::newCollisionHull);
  }

  private VerticalPrismCollisionHull newCollisionHull(SThing thing) {
    return EmployeesOnly.getImplementation(thing).getCollisionHull();
  }

  @Override
  protected void fire(Object listener, CollisionEvent event) {
    if (listener instanceof CollisionStartListener) {
      CollisionStartListener startCollisionEvent = (CollisionStartListener) listener;
      startCollisionEvent.collisionStarted((StartCollisionEvent) event);
    } else if (listener instanceof CollisionEndListener) {
      CollisionEndListener endCollisionEvent = (CollisionEndListener) listener;
      endCollisionEvent.collisionEnded((EndCollisionEvent) event);
    }
  }
}
