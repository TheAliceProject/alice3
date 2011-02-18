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

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public class SceneImplementation extends EntityImplementation {
	private static class Capsule {
		private final TransformableImplementation transformable;
		private EntityImplementation vehicle;
		private edu.cmu.cs.dennisc.math.AffineMatrix4x4 localTransformation;
		public Capsule( TransformableImplementation transformable ) {
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

	private final java.util.List< Capsule > capsules = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private ProgramImplementation program;
	private final org.lookingglassandalice.storytelling.Scene abstraction;
	
	public SceneImplementation( org.lookingglassandalice.storytelling.Scene abstraction ) {
		this.abstraction = abstraction;
		this.sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1.0f, 1.0f ) );
		this.sgScene.background.setValue( this.sgBackground );
		this.sgAmbientLight.brightness.setValue( 0.3f );
		this.sgScene.addComponent( this.sgAmbientLight );
		this.putInstance( this.sgScene );
	}
	@Override
	public edu.cmu.cs.dennisc.scenegraph.Scene getSgComposite() {
		return this.sgScene;
	}
	@Override
	public org.lookingglassandalice.storytelling.Scene getAbstraction() {
		return this.abstraction;
	}

	@Override
	protected SceneImplementation getScene() {
		return this;
	}
	@Override
	protected org.lookingglassandalice.storytelling.implementation.ProgramImplementation getProgram() {
		return this.program;
	}
	public void setProgram( ProgramImplementation program ) {
		this.program = program;
	}

	public void preserveVehiclesAndVantagePoints() {
//		for( Entity entity : this.entities ) {
//			this.pointOfViewMap.put( entity, null );
//		}
	}
	public void restoreVehiclesAndVantagePoints() {
//		for( Entity entity : this.entities ) {
//			this.pointOfViewMap.put( entity, null );
//		}
	}
	public void addCamerasTo( ProgramImplementation program ) {
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : this.sgScene.getComponents() ) {
			EntityImplementation entityImplementation = EntityImplementation.getInstance( sgComponent );
			if( entityImplementation instanceof CameraImplementation ) {
				CameraImplementation cameraImplementation = (CameraImplementation)entityImplementation;
				program.getOnscreenLookingGlass().addCamera( cameraImplementation.getSgCamera() );
			}
		}
	}
	public void removeCamerasFrom( ProgramImplementation program ) {
		for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : this.sgScene.getComponents() ) {
			EntityImplementation entityImplementation = EntityImplementation.getInstance( sgComponent );
			if( entityImplementation instanceof CameraImplementation ) {
				CameraImplementation cameraImplementation = (CameraImplementation)entityImplementation;
				program.getOnscreenLookingGlass().removeCamera( cameraImplementation.getSgCamera() );
			}
		}
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
					SceneImplementation.this.setGlobalBrightness( globalBrightness );
				}
			} );
		}
		
	}
}
