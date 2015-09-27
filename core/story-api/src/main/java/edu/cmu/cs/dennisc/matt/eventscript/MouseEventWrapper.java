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
package edu.cmu.cs.dennisc.matt.eventscript;

import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 * @author Matt May
 */
public class MouseEventWrapper {
	private final MouseEvent event;
	private final int originalWidth;
	private final int originalHeight;

	public MouseEventWrapper( MouseEvent e ) {
		this.event = e;
		this.originalWidth = e.getComponent().getWidth();
		this.originalHeight = e.getComponent().getHeight();
	}

	public MouseEvent getTranslatedPointIfNecessary( Component component ) {
		int newWidth = component.getWidth();
		int newHeight = component.getHeight();
		int finalX;
		int finalY;
		if( ( this.originalWidth != newWidth ) || ( this.originalHeight != newHeight ) ) {
			finalX = (int)Math.round( ( event.getX() * newWidth ) / (double)originalWidth );
			finalY = (int)Math.round( ( event.getY() * newHeight ) / (double)originalHeight );
		} else {
			finalX = event.getX();
			finalY = event.getY();
		}
		return new MouseEvent( component,
				this.event.getID(),
				this.event.getWhen(),
				this.event.getModifiers() | this.event.getModifiersEx(),
				(int)Math.round( finalX ), (int)Math.round( finalY ),
				this.event.getXOnScreen(),
				this.event.getYOnScreen(),
				this.event.getClickCount(),
				this.event.isPopupTrigger(),
				this.event.getButton() );
	}
}
