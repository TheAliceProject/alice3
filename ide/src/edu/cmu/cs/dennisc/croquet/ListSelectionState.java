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
public abstract class ListSelectionState<E> extends Model implements Iterable<E> {
	public static interface ValueObserver<E> {
		public void changed(E nextValue);
	};

	private java.util.List<ValueObserver<E>> valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public void addValueObserver(ValueObserver<E> valueObserver) {
		this.valueObservers.add(valueObserver);
	}

	public void addAndInvokeValueObserver(ValueObserver<E> valueObserver) {
		this.addValueObserver(valueObserver);
		valueObserver.changed(this.getSelectedItem());
	}

	public void removeValueObserver(ValueObserver<E> valueObserver) {
		this.valueObservers.remove(valueObserver);
	}

	private void fireValueChanged(E nextValue) {
		for (ValueObserver<E> valueObserver : this.valueObservers) {
			valueObserver.changed(nextValue);
		}
	}

	private class SingleListSelectionModel implements javax.swing.ListSelectionModel {
		private java.util.List<javax.swing.event.ListSelectionListener> listeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		private int index = -1;
		private int indexOfLastPerform = -1;
		private boolean isAdjusting;

		private ViewController<?,?> mostRecentViewController;
		private java.util.EventObject mostRecentEvent;

		public void setMostRecentEventAndViewController(java.util.EventObject mostRecentEvent, ViewController<?,?> mostRecentViewController) {
			this.mostRecentEvent = mostRecentEvent;
			this.mostRecentViewController = mostRecentViewController;
		}

		public int getSelectionMode() {
			return javax.swing.ListSelectionModel.SINGLE_SELECTION;
		}

		public void setSelectionMode(int selectionMode) {
			assert selectionMode == javax.swing.ListSelectionModel.SINGLE_SELECTION;
		}

		public void addListSelectionListener(javax.swing.event.ListSelectionListener listener) {
			this.listeners.add(listener);
		}

		public void removeListSelectionListener(javax.swing.event.ListSelectionListener listener) {
			this.listeners.remove(listener);
		}

		public boolean getValueIsAdjusting() {
			return this.isAdjusting;
		}

		public void setValueIsAdjusting(boolean isAdjusting) {
			this.isAdjusting = isAdjusting;
			this.fireChanged(-1, -1, this.isAdjusting);
		}

		public boolean isSelectedIndex(int index) {
			return this.index == index;
		}

		public boolean isSelectionEmpty() {
			return this.index < 0;
		}

		public int getAnchorSelectionIndex() {
			return this.index;
		}

		public int getLeadSelectionIndex() {
			return this.index;
		}

		public int getMaxSelectionIndex() {
			return this.index;
		}

		public int getMinSelectionIndex() {
			return this.index;
		}

		private void fireChanged(int firstIndex, int lastIndex, boolean isAdjusting) {
			javax.swing.event.ListSelectionEvent e = new javax.swing.event.ListSelectionEvent(this, firstIndex, lastIndex, isAdjusting);
			for (javax.swing.event.ListSelectionListener listener : this.listeners) {
				listener.valueChanged(e);
			}
		}

		private E getSelection(int index) {
			if (index >= 0) {
				return (E) comboBoxModel.getElementAt(index);
			} else {
				return null;
			}
		}

		private void setSelectedIndex(int nextIndex, boolean isValueChangedInvocationDesired) {
			int prevIndex = this.index;
			this.index = nextIndex;
			int firstIndex = Math.min(prevIndex, nextIndex);
			int lastIndex = Math.max(prevIndex, nextIndex);
			this.fireChanged(firstIndex, lastIndex, this.isAdjusting);

			if( isValueChangedInvocationDesired ) {
				if (nextIndex != this.indexOfLastPerform) {
					E prevSelection = this.getSelection(this.indexOfLastPerform);
					E nextSelection = this.getSelection(nextIndex);
					this.indexOfLastPerform = nextIndex;

					Application application = Application.getSingleton();
					ModelContext<?> parentContext = application.getCurrentContext();
					ListSelectionStateContext< E > childContext = parentContext.createItemSelectionStateContext( ListSelectionState.this, this.mostRecentEvent, this.mostRecentViewController, prevIndex, prevSelection, nextIndex, nextSelection );
					childContext.commitAndInvokeDo( new ListSelectionEdit<E>( this.mostRecentEvent, prevSelection, nextSelection ) );
					ListSelectionState.this.fireValueChanged(nextSelection);

					this.mostRecentEvent = null;
					this.mostRecentViewController = null;
				}
			} else {
				this.indexOfLastPerform = nextIndex;
			}
		}

		public void clearSelection() {
			this.setSelectedIndex(-1, true);
		}

		public void addSelectionInterval(int index0, int index1) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: addSelectionInterval");
		}

		public void insertIndexInterval(int index, int length, boolean before) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: insertIndexInterval");
		}

		public void removeIndexInterval(int index0, int index1) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: removeIndexInterval");
		}

		public void removeSelectionInterval(int index0, int index1) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: removeSelectionInterval");
		}

		public void setAnchorSelectionIndex(int index) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: setAnchorSelectionIndex");
		}

		public void setLeadSelectionIndex(int index) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: setLeadSelectionIndex");
		}

		public void setSelectionInterval(int index0, int index1) {
			assert index0 == index1;
			this.setSelectedIndex(index0, true);
		}

	};

	private class ComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
		private java.util.ArrayList<E> items = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();

		public Object getSelectedItem() {
			int index = ListSelectionState.this.listSelectionModel.getMaxSelectionIndex();
			if (index >= 0) {
				if( index < this.getSize() ) {
					return this.items.get( index );
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: item selection out of bounds" );
					return null;
				}
			} else {
				return null;
			}
		}

		public void setSelectedItem(Object item) {
			if (item != this.getSelectedItem()) {
				final int N = this.getSize();
				int selectedIndex = -1;
				for (int i = 0; i < N; i++) {
					if (this.items.get( i ) == item) {
						selectedIndex = i;
						break;
					}
				}
				ListSelectionState.this.listSelectionModel.setSelectedIndex(selectedIndex, true);
				this.fireContentsChanged(this, -1, -1);
			}
		}

		public Object getElementAt(int index) {
			if( index >= 0 ) {
				if( index < this.items.size() ) {
					return this.items.get( index );
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: getElementAt out of range" );
					return null;
				}
			} else {
				return null;
			}
		}

		public int getSize() {
			return this.items.size();
		}

		
		private void setListData(int selectedIndex, E[] items) {
			this.items.clear();
			this.items.ensureCapacity( items.length );
			for( E item : items ) {
				this.items.add( item );
			}
			this.fireContentsChanged(this, 0, this.getSize() - 1);
			ListSelectionState.this.listSelectionModel.setSelectedIndex(selectedIndex, true);
		}

		private void setListData(int selectedIndex, java.util.Collection<E> items) {
			this.items.clear();
			this.items.ensureCapacity( items.size() );
			for( E item : items ) {
				this.items.add( item );
			}
			this.fireContentsChanged(this, 0, this.getSize() - 1);
			ListSelectionState.this.listSelectionModel.setSelectedIndex(selectedIndex, true);
		}
		
		private void addItem( E item ) {
			int index = this.items.size();
			this.items.add( item );
			this.fireIntervalAdded( this, index, index );
		}
		private void removeItem( E item ) {
			int index = this.items.indexOf( item );
			assert index >= 0;
			this.items.remove( item );
			this.fireIntervalRemoved( this, index, index );
		}
	}

	private final SingleListSelectionModel listSelectionModel = new SingleListSelectionModel();
	private final ComboBoxModel comboBoxModel = new ComboBoxModel();

	private ListSelectionState(Group group, java.util.UUID individualUUID, int selectedIndex, E... items) {
		super(group, individualUUID);
		this.listSelectionModel.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		this.comboBoxModel.setListData(selectedIndex, items);
	}

	public ListSelectionState(Group group, java.util.UUID individualUUID) {
		this(group, individualUUID, -1);
	}

	protected abstract E decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder);
	protected abstract void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, E value);


	public E getSelectedItem() {
		return (E) this.comboBoxModel.getSelectedItem();
	}

	public void setSelectedItem(E selectedItem) {
		this.comboBoxModel.setSelectedItem(selectedItem);
	}

	public java.util.Iterator< E > iterator() {
		return this.comboBoxModel.items.iterator();
	}
	public E getItemAt(int index) {
		return (E) this.comboBoxModel.getElementAt(index);
	}
	public int getItemCount() {
		return this.comboBoxModel.getSize();
	}

	public void setRandomSelectedValue() {
		final int N = this.comboBoxModel.getSize();
		int i;
		if (N > 0) {
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive(N);
		} else {
			i = -1;
		}
		this.listSelectionModel.setSelectedIndex(i,true);
	}

	public void setListData(int selectedIndex, E... items) {
		synchronized (this.comboBoxModel) {
			this.comboBoxModel.setListData(selectedIndex, items);
		}
	}

	public void setListData(int selectedIndex, java.util.Collection<E> items) {
		synchronized (this.comboBoxModel) {
			this.comboBoxModel.setListData(selectedIndex, items);
		}
	}

	public boolean containsItem(E item) {
		synchronized (this.comboBoxModel) {
			final int N = this.comboBoxModel.getSize();
			for (int i = 0; i < N; i++) {
				if (this.comboBoxModel.getElementAt(i) == item) {
					return true;
				}
			}
			return false;
		}
	}

	public void addItem(E item) {
		synchronized (this.comboBoxModel) {
			this.comboBoxModel.addItem( item );
		}
	}
	
	public void removeItem( E item ) {
		synchronized (this.comboBoxModel) {
			this.listSelectionModel.clearSelection();
			this.comboBoxModel.removeItem( item );
			if( this.comboBoxModel.getSize() > 0 ) {
				this.comboBoxModel.setSelectedItem( this.comboBoxModel.getElementAt( 0 ) );
			}
		}
	}

	public void clear() {
		java.util.Collection<E> items = java.util.Collections.emptyList();
		this.setListData(-1, items);
	}

	public ComboBox<E> createComboBox() {
		ComboBox<E> rv = new ComboBox<E>( this ) {
			// private ItemListener itemListener = new ItemListener( this );
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo(parent);
				ListSelectionState.this.addComponent(this);
				// this.addItemListener( this.itemListener );
			};

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				// this.removeItemListener( this.itemListener );
				ListSelectionState.this.removeComponent(this);
				super.handleRemovedFrom(parent);
			}
		};
		Application.getSingleton().register(this);
		rv.setSwingComboBoxModel(this.comboBoxModel);
		return rv;
	}

	public List<E> createList() {
		List<E> rv = new List<E>( this ) {
			// private ListSelectionListener listSelectionListener = new
			// ListSelectionListener( this );
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo(parent);
				ListSelectionState.this.addComponent(this);
				// this.addListSelectionListener( this.listSelectionListener );
			};

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				// this.removeListSelectionListener( this.listSelectionListener
				// );
				ListSelectionState.this.removeComponent(this);
				super.handleRemovedFrom(parent);
			}
		};
		Application.getSingleton().register(this);
		rv.setSwingListModel(this.comboBoxModel);
		rv.setSelectionModel(this.listSelectionModel);
		return rv;
	}

	/*package-private*/ <R extends ItemSelectablePanel<E, ?>> R register(final R rv) {
		Application.getSingleton().register(this);
		rv.setSwingComboBoxModel(this.comboBoxModel);
		rv.setSelectionModel(this.listSelectionModel);
		rv.addContainmentObserver(new Component.ContainmentObserver() {
			// private ItemListener itemListener = new ItemListener( rv );
			public void addedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				ListSelectionState.this.addComponent(rv);
				// rv.addItemListener( this.itemListener );
			}

			public void removedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				// rv.removeItemListener( this.itemListener );
				ListSelectionState.this.removeComponent(rv);
			}
		});
		return rv;
	}

	public DefaultRadioButtons<E> createDefaultRadioButtons() {
		return register(new DefaultRadioButtons<E>( this ));
	}

	public interface TabCreator<E> {
		public java.util.UUID getId(E item);
		public void customizeTitleComponent(BooleanState booleanState, AbstractButton< ?, BooleanState > button, E item);
		public JComponent<?> createMainComponent(E item);
		public ScrollPane createScrollPane(E item);
		public boolean isCloseAffordanceDesired();
	}

	public FolderTabbedPane<E> createFolderTabbedPane(TabCreator<E> tabCreator) {
		return register(new FolderTabbedPane<E>(this, tabCreator));
	};

	public ToolPaletteTabbedPane<E> createToolPaletteTabbedPane(TabCreator<E> tabCreator) {
		return register(new ToolPaletteTabbedPane<E>(this, tabCreator));
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

	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		}
	};
	
	public String getName() {
		return String.class.cast(this.action.getValue(javax.swing.Action.NAME));
	}

	public void setName(String name) {
		this.action.putValue(javax.swing.Action.NAME, name);
	}

	public javax.swing.Icon getSmallIcon() {
		return javax.swing.Icon.class.cast(this.action.getValue(javax.swing.Action.SMALL_ICON));
	}

	public void setSmallIcon(javax.swing.Icon icon) {
		this.action.putValue(javax.swing.Action.SMALL_ICON, icon);
	}

	public int getMnemonicKey() {
		return Integer.class.cast(this.action.getValue(javax.swing.Action.MNEMONIC_KEY));
	}

	public void setMnemonicKey(int mnemonicKey) {
		this.action.putValue(javax.swing.Action.MNEMONIC_KEY, mnemonicKey);
	}

	public Menu<ListSelectionState<E>> createMenu() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: ListSelectionState createMenu()");
		Menu<ListSelectionState<E>> rv = new Menu<ListSelectionState<E>>( this ) {
			@Override
			protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleAddedTo(parent);
				javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
				for (final Object item : ListSelectionState.this.comboBoxModel.items) {
					javax.swing.Action action = new javax.swing.AbstractAction() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							comboBoxModel.setSelectedItem(item);
						}
					};
					action.putValue(javax.swing.Action.NAME, item.toString());
					javax.swing.JCheckBoxMenuItem jMenuItem = new javax.swing.JCheckBoxMenuItem(action);
					buttonGroup.add(jMenuItem);
					jMenuItem.setSelected(comboBoxModel.getSelectedItem() == item);
					this.getAwtComponent().add(jMenuItem);
				}
			}

			@Override
			protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				super.handleRemovedFrom(parent);
				this.getAwtComponent().removeAll();
			}
		};
		rv.getAwtComponent().setAction(this.action);
		return rv;
	}
}
