/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class TypeDeclaredInJava extends AbstractType {
	private static java.util.Map< ClassReflectionProxy, TypeDeclaredInJava > s_map = new java.util.HashMap< ClassReflectionProxy, TypeDeclaredInJava >();
	public static final TypeDeclaredInJava VOID_TYPE = get( Void.TYPE );

	public static final TypeDeclaredInJava BOOLEAN_PRIMITIVE_TYPE = get( Boolean.TYPE );
	public static final TypeDeclaredInJava BOOLEAN_OBJECT_TYPE = get( Boolean.class );

	public static final TypeDeclaredInJava INTEGER_PRIMITIVE_TYPE = get( Integer.TYPE );
	public static final TypeDeclaredInJava INTEGER_OBJECT_TYPE = get( Integer.class );
	public static final TypeDeclaredInJava DOUBLE_PRIMITIVE_TYPE = get( Double.TYPE );
	public static final TypeDeclaredInJava DOUBLE_OBJECT_TYPE = get( Double.class );

	public static final TypeDeclaredInJava[] BOOLEAN_TYPES = { BOOLEAN_PRIMITIVE_TYPE, BOOLEAN_OBJECT_TYPE };
	public static final TypeDeclaredInJava[] INTEGER_TYPES = { INTEGER_PRIMITIVE_TYPE, INTEGER_OBJECT_TYPE };
	public static final TypeDeclaredInJava[] DOUBLE_TYPES = { DOUBLE_PRIMITIVE_TYPE, DOUBLE_OBJECT_TYPE };

	public static TypeDeclaredInJava get( ClassReflectionProxy classReflectionProxy ) {
		if( classReflectionProxy != null ) {
			TypeDeclaredInJava rv = s_map.get( classReflectionProxy );
			if( rv != null ) {
				//pass
			} else {
				rv = new TypeDeclaredInJava( classReflectionProxy );
				s_map.put( classReflectionProxy, rv );
				Class< ? > cls = classReflectionProxy.getCls();
				if( cls != null ) {
					for( java.lang.reflect.Constructor< ? > cnstrctr : cls.getDeclaredConstructors() ) {
						rv.constructors.add( ConstructorDeclaredInJava.get( cnstrctr ) );
					}
					for( java.lang.reflect.Field fld : cls.getDeclaredFields() ) {
						rv.fields.add( FieldDeclaredInJavaWithField.get( fld ) );
					}

					java.util.Set< java.lang.reflect.Method > set = null;
					Iterable< edu.cmu.cs.dennisc.alice.reflect.MethodInfo > methodInfos = edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.getMethodInfos( cls );
					if( methodInfos != null ) {
						set = new java.util.HashSet< java.lang.reflect.Method >();
						for( edu.cmu.cs.dennisc.alice.reflect.MethodInfo methodInfo : methodInfos ) {
							try {
								java.lang.reflect.Method mthd = methodInfo.getMthd();
								if( mthd != null ) {
									rv.handleMthd( mthd );
									set.add( mthd );
								}
							} catch( RuntimeException re ) {
								edu.cmu.cs.dennisc.print.PrintUtilities.println( "no such method", methodInfo, "on Class", cls );
								//re.printStackTrace();
							}
						}
					}
					for( java.lang.reflect.Method mthd : cls.getDeclaredMethods() ) {
						if( set != null && set.contains( mthd ) ) {
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
	public static TypeDeclaredInJava get( Class< ? > cls ) {
		if( cls != null ) {
			TypeDeclaredInJava rv = s_map.get( cls );
			if( rv != null ) {
				//pass
			} else {
				rv = get( new ClassReflectionProxy( cls ) );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static TypeDeclaredInJava[] get( Class< ? >[] clses ) {
		TypeDeclaredInJava[] rv = new TypeDeclaredInJava[ clses.length ];
		for( int i = 0; i < clses.length; i++ ) {
			rv[ i ] = get( clses[ i ] );
		}
		return rv;
	}
	public static TypeDeclaredInJava[] get( ClassReflectionProxy[] classReflectionProxies ) {
		TypeDeclaredInJava[] rv = new TypeDeclaredInJava[ classReflectionProxies.length ];
		for( int i = 0; i < classReflectionProxies.length; i++ ) {
			rv[ i ] = get( classReflectionProxies[ i ] );
		}
		return rv;
	}
	
//	public static FieldDeclaredInJavaWithField getField( FieldReflectionProxy fieldReflectionProxy ) {
//		return getField( fieldReflectionProxy.getFld() );
//	}
//	public static FieldDeclaredInJavaWithGetterAndSetter getField( MethodReflectionProxy getterReflectionProxy, MethodReflectionProxy setterReflectionProxy ) {
//		return getField( getterReflectionProxy.getMthd(), setterReflectionProxy.getMthd() );
//	}
//	public static FieldDeclaredInJavaWithField getField( java.lang.reflect.Field fld ) {
//		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( fld.getDeclaringClass() );
//		for( AbstractField field : typeDeclaredInJava.fields ) {
//			if( field instanceof FieldDeclaredInJavaWithField ) {
//				FieldDeclaredInJavaWithField fieldDeclaredInJavaWithField = (FieldDeclaredInJavaWithField)field;
//				if( fld.equals( fieldDeclaredInJavaWithField.getFld() ) ) {
//					return fieldDeclaredInJavaWithField;
//				}
//			}
//		}
//		assert false : fld;
//		return null;
//	}
//	public static FieldDeclaredInJavaWithGetterAndSetter getField( java.lang.reflect.Method gttr, java.lang.reflect.Method sttr ) {
//		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( gttr.getDeclaringClass() );
//		for( AbstractField field : typeDeclaredInJava.fields ) {
//			if( field instanceof FieldDeclaredInJavaWithGetterAndSetter ) {
//				FieldDeclaredInJavaWithGetterAndSetter fieldDeclaredInJavaWithGetterAndSetter = (FieldDeclaredInJavaWithGetterAndSetter)field;
//				if( gttr.equals( fieldDeclaredInJavaWithGetterAndSetter.getGttr() ) ) {
//					assert sttr.equals( fieldDeclaredInJavaWithGetterAndSetter.getSttr() ) : sttr;
//					return fieldDeclaredInJavaWithGetterAndSetter;
//				}
//			}
//		}
//		assert false : gttr;
//		return null;
//	}
//
//	public static ConstructorDeclaredInJava getConstructor( java.lang.reflect.Constructor< ? > cnstrctr ) {
//		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( cnstrctr.getDeclaringClass() );
//		for( AbstractConstructor constructor : typeDeclaredInJava.constructors ) {
//			ConstructorDeclaredInJava constructorDeclaredInJava = (ConstructorDeclaredInJava)constructor;
//			if( cnstrctr.equals( constructorDeclaredInJava.getCnstrctr() ) ) {
//				return constructorDeclaredInJava;
//			}
//		}
//		assert false : cnstrctr;
//		return null;
//	}
//	public static ConstructorDeclaredInJava getConstructor( ConstructorReflectionProxy cnstrctr ) {
//		return getConstructor( cnstrctr.getCnstrctr() );
//	}
//	public static MethodDeclaredInJava getMethod( java.lang.reflect.Method mthd ) {
//		//System.err.println( "searching for: " + mthd.getName() );
//		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( mthd.getDeclaringClass() );
//		for( AbstractMethod method : typeDeclaredInJava.methods ) {
//			//System.err.println( "checking: " + method.getName() );
//			if( method.getName().equals( mthd.getName() ) ) {
//				//System.err.println( "FOUND: " + mthd.getName() );
//				MethodDeclaredInJava methodDeclaredInJava = (MethodDeclaredInJava)method;
//				MethodDeclaredInJava m = methodDeclaredInJava;
//				while( m != null ) {
//					if( mthd.equals( m.getMthd() ) ) {
//						//System.err.println( "__FOUND__: " + mthd.getName() );
//						return m;
//					}
//					m = (MethodDeclaredInJava)m.getNextShorterInChain();
//				}
//			}
//		}
////		assert false : mthd;
////		return null;
//		//System.err.println( "NOT FOUND:" + mthd.getName() );
//		//Thread.dumpStack();
//		return new MethodDeclaredInJava( mthd );
//	}
//	public static MethodDeclaredInJava getMethod( MethodReflectionProxy methodReflectionProxy ) {
//		return getMethod( methodReflectionProxy.getMthd() );
//	}

	private static boolean isMask( int modifiers, int required ) {
		return (modifiers & required) != 0;
	}
	private static boolean isNotMask( int modifiers, int prohibited ) {
		return (modifiers & prohibited) == 0;
	}

	private ClassReflectionProxy classReflectionProxy;
	private java.util.ArrayList< ConstructorDeclaredInJava > constructors = new java.util.ArrayList< ConstructorDeclaredInJava >();
	private java.util.ArrayList< MethodDeclaredInJava > methods = new java.util.ArrayList< MethodDeclaredInJava >();
	private java.util.ArrayList< FieldDeclaredInJava > fields = new java.util.ArrayList< FieldDeclaredInJava >();

	private TypeDeclaredInJava( ClassReflectionProxy classReflectionProxy ) {
		this.classReflectionProxy = classReflectionProxy;
	}

	@Override
	public boolean isFollowToSuperClassDesired() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		if( cls != null ) {
			if( cls.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class ) ) {
				edu.cmu.cs.dennisc.alice.annotations.ClassTemplate classTemplate = cls.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class );
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
		Class< ? > cls = this.classReflectionProxy.getCls();
		if( cls != null ) {
			if( cls.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class ) ) {
				edu.cmu.cs.dennisc.alice.annotations.ClassTemplate classTemplate = cls.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class );
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
		Class< ? > cls = this.classReflectionProxy.getCls();
		if( cls != null ) {
			return PackageDeclaredInJava.get( cls.getPackage() );
		} else {
			return null;
		}
	}
	@Override
	public AbstractType getSuperType() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		if( cls != null ) {
			return TypeDeclaredInJava.get( cls.getSuperclass() );
		} else {
			return null;
		}
	}
	@Override
	public java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors() {
		return this.constructors;
	}
	@Override
	public java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods() {
		return this.methods;
	}
	@Override
	public java.util.ArrayList< ? extends AbstractField > getDeclaredFields() {
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
				rv = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( src.getDeclaringClass(), name, dstParameterClses );
				if( rv.getReturnType() == srcReturnCls ) {
					//pass
				} else {
					rv = null;
				}
			} catch( RuntimeException re ) {
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
			if( edu.cmu.cs.dennisc.property.PropertyUtilities.isGetterAndSetterExists( mthd ) ) {
				java.lang.reflect.Method sttr = edu.cmu.cs.dennisc.property.PropertyUtilities.getSetterForGetter( mthd );
				this.fields.add( FieldDeclaredInJavaWithGetterAndSetter.get( mthd, sttr ) );
			} else if( edu.cmu.cs.dennisc.property.PropertyUtilities.isSetterAndGetterExists( mthd ) ) {
				//pass
			} else if( edu.cmu.cs.dennisc.property.PropertyUtilities.isSetterWithExtraParametersAndGetterExists( mthd ) ) {
				//pass
			} else {
				//				NodeListProperty nodeListProperty;
				//				if( mthd.getReturnType() == Void.TYPE ) {
				//					nodeListProperty = this.actions;
				//				} else {
				//					nodeListProperty = this.questions;
				//				}
				MethodDeclaredInJava methodDeclaredInJava = MethodDeclaredInJava.get( mthd );
				edu.cmu.cs.dennisc.alice.annotations.Visibility visibility = methodDeclaredInJava.getVisibility();

				if( visibility == edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME ) {
					MethodDeclaredInJava longer = methodDeclaredInJava;
					java.lang.reflect.Method _mthd = mthd;
					while( true ) {
						_mthd = getNextShorterInChain( _mthd );
						if( _mthd != null ) {
							MethodDeclaredInJava shorter = MethodDeclaredInJava.get( _mthd );
							if( shorter.getVisibility() == edu.cmu.cs.dennisc.alice.annotations.Visibility.CHAINED ) {
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
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "adding method:", methodDeclaredInJava );
				this.methods.add( methodDeclaredInJava );
				//				else if( visibility == edu.cmu.cs.dennisc.alice.annotations.Visibility.CHAINED ) {
				//				} else if( visibility == edu.cmu.cs.dennisc.alice.annotations.Visibility.COMPLETELY_HIDDEN ) {
				//				} else {
				//				}
			}
		}
	}
	
	public ClassReflectionProxy getClassReflectionProxy() {
		return this.classReflectionProxy;
	}

//	//todo: reduce visibility?
//	public Class< ? > getCls() {
//		return this.classReflectionProxy.getCls();
//	}
//	public String getClsName() {
//		return this.classReflectionProxy.getName();
//	}

	@Override
	public boolean isDeclaredInAlice() {
		return false;
	}

	@Override
	public Access getAccess() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return Access.get( cls.getModifiers() );
	}

	@Override
	public boolean isInterface() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return cls.isInterface();
	}

	@Override
	public boolean isStatic() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return java.lang.reflect.Modifier.isStatic( cls.getModifiers() );
	}
	@Override
	public boolean isAbstract() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return java.lang.reflect.Modifier.isAbstract( cls.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return java.lang.reflect.Modifier.isFinal( cls.getModifiers() );
	}
	@Override
	public boolean isStrictFloatingPoint() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return java.lang.reflect.Modifier.isStrict( cls.getModifiers() );
	}

	@Override
	public boolean isArray() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return cls.isArray();
	}
	@Override
	public AbstractType getComponentType() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return TypeDeclaredInJava.get( cls.getComponentType() );
	}

	@Override
	public AbstractType getArrayType() {
		Class< ? > cls = this.classReflectionProxy.getCls();
		assert cls != null;
		return TypeDeclaredInJava.get( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getArrayClass( cls ) );
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof TypeDeclaredInJava ) {
			return classReflectionProxy.equals( ((TypeDeclaredInJava)other).classReflectionProxy );
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return getClass() + "[cls=" + this.classReflectionProxy.getName() + "]";
	}
}
