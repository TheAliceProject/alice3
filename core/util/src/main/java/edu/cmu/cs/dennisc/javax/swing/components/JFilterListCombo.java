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
package edu.cmu.cs.dennisc.javax.swing.components;

/**
 * @author Dennis Cosgrove
 */
public class JFilterListCombo extends javax.swing.JPanel {
	class Model extends javax.swing.AbstractListModel {
		private String[] m_data = {};
		private java.util.List<String> m_filteredData = new java.util.LinkedList<String>();

		@Override
		public Object getElementAt( int index ) {
			return m_filteredData.get( index );
		}

		@Override
		public int getSize() {
			return m_filteredData.size();
		}

		public void setData( String[] data ) {
			m_data = data;
		}

		public void handleFilterChange( String filter ) {
			//todo: allow sensitive and insensitive filtering
			String filterLower = filter.toLowerCase();
			m_filteredData = new java.util.LinkedList<String>();
			for( String s : m_data ) {
				String sLower = s.toLowerCase();
				if( sLower.contains( filterLower ) ) {
					m_filteredData.add( s );
				}
			}
			fireContentsChanged( this, 0, getSize() );
		}
	}

	private Model m_model = new Model();

	private javax.swing.JTextField m_filter = new javax.swing.JTextField();
	private JList<String> m_list = new JList<String>();

	public JFilterListCombo() {
		m_list.setModel( m_model );
		m_filter.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			@Override
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				JFilterListCombo.this.handleFilterChange( e );
			}

			@Override
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				JFilterListCombo.this.handleFilterChange( e );
			}

			@Override
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				JFilterListCombo.this.handleFilterChange( e );
			}
		} );

		setLayout( new java.awt.BorderLayout() );
		add( m_filter, java.awt.BorderLayout.NORTH );
		add( new javax.swing.JScrollPane( m_list ), java.awt.BorderLayout.CENTER );

	}

	public javax.swing.JTextField getTextField() {
		return m_filter;
	}

	public JList<String> getList() {
		return m_list;
	}

	public String getElementAt( int index ) {
		return (String)m_model.getElementAt( index );
	}

	public void setData( String[] data ) {
		m_model.setData( data );
		m_model.handleFilterChange( m_filter.getText() );
	}

	private void handleFilterChange( javax.swing.event.DocumentEvent e ) {
		m_model.handleFilterChange( m_filter.getText() );
	}
}
