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
public class MethodInfo extends MemberWithParametersInfo {
	private transient java.lang.reflect.Method m_mthd;
	private transient boolean m_isNonExistent = false;
	private String m_name;
	public MethodInfo( java.lang.reflect.Method mthd, String[] parameterNames ) {
		super( mthd.getDeclaringClass(), mthd.getParameterTypes(), parameterNames );
		m_mthd = mthd;
		m_name = m_mthd.getName();
	}
	public MethodInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	
	public java.lang.reflect.Method getMthd() {
		if( m_mthd != null || m_isNonExistent ) {
			//pass
		} else {
			try {
				m_mthd = ReflectionUtilities.getMethod( getDeclaringCls(), m_name, getParameterClses() );
			} catch( RuntimeException re ) {
				m_mthd = null;
				m_isNonExistent = true;
			}
		}
		return m_mthd;
	}

	@Override
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super.decode( binaryDecoder );
		m_name = binaryDecoder.decodeString();
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( m_name );
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( getClass().getName() );
		sb.append( "[name=" );
		sb.append( m_name );
		sb.append( "]" );
		return sb.toString();
	}
}
