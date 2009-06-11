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
public enum PointWithBothHandsStyle {
	NORMAL( Adult.Cycle.POINT_BOTH_HANDS ),
	WITH_A_FLOURISH( Adult.Cycle.POINT_BOTH_HANDS_WITH_A_FLOURISH ),
	WITH_A_CLAP( Adult.Cycle.CLAP_HEY_THERE );
	private Adult.Cycle m_cycle;
	PointWithBothHandsStyle( Adult.Cycle cycle ) {
		m_cycle = cycle;
	}
	public Adult.Cycle getCycle() {
		return m_cycle;
	}
	public static PointWithBothHandsStyle getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( PointWithBothHandsStyle.class );
	}
}
	
