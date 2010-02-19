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
		if( this.resources != null ) {
			return this.resources.length;
		} else {
			return 0;
		}
	}
	@Override
	public String getColumnName( int columnIndex ) {
		switch( columnIndex ) {
		case 0:
			return "is referenced?";
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
			return this.resources[ rowIndex ].getName();
		case 2:
			return this.resources[ rowIndex ];
		default:
			return null;
		}
	}
}

abstract class ResourceTableCellRenderer< E > extends edu.cmu.cs.dennisc.croquet.swing.TableCellRenderer< E > {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent(javax.swing.JLabel rv, javax.swing.JTable table, E value, boolean isSelected, boolean hasFocus, int row, int column) {
		rv.setHorizontalAlignment( javax.swing.SwingConstants.CENTER );
		rv.setBorder( null );
		return rv;
	}
}

class ResourceIsReferencedTableCellRenderer extends ResourceTableCellRenderer< Boolean > {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, Boolean value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		java.awt.Color foreground;
		if( value ) {
			foreground = java.awt.Color.BLACK;
			text = "yes";
		} else {
			foreground = new java.awt.Color( 255, 0, 0 );
			text = "NO";
		}
		rv.setText( text );
		rv.setForeground( foreground );
		return rv;
	}
}

class ResourceTypeTableCellRenderer extends ResourceTableCellRenderer< org.alice.virtualmachine.Resource > {
	@Override
	protected javax.swing.JLabel getTableCellRendererComponent( javax.swing.JLabel rv, javax.swing.JTable table, org.alice.virtualmachine.Resource value, boolean isSelected, boolean hasFocus, int row, int column ) {
		rv = super.getTableCellRendererComponent( rv, table, value, isSelected, hasFocus, row, column );
		String text;
		java.awt.Color foreground;
		if( value != null ) {
			foreground = java.awt.Color.BLACK;
			text = value.getClass().getSimpleName();
			final String RESOURCE_TEXT = "Resource";
			if( text.endsWith( RESOURCE_TEXT ) ) {
				text = text.substring( 0, text.length()-RESOURCE_TEXT.length() );
			}
//			text += " (";
//			text += value.getContentType();
//			text += ")";
		} else {
			foreground = new java.awt.Color( 255, 0, 0 );
			text = "ERROR";
		}
		rv.setText( text );
		rv.setForeground( foreground );
		return rv;
	}
}

class ResourceNameTableCellRenderer extends ResourceTableCellRenderer< org.alice.virtualmachine.Resource > {
}

/**
 * @author Dennis Cosgrove
 */
public class ResourcesPane extends org.alice.ide.Component {
	private javax.swing.JTable table = new javax.swing.JTable();
	public ResourcesPane() {
		this.setLayout( new java.awt.BorderLayout() );
		edu.cmu.cs.dennisc.alice.Project project = this.getIDE().getProject();
		if( project != null ) {
			org.alice.virtualmachine.Resource[] resources = edu.cmu.cs.dennisc.util.CollectionUtilities.createArray( project.getResources(), org.alice.virtualmachine.Resource.class, true );
			java.util.Set< org.alice.virtualmachine.Resource > referencedResources = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getReferencedResources( project );
			javax.swing.table.TableModel tableModel = new ResourceTableModel( resources, referencedResources );
			table.setModel( tableModel );
			javax.swing.table.TableColumn column0 = this.table.getColumn( this.table.getColumnName( 0 ) );
			javax.swing.table.TableColumn column1 = this.table.getColumn( this.table.getColumnName( 1 ) );
			javax.swing.table.TableColumn column2 = this.table.getColumn( this.table.getColumnName( 2 ) );
			column0.setCellRenderer( new ResourceIsReferencedTableCellRenderer() );
			column1.setCellRenderer( new ResourceNameTableCellRenderer() );
			column2.setCellRenderer( new ResourceTypeTableCellRenderer() );
			column0.setPreferredWidth( 200 );
			column1.setPreferredWidth( 400 );
			column2.setPreferredWidth( 200 );
		}
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.table );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
		//this.add( this.table, java.awt.BorderLayout.CENTER );
	}
}
