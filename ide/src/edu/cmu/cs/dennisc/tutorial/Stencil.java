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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/* package-private */abstract class Stencil extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JPanel> {
	/*package-private*/ static final java.awt.Color STENCIL_BASE_COLOR =  new java.awt.Color( 181, 140, 140, 150 );
	/*package-private*/ static final java.awt.Color STENCIL_LINE_COLOR =  new java.awt.Color( 92, 48, 24, 63 );
	private static java.awt.Paint stencilPaint = null;
	private static java.awt.Paint getStencilPaint() {
		if( Stencil.stencilPaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
			g2.setColor( STENCIL_BASE_COLOR );
			g2.fillRect( 0, 0, width, height );
			g2.setColor( STENCIL_LINE_COLOR );
			g2.drawLine( 0, height, width, 0 );
			g2.fillRect( 0, 0, 1, 1 );
			g2.dispose();
			Stencil.stencilPaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return Stencil.stencilPaint;
	}

	protected abstract Step getCurrentStep();
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		class JStencil extends javax.swing.JPanel {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

				Step step = Stencil.this.getCurrentStep();
				if( step != null ) {
					java.awt.geom.Area area = new java.awt.geom.Area(g2.getClip());
					for( Note note : step.getNotes() ) {
						if( note.isActive() ) {
							for( Feature feature : note.getFeatures() ) {
								java.awt.geom.Area featureArea = feature.getAreaToSubstractForPaint( Stencil.this );
								if( featureArea != null ) {
									area.subtract( featureArea );
								}
							}
						}
					}
					g2.setPaint(getStencilPaint());
					g2.fill(area);
				}
				super.paintComponent(g);
				if( step != null ) {
					for( Note note : step.getNotes() ) {
						if( note.isActive() ) {
							for( Feature feature : note.getFeatures() ) {
								feature.paint( g2, Stencil.this, note );
							}
						}
					}
				}
			}

			@Override
			public boolean contains(int x, int y) {
				Step step = Stencil.this.getCurrentStep();
				if( step != null ) {
					java.awt.geom.Area area = new java.awt.geom.Area(new java.awt.Rectangle(0, 0, this.getWidth(), this.getHeight()));
					for( Note note : step.getNotes() ) {
						if( note.isActive() ) {
							for( Feature feature : note.getFeatures() ) {
								java.awt.geom.Area featureArea = feature.getAreaToSubstractForContains( Stencil.this );
								if( featureArea != null ) {
									area.subtract( featureArea );
								}
							}
						}
					}
					return area.contains(x, y);
				} else {
					return super.contains( x, y );
				}
			}

			private void redispatch(java.awt.event.MouseEvent e) {
				java.awt.Point p = e.getPoint();
				if (this.contains(p.x, p.y)) {
					// pass
				} else {
					java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtWindow().getLayeredPane(), p.x, p.y);
					if (component != null) {
						java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(this, p, component);
						component.dispatchEvent(new java.awt.event.MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger()));
					}
				}
			}
		}
		final JStencil rv = new JStencil();
		rv.addMouseListener(new java.awt.event.MouseListener() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}

			public void mouseEntered(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}

			public void mouseExited(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}

			public void mousePressed(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}

			public void mouseReleased(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}
		});
		rv.addMouseMotionListener(new java.awt.event.MouseMotionListener() {
			public void mouseMoved(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}

			public void mouseDragged(java.awt.event.MouseEvent e) {
				rv.redispatch(e);
			}
		});
		rv.setLayout(new java.awt.BorderLayout());
		rv.setOpaque(false);
		return rv;
	}
}
