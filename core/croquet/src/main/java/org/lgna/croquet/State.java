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

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class State<T> extends AbstractCompletionModel {
	// These are "Old School" listeners and are being replaced by the "New School" org.lgna.croquet.event.ValueListener
	public interface ValueListener<T> {
		void changing( State<T> state, T prevValue, T nextValue );
		void changed( State<T> state, T prevValue, T nextValue );
	};

	private final List<org.lgna.croquet.event.ValueListener<T>> newSchoolValueListeners = Lists.newCopyOnWriteArrayList();
	private final List<ValueListener<T>> oldSchoolValueListeners = Lists.newCopyOnWriteArrayList();

	private T currentValue;
	T previousValue;

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
		valueListener.changed( this, prevValue, this.getValue() );
	}

	public void removeValueListener( ValueListener<T> valueListener ) {
		if ( !this.oldSchoolValueListeners.contains( valueListener ) ) {
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
		if ( !this.newSchoolValueListeners.contains( valueListener ) ) {
			Logger.severe( "listener not contained", this, valueListener );
		}
		this.newSchoolValueListeners.remove( valueListener );
	}

	protected void fireChanging( T prevValue, T nextValue ) {
		for( ValueListener<T> valueListener : this.oldSchoolValueListeners ) {
			valueListener.changing( this, prevValue, nextValue );
		}
	}

	protected void fireChanged( T prevValue, T nextValue, boolean isAdjusting ) {
		for( ValueListener<T> valueListener : this.oldSchoolValueListeners ) {
			valueListener.changed( this, prevValue, nextValue );
		}
		if( this.newSchoolValueListeners.size() > 0 ) {
			ValueEvent<T> e = ValueEvent.createInstance( prevValue, nextValue, isAdjusting );
			for( org.lgna.croquet.event.ValueListener<T> valueListener : this.newSchoolValueListeners ) {
				valueListener.valueChanged( e );
			}
		}
	}

	protected void handleTruthAndBeautyValueChange( T nextValue ) {
	}

	void commitStateEdit( T prevValue, T nextValue, UserActivity activity ) {
		// NB This activity is not expected to have any child activities
		if (activity != null) {
			activity.setCompletionModel( this );
			StateEdit<T> edit = new StateEdit<T>( activity, prevValue, nextValue );
			activity.commitAndInvokeDo( edit );
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

	public final T getValue() {
		return this.getCurrentTruthAndBeautyValue();
	}

	private boolean isInTheMidstOfChange = false;

	void changeValue( T prevValue, T nextValue ) {
		changeValue( prevValue, nextValue, () -> recordFinalValue( prevValue, nextValue) );
	}

	void changeValue( T prevValue, T nextValue, Runnable bookkeeping) {
		if ( shouldUpdateSwingModel( nextValue ) ) {
			setSwingValue( nextValue );
		}
		changeModelValue( prevValue, nextValue, bookkeeping );
	}

	protected boolean shouldUpdateSwingModel( T nextValue ) {
		return !(Objects.equals( getSwingValue(), nextValue ));
	}

	final void changeModelValue( T prevValue, T nextValue, Runnable bookkeeping ) {
		if ( !Objects.equals( this.previousValue, nextValue ) && !isInTheMidstOfChange ) {
			isInTheMidstOfChange = true;
			try {
				fireChanging( prevValue, nextValue );
				setCurrentTruthAndBeautyValue( nextValue );

				bookkeeping.run();

				previousValue = nextValue;
			} finally {
				isInTheMidstOfChange = false;
			}
		}
	}

	private void recordFinalValue( T prevValue, T nextValue ) {
		fireChanged( prevValue, nextValue, false );
	}

	private void commitFinalValue( T prevValue, T nextValue, UserActivity activity ) {
		commitStateEdit( prevValue, nextValue, activity );
		fireChanged( prevValue, nextValue, false );
	}

	protected void adjustModelValueFromSwing( T nextValue, UserActivity activity ) {
		// Ignore adjustments by default
	}


	// Updates to the state of the State come in to these methods

	// A change from the GUI. The isAdjusting flag indicates if the value is still in flux.
	final void changingValueFromSwing( T nextValue, boolean isAdjusting, UserActivity activity ) {
		if (isAdjusting) {
			adjustModelValueFromSwing( nextValue, activity );
		} else {
			changeValueFromSwing( nextValue, activity );
		}
	}

	// Final (not adjusting) change from the GUI. Change only the model's value.
	protected final void changeValueFromSwing( T nextValue, UserActivity activity ) {
		changeModelValue( previousValue, nextValue, () -> commitFinalValue( previousValue, nextValue, activity ) );
	}

	void changeValueFromIndirectModel( T nextValue, UserActivity activity ) {
		changeValue( previousValue, nextValue, () -> commitFinalValue( previousValue, nextValue, activity ) );
	}

	public final void setValueTransactionlessly( T value ) {
		changeValue( previousValue, value );
	}

	// Used by undo and redo
	public final void changeValueFromEdit( T nextValue ) {
		changeValue( previousValue, nextValue );
		handleTruthAndBeautyValueChange( nextValue );
	}
}
