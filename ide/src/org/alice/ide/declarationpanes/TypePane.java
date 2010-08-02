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
class IsArrayState extends edu.cmu.cs.dennisc.croquet.BooleanState {
	private edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty;
	public IsArrayState( edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty ) {
		super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "ffa22de2-eb3e-46d2-8ccc-ada365f29205" ), isArrayProperty.getValue(), "is array" );
		this.isArrayProperty = isArrayProperty;
		this.addValueObserver( new ValueObserver() {
			public void changing( boolean nextValue ) {
			}
			public void changed( boolean nextValue ) {
				IsArrayState.this.isArrayProperty.setValue( nextValue );
			}
		} );
	}
}


public class TypePane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
//	private static class TypeSelectionState extends edu.cmu.cs.dennisc.croquet.ListSelectionState< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > {
//		private static class TypeListCellRenderer extends edu.cmu.cs.dennisc.javax.swing.components.JBorderPane implements javax.swing.ListCellRenderer {
//			private javax.swing.JLabel leadingLabel = new javax.swing.JLabel();
//			private class Label extends javax.swing.JLabel {
//				private boolean isPaintingDesired = true;
//				@Override
//				protected void paintComponent(java.awt.Graphics g) {
//					if( this.isPaintingDesired ) {
//						super.paintComponent( g );
//					}
//				}
//			}
//			private Label trailingLabel = new Label();
//			public TypeListCellRenderer() {
//				this.add( this.leadingLabel, java.awt.BorderLayout.LINE_START );
//				this.add( javax.swing.Box.createHorizontalStrut( 16 ) );
//				this.add( this.trailingLabel, java.awt.BorderLayout.LINE_END );
//				this.setOpaque( true );
//				this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
//			}
//			public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = (edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>)value;
//				org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//				this.leadingLabel.setIcon( ide.getIconFor( type ) );
//				this.trailingLabel.setText( ide.getTextFor( type ) );
//				
//				if( isSelected ) {
//					this.setBackground( list.getSelectionBackground() );
//					this.leadingLabel.setForeground( list.getSelectionForeground() );
//					this.trailingLabel.setForeground( list.getSelectionForeground() );
//				} else {
//					this.setBackground( list.getBackground() );
//					this.leadingLabel.setForeground( list.getForeground() );
//					this.trailingLabel.setForeground( list.getForeground() );
//				}
//				this.trailingLabel.isPaintingDesired = index != -1;
////				if( index != -1 ) {
////					//pass
////				} else {
////					this.trailingLabel.setForeground( null );
////				}
//				return this;
//			}
//		}
//
//		public TypeSelectionState() {
//			super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "ef5677ca-a5d9-49c4-90bb-5fb43ef15ba6" ) );
//			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types = edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( org.alice.ide.IDE.getSingleton().getTypesForComboBoxes(), edu.cmu.cs.dennisc.alice.ast.AbstractType.class );
//			//int selectedIndex = java.util.Arrays.binarySearch( types, type );
//			this.setListData( -1, types );
//		}
//		@Override
//		public edu.cmu.cs.dennisc.croquet.ComboBox<edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>> createComboBox() {
//			edu.cmu.cs.dennisc.croquet.ComboBox<edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>> rv = super.createComboBox();
//			TypeListCellRenderer typeListCellRenderer = new TypeListCellRenderer();
//			rv.setRenderer( typeListCellRenderer );
//			rv.setMaximumRowCount( Math.min( this.getItemCount(), 20 ) );
//			return rv;
//		}
//		
//		@Override
//		public void setSelectedItem(edu.cmu.cs.dennisc.alice.ast.AbstractType<?, ?, ?> selectedItem) {
//			if( selectedItem != null ) {
//				if( this.containsItem( selectedItem ) ) {
//					//pass
//				} else {
//					this.addItem( selectedItem );
//				}
//			}
//			super.setSelectedItem(selectedItem);
//		}
//		@Override
//		protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> value) {
//			throw new RuntimeException( "todo" );
//		}
//		@Override
//		protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
//			throw new RuntimeException( "todo" );
//		}
//	}

	private edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > typeProperty;
	private IsArrayState isArrayStateState;
	//private TypeSelectionState typeSelectionState = new TypeSelectionState();
	private class SelectTypeOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
		public SelectTypeOperation( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
			super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "8f3e1f74-d1fd-4484-98e0-bc37da452005" ) );
			this.type = type;
			this.setSmallIcon( org.alice.ide.common.TypeIcon.getInstance( this.type ) );
			this.setName( org.alice.ide.IDE.getSingleton().getTextFor( type ) );
		}
		@Override
		protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
			typeProperty.setValue( this.type );
			context.finish();
		}
	}
	
	private edu.cmu.cs.dennisc.croquet.AbstractPopupMenuOperation popupMenuOperation = new edu.cmu.cs.dennisc.croquet.AbstractPopupMenuOperation( java.util.UUID.fromString( "b59ec150-56cd-4270-8b5a-80dcb5cd7bd9" ) ) {
		@Override
		public edu.cmu.cs.dennisc.croquet.Model[] getModels() {
			
			java.util.List< edu.cmu.cs.dennisc.croquet.Model > models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();

			java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > javaTypes = org.alice.ide.IDE.getSingleton().getPrimeTimeSelectableTypesDeclaredInJava();
			for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava javaType : javaTypes ) {
				models.add( new SelectTypeOperation( javaType ) );
			}
			
			java.util.List< edu.cmu.cs.dennisc.croquet.Model > myTypeModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice > aliceTypes = org.alice.ide.IDE.getSingleton().getTypesDeclaredInAlice();
			for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice aliceType : aliceTypes ) {
				myTypeModels.add( new SelectTypeOperation( aliceType ) );
			}
			if( myTypeModels.size() > 0 ) {
				models.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
				models.add( new org.alice.ide.croquet.models.ast.MyTypesMenuModel( myTypeModels ) );
			}

			java.util.List< edu.cmu.cs.dennisc.croquet.Model > otherTypeModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			java.util.List< edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > otherTypes = org.alice.ide.IDE.getSingleton().getSecondarySelectableTypesDeclaredInJava();
			for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava otherType : otherTypes ) {
				otherTypeModels.add( new SelectTypeOperation( otherType ) );
			}
			if( otherTypeModels.size() > 0 ) {
				models.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
				models.add( new org.alice.ide.croquet.models.ast.OtherTypesMenuModel( otherTypeModels ) );
			}
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( models, edu.cmu.cs.dennisc.croquet.Model.class );
		}
	};
	
	private class TypeDropDownPane extends org.alice.ide.common.AbstractDropDownPane {
		private edu.cmu.cs.dennisc.croquet.Label label = new edu.cmu.cs.dennisc.croquet.Label();
		public TypeDropDownPane() {
			this.addComponent( label );
		}
		
		private void refresh() {
			this.label.setIcon( org.alice.ide.common.TypeIcon.getInstance( typeProperty.getValue() ) );
		}
		@Override
		protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
			return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.LINE_AXIS );
		}
		
		@Override
		protected int getInsetTop() {
			return super.getInsetTop() + 3;
		}
		@Override
		protected int getInsetLeft() {
			return super.getInsetLeft() + 3;
		}
		@Override
		protected int getInsetBottom() {
			return super.getInsetBottom() + 3;
		}
		@Override
		protected int getInsetRight() {
			return super.getInsetRight() + 3;
		}
	};
	
	public TypePane( edu.cmu.cs.dennisc.alice.ast.DeclarationProperty< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > typeProperty, edu.cmu.cs.dennisc.property.BooleanProperty isArrayProperty, boolean isTypeComboBoxEnabled, boolean isArrayCheckBoxEnabled ) {
		assert typeProperty != null;
		this.typeProperty = typeProperty;
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = this.typeProperty.getValue();
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> componentType;
//		if( type != null ) {
//			if( type.isArray() ) {
//				componentType = type.getComponentType();
//			} else {
//				componentType = type;
//			}
//		} else {
//			componentType = null;
//		}

		final TypeDropDownPane typeDropDownPane = new TypeDropDownPane();
//		this.typeSelectionState.setEnabled( isTypeComboBoxEnabled );
//		this.typeSelectionState.setSelectedItem( componentType );
		typeDropDownPane.setLeftButtonPressOperation( popupMenuOperation );
		typeDropDownPane.refresh();
		
		this.typeProperty.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				typeDropDownPane.refresh();
//				typeSelectionState.setSelectedItem( (edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>)e.getValue() );
			}
		} );
//		
//		isArrayProperty.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
//			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//			}
//			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
//				TypePane.this.updateTypeProperty();
//			}
//		} );
		
		
		this.isArrayStateState = new IsArrayState( isArrayProperty );
		this.isArrayStateState.setEnabled( isArrayCheckBoxEnabled );
		
		edu.cmu.cs.dennisc.croquet.CheckBox isArrayCheckBox = this.isArrayStateState.createCheckBox();
		isArrayCheckBox.setBackgroundColor( null );
		
//		this.typeSelectionState.addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>>() {
//			public void changed(edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> nextValue) {
//				TypePane.this.typeProperty.setValue( nextValue );
//			}
//		} );
//		
//		this.addComponent( this.typeSelectionState.createComboBox() );
		this.addComponent( typeDropDownPane, Constraint.CENTER );
		this.addComponent( isArrayCheckBox, Constraint.LINE_END );
	}
	
	public edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> getValueType() {
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> rv = this.typeSelectionState.getSelectedItem();
		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> rv = this.typeProperty.getValue();
		if( rv != null ) {
			if( this.isArrayStateState.getValue() ) {
				rv = rv.getArrayType();
			}
		}
		return rv;
	}
	
	public void disableComboBox() {
		//this.typeSelectionState.setEnabled( false );
	}
	
//	private void updateTypeProperty() {
//		this.typeProperty.setValue( this.typeSelectionState.getSelectedItem() );
//	}
}
