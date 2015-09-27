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

import java.awt.event.InputEvent;

import org.alice.interact.handle.ManipulationHandle;

import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Visual;

//import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class InputState {

	public static enum InputEventType {
		MOUSE_WHEEL,
		MOUSE_DOWN,
		MOUSE_UP,
		MOUSE_DRAGGED,
		KEY_DOWN,
		KEY_UP,
		NULL_EVENT,
	}

	public InputState() {
	}

	public InputState( InputState other ) {
		this();
		copyState( other );
	}

	public InputEvent getInputEvent() {
		return this.inputEvent;
	}

	public void setInputEvent( InputEvent inputEvent ) {
		this.inputEvent = inputEvent;
	}

	public ManipulationHandle getRolloverHandle() {
		return this.rolloverHandle;
	}

	public AbstractCamera getPickCamera() {
		if( ( this.rolloverPickResult != null ) && ( this.rolloverPickResult.getSource() instanceof AbstractCamera ) ) {
			return (AbstractCamera)this.rolloverPickResult.getSource();
		} else if( ( this.clickPickResult != null ) && ( this.clickPickResult.getSource() instanceof AbstractCamera ) ) {
			return (AbstractCamera)this.clickPickResult.getSource();
		} else {
			return null;
		}
	}

	public void setIsDragEvent( boolean isDragEvent ) {
		this.isDragEvent = isDragEvent;
	}

	public boolean getIsDragEvent() {
		return this.isDragEvent;
	}

	public void setDragAndDropContext( Object dragAndDropContext ) {
		this.dragAndDropContext = dragAndDropContext;
	}

	public Object getDragAndDropContext() {
		return this.dragAndDropContext;
	}

	public void setRolloverHandle( ManipulationHandle rolloverHandle ) {
		this.rolloverHandle = rolloverHandle;
	}

	public ManipulationHandle getClickHandle() {
		return this.clickHandle;
	}

	public void setClickHandle( ManipulationHandle clickHandle ) {
		this.clickHandle = clickHandle;
	}

	public AbstractTransformable getCurrentlySelectedObject() {
		return currentlySelectedObject;
	}

	public void setCurrentlySelectedObject( AbstractTransformable currentlySelectedObject ) {
		this.currentlySelectedObject = currentlySelectedObject;
	}

	public boolean isKeyDown( int keyIndex ) {
		if( currentKeysToStatesMap.containsKey( keyIndex ) ) {
			return currentKeysToStatesMap.get( keyIndex ).booleanValue();
		} else {
			return false;
		}
	}

	public void setKeyState( int keyIndex, boolean isDown ) {
		currentKeysToStatesMap.put( keyIndex, new Boolean( isDown ) );
	}

	public void clearKeyState() {
		this.currentKeysToStatesMap.clear();
	}

	public void clearMouseState() {
		this.currentMouseButtonsToStatesMap.clear();
	}

	public void clearMouseWheelState() {
		this.currentMouseWheelState = 0;
	}

	public void setMouseState( int mouseButton, boolean isDown ) {
		Integer mouseInt = new Integer( mouseButton );
		currentMouseButtonsToStatesMap.put( mouseInt, new Boolean( isDown ) );
	}

	public boolean isAnyMouseButtonDown() {
		for( Integer key : this.currentMouseButtonsToStatesMap.keySet() ) {
			if( this.currentMouseButtonsToStatesMap.get( key ).booleanValue() ) {
				return true;
			}
		}
		return false;
	}

	public boolean isMouseDown( int mouseButton ) {
		if( currentMouseButtonsToStatesMap.containsKey( mouseButton ) ) {
			return currentMouseButtonsToStatesMap.get( mouseButton ).booleanValue();
		} else {
			return false;
		}
	}

	public void setMouseWheelState( int mouseWheelMovement ) {
		this.currentMouseWheelState = mouseWheelMovement;
	}

	public int getMouseWheelState() {
		return this.currentMouseWheelState;
	}

	public void setMouseLocation( java.awt.Point mouseLocation ) {
		this.currentMouseLocation = mouseLocation;
	}

	public java.awt.Point getMouseLocation() {
		return this.currentMouseLocation;
	}

	public void setInputEventType( InputEventType eventType ) {
		this.currentInputEventType = eventType;
	}

	public InputEventType getInputEventType() {
		return this.currentInputEventType;
	}

	public void setClickPickResult( edu.cmu.cs.dennisc.render.PickResult pickResult ) {
		this.clickPickResult = pickResult;
		AbstractTransformable picked = this.getClickPickedTransformable( true );
		PickHint clickedObjectType = PickUtilities.getPickType( this.clickPickResult );
		if( !clickedObjectType.intersects( PickHint.PickType.NOTHING.pickHint() ) ) {
			this.setClickPickTransformable( picked );
		}
		//		else if (clickedObjectType.intersects( PickHint.HANDLES) )
		//		{
		//			this.setClickPickTransformable(picked);
		//		}
		else {
			this.setClickPickTransformable( null );
		}
		if( picked instanceof ManipulationHandle ) {
			this.clickHandle = (ManipulationHandle)picked;
		} else {
			this.clickHandle = null;
		}
	}

	public edu.cmu.cs.dennisc.render.PickResult getClickPickResult() {
		return this.clickPickResult;
	}

	public PickHint getCurrentlySelectedObjectPickHint() {
		if( this.getCurrentlySelectedObject() != null ) {
			return PickUtilities.getPickType( this.getCurrentlySelectedObject() );
		} else {
			return PickHint.PickType.NOTHING.pickHint();
		}
	}

	public PickHint getClickPickHint() {
		//Evaluate handles first since they may be overlayed on the scene
		if( this.clickHandle != null ) {
			return this.clickHandle.getPickHint();
		} else if( this.getClickPickResult() != null ) {
			return PickUtilities.getPickType( this.getClickPickResult() );
		} else {
			return PickHint.PickType.NOTHING.pickHint();
		}
	}

	public PickHint getRolloverPickHint() {
		if( this.getRolloverPickResult() != null ) {
			return PickUtilities.getPickType( this.getRolloverPickResult() );
		} else if( this.rolloverHandle != null ) {
			return this.rolloverHandle.getPickHint();
		} else {
			return PickHint.PickType.NOTHING.pickHint();
		}
	}

	public PickResult getRolloverPickResult() {
		return this.rolloverPickResult;
	}

	public void setRolloverPickResult( PickResult rolloverPickResult ) {
		this.rolloverPickResult = rolloverPickResult;
		AbstractTransformable picked = this.getRolloverPickedTransformable( true );
		boolean validPick = true;
		if( picked instanceof ManipulationHandle ) {
			ManipulationHandle handle = (ManipulationHandle)picked;
			if( !handle.isPickable() ) {
				validPick = false;
			}
		}
		PickHint rolloverObjectType = PickUtilities.getPickType( this.rolloverPickResult );
		if( validPick && !rolloverObjectType.intersects( PickHint.PickType.NOTHING.pickHint() ) ) {
			this.setRolloverPickTransformable( picked );
		} else {
			this.setRolloverPickTransformable( null );
		}
		if( validPick && ( picked instanceof ManipulationHandle ) ) {
			this.rolloverHandle = (ManipulationHandle)picked;
		} else {
			this.rolloverHandle = null;
		}
	}

	public AbstractTransformable getRolloverPickTransformable() {
		return this.rolloverPickTransformable;
	}

	public void setRolloverPickTransformable( AbstractTransformable rolloverPickTransformable ) {
		this.rolloverPickTransformable = rolloverPickTransformable;
	}

	public AbstractTransformable getClickPickTransformable() {
		return this.clickPickTransformable;
	}

	public void setClickPickTransformable( AbstractTransformable clickPickTransformable ) {
		this.clickPickTransformable = clickPickTransformable;
	}

	protected AbstractTransformable getPickedTransformable( PickResult pickResult, boolean getFirstClass ) {
		if( pickResult != null ) {
			Visual sgVisual = pickResult.getVisual();
			if( sgVisual != null ) {
				Composite sgParent = sgVisual.getParent();
				if( sgParent instanceof edu.cmu.cs.dennisc.scenegraph.Scalable ) {
					sgParent = sgParent.getParent();
				}
				if( sgParent instanceof edu.cmu.cs.dennisc.scenegraph.Transformable ) {
					if( getFirstClass ) {
						Component firstClassComponent = PickUtilities.getFirstClassFromComponent( sgParent );
						if( firstClassComponent instanceof AbstractTransformable ) {
							return (AbstractTransformable)firstClassComponent;
						}
					} else {
						return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgParent;
					}
				}
			}
		}
		return null;
	}

	public AbstractTransformable getClickPickedTransformable( boolean getFirstClassObject ) {
		return this.getPickedTransformable( this.clickPickResult, getFirstClassObject );
	}

	public AbstractTransformable getRolloverPickedTransformable( boolean getFirstClassObject ) {
		return this.getPickedTransformable( this.rolloverPickResult, getFirstClassObject );
	}

	public long getTimeCaptured() {
		return this.timeCaptured;
	}

	public void setTimeCaptured() {
		this.timeCaptured = System.currentTimeMillis();
	}

	public void setTimeCaptured( long currentTime ) {
		this.timeCaptured = currentTime;
	}

	public void copyState( InputState sourceState ) {
		this.currentKeysToStatesMap = (java.util.HashMap<Integer, Boolean>)sourceState.currentKeysToStatesMap.clone();
		this.currentMouseButtonsToStatesMap = (java.util.HashMap<Integer, Boolean>)sourceState.currentMouseButtonsToStatesMap.clone();
		this.currentMouseLocation.setLocation( sourceState.currentMouseLocation );
		this.currentMouseWheelState = sourceState.currentMouseWheelState;
		this.currentInputEventType = sourceState.currentInputEventType;
		this.clickPickResult = sourceState.clickPickResult;
		this.clickPickTransformable = sourceState.clickPickTransformable;
		this.currentlySelectedObject = sourceState.currentlySelectedObject;
		this.rolloverPickTransformable = sourceState.rolloverPickTransformable;
		this.rolloverPickResult = sourceState.rolloverPickResult;
		this.rolloverHandle = sourceState.rolloverHandle;
		this.clickHandle = sourceState.clickHandle;
		this.timeCaptured = sourceState.timeCaptured;
		this.isDragEvent = sourceState.isDragEvent;
		this.dragAndDropContext = sourceState.dragAndDropContext;
	}

	@Override
	public String toString() {
		String mouseButtonState = "";
		String keyState = "";

		boolean isFirst = true;
		Object[] keyKeys = this.currentKeysToStatesMap.keySet().toArray();
		for( Object keyKey : keyKeys ) {
			if( this.currentKeysToStatesMap.get( keyKey ).booleanValue() ) {
				if( isFirst ) {
					isFirst = false;
				} else {
					keyState += ", ";
				}
				keyState += java.awt.event.KeyEvent.getKeyText( (Integer)keyKey );
			}
		}
		isFirst = true;
		Object[] mouseKeys = this.currentMouseButtonsToStatesMap.keySet().toArray();
		for( Object mouseKey : mouseKeys ) {
			if( this.currentMouseButtonsToStatesMap.get( mouseKey ).booleanValue() ) {
				if( isFirst ) {
					isFirst = false;
				} else {
					mouseButtonState += ", ";
				}
				mouseButtonState += "button " + mouseKey;
			}
		}
		return "Event Type: " + this.currentInputEventType + "\nKeys: " + keyState + "\nMouse Buttons: " + mouseButtonState + "\nMouse Wheel: " + this.currentMouseWheelState + "\nMouse Location: " + this.currentMouseLocation;

	}

	private java.awt.Point currentMouseLocation = new java.awt.Point();
	private java.util.HashMap<Integer, Boolean> currentKeysToStatesMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private java.util.HashMap<Integer, Boolean> currentMouseButtonsToStatesMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private int currentMouseWheelState = 0;
	private InputEventType currentInputEventType = InputEventType.NULL_EVENT;
	private PickResult clickPickResult = null;
	private PickResult rolloverPickResult = null;
	private AbstractTransformable rolloverPickTransformable = null;
	private AbstractTransformable clickPickTransformable = null;
	private AbstractTransformable currentlySelectedObject = null;
	private ManipulationHandle clickHandle = null;
	private ManipulationHandle rolloverHandle = null;
	private long timeCaptured = 0;
	private InputEvent inputEvent = null;
	private boolean isDragEvent = false;
	private Object dragAndDropContext = null;
}
