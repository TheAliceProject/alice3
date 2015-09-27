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
package edu.cmu.cs.dennisc.java.awt.event;

/**
 * @author Dennis Cosgrove
 */
public class MouseEventUtilities {
	public static boolean isQuoteLeftUnquoteMouseButton( java.awt.event.MouseEvent e ) {
		if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
				return e.isControlDown() == false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean isQuoteRightUnquoteMouseButton( java.awt.event.MouseEvent e ) {
		if( javax.swing.SwingUtilities.isRightMouseButton( e ) ) {
			return true;
		} else {
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
				if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
					return e.isControlDown();
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public static java.awt.event.MouseEvent performPlatformFilter( java.awt.event.MouseEvent original ) {
		java.awt.event.MouseEvent rv;
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			int completeModifiers = InputEventUtilities.getCompleteModifiers( original );
			int filteredModifiers = InputEventUtilities.performPlatformModifiersFilter( completeModifiers );
			if( completeModifiers == filteredModifiers ) {
				rv = original;
			} else {
				//todo:
				boolean isPopupTrigger = original.isPopupTrigger();

				rv = new java.awt.event.MouseEvent( original.getComponent(), original.getID(), original.getWhen(), filteredModifiers, original.getX(), original.getY(), original.getClickCount(), isPopupTrigger );
			}
		} else {
			rv = original;
		}
		return rv;
	}

	public static java.awt.event.MouseEvent convertMouseEvent( java.awt.Component source, java.awt.event.MouseEvent sourceEvent, java.awt.Component destination ) {
		int modifiers = sourceEvent.getModifiers();
		int modifiersEx = sourceEvent.getModifiersEx();
		int modifiersComplete = modifiers | modifiersEx;
		java.awt.event.MouseEvent me = javax.swing.SwingUtilities.convertMouseEvent( source, sourceEvent, destination );
		if( me instanceof java.awt.event.MouseWheelEvent ) {
			java.awt.event.MouseWheelEvent mwe = (java.awt.event.MouseWheelEvent)me;
			return new java.awt.event.MouseWheelEvent( me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation() );
		} else if( me instanceof javax.swing.event.MenuDragMouseEvent ) {
			javax.swing.event.MenuDragMouseEvent mdme = (javax.swing.event.MenuDragMouseEvent)me;
			return new javax.swing.event.MenuDragMouseEvent( me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), mdme.getPath(), mdme.getMenuSelectionManager() );
		} else {
			return new java.awt.event.MouseEvent( me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger() );
		}
	}
}
