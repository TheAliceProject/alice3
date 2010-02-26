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
public class MostlyDeterminedStringConcatenationFillIn extends edu.cmu.cs.dennisc.cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.StringConcatenation > {
	private edu.cmu.cs.dennisc.alice.ast.Expression leftOperand;
	public MostlyDeterminedStringConcatenationFillIn( edu.cmu.cs.dennisc.alice.ast.Expression leftOperand ) {
		this.leftOperand = leftOperand;
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	@Override
	protected void addChildren() {
		this.addChild( new ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.OBJECT_TYPE ) );
	}
	@Override
	protected final javax.swing.JComponent createMenuProxy() {
		return (javax.swing.JComponent)org.alice.ide.IDE.getSingleton().getPreviewFactory().createExpressionPane( org.alice.ide.ast.NodeUtilities.createIncompleteStringConcatenation( this.leftOperand ) );
	}
	@Override
	public final edu.cmu.cs.dennisc.alice.ast.StringConcatenation getValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression rightExpression = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getValue();
		return new edu.cmu.cs.dennisc.alice.ast.StringConcatenation( this.leftOperand, rightExpression );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.StringConcatenation getTransientValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression rightExpression = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getTransientValue();
		return new edu.cmu.cs.dennisc.alice.ast.StringConcatenation( this.leftOperand, rightExpression );
	}
	
}
