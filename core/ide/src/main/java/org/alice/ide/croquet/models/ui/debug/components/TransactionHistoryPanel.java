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

package org.alice.ide.croquet.models.ui.debug.components;

/**
 * @author Dennis Cosgrove
 */
//<kjh remove file/>
@Deprecated
public class TransactionHistoryPanel extends org.lgna.croquet.components.BorderPanel {

	private final org.lgna.croquet.history.event.Listener transactionListener = new org.lgna.croquet.history.event.Listener() {
		public void changing( org.lgna.croquet.history.event.Event<?> e ) {
		}

		public void changed( org.lgna.croquet.history.event.Event<?> e ) {
			if( ( e instanceof org.lgna.croquet.history.event.AddStepEvent ) || ( e instanceof org.lgna.croquet.history.event.AddTransactionEvent ) ) {
				TransactionHistoryPanel.this.reload();
			} else if( ( e instanceof org.lgna.croquet.history.event.FinishedEvent ) || ( e instanceof org.lgna.croquet.history.event.EditCommittedEvent ) ) {
				tree.repaint();
			}
		}
	};

	private final org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane();
	private final javax.swing.JTree tree = new javax.swing.JTree();
	private org.lgna.croquet.history.TransactionHistory transactionHistory;
	private final java.awt.event.ActionListener refreshListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			TransactionHistoryPanel.this.reload();
		}
	};

	public TransactionHistoryPanel() {
		this.tree.setCellRenderer( new TransactionHistoryCellRenderer() );
		this.scrollPane.getAwtComponent().setViewportView( this.tree );
		this.addCenterComponent( scrollPane );
		this.tree.setRootVisible( false );
		this.tree.registerKeyboardAction( this.refreshListener, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F5, 0 ), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
	}

	private void reload() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				TransactionHistoryTreeModel treeModel = (TransactionHistoryTreeModel)tree.getModel();
				treeModel.reload();
				int childCount = treeModel.getChildCount( treeModel.getRoot() );
				for( int i = 0; i < tree.getRowCount(); i++ ) {
					if( i < ( childCount - 1 ) ) {
						tree.collapseRow( i );
					} else {
						tree.expandRow( i );
					}
				}
				tree.scrollRowToVisible( tree.getRowCount() - 1 );
				if( scrollPane != null ) {
					scrollPane.getAwtComponent().getHorizontalScrollBar().setValue( 0 );
				}
			}
		} );
	}

	public void setTransactionHistory( org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		this.removeTransactionListener();
		this.transactionHistory = transactionHistory;

		if( this.isShowing() ) {
			this.addTransactionListener();
		}
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.addTransactionListener();
	}

	@Override
	protected void handleUndisplayable() {
		this.removeTransactionListener();
		super.handleUndisplayable();
	}

	private void addTransactionListener() {
		if( this.transactionHistory != null ) {
			TransactionHistoryTreeModel treeModel = new TransactionHistoryTreeModel( this.transactionHistory );
			treeModel.reload();
			this.tree.setModel( treeModel );
			for( int i = 0; i < this.tree.getRowCount(); i++ ) {
				this.tree.expandRow( i );
			}
			this.transactionHistory.addListener( this.transactionListener );
		}
	}

	private void removeTransactionListener() {
		if( ( this.transactionHistory != null ) && ( this.transactionHistory.isListening( this.transactionListener ) ) ) {
			this.transactionHistory.removeListener( this.transactionListener );
		}
	}
}
