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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.edits.StateEdit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.Trigger;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class State<T> extends AbstractCompletionModel {
	public static interface ValueListener<T> {
		public void changing( State<T> state, T prevValue, T nextValue, boolean isAdjusting );

		public void changed( State<T> state, T prevValue, T nextValue, boolean isAdjusting );
	};

	private final List<org.lgna.croquet.event.ValueListener<T>> newSchoolValueListeners = Lists.newCopyOnWriteArrayList();
	private final List<ValueListener<T>> oldSchoolValueListeners = Lists.newCopyOnWriteArrayList();

	private T currentValue;
	private T previousValue;

	public State( Group group, UUID migrationId, T initialValue ) {
		super( group, migrationId );
		currentValue = initialValue;
		previousValue = initialValue;
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.setSwingValue( this.getCurrentTruthAndBeautyValue() );
	}

	public abstract T decodeValue( BinaryDecoder binaryDecoder );

	public abstract void encodeValue( BinaryEncoder binaryEncoder, T value );

	public abstract void appendRepresentation( StringBuilder sb, T value );

	public void addValueListener( ValueListener<T> valueListener ) {
		if( this.oldSchoolValueListeners.contains( valueListener ) ) {
			Logger.severe( "listener already contained", this, valueListener );
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
			Logger.severe( "listener not contained", this, valueListener );
		}
		this.oldSchoolValueListeners.remove( valueListener );
	}

	public void addNewSchoolValueListener( org.lgna.croquet.event.ValueListener<T> valueListener ) {
		if( this.newSchoolValueListeners.contains( valueListener ) ) {
			Logger.severe( "listener already contained", this, valueListener );
		}
		this.newSchoolValueListeners.add( valueListener );
	}

	public void addAndInvokeNewSchoolValueListener( org.lgna.croquet.event.ValueListener<T> valueListener ) {
		this.addNewSchoolValueListener( valueListener );
		ValueEvent<T> e = ValueEvent.createInstance( this.getValue() );
		valueListener.valueChanged( e );
	}

	public void removeNewSchoolValueListener( org.lgna.croquet.event.ValueListener<T> valueListener ) {
		if( this.newSchoolValueListeners.contains( valueListener ) ) {
			//pass
		} else {
			Logger.severe( "listener not contained", this, valueListener );
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
			ValueEvent<T> e = ValueEvent.createInstance( prevValue, nextValue, isAdjusting.value );
			for( org.lgna.croquet.event.ValueListener<T> valueListener : this.newSchoolValueListeners ) {
				valueListener.valueChanged( e );
			}
		}
	}

	@Override
	protected final void perform( UserActivity transaction, Trigger trigger ) {
		throw new UnsupportedOperationException();
	}

	private StateEdit<T> createStateEdit( UserActivity userActivity, T prevValue, T nextValue, IsAdjusting isAdjusting ) {
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "createStateEdit", prevValue, nextValue, isAdjusting.value ? "isAdjusting=true" : "" );
		return new StateEdit<T>( userActivity, prevValue, nextValue );
	}

	protected void handleTruthAndBeautyValueChange( T nextValue ) {
	}

	private void commitStateEdit( T prevValue, T nextValue, IsAdjusting isAdjusting, Trigger trigger ) {
		if (trigger != null) {
			// NB This activity is not expected to have any child activities
			UserActivity activity = trigger.getUserActivity();
			if (activity != null) {
				activity.setCompletionModel( this, trigger );
				StateEdit<T> edit = this.createStateEdit( activity, prevValue, nextValue, isAdjusting );
				activity.commitAndInvokeDo( edit );
			}
		}
		this.handleTruthAndBeautyValueChange( nextValue );
	}

	protected T getCurrentTruthAndBeautyValue() {
		return this.currentValue;
	}

	protected void setCurrentTruthAndBeautyValue( T value ) {
		this.currentValue = value;
	}

	protected abstract T getSwingValue();

	protected abstract void setSwingValue( T nextValue );

	protected boolean isSwingValueValid() {
		return true;
	}

	private void updateSwingModelIfAppropriate( T nextValue, Origin origin ) {
		if ( origin.isUpdatingSwingAppropriate() &&
				!(isSwingValueValid() && Objects.equals( getSwingValue(), nextValue )) ) {
			setSwingValue( nextValue );
		}
	}

	public final T getValue() {
		return this.getCurrentTruthAndBeautyValue();
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

	private static final Trigger NULL_TRIGGER = null;

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
			return !isFromSwing;
		}

		public boolean isCommitingEditAppropriate() {
			return !isFromEdit && !isFromSetValueTransactionlessly;
		}

	}

	// TODO move this down to SingleSelectListState or eliminate completely
	private int atomicCount;
	private T prevValueAtStartOfAtomicChange;

	void pushIsInTheMidstOfAtomicChange() {
		if( isNotInTheMidstOfAtomicChange() ) {
			this.prevValueAtStartOfAtomicChange = this.previousValue;
		}
		this.atomicCount++;
	}

	void popIsInTheMidstOfAtomicChange() {
		this.atomicCount--;
		if( isNotInTheMidstOfAtomicChange() ) {
			T nextValue = this.getCurrentTruthAndBeautyValue();
			this.changeValue( this.prevValueAtStartOfAtomicChange, nextValue, IsAdjusting.FALSE, NULL_TRIGGER, Origin.FROM_SET_VALUE_TRANSACTIONLESSLY );
		}
	}

	private boolean isNotInTheMidstOfAtomicChange() {
		return this.atomicCount <= 0;
	}

	private void changeValue( T prevValue, T nextValue, IsAdjusting isAdjusting, Trigger trigger, Origin origin ) {
		this.updateSwingModelIfAppropriate( nextValue, origin );
		if ( !Objects.equals( this.previousValue, nextValue ) && !this.isInTheMidstOfChange ) {
			this.isInTheMidstOfChange = true;
			try {
				this.fireChanging( prevValue, nextValue, isAdjusting );
				this.setCurrentTruthAndBeautyValue( nextValue );
				if ( origin.isCommitingEditAppropriate() ) {
					this.commitStateEdit( prevValue, nextValue, isAdjusting, trigger );
				}
				this.fireChanged( prevValue, nextValue, isAdjusting );
				this.previousValue = nextValue;
			} finally {
				this.isInTheMidstOfChange = false;
			}
		}
	}

	protected boolean isAdjustingIgnored() {
		return true;
	}

	private void changeValue( T nextValue, IsAdjusting isAdjusting, Trigger trigger, Origin origin ) {
		if ( isNotInTheMidstOfAtomicChange() &&
				!(isAdjusting.value && isAdjustingIgnored()) ) {
			this.changeValue( this.previousValue, nextValue, isAdjusting, trigger, origin );
		}
	}

	protected final void changeValueFromSwing( T nextValue, IsAdjusting isAdjusting, Trigger trigger ) {
		this.changeValue( nextValue, isAdjusting, trigger, Origin.FROM_SWING );
	}

	void changeValueFromIndirectModel( T nextValue, IsAdjusting isAdjusting, Trigger trigger ) {
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
		this.handleTruthAndBeautyValueChange( nextValue );
	}
}
