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

package edu.cmu.cs.dennisc.java.lang;

/**
 * @author Dennis Cosgrove
 */
public class ClassUtilities {
	private static final java.util.Map<String, Class<?>> s_primativeTypeMap;
	static {
		java.util.Map<String, Class<?>> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		map.put( Void.TYPE.getName(), Void.TYPE );
		map.put( Boolean.TYPE.getName(), Boolean.TYPE );
		map.put( Byte.TYPE.getName(), Byte.TYPE );
		map.put( Character.TYPE.getName(), Character.TYPE );
		map.put( Short.TYPE.getName(), Short.TYPE );
		map.put( Integer.TYPE.getName(), Integer.TYPE );
		map.put( Long.TYPE.getName(), Long.TYPE );
		map.put( Double.TYPE.getName(), Double.TYPE );
		map.put( Float.TYPE.getName(), Float.TYPE );
		s_primativeTypeMap = java.util.Collections.unmodifiableMap( map );
	}

	public static <E> E getInstance( Object o, Class<E> cls ) {
		E rv = null;
		if( o != null ) {
			if( cls.isAssignableFrom( o.getClass() ) ) {
				rv = cls.cast( o );
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

	public static int getArrayDimensionCount( String packageNameAndSimpleClassNames ) {
		final int N = packageNameAndSimpleClassNames.length();
		int i;
		for( i = 0; i < N; i++ ) {
			char c = packageNameAndSimpleClassNames.charAt( i );
			if( c == '[' ) {
				//pass
			} else {
				break;
			}
		}
		return i;
	}

	public static String getPackageName( String packageNameAndSimpleClassNames ) {
		int index = packageNameAndSimpleClassNames.lastIndexOf( '.' );
		if( index != -1 ) {
			int beginIndex = getArrayDimensionCount( packageNameAndSimpleClassNames );
			if( beginIndex > 0 ) {
				assert packageNameAndSimpleClassNames.charAt( beginIndex ) == 'L';
				beginIndex += 1;
			}
			return packageNameAndSimpleClassNames.substring( beginIndex, index );
		} else {
			return null;
		}
	}

	public static String[] getSimpleClassNames( String packageNameAndSimpleClassNames ) {
		int index = packageNameAndSimpleClassNames.lastIndexOf( '.' );
		int n = packageNameAndSimpleClassNames.length();
		if( packageNameAndSimpleClassNames.charAt( n - 1 ) == ';' ) {
			n -= 1;
		}
		String simpleClassNames = packageNameAndSimpleClassNames.substring( index + 1, n );
		return simpleClassNames.split( "\\$" );
	}

	public static Package getPackage( Class<?> cls ) {
		if( cls.isArray() ) {
			return getPackage( cls.getComponentType() );
		} else {
			return cls.getPackage();
		}
	}

	public static String getTrimmedClassName( Class<?> cls ) {
		if( cls != null ) {
			if( cls.isMemberClass() ) {
				Package pckg = getPackage( cls );
				return cls.getName().substring( pckg.getName().length() + 1 );
			} else {
				return cls.getSimpleName();
			}
		} else {
			return null;
		}
	}

	public static String getTrimmedClassNameForInstance( Object instance ) {
		if( instance != null ) {
			return getTrimmedClassName( instance.getClass() );
		} else {
			return null;
		}
	}
}
