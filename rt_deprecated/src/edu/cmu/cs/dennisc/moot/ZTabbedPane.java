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
package edu.cmu.cs.dennisc.moot;

/**
 * @author Dennis Cosgrove
 */
class ZTabbedPaneUI extends edu.cmu.cs.dennisc.swing.plaf.TabbedPaneUI {
	private ZTabbedPane tabbedPane;

	public ZTabbedPaneUI( ZTabbedPane tabbedPane ) {
		this.tabbedPane = tabbedPane;
	}
	@Override
	protected boolean isCloseButtonDesiredAt( int index ) {
		return this.tabbedPane.isCloseButtonDesiredAt( index );
	}
	@Override
	protected void closeTab( int index, java.awt.event.MouseEvent e ) {
		this.tabbedPane.closeTab( index, e );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ZTabbedPane extends javax.swing.JTabbedPane {
	private CancellableOperation tabCloseOperation;

	public ZTabbedPane( CancellableOperation tabCloseOperation ) {
		this.tabCloseOperation = tabCloseOperation;
		this.setTabLayoutPolicy( javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT );
		this.setUI( new ZTabbedPaneUI( this ) );
	}
	public boolean isCloseButtonDesiredAt( int index ) {
		return this.tabCloseOperation != null;
	}
	public void closeTab( int index, java.awt.event.MouseEvent mouseEvent ) {
		edu.cmu.cs.dennisc.moot.event.TabEvent tabEvent = new edu.cmu.cs.dennisc.moot.event.TabEvent( this, index, mouseEvent );
		ZManager.performIfAppropriate( this.tabCloseOperation, tabEvent );
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

		//SingleSelectionOperation tabSelectionOperation = null;
		CancellableOperation tabCloseOperation = new CancellableOperation() {
			private edu.cmu.cs.dennisc.moot.event.TabEvent e;
			public CancellableOperation.PreparationResult prepare( java.util.EventObject e, CancellableOperation.PreparationObserver observer ) {
				this.e = (edu.cmu.cs.dennisc.moot.event.TabEvent)e;
				return CancellableOperation.PreparationResult.PERFORM;
			}
			public void perform() {
				this.e.getTypedSource().remove( this.e.getIndex() );
			}
		};
		javax.swing.JFrame frame = new javax.swing.JFrame();
		ZTabbedPane tabbedPane = new ZTabbedPane( tabCloseOperation ) {
			@Override
			public boolean isCloseButtonDesiredAt(int index) {
				return index % 2 == 1;
			}
		};
		String[] tabTitles = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		for( String tabTitle : tabTitles ) {
			tabbedPane.addTab( tabTitle, new MonthPane( tabTitle ) );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "addTab", tabbedPane.getTabCount() );
		}
		frame.getContentPane().add( tabbedPane );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
