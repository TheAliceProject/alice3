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

package org.alice.ide.swing.icons;
import javax.swing.Icon;

public class ColorIcon implements Icon 
{

	private static final int DEFAULT_SIZE = 15;
	private java.awt.Color fillColor;
	private java.awt.Color outlineColor;
	private int width;
	private int height;
	
	public ColorIcon( java.awt.Color color ) {
		this(color, DEFAULT_SIZE);
	}
	
	public ColorIcon( java.awt.Color color, int size) {
		this(color, size, size);
	}
	
	public ColorIcon( java.awt.Color color, int width, int height) {
		this.fillColor = color;
		this.width = width;
		this.height = height;
		float[] hsb = new float[ 3 ];
		//todo
		java.awt.Color.RGBtoHSB( this.fillColor.getRed(), this.fillColor.getGreen(), this.fillColor.getBlue(), hsb );
		if( hsb[ 2 ] > 0.9f ) {
			this.outlineColor = java.awt.Color.GRAY;
		} else {
			this.outlineColor = null;
		}
	}

	public ColorIcon( java.awt.Color color, java.awt.Dimension size ) {
		this( color, size.width, size.height );
	}
	
	public int getIconWidth() {
		return this.width + 3 + 2;
	}
	
	public int getIconHeight() {
		return this.height + 3;
	}
	
	public void paintIcon( java.awt.Component arg0, java.awt.Graphics g, int x, int y ) {
		g.setColor( this.fillColor );
		g.fillRect( x + 1 + 2, y + 1, this.width, this.height );
		if( this.outlineColor != null ) {
			g.setColor( this.outlineColor );
			g.drawRect( x + 1 + 2, y + 1, this.width, this.height );
		}
	}

}
