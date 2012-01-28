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
public abstract class ValueInputDialogOperation<T> extends InputDialogOperation< T > {
	public static final class InternalFillInResolver<F> extends IndirectResolver< InternalFillIn<F>, ValueInputDialogOperation<F> > {
		private InternalFillInResolver( ValueInputDialogOperation<F> internal ) {
			super( internal );
		}
		public InternalFillInResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalFillIn<F> getDirect( ValueInputDialogOperation<F> indirect ) {
			return indirect.getFillIn();
		}
	}
	private static final class InternalFillIn<F> extends CascadeFillIn< F, Void > {
		private final ValueInputDialogOperation<F> valueInputDialogOperation;
		private InternalFillIn( ValueInputDialogOperation<F> valueInputDialogOperation ) {
			super( java.util.UUID.fromString( "f2c75b9f-aa0d-487c-a161-46cb23ff3e76" ) );
			this.valueInputDialogOperation = valueInputDialogOperation;
		}
		public ValueInputDialogOperation<F> getInputDialogOperation() {
			return this.valueInputDialogOperation;
		}
		@Override
		protected InternalFillInResolver<F> createResolver() {
			return new InternalFillInResolver<F>( this.valueInputDialogOperation );
		}
		@Override
		protected String getTutorialItemText() {
			return this.valueInputDialogOperation.getDefaultLocalizedText();
		}
		@Override
		protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super F,Void > step ) {
			return new javax.swing.JLabel( this.getTutorialItemText() );
		}
		@Override
		public final F createValue( org.lgna.croquet.cascade.ItemNode< ? super F,Void > step ) {
			org.lgna.croquet.history.InputDialogOperationStep<F> inputDialogStep = this.valueInputDialogOperation.fire();
			if( inputDialogStep.isValueCommitted() ) {
				return inputDialogStep.getCommittedValue();
			} else {
				throw new CancelException();
			}
		}
		@Override
		public F getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super F,Void > step ) {
			return null;
		}
		
		@Override
		protected StringBuilder appendRepr( StringBuilder rv ) {
			super.appendRepr( rv );
			rv.append( "[" );
			rv.append( this.getInputDialogOperation() );
			rv.append( "]" );
			return rv;
		}
	}

	public ValueInputDialogOperation( org.lgna.croquet.Group group, java.util.UUID id ) {
		super( group, id );
	}
	private InternalFillIn<T> cascadeFillIn;
	public synchronized InternalFillIn<T> getFillIn() {
		if( this.cascadeFillIn != null ) {
			//pass
		} else {
			this.cascadeFillIn = new InternalFillIn<T>( this );
		}
		return this.cascadeFillIn;
	}
	protected abstract T createValue( org.lgna.croquet.history.InputDialogOperationStep< T > step );
	@Override
	protected final void epilogue( org.lgna.croquet.history.InputDialogOperationStep< T > step, boolean isCommit ) {
		if( isCommit ) {
			T value = this.createValue( step );
			if( value != null ) {
				step.commitValue( value );
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
}
