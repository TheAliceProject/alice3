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

import org.alice.interact.MovementDirection;
import org.alice.interact.event.ManipulationEvent;

/**
 * @author David Culyba
 */
public class ManipulationHandle2DCameraStrafe extends ImageBasedManipulationHandle2D {

	private static enum ControlState implements ImageBasedManipulationHandle2D.ImageState {
		Inactive( "images/slide.png" ),
		Highlighted( "images/slideHighlight.png" ),
		Down( "images/slideDown.png" ),
		DownLeft( "images/slideDownLeft.png" ),
		DownRight( "images/slideDownRight.png" ),
		Up( "images/slideUp.png" ),
		UpLeft( "images/slideUpLeft.png" ),
		UpRight( "images/slideUpRight.png" ),
		Left( "images/slideLeft.png" ),
		Right( "images/slideRight.png" );

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

	private MovementDirection handleUp = MovementDirection.UP;
	private MovementDirection handleDown = MovementDirection.DOWN;
	private MovementDirection handleRight = MovementDirection.RIGHT;
	private MovementDirection handleLeft = MovementDirection.LEFT;

	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean movingUp = false;
	private boolean movingDown = false;

	public ManipulationHandle2DCameraStrafe() {
		super( "images/slideMask.png" );
	}

	public void remapDirections( MovementDirection up, MovementDirection down, MovementDirection left, MovementDirection right ) {
		this.handleUp = up;
		this.handleDown = down;
		this.handleLeft = left;
		this.handleRight = right;
	}

	@Override
	protected ImageState getStateForManipulationStatus() {
		if( this.movingDown && !this.movingLeft && !this.movingRight ) {
			return ControlState.Down;
		} else if( this.movingDown && this.movingLeft ) {
			return ControlState.DownLeft;
		} else if( this.movingDown && this.movingRight ) {
			return ControlState.DownRight;
		} else if( this.movingUp && !this.movingLeft && !this.movingRight ) {
			return ControlState.Up;
		} else if( this.movingUp && this.movingLeft ) {
			return ControlState.UpLeft;
		} else if( this.movingUp && this.movingRight ) {
			return ControlState.UpRight;
		} else if( this.movingLeft && !this.movingUp && !this.movingDown ) {
			return ControlState.Left;
		} else if( this.movingRight && !this.movingUp && !this.movingDown ) {
			return ControlState.Right;
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
		if( event.getMovementDescription().direction == handleUp ) {
			this.movingUp = isActive;
		} else if( event.getMovementDescription().direction == handleDown ) {
			this.movingDown = isActive;
		} else if( event.getMovementDescription().direction == handleRight ) {
			this.movingRight = isActive;
		} else if( event.getMovementDescription().direction == handleLeft ) {
			this.movingLeft = isActive;
		}
	}
}
