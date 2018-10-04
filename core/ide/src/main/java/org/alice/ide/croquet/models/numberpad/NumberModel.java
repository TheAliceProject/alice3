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
package org.alice.ide.croquet.models.numberpad;

import org.lgna.croquet.Group;
import org.lgna.croquet.history.PrepStep;
import org.lgna.croquet.history.EmptyPrepStep;
import org.lgna.croquet.triggers.DocumentEventTrigger;
import org.lgna.project.ast.Expression;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.PlainDocument;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class NumberModel<N extends Expression> /* extends edu.cmu.cs.dennisc.croquet.StringState */{
	@Deprecated/* package-private */final static Group NUMBER_PAD_GROUP = Group.getInstance( UUID.fromString( "afe9fee0-e91f-4344-9b80-6fa84f3458d3" ), "NUMBER_PAD_GROUP" );

	private final PlainDocument document = new PlainDocument();
	private final JTextField textField = new JTextField();

	private static final class NumberDocumentListener implements DocumentListener {
		private int ignoreCount;

		void pushIgnore() {
			this.ignoreCount++;
		}

		void popIgnore() {
			this.ignoreCount--;
		}

		private void update( DocumentEvent e ) {
			if( this.ignoreCount == 0 ) {
				PrepStep step = new EmptyPrepStep( DocumentEventTrigger.createUserInstance( e ), "Key pressed");
				step.getUserActivity().finish();
			}
		}

		@Override
		public void changedUpdate( DocumentEvent e ) {
			this.update( e );
		}

		@Override
		public void insertUpdate( DocumentEvent e ) {
			this.update( e );
		}

		@Override
		public void removeUpdate( DocumentEvent e ) {
			this.update( e );
		}
	}

	private final NumberDocumentListener documentListener = new NumberDocumentListener();

	public NumberModel( Group group, UUID id ) {
		this.textField.setDocument( this.document );
		this.document.addDocumentListener( this.documentListener );
	}

	public JTextField getTextField() {
		return this.textField;
	}

	private void replaceSelection( String s ) {
		this.documentListener.pushIgnore();
		try {
			this.textField.replaceSelection( s );
		} finally {
			this.documentListener.popIgnore();
		}
	}

	public void replaceSelection( short numeral ) {
		this.replaceSelection( Short.toString( numeral ) );
	}

	public void replaceSelectionWithDecimalPoint() {
		this.replaceSelection( DecimalPointOperation.getInstance( this ).getImp().getName() );
	}

	public void negate() {
		this.documentListener.pushIgnore();
		try {
			final int N = document.getLength();
			try {
				boolean isNegative;
				if( N > 0 ) {
					String s0 = document.getText( 0, 1 );
					isNegative = s0.charAt( 0 ) == '-';
				} else {
					isNegative = false;
				}
				if( isNegative ) {
					document.remove( 0, 1 );
				} else {
					document.insertString( 0, "-", null );
				}
			} catch( BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		} finally {
			this.documentListener.popIgnore();
		}
	}

	public void delete() {
		this.documentListener.pushIgnore();
		try {
			Caret caret = this.textField.getCaret();
			int dot = caret.getDot();
			int mark = caret.getMark();
			if( dot != mark ) {
				this.replaceSelection( "" );
			} else {
				if( dot > 0 ) {
					try {
						document.remove( dot - 1, 1 );
					} catch( BadLocationException ble ) {
						throw new RuntimeException( ble );
					}
				}
			}
		} finally {
			this.documentListener.popIgnore();
		}
	}

	public void setText( String text ) {
		this.documentListener.pushIgnore();
		try {
			try {
				this.document.replace( 0, this.document.getLength(), text, null );
			} catch( BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		} finally {
			this.documentListener.popIgnore();
		}
	}

	public void selectAll() {
		this.textField.selectAll();
	}

	public abstract boolean isDecimalPointSupported();

	protected abstract N valueOf( String s );

	//	protected abstract String getEmptyTextExplanation();
	public String getExplanationIfOkButtonShouldBeDisabled() {
		try {
			final int N = document.getLength();
			if( N > 0 ) {
				String text = document.getText( 0, N );
				try {
					Expression rv = this.valueOf( text );
					if( rv != null ) {
						return null;
					} else {
						return "isNotValid";
					}
				} catch( NumberFormatException nfe ) {
					return "isNotValid";
				}
			} else {
				return "enterNumber";
			}
		} catch( BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	public N getExpressionValue() {
		try {
			final int N = document.getLength();
			String text = document.getText( 0, N );
			try {
				N rv = this.valueOf( text );
				return rv;
			} catch( NumberFormatException nfe ) {
				return null;
			}
		} catch( BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
}
