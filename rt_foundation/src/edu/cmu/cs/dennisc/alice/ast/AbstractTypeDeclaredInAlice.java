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
public abstract class AbstractTypeDeclaredInAlice extends AbstractType {
	protected class Adapter<E extends AbstractMember> implements edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > {
		private void handleAdd( E member ) {
			assert member instanceof MemberDeclaredInAlice;
			assert member.getDeclaringType() == null;
			MemberDeclaredInAlice memberDeclaredInAlice = (MemberDeclaredInAlice)member;
			memberDeclaredInAlice.setDeclaringType( AbstractTypeDeclaredInAlice.this );
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

	public DeclarationProperty< AbstractType > superType = new DeclarationProperty< AbstractType >( this ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, AbstractType value) {
			assert value.isArray() == false;
			super.setValue( owner, value );
		}
	};
	public NodeListProperty< MethodDeclaredInAlice > methods = new NodeListProperty< MethodDeclaredInAlice >( this );
	public NodeListProperty< FieldDeclaredInAlice > fields = new NodeListProperty< FieldDeclaredInAlice >( this );
	public AbstractTypeDeclaredInAlice() {
		this.addListenersForMethodsAndFields();
	}
	public AbstractTypeDeclaredInAlice( AbstractType superType, MethodDeclaredInAlice[] methods, FieldDeclaredInAlice[] fields ) {
		this.addListenersForMethodsAndFields();
		this.superType.setValue( superType );
		this.methods.add( methods );
		this.fields.add( fields );
	}
	private void addListenersForMethodsAndFields() {
		this.methods.addListPropertyListener( new Adapter< MethodDeclaredInAlice >() );
		this.fields.addListPropertyListener( new Adapter< FieldDeclaredInAlice >() );
	}
	
	@Override
	public final boolean isFollowToSuperClassDesired() {
		return true;
	}
	@Override
	public final boolean isConsumptionBySubClassDesired() {
		return false;
	}
	@Override
	public final AbstractType getSuperType() {
		return superType.getValue();
	}
	@Override
	public final java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods() {
		return methods.getValue();
	}
	@Override
	public final java.util.ArrayList< ? extends AbstractField > getDeclaredFields() {
		return fields.getValue();
	}
	
	@Override
	public final boolean isInterface() {
		return false;
	}
	@Override
	public final boolean isDeclaredInAlice() {
		return true;
	}

	@Override
	public final boolean isArray() {
		return false;
	}
	@Override
	public final edu.cmu.cs.dennisc.alice.ast.AbstractType getComponentType() {
		return null;
	}
	@Override
	public final AbstractType getArrayType() {
		return ArrayTypeDeclaredInAlice.get( this, 1 );
	}
}
