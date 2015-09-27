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
package edu.cmu.cs.dennisc.render.gl.imp.adapters.graphics;

import edu.cmu.cs.dennisc.scenegraph.graphics.BubbleManager;
import edu.cmu.cs.dennisc.scenegraph.graphics.OnscreenBubble;

public abstract class GlrBubble<T extends edu.cmu.cs.dennisc.scenegraph.graphics.Bubble> extends GlrShapeEnclosedText<T> {
	private java.awt.geom.Point2D.Float originOfTail = new java.awt.geom.Point2D.Float();
	private java.awt.geom.Point2D.Float bodyConnectionLocationOfTail = new java.awt.geom.Point2D.Float();
	private java.awt.geom.Point2D.Float textBoundsOffset = new java.awt.geom.Point2D.Float();

	@Override
	protected float getWrapWidth( java.awt.Rectangle actualViewport ) {
		return (float)( actualViewport.getWidth() * 0.9 );
	}

	protected abstract void render(
			edu.cmu.cs.dennisc.render.Graphics2D g2,
			edu.cmu.cs.dennisc.render.RenderTarget renderTarget,
			java.awt.Rectangle actualViewport,
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera,
			edu.cmu.cs.dennisc.java.awt.MultilineText multilineText,
			java.awt.Font font,
			java.awt.Color textColor,
			float wrapWidth,
			java.awt.Color fillColor,
			java.awt.Color outlineColor,
			OnscreenBubble bubble,
			double portion );

	@Override
	protected void render(
			edu.cmu.cs.dennisc.render.Graphics2D g2,
			edu.cmu.cs.dennisc.render.RenderTarget renderTarget,
			java.awt.Rectangle actualViewport,
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera,
			edu.cmu.cs.dennisc.java.awt.MultilineText multilineText,
			java.awt.Font font,
			java.awt.Color textColor,
			float wrapWidth,
			java.awt.Color fillColor,
			java.awt.Color outlineColor ) {
		edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator originator = this.owner.getOriginator();
		if( originator != null ) {
			g2.setFont( font );
			java.awt.geom.Dimension2D size = multilineText.getDimension( g2, wrapWidth );
			originator.calculate( originOfTail, bodyConnectionLocationOfTail, textBoundsOffset, this.owner, renderTarget, actualViewport, camera, size );
			OnscreenBubble bubble = BubbleManager.getInstance().getBubble( this.owner );
			if( bubble == null )
			{
				float padding;
				if( this instanceof GlrThoughtBubble )
				{
					padding = font.getSize2D() * 1.2f;
				}
				else
				{
					padding = font.getSize2D() * .4f;
				}
				bubble = BubbleManager.getInstance().addBubble( this.owner, originOfTail, size, padding, actualViewport );
			}
			else
			{
				bubble.updateOriginOfTail( originOfTail, actualViewport );
			}

			this.render( g2, renderTarget, actualViewport, camera, multilineText, font, textColor, wrapWidth, fillColor, outlineColor, bubble, owner.portion.getValue() );
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.portion ) {
			//pass
		} else {
			super.propertyChanged( property );
		}
	}
}
