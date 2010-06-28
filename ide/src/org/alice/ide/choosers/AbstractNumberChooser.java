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
package org.alice.ide.choosers;

abstract class NumberModel extends edu.cmu.cs.dennisc.croquet.StringState {
	@Deprecated
	protected final static edu.cmu.cs.dennisc.croquet.Group CALCULATOR_GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "afe9fee0-e91f-4344-9b80-6fa84f3458d3" ), "CALCULATOR_GROUP" );
	private static String getInitialText() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = ide.getPreviousExpression();
			if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handle previous expression; replace selected text later." );
//				edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral valueLiteral = (edu.cmu.cs.dennisc.alice.ast.AbstractValueLiteral)previousExpression;
//				return valueLiteral.getValueProperty().getValue().toString();
			}
		}
		return "";
	}
	public NumberModel( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID id ) {
		super( group, id, getInitialText() );
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
	protected abstract boolean isDecimalPointSupported();
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression valueOf( String s );
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
	public edu.cmu.cs.dennisc.alice.ast.Expression getValue() {
		try {
			javax.swing.text.Document document = this.getDocument();
			try {
				edu.cmu.cs.dennisc.alice.ast.Expression rv = this.valueOf( document.getText( 0, document.getLength() ) );
				return rv;
			} catch( NumberFormatException nfe ) {
				return null;
			}
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
}

abstract class CalculatorOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	protected NumberModel model;
	public CalculatorOperation( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID individualId, NumberModel model ) {
		super( group, individualId );
		this.model = model;
	}
}

class NumeralOperation extends CalculatorOperation {
	private short numeral;
	public NumeralOperation( edu.cmu.cs.dennisc.croquet.Group group, short numeral, NumberModel model ) {
		super( group, java.util.UUID.fromString( "33f4d69b-ab96-4dfe-ba28-dc454227d5bf" ), model );
		this.numeral = numeral;
		this.setName( Short.toString( this.numeral ) );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.model.append( this.numeral );
		context.finish();
	}
}

class PlusMinusOperation extends CalculatorOperation {
	public PlusMinusOperation( edu.cmu.cs.dennisc.croquet.Group group, NumberModel model ) {
		super( group, java.util.UUID.fromString( "6845e168-dfce-4f9e-b94f-d5674613f38c" ), model );
		this.setName( "\u00B1" );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.model.negate();
		context.finish();
	}
}

class DecimalPointOperation extends CalculatorOperation {
	public DecimalPointOperation( edu.cmu.cs.dennisc.croquet.Group group, NumberModel model ) {
		super( group, java.util.UUID.fromString( "45fb7f55-166b-421c-9e6d-cf781b562936" ), model );
		this.setName( "." );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.model.appendDecimalPoint();
		context.finish();
	}
}

class BackspaceOperation extends CalculatorOperation {
	public BackspaceOperation( edu.cmu.cs.dennisc.croquet.Group group, NumberModel model ) {
		super( group, java.util.UUID.fromString( "16a86a58-7672-4eb7-8138-9853978f2d00" ), model );
		this.setName( "\u2190" );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.model.deleteLastCharacter();
		context.finish();
	}
}

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractNumberChooser extends ValueChooser< edu.cmu.cs.dennisc.alice.ast.Expression > {
	private NumberModel numberModel;
	public AbstractNumberChooser( NumberModel numberModel ) {
		this.numberModel = numberModel;
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		return this.numberModel.getExplanationIfOkButtonShouldBeDisabled();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getValue() {
		return this.numberModel.getValue();
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Component< ? > createMainComponent() {
		edu.cmu.cs.dennisc.croquet.Group group = this.numberModel.getGroup();
		NumeralOperation[] numeralOperations = new NumeralOperation[ 10 ];
		for( short i=0; i<numeralOperations.length; i++ ) {
			numeralOperations[ i ] = new NumeralOperation( group, i, this.numberModel );
		}
		
		PlusMinusOperation plusMinusOperation = new PlusMinusOperation( group, this.numberModel );
		if( this.numberModel.isDecimalPointSupported() ) {
			DecimalPointOperation decimalPointOperation = new DecimalPointOperation( group, this.numberModel );
			decimalPointOperation.setEnabled( this.numberModel.isDecimalPointSupported() );
		}

		edu.cmu.cs.dennisc.croquet.GridBagPanel gridBagPanel = new edu.cmu.cs.dennisc.croquet.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gridBagPanel.addComponent( numeralOperations[ 7 ].createButton(), gbc );
		gridBagPanel.addComponent( numeralOperations[ 8 ].createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( numeralOperations[ 9 ].createButton(), gbc );

		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		gridBagPanel.addComponent( numeralOperations[ 4 ].createButton(), gbc );
		gridBagPanel.addComponent( numeralOperations[ 5 ].createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( numeralOperations[ 6 ].createButton(), gbc );

		gbc.gridwidth = 1;
		gridBagPanel.addComponent( numeralOperations[ 1 ].createButton(), gbc );
		gridBagPanel.addComponent( numeralOperations[ 2 ].createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( numeralOperations[ 3 ].createButton(), gbc );

		
		if( this.numberModel.isDecimalPointSupported() ) {
			gbc.gridwidth = 1;
			gridBagPanel.addComponent( numeralOperations[ 0 ].createButton(), gbc );
			DecimalPointOperation decimalPointOperation = new DecimalPointOperation( group, this.numberModel );
			gridBagPanel.addComponent( decimalPointOperation.createButton(), gbc );
		} else {
			gbc.gridwidth = 2;
			gridBagPanel.addComponent( numeralOperations[ 0 ].createButton(), gbc );
		}
		gridBagPanel.addComponent( plusMinusOperation.createButton(), gbc );

		edu.cmu.cs.dennisc.croquet.TextField view = this.numberModel.createTextField();
		view.selectAll();

		BackspaceOperation backspaceOperation = new BackspaceOperation( group, this.numberModel );
		edu.cmu.cs.dennisc.croquet.LineAxisPanel lineAxisPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel(
				view, 
				backspaceOperation.createButton()
		);

		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		rv.addComponent( lineAxisPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH );
		rv.addComponent( gridBagPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

		java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( gridBagPanel.getFont(), 3.0f );
		view.setFont( font );
		for( edu.cmu.cs.dennisc.croquet.Button button : edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( rv, edu.cmu.cs.dennisc.croquet.Button.class ) ) {
			button.setFont( font );
		}
		return rv;
	}
}
