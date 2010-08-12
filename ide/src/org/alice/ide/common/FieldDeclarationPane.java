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
public class FieldDeclarationPane extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
	private edu.cmu.cs.dennisc.croquet.Label finalLabel = new edu.cmu.cs.dennisc.croquet.Label();
	public FieldDeclarationPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		this.field = field;
		if( org.alice.ide.croquet.models.ui.preferences.IsExposingReassignableStatusState.getInstance().getValue() ) {
			this.addComponent( finalLabel );
		}
		this.addComponent( TypeComponent.createInstance( field.getValueType() ) );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		org.alice.ide.common.DeclarationNameLabel nameLabel = new org.alice.ide.common.DeclarationNameLabel( field );
		nameLabel.scaleFont( 1.5f );
		this.addComponent( nameLabel );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 8 ) );
		this.addComponent( new org.alice.ide.common.GetsPane( true ) );
		
		//todo
		//boolean isDropDownPotentiallyDesired = factory instanceof org.alice.ide.memberseditor.Factory;
		
		edu.cmu.cs.dennisc.croquet.Component< ? > component = new org.alice.ide.common.ExpressionPropertyPane( factory, field.initializer );
//		if( factory instanceof org.alice.ide.memberseditor.Factory ) {
//			if( org.alice.ide.IDE.getSingleton().isDropDownDesiredForFieldInitializer( field ) ) {
//				component = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane(null, component, field.initializer, field.getDesiredValueType() );
//			}
//		}
		this.addComponent( component );
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
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		this.updateFinalLabel();
		this.field.finalVolatileOrNeither.addPropertyListener( this.propertyListener );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.field.finalVolatileOrNeither.addPropertyListener( this.propertyListener );
		super.handleRemovedFrom( parent );
	}
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getField() {
		return this.field;
	}
}
