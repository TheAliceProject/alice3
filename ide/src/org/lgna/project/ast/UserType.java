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

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class UserType<C extends AbstractConstructor> extends AbstractType<C, UserMethod, UserField> {
	protected class Adapter<E extends AbstractMember> implements edu.cmu.cs.dennisc.property.event.ListPropertyListener< E > {
		private void handleAdd( E member ) {
			assert member instanceof UserMember;
			if (member.getDeclaringType() != null) {
				System.err.println("NOTE: Declaring type of "+member+" is non-null before being added to "+this+". This was probably done to initialize the member before triggering member-added listeners.");
			}
			UserMember memberDeclaredInAlice = (UserMember)member;
			memberDeclaredInAlice.setDeclaringType( UserType.this );
		}
		private void handleRemove( E member ) {
			assert member instanceof UserMember;
			assert member.getDeclaringType() != null;
			UserMember memberDeclaredInAlice = (UserMember)member;
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

	public DeclarationProperty< AbstractType<?,?,?> > superType = new DeclarationProperty< AbstractType<?,?,?> >( this ) {
		@Override
		public void setValue(edu.cmu.cs.dennisc.property.PropertyOwner owner, AbstractType<?,?,?> value) {
			assert value == null || value.isArray() == false;
			super.setValue( owner, value );
		}
	};
	public NodeListProperty< UserMethod > methods = new NodeListProperty< UserMethod >( this );
	public NodeListProperty< UserField > fields = new NodeListProperty< UserField >( this );
	public UserType() {
		this.addListenersForMethodsAndFields();
	}
	public UserType( AbstractType<?,?,?> superType, UserMethod[] methods, UserField[] fields ) {
		this.addListenersForMethodsAndFields();
		this.superType.setValue( superType );
		this.methods.add( methods );
		this.fields.add( fields );
	}
	private void addListenersForMethodsAndFields() {
		this.methods.addListPropertyListener( new Adapter< UserMethod >() );
		this.fields.addListPropertyListener( new Adapter< UserField >() );
	}
	
	@Override
	public AbstractType< ?, ?, ? > getKeywordFactoryType() {
		return null;
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
	public final AbstractType<?,?,?> getSuperType() {
		return superType.getValue();
	}
	@Override
	public final java.util.ArrayList< UserMethod > getDeclaredMethods() {
		return methods.getValue();
	}
	@Override
	public final java.util.ArrayList< UserField > getDeclaredFields() {
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
	public final AbstractType<?,?,?> getComponentType() {
		return null;
	}
	@Override
	public final AbstractType<?,?,?> getArrayType() {
		return UserArrayType.getInstance( this, 1 );
	}
}
