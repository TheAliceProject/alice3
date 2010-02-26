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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class IncompleteStringConcatenationFillIn extends IncompleteExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.StringConcatenation > {
	@Override
	protected void addChildren() {
		this.addChild( new ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.OBJECT_TYPE ) );
		this.addChild( new ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.OBJECT_TYPE ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.StringConcatenation getValue() {
		edu.cmu.cs.dennisc.alice.ast.StringConcatenation rv  = new edu.cmu.cs.dennisc.alice.ast.StringConcatenation();
		rv.leftOperand.setValue( (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getValue() );
		rv.rightOperand.setValue( (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 1 ).getSelectedFillIn().getValue() );
		return rv;
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.StringConcatenation getTransientValue() {
		edu.cmu.cs.dennisc.alice.ast.StringConcatenation rv  = new edu.cmu.cs.dennisc.alice.ast.StringConcatenation();
		rv.leftOperand.setValue( (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getTransientValue() );
		rv.rightOperand.setValue( (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 1 ).getSelectedFillIn().getTransientValue() );
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.StringConcatenation createIncomplete() {
		return org.alice.ide.ast.NodeUtilities.createIncompleteStringConcatenation();
	}
}
