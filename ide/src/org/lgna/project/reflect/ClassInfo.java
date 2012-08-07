/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.lgna.project.reflect;

/**
 * @author Dennis Cosgrove
 */
public class ClassInfo implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private transient boolean isGetClassForNameAlreadyAttempted = false;
	private transient Class<?> cls;
	private final String clsName;

	private final java.util.List< ConstructorInfo > constructorInfos = new java.util.LinkedList< ConstructorInfo >();
	private final java.util.List< MethodInfo > methodInfos = new java.util.LinkedList< MethodInfo >();
	
	private static java.util.Map< String, ClassInfo > map = new java.util.HashMap< String, ClassInfo >();
	public static ClassInfo forName( String clsName ) {
		ClassInfo rv = map.get( clsName );
		if( rv != null ) {
			//pass
		} else {
			rv = new ClassInfo( clsName );
		}
		return rv;
	}
	private ClassInfo( String clsName ) {
		this.clsName = clsName;
	}
	public ClassInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.clsName = binaryDecoder.decodeString();
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( this.constructorInfos, binaryDecoder.decodeBinaryEncodableAndDecodableArray( ConstructorInfo.class ) );
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( this.methodInfos, binaryDecoder.decodeBinaryEncodableAndDecodableArray( MethodInfo.class ) );
	}
	
	public String getClsName() {
		return this.clsName;
	}
	
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.clsName );
		binaryEncoder.encode( edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.constructorInfos, ConstructorInfo.class ) );
		binaryEncoder.encode( edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( this.methodInfos, MethodInfo.class ) );
	}
	/*package-private*/ Class<?> getCls() {
		if( this.isGetClassForNameAlreadyAttempted ) {
			//pass
		} else {
			this.isGetClassForNameAlreadyAttempted = true;
//			if( this.cls != null ) {
//				//pass
//			} else {
				try {
					this.cls = Class.forName( this.clsName );
					assert this.cls != null : this.clsName;
				} catch( Throwable t ) {
					System.err.println( "getCls" + t + " " + this.clsName );
				}
//			}
		}
		return this.cls;
	}
	public void addConstructorInfo( String[] parameterClassNames, String[] parameterNames ) {
		ConstructorInfo constructorInfo = new ConstructorInfo( this, parameterClassNames, parameterNames );
		this.constructorInfos.add( constructorInfo );
	}
	public void addMethodInfo( String name, String[] parameterClassNames, String[] parameterNames ) {
		MethodInfo methodInfo = new MethodInfo( this, name, parameterClassNames, parameterNames );
		this.methodInfos.add( methodInfo );
	}

	public java.util.List< ConstructorInfo > getConstructorInfos() {
		return java.util.Collections.unmodifiableList( this.constructorInfos );
	}
	public java.util.List< MethodInfo > getMethodInfos() {
		return java.util.Collections.unmodifiableList( this.methodInfos );
	}
	
	private java.util.Set< MethodInfo > outOfDateMethodInfos = new java.util.HashSet< MethodInfo >();
	public MethodInfo lookupInfo( java.lang.reflect.Method mthd ) {
		for( MethodInfo methodInfo : getMethodInfos() ) {
			if( this.outOfDateMethodInfos.contains( methodInfo ) ) {
				//pass
			} else {
				try {
					java.lang.reflect.Method m = methodInfo.getMthd();
					if( m.equals( mthd ) ) {
						return methodInfo;
					}
				} catch( RuntimeException re ) {
					//re.printStackTrace();
					this.outOfDateMethodInfos.add( methodInfo ); 
				}
			}
		}
		return null;
	}
	public ConstructorInfo lookupInfo( java.lang.reflect.Constructor<?> cnstrctr ) {
		for( ConstructorInfo constructorInfo : getConstructorInfos() ) {
			if( constructorInfo.getCnstrctr().equals( cnstrctr ) ) {
				return constructorInfo;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[name=" );
		sb.append( this.getClsName() );
		sb.append( "]" );
		for( MethodInfo methodInfo : this.methodInfos ) {
			sb.append( "\n\t" );
			sb.append( methodInfo );
		}
		return sb.toString();
	}
}
