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

package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
public final class ClassReflectionProxy extends ReflectionProxy<Class<?>> {
	public static ClassReflectionProxy[] create( Class<?>[] clses ) {
		ClassReflectionProxy[] rv = new ClassReflectionProxy[ clses.length ];
		for( int i = 0; i < clses.length; i++ ) {
			rv[ i ] = new ClassReflectionProxy( clses[ i ] );
		}
		return rv;
	}

	public static Class<?>[] getReifications( ClassReflectionProxy[] classReflectionProxies ) {
		Class<?>[] rv = new Class<?>[ classReflectionProxies.length ];
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = classReflectionProxies[ i ].getReification();
		}
		return rv;
	}

	private final String name;

	public ClassReflectionProxy( String name ) {
		this.name = name;
	}

	public ClassReflectionProxy( Class<?> cls ) {
		super( cls );
		this.name = cls.getName();
	}

	@Override
	protected int hashCodeNonReifiable() {
		return this.name.hashCode();
	}

	@Override
	protected boolean equalsInstanceOfSameClassButNonReifiable( ReflectionProxy<?> o ) {
		ClassReflectionProxy other = (ClassReflectionProxy)o;
		return this.name != null ? this.name.equals( other.name ) : other.name == null;
	}

	public String getName() {
		return this.name;
	}

	public String getSimpleName() {
		Class<?> cls = this.getReification();
		if( cls != null ) {
			return cls.getSimpleName();
		} else {
			String[] simpleNames = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getSimpleClassNames( this.name );
			return simpleNames[ simpleNames.length - 1 ];
		}
	}

	public boolean isArray() {
		Class<?> cls = this.getReification();
		if( cls != null ) {
			return cls.isArray();
		} else {
			return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getArrayDimensionCount( this.name ) > 0;
		}
	}

	public ClassReflectionProxy getComponentClassReflectionProxy() {
		Class<?> cls = this.getReification();
		if( cls != null ) {
			Class<?> componentCls = cls.getComponentType();
			if( componentCls != null ) {
				return new ClassReflectionProxy( componentCls );
			} else {
				return null;
			}
		} else {
			if( this.name.charAt( 0 ) == '[' ) {
				String s = this.name.substring( 1 );
				if( s.charAt( 0 ) == '[' ) {
					//pass
				} else {
					assert s.charAt( 0 ) == 'L';
					assert s.charAt( s.length() - 1 ) == ';';
					s = s.substring( 1, s.length() - 1 );
				}
				return new ClassReflectionProxy( s );
			} else {
				return null;
			}
		}
	}

	public ClassReflectionProxy getDeclaringClassReflectionProxy() {
		Class<?> cls = this.getReification();
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
		Class<?> cls = this.getReification();
		if( cls != null ) {
			Package pckg = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getPackage( cls );
			if( pckg != null ) {
				return new PackageReflectionProxy( pckg );
			} else {
				return null;
			}
		} else {
			String packageName = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getPackageName( this.name );
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
			return edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( this.name );
		} catch( ClassNotFoundException cnfe ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "could not find class", this.name );
			return null;
		}
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		sb.append( "name=" );
		sb.append( this.name );
	}
}
