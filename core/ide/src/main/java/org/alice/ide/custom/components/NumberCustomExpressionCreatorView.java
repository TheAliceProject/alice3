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

package org.alice.ide.custom.components;

/**
 * @author Dennis Cosgrove
 */
public final class NumberCustomExpressionCreatorView extends CustomExpressionCreatorView {
	public NumberCustomExpressionCreatorView( org.alice.ide.custom.NumberCustomExpressionCreatorComposite composite ) {
		super( composite );
	}

	@Override
	protected org.lgna.croquet.views.SwingComponentView<?> createMainComponent() {
		org.alice.ide.custom.NumberCustomExpressionCreatorComposite composite = (org.alice.ide.custom.NumberCustomExpressionCreatorComposite)this.getComposite();
		org.alice.ide.croquet.models.numberpad.NumberModel numberModel = composite.getNumberModel();
		javax.swing.JTextField view = numberModel.getTextField();

		org.alice.ide.croquet.models.numberpad.PlusMinusOperation plusMinusOperation = org.alice.ide.croquet.models.numberpad.PlusMinusOperation.getInstance( numberModel );
		if( numberModel.isDecimalPointSupported() ) {
			org.alice.ide.croquet.models.numberpad.DecimalPointOperation decimalPointOperation = org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( numberModel );
			decimalPointOperation.setEnabled( numberModel.isDecimalPointSupported() );
		}

		org.lgna.croquet.views.GridBagPanel gridBagPanel = new org.lgna.croquet.views.GridBagPanel();
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weighty = 0.0;
		gbc.weightx = 1.0;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)7 ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)8 ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)9 ).createButton(), gbc );

		gbc.weightx = 0.0;
		gbc.gridwidth = 1;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)4 ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)5 ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)6 ).createButton(), gbc );

		gbc.gridwidth = 1;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)1 ).createButton(), gbc );
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)2 ).createButton(), gbc );
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)3 ).createButton(), gbc );

		if( numberModel.isDecimalPointSupported() ) {
			gbc.gridwidth = 1;
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)0 ).createButton(), gbc );
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.DecimalPointOperation.getInstance( numberModel ).createButton(), gbc );
		} else {
			gbc.gridwidth = 2;
			gridBagPanel.addComponent( org.alice.ide.croquet.models.numberpad.NumeralOperation.getInstance( numberModel, (short)0 ).createButton(), gbc );
		}
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagPanel.addComponent( plusMinusOperation.createButton(), gbc );

		gbc.weighty = 1.0;
		gridBagPanel.addComponent( org.lgna.croquet.views.BoxUtilities.createGlue(), gbc );

		gridBagPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 0, 0, 0 ) );

		org.lgna.croquet.views.LineAxisPanel lineAxisPanel = new org.lgna.croquet.views.LineAxisPanel(
				new org.lgna.croquet.views.SwingAdapter( view ),
				org.alice.ide.croquet.models.numberpad.BackspaceOperation.getInstance( numberModel ).createButton()
				);

		org.lgna.croquet.views.BorderPanel rv = new org.lgna.croquet.views.BorderPanel.Builder()
				.pageStart( lineAxisPanel )
				.center( gridBagPanel )
				.build();

		java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( gridBagPanel.getFont(), 2.0f );
		view.setFont( font );
		for( org.lgna.croquet.views.Button button : org.lgna.croquet.views.HierarchyUtilities.findAllMatches( rv, org.lgna.croquet.views.Button.class ) ) {
			button.setFont( font );
		}

		return rv;
	}
}
