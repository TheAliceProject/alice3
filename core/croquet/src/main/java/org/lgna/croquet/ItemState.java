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
import edu.cmu.cs.dennisc.java.lang.callable.ValueCallable;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Objects;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.edits.StateEdit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Transaction;
import org.lgna.croquet.triggers.ItemEventTrigger;
import org.lgna.croquet.triggers.Trigger;

import javax.swing.JToggleButton;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author Dennis Cosgrove
 */
public abstract class ItemState<T> extends SimpleValueState<T> { //todo: extend State
	private final ItemCodec<T> itemCodec;

	public ItemState( Group group, UUID id, T initialValue, ItemCodec<T> itemCodec ) {
		super( group, id, initialValue );
		//assert itemCodec != null;
		if( itemCodec != null ) {
			//pass
		} else {
			Logger.severe( "itemCodec is null for", this );
		}
		this.itemCodec = itemCodec;
	}

	@Override
	public Class<T> getItemClass() {
		return this.itemCodec.getValueClass();
	}

	@Override
	public final T decodeValue( BinaryDecoder binaryDecoder ) {
		return this.itemCodec.decodeValue( binaryDecoder );
	}

	@Override
	public final void encodeValue( BinaryEncoder binaryEncoder, T value ) {
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
	public void appendUserRepr( StringBuilder sb ) {
		this.appendRepresentation( sb, this.getValue() );
	}

	private static <T> T getItem( Callable<T> itemCallable ) {
		try {
			return itemCallable.call();
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	private static abstract class AbstractInternalItemSelectedState<T> extends BooleanState {
		private final ItemState<T> state;

		private static class RadioButtonesqueModel extends JToggleButton.ToggleButtonModel {
			private boolean isIgnoringSetSelectedFalse;

			@Override
			public void setSelected( boolean b ) {
				if( ( b == false ) && this.isIgnoringSetSelectedFalse ) {
					//pass
				} else {
					super.setSelected( b );
				}
			}

			@Override
			public void setPressed( boolean b ) {
				if( this.isSelected() ) {
					this.isIgnoringSetSelectedFalse = true;
				}
				super.setPressed( b );
				this.isIgnoringSetSelectedFalse = false;
			}
		}

		private AbstractInternalItemSelectedState( ItemState<T> state, UUID migrationId, boolean initialValue ) {
			super( state.getGroup(), UUID.fromString( "18f0b3e3-392f-49e0-adab-a6fca7816d63" ), initialValue, new RadioButtonesqueModel() );
			assert state != null;
			this.state = state;
		}

		public ItemState<T> getState() {
			return this.state;
		}

		@Override
		protected StateEdit<Boolean> createEdit( CompletionStep<State<Boolean>> completionStep, Boolean nextValue ) {
			Logger.severe( this, nextValue );
			return null;
		}
	}

	private static class InternalItemSelectedState<T> extends AbstractInternalItemSelectedState<T> {
		private final Callable<T> itemCallable;

		private InternalItemSelectedState( ItemState<T> state, Callable<T> itemCallable ) {
			super( state, UUID.fromString( "18f0b3e3-392f-49e0-adab-a6fca7816d63" ), state.getValue() == getItem( itemCallable ) );
			this.itemCallable = itemCallable;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			this.getState().getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
			this.setTextForBothTrueAndFalse( sb.toString() );
		}

		@Override
		protected StateEdit<Boolean> createEdit( CompletionStep<State<Boolean>> completionStep, Boolean nextValue ) {
			Logger.severe( this, nextValue );
			return null;
		}

		@Override
		protected void handleItemStateChanged( ItemEvent e ) {
			//note: do not invoke super
			if( e.getStateChange() == ItemEvent.SELECTED ) {
				T item = getItem( this.itemCallable );
				this.getState().changeValueFromIndirectModel( item, IsAdjusting.FALSE, ItemEventTrigger.createUserInstance( e ) );
			}
		}

		@Override
		protected void appendRepr( StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";item=" );
			this.getState().getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
		}
	}

	private static class InternalSelectItemOperation<T> extends Operation {
		private final ItemState<T> state;
		private final Callable<T> itemCallable;
		private final boolean isAlternateLocalization;

		private InternalSelectItemOperation( ItemState<T> state, Callable<T> itemCallable, boolean isAlternateLocalization ) {
			super( state.getGroup(), UUID.fromString( "6de1225e-3fb6-4bd0-9c78-1188c642325c" ) );
			assert state != null;
			this.state = state;
			this.itemCallable = itemCallable;
			this.isAlternateLocalization = isAlternateLocalization;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			if( this.isAlternateLocalization ) {
				sb.append( "Edit" );
			} else {
				this.state.getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
			}
			this.setName( sb.toString() );
		}

		@Override
		protected CompletionStep<?> createTransactionAndInvokePerform( Trigger trigger ) {
			T item = getItem( this.itemCallable );
			return this.state.changeValueFromIndirectModel( item, IsAdjusting.FALSE, trigger );
		}

		@Override
		protected final void perform( Transaction transaction, Trigger trigger ) {
			Logger.severe( this, transaction, trigger );
		}
	}

	private Map<Callable<T>, InternalItemSelectedState<T>> mapItemCallableToItemSelectedState;

	//note: itemCallable must be valid key
	public BooleanState getItemSelectedState( Callable<T> itemCallable ) {
		if( mapItemCallableToItemSelectedState != null ) {
			//pass
		} else {
			this.mapItemCallableToItemSelectedState = Maps.newHashMap();
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
		return getItemSelectedState( new ValueCallable<T>( item ) );
	}

	private Map<Callable<T>, InternalSelectItemOperation<T>> mapItemCallableToSelectionOperation;
	private Map<Callable<T>, InternalSelectItemOperation<T>> mapItemCallableToSelectionOperationAlternate;

	//note: itemCallable must be valid key

	private Map<Callable<T>, InternalSelectItemOperation<T>> getMapItemCallableToSelectionOperation( boolean isAlternateLocalization ) {
		if( isAlternateLocalization ) {
			if( mapItemCallableToSelectionOperationAlternate != null ) {
				//pass
			} else {
				this.mapItemCallableToSelectionOperationAlternate = Maps.newHashMap();
			}
			return this.mapItemCallableToSelectionOperationAlternate;
		} else {
			if( mapItemCallableToSelectionOperation != null ) {
				//pass
			} else {
				this.mapItemCallableToSelectionOperation = Maps.newHashMap();
			}
			return this.mapItemCallableToSelectionOperation;
		}
	}

	private Operation getItemSelectionOperation( Callable<T> itemCallable, boolean isAlternateLocalization ) {
		Map<Callable<T>, InternalSelectItemOperation<T>> map = getMapItemCallableToSelectionOperation( isAlternateLocalization );
		InternalSelectItemOperation<T> rv = map.get( itemCallable );
		if( rv != null ) {
			//pass
		} else {
			rv = new InternalSelectItemOperation<T>( this, itemCallable, isAlternateLocalization );
			map.put( itemCallable, rv );
		}
		return rv;
	}

	public Operation getItemSelectionOperation( Callable<T> itemCallable ) {
		return this.getItemSelectionOperation( itemCallable, false );
	}

	public final Operation getItemSelectionOperation( final T item ) {
		return this.getItemSelectionOperation( new ValueCallable<T>( item ) );
	}

	public Operation getAlternateLocalizationItemSelectionOperation( Callable<T> itemCallable ) {
		return this.getItemSelectionOperation( itemCallable, true );
	}

	public final Operation getAlternateLocalizationItemSelectionOperation( final T item ) {
		return this.getAlternateLocalizationItemSelectionOperation( new ValueCallable<T>( item ) );
	}

	@Override
	protected void fireChanged( T prevValue, T nextValue, State.IsAdjusting isAdjusting ) {
		super.fireChanged( prevValue, nextValue, isAdjusting );
		//todo
		if( this.mapItemCallableToItemSelectedState != null ) {
			for( InternalItemSelectedState<T> itemSelectedState : this.mapItemCallableToItemSelectedState.values() ) {
				T item = getItem( itemSelectedState.itemCallable );
				boolean isSelected = Objects.equals( item, nextValue );
				itemSelectedState.getImp().getSwingModel().getButtonModel().setSelected( isSelected );
			}
		}
	}
	//	@Override
	//	protected void setCurrentTruthAndBeautyValue( T value ) {
	//		super.setCurrentTruthAndBeautyValue( value );
	//		if( this.mapItemCallableToItemSelectedState != null ) {
	//			for( InternalItemSelectedState<T> itemSelectedState : this.mapItemCallableToItemSelectedState.values() ) {
	//				T item = getItem( itemSelectedState.itemCallable );
	//				boolean isSelected = item == value;
	//				itemSelectedState.setCurrentTruthAndBeautyValue( isSelected );
	//			}
	//		}
	//	}
	//
	//	@Override
	//	protected void setSwingValue( T value ) {
	//		if( this.mapItemCallableToItemSelectedState != null ) {
	//			for( InternalItemSelectedState<T> itemSelectedState : this.mapItemCallableToItemSelectedState.values() ) {
	//				T item = getItem( itemSelectedState.itemCallable );
	//				boolean isSelected = item == value;
	//				itemSelectedState.setSwingValue( isSelected );
	//			}
	//		}
	//	}
}
