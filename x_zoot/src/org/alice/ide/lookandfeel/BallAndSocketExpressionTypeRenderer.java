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
package org.alice.ide.lookandfeel;


/**
 * @author Dennis Cosgrove
 */
public class BallAndSocketExpressionTypeRenderer implements ExpressionTypeRenderer {
	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape( edu.cmu.cs.dennisc.alice.ast.AbstractType type, int x, int y, int width, int height ) {
		final int INSET = BallAndSocketBorderFactory.INSET;
		java.awt.geom.RoundRectangle2D.Float shape = new java.awt.geom.RoundRectangle2D.Float( INSET+BallAndSocketBorderFactory.DOCKING_BAY_INSET_LEFT, INSET, (float)width-2*INSET-BallAndSocketBorderFactory.DOCKING_BAY_INSET_LEFT, (float)height-2*INSET, 8, 8 );
		if( type != null ) {
			assert type != edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE;
		} else {
			type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class );
		}
		edu.cmu.cs.dennisc.awt.BeveledShape rv = edu.cmu.cs.dennisc.alice.ui.BeveledShapeForType.createBeveledShapeFor( type, shape, BallAndSocketBorderFactory.DOCKING_BAY_INSET_LEFT, Math.min( height*0.5f, 16.0f ) );
		return rv;
	}

	public void fillBounds( edu.cmu.cs.dennisc.alice.ast.AbstractType context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.awt.BeveledShape beveledShape = createBoundsShape( context, x, y, width, height );
		beveledShape.fill( g2 );
	}
	private edu.cmu.cs.dennisc.awt.BevelState getBevelState(boolean isActive, boolean isPressed, boolean isSelected) {
		if (isActive) {
			if (isPressed) {
				return edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
			} else {
				return edu.cmu.cs.dennisc.awt.BevelState.RAISED;
			}
		} else {
			return edu.cmu.cs.dennisc.awt.BevelState.FLUSH;
		}
	}

	public void paintPrologue( edu.cmu.cs.dennisc.alice.ast.AbstractType context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height, boolean isActive, boolean isPressed, boolean isSelected ) {
		g2.setPaint( c.getBackground() );
		edu.cmu.cs.dennisc.awt.BevelState bevelState;
		if( c instanceof org.alice.ide.ast.EmptyExpressionPane ) {
			bevelState = edu.cmu.cs.dennisc.awt.BevelState.SUNKEN;
		} else {
			bevelState = this.getBevelState(isActive, isPressed, isSelected);
		}
		edu.cmu.cs.dennisc.awt.BeveledShape beveledShape = createBoundsShape( context, x, y, width, height );
		beveledShape.paint( g2, bevelState, 3.0f, 1.0f, 1.0f );
	}
	public void paintEpilogue( edu.cmu.cs.dennisc.alice.ast.AbstractType context, java.awt.Component c, java.awt.Graphics2D g2, int x, int y, int width, int height, boolean isActive, boolean isPressed, boolean isSelected ) {
	}
}
