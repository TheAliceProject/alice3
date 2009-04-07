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

import org.alice.interact.PickHint;
import org.alice.interact.condition.MovementDescription;
import org.alice.interact.condition.PickCondition;

/**
 * @author David Culyba
 */
public class ManipulationEventCriteria {
	
	public MovementDescription description;
	public ManipulationEvent.EventType eventType;
	public PickHint targetCriteria;
	
	public ManipulationEventCriteria( ManipulationEvent.EventType eventType, MovementDescription description, PickHint targetCriteria)
	{
		this.eventType = eventType;
		this.description = description;
		this.targetCriteria = targetCriteria;
	}
	
	public boolean matches( ManipulationEvent event )
	{
		boolean isValidEventType = event.getType() == this.eventType;
		boolean isValidTarget = this.targetCriteria.intersects( PickCondition.getPickType( event.getTarget() ) );
		boolean isValidMovement = event.getMovementDescription().equals(this.description);
		return isValidEventType && isValidTarget && isValidMovement;
	}
	
	@Override
	public String toString()
	{
		return this.description.toString() + ", "+ this.targetCriteria; 
	}

}
