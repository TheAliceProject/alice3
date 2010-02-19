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
package org.alice.ide.resource;

class ResourceTableModel extends javax.swing.table.AbstractTableModel {
	private org.alice.virtualmachine.Resource[] resources;
	private java.util.Set< org.alice.virtualmachine.Resource > referencedResources;
	public ResourceTableModel( org.alice.virtualmachine.Resource[] resources, java.util.Set< org.alice.virtualmachine.Resource > referencedResources ) {
		this.resources = resources;
		this.referencedResources = referencedResources;
	}
	public int getColumnCount() {
		return 3;
	}
	public int getRowCount() {
		return this.resources.length;
	}
	@Override
	public String getColumnName( int columnIndex ) {
		switch( columnIndex ) {
		case 0:
			return "referenced?";
		case 1:
			return "name";
		case 2:
			return "type";
		default:
			return null;
		}
	}
	public Object getValueAt( int rowIndex, int columnIndex ) {
		switch( columnIndex ) {
		case 0:
			return this.referencedResources.contains( this.resources[ rowIndex ] );
		case 1:
			return this.resources[ rowIndex ];
		case 2:
			return this.resources[ rowIndex ].getClass();
		default:
			return null;
		}
	}
}

class IsReferencedTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
	public IsReferencedTableCellRenderer() {
		this.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
	}
	@Override
	public java.awt.Component getTableCellRendererComponent( javax.swing.JTable table, java.lang.Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
		java.awt.Component rv = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		return rv;
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}
/**
 * @author Dennis Cosgrove
 */
public class ResourcesPane extends org.alice.ide.Component {
//	private edu.cmu.cs.dennisc.swing.List< org.alice.virtualmachine.Resource > list;
	private javax.swing.JTable table = new javax.swing.JTable();
	public ResourcesPane() {
		this.setLayout( new java.awt.BorderLayout() );
		org.alice.virtualmachine.Resource[] resources = edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( this.getIDE().getResources(), org.alice.virtualmachine.Resource.class );
//		javax.swing.ListModel listModel = new edu.cmu.cs.dennisc.javax.swing.ArrayListModel( resources );
//		this.list = new edu.cmu.cs.dennisc.swing.List< org.alice.virtualmachine.Resource >( listModel );
		
		java.util.Set< org.alice.virtualmachine.Resource > referencedResources = new java.util.HashSet< org.alice.virtualmachine.Resource >();
		javax.swing.table.TableModel tableModel = new ResourceTableModel( resources, referencedResources );
		table.setModel( tableModel );
		javax.swing.table.TableColumn column0 = this.table.getColumn( this.table.getColumnName( 0 ) );
		//column0.setMaxWidth( 16 );
		column0.setCellRenderer( new IsReferencedTableCellRenderer() );
		
		this.add( new javax.swing.JScrollPane( this.table ), java.awt.BorderLayout.CENTER );
	}
}
