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

package org.alice.ide.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class KnurlDragComponent<M extends org.lgna.croquet.DragModel> extends org.lgna.croquet.views.DragComponent<M> {
	protected static final int KNURL_WIDTH = 8;

	public KnurlDragComponent( M model, boolean isAlphaDesiredWhenOverDropReceptor ) {
		super( model, isAlphaDesiredWhenOverDropReceptor );
	}

	protected java.awt.Paint getForegroundPaint( int x, int y, int width, int height ) {
		return this.getForegroundColor();
	}

	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return this.getBackgroundColor();
	}

	protected final boolean isKnurlDesired() {
		return this.getModel() != null;
	}

	protected abstract int getInsetTop();

	protected abstract int getDockInsetLeft();

	protected final int getKnurlInsetLeft() {
		if( this.isKnurlDesired() ) {
			return KNURL_WIDTH;
		} else {
			return 0;
		}
	}

	protected abstract int getInternalInsetLeft();

	protected final int getInsetLeft() {
		int rv = 0;
		rv += this.getDockInsetLeft();
		rv += this.getKnurlInsetLeft();
		rv += this.getInternalInsetLeft();
		return rv;
	}

	protected abstract int getInsetBottom();

	protected abstract int getInsetRight();

	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jComponent );

	@Override
	protected org.lgna.croquet.views.imp.JDragView createAwtComponent() {
		org.lgna.croquet.views.imp.JDragView rv = new org.lgna.croquet.views.imp.JDragView() {
			@Override
			public boolean isOpaque() {
				return false;
			}

			@Override
			public boolean contains( int x, int y ) {
				return KnurlDragComponent.this.contains( x, y, super.contains( x, y ) );
			}

			@Override
			public javax.swing.JToolTip createToolTip() {
				return KnurlDragComponent.this.createToolTip( super.createToolTip() );
			}

			@Override
			public java.awt.Point getToolTipLocation( java.awt.event.MouseEvent event ) {
				return KnurlDragComponent.this.getToolTipLocation( super.getToolTipLocation( event ), event );
			}

			@Override
			public java.awt.Dimension getMaximumSize() {
				if( KnurlDragComponent.this.isMaximumSizeClampedToPreferredSize() ) {
					return this.getPreferredSize();
				} else {
					return super.getMaximumSize();
				}
			}

			@Override
			public java.awt.Dimension getPreferredSize() {
				return KnurlDragComponent.this.getPreferredSize( super.getPreferredSize() );
			}

			@Override
			public void paint( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				int x = 0;
				int y = 0;
				int width = this.getWidth();
				int height = this.getHeight();

				java.awt.Paint prevPaint;
				prevPaint = g2.getPaint();
				try {
					g2.setPaint( KnurlDragComponent.this.getBackgroundPaint( x, y, width, height ) );
					KnurlDragComponent.this.paintPrologue( g2, x, y, width, height );
				} finally {
					g2.setPaint( prevPaint );
				}

				this.paintBorder( g2 );
				this.paintComponent( g2 );
				this.paintChildren( g );

				prevPaint = g2.getPaint();
				g2.setPaint( KnurlDragComponent.this.getForegroundPaint( x, y, width, height ) );
				try {
					KnurlDragComponent.this.paintEpilogue( g2, x, y, width, height );
				} finally {
					g2.setPaint( prevPaint );
				}
			}

		};

		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( this.getInsetTop(), this.getInsetLeft(), this.getInsetBottom(), this.getInsetRight() ) );
		if( this.isKnurlDesired() ) {
			rv.setCursor( java.awt.Cursor.getPredefinedCursor( java.awt.Cursor.HAND_CURSOR ) );
		}

		java.awt.LayoutManager layoutManager = this.createLayoutManager( rv );
		rv.setLayout( layoutManager );

		rv.setOpaque( false );
		rv.setBackground( null );

		rv.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		rv.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );

		return rv;
	}

	protected boolean contains( int x, int y, boolean jContains ) {
		return jContains;
	}

	protected java.awt.Dimension getPreferredSize( java.awt.Dimension size ) {
		return size;
	}

	protected javax.swing.JToolTip createToolTip( javax.swing.JToolTip jToolTip ) {
		return jToolTip;
	}

	protected java.awt.Point getToolTipLocation( java.awt.Point location, java.awt.event.MouseEvent event ) {
		return location;
	}

	private static final java.awt.Stroke ACTIVE_STROKE = new java.awt.BasicStroke( 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static final java.awt.Stroke PASSIVE_STROKE = new java.awt.BasicStroke( 1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static final java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 255 );
	private static final java.awt.Color SHADOW_COLOR = new java.awt.Color( 0, 0, 0 );

	protected java.awt.Paint getPassiveOutlinePaint() {
		return java.awt.Color.GRAY;
	}

	protected void paintOutline( java.awt.Graphics2D g2, java.awt.Shape shape ) {
		if( shape != null ) {
			java.awt.Stroke prevStroke = g2.getStroke();
			if( this.isActive() ) {
				edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.draw3DishShape( g2, shape, HIGHLIGHT_COLOR, SHADOW_COLOR, ACTIVE_STROKE );
			} else {
				g2.setPaint( this.getPassiveOutlinePaint() );
				g2.setStroke( PASSIVE_STROKE );
				g2.draw( shape );
			}
			g2.setStroke( prevStroke );
		}
	}

	protected abstract java.awt.Shape createShape( int x, int y, int width, int height );

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		java.awt.Shape shape = this.createShape( x, y, width, height );
		this.paintOutline( g2, shape );
		if( isKnurlDesired() ) {
			int grayscale;
			if( this.isActive() ) {
				grayscale = 0;
			} else {
				grayscale = 127;
			}
			g2.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( grayscale ) );
			edu.cmu.cs.dennisc.java.awt.KnurlUtilities.paintKnurl5( g2, x + this.getDockInsetLeft(), y + 2, KNURL_WIDTH, height - 5 );
		}
	}

	public void addComponent( org.lgna.croquet.views.AwtComponentView<?> component ) {
		this.internalAddComponent( component );
	}

	public void addComponent( org.lgna.croquet.views.AwtComponentView<?> component, Object constraints ) {
		this.internalAddComponent( component, constraints );
	}

	public void forgetAndRemoveComponent( org.lgna.croquet.views.AwtComponentView<?> component ) {
		this.internalForgetAndRemoveComponent( component );
	}

	public void removeAllComponents() {
		this.internalRemoveAllComponents();
	}

	public void forgetAndRemoveAllComponents() {
		this.internalForgetAndRemoveAllComponents();
	}

	protected void fillBounds( java.awt.Graphics2D g2 ) {
		this.fillBounds( g2, 0, 0, this.getWidth(), this.getHeight() );
	}
}
