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
	public static Class<?>[] getReifications( ClassReflectionProxy[] classReflectionProxies ) {
		Class<?>[] rv = new Class<?>[ classReflectionProxies.length ];
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = classReflectionProxies[ i ].getReification();
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
			String[] simpleNames = edu.cmu.cs.dennisc.lang.ClassUtilities.getSimpleClassNames( this.name );
			return simpleNames[ simpleNames.length-1 ];
		}
	}
	public ClassReflectionProxy getDeclaringClassReflectionProxy() {
		Class< ? > cls = this.getReification();
		if( cls != null ) {
			Class<?> declaringCls = cls.getDeclaringClass();
			if( declaringCls != null ) {
				return new ClassReflectionProxy( declaringCls );
			} else {
				return null;
			}
		} else {
			int index = this.name.lastIndexOf( "$" );
			if( index != -1 ) {
				return new ClassReflectionProxy( this.name.substring( 0, index ) );
			} else {
				return null;
			}
		}
	}
	
	public PackageReflectionProxy getPackageReflectionProxy() {
		Class< ? > cls = this.getReification();
		if( cls != null ) {
			Package pckg = cls.getPackage();
			if( pckg != null ) {
				return new PackageReflectionProxy( pckg );
			} else {
				return null;
			}
		} else {
			String packageName = edu.cmu.cs.dennisc.lang.ClassUtilities.getPackageName( this.name );
			if( packageName != null ) {
				return new PackageReflectionProxy( packageName );
			} else {
				return null;
			}
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
}
