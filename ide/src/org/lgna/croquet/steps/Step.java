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
package org.lgna.croquet.steps;

/**
 * @author Dennis Cosgrove
 */
public abstract class Step< M extends edu.cmu.cs.dennisc.croquet.Model > implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
//	private static final java.util.Map< java.util.UUID, Step > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
//	public static <S extends Step> S lookup( java.util.UUID id ) {
//		return (S)map.get( id );
//	}

	private Transaction parent;
	private final edu.cmu.cs.dennisc.croquet.CodableResolver< M > modelResolver;
	private final transient org.lgna.croquet.Trigger trigger;
//	private final java.util.UUID id;
	public Step( Transaction parent, M model, org.lgna.croquet.Trigger trigger ) {
		this.setParent( parent );
		//this.modelResolver = model != null ? model.getCodableResolver() : null;
		if( model != null ) {
			this.modelResolver = model.getCodableResolver();
		} else {
			this.modelResolver = null;
		}
		this.trigger = trigger;
//		this.id = java.util.UUID.randomUUID();
//		map.put( this.id, this );
	}
	public Step( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.modelResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
//		this.id = binaryDecoder.decodeId();
		this.trigger = null;
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.modelResolver );
//		binaryEncoder.encode( this.id );
	}

	public org.lgna.croquet.Trigger getTrigger() {
		return this.trigger;
	}
//	public java.util.UUID getId() {
//		return this.id;
//	}
	protected edu.cmu.cs.dennisc.croquet.Model getModelForTutorialNoteText() {
		return this.getModel();
	}
	public String getTutorialNoteText( edu.cmu.cs.dennisc.croquet.Edit< ? > edit, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		edu.cmu.cs.dennisc.croquet.Model model = this.getModelForTutorialNoteText();
		if( model != null ) {
			return model.getTutorialNoteText( this, edit, userInformation );
		} else {
			return null;
		}
	}
	public M getModel() {
		return this.modelResolver != null ? this.modelResolver.getResolved() : null;
	}
	public Transaction getParent() {
		return this.parent;
	}
	/*package-private*/ void setParent( Transaction parent ) {
		this.parent = parent;
	}

	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		if( this.modelResolver instanceof edu.cmu.cs.dennisc.croquet.RetargetableResolver<?> ) {
			edu.cmu.cs.dennisc.croquet.RetargetableResolver<?> retargetableResolver = (edu.cmu.cs.dennisc.croquet.RetargetableResolver<?>)this.modelResolver;
			retargetableResolver.retarget( retargeter );
		}
	}
	
	protected StringBuilder updateRepr( StringBuilder rv ) {
		edu.cmu.cs.dennisc.croquet.Model model = this.getModel();
		if( model != null ) {
			rv.append( this.getModel().getClass().getName() );
		}
		return rv;
	}
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		updateRepr( sb );
		sb.append( "]" );
		return sb.toString();
	}
}
