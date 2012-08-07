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
public abstract class State<T> extends AbstractCompletionModel implements org.lgna.croquet.ContextFactory< StateContext<T> > {
	public static interface ValueListener<T> {
		public void changing( State< T > state, T prevValue, T nextValue, boolean isAdjusting );
		public void changed( State< T > state, T prevValue, T nextValue, boolean isAdjusting );
	};
	private final java.util.List< ValueListener<T> > valueListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private T prevValueForSkipCheck;
	private boolean prevIsAdjustingForSkipCheck;
	private final java.util.Stack<T> generatorValueStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	public State( Group group, java.util.UUID id, T initialValue ) {
		super(group, id);
		this.prevValueForSkipCheck = initialValue;
		this.prevIsAdjustingForSkipCheck = false;
	}

	public abstract Class<T> getItemClass();
	public abstract T decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	public abstract void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, T value );
	public abstract StringBuilder appendRepresentation( StringBuilder rv, T value );

	public void pushGeneratedValue( T value ) {
		this.generatorValueStack.push( value );
	}
	public T popGeneratedValue() {
		return this.generatorValueStack.pop();
	}
	public org.lgna.croquet.StateContext< T > createContext( org.lgna.croquet.triggers.Trigger.Origin origin ) {
		T value;
		if( origin == org.lgna.croquet.triggers.Trigger.Origin.GENERATOR ) {
			assert this.generatorValueStack.isEmpty()==false : this;
			value = this.generatorValueStack.peek();
		} else {
			value = this.getValue();
		}
		return new StateContext< T >( this, value );
	}

	public void addValueListener( ValueListener<T> valueListener ) {
		assert this.valueListeners.contains( valueListener ) == false : valueListener;
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
	protected void fireChanging( T prevValue, T nextValue, boolean isAdjusting ) {
		this.isInTheMidstOfChanging = true;
		this.prevValueForChanging = prevValue;
		try {
			for( ValueListener<T> valueListener : this.valueListeners ) {
				valueListener.changing( this, prevValue, nextValue, isAdjusting );
			}
		} finally {
			this.prevValueForChanging = null;
			this.isInTheMidstOfChanging = false;
		}
	}
	protected void fireChanged( T prevValue, T nextValue, boolean isAdjusting ) {
		for( ValueListener<T> valueListener : this.valueListeners ) {
			valueListener.changed( this, prevValue, nextValue, isAdjusting );
		}
	}

	@Override
	public org.lgna.croquet.history.Step<?> fire(org.lgna.croquet.triggers.Trigger trigger) {
		throw new UnsupportedOperationException();
	}

	protected org.lgna.croquet.edits.StateEdit< T > commitStateEdit( T prevValue, T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.Transaction owner = org.lgna.croquet.Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory().acquireActiveTransaction();
		org.lgna.croquet.history.CompletionStep< State<T> > step = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( owner, this, trigger, null );
		org.lgna.croquet.edits.StateEdit< T > edit = new org.lgna.croquet.edits.StateEdit( step, prevValue, nextValue );
		step.commitAndInvokeDo( edit );
		return edit;
	}

	@Override
	public final org.lgna.croquet.edits.StateEdit< T > commitTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep< ? > step, org.lgna.croquet.edits.Edit< ? > originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		assert originalEdit instanceof org.lgna.croquet.edits.StateEdit;
		org.lgna.croquet.edits.StateEdit< T > originalStateEdit = (org.lgna.croquet.edits.StateEdit< T >)originalEdit;
		return this.commitStateEdit( originalStateEdit.getPreviousValue(), originalStateEdit.getNextValue(), false, new org.lgna.croquet.triggers.AutomaticCompletionTrigger() );
	}

	@Override
	protected final StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit ) {
		if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
			org.lgna.croquet.edits.StateEdit< T > stateEdit = (org.lgna.croquet.edits.StateEdit< T >)edit;
			rv.append( " <strong>" );
			this.appendRepresentation( rv, stateEdit.getNextValue() );
			rv.append( "</strong>." );
		} else {
			rv.append( "UNKNOWN EDIT: " + edit );
		}
		return rv;
	}

	private static final boolean IS_ADJUSTING_IGNORED = true; //todo
	protected abstract void updateSwingModel( T nextValue );
	private void changeValue( T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger, boolean isFromSwing ) {
		if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.prevValueForSkipCheck, nextValue ) && ( IS_ADJUSTING_IGNORED || ( isAdjusting == this.prevIsAdjustingForSkipCheck ) ) ) {
			//pass
		} else {
			T prevValue = this.prevValueForSkipCheck;
			this.prevValueForSkipCheck = nextValue;
			this.prevIsAdjustingForSkipCheck = isAdjusting;
			this.fireChanging( prevValue, nextValue, isAdjusting );
			if( isFromSwing ) {
				//pass
			} else {
				this.updateSwingModel( nextValue );
			}
			if( this.isAppropriateToComplete() ) {
				this.commitStateEdit( prevValue, nextValue, isAdjusting, trigger );
			}
			//todo?
			//			for( org.lgna.croquet.components.JComponent< ? > component : this.getComponents() ) {
			//				component.revalidateAndRepaint();
			//			}
			this.fireChanged( prevValue, nextValue, isAdjusting );
		}

	}
	protected final void changeValue( T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		this.changeValue( nextValue, isAdjusting, trigger, false );
	}
	protected final void changeValueFromSwing( T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		this.changeValue( nextValue, isAdjusting, trigger, true );
	}

	private T prevValueForChanging;
	private boolean isInTheMidstOfChanging = false;
	protected abstract T getActualValue();
	public final T getValue() {
		if( this.isInTheMidstOfChanging ) {
			return this.prevValueForChanging;
		} else {
			return this.getActualValue();
		}
	}
	public final void setValue( T value ) {
		this.changeValue( value, false, null );
	}

	private void changeValueTransactionlessly( T value, boolean isAdjusting ) {
		this.pushIgnore();
		try {
			this.changeValue( value, isAdjusting, null );
		} finally {
			this.popIgnore();
		}
	}

	public final void setValueTransactionlessly( T value ) {
		this.changeValueTransactionlessly( value, false );
	}
	public final void adjustValueTransactionlessly( T value ) {
		this.changeValueTransactionlessly( value, true );
	}
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit< ? > edit ) {
		if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
			org.lgna.croquet.edits.StateEdit< T > stateEdit = (org.lgna.croquet.edits.StateEdit< T >)edit;
			T a = this.getValue();
			T b = stateEdit.getNextValue();
			return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( a, b );
		} else {
			return false;
		}
	}

	protected org.lgna.croquet.edits.StateEdit<T> createEdit( org.lgna.croquet.history.CompletionStep< State<T> > completionStep, T nextValue) {
		T prevValue = this.getValue();
		return new org.lgna.croquet.edits.StateEdit< T >( completionStep, prevValue, nextValue );
	}
	
	public org.lgna.croquet.history.Transaction addGeneratedStateChangeTransaction( org.lgna.croquet.history.TransactionHistory history, T prevValue, T nextValue ) {
		return this.addGeneratedTransaction( history, org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance(), new org.lgna.croquet.edits.StateEdit( null, prevValue, nextValue ) );
	}
}
