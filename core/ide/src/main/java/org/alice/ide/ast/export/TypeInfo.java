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
package org.alice.ide.ast.export;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.UserConstructor;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class TypeInfo extends DeclarationInfo<UserType<?>> {
	private final Map<UserConstructor, ConstructorInfo> constructorInfoMap;
	private final Map<UserMethod, MethodInfo> methodInfoMap;
	private final Map<UserField, FieldInfo> fieldInfoMap;

	public TypeInfo( ProjectInfo projectInfo, UserType<?> type ) {
		super( projectInfo, type );
		Map<UserConstructor, ConstructorInfo> mapC = Maps.newHashMap();
		for( UserConstructor constructor : type.getDeclaredConstructors() ) {
			mapC.put( constructor, new ConstructorInfo( projectInfo, constructor ) );
		}
		this.constructorInfoMap = Collections.unmodifiableMap( mapC );
		Map<UserMethod, MethodInfo> mapM = Maps.newHashMap();
		for( UserMethod method : type.methods ) {
			mapM.put( method, new MethodInfo( projectInfo, method ) );
		}
		this.methodInfoMap = Collections.unmodifiableMap( mapM );
		Map<UserField, FieldInfo> mapF = Maps.newHashMap();
		for( UserField field : type.fields ) {
			mapF.put( field, new FieldInfo( projectInfo, field ) );
		}
		this.fieldInfoMap = Collections.unmodifiableMap( mapF );
	}

	public TypeInfo getSuperTypeInfo() {
		AbstractType<?, ?, ?> superType = this.getDeclaration().getSuperType();
		if( superType instanceof UserType<?> ) {
			return this.getProjectInfo().getInfoForType( (UserType<?>)superType );
		} else {
			return null;
		}
	}

	public Collection<ConstructorInfo> getConstructorInfos() {
		return this.constructorInfoMap.values();
	}

	public Collection<MethodInfo> getMethodInfos() {
		return this.methodInfoMap.values();
	}

	public Collection<FieldInfo> getFieldInfos() {
		return this.fieldInfoMap.values();
	}

	public ConstructorInfo getInfoForConstructor( UserConstructor constructor ) {
		return this.constructorInfoMap.get( constructor );
	}

	public MethodInfo getInfoForMethod( UserMethod method ) {
		return this.methodInfoMap.get( method );
	}

	public FieldInfo getInfoForField( UserField field ) {
		return this.fieldInfoMap.get( field );
	}

	/* package-private */void updateDependencies() {
		for( ConstructorInfo info : this.constructorInfoMap.values() ) {
			info.updateDependencies();
		}
		for( MethodInfo info : this.methodInfoMap.values() ) {
			info.updateDependencies();
		}
		for( FieldInfo info : this.fieldInfoMap.values() ) {
			info.updateDependencies();
		}
	}

	@Override
	public void appendDesired( List<DeclarationInfo<?>> desired ) {
		super.appendDesired( desired );
		for( ConstructorInfo info : this.constructorInfoMap.values() ) {
			info.appendDesired( desired );
		}
		for( MethodInfo info : this.methodInfoMap.values() ) {
			info.appendDesired( desired );
		}
		for( FieldInfo info : this.fieldInfoMap.values() ) {
			info.appendDesired( desired );
		}
	}

	@Override
	public void resetRequired() {
		super.resetRequired();
		for( ConstructorInfo info : this.constructorInfoMap.values() ) {
			info.resetRequired();
		}
		for( MethodInfo info : this.methodInfoMap.values() ) {
			info.resetRequired();
		}
		for( FieldInfo info : this.fieldInfoMap.values() ) {
			info.resetRequired();
		}
	}

	@Override
	public void updateSwing() {
		super.updateSwing();
		for( ConstructorInfo info : this.constructorInfoMap.values() ) {
			info.updateSwing();
		}
		for( MethodInfo info : this.methodInfoMap.values() ) {
			info.updateSwing();
		}
		for( FieldInfo info : this.fieldInfoMap.values() ) {
			info.updateSwing();
		}
	}
}
