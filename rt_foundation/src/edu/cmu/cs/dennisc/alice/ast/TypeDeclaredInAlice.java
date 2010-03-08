/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
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
