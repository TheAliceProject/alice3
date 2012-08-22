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

/**
 * @author Dennis Cosgrove
 */
public abstract class TableRowSelectionState<T> extends ItemState<T> {
	public class SwingModel {
		private final javax.swing.table.TableModel tableModel;
		private final javax.swing.ListSelectionModel listSelectionModel;
		private final javax.swing.table.TableColumnModel tableColumnModel;

		private SwingModel( javax.swing.table.TableModel tableModel, javax.swing.table.TableColumnModel tableColumnModel, javax.swing.ListSelectionModel listSelectionModel ) {
			this.tableModel = tableModel;
			this.tableColumnModel = tableColumnModel;
			this.listSelectionModel = listSelectionModel;
		}

		public javax.swing.table.TableModel getTableModel() {
			return this.tableModel;
		}

		public javax.swing.table.TableColumnModel getTableColumnModel() {
			return this.tableColumnModel;
		}

		public javax.swing.ListSelectionModel getListSelectionModel() {
			return this.listSelectionModel;
		}
	}

	private final javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			handleListSelectionChanged( e );
		}
	};
	private final SwingModel swingModel;

	public TableRowSelectionState( Group group, java.util.UUID migrationId, T initialValue, ItemCodec<T> itemCodec, javax.swing.table.TableModel tableModel, javax.swing.table.TableColumnModel tableColumnModel, javax.swing.ListSelectionModel listSelectionModel ) {
		super( group, migrationId, initialValue, itemCodec );
		this.swingModel = new SwingModel( tableModel, tableColumnModel, listSelectionModel );
		this.swingModel.getListSelectionModel().addListSelectionListener( this.listSelectionListener );
	}

	public TableRowSelectionState( Group group, java.util.UUID migrationId, T initialValue, ItemCodec<T> itemCodec, javax.swing.table.TableModel tableModel, javax.swing.table.TableColumnModel tableColumnModel ) {
		this( group, migrationId, initialValue, itemCodec, tableModel, tableColumnModel, new javax.swing.DefaultListSelectionModel() );
	}

	public TableRowSelectionState( Group group, java.util.UUID migrationId, T initialValue, ItemCodec<T> itemCodec, javax.swing.table.TableModel tableModel ) {
		this( group, migrationId, initialValue, itemCodec, tableModel, null ); //new javax.swing.table.DefaultTableColumnModel() );
	}

	private void handleListSelectionChanged( javax.swing.event.ListSelectionEvent e ) {
		T prevValue = null;
		T nextValue = this.getValue();
		boolean isAdjusting = false;
		org.lgna.croquet.triggers.Trigger trigger = null;
		this.fireChanging( prevValue, nextValue, isAdjusting );
		if( this.isAppropriateToComplete() ) {
			this.commitStateEdit( prevValue, nextValue, isAdjusting, trigger );
		}
		this.fireChanged( prevValue, nextValue, isAdjusting );
	}

	@Override
	protected void localize() {
	}

	protected abstract T getActualValueAt( int selectionIndex );

	@Override
	protected final T getActualValue() {
		javax.swing.ListSelectionModel listSelectionModel = this.getSwingModel().getListSelectionModel();
		int selectionIndex = listSelectionModel.getLeadSelectionIndex();
		if( selectionIndex < 0 ) {
			return null;
		} else {
			final int N = this.swingModel.getTableModel().getRowCount();
			if( selectionIndex < N ) {
				return this.getActualValueAt( selectionIndex );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( selectionIndex, N, this );
				return null;
			}
		}
	}

	@Override
	public Iterable<? extends org.lgna.croquet.PrepModel> getPotentialRootPrepModels() {
		return java.util.Collections.emptyList();
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	public org.lgna.croquet.components.Table<T> createTable() {
		return new org.lgna.croquet.components.Table<T>( this );
	}
}
