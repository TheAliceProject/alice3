/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class UserType<C extends UserConstructor> extends AbstractType<C, UserMethod, UserField> {
	public UserType() {
	}

	public UserType( AbstractType<?, ?, ?> superType, UserMethod[] methods, UserField[] fields ) {
		this.superType.setValue( superType );
		this.methods.add( methods );
		this.fields.add( fields );
	}

	private boolean isEqualToOrSubTypeOf( UserType<?> candidate ) {
		if( this == candidate ) {
			return true;
		} else {
			AbstractType<?, ?, ?> superType = this.superType.getValue();
			if( superType instanceof UserType<?> ) {
				UserType<?> superUserType = (UserType<?>)superType;
				return superUserType.isEqualToOrSubTypeOf( candidate );
			} else {
				return false;
			}
		}
	}

	@Override
	protected boolean isAssignableFromType( org.lgna.project.ast.AbstractType<?, ?, ?> other ) {
		if( other.isArray() ) {
			return false;
		} else {
			if( other instanceof UserType<?> ) {
				UserType<?> otherUserType = (UserType<?>)other;
				return otherUserType.isEqualToOrSubTypeOf( this );
			} else {
				return false;
			}
		}
	}

	@Override
	public AbstractType<?, ?, ?> getKeywordFactoryType() {
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
	public final AbstractType<?, ?, ?> getSuperType() {
		return superType.getValue();
	}

	@Override
	public AbstractType<?, ?, ?>[] getInterfaces() {
		return new AbstractType<?, ?, ?>[] {};
	}

	@Override
	public final java.util.List<UserMethod> getDeclaredMethods() {
		return methods.getValue();
	}

	@Override
	public final java.util.List<UserField> getDeclaredFields() {
		return fields.getValue();
	}

	@Override
	public boolean isPrimitive() {
		return false;
	}

	@Override
	public final boolean isInterface() {
		return false;
	}

	@Override
	public final boolean isUserAuthored() {
		return true;
	}

	@Override
	public final boolean isArray() {
		return false;
	}

	@Override
	public boolean isEnum() {
		return false;
	}

	@Override
	public final AbstractType<?, ?, ?> getComponentType() {
		return null;
	}

	@Override
	public final AbstractType<?, ?, ?> getArrayType() {
		return UserArrayType.getInstance( this, 1 );
	}

	public final DeclarationProperty<AbstractType<?, ?, ?>> superType = new DeclarationProperty<AbstractType<?, ?, ?>>( this ) {
		@Override
		public boolean isReference() {
			return true;
		}

		@Override
		public void setValue( AbstractType<?, ?, ?> value ) {
			assert ( value == null ) || ( value.isArray() == false );
			super.setValue( value );
		}
	};
	public final NodeListProperty<UserMethod> methods = new NodeListProperty<UserMethod>( this );
	public final NodeListProperty<UserField> fields = new NodeListProperty<UserField>( this );
}
