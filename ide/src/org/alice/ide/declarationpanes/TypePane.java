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
class IsArrayStateOperation extends edu.cmu.cs.dennisc.croquet.BooleanState {
	private edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty;
	public IsArrayStateOperation( edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty ) {
		super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "ffa22de2-eb3e-46d2-8ccc-ada365f29205" ), isArrayProperty.getValue(), "is array" );
		this.isArrayProperty = isArrayProperty;
		this.addValueObserver( new ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				IsArrayStateOperation.this.isArrayProperty.setValue( nextValue );
			}
		} );
	}
}


public class TypePane extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
	private static class TypeSelectionOperation extends edu.cmu.cs.dennisc.croquet.ListSelectionState< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
		private static class TypeListCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.AbstractType value, int index, boolean isSelected, boolean cellHasFocus ) {
				org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
				rv.setText( ide.getTextFor( value ) );
				rv.setIcon( ide.getIconFor( value ) );
//				if( value != null ) {
//					org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//					rv.setText( ide.getTextFor( value ) );
//					rv.setIcon( ide.getIconFor( value ) );
////					rv.setHorizontalTextPosition( javax.swing.SwingConstants.TRAILING );
//				} else {
//					rv.setText( null );
//					rv.setIcon( null );
////					rv.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
//				}
				return rv;
			}
		}

		public TypeSelectionOperation() {
			super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "ef5677ca-a5d9-49c4-90bb-5fb43ef15ba6" ) );
			edu.cmu.cs.dennisc.alice.ast.AbstractType[] types = org.alice.ide.IDE.getSingleton().getTypesForComboBoxes();
			//int selectedIndex = java.util.Arrays.binarySearch( types, type );
			this.setListData( -1, types );
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.ComboBox<edu.cmu.cs.dennisc.alice.ast.AbstractType> createComboBox() {
			edu.cmu.cs.dennisc.croquet.ComboBox<edu.cmu.cs.dennisc.alice.ast.AbstractType> rv = super.createComboBox();
			rv.setRenderer( new TypeListCellRenderer() );
			rv.setMaximumRowCount( Math.min( this.getItemCount(), 20 ) );
			return rv;
		}
		@Override
		protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.alice.ast.AbstractType value) {
			throw new RuntimeException( "todo" );
		}
		@Override
		protected edu.cmu.cs.dennisc.alice.ast.AbstractType decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
			throw new RuntimeException( "todo" );
		}
	}

	private edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType > typeProperty;
	private IsArrayStateOperation isArrayStateOperation;
	private TypeSelectionOperation typeSelectionOperation = new TypeSelectionOperation();
	
	public TypePane( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType > typeProperty, edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty, boolean isTypeComboBoxEnabled, boolean isArrayCheckBoxEnabled ) {
		assert typeProperty != null;
		this.typeProperty = typeProperty;
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.typeProperty.getValue();
		edu.cmu.cs.dennisc.alice.ast.AbstractType componentType;
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

		this.typeSelectionOperation.setEnabled( isTypeComboBoxEnabled );
		this.typeSelectionOperation.setValue( componentType );
		//todo: listen to changes on typeProperty

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
				typeSelectionOperation.setValue( (edu.cmu.cs.dennisc.alice.ast.AbstractType)e.getValue() );
			}
		} );
		
		this.isArrayStateOperation = new IsArrayStateOperation( isArrayProperty );
		this.isArrayStateOperation.setEnabled( isArrayCheckBoxEnabled );
		
		edu.cmu.cs.dennisc.croquet.CheckBox isArrayCheckBox = this.isArrayStateOperation.createCheckBox();
		isArrayCheckBox.setBackgroundColor( null );
		
		this.typeSelectionOperation.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.AbstractType>() {
			public void changed(edu.cmu.cs.dennisc.alice.ast.AbstractType nextValue) {
				TypePane.this.typeProperty.setValue( nextValue );
			}
		} );
		
		this.addComponent( this.typeSelectionOperation.createComboBox() );
		this.addComponent( isArrayCheckBox );
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType rv = this.typeSelectionOperation.getValue();
		if( rv != null ) {
			if( this.isArrayStateOperation.getValue() ) {
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
		this.typeSelectionOperation.setEnabled( false );
//		this.isArrayCheckBox.setSelected( type.isArray() );
//		this.isArrayCheckBox.setEnabled( false );
	}
	
	private void updateTypeProperty() {
		this.typeProperty.setValue( this.typeSelectionOperation.getValue() );
	}
}
