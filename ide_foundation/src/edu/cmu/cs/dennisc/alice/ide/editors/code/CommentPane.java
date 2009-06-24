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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

/**
 * @author Dennis Cosgrove
 */
class CommentLine extends javax.swing.JTextArea {
	private edu.cmu.cs.dennisc.alice.ast.Comment comment;

	public CommentLine( edu.cmu.cs.dennisc.alice.ast.Comment comment ) {
		this.comment = comment;
		this.setText( this.comment.text.getValue() );
		this.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}
		} );
		//this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.createGray( 240 ) );
		this.setBackground( edu.cmu.cs.dennisc.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.Comment.class ) );
		//this.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.BLACK ) );
		this.setMargin( new java.awt.Insets( 2, 4, 2, 32 ) );
		this.handleUpdate();
		if( edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getCommentThatWantsFocus() == this.comment ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					CommentLine.this.requestFocus();
					edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().setCommentThatWantsFocus( null );
				}
			} ); 
		}
		
		//todo: remove?
		this.addKeyListener( new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE ) {
					CommentLine.this.transferFocus();
				}
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		} );
		
		
		this.addFocusListener( new java.awt.event.FocusListener() {
			public void focusGained( java.awt.event.FocusEvent e ) {
				CommentLine.this.repaint();
			}
			public void focusLost( java.awt.event.FocusEvent e ) {
				CommentLine.this.repaint();
			}
		} );
	}
	@Override
	public java.awt.Font getFont() {
		java.awt.Font rv = super.getFont();
		if( rv != null ) {
			rv = rv.deriveFont( rv.getSize2D() * 1.4f );
			//rv = rv.deriveFont( java.awt.Font.BOLD );
			rv = rv.deriveFont( java.awt.Font.ITALIC );
		}
		return rv;
	}
	private void handleUpdate() {
		this.revalidate();
		this.setSize( getPreferredSize() );
		this.comment.text.setValue( this.getText() );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		if( this.getText().length() == 0 ) {
			String text = "enter your comment here";
			g.setFont( this.getFont().deriveFont( java.awt.Font.ITALIC ) );
			g.setColor( java.awt.Color.GRAY );
			java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( text, g );
			g.drawString( "enter your comment here", 4, (int)bounds.getHeight() );
		}
		//g.draw3DRect( 0, 0, getWidth() - 1, getHeight() - 1, false );
//		if( this.isFocusOwner() ) {
//			g.setColor( java.awt.Color.DARK_GRAY );
//			g.draw3DRect( 0, 0, getWidth() - 1, getHeight() - 1, false );
//			g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
//		}
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		rv.width = Math.max( rv.width, 256 );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CommentPane extends AbstractStatementPane {
	public CommentPane( edu.cmu.cs.dennisc.alice.ast.Comment comment, edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner ) {
		super( comment, owner );
		edu.cmu.cs.dennisc.moot.ZLabel label = new edu.cmu.cs.dennisc.moot.ZLabel( "comment:" );
		label.setFontToDerivedFont( java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_LIGHT );
		this.add( label );
		this.add( new CommentLine( comment ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 16, 4, 16 ) );
	}
}
