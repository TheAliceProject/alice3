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

class IsArrayStateOperation extends zoot.AbstractBooleanStateOperation {
	private edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty;
	public IsArrayStateOperation( edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty ) {
		super( isArrayProperty.getValue() );
		this.isArrayProperty = isArrayProperty;
		this.putValue( javax.swing.Action.NAME, "is array" );
	}
	public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
		this.isArrayProperty.setValue( booleanStateContext.getNextValue() );
	}
}

public class TypePane extends swing.LineAxisPane {
	private DeclarationProperty< AbstractType > typeProperty;
	private org.alice.ide.common.TypeComboBox typeComboBox;
	private zoot.ZCheckBox isArrayCheckBox;

	public TypePane( DeclarationProperty< AbstractType > typeProperty, edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty, boolean isArrayCheckBoxEnabled ) {
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
		
		this.isArrayCheckBox = new zoot.ZCheckBox( new IsArrayStateOperation( isArrayProperty ) );
		this.isArrayCheckBox.setOpaque( false );
		this.isArrayCheckBox.setEnabled( isArrayCheckBoxEnabled );
		this.add( this.typeComboBox );
		this.add( this.isArrayCheckBox );
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
