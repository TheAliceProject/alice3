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
package org.lgna.stencil;

/**
 * @author Dennis Cosgrove
 */
public class Note extends org.lgna.croquet.views.SwingComponentView<javax.swing.JPanel> {
	private static java.awt.Color BASE_COLOR = new java.awt.Color( 255, 255, 100 );
	private static java.awt.Color HIGHLIGHT_COLOR = new java.awt.Color( 255, 255, 180 );

	private final java.util.List<Feature> features = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final javax.swing.text.html.HTMLDocument document = new javax.swing.text.html.HTMLDocument();

	public String getText() {
		try {
			return this.document.getText( 0, this.document.getLength() );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	public void setText( String text ) {
		try {
			this.document.replace( 0, this.document.getLength(), text, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( text, ble );
		}
	}

	public void addFeature( Feature feature ) {
		if( feature != null ) {
			this.features.add( feature );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
		}
	}

	public void removeAllFeatures() {
		this.features.clear();
	}

	public java.util.List<Feature> getFeatures() {
		return this.features;
	}

	public java.awt.Point calculateLocation( org.lgna.croquet.views.AwtContainerView<?> container ) {
		java.awt.Point rv;
		if( this.features.size() > 0 ) {
			Feature feature = this.features.get( 0 );
			rv = feature.calculateNoteLocation( container, this );
		} else {
			rv = new java.awt.Point( ( container.getWidth() - this.getWidth() ) / 2, 320 );
		}
		return rv;
	}

	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JEditorPane editorPane = new javax.swing.JEditorPane() {
			@Override
			public void updateUI() {
				this.setUI( new javax.swing.plaf.basic.BasicEditorPaneUI() );
			}

			@Override
			public boolean contains( int x, int y ) {
				return false;
			}
		};
		final int X_BORDER_PAD = 16;
		final int Y_BORDER_PAD = 12;
		int top = Y_BORDER_PAD;
		int bottom = Y_BORDER_PAD;
		int left = X_BORDER_PAD;
		int right = X_BORDER_PAD;
		editorPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( top, left, bottom, right ) );
		editorPane.setOpaque( false );
		editorPane.setContentType( "text/html" );
		editorPane.setEditable( false );
		editorPane.setDocument( this.document );

		javax.swing.JPanel rv = new javax.swing.JPanel() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

				int w = this.getWidth();
				int h = this.getHeight();

				java.awt.Shape shape = new java.awt.geom.Rectangle2D.Float( 0, 0, w - 4, h - 4 );

				int x1 = w - 20;
				int y1 = h - 20;
				java.awt.Paint paint = new java.awt.GradientPaint( x1, y1, HIGHLIGHT_COLOR, x1 - 160, y1 - 160, BASE_COLOR );
				g2.setPaint( paint );

				g2.fill( shape );

				if( Note.this.isActive() ) {
					g2.setPaint( java.awt.Color.GRAY );
					java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
					pathShadow.moveTo( w - 4, 0 );
					pathShadow.lineTo( w, h );
					pathShadow.lineTo( 0, h - 4 );
					pathShadow.lineTo( w - 4, h - 4 );
					pathShadow.closePath();
					g2.fill( pathShadow );
				}
				super.paintComponent( g );
			}

			@Override
			public void paint( java.awt.Graphics g ) {
				if( Note.this.getText().length() > 0 ) {
					super.paint( g );
				}
			}

			@Override
			public java.awt.Dimension getPreferredSize() {
				java.awt.Dimension rv = super.getPreferredSize();
				rv.width = 240;
				rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( rv, rv.width );
				return rv;
			}
		};
		rv.setLayout( new java.awt.BorderLayout() );
		rv.add( editorPane, java.awt.BorderLayout.PAGE_START );
		rv.setOpaque( false );
		return rv;
	}

	private javax.swing.event.MouseInputListener mouseInputListener = new javax.swing.event.MouseInputListener() {
		private java.awt.event.MouseEvent ePressed;
		private java.awt.Point ptPressed;

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			this.ePressed = javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, e.getComponent().getParent() );
			this.ptPressed = Note.this.getAwtComponent().getLocation();
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			java.awt.event.MouseEvent eDragged = javax.swing.SwingUtilities.convertMouseEvent( e.getComponent(), e, e.getComponent().getParent() );
			int xDelta = eDragged.getX() - this.ePressed.getX();
			int yDelta = eDragged.getY() - this.ePressed.getY();
			int x = ptPressed.x + xDelta;
			int y = ptPressed.y + yDelta;
			Note.this.getAwtComponent().setLocation( x, y );
			Note.this.getAwtComponent().getParent().repaint();
		}

		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}
	};

	private boolean isActive = true;

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive( boolean isActive ) {
		if( this.isActive != isActive ) {
			this.isActive = isActive;
			for( Feature feature : this.features ) {
				feature.updateTrackableShapeIfNecessary();
			}
			org.lgna.croquet.views.AwtContainerView<?> container = this.getParent();
			if( container != null ) {
				container.repaint();
			}
		}
	}

	private void bind() {
		for( Feature feature : this.features ) {
			feature.bind();
		}
	}

	private void unbind() {
		for( Feature feature : this.features ) {
			feature.unbind();
		}
	}

	private java.awt.event.HierarchyListener hierarchyListener = new java.awt.event.HierarchyListener() {
		@Override
		public void hierarchyChanged( java.awt.event.HierarchyEvent e ) {
			if( ( e.getChangeFlags() & java.awt.event.HierarchyEvent.SHOWING_CHANGED ) != 0 ) {
				Note.this.handleShowingChanged( e.getChanged().isShowing() );
			}
		}
	};

	private void handleShowingChanged( boolean isShowing ) {
		this.reset();
		this.revalidateAndRepaint();
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.addHierarchyListener( this.hierarchyListener );
		this.addMouseListener( this.mouseInputListener );
		this.addMouseMotionListener( this.mouseInputListener );
		this.bind();
	}

	@Override
	protected void handleUndisplayable() {
		this.unbind();
		this.removeMouseMotionListener( this.mouseInputListener );
		this.removeMouseListener( this.mouseInputListener );
		this.removeHierarchyListener( this.hierarchyListener );
		super.handleUndisplayable();
	}

	public void reset() {
		unbind();
		bind();
	}
}
