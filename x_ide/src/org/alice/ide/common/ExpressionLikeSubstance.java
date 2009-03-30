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
public abstract class ExpressionLikeSubstance extends NodeLikeSubstance {
	private static final int INSET = 2;
	private static final int DOCKING_BAY_INSET_LEFT = 8;

	public ExpressionLikeSubstance() {
		this.setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.LINE_AXIS ) );
	}
	
	private boolean isVoid() {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = getExpressionType();
		return type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE;
	}
	
	@Override
	protected int getInsetTop() {
		if( this.isVoid() ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET + 2;
		}
	}
	
	@Override
	protected int getDockInsetLeft() {
		if( this.isVoid() ) {
			return 0;
		} else {
			return DOCKING_BAY_INSET_LEFT + 2;
		}
	}
	@Override
	protected int getInternalInsetLeft() {
		if( this.isVoid() ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET + 2;
		}
	}
	
	@Override
	protected int getInsetBottom() {
		if( this.isVoid() ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET + 2;
		}
	}
	@Override
	protected int getInsetRight() {
		if( this.isVoid() ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET;
		}
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
	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.awt.BeveledShape beveledShape = createBoundsShape( x, y, width, height );
		beveledShape.fill( g2 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isVoid() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.awt.BevelState bevelState = this.getBevelState();
			edu.cmu.cs.dennisc.awt.BeveledShape beveledShape = createBoundsShape( x, y, width, height );
			beveledShape.paint( g2, bevelState, 3.0f, 1.0f, 1.0f );
		}
	}
	
	@Override
	protected boolean isKnurlDesired() {
		return false;
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
