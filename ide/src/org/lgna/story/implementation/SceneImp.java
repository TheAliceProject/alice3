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
	private static class Capsule {
		private final TransformableImp transformable;
		private EntityImp vehicle;
		private edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTransformation;
		public Capsule( TransformableImp transformable ) {
			this.transformable = transformable;
		}
		public void preserve() {
			this.vehicle = this.transformable.getVehicle();
			this.localTransformation = this.transformable.getSgComposite().getLocalTransformation();
		}
		public void restore() {
			this.transformable.setVehicle( this.vehicle );
			this.transformable.getSgComposite().setLocalTransformation( this.localTransformation );
		}
	}

	private final edu.cmu.cs.dennisc.scenegraph.Scene sgScene = new edu.cmu.cs.dennisc.scenegraph.Scene();
	private final edu.cmu.cs.dennisc.scenegraph.Background sgBackground = new edu.cmu.cs.dennisc.scenegraph.Background();
	private final edu.cmu.cs.dennisc.scenegraph.AmbientLight sgAmbientLight = new edu.cmu.cs.dennisc.scenegraph.AmbientLight(); 
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromAboveDirectionalLightA = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight(); 
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromAboveDirectionalLightB = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight(); 
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromAboveDirectionalLightC = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight(); 
	private final edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgFromBelowDirectionalLight = new edu.cmu.cs.dennisc.scenegraph.DirectionalLight(); 
	private final edu.cmu.cs.dennisc.scenegraph.ExponentialFog sgFog = new edu.cmu.cs.dennisc.scenegraph.ExponentialFog();

	private final java.util.List< org.lgna.story.event.SceneActivationListener > sceneActivationListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List< Capsule > capsules = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private ProgramImp program;
	private final org.lgna.story.Scene abstraction;
	private float fogDensityValue = 0;
	
	public final ColorProperty atmosphereColor = new ColorProperty( SceneImp.this ) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.ImplementationAccessor.createColor( SceneImp.this.sgBackground.color.getValue() );
		}
		@Override
		protected void handleSetValue(org.lgna.story.Color value) {
			edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.ImplementationAccessor.getColor4f( value );
			SceneImp.this.sgBackground.color.setValue( color );
			SceneImp.this.sgFog.color.setValue( color );
		}
	};
	public final ColorProperty fromAboveLightColor = new ColorProperty( SceneImp.this ) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.ImplementationAccessor.createColor( SceneImp.this.sgAmbientLight.color.getValue() );
		}
		@Override
		protected void handleSetValue(org.lgna.story.Color value) {
			edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.ImplementationAccessor.getColor4f( value );
			SceneImp.this.sgAmbientLight.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightA.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightB.color.setValue( color );
			SceneImp.this.sgFromAboveDirectionalLightC.color.setValue( color );
		}
	};
	public final ColorProperty fromBelowLightColor = new ColorProperty( SceneImp.this ) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.ImplementationAccessor.createColor( SceneImp.this.sgFromBelowDirectionalLight.color.getValue() );
		}
		@Override
		protected void handleSetValue(org.lgna.story.Color value) {
			edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.ImplementationAccessor.getColor4f( value );
			SceneImp.this.sgFromBelowDirectionalLight.color.setValue( color );
		}
	};
	public final FloatProperty globalLightBrightness = new FloatProperty( SceneImp.this ) {
		@Override
		public Float getValue() {
			return SceneImp.this.sgScene.globalBrightness.getValue();
		}
		@Override
		protected void handleSetValue( Float value ) {
			SceneImp.this.sgScene.globalBrightness.setValue( value );
		}
	};
	
	public final FloatProperty fogDensity = new FloatProperty( SceneImp.this) {
		@Override
		public Float getValue() {
			return SceneImp.this.fogDensityValue;
		}
		@Override
		protected void handleSetValue( Float value ) {
			SceneImp.this.setFogDensity(value);
		}
	};

	private static final edu.cmu.cs.dennisc.scenegraph.Transformable createDirectionalLightTransformable( edu.cmu.cs.dennisc.scenegraph.DirectionalLight sgDirectionalLight, edu.cmu.cs.dennisc.math.Angle yaw, edu.cmu.cs.dennisc.math.Angle pitch, float brightness ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable rv = new edu.cmu.cs.dennisc.scenegraph.Transformable();
		rv.applyRotationAboutYAxis( yaw );
		rv.applyRotationAboutXAxis( pitch );
		sgDirectionalLight.brightness.setValue( brightness );
		sgDirectionalLight.setParent( rv );
		return rv;
	}
	
	public SceneImp( org.lgna.story.Scene abstraction ) {
		this.abstraction = abstraction;
		this.sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1.0f, 1.0f ) );
		this.sgFog.color.setValue(this.sgBackground.color.getValue());
		this.sgScene.background.setValue( this.sgBackground );
		this.sgAmbientLight.brightness.setValue( 0.3f );
		this.sgScene.addComponent( this.sgAmbientLight );
		this.setFogDensity(0);
		this.putInstance( this.sgScene );
		
		final edu.cmu.cs.dennisc.math.Angle fromAbovePitch = new edu.cmu.cs.dennisc.math.AngleInDegrees( -60.0 );
		final float fromAboveBrightness = 0.333f;
		createDirectionalLightTransformable( this.sgFromAboveDirectionalLightA, new edu.cmu.cs.dennisc.math.AngleInDegrees( 0 ), fromAbovePitch, fromAboveBrightness ).setParent( this.sgScene );
		createDirectionalLightTransformable( this.sgFromAboveDirectionalLightB, new edu.cmu.cs.dennisc.math.AngleInDegrees( 120 ), fromAbovePitch, fromAboveBrightness ).setParent( this.sgScene );
		createDirectionalLightTransformable( this.sgFromAboveDirectionalLightC, new edu.cmu.cs.dennisc.math.AngleInDegrees( 240 ), fromAbovePitch, fromAboveBrightness ).setParent( this.sgScene );

		final edu.cmu.cs.dennisc.math.Angle fromBelowPitch = new edu.cmu.cs.dennisc.math.AngleInDegrees( 90.0 );
		final float fromBelowBrightness = 1.0f;
		createDirectionalLightTransformable( this.sgFromBelowDirectionalLight, new edu.cmu.cs.dennisc.math.AngleInDegrees( 0 ), fromBelowPitch, fromBelowBrightness ).setParent( this.sgScene );
		this.sgFromBelowDirectionalLight.color.setValue( edu.cmu.cs.dennisc.color.Color4f.BLACK );
	}
	
	public void addSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.sceneActivationListeners.add( sceneActivationListener );
	}
	public void removeSceneActivationListener( org.lgna.story.event.SceneActivationListener sceneActivationListener ) {
		this.sceneActivationListeners.remove( sceneActivationListener );
	}
	
	private int ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount = 0;
	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushPerformMinimalInitialization() {
		ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount ++;
	}
	public void ACCEPTABLE_HACK_FOR_SCENE_EDITOR_popPerformMinimalInitialization() {
		ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount --;
	}
	
	private void fireSceneActivationListeners() {
		final org.lgna.story.event.SceneActivationEvent e = new org.lgna.story.event.SceneActivationEvent();
		for( final org.lgna.story.event.SceneActivationListener sceneActivationListener : this.sceneActivationListeners ) {
			new org.lgna.common.ComponentThread( new Runnable() {
				public void run() {
					sceneActivationListener.sceneActivated( e );
				}
			}, "SceneActivation" ).start();
		}
	}
	
	private void changeActiveStatus( ProgramImp programImp, boolean isActive, int activationCount ) {
		double prevSimulationSpeedFactor = program.getSimulationSpeedFactor();
		program.setSimulationSpeedFactor( Double.POSITIVE_INFINITY );
		if( ACCEPTABLE_HACK_FOR_SCENE_EDITOR_performMinimalInitializationCount > 0 ) {
			//pass
		} else {
			org.lgna.story.EmployeesOnly.invokeHandleActiveChanged( this.getAbstraction(), isActive, activationCount );
			this.fireSceneActivationListeners();
		}
		if( isActive ) {
			this.addCamerasTo( programImp );
		} else {
			this.removeCamerasFrom( programImp );
		}
		program.setSimulationSpeedFactor( prevSimulationSpeedFactor );
	}

	private int activeCount;
	private int deactiveCount;

	public void activate( ProgramImp programImp ) {
		assert deactiveCount == activeCount;
		activeCount++;
		this.setProgram( programImp );
		this.setGlobalBrightness( 0.0f );
		this.changeActiveStatus( program, true, activeCount );
		this.animateGlobalBrightness( 1.0f, 0.5, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
	}
	public void deactivate( ProgramImp programImp ) {
		deactiveCount++;
		assert deactiveCount == activeCount;
		this.animateGlobalBrightness( 0.0f, 0.25, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
		this.changeActiveStatus( programImp, false, activeCount );
		this.setProgram( null );
	}

	
	private void setFogDensity(float densityValue) {
		this.fogDensityValue = densityValue;
		if (densityValue == 0 && this.sgFog.getParent() == this.sgScene) {
			this.sgScene.removeComponent(this.sgFog);
		}
		else if (densityValue > 0 && this.sgFog.getParent() != this.sgScene) {
			this.sgScene.addComponent(this.sgFog);
		}
		this.sgFog.density.setValue((double)(densityValue*densityValue*densityValue));
	}
	
	@Override
	public edu.cmu.cs.dennisc.scenegraph.Scene getSgComposite() {
		return this.sgScene;
	}
	@Override
	public org.lgna.story.Scene getAbstraction() {
		return this.abstraction;
	}

	@Override
	public SceneImp getScene() {
		return this;
	}
	@Override
	protected org.lgna.story.implementation.ProgramImp getProgram() {
		return this.program;
	}
	public void setProgram( ProgramImp program ) {
		this.program = program;
	}

	public void preserveStateAndEventListeners() {
//		for( Entity entity : this.entities ) {
//			this.pointOfViewMap.put( entity, null );
//		}
	}
	public void restoreStateAndEventListeners() {
//		for( Entity entity : this.entities ) {
//			this.pointOfViewMap.put( entity, null );
//		}
	}
	public void addCamerasTo( ProgramImp program ) {
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : this.sgScene.getComponents() ) {
			EntityImp entityImplementation = EntityImp.getInstance( sgComponent );
			if( entityImplementation instanceof CameraImp ) {
				CameraImp cameraImplementation = (CameraImp)entityImplementation;
				program.getOnscreenLookingGlass().addCamera( cameraImplementation.getSgCamera() );
			}
		}
	}
	public void removeCamerasFrom( ProgramImp program ) {
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : this.sgScene.getComponents() ) {
			EntityImp entityImplementation = EntityImp.getInstance( sgComponent );
			if( entityImplementation instanceof CameraImp ) {
				CameraImp cameraImplementation = (CameraImp)entityImplementation;
				program.getOnscreenLookingGlass().removeCamera( cameraImplementation.getSgCamera() );
			}
		}
	}
	
	public CameraImp findFirstCamera() {
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : this.sgScene.getComponents() ) {
			EntityImp entityImplementation = EntityImp.getInstance( sgComponent );
			if( entityImplementation instanceof CameraImp ) {
				return (CameraImp)entityImplementation;
			}
		}
		return null;
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
			this.perform( new edu.cmu.cs.dennisc.animation.interpolation.FloatAnimation( duration, style, this.sgScene.globalBrightness.getValue(), globalBrightness ) {
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
}
