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

public abstract class ValueProducer<T> extends CompletionModel { //todo: PrepModel?  w/ transaction history?
	private static final org.lgna.croquet.history.Step.Key< Object > VALUE_KEY = org.lgna.croquet.history.Step.Key.createInstance( "ValueProducer.VALUE_KEY" );

	public ValueProducer( Group group, java.util.UUID id ) {
		super( group, id );
	}
	
	protected abstract org.lgna.croquet.history.TransactionHistory createTransactionHistoryIfNecessary();
	protected abstract T internalGetValue( org.lgna.croquet.history.ValueProducerStep<T> step ) throws CancelException;

	@Override
	public org.lgna.croquet.history.ValueProducerStep<T> fire( org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.ValueProducerStep<T> step = org.lgna.croquet.history.TransactionManager.addValueProducerStep( this, trigger, this.createTransactionHistoryIfNecessary() );
		T value = this.internalGetValue( step );
		step.putEphemeralDataFor( VALUE_KEY, value );
		return step;
	}
	public T getValue( org.lgna.croquet.history.ValueProducerStep<T> step ) {
		if( step.containsEphemeralDataFor( VALUE_KEY ) ) {
			return (T)step.getEphemeralDataFor( VALUE_KEY );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
			return null;
		}
	}
	
	@Override
	public Iterable<? extends org.lgna.croquet.PrepModel> getPotentialRootPrepModels() {
		return java.util.Collections.emptyList();
	}
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit<?> edit ) {
		return false;
	}
	@Override
	protected void localize() {
	}
	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit, UserInformation userInformation ) {
		return rv;
	}
}
