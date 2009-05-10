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
public class TypeDeclaredInAlice extends AbstractTypeDeclaredInAlice {
	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
	public DeclarationProperty< PackageDeclaredInAlice > _package = new DeclarationProperty< PackageDeclaredInAlice >( this );
	public NodeListProperty< ConstructorDeclaredInAlice > constructors = new NodeListProperty< ConstructorDeclaredInAlice >( this );
	public edu.cmu.cs.dennisc.property.EnumProperty< Access > access = new edu.cmu.cs.dennisc.property.EnumProperty< Access >( this, Access.PUBLIC );
	public edu.cmu.cs.dennisc.property.EnumProperty< TypeModifierFinalAbstractOrNeither > finalAbstractOrNeither = new edu.cmu.cs.dennisc.property.EnumProperty< TypeModifierFinalAbstractOrNeither >( this, TypeModifierFinalAbstractOrNeither.NEITHER );
	public edu.cmu.cs.dennisc.property.BooleanProperty isStrictFloatingPoint = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );

	public TypeDeclaredInAlice() {
		this.addListenerForConstructors();
	}
	public TypeDeclaredInAlice( String name, PackageDeclaredInAlice _package, AbstractType superType, ConstructorDeclaredInAlice[] constructors, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		super( superType, methods, fields );
		this.addListenerForConstructors();
		this.name.setValue( name );
		this._package.setValue( _package );
		this.constructors.add( constructors );
	}
	public TypeDeclaredInAlice( String name, PackageDeclaredInAlice _package, Class< ? > superCls, ConstructorDeclaredInAlice[] constructors, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		this( name, _package, TypeDeclaredInJava.get( superCls ), constructors, methods, fields );
	}
	
	private void addListenerForConstructors() {
		this.constructors.addListPropertyListener( new Adapter< ConstructorDeclaredInAlice >() );
	}
	

//	private void postDecode( MemberDeclaredInAlice member ) {
//		if( member.getDeclaringType() == this ) {
//			//pass
//		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: fixing declaring type for:", member );
//			member.setDeclaringType( this );
//		}
//	}
//	@Override
//	protected void postDecode() {
//		super.postDecode();
//		for( ConstructorDeclaredInAlice constructor : this.constructors ) {
//			postDecode( constructor );
//		}
//		for( MethodDeclaredInAlice method : this.methods ) {
//			postDecode( method );
//		}
//		for( FieldDeclaredInAlice field : this.fields ) {
//			postDecode( field );
//		}
//	}
	
	@Override
	public String getName() {
		return name.getValue();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return this.name;
	}
	@Override
	public AbstractPackage getPackage() {
		return _package.getValue();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors() {
		return constructors.getValue();
	}
	
	@Override
	public Access getAccess() {
		return this.access.getValue();
	}

	@Override
	public boolean isStatic() {
		return false;
		//return this.isStatic.getValue();
	}
	@Override
	public boolean isAbstract() {
		return this.finalAbstractOrNeither.getValue() == TypeModifierFinalAbstractOrNeither.ABSTRACT;
	}
	@Override
	public boolean isFinal() {
		return this.finalAbstractOrNeither.getValue() == TypeModifierFinalAbstractOrNeither.FINAL;
	}
	@Override
	public boolean isStrictFloatingPoint() {
		return this.isStrictFloatingPoint.getValue();
	}
}
