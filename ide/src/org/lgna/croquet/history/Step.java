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
package org.lgna.croquet.history;

/**
 * @author Dennis Cosgrove
 */
public abstract class Step< M extends org.lgna.croquet.Model > extends Node<Transaction> {
	private final org.lgna.croquet.resolvers.CodableResolver< M > modelResolver;
	private final transient org.lgna.croquet.triggers.Trigger trigger;
	private final java.util.UUID id;
	public Step( Transaction parent, M model, org.lgna.croquet.triggers.Trigger trigger ) {
		super( parent );
		if( model != null ) {
			this.modelResolver = model.getCodableResolver();
		} else {
			this.modelResolver = null;
		}
		if( trigger != null ) {
			//pass
		} else {
			trigger = new org.lgna.croquet.triggers.SimulatedTrigger();
		}
		this.trigger = trigger;
		this.id = java.util.UUID.randomUUID();
	}
	public Step( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.modelResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.trigger = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.id = binaryDecoder.decodeId();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.modelResolver );
		binaryEncoder.encode( this.trigger );
		binaryEncoder.encode( this.id );
	}

	public org.lgna.croquet.triggers.Trigger getTrigger() {
		return this.trigger;
	}
	public java.util.UUID getId() {
		return this.id;
	}
	
	protected org.lgna.croquet.components.ViewController< ?, ? > getViewController() {
		return this.trigger != null ? this.trigger.getViewController() : null;
	}
	
	protected org.lgna.croquet.Model getModelForTutorialNoteText() {
		return this.getModel();
	}
	public String getTutorialNoteText( org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		org.lgna.croquet.Model model = this.getModelForTutorialNoteText();
		if( model != null ) {
			org.lgna.croquet.triggers.Trigger trigger = this.getTrigger();
			String triggerText = trigger != null ? trigger.getNoteText( userInformation.getLocale() ) : null;
			return model.getTutorialNoteText( this, triggerText, edit, userInformation );
		} else {
			return null;
		}
	}
	public M getModel() {
		return this.modelResolver != null ? this.modelResolver.getResolved() : null;
	}

	public void retarget( org.lgna.croquet.Retargeter retargeter ) {
		if( this.modelResolver instanceof org.lgna.croquet.resolvers.RetargetableResolver<?> ) {
			org.lgna.croquet.resolvers.RetargetableResolver<?> retargetableResolver = (org.lgna.croquet.resolvers.RetargetableResolver<?>)this.modelResolver;
			retargetableResolver.retarget( retargeter );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.modelResolver );
		}
		if( this.trigger instanceof org.lgna.croquet.triggers.RetargetableTrigger ) {
			org.lgna.croquet.triggers.RetargetableTrigger retargetableTrigger = (org.lgna.croquet.triggers.RetargetableTrigger)this.trigger;
			retargetableTrigger.retarget( retargeter );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.trigger );
		}
	}
	
	protected StringBuilder updateRepr( StringBuilder rv ) {
		org.lgna.croquet.Model model = this.getModel();
		if( model != null ) {
			java.util.Locale locale = null;
			rv.append( "model=" );
			rv.append( model.getClass().getName() );
			rv.append( ";trigger=" );
			org.lgna.croquet.triggers.Trigger trigger = this.getTrigger();
			rv.append( trigger != null ? trigger.getNoteText( locale ) : null );
			rv.append( ";text=" );
			rv.append( model.getTutorialNoteText( this, null, null, null ) );
			rv.append( ";" );
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
