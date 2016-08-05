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

public abstract class GlrText<T extends edu.cmu.cs.dennisc.scenegraph.graphics.Text> extends edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrGraphic<T> {
	private edu.cmu.cs.dennisc.java.awt.MultilineText multilineText;
	private java.awt.Font rememberedFont = null;
	private java.awt.Color textColor = null;

	protected abstract float getWrapWidth( java.awt.Rectangle actualViewport );

	protected abstract void render( edu.cmu.cs.dennisc.render.Graphics2D g2, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, edu.cmu.cs.dennisc.java.awt.MultilineText multilineText, java.awt.Font font, java.awt.Color textColor, float wrapWidth );

	private void forgetFontIfNecessary( edu.cmu.cs.dennisc.render.Graphics2D g2 ) {
		if( this.rememberedFont != null ) {
			g2.forget( this.rememberedFont );
			this.rememberedFont = null;
		}
	}

	@Override
	protected void render( edu.cmu.cs.dennisc.render.Graphics2D g2, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		String text = this.owner.text.getValue();
		java.awt.Font font = this.owner.font.getValue();
		if( font == this.rememberedFont ) {
			//pass
		} else {
			this.forgetFontIfNecessary( g2 );
			g2.remember( font );
			this.rememberedFont = font;
		}
		if( this.multilineText != null ) {
			//pass
		} else {
			this.multilineText = new edu.cmu.cs.dennisc.java.awt.MultilineText( text );
		}
		this.render( g2, renderTarget, actualViewport, camera, this.multilineText, this.rememberedFont, this.textColor, this.getWrapWidth( actualViewport ) );
	}

	@Override
	protected void forget( edu.cmu.cs.dennisc.render.Graphics2D g2 ) {
		this.forgetFontIfNecessary( g2 );
	}

	private void markMultilineTextInvalid() {
		this.multilineText = null;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.text ) {
			this.markMultilineTextInvalid();
		} else if( property == owner.font ) {
			this.markMultilineTextInvalid();
		} else if( property == owner.textColor ) {
			this.textColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.toAwtColor( this.owner.textColor.getValue() );
		} else {
			super.propertyChanged( property );
		}
	}
}
