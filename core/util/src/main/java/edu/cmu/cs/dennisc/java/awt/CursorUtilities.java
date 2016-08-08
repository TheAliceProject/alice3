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
public class CursorUtilities {
	private static final java.util.Map<java.awt.Component, java.util.Stack<java.awt.Cursor>> mapComponentToStack = new java.util.HashMap<java.awt.Component, java.util.Stack<java.awt.Cursor>>();
	public static final java.awt.Cursor NULL_CURSOR;
	static {
		java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		java.awt.image.MemoryImageSource source = new java.awt.image.MemoryImageSource( 1, 1, new int[] { 0 }, 0, 1 );
		java.awt.Image nullImage = toolkit.createImage( source );
		NULL_CURSOR = toolkit.createCustomCursor( nullImage, new java.awt.Point( 0, 0 ), "NULL_CURSOR" );
	}

	private static java.util.Stack<java.awt.Cursor> getStack( java.awt.Component component ) {
		java.util.Stack<java.awt.Cursor> rv = CursorUtilities.mapComponentToStack.get( component );
		if( rv != null ) {
			//pass
		} else {
			rv = new java.util.Stack<java.awt.Cursor>();
			CursorUtilities.mapComponentToStack.put( component, rv );
		}
		return rv;
	}

	public static void pushAndSet( java.awt.Component component, java.awt.Cursor nextCursor ) {
		if( nextCursor != null ) {
			//pass
		} else {
			nextCursor = NULL_CURSOR;
		}
		java.util.Stack<java.awt.Cursor> stack = CursorUtilities.getStack( component );
		java.awt.Cursor prevCursor = component.getCursor();
		stack.push( prevCursor );
		component.setCursor( nextCursor );
	}

	public static void pushAndSetPredefinedCursor( java.awt.Component component, int nextCursorType ) {
		pushAndSet( component, java.awt.Cursor.getPredefinedCursor( nextCursorType ) );

	}

	public static java.awt.Cursor popAndSet( java.awt.Component component ) {
		java.util.Stack<java.awt.Cursor> stack = CursorUtilities.getStack( component );
		java.awt.Cursor prevCursor = stack.pop();
		component.setCursor( prevCursor );
		return prevCursor;
	}
}
