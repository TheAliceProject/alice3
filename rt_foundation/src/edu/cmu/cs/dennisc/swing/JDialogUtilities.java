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
public class JDialogUtilities {
	public static javax.swing.JDialog createPackedJDialog( java.awt.Component content, java.awt.Component ownerComponent, String title, boolean isModal, int closeOperation ) {
		javax.swing.JDialog rv;
		java.awt.Component root;
		if( ownerComponent != null ) {
			root = javax.swing.SwingUtilities.getRoot( ownerComponent );
		} else {
			root = null;
		}
		if( root instanceof java.awt.Frame ) {
			rv = new javax.swing.JDialog( (java.awt.Frame)root );
		} else if( root instanceof java.awt.Dialog ) {
			rv = new javax.swing.JDialog( (java.awt.Dialog)root );
		} else {
			rv = new javax.swing.JDialog();
		}
		rv.setTitle( title );
		rv.setModal( isModal );

		rv.getContentPane().add( content );
		rv.pack();
		rv.setDefaultCloseOperation( closeOperation );
		return rv;
	}
}
