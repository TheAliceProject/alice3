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
package edu.cmu.cs.dennisc.property;

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;

import java.lang.reflect.Method;

/**
 * @author Dennis Cosgrove
 */
public final class PropertyUtilities {
	private PropertyUtilities() {
	}

	private static Method getGetter( Class<?> cls, String propertyName ) {
		Class<?>[] parameterTypes = {};
		String methodName;
		if( propertyName.startsWith( "Is" ) && Character.isUpperCase( propertyName.charAt( 2 ) ) ) {
			methodName = "is" + propertyName.substring( 2 );
		} else {
			methodName = "get" + propertyName;
		}
		return ReflectionUtilities.getMethod( cls, methodName, parameterTypes );
	}

	private static Method getSetter( Class<?> cls, String methodName, Class<?>[] parameterTypes ) {
		Method rv;
		try {
			rv = cls.getMethod( methodName, parameterTypes );
		} catch( NoSuchMethodException nsme ) {
			rv = null;
			for( Method mthd : cls.getMethods() ) {
				if( methodName.equals( mthd.getName() ) ) {
					if( mthd.isVarArgs() ) {
						Class<?>[] mthdParameterTypes = mthd.getParameterTypes();
						if( mthdParameterTypes.length == ( parameterTypes.length + 1 ) ) {
							boolean isMatch = true;
							for( int i = 0; i < parameterTypes.length; i++ ) {
								if (mthdParameterTypes[i] != parameterTypes[i]) {
									isMatch = false;
									break;
								}
							}
							if( isMatch ) {
								rv = mthd;
								break;
							}
						}
					}
				}
			}
		}
		return rv;
	}

	private static Method getSetter( Class<?> cls, String propertyName ) {
		Method getter = getGetter( cls, propertyName );
		Class<?> valueClass = getter.getReturnType();
		Class<?>[] parameterTypes = { valueClass };
		String methodName;
		if( propertyName.startsWith( "Is" ) && Character.isUpperCase( propertyName.charAt( 2 ) ) ) {
			methodName = "set" + propertyName.substring( 2 );
		} else {
			methodName = "set" + propertyName;
		}

		Method rv = getSetter( cls, methodName, parameterTypes );
		if (rv == null) {
			if( valueClass.equals( Double.TYPE ) || valueClass.equals( Double.class ) ) {
				parameterTypes[ 0 ] = Number.class;
				rv = getSetter( cls, methodName, parameterTypes );
			}
		}
		return rv;
	}

	public static String getPropertyNameForGetter( Method method ) {
		Class<?> valueClass = method.getReturnType();
		boolean isBoolean = valueClass.equals( Boolean.TYPE ) || valueClass.equals( Boolean.class );
		String prefix;
		if( isBoolean ) {
			prefix = "is";
		} else {
			prefix = "get";
		}
		String name = method.getName();
		if( name.startsWith( prefix ) ) {
			String rv = name.substring( prefix.length() );
			if( isBoolean ) {
				rv = "Is" + rv;
			}
			return rv;
		} else {
			return null;
		}
	}

	public static Method getSetterForGetter( Method method, Class<?> cls ) {
		return getSetter( cls, getPropertyNameForGetter( method ) );
	}

	public static Method getSetterForGetter( Method method ) {
		return getSetterForGetter( method, method.getDeclaringClass() );
	}
}
