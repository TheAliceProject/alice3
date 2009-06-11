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
package org.alice.ide.ubiquitouspane.templates;

/**
 * @author Dennis Cosgrove
 */
public class VariableArrayAssignmentStatementTemplate extends ArrayAssignmentStatementTemplate {
	private edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable;
	public VariableArrayAssignmentStatementTemplate( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable ) {
		super( org.alice.ide.ast.NodeUtilities.createIncompleteVariableArrayAssignmentStatement( variable ) );
		this.variable = variable;
	}
	@Override
	protected String getTransientName() {
		return this.variable.getName();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getTransientComponentType() {
		return this.variable.valueType.getValue().getComponentType();
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		return org.alice.ide.ast.NodeUtilities.createVariableArrayAssignmentStatement( this.variable, expressions[ 0 ], expressions[ 1 ] );
	}
}
