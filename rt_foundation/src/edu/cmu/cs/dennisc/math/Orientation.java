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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public interface Orientation {
	public void setNaN();
	public boolean isNaN();
	public void setIdentity();
	public boolean isIdentity();

	public OrthogonalMatrix3x3 createOrthogonalMatrix3x3();
	public UnitQuaternion createUnitQuaternion();
	public AxisRotation createAxisRotation();
	public EulerAngles createEulerAngles();
	public ForwardAndUpGuide createForwardAndUpGuide();

	public OrthogonalMatrix3x3 getValue( OrthogonalMatrix3x3 rv );
	public UnitQuaternion getValue( UnitQuaternion rv );
	public AxisRotation getValue( AxisRotation rv );
	public EulerAngles getValue( EulerAngles rv );
	public ForwardAndUpGuide getValue( ForwardAndUpGuide rv );

	public void setValue( OrthogonalMatrix3x3 m );
	public void setValue( UnitQuaternion q );
	public void setValue( AxisRotation aa );
	public void setValue( EulerAngles ea );
	public void setValue( ForwardAndUpGuide faug );
}
