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
public final class MethodReflectionProxy extends InvocableReflectionProxy<java.lang.reflect.Method> {
	private static java.lang.reflect.Method findVarArgsVersion( Class<?> cls, String name, Class<?>[] desiredParameterTypes ) {
		for( java.lang.reflect.Method mthd : cls.getDeclaredMethods() ) {
			if( mthd.isVarArgs() ) {
				if( mthd.getName().equals( name ) ) {
					Class<?>[] candidateParameterTypes = mthd.getParameterTypes();
					if( candidateParameterTypes.length == ( desiredParameterTypes.length + 1 ) ) {
						java.lang.reflect.Method rv = mthd;
						for( int i = 0; i < desiredParameterTypes.length; i++ ) {
							if( candidateParameterTypes[ i ].equals( desiredParameterTypes[ i ] ) ) {
								//pass
							} else {
								rv = null;
							}
						}
						if( rv != null ) {
							edu.cmu.cs.dennisc.java.util.logging.Logger.info( "MIGRATION: varArgs version used", rv );
							return rv;
						}
					}
				}
			}
		}
		return null;
	}

	private static Class<?>[] getTrimmedParameterTypes( Class<?>[] parameterTypes ) {
		Class<?>[] trimmedParameterTypes = new Class[ parameterTypes.length - 1 ];
		System.arraycopy( parameterTypes, 0, trimmedParameterTypes, 0, trimmedParameterTypes.length );
		return trimmedParameterTypes;
	}

	private static java.lang.reflect.Method getNonVarArgsVersion( Class<?> cls, String name, Class<?>[] parameterTypes ) {
		Class<?>[] trimmedParameterTypes = getTrimmedParameterTypes( parameterTypes );
		try {
			return cls.getDeclaredMethod( name, trimmedParameterTypes );
		} catch( NoSuchMethodException nsme ) {
			return null;
		}
	}

	public static MethodReflectionProxy getReplacementIfNecessary( MethodReflectionProxy original ) {
		Class<?> cls = original.getDeclaringClassReflectionProxy().getReification();
		if( cls != null ) {
			Class<?>[] parameterTypes = ClassReflectionProxy.getReifications( original.parameterClassReflectionProxies );
			try {
				cls.getDeclaredMethod( original.name, parameterTypes );
			} catch( NoSuchMethodException nsme ) {
				java.lang.reflect.Method varArgsMthd;
				if( original.isVarArgs ) {
					//varArgsMthd = getNonVarArgsVersion( cls, original.name, parameterTypes );
					varArgsMthd = null;
				} else {
					varArgsMthd = findVarArgsVersion( cls, original.name, parameterTypes );
					if( varArgsMthd != null ) {
						//pass
					} else {
						if( parameterTypes.length > 0 ) {
							Class<?> lastParameterType = parameterTypes[ parameterTypes.length - 1 ];
							if( lastParameterType != null ) {
								if( lastParameterType.isArray() ) {
									Class<?>[] trimmedParameterTypes = getTrimmedParameterTypes( parameterTypes );
									varArgsMthd = findVarArgsVersion( cls, original.name, trimmedParameterTypes );
								}
							}
						}
					}
				}
				if( varArgsMthd != null ) {
					return new MethodReflectionProxy( varArgsMthd );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( original );
				}
			}
		}
		return null;
	}

	public MethodReflectionProxy( ClassReflectionProxy declaringClassReflectionProxy, String name, ClassReflectionProxy[] parameterClassReflectionProxies, boolean isVarArgs ) {
		super( declaringClassReflectionProxy, parameterClassReflectionProxies );
		this.name = name;
		this.isVarArgs = isVarArgs;
	}

	public MethodReflectionProxy( java.lang.reflect.Method mthd ) {
		super( mthd, mthd.getDeclaringClass(), mthd.getParameterTypes() );
		this.name = mthd.getName();
		this.isVarArgs = mthd.isVarArgs();
	}

	public boolean isVarArgs() {
		return this.isVarArgs;
	}

	@Override
	protected int hashCodeNonReifiable() {
		int rv = super.hashCodeNonReifiable();
		rv = ( 37 * rv ) + this.name.hashCode();
		return rv;
	}

	@Override
	protected boolean equalsInstanceOfSameClassButNonReifiable( org.lgna.project.ast.ReflectionProxy<?> o ) {
		if( super.equalsInstanceOfSameClassButNonReifiable( o ) ) {
			MethodReflectionProxy other = (MethodReflectionProxy)o;
			return this.name != null ? this.name.equals( other.name ) : other.name == null;
		} else {
			return false;
		}
	}

	public String getName() {
		return this.name;
	}

	@Override
	protected java.lang.reflect.Method reify() {
		Class<?> cls = this.getDeclaringClassReflectionProxy().getReification();
		if( cls != null ) {
			Class<?>[] parameterTypes = ClassReflectionProxy.getReifications( this.parameterClassReflectionProxies );
			try {
				return cls.getDeclaredMethod( name, parameterTypes );
			} catch( NoSuchMethodException nsme ) {
				return null;
				//				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( nsme, this );
				//				java.lang.reflect.Method rv = findVarArgsVersion( cls, name, parameterTypes );
				//				if( rv != null ) {
				//					//pass
				//				} else {
				//					if( parameterTypes.length > 0 ) {
				//						Class<?> lastParameterType = parameterTypes[ parameterTypes.length - 1 ];
				//						if( lastParameterType != null ) {
				//							if( lastParameterType.isArray() ) {
				//								Class<?>[] trimmedParameterTypes = new Class[ parameterTypes.length - 1 ];
				//								System.arraycopy( parameterTypes, 0, trimmedParameterTypes, 0, trimmedParameterTypes.length );
				//								rv = findVarArgsVersion( cls, name, trimmedParameterTypes );
				//								if( rv != null ) {
				//									edu.cmu.cs.dennisc.java.util.logging.Logger.severe( rv );
				//								}
				//							}
				//						} else {
				//							edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
				//						}
				//					}
				//				}
				//				return rv;
			}
		} else {
			return null;
		}
	}

	@Override
	protected java.lang.annotation.Annotation[][] getReifiedParameterAnnotations() {
		java.lang.reflect.Method mthd = this.getReification();
		if( mthd != null ) {
			return mthd.getParameterAnnotations();
		} else {
			return null;
		}
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( ";name=" );
		sb.append( this.name );
	}

	private final String name;
	private final boolean isVarArgs;
}
