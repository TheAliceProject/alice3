/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class Hole extends Feature {
	private static final int PAD = 4;
	private static final java.awt.Insets PAINT_INSETS = new java.awt.Insets( PAD, PAD, PAD, PAD );
	private static final java.awt.Paint HIGHLIGHT_PAINT = new java.awt.Color(255, 255, 0, 23);
//	private static final java.awt.Stroke HOLE_BEVEL_STROKE = new java.awt.BasicStroke(2.0f);
	private static final java.awt.Stroke[] HIGHLIGHT_STROKES;
	static {
		final int N = 8;
		HIGHLIGHT_STROKES = new java.awt.Stroke[N];
		for (int i = 0; i < N; i++) {
			HIGHLIGHT_STROKES[i] = new java.awt.BasicStroke((i + 1) * 5.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);
		}
	};

	public Hole( edu.cmu.cs.dennisc.croquet.TrackableShape trackableShape, ConnectionPreference connectionPreference ) {
		super( trackableShape, connectionPreference );
	}
	
	@Override
	protected java.awt.Insets getContainsInsets() {
		return null;
	}
	@Override
	protected java.awt.Insets getPaintInsets() {
		return PAINT_INSETS;
	}

	@Override
	protected void paint(java.awt.Graphics2D g2, java.awt.Shape shape) {
		java.awt.Shape prevClip = g2.getClip();
		java.awt.Paint prevPaint = g2.getPaint();
		java.awt.Stroke prevStroke = g2.getStroke();
		
		java.awt.geom.Area area = new java.awt.geom.Area(prevClip);
		area.subtract(new java.awt.geom.Area(shape));

		g2.setClip(area);

		g2.setPaint(HIGHLIGHT_PAINT);
		for (java.awt.Stroke stroke : HIGHLIGHT_STROKES) {
			g2.setStroke(stroke);
			g2.draw(shape);
		}

		g2.setStroke( prevStroke );
		g2.setPaint( prevPaint );
		g2.setClip(prevClip);

//		java.awt.Stroke prevStroke = g2.getStroke();
//		g2.setStroke(HOLE_BEVEL_STROKE);
//
//		// g2.setPaint( java.awt.Color.GRAY );
//		// g2.draw3DRect(componentBounds.x, componentBounds.y,
//		// componentBounds.width, componentBounds.height, false);
//
//		int x0 = componentBounds.x;
//		int y0 = componentBounds.y;
//		int x1 = componentBounds.x + componentBounds.width;
//		int y1 = componentBounds.y + componentBounds.height;
//		g2.setPaint(java.awt.Color.LIGHT_GRAY);
//		g2.drawLine(x1, y0, x1, y1);
//		g2.drawLine(x1, y1, x0, y1);
//		g2.setPaint(java.awt.Color.DARK_GRAY);
//		g2.drawLine(x0, y1, x0, y0);
//		g2.drawLine(x0, y0, x1, y0);
//
//		g2.setStroke(prevStroke);
	}
}
