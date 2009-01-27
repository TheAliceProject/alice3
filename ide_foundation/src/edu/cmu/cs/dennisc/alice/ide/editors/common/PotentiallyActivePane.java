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
package edu.cmu.cs.dennisc.alice.ide.editors.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class PotentiallyActivePane extends edu.cmu.cs.dennisc.zoot.ZBoxPane {
	private boolean isActive = false;

	public PotentiallyActivePane( int axis ) {
		super( axis );
		if( this.isActiveMouseListeningDesired() ) {
			this.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseEntered( java.awt.event.MouseEvent e ) {
					if( PotentiallyActivePane.this.isActuallyPotentiallyActive() ) {
						if( getIDE().isDragInProgress() ) {
							//
						} else {
							PotentiallyActivePane.this.setActive( true );
						}
					}
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
					if( PotentiallyActivePane.this.isActuallyPotentiallyActive() ) {
						if( getIDE().isDragInProgress() ) {
							//
						} else {
							PotentiallyActivePane.this.setActive( false );
						}
					}
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
						PotentiallyActivePane.this.handleLeftMousePress( e );
					} else if( javax.swing.SwingUtilities.isRightMouseButton( e ) ) {
						PotentiallyActivePane.this.handleRightMousePress( e );
					}
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
			} );
		}
	}
	protected edu.cmu.cs.dennisc.alice.ide.IDE getIDE() {
		return edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton();
	}
	protected boolean isActiveMouseListeningDesired() {
		return isActuallyPotentiallyActive();
	}
	protected boolean isActuallyPotentiallyActive() {
		return true;
	}
	protected void handleLeftMousePress( java.awt.event.MouseEvent e ) {
		//pass
	}
	protected void handleRightMousePress( java.awt.event.MouseEvent e ) {
		//pass
	}
	public boolean isActive() {
		return this.isActive;
	}
	public void setActive( boolean isActive ) {
		this.isActive = isActive;
		this.repaint();
	}
	protected edu.cmu.cs.dennisc.awt.BevelState getBevelState() {
		if( this.isActive() ) {
			return edu.cmu.cs.dennisc.awt.BevelState.RAISED;
		} else {
			return edu.cmu.cs.dennisc.awt.BevelState.FLUSH;
		}
	}
	@Override
	protected void paintBorder( java.awt.Graphics g ) {
		//super.paintBorder( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		edu.cmu.cs.dennisc.awt.BevelState bevelState = this.getBevelState();
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		this.createBoundsShape().draw( g2, bevelState, 2.0f, 1.0f, 1.0f );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.Paint prev = g2.getPaint();
		java.awt.Color color = this.getBackground();
		g2.setPaint( color );
		try {
			this.createBoundsShape().fill( g2 );
		} finally {
			g2.setPaint( prev );
		}
		super.paintComponent( g );
	}

	protected abstract edu.cmu.cs.dennisc.awt.BeveledShape createBoundsShape();
	protected void paintActiveBorder( java.awt.Graphics2D g2 ) {
		final float STROKE_WIDTH = 3.0f;
		this.createBoundsShape().draw( g2, edu.cmu.cs.dennisc.awt.BevelState.RAISED, STROKE_WIDTH, STROKE_WIDTH, STROKE_WIDTH );
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		if( this.isActive() ) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			this.paintActiveBorder( g2 );
		}
	}
}
