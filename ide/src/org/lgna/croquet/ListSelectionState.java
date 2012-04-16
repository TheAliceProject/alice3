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

import org.lgna.croquet.components.ComponentManager;

/*package-private*/class ComboBoxModel<T> extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
	private final ListSelectionState< T > listSelectionState;

	public ComboBoxModel( ListSelectionState< T > listSelectionState ) {
		this.listSelectionState = listSelectionState;
	}
	public T getSelectedItem() {
		return this.listSelectionState.getSelectedItem();
	}
	public void setSelectedItem( Object item ) {
		if( item != this.getSelectedItem() ) {
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( item, this.getSelectedItem() ) ) {
				throw new RuntimeException();
			}
			this.listSelectionState.setSelectionFromSwing( (T)item );
			this.fireContentsChanged( this, -1, -1 );
		}
	}
	public Object getElementAt( int index ) {
		return this.listSelectionState.getItemAt( index );
	}
	public int getSize() {
		return this.listSelectionState.getItemCount();
	}

	/*package-private*/void ACCESS_fireContentsChanged( Object source, int index0, int index1 ) {
		this.fireContentsChanged( source, index0, index1 );
	}
	/*package-private*/void ACCESS_fireIntervalAdded( Object source, int index0, int index1 ) {
		this.fireIntervalAdded( source, index0, index1 );
	}
	/*package-private*/void ACCESS_fireIntervalRemoved( Object source, int index0, int index1 ) {
		this.fireIntervalRemoved( source, index0, index1 );
	}
}

/*package-private*/class ListSelectionModel<T> implements javax.swing.ListSelectionModel {
	private final ListSelectionState< T > listSelectionState;
	private java.util.List< javax.swing.event.ListSelectionListener > listSelectionListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private boolean isAdjusting;

	public ListSelectionModel( ListSelectionState< T > listSelectionState ) {
		this.listSelectionState = listSelectionState;
	}

	public void addListSelectionListener( javax.swing.event.ListSelectionListener listener ) {
		this.listSelectionListeners.add( listener );
	}
	public void removeListSelectionListener( javax.swing.event.ListSelectionListener listener ) {
		this.listSelectionListeners.remove( listener );
	}
	/*package-private*/void fireListSelectionChanged( int firstIndex, int lastIndex, boolean isAdjusting ) {
		javax.swing.event.ListSelectionEvent e = new javax.swing.event.ListSelectionEvent( this, firstIndex, lastIndex, isAdjusting );
		for( javax.swing.event.ListSelectionListener listener : this.listSelectionListeners ) {
			listener.valueChanged( e );
		}
	}

	public int getSelectionMode() {
		return javax.swing.ListSelectionModel.SINGLE_SELECTION;
	}
	public void setSelectionMode( int selectionMode ) {
		assert selectionMode == javax.swing.ListSelectionModel.SINGLE_SELECTION;
	}

	public boolean getValueIsAdjusting() {
		return this.isAdjusting;
	}
	public void setValueIsAdjusting( boolean isAdjusting ) {
		this.isAdjusting = isAdjusting;
		this.fireListSelectionChanged( -1, -1, this.isAdjusting );
	}
	public boolean isSelectedIndex( int index ) {
		return this.listSelectionState.getSelectedIndex() == index;
	}
	public boolean isSelectionEmpty() {
		return this.listSelectionState.getSelectedIndex() < 0;
	}
	public int getAnchorSelectionIndex() {
		return this.listSelectionState.getSelectedIndex();
	}
	public int getLeadSelectionIndex() {
		return this.listSelectionState.getSelectedIndex();
	}
	public int getMaxSelectionIndex() {
		return this.listSelectionState.getSelectedIndex();
	}
	public int getMinSelectionIndex() {
		return this.listSelectionState.getSelectedIndex();
	}

	public void addSelectionInterval( int index0, int index1 ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( index0, index1 );
	}
	public void insertIndexInterval( int index, int length, boolean before ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( index, length, before );
	}
	public void removeIndexInterval( int index0, int index1 ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( index0, index1 );
	}
	public void removeSelectionInterval( int index0, int index1 ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( index0, index1 );
	}
	public void setAnchorSelectionIndex( int index ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( index );
	}
	public void setLeadSelectionIndex( int index ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( index );
	}
	public void setSelectionInterval( int index0, int index1 ) {
		assert index0 == index1;
		this.listSelectionState.setSelectionIndexFromSwing( index0 );
		this.fireListSelectionChanged( index0, index1, this.isAdjusting );
	}
	public void clearSelection() {
		this.setSelectionInterval( -1, -1 );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class ListSelectionState<T> extends ItemState< T > implements Iterable< T >/*, java.util.List<E>*/{
	public class SwingModel {
		private final ComboBoxModel< T > comboBoxModel;
		private final ListSelectionModel< T > listSelectionModel;

		private SwingModel( ComboBoxModel< T > comboBoxModel, ListSelectionModel< T > listSelectionModel ) {
			this.comboBoxModel = comboBoxModel;
			this.listSelectionModel = listSelectionModel;
		}
		public javax.swing.ComboBoxModel getComboBoxModel() {
			return this.comboBoxModel;
		}
		public javax.swing.ListSelectionModel getListSelectionModel() {
			return this.listSelectionModel;
		}
	}

	private final SwingModel swingModel = new SwingModel( new ComboBoxModel< T >( this ), new ListSelectionModel< T >( this ) );
	private int index = -1;

	public ListSelectionState( Group group, java.util.UUID id, ItemCodec< T > codec, int selectionIndex, T... data ) {
		super( group, id, null, codec );
		this.index = selectionIndex;
	}
	public SwingModel getSwingModel() {
		return this.swingModel;
	}
	@Override
	protected void localize() {
	}

	/*package-private*/void setSelectionIndexFromSwing( int index, org.lgna.croquet.triggers.Trigger trigger ) {
		this.pushAtomic( trigger );
		this.index = index;
		this.popAtomic();
	}
	/*package-private*/void setSelectionIndexFromSwing( int index ) {
		this.setSelectionIndexFromSwing( index, null );
	}
	/*package-private*/void setSelectionFromSwing( T item, org.lgna.croquet.triggers.Trigger trigger ) {
		this.pushAtomic( trigger );
		this.index = this.indexOf( item );
		this.popAtomic();
	}
	/*package-private*/void setSelectionFromSwing( T item ) {
		this.setSelectionFromSwing( item, null );
	}

	private InternalPrepModel< T > prepModel;
	public synchronized InternalPrepModel< T > getPrepModel() {
		if( this.prepModel != null ) {
			//pass
		} else {
			this.prepModel = new InternalPrepModel< T >( this );
		}
		return this.prepModel;
	}
	
	@Override
	public java.lang.Iterable< ? extends org.lgna.croquet.PrepModel > getPotentialRootPrepModels() {
		return null;
	}

	public javax.swing.Action createActionForItem( final T item ) {
		javax.swing.Action action = new javax.swing.AbstractAction() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				ListSelectionState.this.setSelectionFromSwing( item, new org.lgna.croquet.triggers.ActionEventTrigger( e ) );
				ListSelectionState.this.swingModel.listSelectionModel.fireListSelectionChanged( ListSelectionState.this.index, ListSelectionState.this.index, ListSelectionState.this.swingModel.listSelectionModel.getValueIsAdjusting() );
			}
		};
		action.putValue( javax.swing.Action.NAME, getMenuText( item ) );
		action.putValue( javax.swing.Action.SMALL_ICON, getMenuSmallIcon( item ) );
		return action;
	}

	@Override
	protected T getActualValue() {
		return this.getSelectedItem();
	}

	public void addListDataListener( javax.swing.event.ListDataListener listener ) {
		this.swingModel.comboBoxModel.addListDataListener( listener );
	}
	public void removeListDataListener( javax.swing.event.ListDataListener listener ) {
		this.swingModel.comboBoxModel.removeListDataListener( listener );
	}

	public T getSelectedItem() {
		if( this.index >= 0 ) {
			if( this.index < this.getItemCount() ) {
				return this.getItemAt( index );
			} else {
				//				throw new IndexOutOfBoundsException( this.index + " " + this.getItemCount() + " " + this );
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "item selection out of bounds", this.index, this.getItemCount(), this );
				return null;
			}
		} else {
			return null;
		}
	}
	protected void handleMissingItem( T missingItem ) {
	}
	public void setSelectedItem( T selectedItem ) {
		int index;
		if( selectedItem != null ) {
			index = this.indexOf( selectedItem );
			if( index == -1 ) {
				this.handleMissingItem( selectedItem );
				index = this.indexOf( selectedItem );
			}
		} else {
			index = -1;
		}
		this.setSelectedIndex( index );
	}
	@Override
	protected void updateSwingModel( T nextValue ) {
		this.setSelectedItem( nextValue );
	}
	public int getSelectedIndex() {
		return this.index;
	}
	public void setSelectedIndex( int nextIndex ) {
		this.pushAtomic();
		this.index = nextIndex;
		this.popAtomic();
	}
	public final void clearSelection() {
		this.setSelectedIndex( -1 );
	}

	public abstract T getItemAt( int index );
	public abstract int getItemCount();
	public abstract T[] toArray( Class< T > componentType );
	public final T[] toArray() {
		return this.toArray( this.getItemCodec().getValueClass() );
	}

	public abstract int indexOf( T item );
	public boolean containsItem( T item ) {
		return indexOf( item ) != -1;
	}

	protected void handleItemAdded( T item ) {
	}
	protected void handleItemRemoved( T item ) {
	}
	protected abstract void internalAddItem( T item );
	protected abstract void internalRemoveItem( T item );
	protected abstract void internalSetItems( java.util.Collection< T > items );

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

	public final void addItem( T item ) {
		this.pushAtomic();
		try {
			this.internalAddItem( item );

			int index = this.getItemCount() - 1;
			this.swingModel.comboBoxModel.ACCESS_fireIntervalAdded( this, index, index );
			this.handleItemAdded( item );
		} finally {
			this.popAtomic();
		}
	}
	public final void removeItem( T item ) {
		this.pushAtomic();
		try {
			int index = this.indexOf( item );
			this.internalRemoveItem( item );
			this.swingModel.comboBoxModel.ACCESS_fireIntervalRemoved( this, index, index );
			this.handleItemRemoved( item );
		} finally {
			this.popAtomic();
		}
	}

	public final void setItems( java.util.Collection< T > items ) {
		this.pushAtomic();
		try {
			java.util.Set< T > previous = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( this.toArray() );
			java.util.Set< T > next = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( items );
			java.util.List< T > added = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List< T > removed = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

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
			this.index = this.indexOf( previousSelectedValue );
			//			} else {
			//				this.index = -1;
			//			}

			this.swingModel.comboBoxModel.ACCESS_fireContentsChanged( this, 0, this.getItemCount() );
			for( T item : removed ) {
				this.handleItemRemoved( item );
			}
			for( T item : added ) {
				this.handleItemAdded( item );
			}

		} finally {
			this.popAtomic();
		}
	}

	//todo
	@Override
	protected void fireChanging( T prevValue, T nextValue, boolean isAdjusting ) {
		super.fireChanging( prevValue, nextValue, isAdjusting );
		this.swingModel.listSelectionModel.fireListSelectionChanged( this.index, this.index, this.swingModel.listSelectionModel.getValueIsAdjusting() );
		this.fireContentsChanged( this.index, this.index );
	}
	
	protected void fireContentsChanged( int index0, int index1 ) {
		this.swingModel.comboBoxModel.ACCESS_fireContentsChanged( this, index0, index1 );
	}
	protected void fireIntervalAdded( int index0, int index1 ) {
		this.swingModel.comboBoxModel.ACCESS_fireIntervalAdded( this, index0, index1 );
	}
	protected void fireIntervalRemoved( int index0, int index1 ) {
		this.swingModel.comboBoxModel.ACCESS_fireIntervalRemoved( this, index0, index1 );
	}

	public final void setItems( T... items ) {
		this.setItems( java.util.Arrays.asList( items ) );
	}
	public void clear() {
		java.util.Collection< T > items = java.util.Collections.emptyList();
		this.setListData( -1, items );
	}
	@Deprecated
	public void setListData( int selectedIndex, T... items ) {
		this.pushAtomic();
		try {
			this.setItems( items );
			this.setSelectedIndex( selectedIndex );
		} finally {
			this.popAtomic();
		}
	}
	@Deprecated
	public void setListData( int selectedIndex, java.util.Collection< T > items ) {
		this.pushAtomic();
		try {
			this.setItems( items );
			this.setSelectedIndex( selectedIndex );
		} finally {
			this.popAtomic();
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

	public org.lgna.croquet.components.List< T > createList() {
		return new org.lgna.croquet.components.List< T >( this );
	}
	public org.lgna.croquet.components.DefaultRadioButtons< T > createVerticalDefaultRadioButtons() {
		return new org.lgna.croquet.components.DefaultRadioButtons< T >( this, true );
	}
	public org.lgna.croquet.components.DefaultRadioButtons< T > createHorizontalDefaultRadioButtons() {
		return new org.lgna.croquet.components.DefaultRadioButtons< T >( this, false );
	}

	public org.lgna.croquet.components.TrackableShape getTrackableShapeFor( T item ) {
		org.lgna.croquet.components.ItemSelectable< ?, T > itemSelectable = ComponentManager.getFirstComponent( this, org.lgna.croquet.components.ItemSelectable.class );
		if( itemSelectable != null ) {
			return itemSelectable.getTrackableShapeFor( item );
		} else {
			return null;
		}
	}

	public static final class InternalMenuModelResolver<T> extends IndirectResolver< InternalMenuModel< T >, ListSelectionState< T > > {
		private InternalMenuModelResolver( ListSelectionState< T > indirect ) {
			super( indirect );
		}
		public InternalMenuModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalMenuModel< T > getDirect( ListSelectionState< T > indirect ) {
			return indirect.getMenuModel();
		}
	}

	private static final class InternalMenuModel<T> extends MenuModel {
		private ListSelectionState< T > listSelectionState;

		public InternalMenuModel( ListSelectionState< T > listSelectionState ) {
			super( java.util.UUID.fromString( "e33bc1ff-3790-4715-b88c-3c978aa16947" ), listSelectionState.getClass() );
			this.listSelectionState = listSelectionState;
		}
		public ListSelectionState< T > getListSelectionState() {
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
		protected InternalMenuModelResolver< T > createResolver() {
			return new InternalMenuModelResolver< T >( this.listSelectionState );
		}
		@Override
		protected void handleShowing( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( menuItemContainer, e );
			super.handleShowing( menuItemContainer, e );
			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			for( final Object item : this.listSelectionState ) {
				javax.swing.Action action = this.listSelectionState.createActionForItem( (T)item );
				javax.swing.JCheckBoxMenuItem jMenuItem = new javax.swing.JCheckBoxMenuItem( action );
				buttonGroup.add( jMenuItem );
				jMenuItem.setSelected( this.listSelectionState.getSelectedItem() == item );
				menuItemContainer.getViewController().getAwtComponent().add( jMenuItem );
			}
		}
		@Override
		protected void handleHiding( org.lgna.croquet.components.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
			menuItemContainer.forgetAndRemoveAllMenuItems();
			super.handleHiding( menuItemContainer, e );
		}
	}
	private InternalMenuModel< T > menuModel;
	public synchronized InternalMenuModel< T > getMenuModel() {
		if( this.menuModel != null ) {
			//pass
		} else {
			this.menuModel = new InternalMenuModel< T >( this );
		}
		return this.menuModel;
	}
	
	public static final class InternalPrepModelResolver<T> extends IndirectResolver< InternalPrepModel<T>, ListSelectionState< T > > {
		private InternalPrepModelResolver( ListSelectionState< T > indirect ) {
			super( indirect );
		}
		public InternalPrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalPrepModel<T> getDirect( ListSelectionState< T > indirect ) {
			return indirect.getPrepModel();
		}
	}
	
	public static final class InternalPrepModel<T> extends AbstractPrepModel {
		private final ListSelectionState< T > listSelectionState;
		private InternalPrepModel( ListSelectionState< T > listSelectionState ) {
			super( java.util.UUID.fromString( "c4b634e1-cd4f-465d-b0af-ab8d76cc7842" ) );
			assert listSelectionState != null;
			this.listSelectionState = listSelectionState;
		}
		@Override
		public Iterable< ? extends Model > getChildren() {
			return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( this.listSelectionState );
		}
		@Override
		protected void localize() {
		}
		@Override
		public org.lgna.croquet.history.Step<?> fire(org.lgna.croquet.triggers.Trigger trigger) {
			throw new RuntimeException();
		}
		public ListSelectionState< T > getListSelectionState() {
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
		public org.lgna.croquet.components.ComboBox< T > createComboBox() {
			return new org.lgna.croquet.components.ComboBox< T >( this );
		}
		@Override
		protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
			if( edit != null ) {
				org.lgna.croquet.edits.StateEdit< T > stateEdit = (org.lgna.croquet.edits.StateEdit< T >)edit;
				rv.append( "First press on " );
				rv.append( "<strong>" );
				this.getListSelectionState().appendRepresentation( rv, stateEdit.getPreviousValue(), userInformation.getLocale() );
				rv.append( "</strong>" );
				rv.append( " in order to change it to " );
				rv.append( "<strong>" );
				this.getListSelectionState().appendRepresentation( rv, stateEdit.getNextValue(), userInformation.getLocale() );
				rv.append( "</strong>." );
			}
			return rv;
		}
	}

	private static class InternalSelectItemOperation<T> extends ActionOperation {
		private final ListSelectionState< T > listSelectionState;
		private final T item;
		private InternalSelectItemOperation( ListSelectionState< T > listSelectionState, T item ) {
			super( listSelectionState.getGroup(), java.util.UUID.fromString( "6de1225e-3fb6-4bd0-9c78-1188c642325c" ) );
			assert listSelectionState != null;
			this.listSelectionState = listSelectionState;
			this.item = item;
		}
		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			this.listSelectionState.setValueTransactionlessly( this.item );
			step.finish();
		}
	}
	private java.util.Map< T, InternalSelectItemOperation<T> > mapItemToSelectionOperation;
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
			rv = new InternalSelectItemOperation< T >( this, item );
			this.mapItemToSelectionOperation.put( item, rv );
		}
		return rv;
	}
}
