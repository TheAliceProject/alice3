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
public class Position {
	private edu.cmu.cs.dennisc.math.Point3 internal = new edu.cmu.cs.dennisc.math.Point3();
	public Position() {
	}
	public Position( edu.cmu.cs.dennisc.math.Point3 internal ) {
		this.internal.set( internal );
	}
	public Position( Number x, Number y, Number z ) {
		this.internal.set( x.doubleValue(), y.doubleValue(), z.doubleValue() );
	}
	public edu.cmu.cs.dennisc.math.Point3 get( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( this.internal );
		return rv;
	}
}
