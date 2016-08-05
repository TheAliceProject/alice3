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

package org.alice.stageide.sceneeditor.interact.croquet;

import edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;

public class PredeterminedSetOrthographicPicturePlaneActionOperation extends org.lgna.croquet.ActionOperation {
	private boolean isDoRequired;
	private edu.cmu.cs.dennisc.animation.Animator animator;
	private OrthographicCamera orthoCamera;
	private double previousPicturePlaneHeight;
	private double nextPicturePlaneHeight;

	private String editPresentationKey;

	public PredeterminedSetOrthographicPicturePlaneActionOperation( org.lgna.croquet.Group group, boolean isDoRequired, edu.cmu.cs.dennisc.animation.Animator animator, OrthographicCamera orthoCamera, double previousPicturePlaneHeight, double nextPicturePlaneHeight, String editPresentationKey ) {
		super( group, java.util.UUID.fromString( "67faf90c-97c6-40d4-9ddb-f31f22003682" ) );
		this.isDoRequired = isDoRequired;
		this.animator = animator;
		this.orthoCamera = orthoCamera;

		this.previousPicturePlaneHeight = previousPicturePlaneHeight;
		this.nextPicturePlaneHeight = nextPicturePlaneHeight;

		this.editPresentationKey = editPresentationKey;
	}

	private void setHeightOnCamera( OrthographicCamera camera, double height )
	{
		ClippedZPlane picturePlane = PredeterminedSetOrthographicPicturePlaneActionOperation.this.orthoCamera.picturePlane.getValue();
		picturePlane.setHeight( height );
		PredeterminedSetOrthographicPicturePlaneActionOperation.this.orthoCamera.picturePlane.setValue( picturePlane );
	}

	private void setPicturePlaneHeight( final double height ) {
		if( this.animator != null ) {
			class ZoomAnimation extends DoubleAnimation {
				public ZoomAnimation() {
					super( 0.5, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY, orthoCamera.picturePlane.getValue().getHeight(), height );
				}

				@Override
				protected void updateValue( Double newHeight ) {
					setHeightOnCamera( orthoCamera, newHeight.doubleValue() );
				}
			}
			this.animator.invokeLater( new ZoomAnimation(), null );
		} else
		{
			setHeightOnCamera( orthoCamera, height );
		}

	}

	@Override
	protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
		step.commitAndInvokeDo( new org.lgna.croquet.edits.AbstractEdit( step ) {
			@Override
			protected void doOrRedoInternal( boolean isDo ) {
				if( isDo && ( isDoRequired == false ) ) {
					//pass
				} else {
					setPicturePlaneHeight( nextPicturePlaneHeight );
				}
			}

			@Override
			protected void undoInternal() {
				setPicturePlaneHeight( previousPicturePlaneHeight );
			}

			@Override
			protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
				rv.append( editPresentationKey );
			}
		} );
	}

}
