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
public enum SearchForSourceOfOdorStyle {
	//perform( Cycle.DISCOVER_ARM_PIT_REEKS_SUBTLY );
	//perform( Cycle.DISCOVER_ARM_PIT_REEKS );
	DISCOVER_UNDER_ARM( Adult.Cycle.SMELL_UNDERARMS_APPROVINGLY ),
	FAIL_TO_DISCOVER_UNDER_ARM( Adult.Cycle.SMELL_UNDERARMS_WITHOUT_REACHING_CONCLUSION );
	private Adult.Cycle m_cycle;
	SearchForSourceOfOdorStyle( Adult.Cycle cycle ) {
		m_cycle = cycle;
	}
	public Adult.Cycle getCycle() {
		return m_cycle;
	}
	public static SearchForSourceOfOdorStyle getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( SearchForSourceOfOdorStyle.class );
	}
}
	
