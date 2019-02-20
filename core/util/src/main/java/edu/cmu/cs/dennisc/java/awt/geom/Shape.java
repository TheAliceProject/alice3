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
package edu.cmu.cs.dennisc.java.awt.geom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Area;

/**
 * @author Dennis Cosgrove
 */
public abstract class Shape extends Transformable {
	private Paint m_fillPaint = Color.WHITE;
	private Paint m_drawPaint = Color.BLACK;
	private boolean m_isFilled = true;
	private boolean m_isDrawn = true;

	public boolean isFilled() {
		return m_isFilled;
	}

	public void setFilled( boolean isFilled ) {
		m_isFilled = isFilled;
	}

	public boolean isDrawn() {
		return m_isDrawn;
	}

	public void setDrawn( boolean isDrawn ) {
		m_isDrawn = isDrawn;
	}

	public Paint getDrawPaint() {
		return m_drawPaint;
	}

	public void setDrawPaint( Paint drawPaint ) {
		m_drawPaint = drawPaint;
	}

	public Paint getFillPaint() {
		return m_fillPaint;
	}

	public void setFillPaint( Paint fillPaint ) {
		m_fillPaint = fillPaint;
	}

	protected abstract java.awt.Shape getFillShape();

	protected abstract java.awt.Shape getDrawShape();

	@Override
	protected void paintComponent( GraphicsContext gc ) {
		Graphics2D g2 = gc.getAWTGraphics2D();

		if( isFilled() ) {
			java.awt.Shape fillShape = getFillShape();
			if( fillShape != null ) {
				g2.setPaint( m_fillPaint );
				g2.fill( fillShape );
			}
		}
		if( isDrawn() ) {
			java.awt.Shape drawShape = getDrawShape();
			if( drawShape != null ) {
				g2.setPaint( m_drawPaint );
				g2.draw( drawShape );
			}
		}
	}

	@Override
	protected Area update( Area rv, TransformContext tc ) {
		if( isFilled() ) {// TODO Auto-generated method stub
			java.awt.Shape fillShape = getFillShape();
			if( fillShape != null ) {
				Area area = new Area( fillShape );
				area.transform( tc.getAffineTransform() );
				rv.add( area );
			}
		}
		return rv;
	}
}
