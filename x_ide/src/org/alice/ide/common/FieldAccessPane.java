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

	public FieldAccessPane( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		this.fieldAccess = fieldAccess;
		boolean isExpressionDesired;
		if( this.fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.TypeExpression ) {
			isExpressionDesired = "java".equals( org.alice.ide.IDE.getSingleton().getLocale().getVariant() );
		} else {
			isExpressionDesired = true;
		}
		if( isExpressionDesired ) {
			this.add( new ExpressionPropertyPane( getIDE().getCodeFactory(), this.fieldAccess.expression, false ) );
			this.add( new zoot.ZLabel( ".") );
		}
		this.add( new org.alice.ide.common.NodeNameLabel( this.fieldAccess.field.getValue() ) );
		this.setBackground( getIDE().getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) );
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
