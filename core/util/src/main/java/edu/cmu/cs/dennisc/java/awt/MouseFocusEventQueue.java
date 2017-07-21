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
public class MouseFocusEventQueue extends java.awt.EventQueue {
	private static MouseFocusEventQueue singleton;

	public static MouseFocusEventQueue getSingleton() {
		if( singleton != null ) {
			//pass
		} else {
			singleton = new MouseFocusEventQueue();
		}
		return singleton;
	}

	private java.awt.Component componentWithMouseFocus = null;

	public void pushComponentWithMouseFocus( java.awt.Component componentWithMouseFocus ) {
		assert this.componentWithMouseFocus == null;
		this.componentWithMouseFocus = componentWithMouseFocus;
		java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( toolkit.getSystemEventQueue() );

		//sadly, this breaks opengl on for some video card drivers
		toolkit.getSystemEventQueue().push( this );

		//edu.cmu.cs.dennisc.print.PrintUtilities.println( toolkit.getSystemEventQueue() );
	}

	public java.awt.Component popComponentWithMouseFocus() {
		assert this.componentWithMouseFocus != null;
		java.awt.Component rv = this.componentWithMouseFocus;
		this.componentWithMouseFocus = null;
		//java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( toolkit.getSystemEventQueue() );
		this.pop();
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( toolkit.getSystemEventQueue() );
		return rv;
	}

	@Override
	protected void dispatchEvent( java.awt.AWTEvent e ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "dispatchEvent", e );
		if( this.componentWithMouseFocus != null ) {
			if( e instanceof java.awt.event.MouseEvent ) {
				java.awt.event.MouseEvent me = (java.awt.event.MouseEvent)e;
				java.awt.Component curr = me.getComponent();
				if( curr == this.componentWithMouseFocus ) {
					//pass
				} else {
					e = edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( curr, me, this.componentWithMouseFocus );
				}
			}
		}
		super.dispatchEvent( e );
	}
}
