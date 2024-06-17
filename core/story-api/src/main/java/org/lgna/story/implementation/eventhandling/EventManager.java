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

import edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.matt.eventscript.EventScript;
import edu.cmu.cs.dennisc.matt.eventscript.InputEventRecorder;
import edu.cmu.cs.dennisc.matt.eventscript.MouseEventWrapper;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.RuntimeDragAdapter;
import org.lgna.story.HeldKeyPolicy;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.Visual;
import org.lgna.story.event.*;
import org.lgna.story.implementation.SceneImp;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Matt May
 */
public class EventManager {

  private final SceneImp scene;
  private final InputEventRecorder inputRecorder;
  private final KeyPressedHandler keyHandler = new KeyPressedHandler();
  private final MouseClickedHandler mouseHandler = new MouseClickedHandler();
  private final TransformationHandler transHandler = new TransformationHandler();
  private final OcclusionHandler occlusionHandler = new OcclusionHandler();
  private final ViewEventHandler viewHandler = new ViewEventHandler();
  private final Map<SThing, VerticalPrismCollisionHull> hulls = Maps.newConcurrentHashMap();
  private final CollisionHandler collisionHandler = new CollisionHandler(hulls);
  private final ProximityEventHandler proxyHandler = new ProximityEventHandler(hulls);
  private final TimerEventHandler timer = new TimerEventHandler();
  private final SceneActivationHandler sceneActivationHandler = new SceneActivationHandler();
  private final AbstractEventHandler<?, ?>[] handlers = new AbstractEventHandler[] {keyHandler, mouseHandler, transHandler, occlusionHandler, viewHandler, collisionHandler, proxyHandler, timer, sceneActivationHandler};

  private final TimerContingencyManager contingent;

  public final CustomLenientMouseAdapter mouseAdapter = new CustomLenientMouseAdapter();

  public CollisionHandler getCollisionHandler() {
    return collisionHandler;
  }

  private class CustomLenientMouseAdapter extends LenientMouseClickAdapter {
    @Override
    protected void mouseQuoteClickedUnquote(MouseEvent e, int quoteClickCountUnquote) {
      inputRecorder.record(createWrapper(e));
      EventManager.this.mouseHandler.handleMouseQuoteClickedUnquote(e, /* quoteClickCountUnquote, */EventManager.this.scene.getAbstraction());
    }

    public void handleReplayedEvent(MouseEventWrapper e) {
      mouseQuoteClickedUnquote(e.getTranslatedPointIfNecessary(scene.getProgram().getOnscreenRenderTarget().getAwtComponent()), 0);
    }
  }

  private KeyListener keyAdapter = new KeyListener() {
    @Override
    public void keyPressed(KeyEvent e) {
      org.lgna.story.event.KeyEvent event = new org.lgna.story.event.KeyEvent(e);
      inputRecorder.record(e);
      keyHandler.handleKeyPress(event);
    }

    @Override
    public void keyReleased(KeyEvent e) {
      org.lgna.story.event.KeyEvent event = new org.lgna.story.event.KeyEvent(e);
      inputRecorder.record(e);
      keyHandler.handleKeyRelease(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
  };

  private FocusListener focusAdapter = new FocusListener() {

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
      keyHandler.releaseAllKeys();
    }
  };

  private RuntimeDragAdapter dragAdapter;

  public EventManager(SceneImp scene) {
    this.scene = scene;
    for (AbstractEventHandler<?, ?> handler : handlers) {
      handler.setScene(scene);
    }
    inputRecorder = new InputEventRecorder(scene);
    contingent = new TimerContingencyManager(timer);
  }

  public void initialize() {
    scene.addSceneActivationListener(timer);
  }

  public MouseEventWrapper createWrapper(MouseEvent e) {
    return new MouseEventWrapper(e);
  }

  public void removeKeyListener(KeyPressListener keyListener) {
    throw new RuntimeException("todo");
    //    this.mouse.removeListener(keyListener);
    //    this.keyListeners.remove( keyListener );
  }

  public void addListenersTo(OnscreenRenderTarget onscreenRenderTarget) {
    Component component = onscreenRenderTarget.getAwtComponent();
    component.addMouseListener(this.mouseAdapter);
    component.addMouseMotionListener(this.mouseAdapter);
    component.addKeyListener(this.keyAdapter);
    component.addFocusListener(this.focusAdapter);
  }

  public void removeListenersFrom(OnscreenRenderTarget onscreenRenderTarget) {
    Component component = onscreenRenderTarget.getAwtComponent();
    component.removeMouseListener(this.mouseAdapter);
    component.removeMouseMotionListener(this.mouseAdapter);
    component.removeKeyListener(this.keyAdapter);
    component.removeFocusListener(this.focusAdapter);
  }

  private AbstractEventHandler<?, ?>[] getEventHandlers() {
    return handlers;
  }

  public void silenceAllListeners() {
    for (AbstractEventHandler<?, ?> handler : this.getEventHandlers()) {
      handler.silenceListeners();
    }
  }

  public void restoreAllListeners() {
    for (AbstractEventHandler<?, ?> handler : this.getEventHandlers()) {
      handler.restoreListeners();
    }
  }

  public void addCollisionListener(Object collisionListener, List<SThing> groupOne, List<SThing> groupTwo, MultipleEventPolicy policy) {
    collisionHandler.addCollisionListener(collisionListener, groupOne, groupTwo, policy);
  }

  public void addProximityEventListener(Object proximityEventListener, List<SThing> groupOne, List<SThing> groupTwo, Number dist, MultipleEventPolicy policy) {
    proxyHandler.addProximityEventListener(proximityEventListener, groupOne, groupTwo, dist.doubleValue(), policy);
  }

  public void addTimerEventListener(TimeListener timerEventListener, Number frequency, MultipleEventPolicy policy) {
    timer.addListener(timerEventListener, frequency.doubleValue(), policy);
  }

  public void addKeyListener(KeyPressListener keyListener, MultipleEventPolicy eventPolicy, HeldKeyPolicy heldKeyPolicy) {
    keyHandler.addListener(keyListener, eventPolicy, heldKeyPolicy);
  }

  public void addNumberKeyListener(NumberKeyPressListener keyPressListener, MultipleEventPolicy policy, HeldKeyPolicy heldKeyPolicy) {
    keyHandler.addListener(keyPressListener, policy, NumberKeyEvent.NUMBERS, heldKeyPolicy);
  }

  public void addArrowKeyListener(ArrowKeyPressListener keyPressListener, MultipleEventPolicy policy, HeldKeyPolicy heldKeyPolicy) {
    keyHandler.addListener(keyPressListener, policy, ArrowKeyEvent.ARROWS, heldKeyPolicy);
  }

  public void moveWithArrows(SMovableTurnable entity, Double speed) {
    MoveWithArrows.createNewAndAddTo(entity, speed, this.keyHandler);
  }

  public void addMouseClickOnScreenListener(MouseClickOnScreenListener listener, MultipleEventPolicy policy) {
    mouseHandler.addListener(listener, policy, null);
  }

  public void addMouseClickOnObjectListener(MouseClickOnObjectListener listener, MultipleEventPolicy policy, Visual[] targets) {
    this.mouseHandler.addListener(listener, policy, targets);
  }

  @Deprecated //(since = "3.1.39.0.0", forRemoval = true)   // It was split into addMouseClickOnScreenListener and addMouseClickOnObjectListener above
  public void addMouseButtonListener(Object listener, MultipleEventPolicy policy, Visual[] targets) {
    this.mouseHandler.addListener(listener, policy, targets);
  }

  public void addTransformationListener(PointOfViewChangeListener transformationlistener, SThing[] shouldListenTo) {
    this.transHandler.addTransformationListener(transformationlistener, shouldListenTo);
  }

  public void addComesIntoViewEventListener(ViewEnterListener listener, SModel[] entities, MultipleEventPolicy policy) {
    this.viewHandler.addViewEventListener(listener, entities, policy);
  }

  public void addLeavesViewEventListener(ViewExitListener listener, SModel[] entities, MultipleEventPolicy policy) {
    this.viewHandler.addViewEventListener(listener, entities, policy);
  }

  public void sceneActivated() {
    this.sceneActivationHandler.handleEventFire(new SceneActivationEvent());
  }

  public void addDragAdapter(Visual[] visuals) {
    if (this.dragAdapter == null) {
      this.dragAdapter = new RuntimeDragAdapter(visuals);
      OnscreenRenderTarget renderTarget = this.scene.getProgram().getOnscreenRenderTarget();
      SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera) scene.findFirstCamera().getSgCamera();
      this.dragAdapter.setOnscreenRenderTarget(renderTarget);
      this.dragAdapter.addCameraView(CameraView.MAIN, camera);
      this.dragAdapter.makeCameraActive(camera);
      this.dragAdapter.setAnimator(this.scene.getProgram().getAnimator());
    } else {
      dragAdapter.addTargets(visuals);
    }
  }

  public void addWhileCollisionListener(WhileCollisionListener listener, ArrayList<SThing> groupOne, ArrayList<SThing> groupTwo, Double frequency, MultipleEventPolicy policy) {
    contingent.register(listener, groupOne, groupTwo, frequency, policy);
  }

  public void addWhileProximityListener(WhileProximityListener listener, ArrayList<SThing> groupOne, ArrayList<SThing> groupTwo, Number dist, Double frequency, MultipleEventPolicy policy) {
    contingent.register(listener, groupOne, groupTwo, dist.doubleValue(), frequency, policy);
  }

  public void addWhileOcclusionListener(WhileOcclusionListener listener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo, Double frequency, MultipleEventPolicy policy) {
    contingent.register(listener, groupOne, groupTwo, frequency, policy);
  }

  public void addWhileInViewListener(WhileInViewListener listener, ArrayList<SModel> group, Double frequency, MultipleEventPolicy policy) {
    contingent.register(listener, group, frequency, policy);
  }

  public void addOcclusionEventListener(OcclusionStartListener occlusionEventListener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo, MultipleEventPolicy policy) {
    occlusionHandler.addOcclusionEventListener(occlusionEventListener, groupOne, groupTwo, policy);
  }

  public void addOcclusionEventListener(OcclusionEndListener occlusionEventListener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo, MultipleEventPolicy policy) {
    occlusionHandler.addOcclusionEventListener(occlusionEventListener, groupOne, groupTwo, policy);
  }

  public void addSceneActivationListener(SceneActivationListener listener) {
    sceneActivationHandler.addListener(listener);
  }

  public void removeSceneActivationListener(SceneActivationListener listener) {
    sceneActivationHandler.removeListener(listener);
  }

  public EventScript getScript() {
    return inputRecorder.getScript();
  }
}
