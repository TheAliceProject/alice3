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

package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MoveToImageIcon extends ImageIcon {
	
	public static int SUB_ICON_WIDTH = 32;
	public static int SUB_ICON_HEIGHT = 32;
	
	private static int HORIZONTAL_OFFSET = -4;

	private Icon leftImage;
	private Icon rightImage;
	private Icon arrowImage;
	
	
	public MoveToImageIcon()
	{
		super();
		this.arrowImage = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.stageide.sceneeditor.StorytellingSceneEditor.class.getResource("images/moveToArrowIcon.png") );
	}
	
	public MoveToImageIcon(Icon leftImage, Icon rightImage)
	{
		this();
		this.setLeftImage(leftImage);
		this.setRightImage(rightImage);
		
	}

	private void setSizeIfNeeded() {
		if (this.leftImage != null && this.rightImage != null) {
            int totalHeight = Math.max( this.arrowImage.getIconHeight(), Math.max(this.leftImage.getIconHeight(), this.rightImage.getIconHeight() ) );
            int totalWidth = this.arrowImage.getIconWidth() + this.leftImage.getIconWidth() + this.rightImage.getIconWidth() + 2*HORIZONTAL_OFFSET;
            if (totalHeight != this.getIconHeight() || totalWidth != this.getIconWidth())
            {
            	this.setImage(new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB));
            }
		}
	}
	
	public void setLeftImage(Icon leftImage)
	{
		this.leftImage = leftImage;
		if (this.leftImage != null)
        {
            setSizeIfNeeded();
        }
	}
	
	public void setRightImage(Icon rightImage)
	{
		this.rightImage = rightImage;
		if (this.rightImage != null)
		{
			setSizeIfNeeded();
		}
	}
	
	@Override
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		// TODO Auto-generated method stub
		super.paintIcon(c, g, x, y);
		int xPos = HORIZONTAL_OFFSET;
		if (this.leftImage != null)
		{
			int yOffset = (int)((this.getIconHeight() - this.leftImage.getIconHeight()) *.5);
			this.leftImage.paintIcon(c, g, x+xPos, y + yOffset);
			xPos += this.leftImage.getIconWidth();
		}
		if (this.arrowImage != null) {
			int yOffset = (int)((this.getIconHeight() - this.arrowImage.getIconHeight()) *.5);
			this.arrowImage.paintIcon(c, g, x+xPos, y + yOffset);
			xPos += this.arrowImage.getIconWidth();
		}
		if (this.rightImage != null)
		{
			int yOffset = (int)((this.getIconHeight() - this.rightImage.getIconHeight()) *.5);
			this.rightImage.paintIcon(c, g, x+xPos, y + yOffset);
		}
	}
	
}
