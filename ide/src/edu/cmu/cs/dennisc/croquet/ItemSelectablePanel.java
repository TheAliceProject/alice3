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
public abstract class ItemSelectablePanel< E, D extends ItemSelectablePanel.ItemDetails > extends ItemSelectable<javax.swing.JPanel, E> {
	//todo: better name
	protected class ItemDetails {
		private E item;
		private AbstractButton<?,?> button;
		private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				ItemSelectablePanel.this.model.setSelectedItem( ItemDetails.this.item );
				ItemSelectablePanel.this.fireItemStateChanged( e );
			}
		};
		public ItemDetails( E item, AbstractButton<?,?> button ) {
			this.item = item;
			this.button = button;
		}
		public AbstractButton<?,?> getButton() {
			return this.button;
		}
		public void add() {
			this.button.getAwtComponent().addItemListener( this.itemListener );
			ItemSelectablePanel.this.buttonGroup.add( this.button.getAwtComponent() );
		}
		public void remove() {
			//note: should already be removed by removeAllComponents()
			assert this.button.getParent() == null;
			this.button.getAwtComponent().removeItemListener( this.itemListener );
			ItemSelectablePanel.this.buttonGroup.remove( this.button.getAwtComponent() );
		}
		public void setSelected( boolean isSelected ) {
			if( this.button.getAwtComponent().isSelected() != isSelected ) {
				this.button.getAwtComponent().setSelected( isSelected );
			}
		}
	}
	/*package-private*/ ItemSelectablePanel( ListSelectionState<E> model ) {
		super( model );
	}
	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel );
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		java.awt.LayoutManager layoutManager = this.createLayoutManager( rv );
		rv.setLayout( layoutManager );
		rv.setOpaque( false );
		rv.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		rv.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		return rv;
	}

	protected abstract D createItemDetails( E item );
	
	private java.util.Map<E, D > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.ComboBoxModel model;
	private javax.swing.ListSelectionModel listSelectionModel;
	private javax.swing.event.ListDataListener listDataListener = new javax.swing.event.ListDataListener() {
		public void intervalAdded(javax.swing.event.ListDataEvent e) {
			ItemSelectablePanel.this.handleListDataChanged();
		}
		public void intervalRemoved(javax.swing.event.ListDataEvent e) {
			ItemSelectablePanel.this.handleListDataChanged();
		}
		public void contentsChanged(javax.swing.event.ListDataEvent e) {
			ItemSelectablePanel.this.handleListDataChanged();
		}
	};
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			ItemSelectablePanel.this.handleListSelectionChanged();
		}
	};
	private void fireItemStateChanged( java.awt.event.ItemEvent e ) {
		for( java.awt.event.ItemListener itemListener : this.itemListeners ) {
			itemListener.itemStateChanged(e);
		}
	}

	protected D getItemDetails( E item ) {
		return this.map.get( item );
	}
	protected Iterable<D> getAllItemDetails() {
		return this.map.values();
	}
//	public D getSelectedItemDetails() {
//		for( D details : this.getAllItemDetails() ) {
//			AbstractButton<?> button = details.getButton();
//			if( button.getAwtComponent().getModel().isSelected() ) {
//				return details;
//			}
//		}
//		return null;
//	}
	
	protected abstract void removeAllDetails();
	protected abstract void addPrologue( int count );
	protected abstract void addItem( D itemDetails );
	protected abstract void addEpilogue();
	
	private java.util.ArrayList<E> prevItems = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
	
	private void handleListDataChanged() {
		synchronized( this.model ) {
			final int N = this.model.getSize();
			
			boolean isActuallyChanged;
			if( N == prevItems.size() ) {
				isActuallyChanged = false;
				for( int i=0; i<N; i++ ) {
					E item = (E)this.model.getElementAt( i );
					if( item == prevItems.get( i ) ) {
						//pass
					} else {
						isActuallyChanged = true;
						break;
					}
				}
			} else {
				isActuallyChanged = true;;
			}
			
			if( isActuallyChanged ) {
				this.removeAllDetails();
				this.prevItems.clear();
				this.prevItems.ensureCapacity( N );
				this.addPrologue( N );
				for( int i=0; i<N; i++ ) {
					E item = (E)this.model.getElementAt( i );
					D itemDetails = this.map.get( item );
					if( itemDetails != null ) {
						//pass
					} else {
						itemDetails = this.createItemDetails( item );
						this.map.put( item, itemDetails );
					}
					itemDetails.add();
					this.addItem( itemDetails );
					this.prevItems.add( item );
				}
				this.addEpilogue();
			}
			
//			int i = this.listSelectionModel.getLeadSelectionIndex();
//			E selectedItem;
//			if( i > 0 ) {
//				selectedItem = (E)this.model.getElementAt( i );
//			} else {
//				selectedItem = null;
//			}
//			this.handleItemSelected( selectedItem );
		}
		this.revalidateAndRepaint();
	}
	protected void handleItemSelected( E item ) {
		if( item != null ) {
			D itemDetails = this.map.get( item );
			assert itemDetails != null;
			itemDetails.setSelected( true );
		} else {
			javax.swing.ButtonModel model = this.buttonGroup.getSelection();
			if( model != null ) {
				this.buttonGroup.setSelected(model, false);
			}
		}
	}
	private void handleListSelectionChanged() {
		this.handleItemSelected( (E)this.model.getSelectedItem() );
	}

	@Override
	/*package-private*/ TrackableShape getTrackableShapeFor( E item ) {
		ItemDetails itemDetails = this.getItemDetails( item );
		if( itemDetails != null ) {
			return itemDetails.getButton();
		} else {
			return null;
		}
	}
	
	private java.util.List<java.awt.event.ItemListener> itemListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList(); 
	/*package-private*/ void setSwingComboBoxModel( javax.swing.ComboBoxModel model ) {
		if( this.model != null ) {
			synchronized( this.model ) {
				this.model.removeListDataListener( this.listDataListener );
			}
		}
		this.model = model;
		this.handleListDataChanged();
		if( this.model != null ) {
			synchronized( this.model ) {
				this.model.addListDataListener( this.listDataListener );
			}
		}
	}
	/*package-private*/ void setSelectionModel( javax.swing.ListSelectionModel listSelectionModel ) {
		if( this.listSelectionModel != null ) {
			synchronized( this.listSelectionModel ) {
				this.listSelectionModel.removeListSelectionListener( this.listSelectionListener );
			}
		}
		this.listSelectionModel = listSelectionModel;
		this.handleListSelectionChanged();
		if( this.listSelectionModel != null ) {
			synchronized( this.listSelectionModel ) {
				this.listSelectionModel.addListSelectionListener( this.listSelectionListener );
			}
		}
	}
	/*package-private*/ void addItemListener(java.awt.event.ItemListener itemListener) {
		this.itemListeners.add( itemListener );
	}
	/*package-private*/ void removeItemListener(java.awt.event.ItemListener itemListener) {
		this.itemListeners.remove( itemListener );
	}
}
