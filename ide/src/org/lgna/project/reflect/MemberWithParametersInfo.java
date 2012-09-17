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

import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberWithParametersInfo extends MemberInfo {
	private transient Class<?>[] parameterClses;
	private final String[] parameterClassNames;
	private final String[] parameterNames;

	public MemberWithParametersInfo( ClassInfo declaringClassInfo, String[] parameterClassNames, String[] parameterNames ) {
		super( declaringClassInfo );
		this.parameterClassNames = parameterClassNames;
		this.parameterNames = parameterNames;
	}

	public MemberWithParametersInfo( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.parameterClassNames = binaryDecoder.decodeStringArray();
		this.parameterNames = binaryDecoder.decodeStringArray();
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.parameterClassNames );
		binaryEncoder.encode( this.parameterNames );
	}

	public Class<?>[] getParameterClses() {
		if( this.parameterClses != null ) {
			//pass
		} else {
			this.parameterClses = new Class<?>[ this.parameterClassNames.length ];
			int i = 0;
			for( String name : this.parameterClassNames ) {
				//				boolean isArray = name.startsWith( "L[" ) && name.endsWith( ";" );
				//				if( isArray ) {
				//					name = name.substring( 2, name.length()-1 );
				//				}
				boolean isArray = name.endsWith( "[]" );
				if( isArray ) {
					name = name.substring( 0, name.length() - 2 );
				}
				this.parameterClses[ i ] = ReflectionUtilities.getClassForName( name );
				if( isArray ) {
					this.parameterClses[ i ] = ReflectionUtilities.getArrayClass( this.parameterClses[ i ] );
				}
				if( this.parameterClses[ i ] != null ) {
					//pass
				} else {
					throw new NullPointerException( name );
				}
				i++;
			}
		}
		return this.parameterClses;
	}

	public String[] getParameterNames() {
		return this.parameterNames;
	}

	protected abstract void appendRepr( StringBuilder sb );

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( getClass().getSimpleName() );
		sb.append( "[" );
		this.appendRepr( sb );
		sb.append( "]" );
		for( int i = 0; i < this.parameterClassNames.length; i++ ) {
			sb.append( "\t\t" );
			sb.append( this.parameterClassNames[ i ] );
			sb.append( " " );
			sb.append( this.parameterNames[ i ] );
		}
		return sb.toString();
	}
}
