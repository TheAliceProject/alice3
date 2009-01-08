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
public class ParameterDeclaredInJavaConstructor extends ParameterDeclaredInJava {
	private static String getParameterNameFor( java.lang.reflect.Constructor<?> cnstrctr, int index ) {
		//todo
//		edu.cmu.cs.dennisc.reflect.ClassInfo classInfo = edu.cmu.cs.dennisc.reflect.ClassInfoManager.get( cnstrctr.getDeclaringClass() );
//		if( classInfo != null ) {
//			return classInfo.getParameterNamesFor( cnstrctr )[ index ];
//		} else {
//			return "p" + index;
//		}
		return null;
	}
	private ConstructorDeclaredInJava m_constructor;
	private int m_index;
	private String m_name;
	private TypeDeclaredInJava m_valueType;
	/*package-private*/ ParameterDeclaredInJavaConstructor( ConstructorDeclaredInJava constructor, int index, java.lang.annotation.Annotation[] annotations ) {
		super( annotations );
		m_constructor = constructor;
		m_index = index;
		java.lang.reflect.Constructor<?> cnstrctr = m_constructor.getCnstrctr();
		m_name = getParameterNameFor( cnstrctr, m_index );
		m_valueType = TypeDeclaredInJava.get( cnstrctr.getParameterTypes()[ index ] );
	}
	
	public ConstructorDeclaredInJava getConstructor() {
		return m_constructor;
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
	public boolean equals( Object other ) {
		if( other instanceof ParameterDeclaredInJavaConstructor ) {
			ParameterDeclaredInJavaConstructor otherPDIJC = (ParameterDeclaredInJavaConstructor)other;
			return m_constructor.equals( otherPDIJC.m_constructor ) && m_index == otherPDIJC.m_index && m_name.equals( otherPDIJC.m_name ) && m_valueType.equals( otherPDIJC.m_valueType );
		} else {
			return false;
		}
	}
}
