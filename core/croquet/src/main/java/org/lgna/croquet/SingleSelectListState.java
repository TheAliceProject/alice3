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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.Lazy;
import org.lgna.croquet.data.ListData;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.liststate.SingleSelectListStateMenuModel;
import org.lgna.croquet.imp.liststate.SingleSelectListStateSwingModel;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.croquet.views.DefaultRadioButtons;
import org.lgna.croquet.views.List;
import org.lgna.croquet.views.renderers.ItemCodecListCellRenderer;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class SingleSelectListState<T, D extends ListData<T>> extends ItemState<T> implements Iterable<T> {
	private class DataIndexPair implements ComboBoxModel {
		public DataIndexPair( D data, int index ) {
			this.data = data;
			this.index = index;
		}

		@Override
		public void addListDataListener( ListDataListener listDataListener ) {
			this.data.addListener( listDataListener );
		}

		@Override
		public void removeListDataListener( ListDataListener listDataListener ) {
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
			SingleSelectListState.this.swingModel.setSelectionIndex( index );

			//todo: update this.index???
		}

		private final D data;
		private int index;
	}

	private static <T> T getItemAt( ListData<T> data, int index ) {
		if( index != -1 ) {
			if( index < data.getItemCount() ) {
				return data.getItemAt( index );
			} else {
				Logger.errln( "note: index out of bounds", index, data.getItemCount(), data );
				return null;
			}
		} else {
			return null;
		}
	}

	public SingleSelectListState( Group group, UUID id, int selectionIndex, D data ) {
		super( group, id, getItemAt( data, selectionIndex ), data.getItemCodec() );
		this.dataIndexPair = new DataIndexPair( data, selectionIndex );
		swingModel = new SingleSelectListStateSwingModel( this.dataIndexPair );
		swingModel.getListSelectionModel().addListSelectionListener( this.listSelectionListener );
	}

	// TODO Remove view from model
	public SingleSelectListStateSwingModel getSwingModel() {
		return this.swingModel;
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
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		//todo:
		return Collections.emptyList();
	}

	@Override
	protected boolean isSwingValueValid() {
		int index = this.swingModel.getSelectionIndex();
		return ( -1 <= index ) && ( index < this.getItemCount() );
	}

	@Override
	protected T getSwingValue() {
		int index = this.swingModel.getSelectionIndex();
		if( index != -1 ) {
			return (T)this.swingModel.getComboBoxModel().getElementAt( index );
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
					Logger.severe( "indices do not match", index, this.dataIndexPair.index, nextValue, this );
					index = this.dataIndexPair.index;
				}
			}
			isInTheMidstOfSettingSwingValue = true;
			try {
				this.swingModel.setSelectionIndex( index );
			} finally {
				isInTheMidstOfSettingSwingValue = false;
			}
			this.swingModel.fireListSelectionChanged( index, index, false );
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
			Random random = new Random();
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
	public final Iterator<T> iterator() {
		return this.dataIndexPair.data.iterator();
	}

	void fireContentsChanged( int index0, int index1 ) {
		Logger.errln( "todo: fireContentsChanged", this, index0, index1 );
	}

	void fireIntervalAdded( int index0, int index1 ) {
		Logger.errln( "todo: fireIntervalAdded", this, index0, index1 );
	}

	void fireIntervalRemoved( int index0, int index1 ) {
		Logger.errln( "todo: fireIntervalRemoved", this, index0, index1 );
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

	public final void setItems( Collection<T> items ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			Set<T> previous = Sets.newHashSet( this.toArray() );
			Set<T> next = Sets.newHashSet( items );
			java.util.List<T> added = Lists.newLinkedList();
			java.util.List<T> removed = Lists.newLinkedList();

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
		this.setItems( Arrays.asList( items ) );
	}

	public void clear() {
		Collection<T> items = Collections.emptyList();
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
	public void setListData( int selectedIndex, Collection<T> items ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.setItems( items );
			this.setSelectedIndex( selectedIndex );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public List<T> createList() {
		return new List<T>( this );
	}

	public List<T> createListWithItemCodecListCellRenderer() {
		List<T> rv = this.createList();
		rv.setCellRenderer( new ItemCodecListCellRenderer<T>( this.getItemCodec() ) );
		return rv;
	}

	public DefaultRadioButtons<T> createVerticalDefaultRadioButtons() {
		return new DefaultRadioButtons<T>( this, true );
	}

	public DefaultRadioButtons<T> createHorizontalDefaultRadioButtons() {
		return new DefaultRadioButtons<T>( this, false );
	}

	public MenuModel getMenuModel() {
		return menuModelLazy.get();
	}

	private class EmptyConditionText extends PlainStringValue {
		public EmptyConditionText() {
			super( UUID.fromString( "c71e2755-d05a-4676-87db-99b3baec044d" ) );
		}

		@Override
		protected Class<? extends Element> getClassUsedForLocalization() {
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
	private final SingleSelectListStateSwingModel swingModel;
	private final Lazy<MenuModel> menuModelLazy = new Lazy<MenuModel>() {
		@Override
		protected MenuModel create() {
			return new SingleSelectListStateMenuModel<T, D>( SingleSelectListState.this );
		}
	};
	private final PlainStringValue emptyConditionText = new EmptyConditionText();
	private final ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged( ListSelectionEvent e ) {
			if( isInTheMidstOfSettingSwingValue ) {
				//pass
			} else {
				int index = swingModel.getSelectionIndex();
				T nextValue;
				if( index != -1 ) {
					nextValue = (T)swingModel.getComboBoxModel().getElementAt( index );
				} else {
					nextValue = null;
				}
				// TODO Carry through a user activity on a UI element
				final UserActivity activity = NullTrigger.createUserActivity();
				SingleSelectListState.this.changeValueFromSwing( nextValue, IsAdjusting.valueOf( e.getValueIsAdjusting() ), activity.getTrigger());
				if (activity.isPending()) {
					activity.finish();
				}
			}
		}
	};

	private final Lazy<SingleSelectListStateComboBoxPrepModel<T, D>> comboBoxPrepModelLazy = new Lazy<SingleSelectListStateComboBoxPrepModel<T, D>>() {
		@Override
		protected SingleSelectListStateComboBoxPrepModel<T, D> create() {
			return new SingleSelectListStateComboBoxPrepModel<T, D>( SingleSelectListState.this );
		}
	};
	private boolean isInTheMidstOfSettingSwingValue;
}
