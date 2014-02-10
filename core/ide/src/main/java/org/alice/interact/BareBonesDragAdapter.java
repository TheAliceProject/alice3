/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.interact;

import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.handle.ManipulationHandle;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.AnimatorDependentManipulator;
import org.alice.interact.manipulator.OnScreenLookingGlassInformedManipulator;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.lookingglass.PickResult;

/**
 * @author Dennis Cosgrove
 */
public abstract class BareBonesDragAdapter {
	private static final double MOUSE_WHEEL_TIMEOUT_TIME = 1.0;
	private static final double CANCEL_MOUSE_WHEEL_DISTANCE = 3;

	private boolean isComponentListener( java.awt.Component component ) {
		for( java.awt.event.MouseListener listener : component.getMouseListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		for( java.awt.event.MouseMotionListener listener : component.getMouseMotionListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		for( java.awt.event.KeyListener listener : component.getKeyListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		for( java.awt.event.MouseWheelListener listener : component.getMouseWheelListeners() ) {
			if( listener == this ) {
				return true;
			}
		}
		return false;
	}

	public void addListeners( java.awt.Component component ) {
		if( !this.isComponentListener( component ) ) {
			component.addMouseListener( this.mouseListener );
			component.addMouseMotionListener( this.mouseMotionListener );
			component.addKeyListener( this.keyListener );
			component.addMouseWheelListener( this.mouseWheelListener );
		}
	}

	public void removeListeners( java.awt.Component component ) {
		if( this.isComponentListener( component ) ) {
			component.removeMouseListener( this.mouseListener );
			component.removeMouseMotionListener( this.mouseMotionListener );
			component.removeKeyListener( this.keyListener );
			component.removeMouseWheelListener( this.mouseWheelListener );
		}
	}

	public void addManipulatorConditionSet( ManipulatorConditionSet manipulator ) {
		this.manipulators.add( manipulator );
	}

	public Iterable<ManipulatorConditionSet> getManipulatorConditionSets() {
		return this.manipulators;
	}

	private java.awt.Component getAWTComponentToAddListenersTo( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		if( onscreenLookingGlass != null ) {
			return onscreenLookingGlass.getAWTComponent();
		} else {
			return null;
		}
	}

	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}

	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		if( this.onscreenLookingGlass != null ) {
			this.onscreenLookingGlass.getLookingGlassFactory().removeAutomaticDisplayListener( this.automaticDisplayAdapter );
		}
		this.onscreenLookingGlass = onscreenLookingGlass;
		setAWTComponent( getAWTComponentToAddListenersTo( this.onscreenLookingGlass ) );
		if( this.onscreenLookingGlass != null ) {
			this.onscreenLookingGlass.getLookingGlassFactory().addAutomaticDisplayListener( this.automaticDisplayAdapter );
		}
	}

	protected java.awt.Component getAWTComponent() {
		return this.lookingGlassComponent;
	}

	public void setAWTComponent( java.awt.Component awtComponent ) {
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

	public void setLookingGlassOnManipulator( OnScreenLookingGlassInformedManipulator manipulator ) {
		manipulator.setOnscreenLookingGlass( this.onscreenLookingGlass );
	}

	protected abstract void setManipulatorStartState( AbstractManipulator manipulator, InputState startState );

	protected void handleStateChange() {
		java.util.List<AbstractManipulator> toStart = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<AbstractManipulator> toEnd = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<AbstractManipulator> toUpdate = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<AbstractManipulator> toClick = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
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

	protected boolean shouldStopMouseWheel( java.awt.Point currentMouse ) {
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

	private ManipulationHandle getHandleForComponent( java.awt.Component c ) {
		if( c == null ) {
			return null;
		}
		org.lgna.croquet.views.AwtComponentView<?> lgc = org.lgna.croquet.views.AwtComponentView.lookup( c );
		if( lgc instanceof ManipulationHandle ) {
			return (ManipulationHandle)lgc;
		} else {
			return getHandleForComponent( c.getParent() );
		}
	}

	private PickResult pickIntoScene( java.awt.Point mouseLocation ) {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		assert onscreenLookingGlass != null;
		edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = onscreenLookingGlass.getPicker().pickFrontMost( mouseLocation.x, mouseLocation.y, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.NOT_REQUIRED );
		return pickResult;
	}

	protected void handleMouseEntered( java.awt.event.MouseEvent e ) {
		this.currentRolloverComponent = e.getComponent();
		if( !this.currentInputState.isAnyMouseButtonDown() ) {
			this.currentInputState.setMouseLocation( e.getPoint() );
			if( e.getComponent() == this.lookingGlassComponent ) {
				try {
					PickResult pr = pickIntoScene( e.getPoint() );
					this.currentInputState.setRolloverPickResult( pr );
				} catch( javax.media.opengl.GLException gle ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "Error picking into scene", gle );
				}
			} else {
				this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ) );
			}
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent( e );
			this.fireStateChange();
		}
	}

	protected void handleMouseExited( java.awt.event.MouseEvent e ) {
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

	protected void handleMousePressed( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DOWN );
		this.currentInputState.setInputEvent( e );
		e.getComponent().requestFocus();

		if( e.getComponent() == this.lookingGlassComponent ) {
			this.currentInputState.setClickPickResult( pickIntoScene( e.getPoint() ) );
		} else {
			this.currentInputState.setClickHandle( this.getHandleForComponent( e.getComponent() ) );
		}
		this.currentInputState.setTimeCaptured();
		this.stopMouseWheel();
		this.fireStateChange();
	}

	protected void handleMouseReleased( java.awt.event.MouseEvent e ) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_UP );
		this.currentInputState.setInputEvent( e );
		if( this.currentRolloverComponent == this.lookingGlassComponent ) {
			this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
		} else {
			this.currentInputState.setRolloverHandle( this.getHandleForComponent( this.currentRolloverComponent ) );
		}
		this.currentInputState.setTimeCaptured();
		this.fireStateChange();
	}

	protected void handleMouseDragged( java.awt.event.MouseEvent e ) {
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

	protected void handleMouseMoved( java.awt.event.MouseEvent e ) {
		if( !this.currentInputState.getIsDragEvent() ) //If we haven't already handled it through dragAndDrop
		{
			//java.awt.Component c = e.getComponent();
			this.currentInputState.setMouseLocation( e.getPoint() );
			if( e.getComponent() == this.lookingGlassComponent )
			{
				//Don't pick into the scene if a mouse button is already down 
				if( !this.currentInputState.isAnyMouseButtonDown() )
				{
					try {
						PickResult pr = pickIntoScene( e.getPoint() );
						this.currentInputState.setRolloverPickResult( pr );
					} catch( javax.media.opengl.GLException gle ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "Error picking into scene", gle );
					}
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

	protected void handleMouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
		this.currentInputState.setMouseWheelState( e.getWheelRotation() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_WHEEL );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( e );
		if( this.mouseWheelStartLocation == null ) {
			this.mouseWheelStartLocation = new java.awt.Point( e.getPoint() );
		}
		this.mouseWheelTimeoutTime = MOUSE_WHEEL_TIMEOUT_TIME;
		this.fireStateChange();
	}

	protected void handleKeyPressed( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), true );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_DOWN );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( e );
		this.fireStateChange();

	}

	protected void handleKeyReleased( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), false );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_UP );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent( e );
		this.fireStateChange();

	}

	protected abstract void handleAutomaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e );

	private final edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayAdapter = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			handleAutomaticDisplayCompleted( e );
		}
	};

	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		public void mouseWheelMoved( java.awt.event.MouseWheelEvent e ) {
			handleMouseWheelMoved( e );
		}
	};
	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved( java.awt.event.MouseEvent e ) {
			handleMouseMoved( e );
		}

		public void mouseDragged( java.awt.event.MouseEvent e ) {
			handleMouseDragged( e );
		}
	};
	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			handleMouseEntered( e );
		}

		public void mouseExited( java.awt.event.MouseEvent e ) {
			handleMouseExited( e );
		}

		public void mousePressed( java.awt.event.MouseEvent e ) {
			handleMousePressed( e );
		}

		public void mouseReleased( java.awt.event.MouseEvent e ) {
			handleMouseReleased( e );
		}

		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};
	private final java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
		public void keyPressed( java.awt.event.KeyEvent e ) {
			handleKeyPressed( e );
		}

		public void keyReleased( java.awt.event.KeyEvent e ) {
			handleKeyReleased( e );
		}

		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};

	protected/*private*/final java.util.List<ManipulatorConditionSet> manipulators = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
	private java.awt.Component lookingGlassComponent = null;
	private java.awt.Component currentRolloverComponent = null;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	protected/*private*/InputState currentInputState = new InputState();
	protected/*private*/InputState previousInputState = new InputState();
	private boolean isInStageChange = false;
	protected/*private*/double mouseWheelTimeoutTime = 0;
	private java.awt.Point mouseWheelStartLocation = null;
}
