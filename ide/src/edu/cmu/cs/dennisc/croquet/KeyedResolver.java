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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class KeyedResolver<T> implements CodableResolver< T > {
	private T instance;
	public KeyedResolver( T instance ) {
		this.instance = instance;
	}
	public KeyedResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.decode( binaryDecoder );
	}

	protected T getInstance() {
		return this.instance;
	}
	
	private Class<T> instanceCls;
	private java.lang.Class<?>[] parameterTypes;
	private Object[] arguments;
	
	protected Object[] getArguments() {
		return this.arguments;
	}
	
	protected abstract T resolve( Class<T> instanceCls, Class<?>[] parameterTypes, Object[] arguments );
	public final T getResolved() {
		if( this.instance != null ) {
			return this.instance;
		} else {
			return this.resolve( this.instanceCls, this.parameterTypes, this.arguments );
		}
	}
	protected final void handleDecoded(Class<T> instanceCls, java.lang.Class<?>[] parameterTypes, Object[] arguments) {
		this.instanceCls = instanceCls;
		this.parameterTypes = parameterTypes;
		this.arguments = arguments;
	}
	
	protected final void encodeClass( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Class<?> cls ) {
		binaryEncoder.encode( cls.getName() );
	}
	protected final Class<?> decodeClass( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) { 
		String clsName = binaryDecoder.decodeString();
		try {
			return edu.cmu.cs.dennisc.java.lang.ClassUtilities.forName( clsName );
		} catch( ClassNotFoundException cnfe ) {
			throw new RuntimeException( clsName, cnfe );
		}
	}
	
	protected Class<T> decodeInstanceClass( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		return (Class<T>)decodeClass( binaryDecoder );
	}
	protected abstract Class<?>[] decodeParameterTypes( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	protected abstract Object[] decodeArguments( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	protected void encodeInstanceClass( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Class<T> cls ) {
		this.encodeClass( binaryEncoder, cls );
	}
	protected abstract void encodeParameterTypes( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder );
	protected abstract void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder );
	public final void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		Class<T> instanceCls = this.decodeInstanceClass( binaryDecoder );
		Class<?>[] parameterTypes = this.decodeParameterTypes( binaryDecoder );
		Object[] arguments = this.decodeArguments( binaryDecoder );
		this.handleDecoded( instanceCls, parameterTypes, arguments );
	}
	public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		this.encodeInstanceClass( binaryEncoder, (Class<T>)this.instance.getClass() );
		this.encodeParameterTypes( binaryEncoder );
		this.encodeArguments( binaryEncoder );
	}
}
