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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SThing;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionEvent;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.StartCollisionEvent;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Matt May
 */
public class CollisionHandler extends AbstractBinaryEventHandler<Object, CollisionEvent> {

  protected final CollisionEventHandler collisionEventHandler = new CollisionEventHandler();

  public void addCollisionListener(Object collisionListener, List<SThing> groupOne, List<SThing> groupTwo, MultipleEventPolicy policy) {
    registerIsFiringMap(collisionListener);
    registerPolicyMap(collisionListener, policy);
    List<SThing> allObserving = Lists.newCopyOnWriteArrayList(groupOne);
    allObserving.addAll(groupTwo);
    for (SThing m : allObserving) {
      if (!getModelList().contains(m)) {
        getModelList().add(m);
        EmployeesOnly.getImplementation(m).getSgComposite().addAbsoluteTransformationListener(this);
      }
    }
    collisionEventHandler.register(collisionListener, groupOne, groupTwo);
  }

  @Override
  protected void check(SThing changedEntity) {
    collisionEventHandler.check(changedEntity);
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

  private class CollisionEventHandler {

    private final Map<SThing, CopyOnWriteArrayList<SThing>> checkMap = Maps.newConcurrentHashMap();
    private final Map<SThing, Map<SThing, CopyOnWriteArrayList<Object>>> eventMap = Maps.newConcurrentHashMap();
    private final Map<SThing, Map<SThing, Boolean>> wereTouchingMap = Maps.newConcurrentHashMap();
    private final Map<Object, List<SThing>> listenerToGroupAMap = Maps.newConcurrentHashMap();

    public void check(SThing changedEntity) {
      for (SThing m : checkMap.get(changedEntity)) {
        CopyOnWriteArrayList<Object> listenerList = eventMap.get(changedEntity).get(m);
        if ((listenerList == null) || (listenerList.size() == 0)) {
          return;
        }
        for (Object colList : listenerList) {
          if (check(colList, m, changedEntity)) {
            if (colList instanceof CollisionStartListener) {
              if (listenerToGroupAMap.get(colList).contains(m)) {
                fireEvent(colList, new StartCollisionEvent(m, changedEntity), m, changedEntity);
              } else {
                fireEvent(colList, new StartCollisionEvent(changedEntity, m), changedEntity, m);
              }
            } else if (colList instanceof CollisionEndListener) {
              if (listenerToGroupAMap.get(colList).contains(m)) {
                fireEvent(colList, new EndCollisionEvent(m, changedEntity), m, changedEntity);
              } else {
                fireEvent(colList, new EndCollisionEvent(changedEntity, m), changedEntity, m);
              }
            }
          }
        }
        boolean doTheseCollide = AabbCollisionDetector.doTheseCollide(m, changedEntity);
        wereTouchingMap.get(m).put(changedEntity, doTheseCollide);
        wereTouchingMap.get(changedEntity).put(m, doTheseCollide);
      }
    }

    private boolean check(Object colList, SThing m, SThing changedEntity) {
      if (colList instanceof CollisionStartListener) {
        return !wereTouchingMap.get(m).get(changedEntity) && AabbCollisionDetector.doTheseCollide(m, changedEntity);
      } else if (colList instanceof CollisionEndListener) {
        return wereTouchingMap.get(m).get(changedEntity) && !AabbCollisionDetector.doTheseCollide(m, changedEntity);
      }
      Logger.errln("UNHANDLED CollisionListener TYPE", colList.getClass());
      return false;
    }

    public void register(Object collisionListener, List<SThing> groupOne, List<SThing> groupTwo) {
      listenerToGroupAMap.put(collisionListener, groupOne);
      for (SThing m : groupOne) {
        if (eventMap.get(m) == null) {

          eventMap.put(m, new ConcurrentHashMap<SThing, CopyOnWriteArrayList<Object>>());
          wereTouchingMap.put(m, new ConcurrentHashMap<SThing, Boolean>());
          checkMap.put(m, new CopyOnWriteArrayList<SThing>());
        }
        for (SThing t : groupTwo) {
          if (eventMap.get(m).get(t) == null) {
            eventMap.get(m).put(t, new CopyOnWriteArrayList<Object>());
          }
          if (!m.equals(t)) {
            eventMap.get(m).get(t).add(collisionListener);
            wereTouchingMap.get(m).put(t, false);
            if (!checkMap.get(m).contains(t)) {
              checkMap.get(m).add(t);
            }
          }
        }
      }
      for (SThing m : groupTwo) {
        if (eventMap.get(m) == null) {
          eventMap.put(m, new ConcurrentHashMap<SThing, CopyOnWriteArrayList<Object>>());
          wereTouchingMap.put(m, new ConcurrentHashMap<SThing, Boolean>());
          checkMap.put(m, new CopyOnWriteArrayList<SThing>());
        }
        for (SThing t : groupOne) {
          if (eventMap.get(m).get(t) == null) {
            eventMap.get(m).put(t, new CopyOnWriteArrayList<Object>());
          }
          if (!m.equals(t)) {
            eventMap.get(m).get(t).add(collisionListener);
            wereTouchingMap.get(m).put(t, AabbCollisionDetector.doTheseCollide(m, t));
            if (!checkMap.get(m).contains(t)) {
              checkMap.get(m).add(t);
            }
          }
        }
      }
    }
  }
}
