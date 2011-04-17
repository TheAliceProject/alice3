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

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractNumberChooser<N extends edu.cmu.cs.dennisc.alice.ast.Expression> extends ValueChooser< N > {
	private org.alice.ide.croquet.models.numberpad.NumberModel<N> numberModel;
	public AbstractNumberChooser( org.alice.ide.croquet.models.numberpad.NumberModel<N> numberModel ) {
		this.numberModel = numberModel;
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		return this.numberModel.getExplanationIfOkButtonShouldBeDisabled();
	}
	@Override
	public N getValue() {
		return this.numberModel.getExpressionValue();
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Component< ? > createMainComponent() {
		org.alice.ide.croquet.models.numberpad.PlusMinusOperation plusMinusOperation = org.alice.ide.croquet.models.numberpad.PlusMinusOperation.getInstance( this.numberModel );
		if( this.numberModel.isDecimalPointSupported() ) {
			org.alice.ide.croquet.models.numberpad.DecimalPointOperation decimalPointOperation = org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( this.numberModel );
			decimalPointOperation.setEnabled( this.numberModel.isDecimalPointSupported() );
		}

		edu.cmu.cs.dennisc.croquet.GridBagPanel gridBagPanel = new edu.cmu.cs.dennisc.croquet.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral7Operation.getInstance( this.numberModel ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral8Operation.getInstance( this.numberModel ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral9Operation.getInstance( this.numberModel ).createButton(), gbc );

		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral4Operation.getInstance( this.numberModel ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral5Operation.getInstance( this.numberModel ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral6Operation.getInstance( this.numberModel ).createButton(), gbc );

		gbc.gridwidth = 1;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral1Operation.getInstance( this.numberModel ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral2Operation.getInstance( this.numberModel ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral3Operation.getInstance( this.numberModel ).createButton(), gbc );

		
		if( this.numberModel.isDecimalPointSupported() ) {
			gbc.gridwidth = 1;
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral0Operation.getInstance( this.numberModel ).createButton(), gbc );
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( this.numberModel ).createButton(), gbc );
		} else {
			gbc.gridwidth = 2;
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.Numeral0Operation.getInstance( this.numberModel ).createButton(), gbc );
		}
		gridBagPanel.addComponent( plusMinusOperation.createButton(), gbc );

		edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JTextField > view = this.numberModel.createTextField();
		view.getAwtComponent().selectAll();

		edu.cmu.cs.dennisc.croquet.LineAxisPanel lineAxisPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel(
				view, 
				org.alice.ide.croquet.models.numberpad.BackspaceOperation.getInstance( this.numberModel ).createButton()
		);

		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		rv.addComponent( lineAxisPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START );
		rv.addComponent( gridBagPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );

		java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( gridBagPanel.getFont(), 3.0f );
		view.setFont( font );
		for( edu.cmu.cs.dennisc.croquet.Button button : edu.cmu.cs.dennisc.croquet.HierarchyUtilities.findAllMatches( rv, edu.cmu.cs.dennisc.croquet.Button.class ) ) {
			button.setFont( font );
		}
		return rv;
	}
}
