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
public abstract class MemberWithParametersInfo extends MemberInfo {
	private transient Class<?>[] m_parameterClses;
	private String[] m_parameterClsNames;
	private String[] m_parameterNames;
	public MemberWithParametersInfo( Class<?> declaringCls, Class<?>[] parameterClses, String[] parameterNames ) {
		super( declaringCls );
		m_parameterClses = parameterClses;
		m_parameterClsNames = new String[ m_parameterClses.length ];
		int i = 0;
		for( Class< ? > cls : m_parameterClses ) {
			m_parameterClsNames[ i++ ] = cls.getName();
		}
		m_parameterNames = parameterNames;
	}
	public MemberWithParametersInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	public Class<?>[] getParameterClses() {
		if( m_parameterClses != null ) {
			//pass
		} else {
			m_parameterClses = new Class<?>[ m_parameterClsNames.length ];
			int i = 0;
			for( String name : m_parameterClsNames ) {
				m_parameterClses[ i++ ] = ReflectionUtilities.getClassForName( name );
			}
		}
		return m_parameterClses;
	}
	public String[] getParameterNames() {
		return m_parameterNames;
	}
	@Override
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super.decode( binaryDecoder );
		m_parameterClsNames = binaryDecoder.decodeStringArray();
		m_parameterNames = binaryDecoder.decodeStringArray();
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( m_parameterClsNames );
		binaryEncoder.encode( m_parameterNames );
	}
	
}
