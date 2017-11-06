/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.alice.ide.codeeditor;

import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;

/**
 * @author Dennis Cosgrove
 */
class CommentLine extends edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea {
	//class CommentLine extends javax.swing.JTextArea {
	private org.lgna.project.ast.Comment comment;

	public CommentLine( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.Comment comment ) {
		this.setText( comment.text.getValue() );
		String localizedSuggestion = ResourceBundleUtilities
						.getStringForKey( "commentHint", "org.alice.ide.codeeditor.CodeEditor");
		this.setTextForBlankCondition( localizedSuggestion );
		this.comment = comment;
		this.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
			@Override
			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}

			@Override
			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}

			@Override
			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}
		} );
		this.setBackground( org.alice.ide.ThemeUtilities.getActiveTheme().getColorFor( org.lgna.project.ast.Comment.class ) );
		this.setForeground( org.alice.ide.ThemeUtilities.getActiveTheme().getCommentForegroundColor() );
		//this.setMargin( new java.awt.Insets( 2, 4, 2, 32 ) );
		this.handleUpdate();
		if( factory.isCommentMutable( comment ) ) {
			if( org.alice.ide.IDE.getActiveInstance().getCommentThatWantsFocus() == this.comment ) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						CommentLine.this.requestFocus();
						org.alice.ide.IDE.getActiveInstance().setCommentThatWantsFocus( null );
					}
				} );
			}
			//todo: remove?
			this.addKeyListener( new java.awt.event.KeyListener() {
				@Override
				public void keyPressed( java.awt.event.KeyEvent e ) {
					if( e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE ) {
						CommentLine.this.transferFocus();
					}
				}

				@Override
				public void keyReleased( java.awt.event.KeyEvent e ) {
				}

				@Override
				public void keyTyped( java.awt.event.KeyEvent e ) {
				}
			} );

			this.addFocusListener( new java.awt.event.FocusListener() {
				@Override
				public void focusGained( java.awt.event.FocusEvent e ) {
					CommentLine.this.setToolTipText( "Press the escape key to remove focus" );
					//CommentLine.this.repaint();
				}

				@Override
				public void focusLost( java.awt.event.FocusEvent e ) {
					CommentLine.this.setToolTipText( null );
					//CommentLine.this.repaint();
				}
			} );
		} else {
			this.setEditable( false );
		}

		//		java.awt.Font font = this.getFont();
		//		font = font.deriveFont( java.awt.Font.BOLD );
		//		font = font.deriveFont( font.getSize() * 1.2f );
		//		this.setFont( font );
		this.updateBorder();
	}

	@Override
	public boolean contains( int x, int y ) {
		return this.isEditable() && super.contains( x, y );
	}

	private void updateBorder() {
		java.awt.Graphics g = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.getGraphics();
		java.awt.FontMetrics fm = g.getFontMetrics( this.getFont() );
		java.awt.geom.Rectangle2D bounds = fm.getStringBounds( "//", g );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, (int)bounds.getWidth() + 2, 0, 0 ) );
	}

	@Override
	public void setFont( java.awt.Font f ) {
		super.setFont( f );
		this.updateBorder();
	}

	private void handleUpdate() {
		this.revalidate();
		this.setSize( getPreferredSize() );
		this.comment.text.setValue( this.getText() );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( rv, 256 );
		rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMaximumHeight( rv, this.getMinimumSize().height );
		return rv;
	}

	@Override
	public java.awt.Dimension getMaximumSize() {
		java.awt.Dimension rv = super.getMaximumSize();
		java.awt.Dimension preferredSize = this.getPreferredSize();
		rv.height = preferredSize.height;
		return rv;
	}

	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		g.setColor( this.getForeground() );
		final int ROW_HEIGHT = this.getRowHeight();
		final int N = this.getLineCount();
		int y = -g.getFontMetrics().getDescent();
		for( int i = 0; i < N; i++ ) {
			y += ROW_HEIGHT;
			g.drawString( "//", 0, y );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class CommentPane extends org.alice.ide.common.AbstractStatementPane {
	public CommentPane( org.lgna.croquet.DragModel model, org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.Comment comment, org.lgna.project.ast.StatementListProperty owner ) {
		super( model, factory, comment, owner );
		CommentLine commentLine = new CommentLine( factory, comment );
		this.addComponent( new org.lgna.croquet.views.SwingAdapter( commentLine ) );
	}
}
