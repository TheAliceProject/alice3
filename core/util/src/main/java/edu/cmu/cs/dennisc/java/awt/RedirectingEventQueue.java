/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class RedirectingEventQueue extends java.awt.EventQueue {
	private java.awt.Component src;
	private java.awt.Component dst;
	private java.awt.Component last;

	public RedirectingEventQueue( java.awt.Component src, java.awt.Component dst ) {
		this.src = src;
		this.dst = dst;
		this.last = null;
	}

	private static java.awt.Component getDeepestMouseListener( java.awt.Component dst, java.awt.Component descendant ) {
		java.awt.Component rv = descendant;
		while( rv != null ) {
			if( ( rv.getMouseListeners().length > 0 ) || ( rv.getMouseMotionListeners().length > 0 ) ) {
				break;
			}
			if( rv == dst ) {
				rv = null;
				break;
			}
			rv = rv.getParent();
		}
		return rv;
	}

	@Override
	protected void dispatchEvent( java.awt.AWTEvent e ) {
		if( e instanceof java.awt.event.MouseEvent ) {
			java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
			java.awt.Component curr = me.getComponent();
			int id = me.getID();
			if( curr == this.src ) {
				if( ( id == java.awt.event.MouseEvent.MOUSE_ENTERED ) || ( id == java.awt.event.MouseEvent.MOUSE_EXITED ) ) {
					e = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( this.src, me, this.dst );
				} else if( id == java.awt.event.MouseEvent.MOUSE_MOVED ) {
					me = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( this.src, me, dst );
					java.awt.Component descendant = javax.swing.SwingUtilities.getDeepestComponentAt( this.dst, me.getX(), me.getY() );
					descendant = RedirectingEventQueue.getDeepestMouseListener( dst, descendant );
					if( this.last != descendant ) {
						java.awt.Component exitComponent;
						if( this.last != null ) {
							exitComponent = this.last;
						} else {
							exitComponent = this.src;
						}
						java.awt.event.MouseEvent exitEvent = new java.awt.event.MouseEvent( exitComponent, java.awt.event.MouseEvent.MOUSE_EXITED, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), me.getButton() );
						exitEvent = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( this.src, exitEvent, exitComponent );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( exitEvent );
						super.dispatchEvent( exitEvent );
					}
					if( descendant != null ) {
						e = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( dst, me, descendant );
					}
					if( this.last != descendant ) {
						java.awt.Component enterComponent;
						if( descendant != null ) {
							enterComponent = descendant;
						} else {
							enterComponent = this.src;
						}
						java.awt.event.MouseEvent enterEvent = new java.awt.event.MouseEvent( enterComponent, java.awt.event.MouseEvent.MOUSE_ENTERED, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), me.getButton() );
						enterEvent = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( this.src, enterEvent, enterComponent );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println( enterEvent );
						super.dispatchEvent( enterEvent );
						this.last = descendant;
					}
				} else if( ( id == java.awt.event.MouseEvent.MOUSE_PRESSED ) || ( id == java.awt.event.MouseEvent.MOUSE_CLICKED ) || ( id == java.awt.event.MouseEvent.MOUSE_RELEASED ) || ( id == java.awt.event.MouseEvent.MOUSE_DRAGGED ) ) {
					if( this.last != null ) {
						e = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( this.src, me, this.last );
					}
				}
			}
		}
		super.dispatchEvent( e );
	}
}
