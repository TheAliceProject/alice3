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

package edu.cmu.cs.dennisc.lang;

/**
 * @author Dennis Cosgrove
 */
public class ClassUtilities {
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
	public static <E> E getInstance( Object o, Class<E> cls ) {
		E rv = null;
		if( o != null ) {
			if( cls.isAssignableFrom( o.getClass() ) ) {
				rv = (E)o;
			}
		}
		return rv;
	}

	public static Class<?> forName( String className ) throws ClassNotFoundException {
		assert className != null;
		assert className.length() > 0;
		try {
			return Class.forName( className );
		} catch( ClassNotFoundException cnfe ) {
			if( s_primativeTypeMap.containsKey( className ) ) {
				return s_primativeTypeMap.get( className );
			} else {
				throw cnfe;
			}
		}
	}
	
	public static boolean isAssignableToAtLeastOne( Class<?> right, Class<?>... lefts ) {
		for( Class<?> left : lefts ) {
			if( left.isAssignableFrom( right ) ) {
				return true;
			}
		}
		return false;
	}
}
