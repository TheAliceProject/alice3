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
package org.lgna.story.implementation.overlay;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.scenegraph.Layer;
import edu.cmu.cs.dennisc.scenegraph.graphics.Bubble;
import edu.cmu.cs.dennisc.scenegraph.graphics.BubbleManager;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.DoubleProperty;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.implementation.PropertyOwnerImp;
import org.lgna.story.implementation.SceneImp;

import java.awt.Font;

/**
 * @author Dennis Cosgrove
 */
public abstract class BubbleImp extends PropertyOwnerImp {
	public BubbleImp( EntityImp imp, Bubble bubble, String text, Font font, Color4f textColor, Color4f fillColor, Color4f outlineColor ) {
		this.imp = imp;
		this.bubble = bubble;
		this.bubble.text.setValue( text );
		this.bubble.font.setValue( font );
		this.bubble.textColor.setValue( textColor );
		this.bubble.fillColor.setValue( fillColor );
		this.bubble.outlineColor.setValue( outlineColor );
	}

	public Bubble getBubble() {
		return this.bubble;
	}

	@Override
	public ProgramImp getProgram() {
		return this.imp.getProgram();
	}

	private final EntityImp imp;
	private final Bubble bubble;

	private static final double CLOSE_ENOUGH_TO_ZERO = 0.001;
	public final DoubleProperty portion = new DoubleProperty( this ) {
		private Layer getSgLayer() {
			SceneImp scene = imp.getScene();
			CameraImp<?> camera = scene.findFirstCamera();
			return camera.getPostRenderLayer();
		}

		@Override
		public Double getValue() {
			return bubble.portion.getValue();
		}

		@Override
		protected void handleSetValue( Double value ) {
			double prevValue = bubble.portion.getValue();
			double nextValue = value.doubleValue();
			if( prevValue < CLOSE_ENOUGH_TO_ZERO ) {
				if( nextValue < CLOSE_ENOUGH_TO_ZERO ) {
					//pass
				} else {
					this.getSgLayer().addGraphic( bubble );
				}
			} else {
				if( nextValue < CLOSE_ENOUGH_TO_ZERO ) {
					this.getSgLayer().removeGraphic( bubble );
					//todo:
					BubbleManager.getInstance().removeBubble( bubble );
				}
			}
			bubble.portion.setValue( nextValue );
		}
	};
}
