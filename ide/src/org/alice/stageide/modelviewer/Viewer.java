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

package org.alice.stageide.modelviewer;

/**
 * @author Dennis Cosgrove
 */
abstract class Viewer extends org.lgna.croquet.components.BorderPanel {
	private edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().createHeavyweightOnscreenLookingGlass();
	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	private org.lgna.story.implementation.SceneImp scene = new org.lgna.story.implementation.SceneImp( null );
	private org.lgna.story.implementation.SymmetricPerspectiveCameraImp camera = new org.lgna.story.implementation.SymmetricPerspectiveCameraImp( null );
	private org.lgna.story.implementation.SunImp sunLight = new org.lgna.story.implementation.SunImp( null );
	private org.lgna.croquet.components.Component<?> adapter;

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			animator.update();
		}
	};

	public Viewer() {
		this.camera.setVehicle( this.scene );
		this.sunLight.setVehicle( this.scene );
		this.sunLight.applyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3.accessNegativeXAxis(), 0.25 );
	}

	private boolean isInitialized = false;

	private class LookingGlassAdapter extends org.lgna.croquet.components.Component<java.awt.Component> {
		@Override
		protected java.awt.Component createAwtComponent() {
			return Viewer.this.onscreenLookingGlass.getAWTComponent();
		}
	}

	protected void initialize() {
		this.onscreenLookingGlass.addCamera( this.camera.getSgCamera() );
		this.adapter = new LookingGlassAdapter();
	}

	protected edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}

	protected org.lgna.story.implementation.SceneImp getScene() {
		return this.scene;
	}

	protected org.lgna.story.implementation.SymmetricPerspectiveCameraImp getCamera() {
		return this.camera;
	}

	protected org.lgna.story.implementation.SunImp getSunLight() {
		return this.sunLight;
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		if( this.isInitialized ) {
			//pass
		} else {
			this.initialize();
			this.isInitialized = true;
		}
		this.addCenterComponent( this.adapter );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().addAutomaticDisplayListener( this.automaticDisplayListener );
	}

	@Override
	protected void handleUndisplayable() {
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().decrementAutomaticDisplayCount();
		this.removeComponent( this.adapter );
		super.handleUndisplayable();
	}

	protected edu.cmu.cs.dennisc.animation.Animator getAnimator() {
		return this.animator;
	}
}
