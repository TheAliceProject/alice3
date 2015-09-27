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
public class UserArrayType extends AbstractType {
	private static edu.cmu.cs.dennisc.map.MapToMap<UserType<?>, Integer, UserArrayType> s_map = new edu.cmu.cs.dennisc.map.MapToMap<UserType<?>, Integer, UserArrayType>();

	public static UserArrayType getInstance( UserType<?> leafType, int dimensionCount ) {
		UserArrayType rv = s_map.get( leafType, dimensionCount );
		if( rv != null ) {
			//pass
		} else {
			rv = new UserArrayType( leafType, dimensionCount );
			s_map.put( leafType, dimensionCount, rv );
		}
		return rv;
	}

	private UserArrayType( UserType<?> leafType, int dimensionCount ) {
		this.leafType = leafType;
		this.dimensionCount = dimensionCount;
	}

	public UserType<?> getLeafType() {
		return this.leafType;
	}

	public int getDimensionCount() {
		return this.dimensionCount;
	}

	@Override
	protected boolean isAssignableFromType( org.lgna.project.ast.AbstractType other ) {
		if( other.isArray() ) {
			return this.getComponentType().isAssignableFrom( other.getComponentType() );
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append( this.leafType.getName() );
		for( int i = 0; i < this.dimensionCount; i++ ) {
			sb.append( "[]" );
		}
		return sb.toString();
	}

	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		//todo?
		return null;
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public AbstractType<?, ?, ?> getComponentType() {
		if( this.dimensionCount == 1 ) {
			return this.leafType;
		} else {
			return UserArrayType.getInstance( this.leafType, this.dimensionCount - 1 );
		}
	}

	@Override
	public boolean isUserAuthored() {
		return true;
	}

	@Override
	public AbstractPackage getPackage() {
		//todo?
		return this.leafType.getPackage();
	}

	@Override
	public AbstractType<?, ?, ?> getSuperType() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "the super type of a java array is Object" );
		AbstractType<?, ?, ?> leafSuperType = this.leafType.getSuperType();
		if( leafSuperType instanceof UserType<?> ) {
			return UserArrayType.getInstance( ( (UserType<?>)leafSuperType ), this.dimensionCount );
		} else {
			assert leafSuperType instanceof JavaType;
			Class<?> leafSuperCls = ( (JavaType)leafSuperType ).getClassReflectionProxy().getReification();
			Class<?> superCls = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getArrayClass( leafSuperCls, this.dimensionCount );
			return JavaType.getInstance( superCls );
		}
	}

	@Override
	public AbstractType<?, ?, ?>[] getInterfaces() {
		return new AbstractType<?, ?, ?>[] {};
	}

	@Override
	public AbstractType<?, ?, ?> getKeywordFactoryType() {
		return null;
	}

	@Override
	public boolean isFollowToSuperClassDesired() {
		//todo?
		return this.leafType.isFollowToSuperClassDesired();
	}

	@Override
	public boolean isConsumptionBySubClassDesired() {
		//todo?
		return this.leafType.isConsumptionBySubClassDesired();
	}

	@Override
	public java.util.List<AbstractConstructor> getDeclaredConstructors() {
		return java.util.Collections.emptyList();
	}

	@Override
	public java.util.List<AbstractField> getDeclaredFields() {
		return java.util.Collections.emptyList();
	}

	@Override
	public java.util.List<AbstractMethod> getDeclaredMethods() {
		return java.util.Collections.emptyList();
	}

	@Override
	public AccessLevel getAccessLevel() {
		return this.leafType.getAccessLevel();
	}

	@Override
	public boolean isPrimitive() {
		return false;
	}

	@Override
	public boolean isInterface() {
		//todo?
		return this.leafType.isInterface();
	}

	@Override
	public boolean isAbstract() {
		//todo?
		return this.leafType.isAbstract();
	}

	@Override
	public boolean isFinal() {
		//todo?
		return this.leafType.isFinal();
	}

	@Override
	public boolean isStatic() {
		//todo?
		return this.leafType.isStatic();
	}

	@Override
	public boolean isStrictFloatingPoint() {
		//todo?
		return this.leafType.isStrictFloatingPoint();
	}

	@Override
	public boolean isEnum() {
		return false;
	}

	@Override
	public AbstractType getArrayType() {
		return UserArrayType.getInstance( this.leafType, this.dimensionCount + 1 );
	}

	private final UserType<?> leafType;
	private final int dimensionCount;
}
