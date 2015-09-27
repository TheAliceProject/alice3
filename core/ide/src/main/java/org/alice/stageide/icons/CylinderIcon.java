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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class CylinderIcon extends ShapeIcon {
	public CylinderIcon( java.awt.Dimension size ) {
		super( size );
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2, int width, int height, java.awt.Paint fillPaint, java.awt.Paint drawPaint ) {
		float capHeight = height * 0.2f;
		float x = 0.1f * width;
		float w = 0.8f * width;
		java.awt.geom.Ellipse2D topCap = new java.awt.geom.Ellipse2D.Float( x, 0, w, capHeight );
		java.awt.geom.Ellipse2D bottomCap = new java.awt.geom.Ellipse2D.Float( x, height - capHeight, w, capHeight );
		java.awt.geom.Rectangle2D core = new java.awt.geom.Rectangle2D.Float( x, capHeight * 0.5f, w, height - capHeight );
		java.awt.geom.Area area = new java.awt.geom.Area( core );
		area.add( new java.awt.geom.Area( bottomCap ) );

		g2.setPaint( fillPaint );
		g2.fill( area );
		g2.setPaint( drawPaint );
		g2.draw( area );

		g2.setPaint( fillPaint );
		g2.fill( topCap );
		g2.setPaint( drawPaint );
		g2.draw( topCap );
	}
}
