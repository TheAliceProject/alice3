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
package org.alice.stageide.person.components;

/**
 * @author Dennis Cosgrove
 */
public class PersonViewer extends org.alice.stageide.modelviewer.ModelViewer {
	private org.alice.interact.CreateASimDragAdapter dragAdapter = new org.alice.interact.CreateASimDragAdapter();

	private static class SingletonHolder {
		private static PersonViewer instance = new PersonViewer();
	}

	public static PersonViewer getInstance( org.alice.stageide.person.PersonImp personImp ) {
		SingletonHolder.instance.setPerson( personImp );
		return SingletonHolder.instance;
	}

	private PersonViewer() {
	}

	private void positionAndOrientCamera( double height, int index, double duration ) {
		double xzFactor;
		if( index == 0 ) {
			xzFactor = 2.0 * height;
		} else {
			xzFactor = 1.0;
		}
		double yFactor;
		if( index == 0 ) {
			yFactor = 1.5;
		} else {
			yFactor = 1.5;
		}
		yFactor *= 0.65;
		xzFactor *= 0.65;
		if( this.getScene() != null ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevPOV = this.getCamera().getLocalTransformation();
			this.getCamera().setTransformation( this.getScene().createOffsetStandIn( -0.3 * xzFactor, height * yFactor, -height * xzFactor ) );
			this.getCamera().setOrientationOnlyToPointAt( this.getScene().createOffsetStandIn( 0, height * .5, 0 ) );
			edu.cmu.cs.dennisc.animation.Animator animator = this.getAnimator();
			if( ( duration > 0.0 ) && ( animator != null ) ) {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextPOV = this.getCamera().getLocalTransformation();
				this.getCamera().setLocalTransformation( prevPOV );

				edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation povAnimation = new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( this.getCamera().getSgComposite(), edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT, null, nextPOV );
				povAnimation.setDuration( duration );

				animator.completeAll( null );
				animator.invokeLater( povAnimation, null );
			}
		}
	}

	public org.alice.stageide.person.PersonImp getPerson() {
		return (org.alice.stageide.person.PersonImp)this.getModel();
	}

	public void setPerson( org.alice.stageide.person.PersonImp person ) {
		this.setModel( person );
		this.dragAdapter.setSelectedImplementation( person );
		if( person != null ) {
			double height = person.getSize().y;
			this.positionAndOrientCamera( height, 0, 0.0 );
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.dragAdapter.setOnscreenLookingGlass( this.getOnscreenLookingGlass() );
		this.dragAdapter.addCameraView( org.alice.interact.AbstractDragAdapter.CameraView.MAIN, this.getCamera().getSgCamera(), null );
		this.dragAdapter.makeCameraActive( this.getCamera().getSgCamera() );
	}
}
