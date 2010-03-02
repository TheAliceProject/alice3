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
public abstract class ContentsCachingListCellRenderer< E > extends edu.cmu.cs.dennisc.croquet.swing.LineAxisPane implements javax.swing.ListCellRenderer {
	private static java.awt.Color selectionBackground = javax.swing.UIManager.getColor("List.selectionBackground");
	private java.util.Map< E, java.awt.Component > map = new java.util.HashMap< E, java.awt.Component >();
	
	protected abstract void update( java.awt.Component contents, int index, boolean isSelected, boolean cellHasFocus );
	protected abstract java.awt.Component createComponent( E e );

	public ContentsCachingListCellRenderer() {
		this.setBackground( selectionBackground );
		this.setAlignmentX( 0.0f );
	}
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
		java.awt.Component component = this.getComponent( (E)value );
		this.update( component, index, isSelected, cellHasFocus );
		return this;
	}
}
