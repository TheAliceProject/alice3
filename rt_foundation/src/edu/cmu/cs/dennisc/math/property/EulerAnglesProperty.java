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
package edu.cmu.cs.dennisc.math.property;

/**
 * @author Dennis Cosgrove
 */
public class EulerAnglesProperty extends edu.cmu.cs.dennisc.property.InstanceProperty< edu.cmu.cs.dennisc.math.EulerAngles > implements edu.cmu.cs.dennisc.property.CopyableProperty< edu.cmu.cs.dennisc.math.EulerAngles >{
	public EulerAnglesProperty( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, edu.cmu.cs.dennisc.math.EulerAngles value ) {
		super( owner, value );
	}
	public void setValue( edu.cmu.cs.dennisc.property.InstancePropertyOwner owner, edu.cmu.cs.dennisc.math.EulerAngles value ) {
		assert value != null;
		assert value.isNaN() == false;  
		super.setValue( owner, value );
	}
	public edu.cmu.cs.dennisc.math.EulerAngles getCopy( edu.cmu.cs.dennisc.math.EulerAngles rv, edu.cmu.cs.dennisc.property.PropertyOwner owner ) {
		rv.setValue( getValue( owner ) );
		return rv;
	}
	public edu.cmu.cs.dennisc.math.EulerAngles getCopy( edu.cmu.cs.dennisc.property.PropertyOwner owner ) {
		return getCopy( edu.cmu.cs.dennisc.math.EulerAngles.createNaN(), owner );
	}
	public void setCopy( edu.cmu.cs.dennisc.property.PropertyOwner owner, edu.cmu.cs.dennisc.math.EulerAngles value ) {
		//todo?
		setValue( owner, new edu.cmu.cs.dennisc.math.EulerAngles( value ) );
	}
}
