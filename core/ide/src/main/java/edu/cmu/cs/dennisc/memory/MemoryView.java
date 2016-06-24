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
package edu.cmu.cs.dennisc.memory;

public class MemoryView extends javax.swing.JComponent {
	private static final long K = 1024;
	//private static final long M = K*K;
	private java.awt.event.MouseListener mouseAdapter = new java.awt.event.MouseListener() {
		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			MemoryView.this.repaint();
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	@Override
	public void addNotify() {
		super.addNotify();
		this.addMouseListener( this.mouseAdapter );
	}

	@Override
	public void removeNotify() {
		this.removeMouseListener( this.mouseAdapter );
		super.removeNotify();
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent(g);
		java.lang.management.MemoryMXBean memory = java.lang.management.ManagementFactory.getMemoryMXBean();
		java.lang.management.MemoryUsage heapUsage = memory.getHeapMemoryUsage();
		//java.lang.management.MemoryUsage nonHeapUsage = memory.getNonHeapMemoryUsage();
		long maxKB = heapUsage.getMax() / K;
		long usedKB = heapUsage.getUsed() / K;
		double portion = usedKB / (double)maxKB;

		//portion = 0.1;

		int w = this.getWidth();
		int h = this.getHeight();

		int xValue = (int)( w * portion );
		int xWarning = ( 2 * w ) / 5;
		int xDanger = ( 4 * w ) / 5;

		java.awt.Color fineColor = new java.awt.Color( 0, 191, 0 );
		java.awt.Color warningColor = new java.awt.Color( 255, 255, 0 );
		java.awt.Color dangerColor = new java.awt.Color( 255, 0, 0 );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( fineColor );
		g2.fillRect( 0, 0, xValue, h );
		if( xValue > xWarning ) {
			g2.setPaint( new java.awt.GradientPaint( xWarning, 0, fineColor, xDanger, 0, warningColor ) );
			g2.fillRect( xWarning, 0, xValue - xWarning, h );
			if( xValue > xDanger ) {
				g2.setPaint( new java.awt.GradientPaint( xDanger, 0, warningColor, w, 0, dangerColor ) );
				g2.fillRect( xDanger, 0, xValue - xDanger, h );
			}
		}
		g2.setPaint( java.awt.Color.DARK_GRAY );
		g2.fillRect( xValue, 0, w - xValue, h );

		int percent = (int)( portion * 100 );
		String text = "in use: " + percent + "%";
		java.awt.geom.Rectangle2D bounds = g2.getFontMetrics().getStringBounds( text, g2 );
		if( bounds.getWidth() < xValue ) {
			g.drawString( text, xValue - (int)bounds.getWidth() - 4, h - ( (int)bounds.getHeight() / 2 ) );
		}
	}
}
