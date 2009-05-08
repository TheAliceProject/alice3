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
public class StringConcatenationFillIn extends cascade.FillIn< edu.cmu.cs.dennisc.alice.ast.StringConcatenation > {
	@Override
	protected void addChildren() {
		edu.cmu.cs.dennisc.alice.ast.StringConcatenation instance = new edu.cmu.cs.dennisc.alice.ast.StringConcatenation();
		this.addChild( new ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) ) );
		this.addChild( new ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.StringConcatenation getValue() {
		edu.cmu.cs.dennisc.alice.ast.StringConcatenation rv  = new edu.cmu.cs.dennisc.alice.ast.StringConcatenation();
		rv.leftOperand.setValue( (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getValue() );
		rv.rightOperand.setValue( (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 1 ).getSelectedFillIn().getValue() );
		return rv;
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		org.alice.ide.common.Factory factory = org.alice.ide.IDE.getSingleton().getPreviewFactory();
		return (javax.swing.JComponent)factory.createExpressionPane( org.alice.ide.ast.NodeUtilities.createIncompleteStringConcatenation() );
	}
//	def addChildren( self ):
//		instance = alice.ast.StringConcatenation()
//		self.addChild( ExpressionPropertyBlank( instance.leftOperand ) )
//		self.addChild( ExpressionPropertyBlank( instance.rightOperand ) )
//	def getValue( self ):
//		rv = alice.ast.StringConcatenation()
//		rv.leftOperand.setValue( self.getChildren()[ 0 ].getSelectedFillIn().getValue() )
//		rv.rightOperand.setValue( self.getChildren()[ 1 ].getSelectedFillIn().getValue() )
//		return rv
//	def createMenuProxy( self ):
//		operandType = ecc.dennisc.alice.ast.getType( java.lang.Object )
//		leftValue = alice.ide.editors.code.EmptyExpression( operandType )
//		rightValue = alice.ide.editors.code.EmptyExpression( operandType )
//		return alice.ide.editors.code.ExpressionPane( alice.ast.StringConcatenation( leftValue, rightValue ) )
}
