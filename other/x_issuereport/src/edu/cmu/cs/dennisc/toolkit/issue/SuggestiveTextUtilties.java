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
package edu.cmu.cs.dennisc.toolkit.issue;

/**
 * @author Dennis Cosgrove
 */
@Deprecated
class FocusAdapter implements java.awt.event.FocusListener {
	private javax.swing.text.JTextComponent textComponent;

	public FocusAdapter( javax.swing.text.JTextComponent textComponent ) {
		this.textComponent = textComponent;
	}
	public void focusGained( java.awt.event.FocusEvent arg0 ) {
		if( this.textComponent.getText().length() == 0 ) {
			this.textComponent.repaint();
		}
		//this.textComponent.setBackground( new java.awt.Color( 230, 230, 255 ) );
	}
	public void focusLost( java.awt.event.FocusEvent arg0 ) {
		if( this.textComponent.getText().length() == 0 ) {
			this.textComponent.repaint();
		}
		//this.textComponent.setBackground( java.awt.Color.WHITE );
	}
}

/**
 * @author Dennis Cosgrove
 */
@Deprecated
class SuggestiveTextUtilties {
	public static void drawBlankTextIfNecessary( javax.swing.text.JTextComponent textComponent, java.awt.Graphics g, String textForBlankCondition ) {
		String text = textComponent.getText();
		if( text.length() > 0 ) {
			//pass
		} else {
			java.awt.Font font = textComponent.getFont();
			font = font.deriveFont( java.awt.Font.ITALIC );
			//int grayscale = 160;
			int grayscale;
			if( textComponent.isFocusOwner() ) {
				grayscale = 120;
			} else {
				grayscale = 160;
				font = font.deriveFont( font.getSize2D() * 0.9f );
			}
			g.setFont( font );
			g.setColor( new java.awt.Color( grayscale, grayscale, grayscale ) );
			java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( textForBlankCondition, g );
			int x = (int)(textComponent.getWidth() - bounds.getWidth() - textComponent.getInsets().right - 4 );
			int y = (int)bounds.getHeight();
			g.drawString( textForBlankCondition, x, y );
		}
	}
}

class TextComponentBorder extends javax.swing.border.CompoundBorder {
	public TextComponentBorder() {
		super( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ), javax.swing.BorderFactory.createEmptyBorder( 1, 3, 1, 3 ) );
	}
}

/**
 * @author Dennis Cosgrove
 */
@Deprecated
class SuggestiveTextField extends javax.swing.JTextField {
	private String textForBlankCondition;

	public SuggestiveTextField( String text, String textForBlankCondition ) {
		super( text );
		this.setBorder( new TextComponentBorder() );
		this.textForBlankCondition = textForBlankCondition;
		this.addFocusListener( new FocusAdapter( this ) );
		//setToolTipText( this.textForBlankCondition );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		java.awt.Dimension rv = super.getMaximumSize();
		java.awt.Dimension preferred = getPreferredSize();
		rv.height = preferred.height;
		return rv;
	}
	public String getSuggestiveText() {
		return this.textForBlankCondition;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
	}
}

/**
 * @author Dennis Cosgrove
 */
@Deprecated
class SuggestiveTextArea extends javax.swing.JTextArea {
	private String textForBlankCondition;

	public SuggestiveTextArea( String text, String textForBlankCondition ) {
		super( text );
		this.textForBlankCondition = textForBlankCondition;
		//this.setToolTipText( this.textForBlankCondition );
		this.setBorder( new TextComponentBorder() );
		this.addFocusListener( new FocusAdapter( this ) );
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
	public String getSuggestiveText() {
		return this.textForBlankCondition;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
	}
}
