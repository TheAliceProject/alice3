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
public class MethodDeclaredInAlice extends AbstractMethod implements CodeDeclaredInAlice {
	public edu.cmu.cs.dennisc.property.EnumProperty< Access > access = new edu.cmu.cs.dennisc.property.EnumProperty< Access >( this, Access.PUBLIC );
	public edu.cmu.cs.dennisc.property.BooleanProperty isStatic = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isAbstract = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isFinal = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	//public edu.cmu.cs.dennisc.property.BooleanProperty isNative = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isSynchronized = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isStrictFloatingPoint = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public DeclarationProperty< AbstractType > returnType = new DeclarationProperty< AbstractType >( this );
	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
	public NodeListProperty< ParameterDeclaredInAlice > parameters = new NodeListProperty< ParameterDeclaredInAlice >( this );
	public NodeProperty< BlockStatement > body = new NodeProperty< BlockStatement >( this );
	public edu.cmu.cs.dennisc.property.BooleanProperty isSignatureLocked = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.BooleanProperty isDeletionAllowed = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.TRUE );

	private AbstractType m_declaringType;
	private edu.cmu.cs.dennisc.alice.annotations.Visibility m_visibility = edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME; 

	public MethodDeclaredInAlice() {
	}
	public MethodDeclaredInAlice( String name, AbstractType returnType, ParameterDeclaredInAlice[] parameters, BlockStatement body ) {
		this.name.setValue( name );
		this.returnType.setValue( returnType );
		this.parameters.add( parameters );
		this.body.setValue( body );
	}
	public MethodDeclaredInAlice( String name, Class<?> returnCls, ParameterDeclaredInAlice[] parameters, BlockStatement body ) {
		this( name, TypeDeclaredInJava.get( returnCls ), parameters, body );
	}
		

	public NodeProperty< BlockStatement > getBodyProperty() {
		return this.body;
	}
	public NodeListProperty< ParameterDeclaredInAlice > getParamtersProperty() {
		return this.parameters;
	}
	
	@Override
	public String getName() {
		return name.getValue();
	}
	@Override
	public AbstractType getReturnType() {
		return returnType.getValue();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractParameter > getParameters() {
		return parameters.getValue();
	}
	@Override
	public AbstractType getDeclaringType() {
		return m_declaringType;
	}
	public void setDeclaringType( AbstractType declaringType ) {
		m_declaringType = declaringType;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.annotations.Visibility getVisibility() {
		return m_visibility;
	}
	public void setVisibility( edu.cmu.cs.dennisc.alice.annotations.Visibility visibility ) {
		m_visibility = visibility;
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
		return this.access.getValue();
	}

	@Override
	public boolean isStatic() {
		return this.isStatic.getValue();
	}
	@Override
	public boolean isAbstract() {
		return this.isAbstract.getValue();
	}
	@Override
	public boolean isFinal() {
		return this.isFinal.getValue();
	}
	@Override
	public boolean isNative() {
		return false;
		//return this.isNative.getValue();
	}
	@Override
	public boolean isSynchronized() {
		return this.isSynchronized.getValue();
	}
	@Override
	public boolean isStrictFloatingPoint() {
		return this.isStrictFloatingPoint.getValue();
	}
	
//	@Override
//	public boolean isOverride() {
//		//todo: this will need to be udpated when you can inherit from other TypesDeclaredInAlice
//		TypeDeclaredInJava typeDeclaredInJava = this.getDeclaringType().getFirstTypeEncounteredDeclaredInJava();
//		Class<?> clsDeclaredInJava = typeDeclaredInJava.getCls();
//		Class<?>[] parameterClses = new Class< ? >[ this.parameters.size() ];
//		int i = 0;
//		for( AbstractParameter parameter : this.parameters ) {
//			if( parameter instanceof ParameterDeclaredInJava ) {
//				ParameterDeclaredInJava parameterDeclaredInJava = (ParameterDeclaredInJava)parameter;
//				parameterClses[ i ] = parameterDeclaredInJava.getValueTypeDeclaredInJava().getCls();
//			} else {
//				return false;
//			}
//			i++;
//		}
//		java.lang.reflect.Method mthd = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( clsDeclaredInJava, this.getName(), parameterClses );
//		return mthd != null;
//	}
}
