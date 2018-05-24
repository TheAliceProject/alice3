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

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.javax.swing.components.JSuggestiveTextArea;
import org.alice.ide.IDE;
import org.alice.ide.ThemeUtilities;
import org.alice.ide.common.AbstractStatementPane;
import org.alice.ide.x.AstI18nFactory;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.views.SwingAdapter;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.StatementListProperty;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

/**
 * @author Dennis Cosgrove
 */
class CommentLine extends JSuggestiveTextArea {
	//class CommentLine extends javax.swing.JTextArea {
	private Comment comment;

	public CommentLine( AstI18nFactory factory, Comment comment ) {
		this.setText( comment.text.getValue() );
		String localizedSuggestion = ResourceBundleUtilities
						.getStringForKey( "commentHint", "org.alice.ide.codeeditor.CodeEditor");
		this.setTextForBlankCondition( localizedSuggestion );
		this.comment = comment;
		this.getDocument().addDocumentListener( new DocumentListener() {
			@Override
			public void changedUpdate( DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}

			@Override
			public void insertUpdate( DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}

			@Override
			public void removeUpdate( DocumentEvent e ) {
				CommentLine.this.handleUpdate();
			}
		} );
		this.setBackground( ThemeUtilities.getActiveTheme().getColorFor( Comment.class ) );
		this.setForeground( ThemeUtilities.getActiveTheme().getCommentForegroundColor() );
		//this.setMargin( new java.awt.Insets( 2, 4, 2, 32 ) );
		this.handleUpdate();
		if( factory.isCommentMutable( comment ) ) {
			if( IDE.getActiveInstance().getCommentThatWantsFocus() == this.comment ) {
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						CommentLine.this.requestFocus();
						IDE.getActiveInstance().setCommentThatWantsFocus( null );
					}
				} );
			}
			//todo: remove?
			this.addKeyListener( new KeyListener() {
				@Override
				public void keyPressed( KeyEvent e ) {
					if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
						CommentLine.this.transferFocus();
					}
				}

				@Override
				public void keyReleased( KeyEvent e ) {
				}

				@Override
				public void keyTyped( KeyEvent e ) {
				}
			} );

			this.addFocusListener( new FocusListener() {
				@Override
				public void focusGained( FocusEvent e ) {
					CommentLine.this.setToolTipText( "Press the escape key to remove focus" );
					//CommentLine.this.repaint();
				}

				@Override
				public void focusLost( FocusEvent e ) {
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
		Graphics g = GraphicsUtilities.getGraphics();
		FontMetrics fm = g.getFontMetrics( this.getFont() );
		Rectangle2D bounds = fm.getStringBounds( "//", g );
		this.setBorder( BorderFactory.createEmptyBorder( 0, (int)bounds.getWidth() + 2, 0, 0 ) );
	}

	@Override
	public void setFont( Font f ) {
		super.setFont( f );
		this.updateBorder();
	}

	private void handleUpdate() {
		this.revalidate();
		this.setSize( getPreferredSize() );
		this.comment.text.setValue( this.getText() );
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension rv = super.getPreferredSize();
		rv = DimensionUtilities.constrainToMinimumWidth( rv, 256 );
		rv = DimensionUtilities.constrainToMaximumHeight( rv, this.getMinimumSize().height );
		return rv;
	}

	@Override
	public Dimension getMaximumSize() {
		Dimension rv = super.getMaximumSize();
		Dimension preferredSize = this.getPreferredSize();
		rv.height = preferredSize.height;
		return rv;
	}

	@Override
	public void paint( Graphics g ) {
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
public class CommentPane extends AbstractStatementPane {
	public CommentPane( DragModel model, AstI18nFactory factory, Comment comment, StatementListProperty owner ) {
		super( model, factory, comment, owner );
		CommentLine commentLine = new CommentLine( factory, comment );
		this.addComponent( new SwingAdapter( commentLine ) );
	}
}
