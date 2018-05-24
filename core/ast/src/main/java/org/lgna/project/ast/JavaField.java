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

import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.property.StringProperty;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BinaryOperator;

/**
 * @author Dennis Cosgrove
 */
public class JavaField extends AbstractField {
	private static final InitializingIfAbsentMap<FieldReflectionProxy, JavaField> mapReflectionProxyToInstance = Maps.newInitializingIfAbsentHashMap();

	public static JavaField getInstance( FieldReflectionProxy fieldReflectionProxy ) {
		if( fieldReflectionProxy != null ) {
			return mapReflectionProxyToInstance.getInitializingIfAbsent( fieldReflectionProxy, new InitializingIfAbsentMap.Initializer<FieldReflectionProxy, JavaField>() {
				@Override
				public JavaField initialize( FieldReflectionProxy key ) {
					return new JavaField( key );
				}
			} );
		} else {
			return null;
		}
	}

	public static JavaField getInstance( Field fld ) {
		return getInstance( new FieldReflectionProxy( fld ) );
	}

	public static JavaField getInstance( Class<?> declaringCls, String name ) {
		return getInstance( ReflectionUtilities.getField( declaringCls, name ) );
	}

	private JavaField( FieldReflectionProxy fieldReflectionProxy ) {
		this.fieldReflectionProxy = fieldReflectionProxy;
		final boolean IS_CHECK_REIFICATION_DESIRED = false;
		if( IS_CHECK_REIFICATION_DESIRED ) {
			if( this.fieldReflectionProxy.getReification() != null ) {
				//pass
			} else {
				Logger.severe( this.fieldReflectionProxy );
			}
		}
	}

	@Override
	public StringProperty getNamePropertyIfItExists() {
		return null;
	}

	public FieldReflectionProxy getFieldReflectionProxy() {
		return this.fieldReflectionProxy;
	}

	@Override
	public JavaType getDeclaringType() {
		return JavaType.getInstance( this.fieldReflectionProxy.getDeclaringClassReflectionProxy() );
	}

	@Override
	public Visibility getVisibility() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			if( fld.isAnnotationPresent( FieldTemplate.class ) ) {
				FieldTemplate propertyFieldTemplate = fld.getAnnotation( FieldTemplate.class );
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
	public boolean isValid() {
		Field fld = this.fieldReflectionProxy.getReification();
		return fld != null;
	}

	@Override
	public JavaType getValueType() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			return JavaType.getInstance( fld.getType() );
		} else {
			Logger.severe( this.fieldReflectionProxy );
			return JavaType.OBJECT_TYPE;
		}
	}

	@Override
	public AccessLevel getAccessLevel() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			return AccessLevel.getValueFromModifiers( fld.getModifiers() );
		} else {
			Logger.severe( this.fieldReflectionProxy );
			return AccessLevel.PRIVATE;
		}
	}

	@Override
	public boolean isStatic() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			return Modifier.isStatic( fld.getModifiers() );
		} else {
			Logger.severe( this.fieldReflectionProxy );
			return false;
		}
	}

	@Override
	public boolean isFinal() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			return Modifier.isFinal( fld.getModifiers() );
		} else {
			Logger.severe( this.fieldReflectionProxy );
			return false;
		}
	}

	@Override
	public boolean isVolatile() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			return Modifier.isVolatile( fld.getModifiers() );
		} else {
			Logger.severe( this.fieldReflectionProxy );
			return false;
		}
	}

	@Override
	public boolean isTransient() {
		Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			return Modifier.isTransient( fld.getModifiers() );
		} else {
			Logger.severe( this.fieldReflectionProxy );
			return false;
		}
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		JavaField other = ClassUtilities.getInstance( o, JavaField.class );
		if( other != null ) {
			return this.fieldReflectionProxy.equals( other.fieldReflectionProxy );
		} else {
			return false;
		}
	}

	@Override
	public boolean isUserAuthored() {
		return false;
	}

	@Override
	public String formatName(BinaryOperator<String> localizer) {
		String name = isStatic() ? getFieldReflectionProxy().getReification().getName() : getName();
		return localizer.apply(name, name);
	}

	private final FieldReflectionProxy fieldReflectionProxy;
}
