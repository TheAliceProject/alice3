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
public class List<E> extends ItemSelectable<javax.swing.JList, E > {
	private long tModelChange;
	private class ListUI extends javax.swing.plaf.basic.BasicListUI {
		@Override
		protected javax.swing.event.MouseInputListener createMouseInputListener() {
			return new javax.swing.event.MouseInputListener() {
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
				public void mouseEntered( java.awt.event.MouseEvent e ) {
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					long tCurrent = e.getWhen();
					long tDelta = tCurrent - List.this.tModelChange;
					if( tDelta > 400 ) {
						int row = ListUI.this.locationToIndex( list, e.getPoint() );
		                list.setValueIsAdjusting( true );
		                list.setSelectionInterval(row, row);
					}
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
	                list.setValueIsAdjusting( false );
				}
				public void mouseMoved( java.awt.event.MouseEvent e ) {
				}
				public void mouseDragged( java.awt.event.MouseEvent e ) {
				}
			};
		}
	}

	public enum LayoutOrientation {
		VERTICAL ( javax.swing.JList.VERTICAL ),
		VERTICAL_WRAP( javax.swing.JList.VERTICAL_WRAP ),
		HORIZONTAL_WRAP( javax.swing.JList.HORIZONTAL_WRAP );
		private int internal;
		private LayoutOrientation( int internal ) {
			this.internal = internal;
		}
//		/*package-private*/ int getInternal() {
//			return this.internal;
//		}
	}
	
	public List( ListSelectionState<E> model ) {
		super( model );
		this.setSwingListModel(model.getComboBoxModel());
		this.setSelectionModel(model.getListSelectionModel());
	}

	@Override
	protected javax.swing.JList createAwtComponent() {
		return new javax.swing.JList() {
			@Override
			public void updateUI() {
				this.setUI( new ListUI() );
			}
		};
	}
	
	@Override
	/*package-private*/ TrackableShape getTrackableShapeFor( E item ) {
		//todo
		return this;
	}
	
	
//	public enum DoubleClickBehavior {
//		DO_NOTHING,
//		DO_DEFAULT_BUTTON_CLICK,
//	}
//	private DoubleClickBehavior doubleClickBehavior = DoubleClickBehavior.DO_NOTHING;
//	public DoubleClickBehavior getDoubleClickBehavior() {
//		return this.doubleClickBehavior;
//	}
//	public void setDoubleClickBehavior( DoubleClickBehavior doubleClickBehavior ) {
//		assert doubleClickBehavior != null;
//		this.doubleClickBehavior = doubleClickBehavior;
//	}

	public javax.swing.ListCellRenderer getCellRenderer() {
		return this.getAwtComponent().getCellRenderer();
	}
	public void setCellRenderer( javax.swing.ListCellRenderer listCellRenderer ) {
		this.getAwtComponent().setCellRenderer( listCellRenderer );
	}


	public int getVisibleRowCount() {
		return this.getAwtComponent().getVisibleRowCount();
	}
	public void setVisibleRowCount( int visibleRowCount ) {
		this.getAwtComponent().setVisibleRowCount( visibleRowCount );
	}
	
	public void setLayoutOrientation( LayoutOrientation layoutOrientation ) {
		this.getAwtComponent().setLayoutOrientation( layoutOrientation.internal );
	}

	private class ListDataListener implements javax.swing.event.ListDataListener { 
		private void handleChanged() {
			List.this.tModelChange = System.currentTimeMillis();
			List.this.revalidateAndRepaint();
		}
		public void contentsChanged( javax.swing.event.ListDataEvent e ) {
			this.handleChanged();
		}
		public void intervalAdded( javax.swing.event.ListDataEvent e ) {
			this.handleChanged();
		}
		public void intervalRemoved( javax.swing.event.ListDataEvent e ) {
			this.handleChanged();
		}
	}
	private ListDataListener listDataListener = new ListDataListener();
	private javax.swing.ListModel model;
	/*package-private*/ void setSwingListModel( javax.swing.ListModel model ) {
		if( this.model != null ) {
			this.model.removeListDataListener( this.listDataListener );
		}
		this.model = model;
		this.getAwtComponent().setModel( model );
		this.listDataListener.handleChanged();
		if( this.model != null ) {
			this.model.addListDataListener( this.listDataListener );
		}
	}
	
	/*package-private*/ void setSelectionModel( javax.swing.ListSelectionModel listSelectionModel ) {
		this.getAwtComponent().setSelectionModel( listSelectionModel );
	}
	/*package-private*/ void addListSelectionListener( javax.swing.event.ListSelectionListener listSelectionListener ) {
		this.getAwtComponent().addListSelectionListener( listSelectionListener );
	}
	/*package-private*/ void removeListSelectionListener( javax.swing.event.ListSelectionListener listSelectionListener ) {
		this.getAwtComponent().removeListSelectionListener( listSelectionListener );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().addComponent( this );
	}
	@Override
	protected void handleUndisplayable() {
		this.getModel().removeComponent( this );
		super.handleUndisplayable();
	}
}
