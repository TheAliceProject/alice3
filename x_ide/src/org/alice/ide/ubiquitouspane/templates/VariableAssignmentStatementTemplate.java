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
public class VariableAssignmentStatementTemplate extends CascadingUbiquitousStatementTemplate {
	private edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable;
	public VariableAssignmentStatementTemplate( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable ) {
		super( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class, org.alice.ide.ast.NodeUtilities.createIncompleteVariableAssignmentStatement( variable ) );
		this.variable = variable;
		this.variable.name.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				VariableAssignmentStatementTemplate.this.updateLabel();
			}
		} );
	}
	@Override
	protected String getLabelText() {
		assert this.variable != null;
		return this.variable.getName() + "\u2190\u2423";
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes() {
		assert this.variable != null;
		return new edu.cmu.cs.dennisc.alice.ast.AbstractType[] { this.variable.valueType.getValue() };
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		return org.alice.ide.ast.NodeUtilities.createVariableAssignmentStatement( this.variable, expressions[ 0 ] );
	}
}
