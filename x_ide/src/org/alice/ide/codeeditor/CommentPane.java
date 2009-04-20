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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
class CommentLine extends zoot.ZSuggestiveTextField {
	private edu.cmu.cs.dennisc.alice.ast.Comment comment;
	public CommentLine( edu.cmu.cs.dennisc.alice.ast.Comment comment ) {
		super( comment.text.getValue(), "enter your comment here" );
		this.comment = comment;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 0, 2, 0 ) );
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
		this.setBackground( org.alice.ide.IDE.getSingleton().getColorFor( edu.cmu.cs.dennisc.alice.ast.Comment.class ) );
		this.setForeground( org.alice.ide.IDE.getSingleton().getCommentForegroundColor() );
		this.setMargin( new java.awt.Insets( 2, 4, 2, 32 ) );
		this.handleUpdate();
		if( org.alice.ide.IDE.getSingleton().getCommentThatWantsFocus() == this.comment ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					CommentLine.this.requestFocus();
					org.alice.ide.IDE.getSingleton().setCommentThatWantsFocus( null );
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
				CommentLine.this.setToolTipText( "Press the escape key to remove focus" );
				CommentLine.this.repaint();
			}
			public void focusLost( java.awt.event.FocusEvent e ) {
				CommentLine.this.setToolTipText( null );
				CommentLine.this.repaint();
			}
		} );
		
		this.setFont( this.getFont().deriveFont( java.awt.Font.BOLD ) );
	}
	private void handleUpdate() {
		this.revalidate();
		this.setSize( getPreferredSize() );
		this.comment.text.setValue( this.getText() );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		rv = edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( rv, 256 );
		rv = edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMaximumHeight( rv, this.getMinimumSize().height );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CommentPane extends org.alice.ide.common.AbstractStatementPane {
	public CommentPane( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.Comment comment, edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner ) {
		super( factory, comment, owner );
		CommentLine commentLine = new CommentLine( comment );
		zoot.ZLabel label = new zoot.ZLabel( "// " );
		label.setFont( commentLine.getFont() );
		label.setForeground( commentLine.getForeground() );
		this.add( label );
		this.add( commentLine );
	}
}
