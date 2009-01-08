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

package edu.cmu.cs.dennisc.random;

/**
 * @author Dennis Cosgrove
 */
public class RandomUtilities {
	private static java.util.Random s_random = new java.util.Random();

	private static int getRandomIndex( int n ) {
		return s_random.nextInt( n );
	}
	public static void setSeed( long seed ) {
		s_random.setSeed( seed );
	}
	public static int nextInt( int n ) {
		return s_random.nextInt( n );
	}
	public static boolean nextBoolean() {
		//return nextInt( 2 ) == 1;
		return s_random.nextBoolean();
	}

	public static double nextDouble() {
		return s_random.nextDouble();
	}
	public static double nextDoubleInRange( double min, double max ) {
		return min + (nextDouble() * (max - min));
	}

	public static <E extends Object> E getRandomValueFrom( E[] array ) {
		assert array != null;
		assert array.length > 0;
		return array[ getRandomIndex( array.length ) ];
	}
	public static <E extends Object> E getRandomValueFrom( java.util.List< E > list ) {
		assert list != null;
		if( list.size() > 0 ) {
			return list.get( getRandomIndex( list.size() ) );
		} else {
			return null;
		}
	}

	public static <E extends Enum< ? >> E getRandomEnumConstant( Class< E > cls ) {
		E[] enumConstants = cls.getEnumConstants();
		assert enumConstants.length > 0 : cls;
		int index = s_random.nextInt( enumConstants.length );
		return enumConstants[ index ];
	}
}
