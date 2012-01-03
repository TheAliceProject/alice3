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
package org.lgna.stencil;

/**
 * @author Dennis Cosgrove
 */
public abstract class LayeredPaneComponent extends org.lgna.croquet.components.JComponent<javax.swing.JPanel> {
	private final java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener() {
		public void componentResized( java.awt.event.ComponentEvent e ) {
			LayeredPaneComponent.this.getAwtComponent().setBounds( e.getComponent().getBounds() );
			LayeredPaneComponent.this.revalidateAndRepaint();
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}
	};

	private final javax.swing.JLayeredPane layeredPane;
	private final MenuPolicy menuPolicy;
	public LayeredPaneComponent( javax.swing.JLayeredPane layeredPane, MenuPolicy menuPolicy ) {
		this.layeredPane = layeredPane;
		this.menuPolicy = menuPolicy;
	}
	public MenuPolicy getMenuPolicy() {
		return this.menuPolicy;
	}
	public void addToLayeredPane() {
		this.layeredPane.add( this.getAwtComponent(), null );
		this.layeredPane.setLayer( this.getAwtComponent(), this.menuPolicy.getStencilLayer() );
	}
	public void removeFromLayeredPane() {
		this.layeredPane.remove( this.getAwtComponent() );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getAwtComponent().setBounds( this.layeredPane.getBounds() );
		this.layeredPane.addComponentListener( this.componentListener );
		edu.cmu.cs.dennisc.stencil.RepaintManagerUtilities.pushStencil( this.getAwtComponent() );
	}
	@Override
	protected void handleUndisplayable() {
		assert edu.cmu.cs.dennisc.stencil.RepaintManagerUtilities.popStencil() == this.getAwtComponent();
		this.layeredPane.removeComponentListener( this.componentListener );
		super.handleUndisplayable();
	}
	protected abstract void paintComponentPrologue( java.awt.Graphics2D g2 );
	protected abstract void paintComponentEpilogue( java.awt.Graphics2D g2 );
	protected abstract void paintEpilogue( java.awt.Graphics2D g2 );
	protected abstract boolean contains( int x, int y, boolean superContains );
	@Override
	protected final javax.swing.JPanel createAwtComponent() {
		class JStencil extends javax.swing.JPanel {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				java.awt.Paint prevPaint = g2.getPaint();
				Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				try {
					LayeredPaneComponent.this.paintComponentPrologue( g2 );
					super.paintComponent( g2 );
					LayeredPaneComponent.this.paintComponentEpilogue( g2 );
				} finally {
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
					g2.setPaint( prevPaint );
				}
			}
			
			@Override
			public void paint(java.awt.Graphics g) {
				super.paint(g);
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				LayeredPaneComponent.this.paintEpilogue( g2 );
			}

			@Override
			public boolean contains(int x, int y) {
				return LayeredPaneComponent.this.contains( x, y, super.contains( x, y ) );
			}

		}
		final JStencil rv = new JStencil();
		rv.setLayout(new java.awt.BorderLayout());
		rv.setOpaque(false);
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( rv, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		return rv;
	}
}
