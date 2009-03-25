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
public class CreateLocalPane extends CreateTypedDeclarationWithInitializerPane<edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement> {
	@Override
	protected boolean isNameAcceptable( String name ) {
		return false;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement getActualInputValue() {
		String name = this.getNameText();
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getValueType();
		boolean isContant = this.isConstant();
		edu.cmu.cs.dennisc.alice.ast.Expression initializer = this.getInitializer();
		edu.cmu.cs.dennisc.alice.ast.LocalDeclarationStatement rv;
		if( isContant ) {
			edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice constant = new edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice(
					name,
					type
			);
			rv = new edu.cmu.cs.dennisc.alice.ast.ConstantDeclarationStatement(
					constant,
					initializer
			);
		} else {
			edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = new edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice(
					name,
					type
			);
			rv = new edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement(
					variable,
					initializer
			);
		}
		return rv;
	}

	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.IDE() {
			@Override
			protected zoot.ActionOperation createAboutOperation() {
				return null;
			}
			@Override
			protected void promptForLicenseAgreements() {
			}
		};
		CreateLocalPane createLocalPane = new CreateLocalPane();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( createLocalPane.showInJDialog( ide ) );
	}
}
