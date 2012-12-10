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
public abstract class State<T> extends AbstractCompletionModel implements org.lgna.croquet.ContextFactory<StateContext<T>> {
	public static interface ValueListener<T> {
		public void changing( State<T> state, T prevValue, T nextValue, boolean isAdjusting );

		public void changed( State<T> state, T prevValue, T nextValue, boolean isAdjusting );
	};

	private final java.util.List<ValueListener<T>> valueListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.Stack<T> generatorValueStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	private T previousValue;

	public State( Group group, java.util.UUID migrationId, T initialValue ) {
		super( group, migrationId );
		this.previousValue = initialValue;
	}

	public abstract Class<T> getItemClass();

	public abstract T decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );

	public abstract void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, T value );

	public abstract void appendRepresentation( StringBuilder sb, T value );

	public void addValueListener( ValueListener<T> valueListener ) {
		if( this.valueListeners.contains( valueListener ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, valueListener );
		}
		this.valueListeners.add( valueListener );
	}

	@Deprecated
	public void addAndInvokeValueListener( ValueListener<T> valueListener ) {
		this.addValueListener( valueListener );
		//todo
		T prevValue = null;
		valueListener.changed( this, prevValue, this.getValue(), false );
	}

	public void removeValueListener( ValueListener<T> valueListener ) {
		if( this.valueListeners.contains( valueListener ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener not contained", valueListener );
		}
		this.valueListeners.remove( valueListener );
	}

	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit<?> edit ) {
		if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
			org.lgna.croquet.edits.StateEdit<T> stateEdit = (org.lgna.croquet.edits.StateEdit<T>)edit;
			T a = this.getValue();
			T b = stateEdit.getNextValue();
			return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( a, b );
		} else {
			return false;
		}
	}

	public void pushGeneratedValue( T value ) {
		this.generatorValueStack.push( value );
	}

	public T popGeneratedValue() {
		return this.generatorValueStack.pop();
	}

	public org.lgna.croquet.StateContext<T> createContext( org.lgna.croquet.triggers.Trigger.Origin origin ) {
		T value;
		if( origin == org.lgna.croquet.triggers.Trigger.Origin.GENERATOR ) {
			assert this.generatorValueStack.isEmpty() == false : this;
			value = this.generatorValueStack.peek();
		} else {
			value = this.getValue();
		}
		return new StateContext<T>( this, value );
	}

	public org.lgna.croquet.history.Transaction addGeneratedStateChangeTransaction( org.lgna.croquet.history.TransactionHistory history, T prevValue, T nextValue ) throws UnsupportedGenerationException {
		return this.addGeneratedTransaction( history, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance(), new org.lgna.croquet.edits.StateEdit( null, prevValue, nextValue ) );
	}

	protected void fireChanging( T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		for( ValueListener<T> valueListener : this.valueListeners ) {
			valueListener.changing( this, prevValue, nextValue, isAdjusting.value );
		}
	}

	protected void fireChanged( T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		for( ValueListener<T> valueListener : this.valueListeners ) {
			valueListener.changed( this, prevValue, nextValue, isAdjusting.value );
		}
	}

	@Override
	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		throw new UnsupportedOperationException();
	}

	private org.lgna.croquet.edits.StateEdit<T> createStateEdit( org.lgna.croquet.history.CompletionStep<State<T>> step, T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( prevValue, nextValue );
		return new org.lgna.croquet.edits.StateEdit<T>( step, prevValue, nextValue );
	}

	protected org.lgna.croquet.edits.StateEdit<T> commitStateEdit( T prevValue, T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "commitStateEdit", prevValue, nextValue );
		org.lgna.croquet.history.Transaction owner = org.lgna.croquet.Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory().acquireActiveTransaction();
		org.lgna.croquet.history.CompletionStep<State<T>> completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( owner, this, trigger, null );
		org.lgna.croquet.edits.StateEdit<T> edit = this.createStateEdit( completionStep, prevValue, nextValue, isAdjusting );
		if( edit != null ) {
			completionStep.commitAndInvokeDo( edit );
		}
		return edit;
	}

	@Override
	public final org.lgna.croquet.edits.StateEdit<T> commitTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.edits.Edit<?> originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		assert originalEdit instanceof org.lgna.croquet.edits.StateEdit;
		org.lgna.croquet.edits.StateEdit<T> originalStateEdit = (org.lgna.croquet.edits.StateEdit<T>)originalEdit;
		return this.commitStateEdit( originalStateEdit.getPreviousValue(), originalStateEdit.getNextValue(), IsAdjusting.FALSE, org.lgna.croquet.triggers.AutomaticCompletionTrigger.createUserInstance() );
	}

	@Override
	protected final StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit ) {
		if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
			org.lgna.croquet.edits.StateEdit<T> stateEdit = (org.lgna.croquet.edits.StateEdit<T>)edit;
			rv.append( " <strong>" );
			this.appendRepresentation( rv, stateEdit.getNextValue() );
			rv.append( "</strong>." );
		} else {
			rv.append( "UNKNOWN EDIT: " + edit );
		}
		return rv;
	}

	protected org.lgna.croquet.edits.StateEdit<T> createEdit( org.lgna.croquet.history.CompletionStep<State<T>> completionStep, T nextValue ) {
		T prevValue = this.getValue();
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( prevValue, nextValue ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( nextValue );
		}
		return new org.lgna.croquet.edits.StateEdit<T>( completionStep, prevValue, nextValue );
	}

	protected abstract T getCurrentTruthAndBeautyValue();

	protected abstract void setCurrentTruthAndBeautyValue( T value );

	protected final T getPreviousTruthAndBeautyValue() {
		return this.previousValue;
	}

	protected abstract T getSwingValue();

	protected abstract void setSwingValue( T nextValue );

	private void updateSwingModelIfAppropriate( T nextValue, Origin origin ) {
		if( origin.isUpdatingSwingAppropriate() ) {
			T prevSwingValue = this.getSwingValue();
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( prevSwingValue, nextValue ) ) {
				//pass
			} else {
				this.setSwingValue( nextValue );
			}
		}
	}

	public final T getValue() {
		return this.getCurrentTruthAndBeautyValue();
	}

	protected boolean isAppropriateToChange() {
		return true;
	}

	private boolean isInTheMidstOfChange = false;

	protected static enum IsAdjusting {
		TRUE( true ),
		FALSE( false );
		private final boolean value;

		private IsAdjusting( boolean value ) {
			this.value = value;
		}

		public boolean getValue() {
			return this.value;
		}

		public static IsAdjusting valueOf( boolean value ) {
			return value ? TRUE : FALSE;
		}
	}

	private static final org.lgna.croquet.triggers.Trigger NULL_TRIGGER = null;

	private static enum Origin {
		FROM_SWING( true, false, false, false ),
		FROM_EDIT( false, true, false, false ),
		FROM_INDIRECT_MODEL( false, false, true, false ),
		FROM_SET_VALUE_TRANSACTIONLESSLY( false, false, true, false );
		private final boolean isFromSwing;
		private final boolean isFromEdit;
		private final boolean isFromIndirectModel;
		private final boolean isFromSetValueTransactionlessly;

		private Origin( boolean isFromSwing, boolean isFromEdit, boolean isFromIndirectModel, boolean isFromSetValueTransactionlessly ) {
			this.isFromSwing = isFromSwing;
			this.isFromEdit = isFromEdit;
			this.isFromIndirectModel = isFromIndirectModel;
			this.isFromSetValueTransactionlessly = isFromSetValueTransactionlessly;
		}

		public boolean isUpdatingSwingAppropriate() {
			return this.isFromSwing == false;
		}

		public boolean isCommitingEditAppropriate() {
			return ( this.isFromEdit == false ) && ( this.isFromSetValueTransactionlessly == false );
		}

	}

	private int atomicCount;
	private T prevValueAtStartOfAtomicChange;

	protected void pushIsInTheMidstOfAtomicChange() {
		if( this.atomicCount == 0 ) {
			this.prevValueAtStartOfAtomicChange = this.previousValue;
		}
		this.atomicCount++;
	}

	protected void popIsInTheMidstOfAtomicChange() {
		this.atomicCount--;
		if( this.atomicCount == 0 ) {
			T nextValue = this.getCurrentTruthAndBeautyValue();
			this.changeValue( this.prevValueAtStartOfAtomicChange, nextValue, IsAdjusting.FALSE, NULL_TRIGGER, Origin.FROM_SET_VALUE_TRANSACTIONLESSLY );
		}
		if( this.atomicCount < 0 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, this.atomicCount );
		}
	}

	private void changeValue( T prevValue, T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger, Origin origin ) {
		this.updateSwingModelIfAppropriate( nextValue, origin );
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.previousValue, nextValue ) ) {
			//pass
		} else {
			if( this.isInTheMidstOfChange ) {
				//pass
			} else {
				this.isInTheMidstOfChange = true;
				try {
					this.fireChanging( prevValue, nextValue, isAdjusting );
					this.setCurrentTruthAndBeautyValue( nextValue );
					if( origin.isCommitingEditAppropriate() ) {
						this.commitStateEdit( prevValue, nextValue, isAdjusting, trigger );
					}
					this.fireChanged( prevValue, nextValue, isAdjusting );
					this.previousValue = nextValue;
				} finally {
					this.isInTheMidstOfChange = false;
				}
			}
		}
	}

	private void changeValue( T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger, Origin origin ) {
		if( this.atomicCount > 0 ) {
			//pass
		} else {
			if( isAdjusting.value ) {
				//pass
			} else {
				this.changeValue( this.previousValue, nextValue, isAdjusting, trigger, origin );
			}
		}
	}

	protected final void changeValueFromSwing( T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		this.changeValue( nextValue, isAdjusting, trigger, Origin.FROM_SWING );
	}

	protected final void changeValueFromIndirectModel( T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		this.changeValue( nextValue, isAdjusting, trigger, Origin.FROM_INDIRECT_MODEL );
	}

	private void changeValueTransactionlessly( T value, IsAdjusting isAdjusting ) {
		this.changeValue( value, isAdjusting, NULL_TRIGGER, Origin.FROM_SET_VALUE_TRANSACTIONLESSLY );
	}

	public final void setValueTransactionlessly( T value ) {
		this.changeValueTransactionlessly( value, IsAdjusting.FALSE );
	}

	public final void changeValueFromEdit( T nextValue ) {
		this.changeValue( nextValue, IsAdjusting.FALSE, NULL_TRIGGER, Origin.FROM_EDIT );
	}

	@Deprecated
	protected org.lgna.croquet.edits.StateEdit<T> TEMPORARY_HACK_createEdit( org.lgna.croquet.history.CompletionStep<State<T>> completionStep, T nextValue ) {
		T prevValue = this.getValue();
		return this.createStateEdit( completionStep, prevValue, nextValue, IsAdjusting.FALSE );
	}
}
