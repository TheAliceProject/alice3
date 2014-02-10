/*
 * Copyright (c) 2006-2013, Carnegie Mellon University. All rights reserved.
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
 */

package org.lgna.story.implementation.eventhandling;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.alice.interact.AbstractDragAdapter.CameraView;
import org.lgna.story.HeldKeyPolicy;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.SModel;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.Visual;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.MoveWithArrows;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass;
import edu.cmu.cs.dennisc.matt.eventscript.EventScript;
import edu.cmu.cs.dennisc.matt.eventscript.InputEventRecorder;
import edu.cmu.cs.dennisc.matt.eventscript.MouseEventWrapper;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

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
	private final CollisionHandler collisionHandler = new CollisionHandler();
	private final ProximityEventHandler proxyHandler = new ProximityEventHandler();
	private final TimerEventHandler timer = new TimerEventHandler();
	private final SceneActivationHandler sceneActivationHandler = new SceneActivationHandler();
	private final AbstractEventHandler<?, ?>[] handlers = new AbstractEventHandler[] { keyHandler, mouseHandler, transHandler, occlusionHandler, viewHandler, collisionHandler, proxyHandler, timer, sceneActivationHandler };

	private final TimerContingencyManager contingent;

	public void recieveEvent( Object event ) {
		if( event instanceof MouseEventWrapper ) {
			MouseEventWrapper mouseEvent = (MouseEventWrapper)event;
			mouseAdapter.handleReplayedEvent( mouseEvent );
		} else if( event instanceof KeyEvent ) {
			KeyEvent keyEvent = (KeyEvent)event;
			if( new Integer( keyEvent.getID() ).equals( KeyEvent.KEY_PRESSED ) ) {
				keyAdapter.keyPressed( keyEvent );
			} else if( new Integer( keyEvent.getID() ).equals( KeyEvent.KEY_RELEASED ) ) {
				keyAdapter.keyReleased( keyEvent );
			} else {
				Logger.errln( "mishandled recieved keyboard event", keyEvent );
			}
		} else {
			Logger.errln( "mishandled recieved event ", event );
		}
	}

	public final CustomLenientMouseAdapter mouseAdapter = new CustomLenientMouseAdapter();

	private class CustomLenientMouseAdapter extends edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			inputRecorder.record( createWrapper( e ) );
			EventManager.this.mouseHandler.handleMouseQuoteClickedUnquote( e, /* quoteClickCountUnquote, */EventManager.this.scene.getAbstraction() );
		}

		public void handleReplayedEvent( MouseEventWrapper e ) {
			mouseQuoteClickedUnquote( e.getTranslatedPointIfNecessary( scene.getProgram().getOnscreenLookingGlass().getAWTComponent() ), 0 );
		}
	};

	private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
		public void keyPressed( KeyEvent e ) {
			org.lgna.story.event.KeyEvent event = new org.lgna.story.event.KeyEvent( e );
			inputRecorder.record( e );
			keyHandler.handleKeyPress( event );
		}

		public void keyReleased( KeyEvent e ) {
			org.lgna.story.event.KeyEvent event = new org.lgna.story.event.KeyEvent( e );
			inputRecorder.record( e );
			keyHandler.handleKeyRelease( event );
		}

		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};

	//	private final java.util.List< org.lgna.story.event.MouseButtonListener > mouseButtonListeners = Collections.newCopyOnWriteArrayList();
	//	private final java.util.List< org.lgna.story.event.KeyListener > keyListeners = Collections.newCopyOnWriteArrayList();

	private org.alice.interact.AbstractDragAdapter dragAdapter;

	public EventManager( SceneImp scene ) {
		this.scene = scene;
		for( AbstractEventHandler<?, ?> handler : handlers ) {
			handler.setScene( scene );
		}
		inputRecorder = new InputEventRecorder( scene );
		contingent = new TimerContingencyManager( timer );
	}

	public void initialize() {
		scene.addSceneActivationListener( timer );
	}

	public MouseEventWrapper createWrapper( MouseEvent e ) {
		return new MouseEventWrapper( e );
	}

	public void removeKeyListener( KeyPressListener keyListener ) {
		throw new RuntimeException( "todo" );
		//		this.mouse.removeListener(keyListener);
		//		this.keyListeners.remove( keyListener );
	}

	public void addListenersTo( OnscreenLookingGlass onscreenLookingGlass ) {
		java.awt.Component component = onscreenLookingGlass.getAWTComponent();
		component.addMouseListener( this.mouseAdapter );
		component.addMouseMotionListener( this.mouseAdapter );
		component.addKeyListener( this.keyAdapter );
	}

	public void removeListenersFrom( OnscreenLookingGlass onscreenLookingGlass ) {
		java.awt.Component component = onscreenLookingGlass.getAWTComponent();
		component.removeMouseListener( this.mouseAdapter );
		component.removeMouseMotionListener( this.mouseAdapter );
		component.removeKeyListener( this.keyAdapter );
	}

	//	public void mouseButtonClicked(MouseButtonEvent e) {
	//		mouse.fireAllTargeted(e.getModelAtMouseLocation());
	//	}
	//
	//	public void keyPressed(KeyEvent e) {
	//		key.fireAllTargeted(e);
	//	}

	private AbstractEventHandler<?, ?>[] getEventHandlers() {
		return handlers;
	}

	public void silenceAllListeners() {
		for( AbstractEventHandler<?, ?> handler : this.getEventHandlers() ) {
			handler.silenceListeners();
		}
	}

	public void restoreAllListeners() {
		for( AbstractEventHandler<?, ?> handler : this.getEventHandlers() ) {
			handler.restoreListeners();
		}
	}

	public void addCollisionListener( Object collisionListener, List<SThing> groupOne, List<SThing> groupTwo ) {
		collisionHandler.addCollisionListener( collisionListener, groupOne, groupTwo );
	}

	public void addProximityEventListener( Object proximityEventListener, List<SThing> groupOne, List<SThing> groupTwo, Number dist ) {
		proxyHandler.addProximityEventListener( proximityEventListener, groupOne, groupTwo, dist.doubleValue() );
	}

	public void addTimerEventListener( TimeListener timerEventListener, Number frequency, MultipleEventPolicy policy ) {
		timer.addListener( timerEventListener, frequency.doubleValue(), policy );
	}

	public void addKeyListener( KeyPressListener keyListener, MultipleEventPolicy eventPolicy, HeldKeyPolicy heldKeyPolicy ) {
		this.keyHandler.addListener( keyListener, eventPolicy, null, heldKeyPolicy );
	}

	public void addNumberKeyListener( NumberKeyPressListener keyPressListener, MultipleEventPolicy policy, HeldKeyPolicy heldKeyPolicy ) {
		keyHandler.addListener( keyPressListener, policy, NumberKeyEvent.NUMBERS, heldKeyPolicy );
	}

	public void addArrowKeyListener( ArrowKeyPressListener keyPressListener, MultipleEventPolicy policy, HeldKeyPolicy heldKeyPolicy ) {
		keyHandler.addListener( keyPressListener, policy, ArrowKeyEvent.ARROWS, heldKeyPolicy );
	}

	public void moveWithArrows( SMovableTurnable entity ) {
		this.keyHandler.addListener( new MoveWithArrows( entity ), MultipleEventPolicy.COMBINE, ArrowKeyEvent.ARROWS, HeldKeyPolicy.FIRE_MULTIPLE );
	}

	public void addMouseClickOnScreenListener( MouseClickOnScreenListener listener, MultipleEventPolicy policy ) {
		mouseHandler.addListener( listener, policy, null );
	}

	public void addMouseClickOnObjectListener( MouseClickOnObjectListener listener, MultipleEventPolicy policy, Visual[] targets ) {
		this.mouseHandler.addListener( listener, policy, targets );
	}

	public void addMouseButtonListener( Object listener, MultipleEventPolicy policy, Visual[] targets ) {
		this.mouseHandler.addListener( listener, policy, targets );
	}

	public void addTransformationListener( PointOfViewChangeListener transformationlistener, SThing[] shouldListenTo ) {
		this.transHandler.addTransformationListener( transformationlistener, shouldListenTo );
	}

	//	public void addOcclusionEventListener( OcclusionListener occlusionEventListener, ArrayList<Entity> groupOne, ArrayList<Entity> groupTwo) {
	//		this.occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	//	}

	public void addComesIntoViewEventListener( ViewEnterListener listener, SModel[] entities ) {
		this.viewHandler.addViewEventListener( listener, entities );
	}

	public void addLeavesViewEventListener( ViewExitListener listener, SModel[] entities ) {
		this.viewHandler.addViewEventListener( listener, entities );
	}

	public void sceneActivated() {
		this.sceneActivationHandler.handleEventFire( new SceneActivationEvent() );
	}

	public void addDragAdapter() {
		if( this.dragAdapter != null ) {
			//pass
		} else {
			this.dragAdapter = new org.alice.interact.RuntimeDragAdapter();
			OnscreenLookingGlass lookingGlass = this.scene.getProgram().getOnscreenLookingGlass();
			SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera)scene.findFirstCamera().getSgCamera();
			//			for( int i = 0; i < lookingGlass.getCameraCount(); i++ ) {
			//				if( lookingGlass.getCameraAt( i ) instanceof SymmetricPerspectiveCamera ) 
			//				{
			//					camera = (SymmetricPerspectiveCamera)lookingGlass.getCameraAt( i );
			//					break;
			//				}
			//			}
			this.dragAdapter.setOnscreenLookingGlass( lookingGlass );
			this.dragAdapter.addCameraView( CameraView.MAIN, camera, null );
			this.dragAdapter.makeCameraActive( camera );
			this.dragAdapter.setAnimator( this.scene.getProgram().getAnimator() );
			//			for( Transformable transformable : this.scene.getComponents() ) {
			//				this.putBonusDataFor( transformable );
			//			}
		}
	}

	public void addWhileCollisionListener( WhileCollisionListener listener, ArrayList<SThing> groupOne, ArrayList<SThing> groupTwo, Double frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, groupOne, groupTwo, frequency, policy );
	}

	public void addWhileProximityListener( WhileProximityListener listener, ArrayList<SThing> groupOne, ArrayList<SThing> groupTwo, Number dist, Double frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, groupOne, groupTwo, dist.doubleValue(), frequency, policy );
	}

	public void addWhileOcclusionListener( WhileOcclusionListener listener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo, Double frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, groupOne, groupTwo, frequency, policy );
	}

	public void addWhileInViewListener( WhileInViewListener listener, ArrayList<SModel> group, Double frequency, MultipleEventPolicy policy ) {
		contingent.register( listener, group, frequency, policy );
	}

	public void addOcclusionEventListener( OcclusionStartListener occlusionEventListener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo ) {
		occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	}

	public void addOcclusionEventListener( OcclusionEndListener occlusionEventListener, ArrayList<SModel> groupOne, ArrayList<SModel> groupTwo ) {
		occlusionHandler.addOcclusionEvent( occlusionEventListener, groupOne, groupTwo );
	}

	public void addSceneActivationListener( SceneActivationListener listener ) {
		sceneActivationHandler.addListener( listener );
	}

	public void removeSceneActivationListener( SceneActivationListener listener ) {
		sceneActivationHandler.removeListener( listener );
	}

	public EventScript getScript() {
		return inputRecorder.getScript();
	}
}
