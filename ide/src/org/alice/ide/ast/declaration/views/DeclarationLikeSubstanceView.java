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
package org.alice.ide.ast.declaration.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationLikeSubstanceView extends org.alice.ide.preview.components.PanelWithPreview {
	private org.lgna.croquet.components.TextField nameTextField;

	public DeclarationLikeSubstanceView( org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?> composite ) {
		super( composite );
		this.setMinimumPreferredWidth( 480 );
	}

	public void handleValueTypeChanged( org.lgna.project.ast.AbstractType<?, ?, ?> nextType ) {
	}

	protected org.lgna.croquet.components.JComponent<?> createPageStartComponent() {
		final org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?> composite = (org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?>)this.getComposite();
		org.lgna.croquet.components.FormPanel rv = new org.lgna.croquet.components.FormPanel() {
			@Override
			protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
				java.awt.LayoutManager rv = super.createLayoutManager( jPanel );
				if( rv instanceof net.miginfocom.swing.MigLayout ) {
					net.miginfocom.swing.MigLayout migLayout = (net.miginfocom.swing.MigLayout)rv;
					migLayout.setRowConstraints( "[]20[]10[]" );
				}
				return rv;
			}

			@Override
			protected void appendRows( java.util.List<org.lgna.croquet.components.LabeledFormRow> rows ) {
				org.alice.ide.x.AstI18nFactory factory = org.alice.ide.x.PreviewAstI18nFactory.getInstance();

				org.lgna.croquet.BooleanState isFinalState = composite.getIsFinalState();
				if( isFinalState != null ) {
					rows.add( new org.lgna.croquet.components.LabeledFormRow(
							isFinalState.getSidekickLabel(),
							isFinalState.createVerticalRadioButtons( false ),
							org.lgna.croquet.components.VerticalAlignment.TOP
							)
							);
				}

				if( composite.isValueComponentTypeDisplayed() ) {
					org.lgna.croquet.CustomItemState<org.lgna.project.ast.AbstractType> valueComponentTypeState = composite.getValueComponentTypeState();
					org.lgna.croquet.BooleanState valueIsArrayTypeState = composite.getValueIsArrayTypeState();
					if( valueComponentTypeState != null ) {
						org.lgna.croquet.components.JComponent<?> component;
						if( valueComponentTypeState.isEnabled() ) {
							org.alice.ide.croquet.components.TypeDropDown typeDropDown = new org.alice.ide.croquet.components.TypeDropDown( valueComponentTypeState );
							if( composite.isValueIsArrayTypeStateDisplayed() ) {
								component = new org.lgna.croquet.components.BorderPanel.Builder()
										.center( typeDropDown )
										.lineEnd( valueIsArrayTypeState.createCheckBox() )
										.build();
							} else {
								component = typeDropDown;
							}
						} else {
							if( composite.isValueIsArrayTypeStateDisplayed() ) {
								component = new org.lgna.croquet.components.Label( "todo" );
							} else {
								component = new org.alice.ide.croquet.components.TypeView( valueComponentTypeState, valueIsArrayTypeState.getValue() );
							}
						}
						rows.add( new org.lgna.croquet.components.LabeledFormRow(
								valueComponentTypeState.getSidekickLabel(),
								component,
								false
								) );
					}
				}

				org.lgna.croquet.StringState nameState = composite.getNameState();
				if( nameState != null ) {
					nameTextField = nameState.createTextField();
					rows.add( new org.lgna.croquet.components.LabeledFormRow(
							nameState.getSidekickLabel(),
							nameTextField
							) );
				}

				if( composite.isInitializerDisplayed() ) {
					org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> initializerState = composite.getInitializerState();
					if( initializerState != null ) {
						org.lgna.croquet.components.JComponent<?> component;
						if( initializerState.isEnabled() ) {
							component = new org.alice.ide.croquet.components.ExpressionDropDown( initializerState, factory );
						} else {
							component = factory.createExpressionPane( initializerState.getValue() );
						}
						rows.add( new org.lgna.croquet.components.LabeledFormRow(
								initializerState.getSidekickLabel(),
								component,
								false
								) );
					}
				}
			}
		};
		return rv;
	}

	@Override
	protected org.lgna.croquet.components.BorderPanel createMainComponent() {
		return new org.lgna.croquet.components.BorderPanel.Builder()
				.pageStart( this.createPageStartComponent() )
				.build();

	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		if( this.nameTextField != null ) {
			final org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?> composite = (org.alice.ide.ast.declaration.DeclarationLikeSubstanceComposite<?>)this.getComposite();
			final org.lgna.croquet.StringState nameState = composite.getNameState();
			if( nameState != null ) {
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						nameState.selectAll();
						nameTextField.requestFocus();
					}
				} );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
			}
		}
	}
}
