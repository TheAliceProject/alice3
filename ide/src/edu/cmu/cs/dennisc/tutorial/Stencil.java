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

	private java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener() {
		public void componentResized( java.awt.event.ComponentEvent e ) {
			Stencil.this.getAwtComponent().setBounds( e.getComponent().getBounds() );
			Stencil.this.revalidateAndRepaint();
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}
	};

	private javax.swing.JLayeredPane layeredPane;
	public Stencil( javax.swing.JLayeredPane layeredPane ) {
		this.layeredPane = layeredPane;
	}
	
	public void addToLayeredPane() {
		this.layeredPane.add( this.getAwtComponent(), null );
		this.layeredPane.setLayer( this.getAwtComponent(), javax.swing.JLayeredPane.POPUP_LAYER - 1 );
	}
	public void removeFromLayeredPane() {
		this.layeredPane.remove( this.getAwtComponent() );
	}
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo(parent);
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener( this.awtEventListener, java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
		this.getAwtComponent().setBounds( this.layeredPane.getBounds() );
		this.layeredPane.addComponentListener( this.componentListener );
		edu.cmu.cs.dennisc.stencil.RepaintManagerUtilities.pushStencil( this.getAwtComponent() );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		assert edu.cmu.cs.dennisc.stencil.RepaintManagerUtilities.popStencil() == this.getAwtComponent();
		this.layeredPane.removeComponentListener( this.componentListener );
		java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener( this.awtEventListener );
		super.handleRemovedFrom(parent);
	}
	private static void redispatchMouseEvent(java.awt.event.MouseEvent e) {
		java.awt.Point p = e.getPoint();
		if( e.getComponent().contains( p.x, p.y ) ) {
			// pass
		} else {
			java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtComponent().getContentPane(), p.x, p.y);
			if (component != null) {
				java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(e.getComponent(), p, component);
				component.dispatchEvent(new java.awt.event.MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger()));
			}
		}
	}
	private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseEntered(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mousePressed(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
	};
	private java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseDragged(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
	};
	private java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
			java.awt.Point p = e.getPoint();
			java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtComponent().getContentPane(), p.x, p.y);
			if (component != null) {
				java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(e.getComponent(), p, component);
				component.dispatchEvent(new java.awt.event.MouseWheelEvent(component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation()));
			}
		}
	};
	private boolean isEventInterceptEnabled = false;
	public boolean isEventInterceptEnabled() {
		return this.isEventInterceptEnabled;
	}
	public void setEventInterceptEnabled( boolean isEventInterceptEnabled ) {
		if( this.isEventInterceptEnabled != isEventInterceptEnabled ) {
			if( this.isEventInterceptEnabled ) {
				this.getAwtComponent().removeMouseListener( this.mouseListener );
				this.getAwtComponent().removeMouseMotionListener( this.mouseMotionListener );
				this.getAwtComponent().removeMouseWheelListener( this.mouseWheelListener );
			}
			this.isEventInterceptEnabled = isEventInterceptEnabled;
			if( this.isEventInterceptEnabled ) {
				this.getAwtComponent().addMouseListener( this.mouseListener );
				this.getAwtComponent().addMouseMotionListener( this.mouseMotionListener );
				this.getAwtComponent().addMouseWheelListener( this.mouseWheelListener );
			}
		}
	}
	
	protected abstract boolean isPaintingStencilEnabled();
	
	private java.awt.event.AWTEventListener awtEventListener = new java.awt.event.AWTEventListener() {
		public void eventDispatched(java.awt.AWTEvent event) {
			java.awt.event.MouseEvent e = (java.awt.event.MouseEvent)event;
			e = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent(e.getComponent(), e, Stencil.this.getAwtComponent());
			Stencil.this.handleMouseMoved( e );
		}
	};
	protected abstract Step getCurrentStep();
	private Feature enteredFeature;
	public void setEnteredFeature(Feature enteredFeature) {
		if( this.enteredFeature != enteredFeature ) {
			if( this.enteredFeature != null ) {
				this.enteredFeature.setEntered( false );
				java.awt.Rectangle bounds = this.enteredFeature.getBoundsForRepaint( this );
				if( bounds != null ) {
					this.getAwtComponent().repaint( bounds );
				}
			}
			this.enteredFeature = enteredFeature;
			if( this.enteredFeature != null ) {
				this.enteredFeature.setEntered( true );
				java.awt.Rectangle bounds = this.enteredFeature.getBoundsForRepaint( this );
				if( bounds != null ) {
					this.getAwtComponent().repaint( bounds );
				}
			}
			//this.getAwtComponent().repaint();
		}
	}
	private void handleMouseMoved(java.awt.event.MouseEvent e) {
		Step step = Stencil.this.getCurrentStep();
		if( step != null ) {
			for( Note note : step.getNotes() ) {
				if( note.isActive() ) {
					for( Feature feature : note.getFeatures() ) {
						java.awt.Shape shape = feature.getShape( Stencil.this, null );
						if( shape != null ) {
							if( shape.contains( e.getX(), e.getY() ) ) {
								this.setEnteredFeature(feature);
								return;
							}
						}
					}
				}
			}
		}
		this.setEnteredFeature( null );
	}
	
	private static void drawScrollFeedback( java.awt.Graphics2D g2, java.awt.Rectangle rect ) {
		g2.setColor( java.awt.Color.BLACK );
		g2.drawRect( rect.x, rect.y, rect.width, rect.height );
		g2.setColor( java.awt.Color.YELLOW );
		g2.drawRect( rect.x-1, rect.y-1, rect.width+2, rect.height+2 );
		g2.setColor( java.awt.Color.BLACK );
		g2.drawRect( rect.x-2, rect.y-2, rect.width+4, rect.height+4 );
	}

	@Override
	protected javax.swing.JPanel createAwtComponent() {
		class JStencil extends javax.swing.JPanel {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				java.awt.Paint prevPaint = g2.getPaint();
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

				Step step = Stencil.this.getCurrentStep();
				if( Stencil.this.isPaintingStencilEnabled() ) {
					if( step != null ) {
						if( step.isStencilRenderingDesired() ) {
							java.awt.geom.Area area = new java.awt.geom.Area(g2.getClip());
							for( Note note : step.getNotes() ) {
								if( note.isActive() ) {
									for( Feature feature : note.getFeatures() ) {
										edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = feature.getTrackableShape();
										if( trackableShape != null ) {
											if( trackableShape.isInView() ) {
												java.awt.geom.Area featureArea = feature.getAreaToSubstractForPaint( Stencil.this );
												if( featureArea != null ) {
													area.subtract( featureArea );
												}
											} else {
												if( feature.isPotentiallyScrollable() ) {
													edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
													if( scrollPane != null ) {
														javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
														java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle(scrollBar.getParent(), scrollBar.getBounds(), Stencil.this.getAwtComponent() );
														area.subtract( new java.awt.geom.Area( rect ) );
													} else {
														System.err.println( "cannot find scroll pane for: " + feature );
													}
												}
											}
										} else {
											System.err.println( "cannot find trackable shape for: " + feature );
											feature.unbind();
											feature.bind();
										}
									}
								}
							}
							g2.setPaint(getStencilPaint());
							g2.fill(area);
						}
					}
				}
				super.paintComponent(g);
				if( Stencil.this.isPaintingStencilEnabled() ) {
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
				g2.setPaint( prevPaint );
			}
			
			@Override
			public void paint(java.awt.Graphics g) {
				super.paint(g);
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				Step step = Stencil.this.getCurrentStep();
				if( Stencil.this.isPaintingStencilEnabled() ) {
					if( step != null ) {
						for( Note note : step.getNotes() ) {
							if( note.isActive() ) {
								for( Feature feature : note.getFeatures() ) {
									edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = feature.getTrackableShape();
									if( trackableShape != null ) {
										if( trackableShape.isInView() ) {
											//pass
										} else {
											edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
											if( scrollPane != null ) {
												edu.cmu.cs.dennisc.croquet.Component<?> view = scrollPane.getViewportView();
												
												java.awt.Shape shape = trackableShape.getShape( view, null);
												if( shape != null ) {
													java.awt.geom.Rectangle2D bounds = shape.getBounds2D();
													double portion = bounds.getCenterY() / view.getHeight();
		
													javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
													java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle(scrollBar.getParent(), scrollBar.getBounds(), Stencil.this.getAwtComponent() );
		
													StringBuilder sb = new StringBuilder();
													sb.append( "You must SCROLL " );
													
													javax.swing.JViewport viewport = scrollPane.getAwtComponent().getViewport();
													java.awt.Rectangle viewBounds = viewport.getViewRect();
													if( bounds.getY() < viewBounds.y ) {
														sb.append( "UP" );
													} else if( bounds.getY() > ( viewBounds.y + viewBounds.height ) ) {
														sb.append( "DOWN" );
													} else {
														//pass
													}
													sb.append( " first." );
													
													String s = sb.toString();
		
													java.awt.FontMetrics fm = g2.getFontMetrics();
													java.awt.Rectangle textBounds = fm.getStringBounds( s, g2 ).getBounds();
													
													textBounds.x += rect.x + rect.width + 12;
													textBounds.y += rect.y + rect.height/2;
													
													
													edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( textBounds, new java.awt.Insets( 4,4,4,4 ) );
													g2.setColor( java.awt.Color.WHITE );
													g2.fill( textBounds );
													drawScrollFeedback( g2, textBounds );
													edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText(g2, s, textBounds );
		
													drawScrollFeedback( g2, rect );
													int y = rect.y + (int)( rect.height * portion );
													
													float xSize = 24.0f;
													float yHalfSize = xSize * 0.5f;
													java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
													path.moveTo( 0,0 );
													path.lineTo( -xSize, yHalfSize );
													path.lineTo( -xSize, -yHalfSize );
													path.closePath();
													
													java.awt.geom.AffineTransform m = g2.getTransform();
													g2.translate( rect.x-2, y );
													g2.setColor( java.awt.Color.YELLOW );
													g2.fill( path );
													g2.setColor( java.awt.Color.BLACK );
													g2.draw( path );
													g2.translate( rect.width+4, 0 );
													g2.rotate( Math.PI );
													g2.setColor( java.awt.Color.YELLOW );
													g2.fill( path );
													g2.setColor( java.awt.Color.BLACK );
													g2.draw( path );
													g2.setTransform( m );
												}
											}
										}
									}
								}
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
								edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = feature.getTrackableShape();
								if( trackableShape != null ) {
									if( trackableShape.isInView() ) {
										java.awt.geom.Area featureArea = feature.getAreaToSubstractForContains( Stencil.this );
										if( featureArea != null ) {
											area.subtract( featureArea );
										}
									} else {
										if( feature.isPotentiallyScrollable() ) {
											edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = trackableShape.getScrollPaneAncestor();
											if( scrollPane != null ) {
												javax.swing.JScrollBar scrollBar = scrollPane.getAwtComponent().getVerticalScrollBar();
												java.awt.Rectangle rect = javax.swing.SwingUtilities.convertRectangle(scrollBar.getParent(), scrollBar.getBounds(), Stencil.this.getAwtComponent() );
												area.subtract( new java.awt.geom.Area( rect ) );
											} else {
												System.err.println( "cannot find scroll pane for: " + feature );
											}
										}
									}
								} else {
									System.err.println( "cannot find trackable shape for: " + feature );
								}
							}
						}
					}
					return area.contains(x, y);
				} else {
					return super.contains( x, y );
				}
			}

		}
		final JStencil rv = new JStencil();
		rv.setLayout(new java.awt.BorderLayout());
		rv.setOpaque(false);
		edu.cmu.cs.dennisc.java.awt.font.FontUtilities.setFontToDerivedFont( rv, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD );
		return rv;
	}
}
