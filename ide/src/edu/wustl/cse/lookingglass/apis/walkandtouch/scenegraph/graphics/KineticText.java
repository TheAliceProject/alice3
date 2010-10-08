/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.*;

import edu.cmu.cs.dennisc.lookingglass.Graphics2D;
import edu.cmu.cs.dennisc.lookingglass.LookingGlass;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

public class KineticText extends OverHeadTextGraphic {

	public float Alpha = 1f;
	public int size = 30;
	public int ax = 0;
	public int ay = 0;
	public int type;
	public Color c;
	public int startSize;
	public int endSize;

	public int height;
	public int width;
	public int degree;
	public int startD;
	public int endD;
	public int moveX = 0;
	public int moveY = 0;
	public int currentX = 0;
	public int currentY = 0;
	public int dir;

	public Point endPoint = new Point( 400, 400 );
	public Point startPoint;
	public boolean top = false;
	public boolean bottom = false;
	public boolean left = false;
	public boolean right = false;

	public KineticText( String str, Point p1, int min, int max, int type, int start, int end, int startD, int endD, int dir ) {
		super( str, p1, 0, min, max );
		this.type = type;
		this.startSize = start;
		this.endSize = end;

		this.startD = startD;
		this.endD = endD;
		this.degree = startD;
		this.dir = dir;

		this.startPoint = p1;

	}


	public void moveLeft( double cd, double ts ) {
		int inc = (int)(currentX / cd);
		moveX = (int)(inc * ts);
	}

	public void moveRight( double cd, double ts ) {
		int inc = (int)((width - currentX) / cd);
		moveX = -(int)(inc * ts);
	}

	public void moveUp( double cd, double ts ) {
		int inc = (int)(currentY / cd);
		moveY = (int)(inc * ts);
	}

	public void moveDown( double cd, double ts ) {
		int inc = (int)((height - currentY) / cd);
		moveY = -(int)(inc * ts);
	}

	public void moveToward( double p ) {
		int xInc = -(int)(((endPoint.x - startPoint.x) * p));
		int yInc = -(int)(((endPoint.y - startPoint.y) * p));
		moveX = (int)(xInc);
		moveY = (int)(yInc);
		System.out.println( moveX );
		System.out.println( moveY );

	}

	private AlphaComposite makeComposite( float alpha ) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance( type, alpha ));
	}

}
