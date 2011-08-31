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

package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class ItemSelectablePanel< E, D extends ItemDetails<E,D,?> > extends ItemSelectable<javax.swing.JPanel, E> {
	private static final org.lgna.croquet.Group ITEM_SELECTABLE_IMPLEMENTATION_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "40759574-7892-469f-93c7-730ed6617d3e" ), "ITEM_SELECTABLE_IMPLEMENTATION_GROUP" );
	private static class ImplementationBooleanState extends org.lgna.croquet.BooleanState {
		public ImplementationBooleanState() {
			super( ITEM_SELECTABLE_IMPLEMENTATION_GROUP, java.util.UUID.fromString( "f0faf391-1b41-417d-98a9-ab9ba1a20335" ), false );
			this.pushIgnore();  //note: we do not pop
		}
	}
	public ItemSelectablePanel( org.lgna.croquet.ListSelectionState<E> model ) {
		super( model );
	}
	
	private boolean isInitialized = false;
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		if( this.isInitialized ) {
			//pass
		} else {
			this.setSwingComboBoxModel(this.getModel().getComboBoxModel());
			this.setSwingListSelectionModel(this.getModel().getListSelectionModel());
			this.isInitialized = true;
		}
	}
	@Override
	protected void handleUndisplayable() {
		//todo?
		super.handleUndisplayable();
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

	private java.util.Map<E, D > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.ComboBoxModel comboBoxModel;
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
	
	protected abstract D createItemDetails( E item, org.lgna.croquet.BooleanState booleanState );
	protected abstract void removeAllDetails();
	protected abstract void addPrologue( int count );
	protected abstract void addItem( D itemDetails );
	protected abstract void addEpilogue();
	
	private java.util.ArrayList<E> prevItems = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
	
	private void handleListDataChanged() {
		synchronized( this.comboBoxModel ) {
			final int N = this.comboBoxModel.getSize();
			
			boolean isActuallyChanged;
			if( N == prevItems.size() ) {
				isActuallyChanged = false;
				for( int i=0; i<N; i++ ) {
					E item = (E)this.comboBoxModel.getElementAt( i );
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
					E item = (E)this.comboBoxModel.getElementAt( i );
					D itemDetails = this.map.get( item );
					if( itemDetails != null ) {
						//pass
					} else {
						org.lgna.croquet.BooleanState booleanState = new ImplementationBooleanState();
						itemDetails = this.createItemDetails( item, booleanState );
						this.map.put( item, itemDetails );
					}
					itemDetails.add( this.buttonGroup );
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
			assert itemDetails != null : item;
			itemDetails.setSelected( true );
		} else {
			javax.swing.ButtonModel model = this.buttonGroup.getSelection();
			if( model != null ) {
				this.buttonGroup.setSelected(model, false);
			}
		}
	}
	private void handleListSelectionChanged() {
		this.handleItemSelected( (E)this.comboBoxModel.getSelectedItem() );
	}

	@Override
	public org.lgna.croquet.components.TrackableShape getTrackableShapeFor( E item ) {
		D itemDetails = this.getItemDetails( item );
		if( itemDetails != null ) {
			return itemDetails.getTrackableShape();
		} else {
			return null;
		}
	}

	/*package-private*/ javax.swing.ComboBoxModel getSwingComboBoxModel() {
		return this.comboBoxModel;
	}
	private void setSwingComboBoxModel( javax.swing.ComboBoxModel model ) {
		if( this.comboBoxModel != null ) {
			synchronized( this.comboBoxModel ) {
				this.comboBoxModel.removeListDataListener( this.listDataListener );
			}
		}
		this.comboBoxModel = model;
		this.handleListDataChanged();
		if( this.comboBoxModel != null ) {
			synchronized( this.comboBoxModel ) {
				this.comboBoxModel.addListDataListener( this.listDataListener );
			}
		}
	}
	/*package-private*/ javax.swing.ListSelectionModel getSwingListSelectionModel() {
		return this.listSelectionModel;
	}
	private void setSwingListSelectionModel( javax.swing.ListSelectionModel listSelectionModel ) {
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
}
