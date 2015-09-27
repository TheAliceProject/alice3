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

package org.lgna.cheshire.simple;

/**
 * @author Dennis Cosgrove
 */
public class SimpleScrollRenderer implements ScrollRenderer {

	private java.awt.geom.Area drawScrollFeedback( java.awt.Graphics2D g2, java.awt.Rectangle rect ) {
		g2.setColor( java.awt.Color.BLACK );
		g2.drawRect( rect.x, rect.y, rect.width, rect.height );
		g2.setColor( java.awt.Color.YELLOW );
		g2.drawRect( rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2 );
		g2.setColor( java.awt.Color.BLACK );
		g2.drawRect( rect.x - 2, rect.y - 2, rect.width + 4, rect.height + 4 );
		return new java.awt.geom.Area( new java.awt.Rectangle( rect.x - 2, rect.y - 2, rect.width + 4 + 1, rect.height + 4 + 1 ) );
	}

	@Override
	public java.awt.Shape renderScrollIndicators( java.awt.Graphics2D g2, org.lgna.croquet.views.ScreenElement root, org.lgna.croquet.views.TrackableShape trackableShape ) {
		org.lgna.croquet.views.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
		if( scrollPane != null ) {
			org.lgna.croquet.views.AwtComponentView<?> view = scrollPane.getViewportView();

			java.awt.Shape shape = trackableShape.getShape( view, null );
			if( shape != null ) {
				java.awt.geom.Area repaintShape = new java.awt.geom.Area();

				java.awt.geom.Rectangle2D bounds = shape.getBounds2D();
				double portion = bounds.getCenterY() / view.getHeight();

				javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
				java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle( scrollBar.getParent(), scrollBar.getBounds(), root.getAwtComponent() );

				StringBuilder sb = new StringBuilder();
				sb.append( "You must scroll " );

				javax.swing.JViewport viewport = scrollPane.getAwtComponent().getViewport();
				java.awt.Rectangle viewBounds = viewport.getViewRect();
				if( bounds.getY() < viewBounds.y ) {
					sb.append( "up" );
				} else if( bounds.getY() > ( viewBounds.y + viewBounds.height ) ) {
					sb.append( "down" );
				} else {
					//pass
				}
				sb.append( " first." );

				String s = sb.toString();

				java.awt.FontMetrics fm = g2.getFontMetrics();
				java.awt.Rectangle textBounds = fm.getStringBounds( s, g2 ).getBounds();

				textBounds.x += rect.x + rect.width + 12;
				textBounds.y += rect.y + ( rect.height / 2 );

				edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( textBounds, new java.awt.Insets( 4, 4, 4, 4 ) );
				g2.setColor( java.awt.Color.WHITE );
				g2.fill( textBounds );

				repaintShape.add( drawScrollFeedback( g2, textBounds ) );

				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, s, textBounds );

				repaintShape.add( drawScrollFeedback( g2, rect ) );

				int y = rect.y + (int)( rect.height * portion );

				float xSize = 24.0f;
				float yHalfSize = xSize * 0.5f;
				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
				path.moveTo( 0, 0 );
				path.lineTo( -xSize, yHalfSize );
				path.lineTo( -xSize, -yHalfSize );
				path.closePath();

				repaintShape.add( new java.awt.geom.Area( new java.awt.geom.Rectangle2D.Float( rect.x - 2 - xSize, y - yHalfSize, xSize + 1, yHalfSize + yHalfSize + 1 ) ) );
				repaintShape.add( new java.awt.geom.Area( new java.awt.geom.Rectangle2D.Float( rect.x + rect.width + 2, y - yHalfSize, xSize + 1, yHalfSize + yHalfSize + 1 ) ) );

				java.awt.geom.AffineTransform m = g2.getTransform();
				g2.translate( rect.x - 2, y );
				g2.setColor( java.awt.Color.YELLOW );
				g2.fill( path );
				g2.setColor( java.awt.Color.BLACK );
				g2.draw( path );
				g2.translate( rect.width + 4, 0 );
				g2.rotate( Math.PI );
				g2.setColor( java.awt.Color.YELLOW );
				g2.fill( path );
				g2.setColor( java.awt.Color.BLACK );
				g2.draw( path );
				g2.setTransform( m );

				return repaintShape;
			}
		}
		return null;
	}
}
