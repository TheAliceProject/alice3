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
package org.alice.ide.declarationpanes;

/**
 * @author Dennis Cosgrove
 */
class IsArrayState extends org.lgna.croquet.BooleanState {
	private edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty;
	public IsArrayState( edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty ) {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "ffa22de2-eb3e-46d2-8ccc-ada365f29205" ), isArrayProperty.getValue() );
		this.isArrayProperty = isArrayProperty;
		this.setTextForBothTrueAndFalse( "is array" );
		this.addValueObserver( new ValueObserver< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				IsArrayState.this.isArrayProperty.setValue( nextValue );
			}
		} );
	}
}

class TypePropertyItemState extends org.lgna.croquet.CustomItemState< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
	private final edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > typeProperty;
	public TypePropertyItemState( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > typeProperty ) {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "1818f209-d305-431c-8fea-bcb8698ba908" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType.class ), org.alice.ide.croquet.models.ast.declaration.TypeBlank.getInstance() );
		this.typeProperty = typeProperty;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getValue() {
		return this.typeProperty.getValue();
	}
	@Override
	protected void handleValueChange( edu.cmu.cs.dennisc.alice.ast.AbstractType value ) {
		this.typeProperty.setValue( value );
	}
}

public class TypePane extends org.lgna.croquet.components.BorderPanel {
	private edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > typeProperty;
	private IsArrayState isArrayStateState;
	
	private class TypeDropDownPane extends org.lgna.croquet.components.ItemDropDown< edu.cmu.cs.dennisc.alice.ast.AbstractType, TypePropertyItemState > {
		public TypeDropDownPane( TypePropertyItemState model ) {
			super( model );
			this.update( model.getValue() );
			this.getAwtComponent().setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4,4,4,4 ) );
		}
		private void update( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
			this.getAction().putValue( javax.swing.Action.SMALL_ICON, new org.alice.ide.common.TypeIcon( type ) {
				@Override
				protected java.awt.Color getTextColor(java.awt.Component c) {
					return super.getTextColor( TypeDropDownPane.this.getAwtComponent() );
				}
			} );
		}
		
		@Override
		protected void handleChanged( org.lgna.croquet.State< edu.cmu.cs.dennisc.alice.ast.AbstractType > state, edu.cmu.cs.dennisc.alice.ast.AbstractType prevValue, edu.cmu.cs.dennisc.alice.ast.AbstractType nextValue, boolean isAdjusting ) {
			this.update( nextValue );
		}
	};
	
	public TypePane( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > typeProperty, edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty, boolean isTypeComboBoxEnabled, boolean isArrayCheckBoxEnabled ) {
		assert typeProperty != null;
		this.typeProperty = typeProperty;
		final TypeDropDownPane typeDropDownPane = new TypeDropDownPane( new TypePropertyItemState( typeProperty ) );
		typeDropDownPane.getAwtComponent().setEnabled( isTypeComboBoxEnabled );
		this.isArrayStateState = new IsArrayState( isArrayProperty );
		this.isArrayStateState.setEnabled( isArrayCheckBoxEnabled );
		
		org.lgna.croquet.components.CheckBox isArrayCheckBox = this.isArrayStateState.createCheckBox();
		isArrayCheckBox.setBackgroundColor( null );
		this.addComponent( typeDropDownPane, Constraint.CENTER );
		this.addComponent( isArrayCheckBox, Constraint.LINE_END );
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getValueType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> rv = this.typeProperty.getValue();
		if( rv != null ) {
			if( this.isArrayStateState.getValue() ) {
				rv = rv.getArrayType();
			}
		}
		return rv;
	}
}
