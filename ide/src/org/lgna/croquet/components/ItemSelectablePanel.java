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
public abstract class ItemSelectablePanel<E, ID extends ItemDetails<E>> extends ItemSelectable<javax.swing.JPanel, E, org.lgna.croquet.ListSelectionState<E>> {
	public ItemSelectablePanel( org.lgna.croquet.ListSelectionState<E> model ) {
		super( model );
	}

	private boolean isInitialized = false;

	@Override
	protected void handleDisplayable() {
		if( this.isInitialized ) {
			//pass
		} else {
			this.setSwingComboBoxModel( this.getModel().getSwingModel().getComboBoxModel() );
			this.setSwingListSelectionModel( this.getModel().getSwingModel().getListSelectionModel() );
			this.isInitialized = true;
		}
		super.handleDisplayable();
	}

	@Override
	protected void handleUndisplayable() {
		//todo?
		super.handleUndisplayable();
	}

	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel );

	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return constrainPreferredSizeIfNecessary( super.getPreferredSize() );
			}

			@Override
			public java.awt.Dimension getMaximumSize() {
				java.awt.Dimension rv = super.getMaximumSize();
				if( ItemSelectablePanel.this.isMaximumSizeClampedToPreferredSize() ) {
					rv.setSize( this.getPreferredSize() );
				}
				return rv;
			}
		};
		java.awt.LayoutManager layoutManager = this.createLayoutManager( rv );
		rv.setLayout( layoutManager );
		rv.setOpaque( false );
		rv.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		rv.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		return rv;
	}

	private java.util.Map<E, ID> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
	private javax.swing.ComboBoxModel comboBoxModel;
	private javax.swing.ListSelectionModel listSelectionModel;
	private javax.swing.event.ListDataListener listDataListener = new javax.swing.event.ListDataListener() {
		public void intervalAdded( javax.swing.event.ListDataEvent e ) {
			ItemSelectablePanel.this.handleListDataChanged();
		}

		public void intervalRemoved( javax.swing.event.ListDataEvent e ) {
			ItemSelectablePanel.this.handleListDataChanged();
		}

		public void contentsChanged( javax.swing.event.ListDataEvent e ) {
			ItemSelectablePanel.this.handleListDataChanged();
		}
	};

	protected ID getItemDetails( E item ) {
		return this.map.get( item );
	}

	protected java.util.Collection<ID> getAllItemDetails() {
		return this.map.values();
	}

	protected abstract void removeAllDetails();

	protected abstract void addPrologue( int count );

	protected abstract void addItem( ID itemDetails );

	protected abstract void addEpilogue();

	protected abstract BooleanStateButton<?> createButtonForItemSelectedState( E item, org.lgna.croquet.BooleanState itemSelectedState );

	private java.util.ArrayList<E> prevItems = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();

	private void handleListDataChanged() {
		synchronized( this.comboBoxModel ) {
			final int N = this.comboBoxModel.getSize();

			boolean isActuallyChanged;
			if( N == prevItems.size() ) {
				isActuallyChanged = false;
				for( int i = 0; i < N; i++ ) {
					E item = (E)this.comboBoxModel.getElementAt( i );
					if( item == prevItems.get( i ) ) {
						//pass
					} else {
						isActuallyChanged = true;
						break;
					}
				}
			} else {
				isActuallyChanged = true;
				;
			}

			if( isActuallyChanged ) {
				synchronized( this.getTreeLock() ) {
					this.removeAllDetails();
					this.prevItems.clear();
					this.prevItems.ensureCapacity( N );
					this.addPrologue( N );
					for( int i = 0; i < N; i++ ) {
						E item = (E)this.comboBoxModel.getElementAt( i );
						ID itemDetails = this.map.get( item );
						if( itemDetails != null ) {
							//pass
						} else {
							itemDetails = this.createItemDetails( item );
							this.map.put( item, itemDetails );
						}
						itemDetails.add( this.buttonGroup );
						this.addItem( itemDetails );
						this.prevItems.add( item );
					}
					this.addEpilogue();
				}
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

	protected abstract ID createItemDetails( E item );

	//	protected void handleItemSelected( E item ) {
	//		if( item != null ) {
	//			ItemDetails<E> itemDetails = this.map.get( item );
	//			assert itemDetails != null : item;
	//			itemDetails.setSelected( true );
	//		} else {
	//			//todo: use buttonGroup.clearSelection() when 1.6
	//			java.util.Enumeration<javax.swing.AbstractButton> buttonEnum = this.buttonGroup.getElements();
	//			while( buttonEnum.hasMoreElements() ) {
	//				javax.swing.AbstractButton button = buttonEnum.nextElement();
	//				javax.swing.ButtonModel model = button.getModel();
	//				if( model.isSelected() ) {
	//					this.buttonGroup.remove( button );
	//					model.setSelected( false );
	//					this.buttonGroup.add( button );
	//				}
	//			}
	//		}
	//	}

	@Override
	public org.lgna.croquet.components.TrackableShape getTrackableShapeFor( E item ) {
		ItemDetails<E> itemDetails = this.getItemDetails( item );
		if( itemDetails != null ) {
			return itemDetails.getTrackableShape();
		} else {
			return null;
		}
	}

	/* package-private */javax.swing.ComboBoxModel getSwingComboBoxModel() {
		return this.comboBoxModel;
	}

	private void setSwingComboBoxModel( javax.swing.ComboBoxModel model ) {
		//		if( this.comboBoxModel != null ) {
		//			synchronized( this.comboBoxModel ) {
		//				this.comboBoxModel.removeListDataListener( this.listDataListener );
		//			}
		//		}
		this.comboBoxModel = model;
		this.handleListDataChanged();
		//		if( this.comboBoxModel != null ) {
		//			synchronized( this.comboBoxModel ) {
		//				this.comboBoxModel.addListDataListener( this.listDataListener );
		//			}
		//		}
	}

	/* package-private */javax.swing.ListSelectionModel getSwingListSelectionModel() {
		return this.listSelectionModel;
	}

	private void setSwingListSelectionModel( javax.swing.ListSelectionModel listSelectionModel ) {
		//		if( this.listSelectionModel != null ) {
		//			synchronized( this.listSelectionModel ) {
		//				this.listSelectionModel.removeListSelectionListener( this.listSelectionListener );
		//			}
		//		}
		this.listSelectionModel = listSelectionModel;
		//this.handleListSelectionChanged();
		//		if( this.listSelectionModel != null ) {
		//			synchronized( this.listSelectionModel ) {
		//				this.listSelectionModel.addListSelectionListener( this.listSelectionListener );
		//			}
		//		}
	}
}
