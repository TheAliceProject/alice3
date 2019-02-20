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

package org.alice.ide.highlight;

import org.lgna.cheshire.simple.ScrollRenderer;
import org.lgna.cheshire.simple.SimpleScrollRenderer;
import org.lgna.croquet.resolvers.RuntimeResolver;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.LayerStencil;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.stencil.BasicPainter;
import org.lgna.stencil.Feature;
import org.lgna.stencil.GlowPainter;
import org.lgna.stencil.Hole;
import org.lgna.stencil.Note;
import org.lgna.stencil.Painter;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class HighlightStencil extends LayerStencil {
	private static final Color STENCIL_BASE_COLOR = new Color( 181, 140, 140, 150 );
	private static final Color STENCIL_LINE_COLOR = new Color( 92, 48, 24, 63 );

	private static final Painter GLOW_PAINTER = new GlowPainter( new Color( 255, 255, 0, 23 ) );
	private static final Painter OUTLINE_PAINTER = new BasicPainter( new BasicStroke( 2.0f ), Color.RED );

	private final AWTEventListener awtEventListener = new AWTEventListener() {
		@Override
		public void eventDispatched( AWTEvent event ) {
			MouseEvent e = (MouseEvent)event;
			if( e.getID() == MouseEvent.MOUSE_PRESSED ) {
				HighlightStencil.this.hide();
			}
		}
	};

	private final Paint stencilPaint = this.createStencilPaint();
	private final ScrollRenderer scrollRenderer = new SimpleScrollRenderer();
	private final Note note = new Note();

	public HighlightStencil( AbstractWindow<?> window, Integer layerId ) {
		super( window, layerId );
		this.note.setActive( true );
		this.internalAddComponent( this.note );
	}

	protected Paint createStencilPaint() {
		int width = 8;
		int height = 8;
		BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		g2.setColor( STENCIL_BASE_COLOR );
		g2.fillRect( 0, 0, width, height );
		g2.setColor( STENCIL_LINE_COLOR );
		g2.drawLine( 0, height, width, 0 );
		g2.fillRect( 0, 0, 1, 1 );
		g2.dispose();
		return new TexturePaint( image, new Rectangle( 0, 0, width, height ) );
	}

	@Override
	protected boolean contains( int x, int y, boolean superContains ) {
		if( superContains ) {
			Shape shape = this.getLocalBounds();
			Area area = new Area( shape );
			for( Feature feature : note.getFeatures() ) {
				Area featureAreaToSubtract = feature.getAreaToSubstractForContains( HighlightStencil.this );
				if( featureAreaToSubtract != null ) {
					area.subtract( featureAreaToSubtract );
					shape = area;
				}
			}
			if( shape.contains( x, y ) ) {
				return superContains;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jPanel ) {
		return new FlowLayout() {
			@Override
			public void layoutContainer( Container target ) {
				super.layoutContainer( target );
				note.setLocation( note.calculateLocation( HighlightStencil.this ) );
			}
		};
	}

	@Override
	protected void paintComponentPrologue( Graphics2D g2 ) {
		Shape prevClip = g2.getClip();
		Paint prevPaint = g2.getPaint();
		Stroke prevStroke = g2.getStroke();

		Shape shape = prevClip;
		Area area = new Area( shape );
		for( Feature feature : note.getFeatures() ) {
			Area featureAreaToSubtract = feature.getAreaToSubstractForPaint( HighlightStencil.this );
			if( featureAreaToSubtract != null ) {
				area.subtract( featureAreaToSubtract );
				shape = area;
			}
		}
		g2.setPaint( stencilPaint );
		g2.fill( shape );

		g2.setStroke( prevStroke );
		g2.setPaint( prevPaint );
	}

	@Override
	protected void paintComponentEpilogue( Graphics2D g2 ) {
		if( note.isActive() ) {
			for( Feature feature : note.getFeatures() ) {
				feature.paint( g2, HighlightStencil.this, note );
			}
		}
	}

	@Override
	protected void paintEpilogue( Graphics2D g2 ) {
		if( note.isActive() ) {
			for( Feature feature : note.getFeatures() ) {
				TrackableShape trackableShape = feature.getTrackableShape();
				if( trackableShape != null ) {
					if( trackableShape.isInView() ) {
						//pass
					} else {
						if( scrollRenderer != null ) {
							Shape repaintShape = scrollRenderer.renderScrollIndicators( g2, HighlightStencil.this, trackableShape );
							if( repaintShape != null ) {
								//todo: repaint?
							}
						}
					}
				}
			}
		}
	}

	private static final KeyStroke HIDE_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 );
	private ActionListener hideAction = new ActionListener() {
		@Override
		public void actionPerformed( ActionEvent e ) {
			hide();
		}
	};

	private void show() {
		this.registerKeyboardAction( this.hideAction, HIDE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.setStencilShowing( true );
		Toolkit.getDefaultToolkit().addAWTEventListener( this.awtEventListener, AWTEvent.MOUSE_EVENT_MASK );

	}

	private void hide() {
		Toolkit.getDefaultToolkit().removeAWTEventListener( this.awtEventListener );
		this.setStencilShowing( false );
		this.unregisterKeyboardAction( HIDE_KEY_STROKE );
	}

	public void hideIfNecessary() {
		if( this.isStencilShowing() ) {
			this.hide();
		}
	}

	protected void show( RuntimeResolver<TrackableShape> trackableShapeResolverA, RuntimeResolver<TrackableShape> trackableShapeResolverB, final String noteText ) {
		this.note.removeAllFeatures();

		Painter painter;
		if( trackableShapeResolverB != null ) {
			painter = OUTLINE_PAINTER;
		} else {
			painter = GLOW_PAINTER;
		}
		Hole hole = new Hole( trackableShapeResolverA, Feature.ConnectionPreference.NORTH_SOUTH, painter ) {
			@Override
			protected boolean isPathRenderingDesired() {
				return ( noteText != null ) && ( noteText.length() > 0 );
			}
		};
		this.note.addFeature( hole );
		this.note.setText( noteText );

		if( trackableShapeResolverB != null ) {
			Hole holeB = new Hole( trackableShapeResolverB, Feature.ConnectionPreference.NORTH_SOUTH, painter ) {
				@Override
				protected boolean isPathRenderingDesired() {
					return noteText.length() > 0;
				}
			};
			this.note.addFeature( holeB );
		}
		this.note.reset();
		this.show();
	}
}
