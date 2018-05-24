/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.croquet;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.croquet.views.Table;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class SingleSelectTableRowState<T> extends ItemState<T> implements Iterable<T> {
	public class SwingModel {
		private final TableModel tableModel;
		private final ListSelectionModel listSelectionModel;
		private final TableColumnModel tableColumnModel;

		private SwingModel( TableModel tableModel, TableColumnModel tableColumnModel, ListSelectionModel listSelectionModel ) {
			this.tableModel = tableModel;
			this.tableColumnModel = tableColumnModel;
			this.listSelectionModel = listSelectionModel;
		}

		public TableModel getTableModel() {
			return this.tableModel;
		}

		public TableColumnModel getTableColumnModel() {
			return this.tableColumnModel;
		}

		public ListSelectionModel getListSelectionModel() {
			return this.listSelectionModel;
		}
	}

	private final ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged( ListSelectionEvent e ) {
			handleListSelectionChanged( e );
		}
	};
	private final SwingModel swingModel;

	public SingleSelectTableRowState( Group group, UUID migrationId, T initialValue, ItemCodec<T> itemCodec, TableModel tableModel, TableColumnModel tableColumnModel, ListSelectionModel listSelectionModel ) {
		super( group, migrationId, initialValue, itemCodec );
		this.swingModel = new SwingModel( tableModel, tableColumnModel, listSelectionModel );
		this.swingModel.getListSelectionModel().addListSelectionListener( this.listSelectionListener );
	}

	public SingleSelectTableRowState( Group group, UUID migrationId, T initialValue, ItemCodec<T> itemCodec, TableModel tableModel, TableColumnModel tableColumnModel ) {
		this( group, migrationId, initialValue, itemCodec, tableModel, tableColumnModel, new DefaultListSelectionModel() );
	}

	public SingleSelectTableRowState( Group group, UUID migrationId, T initialValue, ItemCodec<T> itemCodec, TableModel tableModel ) {
		this( group, migrationId, initialValue, itemCodec, tableModel, null ); //new javax.swing.table.DefaultTableColumnModel() );
	}

	private void handleListSelectionChanged( ListSelectionEvent e ) {
		Trigger trigger = null;
		this.changeValueFromSwing( this.getSwingValue(), IsAdjusting.valueOf( e.getValueIsAdjusting() ), trigger );
	}

	@Override
	protected void localize() {
	}

	public abstract T getItemAt( int index );

	public int getItemCount() {
		return this.swingModel.tableModel.getRowCount();
	}

	public Collection<T> getItems() {
		final int N = this.getItemCount();
		List<T> rv = Lists.newArrayListWithInitialCapacity( N );
		for( int i = 0; i < N; i++ ) {
			rv.add( this.getItemAt( i ) );
		}
		return rv;
	}

	@Override
	public Iterator<T> iterator() {
		return this.getItems().iterator();
	}

	@Override
	protected final T getSwingValue() {
		ListSelectionModel listSelectionModel = this.getSwingModel().getListSelectionModel();
		int selectionIndex = listSelectionModel.getLeadSelectionIndex();
		if( selectionIndex < 0 ) {
			return null;
		} else {
			final int N = this.swingModel.getTableModel().getRowCount();
			if( selectionIndex < N ) {
				return this.getItemAt( selectionIndex );
			} else {
				Logger.severe( selectionIndex, N, this );
				return null;
			}
		}
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Collections.emptyList();
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	public Table<T> createTable() {
		return new Table<T>( this );
	}
}
