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
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.ItemEventTrigger;

import javax.swing.JToggleButton;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * @author Dennis Cosgrove
 */
public abstract class ItemState<T> extends State<T> {
	private final ItemCodec<T> itemCodec;

	public ItemState( Group group, UUID id, T initialValue, ItemCodec<T> itemCodec ) {
		super( group, id, initialValue );
		if ( itemCodec == null ) {
			Logger.severe( "itemCodec is null for ", this );
		}
		this.itemCodec = itemCodec;
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

	private static class RadioButtonesqueModel extends JToggleButton.ToggleButtonModel {
		private boolean isIgnoringSetSelectedFalse;

		@Override
		public void setSelected( boolean b ) {
			if ( b || !this.isIgnoringSetSelectedFalse ) {
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

	private class InternalItemSelectedState extends BooleanState {
		private final Callable<T> itemCallable;

		private InternalItemSelectedState( ItemState<T> state, Callable<T> itemCallable ) {
			super( state.getGroup(),
						 UUID.fromString( "18f0b3e3-392f-49e0-adab-a6fca7816d63" ),
						 state.getValue() == getItem( itemCallable ),
						 new RadioButtonesqueModel() );
			this.itemCallable = itemCallable;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
			this.setTextForBothTrueAndFalse( sb.toString() );
		}

		@Override
		protected void handleItemStateChanged( ItemEvent e ) {
			//note: do not invoke super
			if( e.getStateChange() == ItemEvent.SELECTED ) {
				T item = getItem( this.itemCallable );
				final UserActivity activity = ItemEventTrigger.createUserActivity( e );
				ItemState.this.changeValueFromIndirectModel( item, activity );
			}
		}

		@Override
		protected void appendRepr( StringBuilder sb ) {
			super.appendRepr( sb );
			sb.append( ";item=" );
			getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
		}
	}

	private class InternalSelectItemOperation extends Operation {
		private final Callable<T> itemCallable;
		private final boolean isAlternateLocalization;

		private InternalSelectItemOperation( ItemState<T> state, Callable<T> itemCallable, boolean isAlternateLocalization ) {
			super( state.getGroup(), UUID.fromString( "6de1225e-3fb6-4bd0-9c78-1188c642325c" ) );
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
				getItemCodec().appendRepresentation( sb, getItem( this.itemCallable ) );
			}
			this.setName( sb.toString() );
		}

		@Override
		protected final void performInActivity( UserActivity userActivity ) {
			ItemState.this.changeValueFromIndirectModel( getItem( this.itemCallable ), userActivity );
		}
	}

	private Map<Callable<T>, InternalItemSelectedState> mapItemCallableToItemSelectedState;

	//note: itemCallable must be valid key
	public BooleanState getItemSelectedState( Callable<T> itemCallable ) {
		if ( mapItemCallableToItemSelectedState == null ) {
			this.mapItemCallableToItemSelectedState = Maps.newHashMap();
		}
		InternalItemSelectedState rv = this.mapItemCallableToItemSelectedState.get( itemCallable );
		if ( rv == null ) {
			rv = new InternalItemSelectedState( this, itemCallable );
			this.mapItemCallableToItemSelectedState.put( itemCallable, rv );
		}
		return rv;
	}

	public BooleanState getItemSelectedState( T item ) {
		return getItemSelectedState( new ValueCallable<>( item ) );
	}

	private Map<Callable<T>, InternalSelectItemOperation> mapItemCallableToSelectionOperation;
	private Map<Callable<T>, InternalSelectItemOperation> mapItemCallableToSelectionOperationAlternate;

	private Map<Callable<T>, InternalSelectItemOperation> getMapItemCallableToSelectionOperation( boolean isAlternateLocalization ) {
		if( isAlternateLocalization ) {
			if ( mapItemCallableToSelectionOperationAlternate == null ) {
				this.mapItemCallableToSelectionOperationAlternate = Maps.newHashMap();
			}
			return this.mapItemCallableToSelectionOperationAlternate;
		} else {
			if ( mapItemCallableToSelectionOperation == null ) {
				this.mapItemCallableToSelectionOperation = Maps.newHashMap();
			}
			return this.mapItemCallableToSelectionOperation;
		}
	}

	private Operation getItemSelectionOperation( Callable<T> itemCallable, boolean isAlternateLocalization ) {
		Map<Callable<T>, InternalSelectItemOperation> map = getMapItemCallableToSelectionOperation( isAlternateLocalization );
		InternalSelectItemOperation rv = map.get( itemCallable );
		if ( rv == null ) {
			rv = new InternalSelectItemOperation( this, itemCallable, isAlternateLocalization );
			map.put( itemCallable, rv );
		}
		return rv;
	}

	public Operation getItemSelectionOperation( Callable<T> itemCallable ) {
		return this.getItemSelectionOperation( itemCallable, false );
	}

	public final Operation getItemSelectionOperation( final T item ) {
		return this.getItemSelectionOperation( new ValueCallable<>( item ) );
	}

	private Operation getAlternateLocalizationItemSelectionOperation( Callable<T> itemCallable ) {
		return this.getItemSelectionOperation( itemCallable, true );
	}

	public final Operation getAlternateLocalizationItemSelectionOperation( final T item ) {
		return this.getAlternateLocalizationItemSelectionOperation( new ValueCallable<>( item ) );
	}

	@Override
	protected void fireChanged( T prevValue, T nextValue, boolean isAdjusting ) {
		super.fireChanged( prevValue, nextValue, isAdjusting );
		//todo
		if( this.mapItemCallableToItemSelectedState != null ) {
			for( InternalItemSelectedState itemSelectedState : this.mapItemCallableToItemSelectedState.values() ) {
				T item = getItem( itemSelectedState.itemCallable );
				boolean isSelected = Objects.equals( item, nextValue );
				itemSelectedState.getImp().getSwingModel().getButtonModel().setSelected( isSelected );
			}
		}
	}
}
