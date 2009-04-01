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
class TypePane extends swing.LineAxisPane {
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
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: updateOKButton()" );
		//updateOKButton();
	}
	private org.alice.ide.common.TypeComboBox typeComboBox;
	private zoot.ZCheckBox isArrayCheckBox;
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		return (edu.cmu.cs.dennisc.alice.ast.AbstractType)this.typeComboBox.getSelectedItem();
	}
	public TypePane() {
		this.typeComboBox = new org.alice.ide.common.TypeComboBox();
		this.isArrayCheckBox = new zoot.ZCheckBox( new IsArrayStateOperation() );
		this.isArrayCheckBox.setOpaque( false );
		this.add( this.typeComboBox );
		this.add( this.isArrayCheckBox );
	}
}
