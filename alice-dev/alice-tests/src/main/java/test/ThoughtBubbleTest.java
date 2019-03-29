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
package test;

/**
 * @author Dennis Cosgrove
 */
public class ThoughtBubbleTest {
	private static java.awt.geom.Ellipse2D createScaledOffsetRectangle2D( java.awt.geom.Rectangle2D.Double r, double offsetPortionX, double offsetPortionY, double scaleX, double scaleY ) {
		return new java.awt.geom.Ellipse2D.Double( r.x + ( offsetPortionX * r.width ), r.y + ( offsetPortionY * r.height ), r.width * scaleX, r.height * scaleY );
	}

	private static java.awt.geom.Area createRotatedAboutCenterArea( java.awt.geom.Ellipse2D e, double theta ) {
		return new java.awt.geom.Area( e ).createTransformedArea( java.awt.geom.AffineTransform.getRotateInstance( theta, e.getCenterX(), e.getCenterY() ) );
	}

	private static java.awt.Shape createBubbleAround( java.awt.geom.Rectangle2D.Double r ) {
		java.awt.geom.Ellipse2D rightEllipse = createScaledOffsetRectangle2D( r, 0.45, -0.05, 0.85, 1.25 );
		java.awt.geom.Ellipse2D topEllipse = createScaledOffsetRectangle2D( r, 0.1, -0.4, 0.8, 1.2 );
		java.awt.geom.Ellipse2D leftEllipse = createScaledOffsetRectangle2D( r, -0.2, -0.1, 0.7, 0.9 );
		java.awt.geom.Ellipse2D bottomLeftEllipse = createScaledOffsetRectangle2D( r, -0.1, 0.15, 0.7, 1.2 );
		java.awt.geom.Ellipse2D bottomRightEllipse = createScaledOffsetRectangle2D( r, 0.15, 0.45, 0.8, 1.0 );

		java.awt.geom.AffineTransform m = java.awt.geom.AffineTransform.getRotateInstance( 0.1, topEllipse.getCenterX(), topEllipse.getCenterY() );

		java.awt.geom.Area rv = new java.awt.geom.Area( r );
		rv.add( createRotatedAboutCenterArea( rightEllipse, -0.1 ) );
		rv.add( createRotatedAboutCenterArea( topEllipse, -0.05 ) );
		rv.add( new java.awt.geom.Area( leftEllipse ) );
		rv.add( createRotatedAboutCenterArea( bottomLeftEllipse, -0.1 ) );
		rv.add( createRotatedAboutCenterArea( bottomRightEllipse, 0.1 ) );
		return rv;
	}

	public static void main( String[] args ) {
		TestCroquet testCroquet = new TestCroquet();
		testCroquet.initialize( args );

		class JBubble extends javax.swing.JComponent {
			java.awt.Point p0 = new java.awt.Point( 100, 100 );
			java.awt.Point p1;

			@Override
			public void paint( java.awt.Graphics g ) {
				super.paint( g );

				if( ( p0 != null ) && ( p1 != null ) ) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

					java.awt.geom.Rectangle2D.Double bounds = new java.awt.geom.Rectangle2D.Double( p0.x, p0.y, p1.x - p0.x, p1.y - p0.y );
					g2.setPaint( java.awt.Color.RED );
					g2.fill( bounds );
					g2.setPaint( java.awt.Color.BLACK );
					g2.draw( createBubbleAround( bounds ) );
				}
			}
		}

		final JBubble a = new JBubble();

		a.addMouseListener( new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed( java.awt.event.MouseEvent e ) {
				a.p0 = e.getPoint();
				a.p1 = null;
				a.repaint();
			}
		} );
		a.addMouseMotionListener( new java.awt.event.MouseMotionAdapter() {
			@Override
			public void mouseDragged( java.awt.event.MouseEvent e ) {
				a.p1 = e.getPoint();
				a.repaint();
			}
		} );

		a.p1 = new java.awt.Point( a.p0.x + 600, a.p0.y + 40 );

		org.lgna.croquet.DocumentFrame documentFrame = testCroquet.getDocumentFrame();
		org.lgna.croquet.views.Frame frame = documentFrame.getFrame();
		frame.getContentPane().getAwtComponent().add( a );
		frame.setSize( 800, 450 );
		frame.setVisible( true );
	}
}
