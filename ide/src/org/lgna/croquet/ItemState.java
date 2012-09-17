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
public abstract class ItemState<T> extends State<T> {
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
	public final StringBuilder appendRepresentation( StringBuilder rv, T value ) {
		return this.itemCodec.appendRepresentation( rv, value );
	}

	public ItemCodec<T> getItemCodec() {
		return this.itemCodec;
	}

	@Override
	public void appendUserRepr( java.lang.StringBuilder sb ) {
		this.appendRepresentation( sb, this.getValue() );
	}

	private static class InternalItemSelectedState<T> extends BooleanState {
		private final ItemState<T> state;
		private final T item;

		private InternalItemSelectedState( ItemState<T> state, T item ) {
			super( state.getGroup(), java.util.UUID.fromString( "18f0b3e3-392f-49e0-adab-a6fca7816d63" ), state.getValue() == item );
			assert state != null;
			this.state = state;
			this.item = item;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			this.state.getItemCodec().appendRepresentation( sb, this.item );
			this.setTextForBothTrueAndFalse( sb.toString() );
		}
	}

	private java.util.Map<T, InternalItemSelectedState<T>> mapItemToItemSelectedState;

	public BooleanState getItemSelectedState( T item ) {
		if( mapItemToItemSelectedState != null ) {
			//pass
		} else {
			this.mapItemToItemSelectedState = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		}
		InternalItemSelectedState<T> rv = this.mapItemToItemSelectedState.get( item );
		if( rv != null ) {
			//pass
		} else {
			rv = new InternalItemSelectedState<T>( this, item );
			this.mapItemToItemSelectedState.put( item, rv );
		}
		return rv;
	}

	private static class InternalSelectItemOperation<T> extends ActionOperation {
		private final ItemState<T> state;
		private final T item;

		private InternalSelectItemOperation( ItemState<T> state, T item ) {
			super( state.getGroup(), java.util.UUID.fromString( "6de1225e-3fb6-4bd0-9c78-1188c642325c" ) );
			assert state != null;
			this.state = state;
			this.item = item;
		}

		@Override
		protected void localize() {
			super.localize();
			StringBuilder sb = new StringBuilder();
			this.state.getItemCodec().appendRepresentation( sb, this.item );
			this.setName( sb.toString() );
		}

		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			this.state.setValueTransactionlessly( this.item );
			step.finish();
		}
	}

	private java.util.Map<T, InternalSelectItemOperation<T>> mapItemToSelectionOperation;

	public ActionOperation getItemSelectionOperation( T item ) {
		if( mapItemToSelectionOperation != null ) {
			//pass
		} else {
			this.mapItemToSelectionOperation = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		}
		InternalSelectItemOperation<T> rv = this.mapItemToSelectionOperation.get( item );
		if( rv != null ) {
			//pass
		} else {
			rv = new InternalSelectItemOperation<T>( this, item );
			this.mapItemToSelectionOperation.put( item, rv );
		}
		return rv;
	}
}
