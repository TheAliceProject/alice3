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
package org.alice.ide.stencil;

/**
 * @author Dennis Cosgrove
 */
public abstract class CustomView extends org.lgna.croquet.views.SwingComponentView<javax.swing.JPanel> {
	protected abstract void paintComponentPrologue( java.awt.Graphics2D g2 );

	protected abstract void paintComponentEpilogue( java.awt.Graphics2D g2 );

	protected abstract void paintEpilogue( java.awt.Graphics2D g2 );

	protected abstract boolean contains( int x, int y, boolean superContains );

	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = edu.cmu.cs.dennisc.java.awt.GraphicsContext.getInstanceAndPushGraphics( g );
				gc.pushAndSetAntialiasing( true );
				gc.pushPaint();
				try {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					CustomView.this.paintComponentPrologue( g2 );
					super.paintComponent( g2 );
					CustomView.this.paintComponentEpilogue( g2 );
				} finally {
					gc.popAll();
				}
			}

			@Override
			public void paint( java.awt.Graphics g ) {
				super.paint( g );
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				CustomView.this.paintEpilogue( g2 );
			}

			@Override
			public boolean contains( int x, int y ) {
				return CustomView.this.contains( x, y, super.contains( x, y ) );
			}
		};
		rv.setOpaque( false );
		return rv;
	}
}
