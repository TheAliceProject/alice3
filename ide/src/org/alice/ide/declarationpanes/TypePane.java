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
import edu.cmu.cs.dennisc.alice.ast.AbstractType;
import edu.cmu.cs.dennisc.alice.ast.DeclarationProperty;


//todo: BooleanPropertyOperation
class IsArrayStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	private edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty;
	public IsArrayStateOperation( edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty ) {
		super( edu.cmu.cs.dennisc.zoot.ZManager.UNKNOWN_GROUP, isArrayProperty.getValue(), "is array" );
		this.isArrayProperty = isArrayProperty;
	}
	@Override
	protected void handleStateChange(boolean value) {
		this.isArrayProperty.setValue( value );
	}
}

public class TypePane extends edu.cmu.cs.dennisc.croquet.KLineAxisPanel {
	private DeclarationProperty< AbstractType > typeProperty;
	private org.alice.ide.common.TypeComboBox typeComboBox;
	private IsArrayStateOperation isArrayStateOperation;

	public TypePane( DeclarationProperty< AbstractType > typeProperty, edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty, boolean isTypeComboBoxEnabled, boolean isArrayCheckBoxEnabled ) {
		assert typeProperty != null;
		this.typeProperty = typeProperty;
		AbstractType type = this.typeProperty.getValue();
		AbstractType componentType;
//		boolean isArrayCheckBoxSelected;
		if( type != null ) {
			if( type.isArray() ) {
				componentType = type.getComponentType();
//				isArrayCheckBoxSelected = true;
			} else {
				componentType = type;
//				isArrayCheckBoxSelected = false;
			}
		} else {
			componentType = null;
//			isArrayCheckBoxSelected = isArrayCheckBoxSelectedIfTypeValueIsNull;
		}
		//todo: listen to changes on typeProperty
		this.typeComboBox = new org.alice.ide.common.TypeComboBox( componentType ) {
			@Override
			protected void handleTypeChange() {
				TypePane.this.updateTypeProperty();
			}
		};
		
		this.typeComboBox.setEnabled( isTypeComboBoxEnabled );
		
		isArrayProperty.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				TypePane.this.updateTypeProperty();
			}
		} );
		
		
		this.typeProperty.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				typeComboBox.setSelectedItem( e.getValue() );
			}
		} );
		
		this.isArrayStateOperation = new IsArrayStateOperation( isArrayProperty );
		this.isArrayStateOperation.setEnabled( isArrayCheckBoxEnabled );
		
		javax.swing.JCheckBox isArrayCheckBox = edu.cmu.cs.dennisc.zoot.ZManager.createCheckBox( this.isArrayStateOperation );
		isArrayCheckBox.setOpaque( false );
		
		this.add( this.typeComboBox );
		this.add( isArrayCheckBox );
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType rv = (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem();
		if( rv != null ) {
			if( this.isArrayStateOperation.getState() ) {
				rv = rv.getArrayType();
			}
		}
		return rv;
	}
	
	public void disableComboBox() {
//		if( type.isArray() ) {
//			this.typeComboBox.setSelectedItem( type.getComponentType() );
//		} else {
//			this.typeComboBox.setSelectedItem( type );
//		}
		this.typeComboBox.setEnabled( false );
//		this.isArrayCheckBox.setSelected( type.isArray() );
//		this.isArrayCheckBox.setEnabled( false );
	}
	
	private void updateTypeProperty() {
		this.typeProperty.setValue( (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem() );
	}
}
