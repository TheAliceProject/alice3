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
public abstract class LenientMouseClickAdapter implements javax.swing.event.MouseInputListener /* java.awt.event.MouseListener, java.awt.event.MouseMotionListener */{
	private static final long CLICK_THRESHOLD_MILLIS = 500; //todo: query windowing system 
	private static final long CLICK_THRESHOLD_PIXELS_SQUARED = 25; //todo: query windowing system 
	private boolean isStillClick = false;
	private boolean isStillUnclick = false;
	private java.awt.event.MouseEvent ePressed = null;
	private java.awt.event.MouseEvent eReleased = null;
	private int count = 0;

	protected abstract void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote );

	private boolean isWithinThreshold( java.awt.event.MouseEvent eThen, java.awt.event.MouseEvent eNow ) {
		if( eThen != null ) {
			long whenDelta = eNow.getWhen() - eThen.getWhen();
			if( whenDelta < CLICK_THRESHOLD_MILLIS ) {
				int xDelta = eNow.getX() - eThen.getX();
				int yDelta = eNow.getY() - eThen.getY();
				int distanceSquared = ( xDelta * xDelta ) + ( yDelta * yDelta );
				return distanceSquared <= CLICK_THRESHOLD_PIXELS_SQUARED;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void updateStillClick( java.awt.event.MouseEvent eNow ) {
		if( this.isStillClick ) {
			this.isStillClick = this.isWithinThreshold( this.ePressed, eNow );
		}
	}

	private void updateStillUnclick( java.awt.event.MouseEvent eNow ) {
		if( this.isStillUnclick ) {
			this.isStillUnclick = this.isWithinThreshold( this.eReleased, eNow );
		}
	}

	@Override
	public final void mouseEntered( java.awt.event.MouseEvent e ) {
	}

	@Override
	public final void mouseExited( java.awt.event.MouseEvent e ) {
	}

	@Override
	public final void mousePressed( java.awt.event.MouseEvent e ) {
		this.updateStillUnclick( e );
		if( this.isStillUnclick ) {
			//pass
		} else {
			this.count = 0;
		}
		this.isStillClick = true;
		this.ePressed = e;
	}

	@Override
	public final void mouseReleased( java.awt.event.MouseEvent e ) {
		this.updateStillClick( e );
		if( this.isStillClick ) {
			this.count++;
			this.mouseQuoteClickedUnquote( e, this.count );
		} else {
			this.count = 0;
		}
		this.isStillUnclick = true;
		this.eReleased = e;
	}

	@Override
	public final void mouseClicked( java.awt.event.MouseEvent e ) {
	}

	@Override
	public final void mouseMoved( java.awt.event.MouseEvent e ) {
		this.updateStillUnclick( e );
	}

	@Override
	public final void mouseDragged( java.awt.event.MouseEvent e ) {
		this.updateStillClick( e );
	}
}
