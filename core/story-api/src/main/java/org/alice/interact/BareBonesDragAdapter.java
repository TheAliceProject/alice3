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
package org.alice.interact;

import com.jogamp.opengl.GLException;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.PickFrontMostObserver;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventManager;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.handle.ManipulationHandle;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.AnimatorDependentManipulator;
import org.alice.interact.manipulator.OnscreenPicturePlaneInformedManipulator;

import edu.cmu.cs.dennisc.animation.Animator;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class BareBonesDragAdapter {
	private static final double MOUSE_WHEEL_TIMEOUT_TIME = 1.0;
	private static final double CANCEL_MOUSE_WHEEL_DISTANCE = 3;

	private boolean isComponentListener( Component component ) {
		for( MouseListener listener : component.getMouseListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		for( MouseMotionListener listener : component.getMouseMotionListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		for( KeyListener listener : component.getKeyListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		for( MouseWheelListener listener : component.getMouseWheelListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		return false;
	}

	public void addListeners( Component component ) {
		if( !this.isComponentListener( component ) ) {
			component.addMouseListener( this.mouseListener );
			component.addMouseMotionListener( this.mouseMotionListener );
			component.addKeyListener( this.keyListener );
			component.addMouseWheelListener( this.mouseWheelListener );
		}
	}

	public void removeListeners( Component component ) {
		if( this.isComponentListener( component ) ) {
			component.removeMouseListener( this.mouseListener );
			component.removeMouseMotionListener( this.mouseMotionListener );
			component.removeKeyListener( this.keyListener );
			component.removeMouseWheelListener( this.mouseWheelListener );
		}
	}

	public void addManipulationListener( ManipulationListener listener ) {
		this.manipulationEventManager.addManipulationListener( listener );
	}

	public void removeManipulationListener( ManipulationListener listener ) {
		this.manipulationEventManager.removeManipulationListener( listener );
	}

	public void triggerManipulationEvent( ManipulationEvent event, boolean isActivate ) {
		event.setInputState( this.currentInputState );
		this.manipulationEventManager.triggerEvent( event, isActivate );
	}

	public void addManipulatorConditionSet( ManipulatorConditionSet manipulator ) {
		this.manipulators.add( manipulator );
	}

	public Iterable<ManipulatorConditionSet> getManipulatorConditionSets() {
		return this.manipulators;
	}

	private Component getAWTComponentToAddListenersTo( OnscreenRenderTarget<?> onscreenRenderTarget ) {
		if( onscreenRenderTarget != null ) {
			return onscreenRenderTarget.getAwtComponent();
		} else {
			return null;
		}
	}

	public OnscreenRenderTarget<?> getOnscreenRenderTarget() {
		return this.onscreenRenderTarget;
	}

	public void setOnscreenRenderTarget( OnscreenRenderTarget<?> onscreenRenderTarget ) {
		if( this.onscreenRenderTarget != null ) {
			this.onscreenRenderTarget.getRenderFactory().removeAutomaticDisplayListener( this.automaticDisplayAdapter );
		}
		this.onscreenRenderTarget = onscreenRenderTarget;
		setAWTComponent( getAWTComponentToAddListenersTo( this.onscreenRenderTarget ) );
		if( this.onscreenRenderTarget != null ) {
			this.onscreenRenderTarget.getRenderFactory().addAutomaticDisplayListener( this.automaticDisplayAdapter );
		}
	}

	protected Component getAWTComponent() {
		return this.lookingGlassComponent;
	}

	public void setAWTComponent( Component awtComponent ) {
		if( this.lookingGlassComponent != null ) {
			removeListeners( this.lookingGlassComponent );
		}
		this.lookingGlassComponent = awtComponent;
		if( this.lookingGlassComponent != null ) {
			addListeners( awtComponent );
		}
	}

	public Animator getAnimator() {
		return this.animator;
	}

	public void setAnimator( Animator animator ) {
		this.animator = animator;
		for( ManipulatorConditionSet manipulatorSet : this.manipulators ) {
			if( manipulatorSet.getManipulator() instanceof AnimatorDependentManipulator ) {
				( (AnimatorDependentManipulator)manipulatorSet.getManipulator() ).setAnimator( this.animator );
			}
		}
	}

	public void setLookingGlassOnManipulator( OnscreenPicturePlaneInformedManipulator manipulator ) {
		manipulator.setOnscreenRenderTarget( this.onscreenRenderTarget );
	}

	protected abstract void setManipulatorStartState( AbstractManipulator manipulator, InputState startState );

	protected void handleStateChange() {
		List<AbstractManipulator> toStart = Lists.newLinkedList();
		List<AbstractManipulator> toEnd = Lists.newLinkedList();
		List<AbstractManipulator> toUpdate = Lists.newLinkedList();
		List<AbstractManipulator> toClick = Lists.newLinkedList();
		for( ManipulatorConditionSet currentManipulatorSet : this.manipulators ) {
			//			System.out.println(currentManipulatorSet.getManipulator()+": "+currentManipulatorSet.getCondition(0));
			currentManipulatorSet.update( this.currentInputState, this.previousInputState );
			if( currentManipulatorSet.isEnabled() ) {
				if( currentManipulatorSet.stateChanged( this.currentInputState, this.previousInputState ) ) {
					if( currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ) ) {
						toUpdate.add( currentManipulatorSet.getManipulator() );
					} else if( currentManipulatorSet.justStarted( this.currentInputState, this.previousInputState ) ) {
						//						System.out.println("Just starting "+currentManipulatorSet.getManipulator());
						toStart.add( currentManipulatorSet.getManipulator() );
					} else if( currentManipulatorSet.justEnded( this.currentInputState, this.previousInputState ) ) {
						toEnd.add( currentManipulatorSet.getManipulator() );
					} else if( currentManipulatorSet.clicked( this.currentInputState, this.previousInputState ) ) {
						toClick.add( currentManipulatorSet.getManipulator() );
					}
				} else {
					boolean whyFailed = currentManipulatorSet.stateChanged( this.currentInputState, this.previousInputState );
				}
			} else { //Manipulator is not enabled
				if( currentManipulatorSet.getManipulator().hasStarted() ) {
					toEnd.add( currentManipulatorSet.getManipulator() );
				}
			}
		}

		//End manipulators first
		for( AbstractManipulator toEndManipulator : toEnd ) {
			toEndManipulator.endManipulator( this.currentInputState, this.previousInputState );
		}
		for( AbstractManipulator toClickManipulator : toClick ) {
			this.setManipulatorStartState( toClickManipulator, this.currentInputState );
			toClickManipulator.clickManipulator( this.currentInputState, this.previousInputState );
		}
		for( AbstractManipulator toStartManipulator : toStart ) {
			this.setManipulatorStartState( toStartManipulator, this.currentInputState );
			toStartManipulator.startManipulator( this.currentInputState );
		}
		for( AbstractManipulator toUpdateManipulator : toUpdate ) {
			//If the manipulator we're updating was just started, don't update it with previous data (it's out of scope for the manipulator)
			if( toStart.contains( toUpdateManipulator ) ) {
				toUpdateManipulator.dataUpdateManipulator( this.currentInputState, this.currentInputState );
			} else {
				toUpdateManipulator.dataUpdateManipulator( this.currentInputState, this.previousInputState );
			}
		}
	}

	protected void fireStateChange() {
		this.isInStageChange = true;
		try {
			this.handleStateChange();
		} finally {
			this.isInStageChange = false;
		}
	}

	protected boolean isMouseWheelActive() {
		return ( this.mouseWheelTimeoutTime > 0 );
	}

	protected void stopMouseWheel() {
		this.mouseWheelTimeoutTime = 0;
		this.currentInputState.setMouseWheelState( 0 );
		this.mouseWheelStartLocation = null;
	}

	protected boolean shouldStopMouseWheel( Point currentMouse ) {
		if( this.mouseWheelStartLocation != null ) {
			double distance = currentMouse.distance( this.mouseWheelStartLocation );
			if( distance > CANCEL_MOUSE_WHEEL_DISTANCE ) {
				return true;
			}
		}
		return false;
	}

	public boolean isInStateChange() {
		return this.isInStageChange;
	}

	private ManipulationHandle getHandleForComponent( Component c ) {
		if( c == null ) {
			return null;
		}
		if( c instanceof ManipulationHandle ) {
			return (ManipulationHandle)c;
		} else {
			return getHandleForComponent( c.getParent() );
		}
	}

	private static enum IsSuppressionOfGlExceptionDesired {
		TRUE,
		FALSE;
	}

	private void pickIntoScene( Point mouseLocation, IsSuppressionOfGlExceptionDesired isSuppressionOfGlExceptionDesired, PickFrontMostObserver observer ) {
		OnscreenRenderTarget<?> onscreenRenderTarget = this.getOnscreenRenderTarget();
		assert onscreenRenderTarget != null;
		final boolean IS_ASYNCHRONOUS_PICK_READY_FOR_PRIME_TIME = false;
		if( IS_ASYNCHRONOUS_PICK_READY_FOR_PRIME_TIME ) {
			getOnscreenRenderTarget().getAsynchronousPicker().pickFrontMost( mouseLocation.x, mouseLocation.y, PickSubElementPolicy.NOT_REQUIRED, null, observer );
		} else {
			try {
				PickResult pickResult = onscreenRenderTarget.getSynchronousPicker().pickFrontMost( mouseLocation.x, mouseLocation.y, PickSubElementPolicy.NOT_REQUIRED );
				observer.done( pickResult );
			} catch( GLException gle ) {
				if( isSuppressionOfGlExceptionDesired == IsSuppressionOfGlExceptionDesired.TRUE ) {
					Logger.errln( "Error picking into scene", gle );
				} else {
					throw gle;
				}
			}
		}
	}

	public void clearMouseAndKeyboardState() {
		this.currentInputState.clearKeyState();
		this.currentInputState.clearMouseState();
		this.currentInputState.clearMouseWheelState();

		this.fireStateChange();
	}

	protected void handleMouseEntered( MouseEvent e ) {
		this.currentRolloverComponent = e.getComponent();
		if( !this.currentInputState.isAnyMouseButtonDown() ) {
			this.currentInputState.setMouseLocation( e.getPoint() );
			if( e.getComponent() == this.lookingGlassComponent ) {
				this.pickIntoScene( e.getPoint(), IsSuppressionOfGlExceptionDesired.TRUE, new PickFrontMostObserver() {
					@Override
					public void done( PickResult pickResult ) {
						currentInputState.setRolloverPickResult( pickResult );
					}
				} );
			} else {
				this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ) );
			}
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent( e );
			this.fireStateChange();
		}
	}

	protected void handleMouseExited( MouseEvent e ) {
		this.currentRolloverComponent = null;
		if( !this.currentInputState.isAnyMouseButtonDown() ) {
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setRolloverHandle( null );
			this.currentInputState.setRolloverPickResult( null );
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent( e );
			this.fireStateChange();
		}

	}

	protected void handleMousePressed( MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DOWN );
		this.currentInputState.setInputEvent( e );
		e.getComponent().requestFocus();

		if( e.getComponent() == this.lookingGlassComponent ) {
			this.pickIntoScene( e.getPoint(), IsSuppressionOfGlExceptionDesired.FALSE, new PickFrontMostObserver() {
				@Override
				public void done( PickResult result ) {
					currentInputState.setClickPickResult( result );
				}
			} );
		} else {
			this.currentInputState.setClickHandle( this.getHandleForComponent( e.getComponent() ) );
		}
		this.currentInputState.setTimeCaptured();
		this.stopMouseWheel();
		this.fireStateChange();
	}

	protected void handleMouseReleased( MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_UP );
		this.currentInputState.setInputEvent( e );
		if( this.currentRolloverComponent == this.lookingGlassComponent ) {
			this.pickIntoScene( e.getPoint(), IsSuppressionOfGlExceptionDesired.FALSE, new PickFrontMostObserver() {
				@Override
				public void done( PickResult pickResult ) {
					currentInputState.setRolloverPickResult( pickResult );
				}
			} );
		} else {
			this.currentInputState.setRolloverHandle( this.getHandleForComponent( this.currentRolloverComponent ) );
		}
		this.currentInputState.setTimeCaptured();
		this.fireStateChange();
	}

	protected void handleMouseDragged( MouseEvent e ) {
		try {
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DRAGGED );
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent( e );
			this.fireStateChange();
		} catch( RuntimeException re ) {
			re.printStackTrace();
		}
	}

	protected void handleMouseMoved( MouseEvent e ) {
		if( !this.currentInputState.getIsDragEvent() ) //If we haven't already handled it through dragAndDrop
		{
			//java.awt.Component c = e.getComponent();
			this.currentInputState.setMouseLocation( e.getPoint() );
			if( e.getComponent() == this.lookingGlassComponent )
			{
				//Don't pick into the scene if a mouse button is already down
				if( !this.currentInputState.isAnyMouseButtonDown() )
				{
					this.pickIntoScene( e.getPoint(), IsSuppressionOfGlExceptionDesired.TRUE, new PickFrontMostObserver() {
						@Override
						public void done( PickResult pickResult ) {
							currentInputState.setRolloverPickResult( pickResult );
						}
					} );
				}
			}
			else
			{
				this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ) );
			}
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent( e );
			if( shouldStopMouseWheel( e.getPoint() ) ) {
				this.stopMouseWheel();
			}
			this.fireStateChange();
		}
	}

	protected void handleMouseWheelMoved( MouseWheelEvent e ) {
		this.currentInputState.setMouseWheelState( e.getWheelRotation() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_WHEEL );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( e );
		if( this.mouseWheelStartLocation == null ) {
			this.mouseWheelStartLocation = new Point( e.getPoint() );
		}
		this.mouseWheelTimeoutTime = MOUSE_WHEEL_TIMEOUT_TIME;
		this.fireStateChange();
	}

	protected void handleKeyPressed( KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), true );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_DOWN );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( e );
		this.fireStateChange();

	}

	protected void handleKeyReleased( KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), false );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_UP );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( e );
		this.fireStateChange();

	}

	protected abstract void handleAutomaticDisplayCompleted( AutomaticDisplayEvent e );

	private final AutomaticDisplayListener automaticDisplayAdapter = new AutomaticDisplayListener() {
		@Override
		public void automaticDisplayCompleted( AutomaticDisplayEvent e ) {
			handleAutomaticDisplayCompleted( e );
		}
	};

	private final MouseWheelListener mouseWheelListener = new MouseWheelListener() {
		@Override
		public void mouseWheelMoved( MouseWheelEvent e ) {
			handleMouseWheelMoved( e );
		}
	};
	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved( MouseEvent e ) {
			handleMouseMoved( e );
		}

		@Override
		public void mouseDragged( MouseEvent e ) {
			handleMouseDragged( e );
		}
	};
	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mouseEntered( MouseEvent e ) {
			handleMouseEntered( e );
		}

		@Override
		public void mouseExited( MouseEvent e ) {
			handleMouseExited( e );
		}

		@Override
		public void mousePressed( MouseEvent e ) {
			handleMousePressed( e );
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			handleMouseReleased( e );
		}

		@Override
		public void mouseClicked( MouseEvent e ) {
		}
	};
	private final KeyListener keyListener = new KeyListener() {
		@Override
		public void keyPressed( KeyEvent e ) {
			handleKeyPressed( e );
		}

		@Override
		public void keyReleased( KeyEvent e ) {
			handleKeyReleased( e );
		}

		@Override
		public void keyTyped( KeyEvent e ) {
		}
	};

	protected/*private*/final List<ManipulatorConditionSet> manipulators = Lists.newCopyOnWriteArrayList();
	private final ManipulationEventManager manipulationEventManager = new ManipulationEventManager();
	private OnscreenRenderTarget<?> onscreenRenderTarget;
	private Component lookingGlassComponent = null;
	private Component currentRolloverComponent = null;
	private Animator animator;
	protected/*private*/InputState currentInputState = new InputState();
	protected/*private*/InputState previousInputState = new InputState();
	private boolean isInStageChange = false;
	protected/*private*/double mouseWheelTimeoutTime = 0;
	private Point mouseWheelStartLocation = null;
}
