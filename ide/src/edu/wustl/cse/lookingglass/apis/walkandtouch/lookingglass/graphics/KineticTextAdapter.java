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
package edu.wustl.cse.lookingglass.apis.walkandtouch.lookingglass.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import edu.cmu.cs.dennisc.lookingglass.Graphics2D;
import edu.cmu.cs.dennisc.lookingglass.LookingGlass;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

//goodness gracious this is horrible - dennisc
public class KineticTextAdapter extends OverHeadTextGraphicAdapter< edu.wustl.cse.lookingglass.apis.walkandtouch.scenegraph.graphics.KineticText > {
	@Override
	protected void render( Graphics2D g2, LookingGlass lookingGlass, AbstractCamera sgCamera, String str, int x1, int y1, int num, int min, int max ) {
		Font f = new Font( "SansSerif", Font.PLAIN, m_element.size );
		g2.setFont( f );
		//System.out.println( m_element.Alpha );
		m_element.c = new Color( 0f, 0f, 0f, m_element.Alpha );

		g2.setColor( m_element.c );

		//	int w= g2.getFontMetrics().stringWidth(str);

		//	g2.drawString(str, x1-w/2, y1-size/2);

		TextLayout ti;
		AffineTransform at = new AffineTransform();
		int tempX, tempY;

		int w = g2.getFontMetrics().stringWidth( str );
		ti = new TextLayout( str, f, new FontRenderContext( null, false, false ) );
		Shape shape = ti.getOutline( at );
		tempX = x1;
		tempY = y1 - 10;
		if( m_element.right )
			tempX = x1 - w;
		if( m_element.top )
			tempY = y1 + m_element.size + 10;
		if( !m_element.right && !m_element.left )
			tempX = x1 - w / 2;

		m_element.currentX = tempX;
		m_element.currentY = tempY;

		at.setToIdentity();
		at.translate( (double)(tempX - m_element.moveX), (double)tempY - m_element.moveY );

		at.rotate( Math.toRadians( m_element.degree ), (double)shape.getBounds2D().getWidth() / 2, (double)shape.getBounds2D().getHeight() / 2 ); //315 = -45 degrees angle as 0=360
		g2.transform( at );

		//Not Implemented will work when dennis implements it
		//g2.setComposite(makeComposite(Alpha));

		g2.fill( shape );
		g2.draw( shape );
		m_element.height = lookingGlass.getHeight();
		m_element.width = lookingGlass.getWidth();
	}
}
