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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.animation.TraditionalStyle;
import edu.cmu.cs.dennisc.animation.interpolation.FloatAnimation;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.matt.eventscript.EventScript;
import edu.cmu.cs.dennisc.pattern.VisitUtilities;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AmbientLight;
import edu.cmu.cs.dennisc.scenegraph.Background;
import edu.cmu.cs.dennisc.scenegraph.DirectionalLight;
import edu.cmu.cs.dennisc.scenegraph.ExponentialFog;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;
import org.lgna.story.Color;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SScene;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.implementation.eventhandling.EventManager;

/**
 * @author Dennis Cosgrove
 */
public class SceneImp extends EntityImp {
	private static final Transformable createDirectionalLightTransformable( DirectionalLight sgDirectionalLight, Angle yaw, Angle pitch, float brightness ) {
		Transformable rv = new Transformable();
		rv.applyRotationAboutYAxis( yaw );
		rv.applyRotationAboutXAxis( pitch );
		sgDirectionalLight.brightness.setValue( brightness );
		sgDirectionalLight.setParent( rv );
		return rv;
	}

	public SceneImp( SScene abstraction ) {
		this.abstraction = abstraction;
		this.sgBackground.color.setValue( new Color4f( 0.5f, 0.5f, 1.0f, 1.0f ) );
		this.sgFog.color.setValue( this.sgBackground.color.getValue() );
		this.sgScene.background.setValue( this.sgBackground );
		this.sgAmbientLight.brightness.setValue( 0.3f );
		this.sgScene.addComponent( this.sgAmbientLight );
		this.putInstance( this.sgScene );

		final Angle fromAbovePitch = new AngleInDegrees( -45.0 );
		final float fromAboveBrightness = 0.533f;
		this.addToScene( createDirectionalLightTransformable( this.sgFromAboveDirectionalLightA, new AngleInDegrees( 0 ), fromAbovePitch, fromAboveBrightness ) );
		this.addToScene( createDirectionalLightTransformable( this.sgFromAboveDirectionalLightB, new AngleInDegrees( 120 ), fromAbovePitch, fromAboveBrightness ) );
		this.addToScene( createDirectionalLightTransformable( this.sgFromAboveDirectionalLightC, new AngleInDegrees( 240 ), fromAbovePitch, fromAboveBrightness ) );

		final Angle fromBelowPitch = new AngleInDegrees( 90.0 );
		final float fromBelowBrightness = 1.0f;
		this.addToScene( createDirectionalLightTransformable( this.sgFromBelowDirectionalLight, new AngleInDegrees( 0 ), fromBelowPitch, fromBelowBrightness ) );
		this.sgFromBelowDirectionalLight.color.setValue( Color4f.BLACK );

		this.eventManager = new EventManager( this );
		this.eventManager.initialize();
	}

	private void addToScene( Transformable sgTransformable ) {
		sgTransformable.setParent( this.sgScene );
	}

	public void addSceneActivationListener( SceneActivationListener sceneActivationListener ) {
		this.eventManager.addSceneActivationListener( sceneActivationListener );
	}

	public void removeSceneActivationListener( SceneActivationListener sceneActivationListener ) {
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
			EmployeesOnly.invokeHandleActiveChanged( this.getAbstraction(), isActive, activationCount );
		}
		program.setSimulationSpeedFactor( prevSimulationSpeedFactor );
		if( isActive ) {
			//This forces the scene to initialize itself to make sure we can properly query bounding boxes and other render dependent things
			//All this info is critical to a scene running
			AdapterFactory.getAdapterFor( this.sgScene );

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

	private static final long TIMEOUT_DURATION = 10000; //Forces the scene to start if the wait loop exceed this time

	public void activate( ProgramImp programImp ) {
		assert deactiveCount == activeCount;
		activeCount++;
		this.setProgram( programImp );
		if( this.isGlobalLightBrightnessAnimationDesired ) {
			this.setGlobalBrightness( 0.0f );
		}
		this.changeActiveStatus( program, true, activeCount );
		if( this.isGlobalLightBrightnessAnimationDesired ) {
			this.animateGlobalBrightness( 1.0f, 0.5, TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
	}

	public void deactivate( ProgramImp programImp ) {
		deactiveCount++;
		assert deactiveCount == activeCount;
		if( this.isGlobalLightBrightnessAnimationDesired ) {
			this.animateGlobalBrightness( 0.0f, 0.25, TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
		this.changeActiveStatus( programImp, false, activeCount );
		this.setProgram( null );
	}

	@Override
	public Scene getSgComposite() {
		return this.sgScene;
	}

	@Override
	public SScene getAbstraction() {
		return this.abstraction;
	}

	@Override
	public SceneImp getScene() {
		return this;
	}

	@Override
	public ProgramImp getProgram() {
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
		for( AbstractCamera sgCamera : VisitUtilities.getAll( this.sgScene, AbstractCamera.class ) ) {
			EntityImp entityImp = EntityImp.getInstance( sgCamera );
			if( entityImp instanceof CameraImp ) {
				CameraImp cameraImp = (CameraImp)entityImp;
				program.getOnscreenRenderTarget().addSgCamera( cameraImp.getSgCamera() );
			}
		}
	}

	public void removeCamerasFrom( ProgramImp program ) {
		for( AbstractCamera sgCamera : VisitUtilities.getAll( this.sgScene, AbstractCamera.class ) ) {
			EntityImp entityImp = EntityImp.getInstance( sgCamera );
			if( entityImp instanceof CameraImp ) {
				CameraImp cameraImp = (CameraImp)entityImp;
				program.getOnscreenRenderTarget().removeSgCamera( cameraImp.getSgCamera() );
			}
		}
	}

	public CameraImp findFirstCamera() {
		AbstractCamera sgCamera = VisitUtilities.getFirst( this.sgScene, AbstractCamera.class );
		return (CameraImp)EntityImp.getInstance( sgCamera );
	}

	public float getGlobalBrightness() {
		return this.sgScene.globalBrightness.getValue();
	}

	public void setGlobalBrightness( float globalBrightness ) {
		this.sgScene.globalBrightness.setValue( globalBrightness );
	}

	public void animateGlobalBrightness( float globalBrightness, double duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setGlobalBrightness( globalBrightness );
		} else {
			this.perform( new FloatAnimation( duration, style, this.sgScene.globalBrightness.getValue(), globalBrightness) {
				@Override
				protected void updateValue( Float globalBrightness ) {
					SceneImp.this.setGlobalBrightness( globalBrightness );
				}
			} );
		}
	}

	@Override
	protected CumulativeBound updateCumulativeBound( CumulativeBound rv, AffineMatrix4x4 trans ) {
		//todo
		return rv;
	}

	public EventManager getEventManager() {
		return this.eventManager;
	}

	public EventScript getTranscript() {
		return eventManager.getScript();
	}

	private ProgramImp program;
	private final SScene abstraction;
	private final EventManager eventManager;

	private int ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount = 0;

	private int activeCount;
	private int deactiveCount;

	private boolean isGlobalLightBrightnessAnimationDesired = true;

	private final Scene sgScene = new Scene();
	private final Background sgBackground = new Background();
	private final AmbientLight sgAmbientLight = new AmbientLight();
	private final DirectionalLight sgFromAboveDirectionalLightA = new DirectionalLight();
	private final DirectionalLight sgFromAboveDirectionalLightB = new DirectionalLight();
	private final DirectionalLight sgFromAboveDirectionalLightC = new DirectionalLight();
	private final DirectionalLight sgFromBelowDirectionalLight = new DirectionalLight();
	private final ExponentialFog sgFog = new ExponentialFog();

	public final ColorProperty atmosphereColor = new ColorProperty( SceneImp.this) {
		@Override
		public Color getValue() {
			return EmployeesOnly.createColor( SceneImp.this.sgBackground.color.getValue() );
		}

		@Override
		protected void handleSetValue( Color value ) {
			Color4f color = EmployeesOnly.getColor4f( value );
			SceneImp.this.sgBackground.color.setValue( color );
			SceneImp.this.sgFog.color.setValue( color );
		}
	};
	public final ColorProperty fromAboveLightColor = new ColorProperty( SceneImp.this) {
		@Override
		public Color getValue() {
			return EmployeesOnly.createColor( SceneImp.this.sgAmbientLight.color.getValue() );
		}

		@Override
		protected void handleSetValue( Color value ) {
			Color4f color = EmployeesOnly.getColor4f( value );
			SceneImp.this.sgAmbientLight.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightA.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightB.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightC.color.setValue( color );
		}
	};
	public final ColorProperty fromBelowLightColor = new ColorProperty( SceneImp.this) {
		@Override
		public Color getValue() {
			return EmployeesOnly.createColor( SceneImp.this.sgFromBelowDirectionalLight.color.getValue() );
		}

		@Override
		protected void handleSetValue( Color value ) {
			Color4f color = EmployeesOnly.getColor4f( value );
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
