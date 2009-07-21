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

/**
 * @author Dennis Cosgrove
 */
public class MethodInfo extends MemberWithParametersInfo {
	private transient java.lang.reflect.Method mthd;
	private String name;
	public MethodInfo( ClassInfo classInfo, String name, String[] parameterClassNames, String[] parameterNames ) {
		super( classInfo, parameterClassNames, parameterNames );
		this.name = name;
	}
	public MethodInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	
	public java.lang.reflect.Method getMthd() {
		if( this.mthd != null ) {
			//pass
		} else {
			this.mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( getDeclaringCls(), this.name, getParameterClses() );
		}
		return this.mthd;
	}

	@Override
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super.decode( binaryDecoder );
		this.name = binaryDecoder.decodeString();
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.name );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getName() );
		sb.append( "[name=" );
		sb.append( this.name );
		sb.append( "]" );
		return sb.toString();
	}
}
