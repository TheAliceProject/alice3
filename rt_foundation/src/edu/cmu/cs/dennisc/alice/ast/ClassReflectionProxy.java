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
public class ClassReflectionProxy extends ReflectionProxy {
	private String name;
	private Class< ? > cls;

	public ClassReflectionProxy( String name ) {
		this.name = name;
	}
	public ClassReflectionProxy( Class<?> cls ) {
		this.name = cls.getName();
		this.cls = cls;
		this.isAttempted = true;
	}
	@Override
	public boolean equals( Object other ) {
		if( other instanceof ClassReflectionProxy ) {
			return this.name.equals( ((ClassReflectionProxy)other).name );
		}
		return super.equals( other );
	}
	public String getName() {
		return this.name;
	}
	public String getSimpleName() {
		Class< ? > cls = this.getCls();
		if( cls != null ) {
			return cls.getSimpleName();
		} else {
			return this.name.substring( this.name.lastIndexOf( '.' )+1 );
		}
	}
	public Class< ? > getCls() {
		if( this.isAttempted ) {
			//pass
		} else {
			try {
				this.cls = edu.cmu.cs.dennisc.lang.ClassUtilities.forName( this.name );
			} catch( ClassNotFoundException cnfe ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: could not find class", this.name );
				this.cls = null;
			}
			this.isAttempted = true;
		}
		return this.cls;
	}
	
	private static Class<?>[] getClses( ClassReflectionProxy[] classReflectionProxies ) {
		Class<?>[] rv = new Class<?>[ classReflectionProxies.length ];
		for( int i=0; i<rv.length; i++ ) {
			rv[ i ] = classReflectionProxies[ i ].getCls();
		}
		return rv;
	}
	
	/*package private*/ java.lang.reflect.Field getFld( String name ) {
		Class< ? > cls = this.getCls();
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
		Class< ? > cls = this.getCls();
		if( cls != null ) {
			try {
				return cls.getConstructor( getClses( parameterClassReflectionProxies ) );
			} catch( NoSuchMethodException nsme ) {
				return null;
			}
		} else {
			return null;
		}
	}
	/*package private*/ java.lang.reflect.Method getMthd( String name, ClassReflectionProxy[] parameterClassReflectionProxies ) {
		Class< ? > cls = this.getCls();
		if( cls != null ) {
			try {
				return cls.getMethod( name, getClses( parameterClassReflectionProxies ) );
			} catch( NoSuchMethodException nsme ) {
				return null;
			}
		} else {
			return null;
		}
	}
}
