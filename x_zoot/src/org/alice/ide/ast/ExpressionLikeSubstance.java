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
public abstract class ExpressionLikeSubstance extends org.alice.ide.AbstractControl {
	private static final int INSET = 2;
	private static final int DOCKING_BAY_INSET_LEFT = 8;

	public ExpressionLikeSubstance() {
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
	}
	@Override
	protected int getInsetTop() {
		return ExpressionLikeSubstance.INSET + 2;
	}
	@Override
	protected int getInsetLeft() {
		return ExpressionLikeSubstance.INSET + 4 + DOCKING_BAY_INSET_LEFT;
	}
	@Override
	protected int getInsetBottom() {
		return ExpressionLikeSubstance.INSET + 2;
	}
	@Override
	protected int getInsetRight() {
		return ExpressionLikeSubstance.INSET;
	}

	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape( int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getExpressionType();
		if( type != null ) {
//			assert type != edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE;
		} else {
			type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class );
		}
//		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE ) {
//			return null;
//		} else {
			java.awt.geom.RoundRectangle2D.Float shape = new java.awt.geom.RoundRectangle2D.Float( INSET + ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT, INSET, (float)width - 2 * INSET - ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT, (float)height - 2 * INSET, 8, 8 );
			return edu.cmu.cs.dennisc.alice.ui.BeveledShapeForType.createBeveledShapeFor( type, shape, ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT, Math.min( height * 0.5f, 16.0f ) );
//		}
	}
	protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
		if( this.isActive() ) {
			if( this.isPressed() ) {
				return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
			} else {
				return edu.cmu.cs.dennisc.awt.BevelState.RAISED;
			}
		} else {
			return edu.cmu.cs.dennisc.awt.BevelState.FLUSH;
		}
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.awt.BeveledShape beveledShape = createBoundsShape( x, y, width, height );
		beveledShape.fill( g2 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.awt.BevelState bevelState = this.getBevelState();
		edu.cmu.cs.dennisc.awt.BeveledShape beveledShape = createBoundsShape( x, y, width, height );
		beveledShape.paint( g2, bevelState, 3.0f, 1.0f, 1.0f );
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
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
	//	@Override
	//	protected boolean isActuallyPotentiallyActive() {
	//		return false;
	//	}
	//	//todo
	//	@Override
	//	protected boolean isActuallyPotentiallySelectable() {
	//		return false;
	//	}
}
