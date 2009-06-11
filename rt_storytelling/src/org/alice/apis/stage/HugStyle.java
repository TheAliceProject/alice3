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
public enum HugStyle {
	FRIENDLY( Adult.CycleAB.HUG_FRIENDLY_ACCEPTED, Adult.CycleAB.HUG_FRIENDLY_REJECTED ),
	//ROMANTICLY( Adult.HUG_ROMANTIC_ACCEPTED, Adult.HUG_ROMANTIC_REJECTED ),
	HEAVY_ON_THE_SQUEEZE( Adult.CycleAB.HUG_SQUEEZE_ACCEPTED, Adult.CycleAB.HUG_SQUEEZE_REJECTED ),
	JUMPING_INTO_ARMS( Adult.CycleAB.HUG_JUMP_INTO_ARMS_ACCEPTED, Adult.CycleAB.HUG_JUMP_INTO_ARMS_REJECTED );
	private Adult.CycleAB m_acceptCycle;
	private Adult.CycleAB m_rejectCycle;
	HugStyle( Adult.CycleAB acceptCycle, Adult.CycleAB rejectCycle ) {
		m_acceptCycle = acceptCycle;
		m_rejectCycle = rejectCycle;
	}
	public Adult.CycleAB getAcceptCycle() {
		return m_acceptCycle;
	}
	public Adult.CycleAB getRejectCycle() {
		return m_rejectCycle;
	}
	public static HugStyle getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( HugStyle.class );
	}
}
	
