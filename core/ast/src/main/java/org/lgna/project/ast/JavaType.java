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

import java.util.function.BinaryOperator;

/**
 * @author Dennis Cosgrove
 */
public class JavaType extends AbstractType<JavaConstructor, JavaMethod, JavaField> {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<ClassReflectionProxy, JavaType> mapReflectionProxyToInstance = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();
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
	public static final JavaType STRING_TYPE = getInstance( String.class );

	private static java.util.Map<JavaType, JavaType> mapPrimitiveToWrapper = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();;
	static {
		addPrimitiveToWrapper( Void.TYPE, Void.class );
		addPrimitiveToWrapper( Boolean.TYPE, Boolean.class );
		addPrimitiveToWrapper( Byte.TYPE, Byte.class );
		addPrimitiveToWrapper( Character.TYPE, Character.class );
		addPrimitiveToWrapper( Short.TYPE, Short.class );
		addPrimitiveToWrapper( Integer.TYPE, Integer.class );
		addPrimitiveToWrapper( Long.TYPE, Long.class );
		addPrimitiveToWrapper( Float.TYPE, Float.class );
		addPrimitiveToWrapper( Double.TYPE, Double.class );
	}

	private static void addPrimitiveToWrapper( Class<?> primitiveCls, Class<?> wrapperCls ) {
		mapPrimitiveToWrapper.put( JavaType.getInstance( primitiveCls ), JavaType.getInstance( wrapperCls ) );
	}

	public static boolean isWrapperType( AbstractType<?, ?, ?> type ) {
		return mapPrimitiveToWrapper.containsValue( type );
	}

	/* package-private */static AbstractType<?, ?, ?> getWrapperTypeIfNecessary( AbstractType<?, ?, ?> type ) {
		if( type instanceof JavaType ) {
			JavaType javaType = (JavaType)type;
			if( javaType.isPrimitive() ) {
				JavaType wrapperType = mapPrimitiveToWrapper.get( javaType );
				if( wrapperType != null ) {
					return wrapperType;
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( type );
					return type;
				}
			} else {
				return type;
			}
		} else {
			return type;
		}
	}

	@Override
	protected boolean isAssignableFromType( org.lgna.project.ast.AbstractType<?, ?, ?> other ) {
		if( other != null ) {
			//todo: handle arrays
			JavaType otherTypeDeclaredInJava = other.getFirstEncounteredJavaType();
			if( otherTypeDeclaredInJava != null ) {
				Class<?> cls = this.getClassReflectionProxy().getReification();
				Class<?> otherCls = otherTypeDeclaredInJava.getClassReflectionProxy().getReification();
				if( ( cls != null ) && ( otherCls != null ) ) {
					return cls.isAssignableFrom( otherCls );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, cls, otherTypeDeclaredInJava, otherCls );
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static JavaType getInstance( final ClassReflectionProxy classReflectionProxy ) {
		if( classReflectionProxy != null ) {
			return mapReflectionProxyToInstance.getInitializingIfAbsent( classReflectionProxy, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<ClassReflectionProxy, JavaType>() {
				@Override
				public JavaType initialize( org.lgna.project.ast.ClassReflectionProxy key ) {
					return new JavaType( classReflectionProxy );
				}
			} );
		} else {
			return null;
		}
	}

	public static JavaType getInstance( Class<?> cls ) {
		if( cls != null ) {
			return getInstance( new ClassReflectionProxy( cls ) );
		} else {
			return null;
		}
	}

	public static JavaType[] getInstances( Class<?>[] clses ) {
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
		return ( modifiers & required ) != 0;
	}

	//	private static boolean isNotMask( int modifiers, int prohibited ) {
	//		return (modifiers & prohibited) == 0;
	//	}

	private JavaType( ClassReflectionProxy classReflectionProxy ) {
		this.classReflectionProxy = classReflectionProxy;
	}

	@Override
	public AbstractType<?, ?, ?> getKeywordFactoryType() {
		Class<?> cls = this.classReflectionProxy.getReification();
		if( cls != null ) {
			if( cls.isAnnotationPresent( org.lgna.project.annotations.ClassTemplate.class ) ) {
				org.lgna.project.annotations.ClassTemplate classTemplate = cls.getAnnotation( org.lgna.project.annotations.ClassTemplate.class );
				Class<?> keywordFactoryCls = classTemplate.keywordFactoryCls();
				if( keywordFactoryCls == org.lgna.project.annotations.ClassTemplate.VOID_ACTS_AS_NULL ) {
					return null;
				} else {
					return JavaType.getInstance( keywordFactoryCls );
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean isFollowToSuperClassDesired() {
		Class<?> cls = this.classReflectionProxy.getReification();
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
		Class<?> cls = this.classReflectionProxy.getReification();
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
	public JavaPackage getPackage() {
		return JavaPackage.getInstance( this.classReflectionProxy.getPackageReflectionProxy() );
	}

	@Override
	public JavaType getSuperType() {
		Class<?> cls = this.classReflectionProxy.getReification();
		if( cls != null ) {
			//			if( cls.isInterface() ) {
			//				Class<?>[] superInterfaces = cls.getInterfaces();
			//				if( superInterfaces.length == 1 ) {
			//					return JavaType.getInstance( superInterfaces[ 0 ] );
			//				} else {
			//					return null;
			//				}
			//			} else {
			return JavaType.getInstance( cls.getSuperclass() );
			//			}
		} else {
			return null;
		}
	}

	@Override
	public JavaType[] getInterfaces() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return JavaType.getInstances( cls.getInterfaces() );
	}

	@Override
	public java.util.List<JavaConstructor> getDeclaredConstructors() {
		return this.constructors.get();
	}

	@Override
	public java.util.List<JavaMethod> getDeclaredMethods() {
		return this.methods.get();
	}

	@Override
	public java.util.List<JavaField> getDeclaredFields() {
		return this.fields.get();
	}

	public java.util.List<JavaGetterSetterPair> getGetterSetterPairs() {
		return this.getterSetterPairs.get();
	}

	private static Class<?>[] trimLast( Class<?>[] src ) {
		Class<?>[] rv = new Class<?>[ src.length - 1 ];
		System.arraycopy( src, 0, rv, 0, rv.length );
		return rv;
	}

	private static java.lang.reflect.Method getNextShorterInChain( java.lang.reflect.Method src ) {
		java.lang.reflect.Method rv;
		Class<?> srcReturnCls = src.getReturnType();
		String name = src.getName();
		Class<?>[] srcParameterClses = src.getParameterTypes();
		if( srcParameterClses.length > 0 ) {
			Class<?>[] dstParameterClses = trimLast( srcParameterClses );
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

	public ClassReflectionProxy getClassReflectionProxy() {
		return this.classReflectionProxy;
	}

	public boolean contentEquals( Class<?> cls ) {
		if( this.classReflectionProxy != null ) {
			Class<?> reification = this.classReflectionProxy.getReification();
			return cls == reification;
		} else {
			return false;
		}
	}

	@Override
	public boolean isUserAuthored() {
		return false;
	}

	@Override
	public AccessLevel getAccessLevel() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return AccessLevel.getValueFromModifiers( cls.getModifiers() );
	}

	@Override
	public boolean isPrimitive() {
		Class<?> cls = this.classReflectionProxy.getReification();
		if( cls != null ) {
			return cls.isPrimitive();
		} else {
			return false;
		}
	}

	@Override
	public boolean isInterface() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return cls.isInterface();
	}

	@Override
	public boolean isStatic() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return java.lang.reflect.Modifier.isStatic( cls.getModifiers() );
	}

	@Override
	public boolean isAbstract() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return java.lang.reflect.Modifier.isAbstract( cls.getModifiers() );
	}

	@Override
	public boolean isFinal() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return java.lang.reflect.Modifier.isFinal( cls.getModifiers() );
	}

	@Override
	public boolean isStrictFloatingPoint() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return java.lang.reflect.Modifier.isStrict( cls.getModifiers() );
	}

	@Override
	public boolean isArray() {
		return this.classReflectionProxy.isArray();
	}

	@Override
	public boolean isEnum() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return cls.isEnum();
	}

	public JavaType getEnclosingType() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return JavaType.getInstance( cls.getEnclosingClass() );
	}

	@Override
	public JavaType getComponentType() {
		return JavaType.getInstance( this.classReflectionProxy.getComponentClassReflectionProxy() );
	}

	@Override
	public JavaType getArrayType() {
		Class<?> cls = this.classReflectionProxy.getReification();
		assert cls != null : this.classReflectionProxy;
		return JavaType.getInstance( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getArrayClass( cls ) );
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof JavaType ) {
			return classReflectionProxy.equals( ( (JavaType)other ).classReflectionProxy );
		} else {
			return false;
		}
	}

	private static void handleMthd( java.lang.reflect.Method mthd, java.util.List<JavaMethod> methods ) {
		int modifiers = mthd.getModifiers();
		if( isMask( modifiers, java.lang.reflect.Modifier.PUBLIC ) || isMask( modifiers, java.lang.reflect.Modifier.PROTECTED ) ) {
			JavaMethod methodDeclaredInJava = JavaMethod.getInstance( mthd );
			if( mthd.isAnnotationPresent( org.lgna.project.annotations.MethodTemplate.class ) ) {
				org.lgna.project.annotations.MethodTemplate methodTemplate = mthd.getAnnotation( org.lgna.project.annotations.MethodTemplate.class );
				if( ( methodTemplate.visibility() == org.lgna.project.annotations.Visibility.PRIME_TIME ) && ( methodTemplate.isFollowedByLongerMethod() == false ) ) {
					JavaMethod longer = methodDeclaredInJava;
					java.lang.reflect.Method _mthd = mthd;
					while( true ) {
						_mthd = getNextShorterInChain( _mthd );
						if( _mthd != null ) {
							JavaMethod shorter = JavaMethod.getInstance( _mthd );
							if( _mthd.isAnnotationPresent( org.lgna.project.annotations.MethodTemplate.class ) ) {
								org.lgna.project.annotations.MethodTemplate shorterMethodTemplate = _mthd.getAnnotation( org.lgna.project.annotations.MethodTemplate.class );
								if( shorterMethodTemplate.isFollowedByLongerMethod() ) {
									longer.setNextShorterInChain( shorter );
									shorter.setNextLongerInChain( longer );
									longer = shorter;
								} else {
									break;
								}
							}
						} else {
							break;
						}
					}
				}
			}
			methods.add( methodDeclaredInJava );
		}
	}

	private final ClassReflectionProxy classReflectionProxy;
	private final edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaConstructor>> constructors = new edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaConstructor>>() {
		@Override
		protected java.util.List<JavaConstructor> create() {
			Class<?> cls = classReflectionProxy.getReification();
			if( cls != null ) {
				java.util.List<JavaConstructor> constructors = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( java.lang.reflect.Constructor<?> cnstrctr : cls.getDeclaredConstructors() ) {
					constructors.add( JavaConstructor.getInstance( cnstrctr ) );
				}
				return java.util.Collections.unmodifiableList( constructors );
			} else {
				return java.util.Collections.emptyList();
			}
		}
	};
	private final edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaMethod>> methods = new edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaMethod>>() {
		@Override
		protected java.util.List<JavaMethod> create() {
			Class<?> cls = classReflectionProxy.getReification();
			if( cls != null ) {
				java.util.List<JavaMethod> methods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				java.util.Set<java.lang.reflect.Method> methodSet = null;
				Iterable<org.lgna.project.reflect.MethodInfo> methodInfos = org.lgna.project.reflect.ClassInfoManager.getMethodInfos( cls );
				if( methodInfos != null ) {
					methodSet = new java.util.HashSet<java.lang.reflect.Method>();
					for( org.lgna.project.reflect.MethodInfo methodInfo : methodInfos ) {
						try {
							java.lang.reflect.Method mthd = methodInfo.getMthd();
							if( mthd != null ) {
								handleMthd( mthd, methods );
								methodSet.add( mthd );
							}
						} catch( RuntimeException re ) {
							//edu.cmu.cs.dennisc.print.PrintUtilities.println( "no such method", methodInfo, "on ", cls );
							//re.printStackTrace();
						}
					}
				}
				for( java.lang.reflect.Method mthd : cls.getDeclaredMethods() ) {
					if( ( methodSet != null ) && methodSet.contains( mthd ) ) {
						//pass
					} else {
						handleMthd( mthd, methods );
					}
				}

				//update value templates for setters
				for( JavaMethod method : methods ) {
					java.lang.reflect.Method mthd = method.getMethodReflectionProxy().getReification();
					org.lgna.project.annotations.GetterTemplate propertyGetterTemplate = mthd.getAnnotation( org.lgna.project.annotations.GetterTemplate.class );
					if( propertyGetterTemplate != null ) {
						java.lang.reflect.Method sttr = edu.cmu.cs.dennisc.property.PropertyUtilities.getSetterForGetter( mthd );
						JavaMethod setter;
						if( sttr != null ) {
							setter = (JavaMethod)JavaMethod.getInstance( sttr ).getLongestInChain();
						} else {
							setter = null;
						}
						if( setter != null ) {
							org.lgna.project.annotations.ValueTemplate valueTemplate = mthd.getAnnotation( org.lgna.project.annotations.ValueTemplate.class );
							if( valueTemplate != null ) {
								JavaMethod m = setter;
								while( m != null ) {
									JavaMethodParameter parameter0 = (JavaMethodParameter)m.getRequiredParameters().get( 0 );
									parameter0.setValueTemplate( valueTemplate );
									m = m.getNextShorterInChain();
								}
							}
						}
					}
				}

				return java.util.Collections.unmodifiableList( methods );
			} else {
				return java.util.Collections.emptyList();
			}
		}
	};
	private final edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaField>> fields = new edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaField>>() {
		@Override
		protected java.util.List<JavaField> create() {
			Class<?> cls = classReflectionProxy.getReification();
			if( cls != null ) {
				java.util.List<JavaField> fields = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( java.lang.reflect.Field fld : cls.getDeclaredFields() ) {
					fields.add( JavaField.getInstance( fld ) );
				}
				return java.util.Collections.unmodifiableList( fields );
			} else {
				return java.util.Collections.emptyList();
			}
		}
	};
	private final edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaGetterSetterPair>> getterSetterPairs = new edu.cmu.cs.dennisc.pattern.Lazy<java.util.List<JavaGetterSetterPair>>() {
		@Override
		protected java.util.List<JavaGetterSetterPair> create() {
			Class<?> cls = classReflectionProxy.getReification();
			if( cls != null ) {
				java.util.List<JavaGetterSetterPair> getterSetterPairs = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( JavaMethod method : getDeclaredMethods() ) {
					java.lang.reflect.Method mthd = method.getMethodReflectionProxy().getReification();
					org.lgna.project.annotations.GetterTemplate propertyGetterTemplate = mthd.getAnnotation( org.lgna.project.annotations.GetterTemplate.class );
					if( propertyGetterTemplate != null ) {
						java.lang.reflect.Method sttr = edu.cmu.cs.dennisc.property.PropertyUtilities.getSetterForGetter( mthd );
						JavaMethod setter;
						if( sttr != null ) {
							setter = (JavaMethod)JavaMethod.getInstance( sttr ).getLongestInChain();
						} else {
							setter = null;
						}
						getterSetterPairs.add( new JavaGetterSetterPair( method, setter ) );
					}
				}
				return java.util.Collections.unmodifiableList( getterSetterPairs );
			} else {
				return java.util.Collections.emptyList();
			}
		}
	};

	@Override
	protected String formatTypeName(BinaryOperator<String> localizer) {
		Class<?> cls = getClassReflectionProxy().getReification();
		return localizer.apply(cls.getName(), cls.getSimpleName());
	}
}
