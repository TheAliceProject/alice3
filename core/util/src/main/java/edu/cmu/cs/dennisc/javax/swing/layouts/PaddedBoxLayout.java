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
package edu.cmu.cs.dennisc.javax.swing.layouts;

/**
 * @author Dennis Cosgrove
 */
public class PaddedBoxLayout extends javax.swing.BoxLayout {
	private static int getPadCount( java.awt.Container target ) {
		return Math.max( target.getComponentCount() - 1, 0 );
	}

	public PaddedBoxLayout( java.awt.Container target, int axis, int pad ) {
		super( target, axis );
		this.axis = axis;
		this.pad = pad;
	}

	@Override
	public java.awt.Dimension minimumLayoutSize( java.awt.Container target ) {
		java.awt.Dimension rv = super.minimumLayoutSize( target );
		final int NUM_PADS = getPadCount( target );
		switch( this.axis ) {
		case Y_AXIS:
		case PAGE_AXIS:
			rv.height += NUM_PADS * this.pad;
			break;
		case X_AXIS:
		case LINE_AXIS:
			rv.width += NUM_PADS * this.pad;
			break;
		}
		return rv;
	}

	@Override
	public java.awt.Dimension preferredLayoutSize( java.awt.Container target ) {
		java.awt.Dimension rv = super.preferredLayoutSize( target );
		final int NUM_PADS = getPadCount( target );
		switch( this.axis ) {
		case Y_AXIS:
		case PAGE_AXIS:
			rv.height += NUM_PADS * this.pad;
			break;
		case X_AXIS:
		case LINE_AXIS:
			rv.width += NUM_PADS * this.pad;
			break;
		}
		return rv;
	}

	@Override
	public java.awt.Dimension maximumLayoutSize( java.awt.Container target ) {
		java.awt.Dimension rv = super.maximumLayoutSize( target );
		final int NUM_PADS = getPadCount( target );
		switch( this.axis ) {
		case Y_AXIS:
		case PAGE_AXIS:
			rv.height += NUM_PADS * this.pad;
			break;
		case X_AXIS:
		case LINE_AXIS:
			rv.width += NUM_PADS * this.pad;
			break;
		}
		return rv;
	}

	@Override
	public void layoutContainer( java.awt.Container target ) {
		super.layoutContainer( target );
		final int N = target.getComponentCount();
		for( int i = 0; i < N; i++ ) {
			java.awt.Component componentI = target.getComponent( i );
			java.awt.Point p = componentI.getLocation();
			//todo: handle right to left
			switch( this.axis ) {
			case Y_AXIS:
			case PAGE_AXIS:
				p.y += i * this.pad;
				break;
			case X_AXIS:
			case LINE_AXIS:
				p.x += i * this.pad;
				break;
			}
			componentI.setLocation( p );
		}
	}

	private final int pad;
	private final int axis;
}
