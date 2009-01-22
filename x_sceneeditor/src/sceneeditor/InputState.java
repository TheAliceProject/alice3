/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package sceneeditor;

import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author David Culyba
 */
public class InputState {

	enum InputEventType
	{
		MOUSE_WHEEL,
		MOUSE_DOWN,
		MOUSE_UP,
		MOUSE_DRAGGED,
		KEY_DOWN,
		KEY_UP,
		NULL_EVENT,
	}
	
	
	private java.awt.Point currentMouseLocation = new java.awt.Point();
	private java.util.HashMap< Integer, Boolean > currentKeysToStatesMap = new java.util.HashMap< Integer, Boolean >();
	private java.util.HashMap< Integer, Boolean > currentMouseButtonsToStatesMap = new java.util.HashMap< Integer, Boolean >();
	private int currentMouseWheelState = 0;
	private InputEventType currentInputEventType = InputEventType.NULL_EVENT;
	private edu.cmu.cs.dennisc.lookingglass.PickResult currentPickResult = null;
	private Transformable currentlySelectedObject = null;
	
	/**
	 * @return the currentlySelectedObject
	 */
	public Transformable getCurrentlySelectedObject() {
		return currentlySelectedObject;
	}

	/**
	 * @param currentlySelectedObject the currentlySelectedObject to set
	 */
	public void setCurrentlySelectedObject( Transformable currentlySelectedObject ) {
		this.currentlySelectedObject = currentlySelectedObject;
	}

	public InputState()
	{
	}
	
	public boolean isKeyDown( int keyIndex )
	{
		if (currentKeysToStatesMap.containsKey( keyIndex ))
		{
			return currentKeysToStatesMap.get( keyIndex ).booleanValue();
		}
		return false;
	}
	
	public void setKeyState( int keyIndex, boolean isDown )
	{
		currentKeysToStatesMap.put( keyIndex, new Boolean(isDown) );
	}
	
	public void setMouseState( int mouseButton, boolean isDown )
	{
		Integer mouseInt = new Integer(mouseButton);
		currentMouseButtonsToStatesMap.put( mouseInt, new Boolean(isDown) );
	}
	
	public boolean isMouseDown( int mouseButton )
	{
		if (currentMouseButtonsToStatesMap.containsKey( mouseButton ))
		{
			return currentMouseButtonsToStatesMap.get( mouseButton ).booleanValue();
		}
		return false;
	}
	
	public void setMouseWheelState( int mouseWheelMovement )
	{
		this.currentMouseWheelState = mouseWheelMovement;
	}
	
	public int getMouseWheelState()
	{
		return this.currentMouseWheelState;
	}
	
	public void setMouseLocation( java.awt.Point mouseLocation )
	{
		this.currentMouseLocation = mouseLocation;
	}
	
	public java.awt.Point getMouseLocation()
	{
		return this.currentMouseLocation;
	}
	
	public void setInputEventType( InputEventType eventType )
	{
		this.currentInputEventType = eventType;
	}
	
	public InputEventType getInputEventType()
	{
		return this.currentInputEventType;
	}
	
	public void setPickResult( edu.cmu.cs.dennisc.lookingglass.PickResult pickResult )
	{
		this.currentPickResult = pickResult;
	}
	
	public edu.cmu.cs.dennisc.lookingglass.PickResult getPickResult()
	{
		return this.currentPickResult;
	}
	
	public Transformable getPickedTransformable()
	{
		if (this.currentPickResult != null)
		{
			Visual sgVisual = this.currentPickResult.getVisual();
			if( sgVisual != null ) {
				Composite sgParent = sgVisual.getParent();
				if( sgParent instanceof edu.cmu.cs.dennisc.scenegraph.Transformable ) {
					return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgParent;
				}
			}
		}
		return null;
	}
	
	public void copyState( InputState sourceState )
	{
		this.currentKeysToStatesMap = (java.util.HashMap< Integer, Boolean >)sourceState.currentKeysToStatesMap.clone();
		this.currentMouseButtonsToStatesMap = (java.util.HashMap< Integer, Boolean >)sourceState.currentMouseButtonsToStatesMap.clone();
		this.currentMouseLocation.setLocation( sourceState.currentMouseLocation );
		this.currentMouseWheelState = sourceState.currentMouseWheelState;
		this.currentInputEventType = sourceState.currentInputEventType;
		this.currentPickResult = sourceState.currentPickResult;
		this.currentlySelectedObject = sourceState.currentlySelectedObject;
	}
	
	@Override
	public String toString()
	{
		String mouseButtonState = "";
		String keyState = "";
		
		boolean isFirst = true;
		Object[] keyKeys = this.currentKeysToStatesMap.keySet().toArray();
		for (int i=0; i<keyKeys.length; i++)
		{
			if (this.currentKeysToStatesMap.get( keyKeys[i] ).booleanValue())
			{
				if (isFirst)
				{
					isFirst = false;
				}
				else
				{
					keyState += ", ";
				}
				keyState += java.awt.event.KeyEvent.getKeyText( (Integer)keyKeys[i] );
			}
		}
		isFirst = true;
		Object[] mouseKeys = this.currentMouseButtonsToStatesMap.keySet().toArray();
		for (int i=0; i<mouseKeys.length; i++)
		{
			if (this.currentMouseButtonsToStatesMap.get( mouseKeys[i] ).booleanValue())
			{
				if (isFirst)
				{
					isFirst = false;
				}
				else
				{
					mouseButtonState += ", ";
				}
				mouseButtonState += "button "+mouseKeys[i] ;
			}
		}
		return "Event Type: "+this.currentInputEventType+"\nKeys: "+keyState+"\nMouse Buttons: "+mouseButtonState+"\nMouse Wheel: "+this.currentMouseWheelState+"\nMouse Location: "+this.currentMouseLocation;
		
	}
	
}
