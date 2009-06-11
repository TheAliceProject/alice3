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
package org.alice.apis.stage;

/**
 * @author Dennis Cosgrove
 */
public enum RockBackAndForthStyle {
	POPPING_FIST_IN_HAND( Adult.Cycle.ROCK_BACK_AND_FORTH ),
	WITH_ARMS_CROSSED( Adult.Cycle.ROCK_BACK_AND_FORTH_WITH_ARMS_CROSSED );
	private Adult.Cycle m_cycle;
	RockBackAndForthStyle( Adult.Cycle cycle ) {
		m_cycle = cycle;
	}
	public Adult.Cycle getCycle() {
		return m_cycle;
	}
	public static RockBackAndForthStyle getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( RockBackAndForthStyle.class );
	}
}
	
