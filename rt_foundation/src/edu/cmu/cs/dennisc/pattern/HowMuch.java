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
package edu.cmu.cs.dennisc.pattern;

/**
 * @author Dennis Cosgrove
 */
public enum HowMuch {
	COMPONENT_ONLY(true, false, false), 
	COMPONENT_AND_CHILDREN_ONLY(true, true, false), 
	CHILDREN_ONLY(false, true, false), 
	DESCENDANTS_ONLY(false, true, true), 
	COMPONENT_AND_DESCENDANTS(true, true, true);
	private boolean m_isComponentACandidate;
	private boolean m_isChildACandidate;
	private boolean m_isGrandchildAndBeyondACandidate;

	HowMuch( boolean isComponentACandidate, boolean isChildACandidate, boolean isGrandchildAndBeyondACandidate ) {
		m_isComponentACandidate = isComponentACandidate;
		m_isChildACandidate = isChildACandidate;
		m_isGrandchildAndBeyondACandidate = isGrandchildAndBeyondACandidate;
	}
	public boolean isComponentACandidate() {
		return m_isComponentACandidate;
	}
	public boolean isChildACandidate() {
		return m_isChildACandidate;
	}
	public boolean isGrandchildAndBeyondACandidate() {
		return m_isGrandchildAndBeyondACandidate;
	}
}
