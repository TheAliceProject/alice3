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
public class FieldDeclaredInJavaWithGetterAndSetter extends FieldDeclaredInJava {
	private static java.util.Map< MethodReflectionProxy, FieldDeclaredInJavaWithGetterAndSetter > s_map = new java.util.HashMap< MethodReflectionProxy, FieldDeclaredInJavaWithGetterAndSetter >();

	private MethodReflectionProxy getterReflectionProxy;
	private MethodReflectionProxy setterReflectionProxy;
	private java.util.ArrayList< JavaMethodParameter > parameters;
	private java.lang.annotation.Annotation[] parameterAnnotations0;

	public static FieldDeclaredInJavaWithGetterAndSetter get( MethodReflectionProxy getterReflectionProxy, MethodReflectionProxy setterReflectionProxy ) {
		if( setterReflectionProxy != null ) {
			FieldDeclaredInJavaWithGetterAndSetter rv = s_map.get( setterReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new FieldDeclaredInJavaWithGetterAndSetter( getterReflectionProxy, setterReflectionProxy );
				s_map.put( setterReflectionProxy, rv );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static FieldDeclaredInJavaWithGetterAndSetter get( java.lang.reflect.Method getter, java.lang.reflect.Method setter ) {
		return get( new MethodReflectionProxy( getter ), new MethodReflectionProxy( setter ) );
	}

	private FieldDeclaredInJavaWithGetterAndSetter( MethodReflectionProxy getterReflectionProxy, MethodReflectionProxy setterReflectionProxy ) {
		this.getterReflectionProxy = getterReflectionProxy;
		this.setterReflectionProxy = setterReflectionProxy;

		java.lang.annotation.Annotation[][] parameterAnnotations = this.setterReflectionProxy.getParameterAnnotations();
		this.parameterAnnotations0 = parameterAnnotations[ 0 ];
	}

	public MethodReflectionProxy getGetterReflectionProxy() {
		return this.getterReflectionProxy;
	}
	public MethodReflectionProxy getSetterReflectionProxy() {
		return this.setterReflectionProxy;
	}
	
	//	private java.lang.reflect.Method m_gttr;
	//	private java.lang.reflect.Method m_sttr;
	//	private java.lang.annotation.Annotation[] m_sttrParameterAnnotations;
	//
	//	private FieldDeclaredInJavaWithGetterAndSetter( java.lang.reflect.Method gttr, java.lang.reflect.Method sttr, java.lang.annotation.Annotation[] sttrParameterAnnotations ) {
	//		m_gttr = gttr;
	//		m_sttr = sttr;
	//		m_sttrParameterAnnotations = sttrParameterAnnotations;
	//	}
	//	//todo: reduce visibility?
	//	public java.lang.reflect.Method getGttr() {
	//		return m_gttr;
	//	}
	//	//todo: reduce visibility?
	//	public java.lang.reflect.Method getSttr() {
	//		return m_sttr;
	//	}

	@Override
	public JavaType getDeclaringType() {
		return JavaType.getInstance( this.setterReflectionProxy.getDeclaringClassReflectionProxy() );
	}

	@Override
	public org.lgna.project.annotations.Visibility getVisibility() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		if( gttr != null ) {
			if( gttr.isAnnotationPresent( org.lgna.project.annotations.PropertyGetterTemplate.class ) ) {
				org.lgna.project.annotations.PropertyGetterTemplate propertyFieldTemplate = gttr.getAnnotation( org.lgna.project.annotations.PropertyGetterTemplate.class );
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
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		if( gttr != null ) {
			return edu.cmu.cs.dennisc.property.PropertyUtilities.getPropertyNameForGetter( gttr );
		} else {
			final String NAME_FOR_UNKNOWN = "UNKNOWN";
			String name = this.getterReflectionProxy.getName();
			if( name != null ) {
				if( name.startsWith( "get" ) ) {
					return name.substring( 3 );
				} else {
					if( name.startsWith( "is" ) ) {
						return "I" + name.substring( 1 );
					} else {
						return NAME_FOR_UNKNOWN;
					}
				}
			} else {
				return NAME_FOR_UNKNOWN;
			}
		}
	}
	@Override
	public JavaType getValueType() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return JavaType.getInstance( gttr.getReturnType() );
	}
	@Override
	public JavaType getDesiredValueType() {
		if( this.parameterAnnotations0 != null ) {
			for( java.lang.annotation.Annotation annotation : parameterAnnotations0 ) {
					if( annotation instanceof org.lgna.project.annotations.ParameterTemplate ) {
					org.lgna.project.annotations.ParameterTemplate parameterTemplate = (org.lgna.project.annotations.ParameterTemplate)annotation;
					return JavaType.getInstance( parameterTemplate.preferredArgumentClass() );
				}
			}
		}
		return getValueType();
	}

	//todo
	@Override
	public AbstractMember getNextLongerInChain() {
		return null;
	}
	//todo
	@Override
	public AbstractMember getNextShorterInChain() {
		return null;
	}

	@Override
	public Access getAccess() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return Access.get( gttr.getModifiers() );
	}
	@Override
	public boolean isStatic() {
		java.lang.reflect.Method gttr = this.getterReflectionProxy.getReification();
		assert gttr != null;
		return java.lang.reflect.Modifier.isStatic( gttr.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		return false;
	}
	@Override
	public boolean isVolatile() {
		return false;
	}
	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public boolean isEquivalentTo( Object o ) {
		FieldDeclaredInJavaWithGetterAndSetter other = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( o, FieldDeclaredInJavaWithGetterAndSetter.class );
		if( other != null ) {
			return this.getterReflectionProxy.equals( other.getterReflectionProxy ) && this.setterReflectionProxy.equals( other.setterReflectionProxy );
		} else {
			return false;
		}
	}
}
