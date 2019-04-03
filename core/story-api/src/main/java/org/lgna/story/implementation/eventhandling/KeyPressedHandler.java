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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

import org.lgna.common.ComponentExecutor;
import org.lgna.story.HeldKeyPolicy;
import org.lgna.story.Key;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class KeyPressedHandler extends AbstractEventHandler<Object, KeyEvent> {

  private final Map<Key, CopyOnWriteArrayList<Object>> keySpecificListeners = new ConcurrentHashMap<>();
  private final CopyOnWriteArrayList<Object> genericKeyListeners = new CopyOnWriteArrayList<>();
  private final Map<Object, HeldKeyPolicy> listenerKeyPolicies = Maps.newConcurrentHashMap();
  private final Set<Key> pressedKeys = new HashSet<>();
  private long sleepTime = 33;

  KeyPressedHandler() {
  }

  private void internalAddListener(Object listener, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy) {
    listenerKeyPolicies.put(listener, heldKeyPolicy);
    if (validKeys == null) {
      genericKeyListeners.add(listener);
    } else {
      for (Key k : validKeys) {
        if (keySpecificListeners.get(k) == null) {
          keySpecificListeners.put(k, new CopyOnWriteArrayList<>());
        }
        keySpecificListeners.get(k).add(listener);
      }
    }
    registerIsFiringMap(listener);
    registerPolicyMap(listener, policy);
  }

  public void addListener(KeyPressListener listener, MultipleEventPolicy policy, HeldKeyPolicy heldKeyPolicy) {
    internalAddListener(listener, policy, null, heldKeyPolicy);
  }

  public void addListener(ArrowKeyPressListener listener, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy) {
    internalAddListener(listener, policy, validKeys, heldKeyPolicy);
  }

  public void addListener(NumberKeyPressListener listener, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy) {
    internalAddListener(listener, policy, validKeys, heldKeyPolicy);
  }

  @Override
  protected void fire(Object listener, KeyEvent event) {
    if (listener instanceof ArrowKeyPressListener) {
      ArrowKeyPressListener arrowListener = (ArrowKeyPressListener) listener;
      arrowListener.arrowKeyPressed(new ArrowKeyEvent(event));
    } else if (listener instanceof NumberKeyPressListener) {
      NumberKeyPressListener numberListener = (NumberKeyPressListener) listener;
      numberListener.numberKeyPressed(new NumberKeyEvent(event));
    } else if (listener instanceof KeyPressListener) {
      KeyPressListener keyListener = (KeyPressListener) listener;
      keyListener.keyPressed(event);
    }
  }

  void handleKeyPress(final KeyEvent event) {
    if (shouldFire) {
      final Key key = event.getKey();
      if (!pressedKeys.contains(key)) {
        pressedKeys.add(key);
        notifyListeners(event, this::fireFirstPressEvent);
      }
    }
  }

  void handleKeyRelease(KeyEvent event) {
    if (shouldFire) {
      notifyListeners(event, this::fireReleaseEvent);
    }
  }

  private void notifyListeners(KeyEvent event, BiConsumer<KeyEvent, Object> notification) {
    CopyOnWriteArrayList<Object> keySpecificListeners = this.keySpecificListeners.get(event.getKey());
    if (keySpecificListeners != null) {
      for (Object listener : keySpecificListeners) {
        notification.accept(event, listener);
      }
    }
    for (Object listener : genericKeyListeners) {
      notification.accept(event, listener);
    }
  }

  private void fireFirstPressEvent(KeyEvent event, Object listener) {
    switch (listenerKeyPolicies.get(listener)) {
    case FIRE_ONCE_ON_PRESS:
      fireEvent(listener, event);
      break;
    case FIRE_MULTIPLE:
      final ComponentExecutor thread = new ComponentExecutor(() -> {
        Key key = event.getKey();
        while (pressedKeys.contains(key)) {
          fireEvent(listener, event);
          try {
            Thread.sleep(sleepTime);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }, "keyPressedThread");
      thread.start();
      break;
    case FIRE_ONCE_ON_RELEASE:
      break;
    }
  }

  private void fireReleaseEvent(KeyEvent event, Object listener) {
    if (listenerKeyPolicies.get(listener) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE) {
      fireEvent(listener, event);
    }
    pressedKeys.remove(event.getKey());
  }

  void releaseAllKeys() {
    pressedKeys.clear();
  }
}
