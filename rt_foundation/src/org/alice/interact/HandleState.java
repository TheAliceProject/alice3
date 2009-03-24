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

/**
 * @author David Culyba
 */
public class HandleState {
	private int isVisible = 0;
	private int isActive = 0;
	private int isRollover = 0;
	private int isMuted = 1;
	private int isFaded = 0;
	
	public HandleState()
	{
		
	}
	
	public HandleState( HandleState state )
	{
		this.setState(state);
	}
	
	public void setState( HandleState state )
	{
		this.isVisible = state.isVisible;
		this.isActive = state.isActive;
		this.isRollover = state.isRollover;
		this.isMuted = state.isMuted;
		this.isFaded = state.isFaded;
	}
	
	@Override
	public String toString()
	{
		return "visible("+isVisible+"), active("+isActive+"), rollover("+isRollover+"), muted("+isMuted+"), faded("+isFaded;
	}
	
	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible != 0;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible( boolean isVisible ) {
		int visibleValue = isVisible ? 1 : -1;
		this.isVisible += visibleValue;
		
		//visibility setting trumps faded and resets it
		this.isFaded = 0;
		
		if (this.isVisible < 0)
		{
			System.err.println("Visible went below 0: "+this.isVisible);
		}
	}

	/**
	 * @return the isFaded
	 */
	public boolean isFaded() {
		return isFaded != 0;
	}

	/**
	 * @param isFaded the isFaded to set
	 */
	public void setFaded( boolean isFaded ) {
		int fadedValue = isFaded ? 1 : -1;
		this.isFaded += fadedValue;
		if (this.isFaded < 0)
		{
			System.err.println("Faded went below 0: "+this.isFaded);
		}
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive != 0;
	}

	
	/**
	 * @param isActive the isActive to set
	 */
	public void setActive( boolean isActive ) {
		int activeValue = isActive ? 1 : -1;
		this.isActive += activeValue;
		if (this.isActive < 0)
		{
			System.err.println("Active went below 0: "+this.isActive);
		}
	}

	/**
	 * @return the isRollover
	 */
	public boolean isRollover() {
		return isRollover != 0;
	}

	/**
	 * @param isRollover the isRollover to set
	 */
	public void setRollover( boolean isRollover ) {
		int rolloverValue = isRollover ? 1 : -1;
		this.isRollover += rolloverValue;
		if (this.isRollover < 0)
		{
			System.err.println("Rollover went below 0: "+this.isRollover);
		}
	}

	/**
	 * @return the isMuted
	 */
	public boolean isMuted() {
		return isMuted != 0;
	}

	/**
	 * @param isMuted the isMuted to set
	 */
	public void setMuted( boolean isMuted ) {
		int mutedValue = isMuted ? 1 : -1;
		this.isMuted += mutedValue;
		if (this.isMuted < 0)
		{
			System.err.println("Muted went below 0: "+this.isMuted);
		}	
	}
}
