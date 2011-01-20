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

/**
 * @author Dennis Cosgrove
 */
public abstract class ListSelectionState<E> extends State< E > implements Iterable< E >/*, java.util.List<E>*/{
	public static interface ValueObserver<E> {
		public void changed( E nextValue );
	}

	private final Codec< E > codec;
	private final java.util.List< ValueObserver< E >> valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	
	public ListSelectionState( Group group, java.util.UUID id, Codec< E > codec ) {
		super( group, id );
		this.codec = codec;
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

	private int pushCount = 0;
	private E prevAtomicSelectedValue;
	public boolean isInMidstOfAtomic() {
		return this.pushCount < 0;
	}
	public void pushAtomic() {
		this.prevAtomicSelectedValue = this.getValue();
		this.pushCount++;
	}
	public void popAtomic() {
		this.pushCount--;
		if( this.pushCount == 0 ) {
			E nextSelectedValue = this.getValue();
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.prevAtomicSelectedValue, nextSelectedValue ) ) {
				//pass
			} else {
				this.fireValueChanged( nextSelectedValue );
			}
		}
	}

	/*package-private*/ abstract javax.swing.Action createActionForItem( E item );
	/*package-private*/ abstract javax.swing.ComboBoxModel getComboBoxModel();
	/*package-private*/ abstract javax.swing.ListSelectionModel getListSelectionModel();

	@Override
	public E getValue() {
		return this.getSelectedItem();
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

	public abstract void addListDataListener( javax.swing.event.ListDataListener listener );
	public abstract void removeListDataListener( javax.swing.event.ListDataListener listener );
	

	public abstract E getItemAt( int index );
	public abstract int getItemCount();
	
	
	public abstract void removeItem( E item );

	public abstract int indexOf( E item );
	public boolean containsItem( E item ) {
		return indexOf( item ) != -1;
	}

	public abstract E getSelectedItem();
	public abstract void setSelectedItem( E selectedItem );
	public abstract int getSelectedIndex();
	public abstract void setSelectedIndex( int nextIndex );
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
		ListSelectionStateContext< E > childContext = ContextManager.createAndPushItemSelectionStateContext( this, e, viewController );
		childContext.commitAndInvokeDo( listSelectionStateEdit );
		ModelContext< ? > popContext = ContextManager.popContext();
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
	public DefaultRadioButtons< E > createDefaultRadioButtons() {
		return new DefaultRadioButtons< E >( this );
	}
	public MutableList< E > createMutableList( MutableList.Factory< E > factory ) {
		return new MutableList< E >( this, factory );
	}

	public interface TabCreator<E> {
		public java.util.UUID getId( E item );
		public void customizeTitleComponent( BooleanState booleanState, AbstractButton< ?, BooleanState > button, E item );
		public JComponent< ? > createMainComponent( E item );
		public ScrollPane createScrollPane( E item );
		public boolean isCloseable( E item );
	}

	public FolderTabbedPane< E > createFolderTabbedPane( TabCreator< E > tabCreator ) {
		return new FolderTabbedPane< E >( this, tabCreator );
	};
	public ToolPaletteTabbedPane< E > createToolPaletteTabbedPane( TabCreator< E > tabCreator ) {
		return new ToolPaletteTabbedPane< E >( this, tabCreator );
	};


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
}
