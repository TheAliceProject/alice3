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

package org.alice.ide.x.components;

/**
 * @author Dennis Cosgrove
 */
public class InfixExpressionView extends AbstractExpressionView<org.lgna.project.ast.InfixExpression> {
	public InfixExpressionView( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.InfixExpression<? extends Enum<?>> infixExpression ) {
		super( factory, infixExpression );
		org.alice.ide.formatter.Formatter formatter = org.alice.ide.croquet.models.ui.formatter.FormatterState.getInstance().getValue();
		org.alice.ide.i18n.Page page = new org.alice.ide.i18n.Page( formatter.getInfixExpressionText( infixExpression ) );
		org.lgna.croquet.views.SwingComponentView<?> component = factory.createComponent( page, infixExpression );
		for( java.awt.Component child : component.getAwtComponent().getComponents() ) {
			if( child instanceof javax.swing.JLabel ) {
				javax.swing.JLabel label = (javax.swing.JLabel)child;
				String text = label.getText();
				//todo: remove this terrible hack
				boolean isScaleDesired = false;
				if( text.length() == 3 ) {
					char c0 = text.charAt( 0 );
					char c1 = text.charAt( 1 );
					char c2 = text.charAt( 2 );
					if( ( c0 == ' ' ) && ( c2 == ' ' ) ) {
						if( Character.isLetterOrDigit( c1 ) ) {
							//pass
						} else {
							isScaleDesired = true;
						}
					}
				} else if( text.length() == 4 ) {
					isScaleDesired = " >= ".equals( text ) || " <= ".equals( text ) || " == ".equals( text );
				}
				edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( label, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
				if( isScaleDesired ) {
					edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToScaledFont( label, 1.5f );
				}
				//label.setVerticalAlignment( javax.swing.SwingConstants.CENTER );
			}
		}
		this.addComponent( component );
	}
}
