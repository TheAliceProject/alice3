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
	public NumberModel( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID id, String text ) {
		super( group, id, text );
	}
	private StringBuffer sb = new StringBuffer();
	public void append( short numeral ) {
		sb.append( numeral );
	}
	public void appendDecimalPoint() {
		sb.append( "." );
	}
	public void negate() {
		if( sb.charAt( 0 ) == '-' ) {
			sb.deleteCharAt( 0 );
		} else {
			sb.insert( 0, '-' );
		}
	}
	public void deleteLastCharacter() {
		if( sb.length() > 0 ) {
			sb.deleteCharAt( sb.length()-1 );
		}

	}
	protected abstract boolean isDecimalPointSupported();
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression valueOf( StringBuffer sb );
	public edu.cmu.cs.dennisc.alice.ast.Expression getValue() {
		try {
			edu.cmu.cs.dennisc.alice.ast.Expression rv = this.valueOf( this.sb );
			return rv;
		} catch( NumberFormatException nfe ) {
			return null;
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
abstract class AbstractNumberChooser implements ValueChooser< edu.cmu.cs.dennisc.alice.ast.Expression > {
	private NumberModel numberModel;
	public AbstractNumberChooser( NumberModel numberModel ) {
		this.numberModel = numberModel;
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		return null;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression getValue() {
		return this.numberModel.getValue();
	}
	@Override
	public java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
		edu.cmu.cs.dennisc.croquet.Group group = this.numberModel.getGroup();
		NumeralOperation[] numeralOperations = new NumeralOperation[ 10 ];
		for( short i=0; i<numeralOperations.length; i++ ) {
			numeralOperations[ i ] = new NumeralOperation( group, i, this.numberModel );
		}
		
		PlusMinusOperation plusMinusOperation = new PlusMinusOperation( group, this.numberModel );
		DecimalPointOperation decimalPointOperation = new DecimalPointOperation( group, this.numberModel );
		decimalPointOperation.setEnabled( this.numberModel.isDecimalPointSupported() );
		
		edu.cmu.cs.dennisc.croquet.GridPanel gridPanel = edu.cmu.cs.dennisc.croquet.GridPanel.createGridPane( 4, 3 );
		gridPanel.addComponent( numeralOperations[ 7 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 8 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 9 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 4 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 5 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 6 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 1 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 2 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 3 ].createButton() );
		gridPanel.addComponent( numeralOperations[ 0 ].createButton() );
		gridPanel.addComponent( decimalPointOperation.createButton() );
		gridPanel.addComponent( plusMinusOperation.createButton() );

		edu.cmu.cs.dennisc.croquet.TextField view = this.numberModel.createTextField();
		view.getAwtComponent().setEditable( false );
		
		BackspaceOperation backspaceOperation = new BackspaceOperation( group, this.numberModel );
		edu.cmu.cs.dennisc.croquet.LineAxisPanel lineAxisPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel(
				view, 
				backspaceOperation.createButton()
		);

		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( lineAxisPanel ) );
		rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( gridPanel ) );
		
		java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( gridPanel.getFont(), 3.0f );
		view.setFont( font );
		for( edu.cmu.cs.dennisc.croquet.Button button : edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( gridPanel, edu.cmu.cs.dennisc.croquet.Button.class ) ) {
			button.setFont( font );
		}
		for( edu.cmu.cs.dennisc.croquet.Button button : edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( lineAxisPanel, edu.cmu.cs.dennisc.croquet.Button.class ) ) {
			button.setFont( font );
		}

		return rv;
	}
}
