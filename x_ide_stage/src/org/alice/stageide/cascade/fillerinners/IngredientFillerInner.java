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

/**
 * @author Dennis Cosgrove
 */
public abstract class IngredientFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	public IngredientFillerInner( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super( type, edu.cmu.cs.dennisc.alice.ast.FieldAccess.class );
	}
	protected abstract Class<?>[] getClses( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender );
	@Override
	public final void addFillIns( cascade.Blank blank ) {
		final org.alice.apis.stage.LifeStage lifeStage = org.alice.apis.stage.LifeStage.ADULT;
		for( final org.alice.apis.stage.Gender gender : org.alice.apis.stage.Gender.values() ) {
			blank.addFillIn( new cascade.MenuFillIn( gender.name() ) {
				@Override
				protected void addChildrenToBlank( cascade.Blank blank ) {
					for( final Class<?> cls : getClses( lifeStage, gender ) ) {
						blank.addFillIn( new cascade.MenuFillIn( cls.getSimpleName() ) {
							@Override
							protected void addChildrenToBlank( cascade.Blank blank ) {
								edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
						 		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
						 			if( field.isPublicAccess() && field.isStatic() && field.isFinal() ) {
						 				IngredientFillerInner.this.addExpressionFillIn( blank, new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
						 			}
						 		}
							}
							@Override
							protected javax.swing.JComponent createMenuProxy() {
								return new org.alice.ide.common.TypeComponent( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ) );
							}
						} );
					}
				}
			} );
		}
	}
}
