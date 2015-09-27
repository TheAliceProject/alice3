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
package edu.cmu.cs.dennisc.javax.swing.plaf;

/**
 * @author Dennis Cosgrove
 */
public abstract class ListUI<E> extends javax.swing.plaf.ListUI {
	private javax.swing.JList list;
	private javax.swing.ListModel model;
	private javax.swing.ButtonGroup group;
	private java.awt.GridBagConstraints gbc;

	private javax.swing.event.ListDataListener listDataAdapter = new javax.swing.event.ListDataListener() {
		@Override
		public void contentsChanged( javax.swing.event.ListDataEvent e ) {
			ListUI.this.refresh();
		}

		@Override
		public void intervalAdded( javax.swing.event.ListDataEvent e ) {
			for( int i = e.getIndex0(); i <= e.getIndex1(); i++ ) {
				ListUI.this.add( i );
			}
		}

		@Override
		public void intervalRemoved( javax.swing.event.ListDataEvent e ) {
			for( int i = e.getIndex1(); i >= e.getIndex0(); i-- ) {
				ListUI.this.remove( i );
			}
		}
	};
	private javax.swing.event.ListSelectionListener listSelectionListener = new javax.swing.event.ListSelectionListener() {
		@Override
		public void valueChanged( javax.swing.event.ListSelectionEvent e ) {

		}
	};
	private java.beans.PropertyChangeListener propertyListener = new java.beans.PropertyChangeListener() {
		@Override
		public void propertyChange( java.beans.PropertyChangeEvent e ) {
			if( "model".equals( e.getPropertyName() ) ) {
				ListUI.this.refresh();
			}
		}
	};

	protected abstract javax.swing.AbstractButton createComponentFor( int index, E e );

	protected abstract void updateIndex( javax.swing.AbstractButton button, int index );

	private void updateIndices() {
		this.list.revalidate();
		this.list.repaint();
		int i = 0;
		java.util.Enumeration<javax.swing.AbstractButton> e = this.group.getElements();
		while( e.hasMoreElements() ) {
			javax.swing.AbstractButton b = e.nextElement();
			this.updateIndex( b, i );
			i++;
		}
	}

	private void add( final int i ) {
		if( this.list != null ) {
			final E value = (E)model.getElementAt( i );
			javax.swing.AbstractButton button = this.createComponentFor( i, value );
			button.addItemListener( new java.awt.event.ItemListener() {
				@Override
				public void itemStateChanged( java.awt.event.ItemEvent e ) {
					if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
						if( ListUI.this.list != null ) {
							javax.swing.ListSelectionModel model = ListUI.this.list.getSelectionModel();
							model.setSelectionInterval( i, i );
						}
					}
				}
			} );
			this.group.add( button );
			this.list.add( button, this.gbc, i );
			this.updateIndices();
		}
	}

	private void remove( int i ) {
		if( this.list != null ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)this.list.getComponent( i );
			this.group.remove( button );
			this.list.remove( button );
			this.updateIndices();
		}
	}

	private void refresh() {
		if( this.list != null ) {
			if( this.model != this.list.getModel() ) {
				if( this.model != null ) {
					this.model.removeListDataListener( this.listDataAdapter );
				}
				this.model = this.list.getModel();
				if( this.model != null ) {
					this.model.addListDataListener( this.listDataAdapter );
				}
			}
			this.list.removeAll();
			this.group = new javax.swing.ButtonGroup();
			final int N = this.model.getSize();
			for( int i = 0; i < N; i++ ) {
				this.add( i );
			}
			gbc.weighty = 1.0;
			this.list.add( javax.swing.Box.createGlue(), gbc );
			gbc.weighty = 0.0;
			this.list.revalidate();
			this.list.repaint();
		}
	}

	@Override
	public void installUI( javax.swing.JComponent c ) {
		super.installUI( c );
		this.list = (javax.swing.JList)c;
		this.list.setLayout( new java.awt.GridBagLayout() );
		this.group = new javax.swing.ButtonGroup();
		this.gbc = new java.awt.GridBagConstraints();
		this.gbc.fill = java.awt.GridBagConstraints.BOTH;
		this.gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		this.gbc.weightx = 1.0;
		this.refresh();
	}

	@Override
	public void uninstallUI( javax.swing.JComponent c ) {
		this.model.removeListDataListener( this.listDataAdapter );
		this.model = null;
		this.group = null;
		this.gbc = null;
		this.list.setLayout( null );
		this.list.removeAll();
		this.list = null;
		super.uninstallUI( c );
	}

	@Override
	public java.awt.Rectangle getCellBounds( javax.swing.JList list, int index1, int index2 ) {
		java.awt.Rectangle rv = null;
		final int N = list.getComponentCount() - 1;
		if( ( ( 0 <= index1 ) && ( index1 < N ) ) && ( ( 0 <= index2 ) && ( index2 < N ) ) ) {
			for( int i = index1; i <= index2; i++ ) {
				java.awt.Component c = list.getComponent( i );
				if( rv != null ) {
					rv = rv.union( c.getBounds() );
				} else {
					rv = c.getBounds();
				}
			}
		}
		return rv;
	}

	@Override
	public java.awt.Point indexToLocation( javax.swing.JList list, int index ) {
		return list.getComponent( index ).getLocation();
	}

	@Override
	public int locationToIndex( javax.swing.JList list, java.awt.Point location ) {
		final int N = list.getComponentCount() - 1;
		for( int i = 0; i < N; i++ ) {
			java.awt.Component c = list.getComponent( i );
			if( c.contains( location ) ) {
				return i;
			}
		}
		return -1;
	}
	//	
	//	public static void main( String[] args ) {
	//		javax.swing.DefaultListModel model = new javax.swing.DefaultListModel();
	//		model.addElement( 1 );
	//		model.addElement( 3 );
	//		model.addElement( 4 );
	//		javax.swing.JList list = new javax.swing.JList( model );
	//				
	//		ListUI ui = new ListUI() {
	//			@Override
	//			protected javax.swing.AbstractButton createComponentFor( int index, Object o ) {
	//				return new javax.swing.JCheckBox( o.toString() );
	//			}
	//		};
	//		list.setUI( ui );
	//		
	//		model.addElement( 5 );
	//		model.add( 0, 0 );
	//		model.add( 2, 2 );
	//		
	//		model.remove( 4 );
	//		model.remove( 0 );
	//		
	//		javax.swing.JFrame frame = new javax.swing.JFrame();
	//		frame.getContentPane().add( list, java.awt.BorderLayout.CENTER );
	//		frame.setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
	//		frame.setSize( 320, 240 );
	//		frame.setVisible( true );
	//	}
}
