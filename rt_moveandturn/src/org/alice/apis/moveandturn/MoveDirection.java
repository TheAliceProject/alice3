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

package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public enum MoveDirection {
	LEFT    ( edu.cmu.cs.dennisc.math.Vector3.createNegativeXAxis() ),
	RIGHT   ( edu.cmu.cs.dennisc.math.Vector3.createPositiveXAxis() ),
	UP      ( edu.cmu.cs.dennisc.math.Vector3.createPositiveYAxis() ),
	DOWN    ( edu.cmu.cs.dennisc.math.Vector3.createNegativeYAxis() ),
	FORWARD ( edu.cmu.cs.dennisc.math.Vector3.createNegativeZAxis() ),
	BACKWARD( edu.cmu.cs.dennisc.math.Vector3.createPositiveZAxis() );
	
	private edu.cmu.cs.dennisc.math.Vector3 m_axis;
	
	private MoveDirection( edu.cmu.cs.dennisc.math.Vector3 axis ) {
		m_axis = axis;
	}
	
	//REMOVED/*package protected*/ 
	public edu.cmu.cs.dennisc.math.Vector3 getAxis() {
		return m_axis;
	}
	
	public edu.cmu.cs.dennisc.math.Vector3 getAxis(double amount) {
		return new edu.cmu.cs.dennisc.math.Vector3(m_axis.x * amount, m_axis.y * amount, m_axis.z * amount);
	}
}
