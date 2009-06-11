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
public class ApplyRotationAboutArbitraryAxisAnimation extends AbstractApplyRotationAnimation {
	private edu.cmu.cs.dennisc.math.Vector3 m_axis = new edu.cmu.cs.dennisc.math.Vector3();
	public ApplyRotationAboutArbitraryAxisAnimation() {
		m_axis.setNaN();
	}
	public ApplyRotationAboutArbitraryAxisAnimation( edu.cmu.cs.dennisc.scenegraph.Transformable sgSubject, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy, edu.cmu.cs.dennisc.math.Angle angle, edu.cmu.cs.dennisc.math.Vector3 axis ) {
		super( sgSubject, sgAsSeenBy, angle );
		setAxis( axis );
	}
	public edu.cmu.cs.dennisc.math.Vector3 accessAxis() {
		return m_axis;
	}
	public edu.cmu.cs.dennisc.math.Vector3 getAxis( edu.cmu.cs.dennisc.math.Vector3 rv ) {
		rv.set( m_axis );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Vector3 getAxis() {
		return getAxis( new edu.cmu.cs.dennisc.math.Vector3() );
	}
	public void setAxis( edu.cmu.cs.dennisc.math.Vector3 axis ) {
		m_axis.set( axis );
	}
	
	@Override
	protected void applyRotationInRadians( double angleInRadians ) {
		getSubject().applyRotationAboutArbitraryAxisInRadians( m_axis, angleInRadians, getAsSeenBy() );
	}
}
