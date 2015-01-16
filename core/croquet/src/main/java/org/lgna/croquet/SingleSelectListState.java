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
public abstract class SingleSelectListState<T, D extends org.lgna.croquet.data.ListData<T>> extends ItemState<T> implements Iterable<T>/* , java.util.List<E> */{
	private class DataIndexPair implements javax.swing.ComboBoxModel {
		public DataIndexPair( D data, int index ) {
			this.data = data;
			this.index = index;
		}

		@Override
		public void addListDataListener( javax.swing.event.ListDataListener listDataListener ) {
			this.data.addListener( listDataListener );
		}

		@Override
		public void removeListDataListener( javax.swing.event.ListDataListener listDataListener ) {
			this.data.removeListener( listDataListener );
		}

		@Override
		public int getSize() {
			return this.data.getItemCount();
		}

		@Override
		public T getElementAt( int index ) {
			if( index != -1 ) {
				return this.data.getItemAt( index );
			} else {
				return null;
			}
		}

		@Override
		public T getSelectedItem() {
			if( this.index != -1 ) {
				return this.getElementAt( this.index );
			} else {
				return null;
			}
		}

		@Override
		public void setSelectedItem( Object item ) {
			int index = this.data.indexOf( (T)item );
			SingleSelectListState.this.imp.getSwingModel().setSelectionIndex( index );

			//todo: update this.index???
		}

		private final D data;
		private int index;
	}

	private static <T> T getItemAt( org.lgna.croquet.data.ListData<T> data, int index ) {
		if( index != -1 ) {
			if( index < data.getItemCount() ) {
				return data.getItemAt( index );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "note: index out of bounds", index, data.getItemCount(), data );
				return null;
			}
		} else {
			return null;
		}
	}

	public SingleSelectListState( Group group, java.util.UUID id, int selectionIndex, D data ) {
		super( group, id, getItemAt( data, selectionIndex ), data.getItemCodec() );
		this.dataIndexPair = new DataIndexPair( data, selectionIndex );
		this.imp = new org.lgna.croquet.imp.liststate.SingleSelectListStateImp<T, D>( this, new org.lgna.croquet.imp.liststate.SingleSelectListStateSwingModel( this.dataIndexPair ) );
		this.imp.getSwingModel().getListSelectionModel().addListSelectionListener( this.listSelectionListener );
	}

	public org.lgna.croquet.imp.liststate.SingleSelectListStateImp<T, D> getImp() {
		return this.imp;
	}

	@Override
	protected T getCurrentTruthAndBeautyValue() {
		return getItemAt( this.dataIndexPair.data, this.dataIndexPair.index );
	}

	@Override
	protected void setCurrentTruthAndBeautyValue( T value ) {
		if( value != null ) {
			this.dataIndexPair.index = this.dataIndexPair.data.indexOf( value );
		} else {
			this.dataIndexPair.index = -1;
		}
	}

	public final D getData() {
		return this.dataIndexPair.data;
	}

	@Override
	protected void localize() {
	}

	public PlainStringValue getEmptyConditionText() {
		this.emptyConditionText.initializeIfNecessary();
		return this.emptyConditionText;
	}

	public SingleSelectListStateComboBoxPrepModel<T, D> getPrepModel() {
		return this.comboBoxPrepModelLazy.get();
	}

	@Override
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		//todo:
		return java.util.Collections.emptyList();
	}

	@Override
	protected boolean isSwingValueValid() {
		int index = this.imp.getSwingModel().getSelectionIndex();
		return ( -1 <= index ) && ( index < this.getItemCount() );
	}

	@Override
	protected T getSwingValue() {
		int index = this.imp.getSwingModel().getSelectionIndex();
		if( index != -1 ) {
			return (T)this.imp.getSwingModel().getComboBoxModel().getElementAt( index );
		} else {
			return null;
		}
	}

	@Override
	protected void setSwingValue( T nextValue ) {
		if( this.dataIndexPair != null ) {
			int index = this.dataIndexPair.data.indexOf( nextValue );
			if( index != this.dataIndexPair.index ) {
				if( ( -1 <= index ) && ( index < this.dataIndexPair.data.getItemCount() ) ) {
					//pass.getLeadSelectionIndex()
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "indices do not match", index, this.dataIndexPair.index, nextValue, this );
					index = this.dataIndexPair.index;
				}
			}
			isInTheMidstOfSettingSwingValue = true;
			try {
				this.imp.getSwingModel().setSelectionIndex( index );
			} finally {
				isInTheMidstOfSettingSwingValue = false;
			}
			this.imp.getSwingModel().fireListSelectionChanged( index, index, false );
		}
	}

	public int getSelectedIndex() {
		return this.dataIndexPair.index;
	}

	public void setSelectedIndex( int nextIndex ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.dataIndexPair.index = nextIndex;
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public void setRandomSelectedValue() {
		final int N = this.getItemCount();
		int i;
		if( N > 0 ) {
			java.util.Random random = new java.util.Random();
			i = random.nextInt( N );
		} else {
			i = -1;
		}
		this.setSelectedIndex( i );
	}

	public final void clearSelection() {
		this.setSelectedIndex( -1 );
	}

	public final T getItemAt( int index ) {
		return this.dataIndexPair.data.getItemAt( index );
	}

	public final int getItemCount() {
		return this.dataIndexPair.data.getItemCount();
	}

	public final T[] toArray() {
		return this.dataIndexPair.data.toArray();
	}

	public final int indexOf( T item ) {
		return this.dataIndexPair.data.indexOf( item );
	}

	public final boolean containsItem( T item ) {
		return indexOf( item ) != -1;
	}

	@Override
	public final java.util.Iterator<T> iterator() {
		return this.dataIndexPair.data.iterator();
	}

	protected void fireContentsChanged( int index0, int index1 ) {
		this.imp.getSwingModel().ACCESS_fireContentsChanged( this, index0, index1 );
	}

	protected void fireIntervalAdded( int index0, int index1 ) {
		this.imp.getSwingModel().ACCESS_fireIntervalAdded( this, index0, index1 );
	}

	protected void fireIntervalRemoved( int index0, int index1 ) {
		this.imp.getSwingModel().ACCESS_fireIntervalRemoved( this, index0, index1 );
	}

	public final void addItem( int index, T item ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.dataIndexPair.data.internalAddItem( index, item );
			this.fireIntervalAdded( index, index );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public final void addItem( T item ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.dataIndexPair.data.internalAddItem( item );

			int index = this.getItemCount() - 1;
			this.fireIntervalAdded( index, index );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public final void removeItem( T item ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			int index = this.indexOf( item );
			this.dataIndexPair.data.internalRemoveItem( item );
			this.fireIntervalRemoved( index, index );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public final void removeItemAndSelectAppropriateReplacement( T item ) {
		T prevSelection = this.getValue();
		this.removeItem( item );
		if( prevSelection == item ) {
			//todo:
			if( this.getItemCount() > 0 ) {
				this.setValueTransactionlessly( this.getItemAt( 0 ) );
			}
		}
	}

	public final void setItems( java.util.Collection<T> items ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			java.util.Set<T> previous = edu.cmu.cs.dennisc.java.util.Sets.newHashSet( this.toArray() );
			java.util.Set<T> next = edu.cmu.cs.dennisc.java.util.Sets.newHashSet( items );
			java.util.List<T> added = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.List<T> removed = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

			for( T item : previous ) {
				if( next.contains( item ) ) {
					//pass
				} else {
					removed.add( item );
				}
			}
			for( T item : next ) {
				if( previous.contains( item ) ) {
					//pass
				} else {
					added.add( item );
				}
			}

			T previousSelectedValue = this.getValue();

			this.dataIndexPair.data.internalSetAllItems( items );

			//			if( items.contains( previousSelectedValue ) ) {
			this.dataIndexPair.index = this.indexOf( previousSelectedValue );
			//			} else {
			//				this.index = -1;
			//			}

			this.fireContentsChanged( 0, this.getItemCount() );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public final void setItems( T... items ) {
		this.setItems( java.util.Arrays.asList( items ) );
	}

	public void clear() {
		java.util.Collection<T> items = java.util.Collections.emptyList();
		this.setListData( -1, items );
	}

	@Deprecated
	public void setListData( int selectedIndex, T... items ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.setItems( items );
			this.setSelectedIndex( selectedIndex );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	@Deprecated
	public void setListData( int selectedIndex, java.util.Collection<T> items ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.setItems( items );
			this.setSelectedIndex( selectedIndex );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public org.lgna.croquet.views.List<T> createList() {
		return new org.lgna.croquet.views.List<T>( this );
	}

	public org.lgna.croquet.views.List<T> createListWithItemCodecListCellRenderer() {
		org.lgna.croquet.views.List<T> rv = this.createList();
		rv.setCellRenderer( new org.lgna.croquet.views.renderers.ItemCodecListCellRenderer<T>( this.getItemCodec() ) );
		return rv;
	}

	public org.lgna.croquet.views.DefaultRadioButtons<T> createVerticalDefaultRadioButtons() {
		return new org.lgna.croquet.views.DefaultRadioButtons<T>( this, true );
	}

	public org.lgna.croquet.views.DefaultRadioButtons<T> createHorizontalDefaultRadioButtons() {
		return new org.lgna.croquet.views.DefaultRadioButtons<T>( this, false );
	}

	public org.lgna.croquet.views.TrackableShape getTrackableShapeFor( T item ) {
		org.lgna.croquet.views.ItemSelectable<?, T, ?> itemSelectable = org.lgna.croquet.views.ComponentManager.getFirstComponent( this, org.lgna.croquet.views.ItemSelectable.class );
		if( itemSelectable != null ) {
			return itemSelectable.getTrackableShapeFor( item );
		} else {
			return null;
		}
	}

	public MenuModel getMenuModel() {
		return this.imp.getMenuModel();
	}

	private class EmptyConditionText extends PlainStringValue {
		public EmptyConditionText() {
			super( java.util.UUID.fromString( "c71e2755-d05a-4676-87db-99b3baec044d" ) );
		}

		@Override
		protected Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return SingleSelectListState.this.getClassUsedForLocalization();
		}

		@Override
		protected String getSubKeyForLocalization() {
			StringBuilder sb = new StringBuilder();
			String subKey = SingleSelectListState.this.getSubKeyForLocalization();
			if( subKey != null ) {
				sb.append( subKey );
				sb.append( "." );
			}
			sb.append( "emptyConditionText" );
			return sb.toString();
		}
	}

	private final DataIndexPair dataIndexPair;
	private final org.lgna.croquet.imp.liststate.SingleSelectListStateImp<T, D> imp;
	private final PlainStringValue emptyConditionText = new EmptyConditionText();
	private final javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		@Override
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			if( isInTheMidstOfSettingSwingValue ) {
				//pass
			} else {
				int index = imp.getSwingModel().getSelectionIndex();
				T nextValue;
				if( index != -1 ) {
					nextValue = (T)imp.getSwingModel().getComboBoxModel().getElementAt( index );
				} else {
					nextValue = null;
				}
				SingleSelectListState.this.changeValueFromSwing( nextValue, IsAdjusting.valueOf( e.getValueIsAdjusting() ), org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
			}
		}
	};

	private final edu.cmu.cs.dennisc.pattern.Lazy<SingleSelectListStateComboBoxPrepModel<T, D>> comboBoxPrepModelLazy = new edu.cmu.cs.dennisc.pattern.Lazy<SingleSelectListStateComboBoxPrepModel<T, D>>() {
		@Override
		protected org.lgna.croquet.SingleSelectListStateComboBoxPrepModel<T, D> create() {
			return new SingleSelectListStateComboBoxPrepModel<T, D>( SingleSelectListState.this );
		}
	};
	private boolean isInTheMidstOfSettingSwingValue;
}
