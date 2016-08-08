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
package org.alice.ide.ast.declaration.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationLikeSubstanceView extends org.alice.ide.preview.components.PanelWithPreview {
	private org.lgna.croquet.views.TextField nameTextField;

	public DeclarationLikeSubstanceView( org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?> composite ) {
		super( composite );
		this.setMinimumPreferredWidth( 480 );
	}

	public void handleInitializerChanged( org.lgna.project.ast.Expression expression ) {
	}

	protected org.lgna.croquet.views.SwingComponentView<?> createPageStartComponent() {
		final org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?> composite = (org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?>)this.getComposite();
		org.lgna.croquet.views.MigPanel rv = new org.lgna.croquet.views.MigPanel( null, "", "[align right]12[]24[]" );
		org.alice.ide.x.AstI18nFactory factory = org.alice.ide.x.PreviewAstI18nFactory.getInstance();

		org.lgna.croquet.BooleanState isFinalState = composite.getIsFinalState();
		if( isFinalState != null ) {
			rv.addComponent( isFinalState.getSidekickLabel().createLabel() );
			rv.addComponent( isFinalState.createVerticalRadioButtons( false ), "wrap" );
		}

		if( composite.isValueComponentTypeDisplayed() ) {
			org.lgna.croquet.CustomItemState<org.lgna.project.ast.AbstractType> valueComponentTypeState = composite.getValueComponentTypeState();
			org.lgna.croquet.BooleanState valueIsArrayTypeState = composite.getValueIsArrayTypeState();
			if( valueComponentTypeState != null ) {
				org.lgna.croquet.views.SwingComponentView<?> component;
				if( valueComponentTypeState.isEnabled() ) {
					org.alice.ide.croquet.components.TypeDropDown typeDropDown = new org.alice.ide.croquet.components.TypeDropDown( valueComponentTypeState );
					if( composite.isValueIsArrayTypeStateDisplayed() ) {
						component = new org.lgna.croquet.views.BorderPanel.Builder()
								.center( typeDropDown )
								.lineEnd( valueIsArrayTypeState.createCheckBox() )
								.build();
					} else {
						component = typeDropDown;
					}
				} else {
					component = new org.alice.ide.croquet.components.TypeView( valueComponentTypeState, valueIsArrayTypeState.getValue() );
				}
				rv.addComponent( valueComponentTypeState.getSidekickLabel().createLabel() );
				rv.addComponent( component, "wrap" );
			}
		}

		org.lgna.croquet.StringState nameState = composite.getNameState();
		if( nameState != null ) {
			nameTextField = nameState.createTextField();
			nameTextField.enableSelectAllWhenFocusGained();
			nameTextField.getAwtComponent().setColumns( 24 );
			rv.addComponent( nameState.getSidekickLabel().createLabel() );
			rv.addComponent( nameTextField, "wrap" );
		}

		if( composite.isInitializerDisplayed() ) {
			org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> initializerState = composite.getInitializerState();
			if( initializerState != null ) {
				org.lgna.croquet.views.SwingComponentView<?> component;
				if( initializerState.isEnabled() ) {
					component = new org.alice.ide.croquet.components.ExpressionDropDown( initializerState, factory );
				} else {
					component = new org.alice.ide.croquet.components.ExpressionStateView( initializerState, factory );
				}
				rv.addComponent( initializerState.getSidekickLabel().createLabel() );
				rv.addComponent( component, "wrap" );
			}
		}
		return rv;
	}

	@Override
	protected org.lgna.croquet.views.BorderPanel createMainComponent() {
		return new org.lgna.croquet.views.BorderPanel.Builder()
				.pageStart( this.createPageStartComponent() )
				.build();

	}

	@Override
	public void handleCompositePreActivation() {
		super.handleCompositePreActivation();
		if( this.nameTextField != null ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					nameTextField.requestFocus();
				}
			} );
		}
	}
}
