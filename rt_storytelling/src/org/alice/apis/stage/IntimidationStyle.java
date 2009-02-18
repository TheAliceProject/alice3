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
public enum IntimidationStyle {
	PUNCH_HAND( Adult.Cycle.PUNCH_HAND ),
	CRACK_KNUCKLES( Adult.Cycle.CRACK_KNUCKLES_TO_INTIMIDATE ),
	RING_FINGERS( Adult.Cycle.RING_FINGERS ),
	LOOK_AWAY_AND_CROSS_ARMS( Adult.Cycle.BIDE_TIME );
	private Adult.Cycle m_cycle;
	IntimidationStyle( Adult.Cycle cycle ) {
		m_cycle = cycle;
	}
	public Adult.Cycle getCycle() {
		return m_cycle;
	}
	public static IntimidationStyle getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( IntimidationStyle.class );
	}
}
	
