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
package edu.cmu.cs.dennisc.property;

/**
 * @author Dennis Cosgrove
 */
public final class PropertyUtilities {
	private PropertyUtilities() {
	}

	public static java.lang.reflect.Method getGetter( Class<?> cls, String propertyName ) {
		Class<?>[] parameterTypes = {};
		String methodName;
		if( propertyName.startsWith( "Is" ) && Character.isUpperCase( propertyName.charAt( 2 ) ) ) {
			methodName = "is" + propertyName.substring( 2 );
		} else {
			methodName = "get" + propertyName;
		}
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( cls, methodName, parameterTypes );
	}

	private static java.lang.reflect.Method getSetter( Class<?> cls, String methodName, Class<?>[] parameterTypes ) {
		java.lang.reflect.Method rv;
		try {
			rv = cls.getMethod( methodName, parameterTypes );
		} catch( NoSuchMethodException nsme ) {
			rv = null;
			for( java.lang.reflect.Method mthd : cls.getMethods() ) {
				if( methodName.equals( mthd.getName() ) ) {
					if( mthd.isVarArgs() ) {
						Class<?>[] mthdParameterTypes = mthd.getParameterTypes();
						if( mthdParameterTypes.length == ( parameterTypes.length + 1 ) ) {
							boolean isMatch = true;
							for( int i = 0; i < parameterTypes.length; i++ ) {
								if( mthdParameterTypes[ i ] == parameterTypes[ i ] ) {
									//pass
								} else {
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

	public static java.lang.reflect.Method getSetter( Class<?> cls, String propertyName ) {
		java.lang.reflect.Method getter = getGetter( cls, propertyName );
		Class<?> valueClass = getter.getReturnType();
		Class<?>[] parameterTypes = { valueClass };
		String methodName;
		if( propertyName.startsWith( "Is" ) && Character.isUpperCase( propertyName.charAt( 2 ) ) ) {
			methodName = "set" + propertyName.substring( 2 );
		} else {
			methodName = "set" + propertyName;
		}

		java.lang.reflect.Method rv = getSetter( cls, methodName, parameterTypes );
		if( rv != null ) {
			//pass
		} else {
			if( valueClass.equals( Double.TYPE ) || valueClass.equals( Double.class ) ) {
				parameterTypes[ 0 ] = Number.class;
				rv = getSetter( cls, methodName, parameterTypes );
			}
		}
		return rv;
	}

	public static boolean hasGetter( Class<?> cls, String propertyName ) {
		try {
			return getGetter( cls, propertyName ) != null;
		} catch( RuntimeException re ) {
			return false;
		}
	}

	public static boolean hasSetter( Class<?> cls, String propertyName ) {
		try {
			return getSetter( cls, propertyName ) != null;
		} catch( RuntimeException re ) {
			return false;
		}
	}

	public static String getPropertyNameForGetter( java.lang.reflect.Method method ) {
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

	private static String getPropertyNameForSetter( java.lang.reflect.Method method, boolean isExtraParametersAcceptable ) {
		String prefix = "set";
		String name = method.getName();
		if( name.startsWith( prefix ) ) {
			Class<?>[] parameterClses = method.getParameterTypes();
			if( ( parameterClses.length == 1 ) || ( isExtraParametersAcceptable && ( parameterClses.length >= 1 ) ) ) {
				Class<?> valueClass = parameterClses[ 0 ];
				boolean isBoolean = valueClass.equals( Boolean.TYPE ) || valueClass.equals( Boolean.class );
				String rv = name.substring( prefix.length() );
				if( isBoolean ) {
					rv = "Is" + rv;
				}
				return rv;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getPropertyNameForSetter( java.lang.reflect.Method method ) {
		return getPropertyNameForSetter( method, false );
	}

	public static boolean isGetterAndSetterExists( java.lang.reflect.Method method ) {
		String propertyName = getPropertyNameForGetter( method );
		if( propertyName != null ) {
			return hasSetter( method.getDeclaringClass(), propertyName );
		} else {
			return false;
		}
	}

	public static boolean isSetterAndGetterExists( java.lang.reflect.Method method ) {
		String propertyName = getPropertyNameForSetter( method, false );
		if( propertyName != null ) {
			return hasGetter( method.getDeclaringClass(), propertyName );
		} else {
			return false;
		}
	}

	public static boolean isSetterWithExtraParametersAndGetterExists( java.lang.reflect.Method method ) {
		String propertyName = getPropertyNameForSetter( method, true );
		if( propertyName != null ) {
			return hasGetter( method.getDeclaringClass(), propertyName );
		} else {
			return false;
		}
	}

	public static java.lang.reflect.Method getSetterForGetter( java.lang.reflect.Method method, Class<?> cls ) {
		return getSetter( cls, getPropertyNameForGetter( method ) );
	}

	public static java.lang.reflect.Method getSetterForGetter( java.lang.reflect.Method method ) {
		return getSetterForGetter( method, method.getDeclaringClass() );
	}

	public static java.lang.reflect.Method getGetterForSetter( java.lang.reflect.Method method ) {
		return getGetter( method.getDeclaringClass(), getPropertyNameForSetter( method ) );
	}

	public static Object getPropertyValue( Object o, String propertyName ) {
		java.lang.reflect.Method getter = getGetter( o.getClass(), propertyName );
		Object[] args = {};
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( o, getter, args );
	}

	public static void setPropertyValue( Object o, String propertyName, Object value ) {
		java.lang.reflect.Method setter = getSetter( o.getClass(), propertyName );
		Object[] args = { value };
		edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( o, setter, args );
	}
}
