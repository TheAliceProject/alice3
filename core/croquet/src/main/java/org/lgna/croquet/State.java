/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class State<T> extends AbstractCompletionModel implements org.lgna.croquet.ContextFactory<StateContext<T>> {
	public static interface ValueListener<T> {
		public void changing( State<T> state, T prevValue, T nextValue, boolean isAdjusting );

		public void changed( State<T> state, T prevValue, T nextValue, boolean isAdjusting );
	};

	private final java.util.List<org.lgna.croquet.event.ValueListener<T>> newSchoolValueListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<ValueListener<T>> oldSchoolValueListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private T previousValue;

	public State( Group group, java.util.UUID migrationId, T initialValue ) {
		super( group, migrationId );
		this.previousValue = initialValue;
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.setSwingValue( this.getCurrentTruthAndBeautyValue() );
	}

	public abstract Class<T> getItemClass();

	public abstract T decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );

	public abstract void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, T value );

	public abstract void appendRepresentation( StringBuilder sb, T value );

	public void addValueListener( ValueListener<T> valueListener ) {
		if( this.oldSchoolValueListeners.contains( valueListener ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener already contained", this, valueListener );
		}
		this.oldSchoolValueListeners.add( valueListener );
	}

	@Deprecated
	public void addAndInvokeValueListener( ValueListener<T> valueListener ) {
		this.addValueListener( valueListener );
		//todo
		T prevValue = null;
		valueListener.changed( this, prevValue, this.getValue(), false );
	}

	public void removeValueListener( ValueListener<T> valueListener ) {
		if( this.oldSchoolValueListeners.contains( valueListener ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener not contained", this, valueListener );
		}
		this.oldSchoolValueListeners.remove( valueListener );
	}

	public void addNewSchoolValueListener( org.lgna.croquet.event.ValueListener<T> valueListener ) {
		if( this.newSchoolValueListeners.contains( valueListener ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener already contained", this, valueListener );
		}
		this.newSchoolValueListeners.add( valueListener );
	}

	public void addAndInvokeNewSchoolValueListener( org.lgna.croquet.event.ValueListener<T> valueListener ) {
		this.addNewSchoolValueListener( valueListener );
		org.lgna.croquet.event.ValueEvent<T> e = org.lgna.croquet.event.ValueEvent.createInstance( this.getValue() );
		valueListener.valueChanged( e );
	}

	public void removeNewSchoolValueListener( org.lgna.croquet.event.ValueListener<T> valueListener ) {
		if( this.newSchoolValueListeners.contains( valueListener ) ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "listener not contained", this, valueListener );
		}
		this.newSchoolValueListeners.remove( valueListener );
	}

	protected void fireChanging( T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		for( ValueListener<T> valueListener : this.oldSchoolValueListeners ) {
			valueListener.changing( this, prevValue, nextValue, isAdjusting.value );
		}
	}

	protected void fireChanged( T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		for( ValueListener<T> valueListener : this.oldSchoolValueListeners ) {
			valueListener.changed( this, prevValue, nextValue, isAdjusting.value );
		}
		if( this.newSchoolValueListeners.size() > 0 ) {
			org.lgna.croquet.event.ValueEvent<T> e = org.lgna.croquet.event.ValueEvent.createInstance( prevValue, nextValue, isAdjusting.value );
			for( org.lgna.croquet.event.ValueListener<T> valueListener : this.newSchoolValueListeners ) {
				valueListener.valueChanged( e );
			}
		}
	}

	@Override
	public org.lgna.croquet.StateContext<T> createContext() {
		return new StateContext<T>( this, this.getValue() );
	}

	@Override
	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		throw new UnsupportedOperationException();
	}

	private org.lgna.croquet.edits.StateEdit<T> createStateEdit( org.lgna.croquet.history.CompletionStep<State<T>> step, T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "createStateEdit", prevValue, nextValue, isAdjusting.value ? "isAdjusting=true" : "" );
		return new org.lgna.croquet.edits.StateEdit<T>( step, prevValue, nextValue );
	}

	protected void handleTruthAndBeautyValueChange( T nextValue ) {
	}

	protected org.lgna.croquet.edits.StateEdit<T> commitStateEdit( T prevValue, T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.Transaction owner = org.lgna.croquet.Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory().acquireActiveTransaction();
		org.lgna.croquet.history.CompletionStep<State<T>> completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( owner, this, trigger, null );
		org.lgna.croquet.edits.StateEdit<T> edit = this.createStateEdit( completionStep, prevValue, nextValue, isAdjusting );
		if( edit != null ) {
			completionStep.commitAndInvokeDo( edit );
			this.handleTruthAndBeautyValueChange( nextValue );
		}
		return edit;
	}

	protected org.lgna.croquet.edits.StateEdit<T> createEdit( org.lgna.croquet.history.CompletionStep<State<T>> completionStep, T nextValue ) {
		T prevValue = this.getValue();
		if( edu.cmu.cs.dennisc.java.util.Objects.equals( prevValue, nextValue ) ) {
			//			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( nextValue );
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

	protected boolean isSwingValueValid() {
		return true;
	}

	private void updateSwingModelIfAppropriate( T nextValue, Origin origin ) {
		if( origin.isUpdatingSwingAppropriate() ) {
			boolean isUpdatingSwingValueAppropriate;
			if( this.isSwingValueValid() ) {
				T prevSwingValue = this.getSwingValue();
				if( edu.cmu.cs.dennisc.java.util.Objects.equals( prevSwingValue, nextValue ) ) {
					isUpdatingSwingValueAppropriate = false;
				} else {
					isUpdatingSwingValueAppropriate = true;
				}
			} else {
				isUpdatingSwingValueAppropriate = true;
			}
			if( isUpdatingSwingValueAppropriate ) {
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
		FROM_SET_VALUE_TRANSACTIONLESSLY( false, false, false, true );
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
	}

	protected boolean isAdjustingIgnored() {
		return true;
	}

	private org.lgna.croquet.edits.StateEdit<T> changeValue( T prevValue, T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger, Origin origin ) {
		org.lgna.croquet.edits.StateEdit<T> rv = null;
		this.updateSwingModelIfAppropriate( nextValue, origin );
		if( edu.cmu.cs.dennisc.java.util.Objects.equals( this.previousValue, nextValue ) ) {
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
						rv = this.commitStateEdit( prevValue, nextValue, isAdjusting, trigger );
					}
					this.fireChanged( prevValue, nextValue, isAdjusting );
					this.previousValue = nextValue;
				} finally {
					this.isInTheMidstOfChange = false;
				}
			}
		}
		return rv;
	}

	private org.lgna.croquet.edits.StateEdit<T> changeValue( T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger, Origin origin ) {
		org.lgna.croquet.edits.StateEdit<T> rv = null;
		if( this.atomicCount > 0 ) {
			//pass
		} else {
			if( isAdjusting.value && this.isAdjustingIgnored() ) {
				//pass
			} else {
				rv = this.changeValue( this.previousValue, nextValue, isAdjusting, trigger, origin );
			}
		}
		return rv;
	}

	protected final void changeValueFromSwing( T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		this.changeValue( nextValue, isAdjusting, trigger, Origin.FROM_SWING );
	}

	protected org.lgna.croquet.history.CompletionStep<?> changeValueFromIndirectModel( T nextValue, IsAdjusting isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.edits.StateEdit<T> edit = this.changeValue( nextValue, isAdjusting, trigger, Origin.FROM_INDIRECT_MODEL );
		return edit != null ? edit.getCompletionStep() : null;
	}

	private void changeValueTransactionlessly( T value, IsAdjusting isAdjusting ) {
		this.changeValue( value, isAdjusting, NULL_TRIGGER, Origin.FROM_SET_VALUE_TRANSACTIONLESSLY );
	}

	public final void setValueTransactionlessly( T value ) {
		this.changeValueTransactionlessly( value, IsAdjusting.FALSE );
	}

	public final void changeValueFromEdit( T nextValue ) {
		this.changeValue( nextValue, IsAdjusting.FALSE, NULL_TRIGGER, Origin.FROM_EDIT );
		this.handleTruthAndBeautyValueChange( nextValue );
	}
}
