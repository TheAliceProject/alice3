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
package org.lgna.stencil;

/**
 * @author Dennis Cosgrove
 */
public abstract class MouseEventFilteringLayeredPaneComponent extends LayeredPaneComponent {
	public MouseEventFilteringLayeredPaneComponent( javax.swing.JLayeredPane layeredPane, MenuPolicy menuPolicy ) {
		super( layeredPane, menuPolicy );
	}

	private final java.awt.event.AWTEventListener awtEventListener = new java.awt.event.AWTEventListener() {
		public void eventDispatched(java.awt.AWTEvent event) {
			java.awt.event.MouseEvent e = (java.awt.event.MouseEvent)event;
			e = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent(e.getComponent(), e, MouseEventFilteringLayeredPaneComponent.this.getAwtComponent());
			MouseEventFilteringLayeredPaneComponent.this.handleMouseMoved( e );
		}
	};
	protected abstract void handleMouseMoved(java.awt.event.MouseEvent e);
	protected abstract void redispatchMouseEvent(java.awt.event.MouseEvent eSrc);
	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseEntered(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseExited(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mousePressed(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseReleased(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
	};
	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
		public void mouseDragged(java.awt.event.MouseEvent e) {
			redispatchMouseEvent(e);
		}
	};
	private final java.awt.event.MouseWheelListener mouseWheelListener = new java.awt.event.MouseWheelListener() {
		public void mouseWheelMoved(java.awt.event.MouseWheelEvent e) {
			java.awt.Point p = e.getPoint();
			java.awt.Component component = javax.swing.SwingUtilities.getDeepestComponentAt(org.lgna.croquet.Application.getActiveInstance().getFrame().getAwtComponent().getContentPane(), p.x, p.y);
			if (component != null) {
				java.awt.Point pComponent = javax.swing.SwingUtilities.convertPoint(e.getComponent(), p, component);
				component.dispatchEvent(new java.awt.event.MouseWheelEvent(component, e.getID(), e.getWhen(), e.getModifiers() + e.getModifiersEx(), pComponent.x, pComponent.y, e.getClickCount(), e.isPopupTrigger(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation()));
			}
		}
	};
	
	private boolean isEventInterceptEnabled = false;
	public boolean isEventInterceptEnabled() {
		return this.isEventInterceptEnabled;
	}
	public void setEventInterceptEnabled( boolean isEventInterceptEnabled ) {
		if( this.isEventInterceptEnabled != isEventInterceptEnabled ) {
			if( this.isEventInterceptEnabled ) {
				this.getAwtComponent().removeMouseListener( this.mouseListener );
				this.getAwtComponent().removeMouseMotionListener( this.mouseMotionListener );
				this.getAwtComponent().removeMouseWheelListener( this.mouseWheelListener );
			}
			this.isEventInterceptEnabled = isEventInterceptEnabled;
			if( this.isEventInterceptEnabled ) {
				this.getAwtComponent().addMouseListener( this.mouseListener );
				this.getAwtComponent().addMouseMotionListener( this.mouseMotionListener );
				this.getAwtComponent().addMouseWheelListener( this.mouseWheelListener );
			}
		}
	}
//	private java.util.Stack< Boolean > isEventInterceptEnabledStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
//	public void pushEventInterceptEnabled( boolean isEventInterceptEnabled ) {
//		this.isEventInterceptEnabledStack.push( this.isEventInterceptEnabled );
//		this.setEventInterceptEnabled( isEventInterceptEnabled );
//	}
//	public boolean popEventInterceptEnabled() {
//		boolean rv = this.isEventInterceptEnabled;
//		boolean nextValue = this.isEventInterceptEnabledStack.pop();
//		this.setEventInterceptEnabled( nextValue );
//		return rv;
//	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener( this.awtEventListener, java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK );
	}
	@Override
	protected void handleUndisplayable() {
		java.awt.Toolkit.getDefaultToolkit().removeAWTEventListener( this.awtEventListener );
		super.handleUndisplayable();
	}
}
