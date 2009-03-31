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
package edu.cmu.cs.dennisc.lookingglass.overlay;

//todo: support Panel
//todo: extend Panel
/**
 * @author Dennis Cosgrove
 */
public class Overlay implements edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener, edu.cmu.cs.dennisc.awt.EventInterceptor, java.awt.event.ComponentListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass m_onscreenLookingGlass;
	private Composite m_composite = new Composite();

	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return m_onscreenLookingGlass;
	}
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		if( m_onscreenLookingGlass != null ) {
			java.awt.Component awtComponent = onscreenLookingGlass.getAWTComponent();
			if( awtComponent != null ) {
				awtComponent.removeComponentListener( this );
				awtComponent.removeMouseListener( this );
				awtComponent.removeMouseMotionListener( this );
				awtComponent.removeKeyListener( this );
			}
			m_onscreenLookingGlass.removeLookingGlassListener( this );
		}
		m_onscreenLookingGlass = onscreenLookingGlass;
		if( m_onscreenLookingGlass != null ) {
			java.awt.Component awtComponent = onscreenLookingGlass.getAWTComponent();
			if( awtComponent != null ) {
				awtComponent.addComponentListener( this );
				awtComponent.addMouseListener( this );
				awtComponent.addMouseMotionListener( this );
				awtComponent.addKeyListener( this );
			}
			m_onscreenLookingGlass.addLookingGlassListener( this );
		}
	}

	public Composite getComposite() {
		return m_composite;
	}
	public void setComposite( Composite composite ) {
		m_composite = composite;
		if( m_composite != null ) {
			m_composite.setOverlay( this );
		}
	}

	public void repaint() {
		if( m_onscreenLookingGlass != null ) {
			m_onscreenLookingGlass.repaint();
		}
	}

	public boolean isEventIntercepted( java.util.EventObject e ) {
		if( m_composite != null ) {
			return m_composite.isEventIntercepted( e );
		} else {
			return false;
		}
	}

	public void initialized( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent e ) {
	}
	public void resized( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent e ) {
	}
	public void displayChanged( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent e ) {
	}
	public void cleared( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
	}
	public void rendered( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
		if( m_composite != null ) {
			m_composite.handleRendered( e, (java.awt.Graphics2D)m_onscreenLookingGlass.getAWTComponent().getGraphics() );
		}
	}

	public void componentShown( java.awt.event.ComponentEvent e ) {
		if( m_composite != null ) {
			m_composite.handleComponentShown( e );
		}
	}
	public void componentHidden( java.awt.event.ComponentEvent e ) {
	}
	public void componentMoved( java.awt.event.ComponentEvent e ) {
	}
	public void componentResized( java.awt.event.ComponentEvent e ) {
		if( m_composite != null ) {
			m_composite.handleComponentResized( e );
		}
	}

	public void mouseClicked( java.awt.event.MouseEvent e ) {
	}

	public void mouseEntered( java.awt.event.MouseEvent e ) {
	}

	public void mouseExited( java.awt.event.MouseEvent e ) {
	}

	public void mousePressed( java.awt.event.MouseEvent e ) {
		if( m_composite != null ) {
			m_composite.handleMousePressed( e );
		}
	}

	public void mouseReleased( java.awt.event.MouseEvent e ) {
		if( m_composite != null ) {
			m_composite.handleMouseReleased( e );
		}
	}

	public void mouseDragged( java.awt.event.MouseEvent e ) {
	}

	public void mouseMoved( java.awt.event.MouseEvent e ) {
		if( m_composite != null ) {
			m_composite.handleMouseMoved( e );
		}
	}

	public void keyPressed( java.awt.event.KeyEvent e ) {
	}

	public void keyReleased( java.awt.event.KeyEvent e ) {
	}

	public void keyTyped( java.awt.event.KeyEvent e ) {
	}
}
