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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class KFauxList< E > extends javax.swing.JList {
	protected abstract java.awt.Component getComponentForNull();
	protected abstract java.awt.Component createComponent( E e );
	protected abstract KFauxListItem< E >  createFauxListItem();
	class ListCellRenderer implements javax.swing.ListCellRenderer {
		private KFauxListItem< E > fauxListItem = createFauxListItem();
		private java.util.Map< E, java.awt.Component > map = new java.util.HashMap< E, java.awt.Component >();
		private java.awt.Component getComponent( E e ) {
			java.awt.Component rv = this.map.get( e );
			if( rv != null ) {
				//pass
			} else {
				rv = createComponent( e );
				this.map.put( e, rv );
			}
			return rv;
		}
		public java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
			java.awt.Component component;
			if( value != null ) {
				component = this.getComponent( (E)value );
			} else {
				component = getComponentForNull();
			}
			this.fauxListItem.update( component, index, isSelected, cellHasFocus );
			return this.fauxListItem.getComponent();
		}
	}
	public KFauxList() {
		this.setCellRenderer( new ListCellRenderer() );
	}
}
