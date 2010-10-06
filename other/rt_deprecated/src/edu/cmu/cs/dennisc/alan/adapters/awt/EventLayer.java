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
package edu.cmu.cs.dennisc.alan.adapters.awt;

/**
 * @author Dennis Cosgrove
 */
public class EventLayer implements edu.cmu.cs.dennisc.awt.EventLayer, java.awt.event.ComponentListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {
	private edu.cmu.cs.dennisc.alan.adapters.ViewAdapter m_viewAdapter;
	
	private edu.cmu.cs.dennisc.alan.View m_prevInteractiveView = null;
	private edu.cmu.cs.dennisc.alan.View m_prevPressedView = null;

	public EventLayer( edu.cmu.cs.dennisc.alan.adapters.ViewAdapter viewAdapter ) {
		m_viewAdapter = viewAdapter;
	}

	private void setPrevInteractiveView( edu.cmu.cs.dennisc.alan.View currInteractiveView, java.awt.event.MouseEvent e ) {
		if( m_prevInteractiveView != currInteractiveView ) {
			if( m_prevInteractiveView != null ) {
				m_prevInteractiveView.setHighlighted( false, e );
				//m_prevInteractiveView.setBevelState( edu.cmu.cs.dennisc.awt.BevelState.FLUSH );
				m_viewAdapter.repaint( m_prevInteractiveView );
			}
			if( currInteractiveView != null ) {
				currInteractiveView.setHighlighted( true, e );
				//currInteractiveView.setBevelState( edu.cmu.cs.dennisc.awt.BevelState.RAISED );
				m_viewAdapter.repaint( currInteractiveView );
			}
			m_prevInteractiveView = currInteractiveView;
		}
	}

	public boolean isEventIntercepted( java.util.EventObject e ) {
		return false;
	}

	public void componentShown( java.awt.event.ComponentEvent e ) {
		m_viewAdapter.setLayoutRequired( true );
	}
	public void componentHidden( java.awt.event.ComponentEvent e ) {
	}
	public void componentMoved( java.awt.event.ComponentEvent e ) {
	}
	public void componentResized( java.awt.event.ComponentEvent e ) {
		m_viewAdapter.setLayoutRequired( true );
	}

	private final int THRESHOLD = 5;
	private final int THRESHOLD_SQUARED = THRESHOLD*THRESHOLD;
	private int m_xPressed = -1;
	private int m_yPressed = -1;
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		setPrevInteractiveView( m_viewAdapter.pickFromOrigin( null, e.getX(), e.getY(), edu.cmu.cs.dennisc.alan.View.class, new edu.cmu.cs.dennisc.pattern.Criterion< edu.cmu.cs.dennisc.alan.View >() {
			public boolean accept( edu.cmu.cs.dennisc.alan.View view ) {
				return view.isInteractive();
			}
		} ), e );
	}
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		int xDelta = e.getX()-m_xPressed;
		int yDelta = e.getY()-m_yPressed;
		if( ( xDelta*xDelta + yDelta*yDelta ) > THRESHOLD_SQUARED ) {
			setPrevInteractiveView( null, e );
		}
	}

	public void mousePressed( java.awt.event.MouseEvent e ) {
		m_xPressed = e.getX();
		m_yPressed = e.getY();
		if( ( e.getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK ) != 0 ) {
			if( m_prevInteractiveView != null ) {
				m_prevPressedView = m_prevInteractiveView;
				m_prevPressedView.setPressed( true, e );
				m_viewAdapter.repaint( m_prevPressedView );
			}
		}
	}
	public void mouseReleased( java.awt.event.MouseEvent e ) {
		if( ( e.getModifiers() & java.awt.event.MouseEvent.BUTTON1_MASK ) != 0 ) {
			if( m_prevPressedView != null ) {
				m_prevPressedView.setPressed( false, e );
				m_viewAdapter.repaint( m_prevPressedView );
			}
		}
		m_xPressed = -1;
		m_yPressed = -1;
	}
	public void mouseClicked( java.awt.event.MouseEvent e ) {
	}
	public void mouseEntered( java.awt.event.MouseEvent e ) {
	}
	public void mouseExited( java.awt.event.MouseEvent e ) {
	}
	
	public void keyPressed( java.awt.event.KeyEvent e ) {
	}
	public void keyReleased( java.awt.event.KeyEvent e ) {
	}
	public void keyTyped( java.awt.event.KeyEvent e ) {
	}
}
