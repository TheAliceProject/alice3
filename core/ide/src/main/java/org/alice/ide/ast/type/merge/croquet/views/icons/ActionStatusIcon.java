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
package org.alice.ide.ast.type.merge.croquet.views.icons;

import org.alice.ide.ast.type.merge.croquet.ActionStatus;

/**
 * @author Dennis Cosgrove
 */
public class ActionStatusIcon extends org.lgna.croquet.icon.AbstractIcon {
	private static final int PAD = 1;
	private static final java.awt.Dimension SIZE = org.lgna.croquet.icon.IconSize.SMALL.getSize();
	private static final java.awt.Paint ADD_REPLACE_FILL_PAINT = new java.awt.Color( 0, 127, 0 );
	private static final java.awt.Paint ADD_REPLACE_DRAW_PAINT = java.awt.Color.DARK_GRAY;
	private static final java.awt.Shape ADD_SHAPE;

	private static final java.awt.Paint ERROR_PAINT = org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities.ACTION_MUST_BE_TAKEN_COLOR;
	private static final java.awt.Font ERROR_FONT = edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( new java.awt.Font( "Serif", 0, SIZE.height - 2 ), edu.cmu.cs.dennisc.java.awt.font.TextWeight.EXTRABOLD );

	private static final java.awt.Shape CHECK_SHAPE;
	private static final java.awt.Stroke CHECK_INNER_STROKE;
	private static final java.awt.Stroke CHECK_OUTER_STROKE;

	private static final java.awt.geom.GeneralPath ERROR_SHAPE;

	static {
		int w = SIZE.width - PAD - PAD;
		int h = SIZE.height - PAD - PAD;

		int unit = h;

		double xA = 0.2;
		double xC = 0.8;
		double xB = xA + ( ( xC - xA ) * 0.3 );

		double yA = 0.45;
		double yB = xC;
		double yC = xA;

		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( xA * unit, yA * unit );
		path.lineTo( xB * unit, yB * unit );
		path.lineTo( xC * unit, yC * unit );
		CHECK_SHAPE = path;

		double v0 = 0.0;
		double v1 = 0.3 * unit;
		double v2 = 0.7 * unit;
		double v3 = unit;

		ERROR_SHAPE = new java.awt.geom.GeneralPath();
		ERROR_SHAPE.moveTo( v1, v0 );
		ERROR_SHAPE.lineTo( v2, v0 );
		ERROR_SHAPE.lineTo( v3, v1 );
		ERROR_SHAPE.lineTo( v3, v2 );
		ERROR_SHAPE.lineTo( v2, v3 );
		ERROR_SHAPE.lineTo( v1, v3 );
		ERROR_SHAPE.lineTo( v0, v2 );
		ERROR_SHAPE.lineTo( v0, v1 );
		ERROR_SHAPE.closePath();
		CHECK_INNER_STROKE = new java.awt.BasicStroke( unit * 0.2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		CHECK_OUTER_STROKE = new java.awt.BasicStroke( unit * 0.25f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );

		int x = 2;
		int y = 2;
		java.awt.Rectangle horizontal = new java.awt.Rectangle( x, ( h / 2 ) - 2, w - x - x, 4 );
		java.awt.Rectangle vertical = new java.awt.Rectangle( ( w / 2 ) - 2, y, 4, h - y - y );
		ADD_SHAPE = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createUnion( horizontal, vertical );

	}

	private org.alice.ide.ast.type.merge.croquet.MemberHub<?> memberHub;

	public ActionStatusIcon( org.alice.ide.ast.type.merge.croquet.MemberHub<?> memberHub ) {
		super( SIZE );
		this.memberHub = memberHub;
	}

	private void paintAdd( java.awt.Component c, java.awt.Graphics2D g2, javax.swing.ButtonModel buttonModel ) {
		g2.setPaint( ADD_REPLACE_FILL_PAINT );
		g2.fill( ADD_SHAPE );
		boolean isRollover = buttonModel != null ? buttonModel.isRollover() : false;
		g2.setPaint( isRollover ? java.awt.Color.WHITE : ADD_REPLACE_DRAW_PAINT );
		g2.draw( ADD_SHAPE );
	}

	private void paintCheck( java.awt.Component c, java.awt.Graphics2D g2, javax.swing.ButtonModel buttonModel, java.awt.Paint fillPaint ) {
		boolean isRollover = buttonModel != null ? buttonModel.isRollover() : false;
		java.awt.Stroke prevStroke = g2.getStroke();
		g2.setStroke( CHECK_OUTER_STROKE );
		g2.setPaint( isRollover ? java.awt.Color.WHITE : java.awt.Color.BLACK );
		g2.draw( CHECK_SHAPE );
		g2.setStroke( CHECK_INNER_STROKE );
		g2.setPaint( fillPaint );
		g2.draw( CHECK_SHAPE );
		g2.setStroke( prevStroke );
	}

	private void paintReplace( java.awt.Component c, java.awt.Graphics2D g2, javax.swing.ButtonModel buttonModel ) {
		paintCheck( c, g2, buttonModel, ADD_REPLACE_FILL_PAINT );
	}

	private void paintKeep( java.awt.Component c, java.awt.Graphics2D g2, javax.swing.ButtonModel buttonModel ) {
		paintCheck( c, g2, buttonModel, java.awt.Color.LIGHT_GRAY );
	}

	private void paintOmit( java.awt.Component c, java.awt.Graphics2D g2, javax.swing.ButtonModel buttonModel, int width, int height ) {
		boolean isPaintDesired = buttonModel != null ? buttonModel.isRollover() : true;
		if( isPaintDesired ) {
			float size = Math.min( width, height ) * 0.9f;

			float w = size;
			float h = size * 0.25f;
			float xC = -w * 0.5f;
			float yC = -h * 0.5f;
			java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( xC, yC, w, h, h, h );

			java.awt.geom.Area area0 = new java.awt.geom.Area( rr );
			java.awt.geom.Area area1 = new java.awt.geom.Area( rr );

			java.awt.geom.AffineTransform m0 = new java.awt.geom.AffineTransform();
			m0.rotate( Math.PI * 0.25 );
			area0.transform( m0 );

			java.awt.geom.AffineTransform m1 = new java.awt.geom.AffineTransform();
			m1.rotate( Math.PI * 0.75 );
			area1.transform( m1 );

			area0.add( area1 );

			java.awt.geom.AffineTransform m = new java.awt.geom.AffineTransform();
			m.translate( ( width / 2 ), ( height / 2 ) );
			area0.transform( m );

			g2.setPaint( new java.awt.Color( 127, 63, 63 ) );
			g2.fill( area0 );
		}
	}

	private void paintError( java.awt.Component c, java.awt.Graphics2D g2, javax.swing.ButtonModel buttonModel, int width, int height ) {
		boolean isRollover = buttonModel != null ? buttonModel.isRollover() : false;
		g2.setPaint( ERROR_PAINT );
		g2.fill( ERROR_SHAPE );
		g2.setPaint( isRollover ? java.awt.Color.WHITE : java.awt.Color.GRAY );
		g2.draw( ERROR_SHAPE );

		java.awt.Font prevFont = g2.getFont();
		g2.setPaint( java.awt.Color.WHITE );
		g2.setFont( ERROR_FONT );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "!", 0, 0, width, height );
		g2.setFont( prevFont );
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2 ) {
		ActionStatus actionStatus = this.memberHub.getActionStatus();
		javax.swing.ButtonModel buttonModel;
		if( c instanceof javax.swing.AbstractButton ) {
			javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
			buttonModel = button.getModel();
		} else {
			buttonModel = null;
		}

		int xOffset = PAD;
		int yOffset = PAD;
		int width = this.getIconWidth() - PAD - PAD;
		int height = this.getIconHeight() - PAD - PAD;
		g2.translate( xOffset, yOffset );

		Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		//			g2.setPaint( java.awt.Color.RED );
		//			g2.fillRect( 0, 0, width, height );
		if( ( actionStatus == ActionStatus.ADD_UNIQUE ) || ( actionStatus == ActionStatus.ADD_AND_RENAME ) ) {
			paintAdd( c, g2, buttonModel );
		} else if( actionStatus == ActionStatus.REPLACE_OVER_ORIGINAL ) {
			paintReplace( c, g2, buttonModel );
		} else if( ( actionStatus == ActionStatus.KEEP_IDENTICAL ) || ( actionStatus == ActionStatus.KEEP_UNIQUE ) || ( actionStatus == ActionStatus.KEEP_OVER_REPLACEMENT ) || ( actionStatus == ActionStatus.KEEP_OVER_DIFFERENT_SIGNATURE ) || ( actionStatus == ActionStatus.KEEP_AND_RENAME ) ) {
			paintKeep( c, g2, buttonModel );
		} else if( ( actionStatus == ActionStatus.RENAME_REQUIRED ) || ( actionStatus == ActionStatus.SELECTION_REQUIRED ) ) {
			paintError( c, g2, buttonModel, width, height );
		} else if( ( actionStatus == ActionStatus.OMIT ) || ( actionStatus == ActionStatus.OMIT_IN_FAVOR_OF_ORIGINAL ) || ( actionStatus == ActionStatus.DELETE_IN_FAVOR_OF_REPLACEMENT ) ) {
			paintOmit( c, g2, buttonModel, width, height );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( actionStatus );
		}
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
		g2.translate( -xOffset, -yOffset );
	}
}
