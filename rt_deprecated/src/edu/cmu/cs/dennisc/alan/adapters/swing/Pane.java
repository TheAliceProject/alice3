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
public class Pane extends javax.swing.JComponent implements edu.cmu.cs.dennisc.alan.adapters.ViewAdapter {
	private edu.cmu.cs.dennisc.alan.adapters.awt.EventLayer m_eventAdapter = new edu.cmu.cs.dennisc.alan.adapters.awt.EventLayer( this );
	private edu.cmu.cs.dennisc.alan.CompositeView m_compositeView = null;
	private float m_xOrigin = 0;
	private float m_yOrigin = 0;

	public Pane() {
		this( null );
	}
	public Pane( edu.cmu.cs.dennisc.alan.CompositeView compositeView ) {
		//todo: true -> false
		this( compositeView, true );
	}
	public Pane( edu.cmu.cs.dennisc.alan.CompositeView compositeView, boolean isInteactive ) {
		setCompositeView( compositeView );
		if( isInteactive ) {
			addComponentListener( m_eventAdapter );
			addMouseListener( m_eventAdapter );
			addMouseMotionListener( m_eventAdapter );
			addKeyListener( m_eventAdapter );
		}
	}

	@Override
	public java.awt.Color getBackground() {
		java.awt.Color rv = m_compositeView.getClearColor();
		if( rv != null ) {
			//pass
		} else {
			rv = super.getBackground();
		}
		return rv;
	}
	//	@Override
	//	public void setBackground( java.awt.Color color ) {
	//		super.setBackground( color );
	//		if( m_compositeView != null ) {
	//			m_compositeView.setClearColor( color );
	//		}
	//	}

	public edu.cmu.cs.dennisc.alan.CompositeView getCompositeView() {
		return m_compositeView;
	}
	public void setCompositeView( edu.cmu.cs.dennisc.alan.CompositeView compositeView ) {
		m_compositeView = compositeView;
		//		super.setBackground( m_compositeView.getClearColor() );
		if( m_compositeView != null ) {
			m_compositeView.setClearColor( getBackground() );
		}
		repaint();
	}

	private void layoutIfNecessary( java.awt.Graphics g ) {
		if( m_compositeView.isLayoutRequired() ) {
			m_compositeView.layout( g );
			revalidate();
		}
	}
	public void setLayoutRequired( boolean isLayoutRequired ) {
		m_compositeView.setLayoutRequired( true );
	}
	public void repaint( edu.cmu.cs.dennisc.alan.View view ) {
		final int BUFFER = 4;
		java.awt.geom.Rectangle2D.Float bounds = view.getAbsoluteBoundsFromOrigin( m_xOrigin, m_yOrigin );
		repaint( (int)bounds.x - BUFFER, (int)bounds.y - BUFFER, (int)bounds.width + BUFFER + BUFFER, (int)bounds.height + BUFFER + BUFFER );
	}
	public <E extends edu.cmu.cs.dennisc.alan.View> E pickFromOrigin( java.awt.geom.Point2D.Float out_offsetWithin, float x, float y, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< E >... criteria ) {
		layoutIfNecessary( getGraphics() );
		return m_compositeView.pickFromOrigin( out_offsetWithin, x, y, m_xOrigin, m_yOrigin, cls, criteria );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		layoutIfNecessary( getGraphics() );
		return m_compositeView.getSize();
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setPaint( getBackground() );
		g2.fillRect( 0, 0, getWidth(), getHeight() );
		layoutIfNecessary( g2 );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		m_compositeView.paintFromOrigin( g2, m_xOrigin, m_yOrigin );
	}
}
