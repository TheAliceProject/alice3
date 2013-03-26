/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class ConsistentMouseDragEventQueue extends java.awt.EventQueue {
	private static final boolean IS_CLICK_AND_CLACK_DESIRED_DEFAULT = false;
	private static final boolean IS_CLICK_AND_CLACK_DESIRED = IS_CLICK_AND_CLACK_DESIRED_DEFAULT || edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "edu.cmu.cs.dennisc.java.awt.ConsistentMouseDragEventQueue.isClickAndClackDesired" );

	private static class SingletonHolder {
		private static ConsistentMouseDragEventQueue instance = new ConsistentMouseDragEventQueue();
	}

	public static ConsistentMouseDragEventQueue getInstance() {
		return SingletonHolder.instance;
	}

	public static void pushIfAppropriate() {
		if( ( IS_CLICK_AND_CLACK_DESIRED == false ) && edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			//pass
		} else {
			java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().push( SingletonHolder.instance );
		}
	}

	private final java.util.Stack<java.awt.Component> stack;
	private int lastPressOrDragModifiers;

	private ConsistentMouseDragEventQueue() {
		if( IS_CLICK_AND_CLACK_DESIRED ) {
			this.stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
		} else {
			this.stack = null;
		}
	}

	public boolean isClickAndClackSupported() {
		return this.stack != null;
	}

	public java.awt.Component peekClickAndClackComponent() {
		if( this.stack != null ) {
			if( this.stack.size() > 0 ) {
				return this.stack.peek();
			} else {
				return null;
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public void pushClickAndClackComponent( java.awt.Component awtComponent ) {
		if( this.stack != null ) {
			this.stack.push( awtComponent );
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public java.awt.Component popClickAndClackComponent() {
		if( this.stack != null ) {
			return this.stack.pop();
		} else {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	protected void dispatchEvent( java.awt.AWTEvent e ) {
		if( e instanceof java.awt.event.MouseWheelEvent ) {
			java.awt.event.MouseWheelEvent mouseWheelEvent = (java.awt.event.MouseWheelEvent)e;

			java.awt.Component source = mouseWheelEvent.getComponent();
			int id = mouseWheelEvent.getID();
			long when = mouseWheelEvent.getWhen();
			int modifiers = mouseWheelEvent.getModifiers();
			int x = mouseWheelEvent.getX();
			int y = mouseWheelEvent.getY();
			//1.7
			//int xAbs = mouseWheelEvent.getXOnScreen();
			//int yAbs = mouseWheelEvent.getYOnScreen();
			int clickCount = mouseWheelEvent.getClickCount();
			boolean popupTrigger = mouseWheelEvent.isPopupTrigger();
			int scrollType = mouseWheelEvent.getScrollType();
			int scrollAmount = mouseWheelEvent.getScrollAmount();
			int wheelRotation = mouseWheelEvent.getWheelRotation();
			//1.7
			//double preciseWheelRotation = mouseWheelEvent.getPreciseWheelRotation();

			//note:
			modifiers = this.lastPressOrDragModifiers;

			// 1.7 
			//= new java.awt.event.MouseWheelEvent( source, id, when, modifiers, x, y, xAbs, yAbs, clickCount, popupTrigger, scrollType, scrollAmount, wheelRotation, preciseWheelRotation );

			// 1.5
			e = new java.awt.event.MouseWheelEvent( source, id, when, modifiers, x, y, clickCount, popupTrigger, scrollType, scrollAmount, wheelRotation );

		} else if( e instanceof java.awt.event.MouseEvent ) {
			java.awt.event.MouseEvent mouseEvent = (java.awt.event.MouseEvent)e;
			int id = mouseEvent.getID();
			switch( id ) {
			case java.awt.event.MouseEvent.MOUSE_PRESSED:
			case java.awt.event.MouseEvent.MOUSE_DRAGGED:
				this.lastPressOrDragModifiers = mouseEvent.getModifiers();
				break;
			}

			if( this.stack != null ) {
				if( this.stack.size() > 0 ) {
					java.awt.Component component = this.stack.peek();
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: send modified", e.paramString(), "to", component.getClass().getSimpleName() );
				}
			}
		}
		super.dispatchEvent( e );
	}
}
