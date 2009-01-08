/*
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
package org.alice.apis.storytelling;

/**
 * @author Dennis Cosgrove
 */
public enum GreetStyle {
	HAND_SHAKE( Adult.CycleAB.SHAKE_HANDS ),
	CROOKED_ARM_SHAKE( Adult.CycleAB.CROOKED_ARM_SHAKE ),
	FINGER_SHAKE( Adult.CycleAB.FINGER_SHAKE ),
	FIST_BUMP( Adult.CycleAB.FIST_BUMP ),
	HIGH_FIVE( Adult.CycleAB.HIGH_FIVE ),
	HIGH_AND_LOW_FIVE( Adult.CycleAB.HIGH_AND_LOW_FIVE ),
	REQUEST_AND_RETURN_SOME_SKIN_TO_BE_SLIPPED( Adult.CycleAB.OFFER_AND_RETURN_LOW_FIVE ),
	REQUEST_AND_RETURN_LOW_TEN( Adult.CycleAB.OFFER_AND_RETURN_LOW_TEN );
	private Adult.CycleAB m_cycle;
	GreetStyle( Adult.CycleAB cycle ) {
		m_cycle = cycle;
	}
	public Adult.CycleAB getCycle() {
		return m_cycle;
	}
	public static GreetStyle getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( GreetStyle.class );
	}
}
	
