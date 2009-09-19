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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
class TypeListCellRenderer extends swing.ListCellRenderer< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.alice.ast.AbstractType value, int index, boolean isSelected, boolean cellHasFocus ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		rv.setText( ide.getTextFor( value ) );
		rv.setIcon( ide.getIconFor( value ) );
//		if( value != null ) {
//			org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//			rv.setText( ide.getTextFor( value ) );
//			rv.setIcon( ide.getIconFor( value ) );
////			rv.setHorizontalTextPosition( javax.swing.SwingConstants.TRAILING );
//		} else {
//			rv.setText( null );
//			rv.setIcon( null );
////			rv.setHorizontalTextPosition( javax.swing.SwingConstants.LEADING );
//		}
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class TypeComboBox extends zoot.ZComboBox {
	public TypeComboBox( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		javax.swing.ComboBoxModel model = org.alice.ide.IDE.getSingleton().getTypeComboBoxModel();
		model.setSelectedItem( type );
		
		this.setItemSelectionOperation( new org.alice.ide.operations.AbstractItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.AbstractType >( model ) {
			@Override
			protected void handleSelectionChange(edu.cmu.cs.dennisc.alice.ast.AbstractType value) {
				TypeComboBox.this.handleTypeChange();
			}
			@Override
			public boolean isSignificant() {
				return true;
			}
		} );
		//this.setModel( model );
		
		
		this.setRenderer( new TypeListCellRenderer() );
		this.setMaximumRowCount( model.getSize() );
		this.setSelectedIndex( -1 );
	}
	
	protected abstract void handleTypeChange();
}
