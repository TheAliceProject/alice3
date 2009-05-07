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
public class IdentifierUtilities {
	public static boolean isValidIdentifier( String name ) {
		final int N = name.length();
		if( name != null && N > 0 ) {
			char name0 = name.charAt( 0 );
			if( Character.isLetter( name0 ) || name0 == '_' ) {
				for( int i=1; i<N; i++ ) {
					char c = name.charAt( i );
					if( Character.isLetterOrDigit( c ) || c == '_' ) {
						//pass
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private static String getConventionalIdentifierName( String name, boolean cap ) {	
		String rv = "";
		boolean isAlphaEncountered = false;
		final int N = name.length();
		for( int i=0; i<N; i++ ) {
			char c = name.charAt( i );
			if( Character.isLetterOrDigit( c ) ) {
				if( Character.isDigit( c ) ) {
					if( isAlphaEncountered ) {
						//pass
					} else {
						rv += "_";
						rv += c;
						isAlphaEncountered = true;
						continue;
				}
				} else {
					isAlphaEncountered = true;
				}
				if( cap ) {
					c = Character.toUpperCase( c );
				}
				rv += c;
				cap = Character.isDigit( c );
			} else {
				cap = true;
			}
		}
		return rv;
	}

	
	public static String getConventionalInstanceName( String text ) {
		return getConventionalIdentifierName( text, false );
	}

	public static String getConventionalClassName( String text ) {
		return getConventionalIdentifierName( text, true );
	}
}
