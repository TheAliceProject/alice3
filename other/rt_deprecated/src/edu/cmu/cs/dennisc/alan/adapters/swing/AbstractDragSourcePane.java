/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.alan.adapters.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDragSourcePane extends Pane {
	private edu.cmu.cs.dennisc.alan.DragSourceView m_dragSourceView;

	public AbstractDragSourcePane() {
		initialize();
	}
	public AbstractDragSourcePane( edu.cmu.cs.dennisc.alan.CompositeView compositeView ) {
		super( compositeView );
		initialize();
	}

	private static edu.cmu.cs.dennisc.alan.DragSourceView getDragSourceView( edu.cmu.cs.dennisc.alan.View view ) {
		if( view != null ) {
			if( view instanceof edu.cmu.cs.dennisc.alan.DragSourceView ) {
				if( view.isInteractive() ) {
					return (edu.cmu.cs.dennisc.alan.DragSourceView)view;
				} else {
					return null;
				}
			} else {
				if( view.isInteractive() ) {
					return null;
				} else {
					return getDragSourceView( view.getParent() );
				}
			}
		} else {
			return null;
		}
	}
	protected void handleMouseButton1Pressed( java.awt.event.MouseEvent e ) {
		//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
	}
	protected void handleMouseButton1Released( java.awt.event.MouseEvent e ) {
		//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
	}
	private void initialize() {
		class MouseAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
			private int m_xDelta = 0;
			private int m_yDelta = 0;

			public void mousePressed( java.awt.event.MouseEvent e ) {
				if( (e.getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK) != 0 ) {
					AbstractDragSourcePane.this.handleMouseButton1Pressed( e );
					DragComponent dragComponent = AbstractDragSourcePane.this.getDragComponent();
					//edu.cmu.cs.dennisc.print.PrintUtilities.println( "drag source pane:", dragComponent );
					if( dragComponent != null ) {
						java.awt.geom.Point2D.Float pickOffsetWithin = new java.awt.geom.Point2D.Float();
						edu.cmu.cs.dennisc.alan.View view = AbstractDragSourcePane.this.pickFromOrigin( pickOffsetWithin, e.getX(), e.getY(), edu.cmu.cs.dennisc.alan.DragSourceView.class );
						edu.cmu.cs.dennisc.alan.View v = AbstractDragSourcePane.this.pickFromOrigin( pickOffsetWithin, e.getX(), e.getY(), edu.cmu.cs.dennisc.alan.View.class );
						while( v != view ) {
							if( v.isInteractive() ) {
								view = null;
							}
							v = v.getParent();
						}
						//m_dragSourceView = DragSourcePane.this.pickFromOrigin( pickOffsetWithin, e.getX(), e.getY(), edu.cmu.cs.dennisc.alan.DragSourceView.class );
						m_dragSourceView = getDragSourceView( view );
						if( m_dragSourceView != null ) {

							//							edu.cmu.cs.dennisc.alan.View v = view;
							//							while( v != m_dragSourceView ) {
							//								pickOffsetWithin.x += v.getXTranslation();
							//								pickOffsetWithin.y += v.getYTranslation();
							//								v = v.getParent();
							//							}

							//edu.cmu.cs.dennisc.print.PrintUtilities.println( m_dragSourceView.getYTranslation() );

							//							pickOffsetWithin.x -= m_dragSourceView.getXMin( true );
							//							pickOffsetWithin.y -= m_dragSourceView.getYMin( true );

							final int DROP_SHADOW_OFFSET = 8;
							float width = m_dragSourceView.getWidth();
							float height = m_dragSourceView.getHeight();

							int w = (int)width + 1 + DROP_SHADOW_OFFSET;
							int h = (int)height + 1 + DROP_SHADOW_OFFSET;

							java.awt.image.BufferedImage bufferedImage = dragComponent.accessBufferedImage( w, h );
							java.awt.Graphics2D g2 = (java.awt.Graphics2D)bufferedImage.getGraphics();

							java.awt.Composite composite = g2.getComposite();
							g2.setComposite( java.awt.AlphaComposite.Clear );
							g2.fillRect( 0, 0, w, h );
							g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC, 0.75f ) );
							g2.setPaint( java.awt.Color.DARK_GRAY );
							g2.fill( m_dragSourceView.getDropShadowShape( DROP_SHADOW_OFFSET, DROP_SHADOW_OFFSET ) );

							m_dragSourceView.paintFromOrigin( g2, 0, 0 );
							g2.setComposite( composite );

							javax.swing.JLayeredPane layeredPane = dragComponent.getLayeredPane();
							java.awt.Point locationOnScreenLayeredPane = layeredPane.getLocationOnScreen();
							java.awt.Point locationOnScreenMembersPane = AbstractDragSourcePane.this.getLocationOnScreen();
							m_xDelta = locationOnScreenMembersPane.x - locationOnScreenLayeredPane.x;
							m_yDelta = locationOnScreenMembersPane.y - locationOnScreenLayeredPane.y;

							m_xDelta -= pickOffsetWithin.x;
							m_yDelta -= pickOffsetWithin.y;

							dragComponent.setLocation( e.getX() + m_xDelta, e.getY() + m_yDelta );

							layeredPane.add( dragComponent, null );
							layeredPane.setLayer( dragComponent, javax.swing.JLayeredPane.DRAG_LAYER );

							dragComponent.handleMousePressed( m_dragSourceView, e );
							m_dragSourceView.handleMousePressed( e );
						}
					}
				}
			}
			public void mouseDragged( java.awt.event.MouseEvent e ) {
				if( (e.getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK) != 0 ) {
					DragComponent dragComponent = AbstractDragSourcePane.this.getDragComponent();
					if( dragComponent != null ) {
						if( m_dragSourceView != null ) {
							dragComponent.setLocation( e.getX() + m_xDelta, e.getY() + m_yDelta );
							dragComponent.handleMouseDragged( m_dragSourceView, e );
							javax.swing.JLayeredPane layeredPane = dragComponent.getLayeredPane();
							java.awt.Rectangle bounds = dragComponent.getBounds();
							layeredPane.repaint( bounds );
						}
					}
				}
			}
			public void mouseClicked( java.awt.event.MouseEvent e ) {

			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
			public void mouseMoved( java.awt.event.MouseEvent e ) {
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
				if( (e.getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK) != 0 ) {
					try {
						DragComponent dragComponent = AbstractDragSourcePane.this.getDragComponent();
						if( dragComponent != null ) {
							if( m_dragSourceView != null ) {
								dragComponent.handleMouseReleased( m_dragSourceView, e );
							}
						}
						repaint();
					} finally {
						AbstractDragSourcePane.this.handleMouseButton1Released( e );
					}
				}
			}
		}
		MouseAdapter mouseAdapter = new MouseAdapter();
		addMouseListener( mouseAdapter );
		addMouseMotionListener( mouseAdapter );
		//todo: add mouseExited
	}
	public abstract DragComponent getDragComponent();
}
