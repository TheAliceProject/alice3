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

import org.alice.interact.event.ManipulationEvent;

public class ManipulationHandle2DCameraZoom extends ImageBasedManipulationHandle2D {
	private static enum ControlState implements ImageBasedManipulationHandle2D.ImageState {
		Inactive( "images/zoom.png" ),
		Highlighted( "images/zoomHighlight.png" ),
		ZoomingIn( "images/zoomIn.png" ),
		ZoomingOut( "images/zoomOut.png" );

		private ControlState( String resourceString ) {
			javax.swing.Icon icon;
			try {
				icon = new javax.swing.ImageIcon( this.getClass().getResource( resourceString ) );
			} catch( Exception e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "cannot load", resourceString, this );
				icon = null;
			}
			this.icon = icon;
		}

		@Override
		public javax.swing.Icon getIcon() {
			return this.icon;
		}

		private final javax.swing.Icon icon;
	}

	public ManipulationHandle2DCameraZoom() {
		super( "images/zoomMask.png" );
	}

	@Override
	protected ImageState getStateForManipulationStatus() {
		if( this.zoomingIn ) {
			return ControlState.ZoomingIn;
		} else if( this.zoomingOut ) {
			return ControlState.ZoomingOut;
		}
		//If we're not moving in one of the directions, choose highlighted or inactive
		else if( this.state.isRollover() ) {
			return ControlState.Highlighted;
		} else {
			return ControlState.Inactive;
		}
	}

	@Override
	protected void setManipulationState( ManipulationEvent event, boolean isActive ) {
		switch( event.getMovementDescription().direction ) {
		case FORWARD:
			this.zoomingIn = isActive;
			break;
		case BACKWARD:
			this.zoomingOut = isActive;
			break;
		}
	}

	private boolean zoomingIn = false;
	private boolean zoomingOut = false;
}
