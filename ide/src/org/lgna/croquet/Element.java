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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public class Element {
	private static java.util.Map<java.util.UUID, Class<? extends Element>> map;
	static {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.lgna.croquet.Element.isIdCheckDesired" ) ) {
			map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
			System.err.println( "org.lgna.croquet.Element.isIdCheckDesired==true" );
		}
	}
	
	protected static abstract class IndirectResolver<D extends Element, I extends Element> implements org.lgna.croquet.resolvers.RetargetableResolver< D > {
		private final org.lgna.croquet.resolvers.CodableResolver<I> resolver;
		public IndirectResolver( I indirect ) {
			this.resolver = indirect.getCodableResolver();
		}
		public IndirectResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		}
		public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			binaryEncoder.encode( this.resolver );
		}
		protected abstract D getDirect( I indirect );
		public final D getResolved() {
			return this.getDirect( this.resolver.getResolved() );
		}
		public final void retarget( org.lgna.croquet.Retargeter retargeter ) {
			if( this.resolver instanceof org.lgna.croquet.resolvers.RetargetableResolver ) {
				org.lgna.croquet.resolvers.RetargetableResolver<I> retargetableResolver = (org.lgna.croquet.resolvers.RetargetableResolver<I>)this.resolver;
				retargetableResolver.retarget( retargeter );
			}
		}
	}

	
	private final java.util.UUID id;
	private org.lgna.croquet.resolvers.CodableResolver<Element> codableResolver;
	public Element( java.util.UUID id ) {
		this.id = id;
		if( map != null ) {
			Class<? extends Element> cls = map.get( id );
			if( cls != null ) {
				assert cls == this.getClass() : id;
			} else {
				map.put( id, this.getClass() );
			}
		}
	}
	public java.util.UUID getId() {
		return this.id;
	}
//	protected abstract <M extends Model> CodableResolver< M > createCodableResolver();
	protected <M extends Element> org.lgna.croquet.resolvers.CodableResolver< M > createCodableResolver() {
		return new org.lgna.croquet.resolvers.SingletonResolver( this );
	}
	public <M extends Element> org.lgna.croquet.resolvers.CodableResolver< M > getCodableResolver() {
		if( this.codableResolver != null ) {
			//pass
		} else {
			this.codableResolver = this.createCodableResolver();
		}
		return (org.lgna.croquet.resolvers.CodableResolver< M >)this.codableResolver;
	}
	
	protected StringBuilder appendRepr( StringBuilder rv ) {
		rv.append( this.getClass().getName() );
		return rv;
	}
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		this.appendRepr( sb );
		return sb.toString();
	}
}
