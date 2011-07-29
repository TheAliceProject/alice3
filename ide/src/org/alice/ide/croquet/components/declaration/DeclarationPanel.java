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

package org.alice.ide.croquet.components.declaration;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationPanel< M extends org.alice.ide.croquet.models.declaration.DeclarationOperation< ? > > extends org.alice.ide.croquet.components.PanelWithPreview< M > {
	public DeclarationPanel( final M model ) {
		super( model );
		class DetailsPanel extends org.lgna.croquet.components.RowsSpringPanel {
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				if( model.getComponentValueTypeState() != null ) {
					org.lgna.croquet.components.Component< ? > component;
					if( model.isValueComponentTypeEditable() ) {
						component = new org.lgna.croquet.components.LineAxisPanel(
								model.getComponentValueTypeState().getCascadeRoot().getPopupPrepModel().createPopupButton(),
								model.getIsArrayState().createCheckBox()
						);
					} else {
						if( model.isIsArrayEditable() ) {
							//todo? this case is not currently supported
							component = null;
						} else {
							component = org.alice.ide.common.TypeComponent.createInstance( model.getValueType() );
						}
					}
					rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( model.getValueTypeLabelText(), component ) );
				}
				if( model.getNameState() != null ) {
					rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( model.getNameState().getLabelText(), model.getNameState().createTextField() ) );
				}
				if( model.getInitializerState() != null ) {
					org.lgna.croquet.components.Component< ? > component;
					if( model.isInitializerEditable() ) {
						component = model.getInitializerState().getCascadeRoot().getPopupPrepModel().createPopupButton();
					} else {
						component = org.alice.ide.IDE.getActiveInstance().getPreviewFactory().createExpressionPane( model.getInitializerState().getValue() );
					}
					rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( model.getInitializerState().getLabelText(), component ) );
				}
				return rv;
			}
		}
		DetailsPanel detailsPanel = new DetailsPanel();
		if( model.getDeclaringTypeState() != null ) {
			org.lgna.croquet.components.PageAxisPanel panel = new org.lgna.croquet.components.PageAxisPanel();
			if( model.isDeclaringTypeEditable() ) {
				panel.addComponent( model.getDeclaringTypeState().getCascadeRoot().getPopupPrepModel().createPopupButton() );
			} else {
				//todo? this case is not currently supported
			}
			//panel.addComponent( new org.lgna.croquet.components.HorizontalSeparator() );
			detailsPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 24, 32, 0, 0 ) );
			panel.addComponent( detailsPanel );
			this.addComponent( panel, Constraint.CENTER );
		} else {
			this.addComponent( detailsPanel, Constraint.CENTER );
		}
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
	}
}
