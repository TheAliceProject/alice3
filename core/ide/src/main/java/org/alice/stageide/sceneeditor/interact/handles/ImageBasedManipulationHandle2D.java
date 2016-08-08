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
package org.alice.stageide.sceneeditor.interact.handles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.handle.HandleRenderState;
import org.alice.interact.handle.ManipulationHandle2D;

/**
 * @author David Culyba
 */
public abstract class ImageBasedManipulationHandle2D extends ManipulationHandle2D {
	protected static interface ImageState {
		public javax.swing.Icon getIcon();
	}

	public ImageBasedManipulationHandle2D( String maskResourceName ) {
		BufferedImage image;
		try {
			image = edu.cmu.cs.dennisc.image.ImageUtilities.read( this.getClass().getResource( maskResourceName ) );
		} catch( Throwable t ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( maskResourceName, this );
			image = null;
		}
		this.imageMask = image;

		this.setStateBasedOnManipulationStatus();
		javax.swing.Icon icon = this.getIcon();
		Dimension size = edu.cmu.cs.dennisc.javax.swing.IconUtilities.newDimension( icon );
		this.setSize( size );
		this.setMinimumSize( size );
		this.setPreferredSize( size );
	}

	public Color getColor( int x, int y ) {
		if( this.contains( x, y ) ) {
			if( this.imageMask != null ) {
				int colorInt = this.imageMask.getRGB( x, y );
				return new Color( colorInt, true );
			}
		}
		return null;
	}

	@Override
	protected void updateVisibleState( HandleRenderState renderState ) {
		this.setStateBasedOnManipulationStatus();
	}

	private void setStateBasedOnManipulationStatus() {
		ImageState newState = this.getStateForManipulationStatus();
		if( newState != this.currentState ) {
			this.currentState = newState;
			this.setIcon( this.currentState.getIcon() );
		}
	}

	@Override
	public void activate( ManipulationEvent event ) {
		this.setManipulationState( event, true );
		this.setStateBasedOnManipulationStatus();
	}

	@Override
	public void deactivate( ManipulationEvent event ) {
		this.setManipulationState( event, false );
		this.setStateBasedOnManipulationStatus();
	}

	protected abstract ImageState getStateForManipulationStatus();

	protected abstract void setManipulationState( ManipulationEvent event, boolean isActive );

	private ImageState currentState;
	private final BufferedImage imageMask;
}
