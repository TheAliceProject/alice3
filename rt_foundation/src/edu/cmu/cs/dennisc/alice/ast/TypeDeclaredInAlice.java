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
public class TypeDeclaredInAlice extends AbstractType {
	private class Adapter<E extends AbstractMember> implements edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > {
		private void handleAdd( E member ) {
			assert member instanceof MemberDeclaredInAlice;
			assert member.getDeclaringType() == null;
			MemberDeclaredInAlice memberDeclaredInAlice = (MemberDeclaredInAlice)member;
			memberDeclaredInAlice.setDeclaringType( TypeDeclaredInAlice.this );
		}
		private void handleRemove( E member ) {
			assert member instanceof MemberDeclaredInAlice;
			assert member.getDeclaringType() != null;
			MemberDeclaredInAlice memberDeclaredInAlice = (MemberDeclaredInAlice)member;
			memberDeclaredInAlice.setDeclaringType( null );
		}

		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
			for( E element : e.getElements() ) {
				handleAdd( element );
			}
			//todo
			//			for( E element : e.getCollection() ) {
			//				handleAdd( element );
			//			}
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< E > e ) {
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
			for( E element : e.getTypedSource() ) {
				handleRemove( element );
			}
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< E > e ) {
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
			for( E element : e.getElements() ) {
				handleRemove( element );
			}
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< E > e ) {
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
			int startIndex = e.getStartIndex();
			java.util.Collection< ? extends E > elements = e.getElements();
			for( int i = 0; i < elements.size(); i++ ) {
				handleRemove( e.getTypedSource().get( startIndex + i ) );
			}
			for( E element : e.getElements() ) {
				handleAdd( element );
			}
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< E > e ) {
		}
	}

	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, null );
	public DeclarationProperty< PackageDeclaredInAlice > _package = new DeclarationProperty< PackageDeclaredInAlice >( this );
	public DeclarationProperty< AbstractType > superType = new DeclarationProperty< AbstractType >( this ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, AbstractType value) {
			assert value.isArray() == false;
			super.setValue( owner, value );
		}
	};
	public NodeListProperty< ConstructorDeclaredInAlice > constructors = new NodeListProperty< ConstructorDeclaredInAlice >( this );
	public NodeListProperty< MethodDeclaredInAlice > methods = new NodeListProperty< MethodDeclaredInAlice >( this );
	public NodeListProperty< FieldDeclaredInAlice > fields = new NodeListProperty< FieldDeclaredInAlice >( this );
	public edu.cmu.cs.dennisc.property.EnumProperty< Access > access = new edu.cmu.cs.dennisc.property.EnumProperty< Access >( this, Access.PUBLIC );
	//inner classes only
	//public edu.cmu.cs.dennisc.property.BooleanProperty isStatic = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );
	public edu.cmu.cs.dennisc.property.EnumProperty< TypeModifierFinalAbstractOrNeither > finalAbstractOrNeither = new edu.cmu.cs.dennisc.property.EnumProperty< TypeModifierFinalAbstractOrNeither >( this, TypeModifierFinalAbstractOrNeither.NEITHER );
	public edu.cmu.cs.dennisc.property.BooleanProperty isStrictFloatingPoint = new edu.cmu.cs.dennisc.property.BooleanProperty( this, Boolean.FALSE );

	public TypeDeclaredInAlice() {
		this.constructors.addListPropertyListener( new Adapter< ConstructorDeclaredInAlice >() );
		this.methods.addListPropertyListener( new Adapter< MethodDeclaredInAlice >() );
		this.fields.addListPropertyListener( new Adapter< FieldDeclaredInAlice >() );
	}
	public TypeDeclaredInAlice( String name, PackageDeclaredInAlice _package, AbstractType superType, ConstructorDeclaredInAlice[] constructors, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		this();
		this.name.setValue( name );
		this._package.setValue( _package );
		this.superType.setValue( superType );
		this.constructors.add( constructors );
		this.methods.add( methods );
		this.fields.add( fields );
	}
	public TypeDeclaredInAlice( String name, PackageDeclaredInAlice _package, Class< ? > superCls, ConstructorDeclaredInAlice[] constructors, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		this( name, _package, TypeDeclaredInJava.get( superCls ), constructors, methods, fields );
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
	public boolean isFollowToSuperClassDesired() {
		return true;
	}
	@Override
	public boolean isConsumptionBySubClassDesired() {
		return false;
	}
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
	public AbstractType getSuperType() {
		return superType.getValue();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors() {
		return constructors.getValue();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods() {
		return methods.getValue();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractField > getDeclaredFields() {
		return fields.getValue();
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
	public boolean isInterface() {
		return false;
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
	@Override
	public boolean isDeclaredInAlice() {
		return true;
	}

	@Override
	public boolean isArray() {
		return false;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getComponentType() {
		return null;
	}
	@Override
	public AbstractType getArrayType() {
		return ArrayTypeDeclaredInAlice.get( this, 1 );
	}
	
}
