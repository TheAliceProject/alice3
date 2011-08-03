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
abstract class AbstractNumberChooser<N extends org.lgna.project.ast.Expression> extends ValueChooser< N > {
	private org.alice.ide.croquet.models.numberpad.NumberModel<N> numberModel;
	private org.lgna.croquet.components.JComponent< javax.swing.JTextField > view;
	public AbstractNumberChooser( org.alice.ide.croquet.models.numberpad.NumberModel<N> numberModel ) {
		this.numberModel = numberModel;
		this.view = this.numberModel.createTextField();
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
	public void handlePrologue( org.lgna.croquet.history.InputDialogOperationStep step ) {
		this.view.getAwtComponent().setText( "" );
		//this.view.getAwtComponent().selectAll();
	}
	
	@Override
	public org.lgna.croquet.components.Component< ? > createMainComponent() {
		org.alice.ide.croquet.models.numberpad.PlusMinusOperation plusMinusOperation = org.alice.ide.croquet.models.numberpad.PlusMinusOperation.getInstance( this.numberModel );
		if( this.numberModel.isDecimalPointSupported() ) {
			org.alice.ide.croquet.models.numberpad.DecimalPointOperation decimalPointOperation = org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( this.numberModel );
			decimalPointOperation.setEnabled( this.numberModel.isDecimalPointSupported() );
		}

		org.lgna.croquet.components.GridBagPanel gridBagPanel = new org.lgna.croquet.components.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)7 ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)8 ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)9 ).createButton(), gbc );

		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)4 ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)5 ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)6 ).createButton(), gbc );

		gbc.gridwidth = 1;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)1 ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)2 ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)3 ).createButton(), gbc );

		
		if( this.numberModel.isDecimalPointSupported() ) {
			gbc.gridwidth = 1;
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)0 ).createButton(), gbc );
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( this.numberModel ).createButton(), gbc );
		} else {
			gbc.gridwidth = 2;
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( this.numberModel, (short)0 ).createButton(), gbc );
		}
		gridBagPanel.addComponent( plusMinusOperation.createButton(), gbc );

		org.lgna.croquet.components.LineAxisPanel lineAxisPanel = new org.lgna.croquet.components.LineAxisPanel(
				view, 
				org.alice.ide.croquet.models.numberpad.BackspaceOperation.getInstance( this.numberModel ).createButton()
		);

		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel();
		rv.addComponent( lineAxisPanel, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
		rv.addComponent( gridBagPanel, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );

		java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( gridBagPanel.getFont(), 3.0f );
		view.setFont( font );
		for( org.lgna.croquet.components.Button button : org.lgna.croquet.components.HierarchyUtilities.findAllMatches( rv, org.lgna.croquet.components.Button.class ) ) {
			button.setFont( font );
		}
		return rv;
	}
}
