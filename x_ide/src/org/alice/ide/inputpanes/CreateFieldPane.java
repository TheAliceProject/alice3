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
public class CreateFieldPane extends AbstractCreateFieldPane {
	public CreateFieldPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		super( declaringType );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		return null;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		return null;
	}

	@Override
	protected java.awt.Component[] createValueTypeRow() {
		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "value class:" ), new TypePane() );
	}
	@Override
	protected java.awt.Component[] createInitializerRow() {
		return null;
	}
//	@Override
//	protected java.util.List< java.awt.Component[] > createComponentRows() {
//		zoot.ZLabel label = new zoot.ZLabel( "initializer:" );
//		this.initializerPane = new InitializerPane();
//		this.isConstantCheckBox = new zoot.ZCheckBox( new IsConstantStateOperation() );
//		java.util.List< java.awt.Component[] > rv = super.createComponentRows();
//		rv.add( new java.awt.Component[] { label, this.initializerPane } );
//		rv.add( new java.awt.Component[] { new javax.swing.JLabel(), this.isConstantCheckBox } );
//		return rv;
//	}
//	public edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
//		return this.initializerPane.getInitializer();
//	}
//	
//	protected boolean isConstant() {
//		return this.isConstantCheckBox.isSelected();
//	}
}
