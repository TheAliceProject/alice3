/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
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
	public final void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		org.alice.apis.stage.LifeStage[] lifeStages = { org.alice.apis.stage.LifeStage.ADULT, org.alice.apis.stage.LifeStage.CHILD }; 
		for( final org.alice.apis.stage.LifeStage lifeStage : lifeStages ) {
			for( final org.alice.apis.stage.Gender gender : org.alice.apis.stage.Gender.values() ) {
				blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( gender.name().toLowerCase() + " " + lifeStage.name().toLowerCase() ) {
					@Override
					protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
						for( final Class<?> cls : getClses( lifeStage, gender ) ) {
							blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( cls.getSimpleName() ) {
								@Override
								protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
									edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
							 		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
							 			if( field.isPublicAccess() && field.isStatic() && field.isFinal() ) {
							 				IngredientFillerInner.this.addExpressionFillIn( blank, new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
							 			}
							 		}
								}
								@Override
								protected javax.swing.JComponent createMenuProxy() {
									return new org.alice.ide.common.TypeComponent( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ) ).getJComponent();
								}
							} );
						}
					}
				} );
			}
		}
	}
}
