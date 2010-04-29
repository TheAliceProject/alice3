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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class KControl extends KWidget {
	private java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mousePressed( java.awt.event.MouseEvent e ) {
			KControl.this.handleMousePressed( e );
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			KControl.this.handleMouseReleased( e );
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
			KControl.this.handleMouseClicked( e );
		}
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			KControl.this.handleMouseEntered( e );
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
			KControl.this.handleMouseExited( e );
		}
	};
	private java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved( java.awt.event.MouseEvent e ) {
			KControl.this.handleMouseMoved( e );
		}
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			KControl.this.handleMouseDragged( e );
		}
	};
	private boolean isActive = false;
	private boolean isPressed = false;
	//private boolean isSelected = false;

	private java.awt.event.MouseEvent mousePressedEvent = null;
	private java.awt.event.MouseEvent leftButtonPressedEvent = null;

	/*package-private*/ KControl() {
	}
	@Override
	protected void adding() {
		super.adding();
		this.getJComponent().addMouseListener( this.mouseListener );
		this.getJComponent().addMouseMotionListener( this.mouseMotionListener );
	}
	@Override
	protected void removed() {
		this.getJComponent().removeMouseMotionListener( this.mouseMotionListener );
		this.getJComponent().removeMouseListener( this.mouseListener );
		super.removed();
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

	//todo: remove
	private ActionOperation leftButtonPressOperation;
	private ActionOperation leftButtonDoubleClickOperation;
	private ActionOperation popupOperation;
	
	public java.awt.event.MouseEvent getLeftButtonPressedEvent() {
		return this.leftButtonPressedEvent;
	}
	protected void handleMousePressed( java.awt.event.MouseEvent e ) {
//		java.awt.event.MouseEvent prevMousePressedEvent = this.mousePressedEvent;
		this.isWithinClickThreshold = true;
		this.mousePressedEvent = e;
		this.leftButtonPressedEvent = null;
		this.setPressed( true );
		if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
			this.leftButtonPressedEvent = e;
			if( this.leftButtonPressOperation != null ) {
				ZManager.performIfAppropriate( this.leftButtonPressOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
			}
		} else if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
			if( ZManager.isDragInProgress() ) {
				this.isWithinClickThreshold = false;
			} else {
				if( this.popupOperation != null ) {
					ZManager.performIfAppropriate( this.popupOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
				}
			}
		}
	}
	protected void handleMouseReleased( java.awt.event.MouseEvent e ) {
		this.setPressed( false );
	}
	protected void handleMouseClicked( java.awt.event.MouseEvent e ) {
		if( e.getClickCount() == 2 ) {
			if( this.leftButtonDoubleClickOperation != null ) {
				ZManager.performIfAppropriate( this.leftButtonDoubleClickOperation, e, ZManager.CANCEL_IS_WORTHWHILE );
			}
		}
	}
	protected void handleMouseEntered( java.awt.event.MouseEvent e ) {
		if( ZManager.isDragInProgress() ) {
			//pass
		} else {
			this.setActive( true );
		}
	}
	protected void handleMouseExited( java.awt.event.MouseEvent e ) {
		if( ZManager.isDragInProgress() ) {
			//pass
		} else {
			this.setActive( false );
		}
	}

	protected void handleMouseMoved( java.awt.event.MouseEvent e ) {
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
	protected void handleMouseDragged( java.awt.event.MouseEvent e ) {
		if( this.isWithinClickThreshold ) {
			int dx = e.getX() - this.mousePressedEvent.getX();
			int dy = e.getY() - this.mousePressedEvent.getY();
			if( dx * dx + dy * dy > this.clickThreshold * this.clickThreshold ) {
				handleMouseDraggedOutsideOfClickThreshold( e );
			}
		}
	}
}
