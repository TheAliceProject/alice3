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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public abstract class ZControl extends ZBoxPane {
	private boolean isActive = false;
	private boolean isPressed = false;
	private boolean isSelected = false;

	public ZControl( int axis ) {
		super( axis );
		if( this.isActiveMouseListeningDesired() ) {
			this.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseEntered( java.awt.event.MouseEvent e ) {
					if( ZControl.this.isActuallyPotentiallyActive() ) {
//						if( getIDE().isDragInProgress() ) {
//							//
//						} else {
							ZControl.this.setActive( true );
//						}
					}
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
					if( ZControl.this.isActuallyPotentiallyActive() ) {
//						if( getIDE().isDragInProgress() ) {
//							//
//						} else {
							ZControl.this.setActive( false );
//						}
					}
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					if( e.isPopupTrigger() ) {
						ZControl.this.handleRightMousePress( e );
					} else {
						ZControl.this.handleLeftMousePress( e );
					}
					ZControl.this.setPressed( true );
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
					ZControl.this.setPressed( false );
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
			} );
		}
		if( this.isSelectionMouseListeningDesired() ) { //todo: allow this value to change
			this.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseEntered( java.awt.event.MouseEvent e ) {
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					if( e.isPopupTrigger() ) {
						//pass
					} else {
						ZControl.this.handleMousePressed( e );
					}
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
					if( e.isPopupTrigger() ) {
						//pass
					} else {
						ZControl.this.handleMouseReleased( e );
					}
				}
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
			} );
			this.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
				public void mouseDragged( java.awt.event.MouseEvent e ) {
					if( edu.cmu.cs.dennisc.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
						//pass
					} else {
						ZControl.this.handleMouseDragged( e );
					}
				}
				public void mouseMoved( java.awt.event.MouseEvent e ) {
				}
			} );
		}
	}
	protected boolean isActiveMouseListeningDesired() {
		return isActuallyPotentiallyActive();
	}
	protected boolean isActuallyPotentiallyActive() {
		return true;
	}
	protected void handleLeftMousePress( java.awt.event.MouseEvent e ) {
	}
	protected void handleRightMousePress( java.awt.event.MouseEvent e ) {
	}

	protected boolean isPressed() {
		return this.isPressed;
	}
	public void setPressed( boolean isPressed ) {
		this.isPressed = isPressed;
	}

	public boolean isActive() {
		return this.isActive;
	}
	public void setActive( boolean isActive ) {
		if( this.isActive != isActive ) {
			this.isActive = isActive;
			this.repaint();
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
	
	private boolean isWithinClickThreshold = false;
	
	//always seems a bit iffy to have a method and a field with the same name
	protected boolean isWithinClickThreshold() {
		return this.isWithinClickThreshold;
	}
	
	private java.awt.event.MouseEvent mousePressedEvent = null;
	public java.awt.event.MouseEvent getMousePressedEvent() {
		return this.mousePressedEvent;
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
//				if( getIDE().isDragInProgress() ) {
//					//
//				} else {
					if( edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( e ) ) {
						this.handleControlClick( e );
					} else {
						this.setSelected( !ZControl.this.isSelected() );
					}
//				}
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
	
}
