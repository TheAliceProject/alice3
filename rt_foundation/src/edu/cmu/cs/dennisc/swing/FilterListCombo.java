/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class FilterListCombo extends javax.swing.JPanel {
	class Model extends javax.swing.AbstractListModel {
		private String[] m_data = {};
		private java.util.List< String > m_filteredData = new java.util.LinkedList< String >();

		public Object getElementAt( int index ) {
			return m_filteredData.get( index );
		}
		public int getSize() {
			return m_filteredData.size();
		}

		public void setData( String[] data ) {
			m_data = data;
		}
		public void handleFilterChange( String filter ) {
			//todo: allow sensitive and insensitive filtering
			String filterLower = filter.toLowerCase();
			m_filteredData = new java.util.LinkedList< String >();
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
	private List< String > m_list = new List< String >();

	public FilterListCombo() {
		m_list.setModel( m_model );
		m_filter.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				FilterListCombo.this.handleFilterChange( e );
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				FilterListCombo.this.handleFilterChange( e );
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				FilterListCombo.this.handleFilterChange( e );
			}
		} );

		setLayout( new java.awt.BorderLayout() );
		add( m_filter, java.awt.BorderLayout.NORTH );
		add( new javax.swing.JScrollPane( m_list ), java.awt.BorderLayout.CENTER );

	}

	public javax.swing.JTextField getTextField() {
		return m_filter;
	}
	public List< String > getList() {
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
