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

import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SThing;
import org.lgna.story.event.EndOcclusionEvent;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionEvent;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.implementation.CameraImp;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class OcclusionHandler extends AbstractBinaryEventHandler<Object, OcclusionEvent, SModel> {
  private final Map<SModel, Map<SModel, Boolean>> wereOccluded = Maps.newConcurrentHashMap();
  private CameraImp<?> camera;

  public void addOcclusionEventListener(Object occlusionEventListener, List<SModel> groupA, List<SModel> groupB, MultipleEventPolicy policy) {
    startTrackingListener(occlusionEventListener, groupA, groupB, policy);
    if ((groupA.size() > 0) && (groupA.get(0) != null) && (camera == null)) {
      camera = groupA.get(0).getImplementation().getScene().findFirstCamera();
      camera.getSgComposite().addAbsoluteTransformationListener(this);
    }
  }

  @Override
  protected void startTrackingThing(SModel thing) {
    super.startTrackingThing(thing);
    if (wereOccluded.get(thing) == null) {
      wereOccluded.put(thing, new ConcurrentHashMap<>());
    }
  }

  // No-op - OcclusionHandler checks in full each time.
  // It does not cache any data to remove or mark as dirty.
  protected void markAsChanged(SThing changedThing) {
  }

  @Override
  protected void checkForEvents(SThing changedThing) {
    if (camera == null) {
      camera = changedThing.getImplementation().getScene().findFirstCamera();
      if (camera == null) {
        return;
      }
    }
    if (changedThing.equals(camera.getAbstraction())) {
      for (SModel model : interactionListeners.keySet()) {
        checkForOcclusions(model);
      }
    } else {
      if (changedThing instanceof SModel) {
        checkForOcclusions((SModel) changedThing);
      }
    }
  }

  protected void checkForOcclusions(SModel changedModel) {
    final Map<SModel, Set<Object>> thingsToOcclude = interactionListeners.get(changedModel);
    for (SModel model : thingsToOcclude.keySet()) {
      Set<Object> listeners = thingsToOcclude.get(model);
      if ((listeners == null) || (listeners.size() == 0)) {
        break;
      }
      boolean doTheseOcclude = AabbOcclusionDetector.doTheseOcclude(camera, changedModel, model);
      final boolean isOcclusionStart = wasFalse(wereOccluded, changedModel, model) && doTheseOcclude;
      final boolean isOcclusionEnd = wasTrue(wereOccluded, changedModel, model) && !doTheseOcclude;
      wereOccluded.get(changedModel).put(model, doTheseOcclude);
      wereOccluded.get(model).put(changedModel, doTheseOcclude);
      if (!isOcclusionStart && !isOcclusionEnd) {
        continue;
      }
      final boolean isChangedModelInBackground = camera.getDistanceTo(model.getImplementation()) < camera.getDistanceTo(changedModel.getImplementation());
      SModel foreground = isChangedModelInBackground ? model : changedModel;
      SModel background = isChangedModelInBackground ? changedModel : model;
      for (Object listener : listeners) {
        if (isOcclusionStart && listener instanceof OcclusionStartListener) {
          fireEvent(listener, new StartOcclusionEvent(foreground, background));
        } else if (isOcclusionEnd && listener instanceof OcclusionEndListener) {
          fireEvent(listener, new EndOcclusionEvent(foreground, background));
        }
      }
    }
  }

  @Override
  protected void fire(Object listener, OcclusionEvent event) {
    if (listener instanceof OcclusionStartListener) {
      OcclusionStartListener start = (OcclusionStartListener) listener;
      start.occlusionStarted((StartOcclusionEvent) event);
    } else if (listener instanceof OcclusionEndListener) {
      OcclusionEndListener start = (OcclusionEndListener) listener;
      start.occlusionEnded((EndOcclusionEvent) event);
    }
  }
}
