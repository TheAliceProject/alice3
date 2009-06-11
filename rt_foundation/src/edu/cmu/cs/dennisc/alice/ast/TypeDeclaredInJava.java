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
	private static java.util.Map< Class<?>, TypeDeclaredInJava > s_map;
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

	public static TypeDeclaredInJava get( Class< ? > cls ) {
		if( cls != null ) {
			if( s_map != null ) {
				//pass
			} else {
				s_map = new java.util.HashMap< Class<?>, TypeDeclaredInJava >();
			}
			TypeDeclaredInJava rv = s_map.get( cls );
			if( rv != null ) {
				//pass
			} else {
				rv = new TypeDeclaredInJava( cls );
			}
			return rv;
		} else {
			return null;
		}
	}
	public static TypeDeclaredInJava[] get( Class< ? >[] clses ) {
		TypeDeclaredInJava[] rv = new TypeDeclaredInJava[ clses.length ];
		for( int i=0; i<clses.length; i++ ) {
			rv[ i ] = get( clses[ i ] );
		}
		return rv;
	}
	public static MethodDeclaredInJava getMethod( java.lang.reflect.Method mthd ) {
		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( mthd.getDeclaringClass() );
		for( AbstractMethod method : typeDeclaredInJava.m_methods ) {
			if( method.getName().equals( mthd.getName() ) ) {
				MethodDeclaredInJava methodDeclaredInJava = (MethodDeclaredInJava)method;
				MethodDeclaredInJava m = methodDeclaredInJava;
				while( m != null ) {
					if( mthd.equals( m.getMthd() ) ) {
						return m;
					}
					m = (MethodDeclaredInJava)m.getNextShorterInChain();
				}
			}
		}
		//assert false : mthd;
		//return null;
		return new MethodDeclaredInJava( mthd );
	}
	public static ConstructorDeclaredInJava getConstructor( java.lang.reflect.Constructor<?> cnstrctr ) {
		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( cnstrctr.getDeclaringClass() );
		for( AbstractConstructor constructor : typeDeclaredInJava.m_constructors ) {
			ConstructorDeclaredInJava constructorDeclaredInJava = (ConstructorDeclaredInJava)constructor;
			if( cnstrctr.equals( constructorDeclaredInJava.getCnstrctr() ) ) {
				return constructorDeclaredInJava;
			}
		}
		assert false : cnstrctr;
		return null;
	}
	public static FieldDeclaredInJavaWithField getField( java.lang.reflect.Field fld ) {
		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( fld.getDeclaringClass() );
		for( AbstractField field : typeDeclaredInJava.m_fields ) {
			if( field instanceof FieldDeclaredInJavaWithField ) {
				FieldDeclaredInJavaWithField fieldDeclaredInJavaWithField = (FieldDeclaredInJavaWithField)field;
				if( fld.equals( fieldDeclaredInJavaWithField.getFld() ) ) {
					return fieldDeclaredInJavaWithField;
				}
			}
		}
		assert false : fld;
		return null;
	}
	public static FieldDeclaredInJavaWithGetterAndSetter getField( java.lang.reflect.Method gttr, java.lang.reflect.Method sttr ) {
		TypeDeclaredInJava typeDeclaredInJava = TypeDeclaredInJava.get( gttr.getDeclaringClass() );
		for( AbstractField field : typeDeclaredInJava.m_fields ) {
			if( field instanceof FieldDeclaredInJavaWithGetterAndSetter ) {
				FieldDeclaredInJavaWithGetterAndSetter fieldDeclaredInJavaWithGetterAndSetter = (FieldDeclaredInJavaWithGetterAndSetter)field;
				if( gttr.equals( fieldDeclaredInJavaWithGetterAndSetter.getGttr() ) ) {
					assert sttr.equals( fieldDeclaredInJavaWithGetterAndSetter.getSttr() ) : sttr;
					return fieldDeclaredInJavaWithGetterAndSetter;
				}
			}
		}
		assert false : gttr;
		return null;
	}

	private static boolean isMask( int modifiers, int required ) {
		return (modifiers & required) != 0;
	}
	private static boolean isNotMask( int modifiers, int prohibited ) {
		return (modifiers & prohibited) == 0;
	}

	private Class< ? > m_cls;
	private java.util.ArrayList< ConstructorDeclaredInJava > m_constructors = new java.util.ArrayList< ConstructorDeclaredInJava >();
	private java.util.ArrayList< MethodDeclaredInJava > m_methods = new java.util.ArrayList< MethodDeclaredInJava >();
	private java.util.ArrayList< FieldDeclaredInJava > m_fields = new java.util.ArrayList< FieldDeclaredInJava >();

	private TypeDeclaredInJava( Class<?> cls ) {
		m_cls = cls;
		s_map.put( cls, this );

		for( java.lang.reflect.Constructor<?> cnstrctr : cls.getDeclaredConstructors() ) {
			m_constructors.add( new ConstructorDeclaredInJava( cnstrctr ) );
		}

		for( java.lang.reflect.Field fld : cls.getDeclaredFields() ) {
//			if( fld.isEnumConstant() ) {
//				m_constants.add( new ConstantDeclaredInJava( fld ) );
//			} else {
//				int modifiers = fld.getModifiers();
//				if( isMask( modifiers, java.lang.reflect.Modifier.PUBLIC ) ) {
//					if( isMask( modifiers, java.lang.reflect.Modifier.STATIC ) ) {
//						if( isMask( modifiers, java.lang.reflect.Modifier.FINAL ) ) {
//							m_constants.add( new ConstantDeclaredInJava( fld ) );
//						}
//					} else {
						m_fields.add( new FieldDeclaredInJavaWithField( fld ) );
//					}
//				}
//			}
		}

		java.util.Set< java.lang.reflect.Method > set = null;
		Iterable< edu.cmu.cs.dennisc.alice.reflect.MethodInfo > methodInfos = edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.getMethodInfos( cls );
		if( methodInfos != null ) {
			set = new java.util.HashSet< java.lang.reflect.Method >();
			for( edu.cmu.cs.dennisc.alice.reflect.MethodInfo methodInfo : methodInfos ) {
				try {
					java.lang.reflect.Method mthd = methodInfo.getMthd();
					if( mthd != null ) {
						handleMthd( mthd );
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
				handleMthd( mthd );
			}
		}
		
//		if( m_cls.getSimpleName().equals( "AbstractTransformable" ) ) {
//			for( AbstractMethod method : m_methods ) {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( ((MethodDeclaredInJava)method).getMthd() );
//			}
//		}
	}

	@Override
	public boolean isFollowToSuperClassDesired() {
		if( m_cls.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class ) ) {
			edu.cmu.cs.dennisc.alice.annotations.ClassTemplate classTemplate = m_cls.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class );
			return classTemplate.isFollowToSuperClassDesired();
		} else {
			return true;
		}
	}	
	@Override
	public boolean isConsumptionBySubClassDesired() {
		if( m_cls.isAnnotationPresent( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class ) ) {
			edu.cmu.cs.dennisc.alice.annotations.ClassTemplate classTemplate = m_cls.getAnnotation( edu.cmu.cs.dennisc.alice.annotations.ClassTemplate.class );
			return classTemplate.isConsumptionBySubClassDesired();
		} else {
			return false;
		}
	}
	
	@Override
	public String getName() {
		return m_cls.getSimpleName();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		return null;
	}
	@Override
	public AbstractPackage getPackage() {
		return PackageDeclaredInJava.get( m_cls.getPackage() );
	}
	@Override
	public AbstractType getSuperType() {
		return TypeDeclaredInJava.get( m_cls.getSuperclass() );
	}
	@Override
	public java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors() {
		return m_constructors;
	}
	@Override
	public java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods() {
		return m_methods;
	}
	@Override
	public java.util.ArrayList< ? extends AbstractField > getDeclaredFields() {
		return m_fields;
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
		if( isMask( modifiers, java.lang.reflect.Modifier.PUBLIC ) /*&& isNotMask( modifiers, java.lang.reflect.Modifier.STATIC )*/ ) {
			if( edu.cmu.cs.dennisc.property.PropertyUtilities.isGetterAndSetterExists( mthd ) ) {
				java.lang.reflect.Method sttr = edu.cmu.cs.dennisc.property.PropertyUtilities.getSetterForGetter( mthd );
				java.lang.annotation.Annotation[][] parameterAnnotations = sttr.getParameterAnnotations();
				m_fields.add( new FieldDeclaredInJavaWithGetterAndSetter( mthd, sttr, parameterAnnotations[ 0 ] ) );
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
				MethodDeclaredInJava methodDeclaredInJava = new MethodDeclaredInJava( mthd );
				edu.cmu.cs.dennisc.alice.annotations.Visibility visibility = methodDeclaredInJava.getVisibility();

				if( visibility == edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME ) {
					MethodDeclaredInJava longer = methodDeclaredInJava;
					java.lang.reflect.Method _mthd = mthd;
					while( true ) {
						_mthd = getNextShorterInChain( _mthd );
						if( _mthd != null ) {
							MethodDeclaredInJava shorter = TypeDeclaredInJava.getMethod( _mthd );
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
				m_methods.add( methodDeclaredInJava );
//				else if( visibility == edu.cmu.cs.dennisc.alice.annotations.Visibility.CHAINED ) {
//				} else if( visibility == edu.cmu.cs.dennisc.alice.annotations.Visibility.COMPLETELY_HIDDEN ) {
//				} else {
//				}
			}
		}
	}

	
	//todo: reduce visibility?
	public Class< ? > getCls() {
		return m_cls;
	}
	@Override
	public boolean isDeclaredInAlice() {
		return false;
	}

	@Override
	public Access getAccess() {
		return Access.get( m_cls.getModifiers() );
	}	
	
	@Override
	public boolean isInterface() {
		return m_cls.isInterface();
	}
	
	@Override
	public boolean isStatic() {
		return java.lang.reflect.Modifier.isStatic( m_cls.getModifiers() );
	}
	@Override
	public boolean isAbstract() {
		return java.lang.reflect.Modifier.isAbstract( m_cls.getModifiers() );
	}
	@Override
	public boolean isFinal() {
		return java.lang.reflect.Modifier.isFinal( m_cls.getModifiers() );
	}
	@Override
	public boolean isStrictFloatingPoint() {
		return java.lang.reflect.Modifier.isStrict( m_cls.getModifiers() );
	}

	@Override
	public boolean isArray() {
		return m_cls.isArray();
	}
	@Override
	public AbstractType getComponentType() {
		return TypeDeclaredInJava.get( m_cls.getComponentType() );
	}
	
	@Override
	public AbstractType getArrayType() {
		return TypeDeclaredInJava.get( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getArrayClass( m_cls ) );
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof TypeDeclaredInJava ) {
			return m_cls.equals( ((TypeDeclaredInJava)other).m_cls );
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return getClass() + "[cls=" + m_cls + "]";
	}
}
