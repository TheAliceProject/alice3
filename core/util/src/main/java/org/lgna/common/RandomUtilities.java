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

package org.lgna.common;

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

	public static Integer nextIntegerFrom0ToNExclusive( Integer n ) {
		if( n <= 0 ) {
			throw new LgnaIllegalArgumentException( "Argument must be positive.  " + n + " is not greater than 0.", 0, n );
		}
		return s_random.nextInt( n );
	}

	public static Integer nextIntegerFromAToBExclusive( Integer a, Integer b ) {
		if( a >= b ) {
			throw new LgnaIllegalArgumentException( "First argument must be less than the second argument." + a + " is not less than " + b + ".", 0, a );
		}
		int n = b - a;
		return a + nextIntegerFrom0ToNExclusive( n );
	}

	public static Integer nextIntegerFromAToBInclusive( Integer a, Integer b ) {
		if( a > b ) {
			throw new LgnaIllegalArgumentException( "First argument must be less than or equal to the second argument.  " + a + " is not less than or equal to " + b + ".", 0, a );
		}
		return nextIntegerFromAToBExclusive( a, b + 1 );
	}

	public static boolean nextBoolean() {
		return s_random.nextBoolean();
	}

	public static Double nextDouble() {
		return s_random.nextDouble();
	}

	public static Double nextDoubleInRange( Number min, Number max ) {
		return min.doubleValue() + ( nextDouble() * ( max.doubleValue() - min.doubleValue() ) );
	}

	public static <E> E getRandomValueFrom( E[] array ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( array, 0 );
		if( array.length > 0 ) {
			return array[ getRandomIndex( array.length ) ];
		} else {
			//todo: throw Exception?
			return null;
		}
	}

	public static <E> E getRandomValueFrom( java.util.List<E> list ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( list, 0 );
		if( list.size() > 0 ) {
			return list.get( getRandomIndex( list.size() ) );
		} else {
			//todo: throw Exception?
			return null;
		}
	}

	public static <E extends Enum<? extends E>> E getRandomEnumConstant( Class<E> cls ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( cls, 0 );
		E[] enumConstants = cls.getEnumConstants();
		assert enumConstants.length > 0 : cls;
		int index = s_random.nextInt( enumConstants.length );
		return enumConstants[ index ];
	}
}
