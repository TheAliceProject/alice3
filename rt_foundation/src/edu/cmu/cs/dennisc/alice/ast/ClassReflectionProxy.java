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
public class ClassReflectionProxy extends ReflectionProxy< Class<?> > {
	public static ClassReflectionProxy[] create( Class<?>[] clses ) {
		ClassReflectionProxy[] rv = new ClassReflectionProxy[ clses.length ];
		for( int i = 0; i < clses.length; i++ ) {
			rv[ i ] = new ClassReflectionProxy( clses[ i ] );
		}
		return rv;
	}

	
	private String name;
	public ClassReflectionProxy( String name ) {
		this.name = name;
	}
	public ClassReflectionProxy( Class<?> cls ) {
		super( cls );
		this.name = cls.getName();
	}
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	@Override
	public boolean equals( Object o ) {
		ClassReflectionProxy other = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( o, ClassReflectionProxy.class );
		if( other != null ) {
			return this.name.equals( other.name );
		} else {
			return false;
		}
	}
	public String getName() {
		return this.name;
	}
	public String getSimpleName() {
		Class< ? > cls = this.getReification();
		if( cls != null ) {
			return cls.getSimpleName();
		} else {
			return this.name.substring( this.name.lastIndexOf( '.' )+1 );
		}
	}
	@Override
	protected Class<?> reify() {
		try {
			return edu.cmu.cs.dennisc.lang.ClassUtilities.forName( this.name );
		} catch( ClassNotFoundException cnfe ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: could not find class", this.name );
			return null;
		}
	}
	private static Class<?>[] getReifications( ClassReflectionProxy[] classReflectionProxies ) {
		Class<?>[] rv = new Class<?>[ classReflectionProxies.length ];
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = classReflectionProxies[ i ].getReification();
		}
		return rv;
	}

	
	
	/*package private*/ java.lang.reflect.Field getFld( String name ) {
		Class< ? > cls = this.getReification();
		if( cls != null ) {
			try {
				return cls.getField( name );
			} catch( NoSuchFieldException nsfe ) {
				return null;
			}
		} else {
			return null;
		}
	}
	/*package private*/ java.lang.reflect.Constructor< ? > getCnstrctr( ClassReflectionProxy[] parameterClassReflectionProxies ) {
		Class< ? > cls = this.getReification();
		if( cls != null ) {
			try {
				return cls.getConstructor( getReifications( parameterClassReflectionProxies ) );
			} catch( NoSuchMethodException nsme ) {
				return null;
			}
		} else {
			return null;
		}
	}
	/*package private*/ java.lang.reflect.Method getMthd( String name, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		Class< ? > cls = this.getReification();
		if( cls != null ) {
			try {
				return cls.getMethod( name, getReifications( parameterClassReflectionProxies ) );
			} catch( NoSuchMethodException nsme ) {
				return null;
			}
		} else {
			return null;
		}
	}
}
