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

/**
 * @author Dennis Cosgrove
 */
public class ZSuggestiveTextArea extends javax.swing.JTextArea {
	private String textForBlankCondition;

	public ZSuggestiveTextArea( String text, String textForBlankCondition ) {
		super( text );
		this.setBorder( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );
		this.textForBlankCondition = textForBlankCondition;
		//this.setToolTipText( this.textForBlankCondition );
		this.addFocusListener( new SuggestiveTextFocusAdapter( this ) );
		this.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_TAB ) {
					e.consume();
					if( e.isShiftDown() ) {
						java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
					} else {
						java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
					}
				}
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		} );
	}
	@Override
	public boolean isManagingFocus() {
		return false;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight( super.getPreferredSize(), 64 );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		int xDelta = this.getInsets().left;
		g.translate( xDelta, 0 );
		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
		g.translate( -xDelta, 0 );
	}
}
