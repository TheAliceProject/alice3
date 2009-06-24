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
package org.alice.interact.event;

import org.alice.interact.InputState;
import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class ManipulationEvent {
	
	public enum EventType
	{
		Translate,
		Scale,
		Rotate,
	}
	
	private EventType type;
	private MovementDescription movementDescription;
	private Transformable target;
	private InputState inputState;
	
	public ManipulationEvent( EventType type, MovementDescription movementDescription, Transformable target)
	{
		this.type = type;
		this.movementDescription = movementDescription;
		this.target = target;
		this.inputState = null;
	}
	
	@Override
	public String toString()
	{
		return this.type + ":["+this.movementDescription.toString()+"(" + this.target+")]";
	}
	
	/**
	 * @return the target
	 */
	public Transformable getTarget() {
		return target;
	}

	/**
	 * @return the type
	 */
	public EventType getType() {
		return type;
	}

	/**
	 * @return the movementDescription
	 */
	public MovementDescription getMovementDescription() {
		return movementDescription;
	}

	/**
	 * @return the inputState
	 */
	public InputState getInputState() {
		return inputState;
	}
	
	public void setInputState( InputState inputState )
	{
		this.inputState = inputState;
	}
	
}
