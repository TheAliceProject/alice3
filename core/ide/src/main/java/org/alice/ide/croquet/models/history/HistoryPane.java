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
package org.alice.ide.croquet.models.history;

import org.lgna.croquet.undo.UndoHistory;

class HistoryStackModel extends javax.swing.AbstractListModel {
	private UndoHistory projectHistory;

	public HistoryStackModel( UndoHistory historyManager ) {
		this.projectHistory = historyManager;
	}

	@Override
	public int getSize() {
		return projectHistory.getStack().size() + 1;
	}

	@Override
	public Object getElementAt( int index ) {
		if( index == 0 ) {
			return null;
		} else {
			return projectHistory.getStack().get( index - 1 );
		}
	}

	public UndoHistory getHistoryManager() {
		return this.projectHistory;
	}

	public void refresh() {
		this.fireContentsChanged( this, 0, this.getSize() );
	}
};

class HistoryCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer<org.lgna.croquet.edits.Edit> {
	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, org.lgna.croquet.edits.Edit value, int index, boolean isSelected, boolean cellHasFocus ) {
		if( index == 0 ) {
			rv.setText( "---open project---" );
		} else {
			String text = value.getTerseDescription();
			rv.setText( text );

			int selectedIndex = list.getSelectedIndex();
			if( ( selectedIndex >= 0 ) && ( index > selectedIndex ) ) {
				rv.setEnabled( false );
			} else {
				rv.setEnabled( true );
			}
		}
		return rv;
	}
}

public class HistoryPane extends edu.cmu.cs.dennisc.javax.swing.components.JBorderPane {
	private org.lgna.croquet.undo.event.HistoryListener historyListener = new org.lgna.croquet.undo.event.HistoryListener() {
		@Override
		public void operationPushing( org.lgna.croquet.undo.event.HistoryPushEvent e ) {
		}

		@Override
		public void operationPushed( org.lgna.croquet.undo.event.HistoryPushEvent e ) {
		}

		@Override
		public void insertionIndexChanging( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
		}

		@Override
		public void insertionIndexChanged( org.lgna.croquet.undo.event.HistoryInsertionIndexEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( e.getNextIndex() );
			HistoryPane.this.list.repaint();
		}

		@Override
		public void clearing( org.lgna.croquet.undo.event.HistoryClearEvent e ) {
		}

		@Override
		public void cleared( org.lgna.croquet.undo.event.HistoryClearEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( 0 );
		}
	};

	private org.lgna.croquet.Group group;
	private javax.swing.JList list;
	private HistoryStackModel historyStackModel;
	private UndoHistory projectHistory;
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		@Override
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
			if( e.getValueIsAdjusting() ) {
				//pass
			} else {
				projectHistory.setInsertionIndex( list.getSelectedIndex() );
				HistoryPane.this.list.repaint();
			}
		}
	};

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.ProjectDocument> projectListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.ProjectDocument>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.ProjectDocument> e ) {
			org.alice.ide.ProjectDocument nextValue = e.getNextValue();
			HistoryPane.this.initializeProjectHistory( nextValue != null ? nextValue : null );
		}
	};

	public HistoryPane( org.lgna.croquet.Group group ) {
		this.group = group;
		org.alice.ide.project.ProjectDocumentState.getInstance().addNewSchoolValueListener( this.projectListener );
		this.list = new javax.swing.JList();
		this.list.setCellRenderer( new HistoryCellRenderer() );
		this.list.addListSelectionListener( this.listSelectionListener );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.list );
		this.add( scrollPane );
		this.initializeProjectHistory( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDocument() );
	}

	public void initializeProjectHistory( org.alice.ide.ProjectDocument projectDocument ) {
		this.projectHistory = projectDocument.getUndoHistory( this.group );
		this.historyStackModel = new HistoryStackModel( this.projectHistory );
		this.list.setModel( this.historyStackModel );
		this.historyStackModel.getHistoryManager().addHistoryListener( this.historyListener );
		this.list.setSelectedIndex( this.historyStackModel.getHistoryManager().getInsertionIndex() );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 240, 768 );
	}
}
