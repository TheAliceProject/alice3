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

package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class Tab extends javax.swing.JComponent {
	private static final java.awt.Color CLOSE_BUTTON_BASE_COLOR = new java.awt.Color( 127, 63, 63 );
	private static final java.awt.Color CLOSE_BUTTON_HIGHLIGHT_COLOR = edu.cmu.cs.dennisc.color.ColorUtilities.shiftHSB( CLOSE_BUTTON_BASE_COLOR, 0, 0, +0.25f );
	private static final java.awt.Color CLOSE_BUTTON_PRESS_COLOR = edu.cmu.cs.dennisc.color.ColorUtilities.shiftHSB( CLOSE_BUTTON_BASE_COLOR, 0, 0, -0.125f );

	class CloseButton extends javax.swing.JComponent {
		private boolean m_isShowing = false;
		private boolean m_isHighlighted = false;
		private boolean m_isPressed = false;

//todo
//		public boolean isShowing() {
//			return m_isShowing;
//		}
		public void setShowing( boolean isShowing ) {
			if( m_isShowing != isShowing ) {
				m_isShowing = isShowing;
				getParent().repaint();
			}
		}
		public boolean isHighlighted() {
			return m_isHighlighted;
		}
		public void setHighlighted( boolean isHighlighted ) {
			if( m_isHighlighted != isHighlighted ) {
				m_isHighlighted = isHighlighted;
				repaint();
			}
		}
		public boolean isPressed() {
			return m_isPressed;
		}
		public void setPressed( boolean isPressed ) {
			if( m_isPressed != isPressed ) {
				m_isPressed = isPressed;
				//todo: investigate why getParent() required
				getParent().repaint();
			}
		}
		
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
			if( m_isClosable && ( isShowing() || isHighlighted() || isPressed() ) ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				
				float size = Math.min( getWidth(), getHeight() ) * 0.9f;

				float w = size;
				float h = size * 0.25f;
				float x = -w * 0.5f;
				float y = -h * 0.5f;
				java.awt.geom.RoundRectangle2D.Float rr = new java.awt.geom.RoundRectangle2D.Float( x, y, w, h, h, h );

				java.awt.geom.Area area0 = new java.awt.geom.Area( rr );
				java.awt.geom.Area area1 = new java.awt.geom.Area( rr );

				java.awt.geom.AffineTransform m0 = new java.awt.geom.AffineTransform();
				m0.rotate( Math.PI * 0.25 );
				area0.transform( m0 );

				java.awt.geom.AffineTransform m1 = new java.awt.geom.AffineTransform();
				m1.rotate( Math.PI * 0.75 );
				area1.transform( m1 );

				area0.add( area1 );

				java.awt.geom.AffineTransform m = new java.awt.geom.AffineTransform();
				m.translate( getWidth() / 2, getHeight() / 2 );
				area0.transform( m );

				java.awt.Paint prevPaint = g2.getPaint();
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				if( isHighlighted() ) {
					if( isPressed() ) {
						g2.setPaint( CLOSE_BUTTON_PRESS_COLOR );
					} else {
						g2.setPaint( CLOSE_BUTTON_HIGHLIGHT_COLOR );
					}
				} else {
					g2.setPaint( java.awt.Color.WHITE );
				}
				g2.fill( area0 );
				g2.setPaint( java.awt.Color.BLACK );
				g2.draw( area0 );
				g2.setPaint( prevPaint );
			}
		}
	}

	private CloseButton m_closeButton = new CloseButton();

	private class CloseButtonMouseAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			Tab.this.m_closeButton.setHighlighted( true );
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
			Tab.this.m_closeButton.setHighlighted( false );
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			Tab.this.m_closeButton.setPressed( true );
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			Tab.this.m_closeButton.setPressed( false );
			if( Tab.this.m_isClosable && Tab.this.m_closeButton.isHighlighted() ) {
				Tab.this.fireClosed();
			}
		}
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}
		public void mouseDragged( java.awt.event.MouseEvent e ) {
		}
	}
	private CloseButtonMouseAdapter m_closeButtonMouseAdapter = null;

	private javax.swing.JComponent m_title;
	private javax.swing.JComponent m_component;
	private boolean m_isSelected;
	private boolean m_isClosable = false;

	class MouseAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			if( m_isClosable ) {
				m_closeButton.setShowing( true );
			}
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
			if( m_isClosable ) {
				m_closeButton.setShowing( false );
			}
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			setSelected( true );
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}
		public void mouseDragged( java.awt.event.MouseEvent e ) {
		}
	}

	private final int EAST_PAD = 30;
	
	public Tab( javax.swing.JComponent title, javax.swing.JComponent component ) {
		m_component = component;

		//m_titleLabel.setFont( edu.cmu.cs.dennisc.ui.storytelling.programeditor.vcs.Look.getBaseFont() );
		m_title = title;
		
		//final int SIZE = edu.cmu.cs.dennisc.ui.storytelling.programeditor.vcs.Look.getBaseFontSize();
		final int SIZE = m_title.getFont().getSize() * 7 / 4;
		m_closeButton.setMinimumSize( new java.awt.Dimension( SIZE, SIZE ) );
		m_closeButton.setPreferredSize( new java.awt.Dimension( SIZE, SIZE ) );
		m_closeButton.setMaximumSize( new java.awt.Dimension( SIZE, SIZE ) );

		setLayout( new javax.swing.BoxLayout( this, javax.swing.BoxLayout.X_AXIS ) );
		add( new javax.swing.Box.Filler( new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( 5, 0 ), new java.awt.Dimension( 5, 0 ) ) );
		add( m_title );
		add( new javax.swing.Box.Filler( new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( 5, 0 ), new java.awt.Dimension( Short.MAX_VALUE, 0 ) ) );
		add( m_closeButton );
		add( javax.swing.Box.createRigidArea( new java.awt.Dimension( EAST_PAD, 0 ) ) );

		//setFont( new java.awt.Font( null, java.awt.Font.PLAIN, 18 ) );

		MouseAdapter mouseAdapter = new MouseAdapter();
		addMouseListener( mouseAdapter );
		addMouseMotionListener( mouseAdapter );

		this.setBorder( new javax.swing.border.EmptyBorder( 2, 0, 0, 0 ) );
	}

	public boolean isClosable() {
		return m_isClosable;
	}
	public void setClosable( boolean isClosable ) {
		m_isClosable = isClosable;
		if( m_isClosable ) {
			if( m_closeButtonMouseAdapter == null ) {
				m_closeButtonMouseAdapter = new CloseButtonMouseAdapter();
				m_closeButton.addMouseListener( m_closeButtonMouseAdapter );
				m_closeButton.addMouseMotionListener( m_closeButtonMouseAdapter );
			}
		}
	}

	private java.util.List< edu.cmu.cs.dennisc.swing.event.TabListener > m_tabListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.swing.event.TabListener >();

	public void addTabListener( edu.cmu.cs.dennisc.swing.event.TabListener tabListener ) {
		m_tabListeners.add( tabListener );
	}
	public void removeTabListener( edu.cmu.cs.dennisc.swing.event.TabListener tabListener ) {
		m_tabListeners.remove( tabListener );
	}
	private void fireSelected() {
		edu.cmu.cs.dennisc.swing.event.TabEvent e = new edu.cmu.cs.dennisc.swing.event.TabEvent( this );
		for( edu.cmu.cs.dennisc.swing.event.TabListener tabListener : m_tabListeners ) {
			tabListener.tabSelected( e );
		}
	}
	private void fireClosed() {
		edu.cmu.cs.dennisc.swing.event.TabEvent e = new edu.cmu.cs.dennisc.swing.event.TabEvent( this );
		for( edu.cmu.cs.dennisc.swing.event.TabListener tabListener : m_tabListeners ) {
			tabListener.tabClosed( e );
		}
	}
	public javax.swing.JComponent getTabPane() {
		return m_component;
	}

	public boolean isSelected() {
		return m_isSelected;
	}
	public void setSelected( boolean isSelected ) {
		if( m_isSelected != isSelected ) {
			m_isSelected = isSelected;
			if( m_isSelected ) {
				fireSelected();
			}
			repaint();
		}
	}

	@Override
	public java.awt.Color getBackground() {
		java.awt.Color rv = super.getBackground();
		if( rv != null ) {
			if( isSelected() ) {
				//pass
			} else {
				final int GRAY_SCALE = 96; 
				rv = new java.awt.Color( ( rv.getRed() + GRAY_SCALE ) / 2, ( rv.getGreen() + GRAY_SCALE ) / 2, ( rv.getBlue() + GRAY_SCALE ) / 2 );
			}
		}
		return rv;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

		float x0 = getWidth() - EAST_PAD;
		float x1 = getWidth();
		float cx0 = x0 + 15;
		float cx1 = x1 - 20;

		float y0 = 0.0f;
		float y1 = getHeight();
		float cy0 = y0;
		float cy1 = y1;

		float xA = getHeight() * 0.3f;
		float yA = xA;
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();

		path.moveTo( x1, y1 );
		path.curveTo( cx1, cy1, cx0, cy0, x0, y0 );
		path.lineTo( xA, 0 );
		path.quadTo( 0, 0, 0, yA );
		path.lineTo( 0, y1 );
		//todo?
		//path.closePath();

		java.awt.Paint prevPaint = g2.getPaint();
		g2.setPaint( this.getBackground() );
		edu.cmu.cs.dennisc.awt.ShapeUtilties.paint( g2, path, edu.cmu.cs.dennisc.awt.BevelState.RAISED );
		g2.setPaint( prevPaint );
	}
}
