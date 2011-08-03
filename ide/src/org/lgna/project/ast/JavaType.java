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
public class JavaType extends AbstractType<JavaConstructor, JavaMethod, JavaField> {
	private static java.util.Map< ClassReflectionProxy, JavaType > s_mapReflectionProxyToJava = new java.util.HashMap< ClassReflectionProxy, JavaType >();
	public static final JavaType VOID_TYPE = getInstance( Void.TYPE );

	public static final JavaType BOOLEAN_PRIMITIVE_TYPE = getInstance( Boolean.TYPE );
	public static final JavaType BOOLEAN_OBJECT_TYPE = getInstance( Boolean.class );

	public static final JavaType NUMBER_OBJECT_TYPE = getInstance( Number.class );

	public static final JavaType INTEGER_PRIMITIVE_TYPE = getInstance( Integer.TYPE );
	public static final JavaType INTEGER_OBJECT_TYPE = getInstance( Integer.class );
	public static final JavaType DOUBLE_PRIMITIVE_TYPE = getInstance( Double.TYPE );
	public static final JavaType DOUBLE_OBJECT_TYPE = getInstance( Double.class );

	public static final JavaType[] BOOLEAN_TYPES = { BOOLEAN_PRIMITIVE_TYPE, BOOLEAN_OBJECT_TYPE };
	public static final JavaType[] INTEGER_TYPES = { INTEGER_PRIMITIVE_TYPE, INTEGER_OBJECT_TYPE };
	public static final JavaType[] DOUBLE_TYPES = { DOUBLE_PRIMITIVE_TYPE, DOUBLE_OBJECT_TYPE };
	
	public static final JavaType OBJECT_TYPE = getInstance( Object.class );

	public static JavaType getInstance( ClassReflectionProxy classReflectionProxy ) {
		if( classReflectionProxy != null ) {
			JavaType rv = s_mapReflectionProxyToJava.get( classReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new JavaType( classReflectionProxy );
				s_mapReflectionProxyToJava.put( classReflectionProxy, rv );
				Class< ? > cls = classReflectionProxy.getReification();
				if( cls != null ) {
					//todo: handle constructors as methods are (set up chains...)
					for( java.lang.reflect.Constructor< ? > cnstrctr : cls.getDeclaredConstructors() ) {
						rv.constructors.add( JavaConstructor.getInstance( cnstrctr ) );
					}
					
					for( java.lang.reflect.Field fld : cls.getDeclaredFields() ) {
						rv.fields.add( JavaField.getInstance( fld ) );
					}

					java.util.Set< java.lang.reflect.Method > methodSet = null;
					Iterable< org.lgna.project.reflect.MethodInfo > methodInfos = org.lgna.project.reflect.ClassInfoManager.getMethodInfos( cls );
					if( methodInfos != null ) {
						methodSet = new java.util.HashSet< java.lang.reflect.Method >();
						for( org.lgna.project.reflect.MethodInfo methodInfo : methodInfos ) {
							try {
								java.lang.reflect.Method mthd = methodInfo.getMthd();
								if( mthd != null ) {
									rv.handleMthd( mthd );
									methodSet.add( mthd );
								}
							} catch( RuntimeException re ) {
								//edu.cmu.cs.dennisc.print.PrintUtilities.println( "no such method", methodInfo, "on ", cls );
								//re.printStackTrace();
							}
						}
					}
					for( java.lang.reflect.Method mthd : cls.getDeclaredMethods() ) {
						if( methodSet != null && methodSet.contains( mthd ) ) {
							//pass
						} else {
							rv.handleMthd( mthd );
						}
					}
				}
			}
			return rv;
		} else {
			return null;
		}
	}
	public static JavaType getInstance( Class< ? > cls ) {
		if( cls != null ) {
			return getInstance( new ClassReflectionProxy( cls ) );
		} else {
			return null;
		}
	}
	public static JavaType[] getInstances( Class< ? >[] clses ) {
		JavaType[] rv = new JavaType[ clses.length ];
		for( int i = 0; i < clses.length; i++ ) {
			rv[ i ] = getInstance( clses[ i ] );
		}
		return rv;
	}
	public static JavaType[] getInstances( ClassReflectionProxy[] classReflectionProxies ) {
		JavaType[] rv = new JavaType[ classReflectionProxies.length ];
		for( int i = 0; i < classReflectionProxies.length; i++ ) {
			rv[ i ] = getInstance( classReflectionProxies[ i ] );
		}
		return rv;
	}

	private static boolean isMask( int modifiers, int required ) {
		return (modifiers & required) != 0;
	}
//	private static boolean isNotMask( int modifiers, int prohibited ) {
//		return (modifiers & prohibited) == 0;
//	}

	private ClassReflectionProxy classReflectionProxy;
	private java.util.ArrayList< JavaConstructor > constructors = new java.util.ArrayList< JavaConstructor >();
	private java.util.ArrayList< JavaMethod > methods = new java.util.ArrayList< JavaMethod >();
	private java.util.ArrayList< JavaField > fields = new java.util.ArrayList< JavaField >();

	private JavaType( ClassReflectionProxy classReflectionProxy ) {
		this.classReflectionProxy = classReflectionProxy;
	}

	@Override
	public boolean isFollowToSuperClassDesired() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		if( cls != null ) {
			if( cls.isAnnotationPresent( org.lgna.project.annotations.ClassTemplate.class ) ) {
				org.lgna.project.annotations.ClassTemplate classTemplate = cls.getAnnotation( org.lgna.project.annotations.ClassTemplate.class );
				return classTemplate.isFollowToSuperClassDesired();
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	@Override
	public boolean isConsumptionBySubClassDesired() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		if( cls != null ) {
			if( cls.isAnnotationPresent( org.lgna.project.annotations.ClassTemplate.class ) ) {
				org.lgna.project.annotations.ClassTemplate classTemplate = cls.getAnnotation( org.lgna.project.annotations.ClassTemplate.class );
				return classTemplate.isConsumptionBySubClassDesired();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String getName() {
		return this.classReflectionProxy.getSimpleName();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}
	@Override
	public AbstractPackage getPackage() {
		return JavaPackage.getInstance( this.classReflectionProxy.getPackageReflectionProxy() );
	}
	@Override
	public AbstractType<?,?,?> getSuperType() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		if( cls != null ) {
			return JavaType.getInstance( cls.getSuperclass() );
		} else {
			return null;
		}
	}
	@Override
	public java.util.ArrayList< JavaConstructor > getDeclaredConstructors() {
		return this.constructors;
	}
	@Override
	public java.util.ArrayList< JavaMethod > getDeclaredMethods() {
		return this.methods;
	}
	@Override
	public java.util.ArrayList< JavaField > getDeclaredFields() {
		return this.fields;
	}

	private static Class< ? >[] trimLast( Class< ? >[] src ) {
		Class< ? >[] rv = new Class< ? >[ src.length - 1 ];
		System.arraycopy( src, 0, rv, 0, rv.length );
		return rv;
	}
	private static java.lang.reflect.Method getNextShorterInChain( java.lang.reflect.Method src ) {
		java.lang.reflect.Method rv;
		Class< ? > srcReturnCls = src.getReturnType();
		String name = src.getName();
		Class< ? >[] srcParameterClses = src.getParameterTypes();
		if( srcParameterClses.length > 0 ) {
			Class< ? >[] dstParameterClses = trimLast( srcParameterClses );
			try {
				rv = src.getDeclaringClass().getMethod( name, dstParameterClses );
				if( rv.getReturnType() == srcReturnCls ) {
					//pass
				} else {
					rv = null;
				}
			} catch( NoSuchMethodException nsme ) {
				rv = null;
			}
		} else {
			rv = null;
		}
		return rv;
	}
	private void handleMthd( java.lang.reflect.Method mthd ) {
		int modifiers = mthd.getModifiers();
		if( isMask( modifiers, java.lang.reflect.Modifier.PUBLIC ) /*&& isNotMask( modifiers, java.lang.reflect.Modifier.STATIC )*/) {
			JavaMethod methodDeclaredInJava = JavaMethod.getInstance( mthd );
			org.lgna.project.annotations.Visibility visibility = methodDeclaredInJava.getVisibility();

			if( visibility == org.lgna.project.annotations.Visibility.PRIME_TIME ) {
				JavaMethod longer = methodDeclaredInJava;
				java.lang.reflect.Method _mthd = mthd;
				while( true ) {
					_mthd = getNextShorterInChain( _mthd );
					if( _mthd != null ) {
						JavaMethod shorter = JavaMethod.getInstance( _mthd );
						if( shorter.getVisibility() == org.lgna.project.annotations.Visibility.CHAINED ) {
							longer.setNextShorterInChain( shorter );
							shorter.setNextLongerInChain( longer );
							longer = shorter;
						} else {
							break;
						}
					} else {
						break;
					}
				}
			}
			this.methods.add( methodDeclaredInJava );
		}
	}
	
	public ClassReflectionProxy getClassReflectionProxy() {
		return this.classReflectionProxy;
	}

	@Override
	public boolean isDeclaredInAlice() {
		return false;
	}

	@Override
	public Access getAccess() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return Access.get( cls.getModifiers() );
	}

	@Override
	public boolean isInterface() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return cls.isInterface();
	}

	@Override
	public boolean isStatic() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return java.lang.reflect.Modifier.isStatic( cls.getModifiers() );
	}
	@Override
	public boolean isAbstract() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return java.lang.reflect.Modifier.isAbstract( cls.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return java.lang.reflect.Modifier.isFinal( cls.getModifiers() );
	}
	@Override
	public boolean isStrictFloatingPoint() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return java.lang.reflect.Modifier.isStrict( cls.getModifiers() );
	}

	@Override
	public boolean isArray() {
		return this.classReflectionProxy.isArray();
	}
	@Override
	public AbstractType<?,?,?> getComponentType() {
		return JavaType.getInstance( this.classReflectionProxy.getComponentClassReflectionProxy() );
	}

	@Override
	public AbstractType<?,?,?> getArrayType() {
		Class< ? > cls = this.classReflectionProxy.getReification();
		assert cls != null;
		return JavaType.getInstance( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getArrayClass( cls ) );
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof JavaType ) {
			return classReflectionProxy.equals( ((JavaType)other).classReflectionProxy );
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return getClass() + "[cls=" + this.classReflectionProxy.getName() + "]";
	}
}
