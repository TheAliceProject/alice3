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
package org.alice.interact.handle;

/**
 * @author David Culyba
 */
public class HandleState {
	
	private boolean isActive = false;
	private boolean isRollover = false;
	private boolean isVisible = false;
	
	public HandleState()
	{
		
	}
	
	public HandleState(HandleState handleState)
	{
		this.isActive = handleState.isActive;
		this.isRollover = handleState.isRollover;
		this.isVisible = handleState.isVisible;
	}
	
	//Any of the booleans being true makes the handle visible
	public boolean shouldRender()
	{
		return this.isVisible || this.isActive || this.isRollover;
	}
	
	
	public boolean isVisible()
	{
		return this.isVisible;
	}
	
	public boolean setVisible(boolean isVisible)
	{
		return this.isVisible = isVisible;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive( boolean isActive ) {
		this.isActive = isActive;
	}

	/**
	 * @return the isRollover
	 */
	public boolean isRollover() {
		return isRollover;
	}

	/**
	 * @param isRollover the isRollover to set
	 */
	public void setRollover( boolean isRollover ) {
		this.isRollover = isRollover;
	}

}
