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
package org.alice.stageide.sceneeditor.side;

/**
 * @author Dennis Cosgrove
 */
public class SnapDetailsToolPaletteCoreComposite extends org.lgna.croquet.ToolPaletteCoreComposite<org.alice.stageide.sceneeditor.side.views.SnapDetailsToolPaletteCoreView> {
	private final org.lgna.croquet.BooleanState isGridShowingState = this.createBooleanState( "isGridShowingState", true );
	private final org.lgna.croquet.BoundedDoubleState gridSpacingState = this.createBoundedDoubleState( "gridSpacingState", new BoundedDoubleDetails().initialValue( 0.5 ).minimum( 0.05 ).maximum( 10.0 ).stepSize( 0.05 ) );
	private final org.lgna.croquet.BooleanState isRotationState = this.createBooleanState( "isRotationState", true );
	private final org.lgna.croquet.BoundedDoubleState angleState = this.createBoundedDoubleState( "angleState", new BoundedDoubleDetails().initialValue( 30.0 ).minimum( 15.0 ).maximum( 360.0 ).stepSize( 15.0 ) );
	private final org.lgna.croquet.BooleanState isSnapToGroundEnabledState = this.createBooleanState( "isSnapToGroundEnabledState", true );

	public SnapDetailsToolPaletteCoreComposite() {
		super( java.util.UUID.fromString( "ce1cebee-b951-4294-b4d6-e5979b7d13a5" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP, false );
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	public org.lgna.croquet.BooleanState getIsGridShowingState() {
		return this.isGridShowingState;
	}

	public org.lgna.croquet.BoundedDoubleState getGridSpacingState() {
		return this.gridSpacingState;
	}

	public org.lgna.croquet.BooleanState getIsRotationState() {
		return this.isRotationState;
	}

	public org.lgna.croquet.BoundedDoubleState getAngleState() {
		return this.angleState;
	}

	public org.lgna.croquet.BooleanState getIsSnapToGroundEnabledState() {
		return this.isSnapToGroundEnabledState;
	}

	@Override
	protected org.alice.stageide.sceneeditor.side.views.SnapDetailsToolPaletteCoreView createView() {
		return new org.alice.stageide.sceneeditor.side.views.SnapDetailsToolPaletteCoreView( this );
	}
}
