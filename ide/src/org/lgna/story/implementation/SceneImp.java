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

import org.lgna.project.ProgramClosedException;
import org.lgna.story.Model;
import org.lgna.story.event.MouseButtonListener;

import edu.cmu.cs.dennisc.java.util.concurrent.Collections;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.matt.AbstractListener;
import edu.cmu.cs.dennisc.matt.EventManager;

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
		
	private final java.util.List< org.lgna.story.event.MouseButtonListener > mouseButtonListeners = Collections.newCopyOnWriteArrayList();
	private EventManager eventManager = new EventManager();
	private final edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter mouseAdapter = new edu.cmu.cs.dennisc.java.awt.event.LenientMouseClickAdapter() {
		@Override
		protected void mouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
			SceneImp.this.handleMouseQuoteClickedUnquote( e, quoteClickCountUnquote );
		}
	};

	
	public void addMouseButtonListener(MouseButtonListener mouseButtonListener){
		this.mouseButtonListeners.add( mouseButtonListener);
	}
	
	public void removeMouseButtonListener(MouseButtonListener mouseButtonListener){
		this.mouseButtonListeners.remove( mouseButtonListener );
	}
	
	private boolean isMouseButtonListenerInExistence() {
//		if( this.mouseButtonListeners.size() > 0 ) {
//			return true;
//		} else {
//			for( Transformable component : this.getComponents() ) {
//				if( component instanceof Model ) {
//					Model model = (Model)component;
//					if( model.getMouseButtonListeners().size() > 0 ) {
//						return true;
//					}
//				}
//			}
//			return false;
//		}
		return true;
	}
	private void handleMouseQuoteClickedUnquote( java.awt.event.MouseEvent e, int quoteClickCountUnquote ) {
		if( this.isMouseButtonListenerInExistence() ) {
			final org.lgna.story.event.MouseButtonEvent mbe = new org.lgna.story.event.MouseButtonEvent( e, this.getAbstraction() );
			Model model = mbe.getModelAtMouseLocation();
			//todo
			if( model != null ) {
				for( final org.lgna.story.event.MouseButtonListener mouseButtonListener : this.mouseButtonListeners ) {
					Logger.todo( "use parent tracking thread" );
					new Thread() {
						@Override
						public void run() {
							ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
								public void run() {
									mouseButtonListener.mouseButtonClicked( mbe );
								}
							} );
						}
					}.start();
				}
//				for( final org.alice.apis.moveandturn.event.MouseButtonListener mouseButtonListener : model.getMouseButtonListeners() ) {
//					new Thread() {
//						@Override
//						public void run() {
//							edu.cmu.cs.dennisc.alice.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
//								public void run() {
//									mouseButtonListener.mouseButtonClicked( mbe );
//								}
//							} );
//						}
//					}.start();
//				}
			}
		}
	}
//	private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
//		public void keyPressed(java.awt.event.KeyEvent e) {
//			SceneImp.this.handleKeyPressed( e );
//		}
//		public void keyReleased(java.awt.event.KeyEvent e) {
//		}
//		public void keyTyped(java.awt.event.KeyEvent e) {
//		}
//	};

	private final edu.cmu.cs.dennisc.scenegraph.Scene sgScene = new edu.cmu.cs.dennisc.scenegraph.Scene();
	private final edu.cmu.cs.dennisc.scenegraph.Background sgBackground = new edu.cmu.cs.dennisc.scenegraph.Background();
	private final edu.cmu.cs.dennisc.scenegraph.AmbientLight sgAmbientLight = new edu.cmu.cs.dennisc.scenegraph.AmbientLight(); 
	private final edu.cmu.cs.dennisc.scenegraph.ExponentialFog sgFog = new edu.cmu.cs.dennisc.scenegraph.ExponentialFog();

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
			SceneImp.this.sgBackground.color.setValue( org.lgna.story.ImplementationAccessor.getColor4f( value ) );
			SceneImp.this.sgFog.color.setValue(SceneImp.this.sgBackground.color.getValue());
		}
	};
	public final ColorProperty ambientLightColor = new ColorProperty( SceneImp.this ) {
		@Override
		public org.lgna.story.Color getValue() {
			return org.lgna.story.ImplementationAccessor.createColor( SceneImp.this.sgAmbientLight.color.getValue() );
		}
		@Override
		protected void handleSetValue(org.lgna.story.Color value) {
			SceneImp.this.sgAmbientLight.color.setValue( org.lgna.story.ImplementationAccessor.getColor4f( value ) );
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
	public SceneImp( org.lgna.story.Scene abstraction ) {
		this.abstraction = abstraction;
		this.sgBackground.color.setValue( new edu.cmu.cs.dennisc.color.Color4f( 0.5f, 0.5f, 1.0f, 1.0f ) );
		this.sgFog.color.setValue(this.sgBackground.color.getValue());
		this.sgScene.background.setValue( this.sgBackground );
		this.sgAmbientLight.brightness.setValue( 0.3f );
		this.sgScene.addComponent( this.sgAmbientLight );
		this.setFogDensity(0);
		this.putInstance( this.sgScene );
		addMouseButtonListener(eventManager);
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
	public org.lgna.story.implementation.ProgramImp getProgram() {
		return this.program;
	}
	public void setProgram( ProgramImp program ) {
		if( this.program != program ) {
			if( program != null ) {
				edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = program.getOnscreenLookingGlass();
				java.awt.Component component = lg.getAWTComponent();
				component.removeMouseListener( this.mouseAdapter );
				component.removeMouseMotionListener( this.mouseAdapter );
//				component.removeKeyListener( this.keyAdapter );
			}
			//handleOwnerChange( null );
			this.program = program;
//			handleOwnerChange( program );
			if( program != null ) {
				edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = program.getOnscreenLookingGlass();
				java.awt.Component component = lg.getAWTComponent();
				component.addMouseListener( this.mouseAdapter );
				component.addMouseMotionListener( this.mouseAdapter );
//				component.addKeyListener( this.keyAdapter );
			}
		}
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

	public void addListener(AbstractListener event) {
		eventManager.addListener(event);
	}

	public void silenceAllListeners() {
		eventManager.silenceAllListeners();
	}

	public void restoreAllListeners() {
		eventManager.restoreAllListeners();
	}
}
