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
package edu.cmu.cs.dennisc.lang.reflect;

/**
 * @author Dennis Cosgrove
 */
public final class ReflectionUtilities {
	private static java.util.HashMap< String, Class< ? >> s_primativeTypeMap = new java.util.HashMap< String, Class< ? >>();
	static {
		s_primativeTypeMap.put( Void.TYPE.getName(), Void.TYPE );
		s_primativeTypeMap.put( Boolean.TYPE.getName(), Boolean.TYPE );
		s_primativeTypeMap.put( Byte.TYPE.getName(), Byte.TYPE );
		s_primativeTypeMap.put( Character.TYPE.getName(), Character.TYPE );
		s_primativeTypeMap.put( Short.TYPE.getName(), Short.TYPE );
		s_primativeTypeMap.put( Integer.TYPE.getName(), Integer.TYPE );
		s_primativeTypeMap.put( Long.TYPE.getName(), Long.TYPE );
		s_primativeTypeMap.put( Double.TYPE.getName(), Double.TYPE );
		s_primativeTypeMap.put( Float.TYPE.getName(), Float.TYPE );
	}
	private ReflectionUtilities() {
	}
	
	@Deprecated
	public static Class< ? > getClassForName( String className ) {
		assert className != null;
		assert className.length() > 0;
		try {
			return Class.forName( className );
		} catch( ClassNotFoundException cnfe ) {
			if( s_primativeTypeMap.containsKey( className ) ) {
				return s_primativeTypeMap.get( className );
			} else {
				throw new RuntimeException( className, cnfe );
			}
		}
	}
	
	
	public static Class<?> getArrayClass( Class<?> componentCls ) {
		Object array = java.lang.reflect.Array.newInstance( componentCls, 0 );
		return array.getClass();
	}
	public static Class<?> getArrayClass( Class<?> componentCls, int dimensionCount ) {
		assert dimensionCount > 0;
		Class<?> rv = componentCls;
		for( int i=0; i<dimensionCount; i++ ) {
			rv = getArrayClass( rv );
		}
		return rv;
	}
	
	public static <T> T newInstance( java.lang.reflect.Constructor< T > cnstrctr, Object... arguments ) {
		try {
			return cnstrctr.newInstance( arguments );
		} catch( java.lang.reflect.InvocationTargetException ite ) {
			throw new RuntimeException( cnstrctr.toString(), ite );
		} catch( InstantiationException ie ) {
			throw new RuntimeException( cnstrctr.toString(), ie );
		} catch( IllegalAccessException iae ) {
			throw new RuntimeException( cnstrctr.toString(), iae );
		}
	}
	public static <T> T newInstance( Class< T > cls, Class<?>[] parameterClses, Object... arguments ) {
		return newInstance( getConstructor( cls, parameterClses ), arguments );
	}
	public static <T> T newInstanceForArguments( Class< T > cls, Object... arguments ) {
		return newInstance( getConstructorForArguments( cls, arguments ), arguments );
	}
	public static <T> T newInstance( Class< T > cls ) {
		try {
			return cls.newInstance();
		} catch( InstantiationException ie ) {
			throw new RuntimeException( cls.getName(), ie );
		} catch( IllegalAccessException iae ) {
			throw new RuntimeException( cls.getName(), iae );
		}
	}
	public static Object newInstance( String className ) {
		return newInstance( getClassForName( className ) );
	}
	public static Object newArrayInstance( Class< ? > componentType, int length ) {
		try {
			return java.lang.reflect.Array.newInstance( componentType, length );
		} catch( NegativeArraySizeException nase ) {
			throw new RuntimeException( nase );
		}
	}
	public static Object newArrayInstance( String componentTypeName, int length ) {
		return newArrayInstance( getClassForName( componentTypeName ), length );
	}

	public static java.lang.reflect.Field getField( Class< ? > cls, String name ) {
		try {
			return cls.getField( name );
		} catch( NoSuchFieldException nsfe ) {
			throw new RuntimeException( nsfe );
		}
	}
	public static java.lang.reflect.Field getDeclaredField( Class< ? > cls, String name ) {
		try {
			return cls.getDeclaredField( name );
		} catch( NoSuchFieldException nsfe ) {
			throw new RuntimeException( nsfe );
		}
	}
	public static java.lang.reflect.Method getMethod( Class< ? > cls, String name, Class< ? >... parameterTypes ) {
		try {
			return cls.getMethod( name, parameterTypes );
		} catch( NoSuchMethodException nsme ) {
			throw new RuntimeException( nsme );
		}
	}
	public static java.lang.reflect.Method getDeclaredMethod( Class< ? > cls, String name, Class< ? >... parameterTypes ) {
		try {
			return cls.getDeclaredMethod( name, parameterTypes );
		} catch( NoSuchMethodException nsme ) {
			throw new RuntimeException( nsme );
		}
	}
	public static <T> java.lang.reflect.Constructor< T > getConstructor( Class< T > cls, Class<?>... parameterClses ) {
		try {
			return cls.getConstructor( parameterClses );
		} catch( NoSuchMethodException nsme ) {
			throw new RuntimeException( cls.getName(), nsme );
		}
	}
	public static <T> java.lang.reflect.Constructor< T > getDeclaredConstructor( Class< T > cls, Class<?>... parameterClses ) {
		try {
			return cls.getDeclaredConstructor( parameterClses );
		} catch( NoSuchMethodException nsme ) {
			throw new RuntimeException( cls.getName(), nsme );
		}
	}
	
	private static <T> java.lang.reflect.Constructor< T > getConstructorForArguments( java.lang.reflect.Constructor< T >[] constructors, Object... arguments ) throws NoSuchMethodException {
		java.lang.reflect.Constructor< T > rv = null;
		for( java.lang.reflect.Constructor< T > constructor : constructors ) {
			Class< ? >[] parameterClses = constructor.getParameterTypes();
			if( parameterClses.length == arguments.length ) {
				if( rv != null ) {
					throw new RuntimeException( "more than one constructor matches arguments" );
				} else {
					rv = constructor;
				}
			}
		}
		if( rv != null ) {
			return rv;
		} else {
			throw new NoSuchMethodException();
		}
	}
	public static <T> java.lang.reflect.Constructor< T > getConstructorForArguments( Class< T > cls, Object... arguments ) {
		try {
			return getConstructorForArguments( (java.lang.reflect.Constructor< T >[])cls.getConstructors(), arguments );
		} catch( NoSuchMethodException nsme ) {
			throw new RuntimeException( cls.getName(), nsme );
		}
	}
	public static <T> java.lang.reflect.Constructor< T > getDeclaredConstructorForArguments( Class< T > cls, Object... arguments ) {
		try {
			return getConstructorForArguments( (java.lang.reflect.Constructor< T >[])cls.getDeclaredConstructors(), arguments );
		} catch( NoSuchMethodException nsme ) {
			throw new RuntimeException( cls.getName(), nsme );
		}
	}

	public static String getDetail( Object instance, java.lang.reflect.Method method, Object[] args ) {
		StringBuffer sb = new StringBuffer();
		sb.append( "instance=" );
		sb.append( instance );
		sb.append( ";method=" );
		if( method != null ) {
			sb.append( method.getName() );
		} else {
			sb.append( "null" );
		}
		sb.append( ";args={" );
		String separator = " ";
		for( Object arg : args ) {
			sb.append( separator );
			sb.append( arg );
			separator = ", ";
		}
		sb.append( " }" );
		return sb.toString();
	}
	public static Object invoke( Object instance, java.lang.reflect.Method method, Object... args ) {
		try {
			return method.invoke( instance, args );
		} catch( IllegalAccessException iae ) {
			throw new RuntimeException( getDetail( instance, method, args ), iae );
		} catch( java.lang.reflect.InvocationTargetException ite ) {
			throw new RuntimeException( getDetail( instance, method, args ), ite );
		} catch( RuntimeException re ) {
			throw new RuntimeException( getDetail( instance, method, args ), re );
		}
	}

	public static Object get( java.lang.reflect.Field field, Object o ) {
		try {
			return field.get( o );
		} catch( IllegalArgumentException iae ) {
			throw new RuntimeException( iae );
		} catch( IllegalAccessException iae ) {
			throw new RuntimeException( iae );
		}
	}
	public static void set( java.lang.reflect.Field field, Object o, Object value ) {
		try {
			field.set( o, value );
		} catch( IllegalArgumentException iae ) {
			throw new RuntimeException( iae );
		} catch( IllegalAccessException iae ) {
			throw new RuntimeException( iae );
		}
	}

	public static Iterable< java.lang.reflect.Field > getPublicStaticFinalFields( Class< ? > cls, Class< ? > clsAssignable ) {
		java.util.List< java.lang.reflect.Field > rv = new java.util.LinkedList< java.lang.reflect.Field >();
		int modifierMask = java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.STATIC | java.lang.reflect.Modifier.FINAL;
		java.lang.reflect.Field[] fields = cls.getFields();
		for( java.lang.reflect.Field field : fields ) {
			if( clsAssignable.isAssignableFrom( field.getType() ) ) {
				if( (field.getModifiers() & modifierMask) == modifierMask ) {
					rv.add( field );
				}
			}
		}
		return rv;
	}

	public static <E extends Object> Iterable< E > getPublicStaticFinalInstances( Class< ? > cls, Class< E > clsAssignable ) {
		java.util.List< E > rv = new java.util.LinkedList< E >();
		int modifierMask = java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.STATIC | java.lang.reflect.Modifier.FINAL;
		java.lang.reflect.Field[] fields = cls.getFields();
		for( java.lang.reflect.Field field : fields ) {
			if( clsAssignable.isAssignableFrom( field.getType() ) ) {
				if( (field.getModifiers() & modifierMask) == modifierMask ) {
					E e = (E)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, null );
					rv.add( e );
				}
			}
		}
		return rv;
	}

	public static <T> T valueOf( Class< T > cls, String s ) {
		java.lang.reflect.Method mthd = getMethod( cls, "valueOf", new Class[] { String.class } );
		return (T)invoke( null, mthd, new Object[] { s } );
	}
}
