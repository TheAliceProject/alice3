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

/**
 * @author Dennis Cosgrove
 */
public class ArrayTypeDeclaredInAlice extends AbstractType {
	private static edu.cmu.cs.dennisc.map.MapToMap< TypeDeclaredInAlice, Integer, ArrayTypeDeclaredInAlice > s_map = new edu.cmu.cs.dennisc.map.MapToMap< TypeDeclaredInAlice, Integer, ArrayTypeDeclaredInAlice >();
	public static ArrayTypeDeclaredInAlice get( TypeDeclaredInAlice leafType, int dimensionCount ) {
		ArrayTypeDeclaredInAlice rv = s_map.get( leafType, dimensionCount );
		if( rv != null ) {
			//pass
		} else {
			rv = new ArrayTypeDeclaredInAlice( leafType, dimensionCount );
			s_map.put( leafType, dimensionCount, rv );
		}
		return rv;
	}

	private TypeDeclaredInAlice m_leafType;
	private int m_dimensionCount;
	
	private ArrayTypeDeclaredInAlice( TypeDeclaredInAlice leafType, int dimensionCount ) {
		m_leafType = leafType;
		m_dimensionCount = dimensionCount;
	}
	
	public TypeDeclaredInAlice getLeafType() {
		return m_leafType;
	}
	public int getDimensionCount() {
		return m_dimensionCount;
	}
	
	@Override
	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append( m_leafType.getName() );
		for( int i=0; i<m_dimensionCount; i++ ) {
			sb.append( "[]" );
		}
		return sb.toString();
	}
	@Override
	public boolean isArray() {
		return true;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getComponentType() {
		if( m_dimensionCount == 1 ) {
			return m_leafType;
		} else {
			return ArrayTypeDeclaredInAlice.get( m_leafType, m_dimensionCount-1 );
		}
	}
	@Override
	public boolean isDeclaredInAlice() {
		return true;
	}
	@Override
	public AbstractPackage getPackage() {
		//todo?
		return m_leafType.getPackage();
	}
	@Override
	public AbstractType getSuperType() {
		AbstractType superType = m_leafType.getSuperType();
		if( superType instanceof TypeDeclaredInAlice ) {
			return ArrayTypeDeclaredInAlice.get( ((TypeDeclaredInAlice)superType), m_dimensionCount );
		} else {
			assert superType instanceof TypeDeclaredInJava;
			return TypeDeclaredInJava.get( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getArrayClass( superType.getClass(), m_dimensionCount ) );
		}
		//todo: investigate
	}
	@Override
	public boolean isFollowToSuperClassDesired() {
		//todo?
		return m_leafType.isFollowToSuperClassDesired();
	}

	@Override
	public java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors() {
		//todo
		return new java.util.ArrayList< AbstractConstructor >();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractField > getDeclaredFields() {
		//todo
		return new java.util.ArrayList< AbstractField >();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods() {
		//todo
		return new java.util.ArrayList< AbstractMethod >();
	}
	@Override
	public Access getAccess() {
		return m_leafType.getAccess();
	}
	@Override
	public boolean isInterface() {
		//todo?
		return m_leafType.isInterface();
	}
	@Override
	public boolean isAbstract() {
		//todo?
		return m_leafType.isAbstract();
	}
	@Override
	public boolean isFinal() {
		//todo?
		return m_leafType.isFinal();
	}
	@Override
	public boolean isStatic() {
		//todo?
		return m_leafType.isStatic();
	}
	@Override
	public boolean isStrictFloatingPoint() {
		//todo?
		return m_leafType.isStrictFloatingPoint();
	}
	
	@Override
	public AbstractType getArrayType() {
		return ArrayTypeDeclaredInAlice.get( m_leafType, m_dimensionCount+1 );
	}
}
