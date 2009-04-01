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
public class CreateLocalPane extends CreateDeclarationPane<edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement> {
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement block;
	public CreateLocalPane( edu.cmu.cs.dennisc.alice.ast.BlockStatement block ) {
		this.block = block;
	}
	@Override
	protected java.awt.Component[] createDeclarationRow() {
		return null;
	}
	@Override
	protected java.awt.Component createInitializerComponent() {
		return null;
	}
	@Override
	protected java.awt.Component createValueTypeComponent() {
		return null;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement getActualInputValue() {
		return null;
	}
//	@Override
//	protected boolean isNameAcceptable( String name ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: isNameAcceptable", name );
//		return true;
//	}
//	@Override
//	protected edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement getActualInputValue() {
//		String name = this.getNameText();
//		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getValueType();
//		boolean isConstant = this.isConstant();
//		edu.cmu.cs.dennisc.alice.ast.Expression initializer = this.getInitializer();
//		edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement rv;
//		if( isConstant ) {
//			rv = new edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement(
//					new edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice(
//							name,
//							type
//					),
//					initializer
//			);
//		} else {
//			rv = new edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement(
//					new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice(
//							name,
//							type
//					),
//					initializer
//			);
//		}
//		return rv;
//	}
//
//	public static void main( String[] args ) {
//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
//		CreateLocalPane createLocalPane = new CreateLocalPane( null );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( createLocalPane.showInJDialog( ide ) );
//		System.exit( 0 );
//	}
}
