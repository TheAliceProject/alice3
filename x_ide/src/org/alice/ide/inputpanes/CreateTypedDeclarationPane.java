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
package org.alice.ide.inputpanes;

/**
 * @author Dennis Cosgrove
 */
abstract class ListCellRenderer<E> extends javax.swing.DefaultListCellRenderer {
	protected abstract javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, E value, int index, boolean isSelected, boolean cellHasFocus );
	@Override
	public final java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
		java.awt.Component rv = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
		if( rv instanceof javax.swing.JLabel ) {
			getListCellRendererComponent( (javax.swing.JLabel)rv, list, (E)value, index, isSelected, cellHasFocus );
		} else {
			//todo
		}
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
class TypeListCellRenderer extends ListCellRenderer< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.AbstractType value, int index, boolean isSelected, boolean cellHasFocus ) {
		if( value != null ) {
			rv.setText( value.getName() );
		} else {
			rv.setText( "null" );
		}
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
class TypeComboBox extends zoot.ZComboBox {
	public TypeComboBox() {
		this.setModel( org.alice.ide.IDE.getSingleton().getTypeComboBoxModel() );
		this.setRenderer( new TypeListCellRenderer() );
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateTypedDeclarationPane<E> extends CreateDeclarationPane<E> {
	class IsArrayStateOperation extends zoot.AbstractBooleanStateOperation {
		public IsArrayStateOperation() {
			super( false );
			//this.getButtonModelForConfiguringSwing().setActionCommand( "is array" );
			this.putValue( javax.swing.Action.NAME, "is array" );
		}
		public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
			handleIsArrayChange( booleanStateContext.getNextValue() );
		}
	}
	protected void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
	}
	protected void handleIsArrayChange( boolean isArray ) {
		updateOKButton();
	}
	private TypeComboBox typeComboBox;
	private zoot.ZCheckBox isArrayCheckBox;
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem();
	}
	@Override
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		zoot.ZLabel label = new zoot.ZLabel( "type:" );
		this.typeComboBox = new TypeComboBox();
		this.isArrayCheckBox = new zoot.ZCheckBox( new IsArrayStateOperation() );
		swing.LineAxisPane linePane = new swing.LineAxisPane( this.typeComboBox, this.isArrayCheckBox );
		java.util.List< java.awt.Component[] > rv = super.createComponentRows();
		rv.add( new java.awt.Component[] { label, linePane } );
		return rv;
	}
}
