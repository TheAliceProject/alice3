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
package edu.cmu.cs.dennisc.croquet;

/*package-private*/ class ComboBoxModel<E> extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
	private final ListSelectionState< E > listSelectionState;
	public ComboBoxModel( ListSelectionState< E > listSelectionState ) {
		this.listSelectionState = listSelectionState;
	}
	public E getSelectedItem() {
		return this.listSelectionState.getSelectedItem();
	}

	public void setSelectedItem( Object item ) {
		if( item != this.getSelectedItem() ) {
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( item, this.getSelectedItem() ) ) {
				throw new RuntimeException();
			}
			this.listSelectionState.setSelectionFromSwing( (E)item );
			this.fireContentsChanged( this, -1, -1 );
		}
	}

	public Object getElementAt( int index ) {
		return this.listSelectionState.getItemAt( index );
	}

	public int getSize() {
		return this.listSelectionState.getItemCount();
	}
	
	@Override
	protected void fireContentsChanged( java.lang.Object source, int index0, int index1 ) {
		super.fireContentsChanged( source, index0, index1 );
	}
	@Override
	protected void fireIntervalAdded( java.lang.Object source, int index0, int index1 ) {
		super.fireIntervalAdded( source, index0, index1 );
	}
	@Override
	protected void fireIntervalRemoved( java.lang.Object source, int index0, int index1 ) {
		super.fireIntervalRemoved( source, index0, index1 );
	}
}


/*package-private*/ class ListSelectionModel<E> implements javax.swing.ListSelectionModel {
	private final ListSelectionState< E > listSelectionState;
	private java.util.List< javax.swing.event.ListSelectionListener > listSelectionListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private boolean isAdjusting;

	public ListSelectionModel( ListSelectionState< E > listSelectionState ) {
		this.listSelectionState = listSelectionState;
	}

	public void addListSelectionListener( javax.swing.event.ListSelectionListener listener ) {
		this.listSelectionListeners.add( listener );
	}
	public void removeListSelectionListener( javax.swing.event.ListSelectionListener listener ) {
		this.listSelectionListeners.remove( listener );
	}
	/*package-private*/ void fireListSelectionChanged( int firstIndex, int lastIndex, boolean isAdjusting ) {
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
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: addSelectionInterval" );
	}
	public void insertIndexInterval( int index, int length, boolean before ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: insertIndexInterval" );
	}
	public void removeIndexInterval( int index0, int index1 ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: removeIndexInterval" );
	}
	public void removeSelectionInterval( int index0, int index1 ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: removeSelectionInterval" );
	}
	public void setAnchorSelectionIndex( int index ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: setAnchorSelectionIndex" );
	}
	public void setLeadSelectionIndex( int index ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: setLeadSelectionIndex" );
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
public abstract class ListSelectionState<E> extends State< E > implements Iterable< E >/*, java.util.List<E>*/{
	public static interface ValueObserver<E> {
		public void changed( E nextValue );
	}

	private final Codec< E > codec;
	private final ComboBoxModel< E > comboBoxModel = new ComboBoxModel< E >( this );
	private final ListSelectionModel< E > listSelectionModel = new ListSelectionModel< E >( this );
	private final java.util.List< ValueObserver< E > > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	
	/*package-private*/ void setSelectionIndexFromSwing( int index ) {
		this.pushAtomic();
		this.index = index;
		this.popAtomic();
	}
	/*package-private*/ void setSelectionFromSwing( E item ) {
		this.pushAtomic();
		this.index = this.indexOf( item );
		this.popAtomic();
	}
	
	private int index = -1;
//	private int indexOfLastPerform = -1;

	private ViewController< ?, ? > mostRecentViewController;
	private java.util.EventObject mostRecentEvent;
	public void setMostRecentEventAndViewController( java.util.EventObject mostRecentEvent, ViewController< ?, ? > mostRecentViewController ) {
		this.mostRecentEvent = mostRecentEvent;
		this.mostRecentViewController = mostRecentViewController;
	}

	private int pushCount = 0;
	private E prevAtomicSelectedValue;
	public boolean isInMidstOfAtomic() {
		return this.pushCount > 0;
	}
	public void pushAtomic() {
		if( this.isInMidstOfAtomic() ) {
			//pass
		} else {
			this.prevAtomicSelectedValue = this.getValue();
		}
		this.pushCount++;
	}
	public void popAtomic() {
		this.pushCount--;
		if( this.pushCount == 0 ) {
			E nextSelectedValue = this.getValue();
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.prevAtomicSelectedValue, nextSelectedValue ) ) {
				//pass
			} else {
				this.listSelectionModel.fireListSelectionChanged( this.index, this.index, this.listSelectionModel.getValueIsAdjusting() );

				if( TransactionManager.isInTheMidstOfUndoOrRedo() ) {
					//pass
				} else {
					this.commitEdit( new ListSelectionStateEdit< E >( this.mostRecentEvent, this.prevAtomicSelectedValue, nextSelectedValue ), this.mostRecentEvent, this.mostRecentViewController );
					//						ListSelectionStateContext< E > childContext = ContextManager.createAndPushItemSelectionStateContext( ListSelectionState.this, this.mostRecentEvent, this.mostRecentViewController /*, prevIndex, prevSelection, nextIndex, nextSelection*/ );
					//						childContext.commitAndInvokeDo( new ListSelectionStateEdit<E>( this.mostRecentEvent, prevSelection, nextSelection ) );
					//						ModelContext< ? > popContext = ContextManager.popContext();
					//						assert popContext == childContext;
				}

				this.fireValueChanged( nextSelectedValue );
				this.mostRecentEvent = null;
				this.mostRecentViewController = null;
			}
		}
	}

//	private void setSelectionIndex( int nextIndex, boolean isValueChangedInvocationDesired ) {
//		int prevIndex = this.index;
//		this.index = nextIndex;
//		int firstIndex = Math.min( prevIndex, nextIndex );
//		int lastIndex = Math.max( prevIndex, nextIndex );
//		this.listSelectionModel.fireListSelectionChanged( firstIndex, lastIndex, this.listSelectionModel.getValueIsAdjusting() );
//
//		if( isValueChangedInvocationDesired ) {
//			if( nextIndex != this.indexOfLastPerform ) {
//				E prevSelection = this.getItemAt( this.indexOfLastPerform );
//				E nextSelection = this.getItemAt( nextIndex );
//				this.indexOfLastPerform = nextIndex;
//
//				if( ContextManager.isInTheMidstOfUndoOrRedo() ) {
//					//pass
//				} else {
//					this.commitEdit( new ListSelectionStateEdit< E >( this.mostRecentEvent, prevSelection, nextSelection ), this.mostRecentEvent, this.mostRecentViewController );
//					//						ListSelectionStateContext< E > childContext = ContextManager.createAndPushItemSelectionStateContext( ListSelectionState.this, this.mostRecentEvent, this.mostRecentViewController /*, prevIndex, prevSelection, nextIndex, nextSelection*/ );
//					//						childContext.commitAndInvokeDo( new ListSelectionStateEdit<E>( this.mostRecentEvent, prevSelection, nextSelection ) );
//					//						ModelContext< ? > popContext = ContextManager.popContext();
//					//						assert popContext == childContext;
//				}
//				this.fireValueChanged( nextSelection );
//				this.mostRecentEvent = null;
//				this.mostRecentViewController = null;
//			}
//		} else {
//			this.indexOfLastPerform = nextIndex;
//		}
//	}

	public ListSelectionState( Group group, java.util.UUID id, Codec< E > codec, int selectionIndex ) {
		super( group, id );
		this.codec = codec;
		this.index = selectionIndex;
	}
	public Codec< E > getCodec() {
		return this.codec;
	}
	@Override
	protected void localize() {
	}

	public void addValueObserver( ValueObserver< E > valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void addAndInvokeValueObserver( ValueObserver< E > valueObserver ) {
		this.addValueObserver( valueObserver );
		valueObserver.changed( this.getSelectedItem() );
	}
	public void removeValueObserver( ValueObserver< E > valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	protected void fireValueChanged( E nextValue ) {
		for( ValueObserver< E > valueObserver : this.valueObservers ) {
			valueObserver.changed( nextValue );
		}
	}

	/*package-private*/ javax.swing.Action createActionForItem( final E item ) {
		javax.swing.Action action = new javax.swing.AbstractAction() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				ListSelectionState.this.setSelectionFromSwing( item );
				ListSelectionState.this.listSelectionModel.fireListSelectionChanged( ListSelectionState.this.index, ListSelectionState.this.index, ListSelectionState.this.listSelectionModel.getValueIsAdjusting() );
			}
		};
		action.putValue( javax.swing.Action.NAME, getMenuText( (E)item ) );
		action.putValue( javax.swing.Action.SMALL_ICON, getMenuSmallIcon( (E)item ) );
		return action;
	}
	/*package-private*/ javax.swing.ComboBoxModel getComboBoxModel() {
		return this.comboBoxModel;
	}
	/*package-private*/ javax.swing.ListSelectionModel getListSelectionModel() {
		return this.listSelectionModel;
	}

	@Override
	public E getValue() {
		return this.getSelectedItem();
	}

	public void addListDataListener( javax.swing.event.ListDataListener listener ) {
		this.comboBoxModel.addListDataListener( listener );
	}
	public void removeListDataListener( javax.swing.event.ListDataListener listener ) {
		this.comboBoxModel.removeListDataListener( listener );
	}
	

	public E getSelectedItem() {
		if( this.index >= 0 ) {
			if( this.index < this.getItemCount() ) {
				return this.getItemAt( index );
			} else {
				throw new IndexOutOfBoundsException( this.index + " " + this.getItemCount() + " " + this );
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: item selection out of bounds" );
//				return null;
			}
		} else {
			return null;
		}
	}
	public void setSelectedItem( E selectedItem ) {
		this.setSelectedIndex( this.indexOf( selectedItem ) );
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

	public abstract E getItemAt( int index );
	public abstract int getItemCount();
	public abstract E[] toArray( Class< E > componentType );
	public final E[] toArray() {
		return this.toArray( this.codec.getValueClass() );
	}
	
	public abstract int indexOf( E item );
	public boolean containsItem( E item ) {
		return indexOf( item ) != -1;
	}

	protected void handleItemAdded( E item ) {
	}
	protected void handleItemRemoved( E item ) {
	}
	protected abstract void internalAddItem( E item );
	protected abstract void internalRemoveItem( E item );
	protected abstract void internalSetItems( java.util.Collection< E > items );

	public final void addItem( E item ) {
		this.pushAtomic();
		try {
			this.internalAddItem( item );
			
			int index = this.getItemCount() - 1;
			this.comboBoxModel.fireIntervalAdded( this, index, index );
			this.handleItemAdded( item );
		} finally {
			this.popAtomic();
		}
	}
	public final void removeItem( E item ) {
		this.pushAtomic();
		try {
			int index = this.indexOf( item );
			this.internalRemoveItem( item );
			this.comboBoxModel.fireIntervalRemoved( this, index, index );
			this.handleItemRemoved( item );
		} finally {
			this.popAtomic();
		}
	}
	
	public final void setItems( java.util.Collection< E > items ) {
		this.pushAtomic();
		try {
			java.util.Set< E > previous = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( this.toArray() );
			java.util.Set< E > next = edu.cmu.cs.dennisc.java.util.Collections.newHashSet( items );
			java.util.List< E > added = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List< E > removed = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			
			for( E item : previous ) {
				if( next.contains( item ) ) {
					//pass
				} else {
					removed.add( item );
				}
			}
			for( E item : next ) {
				if( previous.contains( item ) ) {
					//pass
				} else {
					added.add( item );
				}
			}
			
			E previousSelectedValue = this.getValue();
			
			this.internalSetItems( items );
			
//			if( items.contains( previousSelectedValue ) ) {
				this.index = this.indexOf( previousSelectedValue );
//			} else {
//				this.index = -1;
//			}

			this.comboBoxModel.fireContentsChanged( this, 0, this.getItemCount() );
			for( E item : removed ) {
				this.handleItemRemoved( item );
			}
			for( E item : added ) {
				this.handleItemAdded( item );
			}

			
		} finally {
			this.popAtomic();
		}
	}

	public final void setItems( E... items ) {
		this.setItems( java.util.Arrays.asList( items ) );
	}
	public void clear() {
		java.util.Collection< E > items = java.util.Collections.emptyList();
		this.setListData( -1, items );
	}
	@Deprecated
	public void setListData( int selectedIndex, E... items ) {
		this.pushAtomic();
		try {
			this.setItems( items );
			this.setSelectedIndex( selectedIndex );
		} finally {
			this.popAtomic();
		}
	}
	@Deprecated
	public void setListData( int selectedIndex, java.util.Collection< E > items ) {
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
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.setSelectedIndex( i );
	}
	
	protected String getMenuText( E item ) {
		if( item != null ) {
			return item.toString();
		} else {
			return null;
		}
	}
	protected javax.swing.Icon getMenuSmallIcon( E item ) {
		return null;
	}
	
	protected void commitEdit( ListSelectionStateEdit< E > listSelectionStateEdit, java.util.EventObject e, ViewController< ?, ? > viewController ) {
		ListSelectionStateContext< E > childContext = TransactionManager.createAndPushItemSelectionStateContext( this, e, viewController );
		childContext.commitAndInvokeDo( listSelectionStateEdit );
		ModelContext< ? > popContext = TransactionManager.popContext();
		assert popContext == childContext;
	}

	@Override
	public Edit< ? > commitTutorialCompletionEdit( Edit< ? > originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		assert originalEdit instanceof ListSelectionStateEdit;
		ListSelectionStateEdit< E > listSelectionStateEdit = (ListSelectionStateEdit< E >)originalEdit;
		this.commitEdit( listSelectionStateEdit, null, null );
		return listSelectionStateEdit;
	}

	@Override
	public String getTutorialStepTitle( edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext, UserInformation userInformation ) {
		return getTutorialNoteText( modelContext, userInformation );
	}
	@Override
	public String getTutorialNoteText( ModelContext< ? > modelContext, UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		SuccessfulCompletionEvent successfulCompletionEvent = modelContext.getSuccessfulCompletionEvent();
		if( successfulCompletionEvent != null ) {
			ListSelectionStateEdit< E > listSelectionStateEdit = (ListSelectionStateEdit< E >)successfulCompletionEvent.getEdit();
			sb.append( "Select " );
			sb.append( "<strong>" );
			this.codec.appendRepresentation( sb, listSelectionStateEdit.getNextValue(), java.util.Locale.getDefault() );
			sb.append( "</strong>." );
		}
		return sb.toString();
	}

	public String getTutorialNoteStartText( ListSelectionStateEdit< E > listSelectionStateEdit ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "First press on " );
		sb.append( "<strong>" );
		this.codec.appendRepresentation( sb, listSelectionStateEdit.getPreviousValue(), java.util.Locale.getDefault() );
		sb.append( "</strong>" );
		sb.append( " in order to change it to " );
		sb.append( "<strong>" );
		this.codec.appendRepresentation( sb, listSelectionStateEdit.getNextValue(), java.util.Locale.getDefault() );
		sb.append( "</strong>." );
		return sb.toString();
	}
	public String getTutorialNoteFinishText( ListSelectionStateEdit< E > listSelectionStateEdit ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "Select " );
		sb.append( "<strong>" );
		this.codec.appendRepresentation( sb, listSelectionStateEdit.getNextValue(), java.util.Locale.getDefault() );
		sb.append( "</strong>." );
		return sb.toString();
	}

	public ComboBox< E > createComboBox() {
		return new ComboBox< E >( this );
	}
	public List< E > createList() {
		return new List< E >( this );
	}
	public DefaultRadioButtons< E > createVerticalDefaultRadioButtons() {
		return new DefaultRadioButtons< E >( this, true );
	}
	public DefaultRadioButtons< E > createHorizontalDefaultRadioButtons() {
		return new DefaultRadioButtons< E >( this, false );
	}
	public MutableList< E > createMutableList( MutableList.Factory< E > factory ) {
		return new MutableList< E >( this, factory );
	}

	public TrackableShape getTrackableShapeFor( E item ) {
		ItemSelectable< ?, E > itemSelectable = this.getFirstComponent( ItemSelectable.class );
		if( itemSelectable != null ) {
			return itemSelectable.getTrackableShapeFor( item );
		} else {
			return null;
		}
	}
	public JComponent< ? > getMainComponentFor( E item ) {
		AbstractTabbedPane< E, ? > abstractTabbedPane = this.getFirstComponent( AbstractTabbedPane.class );
		if( abstractTabbedPane != null ) {
			return abstractTabbedPane.getMainComponentFor( item );
		} else {
			return null;
		}
	}
	public ScrollPane getScrollPaneFor( E item ) {
		AbstractTabbedPane< E, ? > abstractTabbedPane = this.getFirstComponent( AbstractTabbedPane.class );
		if( abstractTabbedPane != null ) {
			return abstractTabbedPane.getScrollPaneFor( item );
		} else {
			return null;
		}
	}
	public JComponent< ? > getRootComponentFor( E item ) {
		AbstractTabbedPane< E, ? > abstractTabbedPane = this.getFirstComponent( AbstractTabbedPane.class );
		if( abstractTabbedPane != null ) {
			return abstractTabbedPane.getRootComponentFor( item );
		} else {
			return null;
		}
	}

	public static class ListSelectionMenuModelResolver<E> implements CodableResolver< ListSelectionMenuModel< E > > {
		private ListSelectionMenuModel< E > listSelectionMenuModel;

		public ListSelectionMenuModelResolver( ListSelectionMenuModel< E > listSelectionMenuModel ) {
			this.listSelectionMenuModel = listSelectionMenuModel;
		}
		public ListSelectionMenuModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			this.decode( binaryDecoder );
		}
		public ListSelectionMenuModel< E > getResolved() {
			return this.listSelectionMenuModel;
		}
		public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			CodableResolver< ListSelectionState< E >> listSelectionStateResolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
			ListSelectionState< E > listSelectionState = listSelectionStateResolver.getResolved();
			this.listSelectionMenuModel = listSelectionState.getMenuModel();
		}
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			CodableResolver< ListSelectionState< E >> listSelectionStateResolver = this.listSelectionMenuModel.listSelectionState.getCodableResolver();
			binaryEncoder.encode( listSelectionStateResolver );
		}
	}

	public static class ListSelectionMenuModel<E> extends MenuModel {
		private ListSelectionState< E > listSelectionState;

		public ListSelectionMenuModel( ListSelectionState< E > listSelectionState ) {
			super( java.util.UUID.fromString( "e33bc1ff-3790-4715-b88c-3c978aa16947" ), listSelectionState.getClass() );
			this.listSelectionState = listSelectionState;
		}
		@Override
		protected ListSelectionMenuModelResolver< E > createCodableResolver() {
			return new ListSelectionMenuModelResolver< E >( this );
		}
		@Override
		protected void handleShowing( edu.cmu.cs.dennisc.croquet.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ListSelectionMenuModel handleShowing" );
			super.handleShowing( menuItemContainer, e );
			javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
			for( final Object item : this.listSelectionState ) {
				javax.swing.Action action = this.listSelectionState.createActionForItem( (E)item );
				javax.swing.JCheckBoxMenuItem jMenuItem = new javax.swing.JCheckBoxMenuItem( action );
				buttonGroup.add( jMenuItem );
				jMenuItem.setSelected( this.listSelectionState.getSelectedItem() == item );
				menuItemContainer.getViewController().getAwtComponent().add( jMenuItem );
			}
		}
		@Override
		protected void handleHiding( edu.cmu.cs.dennisc.croquet.MenuItemContainer menuItemContainer, javax.swing.event.PopupMenuEvent e ) {
			menuItemContainer.forgetAndRemoveAllMenuItems();
			super.handleHiding( menuItemContainer, e );
		}
	}

	private ListSelectionMenuModel< E > menuModel;

	public synchronized ListSelectionMenuModel< E > getMenuModel() {
		if( this.menuModel != null ) {
			//pass
		} else {
			this.menuModel = new ListSelectionMenuModel< E >( this );
		}
		return this.menuModel;
	}
}
