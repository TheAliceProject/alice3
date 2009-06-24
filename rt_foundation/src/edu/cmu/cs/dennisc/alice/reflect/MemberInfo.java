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
public abstract class MemberInfo implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private transient Class<?> m_declaringCls;
	private String m_declaringClsName;
	public MemberInfo( Class<?> declaringCls ) {
		m_declaringCls = declaringCls;
		m_declaringClsName = m_declaringCls.getName();
	}
	public MemberInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		decode( binaryDecoder );
	}
	protected Class<?> getDeclaringCls() {
		if( m_declaringCls != null ) {
			//pass
		} else {
			m_declaringCls = ReflectionUtilities.getClassForName( m_declaringClsName );
		}
		return m_declaringCls;
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		m_declaringClsName = binaryDecoder.decodeString();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( m_declaringClsName );
	}
}
