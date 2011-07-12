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
package org.lgna.croquet.components;

public abstract class Control extends Widget {
	private org.lgna.croquet.Model leftButtonPressModel;
	private org.lgna.croquet.Model leftButtonClickModel;
	private org.lgna.croquet.Model leftButtonDoubleClickModel;

	private boolean isActive = false;
	private boolean isPressed = false;
	//private boolean isSelected = false;

	private static class ControlAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		private Control control;
		public ControlAdapter( Control control ) {
			this.control = control;
		}
//		@Override
//		protected void finalize() throws java.lang.Throwable {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "finalize ControlAdapter" );
//			super.finalize();
//		}
		public void mousePressed( java.awt.event.MouseEvent e ) {
			this.control.handleMousePressed( e );
		}
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			this.control.handleMouseReleased( e );
		}
		public void mouseClicked( java.awt.event.MouseEvent e ) {
			this.control.handleMouseClicked( e );
		}
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			this.control.handleMouseEntered( e );
		}
		public void mouseExited( java.awt.event.MouseEvent e ) {
			this.control.handleMouseExited( e );
		}
		public void mouseMoved( java.awt.event.MouseEvent e ) {
			this.control.handleMouseMoved( e );
		}
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			this.control.handleMouseDragged( e );
		}
	}
	private ControlAdapter controlAdapter = null;
	protected boolean isMouseListeningDesired() { 
		return this.leftButtonPressModel != null || this.leftButtonClickModel != null || this.leftButtonDoubleClickModel != null;
	}

//	@Override
//	protected void finalize() throws Throwable {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "finalize", this.getClass() );
//		super.finalize();
//	}
	
	@Override
	protected void addNotify() {
		super.addNotify();
		if( isMouseListeningDesired() ) {
			if( this.controlAdapter != null ) {
				//pass
			} else {
				this.controlAdapter = new ControlAdapter( this );
				this.addMouseListener( this.controlAdapter );
				this.addMouseMotionListener( this.controlAdapter );
			}
		}
	}
	@Override
	protected void removeNotify() {
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
		if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
			this.leftButtonPressedEvent = e;
			if( this.leftButtonPressModel != null ) {
				this.leftButtonPressModel.fire( new org.lgna.croquet.triggers.MouseEventTrigger( this, e ) );
			}
//		} else if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
//			if( Application.getSingleton().isDragInProgress() ) {
//				this.isWithinClickThreshold = false;
//			} else {
//				if( this.popupModel != null ) {
//					this.popupModel.fire( e, this );
//				}
//			}
		}
	}
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		this.setPressed( false );
	}
	public void handleMouseClicked( java.awt.event.MouseEvent e ) {
		switch( e.getClickCount() ) {
		case 1:
			if( this.leftButtonClickModel != null ) {
				this.leftButtonClickModel.fire( new org.lgna.croquet.triggers.MouseEventTrigger( this, e ) );
			}
			break;
		case 2:
			if( this.leftButtonDoubleClickModel != null ) {
				this.leftButtonDoubleClickModel.fire( new org.lgna.croquet.triggers.MouseEventTrigger( this, e ) );
			}
			break;
		}
	}
	public void handleMouseEntered( java.awt.event.MouseEvent e ) {
		if( org.lgna.croquet.Application.getActiveInstance().isDragInProgress() ) {
			//pass
		} else {
			this.setActive( true );
		}
	}
	public void handleMouseExited( java.awt.event.MouseEvent e ) {
		if( org.lgna.croquet.Application.getActiveInstance().isDragInProgress() ) {
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

	public org.lgna.croquet.Model getLeftButtonPressModel() {
		return this.leftButtonPressModel;
	}
	public void setLeftButtonPressModel( org.lgna.croquet.Model leftButtonPressModel ) {
		this.leftButtonPressModel = leftButtonPressModel;
	}
	public org.lgna.croquet.Model getLeftButtonClickModel() {
		return this.leftButtonClickModel;
	}
	public void setLeftButtonClickModel( org.lgna.croquet.Model leftButtonClickModel ) {
		this.leftButtonClickModel = leftButtonClickModel;
	}
	public org.lgna.croquet.Model getLeftButtonDoubleClickModel() {
		return this.leftButtonDoubleClickModel;
	}
	public void setLeftButtonDoubleClickModel( org.lgna.croquet.Model leftButtonDoubleClickModel ) {
		this.leftButtonDoubleClickModel = leftButtonDoubleClickModel;
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
