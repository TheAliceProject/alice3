/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.createdeclarationpanes;

/**
 * @author Dennis Cosgrove
 */
import edu.cmu.cs.dennisc.alice.ast.AbstractType;
import edu.cmu.cs.dennisc.alice.ast.DeclarationProperty;

public class TypePane extends swing.LineAxisPane {
	class IsArrayStateOperation extends zoot.AbstractBooleanStateOperation {
		public IsArrayStateOperation( Boolean initialState ) {
			super( initialState );
			this.putValue( javax.swing.Action.NAME, "is array" );
		}
		public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
			handleIsArrayChange( booleanStateContext.getNextValue() );
		}
	}
	private DeclarationProperty< AbstractType > typeProperty;
	private org.alice.ide.common.TypeComboBox typeComboBox;
	private zoot.ZCheckBox isArrayCheckBox;

	public TypePane( DeclarationProperty< AbstractType > typeProperty, boolean isArrayCheckBoxSelectedIfTypeValueIsNull, boolean isArrayCheckBoxEnabled ) {
		assert typeProperty != null;
		this.typeProperty = typeProperty;
		AbstractType type = this.typeProperty.getValue();
		AbstractType componentType;
		boolean isArrayCheckBoxSelected;
		if( type != null ) {
			if( type.isArray() ) {
				componentType = type.getComponentType();
				isArrayCheckBoxSelected = true;
			} else {
				componentType = type;
				isArrayCheckBoxSelected = false;
			}
		} else {
			componentType = null;
			isArrayCheckBoxSelected = isArrayCheckBoxSelectedIfTypeValueIsNull;
		}
		//todo: listen to changes on typeProperty
		this.typeComboBox = new org.alice.ide.common.TypeComboBox( componentType ) {
			@Override
			protected void handleTypeChange() {
				TypePane.this.handleComponentTypeChange( (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.getSelectedItem() );
			}
		};
		
		this.isArrayCheckBox = new zoot.ZCheckBox( new IsArrayStateOperation( isArrayCheckBoxSelected ) );
		this.isArrayCheckBox.setOpaque( false );
		this.isArrayCheckBox.setEnabled( isArrayCheckBoxEnabled );
		this.add( this.typeComboBox );
		this.add( this.isArrayCheckBox );
	}

	private edu.cmu.cs.dennisc.alice.ast.AbstractType getArrayTypeIfAppropriate( edu.cmu.cs.dennisc.alice.ast.AbstractType componentType ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType rv = componentType;
		if( rv != null ) {
			if( this.isArrayCheckBox.isSelected() ) {
				rv = rv.getArrayType();
			}
		}
		return rv;
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType rv = (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem();
		if( rv != null ) {
			if( this.isArrayCheckBox.isSelected() ) {
				rv = rv.getArrayType();
			}
		}
		return rv;
	}
	private void updateTypeProperty() {
		this.typeProperty.setValue( this.getArrayTypeIfAppropriate( (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem() ) );
	}
	private void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.updateTypeProperty();
	}
	private void handleIsArrayChange( boolean isArray ) {
		this.updateTypeProperty();
	}
}
