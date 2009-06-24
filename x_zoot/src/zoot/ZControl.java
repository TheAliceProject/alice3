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
package zoot;

public abstract class ZControl extends ZComponent {
	private ActionOperation leftButtonPressOperation;
	private ActionOperation leftButtonDoubleClickOperation;
	private ActionOperation popupOperation;

	private boolean isActive = false;
	private boolean isPressed = false;
	//private boolean isSelected = false;

	private zoot.event.ControlAdapter controlAdapter = null;
	protected boolean isMouseListeningDesired() { 
		return this.leftButtonPressOperation != null || this.leftButtonDoubleClickOperation != null || this.popupOperation != null;
	}

//	@Override
//	protected void finalize() throws Throwable {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "finalize", this.getClass() );
//		super.finalize();
//	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if( isMouseListeningDesired() ) {
			this.controlAdapter = new zoot.event.ControlAdapter( this );
			this.addMouseListener( this.controlAdapter );
			this.addMouseMotionListener( this.controlAdapter );
		}
	}
	@Override
	public void removeNotify() {
		super.removeNotify();
		if( this.controlAdapter != null ) {
			this.removeMouseListener( this.controlAdapter );
			this.removeMouseMotionListener( this.controlAdapter );
			this.controlAdapter = null;
//			
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "REMOVE NOTIFY: ", this.getClass() );
		}
	}
	
	private java.awt.event.MouseEvent mousePressedEvent = null;
	private java.awt.event.MouseEvent leftButtonPressedEvent = null;

	public java.awt.event.MouseEvent getLeftButtonPressedEvent() {
		return this.leftButtonPressedEvent;
	}

	public void handleMousePressed( java.awt.event.MouseEvent e ) {
//		java.awt.event.MouseEvent prevMousePressedEvent = this.mousePressedEvent;
		this.isWithinClickThreshold = true;
		this.mousePressedEvent = e;
		this.leftButtonPressedEvent = null;
		this.setPressed( true );
		if( edu.cmu.cs.dennisc.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
			this.leftButtonPressedEvent = e;
			if( this.leftButtonPressOperation != null ) {
				ZManager.performIfAppropriate( this.leftButtonPressOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
			}
		} else if( edu.cmu.cs.dennisc.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
			if( ZManager.isDragInProgress() ) {
				this.isWithinClickThreshold = false;
			} else {
				if( this.popupOperation != null ) {
					ZManager.performIfAppropriate( this.popupOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
				}
			}
		}
	}
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		this.setPressed( false );
	}
	public void handleMouseClicked( java.awt.event.MouseEvent e ) {
		if( e.getClickCount() == 2 ) {
			if( this.leftButtonDoubleClickOperation != null ) {
				ZManager.performIfAppropriate( this.leftButtonDoubleClickOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
			}
		}
	}
	public void handleMouseEntered( java.awt.event.MouseEvent e ) {
		if( ZManager.isDragInProgress() ) {
			//pass
		} else {
			this.setActive( true );
		}
	}
	public void handleMouseExited( java.awt.event.MouseEvent e ) {
		if( ZManager.isDragInProgress() ) {
			//pass
		} else {
			this.setActive( false );
		}
	}

	public void handleMouseMoved( java.awt.event.MouseEvent e ) {
	}

	private float clickThreshold = 5.0f;
	public float getClickThreshold() {
		return this.clickThreshold;
	}
	public void setClickThreshold( float clickThreshold ) {
		this.clickThreshold = clickThreshold;
	}
	private boolean isWithinClickThreshold = false;
	protected boolean isWithinClickThreshold() {
		return this.isWithinClickThreshold;
	}
	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		this.isWithinClickThreshold = false;
	}
	public void handleMouseDragged( java.awt.event.MouseEvent e ) {
		if( this.isWithinClickThreshold ) {
			int dx = e.getX() - this.mousePressedEvent.getX();
			int dy = e.getY() - this.mousePressedEvent.getY();
			if( dx * dx + dy * dy > this.clickThreshold * this.clickThreshold ) {
				handleMouseDraggedOutsideOfClickThreshold( e );
			}
		}
	}

	public ActionOperation getLeftButtonPressOperation() {
		return this.leftButtonPressOperation;
	}
	public void setLeftButtonPressOperation( ActionOperation leftButtonPressOperation ) {
		this.leftButtonPressOperation = leftButtonPressOperation;
	}
	public ActionOperation getLeftButtonDoubleClickOperation() {
		return this.leftButtonDoubleClickOperation;
	}
	public void setLeftButtonDoubleClickOperation( ActionOperation leftButtonDoubleClickOperation ) {
		this.leftButtonDoubleClickOperation = leftButtonDoubleClickOperation;
	}
	public ActionOperation getPopupOperation() {
		return this.popupOperation;
	}
	public void setPopupOperation( ActionOperation popupOperation ) {
		this.popupOperation = popupOperation;
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
	protected boolean isPressed() {
		return this.isPressed;
	}
	public void setPressed( boolean isPressed ) {
		if( this.isPressed != isPressed ) {
			this.isPressed = isPressed;
			this.repaint();
		}
	}
}
