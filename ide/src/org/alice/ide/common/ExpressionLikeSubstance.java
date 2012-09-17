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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionLikeSubstance extends NodeLikeSubstance {
	private static final int INSET = 2;
	public static final int DOCKING_BAY_INSET_LEFT = 5;

	private final boolean isVoid;

	public ExpressionLikeSubstance( org.lgna.croquet.DragModel model, boolean isVoid ) {
		super( model );
		this.isVoid = isVoid;
	}

	public ExpressionLikeSubstance( org.lgna.croquet.DragModel model ) {
		this( model, false );
		;
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.AbstractButton jComponent ) {
		return new javax.swing.BoxLayout( jComponent, javax.swing.BoxLayout.LINE_AXIS );
	}

	protected boolean isExpressionTypeFeedbackDesired() {
		return org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState.getInstance().getValue() || isKnurlDesired();
	}

	protected static boolean isExpressionTypeFeedbackSurpressedBasedOnParentClass( org.lgna.project.ast.Expression e ) {
		if( e != null ) {
			org.lgna.project.ast.Node parent = e.getParent();
			if( parent != null ) {
				if( edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( parent.getClass(), org.lgna.project.ast.ArrayAccess.class, org.lgna.project.ast.ArrayLength.class ) ) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected int getInsetTop() {
		if( this.isVoid ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET;
		}
	}

	@Override
	protected int getDockInsetLeft() {
		if( this.isVoid || ( this.isExpressionTypeFeedbackDesired() == false ) ) {
			return 0;
		} else {
			return DOCKING_BAY_INSET_LEFT + 2;
		}
	}

	@Override
	protected int getInternalInsetLeft() {
		if( this.isVoid ) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	protected int getInsetBottom() {
		if( this.isVoid ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET;
		}
	}

	@Override
	protected int getInsetRight() {
		if( this.isVoid ) {
			return 0;
		} else {
			return ExpressionLikeSubstance.INSET;
		}
	}

	protected edu.cmu.cs.dennisc.java.awt.BeveledShape createBoundsShape( int x, int y, int width, int height ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = this.getExpressionType();
		if( type != null ) {
			//			assert type != org.lgna.project.ast.TypeDeclaredInJava.VOID_TYPE;
		} else {
			type = org.lgna.project.ast.JavaType.OBJECT_TYPE;
		}
		//		java.awt.geom.RoundRectangle2D.Float shape = new java.awt.geom.RoundRectangle2D.Float( INSET + ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT, INSET, (float)width - 2 * INSET - ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT, (float)height - 2 * INSET, 8, 8 );
		int left = this.getDockInsetLeft();
		int top = this.getInsetTop();
		int right = this.getInsetRight();
		int bottom = this.getInsetBottom();
		java.awt.geom.RoundRectangle2D.Float shape = new java.awt.geom.RoundRectangle2D.Float( left, top, width - left - right, height - top - bottom, 8, 8 );
		return BeveledShapeForType.createBeveledShapeFor( type, shape, left, Math.min( height * 0.5f, 12.0f ) );
	}

	@Override
	protected java.awt.Shape createShape( int x, int y, int width, int height ) {
		if( this.isVoid || ( this.isExpressionTypeFeedbackDesired() == false ) ) {
			return null;
		} else {
			return this.createBoundsShape( x, y, width, height ).getBaseShape();
		}
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isExpressionTypeFeedbackDesired() ) {
			edu.cmu.cs.dennisc.java.awt.BeveledShape beveledShape = createBoundsShape( x, y, width, height );
			beveledShape.fill( g2 );
		}
	}

	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		if( this.isVoid || ( this.isExpressionTypeFeedbackDesired() == false ) ) {
			//pass
		} else {
			//edu.cmu.cs.dennisc.awt.BevelState bevelState = this.getBevelState();
			edu.cmu.cs.dennisc.java.awt.BevelState bevelState = edu.cmu.cs.dennisc.java.awt.BevelState.FLUSH;
			edu.cmu.cs.dennisc.java.awt.BeveledShape beveledShape = createBoundsShape( x, y, width, height );
			g2.setPaint( this.getBackgroundPaint( x, y, width, height ) );
			beveledShape.paint( g2, bevelState, 3.0f, 1.0f, 1.0f );
		}
	}

	public abstract org.lgna.project.ast.AbstractType<?, ?, ?> getExpressionType();
	//	@Override
	//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
	//		java.awt.geom.RoundRectangle2D.Float shape = new java.awt.geom.RoundRectangle2D.Float( INSET+DOCKING_BAY_INSET_LEFT, INSET, (float)getWidth()-2*INSET-DOCKING_BAY_INSET_LEFT, (float)getHeight()-2*INSET, 8, 8 );
	//		org.lgna.project.ast.AbstractType type = getExpressionType();
	//		if( type != null ) {
	//			assert type != org.lgna.project.ast.TypeDeclaredInJava.VOID_TYPE;
	//		} else {
	//			type = org.lgna.project.ast.TypeDeclaredInJava.OBJECT_TYPE;
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
