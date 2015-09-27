/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class StatementLikeSubstance extends NodeLikeSubstance {
	private final Class<? extends org.lgna.project.ast.Statement> statementCls;
	private final int axis;

	protected static Class<? extends org.lgna.project.ast.Statement> getClassFor( org.lgna.project.ast.Statement statement ) {
		if( statement != null ) {
			return statement.getClass();
		} else {
			return org.lgna.project.ast.Statement.class;
		}
	}

	public StatementLikeSubstance( org.lgna.croquet.DragModel model, Class<? extends org.lgna.project.ast.Statement> statementCls, int axis ) {
		super( model );
		this.statementCls = statementCls;
		this.axis = axis;
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jComponent ) {
		return new javax.swing.BoxLayout( jComponent, this.axis );
	}

	public Class<? extends org.lgna.project.ast.Statement> getStatementCls() {
		return this.statementCls;
	}

	private static final int INSET = 1;

	@Override
	protected int getInsetTop() {
		return StatementLikeSubstance.INSET;
	}

	@Override
	protected int getDockInsetLeft() {
		return 1;
	}

	@Override
	protected int getInternalInsetLeft() {
		return StatementLikeSubstance.INSET + 2;
	}

	@Override
	protected int getInsetBottom() {
		return StatementLikeSubstance.INSET + 2;
	}

	@Override
	protected int getInsetRight() {
		return StatementLikeSubstance.INSET + 4;
	}

	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return org.alice.ide.ThemeUtilities.getActiveTheme().getPaintFor( this.statementCls, x, y, width, height );
	}

	@Override
	protected java.awt.geom.RoundRectangle2D.Float createShape( int x, int y, int width, int height ) {
		return new java.awt.geom.RoundRectangle2D.Float( x, y, width - 1, height - 1, 8, 8 );
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fill( this.createShape( x, y, width, height ) );
	}

	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		this.fillBounds( g2, x, y, width, height );
	}

	//	@Override
	//	protected edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape() {
	//		return new edu.cmu.cs.dennisc.awt.BeveledRoundRectangle( new java.awt.geom.RoundRectangle2D.Float( 1.5f, 1.5f, (float)getWidth()-3, (float)getHeight()-3, 8.0f, 8.0f ) );
	//	}

	////	//todo: remove
	//	@Override
	//	protected void paintBorder( java.awt.Graphics g ) {
	//		super.paintBorder( g );
	//		if( this.isKnurlDesired() ) {
	//			this.getBorder().paintBorder( this, g, 0, 0, getWidth(), getHeight() );
	//		}
	////		super.paintBorder( g );
	////		if( this.isKnurlDesired() ) {
	////			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
	////			edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, 3, 2, 8, getHeight()-2 );
	////		}
	//	}
}
