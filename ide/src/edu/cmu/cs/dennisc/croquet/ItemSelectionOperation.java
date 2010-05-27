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
public abstract class ItemSelectionOperation<E> extends Operation {
	public static interface ValueObserver<E> {
		public void changed( E nextValue );
	};

	private java.util.List< ValueObserver< E > > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public void addValueObserver( ValueObserver< E > valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void addAndInvokeValueObserver( ValueObserver< E > valueObserver ) {
		this.addValueObserver( valueObserver );
		valueObserver.changed( this.getValue() );
	}
	public void removeValueObserver( ValueObserver< E > valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	private void fireValueChanged( E nextValue ) {
		for( ValueObserver< E > valueObserver : this.valueObservers ) {
			valueObserver.changed( nextValue );
		}
	}

	private class SingleListSelectionModel implements javax.swing.ListSelectionModel {
		private java.util.List< javax.swing.event.ListSelectionListener > listeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		private int index = -1;
		private int indexOfLastPerform = -1;
		private boolean isAdjusting;

		public int getSelectionMode() {
			return javax.swing.ListSelectionModel.SINGLE_SELECTION;
		}
		public void setSelectionMode( int selectionMode ) {
			assert selectionMode == javax.swing.ListSelectionModel.SINGLE_SELECTION;
		}
		public void addListSelectionListener( javax.swing.event.ListSelectionListener listener ) {
			this.listeners.add( listener );
		}
		public void removeListSelectionListener( javax.swing.event.ListSelectionListener listener ) {
			this.listeners.remove( listener );
		}

		public boolean getValueIsAdjusting() {
			return this.isAdjusting;
		}
		public void setValueIsAdjusting( boolean isAdjusting ) {
			this.isAdjusting = isAdjusting;
			this.fireChanged( -1, -1, this.isAdjusting );
		}

		public boolean isSelectedIndex( int index ) {
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

		private void fireChanged( int firstIndex, int lastIndex, boolean isAdjusting ) {
			javax.swing.event.ListSelectionEvent e = new javax.swing.event.ListSelectionEvent( this, firstIndex, lastIndex, isAdjusting );
			for( javax.swing.event.ListSelectionListener listener : this.listeners ) {
				listener.valueChanged( e );
			}
		}
		private E getSelection( int index ) {
			if( index >= 0 ) {
				return (E)comboBoxModel.getElementAt( index );
			} else {
				return null;
			}
		}
		private void setSelectedIndex( int nextIndex ) {
			int prevIndex = this.index;
			this.index = nextIndex;
			int firstIndex = Math.min( prevIndex, nextIndex );
			int lastIndex = Math.max( prevIndex, nextIndex );
			this.fireChanged( firstIndex, lastIndex, this.isAdjusting );

			if( nextIndex != this.indexOfLastPerform ) {
				E prevSelection = this.getSelection( this.indexOfLastPerform );
				E nextSelection = this.getSelection( nextIndex );
				this.indexOfLastPerform = nextIndex;
				java.util.EventObject event = null;
				ItemSelectionOperation.this.perform( event, prevSelection, nextSelection );
			}
		}
		public void clearSelection() {
			this.setSelectedIndex( -1 );
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
			this.setSelectedIndex( index0 );
		}

	};

	//	private final javax.swing.DefaultListSelectionModel listSelectionModel = new javax.swing.DefaultListSelectionModel() {
	//		@Override
	//		protected void fireValueChanged(int firstIndex, int lastIndex, boolean isAdjusting) {
	//			super.fireValueChanged( firstIndex, lastIndex, isAdjusting );
	//			if( isAdjusting ) {
	//				//pass
	//			} else {
	//				assert this.getMinSelectionIndex() == this.getMaxSelectionIndex();
	//				ItemSelectionOperation.this.comboBoxModel.setSelectedIndex( this.getAnchorSelectionIndex() );
	//			}
	//		}
	//	};
	private SingleListSelectionModel listSelectionModel = new SingleListSelectionModel();

	class ComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
		private Object[] items;

		//		private int prevIndex = -1;
		//		private E prevSelection = null;
		//		private boolean isWorking = false;
		//		private void setSelectedIndex( int nextIndex ) {
		//			if( nextIndex != this.prevIndex ) {
		//				if( this.isWorking ) {
		//					//pass
		//				} else {
		//					this.isWorking = true;
		//					try {
		//						if( ItemSelectionOperation.this.listSelectionModel.getMinSelectionIndex() != nextIndex ) {
		//							ItemSelectionOperation.this.listSelectionModel.setValueIsAdjusting( true );
		//							if( nextIndex >= 0 ) {
		//								ItemSelectionOperation.this.listSelectionModel.setSelectionInterval( nextIndex, nextIndex );
		//							} else {
		//								ItemSelectionOperation.this.listSelectionModel.clearSelection();
		//							}
		//							ItemSelectionOperation.this.listSelectionModel.setValueIsAdjusting( false );
		//						}
		//						
		//						E nextSelection = nextIndex != -1 ? (E)comboBoxModel.getElementAt( nextIndex ) : null;
		//						this.prevIndex = nextIndex;
		//						this.prevSelection = nextSelection;
		//
		//						java.util.EventObject e = null;
		//						ItemSelectionOperation.this.perform( e, prevSelection, nextSelection );
		//					} finally {
		//						this.isWorking = false;
		//					}
		//				}
		//			}
		//		}
		public Object getSelectedItem() {
			int index = ItemSelectionOperation.this.listSelectionModel.getMaxSelectionIndex();
			if( index >= 0 ) {
				return this.items[ index ];
			} else {
				return null;
			}
		}
		public void setSelectedItem( Object item ) {
			if( item != this.getSelectedItem() ) {
				final int N = this.getSize();
				int selectedIndex = -1;
				for( int i = 0; i < N; i++ ) {
					if( this.items[ i ] == item ) {
						selectedIndex = i;
						break;
					}
				}
				ItemSelectionOperation.this.listSelectionModel.setSelectedIndex( selectedIndex );
				this.fireContentsChanged( this, -1, -1 );
			}
		}
		public Object getElementAt( int index ) {
			return this.items[ index ];
		}
		public int getSize() {
			if( this.items != null ) {
				return this.items.length;
			} else {
				return 0;
			}
		}
		private void setListData( int selectedIndex, Object[] items ) {
			this.items = items;
			this.fireContentsChanged( this, 0, this.getSize() - 1 );
			ItemSelectionOperation.this.listSelectionModel.setSelectedIndex( selectedIndex );
		}
		private void setListData( int selectedIndex, java.util.Collection< E > items ) {
			this.setListData( selectedIndex, items.toArray() );
		}
	}

	private final ComboBoxModel comboBoxModel = new ComboBoxModel();

	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged( java.awt.event.ItemEvent e ) {
			//			Application application = Application.getSingleton();
			//			Context parentContext = application.getCurrentContext();
			//			Context childContext = parentContext.createChildContext();
			//			childContext.addChild( new ItemSelectionEvent< E >( childContext, ItemSelectionOperation.this, e ) );
			//			ItemSelectionOperation.this.perform( childContext, e );
		}
	};
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			//			if( e.getValueIsAdjusting() ) {
			//				//pass
			//			} else {
			//				Application application = Application.getSingleton();
			//				Context parentContext = application.getCurrentContext();
			//				Context childContext = parentContext.createChildContext();
			//				childContext.addChild( new ItemSelectionEvent< E >( childContext, ItemSelectionOperation.this, e ) );
			//				ItemSelectionOperation.this.perform( childContext, e );
			//			}
		}
	};

	private ItemSelectionOperation( Group group, java.util.UUID individualUUID, int selectedIndex, E... items ) {
		super( group, individualUUID );
		this.listSelectionModel.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
		this.comboBoxModel.setListData( selectedIndex, items );
	}
	public ItemSelectionOperation( Group group, java.util.UUID individualUUID ) {
		this( group, individualUUID, -1 );
	}

	protected abstract E decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	protected abstract void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, E value );

	private void perform( java.util.EventObject e, E prevSelection, E nextSelection ) {
		Application application = Application.getSingleton();
		Context parentContext = application.getCurrentContext();
		Context childContext = parentContext.createChildContext();
		childContext.addChild( new ItemSelectionEvent< E >( childContext, ItemSelectionOperation.this, e, prevSelection, nextSelection ) );
		childContext.commitAndInvokeDo( new ItemSelectionEdit< E >( childContext, e, prevSelection, nextSelection, this ) );
		this.fireValueChanged( nextSelection );
	}

	public E getValue() {
		return (E)this.comboBoxModel.getSelectedItem();
	}

	public void setValue( E value ) {
		this.comboBoxModel.setSelectedItem( value );
	}

	public int getItemCount() {
		return this.comboBoxModel.getSize();
	}

	public void setRandomSelectedValue() {
		final int N = this.comboBoxModel.getSize();
		int i;
		if( N > 0 ) {
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.listSelectionModel.setSelectedIndex( i );
	}

	public void setListData( int selectedIndex, E... items ) {
		synchronized( this.comboBoxModel ) {
			this.comboBoxModel.setListData( selectedIndex, items );
		}
	}
	public void setListData( int selectedIndex, java.util.Collection< E > items ) {
		synchronized( this.comboBoxModel ) {
			this.comboBoxModel.setListData( selectedIndex, items );
		}
	}

	public ComboBox< E > createComboBox() {
		ComboBox< E > rv = new ComboBox< E >() {
			@Override
			protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				super.handleAddedTo( parent );
				ItemSelectionOperation.this.addComponent( this );
				this.addItemListener( ItemSelectionOperation.this.itemListener );
			};
			@Override
			protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				this.removeItemListener( ItemSelectionOperation.this.itemListener );
				ItemSelectionOperation.this.removeComponent( this );
				super.handleRemovedFrom( parent );
			}
		};
		Application.getSingleton().register( this );
		rv.setModel( this.comboBoxModel );
		return rv;
	}
	public List< E > createList() {
		List< E > rv = new List< E >() {
			@Override
			protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				super.handleAddedTo( parent );
				ItemSelectionOperation.this.addComponent( this );
				this.addListSelectionListener( ItemSelectionOperation.this.listSelectionListener );
			};
			@Override
			protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				this.removeListSelectionListener( ItemSelectionOperation.this.listSelectionListener );
				ItemSelectionOperation.this.removeComponent( this );
				super.handleRemovedFrom( parent );
			}
		};
		Application.getSingleton().register( this );
		rv.setModel( this.comboBoxModel );
		rv.setSelectionModel( this.listSelectionModel );
		return rv;
	}

	public <R extends AbstractRadioButtons< E >> R register( final R rv ) {
		Application.getSingleton().register( this );
		rv.setModel( this.comboBoxModel );
		rv.setSelectionModel( this.listSelectionModel );
		rv.addContainmentObserver( new Component.ContainmentObserver() {
			public void addedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				ItemSelectionOperation.this.addComponent( rv );
				rv.addItemListener( ItemSelectionOperation.this.itemListener );
			}
			public void removedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				rv.removeItemListener( ItemSelectionOperation.this.itemListener );
				ItemSelectionOperation.this.removeComponent( rv );
			}
		} );
		return rv;
	}
	public DefaultRadioButtons< E > createDefaultRadioButtons() {
		return register( new DefaultRadioButtons< E >() );
	}

	
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
		}
	};
	public String getName() {
		return String.class.cast( this.action.getValue( javax.swing.Action.NAME ) );
	}
	public void setName( String name ) {
		this.action.putValue( javax.swing.Action.NAME, name );
	}
	public javax.swing.Icon getSmallIcon() {
		return javax.swing.Icon.class.cast( this.action.getValue( javax.swing.Action.SMALL_ICON ) );
	}
	public void setSmallIcon( javax.swing.Icon icon ) {
		this.action.putValue( javax.swing.Action.SMALL_ICON, icon );
	}
	public int getMnemonicKey() {
		return Integer.class.cast( this.action.getValue( javax.swing.Action.MNEMONIC_KEY ) );
	}
	public void setMnemonicKey( int mnemonicKey ) {
		this.action.putValue( javax.swing.Action.MNEMONIC_KEY, mnemonicKey );
	}
	
	public Menu createMenu() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: ItemSelectionOperation createMenu()" );
		Menu rv = new Menu() {
			@Override
			protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				super.handleAddedTo( parent );
				javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
				for( final Object item : ItemSelectionOperation.this.comboBoxModel.items ) {
					javax.swing.Action action = new javax.swing.AbstractAction() {
						public void actionPerformed( java.awt.event.ActionEvent e ) {
							comboBoxModel.setSelectedItem( item );
						}
					};
					action.putValue( javax.swing.Action.NAME, item.toString() );
					javax.swing.JCheckBoxMenuItem jMenuItem = new javax.swing.JCheckBoxMenuItem( action );
					buttonGroup.add( jMenuItem );
					jMenuItem.setSelected( comboBoxModel.getSelectedItem() == item );
					this.getAwtComponent().add( jMenuItem );
				}
			}
			
			@Override
			protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
				super.handleRemovedFrom( parent );
				this.getAwtComponent().removeAll();
			}
		};
		rv.getAwtComponent().setAction( this.action );
		return rv;
	}
}
