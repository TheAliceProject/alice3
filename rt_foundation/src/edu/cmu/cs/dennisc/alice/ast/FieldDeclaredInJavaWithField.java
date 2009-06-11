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
public class FieldDeclaredInJavaWithField extends FieldDeclaredInJava {
	private java.lang.reflect.Field m_fld;
	/*package-private*/ FieldDeclaredInJavaWithField( java.lang.reflect.Field fld ) {
		m_fld = fld;
	}
	//todo: reduce visibility?
	public java.lang.reflect.Field getFld() {
		return m_fld;
	}
	
	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( m_fld.getDeclaringClass() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		if( m_fld.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.PropertyFieldTemplate.class ) ) {
			edu.cmu.cs.dennisc.alice.annotations.PropertyFieldTemplate propertyFieldTemplate = m_fld.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.PropertyFieldTemplate.class );
			return propertyFieldTemplate.visibility();
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		return m_fld.getName();
	}
	@Override
	public AbstractType getValueType() {
		return TypeDeclaredInJava.get( m_fld.getType() );
	}
	@Override
	public AbstractType getDesiredValueType() {
		return getValueType();
	}
	
	@Override
	public AbstractMember getNextLongerInChain() {
		return null;
	}
	@Override
	public AbstractMember getNextShorterInChain() {
		return null;
	}
	@Override
	public Access getAccess() {
		return Access.get( m_fld.getModifiers() );
	}	
	@Override
	public boolean isStatic() {
		return java.lang.reflect.Modifier.isStatic( m_fld.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		return java.lang.reflect.Modifier.isFinal( m_fld.getModifiers() );
	}
	@Override
	public boolean isVolatile() {
		return java.lang.reflect.Modifier.isVolatile( m_fld.getModifiers() );
	}
	@Override
	public boolean isTransient() {
		return java.lang.reflect.Modifier.isTransient( m_fld.getModifiers() );
	}
	
	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof FieldDeclaredInJavaWithField ) {
			return m_fld.equals( ((FieldDeclaredInJavaWithField)other).m_fld );
		} else {
			return false;
		}
	}
	
}
