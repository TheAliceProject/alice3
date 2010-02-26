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
package org.alice.ide.common;


/**
 * @author Dennis Cosgrove
 */
public class FieldAccessPane extends org.alice.ide.common.ExpressionLikeSubstance {
	private edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess;

	public FieldAccessPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		this.fieldAccess = fieldAccess;
		boolean isExpressionDesired;
		if( this.fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
			isExpressionDesired = getIDE().isJava();
		} else {
			isExpressionDesired = true;
		}
		if( isExpressionDesired ) {
			this.add( factory.createExpressionPropertyPane( this.fieldAccess.expression, null ) );
//			if( getIDE().isJava() ) {
//				//pass
//			} else {
				this.add( edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "." ) );
//			}
		}
		org.alice.ide.common.DeclarationNameLabel nodeNameLabel = new org.alice.ide.common.DeclarationNameLabel( this.fieldAccess.field.getValue() );
		//nodeNameLabel.setFontToScaledFont( 1.2f );
		//nodeNameLabel.setFontToDerivedFont( zoot.font.ZTextWeight.BOLD );
		this.add( nodeNameLabel );
		this.setBackground( getIDE().getColorFor( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) );
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		if( this.fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
			return super.isExpressionTypeFeedbackDesired();
		} else {
			return true;
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		if( this.fieldAccess != null ) {
			return this.fieldAccess.field.getValue().getValueType();
		} else {
			return null;
		}
	}
}
