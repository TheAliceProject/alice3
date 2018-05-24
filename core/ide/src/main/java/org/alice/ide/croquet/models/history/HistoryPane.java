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

import edu.cmu.cs.dennisc.javax.swing.components.JBorderPane;
import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocument;
import org.alice.ide.project.ProjectDocumentState;
import org.lgna.croquet.Group;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.undo.UndoHistory;
import org.lgna.croquet.undo.event.HistoryClearEvent;
import org.lgna.croquet.undo.event.HistoryInsertionIndexEvent;
import org.lgna.croquet.undo.event.HistoryListener;
import org.lgna.croquet.undo.event.HistoryPushEvent;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;

class HistoryStackModel extends AbstractListModel {
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

class HistoryCellRenderer extends ListCellRenderer<Edit> {
	@Override
	protected JLabel getListCellRendererComponent( JLabel rv, JList list, Edit value, int index, boolean isSelected, boolean cellHasFocus ) {
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

public class HistoryPane extends JBorderPane {
	private HistoryListener historyListener = new HistoryListener() {
		@Override
		public void operationPushing( HistoryPushEvent e ) {
		}

		@Override
		public void operationPushed( HistoryPushEvent e ) {
		}

		@Override
		public void insertionIndexChanging( HistoryInsertionIndexEvent e ) {
		}

		@Override
		public void insertionIndexChanged( HistoryInsertionIndexEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( e.getNextIndex() );
			HistoryPane.this.list.repaint();
		}

		@Override
		public void clearing( HistoryClearEvent e ) {
		}

		@Override
		public void cleared( HistoryClearEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( 0 );
		}
	};

	private Group group;
	private JList list;
	private HistoryStackModel historyStackModel;
	private UndoHistory projectHistory;
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged( ListSelectionEvent e ) {
			if( e.getValueIsAdjusting() ) {
				//pass
			} else {
				projectHistory.setInsertionIndex( list.getSelectedIndex() );
				HistoryPane.this.list.repaint();
			}
		}
	};

	private final ValueListener<ProjectDocument> projectListener = new ValueListener<ProjectDocument>() {
		@Override
		public void valueChanged( ValueEvent<ProjectDocument> e ) {
			ProjectDocument nextValue = e.getNextValue();
			HistoryPane.this.initializeProjectHistory( nextValue != null ? nextValue : null );
		}
	};

	public HistoryPane( Group group ) {
		this.group = group;
		ProjectDocumentState.getInstance().addNewSchoolValueListener( this.projectListener );
		this.list = new JList();
		this.list.setCellRenderer( new HistoryCellRenderer() );
		this.list.addListSelectionListener( this.listSelectionListener );
		JScrollPane scrollPane = new JScrollPane( this.list );
		this.add( scrollPane );
		this.initializeProjectHistory( IDE.getActiveInstance().getDocumentFrame().getDocument() );
	}

	public void initializeProjectHistory( ProjectDocument projectDocument ) {
		this.projectHistory = projectDocument.getUndoHistory( this.group );
		this.historyStackModel = new HistoryStackModel( this.projectHistory );
		this.list.setModel( this.historyStackModel );
		this.historyStackModel.getHistoryManager().addHistoryListener( this.historyListener );
		this.list.setSelectedIndex( this.historyStackModel.getHistoryManager().getInsertionIndex() );
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension( 240, 768 );
	}
}
