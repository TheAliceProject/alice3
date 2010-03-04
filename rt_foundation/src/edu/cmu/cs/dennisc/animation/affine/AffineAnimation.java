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
package edu.cmu.cs.dennisc.animation.affine;

/**
 * @author Dennis Cosgrove
 */
public abstract class AffineAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
	private edu.cmu.cs.dennisc.scenegraph.AbstractTransformable m_sgSubject = null;
	private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgAsSeenBy = null;
	public AffineAnimation() {
	}
	public AffineAnimation( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgSubject, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		setSubject( sgSubject );
		setAsSeenBy( sgAsSeenBy );
	}
	
	public edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSubject() {
		return m_sgSubject;
	}
	public void setSubject( edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgSubject ) {
		assert sgSubject != null;
		m_sgSubject = sgSubject;
	}
	
	public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getAsSeenBy() {
		return m_sgAsSeenBy;
	}
	public void setAsSeenBy( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		assert sgAsSeenBy != null;
		m_sgAsSeenBy = sgAsSeenBy;
	}
}
