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
public abstract class State<T> extends CompletionModel {
	public static interface ValueObserver<T> {
		public void changing( State< T > state, T prevValue, T nextValue, boolean isAdjusting );
		public void changed( State< T > state, T prevValue, T nextValue, boolean isAdjusting );
	};
	private final java.util.List< ValueObserver<T> > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public State( Group group, java.util.UUID id ) {
		super(group, id);
	}
	public void addValueObserver( ValueObserver<T> valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void addAndInvokeValueObserver( ValueObserver<T> valueObserver ) {
		this.addValueObserver( valueObserver );
		valueObserver.changed( this, null, this.getValue(), false );
	}
	public void removeValueObserver( ValueObserver<T> valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	protected void fireChanging( T prevValue, T nextValue, boolean isAdjusting ) {
		for( ValueObserver<T> valueObserver : this.valueObservers ) {
			valueObserver.changing( this, prevValue, nextValue, isAdjusting );
		}
	}
	protected void fireChanged( T prevValue, T nextValue, boolean isAdjusting ) {
		for( ValueObserver<T> valueObserver : this.valueObservers ) {
			valueObserver.changed( this, prevValue, nextValue, isAdjusting );
		}
	}
	
	@Override
	public org.lgna.croquet.history.Step<?> fire(org.lgna.croquet.triggers.Trigger trigger) {
		throw new UnsupportedOperationException();
	}

	
	private int pushCount = 0;
	private T prevAtomicSelectedValue;
	private org.lgna.croquet.triggers.Trigger trigger;
	public boolean isInMidstOfAtomic() {
		return this.pushCount > 0;
	}
	public void pushAtomic( org.lgna.croquet.triggers.Trigger trigger ) {
		if( this.isInMidstOfAtomic() ) {
			//pass
		} else {
			this.prevAtomicSelectedValue = this.getValue();
			this.trigger = trigger;
		}
		this.pushCount++;
	}
	public void pushAtomic() {
		this.pushAtomic( null );
	}
	public void popAtomic() {
		this.pushCount--;
		if( this.pushCount == 0 ) {
			T nextSelectedValue = this.getValue();
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.prevAtomicSelectedValue, nextSelectedValue ) ) {
				//pass
			} else {
				boolean isAdjusting = false;
				this.fireChanging( this.prevAtomicSelectedValue, nextSelectedValue, isAdjusting );
				if( this.isAppropriateToComplete() ) {
					this.commitStateEdit( this.prevAtomicSelectedValue, nextSelectedValue, isAdjusting, this.trigger );
				}
				this.fireChanged( this.prevAtomicSelectedValue, nextSelectedValue, isAdjusting );
				this.trigger = null;
			}
		}
	}
	@Override
	protected boolean isAppropriateToComplete() {
		return super.isAppropriateToComplete() && this.isInMidstOfAtomic() == false;
	}
	
	protected abstract void commitStateEdit( T prevValue, T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger );
	protected abstract void handleValueChange( T nextValue );
	protected final void changeValue( T prevValue, T nextValue, boolean isAdjusting ) {
		this.fireChanging( prevValue, nextValue, false );
		this.handleValueChange( nextValue );
//		for( org.lgna.croquet.components.JComponent< ? > component : this.getComponents() ) {
//			component.revalidateAndRepaint();
//		}
		this.fireChanged( prevValue, nextValue, false );
	}
	
	public abstract T getValue();
	public final void setValue( T value ) {
		T prevValue = this.getValue();
		this.changeValue( prevValue, value, false );
	}
	
	
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit< ? > edit ) {
		if( edit instanceof org.lgna.croquet.edits.StateEdit ) {
			org.lgna.croquet.edits.StateEdit< ?, T > stateEdit = (org.lgna.croquet.edits.StateEdit< ?, T >)edit;
			T a = this.getValue();
			T b = stateEdit.getNextValue();
			return edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( a, b );
		} else {
			return false;
		}
	}
}
