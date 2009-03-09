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
package zoot;

import alice.ide.IDE;

/**
 * @author Dennis Cosgrove
 */
public abstract class ZFrame extends javax.swing.JFrame {
	public ZFrame() {
		this.setDefaultCloseOperation( javax.swing.JFrame.DO_NOTHING_ON_CLOSE );
		this.addWindowListener( new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
				ZFrame.this.handleWindowOpened( e );
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				ZFrame.this.handleQuit( e );
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
		} );
	}
	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );
	//protected abstract void handleWindowClosing();
	protected abstract void handleQuit( java.util.EventObject e );
}
