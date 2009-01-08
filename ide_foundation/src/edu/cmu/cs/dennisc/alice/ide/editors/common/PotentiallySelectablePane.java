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
public abstract class PotentiallySelectablePane extends PotentiallyActivePane {
	private boolean isSelected = false;
	protected static boolean IS_ACTUALLY_SELECTABLE = true;
	protected static boolean IS_NOT_ACTUALLY_SELECTABLE = false;
	public PotentiallySelectablePane( int axis ) {
		super( axis );
		if( this.isSelectionMouseListeningDesired() ) { //todo: allow this value to change
			this.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseEntered( java.awt.event.MouseEvent e ) {
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
						PotentiallySelectablePane.this.handleMousePressed( e );
					}
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
					if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
						PotentiallySelectablePane.this.handleMouseReleased( e );
					}
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
			} );
			this.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
				public void mouseDragged( java.awt.event.MouseEvent e ) {
					if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
						PotentiallySelectablePane.this.handleMouseDragged( e );
					}
				}
				public void mouseMoved( java.awt.event.MouseEvent e ) {
				}
			} );
		}
	}
	
	protected boolean isSelectionMouseListeningDesired() {
		return isActuallyPotentiallySelectable();
	}
	protected boolean isActuallyPotentiallySelectable() {
		return true;
	}

	private float clickThreshold = 5.0f;
	
	public float getClickThreshold() {
		return this.clickThreshold;
	}
	public void setClickThreshold( float clickThreshold ) {
		this.clickThreshold = clickThreshold;
	}
	
	private java.awt.event.MouseEvent mousePressedEvent = null;
	private boolean isWithinClickThreshold = false;
	
	protected java.awt.event.MouseEvent getMousePressedEvent() {
		return this.mousePressedEvent;
	}
	//always seems a bit iffy to have a method and a field with the same name
	protected boolean isWithinClickThreshold() {
		return this.isWithinClickThreshold;
	}
	
	protected void handleControlClick( java.awt.event.MouseEvent e ) {
	}
	protected void handleMousePressed( java.awt.event.MouseEvent e ) {
		this.mousePressedEvent = e;
		this.isWithinClickThreshold = true;
	}
	protected void handleMouseReleased( java.awt.event.MouseEvent e ) {
		if( this.isWithinClickThreshold ) {
			if( this.isActuallyPotentiallySelectable() ) {
				if( getIDE().isDragInProgress() ) {
					//
				} else {
					if( edu.cmu.cs.dennisc.swing.SwingUtilities.isControlDown( e ) ) {
						this.handleControlClick( e );
					} else {
						this.setSelected( !PotentiallySelectablePane.this.isSelected() );
					}
				}
			}
		}
		this.mousePressedEvent = null;
		this.isWithinClickThreshold = false;
	}
	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		this.isWithinClickThreshold = false;
	}
	protected void handleMouseDragged( java.awt.event.MouseEvent e ) {
		if( this.isWithinClickThreshold ) {
			int dx = e.getX() - this.mousePressedEvent.getX();
			int dy = e.getY() - this.mousePressedEvent.getY();
			if( dx*dx + dy*dy > this.clickThreshold*this.clickThreshold ) {
				handleMouseDraggedOutsideOfClickThreshold( e );
			}
		}
	}
	

	public boolean isSelected() {
		return this.isSelected;
	}
	public void setSelected( boolean isSelected ) {
		this.isSelected = isSelected;
		if( this.isSelected ) {
			this.requestFocus();
		}
		this.repaint();
	}

	private static final java.awt.Color TOP_SELECTED_COLOR = new java.awt.Color( 63, 63, 255, 127 );
	private static final java.awt.Color BOTTOM_SELECTED_COLOR = new java.awt.Color( 0, 0, 0, 127 );

	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		if( this.isSelected() ) {
			java.awt.Paint paint = new java.awt.GradientPaint( 0, 0, TOP_SELECTED_COLOR, 0, (float)getHeight(), BOTTOM_SELECTED_COLOR );
			g2.setPaint( paint );
			this.createBoundsShape().fill( g2 );
		}
	}
}
