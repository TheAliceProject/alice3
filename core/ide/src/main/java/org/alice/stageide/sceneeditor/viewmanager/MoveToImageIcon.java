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

package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MoveToImageIcon implements Icon {

	public static int SUB_ICON_WIDTH = 32;
	public static int SUB_ICON_HEIGHT = 32;

	private static int HORIZONTAL_OFFSET = -4;

	private Icon leftImage;
	private Icon disabledLeftImage;
	private Icon rightImage;
	private Icon disabledRightImage;
	private Icon arrowImage;
	private Icon disabledArrowImage;
	private Icon unknownImage;
	private Icon disabledUnknownImage;

	public MoveToImageIcon()
	{
		super();
		this.arrowImage = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.stageide.sceneeditor.StorytellingSceneEditor.class.getResource( "images/moveToArrowIcon.png" ) );
		this.disabledArrowImage = desaturate( this.arrowImage );
		this.unknownImage = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.stageide.sceneeditor.StorytellingSceneEditor.class.getResource( "images/unknownIcon.png" ) );
		this.disabledUnknownImage = desaturate( this.unknownImage );
	}

	public MoveToImageIcon( Icon leftImage, Icon rightImage )
	{
		this();
		this.setLeftImage( leftImage );
		this.setRightImage( rightImage );
	}

	private Icon getLeftIcon() {
		return getLeftIcon( false );
	}

	private Icon getLeftIcon( boolean disabled ) {
		if( this.leftImage != null ) {
			if( disabled ) {
				return this.disabledLeftImage;
			}
			else {
				return this.leftImage;
			}
		}
		if( disabled ) {
			return this.disabledUnknownImage;
		}
		else {
			return this.unknownImage;
		}
	}

	private Icon getRightIcon() {
		return getRightIcon( false );
	}

	private Icon getRightIcon( boolean disabled ) {
		if( this.rightImage != null ) {
			if( disabled ) {
				return this.disabledRightImage;
			}
			else {
				return this.rightImage;
			}
		}
		if( disabled ) {
			return this.disabledUnknownImage;
		}
		else {
			return this.unknownImage;
		}
	}

	private Icon getArrowIcon( boolean disabled ) {
		if( disabled ) {
			return this.disabledArrowImage;
		}
		else {
			return this.arrowImage;
		}
	}

	@Override
	public int getIconWidth() {
		return this.getLeftIcon().getIconWidth() + this.arrowImage.getIconWidth() + this.getRightIcon().getIconWidth() + ( HORIZONTAL_OFFSET * 2 );
	}

	@Override
	public int getIconHeight() {
		return Math.max( this.arrowImage.getIconHeight(), Math.max( this.getLeftIcon().getIconHeight(), this.getRightIcon().getIconHeight() ) );
	}

	public void setLeftImage( Icon leftImage )
	{
		this.leftImage = leftImage;
		this.disabledLeftImage = desaturate( this.leftImage );
	}

	public void setRightImage( Icon rightImage )
	{
		this.rightImage = rightImage;
		this.disabledRightImage = desaturate( this.rightImage );
	}

	public static BufferedImage desaturate( BufferedImage source ) {
		ColorConvertOp colorConvert =
				new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null );
		colorConvert.filter( source, source );
		return source;
	}

	public static Icon desaturate( Icon source ) {
		BufferedImage imgSrc = null;
		if( source instanceof ImageIcon )
		{
			Image image = ( (ImageIcon)source ).getImage();
			if( image instanceof BufferedImage ) {
				imgSrc = (BufferedImage)image;
			}
		}
		if( ( imgSrc == null ) && ( source != null ) ) {
			imgSrc = new BufferedImage( source.getIconWidth(), source.getIconHeight(), BufferedImage.TYPE_INT_ARGB );
			source.paintIcon( null, imgSrc.getGraphics(), 0, 0 );
		}
		if( imgSrc != null ) {
			ColorConvertOp colorConvert =
					new ColorConvertOp( ColorSpace.getInstance( ColorSpace.CS_GRAY ), null );
			colorConvert.filter( imgSrc, imgSrc );
			return new javax.swing.ImageIcon( imgSrc );
		}
		return source;
	}

	@Override
	public synchronized void paintIcon( Component c, Graphics g, int x, int y ) {
		int xPos = HORIZONTAL_OFFSET;
		int yOffset = (int)( ( this.getIconHeight() - this.getLeftIcon().getIconHeight() ) * .5 );

		boolean disabled = ( c != null ) && !c.isEnabled();

		this.getLeftIcon( disabled ).paintIcon( c, g, x + xPos, y + yOffset );
		xPos += this.getLeftIcon( disabled ).getIconWidth();
		yOffset = (int)( ( this.getIconHeight() - this.getArrowIcon( disabled ).getIconHeight() ) * .5 );
		this.getArrowIcon( disabled ).paintIcon( c, g, x + xPos, y + yOffset );
		xPos += this.getArrowIcon( disabled ).getIconWidth();
		yOffset = (int)( ( this.getIconHeight() - this.getRightIcon().getIconHeight() ) * .5 );
		this.getRightIcon( disabled ).paintIcon( c, g, x + xPos, y + yOffset );
		//		if (!c.isEnabled()) {
		//			if (g instanceof java.awt.Graphics2D) {
		//				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		//				g2.setPaint( org.lgna.croquet.components.PaintUtilities.getDisabledTexturePaint() );
		//				g2.fillRect( x, y, this.getIconWidth(), this.getIconHeight() );
		//			}
		//		}
	}

}
