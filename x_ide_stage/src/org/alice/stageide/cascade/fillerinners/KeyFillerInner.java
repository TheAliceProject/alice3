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
package org.alice.stageide.cascade.fillerinners;

import org.alice.apis.moveandturn.Key;

/**
 * @author Dennis Cosgrove
 */
public class KeyFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public KeyFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Key.class ), edu.cmu.cs.dennisc.alice.ast.FieldAccess.class );
	}
	@Override
	public void addFillIns( cascade.Blank blank ) {
		Key[] lettersAM = {
				Key.A,
				Key.B,
				Key.C,
				Key.D,
				Key.E,
				Key.F,
				Key.G,
				Key.H,
				Key.I,
				Key.J,
				Key.K,
				Key.L,
				Key.M,
		};
		Key[] lettersNZ = {
				Key.N,
				Key.O,
				Key.P,
				Key.Q,
				Key.R,
				Key.S,
				Key.T,
				Key.U,
				Key.V,
				Key.W,
				Key.X,
				Key.Y,
				Key.Z,
		};
		Key[] digits = {
				Key.DIGIT_0,
				Key.DIGIT_1,
				Key.DIGIT_2,
				Key.DIGIT_3,
				Key.DIGIT_4,
				Key.DIGIT_5,
				Key.DIGIT_6,
				Key.DIGIT_7,
				Key.DIGIT_8,
				Key.DIGIT_9,
		};

		Key[] arrows = {
				Key.LEFT,
				Key.UP,
				Key.RIGHT,
				Key.DOWN,
		};
		
		final edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getType();
		String[] names = { "letters (A-M)", "letters (N-Z)", "digits (0-9)", "arrows" };
		Key[][] keySets = new Key[][] { lettersAM, lettersNZ, digits, arrows };
		final int N = names.length;
		for( int i=0; i<N; i++ ) {
			final Key[] keys = keySets[ i ];
			blank.addFillIn( new cascade.MenuFillIn( names[ i ] ) {
				@Override
				protected void addChildrenToBlank( cascade.Blank blank ) {
					for( Key key : keys ) {
			 			edu.cmu.cs.dennisc.alice.ast.AbstractField field = type.getDeclaredField( type, key.name() );
			 			assert field.isPublicAccess() && field.isStatic() && field.isFinal();
			 			KeyFillerInner.this.addExpressionFillIn( blank, new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
			 		}
				}
			} );
		}
		blank.addSeparator();
		blank.addFillIn( new org.alice.stageide.cascade.customfillin.CustomKeyFillIn() );
	}
}
