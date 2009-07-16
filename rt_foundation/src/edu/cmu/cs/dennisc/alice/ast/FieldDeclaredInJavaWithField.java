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
	private static java.util.Map< FieldReflectionProxy, FieldDeclaredInJavaWithField > s_map = new java.util.HashMap< FieldReflectionProxy, FieldDeclaredInJavaWithField >();
	public static FieldDeclaredInJavaWithField get( FieldReflectionProxy fieldReflectionProxy ) {
		if( fieldReflectionProxy != null ) {
			FieldDeclaredInJavaWithField rv = s_map.get( fieldReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new FieldDeclaredInJavaWithField( fieldReflectionProxy );
				s_map.put( fieldReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static FieldDeclaredInJavaWithField get( java.lang.reflect.Field fld ) {
		return get( new FieldReflectionProxy( fld ) );
	}
	public static FieldDeclaredInJavaWithField get( Class<?> declaringCls, String name ) {
		return get( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getField(  declaringCls, name ) );
	}

	private FieldReflectionProxy fieldReflectionProxy;
	private FieldDeclaredInJavaWithField( FieldReflectionProxy fieldReflectionProxy ) {
		this.fieldReflectionProxy = fieldReflectionProxy;
	}
	
	public FieldReflectionProxy getFieldReflectionProxy() {
		return this.fieldReflectionProxy;
	}
	@Override
	public AbstractType getDeclaringType() {
		return TypeDeclaredInJava.get( this.fieldReflectionProxy.getDeclaringClassReflectionProxy() );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		if( fld != null ) {
			if( fld.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.PropertyFieldTemplate.class ) ) {
				edu.cmu.cs.dennisc.alice.annotations.PropertyFieldTemplate propertyFieldTemplate = fld.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.PropertyFieldTemplate.class );
				return propertyFieldTemplate.visibility();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		return this.fieldReflectionProxy.getName();
	}
	@Override
	public AbstractType getValueType() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		assert fld != null;
		return TypeDeclaredInJava.get( fld.getType() );
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
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		assert fld != null;
		return Access.get( fld.getModifiers() );
	}	
	@Override
	public boolean isStatic() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		assert fld != null;
		return java.lang.reflect.Modifier.isStatic( fld.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		assert fld != null;
		return java.lang.reflect.Modifier.isFinal( fld.getModifiers() );
	}
	@Override
	public boolean isVolatile() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		assert fld != null;
		return java.lang.reflect.Modifier.isVolatile( fld.getModifiers() );
	}
	@Override
	public boolean isTransient() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getFld();
		assert fld != null;
		return java.lang.reflect.Modifier.isTransient( fld.getModifiers() );
	}
	
	@Override
	public boolean isEquivalentTo( Object o ) {
		FieldDeclaredInJavaWithField other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, FieldDeclaredInJavaWithField.class );
		if( other != null ) {
			return this.fieldReflectionProxy.equals( other.fieldReflectionProxy );
		} else {
			return false;
		}
	}
	
}
