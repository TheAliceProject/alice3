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
public class ClassInfo implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private transient boolean m_isGetClassForNamAlreadyAttempted = false;
	private transient Class<?> m_cls;
	private String m_clsName;

	private java.util.List< ConstructorInfo > m_constructorInfos = new java.util.LinkedList< ConstructorInfo >();
	private java.util.List< MethodInfo > m_methodInfos = new java.util.LinkedList< MethodInfo >();
	
	public ClassInfo( Class<?> cls ) {
		m_cls = cls;
		m_clsName = m_cls.getName();
	}
	public ClassInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		decode( binaryDecoder );
	}
	
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( m_clsName );
		binaryEncoder.encode( edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( m_constructorInfos, ConstructorInfo.class ) );
		binaryEncoder.encode( edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( m_methodInfos, MethodInfo.class ) );
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		m_clsName = binaryDecoder.decodeString();
		edu.cmu.cs.dennisc.util.CollectionUtilities.set( m_constructorInfos, binaryDecoder.decodeBinaryEncodableAndDecodableArray( ConstructorInfo.class ) );
		edu.cmu.cs.dennisc.util.CollectionUtilities.set( m_methodInfos, binaryDecoder.decodeBinaryEncodableAndDecodableArray( MethodInfo.class ) );
	}
	protected Class<?> getCls() {
		if( m_isGetClassForNamAlreadyAttempted ) {
			//pass
		} else {
			m_isGetClassForNamAlreadyAttempted = true;
//			if( m_cls != null ) {
//				//pass
//			} else {
				try {
					m_cls = ReflectionUtilities.getClassForName( m_clsName );
				} catch( Throwable t ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( t, m_clsName );
				}
//			}
		}
		return m_cls;
	}
	public void add( java.lang.reflect.Constructor< ? > cnstrctr, String[] parameterNames ) {
		assert m_cls != null;
		assert cnstrctr.getDeclaringClass().equals( m_cls );
		ConstructorInfo constructorInfo = new ConstructorInfo( cnstrctr, parameterNames );
		m_constructorInfos.add( constructorInfo );
	}
	public void add( java.lang.reflect.Method mthd, String[] parameterNames ) {
		assert m_cls != null;
		assert mthd.getDeclaringClass().equals( m_cls );
		MethodInfo methodInfo = new MethodInfo( mthd, parameterNames );
		m_methodInfos.add( methodInfo );
	}

	public Iterable< ConstructorInfo > getConstructorInfos() {
		return m_constructorInfos;
	}
	public Iterable< MethodInfo > getMethodInfos() {
		return m_methodInfos;
	}
	
	private java.util.Set< MethodInfo > m_outOfDateMethodInfos = new java.util.HashSet< MethodInfo >();
	public MethodInfo lookupInfo( java.lang.reflect.Method mthd ) {
		for( MethodInfo methodInfo : getMethodInfos() ) {
			if( m_outOfDateMethodInfos.contains( methodInfo ) ) {
				//pass
			} else {
				try {
					java.lang.reflect.Method m = methodInfo.getMthd();
					if( m.equals( mthd ) ) {
						return methodInfo;
					}
				} catch( RuntimeException re ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "no such method", methodInfo, "on Class", m_cls );
					m_outOfDateMethodInfos.add( methodInfo ); 
				}
			}
		}
		return null;
	}
	public ConstructorInfo lookupInfo( java.lang.reflect.Constructor cnstrctr ) {
		for( ConstructorInfo constructorInfo : getConstructorInfos() ) {
			if( constructorInfo.getCnstrctr().equals( cnstrctr ) ) {
				return constructorInfo;
			}
		}
		return null;
	}
}
