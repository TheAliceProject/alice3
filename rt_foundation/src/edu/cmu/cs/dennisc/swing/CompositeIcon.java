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
public class CompositeIcon implements javax.swing.Icon {
	private javax.swing.Icon[] components;
	public CompositeIcon( javax.swing.Icon... components ) {
		this.components = components;
	}
	public int getIconWidth() {
		int rv = 0;
		for( javax.swing.Icon icon : this.components ) {
			rv = Math.max( rv, icon.getIconWidth() );
		}
		return rv;
	}
	public int getIconHeight() {
		int rv = 0;
		for( javax.swing.Icon icon : this.components ) {
			rv = Math.max( rv, icon.getIconHeight() );
		}
		return rv;
	}
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		for( javax.swing.Icon icon : this.components ) {
			icon.paintIcon( c, g, x, y );
		}
	}
}
