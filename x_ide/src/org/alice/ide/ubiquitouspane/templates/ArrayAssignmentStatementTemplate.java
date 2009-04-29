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
public abstract class ArrayAssignmentStatementTemplate extends CascadingUbiquitousStatementTemplate {
	public ArrayAssignmentStatementTemplate( edu.cmu.cs.dennisc.alice.ast.Statement incompleteStatement ) {
		super( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class, incompleteStatement );
	}
	
	protected abstract String getTransientName();
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getTransientComponentType();
	@Override
	protected String getLabelText() {
		return this.getTransientName() + "[\u2423]\u2190\u2423";
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes() {
		return new edu.cmu.cs.dennisc.alice.ast.AbstractType[] { edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, this.getTransientComponentType() };
	}
	
}
