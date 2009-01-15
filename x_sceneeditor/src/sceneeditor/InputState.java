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

/**
 * @author David Culyba
 */
public class InputState {

	static final int MAX_KEY_INDEX = 256;
	static final int MAX_MOUSE_INDEX = 6;
	
	private java.awt.Point currentMouseLocation = new java.awt.Point();
	private boolean[] currentKeyStates = new boolean[MAX_KEY_INDEX];
	private boolean[] currentMouseButtonStates = new boolean[MAX_MOUSE_INDEX];
	private int currentMouseWheelState = 0;
	
	public InputState()
	{
		java.util.Arrays.fill(this.currentKeyStates, false);
		java.util.Arrays.fill(this.currentMouseButtonStates, false);
	}
	
	public boolean isKeyDown( int keyIndex )
	{
		return this.currentKeyStates[keyIndex];
	}
	
	public void setKeyState( int keyIndex, boolean isDown )
	{
		this.currentKeyStates[keyIndex] = isDown;
	}
	
	public void setMouseState( int mouseButton, boolean isDown )
	{
		this.currentMouseButtonStates[mouseButton] = isDown;
	}
	
	public boolean getMouseState( int mouseButton )
	{
		return this.currentMouseButtonStates[mouseButton];
	}
	
	public void setMouseWheelState( int mouseWheelClicks )
	{
		this.currentMouseWheelState = mouseWheelClicks;
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
	
	public void copyState( InputState sourceState )
	{
		for (int i=0; i<this.currentKeyStates.length; i++)
		{
			this.currentKeyStates[i] = sourceState.currentKeyStates[i];
		}
		for (int i=0; i<this.currentMouseButtonStates.length; i++)
		{
			this.currentMouseButtonStates[i] = sourceState.currentMouseButtonStates[i];
		}
		this.currentMouseLocation.setLocation( sourceState.currentMouseLocation );
		this.currentMouseWheelState = sourceState.currentMouseWheelState;
	}
	
	@Override
	public String toString()
	{
		String mouseButtonState = "";
		String keyState = "";
		
		boolean isFirst = true;
		for (int i=0; i<this.currentKeyStates.length; i++)
		{
			if (this.currentKeyStates[i])
			{
				if (isFirst)
				{
					isFirst = false;
				}
				else
				{
					keyState += ", ";
				}
				keyState += java.awt.event.KeyEvent.getKeyText( i );
			}
		}
		isFirst = true;
		for (int i=0; i<this.currentMouseButtonStates.length; i++)
		{
			if (this.currentMouseButtonStates[i])
			{
				if (isFirst)
				{
					isFirst = false;
				}
				else
				{
					mouseButtonState += ", ";
				}
				mouseButtonState += "button "+i;
			}
		}
		
		return "Keys: "+keyState+"\nMouse Buttons: "+mouseButtonState+"\nMouse Wheel: "+this.currentMouseWheelState+"\nMouse Location: "+this.currentMouseLocation;
		
	}
	
}
