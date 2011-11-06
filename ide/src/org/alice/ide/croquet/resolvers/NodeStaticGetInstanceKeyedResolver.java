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

package org.alice.ide.croquet.resolvers;

/**
 * @author Dennis Cosgrove
 */
public class NodeStaticGetInstanceKeyedResolver<T> extends org.lgna.croquet.resolvers.StaticGetInstanceKeyedResolver< T > implements org.lgna.croquet.resolvers.RetargetableResolver< T > {
	private org.lgna.project.ast.Node nodes[];
	private Class< ? extends org.lgna.project.ast.Node > parameterTypes[];
	
	public NodeStaticGetInstanceKeyedResolver( T instance, org.lgna.project.ast.Node[] nodes, Class< ? extends org.lgna.project.ast.Node >[] parameterTypes ) {
		super( instance );
		assert nodes.length > 0;
		assert nodes.length == parameterTypes.length;
		this.nodes = nodes;
		this.parameterTypes = parameterTypes;
	}
	public NodeStaticGetInstanceKeyedResolver( T instance, org.lgna.project.ast.Node node, Class< ? extends org.lgna.project.ast.Node > parameterType ) {
		this( instance, new org.lgna.project.ast.Node[]{ node }, new Class[] { parameterType } );
	}
	public NodeStaticGetInstanceKeyedResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}

	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		Object[] arguments = this.getArguments();
		for( int i=0; i<arguments.length; i++ ) {
			arguments[ i ] = retargeter.retarget( arguments[ i ] );
		}
	}
	@Override
	protected Class< ? >[] decodeParameterTypes( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		final int N = binaryDecoder.decodeInt();
		Class<?>[] rv = new Class<?>[ N ];
		for( int i=0; i<N; i++ ) {
			rv[ i ] = this.decodeClass( binaryDecoder );
		}
		return rv;
	}
	@Override
	protected void encodeParameterTypes( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.parameterTypes.length );
		for( Class< ? extends org.lgna.project.ast.Node > parameterType : parameterTypes ) {
			this.encodeClass( binaryEncoder, parameterType );
		}
	}
	@Override
	protected Object[] decodeArguments( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		final int N = binaryDecoder.decodeInt();
		org.lgna.project.ast.Node[] rv = new org.lgna.project.ast.Node[ N ];
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		for( int i=0; i<N; i++ ) {
			java.util.UUID id = binaryDecoder.decodeId();
			rv[ i ] = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), id );
		}
		return rv;
	}
	@Override
	protected void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.nodes.length );
		for( org.lgna.project.ast.Node node : nodes ) {
			binaryEncoder.encode( node.getUUID() );
		}
	}
}
