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
public class DefaultListSelectionState< E > extends ListSelectionState< E > {
	private static class InternalModel<E> {
		private final ListData< E > listData;
		private int selectionIndex;
		public InternalModel( ListData< E > listData, int selectionIndex ) {
			this.listData = listData;
			this.selectionIndex = selectionIndex;
		}

		public E getSelectedValue() {
			if( this.selectionIndex != -1 ) {
				return this.listData.getElementAt( this.selectionIndex );
			} else {
				return null;
			}
		}
		private boolean isInMidstOfAtomic = false;
		private E prevAtomicSelectedValue;
		public void startAtomic() {
			this.prevAtomicSelectedValue = this.getSelectedValue();
			this.isInMidstOfAtomic = true;
		}
		public void commitAtomic() {
			E nextSelectedValue = this.getSelectedValue();
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.prevAtomicSelectedValue, nextSelectedValue ) ) {
				//pass
			} else {
				
			}
			this.isInMidstOfAtomic = false;
		}
	}
	
	private static class SwingModels<E> extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel, javax.swing.ListSelectionModel {
		private final DefaultListSelectionState< E > listSelectionState;
		public SwingModels( DefaultListSelectionState< E > listSelectionState ) {
			this.listSelectionState = listSelectionState;
			ListData< E > listData = this.listSelectionState.getListData();
			if( listData instanceof MutableListData< ? > ) {
				MutableListData< ? > mutableListData = (MutableListData< ? >)listData;

				//todo: remove on release
				mutableListData.addListDataListener( new javax.swing.event.ListDataListener() {
					public void contentsChanged( javax.swing.event.ListDataEvent e ) {
						SwingModels.this.fireContentsChanged( e.getSource(), e.getIndex0(), e.getIndex1() );
					}
					public void intervalAdded( javax.swing.event.ListDataEvent e ) {
						SwingModels.this.fireIntervalAdded( e.getSource(), e.getIndex0(), e.getIndex1() );
					}
					public void intervalRemoved( javax.swing.event.ListDataEvent e ) {
						SwingModels.this.fireIntervalRemoved( e.getSource(), e.getIndex0(), e.getIndex1() );
					}
				} );
			}
		}
		public E getSelectedItem() {
			int index = this.getMaxSelectionIndex();
			if( index >= 0 ) {
				if( index < this.getSize() ) {
					return this.listSelectionState.getListData().getElementAt( index );
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: item selection out of bounds" );
					return null;
				}
			} else {
				return null;
			}
		}

		public void setSelectedItem( Object item ) {
			if( item != this.getSelectedItem() ) {
				if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( item, this.getSelectedItem() ) ) {
					throw new RuntimeException();
				}
				int selectedIndex = -1;
				if( item != null ) {
					final int N = this.getSize();
					for( int i = 0; i < N; i++ ) {
						if( item.equals( this.listSelectionState.getListData().getElementAt( i ) ) ) {
							selectedIndex = i;
							break;
						}
					}
				}
				this.setSelectedIndex( selectedIndex, true );
				this.fireContentsChanged( this, -1, -1 );
			}
		}

		public Object getElementAt( int index ) {
			return this.listSelectionState.getListData().getElementAt( index );
		}

		public int getSize() {
			return this.listSelectionState.getListData().getSize();
		}

		
		private java.util.List< javax.swing.event.ListSelectionListener > listeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		private int index = -1;
		private int indexOfLastPerform = -1;
		private boolean isAdjusting;

		private ViewController< ?, ? > mostRecentViewController;
		private java.util.EventObject mostRecentEvent;

		public void setMostRecentEventAndViewController( java.util.EventObject mostRecentEvent, ViewController< ?, ? > mostRecentViewController ) {
			this.mostRecentEvent = mostRecentEvent;
			this.mostRecentViewController = mostRecentViewController;
		}

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
				return (E)this.listSelectionState.getListData().getElementAt( index );
			} else {
				return null;
			}
		}

		private void setSelectedIndex( int nextIndex, boolean isValueChangedInvocationDesired ) {
			int prevIndex = this.index;
			this.index = nextIndex;
			int firstIndex = Math.min( prevIndex, nextIndex );
			int lastIndex = Math.max( prevIndex, nextIndex );
			this.fireChanged( firstIndex, lastIndex, this.isAdjusting );

			if( isValueChangedInvocationDesired ) {
				if( nextIndex != this.indexOfLastPerform ) {
					E prevSelection = this.getSelection( this.indexOfLastPerform );
					E nextSelection = this.getSelection( nextIndex );
					this.indexOfLastPerform = nextIndex;

					if( ContextManager.isInTheMidstOfUndoOrRedo() ) {
						//pass
					} else {
						this.listSelectionState.commitEdit( new ListSelectionStateEdit< E >( this.mostRecentEvent, prevSelection, nextSelection ), this.mostRecentEvent, this.mostRecentViewController );
						//						ListSelectionStateContext< E > childContext = ContextManager.createAndPushItemSelectionStateContext( ListSelectionState.this, this.mostRecentEvent, this.mostRecentViewController /*, prevIndex, prevSelection, nextIndex, nextSelection*/ );
						//						childContext.commitAndInvokeDo( new ListSelectionStateEdit<E>( this.mostRecentEvent, prevSelection, nextSelection ) );
						//						ModelContext< ? > popContext = ContextManager.popContext();
						//						assert popContext == childContext;
					}
					this.listSelectionState.fireValueChanged( nextSelection );
					this.mostRecentEvent = null;
					this.mostRecentViewController = null;
				}
			} else {
				this.indexOfLastPerform = nextIndex;
			}
		}

		public void clearSelection() {
			this.setSelectedIndex( -1, true );
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
			this.setSelectedIndex( index0, true );
		}
	}


	private final ListData< E > listData;
	private final SwingModels< E > swingModels;

	public DefaultListSelectionState( Group group, java.util.UUID id, Codec< E > codec, ListData< E > listData, int selectedIndex ) {
		super( group, id, codec );
		this.listData = listData;
		this.swingModels = new SwingModels< E >( this );
		this.swingModels.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION );
	}
	public DefaultListSelectionState( Group group, java.util.UUID id, Codec< E > codec ) {
		this( group, id, codec, new DefaultMutableListData< E >(), -1 );
	}

	@Override
	/*package-private*/ javax.swing.Action createAction( final E item ) {
		javax.swing.Action action = new javax.swing.AbstractAction() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				swingModels.setSelectedItem( item );
			}
		};
		action.putValue( javax.swing.Action.NAME, getMenuText( (E)item ) );
		action.putValue( javax.swing.Action.SMALL_ICON, getMenuSmallIcon( (E)item ) );
		return action;
	}
	@Override
	/*package-private*/ javax.swing.ComboBoxModel getComboBoxModel() {
		return this.swingModels;
	}
	@Override
	/*package-private*/ javax.swing.ListSelectionModel getListSelectionModel() {
		return this.swingModels;
	}
	@Override
	/*package-private*/ ListData< E > getListData() {
		return this.listData;
	}
	
	@Override
	public void addListDataListener( javax.swing.event.ListDataListener listener ) {
		this.swingModels.addListDataListener( listener );
	}
	@Override
	public void removeListDataListener( javax.swing.event.ListDataListener listener ) {
		this.swingModels.removeListDataListener( listener );
	}

	public java.util.Iterator< E > iterator() {
		return this.listData.iterator();
	}

	@Override
	public E getSelectedItem() {
		return (E)this.swingModels.getSelectedItem();
	}
	@Override
	public void setSelectedItem( E selectedItem ) {
		this.swingModels.setSelectedItem( selectedItem );
	}
	@Override
	public int getSelectedIndex() {
		return this.swingModels.getMinSelectionIndex();
	}
	@Override
	public void setSelectedIndex( int nextIndex ) {
		this.swingModels.setSelectedIndex( nextIndex, true );
	}
	@Override
	public void setRandomSelectedValue() {
		final int N = this.swingModels.getSize();
		int i;
		if( N > 0 ) {
			i = org.alice.random.RandomUtilities.nextIntegerFrom0ToNExclusive( N );
		} else {
			i = -1;
		}
		this.swingModels.setSelectedIndex( i, true );
	}
	
	@Override
	public int indexOf( E item ) {
		synchronized( this.listData ) {
			final int N = this.listData.getSize();
			for( int i=0; i<N; i++ ) {
				if( item.equals( this.listData.getElementAt( i ) ) ) {
					return i;
				}
			}
		}
		return -1;
	}
	@Override
	public E getItemAt( int index ) {
		return (E)this.swingModels.getElementAt( index );
	}
	@Override
	public int getItemCount() {
		return this.swingModels.getSize();
	}

	@Deprecated
	public E[] toArray( Class< E > componentType ) {
		//todo: make thread safe
		E[] rv = (E[])java.lang.reflect.Array.newInstance( componentType, this.getItemCount() );
		for( int i = 0; i < rv.length; i++ ) {
			rv[ i ] = this.getItemAt( i );
		}
		return rv;
	}
	@Deprecated
	public void setListData( int selectedIndex, E... items ) {
		if( this.listData instanceof MutableListData ) {
			synchronized( this.listData ) {
				((MutableListData<E>)this.listData).set( items );
				//todo: set selected index
			}
		}
	}
	@Deprecated
	public void setListData( int selectedIndex, java.util.Collection< E > items ) {
		if( this.listData instanceof MutableListData ) {
			synchronized( this.listData ) {
				((MutableListData<E>)this.listData).set( items );
				//todo: set selected index
			}
		}
	}
	@Deprecated
	public void addItem( E item ) {
		if( this.listData instanceof MutableListData ) {
			synchronized( this.listData ) {
				((MutableListData)this.listData).addElement( item );
			}
		}
	}
	@Override
	public void removeItem( E item ) {
		if( this.listData instanceof MutableListData ) {
			synchronized( this.listData ) {
				this.swingModels.clearSelection();
				((MutableListData)this.listData).removeElement( item );
				if( this.swingModels.getSize() > 0 ) {
					this.swingModels.setSelectedItem( this.swingModels.getElementAt( 0 ) );
				}
			}
		}
	}
	@Deprecated
	public void clear() {
		java.util.Collection< E > items = java.util.Collections.emptyList();
		this.setListData( -1, items );
	}
}
