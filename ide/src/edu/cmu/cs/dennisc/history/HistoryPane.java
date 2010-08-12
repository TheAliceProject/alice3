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
package edu.cmu.cs.dennisc.history;

class HistoryStackModel extends javax.swing.AbstractListModel {
	private HistoryManager historyManager;
	public HistoryStackModel( HistoryManager historyManager ) {
		this.historyManager = historyManager;
	}
	public int getSize() {
		return historyManager.getStack().size() + 1;
	}
	public Object getElementAt( int index ) {
		if( index == 0 ) {
			return null;
		} else {
			return historyManager.getStack().elementAt( index-1 );
		}
	}
	public HistoryManager getHistoryManager() {
		return this.historyManager;
	}
	public void refresh() {
		this.fireContentsChanged( this, 0, this.getSize() );
	}
};

class HistoryCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< edu.cmu.cs.dennisc.croquet.Edit > {
	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.croquet.Edit value, int index, boolean isSelected, boolean cellHasFocus ) {
		if( index == 0 ) {
			rv.setText( "---open project---" );
		} else {
			//todo
			java.util.Locale locale = javax.swing.JComponent.getDefaultLocale();
			
			String text = value.getPresentation( locale );
//			text = "edit: " + text;
			rv.setText( text );
			
			int selectedIndex = list.getSelectedIndex();
			if( selectedIndex >= 0 && index > selectedIndex ) {
				rv.setEnabled( false );
			} else {
				rv.setEnabled( true );
			}
		}
		return rv;
	}
}

public class HistoryPane extends edu.cmu.cs.dennisc.javax.swing.components.JBorderPane {
	private edu.cmu.cs.dennisc.history.event.HistoryListener historyListener = new edu.cmu.cs.dennisc.history.event.HistoryListener() {
		public void operationPushing( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
		}
		public void operationPushed( edu.cmu.cs.dennisc.history.event.HistoryPushEvent e ) {
		}
		public void insertionIndexChanging( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
		}
		public void insertionIndexChanged( edu.cmu.cs.dennisc.history.event.HistoryInsertionIndexEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( e.getNextIndex() );
			HistoryPane.this.list.repaint();
		}
		public void clearing( edu.cmu.cs.dennisc.history.event.HistoryClearEvent e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.history.event.HistoryClearEvent e ) {
			HistoryPane.this.historyStackModel.refresh();
			HistoryPane.this.list.setSelectedIndex( 0 );
		}
	};

	private javax.swing.JList list = new javax.swing.JList();
	private HistoryStackModel historyStackModel;
	public HistoryPane( edu.cmu.cs.dennisc.croquet.Group group ) {
		final HistoryManager historyManager = HistoryManager.getInstance( group );
		this.historyStackModel = new HistoryStackModel( historyManager );
		this.list.setModel( this.historyStackModel );
		this.list.setCellRenderer( new HistoryCellRenderer() );
		this.list.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					historyManager.setInsertionIndex(list.getSelectedIndex());
					HistoryPane.this.list.repaint();
				}
			}
		} );
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.list );
		this.add( scrollPane );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.historyStackModel.getHistoryManager().addHistoryListener( this.historyListener );
		this.list.setSelectedIndex( this.historyStackModel.getHistoryManager().getInsertionIndex() );
	}
	@Override
	public void removeNotify() {
		this.list.setSelectedIndex( -1 );
		this.historyStackModel.getHistoryManager().removeHistoryListener( this.historyListener );
		super.removeNotify();
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 240, 768 );
	}
}
