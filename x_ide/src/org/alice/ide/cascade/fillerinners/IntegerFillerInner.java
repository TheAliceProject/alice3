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
package org.alice.ide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public class IntegerFillerInner extends AbstractNumberFillerInner {
	public IntegerFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.IntegerLiteral.class );
	}
	@Override
	public void addFillIns( cascade.Blank blank ) {
		this.addExpressionFillIn( blank, 0 );
		this.addExpressionFillIn( blank, 1 );
		this.addExpressionFillIn( blank, 2 );
		this.addExpressionFillIn( blank, 3 );
		this.addExpressionFillIn( blank, 4 );
		this.addExpressionFillIn( blank, 5 );
		this.addExpressionFillIn( blank, 6 );
		this.addExpressionFillIn( blank, 7 );
		this.addExpressionFillIn( blank, 8 );
		this.addExpressionFillIn( blank, 9 );
		blank.addSeparator();
		blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomIntegerFillIn() );
		blank.addSeparator();
//		self._addArithmeticFillIns( blank, self._type, self._type )
	}
}
