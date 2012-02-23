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
public class JavaField extends AbstractField {
	private static java.util.Map< FieldReflectionProxy, JavaField > s_map = new java.util.HashMap< FieldReflectionProxy, JavaField >();
	public static JavaField getInstance( FieldReflectionProxy fieldReflectionProxy ) {
		if( fieldReflectionProxy != null ) {
			JavaField rv = s_map.get( fieldReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new JavaField( fieldReflectionProxy );
				s_map.put( fieldReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static JavaField getInstance( java.lang.reflect.Field fld ) {
		return getInstance( new FieldReflectionProxy( fld ) );
	}
	public static JavaField getInstance( Class<?> declaringCls, String name ) {
		return getInstance( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getField(  declaringCls, name ) );
	}

	private FieldReflectionProxy fieldReflectionProxy;
	private JavaField( FieldReflectionProxy fieldReflectionProxy ) {
		this.fieldReflectionProxy = fieldReflectionProxy;
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
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
	public org.lgna.project.annotations.Visibility getVisibility() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		if( fld != null ) {
			if( fld.isAnnotationPresent( org.lgna.project.annotations.FieldTemplate.class ) ) {
				org.lgna.project.annotations.FieldTemplate propertyFieldTemplate = fld.getAnnotation( org.lgna.project.annotations.FieldTemplate.class );
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
	public JavaType getValueType() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		assert fld != null;
		return JavaType.getInstance( fld.getType() );
	}
	
	@Override
	public AccessLevel getAccessLevel() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		assert fld != null;
		return AccessLevel.get( fld.getModifiers() );
	}	
	@Override
	public boolean isStatic() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		assert fld != null;
		return java.lang.reflect.Modifier.isStatic( fld.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		assert fld != null;
		return java.lang.reflect.Modifier.isFinal( fld.getModifiers() );
	}
	@Override
	public boolean isVolatile() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		assert fld != null;
		return java.lang.reflect.Modifier.isVolatile( fld.getModifiers() );
	}
	@Override
	public boolean isTransient() {
		java.lang.reflect.Field fld = this.fieldReflectionProxy.getReification();
		assert fld != null;
		return java.lang.reflect.Modifier.isTransient( fld.getModifiers() );
	}
	
	@Override
	public boolean isEquivalentTo( Object o ) {
		JavaField other = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( o, JavaField.class );
		if( other != null ) {
			return this.fieldReflectionProxy.equals( other.fieldReflectionProxy );
		} else {
			return false;
		}
	}
	
}
