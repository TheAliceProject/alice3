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
public abstract class Stencil extends edu.cmu.cs.dennisc.croquet.JComponent<javax.swing.JPanel> {
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

	private ScrollingRequiredRenderer scrollingRequiredRenderer;
	private MenuPolicy menuPolicy;
	private javax.swing.JLayeredPane layeredPane;
	public Stencil( MenuPolicy menuPolicy, ScrollingRequiredRenderer scrollingRequiredRenderer, javax.swing.JLayeredPane layeredPane ) {
		this.menuPolicy = menuPolicy;
		this.scrollingRequiredRenderer = scrollingRequiredRenderer;
		this.layeredPane = layeredPane;
	}
	
	public boolean isAbovePopupMenus() {
		return true;
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
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener( this.awtEventListener, java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
		this.getAwtComponent().setBounds( this.layeredPane.getBounds() );
		this.layeredPane.addComponentListener( this.componentListener );
		edu.cmu.cs.dennisc.stencil.RepaintManagerUtilities.pushStencil( this.getAwtComponent() );
	}
	@Override
	protected void handleUndisplayable() {
		assert edu.cmu.cs.dennisc.stencil.RepaintManagerUtilities.popStencil() == this.getAwtComponent();
		this.layeredPane.removeComponentListener( this.componentListener );
		java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener( this.awtEventListener );
		super.handleUndisplayable();
	}
	private void redispatchMouseEvent(java.awt.event.MouseEvent eSrc) {
		Page page = this.getCurrentPage();
		
		boolean isInterceptable = page == null || page.isEventInterceptable( eSrc );
		java.awt.Point pSrc = eSrc.getPoint();
		java.awt.Component componentSrc = eSrc.getComponent();
		if( isInterceptable && eSrc.getComponent().contains( pSrc.x, pSrc.y ) ) {
			// pass
		} else {
			javax.swing.MenuSelectionManager menuSelectionManager = javax.swing.MenuSelectionManager.defaultManager();
			if( menuSelectionManager.getSelectedPath().length > 0 ) {
				java.awt.Component componentDst = menuSelectionManager.componentForPoint( componentSrc, pSrc );
				if( componentDst != null ) {
					java.awt.Point pDst = javax.swing.SwingUtilities.convertPoint( componentSrc, pSrc, componentDst );
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( pDst );
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( componentDst );
//					java.awt.event.MouseEvent eDst = new java.awt.event.MouseEvent(componentDst, eSrc.getID(), eSrc.getWhen(), eSrc.getModifiers() + eSrc.getModifiersEx(), pDst.x, pDst.y, eSrc.getClickCount(), eSrc.isPopupTrigger() ); 
					//componentDst.dispatchEvent( eDst );
					//menuSelectionManager.processMouseEvent( eDst );
					menuSelectionManager.processMouseEvent( eSrc );
				}
			} else {
				javax.swing.JFrame jFrame = edu.cmu.cs.dennisc.croquet.Application.getSingleton().getFrame().getAwtComponent();
				java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(jFrame.getContentPane(), pSrc.x, pSrc.y);
				if (component != null) {
					java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(componentSrc, pSrc, component);
					component.dispatchEvent(new java.awt.event.MouseEvent(component, eSrc.getID(), eSrc.getWhen(), eSrc.getModifiers() + eSrc.getModifiersEx(), pComponent.x, pComponent.y, eSrc.getClickCount(), eSrc.isPopupTrigger()));
				}
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
//	private java.util.Stack< Boolean > isEventInterceptEnabledStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
//	public void pushEventInterceptEnabled( boolean isEventInterceptEnabled ) {
//		this.isEventInterceptEnabledStack.push( this.isEventInterceptEnabled );
//		this.setEventInterceptEnabled( isEventInterceptEnabled );
//	}
//	public boolean popEventInterceptEnabled() {
//		boolean rv = this.isEventInterceptEnabled;
//		boolean nextValue = this.isEventInterceptEnabledStack.pop();
//		this.setEventInterceptEnabled( nextValue );
//		return rv;
//	}
	
	protected abstract boolean isPaintingStencilEnabled();
	
	private java.awt.event.AWTEventListener awtEventListener = new java.awt.event.AWTEventListener() {
		public void eventDispatched(java.awt.AWTEvent event) {
			java.awt.event.MouseEvent e = (java.awt.event.MouseEvent)event;
			e = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent(e.getComponent(), e, Stencil.this.getAwtComponent());
			Stencil.this.handleMouseMoved( e );
		}
	};
	protected abstract Page getCurrentPage();
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
		Page page = this.getCurrentPage();
		if( page != null ) {
			for( Note note : page.getNotes() ) {
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
	
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		class JStencil extends javax.swing.JPanel {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				java.awt.Paint prevPaint = g2.getPaint();
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );

				Page page = Stencil.this.getCurrentPage();
				if( Stencil.this.isPaintingStencilEnabled() ) {
					if( page != null ) {
						if( page.isStencilRenderingDesired() ) {
							java.awt.geom.Area area = new java.awt.geom.Area(g2.getClip());
							for( Note note : page.getNotes() ) {
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
											//System.err.println( "cannot find trackable shape for: " + feature );
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
					if( page != null ) {
						for( Note note : page.getNotes() ) {
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
				Page page = Stencil.this.getCurrentPage();
				if( Stencil.this.isPaintingStencilEnabled() ) {
					if( page != null ) {
						for( Note note : page.getNotes() ) {
							if( note.isActive() ) {
								for( Feature feature : note.getFeatures() ) {
									edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape = feature.getTrackableShape();
									if( trackableShape != null ) {
										if( trackableShape.isInView() ) {
											//pass
										} else {
											if( scrollingRequiredRenderer != null ) {
												java.awt.Shape repaintShape = scrollingRequiredRenderer.renderScrollIndicators( g2, Stencil.this, trackableShape );
												if( repaintShape != null ) {
													//todo: repaint?
//													g2.setColor( java.awt.Color.RED );
//													g2.fill( repaintShape );
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
				Page page = Stencil.this.getCurrentPage();
				if( page != null ) {
					java.awt.geom.Area area = new java.awt.geom.Area(new java.awt.Rectangle(0, 0, this.getWidth(), this.getHeight()));
					for( Note note : page.getNotes() ) {
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
									//System.err.println( "cannot find trackable shape for: " + feature );
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
	public abstract edu.cmu.cs.dennisc.croquet.Operation< ? > getNextOperation();
}
