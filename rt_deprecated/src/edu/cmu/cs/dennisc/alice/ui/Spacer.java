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
package edu.cmu.cs.dennisc.alice.ui;

/**
 * @author Dennis Cosgrove
 */
public class Spacer {
	private static final float DEFAULT_VELOCITY_MAGNITUDE = 100.0f;
	private float m_currentHeight = 0.0f;
	private float m_desiredHeight = 0.0f;
	private float m_maximumHeight = Float.NaN;

	protected float getVelocityMagnitude() {
		return DEFAULT_VELOCITY_MAGNITUDE;
	}
	public void reset() {
		m_currentHeight = 0.0f;
		m_desiredHeight = 0.0f;
	}
	public float getCurrentHeight() {
		return m_currentHeight;
	}
	public float getDesiredHeight() {
		return m_desiredHeight;
	}
	public void setDesiredHeight( float desiredHeight ) {
		assert desiredHeight >= 0.0f;
		m_desiredHeight = desiredHeight;
	}
	public float getMaximumHeight() {
		return m_maximumHeight;
	}
	public void setMaximumHeight( float maximumHeight ) {
		//assert maximumHeight >= 0.0f;
		maximumHeight = Math.max( maximumHeight, 0.0f );
		m_maximumHeight = maximumHeight;
	}
	private float getActualDesiredHeight() {
		if( Float.isNaN( m_maximumHeight ) ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "isNaN:", m_desiredHeight, m_maximumHeight );
			return m_desiredHeight;
		} else {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "getActualDesiredHeight:", m_desiredHeight, m_maximumHeight );
			return Math.min( m_desiredHeight, m_maximumHeight );
		}
	}
	public boolean update( double tDelta ) {
		float actualDesiredHeight = getActualDesiredHeight();
		if( actualDesiredHeight == m_currentHeight ) {
			return false;
		} else {
			float fDelta = (float)(getVelocityMagnitude() * tDelta);
			if( m_currentHeight > actualDesiredHeight ) {
				m_currentHeight = Math.max( m_currentHeight - fDelta, actualDesiredHeight );
			} else {
				m_currentHeight = Math.min( m_currentHeight + fDelta, actualDesiredHeight );
			}
			return true;
		}
	}
}
