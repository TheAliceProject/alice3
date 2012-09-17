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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class FieldDeclarationPane extends org.lgna.croquet.components.LineAxisPanel {
	private org.lgna.project.ast.UserField field;
	private org.lgna.croquet.components.Label finalLabel = new org.lgna.croquet.components.Label();

	public FieldDeclarationPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.UserField field, boolean isDropDownDesired ) {
		this.field = field;
		if( org.alice.ide.croquet.models.ui.preferences.IsExposingReassignableStatusState.getInstance().getValue() ) {
			this.addComponent( finalLabel );
		}
		this.addComponent( TypeComponent.createInstance( field.getValueType() ) );
		this.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
		org.alice.ide.ast.components.DeclarationNameLabel nameLabel = new org.alice.ide.ast.components.DeclarationNameLabel( field );
		nameLabel.scaleFont( 1.5f );
		this.addComponent( nameLabel );
		this.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
		this.addComponent( new org.alice.ide.common.GetsPane( true ) );

		org.lgna.croquet.components.Component<?> component;
		if( isDropDownDesired ) {
			component = new org.alice.ide.croquet.components.ExpressionDropDown( org.alice.ide.croquet.models.ast.FieldInitializerState.getInstance( this.field ), org.alice.ide.x.DialogAstI18nFactory.getInstance() );
		} else {
			component = new org.alice.ide.x.components.ExpressionPropertyView( factory, field.initializer );
		}
		this.addComponent( component );
	}

	public FieldDeclarationPane( org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.UserField field ) {
		this( factory, field, false );
	}

	private void updateFinalLabel() {
		String text;
		if( field.isFinal() ) {
			text = "permanently set ";
		} else {
			text = "initialize ";
		}
		this.finalLabel.setText( text );
	}

	private edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}

		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			updateFinalLabel();
		}
	};

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.updateFinalLabel();
		this.field.finalVolatileOrNeither.addPropertyListener( this.propertyListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.field.finalVolatileOrNeither.addPropertyListener( this.propertyListener );
		super.handleUndisplayable();
	}

	public org.lgna.project.ast.UserField getField() {
		return this.field;
	}
}
