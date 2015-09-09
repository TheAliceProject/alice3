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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public class SceneImp extends EntityImp {
	private static final edu.cmu.cs.dennisc.scenegraph.Transformable createDirectionalLightTransformable( edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgDirectionalLight, edu.cmu.cs.dennisc.math.Angle yaw, edu.cmu.cs.dennisc.math.Angle pitch, float brightness ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable rv = new edu.cmu.cs.dennisc.scenegraph.Transformable();
		rv.applyRotationAboutYAxis( yaw );
		rv.applyRotationAboutXAxis( pitch );
		sgDirectionalLight.brightness.setValue( brightness );
		sgDirectionalLight.setParent( rv );
		return rv;
	}

	public SceneImp( org.lgna.story.SScene abstraction ) {
		this.abstraction = abstraction;
		this.sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1.0f, 1.0f ) );
		this.sgFog.color.setValue( this.sgBackground.color.getValue() );
		this.sgScene.background.setValue( this.sgBackground );
		this.sgAmbientLight.brightness.setValue( 0.3f );
		this.sgScene.addComponent( this.sgAmbientLight );
		this.putInstance( this.sgScene );

		final edu.cmu.cs.dennisc.math.Angle fromAbovePitch = new edu.cmu.cs.dennisc.math.AngleInDegrees( -45.0 );
		final float fromAboveBrightness = 0.533f;
		this.addToScene( createDirectionalLightTransformable( this.sgFromAboveDirectionalLightA, new edu.cmu.cs.dennisc.math.AngleInDegrees( 0 ), fromAbovePitch, fromAboveBrightness ) );
		this.addToScene( createDirectionalLightTransformable( this.sgFromAboveDirectionalLightB, new edu.cmu.cs.dennisc.math.AngleInDegrees( 120 ), fromAbovePitch, fromAboveBrightness ) );
		this.addToScene( createDirectionalLightTransformable( this.sgFromAboveDirectionalLightC, new edu.cmu.cs.dennisc.math.AngleInDegrees( 240 ), fromAbovePitch, fromAboveBrightness ) );

		final edu.cmu.cs.dennisc.math.Angle fromBelowPitch = new edu.cmu.cs.dennisc.math.AngleInDegrees( 90.0 );
		final float fromBelowBrightness = 1.0f;
		this.addToScene( createDirectionalLightTransformable( this.sgFromBelowDirectionalLight, new edu.cmu.cs.dennisc.math.AngleInDegrees( 0 ), fromBelowPitch, fromBelowBrightness ) );
		this.sgFromBelowDirectionalLight.color.setValue( edu.cmu.cs.dennisc.color.Color4f.BLACK );

		this.eventManager = new org.lgna.story.implementation.eventhandling.EventManager( this );
		this.eventManager.initialize();
	}

	private void addToScene( edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable ) {
		sgTransformable.setParent( this.sgScene );
	}

	public void addSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.eventManager.addSceneActivationListener( sceneActivationListener );
	}

	public void removeSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.eventManager.removeSceneActivationListener( sceneActivationListener );
	}

	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushPerformMinimalInitialization() {
		ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount++;
	}

	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_popPerformMinimalInitialization() {
		ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount--;
	}

	private void fireSceneActivationListeners() {
		this.eventManager.sceneActivated();
	}

	public boolean isGlobalLightBrightnessAnimationDesired() {
		return this.isGlobalLightBrightnessAnimationDesired;
	}

	public void setGlobalLightBrightnessAnimationDesired( boolean isGlobalLightBrightnessAnimationDesired ) {
		this.isGlobalLightBrightnessAnimationDesired = isGlobalLightBrightnessAnimationDesired;
	}

	private void changeActiveStatus( ProgramImp programImp, boolean isActive, int activationCount ) {
		double prevSimulationSpeedFactor = program.getSimulationSpeedFactor();
		program.setSimulationSpeedFactor( Double.POSITIVE_INFINITY );
		if( ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount > 0 ) {
			//pass
		} else {
			org.lgna.story.EmployeesOnly.invokeHandleActiveChanged( this.getAbstraction(), isActive, activationCount );
		}
		program.setSimulationSpeedFactor( prevSimulationSpeedFactor );
		if( isActive ) {
			this.addCamerasTo( programImp );
			if( ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount > 0 ) {
				//pass
			} else {
				this.fireSceneActivationListeners();
			}
		} else {
			this.removeCamerasFrom( programImp );
		}
	}

	private final static long TIMEOUT_DURATION = 10000; //Forces the scene to start if the wait loop exceed this time

	public void activate( ProgramImp programImp ) {
		assert deactiveCount == activeCount;
		activeCount++;
		this.setProgram( programImp );
		if( this.isGlobalLightBrightnessAnimationDesired ) {
			this.setGlobalBrightness( 0.0f );
		}

		//IMPORTANT: This waits until the all the models and whatnot are loaded and transformed in the scene
		//Since all of this happens when the frame is displayed, it happens on a separate thread
		//If we move ahead and call "changeActiveStatus", then that triggers the sceneActivationListeners and causes the world to run
		//If that happens before things are ready, then some calls (particularly to things that require bounding boxes) may not be valid
		edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene sceneAdapter = edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.getAdapterFor( this.sgScene );
		long startTime = System.currentTimeMillis();
		while( !sceneAdapter.isInitialized() ) {
			if( ( System.currentTimeMillis() - startTime ) > TIMEOUT_DURATION ) {
				System.err.println( "Timeout waiting for scene to load (wait time exceeded " + ( TIMEOUT_DURATION * .001 ) + " seconds). Starting scene." );
				break;
			}
			try {
				Thread.sleep( 10 );
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}
		}

		this.changeActiveStatus( program, true, activeCount );
		if( this.isGlobalLightBrightnessAnimationDesired ) {
			this.animateGlobalBrightness( 1.0f, 0.5, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
	}

	public void deactivate( ProgramImp programImp ) {
		deactiveCount++;
		assert deactiveCount == activeCount;
		if( this.isGlobalLightBrightnessAnimationDesired ) {
			this.animateGlobalBrightness( 0.0f, 0.25, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
		this.changeActiveStatus( programImp, false, activeCount );
		this.setProgram( null );
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.Scene getSgComposite() {
		return this.sgScene;
	}

	@Override
	public org.lgna.story.SScene getAbstraction() {
		return this.abstraction;
	}

	@Override
	public SceneImp getScene() {
		return this;
	}

	@Override
	public org.lgna.story.implementation.ProgramImp getProgram() {
		return this.program;
	}

	public void setProgram( ProgramImp program ) {
		if( this.program != program ) {
			if( program != null ) {
				this.eventManager.removeListenersFrom( program.getOnscreenRenderTarget() );
			}
			//handleOwnerChange( null );
			this.program = program;
			//			handleOwnerChange( program );
			if( program != null ) {
				this.eventManager.addListenersTo( program.getOnscreenRenderTarget() );
			}
		}
		this.program = program;
	}

	//todo
	//	private static class Capsule {
	//		private final TransformableImp transformable;
	//		private EntityImp vehicle;
	//		private edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTransformation;
	//
	//		public Capsule( TransformableImp transformable ) {
	//			this.transformable = transformable;
	//		}
	//
	//		public void preserve() {
	//			this.vehicle = this.transformable.getVehicle();
	//			this.localTransformation = this.transformable.getSgComposite().getLocalTransformation();
	//		}
	//
	//		public void restore() {
	//			this.transformable.setVehicle( this.vehicle );
	//			this.transformable.getSgComposite().setLocalTransformation( this.localTransformation );
	//		}
	//	}
	//	private final java.util.List<Capsule> capsules = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	public void preserveStateAndEventListeners() {
		this.eventManager.silenceAllListeners();
		//todo: preserve state
	}

	public void restoreStateAndEventListeners() {
		//todo: restore state
		this.eventManager.restoreAllListeners();
	}

	public void addCamerasTo( ProgramImp program ) {
		for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( this.sgScene, edu.cmu.cs.dennisc.scenegraph.AbstractCamera.class ) ) {
			EntityImp entityImp = EntityImp.getInstance( sgCamera );
			if( entityImp instanceof CameraImp ) {
				CameraImp cameraImp = (CameraImp)entityImp;
				program.getOnscreenRenderTarget().addSgCamera( cameraImp.getSgCamera() );
			}
		}
	}

	public void removeCamerasFrom( ProgramImp program ) {
		for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( this.sgScene, edu.cmu.cs.dennisc.scenegraph.AbstractCamera.class ) ) {
			EntityImp entityImp = EntityImp.getInstance( sgCamera );
			if( entityImp instanceof CameraImp ) {
				CameraImp cameraImp = (CameraImp)entityImp;
				program.getOnscreenRenderTarget().removeSgCamera( cameraImp.getSgCamera() );
			}
		}
	}

	public CameraImp findFirstCamera() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = edu.cmu.cs.dennisc.pattern.VisitUtilities.getFirst( this.sgScene, edu.cmu.cs.dennisc.scenegraph.AbstractCamera.class );
		return (CameraImp)EntityImp.getInstance( sgCamera );
	}

	public float getGlobalBrightness() {
		return this.sgScene.globalBrightness.getValue();
	}

	public void setGlobalBrightness( float globalBrightness ) {
		this.sgScene.globalBrightness.setValue( globalBrightness );
	}

	public void animateGlobalBrightness( float globalBrightness, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setGlobalBrightness( globalBrightness );
		} else {
			this.perform( new edu.cmu.cs.dennisc.animation.interpolation.FloatAnimation( duration, style, this.sgScene.globalBrightness.getValue(), globalBrightness) {
				@Override
				protected void updateValue( Float globalBrightness ) {
					SceneImp.this.setGlobalBrightness( globalBrightness );
				}
			} );
		}
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		//todo
		return rv;
	}

	public org.lgna.story.implementation.eventhandling.EventManager getEventManager() {
		return this.eventManager;
	}

	public edu.cmu.cs.dennisc.matt.eventscript.EventScript getTranscript() {
		return eventManager.getScript();
	}

	private ProgramImp program;
	private final org.lgna.story.SScene abstraction;
	private final org.lgna.story.implementation.eventhandling.EventManager eventManager;

	private int ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount = 0;

	private int activeCount;
	private int deactiveCount;

	private boolean isGlobalLightBrightnessAnimationDesired = true;

	private final edu.cmu.cs.dennisc.scenegraph.Scene sgScene = new edu.cmu.cs.dennisc.scenegraph.Scene();
	private final edu.cmu.cs.dennisc.scenegraph.Background sgBackground = new edu.cmu.cs.dennisc.scenegraph.Background();
	private final edu.cmu.cs.dennisc.scenegraph.AmbientLight sgAmbientLight = new edu.cmu.cs.dennisc.scenegraph.AmbientLight();
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromAboveDirectionalLightA = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight();
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromAboveDirectionalLightB = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight();
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromAboveDirectionalLightC = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight();
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromBelowDirectionalLight = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight();
	private final edu.cmu.cs.dennisc.scenegraph.ExponentialFog sgFog = new edu.cmu.cs.dennisc.scenegraph.ExponentialFog();

	public final ColorProperty atmosphereColor = new ColorProperty( SceneImp.this) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.EmployeesOnly.createColor( SceneImp.this.sgBackground.color.getValue() );
		}

		@Override
		protected void handleSetValue( org.lgna.story.Color value ) {
			edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.EmployeesOnly.getColor4f( value );
			SceneImp.this.sgBackground.color.setValue( color );
			SceneImp.this.sgFog.color.setValue( color );
		}
	};
	public final ColorProperty fromAboveLightColor = new ColorProperty( SceneImp.this) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.EmployeesOnly.createColor( SceneImp.this.sgAmbientLight.color.getValue() );
		}

		@Override
		protected void handleSetValue( org.lgna.story.Color value ) {
			edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.EmployeesOnly.getColor4f( value );
			SceneImp.this.sgAmbientLight.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightA.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightB.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightC.color.setValue( color );
		}
	};
	public final ColorProperty fromBelowLightColor = new ColorProperty( SceneImp.this) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.EmployeesOnly.createColor( SceneImp.this.sgFromBelowDirectionalLight.color.getValue() );
		}

		@Override
		protected void handleSetValue( org.lgna.story.Color value ) {
			edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.EmployeesOnly.getColor4f( value );
			SceneImp.this.sgFromBelowDirectionalLight.color.setValue( color );
		}
	};
	public final FloatProperty globalLightBrightness = new FloatProperty( SceneImp.this) {
		@Override
		public Float getValue() {
			return SceneImp.this.sgScene.globalBrightness.getValue();
		}

		@Override
		protected void handleSetValue( Float value ) {
			SceneImp.this.sgScene.globalBrightness.setValue( value );
		}
	};

	private static class FogDensityProperty extends FloatProperty {
		public FogDensityProperty( SceneImp owner ) {
			super( owner );
		}

		@Override
		public SceneImp getOwner() {
			return (SceneImp)super.getOwner();
		}

		@Override
		public Float getValue() {
			SceneImp sceneImp = this.getOwner();
			if( sceneImp.sgFog.getParent() != null ) {
				double v = sceneImp.sgFog.density.getValue();
				return (float)Math.pow( v, 1 / 3.0 );
			} else {
				return 0.0f;
			}
		}

		@Override
		protected void handleSetValue( Float densityValue ) {
			SceneImp sceneImp = this.getOwner();
			if( ( densityValue == 0 ) && ( sceneImp.sgFog.getParent() == sceneImp.sgScene ) ) {
				sceneImp.sgScene.removeComponent( sceneImp.sgFog );
			} else if( ( densityValue > 0 ) && ( sceneImp.sgFog.getParent() != sceneImp.sgScene ) ) {
				sceneImp.sgScene.addComponent( sceneImp.sgFog );
			}
			sceneImp.sgFog.density.setValue( (double)( densityValue * densityValue * densityValue ) );
		}
	}

	public final FloatProperty fogDensity = new FogDensityProperty( SceneImp.this );
}
