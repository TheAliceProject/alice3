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
package org.alice.interact;

import java.awt.event.KeyEvent;

/**
 * @author David Culyba
 */
public class ModifierMask {
	enum TestType
	{
		ALL_MUST_BE_VALID,
		ANY_MAY_BE_VALID,
	}
	
	public static ModifierKey[] NO_MODIFIERS_DOWN = { ModifierKey.NOT_CONTROL, ModifierKey.NOT_ALT, ModifierKey.NOT_SHIFT};
	
	enum ModifierKey
	{
		CONTROL (KeyEvent.VK_CONTROL, false),
		NOT_CONTROL (KeyEvent.VK_CONTROL, true),
		ALT (KeyEvent.VK_ALT, false),
		NOT_ALT (KeyEvent.VK_ALT, true),
		SHIFT (KeyEvent.VK_SHIFT, false),
		NOT_SHIFT (KeyEvent.VK_SHIFT, true);
		
		private int keyValue;
		private boolean inverted;
		
		private ModifierKey( int keyValue, boolean inverted )
		{
			this.keyValue = keyValue;
			this.inverted = inverted;
		}
		
		public int getKeyValue()
		{
			return this.keyValue;
		}
		
		public boolean testKey( InputState state )
		{
			boolean isDown = state.isKeyDown( this.keyValue );
			if (this.inverted)
			{
				return !isDown;
			}
			return isDown;
		}
		
	}
	
	private ModifierKey[] keys;
	private TestType testType; 
	
	public ModifierMask()
	{
		this.testType = TestType.ALL_MUST_BE_VALID;
		setKeys( new ModifierKey[0]);
	}
	
	public ModifierMask( ModifierKey[] keys, TestType testType )
	{
		this.testType = testType;
		setKeys(keys);
	}
	
	public ModifierMask( ModifierKey[] keys)
	{
		this(keys, TestType.ALL_MUST_BE_VALID);
	}

	public ModifierMask( ModifierKey key)
	{
		this(key, TestType.ALL_MUST_BE_VALID);
	}
	
	public  ModifierMask( ModifierKey key, TestType testType )
	{
		ModifierKey[] keyArray = { key };
		setKeys( keyArray );
		this.testType = testType;
	}
	
	public void setKeys( ModifierKey[] keys)
	{
		this.keys = keys;
	}
	
	public boolean anyValid( InputState state )
	{
		for (int i=0; i<this.keys.length; i++)
		{
			if ( this.keys[i].testKey( state ) )
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean allValid( InputState state )
	{
		for (int i=0; i<this.keys.length; i++)
		{
			if ( !this.keys[i].testKey( state ))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean test( InputState state )
	{
		switch (this.testType)
		{
		case ANY_MAY_BE_VALID : return anyValid(state);
		case ALL_MUST_BE_VALID : return allValid(state);
		default : return false;
		}
	}
	

}
