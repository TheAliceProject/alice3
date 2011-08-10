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
public abstract class DeclarationPanel< M extends org.alice.ide.croquet.models.declaration.DeclarationOperation< ? > > extends org.alice.ide.croquet.components.PanelWithPreview< M > {
	public DeclarationPanel( M model ) {
		super( model );
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
	}
	
	protected org.lgna.croquet.components.Component< ? >[] createWarningRow() {
		return null;
	}
	protected boolean isValueTypeRowDesired() {
		return true;
	}
	
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		javax.swing.JTextField jTextField = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findFirstMatch( this.getAwtComponent(), javax.swing.JTextField.class );
		if( jTextField != null ) {
			jTextField.requestFocus();
		}
	}
	@Override
	protected void handleUndisplayable() {
		super.handleUndisplayable();
	}

	protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv, M model ) {
		if( this.isValueTypeRowDesired() ) {
			if( model.getComponentValueTypeState() != null ) {
				org.lgna.croquet.components.Component< ? > component;
				if( model.isValueComponentTypeEditable() ) {
					org.lgna.croquet.components.BorderPanel panel = new org.lgna.croquet.components.BorderPanel();
					panel.addComponent( new org.alice.ide.croquet.components.TypeDropDown( model.getComponentValueTypeState() ), Constraint.CENTER );
					panel.addComponent( model.getIsArrayState().createCheckBox(), Constraint.LINE_END );
					component = panel;
				} else {
					if( model.isIsArrayEditable() ) {
						//todo? this case is not currently supported
						component = null;
					} else {
						component = new org.alice.ide.croquet.components.TypeView( model.getComponentValueTypeState(), model.getIsArrayState().getValue() );
					}
				}
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( model.getValueTypeLabelText() + ":", component ) );
			}
		}
		if( model.getNameState() != null ) {
			rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( model.getNameLabelText() + ":", model.getNameState().createTextField() ) );
		}
		if( model.getInitializerState() != null ) {
			org.lgna.croquet.components.Component< ? > component;
			if( model.isInitializerEditable() ) {
				component = new org.lgna.croquet.components.LineAxisPanel( 
						model.getInitializerState().createEditor(),
						org.lgna.croquet.components.BoxUtilities.createHorizontalGlue()
				);
			} else {
				component = model.getInitializerState().createView();
			}
			rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( model.getInitializerLabelText() + ":", component ) );
		}
		org.lgna.croquet.components.Component< ? >[] warningRow = this.createWarningRow();
		if( warningRow != null ) {
			rv.add( warningRow );
		}
		return rv;
	}
	private boolean isDeclaringTypeAffordanceDesired() {
		return false;
	}
	@Override
	protected org.lgna.croquet.components.Component< ? > createMainComponent() {
		final M model = this.getModel();
		class DetailsPanel extends org.lgna.croquet.components.RowsSpringPanel {
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				return DeclarationPanel.this.updateComponentRows( rv, DeclarationPanel.this.getModel() );
			}
		}
		DetailsPanel detailsPanel = new DetailsPanel();
		org.lgna.croquet.components.Component< ? > rv;
		if( this.isDeclaringTypeAffordanceDesired() && model.getDeclaringTypeState() != null ) {
			org.lgna.croquet.components.PageAxisPanel panel = new org.lgna.croquet.components.PageAxisPanel();
			if( model.isDeclaringTypeEditable() ) {
				panel.addComponent( model.getDeclaringTypeState().createComponent() );
			} else {
				//todo? this case is not currently supported
			}
			detailsPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 24, 64, 0, 0 ) );
			panel.addComponent( detailsPanel );
			rv = panel;
		} else {
			rv = detailsPanel;
		}
		return rv;
	}
}
