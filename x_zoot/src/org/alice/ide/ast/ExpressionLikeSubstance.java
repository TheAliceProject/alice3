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
package org.alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionLikeSubstance extends PotentiallyDraggablePane {
	private static org.alice.ide.lookandfeel.ExpressionTypeBorderFactory borderFactory = null;
	private static org.alice.ide.lookandfeel.ExpressionTypeRenderer renderer = null;
	public static void setBorderFactory( org.alice.ide.lookandfeel.ExpressionTypeBorderFactory borderFactory ) {
		ExpressionLikeSubstance.borderFactory = borderFactory;
	}
	public static void setRenderer( org.alice.ide.lookandfeel.ExpressionTypeRenderer renderer ) {
		ExpressionLikeSubstance.renderer = renderer;
	}

//	private static final int INSET = 2;
//	private static final int DOCKING_BAY_INSET_LEFT = 8;
	public ExpressionLikeSubstance() {
		super( javax.swing.BoxLayout.LINE_AXIS );
		if( ExpressionLikeSubstance.borderFactory != null ) {
			//todo
			//this.setBorder( ExpressionLikeSubstance.borderFactory.createBorder( getExpressionType() ) );
			this.setBorder( ExpressionLikeSubstance.borderFactory.createBorder( null, this ) );
		} else {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		}
		//this.setBorder( javax.swing.BorderFactory.createMatteBorder( INSET+4, INSET+DOCKING_BAY_INSET_LEFT+4, INSET+4, INSET+4, edu.cmu.cs.dennisc.awt.ColorUtilities.GARISH_COLOR ) );
	}

	@Override
	protected org.alice.ide.lookandfeel.ExpressionTypeRenderer getRenderer() {
		return ExpressionLikeSubstance.renderer;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getContext() {
		return getExpressionType();
	}
	
	public abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType();
//	@Override
//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
//		java.awt.geom.RoundRectangle2D.Float shape = new java.awt.geom.RoundRectangle2D.Float( INSET+DOCKING_BAY_INSET_LEFT, INSET, (float)getWidth()-2*INSET-DOCKING_BAY_INSET_LEFT, (float)getHeight()-2*INSET, 8, 8 );
//		edu.cmu.cs.dennisc.alice.ast.AbstractType type = getExpressionType();
//		if( type != null ) {
//			assert type != edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE;
//		} else {
//			type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class );
//		}
//		edu.cmu.cs.dennisc.awt.BeveledShape rv = edu.cmu.cs.dennisc.alice.ui.BeveledShapeForType.createBeveledShapeFor( type, shape, DOCKING_BAY_INSET_LEFT, Math.min( getHeight()*0.5f, 16.0f ) );
//		return rv;
//	}

	//todo
	@Override
	protected boolean isActuallyPotentiallyActive() {
		return false;
	}
	//todo
	@Override
	protected boolean isActuallyPotentiallySelectable() {
		return false;
	}
}

