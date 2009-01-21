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
package edu.cmu.cs.dennisc.zoot;



/**
 * @author Dennis Cosgrove
 */
class ZTabbedPaneUI extends edu.cmu.cs.dennisc.swing.plaf.TabbedPaneUI {
	private SingleSelectionOperation tabSelectionOperation;
	private ActionOperation tabCloseOperation;
	public ZTabbedPaneUI( SingleSelectionOperation tabSelectionOperation, ActionOperation tabCloseOperation ) {
		this.tabSelectionOperation = tabSelectionOperation;
		this.tabCloseOperation = tabCloseOperation;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ZTabbedPane extends javax.swing.JTabbedPane {
	public ZTabbedPane( SingleSelectionOperation tabSelectionOperation, ActionOperation tabCloseOperation ) {
		this.setTabLayoutPolicy( javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT );
		this.setUI( new edu.cmu.cs.dennisc.swing.plaf.TabbedPaneUI() );
	}

	public static void main( String[] args ) {
		class MonthPane extends ZLabel {
			MonthPane( String text ) {
				this.setText( text );
				this.setOpaque( true );
				java.awt.Color color;
				if( text.charAt( 0 ) == 'J' ) {
					color = new java.awt.Color( 0xedc484 );
				} else {
					color = new java.awt.Color( 0xb4ccaf );
				}
				this.setBackground( color );
				this.setFontToScaledFont( 10.0f );
			}
		}
		
		SingleSelectionOperation tabSelectionOperation = null;
		ActionOperation tabCloseOperation = null;
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ZTabbedPane tabbedPane = new ZTabbedPane( tabSelectionOperation, tabCloseOperation );
		String[] tabTitles = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		for( String tabTitle : tabTitles ) {
			tabbedPane.addTab( tabTitle, new MonthPane( tabTitle ) );
		}
		frame.getContentPane().add( tabbedPane );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
