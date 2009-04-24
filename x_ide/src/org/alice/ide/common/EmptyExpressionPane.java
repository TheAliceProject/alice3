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

import org.alice.ide.ast.EmptyExpression;

/**
 * @author Dennis Cosgrove
 */
public class EmptyExpressionPane extends ExpressionLikeSubstance {
	private EmptyExpression emptyExpression;
	public EmptyExpressionPane( EmptyExpression emptyExpression ) {
		this.emptyExpression = emptyExpression;
		this.add( zoot.ZLabel.acquire( "???" ) );
		this.setBackground( new java.awt.Color( 127, 127, 191 ) );
	}
	public EmptyExpressionPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this( new EmptyExpression( type ) );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return this.emptyExpression.getType();
	}
	@Override
	protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
		return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
	}
	@Override
	protected boolean isExpressionTypeFeedbackDesired() {
		return true;
	}
}
