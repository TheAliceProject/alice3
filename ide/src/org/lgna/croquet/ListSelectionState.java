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
public abstract class ListSelectionState<T> extends ItemState<T> implements Iterable<T>/* , java.util.List<E> */{
	private class DataIndexPair implements javax.swing.ComboBoxModel {
		private final org.lgna.croquet.data.ListData<T> data;
		private int index;

		public DataIndexPair( org.lgna.croquet.data.ListData<T> data, int index ) {
			this.data = data;
			this.index = index;
		}

		public void addListDataListener( javax.swing.event.ListDataListener listDataListener ) {
			this.data.addListener( listDataListener );
		}

		public void removeListDataListener( javax.swing.event.ListDataListener listDataListener ) {
			this.data.removeListener( listDataListener );
		}

		public int getSize() {
			return this.data.getItemCount();
		}

		public T getElementAt( int index ) {
			return this.data.getItemAt( index );
		}

		public T getSelectedItem() {
			if( this.index != -1 ) {
				return this.getElementAt( this.index );
			} else {
				return null;
			}
		}

		public void setSelectedItem( Object item ) {
			int index = this.data.indexOf( (T)item );
			ListSelectionState.this.swingModel.listSelectionModel.setSelectionInterval( index, index );
		}
	}

	private final javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			int index = swingModel.listSelectionModel.getLeadSelectionIndex();
			T nextValue;
			if( index != -1 ) {
				nextValue = (T)ListSelectionState.this.swingModel.comboBoxModel.getElementAt( index );
			} else {
				nextValue = null;
			}
			ListSelectionState.this.changeValueFromSwing( nextValue, IsAdjusting.valueOf( e.getValueIsAdjusting() ), org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
		}
	};

	public static class SwingModel {
		private final javax.swing.ComboBoxModel comboBoxModel;
		private final javax.swing.DefaultListSelectionModel listSelectionModel;

		private SwingModel( javax.swing.ComboBoxModel comboBoxModel, javax.swing.DefaultListSelectionModel listSelectionModel ) {
			this.comboBoxModel = comboBoxModel;
			this.listSelectionModel = listSelectionModel;
		}

		public javax.swing.ComboBoxModel getComboBoxModel() {
			return this.comboBoxModel;
		}

		public javax.swing.ListSelectionModel getListSelectionModel() {
			return this.listSelectionModel;
		}

		/* package-private */void fireListSelectionChanged( int firstIndex, int lastIndex, boolean isAdjusting ) {
			javax.swing.event.ListSelectionEvent e = new javax.swing.event.ListSelectionEvent( this, firstIndex, lastIndex, isAdjusting );
			for( javax.swing.event.ListSelectionListener listener : this.listSelectionModel.getListSelectionListeners() ) {
				listener.valueChanged( e );
			}
		}

		private void ACCESS_fireContentsChanged( Object source, int index0, int index1 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: fireContentsChanged", source, index0, index1 );
		}

		private void ACCESS_fireIntervalAdded( Object source, int index0, int index1 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: fireIntervalAdded", source, index0, index1 );
		}

		private void ACCESS_fireIntervalRemoved( Object source, int index0, int index1 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: fireIntervalRemoved", source, index0, index1 );
		}
	}

	private final DataIndexPair dataIndexPair;
	private final SwingModel swingModel;

	private static <T> T getItemAt( org.lgna.croquet.data.ListData<T> data, int index ) {
		if( index != -1 ) {
			return data.getItemAt( index );
		} else {
			return null;
		}
	}

	public ListSelectionState( Group group, java.util.UUID id, org.lgna.croquet.data.ListData<T> data, int selectionIndex ) {
		super( group, id, getItemAt( data, selectionIndex ), data.getItemCodec() );
		this.dataIndexPair = new DataIndexPair( data, selectionIndex );
		this.swingModel = new SwingModel( this.dataIndexPair, new javax.swing.DefaultListSelectionModel() );
		this.swingModel.listSelectionModel.addListSelectionListener( this.listSelectionListener );
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

	public org.lgna.croquet.data.ListData<T> getData() {
		return this.dataIndexPair.data;
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	@Override
	protected void localize() {
	}

	private InternalPrepModel<T> prepModel;

	public synchronized InternalPrepModel<T> getPrepModel() {
		if( this.prepModel != null ) {
			//pass
		} else {
			this.prepModel = new InternalPrepModel<T>( this );
		}
		return this.prepModel;
	}

	@Override
	public Iterable<? extends org.lgna.croquet.PrepModel> getPotentialRootPrepModels() {
		//todo: 
		return java.util.Collections.emptyList();
	}

	protected void handleMissingItem( T missingItem ) {
	}

	@Override
	protected T getSwingValue() {
		int index = this.swingModel.listSelectionModel.getLeadSelectionIndex();
		if( index != -1 ) {
			return (T)this.swingModel.comboBoxModel.getElementAt( index );
		} else {
			return null;
		}
	}

	@Override
	protected void setSwingValue( T nextValue ) {
		if( this.dataIndexPair != null ) {
			int index = this.dataIndexPair.data.indexOf( nextValue );
			this.swingModel.listSelectionModel.setSelectionInterval( index, index );
			this.swingModel.fireListSelectionChanged( index, index, false );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
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

	public final java.util.Iterator<T> iterator() {
		return this.dataIndexPair.data.iterator();
	}

	protected void handleItemAdded( T item ) {
	}

	protected void handleItemRemoved( T item ) {
	}

	protected final void internalAddItem( T item ) {
		this.dataIndexPair.data.internalAddItem( item );
	}

	protected final void internalRemoveItem( T item ) {
		this.dataIndexPair.data.internalRemoveItem( item );
	}

	protected final void internalSetItems( java.util.Collection<T> items ) {
		this.dataIndexPair.data.internalSetItems( items );
	}

	public final void addItem( T item ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.internalAddItem( item );

			int index = this.getItemCount() - 1;
			this.fireIntervalAdded( index, index );
			this.handleItemAdded( item );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	public final void removeItem( T item ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			int index = this.indexOf( item );
			this.internalRemoveItem( item );
			this.fireIntervalRemoved( index, index );
			this.handleItemRemoved( item );
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
			java.util.Set<T> previous = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( this.toArray() );
			java.util.Set<T> next = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( items );
			java.util.List<T> added = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List<T> removed = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

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

			this.internalSetItems( items );

			//			if( items.contains( previousSelectedValue ) ) {
			this.dataIndexPair.index = this.indexOf( previousSelectedValue );
			//			} else {
			//				this.index = -1;
			//			}

			this.fireContentsChanged( 0, this.getItemCount() );
			for( T item : removed ) {
				this.handleItemRemoved( item );
			}
			for( T item : added ) {
				this.handleItemAdded( item );
			}

		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	protected void fireContentsChanged( int index0, int index1 ) {
		this.swingModel.ACCESS_fireContentsChanged( this, index0, index1 );
	}

	protected void fireIntervalAdded( int index0, int index1 ) {
		this.swingModel.ACCESS_fireIntervalAdded( this, index0, index1 );
	}

	protected void fireIntervalRemoved( int index0, int index1 ) {
		this.swingModel.ACCESS_fireIntervalRemoved( this, index0, index1 );
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

	public void setRandomSelectedValue() {
		final int N = this.getItemCount();
		int i;
		if( N > 0 ) {
			i = org.lgna.common.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.setSelectedIndex( i );
	}

	protected String getMenuText( T item ) {
		if( item != null ) {
			return item.toString();
		} else {
			return null;
		}
	}

	protected javax.swing.Icon getMenuSmallIcon( T item ) {
		return null;
	}

	public org.lgna.croquet.components.List<T> createList() {
		return new org.lgna.croquet.components.List<T>( this );
	}

	public org.lgna.croquet.components.DefaultRadioButtons<T> createVerticalDefaultRadioButtons() {
		return new org.lgna.croquet.components.DefaultRadioButtons<T>( this, true );
	}

	public org.lgna.croquet.components.DefaultRadioButtons<T> createHorizontalDefaultRadioButtons() {
		return new org.lgna.croquet.components.DefaultRadioButtons<T>( this, false );
	}

	public org.lgna.croquet.components.TrackableShape getTrackableShapeFor( T item ) {
		org.lgna.croquet.components.ItemSelectable<?, T, ?> itemSelectable = org.lgna.croquet.components.ComponentManager.getFirstComponent( this, org.lgna.croquet.components.ItemSelectable.class );
		if( itemSelectable != null ) {
			return itemSelectable.getTrackableShapeFor( item );
		} else {
			return null;
		}
	}

	public static final class InternalMenuModelResolver<T> extends IndirectResolver<InternalMenuModel<T>, ListSelectionState<T>> {
		private InternalMenuModelResolver( ListSelectionState<T> indirect ) {
			super( indirect );
		}

		public InternalMenuModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected InternalMenuModel<T> getDirect( ListSelectionState<T> indirect ) {
			return indirect.getMenuModel();
		}
	}

	private static final class InternalMenuModel<T> extends MenuModel {
		private ListSelectionState<T> listSelectionState;

		public InternalMenuModel( ListSelectionState<T> listSelectionState ) {
			super( java.util.UUID.fromString( "e33bc1ff-3790-4715-b88c-3c978aa16947" ), listSelectionState.getClass() );
			this.listSelectionState = listSelectionState;
		}

		public ListSelectionState<T> getListSelectionState() {
			return this.listSelectionState;
		}

		@Override
		public boolean isEnabled() {
			return this.listSelectionState.isEnabled();
		}

		@Override
		public void setEnabled( boolean isEnabled ) {
			this.listSelectionState.setEnabled( isEnabled );
		}

		@Override
		protected InternalMenuModelResolver<T> createResolver() {
			return new InternalMenuModelResolver<T>( this.listSelectionState );
		}

		@Override
		protected void handleShowing( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( menuItemContainer, e );
			super.handleShowing( menuItemContainer, e );
			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			for( final Object item : this.listSelectionState ) {
				Operation operation = this.listSelectionState.getItemSelectionOperation( (T)item );
				operation.initializeIfNecessary();
				javax.swing.Action action = operation.getSwingModel().getAction();
				javax.swing.JCheckBoxMenuItem jMenuItem = new javax.swing.JCheckBoxMenuItem( action );
				buttonGroup.add( jMenuItem );
				jMenuItem.setSelected( this.listSelectionState.getValue() == item );
				menuItemContainer.getViewController().getAwtComponent().add( jMenuItem );
			}
		}

		@Override
		protected void handleHiding( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
			menuItemContainer.forgetAndRemoveAllMenuItems();
			super.handleHiding( menuItemContainer, e );
		}
	}

	private InternalMenuModel<T> menuModel;

	public synchronized InternalMenuModel<T> getMenuModel() {
		if( this.menuModel != null ) {
			//pass
		} else {
			this.menuModel = new InternalMenuModel<T>( this );
		}
		return this.menuModel;
	}

	public static final class InternalPrepModelResolver<T> extends IndirectResolver<InternalPrepModel<T>, ListSelectionState<T>> {
		private InternalPrepModelResolver( ListSelectionState<T> indirect ) {
			super( indirect );
		}

		public InternalPrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected InternalPrepModel<T> getDirect( ListSelectionState<T> indirect ) {
			return indirect.getPrepModel();
		}
	}

	public static final class InternalPrepModel<T> extends AbstractPrepModel {
		private final ListSelectionState<T> listSelectionState;

		private InternalPrepModel( ListSelectionState<T> listSelectionState ) {
			super( java.util.UUID.fromString( "c4b634e1-cd4f-465d-b0af-ab8d76cc7842" ) );
			assert listSelectionState != null;
			this.listSelectionState = listSelectionState;
		}

		@Override
		public Iterable<? extends Model> getChildren() {
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.listSelectionState );
		}

		@Override
		protected void localize() {
		}

		@Override
		public org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger ) {
			throw new RuntimeException();
		}

		public ListSelectionState<T> getListSelectionState() {
			return this.listSelectionState;
		}

		@Override
		public boolean isEnabled() {
			return this.listSelectionState.isEnabled();
		}

		@Override
		public void setEnabled( boolean isEnabled ) {
			this.listSelectionState.setEnabled( isEnabled );
		}

		@Override
		protected InternalPrepModelResolver<T> createResolver() {
			return new InternalPrepModelResolver<T>( this.listSelectionState );
		}

		public org.lgna.croquet.components.ComboBox<T> createComboBox() {
			return new org.lgna.croquet.components.ComboBox<T>( this );
		}

		@Override
		protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit ) {
			if( edit != null ) {
				org.lgna.croquet.edits.StateEdit<T> stateEdit = (org.lgna.croquet.edits.StateEdit<T>)edit;
				rv.append( "First press on " );
				rv.append( "<strong>" );
				this.getListSelectionState().appendRepresentation( rv, stateEdit.getPreviousValue() );
				rv.append( "</strong>" );
				rv.append( " in order to change it to " );
				rv.append( "<strong>" );
				this.getListSelectionState().appendRepresentation( rv, stateEdit.getNextValue() );
				rv.append( "</strong>." );
			}
			return rv;
		}
	}
}
