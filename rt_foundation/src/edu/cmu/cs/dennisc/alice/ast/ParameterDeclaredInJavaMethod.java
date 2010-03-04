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

package edu.cmu.cs.dennisc.alice.ast;

//todo: name
/**
 * @author Dennis Cosgrove
 */
public class ParameterDeclaredInJavaMethod extends ParameterDeclaredInJava {
	private static String getParameterNameFor( MethodReflectionProxy methodReflectionProxy, int index ) {
		String rv = null;
		try {
			edu.cmu.cs.dennisc.alice.reflect.ClassInfo classInfo = edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.get( methodReflectionProxy.getDeclaringClassReflectionProxy().getReification() );
			if( classInfo != null ) {
				edu.cmu.cs.dennisc.alice.reflect.MethodInfo methodInfo = classInfo.lookupInfo( methodReflectionProxy.getReification() );
				if( methodInfo != null ) {
					String[] parameterNames = methodInfo.getParameterNames();
					if( parameterNames != null ) {
						rv = parameterNames[ index ];
					}
				}
			}
		} catch( Throwable t ) {
			//t.printStackTrace();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "ParameterDeclaredInJavaMethod getParameterNameFor: ", methodReflectionProxy, index );
		}
		return rv;
	}

	private MethodDeclaredInJava m_method;
	private int m_index;
	private String m_name;
	private TypeDeclaredInJava m_valueType;
	/*package-private*/ ParameterDeclaredInJavaMethod( MethodDeclaredInJava method, int index, java.lang.annotation.Annotation[] annotations ) {
		super( annotations );
		m_method = method;
		m_index = index;
		MethodReflectionProxy methodReflectionProxy = m_method.getMethodReflectionProxy();
		m_name = getParameterNameFor( methodReflectionProxy, m_index );
		m_valueType = TypeDeclaredInJava.get( methodReflectionProxy.getParameterClassReflectionProxies()[ m_index ] );
	}
	
	public MethodDeclaredInJava getMethod() {
		return m_method;
	}
	public int getIndex() {
		return m_index;
	}
	
	@Override
	public String getName() {
		return m_name;
	}
	@Override
	public AbstractType getValueType() {
		return m_valueType;
	}

	@Override
	public boolean isVariableLength() {
		return false;
	}
	
	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof ParameterDeclaredInJavaMethod ) {
			ParameterDeclaredInJavaMethod otherPDIJM = (ParameterDeclaredInJavaMethod)other;
			return m_method.equals( otherPDIJM.m_method ) && m_index == otherPDIJM.m_index && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_name, otherPDIJM.m_name ) && m_valueType.equals( otherPDIJM.m_valueType );
		} else {
			return false;
		}
	}
}
