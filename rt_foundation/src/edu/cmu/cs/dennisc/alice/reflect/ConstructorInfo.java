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
package edu.cmu.cs.dennisc.alice.reflect;

import edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities;

/**
 * @author Dennis Cosgrove
 */
public class ConstructorInfo extends MemberWithParametersInfo {
	private transient java.lang.reflect.Constructor< ? > cnstrctr = null;
	public ConstructorInfo( ClassInfo classInfo, String[] parameterClassNames, String[] parameterNames ) {
		super( classInfo, parameterClassNames, parameterNames );
	}
	public ConstructorInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public java.lang.reflect.Constructor< ? > getCnstrctr() {
		if( this.cnstrctr != null ) {
			//pass
		} else {
			this.cnstrctr = ReflectionUtilities.getConstructor( getDeclaringCls(), getParameterClses() );
		}
		return this.cnstrctr;
	}
}
