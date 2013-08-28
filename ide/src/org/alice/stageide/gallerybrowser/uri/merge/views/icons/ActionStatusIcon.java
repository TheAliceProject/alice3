/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.gallerybrowser.uri.merge.views.icons;

/**
 * @author Dennis Cosgrove
 */
public abstract class ActionStatusIcon extends org.lgna.croquet.icon.AbstractIcon {
	protected static enum ActionStatus {
		NO_ACTION,
		ADD,
		REPLACE,
		KEEP,
		ERROR
	}

	private static final int PAD = 2;
	private static java.awt.Dimension SIZE = org.lgna.croquet.icon.IconSize.SMALL.getSize();
	private static java.awt.Paint ADD_REPLACE_FILL_PAINT = new java.awt.Color( 0, 127, 0 );
	private static java.awt.Paint ADD_REPLACE_DRAW_PAINT = java.awt.Color.DARK_GRAY;
	private static java.awt.Shape ADD_SHAPE = createAddShape();

	private static java.awt.Font ERROR_FONT = new java.awt.Font( "Serif", java.awt.Font.BOLD, 20 );

	private static java.awt.Shape createAddShape() {
		int w = SIZE.width - PAD - PAD;
		int h = SIZE.height - PAD - PAD;
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( SIZE, w, h );
		java.awt.Rectangle horizontal = new java.awt.Rectangle( 0, ( h / 2 ) - 2, w, 4 );
		java.awt.Rectangle vertical = new java.awt.Rectangle( ( w / 2 ) - 2, 0, 4, h );
		return edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createUnion( horizontal, vertical );
	}

	private static final java.awt.Shape checkShape;
	private static final java.awt.Stroke innerStroke;
	private static final java.awt.Stroke outerStroke;

	static {//java.awt.Shape createCheckShape() {
		int unit = SIZE.height - PAD - PAD;

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
		checkShape = path;

		innerStroke = new java.awt.BasicStroke( unit * 0.2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		outerStroke = new java.awt.BasicStroke( unit * 0.25f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );

	}

	public ActionStatusIcon() {
		super( SIZE );
	}

	private void paintAdd( java.awt.Component c, java.awt.Graphics2D g2 ) {
		g2.setPaint( ADD_REPLACE_FILL_PAINT );
		g2.fill( ADD_SHAPE );
		g2.setPaint( ADD_REPLACE_DRAW_PAINT );
		g2.draw( ADD_SHAPE );
	}

	private void paintCheck( java.awt.Component c, java.awt.Graphics2D g2, java.awt.Paint fillPaint ) {
		java.awt.Stroke prevStroke = g2.getStroke();
		g2.setStroke( outerStroke );
		g2.setPaint( java.awt.Color.BLACK );
		g2.draw( checkShape );
		g2.setStroke( innerStroke );
		g2.setPaint( fillPaint );
		g2.draw( checkShape );
		g2.setStroke( prevStroke );
	}

	private void paintReplace( java.awt.Component c, java.awt.Graphics2D g2 ) {
		paintCheck( c, g2, ADD_REPLACE_FILL_PAINT );
	}

	private void paintKeep( java.awt.Component c, java.awt.Graphics2D g2 ) {
		g2.setPaint( java.awt.Color.LIGHT_GRAY );
		paintCheck( c, g2, java.awt.Color.LIGHT_GRAY );
	}

	private void paintError( java.awt.Component c, java.awt.Graphics2D g2, int width, int height ) {
		g2.setPaint( java.awt.Color.RED.darker() );
		g2.fillRect( 0, 0, width, height );
		java.awt.Font prevFont = g2.getFont();
		g2.setPaint( java.awt.Color.WHITE );
		g2.setFont( ERROR_FONT );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, "!", 0, 0, width, height );
		g2.setFont( prevFont );
	}

	protected abstract ActionStatus getActionStatus();

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2 ) {
		ActionStatus actionStatus = this.getActionStatus();
		if( ( actionStatus != null ) && ( actionStatus != ActionStatus.NO_ACTION ) ) {
			int xOffset = PAD;
			int yOffset = PAD;
			int width = this.getIconWidth() - PAD - PAD;
			int height = this.getIconHeight() - PAD - PAD;
			g2.translate( xOffset, yOffset );

			Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			//			g2.setPaint( java.awt.Color.RED );
			//			g2.fillRect( 0, 0, width, height );
			if( actionStatus == ActionStatus.ADD ) {
				paintAdd( c, g2 );
			} else if( actionStatus == ActionStatus.REPLACE ) {
				paintReplace( c, g2 );
			} else if( actionStatus == ActionStatus.KEEP ) {
				paintKeep( c, g2 );
			} else {
				paintError( c, g2, width, height );
			}
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
			g2.translate( -xOffset, -yOffset );
		}
	}
}
