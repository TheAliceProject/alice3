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
package edu.cmu.cs.dennisc.stencil;

/**
 * @author Dennis Cosgrove
 */
public class Stencil extends javax.swing.JPanel {
	private java.util.List< Hole > m_holes = new java.util.LinkedList< Hole >();
	private java.awt.Container m_container;
	public Stencil( java.awt.Container container ) {
		m_container = container;
		//m_container = this;
		setOpaque( false );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
		} );
		this.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
			public void mouseMoved( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
			public void mouseDragged( java.awt.event.MouseEvent e ) {
				Stencil.this.redispatch( e );
			}
		} );
	}

	private void redispatch( java.awt.event.MouseEvent e ) {
		java.awt.Point p = e.getPoint();
		if( this.contains( p.x, p.y ) ) {
			//pass
		} else {
			java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt( m_container, p.x, p.y );
			if( component != null ) {
				java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint( this, p, component );
				component.dispatchEvent( new java.awt.event.MouseEvent( component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger() ) );
			}
		}
	}
	public void addHole( Hole hole ) {
		synchronized( m_holes ) {
			m_holes.add( hole );
		}
	}
	public void removeHole( Hole hole ) {
		synchronized( m_holes ) {
			m_holes.remove( hole );
		}
	}
	public void clearHoles() {
		synchronized( m_holes ) {
			m_holes.clear();
		}
	}
	public Iterable< Hole > getHoles() {
		return m_holes;
	}

	@Override
	public boolean contains( int x, int y ) {
//todo?
//		if( someone else has hooked up listeners to the glass pane or changed the cursor ) {
//			return super.contains( x, y );
//		} else {
			java.awt.geom.Area area = calculateArea( this.getBounds() );
			return area.contains( x, y );
//		}
	}

	private java.awt.geom.Area calculateArea( java.awt.Rectangle bounds ) {
		java.awt.geom.Area rv = new java.awt.geom.Area( bounds );
		synchronized( m_holes ) {
			for( Hole hole : m_holes ) {
//				if( hole instanceof ComponentHole ) {
//					ComponentHole componentHole = (ComponentHole)hole;
//					java.awt.Component component = componentHole.getComponent(); 
//					java.awt.Rectangle holeBounds = component.getBounds();
//					bounds = javax.swing.SwingUtilities.convertRectangle( component, holeBounds, this );
//					rv.subtract( new java.awt.geom.Area( bounds ) );
//				}
				rv.subtract( hole.getArea( m_container ) );
			}
		}
		return rv;
	}
	
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		assert false;
		super.paintComponent( g );
		java.awt.geom.Area area = calculateArea( g.getClipBounds() );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		
//		java.awt.Color color = g.getColor();
//		g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, color.getAlpha() / 255.0f ) );
//		g2.setColor( this.getBackground() );
//
//		g2.setColor( this.getForeground() );
		g2.fill( area );
	}
}
