/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.croquet.models.numberpad;

/**
 * @author Dennis Cosgrove
 */
public abstract class NumberModel< N extends edu.cmu.cs.dennisc.alice.ast.Expression > /* extends edu.cmu.cs.dennisc.croquet.StringState */ {
	@Deprecated
	/*package-private*/ final static edu.cmu.cs.dennisc.croquet.Group NUMBER_PAD_GROUP = edu.cmu.cs.dennisc.croquet.Group.getInstance( java.util.UUID.fromString( "afe9fee0-e91f-4344-9b80-6fa84f3458d3" ), "NUMBER_PAD_GROUP" );
	
	private static String getInitialText() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = ide.getCascadeManager().createCopyOfPreviousExpression();
			if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle previous expression; replace selected text later." );
//				edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral valueLiteral = (edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral)previousExpression;
//				return valueLiteral.getValueProperty().getValue().toString();
			}
		}
		return "";
	}
	private javax.swing.text.Document document;
	public NumberModel( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID id ) {
		//super( group, id, getInitialText() );
		this.document = new javax.swing.text.PlainDocument();
		try {
			this.document.insertString(0, getInitialText(), null);
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	private javax.swing.text.Document getDocument() {
		return this.document;
	}
	public edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JTextField > createTextField() {
		return new edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JTextField >() {
			@Override
			protected javax.swing.JTextField createAwtComponent() {
				javax.swing.JTextField jTextField = new javax.swing.JTextField();
				jTextField.setDocument( NumberModel.this.document );
				return jTextField;
			}
		};
	}
	private void append( String s ) {
		javax.swing.text.Document document = this.getDocument();
		try {
			document.insertString( document.getLength(), s, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	public void append( short numeral ) {
		this.append( Short.toString( numeral ) );
	}
	public void appendDecimalPoint() {
		this.append( "." );
	}
	public void negate() {
		javax.swing.text.Document document = this.getDocument();
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
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	public void deleteLastCharacter() {
		javax.swing.text.Document document = this.getDocument();
		final int N = document.getLength();
		if( document.getLength() > 0 ) {
			try {
				document.remove( N-1, 1 );
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		}

	}
	public abstract boolean isDecimalPointSupported();
	protected abstract N valueOf( String s );
//	protected abstract String getEmptyTextExplanation();
	public String getExplanationIfOkButtonShouldBeDisabled() {
		try {
			javax.swing.text.Document document = this.getDocument();
			final int N = document.getLength();
			if( N > 0 ) {
				try {
					edu.cmu.cs.dennisc.alice.ast.Expression rv = this.valueOf( document.getText( 0, N ) );
					if( rv != null ) {
						return null;
					} else {
						return document.getText( 0, document.getLength() ) + " is not valid.";
					}
				} catch( NumberFormatException nfe ) {
					return document.getText( 0, document.getLength() ) + " is not valid.";
				}
			} else {
				return "Enter a number.";
			}
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	public N getExpressionValue() {
		try {
			javax.swing.text.Document document = this.getDocument();
			try {
				N rv = this.valueOf( document.getText( 0, document.getLength() ) );
				return rv;
			} catch( NumberFormatException nfe ) {
				return null;
			}
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
}
