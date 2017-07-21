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
package org.alice.ide.capture.views;

/**
 * @author Dennis Cosgrove
 */
public class JZoomView extends javax.swing.JComponent {
	private static final java.awt.Dimension SIZE = new java.awt.Dimension( 192, 192 );
	private java.awt.event.MouseEvent e;

	public JZoomView() {
		this.setBorder( javax.swing.BorderFactory.createMatteBorder( 4, 4, 4, 4, java.awt.Color.WHITE ) );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		return SIZE;
	}

	public java.awt.event.MouseEvent getMouseEvent() {
		return this.e;
	}

	private void printSubComponent( java.awt.Graphics2D g2, java.awt.Component awtComponent ) {
		java.awt.geom.AffineTransform m = g2.getTransform();
		g2.scale( 4.0, 4.0 );
		g2.translate( awtComponent.getX(), awtComponent.getY() );
		//todo: map point?
		g2.translate( -e.getX(), -e.getY() );
		int xCenter = SIZE.width / 2;
		int yCenter = SIZE.height / 2;
		g2.translate( xCenter * 0.25, yCenter * 0.25 );
		final boolean IS_PRINT_GOOD_TO_GO_GL = false;
		if( IS_PRINT_GOOD_TO_GO_GL ) {
			awtComponent.print( g2 );
		} else {
			awtComponent.paint( g2 );
		}
		g2.setTransform( m );
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		if( e != null ) {
			java.awt.Component c = e.getComponent();

			javax.swing.JFrame jFrame = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.getRootJFrame( c );
			javax.swing.JRootPane jRootPane = jFrame.getRootPane();
			javax.swing.JMenuBar jMenuBar = jRootPane.getJMenuBar();
			java.awt.Container jContentPane = jRootPane.getContentPane();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			int xCenter = SIZE.width / 2;
			int yCenter = SIZE.height / 2;
			printSubComponent( g2, jMenuBar );
			printSubComponent( g2, jContentPane );

			g2.setXORMode( java.awt.Color.WHITE );
			g2.setColor( java.awt.Color.BLACK );
			g2.fillRect( 0, yCenter, c.getWidth(), 1 );
			g2.fillRect( xCenter, 0, 1, c.getHeight() );
			g2.setPaintMode();
		} else {
			super.paintComponent( g );
		}
	}

	public void handleMouseMovedOrDragged( java.awt.event.MouseEvent e ) {
		this.e = e;
		this.repaint();
	}
}
