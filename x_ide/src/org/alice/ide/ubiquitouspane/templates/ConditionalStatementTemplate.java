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
public class ConditionalStatementTemplate extends CascadingUbiquitousStatementTemplate {
	public ConditionalStatementTemplate() {
		super( edu.cmu.cs.dennisc.alice.ast.ConditionalStatement.class, org.alice.ide.ast.NodeUtilities.createIncompleteConditionalStatement() );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes() {
		return new edu.cmu.cs.dennisc.alice.ast.AbstractType[] { edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE };
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.ConditionalStatement rv = org.alice.ide.ast.NodeUtilities.createIncompleteConditionalStatement();
		rv.booleanExpressionBodyPairs.get( 0 ).expression.setValue( expressions[ 0 ] );
		return rv;
	}
}
