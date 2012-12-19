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
public abstract class ItemState<T> extends SimpleValueState<T> { //todo: extend State
	private final ItemCodec<T> itemCodec;

	public ItemState( Group group, java.util.UUID id, T initialValue, ItemCodec<T> itemCodec ) {
		super( group, id, initialValue );
		//assert itemCodec != null;
		if( itemCodec != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "itemCodec is null for", this );
		}
		this.itemCodec = itemCodec;
	}

	@Override
	public Class<T> getItemClass() {
		return this.itemCodec.getValueClass();
	}

	@Override
	public T decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		return this.itemCodec.decodeValue( binaryDecoder );
	}

	@Override
	public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, T value ) {
		this.itemCodec.encodeValue( binaryEncoder, value );
	}

	@Override
	public final void appendRepresentation( StringBuilder sb, T value ) {
		this.itemCodec.appendRepresentation( sb, value );
	}

	public ItemCodec<T> getItemCodec() {
		return this.itemCodec;
	}

	@Override
	public void appendUserRepr( java.lang.StringBuilder sb ) {
		this.appendRepresentation( sb, this.getValue() );
	}

	private static <T> T getItem( java.util.concurrent.Callable<T> itemCallable ) {
		try {
			return itemCallable.call();
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	private static class InternalItemSelectedStateButtonModel<T> extends javax.swing.JToggleButton.ToggleButtonModel {
		private final java.util.concurrent.Callable<T> itemCallable;

		public InternalItemSelectedStateButtonModel( java.util.concurrent.Callable<T> itemCallable ) {
			this.itemCallable = itemCallable;
		}

		public java.util.concurrent.Callable<T> getItemCallable() {
			return this.itemCallable;
		}
	}

	private static class InternalItemSelectedState<T> extends BooleanState {
		private final ItemState<T> state;
		private final java.util.concurrent.Callable<T> itemCallable;

		private InternalItemSelectedState( ItemState<T> state, java.util.concurrent.Callable<T> itemCallable ) {
			super( state.getGroup(), java.util.UUID.fromString( "18f0b3e3-392f-49e0-adab-a6fca7816d63" ), state.getValue() == getItem( itemCallable ), new InternalItemSelectedStateButtonModel<T>( itemCallable ) );
			assert state != null;
			this.state = state;
			this.itemCallable = itemCallable;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			this.state.getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
			this.setTextForBothTrueAndFalse( sb.toString() );
		}

		@Override
		protected org.lgna.croquet.edits.StateEdit<Boolean> createEdit( org.lgna.croquet.history.CompletionStep<State<Boolean>> completionStep, Boolean nextValue ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, nextValue );
			return null;
		}

		@Override
		protected void handleItemStateChanged( java.awt.event.ItemEvent e ) {
			//note: do not invoke super
			if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
				InternalItemSelectedStateButtonModel<T> buttonModel = (InternalItemSelectedStateButtonModel<T>)e.getItem();
				T item = getItem( this.itemCallable );
				if( item == getItem( buttonModel.getItemCallable() ) ) {
					this.state.changeValueFromIndirectModel( item, IsAdjusting.FALSE, org.lgna.croquet.triggers.ItemEventTrigger.createUserInstance( e ) );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
				}
			}
		}

		@Override
		protected void appendRepr( StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";item=" );
			this.state.getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
		}
	}

	private static class InternalSelectItemOperation<T> extends ActionOperation {
		private final ItemState<T> state;
		private final java.util.concurrent.Callable<T> itemCallable;

		private InternalSelectItemOperation( ItemState<T> state, java.util.concurrent.Callable<T> itemCallable ) {
			super( state.getGroup(), java.util.UUID.fromString( "6de1225e-3fb6-4bd0-9c78-1188c642325c" ) );
			assert state != null;
			this.state = state;
			this.itemCallable = itemCallable;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			this.state.getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
			this.setName( sb.toString() );
		}

		@Override
		protected org.lgna.croquet.history.CompletionStep<?> createTransactionAndInvokePerform( org.lgna.croquet.triggers.Trigger trigger ) {
			//todo
			if( this.state.mapItemCallableToItemSelectedState != null ) {
				java.util.Collection<InternalItemSelectedState<T>> itemSelectedStates = this.state.mapItemCallableToItemSelectedState.values();
				for( InternalItemSelectedState<T> itemSelectedState : itemSelectedStates ) {
					boolean isSelected = getItem( itemSelectedState.itemCallable ) == getItem( itemCallable );
					if( isSelected ) {
						itemSelectedState.getSwingModel().getButtonModel().setSelected( isSelected );
					}
				}
			}
			return this.state.changeValueFromIndirectModel( getItem( this.itemCallable ), IsAdjusting.FALSE, trigger );
		}

		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, transaction, trigger );
		}
	}

	private java.util.Map<java.util.concurrent.Callable<T>, InternalItemSelectedState<T>> mapItemCallableToItemSelectedState;

	//note: itemCallable must be valid key
	public BooleanState getItemSelectedState( java.util.concurrent.Callable<T> itemCallable ) {
		if( mapItemCallableToItemSelectedState != null ) {
			//pass
		} else {
			this.mapItemCallableToItemSelectedState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		}
		InternalItemSelectedState<T> rv = this.mapItemCallableToItemSelectedState.get( itemCallable );
		if( rv != null ) {
			//pass
		} else {
			rv = new InternalItemSelectedState<T>( this, itemCallable );
			this.mapItemCallableToItemSelectedState.put( itemCallable, rv );
		}
		return rv;
	}

	public BooleanState getItemSelectedState( T item ) {
		return getItemSelectedState( new edu.cmu.cs.dennisc.java.lang.callable.ValueCallable<T>( item ) );
	}

	private java.util.Map<java.util.concurrent.Callable<T>, InternalSelectItemOperation<T>> mapItemCallableToSelectionOperation;

	//note: itemCallable must be valid key
	public ActionOperation getItemSelectionOperation( java.util.concurrent.Callable<T> itemCallable ) {
		if( mapItemCallableToSelectionOperation != null ) {
			//pass
		} else {
			this.mapItemCallableToSelectionOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		}
		InternalSelectItemOperation<T> rv = this.mapItemCallableToSelectionOperation.get( itemCallable );
		if( rv != null ) {
			//pass
		} else {
			rv = new InternalSelectItemOperation<T>( this, itemCallable );
			this.mapItemCallableToSelectionOperation.put( itemCallable, rv );
		}
		return rv;
	}

	public ActionOperation getItemSelectionOperation( final T item ) {
		return getItemSelectionOperation( new edu.cmu.cs.dennisc.java.lang.callable.ValueCallable<T>( item ) );
	}
}
