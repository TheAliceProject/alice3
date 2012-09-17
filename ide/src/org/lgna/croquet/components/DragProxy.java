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
package org.lgna.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public class DragProxy extends Proxy {
	private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
		public void keyPressed( java.awt.event.KeyEvent e ) {
		}

		public void keyReleased( java.awt.event.KeyEvent e ) {
			if( e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE ) {
				DragProxy.this.getDragComponent().handleCancel( e );
			}
		}

		public void keyTyped( java.awt.event.KeyEvent e ) {
		}
	};
	private boolean isAlphaDesiredWhenOverDropReceptor;

	public DragProxy( DragComponent dragComponent, boolean isAlphaDesiredWhenOverDropReceptor ) {
		super( dragComponent );
		this.isAlphaDesiredWhenOverDropReceptor = isAlphaDesiredWhenOverDropReceptor;
	}

	private final int DROP_SHADOW_SIZE = 6;

	@Override
	public java.awt.Dimension getProxySize() {
		java.awt.Dimension rv = super.getProxySize();
		rv.width += DROP_SHADOW_SIZE;
		rv.height += DROP_SHADOW_SIZE;
		return rv;
	}

	@Override
	protected float getAlpha() {
		if( this.isAlphaDesiredWhenOverDropReceptor && this.isOverDropAcceptor() ) {
			return 0.5f;
		} else {
			return 1.0f;
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.addKeyListener( this.keyAdapter );
		this.requestFocus();
	}

	@Override
	public void removeNotify() {
		this.transferFocus();
		this.removeKeyListener( this.keyAdapter );
		super.removeNotify();
	}

	@Override
	protected void paintProxy( java.awt.Graphics2D g2 ) {
		java.awt.Paint prevPaint = g2.getPaint();
		g2.setPaint( new java.awt.Color( 0, 0, 0, 64 ) );
		//todo?
		g2.translate( DROP_SHADOW_SIZE, DROP_SHADOW_SIZE );
		fillBounds( g2 );
		g2.translate( -DROP_SHADOW_SIZE, -DROP_SHADOW_SIZE );
		g2.setPaint( prevPaint );
		this.getSubject().getAwtComponent().print( g2 );

		//		if( isOverDragAccepter ) {
		//			//pass
		//		} else {
		//			g2.setPaint( new java.awt.Color( 127, 127, 127, 127 ) );
		//			this.createBoundsShape().fill( g2 );
		//		}
		if( this.isCopyDesired() ) {
			g2.setPaint( PaintUtilities.getCopyTexturePaint() );
			fillBounds( g2 );
		}
	}
}
